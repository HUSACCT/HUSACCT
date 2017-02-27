package husacct.common.enums;

//Owner: Analyse

public enum DependencySubTypes {
	// DependencyTypes.ACCESS
	ACC_VARIABLE("Variable", DependencyTypes.ACCESS),
	ACC_INSTANCE_VAR("Instance Variable", DependencyTypes.ACCESS),
	ACC_INSTANCE_VAR_CONST("Instance Variable Constant", DependencyTypes.ACCESS),
	ACC_CLASS_VAR("Class Variable", DependencyTypes.ACCESS),
	ACC_CLASS_VAR_CONST("Class Variable Constant", DependencyTypes.ACCESS),
	ACC_ENUMERATION_VAR("Enumeration Variable", DependencyTypes.ACCESS),
	ACC_INTERFACE_VAR("Interface Variable", DependencyTypes.ACCESS),
	ACC_LIBRARY_VAR("Library Variable", DependencyTypes.ACCESS),
	
	// DependencyTypes.DECLARATION
	CALL_METHOD("Method", DependencyTypes.CALL),
	CALL_INSTANCE_METH("Instance Method", DependencyTypes.CALL),
	CALL_CLASS_METH("Class Method", DependencyTypes.CALL),
	CALL_CONSTRUCTOR("Constructor", DependencyTypes.CALL),
	CALL_ENUM_METH("Enumeration Method", DependencyTypes.CALL),
	CALL_INTERFACE_METH("Interface Method", DependencyTypes.CALL),
	CALL_LIBARRY_METH("Library Method", DependencyTypes.CALL),

	// DependencyTypes.DECLARATION
	DECL_CLASS_VAR("Class Variable", DependencyTypes.DECLARATION),
	DECL_EXCEPTION("Exception", DependencyTypes.DECLARATION),
	DECL_INSTANCE_VAR("Instance Variable", DependencyTypes.DECLARATION),
	DECL_LOCAL_VAR("Local Variable", DependencyTypes.DECLARATION),
	DECL_PARAMETER("Parameter", DependencyTypes.DECLARATION),
	DECL_RETURN_TYPE("Return Type", DependencyTypes.DECLARATION),
	DECL_TYPE_PARAMETER("Type Parameter", DependencyTypes.DECLARATION),
	
	// DependencyTypes.DECLARATION
	INH_EXTENDS_CLASS("Extends Class", DependencyTypes.INHERITANCE),
	INH_EXTENDS_ABSTRACT_CLASS("Extends Abstract Class", DependencyTypes.INHERITANCE),
	INH_IMPLEMENTS_INTERFACE("Implements Interface", DependencyTypes.INHERITANCE),
	INH_FROM_LIBRARY_CLASS("From Library Class", DependencyTypes.INHERITANCE),

	// DependencyTypes.REFERENCE
	REF_TYPE("Type", DependencyTypes.REFERENCE),
	REF_TYPE_CAST("Type Cast", DependencyTypes.REFERENCE),
	REF_RETURN_TYPE("Return Type", DependencyTypes.REFERENCE),
	REF_TYPE_OF_VAR("Type of Variable", DependencyTypes.REFERENCE);

	
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