package husacct.define.presentation.jpanel;

import husacct.define.presentation.utils.DefaultMessages;
import husacct.define.task.DefinitionController;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * 
 * @author Henk ter Harmsel
 *
 */
public class EditModuleJPanel extends AbstractDefinitionJPanel implements FocusListener, Observer{

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
		this.setBorder(BorderFactory.createTitledBorder("Module configuration"));
		this.setPreferredSize(new java.awt.Dimension(442, 105));
		
		this.addModuleNameLabel();
		this.addModuleNameTextField();
		this.addModuleDescriptionLabel();
		this.addModuleDescriptionScrollPane();
		
	}
	
	private void setDefaultGridLayout() {
		GridBagLayout jPanel4Layout = new GridBagLayout();
		jPanel4Layout.rowWeights = new double[] { 0.0, 0.1, 0.1 };
		jPanel4Layout.rowHeights = new int[] { 27, 7, 7 };
		jPanel4Layout.columnWeights = new double[] { 0.0, 0.1 };
		jPanel4Layout.columnWidths = new int[] { 118, 7 };
		this.setLayout(jPanel4Layout);
	}
	
	private void addModuleNameLabel() {
		nameLabel = new JLabel();
		this.add(nameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		nameLabel.setText("Module name");
	}
	
	private void addModuleNameTextField() {
		nameTextfield = new JTextField();
		nameTextfield.setToolTipText(DefaultMessages.TIP_LAYER);
		this.add(nameTextfield, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		nameTextfield.addFocusListener(this);
	}
	
	private void addModuleDescriptionLabel() {
		descriptionLabel = new JLabel();
		this.add(descriptionLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		descriptionLabel.setText("Description");
	}
	
	private void addModuleDescriptionScrollPane() {
		descriptionScrollPane = new JScrollPane();
		this.add(descriptionScrollPane, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		descriptionScrollPane.setPreferredSize(new java.awt.Dimension(142, 26));
		descriptionScrollPane.setViewportView(this.createModuleDescriptionTextArea());
	}

	private JTextArea createModuleDescriptionTextArea() {
		descriptionTextArea = new JTextArea();
		descriptionTextArea.setFont(new java.awt.Font("Tahoma", 0, 11));
		descriptionTextArea.setToolTipText(DefaultMessages.TIP_LAYERDESCRIPTION);
		descriptionTextArea.addFocusListener(this);
		return descriptionTextArea;
	}


	@Override
	public void update(Observable o, Object arg) {
		Long moduleId = Long.parseLong(arg.toString());
		if (moduleId != -1){
			HashMap<String, Object> moduleDetails = DefinitionController.getInstance().getModuleDetails(moduleId);
			this.nameTextfield.setText((String) moduleDetails.get("name"));
			this.descriptionTextArea.setText((String) moduleDetails.get("description"));
		}
		this.repaint();
	}
	
	private void updateModule() {
		String moduleName = nameTextfield.getText();
		String moduleDescription = descriptionTextArea.getText();
		DefinitionController.getInstance().updateModule(moduleName, moduleDescription);
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		updateModule();
	}
}
