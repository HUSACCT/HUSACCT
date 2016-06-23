package husacct.analyse.presentation.reconstruct;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import org.apache.log4j.Logger;

import husacct.ServiceProvider;
import husacct.analyse.presentation.reconstruct.parameter.ReconstructArchitectureParameterPanel;
import husacct.analyse.task.AnalyseTaskControl;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants;
import husacct.analyse.task.reconstruct.parameters.ReconstructArchitectureParameterDTO;
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
	private JButton applyButton, reverseButton, clearButton, settingsButton;
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
				
		settingsButton = new JButton("Settings");
		panel.add(settingsButton);
		settingsButton.setVisible(false);
		settingsButton.setPreferredSize(new Dimension(100, 40));
		settingsButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent action) {
		if (action.getSource() == applyButton) {
			try{
				JTabbedPane tappedPane = approachesTableJPanel.tabbedPane;
				Component selectedTappedPane = tappedPane.getSelectedComponent();
				JPanel selectedPanel = (JPanel) selectedTappedPane;
					
				JTable approachesTable = getApproachesTable(selectedPanel);
						
				int selectedRow = approachesTable.getSelectedRow();
				if (selectedRow >= 0 && approachesTable.getName().equals("tableAllApproaches")){
					executeFromAllApproachesTable(approachesTable, selectedRow);
				}
				else if(selectedRow >= 0 && approachesTable.getName().equals("tableDistinctApproaches")){
					executeFromDistinctApproachesTable(approachesTable, selectedRow);
				}
				else{
					logger.warn("No Approache selected");
				}
			}
			catch(Exception e){
				logger.error("Approaches Apply Exception: " + e);
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
		if (action.getSource() == settingsButton){
			JTable approachesTable = approachesTableJPanel.tableAllApproaches;
			int selectedRow = approachesTable.getSelectedRow();
			if (selectedRow >= 0){
				String approachConstant = (String) approachesTable.getModel().getValueAt(selectedRow, 0);
				ReconstructArchitectureDTO reconstructArchitectureDTO = analyseTaskControl.getReconstructArchitectureDTOList().getReconstructArchitectureDTO(approachConstant);
				if (!reconstructArchitectureDTO.parameterDTOs.isEmpty()){
					new ApproachesSettingsFrame(analyseTaskControl, reconstructArchitectureDTO, approachesTableJPanel);
				}
				else{
					JOptionPane.showMessageDialog(this, "this approach has no settings");
				}
			}
		}

	}
	
	public void setButtonVisibility(Boolean visibility){
		applyButton.setVisible(visibility);
		reverseButton.setVisible(visibility);
		clearButton.setVisible(visibility);
		settingsButton.setVisible(visibility);
	}
	
	public void setDistinctApproachesVisibility(){
		settingsButton.setVisible(false);
		applyButton.setVisible(true);
		reverseButton.setVisible(true);
		clearButton.setVisible(true);
	}
	
	
	private void executeFromAllApproachesTable(JTable approachesTable, int selectedRow){
		ReconstructArchitectureDTO reconstructArchitectureDTO = new ReconstructArchitectureDTO();
		ModuleDTO selectedModule = getSelectedModule();
		String approachConstant = (String) approachesTable.getModel().getValueAt(selectedRow, 0);
		reconstructArchitectureDTO = analyseTaskControl.getReconstructArchitectureDTOList().getReconstructArchitectureDTO(approachConstant);
		reconstructArchitectureDTO.setSelectedModule(selectedModule);
		analyseTaskControl.reconstructArchitecture_Execute(reconstructArchitectureDTO);
		ServiceProvider.getInstance().getDefineService().getSarService().updateModulePanel();
	}
	
	
	private void executeFromDistinctApproachesTable(JTable approachesTable, int selectedRow){
		ReconstructArchitectureDTO reconstructArchitectureDTO = new ReconstructArchitectureDTO();
		ModuleDTO selectedModule = getSelectedModule();
		String approachConstant = (String) approachesTable.getModel().getValueAt(selectedRow, 0);
		reconstructArchitectureDTO = createDefaultReconstructArchitectureDTO(approachConstant);
		reconstructArchitectureDTO.setSelectedModule(selectedModule);
		reconstructArchitectureDTO.approachConstant = approachConstant;
		analyseTaskControl.reconstructArchitecture_Execute(reconstructArchitectureDTO);
		ServiceProvider.getInstance().getDefineService().getSarService().updateModulePanel();
	}
	
	
	
	
	
	private JTable getApproachesTable(JPanel selectedPanel){
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
		return approachesTable;
	}
	
	private ModuleDTO getSelectedModule(){
		return ServiceProvider.getInstance().getDefineService().getSarService().getModule_SelectedInGUI();
	}

	private ReconstructArchitectureDTO createDefaultReconstructArchitectureDTO(String approachConstant){
		ReconstructArchitectureDTO customDTO = analyseTaskControl.getReconstructArchitectureDTOList().getReconstructArchitectureDTO(approachConstant);
		ReconstructArchitectureDTO defaultDTO = new ReconstructArchitectureDTO();
		for (ReconstructArchitectureParameterDTO parameterDTO : customDTO.parameterDTOs){
			ReconstructArchitectureParameterPanel.setValueOfReconstructArchitectureDTO(parameterDTO.parameterConstant, defaultDTO, parameterDTO.defaultValue);
		}
		return defaultDTO;
		
	}
	
}