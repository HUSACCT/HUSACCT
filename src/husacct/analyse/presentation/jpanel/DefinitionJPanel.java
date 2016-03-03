package husacct.analyse.presentation.jpanel;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;

import husacct.ServiceProvider;
import husacct.analyse.domain.famix.FamixQueryServiceImpl;
import husacct.analyse.task.reconstruct.ReconstructArchitecture;
import husacct.common.dto.ModuleDTO;
import husacct.common.help.presentation.HelpableJPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class DefinitionJPanel extends HelpableJPanel implements ActionListener{
	private ComponentInformationJPanel componentInformationJPanel;
	private ApproachesTableJPanel approachesTableJPanel;
	private JButton applyButton;
	private JButton reverseButton;
	private JPanel panel;
	
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
		buttonPanel.setLayout(new BorderLayout(0, 0));
		{
			{
				panel = new JPanel();
				buttonPanel.add(panel, BorderLayout.EAST);
				GridBagLayout gbl_panel = new GridBagLayout();
				gbl_panel.columnWidths = new int[]{59, 73, 0};
				gbl_panel.rowHeights = new int[]{23, 0, 0, 0};
				gbl_panel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
				gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
				panel.setLayout(gbl_panel);
				reverseButton = new JButton("Reverse");
				GridBagConstraints gbc_reverseButton = new GridBagConstraints();
				gbc_reverseButton.insets = new Insets(0, 0, 0, 5);
				gbc_reverseButton.anchor = GridBagConstraints.NORTHWEST;
				gbc_reverseButton.gridx = 0;
				gbc_reverseButton.gridy = 2;
				panel.add(reverseButton, gbc_reverseButton);
				reverseButton.setAlignmentY(Component.BOTTOM_ALIGNMENT);
				reverseButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
				{
					applyButton = new JButton("Apply");
					GridBagConstraints gbc_applyButton = new GridBagConstraints();
					gbc_applyButton.anchor = GridBagConstraints.NORTHWEST;
					gbc_applyButton.gridx = 1;
					gbc_applyButton.gridy = 2;
					panel.add(applyButton, gbc_applyButton);
					applyButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
					applyButton.setAlignmentY(Component.BOTTOM_ALIGNMENT);
					applyButton.addActionListener(this);
				}
				reverseButton.addActionListener(this);
			}

		}
			
		return buttonPanel;
	}
	
	public final void initUI(){
		setLayout(new GridLayout(0, 1, 0, 0));
		
		this.add(createComponentInformationJPanel(), BorderLayout.NORTH);
		this.add(createApproachesTableJPanel(), BorderLayout.CENTER);
		this.add(createButtons(), BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent action) {
		if (action.getSource() == applyButton) {
			JTable approachesTable = approachesTableJPanel.approachesTable;
			
			//Getting all selected rows. 
			 int rowCount = approachesTable.getRowCount();
			 for(int i=0; i<=rowCount; i++){
				 if (approachesTable.isRowSelected(i)){
					 
					 System.out.println("---------------------------------- ");
					 System.out.println("Selected Module: " + getSelectedModule().name);
					 System.out.println("Approach: " + approachesTable.getValueAt(i, 0));
					 System.out.println("Threshold: " + approachesTable.getValueAt(i, 1));
					 System.out.println("---------------------------------- ");
					 
					 ReconstructArchitecture ra = new ReconstructArchitecture(new FamixQueryServiceImpl()); //because the task team was not allowed to edit any other classes
					 ra.startReconstruction(getSelectedModule(), (int)approachesTable.getValueAt(i, 1), (String)approachesTable.getValueAt(i, 0));
					 
				 }
			 }
		}
		if (action.getSource() == reverseButton) {
			JTable approachesTable = approachesTableJPanel.approachesTable;
			approachesTable.clearSelection();
		}
	}
	
	private ModuleDTO getSelectedModule(){
		return ServiceProvider.getInstance().getDefineService().getSarService()
				.getModule_SelectedInGUI();
	}

}