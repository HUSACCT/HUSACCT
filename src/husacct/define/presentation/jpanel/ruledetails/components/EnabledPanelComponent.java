package husacct.define.presentation.jpanel.ruledetails.components;

import husacct.ServiceProvider;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

public class EnabledPanelComponent extends AbstractPanelComponent {

    private static final long serialVersionUID = -346719824489244157L;
    private JLabel ruleEnabledLabel;
    private JCheckBox ruleEnabledCheckBox;

    public EnabledPanelComponent() {
        super();
        initGUI();
    }

    public void initGUI() {
        super.initGUI();
        initDetails();
    }

    private void initDetails() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);

        this.ruleEnabledLabel = new JLabel(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Enabled"));
        this.add(this.ruleEnabledLabel, gridBagConstraints);
        gridBagConstraints.gridx++;
        this.ruleEnabledCheckBox = new JCheckBox();
        this.ruleEnabledCheckBox.setSelected(true);
        this.add(this.ruleEnabledCheckBox, gridBagConstraints);
    }

    @Override
    public Object getValue() {
        return ruleEnabledCheckBox.isSelected();
    }

    @Override
    public boolean hasValidData() {
        boolean hasValidData = true;
        //Add checks on description
        return hasValidData;
    }

    @Override
    public void update(Object o) {
        boolean enabled = (Boolean) o;
        this.ruleEnabledCheckBox.setSelected(enabled);
    }
}
