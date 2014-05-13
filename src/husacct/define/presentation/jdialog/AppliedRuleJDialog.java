package husacct.define.presentation.jdialog;

import husacct.ServiceProvider;
import husacct.common.Resource;
import husacct.common.help.presentation.HelpableJDialog;
import husacct.control.ControlServiceImpl;
import husacct.control.presentation.util.DialogUtils;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition;
import husacct.define.presentation.jpanel.ruledetails.AbstractDetailsJPanel;
import husacct.define.presentation.jpanel.ruledetails.FactoryDetails;
import husacct.define.presentation.tables.JTableException;
import husacct.define.presentation.tables.JTableTableModel;
import husacct.define.presentation.utils.KeyValueComboBox;
import husacct.define.presentation.utils.UiDialogs;
import husacct.define.task.AppliedRuleController;
import husacct.define.task.PopUpController;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

public class AppliedRuleJDialog extends HelpableJDialog implements KeyListener, ActionListener, ItemListener, Observer{

	private static final long serialVersionUID = -3491664038962722000L;

	private AppliedRuleController appliedRuleController;
	private FactoryDetails factoryDetails;
	private AbstractDetailsJPanel ruleDetailsJPanel;
	private KeyValueComboBox appliedRuleKeyValueComboBox;
	private JPanel mainPanel;

	private JTableException jTableException;
	private HashMap<Long, Long> jTableExceptionIds;

	private JButton jButtonAddExceptionRow;
	private JButton jButtonRemoveExceptionRow;	
	private JButton jButtonCancel;
	private JButton jButtonSave;

	public AppliedRuleJDialog(long moduleId, long appliedRuleId) {
		super(((ControlServiceImpl) ServiceProvider.getInstance().getControlService()).getMainController().getMainGui(), true);
		this.appliedRuleController = new AppliedRuleController(moduleId, appliedRuleId);
		this.factoryDetails = new FactoryDetails();
		initGUI();
		update();
	}

	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			if (this.appliedRuleController.getAction().equals(PopUpController.ACTION_NEW)){
				setTitle(ServiceProvider.getInstance().getLocaleService().getTranslatedString("NewAppliedRuleTitle"));
			} else {
				setTitle(ServiceProvider.getInstance().getLocaleService().getTranslatedString("EditAppliedRuleTitle"));
			}
			setIconImage(new ImageIcon(Resource.get(Resource.HUSACCT_LOGO)).getImage());

			getContentPane().add(this.createMainPanel(), BorderLayout.CENTER);
			getContentPane().add(this.createButtonPanel(), BorderLayout.SOUTH);

