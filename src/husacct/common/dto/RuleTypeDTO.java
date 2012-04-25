package husacct.common.dto;

public class RuleTypeDTO extends AbstractDTO {
	private final String key;
	private final String descriptionKey;
	private final ViolationTypeDTO[] violationTypes;
	private final RuleTypeDTO[] exceptionRuleTypes;

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
}