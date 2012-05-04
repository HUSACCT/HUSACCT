package husacct.define.task;

import husacct.ServiceProvider;
import husacct.common.dto.CategoryDTO;
import husacct.common.dto.RuleTypeDTO;
import husacct.define.presentation.helper.DataHelper;
import husacct.define.presentation.jframe.JFrameAppliedRule;
import husacct.define.presentation.jframe.JFrameExceptionRule;
import husacct.define.presentation.utils.KeyValueComboBox;
import husacct.define.presentation.utils.UiDialogs;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

public class AppliedRuleController extends PopUpController {

	private JFrameAppliedRule jframeAppliedRule;
	private JFrameExceptionRule jframeExceptionRule;
	private long currentAppliedRuleId;
	private String selectedRuleTypeKey;
	private ArrayList<HashMap<String, Object>> exceptionRules = new ArrayList<HashMap<String, Object>>();

	public AppliedRuleController(long moduleId, long appliedRuleId) {
		super();
		setModuleId(moduleId);
		this.currentAppliedRuleId = appliedRuleId;
	}

	/*
	 * Init GUI
	 */
	@Override
	public void initUi() throws Exception {
		jframeAppliedRule = new JFrameAppliedRule(this);

		// Change view of jframe conforms the action
		if (getAction().equals(SoftwareUnitController.ACTION_NEW)) {
			jframeAppliedRule.jButtonSave.setText("Create");
			jframeAppliedRule.setTitle("New applied rule");
		} else if (getAction().equals(SoftwareUnitController.ACTION_EDIT)) {
			jframeAppliedRule.jButtonSave.setText("Save");
			jframeAppliedRule.setTitle("Edit applied rule");
//			if (currentAppliedRuleId != -1L) {
//				// Load name & type
//				
//				//jframe.jComboBoxToLayer.setSelectedItem(defineDomainService.getLayerName(defineDomainService.getAppliedRuleToLayer(getLayerID(), appliedrule_id)));
//				//jframe.jCheckBoxEnabled.setSelected(defineDomainService.getAppliedRuleIsEnabled(getLayerID(), appliedrule_id));
//
//				// Load table with exceptions
//				JTableException table = jframe.jTableException;
////				JTableTableModel tablemodel = (JTableTableModel) table.getModel();
//
//				ArrayList<Long> exceptionIds = defineDomainService.getExceptionIdsByAppliedRule(currentAppliedRuleId);
//				for (long exception_id : exceptionIds) {
//					DataHelper datahelper = new DataHelper();
//					datahelper.setId(exception_id);
////					datahelper.setValue(defineDomainService.getAppliedruleExceptionName(getLayerID(), appliedRuleId, exception_id));
//
////					Object[] row = { datahelper, defineDomainService.getAppliedRuleExceptionType(getLayerID(), appliedRuleId, exception_id) };
////					tablemodel.addRow(row);
//				}
//			}
		}

		// Set the visibility of the jframe to true so the jframe is now visible
		UiDialogs.showOnScreen(0, jframeAppliedRule);
		jframeAppliedRule.setVisible(true);
	}
	/*
	 * Load Data
	 */
	public void fillRuleTypeComboBox(KeyValueComboBox keyValueComboBoxAppliedRule) {
		CategoryDTO[] categories = ServiceProvider.getInstance().getValidateService().getCategories();
		RuleTypeDTO[] ruleTypes = categories[0].ruleTypes;
		ArrayList<String> ruleTypeKeys = new ArrayList<String>();
		ArrayList<String> ruleTypeValues = new ArrayList<String>();
		
		//foreach ruletype set ruletypekeys array
		for (RuleTypeDTO ruleTypeDTO : ruleTypes){
			ruleTypeKeys.add(ruleTypeDTO.key);
		}
				
		//Get the correct display value for each ruletypekey from the resourcebundle
		for (RuleTypeDTO ruleTypeDTO : ruleTypes){
			String value = resourceBundle.getString(ruleTypeDTO.key);
			ruleTypeValues.add(value);
		}
		keyValueComboBoxAppliedRule.setModel(ruleTypeKeys.toArray(), ruleTypeValues.toArray());
	}
	