			//			this.setResizable(false);
			this.pack();
			this.setSize(820, 590);
		} catch (Exception e) {
			// add your error handling code here
			e.printStackTrace();
		}
	}

	private JPanel createMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setLayout(this.createMainPanelLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

		mainPanel.add(new JLabel(ServiceProvider.getInstance().getLocaleService().getTranslatedString("RuleType")), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		this.createAppliedRuleKeyValueComboBox();
		mainPanel.add(this.appliedRuleKeyValueComboBox, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		String ruleTypeKey = this.appliedRuleController.getSelectedRuleTypeKey();
		ruleDetailsJPanel = factoryDetails.create(this.appliedRuleController, ruleTypeKey);
		ruleDetailsJPanel.initGui();
		mainPanel.add(this.ruleDetailsJPanel, new GridBagConstraints(0, 1, GridBagConstraints.REMAINDER, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		//do this after the initGUI of the ruleDetailsJPanel
		this.appliedRuleKeyValueComboBox.addItemListener(this);

		mainPanel.add(new JLabel("Exceptions"), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));
		mainPanel.add(this.createExceptionsPanel(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));

		return mainPanel;
	}

	private GridBagLayout createMainPanelLayout() {
		GridBagLayout mainPanelLayout = new GridBagLayout();
		mainPanelLayout.rowWeights = new double[] { 0.0, 0.0, 0.0 };
		mainPanelLayout.rowHeights = new int[] { 30, 200, 90};//30 500 90
		mainPanelLayout.columnWeights = new double[] { 0.0, 0.0 };
		mainPanelLayout.columnWidths = new int[] { 130, 660 };
		return mainPanelLayout;
	}

	private void createAppliedRuleKeyValueComboBox() {
		this.appliedRuleKeyValueComboBox = new KeyValueComboBox();
		if (this.appliedRuleController.getAction().equals(PopUpController.ACTION_EDIT)){
			this.appliedRuleController.fillRuleTypeComboBox(this.appliedRuleKeyValueComboBox, true);
			this.appliedRuleKeyValueComboBox.setEnabled(false);
		}else{
			this.appliedRuleController.fillRuleTypeComboBox(this.appliedRuleKeyValueComboBox);
		}
	}

	private void refreshRuleDetailsJPanel() {
		String ruleTypeKey = this.appliedRuleKeyValueComboBox.getSelectedItemKey();
		if (this.appliedRuleController.getAction().equals(PopUpController.ACTION_NEW)) {
		}
		this.mainPanel.remove(this.ruleDetailsJPanel);
		this.ruleDetailsJPanel = factoryDetails.create(this.appliedRuleController, ruleTypeKey);
		this.ruleDetailsJPanel.initGui();
		// updating panel!
		if(this.getComponentCount() > 0) {
			this.getRootPane().revalidate();
		}
		mainPanel.add(this.ruleDetailsJPanel, new GridBagConstraints(0, 1, GridBagConstraints.REMAINDER, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.repaint();
		this.update();
	}

	private JPanel createExceptionsPanel() {
		JPanel exceptionsPanel = new JPanel();
		exceptionsPanel.setLayout(new BorderLayout());
		exceptionsPanel.add(this.createExceptionsScrollPane(), BorderLayout.CENTER);
		exceptionsPanel.add(this.createExceptionsButtonPanel(), BorderLayout.EAST);
		return exceptionsPanel;
	}

	private JScrollPane createExceptionsScrollPane() {
		JScrollPane exceptionsScrollPane = new JScrollPane();
		exceptionsScrollPane.setPreferredSize(new java.awt.Dimension(516, 155));
		jTableException = new JTableException();
		jTableException.setSize(516, 155);
		exceptionsScrollPane.setViewportView(jTableException);
		return exceptionsScrollPane;
	}

	private JPanel createExceptionsButtonPanel() {
		JPanel exceptionsButtonPanel = new JPanel();
		exceptionsButtonPanel.setLayout(this.createExceptionsButtonPanelLayout());
		exceptionsButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));

		jButtonAddExceptionRow = new JButton(ServiceProvider.getInstance().getLocaleService().getTranslatedString("AddException"));
		exceptionsButtonPanel.add(jButtonAddExceptionRow, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		jButtonAddExceptionRow.addActionListener(this);

		jButtonRemoveExceptionRow = new JButton(ServiceProvider.getInstance().getLocaleService().getTranslatedString("RemoveException"));
		exceptionsButtonPanel.add(jButtonRemoveExceptionRow, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		jButtonRemoveExceptionRow.addActionListener(this);

		return exceptionsButtonPanel;
	}

	private GridBagLayout createExceptionsButtonPanelLayout() {
		GridBagLayout exceptionsButtonPanelLayout = new GridBagLayout();
		exceptionsButtonPanelLayout.rowWeights = new double[] { 0.0 };
		exceptionsButtonPanelLayout.rowHeights = new int[] { 30 };
		exceptionsButtonPanelLayout.columnWeights = new double[] { 0.0, 0.0};
		exceptionsButtonPanelLayout.columnWidths = new int[] { 50, 50 };
		return exceptionsButtonPanelLayout;
	}

	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel();

		if (this.appliedRuleController.getAction().equals(PopUpController.ACTION_NEW)){
			jButtonSave = new JButton(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Add"));
		} else {
			jButtonSave = new JButton(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Save"));
		}
		buttonPanel.add(jButtonSave);
		jButtonSave.addActionListener(this);

		jButtonCancel = new JButton(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Cancel"));
		buttonPanel.add(jButtonCancel);
		jButtonCancel.addActionListener(this);

		return buttonPanel;
	}

	public void update() {
		update(appliedRuleController, appliedRuleController.getCurrentAppliedRuleId());
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (appliedRuleController.getCurrentAppliedRuleId() != -1){
			updateDetails();
			updateExceptionTable();
			setButtonEnableState();
		}
	}

	private void updateDetails() {
		if (appliedRuleController.getCurrentAppliedRuleId() != -1){
			HashMap<String, Object> ruleDetails = appliedRuleController.getAppliedRuleDetails(appliedRuleController.getCurrentAppliedRuleId());
			String ruleTypeKey = (String) ruleDetails.get("ruleTypeKey");
			setSelectedRuleTypeKey(ruleTypeKey);

			ruleDetailsJPanel.updateDetails(ruleDetails);
		}
	}

	private void setSelectedRuleTypeKey(String ruleTypeKey){
		for (int i = 0;i < appliedRuleKeyValueComboBox.getItemCount();i++){
			String iteratingItemKey = (String) appliedRuleKeyValueComboBox.getItemKeyAt(i);
			if (iteratingItemKey.equals(ruleTypeKey)){
				appliedRuleKeyValueComboBox.setSelectedIndex(i);
				break;
			}
		}
	}

	public void updateExceptionTable() {
		ArrayList<HashMap<String, Object>> exceptionRules = appliedRuleController.getExceptionRules();
		JTableTableModel tableModel = (JTableTableModel) jTableException.getModel();
		tableModel.getDataVector().removeAllElements();
		jTableExceptionIds = new HashMap<Long, Long>();
		long row = 0;
		for (HashMap<String, Object> exceptionRule : exceptionRules) {	
			String description = (String) exceptionRule.get("description");

			Object from = exceptionRule.get("moduleFromId");
			String moduleFrom = getModuleDisplayValue(from);

			Object to = exceptionRule.get("moduleToId");
			String moduleTo = getModuleDisplayValue(to);

			boolean appliedRuleIsEnabled = (Boolean) exceptionRule.get("enabled");
			String enabled = ServiceProvider.getInstance().getLocaleService().getTranslatedString("Off");
			if (appliedRuleIsEnabled) {
				enabled = ServiceProvider.getInstance().getLocaleService().getTranslatedString("On");
			}

			Object rowdata[] = {moduleFrom, moduleTo, description, enabled};
			tableModel.addRow(rowdata);
			long ruleId = (long) exceptionRule.get("id");
			jTableExceptionIds.put(row, ruleId);
			row ++;
		}
		tableModel.fireTableDataChanged();
		this.repaint();
	}

	private String getModuleDisplayValue(Object o){
		String displayValue = "";
		if (o instanceof SoftwareUnitDefinition){
			displayValue = "SoftwareUnit: " + ((SoftwareUnitDefinition) o).getName();
		} else if (o instanceof Long){
			long moduleId = (Long) o;
			displayValue = appliedRuleController.getModuleName(moduleId);
		}
		return displayValue;
	}

	@Override
	public void actionPerformed(ActionEvent action) {
		if (action.getSource() == this.jButtonSave) {
			this.save();
		} else if (action.getSource() == this.jButtonCancel) {
			this.cancel();
		} else if (action.getSource() == this.jButtonAddExceptionRow) {
			this.addException();
		} else if (action.getSource() == this.jButtonRemoveExceptionRow) {
			this.removeException();
		}

	}

	private void addException() {
		if (ruleDetailsJPanel.hasValidData()) {
			HashMap<String, Object> ruleDetails = this.ruleDetailsJPanel.saveToHashMap();
			long selectedModuleToId = (Long) ruleDetails.get("moduleToId");
			this.appliedRuleController.setModuleToId(selectedModuleToId);
			ExceptionRuleJDialog exceptionFrame = new ExceptionRuleJDialog(this.appliedRuleController, this);
			DialogUtils.alignCenter(exceptionFrame);
			exceptionFrame.setVisible(true);
		} else {
			UiDialogs.errorDialog(this, ServiceProvider.getInstance().getLocaleService().getTranslatedString("CorrectDataError"));
		}
	}

	private void removeException() {
		long selectedRow = (long) jTableException.getSelectedRow();
		if (selectedRow > -1){
			long exceptionId = jTableExceptionIds.get(selectedRow);
			appliedRuleController.removeException(exceptionId);
			updateExceptionTable();
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == this.appliedRuleKeyValueComboBox && e.getStateChange() == 1) {
			this.refreshRuleDetailsJPanel();
		}
	}

	private void cancel() {
		this.dispose();
	}

	private void save() {
		if (this.appliedRuleController.getAction().equals(PopUpController.ACTION_NEW)) {
			if (!ruleDetailsJPanel.hasValidData()) {
				UiDialogs.errorDialog(this, ServiceProvider.getInstance().getLocaleService().getTranslatedString("CorrectDataError"));
			} else {
				HashMap<String, Object> ruleDetails = this.ruleDetailsJPanel.saveToHashMap();
				ruleDetails.put("ruleTypeKey", this.appliedRuleKeyValueComboBox.getSelectedItemKey());
				if(!appliedRuleController.conformRuleConventions(ruleDetails)){
					UiDialogs.errorDialog(this, ServiceProvider.getInstance().getLocaleService().getTranslatedString("RuleNotConformConvention")); // TODO: Add more descriptive errors to the AppliedRule and show them here (appliedRule.toString());
				}else{
					String message = appliedRuleController.saveRule(ruleDetails);
					if (!message.equals("")){
						UiDialogs.errorDialog(this, ServiceProvider.getInstance().getLocaleService().getTranslatedString(message));
					} else {
						this.dispose();
					}
				}
			}
		}
		else if (this.appliedRuleController.getAction().equals(PopUpController.ACTION_EDIT)) {
			if (!ruleDetailsJPanel.hasValidData()) {
				UiDialogs.errorDialog(this, ServiceProvider.getInstance().getLocaleService().getTranslatedString("CorrectDataError"));
			} else {
				HashMap<String, Object> ruleDetails = this.ruleDetailsJPanel.saveToHashMap();
				ruleDetails.put("ruleTypeKey", this.appliedRuleKeyValueComboBox.getSelectedItemKey());
				String message = appliedRuleController.saveRule(ruleDetails);
				if (!message.equals("")){
					UiDialogs.errorDialog(this, ServiceProvider.getInstance().getLocaleService().getTranslatedString(message));
					if (message.equals("IncorrectToModuleFacadeConvExc")){
						this.removeException();
					}
				} else {
					this.dispose();
				}
			}
		}
	}

	public void keyPressed(KeyEvent arg0) {
		// Ignore
	}

	public void keyReleased(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
			this.dispose();
		} else if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
			this.save();
		}
	}

	public void keyTyped(KeyEvent arg0) {
		// Ignore
	}

	private void setButtonEnableState() {
		if (appliedRuleController.hasSelectedRuleTypeHaveExceptions()){
			this.jButtonAddExceptionRow.setEnabled(true);
			this.jButtonRemoveExceptionRow.setEnabled(true);
		} else {
			this.jButtonAddExceptionRow.setEnabled(false);
			this.jButtonRemoveExceptionRow.setEnabled(false);
		}
	}
}
