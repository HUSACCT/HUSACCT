package husacct.analyse.domain;

import java.util.List;

public interface IModelCreationService {

    public void createPackage(String uniqueName, String belongsToPackage, String name);

    public void createClass(String sourceFilePath, int linesOfCode, String uniqueName, String name, String belongsToPackage, boolean isAbstract, boolean isInnerClass, String belongsToClass, String visibillity, boolean isInterface, boolean isEnumeration);

    public void createImport(String importingClass, String importedModule, int lineNumber, String completeImportString, boolean importsCompletePackage);

    public void createAttributeOnly(boolean classScope, boolean isFinal, String accesControlQualifier, String belongsToClass, String declareType, String name, String uniqueName, int lineNumber, String typeInClassDiagram, boolean multipleValues);

    public void createAttribute(boolean classScope, boolean isFinal, String accesControlQualifier, String belongsToClass, String declareType, String name, String uniqueName, int lineNumber, String typeInClassDiagram, boolean multipleValues);

    public void createAnnotation(String belongsToClass, String declareType, String name, String uniqueName, int linenumber, String annotatedElement);

    public void createException(String fromClass, String ExceptionClass, int lineNumber, String declarationType);

    public void createMethod(String name, String uniqueName, String accessControlQualifier, String signature,
            boolean isPureAccessor, String declaredReturnType,
            String belongsToClass, boolean isConstructor, boolean isAbstract,
            boolean hasClassScope, int lineNumber);

    public void createMethodOnly(String name, String uniqueName, String accessControlQualifier, String signature,
            boolean isPureAccessor, String declaredReturnType,
            String belongsToClass, boolean isConstructor, boolean isAbstract,
            boolean hasClassScope, int lineNumber);

    public void createInheritanceDefinition(String from, String to, int lineNumber);

    public void createImplementsDefinition(String from, String to, int lineNumber);

    public void createVariableInvocation(String from, String to, int lineNumber, String belongsToMethod);

    public void createMethodInvocation(String from, String to, int lineNumber, String belongsToMethod, String type);

    public void createDeclarationTypeCast(String from, String to, int lineNumber);

    public void createLocalVariable(String belongsToClass,
            String declareType, String name, String uniqueName, int lineNumber, String belongsToMethodString);

    public void createLocalVariableOnly(String belongsToClass, String declareType, String name, String uniqueName, 
    		int lineNumber, String belongsToMethodString);
    
	public void createParameterOnly(String name, String uniqueName, String declareType,
            String belongsToClass, int lineNumber, String belongsToMethod, List<String> declareTypes);

    public void createParameter(String name, String uniqueName, String declareType, String belongsToClass, int lineNumber,
            String belongsToMethod, List<String> declareTypes);

    public void createGenericParameterType(String belongsToClass, String belongsToMethod, int lineNumber, String parameterType);

    /** Executes post-processes such as connecting entities and associations, 
     * detecting inherited and indirect associations, et cetera.
     */
    public void executePostProcesses();

}
