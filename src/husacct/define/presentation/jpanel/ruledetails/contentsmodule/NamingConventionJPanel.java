package husacct.define.presentation.jpanel.ruledetails.contentsmodule;

import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.domain.SoftwareUnitDefinition.Type;
import husacct.define.presentation.jpanel.ruledetails.AbstractDetailsJPanel;
import husacct.define.presentation.moduletree.CombinedModuleTree;
import husacct.define.task.AppliedRuleController;
import husacct.define.task.components.AbstractCombinedComponent;
import husacct.define.task.components.AbstractDefineComponent;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

public class NamingConventionJPanel extends AbstractDetailsJPanel implements TreeSelectionListener{
	private static final long serialVersionUID = 7255776882803354410L;
	public static final String ruleTypeKey = "NamingConvention";
	
	private JLabel moduleFromLabel;
	private JLabel ruleEnabledLabel;
	private JLabel descriptionLabel;
	private JLabel regexLabel;
	
	public JLabel moduleFromJLabel;
	private Long currentModuleId;
	
	public CombinedModuleTree moduleFromTree;
	public JCheckBox ruleEnabledCheckBox;
	public JTextArea descriptionTextArea;
	public JTextField regexTextField;

	public NamingConventionJPanel(AppliedRuleController appliedRuleController) {
		super(appliedRuleController);
	}

	@Override
	public void initDetails() {
		this.addFromModuleComponents(new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.addEnabledComponents(new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.addRegexComponents(new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.addDescriptionComponents(new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	}
	
	@Override
	protected GridBagLayout createRuleDetailsLayout() {
		GridBagLayout ruleDetailsLayout = new GridBagLayout();
		ruleDetailsLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0 };
		// max total height = 290
		if (!isException){ 
			ruleDetailsLayout.rowHeights = new int[] { 30, 30, 30, 90 };
		} else {
			ruleDetailsLayout.rowHeights = new int[] { 150, 30, 30, 90 };
		}
		ruleDetailsLayout.columnWeights = new double[] { 0.0, 0.0 };
		ruleDetailsLayout.columnWidths = new int[] { 130, 660 };
		return ruleDetailsLayout;
	}

	@Override
	public HashMap<String, Object> saveToHashMap() {
		HashMap<String, Object> ruleDetails = super.saveToHashMap();
		
		saveModuleTreeFromHashMap(ruleDetails);
		
		ruleDetails.put("enabled", this.ruleEnabledCheckBox.isSelected());
		ruleDetails.put("description", this.descriptionTextArea.getText());
		ruleDetails.put("regex", this.regexTextField.getText());
		
		return ruleDetails;
	}

	@Override
	public void updateDetails(HashMap<String, Object> ruleDetails) {
		// TODO Auto-generated method stub
		
	}
	private Object getTreeValue(TreePath path){
		Object returnObject = null;
		AbstractCombinedComponent selectedComponent = (AbstractCombinedComponent) path.getLastPathComponent();
		
		if(selectedComponent instanceof AbstractDefineComponent) {
			AbstractDefineComponent defineComponent = (AbstractDefineComponent) selectedComponent;
			returnObject = defineComponent.getModuleId();
			
		} else if(selectedComponent instanceof AnalyzedModuleComponent) {
			AnalyzedModuleComponent analyzedComponent = (AnalyzedModuleComponent) selectedComponent;
			String uniqueName = analyzedComponent.getUniqueName();
			String stringType = analyzedComponent.getType();
			Type type = Type.valueOf(stringType);
			SoftwareUnitDefinition su = new SoftwareUnitDefinition(uniqueName, type);
			returnObject = su;
		}
		return returnObject;
	}
	
	private void saveModuleTreeFromHashMap(HashMap<String, Object> ruleDetails) {
		if (!isException){ 
			ruleDetails.put("moduleFromId", currentModuleId);
		} else {
			TreePath pathFrom = this.moduleFromTree.getSelectionPath();
			ruleDetails.put("moduleFromId", getTreeValue(pathFrom));
		}
	}

	private void addFromModuleComponents(GridBagConstraints gridBagConstraints) {
		this.moduleFromLabel = new JLabel("From Module");
		this.add(this.moduleFromLabel, gridBagConstraints);
		gridBagConstraints.gridx++;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		if (!isException){
			this.createFromModuleJLabel();
			this.add(this.moduleFromJLabel, gridBagConstraints);
		} else {
			this.add(createFromModuleScrollPane(), gridBagConstraints);
		}
	}
	
	private void createFromModuleJLabel() {
		this.moduleFromJLabel = new JLabel();
		currentModuleId = appliedRuleController.getCurrentModuleId();
		String currentModuleName = appliedRuleController.getCurrentModuleName();		
		this.moduleFromJLabel.setText(currentModuleName);
	}
	
	private JScrollPane createFromModuleScrollPane() {
		AbstractCombinedComponent rootComponent = this.appliedRuleController.getModuleTreeComponents();
		this.moduleFromTree = new CombinedModuleTree(rootComponent, appliedRuleController.getCurrentModuleId());
		this.moduleFromTree.addTreeSelectionListener(this);
		JScrollPane moduleTreeScrollPane = new JScrollPane(this.moduleFromTree);
		return moduleTreeScrollPane;
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
	
	private void addRegexComponents(GridBagConstraints gridBagConstraints){
		this.regexLabel = new JLabel("Regex");
		this.add(this.regexLabel, gridBagConstraints);
		gridBagConstraints.gridx++;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		this.regexTextField = new JTextField();
		this.add(this.regexTextField, gridBagConstraints);
	}

	@Override
	public void valueChanged(TreeSelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
