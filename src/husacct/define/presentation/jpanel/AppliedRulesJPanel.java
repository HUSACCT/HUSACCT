package husacct.define.presentation.jpanel;

import husacct.ServiceProvider;
import husacct.common.services.IServiceListener;
import husacct.control.presentation.util.DialogUtils;
import husacct.define.presentation.jdialog.AppliedRuleJDialog;
import husacct.define.presentation.tables.JTableAppliedRule;
import husacct.define.presentation.tables.JTableTableModel;
import husacct.define.presentation.utils.DataHelper;
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
import java.util.HashMap;
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

public class AppliedRulesJPanel extends JPanel  implements ActionListener, Observer, IServiceListener {
	
	private static final long serialVersionUID = -2052083182258803790L;
	
	private JTableAppliedRule appliedRulesTable;
	private JScrollPane appliedRulesPane;
	private JPopupMenu popupMenu = new JPopupMenu();
	private JMenuItem addRuleItem = new JMenuItem();
	private JMenuItem editRuleItem= new JMenuItem();
	private JMenuItem removeRuleItem = new JMenuItem();
	
	private JButton addRuleButton;
	private JButton editRuleButton;
	private JButton removeRuleButton;

	public AppliedRulesJPanel() {
		super();
	}

	/**
	 * Creating Gui
	 */
	public void initGui() {
		DefinitionController.getInstance().addObserver(this);
		BorderLayout appliedRulesPanelLayout = new BorderLayout();
		this.setLayout(appliedRulesPanelLayout);
		this.setBorder(BorderFactory.createTitledBorder(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Rules")));
		this.add(this.addAppliedRulesTable(), BorderLayout.CENTER);
		this.add(this.addButtonPanel(), BorderLayout.EAST);
		createPopupMenu();
		setButtonEnableState();
		ServiceProvider.getInstance().getLocaleService().addServiceListener(this);
	}
	
	private JScrollPane addAppliedRulesTable() {
		appliedRulesPane = new JScrollPane();
		appliedRulesTable = new JTableAppliedRule();
		appliedRulesTable.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent event) {
				createPopup(event);
				setButtonEnableState();				
			}
			public void mouseClicked(MouseEvent event) {
				createPopup(event);
				setButtonEnableState();			
				if (event.getClickCount()==2){
					editRule();
				}
			}
			public void mouseEntered(MouseEvent event) {
				createPopup(event);
				setButtonEnableState();
			}
		});
		appliedRulesPane.setViewportView(appliedRulesTable);
		return appliedRulesPane;
	}
	
	protected JPanel addButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(this.createButtonPanelLayout());
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 4));
		buttonPanel.setPreferredSize(new java.awt.Dimension(90, 156));
		
		addRuleButton = new JButton();
		buttonPanel.add(addRuleButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		addRuleButton.addActionListener(this);
		
		editRuleButton = new JButton();
		buttonPanel.add(editRuleButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		editRuleButton.addActionListener(this);
		
		removeRuleButton = new JButton();
		buttonPanel.add(removeRuleButton, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		removeRuleButton.addActionListener(this);
		
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
	
	private void createPopup(MouseEvent event){
		if(SwingUtilities.isRightMouseButton(event)){
			int row = appliedRulesTable.rowAtPoint(event.getPoint());
			int column = appliedRulesTable.columnAtPoint(event.getPoint());
			if(!appliedRulesTable.isRowSelected(row)){
				appliedRulesTable.changeSelection(row, column, false, false);
			}
			popupMenu.show(event.getComponent(), event.getX(), event.getY());			
		}
	}
	
	private void createPopupMenu(){
		this.addRuleItem = new JMenuItem(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Add"));
		this.addRuleItem.addActionListener(this);
		this.editRuleItem = new JMenuItem(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Edit"));
		this.editRuleItem.addActionListener(this);
		this.removeRuleItem = new JMenuItem(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Remove"));
		this.removeRuleItem.addActionListener(this);
		
		popupMenu.add(addRuleItem);
		popupMenu.add(editRuleItem);
		popupMenu.add(removeRuleItem);
	}
	
	/**
	 * Handling ActionPerformed
	 */
	@Override
	public void actionPerformed(ActionEvent action) {
		if (action.getSource() == this.addRuleButton || action.getSource() == this.addRuleItem) {
			this.addRule();
		} else if (action.getSource() == this.editRuleButton || action.getSource() == this.editRuleItem) {
			this.editRule();
		} else if (action.getSource() == this.removeRuleButton || action.getSource() == this.removeRuleItem) {
			this.removeRule();
		}
	}
	
	private void addRule() {
		long moduleId = DefinitionController.getInstance().getSelectedModuleId();
			if (moduleId != -1) {
			AppliedRuleJDialog appliedRuleFrame = new AppliedRuleJDialog(moduleId, -1L);
			DialogUtils.alignCenter(appliedRuleFrame);
			appliedRuleFrame.setVisible(true);
			
		} else {
			//TODO Test popup
			JOptionPane.showMessageDialog(this, ServiceProvider.getInstance().getLocaleService().getTranslatedString("ModuleSelectionError"), ServiceProvider.getInstance().getLocaleService().getTranslatedString("WrongSelectionTitle"), JOptionPane.ERROR_MESSAGE);
		}
	}

	private void editRule() {
		long moduleId = DefinitionController.getInstance().getSelectedModuleId();
		long selectedAppliedRuleId = getSelectedAppliedRuleId();
		if (selectedAppliedRuleId != -1){
			AppliedRuleJDialog appliedRuleFrame = new AppliedRuleJDialog(moduleId, selectedAppliedRuleId);
			DialogUtils.alignCenter(appliedRuleFrame);
			appliedRuleFrame.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(this, ServiceProvider.getInstance().getLocaleService().getTranslatedString("RuleSelectionError"), ServiceProvider.getInstance().getLocaleService().getTranslatedString("WrongSelectionTitle"), JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private long getSelectedAppliedRuleId(){
		long selectedAppliedRuleId = -1;
		try {//TODO check if selectedRow exists
			Object o = appliedRulesTable.getValueAt(getSelectedRow(), appliedRulesTable.getRuleTypeColumnIndex());
			if (o instanceof DataHelper) {
				DataHelper datahelper = (DataHelper) o;
				selectedAppliedRuleId = datahelper.getId();
			}
		} catch(Exception e){
			
		}
		return selectedAppliedRuleId;
	}

	private void removeRule() {
		long selectedAppliedRuleId = getSelectedAppliedRuleId();
		if(selectedAppliedRuleId != -1) {
			DefinitionController.getInstance().removeRule(selectedAppliedRuleId);
		} else {
			JOptionPane.showMessageDialog(this, ServiceProvider.getInstance().getLocaleService().getTranslatedString("RuleSelectionError"), ServiceProvider.getInstance().getLocaleService().getTranslatedString("WrongSelectionTitle"), JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Observer
	 */
	@Override
	public void update(Observable o, Object arg) {
		updateAppliedRuleTable();
		setButtonEnableState();
	}

	public void updateAppliedRuleTable() {		
		try {
			JTableTableModel atm = (JTableTableModel) appliedRulesTable.getModel();
			atm.getDataVector().removeAllElements();
			
			long moduleId = DefinitionController.getInstance().getSelectedModuleId();
			JPanelStatus.getInstance(ServiceProvider.getInstance().getLocaleService().getTranslatedString("UpdatingRules")).start();
			if (moduleId != -1) {

				// Get all appliedRuleIds from the service
				ArrayList<Long> appliedRulesIds = DefinitionController.getInstance().getAppliedRuleIdsBySelectedModule();

				if (appliedRulesIds != null) {
					for (long appliedRuleId : appliedRulesIds) {
						
						HashMap<String, Object> ruleDetails = DefinitionController.getInstance().getRuleDetailsByAppliedRuleId(appliedRuleId);
						String ruleTypeKey = (String) ruleDetails.get("ruleTypeKey");
						//SetDataHelper to help retrieve the applied Rule id through the ruleTypeKey
						DataHelper datahelperRuleType = new DataHelper();
						datahelperRuleType.setId(appliedRuleId);
						datahelperRuleType.setValue(ruleTypeKey);

						String moduleToName = (String) ruleDetails.get("moduleToName");
						boolean appliedRuleIsEnabled = (Boolean) ruleDetails.get("enabled");
						String enabled = "Off";
						if (appliedRuleIsEnabled) {
							enabled = "On";
						}
						int numberofexceptions = (Integer) ruleDetails.get("numberofexceptions");

						Object rowdata[] = {datahelperRuleType, moduleToName , enabled, numberofexceptions };

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
		}else if(appliedRulesTable.getSelectedRowCount() == 0 || getSelectedRow() == -1){
			enableAddDisableEditRemoveButtons();
		}else {
			enableButtons();
		}
	}
	
	private void enableButtons() {
		addRuleButton.setEnabled(true);
		addRuleItem.setEnabled(true); 
		
		editRuleButton.setEnabled(true);
		editRuleItem.setEnabled(true);
		
		removeRuleButton.setEnabled(true);
		removeRuleItem.setEnabled(true);
	}

	private void disableButtons() {
		addRuleButton.setEnabled(false);
		addRuleItem.setEnabled(false);
		
		editRuleButton.setEnabled(false);
		editRuleItem.setEnabled(false);
		
		removeRuleButton.setEnabled(false);
		removeRuleItem.setEnabled(false);
	}
	
	private void enableAddDisableEditRemoveButtons(){
		addRuleButton.setEnabled(true);
		addRuleItem.setEnabled(true);
		
		editRuleButton.setEnabled(false);
		editRuleItem.setEnabled(false);
		
		removeRuleButton.setEnabled(false);
		removeRuleItem.setEnabled(false);
	}
	
	public TableModel getModel() {
		return appliedRulesTable.getModel();
	}
	
	public int getSelectedRow() {
		return appliedRulesTable.getSelectedRow();
	}

	@Override
	public void update() {
		this.setButtonTexts();
		this.setBorder(BorderFactory.createTitledBorder(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Rules")));
		this.appliedRulesTable.changeColumnHeaders();
	}
	
	private void setButtonTexts() {
		addRuleButton.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Add"));
		editRuleButton.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Edit"));
		removeRuleButton.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Remove"));
	}
}
