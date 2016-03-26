package husacct.analyse.presentation.reconstruct;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;

import husacct.ServiceProvider;
import husacct.analyse.serviceinterface.dto.ReconstructArchitectureDTO;
import husacct.analyse.task.AnalyseTaskControl;
import husacct.common.dto.ModuleDTO;
import husacct.common.help.presentation.HelpableJPanel;
import husacct.control.task.MainController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;

import java.awt.Dimension;
import java.awt.FlowLayout;

public class ReconstructJPanel extends HelpableJPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	private ComponentInformationJPanel componentInformationJPanel;
	private ApproachesTableJPanel approachesTableJPanel;
	private AnalyseTaskControl analyseTaskControl;
	private JButton applyButton;
	private JButton reverseButton, clearButton;
	private JPanel panel;
	
	/**
	 * Create the panel.
	 */
	public ReconstructJPanel(AnalyseTaskControl atc) {
		super();
		analyseTaskControl = atc;
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
		return null;
	}
	
	public final void initUI(){
		setLayout(new GridLayout(0, 1, 0, 0));
		this.add(createComponentInformationJPanel(), BorderLayout.NORTH);
		this.add(createApproachesTableJPanel(), BorderLayout.CENTER);
		
		panel = new JPanel();
		approachesTableJPanel.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new FlowLayout());
		
		applyButton = new JButton("Apply");
		panel.add(applyButton);
		applyButton.setPreferredSize(new Dimension(100, 40));
		applyButton.addActionListener(this);

		reverseButton = new JButton("Reverse");
		panel.add(reverseButton);
		reverseButton.setPreferredSize(new Dimension(100, 40));
		reverseButton.addActionListener(this);
		
		clearButton = new JButton("Clear All");
		panel.add(clearButton);
		clearButton.setPreferredSize(new Dimension(100, 40));
		clearButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent action) {
		if (action.getSource() == applyButton) {
			JTable approachesTable = approachesTableJPanel.approachesTable;
			ButtonGroup buttonGroupOne = approachesTableJPanel.radioButtonGroup;
			ButtonGroup buttonGroupTwo = approachesTableJPanel.radioButtonGroupTwo;
			//Getting all selected rows. 
			 int rowCount = approachesTable.getRowCount();
			 for(int i=0; i<=rowCount; i++){
				 if (approachesTable.isRowSelected(i)){
					 
					 ModuleDTO selectedModule = getSelectedModule();
					 
				 
					 if(buttonGroupOne.getSelection() != null && buttonGroupTwo.getSelection() != null){
						 String selectedRadioButtonGroupOne = buttonGroupOne.getSelection().getActionCommand();
						 String selectedRadioButtonGroupTwo = buttonGroupTwo.getSelection().getActionCommand(); 
					 }
					 ReconstructArchitectureDTO dto = new ReconstructArchitectureDTO();
					 dto.setSelectedModule(selectedModule);
					 dto.setApproach((String)approachesTable.getValueAt(i, 0));
					 dto.setThreshold((int)approachesTable.getValueAt(i, 1));
					 analyseTaskControl.reconstructArchitecture_Execute(dto);
					 ServiceProvider.getInstance().getDefineService().getSarService().updateModulePanel();
				 }
			 }
		}
		if (action.getSource() == reverseButton) {
			JTable approachesTable = approachesTableJPanel.approachesTable;
			approachesTable.clearSelection();
			analyseTaskControl.reconstructArchitecture_Reverse();
			ServiceProvider.getInstance().getDefineService().getSarService().updateModulePanel();
		}
		if (action.getSource() == clearButton) {
			JTable approachesTable = approachesTableJPanel.approachesTable;
			approachesTable.clearSelection();
			analyseTaskControl.reconstructArchitecture_ClearAll();
			ServiceProvider.getInstance().getDefineService().getSarService().updateModulePanel();
		}

	}
	
	private ModuleDTO getSelectedModule(){
		return ServiceProvider.getInstance().getDefineService().getSarService().getModule_SelectedInGUI();
	}

}