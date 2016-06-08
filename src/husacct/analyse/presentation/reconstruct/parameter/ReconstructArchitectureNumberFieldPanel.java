package husacct.analyse.presentation.reconstruct.parameter;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.NumberFormatter;

import org.apache.log4j.Logger;

import husacct.analyse.task.reconstruct.parameters.ReconstructArchitectureNumberFieldDTO;
import husacct.analyse.task.reconstruct.parameters.ReconstructArchitectureParameterDTO;
import husacct.common.dto.ReconstructArchitectureDTO;

public class ReconstructArchitectureNumberFieldPanel extends ReconstructArchitectureParameterPanel{
	private final Logger logger = Logger.getLogger(ReconstructArchitectureNumberFieldPanel.class);
	private ReconstructArchitectureNumberFieldDTO numberFieldDTO;
	private JFormattedTextField numberField;
	private JLabel numberFieldLabel;
	public int fieldColumns = 10;
	
	public ReconstructArchitectureNumberFieldPanel(ReconstructArchitectureParameterDTO parameterDTO,
			ReconstructArchitectureDTO dto) {
		super(parameterDTO, dto);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JPanel createPanel() {
		JPanel numberFieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		if(parameterDTO instanceof ReconstructArchitectureNumberFieldDTO) {
			numberFieldDTO = (ReconstructArchitectureNumberFieldDTO) parameterDTO;
			numberFieldPanel.setPreferredSize(new Dimension(600, 30));
			
			String translatedLabel = getTranslation(numberFieldDTO.label_TranslationKey);
			numberFieldLabel = new JLabel(translatedLabel);
			
			NumberFormatter numberFormat = createFormatter();
			numberField = new JFormattedTextField(numberFormat);
			numberField.setColumns(fieldColumns);
			numberFieldPanel.add(numberFieldLabel);
			numberFieldPanel.add(numberField);
			
			Object objectValue = getValueFromReconstructArchitectureDTO(numberFieldDTO.parameterConstant, dto);
			try{
				int defaultIntValue = 0;
				defaultIntValue = objectValue == null ? 0 : (int) objectValue;
				numberField.setValue(defaultIntValue);
			}
			catch(Exception e2){
				logger.error("invalid cast for defaultValue ");
			}
			
		}
		
		
		
		return numberFieldPanel;
	}

	private NumberFormatter createFormatter() {
		NumberFormatter numberFormat = new NumberFormatter();
		numberFormat.setMinimum(numberFieldDTO.minValue);
		numberFormat.setMaximum(numberFieldDTO.maxValue);
		return numberFormat;
	}
	
	
	@Override
	public Object getValue() {
		if(numberField != null){
			return numberField.getValue();
		}
		logger.warn(numberFieldLabel + " panel has not been created yet");
		return 0;
	}

}
