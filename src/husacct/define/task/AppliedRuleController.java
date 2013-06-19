package husacct.define.task;

import husacct.ServiceProvider;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.CategoryDTO;
import husacct.common.dto.RuleTypeDTO;
import husacct.common.dto.ViolationTypeDTO;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.appliedrule.AppliedRuleFactory;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleFactory;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.module.modules.Layer;
import husacct.define.domain.services.AppliedRuleDomainService;
import husacct.define.domain.services.AppliedRuleExceptionDomainService;
import husacct.define.domain.services.DefaultRuleDomainService;
import husacct.define.domain.services.ModuleDomainService;
import husacct.define.domain.services.stateservice.StateService;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition.Type;
import husacct.define.presentation.jdialog.AppliedRuleJDialog;
import husacct.define.presentation.utils.DataHelper;
import husacct.define.presentation.utils.KeyValueComboBox;
import husacct.define.task.components.AbstractCombinedComponent;
import husacct.define.task.components.AbstractDefineComponent;
import husacct.define.task.components.AnalyzedModuleComponent;
import husacct.define.task.components.DefineComponentFactory;
import husacct.define.task.components.SoftwareArchitectureComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observer;

public class AppliedRuleController extends PopUpController {

	private AppliedRuleExceptionDomainService appliedRuleExceptionService;
	private AppliedRuleDomainService appliedRuleService;
	private AppliedRuleFactory ruleFactory = new AppliedRuleFactory();
	private long currentAppliedRuleId;
	private ArrayList<HashMap<String, Object>> exceptionRules = new ArrayList<HashMap<String, Object>>();

	private AppliedRuleJDialog jframeAppliedRule;
	private ModuleDomainService moduleService;
	private Long moduleToId;

	private String selectedRuleTypeKey;

	public AppliedRuleController(long moduleId, long appliedRuleId) {
		super();
		setModuleId(moduleId);
		currentAppliedRuleId = appliedRuleId;
		determineAction();

		moduleService = new ModuleDomainService();
		appliedRuleService = new AppliedRuleDomainService();
		appliedRuleExceptionService = new AppliedRuleExceptionDomainService();

		if (getAction().equals(PopUpController.ACTION_EDIT)) {
			loadAllRuleExceptions();
		}
	}

	private void addChildComponents(AnalyzedModuleComponent parentComponent,
			AnalysedModuleDTO module) {
		AnalyzedModuleComponent childComponent = new AnalyzedModuleComponent(
				module.uniqueName, module.name, module.type, module.visibility);
		AnalysedModuleDTO[] children = ServiceProvider.getInstance()
				.getAnalyseService().getChildModulesInModule(module.uniqueName);
		for (AnalysedModuleDTO subModule : children) {
			addChildComponents(childComponent, subModule);
		}
		parentComponent.addChild(childComponent);
	}

	private void addDefineModuleChildComponents(
			AbstractCombinedComponent parentComponent, ModuleStrategy module) {
		AbstractDefineComponent childComponent = DefineComponentFactory
				.getDefineComponent(module);
		for (ModuleStrategy subModule : module.getSubModules()) {
			addDefineModuleChildComponents(childComponent, subModule);
		}

		ArrayList<SoftwareUnitDefinition> softwareUnits = module.getUnits();
		for (SoftwareUnitDefinition softwareUnit : softwareUnits) {
			AnalyzedModuleComponent analysedComponent = new AnalyzedModuleComponent(
					softwareUnit.getName(), softwareUnit.getName(),
					softwareUnit.getType().toString(), "public");

			AnalysedModuleDTO[] children = ServiceProvider.getInstance()
					.getAnalyseService()
					.getChildModulesInModule(softwareUnit.getName());
			for (AnalysedModuleDTO subModule : children) {
				addChildComponents(analysedComponent, subModule);
			}

			childComponent.addChild(analysedComponent);
		}

		parentComponent.addChild(childComponent);
	}

	public void addException(HashMap<String, Object> exceptionRule) {
		exceptionRules.add(exceptionRule);
	}

