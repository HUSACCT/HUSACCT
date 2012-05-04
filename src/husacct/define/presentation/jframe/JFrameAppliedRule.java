package husacct.define.presentation.jframe;

import husacct.define.presentation.helper.DataHelper;
import husacct.define.presentation.jpanel.RuleDetailsJPanel;
import husacct.define.presentation.tables.JTableException;
import husacct.define.presentation.tables.JTableTableModel;
import husacct.define.presentation.utils.KeyValueComboBox;
import husacct.define.task.AppliedRuleController;

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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;


public class JFrameAppliedRule extends JFrame implements KeyListener, ActionListener, ItemListener, Observer{

	private static final long serialVersionUID = -3491664038962722000L;
	
	private AppliedRuleController appliedRuleController;
	
	private JPanel jPanelMain;
	private RuleDetailsJPanel jPanelRuleDetails;
	private JPanel jPanelExceptionTable;
	private JPanel jPanelExceptionButtons;
	private JPanel jPanelUpdateCancel;
	
	private JLabel jLabelRuleType;
	private JScrollPane jScrollPane1;
	private JLabel jLabelExceptions;
		
	public KeyValueComboBox keyValueComboBoxAppliedRule;

	public JTableException jTableException;
	public JButton jButtonAddExceptionRow;
	public JButton jButtonRemoveExceptionRow;
		
	public JButton jButtonCancel;
	public JButton jButtonSave;

	/**
	 * Constructor
	 */
	public JFrameAppliedRule(AppliedRuleController appliedRuleController) {
		super();
		this.appliedRuleController = appliedRuleController;
		this.appliedRuleController.addObserver(this);
		initGUI();
	}

	/**
	 * Creating Gui
	 */
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setTitle("New Applied Rule");
			setIconImage(new ImageIcon(getClass().getClassLoader().getResource("husacct/define/presentation/resources/jframeicon.jpg")).getImage());
			{
				jPanelMain = new JPanel();
				GridBagLayout jPanel1Layout = new GridBagLayout();
				jPanel1Layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.1 };
				jPanel1Layout.rowHeights = new int[] { 30, 23, 6, 0 };
				jPanel1Layout.columnWeights = new double[] { 0.0, 0.1 };
				jPanel1Layout.columnWidths = new int[] { 132, 7 };
				getContentPane().add(jPanelMain, BorderLayout.CENTER);
				
