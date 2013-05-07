package husacct.define.presentation.jpanel;

import husacct.ServiceProvider;
import husacct.common.dto.RuleDTO;
import husacct.common.dto.RuleTypeDTO;
import husacct.common.services.IServiceListener;
import husacct.control.presentation.util.DialogUtils;

import husacct.define.presentation.jdialog.EditSoftwareUnitJDialog;
import husacct.define.presentation.jdialog.SoftwareUnitJDialog;
import husacct.define.presentation.tables.JTableSoftwareUnits;
import husacct.define.presentation.tables.JTableTableModel;
import husacct.define.presentation.utils.JPanelStatus;
import husacct.define.presentation.utils.UiDialogs;
import husacct.define.task.DefinitionController;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.TableModel;

/**
 * 
 * @author Henk ter Harmsel
 *
 */
public class SoftwareUnitsJPanel extends JPanel implements ActionListener, Observer, IServiceListener {

	private static final long serialVersionUID = 8086576683923713276L;
	private JTableSoftwareUnits softwareUnitsTable;
	private JScrollPane softwareUnitsPane;
	
	private JButton addSoftwareUnitButton;
	private JButton removeSoftwareUnitButton;
	private JButton editSoftwareUnitButton;

	public SoftwareUnitsJPanel() {
		super();
	}
	
	/**
	 * Creating Gui
	 */
	public void initGui() {
		DefinitionController.getInstance().addObserver(this);
		BorderLayout softwareUnitsPanelLayout = new BorderLayout();
		this.setLayout(softwareUnitsPanelLayout);
		this.setBorder(BorderFactory.createTitledBorder(ServiceProvider.getInstance().getLocaleService().getTranslatedString("AssignedSoftwareUnitsTitle")));
		this.add(this.addSoftwareUnitsTable(), BorderLayout.CENTER);
		this.add(this.addButtonPanel(), BorderLayout.EAST);
		ServiceProvider.getInstance().getLocaleService().addServiceListener(this);
		setButtonEnableState();
	}
	
	private JScrollPane addSoftwareUnitsTable() {
		softwareUnitsPane = new JScrollPane();
		softwareUnitsTable = new JTableSoftwareUnits();
		softwareUnitsPane.setViewportView(softwareUnitsTable);
		return softwareUnitsPane;
	}
	
