package husacct.define.task;

import husacct.define.domain.DefineDomainService;
import husacct.define.domain.module.Module;
import husacct.define.presentation.helper.DataHelper;
import husacct.define.presentation.jpanel.DefinitionJPanel;
import husacct.define.presentation.tables.JTableTableModel;
import husacct.define.presentation.utils.JPanelStatus;
import husacct.define.presentation.utils.Log;
import husacct.define.presentation.utils.UiDialogs;
import husacct.define.task.components.AbstractDefineComponent;
import husacct.define.task.components.LayerComponent;
import husacct.define.task.components.SoftwareArchitectureComponent;

import java.awt.Component;
import java.awt.Container;
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
import javax.swing.JOptionPane;
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
	 * Create a new module
	 */
	public void newModule() {
		Log.i(this, "newModule()");
		try {
			// Ask the user for the module name
			String moduleName = UiDialogs.inputDialog(definitionJPanel, "Please enter module name", "Please input a value", JOptionPane.QUESTION_MESSAGE);
			if (moduleName != null) {
				//Creating a new module of type Layer
				//has yet to be implemented to support other module types
				String layerLevelString = UiDialogs.inputDialog(definitionJPanel, "Please enter layer level", "Please input a value", JOptionPane.QUESTION_MESSAGE);
				if (layerLevelString != null) {
					JPanelStatus.getInstance("Creating new layer").start();
					int layerLevel = Integer.parseInt(layerLevelString);

					Log.i(this, "newLayer() - name: " + moduleName);
					// Create the layer
					long layerId = DefineDomainService.getInstance().addLayer(moduleName, layerLevel);
				}
			}
		} catch (Exception e) {
			Log.e(this, "newLayer() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}

	/**
	 * Remove a layer which is selected in the JPanel.
	 */
	public void removeModule() {
		Log.i(this, "removeModule()");
		try {
			long moduleId = definitionJPanel.getSelectedModule();
			if (moduleId != -1) {
				boolean confirm = UiDialogs.confirmDialog(definitionJPanel, "Are you sure you want to remove module: \"" + moduleId + "\"", "Remove?");
				if (confirm) {
					JPanelStatus.getInstance("Removing module").start();

					DefineDomainService.getInstance().removeModuleById(moduleId);
				}

			}
		} catch (Exception e) {
			Log.e(this, "removeModule() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}

	/**
	 * Move a layer 1 up in hierarchy
	 */
	public void moveLayerUp() {
		Log.i(this, "moveLayerUp()");
		try {
			long layerId = definitionJPanel.getSelectedModule();

			if (layerId != -1) {
				JPanelStatus.getInstance("Moving layer up").start();

				DefineDomainService.getInstance().moveLayerUp(layerId);
			}
		} catch (Exception e) {
			Log.e(this, "moveLayerUp() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}

	/**
	 * Move a layer 1 down in hierarchy
	 */
	public void moveLayerDown() {
		Log.i(this, "moveLayerDown()");
		try {
			long layerId = definitionJPanel.getSelectedModule();

			if (layerId != -1) {
				JPanelStatus.getInstance("Moving layer down").start();

				DefineDomainService.getInstance().moveLayerDown(layerId);
			}
		} catch (Exception e) {
			Log.e(this, "moveLayerDown() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}

	/**
	 * Add a new software unit to the selected module. This method will make pop-up a new jframe who will handle everything for creating a new sotware unit.
	 */
	public void addSoftwareUnit() {
		Log.i(this, "addSoftwareUnit()");
		try {
			long layerId = definitionJPanel.getSelectedModule();

			if (layerId != -1) {
				// Create a new software unit controller
				SoftwareUnitController c = new SoftwareUnitController(layerId, "");
				// Set the action of the view
				c.setAction(PopUpController.ACTION_NEW);
				c.addObserver(this);
				// Build and show the ui
				c.initUi();
			}
		} catch (Exception e) {
			Log.e(this, "addSoftwareUnit() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		}
	}

	/**
	 * Remove the selected software unit from the table
	 */
	private void removeSoftwareUnit() {
		Log.i(this, "removeSoftwareUnit()");
		try {
			long moduleId = definitionJPanel.getSelectedModule();
			String softwareUnitName = definitionJPanel.getSelectedSoftwareUnitName();

			if (moduleId != -1 && softwareUnitName != null && !softwareUnitName.equals("")) {
				// Ask the user if he is sure to remove the software unit
				boolean confirm = UiDialogs.confirmDialog(definitionJPanel, "Are you sure you want to remove software unit: \"" + softwareUnitName + "\"", "Remove?");
				if (confirm) {
					// Remove the software unit
					JPanelStatus.getInstance("Removing software unit").start();
					DefineDomainService.getInstance().removeSoftwareUnit(moduleId, softwareUnitName);
					// Update the software unit table
					updateSoftwareUnitTable();
				}
			}
		} catch (Exception e) {
			Log.e(this, "removeSoftwareUnit() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}

	private void addRuleToModule() {
		Log.i(this, "addRuleToModule()");

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
			Log.e(this, "addRuleToModule() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		}
	}

	private void editRuleToModule() {
		Log.i(this, "editRuleToModule()");

		try {
			long moduleId = definitionJPanel.getSelectedModule();
			long appliedRuleId = definitionJPanel.getSelectedAppliedRule();

			if (moduleId != -1 && appliedRuleId != -1L) {
				// Create a new software unit controller
				AppliedRulesController a = new AppliedRulesController(moduleId, appliedRuleId);
				// Set the action of the view
				a.setAction(PopUpController.ACTION_EDIT);
				a.addObserver(this);
				// Build and show the ui
				a.initUi();
			} else {
				Log.e(this, "editRuleToModule() - no applied rule selected");
				UiDialogs.errorDialog(definitionJPanel, "Select an applied rule", "Error");
			}
		} catch (Exception e) {
			Log.e(this, "editRuleToModule() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		}
	}

	private void removeRuleToModule() {
		Log.i(this, "removeRuleToModule()");
		try {
			long layerId = definitionJPanel.getSelectedModule();
			int appliedRuleId = (int)definitionJPanel.getSelectedAppliedRule();

			if (layerId != -1 && appliedRuleId != -1L) {
				// Ask the user if he is sure to remove the software unit
				boolean confirm = UiDialogs.confirmDialog(definitionJPanel, "Are you sure you want to remove the applied rule: \"" + DefineDomainService.getInstance().getRuleTypeByAppliedRule(appliedRuleId) + "\"", "Remove?");
				if (confirm) {
					// Remove the software unit
					JPanelStatus.getInstance("Removing applied rule").start();
					DefineDomainService.getInstance().removeAppliedRule(appliedRuleId);

					// Update the applied rules table
					updateAppliedRulesTable();
				}
			}
		} catch (Exception e) {
			Log.e(this, "removeRuleToLayer() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}

	/**
	 * Function which will save the name and description changes to the layer
	 */
	private void updateLayer() {
		Log.i(this, "updateLayer()");
		try {
			long layerId = definitionJPanel.getSelectedModule();

			JPanelStatus.getInstance("Saving layer").start();

			if (layerId != -1) {
				DefineDomainService.getInstance().setModuleName(layerId, definitionJPanel.jTextFieldModuleName.getText());

				//To update the layer list: we need to fetch the DataHelper from the list, update it and fire an updateUI to notice that there is an update
				DefaultListModel dlm = (DefaultListModel) definitionJPanel.jListLayers.getModel();				
				for (int i = 0; i < dlm.getSize(); i++) {
					DataHelper datahelper = (DataHelper) dlm.getElementAt(i);
					if (datahelper.getId() == layerId) {
						datahelper.setValue(definitionJPanel.jTextFieldModuleName.getText());
					}
				}
				definitionJPanel.jListLayers.updateUI();
			}
		} catch (Exception e) {
			Log.e(this, "updateLayer() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}

	public void updateModuleTreeList(JList moduleTreeList) {
		Log.i(this, "updateModuleList()");

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
					Log.e(this, "updateModule() - exception: " + e.getMessage());
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
	 * This function will return the layer name, description. Next it will call two methods which will load the two tables.
	 */
	public HashMap<String, Object> getModuleDetails(long layerId) {
		HashMap<String, Object> moduleDetails = new HashMap<String, Object>();
		Log.i(this, "loadModuleDetails()");

		if (layerId != -1) {
			try {
				//TODO isolate domain classes
				Module module = DefineDomainService.getInstance().getModuleById(layerId);
				moduleDetails.put("name", module.getName());
				moduleDetails.put("description", module.getDescription());
				moduleDetails.put("type", module.getType());
				
			} catch (Exception e) {
				Log.e(this, "loadModuleDetails() - exception: " + e.getMessage());
				UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
			}

			//TODO 
//			// Update the tables
//			updateSoftwareUnitTable();
//			updateAppliedRulesTable();

			// Enable or disable the ui elements
//			enablePanel();
		}
		return moduleDetails;
	}
	
	public void updateModuleDetails(HashMap<String, Object> moduleDetails) {
		Log.i(this, "updateModuleDetails()");
		//TODO updateModuleDetails
		//DefineDomainService.getInstance().updateModule()
	}

	/**
	 * This method will check if the ui elements should be enabled or disabled
	 */
	private void enablePanel() {
		Log.i(this, "enablePanel()");

		long layerId = definitionJPanel.getSelectedModule();

		boolean enabled;
		if (layerId == -1) {
			Log.i(this, "enablePanel() - false");
			enabled = false;
		} else {
			Log.i(this, "enablePanel() - true");
			enabled = true;
		}
		// Buttons, textfields, tables etc.
		definitionJPanel.jTextFieldModuleName.setEnabled(enabled);
		definitionJPanel.jTextAreaLayerDescription.setEnabled(enabled);
		definitionJPanel.jCheckBoxAccess.setEnabled(enabled);
		definitionJPanel.jButtonAddSoftwareUnit.setEnabled(enabled);
//		definitionJPanel.jButtonEditSoftwareUnit.setEnabled(enabled);
		definitionJPanel.jButtonRemoveSoftwareUnit.setEnabled(enabled);
		definitionJPanel.jTableSoftwareUnits.setEnabled(enabled);
		definitionJPanel.jTableAppliedRules.setEnabled(enabled);
		definitionJPanel.jButtonAddRule.setEnabled(enabled);
		definitionJPanel.jButtonEditRule.setEnabled(enabled);
		definitionJPanel.jButtonRemoveRule.setEnabled(enabled);
		definitionJPanel.jButtonMoveLayerUp.setEnabled(enabled);
		definitionJPanel.jButtonMoveLayerDown.setEnabled(enabled);
		definitionJPanel.jButtonRemoveModule.setEnabled(enabled);

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
	 * 
	 * @param layer
	 */
	private void updateSoftwareUnitTable() {
		Log.i(this, "updateSoftwareUnitTable()");
		// half implemented
		try {
			long layerId = definitionJPanel.getSelectedModule();

			if (layerId != -1) {
				JPanelStatus.getInstance("Updating software unit table").start();

				// Get all components from the service
				ArrayList<String> softwareUnitNames = DefineDomainService.getInstance().getSoftwareUnitNames(layerId);

				// Get the tablemodel from the table
				JTableTableModel atm = (JTableTableModel) definitionJPanel.jTableSoftwareUnits.getModel();

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
			Log.e(this, "updateSoftwareUnitTable() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error!");
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}

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
				JTableTableModel atm = (JTableTableModel) definitionJPanel.jTableAppliedRules.getModel();

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
		if (action.getSource() == definitionJPanel.jButtonNewModule) {
			newModule();
		} else if (action.getSource() == definitionJPanel.jButtonRemoveModule) {
			removeModule();
		} else if (action.getSource() == definitionJPanel.jButtonMoveLayerUp) {
			moveLayerUp();
		} else if (action.getSource() == definitionJPanel.jButtonMoveLayerDown) {
			moveLayerDown();
		} else if (action.getSource() == definitionJPanel.jButtonAddSoftwareUnit) {
			addSoftwareUnit();
		} else if (action.getSource() == definitionJPanel.jButtonRemoveSoftwareUnit) {
			removeSoftwareUnit();
		} else if (action.getSource() == definitionJPanel.jButtonAddRule) {
			addRuleToModule();
		} else if (action.getSource() == definitionJPanel.jButtonEditRule) {
			editRuleToModule();
		} else if (action.getSource() == definitionJPanel.jButtonRemoveRule) {
			removeRuleToModule();
		} else if (action.getSource() == definitionJPanel.jCheckBoxAccess) {
			updateLayer();
		} else {
			Log.i(this, "actionPerformed(" + action + ")");
		}
	}
	
	public void update(Observable o, Object arg) {
		Log.i(this, "update(" + o + ", " + arg + ")");
		updateAppliedRulesTable();
		updateSoftwareUnitTable();
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

		if (arg0.getSource() == definitionJPanel.jTextFieldModuleName || arg0.getSource() == definitionJPanel.jTextAreaLayerDescription) {
			updateLayer();
		} else {
			Log.i(this, "keyReleased(" + arg0 + ")");
		}
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
