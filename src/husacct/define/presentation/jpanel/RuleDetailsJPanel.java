package husacct.define.presentation.jpanel;

import husacct.define.presentation.helper.DataHelper;
import husacct.define.task.AppliedRuleController;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class RuleDetailsJPanel extends JPanel{
	
	private static final long serialVersionUID = 3058476011519393145L;
	private AppliedRuleController appliedRulesController;
	
	private JLabel jLabelModuleFrom;
	private JLabel jLabelModuleTo;
	private JLabel jLabelEnabled;
	private JLabel jLabelDescription;
	private JScrollPane jScrollPaneDescription;
//	private JLabel jLabelRegex;
	
	public JCheckBox jCheckBoxEnabled;
	public JComboBox jComboBoxModuleFrom;
	public JComboBox jComboBoxModuleTo;
	public JTextArea jTextAreaDescription;
	public JTextField jTextFieldRegex;
	
	public RuleDetailsJPanel(AppliedRuleController appliedRulesController) {
		super();
		this.appliedRulesController = appliedRulesController;
	}

	/**
	 * Creating Gui
	 */
	public void initGui(String ruleTypeKey) {
		try {
			GridBagLayout jPanel1Layout = new GridBagLayout();
			jPanel1Layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.1 };
			jPanel1Layout.rowHeights = new int[] { 23, 23, 29, 7 };
			jPanel1Layout.columnWeights = new double[] { 0.0, 0.1 };
			jPanel1Layout.columnWidths = new int[] { 132, 7 };
			this.setLayout(jPanel1Layout);
			this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			
			initDetails(ruleTypeKey);
		} catch (Exception e) {
			// add your error handling code here
			e.printStackTrace();
		}
	}
	
	public HashMap<String, Object> saveToHashMap(){
		HashMap<String, Object> ruleDetails = saveDefaultDataToHashMap();
		
		String ruleTypeKey = appliedRulesController.getSelectedRuleTypeKey();
		if (ruleTypeKey.equals("IsNotAllowedToUse")){
			saveIsNotAllowedToUse(ruleDetails);
		} else if (ruleTypeKey.equals("IsAllowedToUse")){
			saveIsAllowedToUse(ruleDetails);
		} else if (ruleTypeKey.equals("IsOnlyAllowedToUse")){
			saveIsOnlyAllowedToUse(ruleDetails);
		} else if (ruleTypeKey.equals("IsOnlyModuleAllowedToUse")){
			saveIsOnlyModuleAllowedToUse(ruleDetails);
		} else if (ruleTypeKey.equals("MustUse")){
			saveMustUse(ruleDetails);
		} else if (ruleTypeKey.equals("BackCall")){
			saveBackCall(ruleDetails);
		} else if (ruleTypeKey.equals("SkipCall")){
			saveSkipCall(ruleDetails);
		}

		return ruleDetails;
	}

	private HashMap<String, Object> saveDefaultDataToHashMap() {
		HashMap<String, Object> ruleDetails = new HashMap<String, Object>();
		long moduleFromId = -1;
		long moduleToId = -1;
		boolean enabled = true;
		String description = "";
		String regex = "";
		
		ruleDetails.put("moduleFromId", moduleFromId);
		ruleDetails.put("moduleToId", moduleToId);
		ruleDetails.put("enabled", enabled);
		ruleDetails.put("description", description);
		ruleDetails.put("regex", regex);
		return ruleDetails;
	}





	private void saveIsNotAllowedToUse(HashMap<String, Object> ruleDetails) {
		DataHelper datahelper1 = (DataHelper) jComboBoxModuleFrom.getSelectedItem();
		ruleDetails.put("moduleFromId", datahelper1.getId());
		DataHelper datahelper = (DataHelper) jComboBoxModuleTo.getSelectedItem();
		ruleDetails.put("moduleToId", datahelper.getId());
		ruleDetails.put("enabled", jCheckBoxEnabled.isSelected());
		ruleDetails.put("description", jTextAreaDescription.getText());
	}

	private void saveIsAllowedToUse(HashMap<String, Object> ruleDetails) {
		DataHelper datahelper1 = (DataHelper) jComboBoxModuleFrom.getSelectedItem();
		ruleDetails.put("moduleFromId", datahelper1.getId());
		DataHelper datahelper = (DataHelper) jComboBoxModuleTo.getSelectedItem();
		ruleDetails.put("moduleToId", datahelper.getId());
		ruleDetails.put("enabled", jCheckBoxEnabled.isSelected());
		ruleDetails.put("description", jTextAreaDescription.getText());
	}
	
	private void saveIsOnlyAllowedToUse(HashMap<String, Object> ruleDetails) {
		DataHelper datahelper1 = (DataHelper) jComboBoxModuleFrom.getSelectedItem();
		ruleDetails.put("moduleFromId", datahelper1.getId());
		DataHelper datahelper = (DataHelper) jComboBoxModuleTo.getSelectedItem();
		ruleDetails.put("moduleToId", datahelper.getId());
		ruleDetails.put("enabled", jCheckBoxEnabled.isSelected());
		ruleDetails.put("description", jTextAreaDescription.getText());
	}

	private void saveIsOnlyModuleAllowedToUse(HashMap<String, Object> ruleDetails) {
		DataHelper datahelper1 = (DataHelper) jComboBoxModuleFrom.getSelectedItem();
		ruleDetails.put("moduleFromId", datahelper1.getId());
		DataHelper datahelper = (DataHelper) jComboBoxModuleTo.getSelectedItem();
		ruleDetails.put("moduleToId", datahelper.getId());
		ruleDetails.put("enabled", jCheckBoxEnabled.isSelected());
		ruleDetails.put("description", jTextAreaDescription.getText());
	}
	private void saveMustUse(HashMap<String, Object> ruleDetails) {
		DataHelper datahelper1 = (DataHelper) jComboBoxModuleFrom.getSelectedItem();
		ruleDetails.put("moduleFromId", datahelper1.getId());
		DataHelper datahelper = (DataHelper) jComboBoxModuleTo.getSelectedItem();
		ruleDetails.put("moduleToId", datahelper.getId());
		ruleDetails.put("enabled", jCheckBoxEnabled.isSelected());
		ruleDetails.put("description", jTextAreaDescription.getText());
	}

	private void saveBackCall(HashMap<String, Object> ruleDetails) {
		DataHelper datahelper1 = (DataHelper) jComboBoxModuleFrom.getSelectedItem();
		ruleDetails.put("moduleFromId", datahelper1.getId());
		ruleDetails.put("enabled", jCheckBoxEnabled.isSelected());
		ruleDetails.put("description", jTextAreaDescription.getText());
	}
	private void saveSkipCall(HashMap<String, Object> ruleDetails) {
		DataHelper datahelper1 = (DataHelper) jComboBoxModuleFrom.getSelectedItem();
		ruleDetails.put("moduleFromId", datahelper1.getId());
		ruleDetails.put("enabled", jCheckBoxEnabled.isSelected());
		ruleDetails.put("description", jTextAreaDescription.getText());
	}

	/**
	 * Creating GUI for the right rule type
	 */
	private void initDetails(String ruleTypeKey){
		this.removeAll();
		
		if (ruleTypeKey.equals("IsNotAllowedToUse")){
			initIsNotAllowedToUse();
		} else if (ruleTypeKey.equals("IsAllowedToUse")){
			initIsAllowedToUse();
		} else if (ruleTypeKey.equals("IsOnlyAllowedToUse")){
			initIsOnlyAllowedToUse();
		} else if (ruleTypeKey.equals("IsOnlyModuleAllowedToUse")){
			initIsOnlyModuleAllowedToUse();
		} else if (ruleTypeKey.equals("MustUse")){
			initMustUse();
		} else if (ruleTypeKey.equals("BackCall")){
			initBackCall();
		} else if (ruleTypeKey.equals("SkipCall")){
			initSkipCall();
		}//TODO complement with remaining ruletypes
	}
	
	private void initIsNotAllowedToUse(){
		addModuleFromLabel(new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		addModuleTo(new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		addEnabled(new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		addDescription(new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.setSize(400, 350);
	}
	private void initIsAllowedToUse(){
		addModuleFromLabel(new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		addModuleTo(new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		addEnabled(new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		addDescription(new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.setSize(400, 350);
	}
	private void initIsOnlyAllowedToUse(){
		addModuleFromLabel(new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		addModuleTo(new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		addEnabled(new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		addDescription(new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.setSize(400, 350);
	}
	private void initIsOnlyModuleAllowedToUse(){
		addModuleFromLabel(new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		addModuleTo(new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		addEnabled(new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		addDescription(new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.setSize(400, 350);
	}
	private void initMustUse(){
		addModuleFromLabel(new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		addModuleTo(new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		addEnabled(new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		addDescription(new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.setSize(400, 350);
	}
	private void initBackCall(){
		addModuleFromLabel(new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		addEnabled(new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		addDescription(new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.setSize(400, 350);
	}
	private void initSkipCall(){
		addModuleFromLabel(new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		addEnabled(new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		addDescription(new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.setSize(400, 350);
	}
	
	private void addModuleFromLabel(GridBagConstraints gbc){
		{
			jLabelModuleFrom = new JLabel();
			this.add(jLabelModuleFrom, gbc);
			jLabelModuleFrom.setText("From Module");
		}
		gbc.gridx++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		{
			jComboBoxModuleFrom = new JComboBox();
			this.add(jComboBoxModuleFrom, gbc);
			String currentModuleName = appliedRulesController.getCurrentModuleName();
			Long currentModuleID = appliedRulesController.getCurrentModuleId();
			
			ComboBoxModel comboBoxModel = new DefaultComboBoxModel();
			DataHelper datahelper = new DataHelper();
			datahelper.setId(currentModuleID);
			datahelper.setValue(currentModuleName);
			comboBoxModel = new DefaultComboBoxModel(new DataHelper[]{datahelper});
			
			jComboBoxModuleFrom.setModel(comboBoxModel);	
			
		}
	}
	
	private void addModuleTo(GridBagConstraints gbc){
		{
			jLabelModuleTo = new JLabel();
			this.add(jLabelModuleTo, gbc);
			jLabelModuleTo.setText("To Module");
		}
		gbc.gridx++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		{
			jComboBoxModuleTo = new JComboBox();
			this.add(jComboBoxModuleTo, gbc);
			jComboBoxModuleTo.setModel(this.appliedRulesController.loadModulesToCombobox());	
		}
	}
	
	private void addEnabled(GridBagConstraints gbc){
		{
			jLabelEnabled = new JLabel();
			this.add(jLabelEnabled, gbc);
			jLabelEnabled.setText("Enabled");
		}
		gbc.gridx++;
		{
			jCheckBoxEnabled = new JCheckBox();
			jCheckBoxEnabled.setSelected(true);
			this.add(jCheckBoxEnabled, gbc);
		}
	}
	
	private void addDescription(GridBagConstraints gbc){
		{
			jLabelDescription = new JLabel();
			this.add(jLabelDescription, gbc);
			jLabelDescription.setText("Description");
		}
		gbc.gridx++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		{
			jTextAreaDescription = new JTextArea(5, 50);
			jTextAreaDescription.setText("");
			jScrollPaneDescription = new JScrollPane(jTextAreaDescription);
			this.add(jScrollPaneDescription, gbc);	
		}
	}
	
}