	protected JPanel addButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(this.createButtonPanelLayout());
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 4));
		buttonPanel.setPreferredSize(new java.awt.Dimension(90, 156));
		
		addSoftwareUnitButton = new JButton();
		buttonPanel.add(addSoftwareUnitButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		addSoftwareUnitButton.addActionListener(this);
		
		editSoftwareUnitButton = new JButton();
		buttonPanel.add(editSoftwareUnitButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		editSoftwareUnitButton.addActionListener(this);
		
		removeSoftwareUnitButton = new JButton();
		buttonPanel.add(removeSoftwareUnitButton, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		removeSoftwareUnitButton.addActionListener(this);
		
		this.setButtonTexts();
		return buttonPanel;
	}
	
	private GridBagLayout createButtonPanelLayout() {
		GridBagLayout buttonPanelLayout = new GridBagLayout();
		buttonPanelLayout.rowWeights = new double[] { 0.0, 0.0, 0.1 };
		buttonPanelLayout.rowHeights = new int[] { 0, 11, 7 };
		buttonPanelLayout.columnWeights = new double[] { 0.1 };
		buttonPanelLayout.columnWidths = new int[] { 7 };
		return buttonPanelLayout;
	}

	/**
	 * Handling ActionPerformed
	 */
	@Override
	public void actionPerformed(ActionEvent action) {
		if (action.getSource() == this.addSoftwareUnitButton) {
			this.addSoftwareUnit();
		} else if (action.getSource() == this.removeSoftwareUnitButton) {
			this.removeSoftwareUnit();
		}
		else if (action.getSource() == this.editSoftwareUnitButton) {
			this.editSoftwareUnit();
		}
	}
	
	private void addSoftwareUnit() {

	
		if (DefinitionController.getInstance().isAnalysed()){

		if (DefinitionController.getInstance().isAnalysed() || ServiceProvider.getInstance().getControlService().isPreAnalysed()){

			long moduleId = DefinitionController.getInstance().getSelectedModuleId();
			if (moduleId != -1) {
				
				SoftwareUnitJDialog softwareUnitFrame = new SoftwareUnitJDialog(moduleId);
				DialogUtils.alignCenter(softwareUnitFrame);
				softwareUnitFrame.setVisible(true);
			}else {
				JOptionPane.showMessageDialog(this, ServiceProvider.getInstance().getLocaleService().getTranslatedString("NotAnalysedYet"), ServiceProvider.getInstance().getLocaleService().getTranslatedString("NotAnalysedYetTitle"), JOptionPane.ERROR_MESSAGE);
			}
		}
		}
		
	} 
	
	private void editSoftwareUnit() {
		if(softwareUnitsTable.getSelectedRow() != -1){
			new EditSoftwareUnitJDialog(DefinitionController.getInstance().getSelectedModuleId(), (String)softwareUnitsTable.getValueAt(softwareUnitsTable.getSelectedRow(), 0));
		}else{
			JOptionPane.showMessageDialog(this, ServiceProvider.getInstance().getLocaleService().getTranslatedString("SoftwareunitSelectionError"), ServiceProvider.getInstance().getLocaleService().getTranslatedString("WrongSelectionTitle"), JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void removeSoftwareUnit(){
		if (softwareUnitsTable.getSelectedRow() != -1){
			List<String> selectedModules = new ArrayList<String>();
			List<String> types = new ArrayList<String>();
			for(int selectedRow : softwareUnitsTable.getSelectedRows()) {
				String softwareUnitName = (String)softwareUnitsTable.getValueAt(selectedRow, 0);
				String type = (String)softwareUnitsTable.getValueAt(selectedRow, 1);
				selectedModules.add(softwareUnitName);
				types.add(type);
			}
			DefinitionController.getInstance().removeSoftwareUnits(selectedModules, types);
		}else{
			JOptionPane.showMessageDialog(this, ServiceProvider.getInstance().getLocaleService().getTranslatedString("SoftwareunitSelectionError"), ServiceProvider.getInstance().getLocaleService().getTranslatedString("WrongSelectionTitle"), JOptionPane.ERROR_MESSAGE);
		}
	}
		
	/**
	 * Observer
	 */
	@Override
	public void update(Observable o, Object arg) {
		updateSoftwareUnitTable();
		setButtonEnableState();
	}
	
	public void updateSoftwareUnitTable() {
		try {
			JTableTableModel atm = (JTableTableModel) softwareUnitsTable.getModel();
			atm.getDataVector().removeAllElements();
			
			long moduleId = DefinitionController.getInstance().getSelectedModuleId();
			JPanelStatus.getInstance(ServiceProvider.getInstance().getLocaleService().getTranslatedString("UpdatingRules")).start();
			if (moduleId != -1) {

				// Get all components from the service
				ArrayList<String> softwareUnitNames = DefinitionController.getInstance().getSoftwareUnitNamesBySelectedModule();
				
				ArrayList<String> regExSoftwareUnitNames = DefinitionController.getInstance().getRegExSoftwareUnitNamesBySelectedModule();
				
				if (softwareUnitNames != null) {
					for (String softwareUnitName : softwareUnitNames) {
						String softwareUnitType = DefinitionController.getInstance().getSoftwareUnitTypeBySoftwareUnitName(softwareUnitName);
						Object rowdata[] = {softwareUnitName, softwareUnitType};
						
						atm.addRow(rowdata);
					}
				}
				
				if (regExSoftwareUnitNames != null) {
					for (String softwareUnitName : regExSoftwareUnitNames) {
						Object rowdata[] = {softwareUnitName, "REGEX"};
						
						atm.addRow(rowdata);
					}
				}
			}
			atm.fireTableDataChanged();
		} catch (Exception e) {
			UiDialogs.errorDialog(this, e.getMessage());
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}
	
	private void setButtonEnableState() {
		if (DefinitionController.getInstance().getSelectedModuleId() == -1){
			disableButtons();
		} else {
			enableButtons();
		}
	}
	
	private void enableButtons() {
		addSoftwareUnitButton.setEnabled(true);
		editSoftwareUnitButton.setEnabled(true);
		removeSoftwareUnitButton.setEnabled(true);
	}

	private void disableButtons() {
		addSoftwareUnitButton.setEnabled(false);
		editSoftwareUnitButton.setEnabled(false);
		removeSoftwareUnitButton.setEnabled(false);
	}
	
	public TableModel getModel(){
		return softwareUnitsTable.getModel();
	}

	public int getSelectedRow() {
		return softwareUnitsTable.getSelectedRow();
	}

	@Override
	public void update() {
		this.setButtonTexts();
		this.setBorder(BorderFactory.createTitledBorder(ServiceProvider.getInstance().getLocaleService().getTranslatedString("AssignedSoftwareUnitsTitle")));
		this.softwareUnitsTable.changeColumnHeaders();

	}
	
	private void setButtonTexts() {
		addSoftwareUnitButton.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Add"));
		editSoftwareUnitButton.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Edit"));
		removeSoftwareUnitButton.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Remove"));
	}
}
