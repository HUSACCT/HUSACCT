package husacct.define.presentation.jpanel.ruledetails.legalitydependency;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import husacct.define.presentation.jpanel.ruledetails.AbstractDetailsJPanel;
import husacct.define.presentation.utils.DataHelper;
import husacct.define.task.AppliedRuleController;

public class IsNotAllowedToUseJPanel extends AbstractDetailsJPanel{
	private static final long serialVersionUID = 6479697173853306907L;
	public static final String ruleTypeKey = "IsNotAllowedToUse";
	
	private JLabel moduleFromLabel;
	private JLabel moduleToLabel;
	private JLabel ruleEnabledLabel;
	private JLabel descriptionLabel;
	
	public JComboBox moduleFromJComboBox;
	public JComboBox moduleToJComboBox;
	public JCheckBox ruleEnabledCheckBox;
	public JTextArea descriptionTextArea;
	
	public IsNotAllowedToUseJPanel(AppliedRuleController appliedRuleController) {
		super(appliedRuleController);
	}

	@Override
	public void initDetails() {
		this.addFromModuleComponents(new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.addToModuleComponents(new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.addEnabledComponents(new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.addDescriptionComponents(new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.setSize(400, 350);
	}

	@Override
	public HashMap<String, Object> saveToHashMap() {
		HashMap<String, Object> ruleDetails = super.saveToHashMap();
		
		DataHelper datahelper1 = (DataHelper) this.moduleFromJComboBox.getSelectedItem();
		ruleDetails.put("moduleFromId", datahelper1.getId());
		DataHelper datahelper = (DataHelper) this.moduleToJComboBox.getSelectedItem();
		ruleDetails.put("moduleToId", datahelper.getId());
		ruleDetails.put("enabled", this.ruleEnabledCheckBox.isSelected());
		ruleDetails.put("description", this.descriptionTextArea.getText());
		
		return ruleDetails;
	}

	@Override
	public void updateDetails(HashMap<String, Object> ruleDetails) {
		// TODO Auto-generated method stub
		
	}

	private void addFromModuleComponents(GridBagConstraints gridBagConstraints) {
		this.moduleFromLabel = new JLabel("From Module");
		this.add(this.moduleFromLabel, gridBagConstraints);
		gridBagConstraints.gridx++;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		this.createFromModuleJComboBox();
		this.add(this.moduleFromJComboBox, gridBagConstraints);
	}
	
	private void createFromModuleJComboBox() {
		this.moduleFromJComboBox = new JComboBox();
		ArrayList<DataHelper> dataHelperList;
		if (!isException){
			String currentModuleName = appliedRuleController.getCurrentModuleName();
			Long currentModuleId = appliedRuleController.getCurrentModuleId();
			
			DataHelper datahelper = new DataHelper();
			datahelper.setId(currentModuleId);
			datahelper.setValue(currentModuleName);
			
			dataHelperList = new ArrayList<DataHelper>();
			dataHelperList.add(datahelper);
		} else {
			dataHelperList = this.appliedRuleController.getChildModules(this.appliedRuleController.getCurrentModuleId());
		}
		ComboBoxModel comboBoxModel = new DefaultComboBoxModel(dataHelperList.toArray());
		this.moduleFromJComboBox.setModel(comboBoxModel);
	}
	
	private void addToModuleComponents(GridBagConstraints gridBagConstraints) {
		this.moduleToLabel = new JLabel("To Module");
		this.add(this.moduleToLabel, gridBagConstraints);
		gridBagConstraints.gridx++;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		this.createToModuleJComboBox();
		this.add(this.moduleToJComboBox, gridBagConstraints);
	}
	
	private void createToModuleJComboBox() {
		this.moduleToJComboBox = new JComboBox();
		ArrayList<DataHelper> dataHelperList;
		if (!isException){
			dataHelperList = this.appliedRuleController.getChildModules(-1);
		} else {
			dataHelperList = this.appliedRuleController.getChildModules(this.appliedRuleController.getModuleToId());
		}
		ComboBoxModel comboBoxModel = new DefaultComboBoxModel(dataHelperList.toArray());
		this.moduleToJComboBox.setModel(comboBoxModel);
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
}
