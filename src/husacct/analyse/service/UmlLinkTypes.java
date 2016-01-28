package husacct.analyse.service;

public enum UmlLinkTypes {
	INHERITANCELINK("Inherits"),
    IMPLEMENTSLINK("Implements"),
    ATTRIBUTELINK("Attribute");
	
	private final String key;
	
	UmlLinkTypes(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return key;
	}

}
