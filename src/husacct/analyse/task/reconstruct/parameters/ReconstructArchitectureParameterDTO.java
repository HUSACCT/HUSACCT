package husacct.analyse.task.reconstruct.parameters;

import husacct.analyse.presentation.reconstruct.parameter.ReconstructArchitectureParameterPanel;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants.AlgorithmParameter;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants.Granularities;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants.RelationTypes;
import husacct.analyse.task.reconstruct.parameters.ReconstructArchitectureComboBoxDTO.ComboBoxValue;



public abstract class ReconstructArchitectureParameterDTO {
	public String parameterConstant;
	public String label_TranslationKey;
	public Object defaultValue;
	public ReconstructArchitectureParameterPanel parameterPanel;
	
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
			ComboBoxValue[] comboBoxValues = new ComboBoxValue[]{
					new ComboBoxValue(RelationTypes.allDependencies, RelationTypes.allDependencies),
					new ComboBoxValue(RelationTypes.umlLinks, RelationTypes.umlLinks),
					new ComboBoxValue(RelationTypes.accessCallReferenceDependencies, RelationTypes.accessCallReferenceDependencies)};
			ReconstructArchitectureParameterDTO comboBoxDTO = new ReconstructArchitectureComboBoxDTO(
					AlgorithmParameter.RelationType, AlgorithmParameter.RelationType, defaultValue, comboBoxValues);
			return comboBoxDTO;
		}
		public static ReconstructArchitectureParameterDTO createGranularityPanel(String defaultValue){
			ComboBoxValue[] comboBoxValues = new ComboBoxValue[]{
					new ComboBoxValue(Granularities.Classes, Granularities.Classes),
					new ComboBoxValue(Granularities.PackagesInRootOnlyClasses, Granularities.PackagesInRootOnlyClasses),
					new ComboBoxValue(Granularities.PackagesWithAllClasses, Granularities.PackagesWithAllClasses)};
			ReconstructArchitectureParameterDTO comboBoxPanel = new ReconstructArchitectureComboBoxDTO(
					AlgorithmParameter.Granularity, AlgorithmParameter.Granularity, defaultValue, comboBoxValues);
			return comboBoxPanel;
		}
	}	
}
