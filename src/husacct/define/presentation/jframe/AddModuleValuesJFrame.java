package husacct.define.presentation.jframe;

import husacct.define.presentation.jpanel.ModuleJPanel;
import husacct.define.task.DefinitionController;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * 
 * @author Henk ter Harmsel
 *
 */
public class AddModuleValuesJFrame extends AbstractValuesJFrame {

	private static final long serialVersionUID = -1729066215610611394L;
	
	private ModuleJPanel modulePanel;
	
	private JPanel innerPanel;
	
	private JLabel parentModuleNameLabel;
	private JTextField moduleNameField;
	private JTextField moduleDescriptionField;
//	private JTextField hierarchicalLevelField;
	
	private JComboBox moduleTypeComboBox;
	
	public AddModuleValuesJFrame(ModuleJPanel modulePanel) {
		super();
		this.modulePanel = modulePanel;
	}
	
	@Override
	public void initUI() {
		this.innerPanel = new JPanel();
		this.innerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		this.innerPanel.setLayout(this.getGridLayout(6, 2));
		this.createInnerPanel();
		this.add(this.innerPanel);
		
		this.setResizable(false);
		this.setVisible(true);
		this.pack();
		this.setSize(700, 190);
	}
	
	private GridLayout getGridLayout(int rows, int columns) {
		GridLayout gridLayout = new GridLayout(rows, columns);
		gridLayout.setHgap(5);
		gridLayout.setVgap(5);
		return gridLayout;
	}
	
	private void createInnerPanel() {
		this.addParentModule();
		this.addModuleValues();
		this.addModuleDescriptionTextArea();
		this.addModuleTypeComboBox();
//		this.addHierarchicalLevelValues();
		this.addButtons();
		this.setVisibles();
	}
	
	private void addParentModule() {
		JLabel parentModuleLabel = new JLabel("Parent module");
		this.innerPanel.add(parentModuleLabel);
		
		Long selectedModuleId = DefinitionController.getInstance().getSelectedModuleId();
		String parentModuleName = DefinitionController.getInstance().getModuleName(selectedModuleId);
		
		parentModuleNameLabel = new JLabel(parentModuleName);
		this.innerPanel.add(parentModuleNameLabel);
	}
	
	private void addModuleValues() {
		JLabel moduleLabel = new JLabel("Module name");
		this.innerPanel.add(moduleLabel);
		
		this.moduleNameField = new JTextField();
		this.moduleNameField.addKeyListener(this);
		this.innerPanel.add(this.moduleNameField);
	}
	
	private void addModuleDescriptionTextArea() {
		JLabel moduleLabel = new JLabel("Module Description");
		this.innerPanel.add(moduleLabel);
		
		this.moduleDescriptionField = new JTextField();
		this.innerPanel.add(this.moduleDescriptionField);
	}
	
