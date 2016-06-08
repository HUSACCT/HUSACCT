package husacct.analyse.task.reconstruct.parameters;

public class ReconstructArchitectureNumberFieldDTO extends ReconstructArchitectureParameterDTO {
	public int maxValue;
	public int minValue;
	
	public ReconstructArchitectureNumberFieldDTO(String label_TranslationKey, String parameterConstant, Object defaultValue, int maxValue, int minValue) {
		super(label_TranslationKey, parameterConstant, defaultValue);
		this.maxValue = maxValue;
		this.minValue = minValue;
	}

}
