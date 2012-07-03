package husacct.define.presentation.jpanel;

import husacct.ServiceProvider;
import husacct.common.services.IServiceListener;

import husacct.define.presentation.utils.DefaultMessages;
import husacct.define.task.DefinitionController;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
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


	@Override
	public void update(Observable o, Object arg) {
		resetGUI();
		Long moduleId = Long.parseLong(arg.toString());
		if (moduleId != -1){
			HashMap<String, Object> moduleDetails = DefinitionController.getInstance().getModuleDetails(moduleId);
			this.nameTextfield.setText((String) moduleDetails.get("name"));
			this.descriptionTextArea.setText((String) moduleDetails.get("description"));
		}
		this.repaint();
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
}