	private ModuleStrategy assignToCorrectModule(Object o) {
		ModuleStrategy module;
		if (o instanceof SoftwareUnitDefinition) {
			module = getModuleWhereSoftwareUnitNeedsToBeMapped(
					(SoftwareUnitDefinition) o, (SoftwareUnitDefinition) o);
		} else if (o instanceof Long) {
			long moduleId = (Long) o;

			if (moduleId != -1) {
				module = SoftwareArchitecture.getInstance().getModuleById(
						moduleId);
			} else {

				module = new ModuleFactory().createDummy("blank");
				module.setId(-1);
			}
		} else {
			module = new ModuleFactory().createDummy("blank");
		}
		return module;
	}

	public void clearRuleExceptions() {
		exceptionRules.clear();
	}

	public boolean conformRuleConventions() {
		return conformRuleConventions(appliedRuleService
				.getAppliedRuleById(currentAppliedRuleId));
	}

	public boolean conformRuleConventions(HashMap<String, Object> ruleDetails) {
		AppliedRuleStrategy dummyRule = ruleFactory
				.createRuleWithModules(ruleDetails);
		return conformRuleConventions(dummyRule);
	}

	private boolean conformRuleConventions(AppliedRuleStrategy appliedRule) {
		return appliedRule.checkConvention();
	}

	private ModuleStrategy createOrAssignModule(ModuleStrategy module,
			SoftwareUnitDefinition su) {
		ModuleStrategy moduleToReturn;
		ArrayList<SoftwareUnitDefinition> softwareUnits = module.getUnits();

		String firstSUName = softwareUnits.get(0).getName();
		if (module.getUnits().size() == 1 && firstSUName.equals(su.getName())) {
			moduleToReturn = module;
		} else {
			ModuleStrategy subModule = new ModuleFactory()
					.createModule("SubSystem");
			subModule.set(su.getName(), "");
			subModule.addSUDefinition(su);
			moduleService.addModuleToParent(module.getId(), subModule);
			moduleToReturn = subModule;
		}
		return moduleToReturn;
	}

	private void determineAction() {
		if (currentAppliedRuleId == -1L) {
			setAction(PopUpController.ACTION_NEW);
		} else {
			setAction(PopUpController.ACTION_EDIT);
		}
	}

	/**
	 * Load Data
	 */
	public void fillRuleTypeComboBox(
			KeyValueComboBox keyValueComboBoxAppliedRule) {
		fillRuleTypeComboBox(keyValueComboBoxAppliedRule, false);
	}

	public void fillRuleTypeComboBox(
			KeyValueComboBox keyValueComboBoxAppliedRule, boolean update) {
		ModuleStrategy selectedModule = this.moduleService
				.getModuleById(DefinitionController.getInstance()
						.getSelectedModuleId());

		String currentRuleType = "";
		if (currentAppliedRuleId != -1) {
			currentRuleType = this.getAppliedRuleDetails(currentAppliedRuleId)
					.get("ruleTypeKey").toString();
		}

		int index = 0;
		ArrayList<String> ruleTypeKeys = new ArrayList<String>();
		ArrayList<String> ruleTypeValues = new ArrayList<String>();

		CategoryDTO[] rulesCategory = ServiceProvider.getInstance()
				.getValidateService().getCategories();
		RuleTypeDTO[] allowedRules = ServiceProvider.getInstance()
				.getValidateService()
				.getAllowedRuleTypesOfModule(selectedModule.getType());

		RuleTypeDTO[] defaultRules = ServiceProvider.getInstance()
				.getValidateService()
				.getDefaultRuleTypesOfModule(selectedModule.getType());

		for (CategoryDTO category : rulesCategory) {
			ArrayList<RuleTypeDTO> _temp = new ArrayList<RuleTypeDTO>();
			for (RuleTypeDTO categoryRule : category.getRuleTypes()) {
				for (RuleTypeDTO allowedRule : allowedRules) {
					if (categoryRule.getKey().equals(allowedRule.getKey())) {
						_temp.add(categoryRule);
					}
				}
			}
			if (!_temp.isEmpty()) {
				ruleTypeKeys.add("setDisabled");
				ruleTypeValues.add("--- "
						+ ServiceProvider.getInstance().getLocaleService()
								.getTranslatedString(category.getKey())
						+ " ---");

				for (RuleTypeDTO rule : _temp) {

					String value = ServiceProvider.getInstance()
							.getLocaleService().getTranslatedString(rule.key);

					if (currentRuleType.equals("")) {
						ruleTypeKeys.add(rule.getKey());
						ruleTypeValues.add(value);
					} else {
						ruleTypeKeys.add(currentRuleType);
						ruleTypeValues.add(ServiceProvider.getInstance()
								.getLocaleService().getTranslatedString(currentRuleType));
//						RuleTypeDTO[] defaultRuleForModule = ServiceProvider.getInstance()
//						.getValidateService().getDefaultRuleTypesOfModule(selectedModule.getType());
//						for(RuleTypeDTO rtd : defaultRuleForModule) {
//							if(rtd.getKey().equals(currentRuleType)) {
//								ruleTypeValues.add(rtd);
//							}
//						}
					}
				}
			}
		}

		keyValueComboBoxAppliedRule.setModel(ruleTypeKeys.toArray(),
				ruleTypeValues.toArray());
		keyValueComboBoxAppliedRule.setSelectedIndex(index);
	}

