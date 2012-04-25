package husacct.validate.domain.validation.violationtype.java;

import husacct.validate.domain.validation.violationtype.IViolationType;

public enum JavaDependencyRecognition implements IViolationType{
	INVOC_METHOD("InvocMethod"),
	INVOC_CONSTRUCTOR("InvocConstructor"),
	ACCESS_PROPERTY_OR_FIELD("AccessPropertyOrField"),
	EXTENDS_CONCRETE("ExtendsConcrete"),
	EXTENDS_ABSTRACT("ExtendsAbstract"),
	IMPLEMENTS("Implements"),
	DECLARATION("Declaration"),
	ANNOTATION("Annotation"),
	IMPORT("Import"),
	EXCEPTION("Exception");

	private final String key;

	JavaDependencyRecognition(String value){
		this.key = value;
	}

	@Override
	public String toString(){
		return key;
	}

	@Override
	public String getCategory() {
		return "DependencyRecognition";
	}
}