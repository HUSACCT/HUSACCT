package husacct.analyse.domain.famix;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import javax.naming.directory.InvalidAttributesException;
import husacct.analyse.domain.ModelCreationService;

public class FamixCreationServiceImpl implements ModelCreationService{
	
	private FamixModel model;
	
	public FamixCreationServiceImpl(){
		model = FamixModel.getInstance();
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
		connectStructuralEntityDependecies();
		connectAssociationDependencies();
	}
	
	private void connectStructuralEntityDependecies() {
		for (FamixStructuralEntity structuralEntity : model.waitingStructuralEntitys){
			boolean found = false;
			if(!structuralEntity.declareType.contains("\\.")){
				FamixClass theClass = getClassForUniqueName(structuralEntity.belongsToClass);
				String thePackage = theClass.belongsToPackage;
				List<FamixImport> importsInClass = model.getImportsInClass(theClass.uniqueName);
				for(FamixImport fImport: importsInClass){
					String importString = fImport.completeImportString;
					if(fImport.importsCompletePackage){
						for(String uniqueClassName: getClassesOrInterfacesInPackage(fImport.importedModule)){
							if(getClassOfUniqueName(uniqueClassName).equals(structuralEntity.declareType)){
								structuralEntity.declareType = uniqueClassName;
								found = true;
							}
						}
						for(String uniqueClassName: getClassesOrInterfacesInPackage(importString.substring(0, importString.length() -2))){
							if(getClassOfUniqueName(uniqueClassName).equals(structuralEntity.declareType)){
								structuralEntity.declareType = uniqueClassName;
								found = true;
							}
						}
					}else{
						List<String> classesInPackage = getClassesOrInterfacesInPackage(fImport.completeImportString);
						for(String uniqueClassName: classesInPackage){
							if(getClassOfUniqueName(uniqueClassName).equals(structuralEntity.declareType)){
								structuralEntity.declareType = uniqueClassName;
								found = true;
							}
						}
					}
				}
				if(!found){
					List<String> classesInPackage = getClassesOrInterfacesInPackage(thePackage);
					for(String uniqueClassName: classesInPackage){
						if(getClassOfUniqueName(uniqueClassName).equals(structuralEntity.declareType)){
							structuralEntity.declareType = uniqueClassName;
						}
					}
				}
			}
			addToModel(structuralEntity);
		}
		
	}
	
	private void connectAssociationDependencies() {
		for(FamixAssociation association: model.waitingAssociations){
			boolean found = false;
			
			//voor de FamixAttribute(MethodInvocation).TO moeten we uit de famixAttribute de naam van de klasse van het gedeclareerde object halen 
			//ipv de zelfgekozen naam voor het object te gebruiken binnen de klasse.
			//bijv: User jaap = new User()
			//jaap.doSomething() <-- 'invocMethod' jaap moet geconverteerd worden naar User
			//TODO translate above comment to english...?
			
			if(association instanceof FamixInvocation){
				if (((FamixInvocation) association).invocationType.equals("invocMethod") || ((FamixInvocation) association).invocationType.equals("accessPropertyOrField")){
					for (FamixAttribute attribute : model.getAttributes()){
						if (attribute.belongsToClass.equals(association.from)){
							if (attribute.name.equals(association.to)){
								association.to = attribute.declareType;
								((FamixInvocation) association).nameOfInstance = attribute.name;

							}
						}
					}
				}
				else {
					for (FamixAttribute attribute : model.getAttributes()){
						if (attribute.belongsToClass.equals(association.from)){
							if (getClassForUniqueName(attribute.declareType).name.equals(association.to)){
								((FamixInvocation) association).nameOfInstance = attribute.name;
							}
						}
					}
				}
			}
			if(!association.to.contains("\\.")){
				FamixClass theClass = getClassForUniqueName(association.from);
				String thePackage = theClass.belongsToPackage;
				List<FamixImport> importsInClass = model.getImportsInClass(theClass.uniqueName);
				for(FamixImport fImport: importsInClass){
					String importString = fImport.completeImportString;
					if(fImport.importsCompletePackage){
						for(String uniqueClassName: getClassesOrInterfacesInPackage(fImport.importedModule)){
							if(getClassOfUniqueName(uniqueClassName).equals(association.to)){
								association.to = uniqueClassName;
								found = true;
							}
						}
						for(String uniqueClassName: getClassesOrInterfacesInPackage(importString.substring(0, importString.length() -2))){
							if(getClassOfUniqueName(uniqueClassName).equals(association.to)){
								association.to = uniqueClassName;
								found = true;
							}
						}
					}else{
						//erik's code
						if(importString.endsWith(association.to)){
							association.to = importString;
							found = true;
						}
					}
				}
				if(!found){
					List<String> classesInPackage = getClassesOrInterfacesInPackage(thePackage);
					for(String uniqueClassName: classesInPackage){
						if(getClassOfUniqueName(uniqueClassName).equals(association.to)){
							association.to = uniqueClassName;
						}
					}
				}
			}
			addToModel(association);
		}
		
	}
	
	private String getClassOfUniqueName(String uniqueName){
		String[] parts = uniqueName.split("\\.");
		return parts[parts.length -1];
	}
	
	private List<String> getClassesOrInterfacesInPackage(String packageUniqueName){
		List<String> result = new ArrayList<String>();
		Iterator<Entry<String, FamixClass>> classIterator = model.classes.entrySet().iterator();
		while(classIterator.hasNext()){
			Entry<String, FamixClass> entry = (Entry<String, FamixClass>)classIterator.next();
			FamixClass currentClass = entry.getValue();
			if(currentClass.belongsToPackage.equals(packageUniqueName)){
				result.add(currentClass.uniqueName);
			}
		}
		Iterator<Entry<String, FamixInterface>> interfaceIterator = model.interfaces.entrySet().iterator();
		while(interfaceIterator.hasNext()){
			Entry<String, FamixInterface> entry = (Entry<String, FamixInterface>)interfaceIterator.next();
			FamixInterface currentInterface = entry.getValue();
			if(currentInterface.belongsToPackage.equals(packageUniqueName)){
				result.add(currentInterface.uniqueName);
			}
		}
		return result;
	}
	
	private FamixClass getClassForUniqueName(String uniqueName){
		Iterator<Entry<String, FamixClass>> classIterator = model.classes.entrySet().iterator();
		while(classIterator.hasNext()){
			Entry<String, FamixClass> entry = (Entry<String, FamixClass>)classIterator.next();
			FamixClass currentClass = entry.getValue();
			if(currentClass.uniqueName.equals(uniqueName)){
				return currentClass;
			}
		}
		return null;
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