	public void fillRuleTypeComboBoxWithExceptions(
			KeyValueComboBox keyValueComboBoxAppliedRule) {
		CategoryDTO[] categories = ServiceProvider.getInstance()
				.getValidateService().getCategories();

		for (CategoryDTO categorie : categories) {
			RuleTypeDTO[] ruleTypes = categorie.ruleTypes;
			// Get currently selected RuleType
			for (RuleTypeDTO ruleTypeDTO : ruleTypes) {
				if (ruleTypeDTO.key.equals(selectedRuleTypeKey)) {
					if (ruleTypeDTO.exceptionRuleTypes.length == 0) {
						throw new RuntimeException(
								"No exception keys found for ruletype: "
										+ selectedRuleTypeKey);
					}

					// Fill combobox with exceptionruletypes of that rule
					ArrayList<String> ruleTypeKeys = new ArrayList<String>();
					ArrayList<String> ruleTypeValues = new ArrayList<String>();

					for (RuleTypeDTO ruleDTO : ruleTypeDTO.exceptionRuleTypes) {
						ruleTypeKeys.add(ruleDTO.key);
						String value = ServiceProvider.getInstance()
								.getLocaleService()
								.getTranslatedString(ruleDTO.key);
						ruleTypeValues.add(value);
					}
					keyValueComboBoxAppliedRule.setModel(
							ruleTypeKeys.toArray(), ruleTypeValues.toArray());
				}
			}
		}
	}

	/*
	 * Saving
	 */

	public HashMap<String, Object> getAppliedRuleDetails(long appliedRuleId) {
		AppliedRuleStrategy rule = appliedRuleService
				.getAppliedRuleById(appliedRuleId);
		HashMap<String, Object> ruleDetails = new HashMap<String, Object>();
		ruleDetails.put("id", rule.getId());
		ruleDetails.put("description", rule.getDescription());
		ruleDetails.put("dependencies", rule.getDependencies());
		ruleDetails.put("moduleFromName", rule.getModuleFrom().getName());
		ruleDetails.put("moduleToName", rule.getModuleTo().getName());
		ruleDetails.put("moduleFromId", rule.getModuleFrom().getId());
		ruleDetails.put("moduleToId", rule.getModuleTo().getId());
		ruleDetails.put("enabled", rule.isEnabled());
		ruleDetails.put("regex", rule.getRegex());
		ruleDetails.put("ruleTypeKey", rule.getRuleType());
		ruleDetails.put("numberofexceptions", rule.getExceptions().size());
		return ruleDetails;
	}

