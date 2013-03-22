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

public class ModuleToPanelComponent extends AbstractPanelComponent implements TreeSelectionListener {

    private static final long serialVersionUID = -4800834732055260518L;
    private AppliedRuleController appliedRuleController;
    private JLabel moduleToJLabel;
    private CombinedModuleTree moduleToTree;

    public ModuleToPanelComponent(AppliedRuleController appliedRuleController) {
        super();
        this.appliedRuleController = appliedRuleController;
        initGUI();
    }

    @Override
    protected void setLayout() {
        GridBagLayout ruleDetailsLayout = new GridBagLayout();
        ruleDetailsLayout.rowWeights = new double[]{0.0, 0.0};
        ruleDetailsLayout.rowHeights = new int[]{150, 30};
        ruleDetailsLayout.columnWeights = new double[]{0.0, 0.0};
        ruleDetailsLayout.columnWidths = new int[]{130, 660};
        this.setLayout(ruleDetailsLayout);
    }

    public void initGUI() {
        super.initGUI();
        initDetails();
    }

    private void initDetails() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);

        this.moduleToJLabel = new JLabel(ServiceProvider.getInstance().getLocaleService().getTranslatedString("ToModule"));
        this.add(this.moduleToJLabel, gridBagConstraints);
        gridBagConstraints.gridx++;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        this.add(createToModuleScrollPane(), gridBagConstraints);
    }

    private JScrollPane createToModuleScrollPane() {
        AbstractCombinedComponent rootComponent = this.appliedRuleController.getModuleTreeComponents();
        this.moduleToTree = new CombinedModuleTree(rootComponent, appliedRuleController.getCurrentModuleId());
        this.moduleToTree.addTreeSelectionListener(this);
        JScrollPane moduleTreeScrollPane = new JScrollPane(this.moduleToTree);
        return moduleTreeScrollPane;
    }

    @Override
    public Object getValue() {
        return this.moduleToTree.getSelectedTreeValue();
    }

    @Override
    public boolean hasValidData() {
        boolean hasValidData = true;
        //Add checks on description
        hasValidData = hasValidData && (getValue() != null);
        return hasValidData;
    }

    @Override
    public void update(Object o) {
        if (moduleToTree != null) {
            if (o instanceof Long) {
                moduleToTree.setSelectedRow((Long) o);
            }
        }
    }

    @Override
    public void valueChanged(TreeSelectionEvent arg0) {
        // TODO Auto-generated method stub
    }
}
