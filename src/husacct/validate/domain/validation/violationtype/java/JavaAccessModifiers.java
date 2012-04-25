package husacct.validate.domain.validation.violationtype.java;

import husacct.validate.domain.validation.violationtype.IViolationType;

public enum JavaAccessModifiers implements IViolationType{
	PUBLIC("public"),
	PROTECTED("protected"),
	DEFAULT("default"),
	PRIVATE("private");

	private final String key;

	JavaAccessModifiers(String key){
		this.key = key;
	}

	@Override
	public String toString(){
		return key;
	}

	@Override
	public String getCategory() {
		return "AccessModifier";
	}
}