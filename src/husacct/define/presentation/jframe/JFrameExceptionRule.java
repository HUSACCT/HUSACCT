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
	public RuleDetailsJPanel ruleDetailsPanel;
	public KeyValueComboBox exceptionRuleKeyValueComboBox;
		
	public JButton cancelButton;
	public JButton saveButton;

	/**
	 * Constructor
	 */
	public JFrameExceptionRule(AppliedRuleController appliedRulesController, JFrameAppliedRule appliedRuleFrame, Long selectedModuleFromId, Long selectedModuleToId) {
		super();
		this.appliedRuleController = appliedRulesController;
		this.appliedRuleFrame = appliedRuleFrame;
		
		this.initGUI();
		this.loadComboboxes(selectedModuleFromId, selectedModuleToId);
		this.setTextures();
	}

	private void loadComboboxes(Long selectedModuleFromId, Long selectedModuleToId) {
		this.ruleDetailsPanel.fromModuleJComboBox.setModel(appliedRuleController.loadsubModulesToCombobox(selectedModuleFromId));
		this.ruleDetailsPanel.toModuleJComboBox.setModel(appliedRuleController.loadsubModulesToCombobox(selectedModuleToId));
	}
	
	private void setTextures() {
		if (this.appliedRuleController.getAction().equals(PopUpController.ACTION_NEW)) {
			this.saveButton.setText("Create");
			this.setTitle("New exception rule");
		} else if (this.appliedRuleController.getAction().equals(PopUpController.ACTION_EDIT)) {
			this.saveButton.setText("Save");
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
			
			getContentPane().add(this.createMainPanel(), BorderLayout.CENTER);
			getContentPane().add(this.createButtonPanel(), BorderLayout.SOUTH);
			
			this.setResizable(false);
			pack();
			this.setSize(740, 280);
		} catch (Exception e) {
			// add your error handling code here
			e.printStackTrace();
		}
	}
	
	private JPanel createMainPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(this.createMainPanelLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		
		mainPanel.add(new JLabel("RuleType"), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.createAppliedRuleKeyValueComboBox();
		mainPanel.add(this.exceptionRuleKeyValueComboBox, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.createRuleDetailPanel();
		mainPanel.add(this.ruleDetailsPanel, new GridBagConstraints(0, 1, GridBagConstraints.REMAINDER, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		
		return mainPanel;
	}
	
	private GridBagLayout createMainPanelLayout() {
		GridBagLayout mainPanelLayout = new GridBagLayout();
		mainPanelLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.1 };
		mainPanelLayout.rowHeights = new int[] { 30, 23, 6, 0 };
		mainPanelLayout.columnWeights = new double[] { 0.0, 0.1 };
		mainPanelLayout.columnWidths = new int[] { 132, 7 };
		return mainPanelLayout;
	}
	
	private void createAppliedRuleKeyValueComboBox() {
		this.exceptionRuleKeyValueComboBox = new KeyValueComboBox();
		this.appliedRuleController.fillRuleTypeComboBoxWithExceptions(this.exceptionRuleKeyValueComboBox);
		this.exceptionRuleKeyValueComboBox.addItemListener(this);
	}
	
	private void createRuleDetailPanel() {
		this.ruleDetailsPanel = new RuleDetailsJPanel(appliedRuleController);
		String ruleTypeKey = this.exceptionRuleKeyValueComboBox.getSelectedItemKey();
		this.ruleDetailsPanel.initGui(ruleTypeKey);
	}
	
	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel();
		
		this.cancelButton = new JButton("Cancel");
		buttonPanel.add(this.cancelButton);
		this.cancelButton.addActionListener(this);
		
		this.saveButton = new JButton("Add");
		buttonPanel.add(this.saveButton);
		this.saveButton.addActionListener(this);
		
		return buttonPanel;
	}
	
	/**
	 * Handling ActionPerformed
	 */
	@Override
	public void actionPerformed(ActionEvent action) {
		if (action.getSource() == this.saveButton) {
			this.save();
		} else if (action.getSource() == this.cancelButton) {
			this.cancel();
		}
	}
	/**
	 * Handling ItemListener
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == this.exceptionRuleKeyValueComboBox){
			String ruleTypeKey = this.exceptionRuleKeyValueComboBox.getSelectedItemKey();
			this.appliedRuleController.setSelectedRuleTypeKey(ruleTypeKey);
			this.ruleDetailsPanel.initGui(ruleTypeKey);
		}
	}

	private void cancel() {
		this.dispose();
	}

	private void save() {	
		HashMap<String, Object> ruleDetails = this.ruleDetailsPanel.saveToHashMap();
		String ruleTypeKey = this.exceptionRuleKeyValueComboBox.getSelectedItemKey();
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
		} else if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
			this.save();
		}
	}

	public void keyTyped(KeyEvent arg0) {
		// Ignore
	}
}
