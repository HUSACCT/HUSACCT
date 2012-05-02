package husacct.analyse.domain;

public interface ModelCreationService {
	
	public void createPackage(String uniqieName, String belongsToPackage, String name);
	
	public void createClass(String uniqueName, String name, String belongsToPackage, boolean isAbstract, boolean isInnerClass);
	public void createClass(String uniqueName, String name, String belongsToPackage, boolean isAbstract, boolean isInnerClass, String belongsToClass);
	
	public void createImport(String importingClass, String importedModule, int lineNumber, String completeImportString, boolean importsCompletePackage);
	
	public void createAttribute(Boolean classScope, String accesControlQualifier, String belongsToClass, String declareType, String name, String uniqueName);
	
	public void createException(String fromClass, String ExceptionClass, int lineNumber, String declarationType);
	
	public void createMethod(String name, String uniqueName,
			String accessControlQualifier, String signature,
			boolean isPureAccessor, String declaredReturnType,
			String belongsToClass, boolean isConstructor, boolean isAbstract,
			boolean hasClassScope);
	
	public void createInheritanceDefinition(String from, String to,
			int lineNumber, String type);
	
	
	
	//Debug-functies voor in de main:
	public void printModel();
	public String represent();
}
