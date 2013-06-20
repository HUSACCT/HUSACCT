package husacct.define.presentation.jpanel.ruledetails.components;

import husacct.ServiceProvider;
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

public class ModuleFromPanelComponent extends AbstractPanelComponent implements
	TreeSelectionListener {

    private static final long serialVersionUID = 5436437579750820119L;
    private AppliedRuleController appliedRuleController;
    private boolean isException;

    private JLabel moduleFromJLabel;
    private CombinedModuleTree moduleFromTree;

    public ModuleFromPanelComponent(boolean isException,
	    AppliedRuleController appliedRuleController) {
	super();
	this.isException = isException;
	this.appliedRuleController = appliedRuleController;
	initGUI();
    }

    private void createFromModuleJLabel() {
	moduleFromJLabel = new JLabel();
	String currentModuleName = appliedRuleController.getCurrentModuleName();
	moduleFromJLabel.setText(currentModuleName);
    }

    private JScrollPane createFromModuleScrollPane() {
	AbstractCombinedComponent rootComponent = appliedRuleController
		.getModuleTreeComponents();

	moduleFromTree = new CombinedModuleTree(rootComponent,
		appliedRuleController.getCurrentModuleId());
	moduleFromTree.addTreeSelectionListener(this);

	JScrollPane moduleTreeScrollPane = new JScrollPane(moduleFromTree);

	return moduleTreeScrollPane;
    }

    @Override
    public Object getValue() {
	Object returnObject;
	returnObject = appliedRuleController.getCurrentModuleId();
	//	To go back to Module selector add this :
	//	if (!isException) {
	//		returnObject = appliedRuleController.getCurrentModuleId();
	//	} else {
	//		returnObject = moduleFromTree.getSelectedTreeValue();
	//	}
	return returnObject;
    }

    @Override
    public boolean hasValidData() {
	boolean hasValidData = true;
	// Add checks on description
	hasValidData = hasValidData && getValue() != null;
	return hasValidData;
    }

    private void initDetails() {
	GridBagConstraints gridBagConstraints = new GridBagConstraints(0, 0, 1,
		1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START,
		GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);

	moduleFromJLabel = new JLabel(ServiceProvider.getInstance()
		.getLocaleService().getTranslatedString("FromModule"));
	this.add(moduleFromJLabel, gridBagConstraints);
	gridBagConstraints.gridx++;
	gridBagConstraints.fill = GridBagConstraints.BOTH;
	createFromModuleJLabel();
    this.add(moduleFromJLabel, gridBagConstraints);
	//	To go back to Module selector add this:
	//	if (!isException) {
	//	    createFromModuleJLabel();
	//	    this.add(moduleFromJLabel, gridBagConstraints);
	//	} else {
	//	    this.add(createFromModuleScrollPane(), gridBagConstraints);
	//	}
    }

    @Override
    public void initGUI() {
	super.initGUI();
	initDetails();
    }

    @Override
    protected void setLayout() {
	GridBagLayout ruleDetailsLayout = new GridBagLayout();
	ruleDetailsLayout.rowWeights = new double[] { 0.0, 0.0 };
	ruleDetailsLayout.rowHeights = new int[] { 0, 0 };
	// max total height = 290
	//	To go back to Module selector add this as !isException clause:
	//	ruleDetailsLayout.rowHeights = new int[] { 120, 30 };
	ruleDetailsLayout.columnWeights = new double[] { 0.0, 0.0 };
	ruleDetailsLayout.columnWidths = new int[] { 130, 660 };
	this.setLayout(ruleDetailsLayout);
    }

    @Override
    public void update(Object o) {
	if (moduleFromTree != null) {
	    if (o instanceof Long) {
		moduleFromTree.setSelectedRow((Long) o);
	    }
	}
    }

    @Override
    public void valueChanged(TreeSelectionEvent arg0) {
	// TODO Auto-generated method stub

    }
}
