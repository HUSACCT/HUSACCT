package husacct.define.presentation.jpanel;

import husacct.ServiceProvider;
import husacct.common.help.presentation.HelpableJPanel;
import husacct.common.services.IServiceListener;
import husacct.define.presentation.draganddrop.customdroptargetlisterner.EditpanelDropListener;
import husacct.define.presentation.utils.DefaultMessages;
import husacct.define.task.DefinitionController;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

public class EditModuleJPanel extends HelpableJPanel implements KeyListener, Observer,
		IServiceListener {

	private static final long serialVersionUID = -9020336576931490389L;
	private String _type;
	private JLabel descriptionLabel;
	private JScrollPane descriptionScrollPane;
	private JTextArea descriptionTextArea;
	private String[] facadeType = { "Interface" };
	private JComboBox<String> moduleTypeComboBox;
	private EditpanelDropListener listener = new EditpanelDropListener(this);
	private JLabel moduleTypeLabel;
	private String[] moduleTypes_Translated = {
			ServiceProvider.getInstance().getLocaleService().getTranslatedString("SubSystem"),
			ServiceProvider.getInstance().getLocaleService().getTranslatedString("Layer"),
			ServiceProvider.getInstance().getLocaleService().getTranslatedString("Component"),
			ServiceProvider.getInstance().getLocaleService().getTranslatedString("ExternalLibrary") };
	private JLabel nameLabel;
	private JTextField nameTextfield;
	private final Logger logger = Logger.getLogger(EditModuleJPanel.class);

	public EditModuleJPanel() {
		super();
	}

	ActionListener moduleTypeComboboxOnChangeListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			String moduleType;
			if (moduleTypeComboBox.getModel().getSize() == 1) {
				moduleType = "Facade";
			} else {
				int location = moduleTypeComboBox.getSelectedIndex();
				String[] moduleTypes = { "SubSystem", "Layer", "Component", "ExternalLibrary" };
				moduleType = moduleTypes[location];
			}
			if (!_type.equals(moduleType)) {
				if (!moduleType.toLowerCase().equals("facade")) {
					DefinitionController.getInstance().updateModuleType(moduleType);
				} 
			}
		}
	};

	private void addModuleDescriptionComponent() {
		descriptionLabel = new JLabel();
		this.add(descriptionLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		descriptionLabel.setText(ServiceProvider.getInstance()
				.getLocaleService().getTranslatedString("Description"));

		descriptionScrollPane = new JScrollPane();
		this.add(descriptionScrollPane, new GridBagConstraints(1, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		descriptionScrollPane.setPreferredSize(new java.awt.Dimension(142, 26));
		descriptionScrollPane
				.setViewportView(createModuleDescriptionTextArea());
	}

	private void addModuleNameComponent() {
		nameLabel = new JLabel();
		this.add(nameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		nameLabel.setText(ServiceProvider.getInstance().getLocaleService()
				.getTranslatedString("ModuleName"));

		nameTextfield = new JTextField();
		listener.addTarget(nameTextfield);
		nameTextfield.setToolTipText(DefaultMessages.TIP_MODULE);
		this.add(nameTextfield, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		nameTextfield.addKeyListener(this);
	}

	private void addModuleTypeComboBox() {
		moduleTypeComboBox = new JComboBox<>(moduleTypes_Translated);
		moduleTypeLabel = new JLabel();
		this.add(moduleTypeLabel, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0,
				GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		moduleTypeLabel.setText("Module Type");
		this.add(moduleTypeComboBox, new GridBagConstraints(1, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		moduleTypeComboBox.addActionListener(moduleTypeComboboxOnChangeListener);
	}

	private JTextArea createModuleDescriptionTextArea() {
		descriptionTextArea = new JTextArea();
		listener.addTarget(descriptionTextArea);
		descriptionTextArea.setFont(new java.awt.Font("Tahoma", 0, 11));
		descriptionTextArea
				.setToolTipText(DefaultMessages.TIP_MODULEDESCRIPTION);
		descriptionTextArea.addKeyListener(this);
		return descriptionTextArea;
	}

	public void initGui() {
		DefinitionController.getInstance().addObserver(this);
		setDefaultGridLayout();
		setBorder(BorderFactory.createTitledBorder(ServiceProvider
				.getInstance().getLocaleService()
				.getTranslatedString("ModulePropertiesTitle")));
		setPreferredSize(new java.awt.Dimension(542, 105));

		addModuleNameComponent();
		addModuleDescriptionComponent();
		addModuleTypeComboBox();
		ServiceProvider.getInstance().getControlService()
				.addServiceListener(this);

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// Ignore, this method is not needed.
	}

	@Override
	public void keyReleased(KeyEvent e) {
		String moduleName = nameTextfield.getText();
		String moduleDescription = descriptionTextArea.getText();
		DefinitionController.getInstance().updateModule(moduleName, moduleDescription);
		if (moduleTypeComboBox.getSelectedItem().toString().equalsIgnoreCase(moduleTypes_Translated[2])) {
			DefinitionController.getInstance().updateFacade(moduleName);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// Ignore, this method is not needed.
	}

	private void resetGUI() {
		nameTextfield.setText("");
		descriptionTextArea.setText("");
	}

	private void setDefaultGridLayout() {
		GridBagLayout jPanel4Layout = new GridBagLayout();
		jPanel4Layout.rowWeights = new double[] { 0.0, 0.1, 0.1 };
		jPanel4Layout.rowHeights = new int[] { 27, 7, 7 };
		jPanel4Layout.columnWeights = new double[] { 0.0, 0.1 };
		jPanel4Layout.columnWidths = new int[] { 118, 7 };
		setLayout(jPanel4Layout);
	}

	@Override
	public void update() {
		setBorder(BorderFactory.createTitledBorder(ServiceProvider
				.getInstance().getLocaleService()
				.getTranslatedString("ModulePropertiesTitle")));
		nameLabel.setText(ServiceProvider.getInstance().getLocaleService()
				.getTranslatedString("ModuleName"));
		descriptionLabel.setText(ServiceProvider.getInstance()
				.getLocaleService().getTranslatedString("Description"));
	}

	@Override
	public void update(Observable o, Object arg) {
		resetGUI();
		try{
			Long moduleId = Long.parseLong(arg.toString());
			if (moduleId != -1) {
				HashMap<String, Object> moduleDetails = DefinitionController.getInstance().getModuleDetails(moduleId);
				if (!((String) moduleDetails.get("name")).equals("")) {
					nameTextfield.setText((String) moduleDetails.get("name"));
					descriptionTextArea.setText((String) moduleDetails.get("description"));
					String moduleType = (String) moduleDetails.get("type");
					String moduleType_Translated = ServiceProvider.getInstance().getLocaleService().getTranslatedString(moduleType);
					_type= moduleType_Translated;
					// Fill moduleTypeComboBox.
					if (moduleType.equals("Facade")) {
						DefaultComboBoxModel<String> facadeModel = new DefaultComboBoxModel<>(facadeType);
						moduleTypeComboBox.setModel(facadeModel);
						moduleTypeComboBox.setSelectedIndex(0);
						moduleTypeComboBox.setEnabled(false);
					} else {
						DefaultComboBoxModel<String> defaultModel = new DefaultComboBoxModel<>(moduleTypes_Translated);
						moduleTypeComboBox.setModel(defaultModel);
						for (int i = 0; i < moduleTypes_Translated.length; i++) {
							if (moduleType_Translated.equalsIgnoreCase(moduleTypes_Translated[i])) {
								moduleTypeComboBox.setSelectedIndex(i);
							}
						}
						moduleTypeComboBox.setEnabled(true);
					}
				}
			}
			this.repaint();
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
	}

}
