package husacct.define.task;

import husacct.ServiceProvider;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.CategoryDTO;
import husacct.common.dto.RuleTypeDTO;
import husacct.common.dto.ViolationTypeDTO;

import husacct.define.domain.AppliedRule;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.domain.SoftwareUnitDefinition.Type;
import husacct.define.domain.module.Layer;
import husacct.define.domain.module.Module;
import husacct.define.domain.module.SubSystem;
import husacct.define.domain.services.AppliedRuleDomainService;
import husacct.define.domain.services.AppliedRuleExceptionDomainService;
import husacct.define.domain.services.ModuleDomainService;
import husacct.define.presentation.jdialog.AppliedRuleJDialog;
import husacct.define.presentation.utils.DataHelper;
import husacct.define.presentation.utils.KeyValueComboBox;
import husacct.define.presentation.utils.UiDialogs;
import husacct.define.task.components.AbstractCombinedComponent;
import husacct.define.task.components.AbstractDefineComponent;
import husacct.define.task.components.AnalyzedModuleComponent;
import husacct.define.task.components.DefineComponentFactory;
import husacct.define.task.components.SoftwareArchitectureComponent;
import husacct.define.task.conventions_checker.RuleConventionsChecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.Observer;

import javax.swing.JOptionPane;

public class AppliedRuleController extends PopUpController {

	private AppliedRuleJDialog jframeAppliedRule;
	private long currentAppliedRuleId;
	private String selectedRuleTypeKey;
	private ArrayList<HashMap<String, Object>> exceptionRules = new ArrayList<HashMap<String, Object>>();
	
	private ModuleDomainService moduleService;
	private AppliedRuleDomainService appliedRuleService;
	private AppliedRuleExceptionDomainService appliedRuleExceptionService;
	
	private Long moduleToId;

	public AppliedRuleController(long moduleId, long appliedRuleId) {
		super();
		this.setModuleId(moduleId);
		this.currentAppliedRuleId = appliedRuleId;
		this.determineAction();
		
		this.moduleService = new ModuleDomainService();
		this.appliedRuleService = new AppliedRuleDomainService();
		this.appliedRuleExceptionService = new AppliedRuleExceptionDomainService();
		
		if (getAction().equals(PopUpController.ACTION_EDIT)){
			loadAllRuleExceptions();
		}
	}
	
	private void determineAction() {
		if(this.currentAppliedRuleId == -1L) {
			this.setAction(PopUpController.ACTION_NEW);
		} else {
			this.setAction(PopUpController.ACTION_EDIT);
		}
	}

	/**
	 * Load Data
	 */
	public void fillRuleTypeComboBox(KeyValueComboBox keyValueComboBoxAppliedRule) {
		CategoryDTO[] categories = ServiceProvider.getInstance().getValidateService().getCategories();
		ArrayList<String> ruleTypeKeys = new ArrayList<String>();
		ArrayList<String> ruleTypeValues = new ArrayList<String>();
		
		for (CategoryDTO categorie : categories) {
			RuleTypeDTO[] ruleTypes = categorie.ruleTypes;
			
			Module selectedModule = this.moduleService.getModuleById(DefinitionController.getInstance().getSelectedModuleId());
					
			//Get the correct display value for each ruletypekey from the resourcebundle
			for (RuleTypeDTO ruleTypeDTO : ruleTypes){
				
				try {
					if(!(selectedModule instanceof Layer)  && (ruleTypeDTO.key.contains("SkipCall") || ruleTypeDTO.key.contains("BackCall"))) {
						continue;
					} else {
						String value = ServiceProvider.getInstance().getLocaleService().getTranslatedString(ruleTypeDTO.key);
						ruleTypeKeys.add(ruleTypeDTO.key);
						ruleTypeValues.add(value);
					}
				} catch(MissingResourceException e){
					ruleTypeValues.add(ruleTypeDTO.key);
					logger.info("Key not found in resourcebundle: " + ruleTypeDTO.key);
				}
			}
		}
		keyValueComboBoxAppliedRule.setModel(ruleTypeKeys.toArray(), ruleTypeValues.toArray());
	}
	
