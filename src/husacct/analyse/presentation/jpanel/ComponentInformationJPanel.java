package husacct.analyse.presentation.jpanel;

import javax.swing.JPanel;

import husacct.ServiceProvider;
import husacct.common.dto.ModuleDTO;
import husacct.common.help.presentation.HelpableJPanel;
import java.awt.GridLayout;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import java.awt.FlowLayout;

public class ComponentInformationJPanel extends HelpableJPanel implements ActionListener{
	private JButton btnNewButton;
	private ModuleDTO selectedModule;
	private JLabel lblNewLabel_2, lblNewLabel_3, moduleTypeValue;
	
	/**
	 * Create the panel.
	 */
	public ComponentInformationJPanel() {
		super();
		setLayout(new BorderLayout(0, 0));
		
		JPanel componentInformationPanel = new JPanel();
		add(componentInformationPanel, BorderLayout.NORTH);
		componentInformationPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel informationPanel = new JPanel();
		componentInformationPanel.add(informationPanel, BorderLayout.WEST);
		GridBagLayout gbl_informationPanel = new GridBagLayout();
		gbl_informationPanel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_informationPanel.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_informationPanel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_informationPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		informationPanel.setLayout(gbl_informationPanel);
		
		JLabel lblNewLabel = new JLabel("Module name:");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		informationPanel.add(lblNewLabel, gbc_lblNewLabel);
		
		lblNewLabel_2 = new JLabel("");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_2.gridx = 2;
		gbc_lblNewLabel_2.gridy = 0;
		informationPanel.add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		
		
		JLabel lblNewLabel_1 = new JLabel("Logical Path:");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;
		informationPanel.add(lblNewLabel_1, gbc_lblNewLabel_1);
				
		lblNewLabel_3 = new JLabel("");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.gridx = 2;
		gbc_lblNewLabel_3.gridy = 1;
		informationPanel.add(lblNewLabel_3, gbc_lblNewLabel_3);
		
		JLabel moduleTypeLabel = new JLabel("Type:");
		GridBagConstraints gbc_moduleTypeLabel = new GridBagConstraints();
		gbc_moduleTypeLabel.insets = new Insets(0, 0, 5, 0);
		gbc_moduleTypeLabel.gridx = 0;
		gbc_moduleTypeLabel.gridy = 2;
		informationPanel.add(moduleTypeLabel, gbc_moduleTypeLabel);
				
		moduleTypeValue = new JLabel("");
		GridBagConstraints gbc_moduleTypeValue = new GridBagConstraints();
		gbc_moduleTypeValue.gridx = 2;
		gbc_moduleTypeValue.gridy = 2;
		informationPanel.add(moduleTypeValue, gbc_moduleTypeValue);
		
		
		
		
		JPanel startStopSarButtonPanel = new JPanel();
		componentInformationPanel.add(startStopSarButtonPanel, BorderLayout.EAST);
		startStopSarButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		btnNewButton = new JButton("Start/Stop SAR");
		startStopSarButtonPanel.add(btnNewButton);
		initUI();
		btnNewButton.addActionListener(this);
	}

	public final void initUI(){
		
	}
	
	@Override
	public void actionPerformed(ActionEvent action) {
		if (action.getSource() == btnNewButton) {
			selectedModule = getSelectedModule();
			
			//Set Values of selected Module
			lblNewLabel_2.setText(selectedModule.name);
			lblNewLabel_3.setText(selectedModule.logicalPath);
			moduleTypeValue.setText(selectedModule.type);
			
		}
	}
	
	private ModuleDTO getSelectedModule(){
		return ServiceProvider.getInstance().getDefineService().getSarService()
				.getModule_SelectedInGUI();
	}

}