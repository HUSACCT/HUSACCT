package husacct.define.presentation.jpanel;

import husacct.ServiceProvider;
import husacct.common.enums.ModuleTypes;
import husacct.common.help.presentation.HelpableJPanel;
import husacct.common.services.IServiceListener;
import husacct.define.presentation.draganddrop.customdroptargetlisterner.EditpanelDropListener;
import husacct.define.presentation.utils.DefaultMessages;
import husacct.define.task.DefinitionController;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.apache.log4j.Logger;

public class EditModuleJPanel extends HelpableJPanel implements Observer,IServiceListener {

	private static final long serialVersionUID = -9020336576931490389L;
	
	private JLabel nameLabel;
	private JTextField nameTextfield;

	private JLabel descriptionLabel;
	private JScrollPane descriptionScrollPane;
	private JTextArea descriptionTextArea;
	private JButton updateDescriptionButton;

	private JLabel moduleTypeLabel;
	private JComboBox<String> moduleTypeComboBox;
	private String[] moduleTypes_Translated = {
			ServiceProvider.getInstance().getLocaleService().getTranslatedString(ModuleTypes.SUBSYSTEM.toString()),
			ServiceProvider.getInstance().getLocaleService().getTranslatedString(ModuleTypes.LAYER.toString()),
			ServiceProvider.getInstance().getLocaleService().getTranslatedString(ModuleTypes.COMPONENT.toString()),
			ServiceProvider.getInstance().getLocaleService().getTranslatedString(ModuleTypes.EXTERNAL_LIBRARY.toString()) };

	private String[] facadeType = { "Interface" };
	private EditpanelDropListener listener = new EditpanelDropListener(this);
	private final Logger logger = Logger.getLogger(EditModuleJPanel.class);

	public EditModuleJPanel() {
		super();
	}

	public void initGui() {
		DefinitionController.getInstance().addObserver(this);
		BorderLayout borderLayout = new BorderLayout();
		setLayout(borderLayout);
		setBorder(BorderFactory.createTitledBorder(ServiceProvider
				.getInstance().getLocaleService()
				.getTranslatedString("ModulePropertiesTitle")));
		setPreferredSize(new java.awt.Dimension(542, 120));
		this.add(addDetailsPanel(), BorderLayout.CENTER);
		this.add(addUpdatePanel(), BorderLayout.EAST);
		ServiceProvider.getInstance().getControlService().addServiceListener(this);
	}

	private JPanel addDetailsPanel() {
		JPanel detailsPanel = new JPanel();
		setDefaultGridLayout(detailsPanel);
		detailsPanel.setPreferredSize(new java.awt.Dimension(439, 120));
		addModuleNameComponent(detailsPanel);
		addModuleDescriptionComponent(detailsPanel);
		addModuleTypeComboBox(detailsPanel);
		return detailsPanel;
	}
	
	private JPanel addUpdatePanel() {
		JPanel upDatePanel = new JPanel();
		BorderLayout borderLayout = new BorderLayout();
		upDatePanel.setLayout(borderLayout);
		upDatePanel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 2));
		upDatePanel.setPreferredSize(new java.awt.Dimension(90, 120));
		updateDescriptionButton = new JButton();
		upDatePanel.add(updateDescriptionButton, BorderLayout.PAGE_END);
		updateDescriptionButton.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Update"));
		updateDescriptionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String moduleName = nameTextfield.getText();
				String moduleDescription = descriptionTextArea.getText();
				ModuleTypes moduleType;
				if (moduleTypeComboBox.getModel().getSize()== 1) { // Excludes Facade/Interface
					moduleType = ModuleTypes.FACADE;
				} else {
					ModuleTypes[] moduleTypes = {ModuleTypes.SUBSYSTEM, ModuleTypes.LAYER, 
							ModuleTypes.COMPONENT, ModuleTypes.EXTERNAL_LIBRARY};
					int indexSelectedModule = moduleTypeComboBox. getSelectedIndex();
					moduleType = moduleTypes[indexSelectedModule];
				}
				DefinitionController.getInstance().updateModuleDetails(moduleName, moduleDescription, moduleType);
			}
		});
		return upDatePanel;
	}
	
	private void addModuleNameComponent(JPanel detailsPanel) {
		nameLabel = new JLabel();
		detailsPanel.add(nameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		nameLabel.setText(ServiceProvider.getInstance().getLocaleService()
				.getTranslatedString("ModuleName"));

		nameTextfield = new JTextField();
		listener.addTarget(nameTextfield);
		nameTextfield.setToolTipText(DefaultMessages.TIP_MODULE);
		detailsPanel.add(nameTextfield, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	}

	private void addModuleDescriptionComponent(JPanel detailsPanel) {
		descriptionLabel = new JLabel();
		GridBagConstraints gbc1 = new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
		detailsPanel.add(descriptionLabel, gbc1);
		descriptionLabel.setText(ServiceProvider.getInstance()
				.getLocaleService().getTranslatedString("Description"));

		descriptionScrollPane = new JScrollPane();
		GridBagConstraints gbc2 = new GridBagConstraints(1, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		detailsPanel.add(descriptionScrollPane, gbc2);
		descriptionScrollPane.setPreferredSize(new java.awt.Dimension(142, 40));

		descriptionTextArea = new JTextArea();
		listener.addTarget(descriptionTextArea);
		//descriptionTextArea.setFont(new java.awt.Font("Tahoma", 0, 11));
		descriptionTextArea.setToolTipText(DefaultMessages.TIP_MODULEDESCRIPTION);
		//descriptionTextArea.addKeyListener(this);
		descriptionScrollPane.setViewportView(descriptionTextArea);
	}

	private void addModuleTypeComboBox(JPanel detailsPanel) {
		moduleTypeComboBox = new JComboBox<String>(moduleTypes_Translated);
		moduleTypeLabel = new JLabel();
		detailsPanel.add(moduleTypeLabel, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0,
				GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE,
				new Insets(5, 0, 0, 0), 0, 0));
		moduleTypeLabel.setText("Module Type");
		detailsPanel.add(moduleTypeComboBox, new GridBagConstraints(1, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.BOTH, new Insets(5, 0, 0, 0), 0, 0));
	}

	private void resetGUI() {
		nameTextfield.setText("");
		descriptionTextArea.setText("");		
	}

	private void setDefaultGridLayout(JPanel detailsPanel) {
		GridBagLayout jPanel4Layout = new GridBagLayout();
		jPanel4Layout.rowWeights = new double[] { 0.0, 0.1, 0.1 };
		jPanel4Layout.rowHeights = new int[] { 27, 7, 7 };
		jPanel4Layout.columnWeights = new double[] { 0.0, 0.1 };
		jPanel4Layout.columnWidths = new int[] { 118, 7 };
		detailsPanel.setLayout(jPanel4Layout);
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
					// Fill moduleTypeComboBox.
					if (moduleType.equals("Facade")) {
						DefaultComboBoxModel<String> facadeModel = new DefaultComboBoxModel<String>(facadeType);
						moduleTypeComboBox.setModel(facadeModel);
						moduleTypeComboBox.setSelectedIndex(0);
						moduleTypeComboBox.setEnabled(false);
					} else {
						String moduleType_Translated = ServiceProvider.getInstance().getLocaleService().getTranslatedString(moduleType);
						DefaultComboBoxModel<String> defaultModel = new DefaultComboBoxModel<String>(moduleTypes_Translated);
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
