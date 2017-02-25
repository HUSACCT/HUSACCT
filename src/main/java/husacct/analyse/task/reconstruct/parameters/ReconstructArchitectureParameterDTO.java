package husacct.analyse.task.reconstruct.parameters;

import java.lang.reflect.Field;
import java.util.ArrayList;

import husacct.analyse.task.reconstruct.AnalyseReconstructConstants;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants.AlgorithmParameter;
import husacct.analyse.task.reconstruct.parameters.ReconstructArchitectureComboBoxDTO.ComboBoxValue;



public abstract class ReconstructArchitectureParameterDTO {
	public String parameterConstant;
	public String label_TranslationKey;
	public Object defaultValue;
	
	public ReconstructArchitectureParameterDTO(String label_TranslationKey, String parameterConstant, Object defaultValue){
		this.label_TranslationKey = label_TranslationKey;
		this.parameterConstant = parameterConstant;
		this.defaultValue = defaultValue;
	}
	
	public static class DefaultParameterDTOs{
		public static ReconstructArchitectureParameterDTO createThresholdParameter(int defaultValue) {
			ReconstructArchitectureParameterDTO numberFieldDTO = new ReconstructArchitectureNumberFieldDTO(
					AlgorithmParameter.Threshold, AlgorithmParameter.Threshold, defaultValue, 100, 0);
			return numberFieldDTO;
		}
		
		public static ReconstructArchitectureParameterDTO createRelationTypeParameter(String defaultValue){
			Field[] relationTypeFields = AnalyseReconstructConstants.RelationTypes.class.getDeclaredFields();
			ArrayList<ComboBoxValue> comboBoxValues = new ArrayList<>();
			for(Field field : relationTypeFields){
				try {
					String fieldName = field.getName();
					String fieldValue;
					fieldValue = (String) field.get(fieldName);
					comboBoxValues.add(new ComboBoxValue(fieldValue, fieldValue));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			ComboBoxValue[] comboBoxValueArray = new ComboBoxValue[]{};
			comboBoxValueArray = comboBoxValues.toArray(comboBoxValueArray);
			ReconstructArchitectureParameterDTO comboBoxDTO = new ReconstructArchitectureComboBoxDTO(
					AlgorithmParameter.RelationType, AlgorithmParameter.RelationType, defaultValue, comboBoxValueArray);
			return comboBoxDTO;
		}
		
		public static ReconstructArchitectureParameterDTO createGranularityPanel(String defaultValue){
			Field[] granularitieFields = AnalyseReconstructConstants.Granularities.class.getDeclaredFields();
			ArrayList<ComboBoxValue> comboBoxValues = new ArrayList<>();
			for(Field field : granularitieFields){
				try {
					String fieldName = field.getName();
					String fieldValue;
					fieldValue = (String) field.get(fieldName);
					comboBoxValues.add(new ComboBoxValue(fieldValue, fieldValue));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			ComboBoxValue[] comboBoxValueArray = new ComboBoxValue[]{};
			comboBoxValueArray = comboBoxValues.toArray(comboBoxValueArray);
			ReconstructArchitectureParameterDTO comboBoxPanel = new ReconstructArchitectureComboBoxDTO(
					AlgorithmParameter.Granularity, AlgorithmParameter.Granularity, defaultValue, comboBoxValueArray);
			return comboBoxPanel;
		}
	}	
}
