package husacct.define.presentation.jdialog;

import husacct.ServiceProvider;
import husacct.common.Resource;
import husacct.common.help.presentation.HelpableJDialog;
import husacct.control.ControlServiceImpl;
import husacct.control.ILocaleChangeListener;
import husacct.define.presentation.jpanel.ModuleJPanel;
import husacct.define.task.DefinitionController;
import husacct.define.task.ValueInputController;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Locale;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddModuleValuesJDialog extends HelpableJDialog implements KeyListener, ActionListener, ILocaleChangeListener {

	private static final long serialVersionUID = -1729066215610611394L;
	
	private ModuleJPanel modulePanel;
	private JPanel innerPanel;
	private JTextField moduleNameField;
	private JTextField moduleDescriptionField;	
	private JComboBox<?> moduleTypeComboBox;
	
	private JButton cancelButton;
	private JButton saveButton;
	private ValueInputController inputController;
	
	public AddModuleValuesJDialog(ModuleJPanel modulePanel) {
		super(((ControlServiceImpl) ServiceProvider.getInstance().getControlService()).getMainController().getMainGui(), true);
		this.modulePanel = modulePanel;
		this.inputController = new ValueInputController();

		this.setSize(700, 190);
	}
	
	public void initGUI() {
		this.setTitle(ServiceProvider.getInstance().getLocaleService().getTranslatedString("NewModule"));
		this.setIconImage(new ImageIcon(Resource.get(Resource.HUSACCT_LOGO)).getImage());
		
		this.innerPanel = new JPanel();
		this.innerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		this.innerPanel.setLayout(this.getGridLayout(5, 2));
		this.createInnerPanel();
		this.add(this.innerPanel);
		
		this.setResizable(true);
		this.setSize(700, 190);
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
	}
	
	private void addParentModule() {
		JLabel parentModuleLabel = new JLabel(ServiceProvider.getInstance().getLocaleService().getTranslatedString("ParentModule"));
		this.innerPanel.add(parentModuleLabel);
		
		Long selectedModuleId = DefinitionController.getInstance().getSelectedModuleId();
		String parentModuleName = DefinitionController.getInstance().getModuleName(selectedModuleId);

		JLabel parentModuleNameLabel = new JLabel(parentModuleName);
		this.innerPanel.add(parentModuleNameLabel);
	}
	
	private void addModuleValues() {
		JLabel moduleLabel = new JLabel(ServiceProvider.getInstance().getLocaleService().getTranslatedString("ModuleName"));
		this.innerPanel.add(moduleLabel);
		
		this.moduleNameField = new JTextField();
		this.moduleNameField.addKeyListener(this);
		this.innerPanel.add(this.moduleNameField);
	}
	
	private void addModuleDescriptionTextArea() {
		JLabel moduleLabel = new JLabel(ServiceProvider.getInstance().getLocaleService().getTranslatedString("ModuleDescription"));
		this.innerPanel.add(moduleLabel);
		
		this.moduleDescriptionField = new JTextField();
		this.moduleDescriptionField.addKeyListener(this);
		
		this.innerPanel.add(this.moduleDescriptionField);
	}
	
	private void addModuleTypeComboBox() {
		JLabel moduleTypeLabel = new JLabel(ServiceProvider.getInstance().getLocaleService().getTranslatedString("ModuleType"));
		this.innerPanel.add(moduleTypeLabel);
		String[] translatedModuleTypes = {ServiceProvider.getInstance().getLocaleService().getTranslatedString("SubSystem"),
				ServiceProvider.getInstance().getLocaleService().getTranslatedString("Layer"), 
				ServiceProvider.getInstance().getLocaleService().getTranslatedString("Component"), 
				ServiceProvider.getInstance().getLocaleService().getTranslatedString("ExternalLibrary")};
		this.moduleTypeComboBox = new JComboBox<>(translatedModuleTypes);
		this.moduleTypeComboBox.setSelectedIndex(0);
		this.moduleTypeComboBox.addActionListener(this);
		this.moduleTypeComboBox.addKeyListener(this);
		this.innerPanel.add(this.moduleTypeComboBox);
	}
	
	protected void addButtons() {
		this.cancelButton = new JButton();
		this.innerPanel.add(this.cancelButton);
		this.cancelButton.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Cancel"));
		this.cancelButton.addActionListener(this);
		
		this.saveButton = new JButton();
		this.innerPanel.add(this.saveButton);
		this.saveButton.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Save"));
		this.saveButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == this.cancelButton) {
			this.cancelButtonAction();
		} else if (event.getSource() == this.saveButton) {
			this.saveButtonAction();
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
		int location = this.moduleTypeComboBox.getSelectedIndex();
		String[] moduleTypes = { "SubSystem", "Layer", "Component", "ExternalLibrary" };
		String moduleType = moduleTypes[location];
		this.submitForModuleType(moduleType);
	}
	
	private void submitForModuleType(String moduleType) {
		if(this.checkModuleName()) {
			String moduleName = this.moduleNameField.getText();
			String moduleDescription = this.moduleDescriptionField.getText();			
			DefinitionController.getInstance().addModule(moduleName, moduleDescription, moduleType);			
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
		JOptionPane.showMessageDialog(this, errorMessage, ServiceProvider.getInstance().getLocaleService().getTranslatedString("WrongInputTitle"), JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void update(Locale newLocale) {
		this.setTitle(ServiceProvider.getInstance().getLocaleService().getTranslatedString("NewModule"));
	}
	
	
	
}
