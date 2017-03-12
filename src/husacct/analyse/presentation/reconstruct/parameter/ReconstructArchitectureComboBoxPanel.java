package husacct.analyse.presentation.reconstruct.parameter;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import husacct.analyse.task.reconstruct.dto.ReconstructArchitectureDTO;
import husacct.analyse.task.reconstruct.parameters.ReconstructArchitectureParameterDTO;
import husacct.analyse.task.reconstruct.parameters.ReconstructArchitectureComboBoxDTO;
import husacct.analyse.task.reconstruct.parameters.ReconstructArchitectureComboBoxDTO.ComboBoxValue;

public class ReconstructArchitectureComboBoxPanel extends ReconstructArchitectureParameterPanel{
	private final Logger logger = Logger.getLogger(ReconstructArchitectureComboBoxPanel.class);
	private JComboBox<ComboBoxValue> comboBox;
	private ReconstructArchitectureComboBoxDTO comboBoxDTO;
	
	public ReconstructArchitectureComboBoxPanel(ReconstructArchitectureParameterDTO parameterDTO, ReconstructArchitectureDTO dto) {
		super(parameterDTO, dto);
	}

	@Override
	public JPanel createPanel() {
		JPanel comboBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		if (parameterDTO instanceof ReconstructArchitectureComboBoxDTO){
			comboBoxDTO = (ReconstructArchitectureComboBoxDTO) parameterDTO;
			comboBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			comboBoxPanel.setPreferredSize(new Dimension(400, 30));
			
			String labelTranslation = getTranslation(parameterDTO.label_TranslationKey);
			JLabel jLabel = new JLabel(labelTranslation);
			jLabel.setPreferredSize(new Dimension(120, 30));
			comboBoxPanel.add(jLabel);
			
			ComboBoxValue[] comboBoxValues = comboBoxDTO.comboBoxValues;
			comboBox = new JComboBox<>(comboBoxValues);
			comboBoxPanel.add(comboBox);
			
			Object objectValue = getValueFromReconstructArchitectureDTO(comboBoxDTO.parameterConstant, dto);
					
			if(objectValue != null){
				try{
					String stringValue = (String) objectValue;
					comboBox.setSelectedItem(comboBoxDTO.getComboboxValue(stringValue));
				}
				catch(Exception e){
					logger.error("Invalid cast for comboBoxValue: " + e);
				}
			}else{logger.error("ComboBox Object value is null");};
		}
		return comboBoxPanel;
	}

	
	@Override
	public Object getValue() {
		try{
			ComboBoxValue cbValue = (ComboBoxValue) comboBox.getSelectedItem();
			return cbValue.getValue();
		}
		catch(Exception e){
			logger.error("Invalid comboBoxValue cast");
		}
		return null;
	}
}
