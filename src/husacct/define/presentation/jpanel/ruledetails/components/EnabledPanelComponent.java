package husacct.define.presentation.jpanel.ruledetails.components;

import husacct.ServiceProvider;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

public class EnabledPanelComponent extends AbstractPanelComponent {

    private static final long serialVersionUID = -346719824489244157L;
    private JCheckBox ruleEnabledCheckBox;
    private JLabel ruleEnabledLabel;

    public EnabledPanelComponent() {
	super();
	initGUI();
    }

    @Override
    public Object getValue() {
	return ruleEnabledCheckBox.isSelected();
    }

    @Override
    public boolean hasValidData() {
	boolean hasValidData = true;
	// Add checks on description
	return hasValidData;
    }

    private void initDetails() {
	GridBagConstraints gridBagConstraints = new GridBagConstraints(0, 0, 1,
		1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START,
		GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);

	ruleEnabledLabel = new JLabel(ServiceProvider.getInstance()
		.getLocaleService().getTranslatedString("Enabled"));
	this.add(ruleEnabledLabel, gridBagConstraints);
	gridBagConstraints.gridx++;
	ruleEnabledCheckBox = new JCheckBox();
	ruleEnabledCheckBox.setSelected(true);
	this.add(ruleEnabledCheckBox, gridBagConstraints);
    }

    @Override
    public void initGUI() {
	super.initGUI();
	initDetails();
    }

    @Override
    public void update(Object o) {
	boolean enabled = (Boolean) o;
	ruleEnabledCheckBox.setSelected(enabled);
    }
}
