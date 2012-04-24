package husacct.define.task;

import husacct.define.domain.DefineDomainService;
import husacct.define.domain.module.Module;
import husacct.define.presentation.helper.DataHelper;
import husacct.define.presentation.jpanel.DefinitionJPanel;
import husacct.define.presentation.tables.JTableSoftwareUnits;
import husacct.define.presentation.tables.JTableTableModel;
import husacct.define.presentation.utils.JPanelStatus;
import husacct.define.presentation.utils.Log;
import husacct.define.presentation.utils.UiDialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;

public class DefinitionController extends Observable implements ActionListener, KeyListener, Observer {

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
		
//		// Set actionlisteners to buttons, lists etc.
//		definitionJPanel.jListLayers.addListSelectionListener(this);
//		definitionJPanel.jButtonNewLayer.addActionListener(this);
//		definitionJPanel.jButtonRemoveLayer.addActionListener(this);
//		definitionJPanel.jButtonMoveLayerUp.addActionListener(this);
//		definitionJPanel.jButtonMoveLayerDown.addActionListener(this);
//
//		definitionJPanel.jTextFieldLayerName.addKeyListener(this);
//		definitionJPanel.jTextAreaLayerDescription.addKeyListener(this);
//		definitionJPanel.jCheckBoxAccess.addActionListener(this);
//
//		definitionJPanel.jButtonAddSoftwareUnit.addActionListener(this);
////		definitionJPanel.jButtonEditSoftwareUnit.addActionListener(this);
//		definitionJPanel.jButtonRemoveSoftwareUnit.addActionListener(this);
//
//		definitionJPanel.jButtonAddRule.addActionListener(this);
//		definitionJPanel.jButtonEditRule.addActionListener(this);
//		definitionJPanel.jButtonRemoveRule.addActionListener(this);

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
			notifyObservers();
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
			notifyObservers();
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
				notifyObservers();
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
				notifyObservers();
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
	private void removeSoftwareUnit(String softwareUnitName) {
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
					notifyObservers();
				}
			}
		} catch (Exception e) {
			Log.e(this, "DefinitionController - removeSoftwareUnit() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}

	private void createNewRuleGUI() {
		Log.i(this, "DefinitionController - createNewRuleGUI()");

		try {
			long layerId = definitionJPanel.getSelectedModule();

			if (layerId != -1) {
				// Create a new software unit controller
				AppliedRulesController a = new AppliedRulesController(layerId, -1L);
				// Set the action of the view
				a.setAction(PopUpController.ACTION_NEW);
				a.addObserver(this);
				// Build and show the ui
				a.initUi();
			}
		} catch (Exception e) {
			Log.e(this, "DefinitionController - createNewRuleGUI() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		}
	}

	private void editRuleGUI(long appliedRuleId) {
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

	private void removeRuleToModule(long appliedRuleId) {
		Log.i(this, "DefinitionController - removeRuleToModule()");
		try {
			long layerId = definitionJPanel.modulePanel.getSelectedModuleId();
//			int appliedRuleId = (int)definitionJPanel.getSelectedAppliedRule();

			if (layerId != -1 && appliedRuleId != -1L) {
				// Ask the user if he is sure to remove the software unit
				boolean confirm = UiDialogs.confirmDialog(definitionJPanel, "Are you sure you want to remove the applied rule: \"" + DefineDomainService.getInstance().getRuleTypeByAppliedRule(appliedRuleId) + "\"", "Remove?");
				if (confirm) {
					// Remove the software unit
					JPanelStatus.getInstance("Removing applied rule").start();
					DefineDomainService.getInstance().removeAppliedRule(appliedRuleId);

					// Update the applied rules table
					notifyObservers();
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
	private void updateModule(long moduleId, String moduleName, String moduleDescription) {
		Log.i(this, "DefinitionController - updateLayer()");
		try {
			JPanelStatus.getInstance("Saving layer").start();

			if (moduleId != -1) {
				DefineDomainService.getInstance().updateModule(moduleId, moduleName, moduleDescription);
			}
		} catch (Exception e) {
			Log.e(this, "DefinitionController - updateLayer() - exception: " + e.getMessage());
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
	
	public void updateModuleDetails(HashMap<String, Object> moduleDetails) {
		Log.i(this, "DefinitionController - updateModuleDetails()");
		Long moduleId = (Long)moduleDetails.get("id");
		String moduleName = (String) moduleDetails.get("name");
		String moduleDescription = (String) moduleDetails.get("description");
		
		DefineDomainService.getInstance().updateModule(moduleId, moduleName, moduleDescription);
	}

	/**
	 * This method will check if the ui elements should be enabled or disabled
	 */
	private void enablePanel(long moduleId) {
		Log.i(this, "DefinitionController - enablePanel()");
		boolean enabled;
		if (moduleId == -1) {
			Log.i(this, "DefinitionController - enablePanel() - false");
			enabled = false;
		} else {
			Log.i(this, "DefinitionController - enablePanel() - true");
			enabled = true;
		}
		// Buttons, textfields, tables etc.
//		definitionJPanel.jTextFieldModuleName.setEnabled(enabled);
//		definitionJPanel.jTextAreaLayerDescription.setEnabled(enabled);
//		definitionJPanel.jCheckBoxAccess.setEnabled(enabled);
//		definitionJPanel.jButtonAddSoftwareUnit.setEnabled(enabled);
////		definitionJPanel.jButtonEditSoftwareUnit.setEnabled(enabled);
//		definitionJPanel.jButtonRemoveSoftwareUnit.setEnabled(enabled);
//		definitionJPanel.jTableSoftwareUnits.setEnabled(enabled);
//		definitionJPanel.jTableAppliedRules.setEnabled(enabled);
//		definitionJPanel.jButtonAddRule.setEnabled(enabled);
//		definitionJPanel.jButtonEditRule.setEnabled(enabled);
//		definitionJPanel.jButtonRemoveRule.setEnabled(enabled);
//		definitionJPanel.jButtonMoveLayerUp.setEnabled(enabled);
//		definitionJPanel.jButtonMoveLayerDown.setEnabled(enabled);
//		definitionJPanel.jButtonRemoveModule.setEnabled(enabled);

//		// Enable or disable menu items
//		if (!definitionService.hasArchitectureDefinition()) {
//			definitionJPanel.jButtonNewLayer.setEnabled(false);
//			ApplicationController.getInstance().jframe.jMenuItemSaveArchitecture.setEnabled(false);
//			ApplicationController.getInstance().jframe.jMenuItemStartAnalyse.setEnabled(false);
//			ApplicationController.getInstance().jframe.jMenuItemCheckDependencies.setEnabled(false);
//		} else {
//			definitionJPanel.jButtonNewLayer.setEnabled(true);
//			ApplicationController.getInstance().jframe.jMenuItemSaveArchitecture.setEnabled(true);
//			ApplicationController.getInstance().jframe.jMenuItemStartAnalyse.setEnabled(true);
//			ApplicationController.getInstance().jframe.jMenuItemCheckDependencies.setEnabled(true);
//		}
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
			if (layerId != -1) {
				JPanelStatus.getInstance("Updating software unit table").start();

				// Get all components from the service
				ArrayList<String> softwareUnitNames = DefineDomainService.getInstance().getSoftwareUnitNames(layerId);

				// Get the tablemodel from the table
				JTableTableModel atm = (JTableTableModel) definitionJPanel.sofwareUnitsPanel.getModel();//jTableSoftwareUnits.getModel();

				// Remove all items in the table
				atm.getDataVector().removeAllElements();
				
				if (softwareUnitNames != null) {
					for (String softwareUnitName : softwareUnitNames) {
//						DataHelper datahelper = new DataHelper();
//						datahelper.setId(softwareUnit_id);
						//datahelper.setValue(DefineDomainService.getInstance().getSoftwareUnitName(layerId, softwareUnit_id));

						// Number of exceptions
//						ArrayList<Long> softwareUnitExceptions = DefineDomainService.getInstance().getSoftwareUnitExceptions(layerId, softwareUnit_id);
//						int numberofexceptions = 0;
//						if (softwareUnitExceptions != null) {
//							numberofexceptions = softwareUnitExceptions.size();
//						}

//						Object rowdata[] = { datahelper, DefineDomainService.getInstance().getSoftwareUnitType(layerId, softwareUnit_id), numberofexceptions };
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

	@Deprecated
	private void updateAppliedRulesTable() {
		Log.i(this, "updateAppliedRulesTable()");
		 //Applied rules still have to get implemented
		try {
			long layerId = definitionJPanel.getSelectedModule();

			if (layerId != -1) {
				JPanelStatus.getInstance("Updating rules applied table").start();

				// Get all applied rules from the service
				ArrayList<Long> appliedRulesIds = DefineDomainService.getInstance().getAppliedRulesIdsByModule(layerId);

				// Get the tablemodel from the table
				JTableTableModel atm = (JTableTableModel) definitionJPanel.appliedRulesPanel.getModel();//jTableAppliedRules.getModel();

				// Remove all items in the table
				atm.getDataVector().removeAllElements();
				if (appliedRulesIds != null) {
					for (long appliedRuleId : appliedRulesIds) {
						DataHelper datahelper = new DataHelper();
						datahelper.setId(appliedRuleId);
						datahelper.setValue(DefineDomainService.getInstance().getRuleTypeByAppliedRule(appliedRuleId));

						// To layer
						long toLayerId = DefineDomainService.getInstance().getModuleToIdOfAppliedRule(appliedRuleId);

						// Is enabled
						boolean appliedRuleIsEnabled = DefineDomainService.getInstance().getAppliedRuleIsEnabled(appliedRuleId);
						String enabled = "Off";
						if (appliedRuleIsEnabled) {
							enabled = "On";
						}
						// Number of exceptions
						ArrayList<Long> appliedRulesExceptionIds = DefineDomainService.getInstance().getExceptionIdsByAppliedRule(appliedRuleId);
						int numberofexceptions = appliedRulesExceptionIds.size();

						Object rowdata[] = { datahelper, DefineDomainService.getInstance().getModuleNameById(toLayerId), enabled, numberofexceptions };

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

	public void actionPerformed(ActionEvent action) {
//		if (action.getSource() == definitionJPanel.jButtonNewModule) {
//			newModule();
//		} else if (action.getSource() == definitionJPanel.jButtonRemoveModule) {
//			removeModule();
//		} else if (action.getSource() == definitionJPanel.jButtonMoveLayerUp) {
//			moveLayerUp();
//		} else if (action.getSource() == definitionJPanel.jButtonMoveLayerDown) {
//			moveLayerDown();
//		} else if (action.getSource() == definitionJPanel.jButtonAddSoftwareUnit) {
//			addSoftwareUnit();
//		} else if (action.getSource() == definitionJPanel.jButtonRemoveSoftwareUnit) {
//			removeSoftwareUnit();
//		} else if (action.getSource() == definitionJPanel.jButtonAddRule) {
//			addRuleToModule();
//		} else if (action.getSource() == definitionJPanel.jButtonEditRule) {
//			editRuleToModule();
//		} else if (action.getSource() == definitionJPanel.jButtonRemoveRule) {
//			removeRuleToModule();
//		} else if (action.getSource() == definitionJPanel.jCheckBoxAccess) {
//			updateModule();
//		} else {
			Log.i(this, "actionPerformed(" + action + ") + this should NOT happen!!!!");
//		}
	}
	
	public void update(Observable o, Object arg) {
		Log.i(this, "update(" + o + ", " + arg + ")");
		notifyObservers();
	}
	
	/**
	 * This function will load notify all to update their data
	 */
	public void notifyObservers(long moduleId){
		for (Observer o : observers){
			o.update(this, moduleId);
		}
	}
	
	
	public void keyPressed(KeyEvent arg0) {
		// Ignore
	}

	public void keyReleased(KeyEvent arg0) {
		Log.i(this, "keyReleased(" + arg0 + ")");

//		if (arg0.getSource() == definitionJPanel.jTextFieldModuleName || arg0.getSource() == definitionJPanel.jTextAreaLayerDescription) {
//			updateModule();
//		} else {
//			Log.i(this, "keyReleased(" + arg0 + ")");
//		}
	}

	public void keyTyped(KeyEvent arg0) {
		// Ignore
	}
	
	public void addObserver(Observer o){
		if (!observers.contains(o)){
			observers.add(o);
		}
	}
	
	public void removeObserver(Observer o){
		if (observers.contains(o)){
			observers.remove(o);
		}
	}
}
