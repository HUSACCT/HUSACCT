package husacct.define.presentation.jpanel.ruledetails.legalitydependency;

import husacct.define.presentation.helper.DataHelper;
import husacct.define.presentation.jpanel.ruledetails.AbstractDetailsJPanel;
import husacct.define.task.AppliedRuleController;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.HashMap;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class IsAllowedToUseJPanel extends AbstractDetailsJPanel{
	private static final long serialVersionUID = 376037038601799822L;
	public static final String ruleTypeKey = "IsAllowedToUse";
	
	private JLabel fromModuleLabel;
	private JLabel toModuleLabel;
	private JLabel ruleEnabledLabel;
	private JLabel descriptionLabel;
	
	public JComboBox fromModuleJComboBox;
	public JComboBox toModuleJComboBox;
	public JCheckBox ruleEnabledCheckBox;
	public JTextArea descriptionTextArea;
	
	public IsAllowedToUseJPanel(AppliedRuleController appliedRuleController) {
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
		HashMap<String, Object> ruleDetails = saveDefaultDataToHashMap();
		
		DataHelper datahelper1 = (DataHelper) this.fromModuleJComboBox.getSelectedItem();
		ruleDetails.put("moduleFromId", datahelper1.getId());
		DataHelper datahelper = (DataHelper) toModuleJComboBox.getSelectedItem();
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
		this.fromModuleLabel = new JLabel("From Module");
		this.add(this.fromModuleLabel, gridBagConstraints);
		gridBagConstraints.gridx++;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		this.createFromModuleJComboBox();
		this.add(this.fromModuleJComboBox, gridBagConstraints);
	}
	
	private void createFromModuleJComboBox() {
		this.fromModuleJComboBox = new JComboBox();
		String currentModuleName = appliedRuleController.getCurrentModuleName();
		Long currentModuleID = appliedRuleController.getCurrentModuleId();
		
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
		this.toModuleJComboBox.setModel(this.appliedRuleController.loadModulesToCombobox());
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
