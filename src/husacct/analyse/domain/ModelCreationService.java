package husacct.analyse.domain;

import java.util.List;

public interface ModelCreationService {
	

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
	
	public void createMethod(String name, String uniqueName,
			String accessControlQualifier, String signature,
			boolean isPureAccessor, String declaredReturnType,
			String belongsToClass, boolean isConstructor, boolean isAbstract,
			boolean hasClassScope);
	
	public void createInheritanceDefinition(String from, String to, int lineNumber);
	public void createImplementsDefinition(String from, String to, int lineNumber);
	
	void createPropertyOrFieldInvocation(String type, String from, String to, int lineNumber, String invocationName);
	void createMethodInvocation(String type, String from, String to, int lineNumber, String invocationName);
	void createConstructorInvocation(String type, String from, String to, int lineNumber, String invocationName);
	
	//Function to connect dependencies, after all modules are known in the model
	public void connectDependencies();
	
	//Debug-functies voor in de main:
	public String represent();

	public void createLocalVariable(String belongsToMethodString, String belongsToClass,
			String declareType, String name, String uniqueName, int lineNumber);

	public void createParameter(String name, String uniqueName,
			String declareType, String belongsToClass, int lineNumber,
			String belongsToMethod, String declareTypes);
	
}
