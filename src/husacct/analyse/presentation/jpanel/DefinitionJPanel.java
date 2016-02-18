package husacct.analyse.presentation.jpanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import husacct.common.help.presentation.HelpableJPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.BoxLayout;
import java.awt.Component;

public class DefinitionJPanel extends HelpableJPanel {
	private ComponentInformationJPanel componentInformationJPanel;
	private ApproachesTableJPanel approachesTableJPanel;
	private JButton btnNewButton;
	private JButton btnNewButton_1;
	
	/**
	 * Create the panel.
	 */
	public DefinitionJPanel() {
		super();
		initUI();
	}
	
	public JPanel createComponentInformationJPanel(){
		componentInformationJPanel = new ComponentInformationJPanel();
		return componentInformationJPanel;
	}
	
	public JPanel createApproachesTableJPanel(){
		approachesTableJPanel = new ApproachesTableJPanel();
		return approachesTableJPanel;
	}
	
	public JPanel createButtons(){
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		{
			btnNewButton = new JButton("Apply");
			btnNewButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
			btnNewButton.setAlignmentY(Component.BOTTOM_ALIGNMENT);
			buttonPanel.add(btnNewButton);
		}
		{
			btnNewButton_1 = new JButton("Reverse");
			btnNewButton_1.setAlignmentY(Component.BOTTOM_ALIGNMENT);
			btnNewButton_1.setAlignmentX(Component.RIGHT_ALIGNMENT);
			buttonPanel.add(btnNewButton_1);
		}
		
		return buttonPanel;
	}
	
	public final void initUI(){
		setLayout(new GridLayout(0, 1, 0, 0));
		
		this.add(createComponentInformationJPanel(), BorderLayout.NORTH);
		this.add(createApproachesTableJPanel(), BorderLayout.CENTER);
		this.add(createButtons(), BorderLayout.SOUTH);
	}

}