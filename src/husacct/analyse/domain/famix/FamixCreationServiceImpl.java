package husacct.analyse.domain.famix;

import java.util.ArrayList;
import java.util.List;

import javax.naming.directory.InvalidAttributesException;
import husacct.analyse.domain.IModelCreationService;

public class FamixCreationServiceImpl implements IModelCreationService {

    private FamixModel model;
    private FamixDependencyConnector dependencyConnector;

    public FamixCreationServiceImpl() {
        model = FamixModel.getInstance();
        dependencyConnector = new FamixDependencyConnector();
    }

    @Override
    public void clearModel() {
        model.clear();
    }

    public void createPackage(String uniqueName, String belongsToPackage, String name) {
        FamixPackage fPackage = new FamixPackage();
        fPackage.uniqueName = uniqueName;
        fPackage.belongsToPackage = belongsToPackage;
        fPackage.name = name;
        addToModel(fPackage);
    }

    @Override
    public void createClass(String uniqueName, String name, String belongsToPackage,
            boolean isAbstract, boolean isInnerClass) {
        createClass(uniqueName, name, belongsToPackage, isAbstract, isInnerClass, "", "public");
    }

    @Override
    public void createClass(String uniqueName, String name, String belongsToPackage,
            boolean isAbstract, boolean isInnerClass, String belongsToClass) {
        createClass(uniqueName, name, belongsToPackage, isAbstract, isInnerClass, belongsToClass, "public");
    }

    @Override
    public void createClass(String uniqueName, String name, String belongsToPackage,
            boolean isAbstract, boolean isInnerClass, String belongsToClass, String visibility) {
        FamixClass fClass = new FamixClass();
        fClass.uniqueName = uniqueName;
        fClass.isAbstract = isAbstract;
        fClass.belongsToPackage = belongsToPackage;
        fClass.isInnerClass = isInnerClass;
        fClass.name = name;
        fClass.belongsToClass = belongsToClass;
        if (visibility.equals("")) {
            fClass.visibility = "default";
        } else {
            fClass.visibility = visibility;
        }
        addToModel(fClass);
    }

    @Override
    public void createInterface(String uniqueName, String name, String belongsToPackage) {
        createInterface(uniqueName, name, belongsToPackage, "default");
    }

    @Override
    public void createInterface(String uniqueName, String name, String belongsToPackage, String visibility) {
        FamixInterface fInterface = new FamixInterface();
        fInterface.uniqueName = uniqueName;
        fInterface.name = name;
        fInterface.belongsToPackage = belongsToPackage;
        fInterface.visibility = visibility;
        addToModel(fInterface);
    }

    @Override
    public void createImport(String importingClass, String importedModule, int lineNumber,
            String completeImportString, boolean importsCompletePackage) {

        FamixImport fImport = new FamixImport();
        fImport.from = importingClass;
        fImport.to = importedModule;
        fImport.lineNumber = lineNumber;
        fImport.importingClass = importingClass;
        fImport.completeImportString = completeImportString;
        fImport.importedModule = completeImportString;
        fImport.importsCompletePackage = importsCompletePackage;
        addToModel(fImport);
    }

    @Override
    public void createMethod(String name, String uniqueName, String accessControlQualifier,
            String signature, boolean isPureAccessor, String declaredReturnType,
            String belongsToClass, boolean isConstructor, boolean isAbstract, boolean hasClassScope) {

        createMethod(name, uniqueName, accessControlQualifier, signature, isPureAccessor, declaredReturnType,
                belongsToClass, isConstructor, isAbstract, hasClassScope, 0);
    }

    public void createMethod(String name, String uniqueName, String accessControlQualifier,
            String signature, boolean isPureAccessor, String declaredReturnType,
            String belongsToClass, boolean isConstructor, boolean isAbstract, boolean hasClassScope, int lineNumber) {

        ArrayList<String> declaredReturnTypes = new ArrayList<String>();
        declaredReturnTypes.add(declaredReturnType);

        createMethod(name, uniqueName, accessControlQualifier, signature, isPureAccessor, declaredReturnTypes,
                belongsToClass, isConstructor, isAbstract, hasClassScope, lineNumber);
    }