	public boolean hasSelectedRuleTypeHaveExceptions(){
		boolean hasException = false;
		CategoryDTO[] categories = ServiceProvider.getInstance().getValidateService().getCategories();
		
		for (CategoryDTO categorie : categories){
			RuleTypeDTO[] ruleTypes = categorie.ruleTypes;
			//Get currently selected RuleType
			for (RuleTypeDTO ruleTypeDTO : ruleTypes){
				if (ruleTypeDTO.key.equals(selectedRuleTypeKey)){
					if (ruleTypeDTO.exceptionRuleTypes.length > 0){
						hasException = true;
					}
				}
			}
		}
		return hasException;
	}
	
	public void fillRuleTypeComboBoxWithExceptions(KeyValueComboBox keyValueComboBoxAppliedRule) {
		CategoryDTO[] categories = ServiceProvider.getInstance().getValidateService().getCategories();
		
		for (CategoryDTO categorie : categories){
			RuleTypeDTO[] ruleTypes = categorie.ruleTypes;
			//Get currently selected RuleType
			for (RuleTypeDTO ruleTypeDTO : ruleTypes){
				if (ruleTypeDTO.key.equals(selectedRuleTypeKey)){
					if (ruleTypeDTO.exceptionRuleTypes.length == 0){ throw new RuntimeException("No exception keys found for ruletype: " + selectedRuleTypeKey);}
					
					//Fill combobox with exceptionruletypes of that rule
					ArrayList<String> ruleTypeKeys = new ArrayList<String>();
					ArrayList<String> ruleTypeValues = new ArrayList<String>();
					
					for (RuleTypeDTO ruleDTO : ruleTypeDTO.exceptionRuleTypes){
						ruleTypeKeys.add(ruleDTO.key);
						String value = ServiceProvider.getInstance().getLocaleService().getTranslatedString(ruleDTO.key);
						ruleTypeValues.add(value);
					}
					keyValueComboBoxAppliedRule.setModel(ruleTypeKeys.toArray(), ruleTypeValues.toArray());
				}
			}
		}
	}
	
	public ArrayList<DataHelper> getSiblingModules(long moduleId){
		ArrayList<Long> moduleIds = new ArrayList<Long>();
		for (Long modId : this.moduleService.getSiblingModuleIds(moduleId)){
			moduleIds.addAll(this.moduleService.getSubModuleIds(modId));
		}
		
		ArrayList<DataHelper> moduleNames = new ArrayList<DataHelper>();
		for (long modId : moduleIds) {
			if (modId != getCurrentModuleId()){
				DataHelper datahelper = new DataHelper();
				datahelper.setId(modId);
				datahelper.setValue("" + this.moduleService.getModuleNameById(modId));
				moduleNames.add(datahelper);
			}
		}
		return moduleNames;
	}
	
	public ArrayList<DataHelper> getChildModules(long parentModuleId){
		ArrayList<Long> moduleIds = this.moduleService.getSubModuleIds(parentModuleId);
		ArrayList<DataHelper> moduleNames = new ArrayList<DataHelper>();
		for (long moduleId : moduleIds) {
			if (moduleId != getCurrentModuleId()){
				DataHelper datahelper = new DataHelper();
				datahelper.setId(moduleId);
				datahelper.setValue("" + this.moduleService.getModuleNameById(moduleId));
				moduleNames.add(datahelper);
			}
		}
		return moduleNames;
	}
	
