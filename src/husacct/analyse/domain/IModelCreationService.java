package husacct.analyse.domain;

import java.util.ArrayList;
import java.util.List;

public interface IModelCreationService {
	

	public void createPackage(String uniqueName, String belongsToPackage, String name);
	public void clearModel( );
	
	@Deprecated /*This function will be replaced after all analysers have adapter one of the below functions*/
	public void createClass(String uniqueName, String name, String belongsToPackage, boolean isAbstract, boolean isInnerClass, String belongsToClass);
	public void createClass(String uniqueName, String name, String belongsToPackage, boolean isAbstract, boolean isInnerClass);
	public void createClass(String uniqueName, String name, String belongsToPackage, boolean isAbstract, boolean isInnerClass, String belongsToClass, String visibillity);
	
	public void createInterface(String uniqueName, String name, String belongsToPackage);
	public void createInterface(String uniqueName, String name, String belongsToPackage, String visibility);
	
	public void createImport(String importingClass, String importedModule, int lineNumber, String completeImportString, boolean importsCompletePackage);
	
	public void createAttribute(Boolean classScope, String accesControlQualifier, String belongsToClass, String declareType, String name, String uniqueName);
	public void createAttribute(Boolean classScope, String accesControlQualifier, String belongsToClass, String declareType, String name, String uniqueName, int lineNumber);
	public void createAttribute(Boolean classScope, String accesControlQualifier, String belongsToClass, String declareType, String name, String uniqueName, int lineNumber, List<String> declareTypes);
	
	public void createAnnotation(String belongsToClass, String declareType, String name, String uniqueName, int linenumber);
	
	public void createException(String fromClass, String ExceptionClass, int lineNumber, String declarationType);
	
	public void createMethod(String name, String uniqueName, String accessControlQualifier, String signature,
			boolean isPureAccessor, String declaredReturnType,
			String belongsToClass, boolean isConstructor, boolean isAbstract,
			boolean hasClassScope);
	public void createMethod(String name, String uniqueName, String accessControlQualifier, String signature,
			boolean isPureAccessor, String declaredReturnType,
			String belongsToClass, boolean isConstructor, boolean isAbstract,
			boolean hasClassScope, int lineNumber);
	public void createMethod(String name, String uniqueName, String accessControlQualifier, String signature,
			boolean isPureAccessor, ArrayList<String> declaredReturnType,
			String belongsToClass, boolean isConstructor, boolean isAbstract,
			boolean hasClassScope, int lineNumber);
	
	public void createInheritanceDefinition(String from, String to, int lineNumber);
	public void createImplementsDefinition(String from, String to, int lineNumber);
	
	void createPropertyOrFieldInvocation(String from, int lineNumber, String to, String invocationName, String nameOfInstance);
	void createMethodInvocation(String from, int lineNumber, String to, String invocationName, String nameOfInstance);
	void createConstructorInvocation(String from, int lineNumber, String to, String invocationName, String nameOfInstance);
	
	void createPropertyOrFieldInvocation(String from, String to, int lineNumber, String invocationName, String belongsToMethod, String nameOfInstance);
	void createMethodInvocation(String from, String to, int lineNumber, String invocationName, String belongsToMethod, String nameOfInstance);
	void createConstructorInvocation(String from, String to, int lineNumber, String invocationName, String belongsToMethod, String nameOfInstance);
	
	public void createLocalVariable(String belongsToClass,
			String declareType, String name, String uniqueName, int lineNumber, String belongsToMethodString);
	
	public void createLocalVariable(String belongsToClass,
			String declareType, String name, String uniqueName, int lineNumber, String belongsToMethodString, List<String> declareTypes);

	public void createParameter(String name, String uniqueName, String declareType, 
			String belongsToClass, int lineNumber, String belongsToMethod);
	
	public void createParameter(String name, String uniqueName,
			String declareType, String belongsToClass, int lineNumber,
			String belongsToMethod, List<String> declareTypes);
	
	//Function to connect dependencies, after all modules are known in the model
	public void connectDependencies();
	
	//Single debugging functionality, to check the completeness of the implementation
	public String represent();
	public void clearMemoryFromObjectsNotUsedAnymore();
}
