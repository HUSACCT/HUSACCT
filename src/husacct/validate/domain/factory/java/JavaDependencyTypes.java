package husacct.validate.domain.factory.java;

enum JavaDependencyTypes{
	INVOC_METHOD("InvocMethod"),
	INVOC_CONSTRUCTOR("InvocConstructor"),
	ACCESS_PROPERTY_OR_FIELD("AccessPropertyOrField"),
	EXTENDS("Extends"),
	IMPLEMENTS("Implements"),
	DECLARATION("Declaration"),
	ANNOTATION("Annotation"),
	IMPORT("Import"),
	EXCEPTION("Exception");

	private final String key;

	JavaDependencyTypes(String value){
		this.key = value;
	}

	@Override
	public String toString(){
		return key;
	}
}