	public HashMap<String, Object> getAppliedRuleDetails(long appliedRuleId){
		AppliedRule rule = this.appliedRuleService.getAppliedRuleById(appliedRuleId);
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
	
	private void addDefineModuleChildComponents(AbstractCombinedComponent parentComponent, Module module) {
		AbstractDefineComponent childComponent = DefineComponentFactory.getDefineComponent(module);
		for(Module subModule : module.getSubModules()) {
			this.addDefineModuleChildComponents(childComponent, subModule);
		}
		
		ArrayList<SoftwareUnitDefinition> softwareUnits = module.getUnits();
		for(SoftwareUnitDefinition softwareUnit : softwareUnits) {
			AnalyzedModuleComponent analysedComponent = new AnalyzedModuleComponent(softwareUnit.getName(), softwareUnit.getName(), softwareUnit.getType().toString(), "public");

			AnalysedModuleDTO[] children = ServiceProvider.getInstance().getAnalyseService().getChildModulesInModule(softwareUnit.getName());
			for(AnalysedModuleDTO subModule : children) {
				this.addChildComponents(analysedComponent, subModule);
			}
			
			childComponent.addChild(analysedComponent);
		}
		
		parentComponent.addChild(childComponent);
	}
	
	private void addChildComponents(AnalyzedModuleComponent parentComponent, AnalysedModuleDTO module) {
		AnalyzedModuleComponent childComponent = new AnalyzedModuleComponent(module.uniqueName, module.name, module.type, module.visibility);
		AnalysedModuleDTO[] children = ServiceProvider.getInstance().getAnalyseService().getChildModulesInModule(module.uniqueName);
		for(AnalysedModuleDTO subModule : children) {
			this.addChildComponents(childComponent, subModule);
		}
		parentComponent.addChild(childComponent);
	}

	public boolean isAnalysed() {
		return ServiceProvider.getInstance().getAnalyseService().isAnalysed();
	}

	public void clearRuleExceptions() {
		this.exceptionRules.clear();
	}
	
	/*
	 * Saving
	 */
	public boolean save(HashMap<String, Object> ruleDetails){
		
		String ruleTypeKey = (String) ruleDetails.get("ruleTypeKey");
		Object from = ruleDetails.get("moduleFromId");
		Object to = ruleDetails.get("moduleToId");
		boolean isEnabled = (Boolean) ruleDetails.get("enabled");
		String description = (String) ruleDetails.get("description");
		String regex = (String) ruleDetails.get("regex");
		String[] dependencies = (String[]) ruleDetails.get("dependencies");
		
		Module moduleFrom = assignToCorrectModule(from);
		Module moduleTo = assignToCorrectModule(to);
		
		try {
			if (this.getAction().equals(PopUpController.ACTION_NEW)) {
				if (!this.checkRuleConventions(moduleFrom, moduleTo, ruleTypeKey)){ return false;}//Does not comply with ruleconventions
				this.currentAppliedRuleId = this.appliedRuleService.addAppliedRule(ruleTypeKey, description, dependencies, regex, moduleFrom, moduleTo, isEnabled);
			} else if (getAction().equals(PopUpController.ACTION_EDIT)) {
				this.appliedRuleService.updateAppliedRule(currentAppliedRuleId, ruleTypeKey, description, dependencies, regex, moduleFrom, moduleTo, isEnabled);
				this.appliedRuleExceptionService.removeAllAppliedRuleExceptions(currentAppliedRuleId);
			}
			
			this.saveAllExceptionRules();
			DefinitionController.getInstance().notifyObservers(this.currentModuleId);
			return true;
		} catch (Exception e) {
			UiDialogs.errorDialog(jframeAppliedRule, e.getMessage());
			return false;
		}
	}
	
	public boolean saveDefualtrules(HashMap<String, Object> ruleDetails){
		
		String ruleTypeKey = (String) ruleDetails.get("ruleTypeKey");
		Object from = ruleDetails.get("moduleFromId");
		Object to = ruleDetails.get("moduleToId");
		boolean isEnabled = (Boolean) ruleDetails.get("enabled");
		String description = (String) ruleDetails.get("description");
		String regex = (String) ruleDetails.get("regex");
		String[] dependencies = (String[]) ruleDetails.get("dependencies");
		
		Module moduleFrom = (Module)(from);
		Module moduleTo = (Module)(to);
		
		appliedRuleService.addAppliedRule(ruleTypeKey, description, dependencies, regex, moduleFrom, moduleTo, isEnabled);
		
		return true;
		
		
		
		
	}
	
	private Module assignToCorrectModule(Object o){
		Module module;
		if (o instanceof SoftwareUnitDefinition){
			module = getModuleWhereSoftwareUnitNeedsToBeMapped((SoftwareUnitDefinition) o, (SoftwareUnitDefinition) o);
		} else if (o instanceof Long){
			long moduleId = (Long) o;
			if (moduleId != -1){
				module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
			} else {
				module = new Module();
				module.setId(-1);
			}
		} else {
			module = new Module();
		}
		return module;
	}
	
	private Module getModuleWhereSoftwareUnitNeedsToBeMapped(SoftwareUnitDefinition currentSoftwareUnit, final SoftwareUnitDefinition finalSoftwareUnit){
		Module returnModule;
		try {
			//Search all module for the softwareunit definition we are trying to map
			Module module = moduleService.getModuleIdBySoftwareUnit(currentSoftwareUnit);
			//Current Softwareunit is now found, adding to current module or sub
			returnModule = createOrAssignModule(module, finalSoftwareUnit);
		} catch (RuntimeException e){
			//Current softwareunit definition not found
			//Go recursive and look if the parent of the softwareunit is mapped.
			
			AnalysedModuleDTO analysedModuleDTO = ServiceProvider.getInstance().getAnalyseService().getParentModuleForModule(currentSoftwareUnit.getName());
			if (!analysedModuleDTO.name.equals("")) { 
				Type type = Type.valueOf(analysedModuleDTO.type.toUpperCase());
				SoftwareUnitDefinition parentSU = new SoftwareUnitDefinition(analysedModuleDTO.uniqueName, type);
				
				returnModule = getModuleWhereSoftwareUnitNeedsToBeMapped(parentSU, finalSoftwareUnit);
				
			} else {//No higher parent of softwareUnit
				//Conclusion: softwareunit is not mapped at all. now at to the root
				logger.info("No parent found for softwareunit : " + currentSoftwareUnit.getName());
				logger.info("Adding " + currentSoftwareUnit.getName() + " to a module in the root");
				
				Module subModule = new SubSystem(currentSoftwareUnit.getName(), "");
				moduleService.addModuleToRoot(subModule);
				returnModule = subModule;
			}
		}
		return returnModule;
	}
	
	private Module createOrAssignModule(Module module, SoftwareUnitDefinition su){
		Module moduleToReturn;
		ArrayList<SoftwareUnitDefinition> softwareUnits = module.getUnits(); 
		
		String firstSUName = softwareUnits.get(0).getName();
		if (module.getUnits().size() == 1 && firstSUName.equals(su.getName())){
			moduleToReturn = module;
		} else {
			Module subModule = new SubSystem(su.getName(), "");
			subModule.addSUDefinition(su);
			moduleService.addModuleToParent(module.getId(), subModule);
			moduleToReturn = subModule;
		}
		return moduleToReturn;
	}
	
	private boolean checkRuleConventions(Module moduleFrom, Module moduleTo, String ruleTypeKey) {
		RuleConventionsChecker conventionsChecker = new RuleConventionsChecker(moduleFrom, moduleTo, ruleTypeKey);
		if(!conventionsChecker.checkRuleConventions()) {
			String errorMessage = conventionsChecker.getErrorMessage();
			JOptionPane.showMessageDialog(jframeAppliedRule, errorMessage, ServiceProvider.getInstance().getLocaleService().getTranslatedString("ConventionError"), JOptionPane.ERROR_MESSAGE);
			return false;
		} else {
			return true;
		}
	}

	public void saveAllExceptionRules(){
		this.appliedRuleExceptionService.removeAllAppliedRuleExceptions(currentAppliedRuleId);
		
		for (HashMap<String, Object> exceptionRule : exceptionRules) {
			long appliedRuleId = currentAppliedRuleId;
			String ruleTypeKey = (String) exceptionRule.get("ruleTypeKey");
			String description = (String) exceptionRule.get("description");
			String[] dependencies = (String[]) exceptionRule.get("dependencies");
			
			Object from = exceptionRule.get("moduleFromId");
			Object to = exceptionRule.get("moduleToId");
			Module moduleFrom = assignToCorrectModule(from);
			Module moduleTo = assignToCorrectModule(to);
			
			this.appliedRuleExceptionService.addExceptionToAppliedRule(appliedRuleId, ruleTypeKey, description, moduleFrom, moduleTo, dependencies);
		}
	}
	private void loadAllRuleExceptions(){
		ArrayList<AppliedRule> exceptions = this.appliedRuleExceptionService.getExceptionsByAppliedRule(this.currentAppliedRuleId);
		for (AppliedRule exception : exceptions){
			HashMap<String, Object> exceptionRule = new HashMap<String, Object>();
			exceptionRule.put("id", exception.getId());
			exceptionRule.put("ruleTypeKey", exception.getRuleType());
			exceptionRule.put("moduleFromId", exception.getModuleFrom().getId());
			exceptionRule.put("moduleToId", exception.getModuleTo().getId());
			exceptionRule.put("dependencies", exception.getDependencies());
			exceptionRule.put("enabled", exception.isEnabled());
			exceptionRule.put("description", exception.getDescription());
			exceptionRule.put("regex", exception.getRegex());
			addException(exceptionRule);
		}
		notifyObservers();
	}

	public void addException(HashMap<String, Object> exceptionRule){
		exceptionRules.add(exceptionRule);
	}
	
	public void removeException(int index){
		if (index != -1){
			try {
				exceptionRules.remove(index);
			} catch (Exception e){}
		}
	}
	
	/*
	 * Oberver
	 */
	public void notifyObservers(long currentAppliedRuleId){
		for (Observer o : this.observers){
			o.update(this, currentAppliedRuleId);
		}
	}
	
	/*
	 * Getters & Setters
	 */
	public ArrayList<HashMap<String, Object>> getExceptionRules(){
		return exceptionRules;
	}

	public String getModuleName(Long moduleIdFrom) {
		return this.moduleService.getModuleNameById(moduleIdFrom);
	}
	
	public AbstractCombinedComponent getModuleTreeComponents() {
		SoftwareArchitectureComponent rootComponent = new SoftwareArchitectureComponent();
		ArrayList<Module> modules = this.moduleService.getSortedModules();
		for (Module module : modules) {
			this.addDefineModuleChildComponents(rootComponent, module);
		}
		return rootComponent;
	}
	
	public void setSelectedRuleTypeKey(String ruleTypeKey) {
		this.selectedRuleTypeKey = ruleTypeKey;
	}
	
	public String getSelectedRuleTypeKey() {
		return this.selectedRuleTypeKey;
	}
	
	public long getCurrentAppliedRuleId(){
		return this.currentAppliedRuleId;
	}
	
	public String getCurrentModuleName(){
		long currentModuleId = getModuleId();
		return this.moduleService.getModuleNameById(currentModuleId);
	}
	
	public Long getCurrentModuleId(){
		long currentModuleId = getModuleId();
		return currentModuleId;
	}
	
	public Long getModuleToId(){
		return moduleToId;
	}
	
	public void setModuleToId(Long moduleToId){
		this.moduleToId = moduleToId;
	}

	public ArrayList<ViolationTypeDTO> getViolationTypesByRuleType(String ruleTypeKey){
		ArrayList<ViolationTypeDTO> violationTypeDtoList = new ArrayList<ViolationTypeDTO>();
		
		CategoryDTO[] categories = ServiceProvider.getInstance().getValidateService().getCategories();
		
		for (CategoryDTO categorie : categories){
			RuleTypeDTO[] ruleTypes = categorie.ruleTypes;
			//Get currently selected RuleType
			for (RuleTypeDTO ruleTypeDTO : ruleTypes){
				if (ruleTypeDTO.key.equals(ruleTypeKey)){
					for (ViolationTypeDTO vt : ruleTypeDTO.violationTypes){
						violationTypeDtoList.add(vt);
					}
				}
				
				//Check exceptions rules
				for (RuleTypeDTO ruleTypeExceptionDTO : ruleTypeDTO.getExceptionRuleTypes()){
					if (ruleTypeExceptionDTO.key.equals(ruleTypeKey)){
						for (ViolationTypeDTO vt : ruleTypeExceptionDTO.violationTypes){
							violationTypeDtoList.add(vt);
						}
					}
				}
				
			}
		}
		return violationTypeDtoList;
	}
	
}