    @Override
    public void createMethod(String name, String uniqueName, String accessControlQualifier,
            String signature, boolean isPureAccessor, ArrayList<String> declaredReturnType,
            String belongsToClass, boolean isConstructor, boolean isAbstract, boolean hasClassScope, int lineNumber) {

        FamixMethod famixMethod = new FamixMethod();
        famixMethod.name = name;
        famixMethod.uniqueName = uniqueName;
        famixMethod.accessControlQualifier = accessControlQualifier;
        famixMethod.signature = signature;
        famixMethod.isPureAccessor = isPureAccessor;
        famixMethod.declaredReturnType = declaredReturnType.size() == 0 ? "" : declaredReturnType.get(0);
        famixMethod.belongsToClass = belongsToClass;
        famixMethod.isConstructor = isConstructor;
        famixMethod.isAbstract = isAbstract;
        famixMethod.hasClassScope = hasClassScope;
        addToModel(famixMethod);

        for (String s : declaredReturnType) {
            if (s != "" && s != null) {
                FamixAssociation fAssocation = new FamixAssociation();
                fAssocation.from = belongsToClass;
                fAssocation.to = s;
                fAssocation.type = "DeclarationReturnType";
                fAssocation.lineNumber = lineNumber;
                model.waitingAssociations.add(fAssocation);
            }
            //}
        }
    }

    @Override
    public void createAttribute(Boolean classScope, String accesControlQualifier,
            String belongsToClass, String declareType, String name, String uniqueName) {

        this.createAttribute(classScope, accesControlQualifier, belongsToClass, declareType, name, uniqueName, 0);
    }

    @Override
    public void createAttribute(Boolean classScope, String accesControlQualifier,
            String belongsToClass, String declareType, String name, String uniqueName,
            int line, List<String> declareTypes) {

        this.createAttribute(classScope, accesControlQualifier, belongsToClass, declareType, name, uniqueName, line);

        for (String type : declareTypes) {
            FamixAssociation fAssocation = new FamixAssociation();
            fAssocation.from = belongsToClass;
            fAssocation.to = type;
            fAssocation.type = "Declaration3";
            fAssocation.lineNumber = line;
            model.waitingAssociations.add(fAssocation);
        }

    }

    @Override
    public void createAttribute(Boolean classScope, String accesControlQualifier, String belongsToClass,
            String declareType, String name, String uniqueName, int line) {

        FamixAttribute famixAttribute = new FamixAttribute();
        famixAttribute.hasClassScope = classScope;
        famixAttribute.accessControlQualifier = accesControlQualifier;
        famixAttribute.belongsToClass = belongsToClass;
        famixAttribute.declareType = declareType;
        famixAttribute.name = name;
        famixAttribute.uniqueName = uniqueName;
        famixAttribute.lineNumber = line;
        model.waitingStructuralEntitys.add(famixAttribute);
        FamixAssociation fAssocation = new FamixAssociation();
        fAssocation.from = belongsToClass;
        fAssocation.to = declareType;
        fAssocation.type = "DeclarationInstanceVariable";
        fAssocation.lineNumber = line;
        model.waitingAssociations.add(fAssocation);
        addToModel(famixAttribute);
    }

    @Override
    public void createLocalVariable(String belongsToClass, String declareType, String name,
            String uniqueName, int lineNumber, String belongsToMethodString, List<String> declareTypes) {
        this.createLocalVariable(belongsToClass, declareType, name, uniqueName, lineNumber, belongsToMethodString);

        for (String type : declareTypes) {
            FamixAssociation fAssocation = new FamixAssociation();
            fAssocation.from = belongsToClass;
            fAssocation.to = type;
            fAssocation.type = "DeclarationLocalVariable";
            fAssocation.lineNumber = lineNumber;
            model.waitingAssociations.add(fAssocation);
        }
    }

