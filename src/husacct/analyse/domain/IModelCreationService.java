package husacct.analyse.domain;

public interface IModelCreationService {
	

	public void createPackage(String uniqueName, String belongsToPackage, String name);
	public void clearModel( );
	
	public void createClass(String uniqueName, String name, String belongsToPackage, boolean isAbstract, boolean isInnerClass);
	public void createClass(String uniqueName, String name, String belongsToPackage, boolean isAbstract, boolean isInnerClass, String belongsToClass);
	
	public void createInterface(String uniqueName, String name, String belongsToPackage);
	
	public void createImport(String importingClass, String importedModule, int lineNumber, String completeImportString, boolean importsCompletePackage);
	
	public void createAttribute(Boolean classScope, String accesControlQualifier, String belongsToClass, String declareType, String name, String uniqueName);
	public void createAttribute(Boolean classScope, String accesControlQualifier, String belongsToClass, String declareType, String name, String uniqueName, int lineNumber);
	
	public void createAnnotation(String belongsToClass, String declareType, String name, String uniqueName, int linenumber);
	
	public void createException(String fromClass, String ExceptionClass, int lineNumber, String declarationType);
	
	public void createMethod(String name, String uniqueName, String accessControlQualifier, String signature,
			boolean isPureAccessor, String declaredReturnType,
			String belongsToClass, boolean isConstructor, boolean isAbstract,
			boolean hasClassScope);
	
	public void createInheritanceDefinition(String from, String to, int lineNumber);
	public void createImplementsDefinition(String from, String to, int lineNumber);
	
	void createPropertyOrFieldInvocation(String from, int lineNumber, String to, String invocationName, String nameOfInstance);
	void createMethodInvocation(String from, int lineNumber, String to, String invocationName, String nameOfInstance);
	void createConstructorInvocation(String from, int lineNumber, String to, String invocationName, String nameOfInstance);
	
	void createPropertyOrFieldInvocation(String from, String to, int lineNumber, String invocationName, String belongsToMethod, String nameOfInstance);
	void createMethodInvocation(String from, String to, int lineNumber, String invocationName, String belongsToMethod, String nameOfInstance);
	void createConstructorInvocation(String from, String to, int lineNumber, String invocationName, String belongsToMethod, String nameOfInstance);
	
	//Function to connect dependencies, after all modules are known in the model
	public void connectDependencies();

	public void createLocalVariable(String belongsToClass,
			String declareType, String name, String uniqueName, int lineNumber, String belongsToMethodString);

	public void createParameter(String name, String uniqueName,
			String declareType, String belongsToClass, int lineNumber,
			String belongsToMethod);
	
	//Single debugging functionality, to check the completeness of the implementation
		public String represent();
	
}
