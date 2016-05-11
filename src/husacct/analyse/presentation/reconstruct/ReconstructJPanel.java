package husacct.analyse.presentation.reconstruct;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import org.apache.log4j.Logger;

import husacct.ServiceProvider;
import husacct.analyse.task.AnalyseTaskControl;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.help.presentation.HelpableJPanel;
import husacct.common.locale.ILocaleService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import java.awt.Dimension;
import java.awt.FlowLayout;

public class ReconstructJPanel extends HelpableJPanel implements ActionListener{
	private final Logger logger = Logger.getLogger(ReconstructJPanel.class);
	private static final long serialVersionUID = 1L;
	private ApproachesTableJPanel approachesTableJPanel;
	private AnalyseTaskControl analyseTaskControl;
	private JButton applyButton, reverseButton, clearButton, testButton, settingsButton;
	private JPanel panel;
	private ILocaleService localService;
	
	/**
	 * Create the panel.
	 */
	public ReconstructJPanel(AnalyseTaskControl atc) {
		super();
		analyseTaskControl = atc;
		localService = ServiceProvider.getInstance().getLocaleService();
		initUI();
	}
	

	public JPanel createApproachesTableJPanel(){
		try {
			approachesTableJPanel = new ApproachesTableJPanel(analyseTaskControl,this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return approachesTableJPanel;
	}
	
	public JPanel createButtons(){
		return null;
	}
	
	public final void initUI(){
		setLayout(new GridLayout(0, 1, 0, 0));
		this.add(createApproachesTableJPanel(), BorderLayout.CENTER);
		
		panel = new JPanel();
		approachesTableJPanel.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new FlowLayout());
		
		String applyTranslation = localService.getTranslatedString("Apply");
		applyButton = new JButton(applyTranslation);
		panel.add(applyButton);
		applyButton.setPreferredSize(new Dimension(100, 40));
		applyButton.addActionListener(this);

		String reverseTranslation = localService.getTranslatedString("Reverse");
		reverseButton = new JButton(reverseTranslation);
		panel.add(reverseButton);
		reverseButton.setPreferredSize(new Dimension(reverseButton.getPreferredSize().width, 40));
		reverseButton.addActionListener(this);
		
		String clearAllTranslation = localService.getTranslatedString("ClearAll");
		clearButton = new JButton(clearAllTranslation);
		panel.add(clearButton);
		clearButton.setPreferredSize(new Dimension(100, 40));
		clearButton.addActionListener(this);
		
		testButton = new JButton("Test");
		panel.add(testButton);
		testButton.setPreferredSize(new Dimension(100, 40));
		testButton.addActionListener(this);
		
		settingsButton = new JButton("Settings");
		panel.add(settingsButton);
		settingsButton.setPreferredSize(new Dimension(100, 40));
		settingsButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent action) {
		if (action.getSource() == applyButton) {
			JTabbedPane tappedPane = approachesTableJPanel.tabbedPane;
			//ButtonGroup radioButtonsRelationType = approachesTableJPanel.RadioButtonsRelationType;
			//ButtonGroup buttonGroupTwo = approachesTableJPanel.radioButtonGroupTwo;

			Component selectedTappedPane = tappedPane.getSelectedComponent();
			JPanel selectedPanel = null;
			try{
				selectedPanel = (JPanel) selectedTappedPane;
			}catch(Exception e){
				logger.error("unable to cast JPanel: " + e);
			}
				
			JTable approachesTable = approachesTableJPanel.tableAllApproaches;
			if (selectedPanel != null){
				if(selectedPanel.getName().equals(AnalyseReconstructConstants.ApproachesTable.PanelAllApproaches)){
					approachesTable = approachesTableJPanel.tableAllApproaches;
				}
				else if (selectedPanel.getName().equals(AnalyseReconstructConstants.ApproachesTable.PanelDistinctApproaches)){
					approachesTable = approachesTableJPanel.tableDistinctApproaches;
				}
			}
			else{
				logger.error("SelectedPanel is null");
			}
			
			
			int selectedRow = approachesTable.getSelectedRow();
			if (selectedRow >= 0){
				/*String approach = (String) approachesTable.getModel().getValueAt(selectedRow, 0);
				//int threshold = Integer.parseInt(approachesTable.getValueAt(selectedRow, approachesThresholdCollumn).toString());

				ReconstructArchitectureDTO dto = new ReconstructArchitectureDTO();
				ModuleDTO selectedModule = getSelectedModule();
				dto.setSelectedModule(selectedModule);
				dto.setApproach(approach);
				//dto.setThreshold(threshold);	*/
				
				
				ReconstructArchitectureDTO dto = new ReconstructArchitectureDTO();
				ModuleDTO selectedModule = getSelectedModule();
				String approachConstant = (String) approachesTable.getModel().getValueAt(selectedRow, 0);
				dto = analyseTaskControl.reconstructArchitectureListDTO.getReconstructArchitectureDTO(approachConstant);
				dto.setSelectedModule(selectedModule);
				analyseTaskControl.reconstructArchitecture_Execute(dto);
				ServiceProvider.getInstance().getDefineService().getSarService().updateModulePanel();
			}
			else{
				logger.warn("No Approache selected");
			}

		}
		if (action.getSource() == reverseButton) {
			JTable allApproachesTable = approachesTableJPanel.tableAllApproaches;
			allApproachesTable.clearSelection();
			analyseTaskControl.reconstructArchitecture_Reverse();
			ServiceProvider.getInstance().getDefineService().getSarService().updateModulePanel();
		}
		if (action.getSource() == clearButton) {
			JTable allApproachesTable = approachesTableJPanel.tableAllApproaches;
			allApproachesTable.clearSelection();
			analyseTaskControl.reconstructArchitecture_ClearAll();
			ServiceProvider.getInstance().getDefineService().getSarService().updateModulePanel();
		}
		if (action.getSource() == testButton){
			/*JTable approachesTable = approachesTableJPanel.tableAllApproaches;
			ButtonGroup radioButtonsRelationType = approachesTableJPanel.RadioButtonsRelationType;
			ButtonGroup buttonGroupTwo = approachesTableJPanel.radioButtonGroupTwo;
			buttonGroupTwo.getClass();// to remove the "is unused" warning

			int selectedRow = approachesTable.getSelectedRow();
			if (selectedRow >= 0){
				String approach = (String) approachesTable.getModel().getValueAt(selectedRow, 0);
				int threshold = Integer.parseInt(approachesTable.getValueAt(selectedRow, approachesThresholdCollumn).toString());
				String relationType = (radioButtonsRelationType.getSelection() != null)	? radioButtonsRelationType.getSelection().getActionCommand() : "";

				ReconstructArchitectureDTO dto = new ReconstructArchitectureDTO();
				ModuleDTO selectedModule = getSelectedModule();
				dto.setSelectedModule(selectedModule);
				dto.setApproach(approach);
				dto.setThreshold(threshold);
				dto.setRelationType(relationType);		
				
				try {
					analyseTaskControl.testAlgorithm(dto);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ServiceProvider.getInstance().getDefineService().getSarService().updateModulePanel();
			}
			else{
				logger.warn("No Approache selected");
			}*/
		}
		if (action.getSource() == settingsButton){
			JTable approachesTable = approachesTableJPanel.tableAllApproaches;
			int selectedRow = approachesTable.getSelectedRow();
			if (selectedRow >= 0){
				String approachConstant = (String) approachesTable.getModel().getValueAt(selectedRow, 0);
				ReconstructArchitectureDTO reconstructArchitectureDTO = analyseTaskControl.reconstructArchitectureListDTO.getReconstructArchitectureDTO(approachConstant);
				new ApproachesSettingsFrame(analyseTaskControl, reconstructArchitectureDTO);
			}
		}

	}
	
	public void setButtonVisibility(Boolean visibility){
		applyButton.setVisible(visibility);
		reverseButton.setVisible(visibility);
		clearButton.setVisible(visibility);
		testButton.setVisible(visibility);
		settingsButton.setVisible(visibility);
		
	}
	
	private ModuleDTO getSelectedModule(){
		return ServiceProvider.getInstance().getDefineService().getSarService().getModule_SelectedInGUI();
	}

}