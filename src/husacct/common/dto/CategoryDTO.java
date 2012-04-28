package husacct.common.dto;

public class CategoryDTO extends AbstractDTO {
	public final String key;
	public final RuleTypeDTO[] ruleTypes;

	public CategoryDTO(String key, RuleTypeDTO[] ruleTypes) {
		this.key = key;
		this.ruleTypes = ruleTypes;
	}
}