package husacct.define.presentation.jpanel.ruledetails.legalitydependency;

import husacct.define.abstraction.language.DefineTranslator;
import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.domain.SoftwareUnitDefinition.Type;
import husacct.define.presentation.jpanel.ruledetails.AbstractDetailsJPanel;
import husacct.define.presentation.moduletree.CombinedModuleTree;
import husacct.define.presentation.utils.DataHelper;
import husacct.define.task.AppliedRuleController;
import husacct.define.task.components.AbstractCombinedComponent;
import husacct.define.task.components.AbstractDefineComponent;
import husacct.define.task.components.AnalyzedModuleComponent;

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
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

public class IsAllowedToUseJPanel extends AbstractDetailsJPanel implements TreeSelectionListener{
	private static final long serialVersionUID = 376037038601799822L;
	public static final String ruleTypeKey = "IsAllowedToUse";
	
	private JLabel moduleFromLabel;
	private JLabel moduleToLabel;
	private JLabel ruleEnabledLabel;
	private JLabel descriptionLabel;
	
	public JComboBox moduleFromJComboBox;
	public CombinedModuleTree moduleFromTree;
	public JComboBox moduleToJComboBox;
	public CombinedModuleTree moduleToTree;
	public JCheckBox ruleEnabledCheckBox;
	public JTextArea descriptionTextArea;
	
	public IsAllowedToUseJPanel(AppliedRuleController appliedRuleController) {
		super(appliedRuleController);
	}

	@Override
	public void initDetails() {
		this.addFromModuleComponents(new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.addToModuleComponents(new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(4, 0, 0, 0), 0, 0));
		this.addEnabledComponents(new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.addDescriptionComponents(new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 4, 0), 0, 0));
//		this.setSize(400, 335);
	}
	
