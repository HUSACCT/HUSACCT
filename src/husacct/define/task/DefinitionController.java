package husacct.define.task;

import husacct.define.domain.module.Component;
import husacct.define.domain.module.ExternalLibrary;
import husacct.define.domain.module.Layer;
import husacct.define.domain.module.Module;
import husacct.define.domain.services.AppliedRuleDomainService;
import husacct.define.domain.services.AppliedRuleExceptionDomainService;
import husacct.define.domain.services.ModuleDomainService;
import husacct.define.domain.services.SoftwareUnitDefinitionDomainService;
import husacct.define.presentation.helper.DataHelper;
import husacct.define.presentation.jpanel.DefinitionJPanel;
import husacct.define.presentation.tables.JTableAppliedRule;
import husacct.define.presentation.tables.JTableSoftwareUnits;
import husacct.define.presentation.tables.JTableTableModel;
import husacct.define.presentation.utils.JPanelStatus;
import husacct.define.presentation.utils.UiDialogs;
import husacct.define.task.components.AbstractDefineComponent;
import husacct.define.task.components.DefineComponentFactory;
import husacct.define.task.components.SoftwareArchitectureComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

public class DefinitionController extends Observable implements Observer {
	
	private DefinitionJPanel definitionJPanel;
	private static DefinitionController instance;
	private List<Observer> observers;
	private Logger logger;
	private long selectedModuleId = -1;
	
	private ModuleDomainService moduleService;
	private AppliedRuleDomainService appliedRuleService;
	private AppliedRuleExceptionDomainService appliedRuleExceptionService;
	private SoftwareUnitDefinitionDomainService softwareUnitDefinitionDomainService;
	
	public static DefinitionController getInstance() {
		return instance == null ? (instance = new DefinitionController()) : instance;
	}

	public DefinitionController() {
		this.observers = new ArrayList<Observer>();
		this.logger = Logger.getLogger(DefinitionController.class);
		this.moduleService = new ModuleDomainService();
		this.appliedRuleService = new AppliedRuleDomainService();
		this.appliedRuleExceptionService = new AppliedRuleExceptionDomainService();
		this.softwareUnitDefinitionDomainService = new SoftwareUnitDefinitionDomainService();
	}
	
	public void initSettings() {
		this.observers.clear();
		this.definitionJPanel = new DefinitionJPanel();
	}

	/**
	 * Init the user interface for creating/editting the definition.
	 * 
	 * @return JPanel The jpanel
	 */
	public JPanel initUi() {
		return definitionJPanel;
	}

	public void setSelectedModuleId(long moduleId) {
		this.selectedModuleId = moduleId;
		logger.info("New Selected Module: " + moduleId);
		notifyObservers(moduleId);
	}

	public long getSelectedModuleId() {
		return selectedModuleId;
	}
	