	public ArrayList<DataHelper> getChildModules(long parentModuleId) {
		ArrayList<Long> moduleIds = moduleService
				.getSubModuleIds(parentModuleId);
		ArrayList<DataHelper> moduleNames = new ArrayList<DataHelper>();
		for (long moduleId : moduleIds) {
			if (moduleId != getCurrentModuleId()) {
				DataHelper datahelper = new DataHelper();
				datahelper.setId(moduleId);
				datahelper.setValue(""
						+ moduleService.getModuleNameById(moduleId));
				moduleNames.add(datahelper);
			}
		}
		return moduleNames;
	}

	public long getCurrentAppliedRuleId() {
		return currentAppliedRuleId;
	}

	public Long getCurrentModuleId() {
		long currentModuleId = getModuleId();
		return currentModuleId;
	}

	public String getCurrentModuleName() {
		long currentModuleId = getModuleId();
		return moduleService.getModuleNameById(currentModuleId);
	}

	/*
	 * Getters & Setters
	 */
	public ArrayList<HashMap<String, Object>> getExceptionRules() {
		return exceptionRules;
	}

	public String getModuleName(Long moduleIdFrom) {
		return moduleService.getModuleNameById(moduleIdFrom);
	}

	public Long getModuleToId() {
		return moduleToId;
	}

	public AbstractCombinedComponent getModuleTreeComponents() {
		SoftwareArchitectureComponent rootComponent = new SoftwareArchitectureComponent();
		ArrayList<ModuleStrategy> modules = moduleService.getSortedModules();
		for (ModuleStrategy module : modules) {
			addDefineModuleChildComponents(rootComponent, module);
		}
		return rootComponent;
	}

	private ModuleStrategy getModuleWhereSoftwareUnitNeedsToBeMapped(
			SoftwareUnitDefinition currentSoftwareUnit,
			final SoftwareUnitDefinition finalSoftwareUnit) {
		ModuleStrategy returnModule;
		try {
			// Search all module for the softwareunit definition we are trying
			// to map
			ModuleStrategy module = moduleService
					.getModuleIdBySoftwareUnit(currentSoftwareUnit);
			// Current Softwareunit is now found, adding to current module or
			// sub
			returnModule = createOrAssignModule(module, finalSoftwareUnit);
		} catch (RuntimeException e) {
			// Current softwareunit definition not found
			// Go recursive and look if the parent of the softwareunit is
			// mapped.

			AnalysedModuleDTO analysedModuleDTO = ServiceProvider.getInstance()
					.getAnalyseService()
					.getParentModuleForModule(currentSoftwareUnit.getName());
			if (!analysedModuleDTO.name.equals("")) {
				Type type = Type.valueOf(analysedModuleDTO.type.toUpperCase());
				SoftwareUnitDefinition parentSU = new SoftwareUnitDefinition(
						analysedModuleDTO.uniqueName, type);

				returnModule = getModuleWhereSoftwareUnitNeedsToBeMapped(
						parentSU, finalSoftwareUnit);

			} else {// No higher parent of softwareUnit
				// Conclusion: softwareunit is not mapped at all. now at to
				// the root
				logger.info("No parent found for softwareunit : "
						+ currentSoftwareUnit.getName());
				logger.info("Adding " + currentSoftwareUnit.getName()
						+ " to a module in the root");

				ModuleStrategy subModule = new ModuleFactory()
						.createModule("SubSystem");
				subModule.set(currentSoftwareUnit.getName(), "");
				moduleService.addModuleToRoot(subModule);
				returnModule = subModule;
			}
		}
		return returnModule;
	}

	public String getSelectedRuleTypeKey() {
		return selectedRuleTypeKey;
	}

