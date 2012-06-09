package husacct.define.presentation.jpanel.ruledetails.components;

import husacct.define.abstraction.language.DefineTranslator;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class RegexPanelComponent extends AbstractPanelComponent{

	private static final long serialVersionUID = 5438017147944566853L;
	private JLabel regexLabel;
	private JTextField regexTextField;
	
	public RegexPanelComponent() {
		super();
		initGUI();
	}
	
	public void initGUI(){
		super.initGUI();
		initDetails();
	}

	private void initDetails() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
		
		this.regexLabel = new JLabel(DefineTranslator.translate("Regex"));
		this.add(this.regexLabel, gridBagConstraints);
		gridBagConstraints.gridx++;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		this.regexTextField = new JTextField();
		this.add(this.regexTextField, gridBagConstraints);
	}
	
	@Override
	public Object getValue(){
		return regexTextField.getText();
	}
	
	@Override
	public boolean hasValidData(){
		boolean hasValidData = true;
		String regex = regexTextField.getText();
		hasValidData = hasValidData && (regex.matches("^!?\\*?[A-Za-z0-9-_.]+\\*?$"));
		
		return hasValidData;
	}

	@Override
	public void update(Object o) {
		String regualairExpression = (String) o;
		this.regexTextField.setText(regualairExpression);
	}
	
}