    @Override
    public void createLocalVariable(String belongsToClass, String declareType, String name,
            String uniqueName, int lineNumber, String belongsToMethodString) {

        FamixLocalVariable famixLocalVariable = new FamixLocalVariable();
        famixLocalVariable.belongsToMethod = belongsToMethodString;
        famixLocalVariable.belongsToClass = belongsToClass;
        famixLocalVariable.declareType = declareType;
        famixLocalVariable.name = name;
        famixLocalVariable.uniqueName = uniqueName;
        famixLocalVariable.lineNumber = lineNumber;
        model.waitingStructuralEntitys.add(famixLocalVariable);
        addToModel(famixLocalVariable);
        FamixAssociation fAssocation = new FamixAssociation();

        fAssocation.from = belongsToClass;
        fAssocation.to = declareType;
        fAssocation.type = "DeclarationVariableWithinMethod";
        fAssocation.lineNumber = lineNumber;
        model.waitingAssociations.add(fAssocation);
    }

    @Override
    public void createParameter(String name, String uniqueName, String declareType, String belongsToClass,
            int lineNumber, String belongsToMethod) {
        List<String> emptyList = new ArrayList<String>();
        this.createParameter(name, uniqueName, declareType, belongsToClass, lineNumber, belongsToMethod, emptyList);
    }

    @Override
    public void createParameter(String name, String uniqueName,
            String declareType, String belongsToClass, int lineNumber,
            String belongsToMethod, List<String> declareTypes) {

        FamixFormalParameter famixParameter = new FamixFormalParameter();
        famixParameter.belongsToClass = belongsToClass;
        famixParameter.belongsToMethod = belongsToMethod;
        famixParameter.declareType = declareType;
        famixParameter.lineNumber = lineNumber;
        famixParameter.name = name;
        famixParameter.uniqueName = uniqueName;
        famixParameter.declaredTypes = declareTypes;
        addToModel(famixParameter);
        model.waitingStructuralEntitys.add(famixParameter);
        FamixAssociation fAssocation = new FamixAssociation();
        fAssocation.from = belongsToClass;
        fAssocation.to = declareType;
        fAssocation.type = "DeclarationParameter";
        fAssocation.lineNumber = lineNumber;
        model.waitingAssociations.add(fAssocation);

        for (String type : declareTypes) {
            FamixAssociation fParamAssocation = new FamixAssociation();
            fParamAssocation.from = belongsToClass;
            fParamAssocation.to = type;
            fParamAssocation.type = "Declaration6";
            fParamAssocation.lineNumber = lineNumber;
            model.waitingAssociations.add(fParamAssocation);
        }

    }

    @Override
    public void createAnnotation(String belongsToClass, String declareType, String name,
            String uniqueName, int linenumber) {

        FamixAttribute famixAttribute = new FamixAttribute();
        famixAttribute.hasClassScope = false;
        famixAttribute.accessControlQualifier = "public";
        famixAttribute.belongsToClass = belongsToClass;
        famixAttribute.declareType = declareType;
        famixAttribute.name = name;
        famixAttribute.uniqueName = uniqueName;
        addToModel(famixAttribute);
        model.waitingStructuralEntitys.add(famixAttribute);
        FamixAssociation fAssocation = new FamixAssociation();
        fAssocation.from = belongsToClass;
        fAssocation.to = declareType;
        fAssocation.type = "Annotation";
        fAssocation.lineNumber = linenumber;
        model.waitingAssociations.add(fAssocation);
    }

    @Override
    public void createException(String fromClass, String ExceptionClass, int lineNumber, String declarationType) {
        FamixException exception = new FamixException();
        exception.from = fromClass;
        exception.to = ExceptionClass;
        exception.lineNumber = lineNumber;
        exception.exceptionType = declarationType;
        model.waitingAssociations.add(exception);
    }

