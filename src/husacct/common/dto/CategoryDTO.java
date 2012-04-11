package husacct.common.dto;

public class CategoryDTO extends AbstractDTO {
	private String key;
	private RuleTypeDTO[] ruleTypes;

	public CategoryDTO(String key, RuleTypeDTO[] ruleTypes) {
		this.key = key;
		this.ruleTypes = ruleTypes;
	}

	public String getKey() {
		return key;
	}

	public RuleTypeDTO[] getRuleTypes() {
		return ruleTypes;
	}
}