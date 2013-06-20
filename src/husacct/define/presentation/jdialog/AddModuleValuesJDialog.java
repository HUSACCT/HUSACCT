package husacct.define.presentation.jdialog;

import husacct.ServiceProvider;
import husacct.common.Resource;
import husacct.control.ControlServiceImpl;
import husacct.control.ILocaleChangeListener;
import husacct.define.domain.module.ModuleFactory;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.DomainGateway;
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
	
	private ModuleJPanel modulePanel;
	private JPanel innerPanel;
	private JLabel parentModuleNameLabel;
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
		this.setVisibles();
	}
	
	private void addParentModule() {
		JLabel parentModuleLabel = new JLabel(ServiceProvider.getInstance().getLocaleService().getTranslatedString("ParentModule"));
		this.innerPanel.add(parentModuleLabel);
		
		Long selectedModuleId = DefinitionController.getInstance().getSelectedModuleId();
		String parentModuleName = DefinitionController.getInstance().getModuleName(selectedModuleId);
		
		parentModuleNameLabel = new JLabel(parentModuleName);
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
		
		String[] moduleTypes = {ServiceProvider.getInstance().getLocaleService().getTranslatedString("SubSystem"),
				ServiceProvider.getInstance().getLocaleService().getTranslatedString("Layer"), 
				ServiceProvider.getInstance().getLocaleService().getTranslatedString("Component"), 
				ServiceProvider.getInstance().getLocaleService().getTranslatedString("ExternalLibrary")};
		this.moduleTypeComboBox = new JComboBox<>(moduleTypes);
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
		if(this.checkModuleName()) {
			String moduleName = this.moduleNameField.getText();
			String moduleDescription = this.moduleDescriptionField.getText();			
			
			boolean hasBeenAdded = DomainGateway.getInstance().addModule(moduleName, moduleDescription, moduleType);
			if (hasBeenAdded){
				//Dirty fix to add facade to the component, logic should be moved elsewhere
				if(moduleType.equals("Component")) {
				    DomainGateway.getInstance().addModule("Facade<"+moduleName+">", "this is the Facade of your Component", "Facade");
				}
				this.modulePanel.updateModuleTree();
				this.dispose();
			}
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
