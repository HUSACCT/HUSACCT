package husacct.define.presentation.jpanel.ruledetails.components;

import husacct.define.abstraction.language.DefineTranslator;
import husacct.define.presentation.moduletree.CombinedModuleTree;
import husacct.define.task.AppliedRuleController;
import husacct.define.task.components.AbstractCombinedComponent;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

public class ModuleFromPanelComponent extends AbstractPanelComponent implements TreeSelectionListener{

	private static final long serialVersionUID = 5436437579750820119L;
	private AppliedRuleController appliedRuleController;
	private boolean isException;
	
	private JLabel moduleFromJLabel;
	private Long currentModuleId;
	private CombinedModuleTree moduleFromTree;
	
	public ModuleFromPanelComponent(boolean isException, AppliedRuleController appliedRuleController) {
		super();
		this.isException = isException;
		this.appliedRuleController = appliedRuleController;
		initGUI();
	}
	
	@Override
	protected void setLayout() {
		GridBagLayout ruleDetailsLayout = new GridBagLayout();
		ruleDetailsLayout.rowWeights = new double[] { 0.0, 0.0 };
		// max total height = 290
		if (!isException){ 
			ruleDetailsLayout.rowHeights = new int[] { 30, 30 };
		} else {
			ruleDetailsLayout.rowHeights = new int[] { 120, 30};
		}
		ruleDetailsLayout.columnWeights = new double[] { 0.0, 0.0 };
		ruleDetailsLayout.columnWidths = new int[] { 130, 660 };
		this.setLayout(ruleDetailsLayout);
	}
	
	public void initGUI(){
		super.initGUI();
		initDetails();
	}

	private void initDetails() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
		
		moduleFromJLabel = new JLabel(DefineTranslator.translate("FromModule"));
		this.add(this.moduleFromJLabel, gridBagConstraints);
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
	
	
	@Override
	public Object getValue(){
		Object returnObject;
		if (!isException){ 
			returnObject = currentModuleId;
		} else {
			returnObject = this.moduleFromTree.getSelectedTreeValue();
		}
		return returnObject;
	}

	
	@Override
	public boolean hasValidData(){
		boolean hasValidData = true;
		//Add checks on description
		hasValidData = hasValidData && (getValue() != null);
		return hasValidData;
	}

	@Override
	public void update(Object o) {
		//FIXME
	}

	@Override
	public void valueChanged(TreeSelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
