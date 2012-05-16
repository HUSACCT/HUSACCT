package husacct.analyse.domain.famix;

import javax.naming.directory.InvalidAttributesException;
import husacct.analyse.domain.ModelCreationService;

public class FamixCreationServiceImpl implements ModelCreationService{
	
	private FamixModel model;
	private FamixDependencyConnector dependencyConnector;
	
	public FamixCreationServiceImpl(){
		model = FamixModel.getInstance();
		dependencyConnector = new FamixDependencyConnector();
	}
	
	@Override
	public void clearModel() {
		model.clear();
	}
	
	public void createPackage(String uniqueName, String belongsToPackage, String name){
		FamixPackage fPackage = new FamixPackage();
		fPackage.uniqueName = uniqueName;
		fPackage.belongsToPackage = belongsToPackage;
		fPackage.name = name;
		addToModel(fPackage);
	}
	
	@Override
	public void createClass(String uniqueName, String name, String belongsToPackage, boolean isAbstract, boolean isInnerClass) {
		FamixClass fClass = new FamixClass();
		fClass.uniqueName = uniqueName;
		fClass.isAbstract = isAbstract;
		fClass.belongsToPackage = belongsToPackage;
		fClass.isInnerClass = isInnerClass;
		fClass.name = name;
		addToModel(fClass);
	}

	@Override
	public void createClass(String uniqueName, String name, String belongsToPackage, boolean isAbstract, boolean isInnerClass, String belongsToClass) {
		FamixClass fClass = new FamixClass();
		fClass.uniqueName = uniqueName;
		fClass.isAbstract = isAbstract;
		fClass.belongsToPackage = belongsToPackage;
		fClass.isInnerClass = isInnerClass;
		fClass.name = name;
		fClass.belongsToClass = belongsToClass;
		addToModel(fClass);
	}
	
	@Override
	public void createInterface(String uniqueName, String name, String belongsToPackage) {
		FamixInterface fInterface = new FamixInterface();
		fInterface.uniqueName = uniqueName;
		fInterface.name = name;
		fInterface.belongsToPackage = belongsToPackage;
		addToModel(fInterface);
	}

