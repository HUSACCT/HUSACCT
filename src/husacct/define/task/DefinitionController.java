package husacct.define.task;

import husacct.define.domain.DefineDomainService;
import husacct.define.task.ApplicationController;
import husacct.define.presentation.helper.DataHelper;
import husacct.define.presentation.jpanel.DefinitionJPanel;
import husacct.define.presentation.tables.JTableTableModel;
import husacct.define.presentation.utils.JPanelStatus;
import husacct.define.presentation.utils.Log;
import husacct.define.presentation.utils.UiDialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class DefinitionController implements ActionListener, ListSelectionListener, KeyListener, Observer {

	private DefinitionJPanel definitionJPanel;
	// create own service
	private DefineDomainService defineDomainService;
	private ApplicationController mainController;

	public DefinitionController(ApplicationController mc) {
		Log.i(this, "constructor()");
		mainController = mc;
		definitionJPanel = new DefinitionJPanel();
		defineDomainService = DefineDomainService.getInstance();
	}

	/**
	 * Init the user interface for creating/editting the definition.
	 * 
	 * @return JPanel The jpanel
	 */
	public JPanel initUi() {
		Log.i(this, "initUi()");

		updateLayerList();

		// Set actionlisteners to buttons, lists etc.
		definitionJPanel.jListLayers.addListSelectionListener(this);
		definitionJPanel.jButtonNewLayer.addActionListener(this);
		definitionJPanel.jButtonRemoveLayer.addActionListener(this);
		definitionJPanel.jButtonMoveLayerUp.addActionListener(this);
		definitionJPanel.jButtonMoveLayerDown.addActionListener(this);

		definitionJPanel.jTextFieldLayerName.addKeyListener(this);
		definitionJPanel.jTextAreaLayerDescription.addKeyListener(this);
		definitionJPanel.jCheckBoxAccess.addActionListener(this);

		definitionJPanel.jButtonAddSoftwareUnit.addActionListener(this);
//		definitionJPanel.jButtonEditSoftwareUnit.addActionListener(this);
		definitionJPanel.jButtonRemoveSoftwareUnit.addActionListener(this);

		definitionJPanel.jButtonAddRule.addActionListener(this);
		definitionJPanel.jButtonEditRule.addActionListener(this);
		definitionJPanel.jButtonRemoveRule.addActionListener(this);

		// Return the definition jpanel
		return definitionJPanel;
	}

	/**
	 * Create an new configuration.
	 */
	public void newConfiguration() {
		Log.i(this, "newConfiguration()");
		try {
			// Ask the user for the architecture name
			String response = UiDialogs.inputDialog(definitionJPanel, "Please enter the architecture name", "Please input a value", JOptionPane.QUESTION_MESSAGE);
			if (response != null) {
				Log.i(this, "newDefinition() - response from inputdialog: " + response);
				JPanelStatus.getInstance("Creating new configuration").start();

				// Create a new configuration
				defineDomainService.createNewArchitectureDefinition(response);

				// Update the layer list, this method is called because it will also clear the existing layers
				updateLayerList();

				// Set the architecture name in the jframe title
				mainController.jframe.setTitle(response);
			}
		} catch (Exception e) {
			Log.e(this, "newConfiguration() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}

	/**
	 * Open an configuration.
	 */
	public void openConfiguration() {
		Log.i(this, "openConfiguration()");
		UiDialogs.errorDialog(definitionJPanel, "XML import yet not possible", "Error");
//		try {
//			// Create a file chooser
//			JFileChooser fc = new JFileChooser();
//			fc.setFileFilter(new XmlFileFilter());
//
//			// In response to a button click:
//			int returnVal = fc.showOpenDialog(definitionJPanel);
//
//			// The user did click on Open
//			if (returnVal == JFileChooser.APPROVE_OPTION) {
//
//				JPanelStatus.getInstance("Opening configuration").start();
//
//				// Getting selected file from dialog
//				File file = fc.getSelectedFile();
//				Log.i(this, "openConfiguration() - opening file: " + file.getName());
//
//				// Pass the file to the service
//				definitionService.importConfiguration(file);
//
//				// Set the architecture name in the jframe title
//				mainController.jframe.setTitle(definitionService.getArchitectureDefinitionName());
//
//				Log.i(this, "openConfiguration() - updating layers list");
//				updateLayerList();
//
//				Log.i(this, "openConfiguration() - success opening configuration");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			Log.e(this, "openConfiguration() - exeption: " + e.getMessage());
//			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
//		} finally {
//			JPanelStatus.getInstance().stop();
//		}
	}

	/**
	 * Save the current configuration to a file.
	 */
	public void saveConfiguration() {
		Log.i(this, "saveConfiguration()");
		UiDialogs.errorDialog(definitionJPanel, "XML export yet not possible", "Error");
//		try {
//			// Create a file chooser
//			JFileChooser fc = new JFileChooser();
//			fc.setFileFilter(new XmlFileFilter());
//
//			// In response to a button click:
//			int returnVal = fc.showSaveDialog(definitionJPanel);
//
//			// The user did click on Save
//			if (returnVal == JFileChooser.APPROVE_OPTION) {
//
//				JPanelStatus.getInstance("Saving configuration").start();
//
//				// Getting selected file from dialog
//				File file;
//				if (fc.getSelectedFile().getName().endsWith(".xml")) {
//					file = fc.getSelectedFile();
//				} else {
//					file = new File(fc.getSelectedFile().getAbsolutePath() + ".xml");
//				}
//				Log.i(this, "saveConfiguration() - configuration needs to be saved to file: " + file.getName());
//
//				// Pass the file to the service
//				definitionService.exportConfiguration(file);
//
//				Log.i(this, "saveConfiguration() - success saving configuration");
//			}
//		} catch (Exception e) {
//			Log.e(this, "saveConfiguration() - exeption: " + e.getMessage());
//			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
//		} finally {
//			JPanelStatus.getInstance().stop();
//		}
	}

	/**
	 * Create a new layer
	 */
	private void newLayer() {
		Log.i(this, "newLayer()");
		try {
			// Ask the user for the layer name
			String layerName = UiDialogs.inputDialog(definitionJPanel, "Please enter layer name", "Please input a value", JOptionPane.QUESTION_MESSAGE);
			if (layerName != null) {
				String layerLevelString = UiDialogs.inputDialog(definitionJPanel, "Please enter layer level", "Please input a value", JOptionPane.QUESTION_MESSAGE);
				if (layerLevelString != null) {
					JPanelStatus.getInstance("Creating new layer").start();
					int layerLevel = Integer.parseInt(layerLevelString);

					Log.i(this, "newLayer() - name: " + layerName);
					// Create the layer
					long layerId = defineDomainService.addLayer(layerName, layerLevel);

					// Update the layer list
					updateLayerList();
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
	private void removeLayer() {
		Log.i(this, "removeLayer()");
		try {
			long layerId = definitionJPanel.getSelectedLayer();
			if (layerId != -1) {
				boolean confirm = UiDialogs.confirmDialog(definitionJPanel, "Are you sure you want to remove layer: \"" + layerId + "\"", "Remove?");
				if (confirm) {
					JPanelStatus.getInstance("Removing layer").start();

					defineDomainService.removeModuleById(layerId);

					updateLayerList();
				}

			}
		} catch (Exception e) {
			Log.e(this, "removeLayer() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}

	/**
	 * Move a layer 1 up in hierarchy
	 */
	private void moveLayerUp() {
		Log.i(this, "moveLayerUp()");
		try {
			long layerId = definitionJPanel.getSelectedLayer();

			if (layerId != -1) {
				JPanelStatus.getInstance("Moving layer up").start();

				defineDomainService.moveLayerUp(layerId);

				updateLayerList();
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
	private void moveLayerDown() {
		Log.i(this, "moveLayerDown()");
//		UiDialogs.errorDialog(definitionJPanel, "Maybe coming in future", "Error");
		try {
			long layerId = definitionJPanel.getSelectedLayer();

			if (layerId != -1) {
				JPanelStatus.getInstance("Moving layer down").start();

				defineDomainService.moveLayerDown(layerId);

				updateLayerList();
			}
		} catch (Exception e) {
			Log.e(this, "moveLayerDown() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}

	/**
	 * Add a new software unit to the selected layer. This method will make pop-up a new jframe who will handle everything for creating a new sotware unit.
	 */
	private void addSoftwareUnit() {
		Log.i(this, "addSoftwareUnit()");
		try {
			long layerId = definitionJPanel.getSelectedLayer();

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
	 * Edit the selected software unit. This method will make a new jframe who will handle everything for editing a new software unit
	 */
	private void editSoftwareUnit() {
		Log.i(this, "editSoftwareUnit()");
		UiDialogs.errorDialog(definitionJPanel, "EDITING SoftwareUnits not working yet", "Error");
		
//		try {
//			int layer_id = definitionJPanel.getSelectedLayer();
//			long softwareunit_id = definitionJPanel.getSelectedSoftwareUnit();
//
//			if (layer_id != -1 && softwareunit_id != -1L) {
//				// Create a new software unit controller
//				SoftwareUnitController c = new SoftwareUnitController(layer_id, softwareunit_id);
//				// Set the action of the view
//				c.setAction(PopUpController.ACTION_EDIT);
//				c.addObserver(this);
//				// Build and show the ui
//				c.initUi();
//			} else {
//				Log.e(this, "editSoftwareUnit() - no software unit selected");
//				UiDialogs.errorDialog(definitionJPanel, "Select a software unit", "Error");
//			}
//		} catch (Exception e) {
//			Log.e(this, "editSoftwareUnit() - exception: " + e.getMessage());
//			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
//		}
	}

	/**
	 * Remove the selected software unit from the table
	 */
	private void removeSoftwareUnit() {
		Log.i(this, "removeSoftwareUnit()");

		try {
			long moduleId = definitionJPanel.getSelectedLayer();
			String softwareUnitName = definitionJPanel.getSelectedSoftwareUnitName();

			if (moduleId != -1 && softwareUnitName != null && !softwareUnitName.equals("")) {
				// Ask the user if he is sure to remove the software unit
				boolean confirm = UiDialogs.confirmDialog(definitionJPanel, "Are you sure you want to remove software unit: \"" + softwareUnitName + "\"", "Remove?");
				if (confirm) {
					// Remove the software unit
					JPanelStatus.getInstance("Removing software unit").start();
					defineDomainService.removeSoftwareUnit(moduleId, softwareUnitName);
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

	private void addRuleToLayer() {
		Log.i(this, "addRuleToLayer()");

		try {
			long layerId = definitionJPanel.getSelectedLayer();

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
			Log.e(this, "addRuleToLayer() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		}
	}

	private void editRuleToLayer() {
		Log.i(this, "editRuleToLayer()");

		try {
			long layerId = definitionJPanel.getSelectedLayer();
			long appliedrule_id = definitionJPanel.getSelectedAppliedRule();

			if (layerId != -1 && appliedrule_id != -1L) {
				// Create a new software unit controller
				AppliedRulesController a = new AppliedRulesController(layerId, appliedrule_id);
				// Set the action of the view
				a.setAction(PopUpController.ACTION_EDIT);
				a.addObserver(this);
				// Build and show the ui
				a.initUi();
			} else {
				Log.e(this, "editRuleToLayer() - no applied rule selected");
				UiDialogs.errorDialog(definitionJPanel, "Select an applied rule", "Error");
			}
		} catch (Exception e) {
			Log.e(this, "editRuleToLayer() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
		}
	}

	private void removeRuleToLayer() {
		Log.i(this, "removeRuleToLayer()");
	
		try {
			long layerId = definitionJPanel.getSelectedLayer();
			int appliedRuleId = (int)definitionJPanel.getSelectedAppliedRule();

			if (layerId != -1 && appliedRuleId != -1L) {
				// Ask the user if he is sure to remove the software unit
				boolean confirm = UiDialogs.confirmDialog(definitionJPanel, "Are you sure you want to remove the applied rule: \"" + defineDomainService.getRuleTypeByAppliedRule(appliedRuleId) + "\"", "Remove?");
				if (confirm) {
					// Remove the software unit
					JPanelStatus.getInstance("Removing applied rule").start();
					defineDomainService.removeAppliedRule(appliedRuleId);

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
			long layerId = definitionJPanel.getSelectedLayer();

			JPanelStatus.getInstance("Saving layer").start();

			if (layerId != -1) {
				defineDomainService.setModuleName(layerId, definitionJPanel.jTextFieldLayerName.getText());

				//To update the layer list: we need to fetch the DataHelper from the list, update it and fire an updateUI to notice that there is an update
				DefaultListModel dlm = (DefaultListModel) definitionJPanel.jListLayers.getModel();				
				for (int i = 0; i < dlm.getSize(); i++) {
					DataHelper datahelper = (DataHelper) dlm.getElementAt(i);
					if (datahelper.getId() == layerId) {
						datahelper.setValue(definitionJPanel.jTextFieldLayerName.getText());
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

	/**
	 * This method updates the layers list in the jpanel.
	 */
	private void updateLayerList() {
		Log.i(this, "updateLayerList()");

		JPanelStatus.getInstance("Updating layers").start();

		// Get all layers from the service
		ArrayList<Long> layerIds = defineDomainService.getLayerIdsSorted();
		// Get ListModel from listlayers
		DefaultListModel dlm = (DefaultListModel) definitionJPanel.jListLayers.getModel();

		// Remove all items in the list
		dlm.removeAllElements();

		// Add layers to the list
		if (layerIds != null) {
			for (Long layerId : layerIds) {
				DataHelper datahelper = new DataHelper();
				datahelper.setId(layerId);
				try {
					datahelper.setValue(defineDomainService.getModuleNameById(layerId));
				} catch (Exception e) {
					Log.e(this, "updateLayer() - exception: " + e.getMessage());
					UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
				}
				dlm.addElement(datahelper);
			}
		}

		enablePanel();

		JPanelStatus.getInstance().stop();
	}

	/**
	 * This function will load the layer name, descriptin and interface acces only checkbox. Next it will call two methods which will load the two tables.
	 */
	private void loadLayerDetail() {
		Log.i(this, "loadLayerDetail()");

		long layerId = definitionJPanel.getSelectedLayer();

		if (layerId != -1) {
			// Set the values
			try {
				definitionJPanel.jTextFieldLayerName.setText(defineDomainService.getModuleNameById(layerId));
			} catch (Exception e) {
				Log.e(this, "loadLayerDetail() - exception: " + e.getMessage());
				UiDialogs.errorDialog(definitionJPanel, e.getMessage(), "Error");
			}
//			definitionJPanel.jTextAreaLayerDescription.setText(definitionService.getLayerDescription(layer_id));
//			definitionJPanel.jCheckBoxAccess.setSelected(definitionService.getLayerInterfaceOnly(layer_id));

			// Update the tables
			updateSoftwareUnitTable();
			updateAppliedRulesTable();

			// Enable or disable the ui elements
			enablePanel();
		}
	}

	/**
	 * This method will check if the ui elements should be enabled or disabled
	 */
	private void enablePanel() {
		Log.i(this, "enablePanel()");

		long layerId = definitionJPanel.getSelectedLayer();

		boolean enabled;
		if (layerId == -1) {
			Log.i(this, "enablePanel() - false");
			enabled = false;
		} else {
			Log.i(this, "enablePanel() - true");
			enabled = true;
		}
		// Buttons, textfields, tables etc.
		definitionJPanel.jTextFieldLayerName.setEnabled(enabled);
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
		definitionJPanel.jButtonRemoveLayer.setEnabled(enabled);

//		// Enable or disable menu items
//		if (!definitionService.hasArchitectureDefinition()) {
//			definitionJPanel.jButtonNewLayer.setEnabled(false);
//			mainController.jframe.jMenuItemSaveArchitecture.setEnabled(false);
//			mainController.jframe.jMenuItemStartAnalyse.setEnabled(false);
//			mainController.jframe.jMenuItemCheckDependencies.setEnabled(false);
//		} else {
//			definitionJPanel.jButtonNewLayer.setEnabled(true);
//			mainController.jframe.jMenuItemSaveArchitecture.setEnabled(true);
//			mainController.jframe.jMenuItemStartAnalyse.setEnabled(true);
//			mainController.jframe.jMenuItemCheckDependencies.setEnabled(true);
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
			long layerId = definitionJPanel.getSelectedLayer();

			if (layerId != -1) {
				JPanelStatus.getInstance("Updating software unit table").start();

				// Get all components from the service
				ArrayList<String> softwareUnitNames = defineDomainService.getSoftwareUnitNames(layerId);

				// Get the tablemodel from the table
				JTableTableModel atm = (JTableTableModel) definitionJPanel.jTableSoftwareUnits.getModel();

				// Remove all items in the table
				atm.getDataVector().removeAllElements();
				
				if (softwareUnitNames != null) {
					for (String softwareUnitName : softwareUnitNames) {
//						DataHelper datahelper = new DataHelper();
//						datahelper.setId(softwareUnit_id);
						//datahelper.setValue(defineDomainService.getSoftwareUnitName(layerId, softwareUnit_id));

						// Number of exceptions
//						ArrayList<Long> softwareUnitExceptions = defineDomainService.getSoftwareUnitExceptions(layerId, softwareUnit_id);
//						int numberofexceptions = 0;
//						if (softwareUnitExceptions != null) {
//							numberofexceptions = softwareUnitExceptions.size();
//						}

//						Object rowdata[] = { datahelper, defineDomainService.getSoftwareUnitType(layerId, softwareUnit_id), numberofexceptions };
						String softwareUnitType = defineDomainService.getSoftwareUnitType(softwareUnitName);
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
			long layerId = definitionJPanel.getSelectedLayer();

			if (layerId != -1) {
				JPanelStatus.getInstance("Updating rules applied table").start();

				// Get all applied rules from the service
				ArrayList<Long> appliedRulesIds = defineDomainService.getAppliedRulesIdsByModule(layerId);

				// Get the tablemodel from the table
				JTableTableModel atm = (JTableTableModel) definitionJPanel.jTableAppliedRules.getModel();

				// Remove all items in the table
				atm.getDataVector().removeAllElements();
				if (appliedRulesIds != null) {
					for (long appliedRuleId : appliedRulesIds) {
						DataHelper datahelper = new DataHelper();
						datahelper.setId(appliedRuleId);
						datahelper.setValue(defineDomainService.getRuleTypeByAppliedRule(appliedRuleId));

						// To layer
						long toLayerId = defineDomainService.getModuleToIdOfAppliedRule(appliedRuleId);

						// Is enabled
						boolean appliedRuleIsEnabled = defineDomainService.getAppliedRuleIsEnabled(appliedRuleId);
						String enabled = "Off";
						if (appliedRuleIsEnabled) {
							enabled = "On";
						}
						// Number of exceptions
						ArrayList<Long> appliedRulesExceptionIds = defineDomainService.getExceptionIdsByAppliedRule(appliedRuleId);
						int numberofexceptions = appliedRulesExceptionIds.size();

						Object rowdata[] = { datahelper, defineDomainService.getModuleNameById(toLayerId), enabled, numberofexceptions };

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
		if (action.getSource() == definitionJPanel.jButtonNewLayer) {
			newLayer();
		} else if (action.getSource() == definitionJPanel.jButtonRemoveLayer) {
			removeLayer();
		} else if (action.getSource() == definitionJPanel.jButtonMoveLayerUp) {
			moveLayerUp();
		} else if (action.getSource() == definitionJPanel.jButtonMoveLayerDown) {
			moveLayerDown();
		} else if (action.getSource() == definitionJPanel.jButtonAddSoftwareUnit) {
			addSoftwareUnit();
//		} else if (action.getSource() == definitionJPanel.jButtonEditSoftwareUnit) {
//			editSoftwareUnit();
		} else if (action.getSource() == definitionJPanel.jButtonRemoveSoftwareUnit) {
			removeSoftwareUnit();
		} else if (action.getSource() == definitionJPanel.jButtonAddRule) {
			addRuleToLayer();
		} else if (action.getSource() == definitionJPanel.jButtonEditRule) {
			editRuleToLayer();
		} else if (action.getSource() == definitionJPanel.jButtonRemoveRule) {
			removeRuleToLayer();
		} else if (action.getSource() == definitionJPanel.jCheckBoxAccess) {
			updateLayer();
		} else {
			Log.i(this, "actionPerformed(" + action + ")");
		}
	}

	public void valueChanged(ListSelectionEvent event) {
		if (event.getSource() == definitionJPanel.jListLayers && !event.getValueIsAdjusting()) {
			loadLayerDetail();
		}
	}

	public void keyPressed(KeyEvent arg0) {
		// Ignore
	}

	public void keyReleased(KeyEvent arg0) {
		Log.i(this, "keyReleased(" + arg0 + ")");

		if (arg0.getSource() == definitionJPanel.jTextFieldLayerName || arg0.getSource() == definitionJPanel.jTextAreaLayerDescription) {
			updateLayer();
		} else {
			Log.i(this, "keyReleased(" + arg0 + ")");
		}
	}

	public void keyTyped(KeyEvent arg0) {
		// Ignore
	}

	public void update(Observable o, Object arg) {
		Log.i(this, "update(" + o + ", " + arg + ")");
		updateAppliedRulesTable();
		updateSoftwareUnitTable();
	}

}
