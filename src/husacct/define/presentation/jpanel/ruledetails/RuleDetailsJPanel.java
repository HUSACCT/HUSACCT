package husacct.define.presentation.jpanel.ruledetails;

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

import org.apache.log4j.Logger;

public class RuleDetailsJPanel extends JPanel{
	
	private static final long serialVersionUID = 3058476011519393145L;
	
	private AppliedRuleController appliedRulesController;
	private Logger logger;
	
	private JLabel fromModuleLabel;
	private JLabel toModuleLabel;
	private JLabel ruleEnabledLabel;
	private JLabel descriptionLabel;
	
	public JComboBox fromModuleJComboBox;
	public JComboBox toModuleJComboBox;
	public JCheckBox ruleEnabledCheckBox;
	public JTextArea descriptionTextArea;
	public JTextField regexTextField;
	
	public RuleDetailsJPanel(AppliedRuleController appliedRulesController) {
		super();
		this.appliedRulesController = appliedRulesController;
		this.logger = Logger.getLogger(RuleDetailsJPanel.class);
	}

	public void initGui(String ruleTypeKey) {
		try {
			this.setLayout(this.createRuleDetailsLayout());
			this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			this.initDetails(ruleTypeKey);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private GridBagLayout createRuleDetailsLayout() {
		GridBagLayout ruleDetailsLayout = new GridBagLayout();
		ruleDetailsLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.1 };
		ruleDetailsLayout.rowHeights = new int[] { 23, 23, 29, 7 };
		ruleDetailsLayout.columnWeights = new double[] { 0.0, 0.1 };
		ruleDetailsLayout.columnWidths = new int[] { 132, 7 };
		return ruleDetailsLayout;
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
		
		// updating panel!
		if(this.getComponentCount() > 0) {
			this.getRootPane().revalidate();
		}
	}
	
	private void initIsNotAllowedToUse(){
		this.addFromModuleComponents(new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.addToModuleComponents(new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.addEnabledComponents(new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.addDescriptionComponents(new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.setSize(400, 350);
	}
	private void initIsAllowedToUse(){
		this.addFromModuleComponents(new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.addToModuleComponents(new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.addEnabledComponents(new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.addDescriptionComponents(new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.setSize(400, 350);
	}
	private void initIsOnlyAllowedToUse(){
		this.addFromModuleComponents(new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.addToModuleComponents(new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.addEnabledComponents(new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.addDescriptionComponents(new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.setSize(400, 350);
	}
	private void initIsOnlyModuleAllowedToUse(){
		this.addFromModuleComponents(new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.addToModuleComponents(new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.addEnabledComponents(new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.addDescriptionComponents(new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.setSize(400, 350);
	}
	private void initMustUse(){
		this.addFromModuleComponents(new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.addToModuleComponents(new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.addEnabledComponents(new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.addDescriptionComponents(new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.setSize(400, 350);
	}
	private void initBackCall(){
		this.addFromModuleComponents(new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.addEnabledComponents(new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.addDescriptionComponents(new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.setSize(400, 350);
	}
	private void initSkipCall(){
		this.addFromModuleComponents(new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.addEnabledComponents(new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.addDescriptionComponents(new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.setSize(400, 350);
	}
	
	private void addFromModuleComponents(GridBagConstraints gridBagConstraints) {
		this.fromModuleLabel = new JLabel("From Module");
		this.add(this.fromModuleLabel, gridBagConstraints);
		gridBagConstraints.gridx++;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		this.createFromModuleJComboBox();
		this.add(this.fromModuleJComboBox, gridBagConstraints);
	}
	
	private void createFromModuleJComboBox() {
		this.fromModuleJComboBox = new JComboBox();
		String currentModuleName = appliedRulesController.getCurrentModuleName();
		Long currentModuleID = appliedRulesController.getCurrentModuleId();
		
		ComboBoxModel comboBoxModel = new DefaultComboBoxModel();
		DataHelper datahelper = new DataHelper();
		datahelper.setId(currentModuleID);
		datahelper.setValue(currentModuleName);
		comboBoxModel = new DefaultComboBoxModel(new DataHelper[]{datahelper});
		
		this.fromModuleJComboBox.setModel(comboBoxModel);
	}
	
	private void addToModuleComponents(GridBagConstraints gridBagConstraints) {
		this.toModuleLabel = new JLabel("To Module");
		this.add(this.toModuleLabel, gridBagConstraints);
		gridBagConstraints.gridx++;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		this.createToModuleJComboBox();
		this.add(this.toModuleJComboBox, gridBagConstraints);
	}
	
	private void createToModuleJComboBox() {
		this.toModuleJComboBox = new JComboBox();
		this.toModuleJComboBox.setModel(this.appliedRulesController.loadModulesToCombobox());
	}
	
	private void addEnabledComponents(GridBagConstraints gridBagConstraints){
		this.ruleEnabledLabel = new JLabel("Enabled");
		this.add(this.ruleEnabledLabel, gridBagConstraints);
		gridBagConstraints.gridx++;
		this.ruleEnabledCheckBox = new JCheckBox();
		this.ruleEnabledCheckBox.setSelected(true);
		this.add(this.ruleEnabledCheckBox, gridBagConstraints);
	}
	
	private void addDescriptionComponents(GridBagConstraints gridBagConstraints){
		this.descriptionLabel = new JLabel("Description");
		this.add(this.descriptionLabel, gridBagConstraints);
		gridBagConstraints.gridx++;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		this.add(this.createDescriptionScrollPane(), gridBagConstraints);
	}
		
	private JScrollPane createDescriptionScrollPane() {
		this.descriptionTextArea = new JTextArea(5, 50);
		this.descriptionTextArea.setText("");
		JScrollPane descriptionScrollPane = new JScrollPane(this.descriptionTextArea);
		return descriptionScrollPane;
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
		DataHelper datahelper1 = (DataHelper) this.fromModuleJComboBox.getSelectedItem();
		ruleDetails.put("moduleFromId", datahelper1.getId());
		DataHelper datahelper = (DataHelper) this.toModuleJComboBox.getSelectedItem();
		ruleDetails.put("moduleToId", datahelper.getId());
		ruleDetails.put("enabled", this.ruleEnabledCheckBox.isSelected());
		ruleDetails.put("description", this.descriptionTextArea.getText());
	}

	private void saveIsAllowedToUse(HashMap<String, Object> ruleDetails) {
		DataHelper datahelper1 = (DataHelper) this.fromModuleJComboBox.getSelectedItem();
		ruleDetails.put("moduleFromId", datahelper1.getId());
		DataHelper datahelper = (DataHelper) toModuleJComboBox.getSelectedItem();
		ruleDetails.put("moduleToId", datahelper.getId());
		ruleDetails.put("enabled", this.ruleEnabledCheckBox.isSelected());
		ruleDetails.put("description", this.descriptionTextArea.getText());
	}
	
	private void saveIsOnlyAllowedToUse(HashMap<String, Object> ruleDetails) {
		DataHelper datahelper1 = (DataHelper) this.fromModuleJComboBox.getSelectedItem();
		ruleDetails.put("moduleFromId", datahelper1.getId());
		DataHelper datahelper = (DataHelper) toModuleJComboBox.getSelectedItem();
		ruleDetails.put("moduleToId", datahelper.getId());
		ruleDetails.put("enabled", this.ruleEnabledCheckBox.isSelected());
		ruleDetails.put("description", this.descriptionTextArea.getText());
	}

	private void saveIsOnlyModuleAllowedToUse(HashMap<String, Object> ruleDetails) {
		DataHelper datahelper1 = (DataHelper) this.fromModuleJComboBox.getSelectedItem();
		ruleDetails.put("moduleFromId", datahelper1.getId());
		DataHelper datahelper = (DataHelper) toModuleJComboBox.getSelectedItem();
		ruleDetails.put("moduleToId", datahelper.getId());
		ruleDetails.put("enabled", this.ruleEnabledCheckBox.isSelected());
		ruleDetails.put("description", this.descriptionTextArea.getText());
	}
	private void saveMustUse(HashMap<String, Object> ruleDetails) {
		DataHelper datahelper1 = (DataHelper) this.fromModuleJComboBox.getSelectedItem();
		ruleDetails.put("moduleFromId", datahelper1.getId());
		DataHelper datahelper = (DataHelper) toModuleJComboBox.getSelectedItem();
		ruleDetails.put("moduleToId", datahelper.getId());
		ruleDetails.put("enabled", this.ruleEnabledCheckBox.isSelected());
		ruleDetails.put("description", this.descriptionTextArea.getText());
	}

	private void saveBackCall(HashMap<String, Object> ruleDetails) {
		DataHelper datahelper1 = (DataHelper) this.fromModuleJComboBox.getSelectedItem();
		ruleDetails.put("moduleFromId", datahelper1.getId());
		ruleDetails.put("enabled", this.ruleEnabledCheckBox.isSelected());
		ruleDetails.put("description", this.descriptionTextArea.getText());
	}
	
	private void saveSkipCall(HashMap<String, Object> ruleDetails) {
		DataHelper datahelper1 = (DataHelper) this.fromModuleJComboBox.getSelectedItem();
		ruleDetails.put("moduleFromId", datahelper1.getId());
		ruleDetails.put("enabled", this.ruleEnabledCheckBox.isSelected());
		ruleDetails.put("description", this.descriptionTextArea.getText());
	}

	public void updateDetails(HashMap<String, Object> ruleDetails) {
			//FIXME
		//update Details
	}
}
