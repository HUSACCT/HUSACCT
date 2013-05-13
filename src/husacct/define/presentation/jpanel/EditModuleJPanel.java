package husacct.define.presentation.jpanel;

import husacct.ServiceProvider;
import husacct.common.services.IServiceListener;

import husacct.define.presentation.utils.DefaultMessages;
import husacct.define.task.DefinitionController;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class EditModuleJPanel extends JPanel implements KeyListener, Observer, IServiceListener{

	private static final long serialVersionUID = -9020336576931490389L;
	private JLabel nameLabel;
	private JTextField nameTextfield;
	private JLabel descriptionLabel;
	private JScrollPane descriptionScrollPane;
	private JTextArea descriptionTextArea;
	private JLabel moduleTypeLabel;
	private JComboBox moduleTypeComboBox;
	private String[] moduleTypes = {ServiceProvider.getInstance().getLocaleService().getTranslatedString("SubSystem"),
			ServiceProvider.getInstance().getLocaleService().getTranslatedString("Layer"), 
			ServiceProvider.getInstance().getLocaleService().getTranslatedString("Component"), 
			ServiceProvider.getInstance().getLocaleService().getTranslatedString("ExternalLibrary")};
	private String[] facadeType= {"Facade"};
    private int currentSelection;

	public EditModuleJPanel() {
		super();
	}

	public void initGui() {
		DefinitionController.getInstance().addObserver(this);
		this.setDefaultGridLayout();
		this.setBorder(BorderFactory.createTitledBorder(ServiceProvider.getInstance().getLocaleService().getTranslatedString("ModulePropertiesTitle")));
		this.setPreferredSize(new java.awt.Dimension(442, 105));
		
		addModuleNameComponent();
		addModuleDescriptionComponent();
		addModuleType();
		ServiceProvider.getInstance().getControlService().addServiceListener(this);
		
	}
	
	

	private void setDefaultGridLayout() {
		GridBagLayout jPanel4Layout = new GridBagLayout();
		jPanel4Layout.rowWeights = new double[] { 0.0, 0.1, 0.1 };
		jPanel4Layout.rowHeights = new int[] { 27, 7, 7 };
		jPanel4Layout.columnWeights = new double[] { 0.0, 0.1 };
		jPanel4Layout.columnWidths = new int[] { 118, 7 };
		this.setLayout(jPanel4Layout);
	}
	
	private void addModuleNameComponent() {
		nameLabel = new JLabel();
		this.add(nameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		nameLabel.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("ModuleName"));
		
		nameTextfield = new JTextField();
		nameTextfield.setToolTipText(DefaultMessages.TIP_MODULE);
		this.add(nameTextfield, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		nameTextfield.addKeyListener(this);
	}
	
	private void addModuleDescriptionComponent() {
		descriptionLabel = new JLabel();
		this.add(descriptionLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		descriptionLabel.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Description"));
		
		descriptionScrollPane = new JScrollPane();
		this.add(descriptionScrollPane, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		descriptionScrollPane.setPreferredSize(new java.awt.Dimension(142, 26));
		descriptionScrollPane.setViewportView(this.createModuleDescriptionTextArea());
	}
	
	private JTextArea createModuleDescriptionTextArea() {
		descriptionTextArea = new JTextArea();
		descriptionTextArea.setFont(new java.awt.Font("Tahoma", 0, 11));
		descriptionTextArea.setToolTipText(DefaultMessages.TIP_MODULEDESCRIPTION);
		descriptionTextArea.addKeyListener(this);
		return descriptionTextArea;
	}
	
	private void addModuleType() {
		
		moduleTypeComboBox=new JComboBox(moduleTypes);
		moduleTypeLabel = new JLabel();
		this.add(moduleTypeLabel, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		moduleTypeLabel.setText("Module Type");
		this.add(moduleTypeComboBox, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		moduleTypeComboBox.addActionListener(moduleTypeComboboxOnChangeListener);
	
		
	}


	@Override
	public void update(Observable o, Object arg) {
		resetGUI();
		Long moduleId = Long.parseLong(arg.toString());
		if (moduleId != -1){
			HashMap<String, Object> moduleDetails = DefinitionController.getInstance().getModuleDetails(moduleId);
			this.nameTextfield.setText((String) moduleDetails.get("name"));
			this.descriptionTextArea.setText((String) moduleDetails.get("description"));
			String type =ServiceProvider.getInstance().getLocaleService().getTranslatedString((String) moduleDetails.get("type")); 
			this.moduleTypeComboBox.setEnabled(true);
			this.moduleTypeComboBox.setSelectedIndex(getModuleType(type));
		}
		this.repaint();
	}
	
	
	
	
	
	
	
	
	
	private int getModuleType(String type) {
		DefaultComboBoxModel defaultModel = new DefaultComboBoxModel(moduleTypes);
		this.moduleTypeComboBox.setModel(defaultModel);
		int result = 0;
		if (ServiceProvider.getInstance().getLocaleService().getTranslatedString("Layer").toLowerCase().equals(type.toLowerCase())) {
		  
			result=1;
		} else if(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Component").toLowerCase().equals(type.toLowerCase())) {
			result=2; 
		}else if(ServiceProvider.getInstance().getLocaleService().getTranslatedString("SubSystem").toLowerCase().equals(type.toLowerCase())) {
			result=0;
		}else if(ServiceProvider.getInstance().getLocaleService().getTranslatedString("ExternalLibrary").toLowerCase().equals(type.toLowerCase())) {
			result=3;
		}else{
			this.moduleTypeComboBox.setEnabled(false);
			DefaultComboBoxModel facadeModel = new DefaultComboBoxModel(facadeType);
			this.moduleTypeComboBox.setModel(facadeModel);
		}
		currentSelection=result;
		return result;
	
	}

	private void resetGUI() {
		this.nameTextfield.setText("");
		this.descriptionTextArea.setText("");
	}

	private void updateModule() {
		String moduleName = nameTextfield.getText();
		String moduleDescription = descriptionTextArea.getText();
		DefinitionController.getInstance().updateModule(moduleName, moduleDescription);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		//Ignore, this method is not needed.
	}

	@Override
	public void keyReleased(KeyEvent e) {
		updateModule();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		//Ignore, this method is not needed.
	}

	@Override
	public void update() {
		this.setBorder(BorderFactory.createTitledBorder(ServiceProvider.getInstance().getLocaleService().getTranslatedString("ModulePropertiesTitle")));
		nameLabel.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("ModuleName"));
		descriptionLabel.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Description"));
	}
	
	ActionListener moduleTypeComboboxOnChangeListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JComboBox selectedCombobox = (JComboBox) e.getSource();
		 if (currentSelection!=selectedCombobox.getSelectedIndex()) {
			 String moduleName = nameTextfield.getText();
			 String moduleDescription = descriptionTextArea.getText();
			 String type=(String) selectedCombobox.getItemAt(selectedCombobox.getSelectedIndex());
			
			DefinitionController.getInstance().updateModule(moduleName, moduleDescription,type);
		}
			
			
		}
	};
		
	
	
	
}
