package husacct.define.presentation.jpanel.ruledetails.components;

import husacct.define.abstraction.language.DefineTranslator;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class DescriptionPanelComponent extends AbstractPanelComponent{

	private static final long serialVersionUID = 1810861688475708958L;
	private JLabel descriptionLabel;
	private JTextArea descriptionTextArea;
	
	public DescriptionPanelComponent() {
		super();
		initGUI();
	}
	
	public void initGUI(){
		super.initGUI();
		initDetails();
	}

	private void initDetails() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
		
		this.descriptionLabel = new JLabel(DefineTranslator.translate("Description"));
		this.add(this.descriptionLabel, gridBagConstraints);
		gridBagConstraints.gridx++;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		this.add(this.createDescriptionScrollPane(), gridBagConstraints);
	}
	
	private JScrollPane createDescriptionScrollPane() {
		this.descriptionTextArea = new JTextArea(5, 50);
		this.descriptionTextArea.setText("");
		JScrollPane descriptionScrollPane = new JScrollPane(this.descriptionTextArea);
		return descriptionScrollPane;
	}
	
	
	@Override
	public Object getValue(){
		return descriptionTextArea.getText();
	}
	
	@Override
	public boolean hasValidData(){
		boolean hasValidData = true;
		//Add checks on description
		return hasValidData;
	}

	@Override
	public void update(Object o) {
		String description = (String) o;
		this.descriptionTextArea.setText(description);
	}
}
