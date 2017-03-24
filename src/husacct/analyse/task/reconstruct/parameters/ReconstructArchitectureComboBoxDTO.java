package husacct.analyse.task.reconstruct.parameters;


public class ReconstructArchitectureComboBoxDTO extends ReconstructArchitectureParameterDTO{
	public ComboBoxValue[] comboBoxValues;
	
	public ReconstructArchitectureComboBoxDTO(String label_TranslationKey, String parameterConstant, Object defaultValue, ComboBoxValue[] comboBoxValues) {
		super(label_TranslationKey, parameterConstant, defaultValue);
		this.comboBoxValues = comboBoxValues;
	}

	public static class ComboBoxValue {
	    private final String label;
	    private final String value;
	    public ComboBoxValue(String label, String value) {
	        this.label = label;
	        this.value = value;
	    }
	    public String getLabel() {
	        return label;
	    }
	    public String getValue() {
	        return value;
	    }
	    @Override
		public String toString(){
	    	return label;
	    }
	}
	
	public ComboBoxValue getComboboxValue(String stringValue){
		ComboBoxValue comboBoxValue = new ComboBoxValue("", "");
		for(ComboBoxValue cbv : comboBoxValues){
			if (cbv.getValue().equals(stringValue)){
				comboBoxValue = cbv;
			}
		}
		return comboBoxValue;
	}
}
