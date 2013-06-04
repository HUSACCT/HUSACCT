package husacct.define.presentation.jpanel.ruledetails.components;

import husacct.ServiceProvider;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class DescriptionPanelComponent extends AbstractPanelComponent {

    private static final long serialVersionUID = 1810861688475708958L;
    private JLabel descriptionLabel;
    private JTextArea descriptionTextArea;

    public DescriptionPanelComponent() {
	super();
	initGUI();
    }

    private JScrollPane createDescriptionScrollPane() {
	descriptionTextArea = new JTextArea(5, 50);
	descriptionTextArea.setText("");
	JScrollPane descriptionScrollPane = new JScrollPane(descriptionTextArea);
	return descriptionScrollPane;
    }

    @Override
    public Object getValue() {
	return descriptionTextArea.getText();
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

	descriptionLabel = new JLabel(ServiceProvider.getInstance()
		.getLocaleService().getTranslatedString("Description"));
	this.add(descriptionLabel, gridBagConstraints);
	gridBagConstraints.gridx++;
	gridBagConstraints.fill = GridBagConstraints.BOTH;
	this.add(createDescriptionScrollPane(), gridBagConstraints);
    }

    @Override
    public void initGUI() {
	super.initGUI();
	initDetails();
    }

    @Override
    public void update(Object o) {
	String description = (String) o;
	descriptionTextArea.setText(description);
    }
}
