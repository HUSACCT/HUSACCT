package husacct.define.presentation.jframe;

import husacct.define.presentation.jpanel.ruledetails.RuleDetailsJPanel;
import husacct.define.presentation.utils.KeyValueComboBox;
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
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class JFrameExceptionRule  extends JFrame implements KeyListener, ActionListener, ItemListener{

	private static final long serialVersionUID = -3491664038962722000L;
	
	private AppliedRuleController appliedRuleController;
	private JFrameAppliedRule appliedRuleFrame;
	private JPanel jPanelMain;
	private JLabel jLabelRuleType;
	private JPanel jPanelUpdateCancel;
		
	public RuleDetailsJPanel jPanelRuleDetails;
	public KeyValueComboBox keyValueComboBoxExceptionRule;
	public JButton jButtonCancel;
	public JButton jButtonSave;

	/**
	 * Constructor
	 */
	public JFrameExceptionRule(AppliedRuleController appliedRulesController, JFrameAppliedRule appliedRuleFrame, Long selectedModuleFromId, Long selectedModuleToId) {
		super();
		this.appliedRuleController = appliedRulesController;
		this.appliedRuleFrame = appliedRuleFrame;
		
		initGUI();
		loadComboboxes(selectedModuleFromId, selectedModuleToId);
		this.setTextures();
	}

	private void loadComboboxes(Long selectedModuleFromId, Long selectedModuleToId) {
		jPanelRuleDetails.fromModuleJComboBox.setModel(appliedRuleController.loadsubModulesToCombobox(selectedModuleFromId));
		jPanelRuleDetails.toModuleJComboBox.setModel(appliedRuleController.loadsubModulesToCombobox(selectedModuleToId));
	}
	
	private void setTextures() {
		if (this.appliedRuleController.getAction().equals(PopUpController.ACTION_NEW)) {
			this.jButtonSave.setText("Create");
			this.setTitle("New exception rule");
		} else if (this.appliedRuleController.getAction().equals(PopUpController.ACTION_EDIT)) {
			this.jButtonSave.setText("Save");
			this.setTitle("Edit exception rule");
		}
	}

	/**
	 * Creating Gui
	 */
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setTitle("New Exception Rule");
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
					keyValueComboBoxExceptionRule = new KeyValueComboBox();
					jPanelMain.add(keyValueComboBoxExceptionRule, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					this.appliedRuleController.fillRuleTypeComboBoxWithExceptions(keyValueComboBoxExceptionRule);
					keyValueComboBoxExceptionRule.addItemListener(this);	
				}
				{
					jPanelRuleDetails = new RuleDetailsJPanel(appliedRuleController);
					String ruleTypeKey = keyValueComboBoxExceptionRule.getSelectedItemKey();
					jPanelRuleDetails.initGui(ruleTypeKey);
					jPanelMain.add(jPanelRuleDetails, new GridBagConstraints(0, 1, GridBagConstraints.REMAINDER, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
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
			this.setSize(740, 280);
		} catch (Exception e) {
			// add your error handling code here
			e.printStackTrace();
		}
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
		}
	}
	/**
	 * Handling ItemListener
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == keyValueComboBoxExceptionRule){
			String ruleTypeKey = keyValueComboBoxExceptionRule.getSelectedItemKey();
			appliedRuleController.setSelectedRuleTypeKey(ruleTypeKey);
			jPanelRuleDetails.initGui(ruleTypeKey);
		}
	}

	private void cancel() {
		this.dispose();
	}

	private void save() {	
		HashMap<String, Object> ruleDetails = jPanelRuleDetails.saveToHashMap();
		String ruleTypeKey = this.keyValueComboBoxExceptionRule.getSelectedItemKey();
		ruleDetails.put("ruleTypeKey", ruleTypeKey);
		this.appliedRuleController.addException(ruleDetails);
		this.appliedRuleFrame.updateExceptionTable();
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
