package husacct.analyse.domain;

import java.util.List;

public interface IModelCreationService {

    public void createPackage(String uniqueName, String belongsToPackage, String name);

    public void clearModel();

    public void createClass(String sourceFilePath, String uniqueName, String name, String belongsToPackage, boolean isAbstract, boolean isInnerClass, String belongsToClass, String visibillity, boolean isInterface);

    public void createImport(String importingClass, String importedModule, int lineNumber, String completeImportString, boolean importsCompletePackage);

    public void createAttributeOnly(Boolean classScope, String accesControlQualifier, String belongsToClass, String declareType, String name, String uniqueName, int lineNumber);

    public void createAttribute(Boolean classScope, String accesControlQualifier, String belongsToClass, String declareType, String name, String uniqueName, int lineNumber);

    public void createAnnotation(String belongsToClass, String declareType, String name, String uniqueName, int linenumber);

    public void createException(String fromClass, String ExceptionClass, int lineNumber, String declarationType);

    public void createMethod(String name, String uniqueName, String accessControlQualifier, String signature,
            boolean isPureAccessor, String declaredReturnType,
            String belongsToClass, boolean isConstructor, boolean isAbstract,
            boolean hasClassScope, int lineNumber);

    public void createInheritanceDefinition(String from, String to, int lineNumber);

    public void createImplementsDefinition(String from, String to, int lineNumber);

    public void createPropertyOrFieldInvocation(String from, String to, int lineNumber, String invocationName, String belongsToMethod, String nameOfInstance);

    public void createMethodInvocation(String from, String to, int lineNumber, String invocationName, String belongsToMethod, String nameOfInstance, String type);

    public void createDeclarationTypeCast(String from, String to, int lineNumber);

    public void createLocalVariable(String belongsToClass,
            String declareType, String name, String uniqueName, int lineNumber, String belongsToMethodString);

    public void createLocalVariableOnly(String belongsToClass, String declareType, String name,
            String uniqueName, int lineNumber, String belongsToMethodString);

	public void createParameterOnly(String name, String uniqueName, String declareType,
            String belongsToClass, int lineNumber, String belongsToMethod, List<String> declareTypes);

    public void createParameter(String name, String uniqueName,
            String declareType, String belongsToClass, int lineNumber,
            String belongsToMethod, List<String> declareTypes);

    //Function to connect dependencies, after all modules are known in the model
    public void executePostProcesses();

    //Single debugging functionality, to check the completeness of the implementation
    public String represent();

    public void clearMemoryFromObjectsNotUsedAnymore();
}
