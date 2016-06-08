package husacct.analyse.presentation.reconstruct;


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import husacct.ServiceProvider;
import husacct.analyse.presentation.reconstruct.parameter.ReconstructArchitectureParameterPanel;
import husacct.analyse.task.AnalyseTaskControl;
import husacct.analyse.task.reconstruct.parameters.ReconstructArchitectureParameterDTO;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.help.presentation.HelpableJInternalFrame;
import husacct.common.locale.ILocaleService;

public class ApproachesSettingsFrame extends HelpableJInternalFrame implements ActionListener{
	//private final Logger logger = Logger.getLogger(ApproachesSettingsFrame.class);
	private static final long serialVersionUID = 1L;
	private ReconstructArchitectureDTO dto;
	private JButton applyButton, cancelButton;
	private JFrame frame;
	private AnalyseTaskControl analyseTaskControl;
	private ApproachesTableJPanel approachesTableJPanel;

	
	public ApproachesSettingsFrame(AnalyseTaskControl atc, ReconstructArchitectureDTO dto, ApproachesTableJPanel apprTJP){
		this.dto = dto;
		this.analyseTaskControl = atc;
		this.approachesTableJPanel = apprTJP;
		buildFrame();
	}
	
	public void buildFrame(){
		frame = new JFrame("Approach Settings");
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setSize(600, 400);
		frame.setVisible(true);
		frame.getContentPane().add(buildPanel(), BorderLayout.CENTER);
	}
	
	private JPanel buildPanel(){
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(this.buildApproachLabel(), BorderLayout.NORTH);
		
		JPanel parametersPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		for (ReconstructArchitectureParameterDTO parameterDTO : dto.parameterDTOs){
			ReconstructArchitectureParameterPanel parameterPanel = ReconstructArchitectureParameterPanel.getParameterPanel(parameterDTO, dto);
			parameterDTO.parameterPanel = parameterPanel;
			
			JPanel panel = parameterPanel.createPanel();
			parametersPanel.add(panel);
			
		}
		mainPanel.add(parametersPanel, BorderLayout.CENTER);
		mainPanel.add(this.buildButtonPanel(), BorderLayout.SOUTH);
		return mainPanel;
	}

	private JPanel buildApproachLabel(){
		JPanel approachLabelPanel = new JPanel();
		JLabel approachLabelLabel = new JLabel(getTranslation("Approach") + ": " + dto.approachConstant);
		approachLabelPanel.add(approachLabelLabel);
		return approachLabelPanel;
	}
	
	
	private JPanel buildButtonPanel(){
		JPanel buttonPanel = new JPanel(new GridLayout(1,1,0,5));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 5, 50));
		applyButton = new JButton("Apply");
		buttonPanel.add(applyButton);
		applyButton.addActionListener(this);
		
		cancelButton = new JButton("Cancel");
		buttonPanel.add(cancelButton);
		cancelButton.addActionListener(this);
		
		return buttonPanel;
	}
	
	private String getTranslation(String translationKey){
		ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
		return localeService.getTranslatedString(translationKey);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == applyButton) {
			for (ReconstructArchitectureParameterDTO parameterDTO : dto.parameterDTOs){
				Object value = parameterDTO.parameterPanel.getValue();
				dto = ReconstructArchitectureParameterPanel.setValueOfReconstructArchitectureDTO(parameterDTO.parameterConstant, dto, value);
			}
			analyseTaskControl.getReconstructArchitectureDTOList().updateReconstructArchitectureDTO(dto);
			int selectedRow = approachesTableJPanel.tableAllApproaches.getSelectedRow();
			int differentRow = selectedRow > 1 ? selectedRow -1 : selectedRow +1;
			approachesTableJPanel.tableAllApproaches.setRowSelectionInterval(differentRow, differentRow);
			approachesTableJPanel.tableAllApproaches.setRowSelectionInterval(selectedRow, selectedRow);
			frame.dispose();
		}
		else if(event.getSource() == cancelButton){
			frame.dispose();
		}
		
	}
}
