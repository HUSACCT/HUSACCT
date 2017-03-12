package husacct.analyse.presentation.reconstruct.parameter;

import javax.swing.JPanel;

import husacct.ServiceProvider;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants;
import husacct.analyse.task.reconstruct.dto.ReconstructArchitectureDTO;
import husacct.analyse.task.reconstruct.parameters.ReconstructArchitectureComboBoxDTO;
import husacct.analyse.task.reconstruct.parameters.ReconstructArchitectureNumberFieldDTO;
import husacct.analyse.task.reconstruct.parameters.ReconstructArchitectureParameterDTO;
import husacct.common.locale.ILocaleService;

public abstract class ReconstructArchitectureParameterPanel {
	public ReconstructArchitectureParameterDTO parameterDTO;
	public ReconstructArchitectureDTO dto;
	
	public abstract JPanel createPanel();
	public abstract Object getValue();
	
	public ReconstructArchitectureParameterPanel(ReconstructArchitectureParameterDTO parameterDTO, ReconstructArchitectureDTO dto){
		this.parameterDTO = parameterDTO;
		this.dto = dto;
	}
	
	protected String getTranslation(String translationKey){
		ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
		return localeService.getTranslatedString(translationKey);
	}
	
	public static Object getValueFromReconstructArchitectureDTO(String parameterConstant, ReconstructArchitectureDTO dto){
		switch (parameterConstant){
		case AnalyseReconstructConstants.AlgorithmParameter.Threshold :
			return dto.threshold;
		case AnalyseReconstructConstants.AlgorithmParameter.Granularity :
			return dto.granularity;
		case AnalyseReconstructConstants.AlgorithmParameter.RelationType :
			return dto.relationType;
		default :
			return null;
		}
	}
	
	public static ReconstructArchitectureDTO setValueOfReconstructArchitectureDTO(String parameterConstant, ReconstructArchitectureDTO dto, Object value){
		try{
			switch (parameterConstant){
			case AnalyseReconstructConstants.AlgorithmParameter.Threshold :
				dto.threshold = (int) value;
				break;
			case AnalyseReconstructConstants.AlgorithmParameter.Granularity :
				dto.granularity = value.toString();
				break;
			case AnalyseReconstructConstants.AlgorithmParameter.RelationType :
				dto.relationType = value.toString();
				break;
			}
		}catch (Exception e){
			
		}
		return dto;
	}
	
	public static ReconstructArchitectureParameterPanel getParameterPanel(ReconstructArchitectureParameterDTO paramDTO, ReconstructArchitectureDTO raDto){
		ReconstructArchitectureParameterPanel parameterPanel = null;
		if (paramDTO instanceof ReconstructArchitectureNumberFieldDTO){
			parameterPanel = new ReconstructArchitectureNumberFieldPanel(paramDTO, raDto);
		}
		else if (paramDTO instanceof ReconstructArchitectureComboBoxDTO){
			parameterPanel = new ReconstructArchitectureComboBoxPanel(paramDTO, raDto);
		}
		return parameterPanel;
	}
	
}
