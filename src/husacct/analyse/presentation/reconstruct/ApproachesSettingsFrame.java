package husacct.analyse.presentation.reconstruct;


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import husacct.ServiceProvider;
import husacct.analyse.presentation.reconstruct.approaches.AllApproachesJPanel;
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
	private AllApproachesJPanel allApproachesJPanel;
	private HashMap<String, ReconstructArchitectureParameterPanel> parameterDTOPanels = new HashMap<String, ReconstructArchitectureParameterPanel>();

	
	public ApproachesSettingsFrame(AnalyseTaskControl atc, ReconstructArchitectureDTO dto, AllApproachesJPanel allApJp){
		this.dto = dto;
		this.analyseTaskControl = atc;
		this.allApproachesJPanel = allApJp;
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
			parameterDTOPanels.put(parameterDTO.parameterConstant, parameterPanel);
			
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
			setParameterValues();
			resetParameterTable();
			frame.dispose();
		}
		else if(event.getSource() == cancelButton){
			frame.dispose();
		}
		
	}

	private void resetParameterTable() {
		int selectedRow = allApproachesJPanel.allApproachesTable.getSelectedRow();
		int differentRow = selectedRow > 1 ? selectedRow -1 : selectedRow +1;
		allApproachesJPanel.allApproachesTable.setRowSelectionInterval(differentRow, differentRow);
		allApproachesJPanel.allApproachesTable.setRowSelectionInterval(selectedRow, selectedRow);
	}

	private void setParameterValues() {
		for (ReconstructArchitectureParameterDTO parameterDTO : dto.parameterDTOs){
			Object value = getParameterPanelValue(parameterDTO.parameterConstant);
			dto = ReconstructArchitectureParameterPanel.setValueOfReconstructArchitectureDTO(parameterDTO.parameterConstant, dto, value);
		}
		analyseTaskControl.getReconstructArchitectureDTOList().updateReconstructArchitectureDTO(dto);
	}
	
	private Object getParameterPanelValue(String parameterConstant){
		ReconstructArchitectureParameterPanel parameterPanel = parameterDTOPanels.get(parameterConstant);
		Object parameterPanelValue = parameterPanel.getValue();
		return parameterPanelValue;
	}
}
