package husacct.analyse.domain.famix;

import husacct.analyse.domain.ModelCreationService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.naming.directory.InvalidAttributesException;

import org.apache.log4j.Logger;

public class FamixCreationServiceImpl implements ModelCreationService{

	private FamixModel model;
	private Logger logger;

	public FamixCreationServiceImpl(){
		model = FamixModel.getInstance();
		logger = Logger.getLogger(FamixCreationServiceImpl.class);
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
		connectStructuralEntityDependecies();
		connectAssociationDependencies();
	}

	private void connectStructuralEntityDependecies() {
		for (FamixStructuralEntity structuralEntity : model.waitingStructuralEntitys){
			try{
				String uniqueName = findUniquename(structuralEntity.belongsToClass, structuralEntity.declareType);
				structuralEntity.declareType = uniqueName;
				addToModel(structuralEntity);
			}catch(Exception e){
			}
		}

	}

	private void connectAssociationDependencies() {
		for(FamixAssociation association : model.waitingAssociations){
			try{

				String uniqueName = "";

				if(association instanceof FamixInvocation){
					if(association.to == null){
						logger.warn("to attribute is null, cant link to null; " + association.from + " -> " + association.to);
						continue;
					}

					if(uniqueName.equals("")){
						String uniqueNameImport = this.getUniquenameOfImport(association.from, association.to);
						if(!uniqueNameImport.equals("")){
							uniqueName = uniqueNameImport;
						}
					}

					if(uniqueName.equals("")){
						String uniqueNameOutOfAttribute = this.getUniquenameOutAttributes(association.from, association.to);
						if(!uniqueNameOutOfAttribute.equals("")){
							uniqueName = uniqueNameOutOfAttribute;
						}
					}

					if(uniqueName.equals("")){
						String uniqueNameSamePackage = this.getUniquenameOutSamePackage(association.from, association.to);
						if(!uniqueNameSamePackage.equals("")){
							uniqueName = uniqueNameSamePackage;
						}
					}

					if(uniqueName.equals("")){
						String uniqueNameReflection = this.getUniquenameByReflection(association.to);
						if(!uniqueNameReflection.equals("")){
							uniqueName = uniqueNameReflection;
						}
					}

					if(uniqueName.equals("")){
						System.out.println("geen assoc gevonden");
						logger.warn("Couldn't find association; " + association.from + " -> " + association.to);
						uniqueName = association.to;
					}

					((FamixInvocation) association).to = uniqueName;
					((FamixInvocation) association).nameOfInstance = this.getClassOfUniqueName(uniqueName);

				} else {
					uniqueName = findUniquename(association.from, association.to);
					association.to = uniqueName;
				}
				addToModel(association);
			} catch(Exception e){
				e.printStackTrace();
			}
		}		
	}	

	private String findUniquename(String currentClass, String searchType){
		if((searchType).indexOf(".") != -1){
			return currentClass;
		}

		String uniqueName = "";
		uniqueName = this.getUniquenameOfImport(currentClass, searchType);
		uniqueName = uniqueName.equals("") ? this.getUniquenameOutSamePackage(currentClass, searchType) : uniqueName;
		uniqueName = uniqueName.equals("") ? this.getUniquenameByReflection(searchType) : uniqueName;
		return uniqueName.equals("") ? searchType : uniqueName;
	}

	private String getUniquenameOfImport(String belongToClass, String type){
		List<FamixImport> importsInClass = model.getImportsInClass(belongToClass);
		for(FamixImport currentImport : importsInClass){
			if(currentImport.completeImportString.contains("*")){
				String lookPackage = currentImport.importedModule.replace(".*", "");
				for(String uniqueClassName: getClassesOrInterfacesInPackage(lookPackage)){
					String className = getClassOfUniqueName(uniqueClassName);
					if(className.equals(type)){
						return uniqueClassName;
					}
				}
			} else {
				String typeName = this.getClassOfUniqueName(currentImport.completeImportString);
				if(typeName.equals(type)){
					return currentImport.completeImportString;
				}
			}
		}
		return "";
	}

	private String getUniquenameOutSamePackage(String belongToClass, String type){
		String currentPackage = belongToClass.substring(0, belongToClass.lastIndexOf("."));
		String searchFor = currentPackage + "." + type;

		FamixEntity uniqueClassObject = model.classes.get(searchFor);
		uniqueClassObject = uniqueClassObject == null ? model.interfaces.get(searchFor) : uniqueClassObject;

		if(uniqueClassObject != null){
			return uniqueClassObject.uniqueName;
		}

		return "";
	}

	private String getUniquenameByReflection(String type){
		Package[] packagesloaded = Package.getPackages();
		for(Package p : packagesloaded){
			String packageName = p.getName();
			String newType = "";
			if (type != null && type.length() > 0) {
				char[] typeCharArray = type.toCharArray();
				typeCharArray[0] = Character.toUpperCase(typeCharArray[0]);
				newType = new String(typeCharArray);
			}
			String predictUniquename;
			if(!newType.equals("")) {
				predictUniquename = packageName + "." + newType;
			}
			else {
				predictUniquename = packageName + "." + type;
			}
			try {	
				Class.forName(predictUniquename);
				return predictUniquename;
			} catch (ClassNotFoundException e) {
				predictUniquename = packageName + "." + type;
				try {	
					Class.forName(predictUniquename);
					return predictUniquename;
				} catch (ClassNotFoundException e2) {
					
				}
				//TODO Logger
			}
		}
		return "";
	}

	private String getUniquenameOutAttributes(String from, String to){
		for (FamixAttribute attribute : model.getAttributes()){
			if (attribute.belongsToClass.equals(from)){
				if (attribute.name.equals(to)){
					return attribute.declareType;
				}
			}
		}
		return "";
	}

	private String getPackageFromUniqueClassName(String completeImportString) {
		List<FamixClass> classes = model.getClasses();
		for (FamixClass fclass : classes){
			if (fclass.uniqueName.equals(completeImportString)){
				return fclass.belongsToPackage;
			}
		}
		return "";
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
			e.printStackTrace();
			return false;
		}
	}

	public String represent(){
		return model.toString();
	}

}
