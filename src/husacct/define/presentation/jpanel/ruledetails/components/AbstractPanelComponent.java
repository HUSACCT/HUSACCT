package husacct.define.presentation.jpanel.ruledetails.components;

import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public abstract class AbstractPanelComponent extends JPanel {

    private static final long serialVersionUID = 404007971467781630L;

    public AbstractPanelComponent() {
    }

    public void initGUI() {
        this.removeAll();
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        setLayout();

    }

    protected void setLayout() {
        GridBagLayout ruleDetailsLayout = new GridBagLayout();
        ruleDetailsLayout.columnWeights = new double[]{0.0, 0.0};
        ruleDetailsLayout.columnWidths = new int[]{130, 660};
        this.setLayout(ruleDetailsLayout);
    }

    public abstract Object getValue();

    public abstract boolean hasValidData();

    public abstract void update(Object o);
}
