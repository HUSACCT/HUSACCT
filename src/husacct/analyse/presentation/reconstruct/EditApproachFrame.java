package husacct.analyse.presentation.reconstruct;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import husacct.ServiceProvider;
import husacct.analyse.presentation.reconstruct.approaches.ApproachesJPanel;
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
	private AnalyseTaskControl analyseTaskControl;
	private ApproachesJPanel allApproachesJPanel;
	private HashMap<String, ReconstructArchitectureParameterPanel> parameterDTOPanels = new HashMap<String, ReconstructArchitectureParameterPanel>();

	
	public EditApproachFrame(AnalyseTaskControl atc, ReconstructArchitectureDTO dto, ApproachesJPanel allApJp){
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
	    	setBounds(paneWidth - minimumSize.width -10, 45, minimumSize.width, minimumSize.height);
    	} else {
        	setBounds(0, 0, minimumSize.width, minimumSize.height);
    	}
    	// Show dialogue
    	setVisible(true);
		pack();
	}
	
	private void buildMainPanel(){
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(buildApproachPanel(), BorderLayout.NORTH);
		mainPanel.add(buildParameterPanel(), BorderLayout.CENTER);
		mainPanel.add(buildButtonPanel(), BorderLayout.SOUTH);
	}

	private JPanel buildApproachPanel(){
		JPanel approachLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		approachLabelPanel.setBorder(new TitledBorder(localService.getTranslatedString("Approach")));
		JLabel approachLabelLabel = new JLabel(getTranslation(dto.approachConstant));
		approachLabelPanel.add(approachLabelLabel);
		return approachLabelPanel;
	}
	
	private JScrollPane buildParameterPanel() {
		JPanel parameterListPanel = new JPanel();
		parameterListPanel.setLayout(new BoxLayout(parameterListPanel, BoxLayout.Y_AXIS));
		for (ReconstructArchitectureParameterDTO parameterDTO : dto.parameterDTOs){
			ReconstructArchitectureParameterPanel parameterPanel = ReconstructArchitectureParameterPanel.getParameterPanel(parameterDTO, dto);
			parameterDTOPanels.put(parameterDTO.parameterConstant, parameterPanel);
			JPanel panel = parameterPanel.createPanel();
			parameterListPanel.add(panel);
		}
		JScrollPane parameterListScrollPane = new JScrollPane(parameterListPanel);
		parameterListScrollPane.setBorder(new TitledBorder(localService.getTranslatedString("Parameters")));
		return parameterListScrollPane;
	}

	private JPanel buildButtonPanel(){
		JPanel buttonPanel = new JPanel(new FlowLayout());
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
		int selectedRow = allApproachesJPanel.approachesTable.getSelectedRow();
		int differentRow = selectedRow > 1 ? selectedRow -1 : selectedRow +1;
		allApproachesJPanel.approachesTable.setRowSelectionInterval(differentRow, differentRow);
		allApproachesJPanel.approachesTable.setRowSelectionInterval(selectedRow, selectedRow);
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
