package husacct.define.task;

import husacct.ServiceProvider;
import husacct.common.dto.CategoryDTO;
import husacct.common.dto.RuleTypeDTO;
import husacct.define.domain.AppliedRule;
import husacct.define.abstraction.language.DefineTranslator;
import husacct.define.domain.module.Layer;
import husacct.define.domain.module.Module;
import husacct.define.domain.services.AppliedRuleDomainService;
import husacct.define.domain.services.AppliedRuleExceptionDomainService;
import husacct.define.domain.services.ModuleDomainService;
import husacct.define.presentation.helper.DataHelper;
import husacct.define.presentation.jframe.JFrameAppliedRule;
import husacct.define.presentation.utils.KeyValueComboBox;
import husacct.define.presentation.utils.UiDialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.Observer;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

public class AppliedRuleController extends PopUpController {

	private JFrameAppliedRule jframeAppliedRule;
	private long currentAppliedRuleId;
	private String selectedRuleTypeKey;
	private ArrayList<HashMap<String, Object>> exceptionRules = new ArrayList<HashMap<String, Object>>();
	
	private ModuleDomainService moduleService;
	private AppliedRuleDomainService appliedRuleService;
	private AppliedRuleExceptionDomainService appliedRuleExceptionService;

	public AppliedRuleController(long moduleId, long appliedRuleId) {
		super();
		this.setModuleId(moduleId);
		this.currentAppliedRuleId = appliedRuleId;
		this.determineAction();
		
		this.moduleService = new ModuleDomainService();
		this.appliedRuleService = new AppliedRuleDomainService();
		this.appliedRuleExceptionService = new AppliedRuleExceptionDomainService();
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
					if(!(selectedModule instanceof Layer) && (ruleTypeDTO.key.equals("SkipCall") || ruleTypeDTO.key.equals("BackCall"))) {
						continue;
					} else {
						String value = DefineTranslator.translate(ruleTypeDTO.key);
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
	
	public void fillRuleTypeComboBoxWithExceptions(KeyValueComboBox keyValueComboBoxAppliedRule) {
		CategoryDTO[] categories = ServiceProvider.getInstance().getValidateService().getCategories();
		
		for (CategoryDTO categorie : categories){
			RuleTypeDTO[] ruleTypes = categorie.ruleTypes;
			//Get currently selected RuleType
			for (RuleTypeDTO ruleTypeDTO : ruleTypes){
				if (ruleTypeDTO.key.equals(selectedRuleTypeKey)){
					//Fill combobox with exceptionruletypes of that rule
					ArrayList<String> ruleTypeKeys = new ArrayList<String>();
					ArrayList<String> ruleTypeValues = new ArrayList<String>();
					
					for (RuleTypeDTO ruleDTO : ruleTypeDTO.exceptionRuleTypes){
						ruleTypeKeys.add(ruleDTO.key);
					}
							
					//Get the correct display value for each ruletypekey from the resourcebundle
					for (RuleTypeDTO ruleDTO : ruleTypeDTO.exceptionRuleTypes){
						String value = DefineTranslator.translate(ruleDTO.key);
						ruleTypeValues.add(value);
					}
					keyValueComboBoxAppliedRule.setModel(ruleTypeKeys.toArray(), ruleTypeValues.toArray());
				}
			}
		}
	}
	
	public ComboBoxModel loadModulesToCombobox() {
		ComboBoxModel comboBoxModel = new DefaultComboBoxModel();
		// loading of all layers
		ArrayList<Long> moduleIds = this.moduleService.getRootModulesIds();

		if (moduleIds != null) {
			// Remove the current layer from the list
			moduleIds.remove(getModuleId());

			ArrayList<DataHelper> layernames = new ArrayList<DataHelper>();
			for (long moduleId : moduleIds) {
				DataHelper datahelper = new DataHelper();
				datahelper.setId(moduleId);
				datahelper.setValue("" + this.moduleService.getModuleNameById(moduleId));
				layernames.add(datahelper);
			}

			comboBoxModel = new DefaultComboBoxModel(layernames.toArray());
		}
		return comboBoxModel;
	}
	
	public ComboBoxModel loadsubModulesToCombobox(Long parentModuleId) {
		ComboBoxModel comboBoxModel = new DefaultComboBoxModel();
		// loading of all layers
		ArrayList<Long> moduleIds = this.moduleService.getSubModuleIds(parentModuleId);

		ArrayList<DataHelper> layernames = new ArrayList<DataHelper>();
		for (long moduleId : moduleIds) {
			DataHelper datahelper = new DataHelper();
			datahelper.setId(moduleId);
			datahelper.setValue("" + this.moduleService.getModuleNameById(moduleId));
			layernames.add(datahelper);
		}

		comboBoxModel = new DefaultComboBoxModel(layernames.toArray());
		return comboBoxModel;
	}
	/*
	 * Getters & Setters
	 */
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

	/*
	 * Saving
	 */
	public void save(String ruleTypeKey, String description, String[] dependencies, String regex,long moduleFromId, long moduleToId, boolean isEnabled) {
		try {
			if (this.getAction().equals(PopUpController.ACTION_NEW)) {
				this.currentAppliedRuleId = this.appliedRuleService.addAppliedRule(ruleTypeKey, description, dependencies, regex, moduleFromId, moduleToId, isEnabled);
			} else if (getAction().equals(PopUpController.ACTION_EDIT)) {
				this.appliedRuleService.updateAppliedRule(currentAppliedRuleId, ruleTypeKey, description, dependencies, regex, moduleFromId, moduleToId, isEnabled);
			}
			this.saveAllExceptionRules();
			DefinitionController.getInstance().notifyObservers(this.currentModuleId);
		} catch (Exception e) {
			UiDialogs.errorDialog(jframeAppliedRule, e.getMessage(), "Error");
		}
	}
	
	public void saveAllExceptionRules(){
		this.appliedRuleExceptionService.removeAllAppliedRuleExceptions(currentAppliedRuleId);
		
		for (HashMap<String, Object> exceptionRule : exceptionRules) {
			long appliedRuleId = currentAppliedRuleId;
			String ruleTypeKey = (String) exceptionRule.get("ruleTypeKey");
			String description = (String) exceptionRule.get("description");
			long moduleFromId = (Long) exceptionRule.get("moduleFromId");
			long moduleToId = (Long) exceptionRule.get("moduleToId");
			
			this.appliedRuleExceptionService.addExceptionToAppliedRule(appliedRuleId, ruleTypeKey, description, moduleFromId, moduleToId);
		}
	}

	public void addException(HashMap<String, Object> exceptionRule){
		exceptionRules.add(exceptionRule);
	}
	
	public void removeException(Long exceptionRuleId){
		for (HashMap<String, Object> exRule : exceptionRules){
			Long exRuleId = (Long) exRule.get("id");
			if (exRuleId == exceptionRuleId){
				exceptionRules.remove(exRule);
			}
		}
	}
	
	/**
	 * This function will load notify all to update their data
	 */
	public void notifyObservers(long currentAppliedRuleId){
		for (Observer o : this.observers){
			o.update(this, currentAppliedRuleId);
		}
	}
		
	public HashMap<String, Object> getAppliedRuleDetails(long appliedRuleId){
		AppliedRule rule = this.appliedRuleService.getAppliedRuleById(appliedRuleId);
		HashMap<String, Object> ruleDetails = new HashMap<String, Object>();
		ruleDetails.put("id", rule.getId());
		ruleDetails.put("description", rule.getDescription());
		ruleDetails.put("dependencies", rule.getDependencies());
		ruleDetails.put("moduleFromName", rule.getModuleFrom().getName());
		ruleDetails.put("moduleToName", rule.getModuleTo().getName());
		ruleDetails.put("enabled", rule.isEnabled());
		ruleDetails.put("regex", rule.getRegex());
		ruleDetails.put("ruleTypeKey", rule.getRuleType());
		ruleDetails.put("numberofexceptions", rule.getExceptions().size());
		return ruleDetails;
	}
	
//		logger.info("Removing software unit " + softwareUnitName);
//		try {
//			long moduleId = getSelectedModuleId();
//
//			if (moduleId != -1 && softwareUnitName != null && !softwareUnitName.equals("")) {
//				// Ask the user if he is sure to remove the software unit
//				boolean confirm = UiDialogs.confirmDialog(definitionJPanel, "Are you sure you want to remove software unit: \"" + softwareUnitName + "\"", "Remove?");
//				if (confirm) {
//					// Remove the software unit
//					JPanelStatus.getInstance("Removing software unit").start();
//					DefineDomainService.getInstance().removeSoftwareUnit(moduleId, softwareUnitName);
//					// Update the software unit table
//					this.notifyObservers();
//				}
//			}
//		} catch (Exception e) {
//			logger.error("removeSoftwareUnit() - exception: " + e.getMessage());
//			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
//		} finally {
//			JPanelStatus.getInstance().stop();
//		}

	
	public ArrayList<HashMap<String, Object>> getExceptionRules(){
		return exceptionRules;
	}

	public String getModuleName(Long moduleIdFrom) {
		return this.moduleService.getModuleNameById(moduleIdFrom);
	}
}