				jPanelMain.setLayout(jPanel1Layout);
				jPanelMain.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
				{
					jLabelRuleType = new JLabel();
					jPanelMain.add(jLabelRuleType, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabelRuleType.setText("RuleType");
				}
				{
					keyValueComboBoxAppliedRule = new KeyValueComboBox();
					jPanelMain.add(keyValueComboBoxAppliedRule, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					this.appliedRuleController.fillRuleTypeComboBox(keyValueComboBoxAppliedRule);
					keyValueComboBoxAppliedRule.addItemListener(this);
				}
				{
					jPanelRuleDetails = new RuleDetailsJPanel(appliedRuleController);
					String ruleTypeKey = keyValueComboBoxAppliedRule.getSelectedItemKey();
					appliedRuleController.setSelectedRuleTypeKey(ruleTypeKey);
					jPanelRuleDetails.initGui(ruleTypeKey);
					jPanelMain.add(jPanelRuleDetails, new GridBagConstraints(0, 1, GridBagConstraints.REMAINDER, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				}
				{
					jLabelExceptions = new JLabel();
					jPanelMain.add(jLabelExceptions, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabelExceptions.setText("Exceptions");
				}
				{
					jPanelExceptionTable = new JPanel();
					BorderLayout jPanel3Layout = new BorderLayout();
					jPanelExceptionTable.setLayout(jPanel3Layout);
					jPanelMain.add(jPanelExceptionTable, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					{
						jScrollPane1 = new JScrollPane();
						jPanelExceptionTable.add(jScrollPane1, BorderLayout.CENTER);
						jScrollPane1.setPreferredSize(new java.awt.Dimension(516, 155));
						{
							jTableException = new JTableException();
							jTableException.setSize(516, 155);
							jScrollPane1.setViewportView(jTableException);
						}
					}
					{
						jPanelExceptionButtons = new JPanel();
						GridBagLayout jPanel4Layout = new GridBagLayout();
						jPanelExceptionTable.add(jPanelExceptionButtons, BorderLayout.EAST);
						jPanel4Layout.rowWeights = new double[] { 0.0, 0.1 };
						jPanel4Layout.rowHeights = new int[] { 15, 7 };
						jPanel4Layout.columnWeights = new double[] { 0.1 };
						jPanel4Layout.columnWidths = new int[] { 7 };
						jPanelExceptionButtons.setLayout(jPanel4Layout);
						jPanelExceptionButtons.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
						{
							jButtonAddExceptionRow = new JButton();
							jPanelExceptionButtons.add(jButtonAddExceptionRow, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
							jButtonAddExceptionRow.setText("Add exception");
							jButtonAddExceptionRow.addActionListener(this);
						}
						{
							jButtonRemoveExceptionRow = new JButton();
							jPanelExceptionButtons.add(jButtonRemoveExceptionRow, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
							jButtonRemoveExceptionRow.setText("Remove exception");
							jButtonRemoveExceptionRow.addActionListener(this);
						}
					}
				}
			}
			{
				jPanelUpdateCancel = new JPanel();
				getContentPane().add(jPanelUpdateCancel, BorderLayout.SOUTH);
				{
					jButtonCancel = new JButton();
					jPanelUpdateCancel.add(jButtonCancel);
					jButtonCancel.setText("Cancel");
					jButtonCancel.addActionListener(this);
				}
				{
					jButtonSave = new JButton();
					jPanelUpdateCancel.add(jButtonSave);
					jButtonSave.setText("Add");
					jButtonSave.addActionListener(this);
				}

			}
			this.setResizable(false);
			pack();
			this.setSize(815, 435);
		} catch (Exception e) {
			// add your error handling code here
			e.printStackTrace();
		}
	}
	
	/**
	 * Handling Updating Observerable
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		updateExceptionTable();
	}
	
	private void updateExceptionTable() {

		// Get all applied rules from the service
		ArrayList<HashMap<String, Object>> exceptionRules = appliedRuleController.getExceptionRules();

		// Get the tablemodel from the table
		JTableTableModel atm = (JTableTableModel) jTableException.getModel();

		// Remove all items in the table
		atm.getDataVector().removeAllElements();
		for (HashMap<String, Object> exceptionRule : exceptionRules) {	
			String description = (String) exceptionRule.get("description");
			Long moduleIdFrom = (Long) exceptionRule.get("moduleFromId");
			String moduleFrom = appliedRuleController.getModuleName(moduleIdFrom);
			Long moduleIdTo = (Long) exceptionRule.get("moduleToId");
			String moduleTo = appliedRuleController.getModuleName(moduleIdTo);
			
			boolean appliedRuleIsEnabled = (Boolean) exceptionRule.get("enabled");
			String enabled = "Off";
			if (appliedRuleIsEnabled) {
				enabled = "On";
			}

			Object rowdata[] = {moduleFrom, moduleTo, description, enabled};
			atm.addRow(rowdata);
		}
		atm.fireTableDataChanged();
		this.repaint();
	}

	/**
	 * Handling ActionPerformed
	 */
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
		//TODO ugly code
		Long selectedModuleFromId = appliedRuleController.getCurrentModuleId();
		DataHelper datahelper = (DataHelper) jPanelRuleDetails.jComboBoxModuleTo.getSelectedItem();
		Long selectedModuleToId = datahelper.getId();
		appliedRuleController.createExceptionGUI(selectedModuleFromId, selectedModuleToId);
	}
	
	private void removeException() {
//		appliedRuleController.removeException(exceptionRuleId);
	}

	/**
	 * Handling ItemListener
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == keyValueComboBoxAppliedRule){
			String ruleTypeKey = keyValueComboBoxAppliedRule.getSelectedItemKey();
			appliedRuleController.setSelectedRuleTypeKey(ruleTypeKey);
			jPanelRuleDetails.initGui(ruleTypeKey);
		}
	}

	private void cancel() {
		this.dispose();
	}

	private void save() {	
		String ruleTypeKey = keyValueComboBoxAppliedRule.getSelectedItemKey();
		HashMap<String, Object> ruleDetails = jPanelRuleDetails.saveToHashMap();
		long moduleFromId = (Long) ruleDetails.get("moduleFromId");
		long moduleToId = (Long) ruleDetails.get("moduleToId");
		boolean isEnabled = (Boolean) ruleDetails.get("enabled");
		String description = (String) ruleDetails.get("description");
		String regex = (String) ruleDetails.get("regex");
		//TODO dependencies
		String[] dependencies = new String[]{};
		
		this.appliedRuleController.save(ruleTypeKey, description,dependencies, regex, moduleFromId, moduleToId, isEnabled);
		this.dispose();
	}
	
	/**
	 * Handling KeyPresses
	 */
	public void keyPressed(KeyEvent arg0) {
		// Ignore
	}

	public void keyReleased(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
			this.dispose();
		}
	}

	public void keyTyped(KeyEvent arg0) {
		// Ignore
	}


}
