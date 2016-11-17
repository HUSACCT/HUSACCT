package husacct.analyse.presentation.reconstruct;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import husacct.ServiceProvider;
import husacct.analyse.presentation.reconstruct.approaches.AllApproachesJPanel;
import husacct.analyse.presentation.reconstruct.parameter.ReconstructArchitectureParameterPanel;
import husacct.analyse.task.AnalyseTaskControl;
import husacct.analyse.task.reconstruct.parameters.ReconstructArchitectureParameterDTO;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.help.presentation.HelpableJDialog;
import husacct.common.locale.ILocaleService;
import husacct.control.ControlServiceImpl;

public class EditApproachFrame extends HelpableJDialog implements ActionListener{
	//private final Logger logger = Logger.getLogger(ApproachesSettingsFrame.class);
	private static final long serialVersionUID = 1L;
	private final ILocaleService localService = ServiceProvider.getInstance().getLocaleService();
	private ReconstructArchitectureDTO dto;
	private JPanel mainPanel;
	private JButton applyButton, cancelButton;
	//private JFrame frame;
	private AnalyseTaskControl analyseTaskControl;
	private AllApproachesJPanel allApproachesJPanel;
	private HashMap<String, ReconstructArchitectureParameterPanel> parameterDTOPanels = new HashMap<String, ReconstructArchitectureParameterPanel>();

	
	public EditApproachFrame(AnalyseTaskControl atc, ReconstructArchitectureDTO dto, AllApproachesJPanel allApJp){
		super(((ControlServiceImpl) ServiceProvider.getInstance().getControlService()).getMainController().getMainGui(), true);
		this.dto = dto;
		this.analyseTaskControl = atc;
		this.allApproachesJPanel = allApJp;
		initUI();
	}
	
	public void initUI(){
		String editApproach = getTranslation("EditApproach");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(editApproach);
    	buildMainPanel();
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		// Determine size and location
		Dimension minimumSize = new Dimension(560, 280);
    	Container c = getParent();
    	if (c != null) {
	    	int paneWidth = getParent().getWidth();
	    	setBounds(paneWidth - minimumSize.width -2, 0, minimumSize.width, minimumSize.height);
    	} else {
        	setBounds(0, 0, minimumSize.width, minimumSize.height);
    	}
    	// Show dialogue
    	setVisible(true);
		pack();
	}
	
	private void buildMainPanel(){
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(this.buildApproachLabel(), BorderLayout.NORTH);
		
		JPanel parameterListPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		for (ReconstructArchitectureParameterDTO parameterDTO : dto.parameterDTOs){
			ReconstructArchitectureParameterPanel parameterPanel = ReconstructArchitectureParameterPanel.getParameterPanel(parameterDTO, dto);
			parameterDTOPanels.put(parameterDTO.parameterConstant, parameterPanel);
			
			JPanel panel = parameterPanel.createPanel();
			parameterListPanel.add(panel);
		}
		mainPanel.add(parameterListPanel, BorderLayout.CENTER);

		mainPanel.add(buildButtonPanel(), BorderLayout.SOUTH);
	}

	private JPanel buildApproachLabel(){
		JPanel approachLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel approachLabelLabel = new JLabel(getTranslation("Approach") + ": " + dto.approachConstant);
		approachLabelPanel.add(approachLabelLabel);
		return approachLabelPanel;
	}
	
	
	private JPanel buildButtonPanel(){
		JPanel buttonPanel = new JPanel(new FlowLayout());
		//JPanel buttonPanel = new JPanel(new GridLayout(1,1,0,5));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 5, 50));

		String applyTranslation = localService.getTranslatedString("Apply");
		applyButton = new JButton(applyTranslation);
		buttonPanel.add(applyButton);
		applyButton.setPreferredSize(new Dimension(140, 40));
		applyButton.addActionListener(this);
		
		String cancel = localService.getTranslatedString("CancelButton");
		cancelButton = new JButton(cancel);
		buttonPanel.add(cancelButton);
		cancelButton.setPreferredSize(new Dimension(140, 40));
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
			dispose();
		}
		else if(event.getSource() == cancelButton){
			dispose();
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
