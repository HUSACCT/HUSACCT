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
}