	public ArrayList<DataHelper> getSiblingModules(long moduleId) {
		ArrayList<Long> moduleIds = new ArrayList<Long>();
		for (Long modId : moduleService.getSiblingModuleIds(moduleId)) {
			moduleIds.addAll(moduleService.getSubModuleIds(modId));
		}

		ArrayList<DataHelper> moduleNames = new ArrayList<DataHelper>();
		for (long modId : moduleIds) {
			if (modId != getCurrentModuleId()) {
				DataHelper datahelper = new DataHelper();
				datahelper.setId(modId);
				datahelper
						.setValue("" + moduleService.getModuleNameById(modId));
				moduleNames.add(datahelper);
			}
		}
		return moduleNames;
	}

	public ArrayList<ViolationTypeDTO> getViolationTypesByRuleType(
			String ruleTypeKey) {
		ArrayList<ViolationTypeDTO> violationTypeDtoList = new ArrayList<ViolationTypeDTO>();

		CategoryDTO[] categories = ServiceProvider.getInstance()
				.getValidateService().getCategories();

		for (CategoryDTO categorie : categories) {
			RuleTypeDTO[] ruleTypes = categorie.ruleTypes;
			// Get currently selected RuleType
			for (RuleTypeDTO ruleTypeDTO : ruleTypes) {
				if (ruleTypeDTO.key.equals(ruleTypeKey)) {
					for (ViolationTypeDTO vt : ruleTypeDTO.violationTypes) {
						violationTypeDtoList.add(vt);
					}
				}

				// Check exceptions rules
				for (RuleTypeDTO ruleTypeExceptionDTO : ruleTypeDTO
						.getExceptionRuleTypes()) {
					if (ruleTypeExceptionDTO.key.equals(ruleTypeKey)) {
						for (ViolationTypeDTO vt : ruleTypeExceptionDTO.violationTypes) {
							violationTypeDtoList.add(vt);
						}
					}
				}

			}
		}
		return violationTypeDtoList;
	}

	public boolean hasSelectedRuleTypeHaveExceptions() {
		boolean hasException = false;
		CategoryDTO[] categories = ServiceProvider.getInstance()
				.getValidateService().getCategories();

		for (CategoryDTO categorie : categories) {
			RuleTypeDTO[] ruleTypes = categorie.ruleTypes;
			// Get currently selected RuleType
			for (RuleTypeDTO ruleTypeDTO : ruleTypes) {
				if (ruleTypeDTO.key.equals(selectedRuleTypeKey)) {
					if (ruleTypeDTO.exceptionRuleTypes.length > 0) {
						hasException = true;
					}
				}
			}
		}
		return hasException;
	}

	public boolean isAnalysed() {
		return ServiceProvider.getInstance().getAnalyseService().isAnalysed();
	}

	private void loadAllRuleExceptions() {
		ArrayList<AppliedRuleStrategy> exceptions = appliedRuleExceptionService
				.getExceptionsByAppliedRule(currentAppliedRuleId);
		for (AppliedRuleStrategy exception : exceptions) {
			HashMap<String, Object> exceptionRule = new HashMap<String, Object>();
			exceptionRule.put("id", exception.getId());
			exceptionRule.put("ruleTypeKey", exception.getRuleType());
			exceptionRule
					.put("moduleFromId", exception.getModuleFrom().getId());
			exceptionRule.put("moduleToId", exception.getModuleTo().getId());
			exceptionRule.put("dependencies", exception.getDependencies());
			exceptionRule.put("enabled", exception.isEnabled());
			exceptionRule.put("description", exception.getDescription());
			exceptionRule.put("regex", exception.getRegex());
			addException(exceptionRule);
		}
		notifyObservers();
	}

	/*
	 * Oberver
	 */
	public void notifyObservers(long currentAppliedRuleId) {
		for (Observer o : observers) {
			o.update(this, currentAppliedRuleId);
		}
	}

	public void removeException(int index) {
		if (index != -1) {
			try {
				exceptionRules.remove(index);
			} catch (Exception e) {
			}
		}
	}