	private void addModuleTypeComboBox() {
		JLabel moduleTypeLabel = new JLabel("Module type");
		this.innerPanel.add(moduleTypeLabel);
		
		String[] moduleTypes = {"SubSystem", "Layer", "Component", "External Library"};
		this.moduleTypeComboBox = new JComboBox(moduleTypes);
		this.moduleTypeComboBox.setSelectedIndex(0);
		this.moduleTypeComboBox.addActionListener(this);
		this.moduleTypeComboBox.addKeyListener(this);
		this.innerPanel.add(this.moduleTypeComboBox);
	}
	
//	private void addHierarchicalLevelValues() {
//		JLabel hierarchicalLevelLabel = new JLabel("Hierarchical Level");
//		this.innerPanel.add(hierarchicalLevelLabel);
//		
//		this.hierarchicalLevelField = new JTextField();
//		this.hierarchicalLevelField.addKeyListener(this);
//		this.innerPanel.add(this.hierarchicalLevelField);
//	}
	
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
	
//	private void setVisibles() {
////		this.hierarchicalLevelField.setVisible(false);
//	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == this.cancelButton) {
			this.cancelButtonAction();
		} else if (event.getSource() == this.saveButton) {
			this.saveButtonAction();
		} else if (event.getSource() == this.moduleTypeComboBox) {
			this.moduleTypeComboBoxAction();
		}
	}
	
	private void moduleTypeComboBoxAction() {
		this.setVisibles();
//		this.checkSelectedModuleType();
	}
	
	private void setVisibles() {
		String moduleType = this.moduleTypeComboBox.getSelectedItem().toString();
		if(moduleType == "SubSystem") {
			
		} else if(moduleType =="Layer") {
//			this.hierarchicalLevelField.setVisible(true);
		} else if(moduleType =="Component") {
			
		} else if(moduleType =="External Library") {
			
		}
	}
	
	/**
	 * Do nothing
	 */
	@Override
	public void keyPressed(KeyEvent event) {
		
	}
	
	@Override
	public void keyReleased(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
			this.cancelButtonAction();
		} else if (event.getKeyCode() == KeyEvent.VK_ENTER) {
			this.saveButtonAction();
		}
	}
	
	/**
	 * Do nothing
	 */
	@Override
	public void keyTyped(KeyEvent event) {
		
	}

	@Override
	protected void cancelButtonAction() {
		this.dispose();
	}

	@Override
	protected void saveButtonAction() {
		String moduleType = this.moduleTypeComboBox.getSelectedItem().toString();
		this.submitForModuleType(moduleType);
	}
	
	private void submitForModuleType(String moduleType) {
		if(moduleType == "SubSystem") {
			this.submitSubSystem();
		} else if(moduleType == "Layer") {
			this.submitLayer();
		} else if(moduleType == "Component") {
			this.submitComponent();
		} else if(moduleType == "External Library") {
			this.submitExternalLibrary();
		}
	}
	
	private void submitSubSystem() {
		if(this.checkModuleName()) {
			String moduleName = this.moduleNameField.getText();
			String moduleDescription = this.moduleDescriptionField.getText();
			
			DefinitionController definitionController = DefinitionController.getInstance();
			definitionController.addSubSystem(definitionController.getSelectedModuleId(), moduleName, moduleDescription);
			//update tree view
			this.modulePanel.updateModuleTree();
			this.dispose();
		}
	}
	
	private void submitLayer() {
		if(this.checkModuleName()) {
			String moduleName = this.moduleNameField.getText();
			String moduleDescription = this.moduleDescriptionField.getText();
//			int level = Integer.parseInt(this.hierarchicalLevelField.getText());
			
			DefinitionController definitionController = DefinitionController.getInstance();
			definitionController.addLayer(definitionController.getSelectedModuleId(), moduleName, moduleDescription);
			//update tree view
			this.modulePanel.updateModuleTree();
			this.dispose();
		}
	}
	
	private void submitComponent() {
		if(this.checkModuleName()) {
			String moduleName = this.moduleNameField.getText();
			String moduleDescription = this.moduleDescriptionField.getText();
			
			DefinitionController definitionController = DefinitionController.getInstance();
			definitionController.addComponent(definitionController.getSelectedModuleId(), moduleName, moduleDescription);
			//update tree view
			this.modulePanel.updateModuleTree();
			this.dispose();
		}
	}
	
	private void submitExternalLibrary() {
		if(this.checkModuleName()) {
			String moduleName = this.moduleNameField.getText();
			String moduleDescription = this.moduleDescriptionField.getText();
			
			DefinitionController definitionController = DefinitionController.getInstance();
			definitionController.addExternalLibrary(definitionController.getSelectedModuleId(), moduleName, moduleDescription);
			//update tree view
			this.modulePanel.updateModuleTree();
			this.dispose();
		}
	}
	
	private boolean checkModuleName() {
		String moduleNameValue = this.moduleNameField.getText();
		if(this.inputController.checkModuleNameInput(moduleNameValue)) {
			return true;
		} else {
			this.throwError(this.inputController.getErrorMessage());
		}
		return false;
	}
	
//	private boolean checkModuleHierarchicalLevel() {
////		String levelValue = this.hierarchicalLevelField.getText();
//		if(this.inputController.checkHierarchicalLevelInput(levelValue)) {
//			return true;
//		} else {
//			this.throwError(this.inputController.getErrorMessage());
//		}
//		return false;
//	}
	
	private void throwError(String errorMessage) {
		JOptionPane.showMessageDialog(this, errorMessage, "Wrong input!", JOptionPane.ERROR_MESSAGE);
	}
	

}
