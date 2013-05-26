package husacct.define.presentation.jpanel.ruledetails.contentsmodule;

import husacct.define.presentation.jpanel.ruledetails.AbstractDetailsJPanel;
import husacct.define.presentation.jpanel.ruledetails.components.DescriptionPanelComponent;
import husacct.define.presentation.jpanel.ruledetails.components.EnabledPanelComponent;
import husacct.define.presentation.jpanel.ruledetails.components.ModuleFromPanelComponent;
import husacct.define.presentation.jpanel.ruledetails.components.ModuleToPanelComponent;
import husacct.define.task.AppliedRuleController;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;

public class InterfaceConventionJPanel extends AbstractDetailsJPanel {
    public static final String ruleTypeKey = "InterfaceConvention";
    private static final long serialVersionUID = 376037038601799822L;

    public DescriptionPanelComponent descriptionPanelComponent;
    public EnabledPanelComponent enabledPanelComponent;
    public ModuleFromPanelComponent moduleFromPanelComponent;
    public ModuleToPanelComponent moduleToPanelComponent;

    public InterfaceConventionJPanel(AppliedRuleController appliedRuleController) {
	super(appliedRuleController);
    }

    @Override
    protected GridBagLayout createRuleDetailsLayout() {
	GridBagLayout ruleDetailsLayout = new GridBagLayout();
	ruleDetailsLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0 };
	// max total height = 290
	if (!isException) {
	    ruleDetailsLayout.rowHeights = new int[] { 30, 150, 30, 90 };
	} else {
	    ruleDetailsLayout.rowHeights = new int[] { 150, 150, 30, 90 };
	}
	ruleDetailsLayout.columnWeights = new double[] { 0.0, 0.0 };
	ruleDetailsLayout.columnWidths = new int[] { 130, 660 };
	return ruleDetailsLayout;
    }

    @Override
    public boolean hasValidData() {
	boolean hasValidData = true;
	hasValidData = hasValidData && moduleFromPanelComponent.hasValidData();
	hasValidData = hasValidData && moduleToPanelComponent.hasValidData();
	hasValidData = hasValidData && enabledPanelComponent.hasValidData();
	hasValidData = hasValidData && descriptionPanelComponent.hasValidData();
	return hasValidData;
    }

    @Override
    public void initDetails() {
	moduleFromPanelComponent = new ModuleFromPanelComponent(isException,
		appliedRuleController);
	this.add(moduleFromPanelComponent, new GridBagConstraints(0, 0, 2, 1,
		0.0, 0.0, GridBagConstraints.FIRST_LINE_START,
		GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

	moduleToPanelComponent = new ModuleToPanelComponent(
		appliedRuleController);
	this.add(moduleToPanelComponent, new GridBagConstraints(0, 1, 2, 1,
		0.0, 0.0, GridBagConstraints.FIRST_LINE_START,
		GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

	enabledPanelComponent = new EnabledPanelComponent();
	this.add(enabledPanelComponent, new GridBagConstraints(0, 2, 2, 1, 0.0,
		0.0, GridBagConstraints.FIRST_LINE_START,
		GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

	descriptionPanelComponent = new DescriptionPanelComponent();
	this.add(descriptionPanelComponent, new GridBagConstraints(0, 3, 2, 1,
		0.0, 0.0, GridBagConstraints.FIRST_LINE_START,
		GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    }

    @Override
    public HashMap<String, Object> saveToHashMap() {
	HashMap<String, Object> ruleDetails = super.saveToHashMap();

	ruleDetails.put("moduleFromId", moduleFromPanelComponent.getValue());
	ruleDetails.put("moduleToId", moduleToPanelComponent.getValue());
	ruleDetails.put("enabled", enabledPanelComponent.getValue());
	ruleDetails.put("description", descriptionPanelComponent.getValue());

	return ruleDetails;
    }

    @Override
    public void updateDetails(HashMap<String, Object> ruleDetails) {
	super.updateDetails(ruleDetails);
	moduleFromPanelComponent.update(ruleDetails.get("moduleFromId"));
	moduleToPanelComponent.update(ruleDetails.get("moduleToId"));
	enabledPanelComponent.update(ruleDetails.get("enabled"));
	descriptionPanelComponent.update(ruleDetails.get("description"));
    }
}
