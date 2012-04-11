package husacct.validate.domain.factory.violationtypeutil;

import husacct.validate.domain.ruletype.Rule;

public class CategorykeyClassDTO {
	private String categoryKey;
	private Class<Rule> ruleClass;	

	public CategorykeyClassDTO(String categoryKey, Class<Rule> ruleClass){
		this.categoryKey = categoryKey;
		this.ruleClass = ruleClass;
	}

	public String getCategoryKey() {
		return categoryKey;
	}

	public Class<Rule> getRuleClass() {
		return ruleClass;
	}
}