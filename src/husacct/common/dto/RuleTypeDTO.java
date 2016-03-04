package husacct.common.dto;

public class RuleTypeDTO extends AbstractDTO {
	public final String key;
	public final String descriptionKey;
	public final ViolationTypeDTO[] violationTypes;
	public final RuleTypeDTO[] exceptionRuleTypes;

	public RuleTypeDTO(String key, String description, ViolationTypeDTO[] violationTypes, RuleTypeDTO[] exceptionRuleTypes) {
		this.key = key;
		this.descriptionKey = description;
		this.violationTypes = violationTypes;
		this.exceptionRuleTypes = exceptionRuleTypes;
	}

	public String getKey() {
		return key;
	}

	public String getDescriptionKey() {
		return descriptionKey;
	}

	public ViolationTypeDTO[] getViolationTypes() {
		return violationTypes;
	}

	public RuleTypeDTO[] getExceptionRuleTypes() {
		return exceptionRuleTypes;
	}

	public String toString() {
    	
        String representation = "";
        representation += "\nRuleTypeKey: " + key + ", DescriptionKey: " + descriptionKey;
        representation += "\nViolationTypeKeys: ";
        for (ViolationTypeDTO v : violationTypes){
        	representation += v.key + ", ";
        }
        representation += "\nExceptionRuleKeys: ";
        for (RuleTypeDTO r : exceptionRuleTypes){
        	representation += r.key + ", ";
        }
        representation += "\n";
        return representation;
    }
}