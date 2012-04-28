package husacct.define.presentation.jframe;

import husacct.define.presentation.helper.DataHelper;
import husacct.define.presentation.tables.JTableException;
import husacct.define.presentation.utils.KeyValueComboBox;
import husacct.define.task.AppliedRulesController;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;


public class JFrameAppliedRules extends JFrame implements KeyListener, ActionListener{

	private static final long serialVersionUID = -3491664038962722000L;
	private JPanel jPanelMain;
	private JLabel jLabelModuleTo;
	private JLabel jLabelModuleFrom;
	private JLabel jLabel2;
	private JLabel jLabelExceptions;
	private JPanel jPanel3;
	private JScrollPane jScrollPane1;
	public JCheckBox jCheckBoxEnabled;
	private JLabel jLabelRuleType;
	public JComboBox jComboBoxModuleFrom;
	public JComboBox jComboBoxModuleTo;
	public KeyValueComboBox keyValueComboBoxAppliedRule;
	public JTableException jTableException;
	private JPanel jPanelExceptionButtons;
	public JButton jButtonAddExceptionRow;
	public JButton jButtonRemoveExceptionRow;
	private JPanel jPanelUpdateCancel;
	public JButton jButtonCancel;
	public JButton jButtonSave;

	private AppliedRulesController appliedRulesController;
	
	{
		// Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */

	/**
	 * Auto-generated main method to display this JFrame
	 */

	public JFrameAppliedRules(AppliedRulesController appliedRulesController) {
		super();
		this.appliedRulesController = appliedRulesController;
		initGUI();
		loadSelectBoxes();
		fillRuleTypeCombobox();
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
				jPanel1Layout.rowHeights = new int[] { 23, 23, 29, 7 };
				jPanel1Layout.columnWeights = new double[] { 0.0, 0.1 };
				jPanel1Layout.columnWidths = new int[] { 132, 7 };
				getContentPane().add(jPanelMain, BorderLayout.CENTER);
				jPanelMain.setLayout(jPanel1Layout);
				jPanelMain.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
//				{
//					jLabelModuleFrom = new JLabel();
//					jPanel1.add(jLabelModuleFrom, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
//					jLabelModuleFrom.setText("From Module");
//				}
				{
					jLabelModuleTo = new JLabel();
					jPanelMain.add(jLabelModuleTo, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabelModuleTo.setText("To Module");
				}
				{
					jLabel2 = new JLabel();
					jPanelMain.add(jLabel2, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabel2.setText("Enabled");
				}
				{
					jLabelExceptions = new JLabel();
					jPanelMain.add(jLabelExceptions, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabelExceptions.setText("Exceptions");
				}
				{
					jPanel3 = new JPanel();
					BorderLayout jPanel3Layout = new BorderLayout();
					jPanel3.setLayout(jPanel3Layout);
					jPanelMain.add(jPanel3, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					{
						jScrollPane1 = new JScrollPane();
						jPanel3.add(jScrollPane1, BorderLayout.CENTER);
						jScrollPane1.setPreferredSize(new java.awt.Dimension(516, 55));
						{
							jTableException = new JTableException();
							jScrollPane1.setViewportView(jTableException);
						}
					}
					{
						jPanelExceptionButtons = new JPanel();
						GridBagLayout jPanel4Layout = new GridBagLayout();
						jPanel3.add(jPanelExceptionButtons, BorderLayout.EAST);
						jPanel4Layout.rowWeights = new double[] { 0.0, 0.1 };
						jPanel4Layout.rowHeights = new int[] { 15, 7 };
						jPanel4Layout.columnWeights = new double[] { 0.1 };
						jPanel4Layout.columnWidths = new int[] { 7 };
						jPanelExceptionButtons.setLayout(jPanel4Layout);
						jPanelExceptionButtons.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
						{
							jButtonAddExceptionRow = new JButton();
							jButtonAddExceptionRow.setEnabled(false);
							jPanelExceptionButtons.add(jButtonAddExceptionRow, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
							jButtonAddExceptionRow.setText("Add row");
						}
						{
							jButtonRemoveExceptionRow = new JButton();
							jButtonRemoveExceptionRow.setEnabled(false);
							jPanelExceptionButtons.add(jButtonRemoveExceptionRow, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
							jButtonRemoveExceptionRow.setText("Remove row");
						}
					}
				}
				{
					jLabelRuleType = new JLabel();
					jPanelMain.add(jLabelRuleType, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabelRuleType.setText("RuleType");
				}
				{
					ComboBoxModel jComboBox1Model = new DefaultComboBoxModel(new Object[] {});
					keyValueComboBoxAppliedRule = new KeyValueComboBox();
					jPanelMain.add(keyValueComboBoxAppliedRule, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					keyValueComboBoxAppliedRule.setModel(jComboBox1Model);
				}
				{
					ComboBoxModel jComboBox2Model = new DefaultComboBoxModel(new String[] { });
					jComboBoxModuleTo = new JComboBox();
					jPanelMain.add(jComboBoxModuleTo, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					jComboBoxModuleTo.setModel(jComboBox2Model);
				}
				{
					jCheckBoxEnabled = new JCheckBox();
					jCheckBoxEnabled.setSelected(true);
					jPanelMain.add(jCheckBoxEnabled, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
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
//			jPanelMain.setBackground(Color.blue);
//			jPanelUpdateCancel.setBackground(Color.cyan);
//			jPanel3.setBackground(Color.green);
//			jPanelExceptionButtons.setBackground(Color.red);
			pack();
			this.setSize(677, 300);
		} catch (Exception e) {
			// add your error handling code here
			e.printStackTrace();
		}
	}
	
	private void loadSelectBoxes() {
		jComboBoxModuleTo.setModel(this.appliedRulesController.loadModuleToCombobox());
	}
	
	private void fillRuleTypeCombobox() {
		this.appliedRulesController.fillRuleTypeComboBox(keyValueComboBoxAppliedRule);
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

	private void cancel() {
		this.dispose();
	}

	private void save() {	
		String ruleTypeKey = keyValueComboBoxAppliedRule.getSelectedItemKey();
		String description = "";
		String[] dependencies = {};
		String regex = "";
		long moduleToId = ((DataHelper) jComboBoxModuleTo.getSelectedItem()).getId();
		boolean isEnabled = jCheckBoxEnabled.isSelected();
		
		this.appliedRulesController.save(ruleTypeKey, description,dependencies, regex, moduleToId, isEnabled);
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
