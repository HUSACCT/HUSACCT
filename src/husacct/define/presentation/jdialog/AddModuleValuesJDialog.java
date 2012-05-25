package husacct.define.presentation.jdialog;

import husacct.control.ILocaleChangeListener;
import husacct.define.abstraction.language.DefineTranslator;
import husacct.define.presentation.jpanel.ModuleJPanel;
import husacct.define.task.DefinitionController;
import husacct.define.task.ValueInputController;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddModuleValuesJDialog extends JDialog implements KeyListener, ActionListener, ILocaleChangeListener {

	private static final long serialVersionUID = -1729066215610611394L;
	private final String husacctIcon = "husacct/common/resources/husacct.png";
	
	private ModuleJPanel modulePanel;
	private JPanel innerPanel;
	private JLabel parentModuleNameLabel;
	private JTextField moduleNameField;
	private JTextField moduleDescriptionField;	
	private JComboBox moduleTypeComboBox;
	
	private JButton cancelButton;
	private JButton saveButton;
	private ValueInputController inputController;
	
	public AddModuleValuesJDialog(ModuleJPanel modulePanel) {
		super();
		this.modulePanel = modulePanel;
		this.inputController = new ValueInputController();

		this.setSize(700, 190);
	}
	
<<<<<<< HEAD
	private void initGUI() {
		this.setTitle(DefineTranslator.translate("NewModule"));
=======
	public void initGUI() {
		this.setTitle("New Module");
>>>>>>> 3c010dd056c1ba53d86096a9a5a099e6a49f5846
		this.setIconImage(new ImageIcon(getClass().getClassLoader().getResource(husacctIcon)).getImage());
		
		this.innerPanel = new JPanel();
		this.innerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		this.innerPanel.setLayout(this.getGridLayout(5, 2));
		this.createInnerPanel();
		this.add(this.innerPanel);
		
		this.setResizable(true);
		this.setSize(700, 190);
		this.setModal(true);
		this.setVisible(true);
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
		this.addButtons();
		this.setVisibles();
	}
	
	private void addParentModule() {
		JLabel parentModuleLabel = new JLabel(DefineTranslator.translate("ParentModule"));
		this.innerPanel.add(parentModuleLabel);
		
		Long selectedModuleId = DefinitionController.getInstance().getSelectedModuleId();
		String parentModuleName = DefinitionController.getInstance().getModuleName(selectedModuleId);
		
		parentModuleNameLabel = new JLabel(parentModuleName);
		this.innerPanel.add(parentModuleNameLabel);
	}
	
	private void addModuleValues() {
		JLabel moduleLabel = new JLabel(DefineTranslator.translate("ModuleName"));
		this.innerPanel.add(moduleLabel);
		
		this.moduleNameField = new JTextField();
		this.moduleNameField.addKeyListener(this);
		this.innerPanel.add(this.moduleNameField);
	}
	
	private void addModuleDescriptionTextArea() {
		JLabel moduleLabel = new JLabel(DefineTranslator.translate("ModuleDescription"));
		this.innerPanel.add(moduleLabel);
		
		this.moduleDescriptionField = new JTextField();
		this.moduleDescriptionField.addKeyListener(this);
		
		this.innerPanel.add(this.moduleDescriptionField);
	}
	
	private void addModuleTypeComboBox() {
		JLabel moduleTypeLabel = new JLabel(DefineTranslator.translate("ModuleType"));
		this.innerPanel.add(moduleTypeLabel);
		
		String[] moduleTypes = {DefineTranslator.translate("SubSystem"), DefineTranslator.translate("Layer"), DefineTranslator.translate("Component"), DefineTranslator.translate("ExternalLibrary")};
		this.moduleTypeComboBox = new JComboBox(moduleTypes);
		this.moduleTypeComboBox.setSelectedIndex(0);
		this.moduleTypeComboBox.addActionListener(this);
		this.moduleTypeComboBox.addKeyListener(this);
		this.innerPanel.add(this.moduleTypeComboBox);
	}
	
	protected void addButtons() {
		this.cancelButton = new JButton();
		this.innerPanel.add(this.cancelButton);
		this.cancelButton.setText(DefineTranslator.translate("Cancel"));
		this.cancelButton.addActionListener(this);
		
		this.saveButton = new JButton();
		this.innerPanel.add(this.saveButton);
		this.saveButton.setText(DefineTranslator.translate("Save"));
		this.saveButton.addActionListener(this);
	}

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
	}
	
	private void setVisibles() {
		String moduleType = this.moduleTypeComboBox.getSelectedItem().toString();
		if(moduleType == "SubSystem") {
			
		} else if(moduleType =="Layer") {
			
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

	protected void cancelButtonAction() {
		this.dispose();
	}

	protected void saveButtonAction() {
		String moduleType = this.moduleTypeComboBox.getSelectedItem().toString();
		this.submitForModuleType(moduleType);
	}
	
	private void submitForModuleType(String moduleType) {
		if(moduleType == DefineTranslator.translate("SubSystem")) {
			this.submitSubSystem();
		} else if(moduleType == DefineTranslator.translate("Layer")) {
			this.submitLayer();
		} else if(moduleType == DefineTranslator.translate("Component")) {
			this.submitComponent();
		} else if(moduleType == DefineTranslator.translate("ExternalLibrary")) {
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
	
	private void throwError(String errorMessage) {
		JOptionPane.showMessageDialog(this, errorMessage, DefineTranslator.translate("WrongInputTitle"), JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void update(Locale newLocale) {
		this.setTitle(DefineTranslator.translate("NewModule"));
	}
	

}
