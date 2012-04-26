package husacct.define.task;

import husacct.define.domain.DefineDomainService;
import husacct.define.domain.module.Module;
import husacct.define.presentation.helper.DataHelper;
import husacct.define.presentation.jpanel.DefinitionJPanel;
import husacct.define.presentation.tables.JTableAppliedRule;
import husacct.define.presentation.tables.JTableSoftwareUnits;
import husacct.define.presentation.tables.JTableTableModel;
import husacct.define.presentation.utils.JPanelStatus;
import husacct.define.presentation.utils.Log;
import husacct.define.presentation.utils.UiDialogs;
import husacct.define.task.components.AbstractDefineComponent;
import husacct.define.task.components.LayerComponent;
import husacct.define.task.components.SoftwareArchitectureComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;

public class DefinitionController extends Observable implements Observer {

	private DefinitionJPanel definitionJPanel;
	private static DefinitionController instance;
	private List<Observer> observers;
	
	public static DefinitionController getInstance() {
		return instance == null ? (instance = new DefinitionController()) : instance;
	}

	public DefinitionController() {
		Log.i(this, "constructor()");
		observers = new ArrayList<Observer>();
	}
	
	public void initSettings() {
		observers.clear();
		definitionJPanel = new DefinitionJPanel();
	}

	/**
	 * Init the user interface for creating/editting the definition.
	 * 
	 * @return JPanel The jpanel
	 */
	public JPanel initUi() {
		Log.i(this, "initUi()");
		// Return the definition jpanel
		return definitionJPanel;
	}

