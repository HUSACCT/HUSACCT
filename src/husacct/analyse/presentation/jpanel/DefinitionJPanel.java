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

public class DefinitionJPanel extends HelpableJPanel implements ActionListener{
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
			btnNewButton.addActionListener(this);
		}
		{
			btnNewButton_1 = new JButton("Reverse");
			btnNewButton_1.setAlignmentY(Component.BOTTOM_ALIGNMENT);
			btnNewButton_1.setAlignmentX(Component.RIGHT_ALIGNMENT);
			buttonPanel.add(btnNewButton_1);
			btnNewButton_1.addActionListener(this);

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
		if (action.getSource() == btnNewButton) {
			JTable approachesTable = approachesTableJPanel.table;
			
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
		if (action.getSource() == btnNewButton_1) {
			JTable approachesTable = approachesTableJPanel.table;
			approachesTable.clearSelection();
		}
	}
	
	private ModuleDTO getSelectedModule(){
		return ServiceProvider.getInstance().getDefineService().getModule_SelectedInGUI();
	}

}