	public void fillRuleTypeComboBoxWithExceptions(KeyValueComboBox keyValueComboBoxAppliedRule) {
		CategoryDTO[] categories = ServiceProvider.getInstance().getValidateService().getCategories();
		RuleTypeDTO[] ruleTypes = categories[0].ruleTypes;
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
					String value = resourceBundle.getString(ruleDTO.key);
					ruleTypeValues.add(value);
				}
				keyValueComboBoxAppliedRule.setModel(ruleTypeKeys.toArray(), ruleTypeValues.toArray());
			}
		}
	}
	
	public ComboBoxModel loadModulesToCombobox() {
		ComboBoxModel comboBoxModel = new DefaultComboBoxModel();
		// loading of all layers
		ArrayList<Long> moduleIds = defineDomainService.getRootModulesIds();

		if (moduleIds != null) {
			// Remove the current layer from the list
			moduleIds.remove(getModuleId());

			ArrayList<DataHelper> layernames = new ArrayList<DataHelper>();
			for (long moduleId : moduleIds) {
				DataHelper datahelper = new DataHelper();
				datahelper.setId(moduleId);
				datahelper.setValue("" + defineDomainService.getModuleNameById(moduleId));
				layernames.add(datahelper);
			}

			comboBoxModel = new DefaultComboBoxModel(layernames.toArray());
		}
		return comboBoxModel;
	}
	
	public ComboBoxModel loadsubModulesToCombobox(Long parentModuleId) {
		ComboBoxModel comboBoxModel = new DefaultComboBoxModel();
		// loading of all layers
		ArrayList<Long> moduleIds = defineDomainService.getSubModuleIds(parentModuleId);

		ArrayList<DataHelper> layernames = new ArrayList<DataHelper>();
		for (long moduleId : moduleIds) {
			DataHelper datahelper = new DataHelper();
			datahelper.setId(moduleId);
			datahelper.setValue("" + defineDomainService.getModuleNameById(moduleId));
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
	
	public String getCurrentModuleName(){
		long currentModuleId = getModuleId();
		return defineDomainService.getModuleNameById(currentModuleId);
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
			if (getAction().equals(PopUpController.ACTION_NEW)) {
				currentAppliedRuleId = defineDomainService.addAppliedRule(ruleTypeKey, description,dependencies,regex, moduleFromId, moduleToId, isEnabled);
			} else if (getAction().equals(PopUpController.ACTION_EDIT)) {
				defineDomainService.updateAppliedRule(currentAppliedRuleId, ruleTypeKey, description,dependencies,regex, moduleFromId, moduleToId, isEnabled);
			}
			saveAllExceptionRules();
			
			jframeAppliedRule.dispose();
			pokeObservers();
		} catch (Exception e) {
			UiDialogs.errorDialog(jframeAppliedRule, e.getMessage(), "Error");
		}
	}
	
	public void saveAllExceptionRules(){
		defineDomainService.removeAllAppliedRuleExceptions(currentAppliedRuleId);
		
		for (HashMap<String, Object> exceptionRule : exceptionRules) {
			long appliedRuleId = currentAppliedRuleId;
			String ruleTypeKey = (String) exceptionRule.get("ruleTypeKey");
			String description = (String) exceptionRule.get("description");
			long moduleFromId = (Long) exceptionRule.get("moduleFromId");
			long moduleToId = (Long) exceptionRule.get("moduleToId");
			
			defineDomainService.addExceptionToAppliedRule(appliedRuleId, ruleTypeKey, description, moduleFromId, moduleToId);
		}
	}

	/*
	 * Exceptions
	 */
	public void createExceptionGUI(Long selectedModuleFromId, Long selectedModuleToId) {
		jframeExceptionRule = new JFrameExceptionRule(this);

		// Change view of jframe conforms the action
		if (getAction().equals(SoftwareUnitController.ACTION_NEW)) {
			jframeExceptionRule.jButtonSave.setText("Create");
			jframeExceptionRule.setTitle("New exception rule");
		} else if (getAction().equals(SoftwareUnitController.ACTION_EDIT)) {
			jframeExceptionRule.jButtonSave.setText("Save");
			jframeExceptionRule.setTitle("Edit exception rule");
		}
		// Set the visibility of the jframe to true so the jframe is now visible
		UiDialogs.showOnScreen(0, jframeExceptionRule);
		
		
		//load the correct submodules in the comboboxes 
		jframeExceptionRule.jPanelRuleDetails.jComboBoxModuleFrom.setModel(loadsubModulesToCombobox(selectedModuleFromId));
		jframeExceptionRule.jPanelRuleDetails.jComboBoxModuleTo.setModel(loadsubModulesToCombobox(selectedModuleToId));
		
		
		
		jframeExceptionRule.setVisible(true);
	}

	public void addException(HashMap<String, Object> exceptionRule){
		exceptionRules.add(exceptionRule);
		notifyObservers();
	}
	
	public void removeException(Long exceptionRuleId){
		for (HashMap<String, Object> exRule : exceptionRules){
			Long exRuleId = (Long) exRule.get("id");
			if (exRuleId == exceptionRuleId){
				exceptionRules.remove(exRule);
			}
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
		
	}
	
	public ArrayList<HashMap<String, Object>> getExceptionRules(){
		return exceptionRules;
	}

	public String getModuleName(Long moduleIdFrom) {
		return defineDomainService.getModuleNameById(moduleIdFrom);
	}
}
