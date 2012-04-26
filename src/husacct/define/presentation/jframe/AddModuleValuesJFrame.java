package husacct.define.presentation.jframe;

import husacct.define.task.DefinitionController;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * 
 * @author Henk ter Harmsel
 *
 */
public class AddModuleValuesJFrame extends AbstractValuesJFrame {

	private static final long serialVersionUID = -1729066215610611394L;
	
	private JPanel innerPanel;
	
	private JTextField moduleNameField;
	private JTextField hierarchicalLevelField;
	
	public AddModuleValuesJFrame() {
		super();
	}
	
	@Override
	public void initUI() {
		this.innerPanel = new JPanel();
		this.add(this.innerPanel);
		this.innerPanel.setLayout(this.createInnerPanelLayout());
		this.innerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		this.addModuleValues();
		this.addHierarchicalLevelValues();
		this.addButtons();
		
		this.setVisible(true);
		this.pack();
		this.setSize(500, 150);
	}
	
	private GridLayout createInnerPanelLayout() {
		GridLayout innerPanelLayout = new GridLayout(2, 2);
		innerPanelLayout.setColumns(2);
		innerPanelLayout.setHgap(5);
		innerPanelLayout.setVgap(5);
		innerPanelLayout.setRows(3);
		return innerPanelLayout;
	}
	
	private void addModuleValues() {
		JLabel moduleLabel = new JLabel("Module name");
		this.innerPanel.add(moduleLabel);
		
		this.moduleNameField = new JTextField();
		this.innerPanel.add(this.moduleNameField);
	}
	
	private void addHierarchicalLevelValues() {
		JLabel hierarchicalLevelLabel = new JLabel("Hierarchical Level");
		this.innerPanel.add(hierarchicalLevelLabel);
		
		this.hierarchicalLevelField = new JTextField();
		this.innerPanel.add(this.hierarchicalLevelField);
	}
	
	@Override
	protected void addButtons() {
		this.cancelButton = new JButton();
		this.innerPanel.add(this.cancelButton);
		this.cancelButton.setText("Cancel");
		this.cancelButton.addActionListener(this);
		
		this.saveButton = new JButton();
		this.innerPanel.add(this.saveButton);
		this.saveButton.setText("Save");
		this.saveButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == this.cancelButton) {
			this.cancelButtonAction(event);
		} else if (event.getSource() == this.saveButton) {
			this.saveButtonAction(event);
		}
	}

	@Override
	protected void cancelButtonAction(ActionEvent event) {
		this.dispose();
	}

	@Override
	protected void saveButtonAction(ActionEvent event) {
		String moduleName = this.getModuleNameValue();
		String level = this.getHierarchicalLevelValue();
		if(!moduleName.isEmpty() && !level.isEmpty()) {
			DefinitionController definitionController = DefinitionController.getInstance();
			definitionController.addLayer(moduleName, Integer.parseInt(level));
			this.dispose();
		}
	}
	
	private String getModuleNameValue() {
		String moduleNameValue = this.moduleNameField.getText();
		if(this.inputController.checkModuleNameInput(moduleNameValue)) {
			return moduleNameValue;
		} else {
			this.throwError(this.inputController.getErrorMessage());
		}
		return "";
	}
	
	private String getHierarchicalLevelValue() {
		String levelValue = this.hierarchicalLevelField.getText();
		if(this.inputController.checkHierarchicalLevelInput(levelValue)) {
			return levelValue;
		} else {
			this.throwError(this.inputController.getErrorMessage());
		}
		return "";
	}
	
	private void throwError(String errorMessage) {
		//TODO: Throw an error!
	}
}
