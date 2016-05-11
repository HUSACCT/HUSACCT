package husacct.analyse.presentation.reconstruct;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.apache.log4j.Logger;

import husacct.ServiceProvider;
import husacct.analyse.task.AnalyseTaskControl;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants.AlgorithmSettings;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants.Granularities;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants.RelationTypes;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.help.presentation.HelpableJInternalFrame;
import husacct.common.locale.ILocaleService;

public class ApproachesSettingsFrame extends HelpableJInternalFrame implements ActionListener{
	private final Logger logger = Logger.getLogger(ApproachesSettingsFrame.class);
	private static final long serialVersionUID = 1L;
	private ReconstructArchitectureDTO dto;
	private JButton applyButton, cancelButton;
	private JFrame frame;
	private JTextField thresholdField;
	private AnalyseTaskControl analyseTaskControl;
	private ButtonGroup relationTypeGroup;
	private ButtonGroup granularityGroup;

	
	public ApproachesSettingsFrame(AnalyseTaskControl atc, ReconstructArchitectureDTO dto){
		this.dto = dto;
		this.analyseTaskControl = atc;
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
		
		JPanel settingsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		for (String s : dto.thresholdSettings){
			if (s.equals(AlgorithmSettings.Threshold)){
				settingsPanel.add(this.buildThresHoldPanel());
			}
			else if (s.equals(AlgorithmSettings.RelationType)){
				settingsPanel.add(this.buildRelationTypePanel());
			}
			else if (s.equals(AlgorithmSettings.Granularity)){
				settingsPanel.add(this.buildGranularityPanel());
			}
		}
		mainPanel.add(settingsPanel, BorderLayout.CENTER);
		
		mainPanel.add(this.buildButtonPanel(), BorderLayout.SOUTH);
		
		return mainPanel;
	}

	private JPanel buildApproachLabel(){
		JPanel approachLabelPanel = new JPanel();
		JLabel approachLabelLabel = new JLabel(getTranslation("Approach") + ": " + dto.approachConstant);
		approachLabelPanel.add(approachLabelLabel);
		return approachLabelPanel;
	}
	
	private JPanel buildThresHoldPanel(){
		JPanel thresholdPanel = new JPanel();
		JLabel thresholdLabel = new JLabel(getTranslation("Threshold"));
		thresholdField = new JTextField();
		thresholdField.setText(dto.threshold + "");
		thresholdField.setColumns(10);
		thresholdPanel.add(thresholdLabel);
		thresholdPanel.add(thresholdField);
		return thresholdPanel;
	}
	
	private JPanel buildRelationTypePanel(){
		JPanel relationTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel relationTypeLabel = new JLabel(getTranslation("RelationType"));
		relationTypePanel.add(relationTypeLabel);
		
		JPanel radioButtonPanel = new JPanel(new GridLayout(0,1));
		relationTypeGroup = new ButtonGroup();
		
		JRadioButton button1 = new JRadioButton(getTranslation(RelationTypes.allDependencies));
		button1.setActionCommand(RelationTypes.allDependencies);
		relationTypeGroup.add(button1);
		radioButtonPanel.add(button1);
		button1.setSelected(dto.relationType.equals(RelationTypes.allDependencies));

		JRadioButton button2 = new JRadioButton(getTranslation(RelationTypes.umlLinks));
		button2.setActionCommand(RelationTypes.umlLinks);
		relationTypeGroup.add(button2);
		radioButtonPanel.add(button2);
		button2.setSelected(dto.relationType.equals(RelationTypes.umlLinks));
		
		JRadioButton button3 = new JRadioButton(getTranslation(RelationTypes.accessCallReferenceDependencies));
		button3.setActionCommand(RelationTypes.accessCallReferenceDependencies);
		relationTypeGroup.add(button3);
		radioButtonPanel.add(button3);
		button3.setSelected(dto.relationType.equals(RelationTypes.accessCallReferenceDependencies));
		
		relationTypePanel.add(radioButtonPanel);
		relationTypePanel.setPreferredSize(new Dimension(580, 70));
		
		return relationTypePanel;
	}
	
	private JPanel buildGranularityPanel(){
		JPanel granularityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel granularityLabel = new JLabel("Granularity");
		granularityPanel.add(granularityLabel);
		
				
		JPanel radioButtonPanel = new JPanel(new GridLayout(0,1));
		granularityGroup = new ButtonGroup();
		
		JRadioButton button1 = new JRadioButton(getTranslation(Granularities.PackagesInRootOnlyClasses));
		button1.setActionCommand(Granularities.PackagesInRootOnlyClasses);
		granularityGroup.add(button1);
		radioButtonPanel.add(button1);
		button1.setSelected(dto.granularity.equals(Granularities.PackagesInRootOnlyClasses));

		JRadioButton button2 = new JRadioButton(getTranslation(Granularities.PackagesWithAllClasses));
		button2.setActionCommand(Granularities.PackagesWithAllClasses);
		granularityGroup.add(button2);
		radioButtonPanel.add(button2);
		button2.setSelected(dto.granularity.equals(Granularities.PackagesWithAllClasses));
		
		JRadioButton button3 = new JRadioButton(getTranslation(Granularities.Classes));
		button3.setActionCommand(Granularities.Classes);
		granularityGroup.add(button3);
		radioButtonPanel.add(button3);
		button3.setSelected(dto.granularity.equals(Granularities.Classes));
		
		granularityPanel.add(radioButtonPanel);
		granularityPanel.setPreferredSize(new Dimension(580, 70));
		
		return granularityPanel;
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
			for (String s : dto.thresholdSettings){
				if (s.equals(AlgorithmSettings.Threshold)){
					try{
						dto.threshold = Integer.parseInt(thresholdField.getText());
					}catch(Exception e){
						logger.error("threshold is no int: " + e);
					}
				}
				else if (s.equals(AlgorithmSettings.RelationType)){
					dto.relationType = relationTypeGroup.getSelection().getActionCommand();
				}
				else if (s.equals(AlgorithmSettings.Granularity)){
					dto.granularity = granularityGroup.getSelection().getActionCommand();
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
