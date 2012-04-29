package husacct.validate.domain.validation.iternal_tranfer_objects;

import husacct.validate.domain.validation.ruletype.RuleType;

public class CategorykeyClassDTO {
	private final String categoryKey;
	private final Class<RuleType> ruleClass;

	public CategorykeyClassDTO(String categoryKey, Class<RuleType> ruleClass){
		this.categoryKey = categoryKey;
		this.ruleClass = ruleClass;
	}

	public String getCategoryKey() {
		return categoryKey;
	}

	public Class<RuleType> getRuleClass() {
		return ruleClass;
	}
}