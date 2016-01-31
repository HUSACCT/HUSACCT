package husacct.analyse.domain;

public enum DependencySubTypes {
	// DependencyTypes.DECLARATION
	ALLDECLARATIONS("All", DependencyTypes.DECLARATION),
	CLASSVAR("Class Variable", DependencyTypes.DECLARATION),
	EXCEPTION("Exception", DependencyTypes.DECLARATION),
	INSTANCEVAR("Instance Variable", DependencyTypes.DECLARATION),
	LOCALVAR("Local Variable", DependencyTypes.DECLARATION),
	PARAMETER("Parameter", DependencyTypes.DECLARATION),
	RETURNTYPE("Return Type", DependencyTypes.DECLARATION),
	TYPEPARAMETER("Type Parameter", DependencyTypes.DECLARATION);
	
	private final String key;
	private final DependencyTypes dependencyType;

	private DependencySubTypes(String key, DependencyTypes dependencyType) {
		this.key = key;
		this.dependencyType = dependencyType;
	}

	public DependencyTypes getDependencyType() {
		return dependencyType;
	}

	public String toString() {
		return key;
	}
}