    @Override
    public void createInheritanceDefinition(String from, String to, int lineNumber) {
        FamixInheritanceDefinition famixInheritanceDefinition = new FamixInheritanceDefinition();
        famixInheritanceDefinition.from = from;
        famixInheritanceDefinition.to = to;
        famixInheritanceDefinition.lineNumber = lineNumber;
        model.waitingAssociations.add(famixInheritanceDefinition);
    }

    @Override
    public void createImplementsDefinition(String from, String to, int lineNumber) {
        FamixImplementationDefinition fImplements = new FamixImplementationDefinition();
        fImplements.from = from;
        fImplements.to = to;
        fImplements.lineNumber = lineNumber;
        model.waitingAssociations.add(fImplements);
    }

    @Override
    public void createPropertyOrFieldInvocation(String from, int lineNumber,
            String to, String invocationName, String nameOfInstance) {
        createPropertyOrFieldInvocation(from, to, lineNumber, invocationName, "", nameOfInstance);

    }

    @Override
    public void createMethodInvocation(String from, int lineNumber, String to,
            String invocationName, String nameOfInstance) {
        createMethodInvocation(from, to, lineNumber, invocationName, "", nameOfInstance);

    }

    @Override
    public void createConstructorInvocation(String from, int lineNumber,
            String to, String invocationName, String nameOfInstance) {
        createConstructorInvocation(from, to, lineNumber, invocationName, "", nameOfInstance);

    }

    @Override
    public void createConstructorInvocation(String from, String to, int lineNumber,
            String invocationName, String belongsToMethod, String nameOfInstance) {

        FamixInvocation famixInvocation = new FamixInvocation();
        famixInvocation.type = "InvocConstructor";
        famixInvocation.from = from;
        famixInvocation.lineNumber = lineNumber;
        famixInvocation.to = to;
        famixInvocation.invocationName = invocationName;
        famixInvocation.belongsToMethod = belongsToMethod;
        famixInvocation.nameOfInstance = nameOfInstance;
        model.waitingAssociations.add(famixInvocation);
    }

    @Override
    public void createMethodInvocation(String from, String to, int lineNumber,
            String invocationName, String belongsToMethod, String nameOfInstance) {

        FamixInvocation famixInvocation = new FamixInvocation();
        famixInvocation.type = "InvocMethod";
        famixInvocation.from = from;
        famixInvocation.lineNumber = lineNumber;
        famixInvocation.to = to;
        famixInvocation.invocationName = invocationName;
        famixInvocation.belongsToMethod = belongsToMethod;
        famixInvocation.nameOfInstance = nameOfInstance;
        model.waitingAssociations.add(famixInvocation);
    }

    @Override
    public void createPropertyOrFieldInvocation(String from, String to, int lineNumber,
            String invocationName, String belongsToMethod, String nameOfInstance) {

        FamixInvocation famixInvocation = new FamixInvocation();
        famixInvocation.type = "AccessPropertyOrField";
        famixInvocation.from = from;
        famixInvocation.lineNumber = lineNumber;
        famixInvocation.to = to;
        famixInvocation.invocationName = invocationName;
        famixInvocation.belongsToMethod = belongsToMethod;
        famixInvocation.nameOfInstance = nameOfInstance;
        model.waitingAssociations.add(famixInvocation);
    }

    @Override
    public void connectDependencies() {
        dependencyConnector.connectStructuralDependecies();
        dependencyConnector.connectAssociationDependencies();
    }

    private boolean addToModel(FamixObject newObject) {
        try {
            model.addObject(newObject);
            return true;
        } catch (InvalidAttributesException e) {
            return false;
        }
    }

    public String represent() {
        return model.toString();
    }

    @SuppressWarnings("unused")
    @Override
    public void clearMemoryFromObjectsNotUsedAnymore() {
        for (FamixStructuralEntity entity : model.structuralEntities.values()) {
            entity = null;
        }

    }
}
