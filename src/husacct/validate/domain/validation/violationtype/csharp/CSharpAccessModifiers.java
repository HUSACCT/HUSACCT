package husacct.validate.domain.validation.violationtype.csharp;

import husacct.validate.domain.validation.DefaultSeverities;
import husacct.validate.domain.validation.violationtype.IViolationType;
import husacct.validate.domain.validation.violationtype.ViolationCategories;

public enum CSharpAccessModifiers implements IViolationType {
	PUBLIC("public", DefaultSeverities.MEDIUM),
	PROTECTED("protected", DefaultSeverities.MEDIUM),
	DEFAULT("default", DefaultSeverities.LOW),
	PRIVATE("private", DefaultSeverities.LOW);

	private final String key;
	private final DefaultSeverities defaultSeverity;
	private final ViolationCategories violationCategory;

	CSharpAccessModifiers(String key, DefaultSeverities defaultSeverity){
		this.key = key;
		this.defaultSeverity = defaultSeverity;
		this.violationCategory = ViolationCategories.ACCESS_MODIFIERS;
	}

	@Override
	public String toString(){
		return key;
	}

	@Override
	public String getCategory() {
		return violationCategory.toString();
	}

	@Override
	public DefaultSeverities getDefaultSeverity() {
		return defaultSeverity;
	}
}