	/**
	 * Adds a new layer
	 */
	public void addLayer(String layerName, int hierarchicalLevel){
		Log.i(this, "DefinitionController - addLayer("+layerName+")");
		try {
			JPanelStatus.getInstance("Adding Layer").start();
			DefineDomainService.getInstance().addLayer(layerName, hierarchicalLevel);
			this.notifyObservers();
		} catch (Exception e) {
			Log.e(this, "DefinitionController - addLayer("+layerName+") - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}

	/**
	 * Remove a module by Id
	 */
	public void removeModuleById(long moduleId) {
		Log.i(this, "DefinitionController - removeModuleById("+moduleId+")");
		try {
			JPanelStatus.getInstance("Removing Module").start();
			DefineDomainService.getInstance().removeModuleById(moduleId);
			this.notifyObservers();
		} catch (Exception e) {
			Log.e(this, "DefinitionController - removeModuleById("+moduleId+") - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}

	/**
	 * Move a layer one up in hierarchy
	 */
	public void moveLayerUp(long layerId) {
		Log.i(this, "DefinitionController - moveLayerUp()");
		try {
			if (layerId != -1) {
				JPanelStatus.getInstance("Moving layer up").start();
				DefineDomainService.getInstance().moveLayerUp(layerId);
				this.notifyObservers();
			}
		} catch (Exception e) {
			Log.e(this, "DefinitionController - moveLayerUp() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}

	/**
	 * Move a layer one down in hierarchy
	 */
	public void moveLayerDown(long layerId) {
		Log.i(this, "DefinitionController - moveLayerDown()");
		try {
			if (layerId != -1) {
				JPanelStatus.getInstance("Moving layer down").start();
				DefineDomainService.getInstance().moveLayerDown(layerId);
				this.notifyObservers();
			}
		} catch (Exception e) {
			Log.e(this, "DefinitionController - moveLayerDown() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}

	/**
	 * Add a new software unit to the selected module. This method will make pop-up a new jframe who will handle everything for creating a new sotware unit.
	 */
	public void createSoftwareUnitGUI() {
		Log.i(this, "DefinitionController - createSoftwareUnitGUI()");
		try {
			long moduleId = definitionJPanel.modulePanel.getSelectedModuleId();
			Log.i(this, "DefinitionController - createSoftwareUnitGUI() - ModuleId: " + moduleId);
			if (moduleId != -1) {
				// Create a new software unit controller
				SoftwareUnitController c = new SoftwareUnitController(moduleId, "");
				// Set the action of the view
				c.setAction(PopUpController.ACTION_NEW);
				c.addObserver(this);
				// Build and show the ui
				c.initUi();
			}
		} catch (Exception e) {
			Log.e(this, "DefinitionController - createSoftwareUnitGUI() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		}
	}

	/**
	 * Remove the selected software unit
	 */
	public void removeSoftwareUnit(String softwareUnitName) {
		Log.i(this, "DefinitionController - removeSoftwareUnit()");
		try {
			long moduleId = definitionJPanel.modulePanel.getSelectedModuleId();

			if (moduleId != -1 && softwareUnitName != null && !softwareUnitName.equals("")) {
				// Ask the user if he is sure to remove the software unit
				boolean confirm = UiDialogs.confirmDialog(definitionJPanel, "Are you sure you want to remove software unit: \"" + softwareUnitName + "\"", "Remove?");
				if (confirm) {
					// Remove the software unit
					JPanelStatus.getInstance("Removing software unit").start();
					DefineDomainService.getInstance().removeSoftwareUnit(moduleId, softwareUnitName);
					// Update the software unit table
					this.notifyObservers();
				}
			}
		} catch (Exception e) {
			Log.e(this, "DefinitionController - removeSoftwareUnit() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}

	public void createRuleGUI() {
		Log.i(this, "DefinitionController - createRuleGUI()");

		try {
			long moduleId = definitionJPanel.modulePanel.getSelectedModuleId();

			if (moduleId != -1) {
				// Create a new software unit controller
				AppliedRulesController a = new AppliedRulesController(moduleId, -1L);
				// Set the action of the view
				a.setAction(PopUpController.ACTION_NEW);
				a.addObserver(this);
				// Build and show the ui
				a.initUi();
			}
		} catch (Exception e) {
			Log.e(this, "DefinitionController - createRuleGUI() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		}
	}

	public void createRuleGUI(long appliedRuleId) {
		Log.i(this, "DefinitionController - EditRuleGUI()");

		try {
			long moduleId = definitionJPanel.modulePanel.getSelectedModuleId();

			if (moduleId != -1 && appliedRuleId != -1L) {
				// Create a new software unit controller
				AppliedRulesController a = new AppliedRulesController(moduleId, appliedRuleId);
				// Set the action of the view
				a.setAction(PopUpController.ACTION_EDIT);
				a.addObserver(this);
				// Build and show the ui
				a.initUi();
			} else {
				Log.e(this, "DefinitionController - EditRuleGUI() - no applied rule selected");
				UiDialogs.errorDialog(definitionJPanel, "Select an applied rule", "Error");
			}
		} catch (Exception e) {
			Log.e(this, "DefinitionController - EditRuleGUI() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		}
	}

	public void removeRule(long appliedRuleId) {
		Log.i(this, "DefinitionController - removeRuleToModule()");
		try {
			long moduleId = definitionJPanel.modulePanel.getSelectedModuleId();
//			int appliedRuleId = (int)definitionJPanel.getSelectedAppliedRule();

			if (moduleId != -1 && appliedRuleId != -1L) {
				// Ask the user if he is sure to remove the software unit
				boolean confirm = UiDialogs.confirmDialog(definitionJPanel, "Are you sure you want to remove the applied rule: \"" + DefineDomainService.getInstance().getRuleTypeByAppliedRule(appliedRuleId) + "\"", "Remove?");
				if (confirm) {
					// Remove the software unit
					JPanelStatus.getInstance("Removing applied rule").start();
					DefineDomainService.getInstance().removeAppliedRule(appliedRuleId);

					// Update the applied rules table
					this.notifyObservers();
				}
			}
		} catch (Exception e) {
			Log.e(this, "DefinitionController - removeRuleToLayer() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}

	/**
	 * Function which will save the name and description changes to the module
	 */
	public void updateModule(String moduleName, String moduleDescription) {
		Log.i(this, "DefinitionController - updateModule()");
		try {
			JPanelStatus.getInstance("Saving layer").start();
			long moduleId = definitionJPanel.modulePanel.getSelectedModuleId();
			if (moduleId != -1) {
				DefineDomainService.getInstance().updateModule(moduleId, moduleName, moduleDescription);
			}
			this.notifyObservers();
		} catch (Exception e) {
			Log.e(this, "DefinitionController - updateModule() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}

	public void updateModuleTreeList(JList moduleTreeList) {
		Log.i(this, "DefinitionController - updateModuleList()");

		JPanelStatus.getInstance("Updating modules").start();

		ArrayList<Long> moduleIds = DefineDomainService.getInstance().getLayerIdsSorted();
		DefaultListModel listModule = (DefaultListModel) moduleTreeList.getModel();

		listModule.removeAllElements();
		if (moduleIds != null) {
			for (Long moduleId : moduleIds) {
				DataHelper datahelper = new DataHelper();
				datahelper.setId(moduleId);
				try {
					datahelper.setValue(DefineDomainService.getInstance().getModuleNameById(moduleId));
				} catch (Exception e) {
					Log.e(this, "DefinitionController - updateModule() - exception: " + e.getMessage());
					UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
				}
				listModule.addElement(datahelper);
			}
		}
		//enablePanel();
		JPanelStatus.getInstance().stop();
	}
	
	public AbstractDefineComponent getRootComponent() {
		Log.i(this, "update Module Tree");
		JPanelStatus.getInstance("Updating modules").start();
		
		
		SoftwareArchitectureComponent rootComponent = new SoftwareArchitectureComponent();
		
		ArrayList<Long> moduleIds = DefineDomainService.getInstance().getLayerIdsSorted();
		if(moduleIds != null) {
			for (Long moduleId : moduleIds) {
				LayerComponent layerComponent = new LayerComponent();
				layerComponent.setHierarchicalLevel(moduleId);
				try {
					layerComponent.setName(DefineDomainService.getInstance().getModuleNameById(moduleId));
				} catch (Exception e) {
					Log.e(this, "updateModule() - exception: " + e.getMessage());
					UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
				}
				rootComponent.addChild(layerComponent);
			}
		}
		
		
		
		JPanelStatus.getInstance().stop();
		return rootComponent;
	}

	/**
	 * This function will return a hashmap with the details of the requested module.
	 */
	public HashMap<String, Object> getModuleDetails(long layerId) {
		HashMap<String, Object> moduleDetails = new HashMap<String, Object>();
		Log.i(this, "DefinitionController - loadModuleDetails()");

		if (layerId != -1) {
			try {
				//TODO isolate domain classes
				Module module = DefineDomainService.getInstance().getModuleById(layerId);
				moduleDetails.put("id", module.getId());
				moduleDetails.put("name", module.getName());
				moduleDetails.put("description", module.getDescription());
				moduleDetails.put("type", module.getType());
				
			} catch (Exception e) {
				Log.e(this, "DefinitionController - loadModuleDetails() - exception: " + e.getMessage());
				UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
			}
		}
		return moduleDetails;
	}
	
	/**
	 * This method updates the component table in the jpanel
	 * @param softwareUnitsTable 
	 * 
	 * @param layer
	 */
	public void updateSoftwareUnitTable(JTableSoftwareUnits softwareUnitsTable) {
		Log.i(this, "DefinitionController - updateSoftwareUnitTable()");
		try {
			long layerId = definitionJPanel.modulePanel.getSelectedModuleId();
			JPanelStatus.getInstance("Updating software unit table").start();
			if (layerId != -1) {

				// Get all components from the service
				ArrayList<String> softwareUnitNames = DefineDomainService.getInstance().getSoftwareUnitNames(layerId);

				// Get the tablemodel from the table
				JTableTableModel atm = (JTableTableModel) softwareUnitsTable.getModel();//definitionJPanel.sofwareUnitsPanel.getModel();

				// Remove all items in the table
				atm.getDataVector().removeAllElements();
				
				if (softwareUnitNames != null) {
					for (String softwareUnitName : softwareUnitNames) {
						String softwareUnitType = DefineDomainService.getInstance().getSoftwareUnitType(softwareUnitName);
						Object rowdata[] = {softwareUnitName, softwareUnitType};
						
						atm.addRow(rowdata);
					}
				}
				atm.fireTableDataChanged();
			}
		} catch (Exception e) {
			Log.e(this, "DefinitionController - updateSoftwareUnitTable() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error!");
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}

	public void updateAppliedRulesTable(JTableAppliedRule appliedRuleTable) {
		Log.i(this, "updateAppliedRulesTable()");
		try {
			long layerId = definitionJPanel.modulePanel.getSelectedModuleId();
			JPanelStatus.getInstance("Updating rules applied table").start();
			if (layerId != -1) {

				// Get all applied rules from the service
				ArrayList<Long> appliedRulesIds = DefineDomainService.getInstance().getAppliedRulesIdsByModule(layerId);

				// Get the tablemodel from the table
				JTableTableModel atm = (JTableTableModel) appliedRuleTable.getModel();//definitionJPanel.appliedRulesPanel.getModel();//jTableAppliedRules.getModel();

				// Remove all items in the table
				atm.getDataVector().removeAllElements();
				if (appliedRulesIds != null) {
					for (long appliedRuleId : appliedRulesIds) {
						String ruleTypeKey = DefineDomainService.getInstance().getRuleTypeByAppliedRule(appliedRuleId);
						DataHelper datahelper = new DataHelper();
						datahelper.setId(appliedRuleId);
						datahelper.setValue(ruleTypeKey);

						// To layer
						long toLayerId = DefineDomainService.getInstance().getModuleToIdOfAppliedRule(appliedRuleId);
						String moduleNameTo = DefineDomainService.getInstance().getModuleNameById(toLayerId);
						// Is enabled
						boolean appliedRuleIsEnabled = DefineDomainService.getInstance().getAppliedRuleIsEnabled(appliedRuleId);
						String enabled = "Off";
						if (appliedRuleIsEnabled) {
							enabled = "On";
						}
						// Number of exceptions
						ArrayList<Long> appliedRulesExceptionIds = DefineDomainService.getInstance().getExceptionIdsByAppliedRule(appliedRuleId);
						int numberofexceptions = appliedRulesExceptionIds.size();

						Object rowdata[] = { appliedRuleId, ruleTypeKey, moduleNameTo , enabled, numberofexceptions };

						atm.addRow(rowdata);
					}
				}
				atm.fireTableDataChanged();
			}
		} catch (Exception e) {
			Log.e(this, "updateAppliedRulesTable() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error!");
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}
	
	public void update(Observable o, Object arg) {
		Log.i(this, "update(" + o + ", " + arg + ")");
		long moduleId = definitionJPanel.modulePanel.getSelectedModuleId();
		notifyObservers(moduleId);
	}
	
	@Override
	public void notifyObservers(){
		long moduleId = definitionJPanel.modulePanel.getSelectedModuleId();
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
