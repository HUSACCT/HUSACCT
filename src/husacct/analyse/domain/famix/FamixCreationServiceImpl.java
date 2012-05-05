package husacct.analyse.domain.famix;

import java.util.ArrayList;
import java.util.HashMap;
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
		addToModel(famixAttribute);
	}
	
	@Override
	public void createException(String fromClass, String ExceptionClass, int lineNumber, String declarationType) {
		FamixException exception = new FamixException();
		exception.from = fromClass;
		exception.to = ExceptionClass;
		exception.lineNumber = lineNumber;
		exception.exceptionType = declarationType;
		addToModel(exception);
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
	public void connectDependencies(){
		for(FamixAssociation assocation: model.waitingAssociations){
			boolean found = false;
			if(!assocation.to.contains("\\.")){
				FamixClass theClass = getClassForUniqueName(assocation.from);
				String thePackage = theClass.belongsToPackage;
				List<FamixImport> importsInClass = model.getImportsInClass(theClass.uniqueName);
				for(FamixImport fImport: importsInClass){
					String importString = fImport.completeImportString;
					if(fImport.importsCompletePackage){
						for(String uniqueClassName: getClassesOrInterfacesInPackage(fImport.importedModule)){
							if(getClassOfUniqueName(uniqueClassName).equals(assocation.to)){
								assocation.to = uniqueClassName;
								found = true;
							}
						}
						for(String uniqueClassName: getClassesOrInterfacesInPackage(importString.substring(0, importString.length() -2))){
							if(getClassOfUniqueName(uniqueClassName).equals(assocation.to)){
								assocation.to = uniqueClassName;
								found = true;
							}
						}
					}else{
						if(importString.endsWith(assocation.to)){
							assocation.to = importString;
							found = true;
						}
					}
				}
				if(!found){
					List<String> classesInPackage = getClassesOrInterfacesInPackage(thePackage);
					for(String uniqueClassName: classesInPackage){
						if(getClassOfUniqueName(uniqueClassName).equals(assocation.to)){
							assocation.to = uniqueClassName;
						}
					}
				}
			}
			addToModel(assocation);
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
