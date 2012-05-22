package husacct.define.presentation.jpanel;

import husacct.ServiceProvider;
import husacct.control.ILocaleChangeListener;
import husacct.define.abstraction.language.DefineTranslator;
import husacct.define.presentation.jframe.JFrameSoftwareUnit;
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
import java.util.Locale;
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
public class SoftwareUnitsJPanel extends AbstractDefinitionJPanel implements ActionListener, Observer, ILocaleChangeListener {

	private static final long serialVersionUID = 8086576683923713276L;
	private JTableSoftwareUnits softwareUnitsTable;
	private JScrollPane softwareUnitsPane;
	
	private JButton addSoftwareUnitButton;
	private JButton removeSoftwareUnitButton;

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
		this.setBorder(BorderFactory.createTitledBorder(DefineTranslator.translate("AssignedSoftwareUnitsTitle")));
		this.add(this.addSoftwareUnitsTable(), BorderLayout.CENTER);
		this.add(this.addButtonPanel(), BorderLayout.EAST);
		ServiceProvider.getInstance().getControlService().addLocaleChangeListener(this);
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
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
		buttonPanel.setPreferredSize(new java.awt.Dimension(90, 156));
		
		addSoftwareUnitButton = new JButton();
		buttonPanel.add(addSoftwareUnitButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		addSoftwareUnitButton.addActionListener(this);
			
		removeSoftwareUnitButton = new JButton();
		buttonPanel.add(removeSoftwareUnitButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		removeSoftwareUnitButton.addActionListener(this);
		
		this.setButtonTexts();
		return buttonPanel;
	}
	
	private GridBagLayout createButtonPanelLayout() {
		GridBagLayout buttonPanelLayout = new GridBagLayout();
		buttonPanelLayout.rowWeights = new double[] { 0.0, 0.0, 0.1 };
		buttonPanelLayout.rowHeights = new int[] { 13, 13, 7 };
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
	}
	
	private void addSoftwareUnit() {
		if (DefinitionController.getInstance().isAnalysed()){
			long moduleId = DefinitionController.getInstance().getSelectedModuleId();
			if (moduleId != -1) {
				JFrameSoftwareUnit softwareUnitFrame = new JFrameSoftwareUnit(moduleId);
				softwareUnitFrame.setLocationRelativeTo(softwareUnitFrame.getRootPane());
				softwareUnitFrame.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(this, DefineTranslator.translate("ModuleSelectionError"), DefineTranslator.translate("WrongSelectionTitle"), JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this, DefineTranslator.translate("NotAnalysedYet"), DefineTranslator.translate("NotAnalysedYetTitle"), JOptionPane.ERROR_MESSAGE);
		}
	}
	private void removeSoftwareUnit(){
		if (getSelectedRow() != -1){
			String softwareUnitName = (String)softwareUnitsTable.getValueAt(getSelectedRow(), 0);
			DefinitionController.getInstance().removeSoftwareUnit(softwareUnitName);
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
			long moduleId = DefinitionController.getInstance().getSelectedModuleId();
			JPanelStatus.getInstance(DefineTranslator.translate("UpdatingRules")).start();
			if (moduleId != -1) {

				// Get all components from the service
				ArrayList<String> softwareUnitNames = DefinitionController.getInstance().getSoftwareUnitNamesBySelectedModule();

				// Get the tablemodel from the table
				JTableTableModel atm = (JTableTableModel) softwareUnitsTable.getModel();

				// Remove all items in the table
				atm.getDataVector().removeAllElements();
				
				if (softwareUnitNames != null) {
					for (String softwareUnitName : softwareUnitNames) {
						String softwareUnitType = DefinitionController.getInstance().getSoftwareUnitTypeBySoftwareUnitName(softwareUnitName);
						Object rowdata[] = {softwareUnitName, softwareUnitType};
						
						atm.addRow(rowdata);
					}
				}
				atm.fireTableDataChanged();
			}
		} catch (Exception e) {
			UiDialogs.errorDialog(this, e.getMessage(), "Error!");
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
		removeSoftwareUnitButton.setEnabled(true);
	}

	private void disableButtons() {
		addSoftwareUnitButton.setEnabled(false);
		removeSoftwareUnitButton.setEnabled(false);
	}
	
	public TableModel getModel(){
		return softwareUnitsTable.getModel();
	}

	public int getSelectedRow() {
		return softwareUnitsTable.getSelectedRow();
	}

	@Override
	public void update(Locale newLocale) {
		this.setButtonTexts();
	}
	
	private void setButtonTexts() {
		addSoftwareUnitButton.setText(DefineTranslator.translate("Add"));
		removeSoftwareUnitButton.setText(DefineTranslator.translate("Remove"));
	}
}
