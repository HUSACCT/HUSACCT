package husacct.define.presentation.jpanel;

import husacct.ServiceProvider;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;

public class SoftwareUnitsJPanel extends JPanel implements ActionListener, Observer, IServiceListener {

	private static final long serialVersionUID = 8086576683923713276L;
	private JTableSoftwareUnits softwareUnitsTable;
	private JScrollPane softwareUnitsPane;
	private JPopupMenu popupMenu = new JPopupMenu();
	private JMenuItem addSoftwareUnitItem = new JMenuItem();
	private JMenuItem editSoftwareUnitItem= new JMenuItem();
	private JMenuItem removeSoftwareUnitItem = new JMenuItem();

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
		createPopupMenu();
		setButtonEnableState();
	}

	private JScrollPane addSoftwareUnitsTable() {
		softwareUnitsPane = new JScrollPane();
		softwareUnitsTable = new JTableSoftwareUnits();
		softwareUnitsPane.setViewportView(softwareUnitsTable);
		softwareUnitsTable.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent event) {
				createPopup(event);
				setButtonEnableState();				
			}
			public void mouseClicked(MouseEvent event) {
				createPopup(event);
				setButtonEnableState();				
			}
			public void mouseEntered(MouseEvent event) {
				createPopup(event);
				setButtonEnableState();
			}
		});
		return softwareUnitsPane;
	}
	private void createPopup(MouseEvent event){
		if(SwingUtilities.isRightMouseButton(event)){
			int row = softwareUnitsTable.rowAtPoint(event.getPoint());
			int column = softwareUnitsTable.columnAtPoint(event.getPoint());
			if(!softwareUnitsTable.isRowSelected(row)){
				softwareUnitsTable.changeSelection(row, column, false, false);
			}
			popupMenu.show(event.getComponent(), event.getX(), event.getY());			
		}
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

	private void createPopupMenu(){
		this.addSoftwareUnitItem = new JMenuItem(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Add"));
		this.addSoftwareUnitItem.addActionListener(this);
		this.editSoftwareUnitItem = new JMenuItem(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Edit"));
		this.editSoftwareUnitItem.addActionListener(this);
		this.removeSoftwareUnitItem = new JMenuItem(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Remove"));
		this.removeSoftwareUnitItem.addActionListener(this);

		popupMenu.add(addSoftwareUnitItem);
		popupMenu.add(editSoftwareUnitItem);
		popupMenu.add(removeSoftwareUnitItem);
	}

	/**
	 * Handling ActionPerformed
	 */
	@Override
	public void actionPerformed(ActionEvent action) {
		if (action.getSource() == this.addSoftwareUnitButton || action.getSource() == this.addSoftwareUnitItem) {
			this.addSoftwareUnit();
		} else if (action.getSource() == this.removeSoftwareUnitButton || action.getSource() == this.removeSoftwareUnitItem) {
			this.removeSoftwareUnit();
		}
		else if (action.getSource() == this.editSoftwareUnitButton || action.getSource() == this.editSoftwareUnitItem) {
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
		if(getSelectedRow() != -1){
			new EditSoftwareUnitJDialog(DefinitionController.getInstance().getSelectedModuleId(), (String)softwareUnitsTable.getValueAt(softwareUnitsTable.getSelectedRow(), 0));
		}else{
			JOptionPane.showMessageDialog(this, ServiceProvider.getInstance().getLocaleService().getTranslatedString("SoftwareunitSelectionError"), ServiceProvider.getInstance().getLocaleService().getTranslatedString("WrongSelectionTitle"), JOptionPane.ERROR_MESSAGE);
		}
	}

	private void removeSoftwareUnit(){
		if (getSelectedRow() != -1){
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
		} else if(softwareUnitsTable.getRowCount() == 0 || getSelectedRow() == -1){
			enableAddDisableEditRemoveButtons();
		} else {
			enableButtons();
			if(!isSelectedRegex()){
				editSoftwareUnitButton.setEnabled(false);
				editSoftwareUnitItem.setEnabled(false);
			}
		}
	}

	private boolean isSelectedRegex(){
		if (((String) softwareUnitsTable.getValueAt(getSelectedRow(), 1)).toLowerCase().equals("regex")) {
			return true;
		}else{
			return false;
		}
	}

	private void enableButtons() {
		addSoftwareUnitButton.setEnabled(true);
		addSoftwareUnitItem.setEnabled(true);

		editSoftwareUnitButton.setEnabled(true);
		editSoftwareUnitItem.setEnabled(true);

		removeSoftwareUnitButton.setEnabled(true);
		removeSoftwareUnitItem.setEnabled(true);		
	}

	private void disableButtons() {
		addSoftwareUnitButton.setEnabled(false);
		addSoftwareUnitItem.setEnabled(false);

		editSoftwareUnitButton.setEnabled(false);
		editSoftwareUnitItem.setEnabled(false);

		removeSoftwareUnitButton.setEnabled(false);
		removeSoftwareUnitItem.setEnabled(false);
	}

	private void enableAddDisableEditRemoveButtons(){
		addSoftwareUnitButton.setEnabled(true);
		addSoftwareUnitItem.setEnabled(true); 

		editSoftwareUnitButton.setEnabled(false);
		editSoftwareUnitItem.setEnabled(false);

		removeSoftwareUnitButton.setEnabled(false);
		removeSoftwareUnitItem.setEnabled(false);
	}

	private void setButtonTexts() {
		addSoftwareUnitButton.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Add"));
		editSoftwareUnitButton.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Edit"));
		removeSoftwareUnitButton.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Remove"));
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
}