	public void addLayer(long selectedModuleId, String layerName, String layerDescription, int hierarchicalLevel){
		logger.info("Adding layer " + layerName);
		try {
			JPanelStatus.getInstance("Adding Layer").start();
			Layer newLayer = new Layer(layerName, layerDescription, hierarchicalLevel);
			this.passModuleToService(selectedModuleId, newLayer);
		} catch (Exception e) {
			logger.error("addLayer(" + layerName + ") - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}
	
	public void addModule(long selectedModuleId, String moduleName, String moduleDescription){
		logger.info("Adding module " + moduleName);
		try {
			JPanelStatus.getInstance("Adding module").start();
			Module newModule = new Module(moduleName, moduleDescription);
			this.passModuleToService(selectedModuleId, newModule);
		} catch (Exception e) {
			logger.error("addModule(" + moduleName + ") - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}
	
	public void addComponent(long selectedModuleId, String componentName, String componentDescription){
		logger.info("Adding component " + componentName);
		try {
			JPanelStatus.getInstance("Adding component").start();
			Component newComponent = new Component(componentName, componentDescription);
			this.passModuleToService(selectedModuleId, newComponent);
		} catch (Exception e) {
			logger.error("addComponent(" + componentName + ") - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}
	
	public void addExternalLibrary(long selectedModuleId, String libraryName, String libraryDescription){
		logger.info("Adding external library " + libraryName);
		try {
			JPanelStatus.getInstance("Adding external library").start();
			ExternalLibrary newComponent = new ExternalLibrary(libraryName, libraryDescription);
			this.passModuleToService(selectedModuleId, newComponent);
		} catch (Exception e) {
			logger.error("addExternalLibrary(" + libraryName + ") - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}
	
	private void passModuleToService(long selectedModuleId, Module module) {
		if(selectedModuleId == -1) {
			this.moduleService.addModuleToRoot(module);
		} else {
			logger.debug("Adding child");
			this.moduleService.addModuleToParent(selectedModuleId, module);
		}
		this.notifyObservers();
	}

	/**
	 * Remove a module by Id
	 */
	public void removeModuleById(long moduleId) {
		logger.info("Removing module by Id " + moduleId);
		try {
			JPanelStatus.getInstance("Removing Module").start();
			this.moduleService.removeModuleById(moduleId);
			this.setSelectedModuleId(-1);
			this.notifyObservers();
		} catch (Exception e) {
			logger.error("removeModuleById(" + moduleId + ") - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}
	
	public void moveLayerUp(long layerId) {
		logger.info("Moving layer up");
		try {
			if (layerId != -1) {
				JPanelStatus.getInstance("Moving layer up").start();
				this.moduleService.moveLayerUp(layerId);
				this.notifyObservers();
			}
		} catch (Exception e) {
			logger.error("moveLayerUp() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}
	
	public void moveLayerDown(long layerId) {
		logger.info("Moving layer down");
		try {
			if (layerId != -1) {
				JPanelStatus.getInstance("Moving layer down").start();
				this.moduleService.moveLayerDown(layerId);
				this.notifyObservers();
			}
		} catch (Exception e) {
			logger.error("moveLayerDown() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}

	/**
	 * Remove the selected software unit
	 */
	public void removeSoftwareUnit(String softwareUnitName) {
		logger.info("Removing software unit " + softwareUnitName);
		try {
			long moduleId = getSelectedModuleId();

			if (moduleId != -1 && softwareUnitName != null && !softwareUnitName.equals("")) {
				// Ask the user if he is sure to remove the software unit
				boolean confirm = UiDialogs.confirmDialog(definitionJPanel, "Are you sure you want to remove software unit: \"" + softwareUnitName + "\"", "Remove?");
				if (confirm) {
					// Remove the software unit
					JPanelStatus.getInstance("Removing software unit").start();
					this.softwareUnitDefinitionDomainService.removeSoftwareUnit(moduleId, softwareUnitName);
					// Update the software unit table
					this.notifyObservers();
				}
			}
		} catch (Exception e) {
			logger.error("removeSoftwareUnit() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}
	
	public void removeRule(long appliedRuleId) {
		logger.info("Removing rule " + appliedRuleId);
		try {
			long moduleId = getSelectedModuleId();
//			int appliedRuleId = (int)definitionJPanel.getSelectedAppliedRule();

			if (moduleId != -1 && appliedRuleId != -1L) {
				// Ask the user if he is sure to remove the software unit
				boolean confirm = UiDialogs.confirmDialog(definitionJPanel, "Are you sure you want to remove the applied rule: \"" + this.appliedRuleService.getRuleTypeByAppliedRule(appliedRuleId) + "\"", "Remove?");
				if (confirm) {
					// Remove the software unit
					JPanelStatus.getInstance("Removing applied rule").start();
					this.appliedRuleService.removeAppliedRule(appliedRuleId);

					// Update the applied rules table
					this.notifyObservers();
				}
			}
		} catch (Exception e) {
			logger.error("removeRule() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}

	/**
	 * Function which will save the name and description changes to the module
	 */
	public void updateModule(String moduleName, String moduleDescription) {
		logger.info("Updating module " + moduleName);
		try {
			JPanelStatus.getInstance("Saving layer").start();
			long moduleId = getSelectedModuleId();
			if (moduleId != -1) {
				this.moduleService.updateModule(moduleId, moduleName, moduleDescription);
			}
			this.notifyObservers();
		} catch (Exception e) {
			logger.error("updateModule() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}
	
	public AbstractDefineComponent getModuleTreeComponents() {
//		logger.info("getting Module Tree Components");
		JPanelStatus.getInstance("Updating Modules").start();
		
		SoftwareArchitectureComponent rootComponent = new SoftwareArchitectureComponent();
		ArrayList<Module> modules = this.moduleService.getSortedModules();
		for (Module module : modules) {
			this.addChildComponents(rootComponent, module);
		}

		JPanelStatus.getInstance().stop();
		return rootComponent;
	}
	
	private void addChildComponents(AbstractDefineComponent parentComponent, Module module) {
		AbstractDefineComponent childComponent = DefineComponentFactory.getDefineComponent(module);
		for(Module subModule : module.getSubModules()) {
			this.addChildComponents(childComponent, subModule);
		}
		parentComponent.addChild(childComponent);
	}

	
	public String getModuleName(long moduleId){
		String moduleName = "Root";
		if (this.getSelectedModuleId() != -1){
			moduleName = this.moduleService.getModuleNameById(this.getSelectedModuleId());
		}
		return moduleName;
	}
	
	/**
	 * This function will return a hash map with the details of the requested module.
	 */
	public HashMap<String, Object> getModuleDetails(long moduleId) {
		HashMap<String, Object> moduleDetails = new HashMap<String, Object>();
		logger.info("loading Module Detail " + moduleId);

		if (moduleId != -1) {
			try {
				Module module = this.moduleService.getModuleById(moduleId);
				moduleDetails.put("id", module.getId());
				moduleDetails.put("name", module.getName());
				moduleDetails.put("description", module.getDescription());
				moduleDetails.put("type", module.getType());
				
			} catch (Exception e) {
				logger.error("getModuleDetails() - exception: " + e.getMessage());
				UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
			}
		}
		return moduleDetails;
	}
	
	/**
	 * #FIXME:: TASK SHOULD NOT CALL VIEW
	 */
	@Deprecated
	public void updateSoftwareUnitTable(JTableSoftwareUnits softwareUnitsTable) {
		try {
			long layerId = getSelectedModuleId();
			JPanelStatus.getInstance("Updating software unit table").start();
			if (layerId != -1) {

				// Get all components from the service
				ArrayList<String> softwareUnitNames = this.softwareUnitDefinitionDomainService.getSoftwareUnitNames(layerId);

				// Get the tablemodel from the table
				JTableTableModel atm = (JTableTableModel) softwareUnitsTable.getModel();//definitionJPanel.sofwareUnitsPanel.getModel();

				// Remove all items in the table
				atm.getDataVector().removeAllElements();
				
				if (softwareUnitNames != null) {
					for (String softwareUnitName : softwareUnitNames) {
						String softwareUnitType = this.softwareUnitDefinitionDomainService.getSoftwareUnitType(softwareUnitName);
						Object rowdata[] = {softwareUnitName, softwareUnitType};
						
						atm.addRow(rowdata);
					}
				}
				atm.fireTableDataChanged();
			}
		} catch (Exception e) {
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error!");
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}
	

	/**
	 * #FIXME:: TASK SHOULD NOT CALL VIEW
	 */
	@Deprecated
	public void updateAppliedRulesTable(JTableAppliedRule appliedRuleTable) {
		try {
			long layerId = getSelectedModuleId();
			JPanelStatus.getInstance("Updating rules applied table").start();
			if (layerId != -1) {

				// Get all applied rules from the service
				ArrayList<Long> appliedRulesIds = this.appliedRuleService.getAppliedRulesIdsByModule(layerId);

				// Get the tablemodel from the table
				JTableTableModel atm = (JTableTableModel) appliedRuleTable.getModel();//definitionJPanel.appliedRulesPanel.getModel();//jTableAppliedRules.getModel();

				// Remove all items in the table
				atm.getDataVector().removeAllElements();
				if (appliedRulesIds != null) {
					for (long appliedRuleId : appliedRulesIds) {
						String ruleTypeKey = this.appliedRuleService.getRuleTypeByAppliedRule(appliedRuleId);
						DataHelper datahelper = new DataHelper();
						datahelper.setId(appliedRuleId);
						datahelper.setValue(ruleTypeKey);

						// To layer
						long toLayerId = this.appliedRuleService.getModuleToIdOfAppliedRule(appliedRuleId);
						String moduleNameTo = this.moduleService.getModuleNameById(toLayerId);
						// Is enabled
						boolean appliedRuleIsEnabled = this.appliedRuleService.getAppliedRuleIsEnabled(appliedRuleId);
						String enabled = "Off";
						if (appliedRuleIsEnabled) {
							enabled = "On";
						}
						// Number of exceptions
						ArrayList<Long> appliedRulesExceptionIds = this.appliedRuleExceptionService.getExceptionIdsByAppliedRule(appliedRuleId);
						int numberofexceptions = appliedRulesExceptionIds.size();

						Object rowdata[] = { appliedRuleId, ruleTypeKey, moduleNameTo , enabled, numberofexceptions };

						atm.addRow(rowdata);
					}
				}
				atm.fireTableDataChanged();
			}
		} catch (Exception e) {
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error!");
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}
	
	public void update(Observable o, Object arg) {
		logger.info("update(" + o + ", " + arg + ")");
		long moduleId = getSelectedModuleId();
		notifyObservers(moduleId);
	}
	
	@Override
	public void notifyObservers(){
		long moduleId = getSelectedModuleId();
		for (Observer o : this.observers){
			o.update(this, moduleId);
		}
	}
	
	/**
	 * This function will load notify all to update their data
	 */
	public void notifyObservers(long moduleId){
		for (Observer o : this.observers){
			o.update(this, moduleId);
		}
	}
	
	public void addObserver(Observer o){
		if (!this.observers.contains(o)){
			this.observers.add(o);
		}
	}
	
	public void removeObserver(Observer o){
		if (this.observers.contains(o)){
			this.observers.remove(o);
		}
	}
}