//	@Override
//	protected GridBagLayout createRuleDetailsLayout() {
//		GridBagLayout ruleDetailsLayout = new GridBagLayout();
//		ruleDetailsLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
//		// max total height = 290
//		ruleDetailsLayout.rowHeights = new int[] { 5, 5, 5, 5, 5 };
//		ruleDetailsLayout.columnWeights = new double[] { 0.0, 0.0 };
//		ruleDetailsLayout.columnWidths = new int[] { 130, 660 };
//		return ruleDetailsLayout;
//	}

	@Override
	public HashMap<String, Object> saveToHashMap() {
		HashMap<String, Object> ruleDetails = super.saveToHashMap();
		
		DataHelper datahelper1 = (DataHelper) this.moduleFromJComboBox.getSelectedItem();
		ruleDetails.put("moduleFromId", datahelper1.getId());
		
		saveModuleTreeToHashMap(ruleDetails);
		ruleDetails.put("enabled", this.ruleEnabledCheckBox.isSelected());
		ruleDetails.put("description", this.descriptionTextArea.getText());
		
		return ruleDetails;
	}

	private void saveModuleTreeToHashMap(HashMap<String, Object> ruleDetails) {
		if (true){//!isException){ 
			DataHelper datahelper = (DataHelper) this.moduleToJComboBox.getSelectedItem();
			ruleDetails.put("moduleToId", datahelper.getId());
		} else {
			TreePath path = this.moduleToTree.getSelectionPath();
			AbstractCombinedComponent selectedComponent = (AbstractCombinedComponent) path.getLastPathComponent();
			
			if(selectedComponent instanceof AbstractDefineComponent) {
				AbstractDefineComponent defineComponent = (AbstractDefineComponent) selectedComponent;
				long selectedModuleId = defineComponent.getModuleId();
				
				ruleDetails.put("moduleToId", selectedModuleId);
			} else if(selectedComponent instanceof AnalyzedModuleComponent) {
				AnalyzedModuleComponent analyzedComponent = (AnalyzedModuleComponent) selectedComponent;
				String uniqueName = analyzedComponent.getUniqueName();
				String stringType = analyzedComponent.getType();
				Type type = Type.valueOf(stringType);
				SoftwareUnitDefinition su = new SoftwareUnitDefinition(uniqueName, type);
				ruleDetails.put("SoftwareUnitTo", su);
			}
		}
	}

	@Override
	public void updateDetails(HashMap<String, Object> ruleDetails) {
		// TODO Auto-generated method stub
		
	}
	
	private void addFromModuleComponents(GridBagConstraints gridBagConstraints) {
		this.moduleFromLabel = new JLabel(DefineTranslator.translate("FromModule"));
		this.add(this.moduleFromLabel, gridBagConstraints);
		gridBagConstraints.gridx++;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		if (true){//!isException){
			this.createFromModuleJComboBox();
			this.add(this.moduleFromJComboBox, gridBagConstraints);
		} else {
			this.add(createFromModuleScrollPane(), gridBagConstraints);
		}
	}
	
	private void createFromModuleJComboBox() {
		this.moduleFromJComboBox = new JComboBox();
		ArrayList<DataHelper> dataHelperList;
		
		String currentModuleName = appliedRuleController.getCurrentModuleName();
		Long currentModuleId = appliedRuleController.getCurrentModuleId();
		
		DataHelper datahelper = new DataHelper();
		datahelper.setId(currentModuleId);
		datahelper.setValue(currentModuleName);
		
		dataHelperList = new ArrayList<DataHelper>();
		dataHelperList.add(datahelper);
		if (isException){
			ArrayList<DataHelper> tmpDataHelperList = this.appliedRuleController.getChildModules(this.appliedRuleController.getCurrentModuleId());
			dataHelperList.addAll(tmpDataHelperList);
		}
		ComboBoxModel comboBoxModel = new DefaultComboBoxModel(dataHelperList.toArray());
		this.moduleFromJComboBox.setModel(comboBoxModel);
	}
	
	private JScrollPane createFromModuleScrollPane() {
		AbstractCombinedComponent rootComponent = this.appliedRuleController.getModuleTreeComponents();
		this.moduleFromTree = new CombinedModuleTree(rootComponent, appliedRuleController.getCurrentModuleId());
		this.moduleFromTree.addTreeSelectionListener(this);
		JScrollPane moduleTreeScrollPane = new JScrollPane(this.moduleToTree);
		return moduleTreeScrollPane;
	}
	
	private void addToModuleComponents(GridBagConstraints gridBagConstraints) {
		this.moduleToLabel = new JLabel(DefineTranslator.translate("ToModule"));
		this.add(this.moduleToLabel, gridBagConstraints);
		gridBagConstraints.gridx++;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		if (true){//!isException){
			this.createToModuleJComboBox();
			this.add(this.moduleToJComboBox, gridBagConstraints);
		} else {
			this.add(createToModuleScrollPane(), gridBagConstraints);
		}
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
	
	private JScrollPane createToModuleScrollPane() {
		AbstractCombinedComponent rootComponent = this.appliedRuleController.getModuleTreeComponents();
		this.moduleToTree = new CombinedModuleTree(rootComponent, appliedRuleController.getCurrentModuleId());
		this.moduleToTree.addTreeSelectionListener(this);
		this.moduleToTree.setSize(150, 660);
		JScrollPane moduleTreeScrollPane = new JScrollPane(this.moduleToTree);
		return moduleTreeScrollPane;
	}
	
	private void addEnabledComponents(GridBagConstraints gridBagConstraints){
		this.ruleEnabledLabel = new JLabel(DefineTranslator.translate("Enabled"));
		this.add(this.ruleEnabledLabel, gridBagConstraints);
		gridBagConstraints.gridx++;
		this.ruleEnabledCheckBox = new JCheckBox();
		this.ruleEnabledCheckBox.setSelected(true);
		this.add(this.ruleEnabledCheckBox, gridBagConstraints);
	}
	
	private void addDescriptionComponents(GridBagConstraints gridBagConstraints){
		this.descriptionLabel = new JLabel(DefineTranslator.translate("Description"));
		this.add(this.descriptionLabel, gridBagConstraints);
		gridBagConstraints.gridx++;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		this.add(this.createDescriptionScrollPane(), gridBagConstraints);
	}
		
	private JScrollPane createDescriptionScrollPane() {
		this.descriptionTextArea = new JTextArea();
		this.descriptionTextArea.setText("");
		JScrollPane descriptionScrollPane = new JScrollPane(this.descriptionTextArea);
		return descriptionScrollPane;
	}

	@Override
	public void valueChanged(TreeSelectionEvent event) {
		
	}

}