	@Override
	public void createImport(String importingClass, String importedModule, int lineNumber, String completeImportString, boolean importsCompletePackage) {
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
	public void createMethod(String name, String uniqueName, String accessControlQualifier, String signature, boolean isPureAccessor, String declaredReturnType,
			String belongsToClass, boolean isConstructor, boolean isAbstract, boolean hasClassScope) {
		
		FamixMethod famixMethod = new FamixMethod();
		famixMethod.name = name;
		famixMethod.uniqueName = uniqueName;
		famixMethod.accessControlQualifier = accessControlQualifier;
		famixMethod.signature = signature;
		famixMethod.isPureAccessor = isPureAccessor;
		famixMethod.declaredReturnType = declaredReturnType;
		famixMethod.belongsToClass = belongsToClass;
		famixMethod.isConstructor = isConstructor;
		famixMethod.isAbstract = isAbstract;
		famixMethod.hasClassScope = hasClassScope;
		addToModel(famixMethod);
	}
	
	@Override
	public void createAttribute(Boolean classScope, String accesControlQualifier, String belongsToClass, String declareType, String name, String uniqueName) {
		FamixAttribute famixAttribute = new FamixAttribute();
		famixAttribute.hasClassScope = classScope;
		famixAttribute.accessControlQualifier = accesControlQualifier;
		famixAttribute.belongsToClass = belongsToClass;
		famixAttribute.declareType = declareType;
		famixAttribute.name = name;
		famixAttribute.uniqueName = uniqueName;
		model.waitingStructuralEntitys.add(famixAttribute);
		addToModel(famixAttribute);
		FamixAssociation fAssocation = new FamixAssociation();
		fAssocation.from = belongsToClass;
		fAssocation.to = declareType;
		fAssocation.type = "declaration";
		fAssocation.lineNumber = 0;
		model.waitingAssociations.add(fAssocation);
	}
	
	@Override
	public void createAttribute(Boolean classScope, String accesControlQualifier, String belongsToClass, String declareType, String name, String uniqueName, int line) {
		FamixAttribute famixAttribute = new FamixAttribute();
		famixAttribute.hasClassScope = classScope;
		famixAttribute.accessControlQualifier = accesControlQualifier;
		famixAttribute.belongsToClass = belongsToClass;
		famixAttribute.declareType = declareType;
		famixAttribute.name = name;
		famixAttribute.uniqueName = uniqueName;
		addToModel(famixAttribute);
		model.waitingStructuralEntitys.add(famixAttribute);
		FamixAssociation fAssocation = new FamixAssociation();
		fAssocation.from = belongsToClass;
		fAssocation.to = declareType;
		fAssocation.type = "declaration";
		fAssocation.lineNumber = line;
		model.waitingAssociations.add(fAssocation);
	}
	
	@Override
	public void createLocalVariable(String belongsToMethodString,
			String belongsToClass, String declareType, String name,
			String uniqueName, int lineNumber) {

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
		fAssocation.type = "declaration";
		fAssocation.lineNumber = lineNumber;
		model.waitingAssociations.add(fAssocation);
	}
	
	@Override
	public void createParameter(String name, String uniqueName,
			String declareType, String belongsToClass, int lineNumber,
			String belongsToMethod, String declareTypes) {
		
		FamixFormalParameter famixParameter = new FamixFormalParameter();
		famixParameter.belongsToClass = belongsToClass;
		famixParameter.belongsToMethod = belongsToMethod;
		famixParameter.declareType = declareType;
		famixParameter.lineNumber = lineNumber;
		famixParameter.name = name;
		famixParameter.uniqueName = uniqueName;
		addToModel(famixParameter);
		model.waitingStructuralEntitys.add(famixParameter);
		FamixAssociation fAssocation = new FamixAssociation();
		fAssocation.from = belongsToClass;
		fAssocation.to = declareType;
		fAssocation.type = "declaration";
		fAssocation.lineNumber = lineNumber;
		model.waitingAssociations.add(fAssocation);
	}
	
	@Override
	public void createAnnotation(String belongsToClass, String declareType, String name, String uniqueName, int linenumber) {
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
		fAssocation.type = "annotation";
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
	public void createConstructorInvocation(String type, String from, String to, int lineNumber, String invocationName) {
		FamixInvocation famixInvocation = new FamixInvocation();
		famixInvocation.type = "invocConstructor";
		famixInvocation.from = from;
		famixInvocation.invocationType = type;
		famixInvocation.lineNumber = lineNumber;
		famixInvocation.to = to;
		famixInvocation.inovcationName = invocationName;
		model.waitingAssociations.add(famixInvocation);
	}
	
	@Override
	public void createMethodInvocation(String type, String from, String to, int lineNumber, String invocationName) {
		FamixInvocation famixInvocation = new FamixInvocation();
		famixInvocation.type = "invocMethod";
		famixInvocation.from = from;
		famixInvocation.invocationType = type;
		famixInvocation.lineNumber = lineNumber;
		famixInvocation.to = to;
		famixInvocation.inovcationName = invocationName;
		model.waitingAssociations.add(famixInvocation);
		
	}

	@Override
	public void createPropertyOrFieldInvocation(String type, String from, String to, int lineNumber, String invocationName) {
		FamixInvocation famixInvocation = new FamixInvocation();
		famixInvocation.type = "accessPropertyOrField";
		famixInvocation.from = from;
		famixInvocation.invocationType = type;
		famixInvocation.lineNumber = lineNumber;
		famixInvocation.to = to;
		famixInvocation.inovcationName = invocationName;
		model.waitingAssociations.add(famixInvocation);
	}
	
	@Override
	public void connectDependencies(){
		dependencyConnector.connectStructuralDependecies();
		dependencyConnector.connectAssociationDependencies();
	}
		
	private boolean addToModel(FamixObject newObject){
		try {
			model.addObject(newObject);
			return true;
		} catch (InvalidAttributesException e) {
			return false;
		}
	}
	
	public String represent(){
		return model.toString();
	}
}