	public boolean save(HashMap<String, Object> ruleDetails) {

		String ruleTypeKey = (String) ruleDetails.get("ruleTypeKey");
		Object from = ruleDetails.get("moduleFromId");
		Object to = ruleDetails.get("moduleToId");
		boolean isEnabled = (Boolean) ruleDetails.get("enabled");
		String description = (String) ruleDetails.get("description");
		String regex = (String) ruleDetails.get("regex");
		String[] dependencies = (String[]) ruleDetails.get("dependencies");

		ModuleStrategy moduleFrom = assignToCorrectModule(from);
		ModuleStrategy moduleTo;
		if (to == null) {
			moduleTo = assignToCorrectModule(from);
		} else {
			moduleTo = assignToCorrectModule(to);
		}

		try {
			if (getAction().equals(PopUpController.ACTION_NEW)) {
				currentAppliedRuleId = appliedRuleService.addAppliedRule(
						ruleTypeKey, description, dependencies, regex,
						moduleFrom, moduleTo, isEnabled);
				if (currentAppliedRuleId == -1) {
					logger.info("An identical rule already exists");
					return false;
				}
			} else if (getAction().equals(PopUpController.ACTION_EDIT)) {
				appliedRuleService.updateAppliedRule(currentAppliedRuleId,
						ruleTypeKey, description, dependencies, regex,
						moduleFrom, moduleTo, isEnabled);
				appliedRuleExceptionService
						.removeAllAppliedRuleExceptions(currentAppliedRuleId);
			}

			saveAllExceptionRules();
			DefinitionController.getInstance().notifyObservers(currentModuleId);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void saveAllExceptionRules() {
		appliedRuleExceptionService
				.removeAllAppliedRuleExceptions(currentAppliedRuleId);
		ArrayList<AppliedRuleStrategy> rules = new ArrayList<AppliedRuleStrategy>();
		AppliedRuleFactory factory = new AppliedRuleFactory();
		for (HashMap<String, Object> exceptionRule : exceptionRules) {
			long appliedRuleId = currentAppliedRuleId;
			String ruleTypeKey = (String) exceptionRule.get("ruleTypeKey");
			String description = (String) exceptionRule.get("description");
			String[] dependencies = (String[]) exceptionRule
					.get("dependencies");

			Object from = exceptionRule.get("moduleFromId");
			Object to = exceptionRule.get("moduleToId");
			ModuleStrategy moduleFrom = assignToCorrectModule(from);
			ModuleStrategy moduleTo = assignToCorrectModule(to);

			appliedRuleExceptionService.addExceptionToAppliedRule(
					appliedRuleId, ruleTypeKey, description, moduleFrom,
					moduleTo, dependencies);

			AppliedRuleFactory ruleFactory = new AppliedRuleFactory();
			AppliedRuleStrategy exceptionRule2 = ruleFactory
					.createRule(ruleTypeKey);
			exceptionRule2.setAppliedRule(description, moduleFrom, moduleTo);
			exceptionRule2.setDependencies(dependencies);
			rules.add(exceptionRule2);

		}
		// hot hack {{
		StateService.instance().addExceptionRule(
				SoftwareArchitecture.getInstance().getAppliedRuleById(
						currentAppliedRuleId), rules);

	}

	public boolean saveDefaultRules(HashMap<String, Object> ruleDetails) {

		String ruleTypeKey = (String) ruleDetails.get("ruleTypeKey");
		Object from = ruleDetails.get("moduleFromId");
		Object to = ruleDetails.get("moduleToId");
		boolean isEnabled = (Boolean) ruleDetails.get("enabled");
		String description = (String) ruleDetails.get("description");
		String regex = (String) ruleDetails.get("regex");
		String[] dependencies = (String[]) ruleDetails.get("dependencies");

		ModuleStrategy moduleFrom = (ModuleStrategy) from;
		ModuleStrategy moduleTo = (ModuleStrategy) to;

		appliedRuleService.addAppliedRule(ruleTypeKey, description,
				dependencies, regex, moduleFrom, moduleTo, isEnabled);

		return true;

	}

	public void setModuleToId(Long moduleToId) {
		this.moduleToId = moduleToId;
	}

	public void setSelectedRuleTypeKey(String ruleTypeKey) {
		selectedRuleTypeKey = ruleTypeKey;
	}

}
