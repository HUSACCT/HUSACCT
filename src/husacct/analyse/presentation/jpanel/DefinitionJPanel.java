package husacct.analyse.presentation.jpanel;

import javax.swing.JPanel;
import husacct.common.help.presentation.HelpableJPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class DefinitionJPanel extends HelpableJPanel {
	private ComponentInformationJPanel componentInformationJPanel;
	private ApproachesTableJPanel approachesTableJPanel;
	
	/**
	 * Create the panel.
	 */
	public DefinitionJPanel() {
		super();
		initUI();
	}
	
	public JPanel createComponentInformationJPanel(){
		componentInformationJPanel = new ComponentInformationJPanel();
		componentInformationJPanel.initUI();
		return componentInformationJPanel;
	}
	
	public JPanel createApproachesTableJPanel(){
		approachesTableJPanel = new ApproachesTableJPanel();
		approachesTableJPanel.initUI();
		return approachesTableJPanel;
	}
	
	public JPanel createButtons(){
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnApply = new JButton("Apply");
		buttonPanel.add(btnApply);
		
		JButton btnReverse = new JButton("Reverse");
		buttonPanel.add(btnReverse);
		
		return buttonPanel;
	}
	
	public final void initUI(){
		setLayout(new GridLayout(0, 1, 0, 0));
		
		this.add(createComponentInformationJPanel(), BorderLayout.NORTH);
		this.add(createApproachesTableJPanel(), BorderLayout.CENTER);
		this.add(createButtons(), BorderLayout.SOUTH);
	}

}
