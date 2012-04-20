package husacct.validate.domain.validation.violationtype.java;

import husacct.validate.domain.rulefactory.violationtypeutil.IViolationType;

public enum JavaAccessTypes implements IViolationType{
	PUBLIC("public", 1),
	PROTECTED("protected", 1),
	DEFAULT("default", 1),
	PRIVATE("private", 1);

	private final String key;
	private final int defaultSeverity;

	JavaAccessTypes(String key, int defaultSeverity){
		this.key = key;
		this.defaultSeverity = defaultSeverity;
	}
	
	public int getDefaultSeverity(){
		return defaultSeverity;
	}

	@Override
	public String toString(){
		return key;
	}
}