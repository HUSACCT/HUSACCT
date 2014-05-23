package husacct.validate.domain.validation.violationtype;

import husacct.validate.domain.validation.DefaultSeverities;

public enum ViolationTypes implements IViolationType {
	CALL("Call", ViolationCategories.DEPENDENCY_RECOGNITION, DefaultSeverities.MEDIUM),
	ACCESS("Access", ViolationCategories.DEPENDENCY_RECOGNITION, DefaultSeverities.MEDIUM),
	IMPORT("Import", ViolationCategories.DEPENDENCY_RECOGNITION, DefaultSeverities.LOW),
	DECLARATION("Declaration", ViolationCategories.DEPENDENCY_RECOGNITION, DefaultSeverities.MEDIUM),
	INHERITANCE("Inheritance", ViolationCategories.DEPENDENCY_RECOGNITION, DefaultSeverities.MEDIUM),
	ANNOTATION("Annotation", ViolationCategories.DEPENDENCY_RECOGNITION, DefaultSeverities.LOW),
	
	PUBLIC("public", ViolationCategories.ACCESS_MODIFIERS, DefaultSeverities.MEDIUM),
	PROTECTED("protected", ViolationCategories.ACCESS_MODIFIERS, DefaultSeverities.MEDIUM),
	DEFAULT("default", ViolationCategories.ACCESS_MODIFIERS, DefaultSeverities.LOW),
	PRIVATE("private", ViolationCategories.ACCESS_MODIFIERS, DefaultSeverities.LOW),
	PACKAGE("package", ViolationCategories.PACKAGING, DefaultSeverities.LOW),
	CLASS("class", ViolationCategories.PACKAGING, DefaultSeverities.LOW);
	
	private final String key;
	private final DefaultSeverities defaultSeverity;
	private final ViolationCategories violationCategory;

	ViolationTypes(String value, ViolationCategories violationCategory, DefaultSeverities defaultSeverity) {
		this.key = value;
		this.defaultSeverity = defaultSeverity;
		this.violationCategory = violationCategory;
	}

	@Override
	public String toString() {
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
