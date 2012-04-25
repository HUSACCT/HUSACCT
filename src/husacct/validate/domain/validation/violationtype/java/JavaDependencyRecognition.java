package husacct.validate.domain.validation.violationtype.java;

import husacct.validate.domain.validation.violationtype.IViolationType;

public enum JavaDependencyRecognition implements IViolationType{
	INVOC_METHOD("InvocMethod", 2),
	INVOC_CONSTRUCTOR("InvocConstructor", 2),
	ACCESS_PROPERTY_OR_FIELD("AccessPropertyOrField" , 2),
	EXTENDS_CONCRETE("ExtendsConcrete", 1),
	EXTENDS_ABSTRACT("ExtendsAbstract", 1),
	IMPLEMENTS("Implements", 1),
	DECLARATION("Declaration", 2),
	ANNOTATION("Annotation", 1),
	IMPORT("Import", 1),
	EXCEPTION("Exception", 1);

	private final String key;
	private final int defaultSeverity;

	JavaDependencyRecognition(String value, int defaultSeverity){
		this.key = value;
		this.defaultSeverity = defaultSeverity;
	}

	public int getDefaultSeverity(){
		return defaultSeverity;
	}

	@Override
	public String toString(){
		return key;
	}

	@Override
	public String getCategory() {
		return "test";
	}
}