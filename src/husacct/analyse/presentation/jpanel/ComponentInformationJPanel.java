package husacct.analyse.presentation.jpanel;

import javax.swing.JPanel;

import husacct.common.help.presentation.HelpableJPanel;
import java.awt.GridLayout;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;

public class ComponentInformationJPanel extends HelpableJPanel {

	/**
	 * Create the panel.
	 */
	public ComponentInformationJPanel() {
		super();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblModule = new JLabel("Module:");
		GridBagConstraints gbc_lblModule = new GridBagConstraints();
		gbc_lblModule.insets = new Insets(0, 0, 5, 5);
		gbc_lblModule.gridx = 2;
		gbc_lblModule.gridy = 1;
		add(lblModule, gbc_lblModule);
		
		JLabel lblModuleVal = new JLabel("module_name");
		GridBagConstraints gbc_lblModuleVal = new GridBagConstraints();
		gbc_lblModuleVal.insets = new Insets(0, 0, 5, 5);
		gbc_lblModuleVal.gridx = 3;
		gbc_lblModuleVal.gridy = 1;
		add(lblModuleVal, gbc_lblModuleVal);
		
		JButton btnStartStopSar = new JButton("Start/Stop SAR");
		GridBagConstraints gbc_btnStartStopSar = new GridBagConstraints();
		gbc_btnStartStopSar.insets = new Insets(0, 0, 5, 5);
		gbc_btnStartStopSar.gridx = 11;
		gbc_btnStartStopSar.gridy = 1;
		add(btnStartStopSar, gbc_btnStartStopSar);
		
		JLabel lblInfo = new JLabel("Info:");
		GridBagConstraints gbc_lblInfo = new GridBagConstraints();
		gbc_lblInfo.insets = new Insets(0, 0, 5, 5);
		gbc_lblInfo.gridx = 2;
		gbc_lblInfo.gridy = 2;
		add(lblInfo, gbc_lblInfo);
		
		JLabel lblInfoValue = new JLabel("info");
		GridBagConstraints gbc_lblInfoValue = new GridBagConstraints();
		gbc_lblInfoValue.insets = new Insets(0, 0, 5, 5);
		gbc_lblInfoValue.gridx = 3;
		gbc_lblInfoValue.gridy = 2;
		add(lblInfoValue, gbc_lblInfoValue);
		
		JLabel label = new JLabel("Info:");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 0, 5);
		gbc_label.gridx = 2;
		gbc_label.gridy = 3;
		add(label, gbc_label);
		
		JLabel label_1 = new JLabel("info");
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.insets = new Insets(0, 0, 0, 5);
		gbc_label_1.gridx = 3;
		gbc_label_1.gridy = 3;
		add(label_1, gbc_label_1);
		initUI();
	}

	public final void initUI(){
		
	}
}
