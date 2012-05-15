package husacct.validate.domain.validation.violationtype.java;

import husacct.validate.domain.validation.DefaultSeverities;
import husacct.validate.domain.validation.violationtype.IViolationType;
import husacct.validate.domain.validation.violationtype.ViolationCategories;

public enum JavaDependencyRecognition implements IViolationType{
	INVOC_METHOD("InvocMethod", DefaultSeverities.MEDIUM),
	INVOC_CONSTRUCTOR("InvocConstructor", DefaultSeverities.MEDIUM),
	ACCESS_PROPERTY_OR_FIELD("AccessPropertyOrField", DefaultSeverities.MEDIUM),
	EXTENDS_CONCRETE("ExtendsConcrete", DefaultSeverities.MEDIUM),
	EXTENDS_ABSTRACT("ExtendsAbstract", DefaultSeverities.MEDIUM),
	IMPLEMENTS("Implements", DefaultSeverities.MEDIUM),
	DECLARATION("Declaration", DefaultSeverities.MEDIUM),
	ANNOTATION("Annotation", DefaultSeverities.LOW),
	IMPORT("Import", DefaultSeverities.LOW),
	EXCEPTION("Exception", DefaultSeverities.MEDIUM);

	private final String key;
	private final DefaultSeverities defaultSeverity;
	private final ViolationCategories violationCategory;

	JavaDependencyRecognition(String value, DefaultSeverities defaultSeverity){
		this.key = value;
		this.defaultSeverity = defaultSeverity;
		this.violationCategory = ViolationCategories.DEPENDENCY_RECOGNITION;
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