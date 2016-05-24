package husacct.analyse.presentation.reconstruct;


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.apache.log4j.Logger;

import husacct.ServiceProvider;
import husacct.analyse.task.AnalyseTaskControl;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants.AlgorithmParameter;
import husacct.analyse.task.reconstruct.parameters.ParameterPanel;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.help.presentation.HelpableJInternalFrame;
import husacct.common.locale.ILocaleService;

public class ApproachesSettingsFrame extends HelpableJInternalFrame implements ActionListener{
	private final Logger logger = Logger.getLogger(ApproachesSettingsFrame.class);
	private static final long serialVersionUID = 1L;
	private ReconstructArchitectureDTO dto;
	private JButton applyButton, cancelButton;
	private JFrame frame;
	private AnalyseTaskControl analyseTaskControl;

	
	public ApproachesSettingsFrame(AnalyseTaskControl atc, ReconstructArchitectureDTO dto){
		this.dto = dto;
		this.analyseTaskControl = atc;
		buildFrame();
	}
	
	public void buildFrame(){
		frame = new JFrame("Approach Settings");
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setSize(400, 400);
		frame.setVisible(true);
		frame.getContentPane().add(buildPanel(), BorderLayout.CENTER);
	}
	
	private JPanel buildPanel(){
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(this.buildApproachLabel(), BorderLayout.NORTH);
		
		JPanel parametersPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		for (ParameterPanel pPanel : dto.parameterPanels){
			parametersPanel.add(pPanel.createPanel(dto));
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
			for (ParameterPanel pPanel : dto.parameterPanels){
				if (pPanel.parameterConstant.equals(AlgorithmParameter.Threshold)){
					try{
						dto.threshold = (int) pPanel.getValue();
					}catch(Exception e){
						logger.error("threshold invalid cast: " + e);
					}
				}
				else if (pPanel.parameterConstant.equals(AlgorithmParameter.RelationType)){
					try {
						dto.relationType = (String) pPanel.getValue();
					}catch(Exception e){
						logger.error("RelationType value cast Exception: " + e);
					}
					
				}
				else if (pPanel.parameterConstant.equals(AlgorithmParameter.Granularity)){
					try{
						dto.granularity = (String) pPanel.getValue();
					}catch(Exception e){
						
					}
				}
			}
			analyseTaskControl.reconstructArchitectureListDTO.updateReconstructArchitectureDTO(dto);
			frame.dispose();
		}
		else if(event.getSource() == cancelButton){
			frame.dispose();
		}
		
	}
}
