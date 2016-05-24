package husacct.analyse.task.reconstruct.parameters;


import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.NumberFormatter;

import org.apache.log4j.Logger;

import husacct.common.dto.ReconstructArchitectureDTO;


public class NumberFieldPanel extends ParameterPanel{
	private final Logger logger = Logger.getLogger(NumberFieldPanel.class);
	private int fieldColumns;
	private JFormattedTextField numberField;
	
	public NumberFieldPanel (String label, String parameterConstant, int fieldColumns){
		super(label, parameterConstant);
		this.fieldColumns = fieldColumns;
	}
	
	
	@Override
	public JPanel createPanel(ReconstructArchitectureDTO dto) {
		JPanel numberFieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		numberFieldPanel.setPreferredSize(new Dimension(300, 30));
		String translatedLabel = getTranslation(label);
		JLabel numberFieldLabel = new JLabel(translatedLabel);
		
		NumberFormatter numberFormat = createFormatter();
		numberField = new JFormattedTextField(numberFormat);
		numberField.setColumns(fieldColumns);
		numberFieldPanel.add(numberFieldLabel);
		numberFieldPanel.add(numberField);
		
		Object objectValue = getValueFromReconstrauctArchitectureDTO(dto);
		try{
			int defaultIntValue = 0;
			defaultIntValue = objectValue == null ? 0 : (int) objectValue;
			numberField.setValue(defaultIntValue);
		}
		catch(Exception e2){logger.error("invalid cast for defaultValue of: " + label + " - " + e2);}
		
		return numberFieldPanel;
	}
		
	private NumberFormatter createFormatter(){
		NumberFormatter numberFormat = new NumberFormatter();
		
		if (minimumValue != null){
			try{
				numberFormat.setMinimum((int) minimumValue);
			}catch(Exception e){ logger.error("invalid cast for minimumvalue of : " + label);}
		}
		if (maximumValue != null){
			try{
				numberFormat.setMaximum((int) maximumValue);
			}catch(Exception e){ logger.error("invalid cast for maximumValue of : " + label);}
		}
		
		return numberFormat;
	}


	@Override
	public Object getValue() {
		if(numberField != null){
			return numberField.getValue();
		}
		logger.warn(label + " panel has not been created yet");
		return 0;
	}
}
