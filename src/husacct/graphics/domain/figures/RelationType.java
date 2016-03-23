package husacct.graphics.domain.figures;

public enum RelationType {
	
//	INHERITANCE("Inherits"),
	DEPENDENCY("Dependency"),
	VIOLATION("Violation"),
//	IMPLEMENTATION("Implements"),
//	ASSOCIATION("Association"),
//	AGGREGATION("Aggregation"),
//	COMPOSITION("Composition");

	INHERITANCELINK("Inherits"),
    IMPLEMENTSLINK("Implements"),
    ATTRIBUTELINK("Attribute");
	
	private final String key;
	
	private RelationType(String key) {
		this.key = key;
	}
	
	public String get(){
		return key;
	}
	
	public static RelationType fromString(String enumName) {
		switch(enumName) {
			case "Inherits": return INHERITANCELINK;
			case "Implements": return IMPLEMENTSLINK;
			case "Attribute": return ATTRIBUTELINK;
			default: return null;
		}
	}

}
