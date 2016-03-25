package husacct.analyse.presentation.jpanel;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;

import husacct.ServiceProvider;
import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.domain.famix.FamixQueryServiceImpl;
import husacct.analyse.task.AnalyseTaskControl;
import husacct.analyse.task.reconstruct.ReconstructArchitecture;
import husacct.common.dto.ModuleDTO;
import husacct.common.help.presentation.HelpableJPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class ReconstructJPanel extends HelpableJPanel implements ActionListener{
	private ComponentInformationJPanel componentInformationJPanel;
	private ApproachesTableJPanel approachesTableJPanel;
	private AnalyseTaskControl analyseTaskControl;
	private JButton applyButton;
	private JButton reverseButton;
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
		
		reverseButton = new JButton("Reverse");
		panel.add(reverseButton);
		reverseButton.setPreferredSize(new Dimension(100, 40));
		
		reverseButton.addActionListener(this);
		
		applyButton = new JButton("Apply");
		panel.add(applyButton);
		applyButton.setPreferredSize(new Dimension(100, 40));
		applyButton.addActionListener(this);

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
					 
					 analyseTaskControl.reconstructArchitecture_Execute(selectedModule, (String)approachesTable.getValueAt(i, 0), (int)approachesTable.getValueAt(i, 1));
					 
				 }
			 }
		}
		if (action.getSource() == reverseButton) {
			JTable approachesTable = approachesTableJPanel.approachesTable;
			approachesTable.clearSelection();
			analyseTaskControl.reconstructArchitecture_Reverse();
		}
	}
	
	private ModuleDTO getSelectedModule(){
		return ServiceProvider.getInstance().getDefineService().getSarService()
				.getModule_SelectedInGUI();
	}

}