package husacct.define.presentation.jpanel;

import husacct.define.presentation.utils.DefaultMessages;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
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
public class EditModuleJPanel extends AbstractDefinitionJPanel {

	private static final long serialVersionUID = -9020336576931490389L;

	public EditModuleJPanel() {
		super();
	}

	@Override
	public void initGui() {
		this.setDefaultGridLayout();
		this.setBorder(BorderFactory.createTitledBorder("Module configuration"));
		this.setPreferredSize(new java.awt.Dimension(442, 105));
		
		this.addModuleNameLabel();
		this.addModuleNameTextField();
		this.addModuleDescriptionLabel();
		this.addModuleDescriptionScrollPane();
		this.addFacadeCheckBox();
		this.addModuleAccessLabel();
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
		JLabel moduleNameLabel = new JLabel();
		this.add(moduleNameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		moduleNameLabel.setText("Module name");
	}
	
	private void addModuleNameTextField() {
		JTextField moduleNameTextfield = new JTextField();
		moduleNameTextfield.setToolTipText(DefaultMessages.TIP_LAYER);
		this.add(moduleNameTextfield, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	}
	
	private void addModuleDescriptionLabel() {
		JLabel descriptionLabel = new JLabel();
		this.add(descriptionLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		descriptionLabel.setText("Description");
	}
	
	private void addModuleDescriptionScrollPane() {
		JScrollPane descriptionScrollPane = new JScrollPane();
		this.add(descriptionScrollPane, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		descriptionScrollPane.setPreferredSize(new java.awt.Dimension(142, 26));
		descriptionScrollPane.setViewportView(this.createModuleDescriptionTextArea());
	}

	private JTextArea createModuleDescriptionTextArea() {
		JTextArea descriptionTextArea = new JTextArea();
		descriptionTextArea.setFont(new java.awt.Font("Tahoma", 0, 11));
		descriptionTextArea.setToolTipText(DefaultMessages.TIP_LAYERDESCRIPTION);
		return descriptionTextArea;
	}
	
	private void addFacadeCheckBox() {
		JCheckBox facadeCheckBox = new JCheckBox();
		facadeCheckBox.setText("Only accessible by a facade (interface)");
		facadeCheckBox.setToolTipText(DefaultMessages.TIP_FACADE);
		this.add(facadeCheckBox, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	}
	
	private void addModuleAccessLabel() {
		JLabel moduleAccessLabel = new JLabel();
		moduleAccessLabel.setText("Access:");
		this.add(moduleAccessLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	}

	@Override
	protected JPanel addButtonPanel() {
		// TODO Auto-generated method stub
		return null;
	}
}
