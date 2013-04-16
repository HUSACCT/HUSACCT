package husacct.validate.domain.validation.ruletype;

import java.util.EnumSet;

public enum RuleTypeCategories {
	PROPERTY_RULE_TYPES("PropertyRuleTypes", "Property Rule Types"), RELATION_RULE_TYPES("RelationruleTypes", "Relation Rule Types");

	public static final EnumSet<RuleTypeCategories> mainRuleTypeCategories = EnumSet.of(PROPERTY_RULE_TYPES, RELATION_RULE_TYPES);
	private final String key;
	private final String categoryName;

	RuleTypeCategories(String key, String categoryName) {
		this.key = key;
		this.categoryName = categoryName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	@Override
	public String toString() {
		return this.key;
	}
}