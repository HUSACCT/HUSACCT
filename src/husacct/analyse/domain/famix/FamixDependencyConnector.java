package husacct.analyse.domain.famix;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.naming.directory.InvalidAttributesException;

import org.apache.log4j.Logger;

class FamixDependencyConnector {

	private static final String EXTENDS = "Extends";
	private static final String EXTENDS_LIBRARY = "ExtendsLibrary";
	private static final String EXTENDS_ABSTRACT = "ExtendsAbstract";
	private static final String EXTENDS_CONCRETE = "ExtendsConcrete";
	private static final String EXTENDS_INTERFACE = "ExtendsInterface";
	
	private FamixModel theModel;
	private Logger logger = Logger.getLogger(FamixDependencyConnector.class);
	
	public FamixDependencyConnector(){
		theModel = FamixModel.getInstance();
	}
	
	void connectStructuralDependecies() {
		for(FamixStructuralEntity entity : theModel.waitingStructuralEntitys){
			try{
				String theClass = entity.belongsToClass;
				if(!isCompleteTypeDeclaration(entity.declareType)){
					String classFoundInImports = findClassInImports(theClass, entity.declareType);
					if(!classFoundInImports.equals("")){
						entity.declareType = classFoundInImports;
					}else{
						String belongsToPackage = getPackageFromUniqueClassName(entity.belongsToClass);
						String to = findClassInPackage(entity.declareType, belongsToPackage);
						if(!to.equals("")){
							entity.declareType = to;
						}
					}
				}
				addToModel(entity);
			} 
			catch(Exception e){
				
			}
		}		
	}

	void connectAssociationDependencies() {
		for(FamixAssociation association : theModel.waitingAssociations){
			try{
				boolean connected = false;
				String theClass = association.from;
				if(!isCompleteTypeDeclaration(association.to)){
					String classFoundInImports = findClassInImports(theClass, association.to);
					if(!classFoundInImports.equals("")){
						association.to = classFoundInImports;
						connected = true;				
					} else {
						String belongsToPackage = getPackageFromUniqueClassName(association.from);
						String to = findClassInPackage(association.to, belongsToPackage);
						if(!to.equals("")){
							association.to = to;
							connected = true;
						}
					}					
					if(!connected){
						if(isInvocation(association)){
							FamixInvocation theInvocation = (FamixInvocation) association;
							if (theInvocation.belongsToMethod.equals("")){
								//Then it is an attribute
								theInvocation.to = getClassForAttribute (theInvocation.from, theInvocation.nameOfInstance);
							}
							else{
								//checking order now: 1) parameter, 2) localVariable, 3) attribute
								theInvocation.to = getClassForParameter(theInvocation.from, theInvocation.belongsToMethod, theInvocation.nameOfInstance);
								if (theInvocation.to.equals("")){
									//checking if it's a localVariable
									theInvocation.to = getClassForLocalVariable(theInvocation.from, theInvocation.belongsToMethod, theInvocation.nameOfInstance);
								}
								if(theInvocation.to.equals("")){
									//now it is an attribute
									theInvocation.to = getClassForAttribute (theInvocation.from, theInvocation.nameOfInstance);
								}
							}
						}
					}
				}				
				if(association.to.equals("") || association.to == null){
					logger.info("Couldn't analyse dependency from " + association.from + ". Reason: External Libraries not implemented yet");
//					logger.info("couldn't connect " + association.from + " to the right entity. Linenumber " + association.lineNumber + ".");
				} else {
					determineType(association);
					addToModel(association);
				}
			} catch(Exception e){
				
			}
		}		
	}	
		
	private void determineType(FamixAssociation association){
		String type = association.type;
		if(type.equals(EXTENDS)){
			FamixClass theClass = getClassForUniqueName(association.to);
			if(theClass != null){
				if(theClass.isAbstract) type = EXTENDS_ABSTRACT;
				else if(!theClass.isAbstract) type = EXTENDS_CONCRETE;
				else type = EXTENDS_LIBRARY;
			} else {
				FamixInterface theInterface = getInterfaceForUniqueName(association.to);
				if(theInterface != null){
					// Interface extends Interface
					type = EXTENDS_INTERFACE;
				}
			}
			
		}
		association.type = type;
	}
	
	private FamixClass getClassForUniqueName(String uniqueName){
		return theModel.classes.get(uniqueName);
	}
	
	private FamixInterface getInterfaceForUniqueName(String uniqueName){
		return theModel.interfaces.get(uniqueName);
	} 

	private String getClassForAttribute(String delcareClass, String attributeName){
		for(FamixAttribute famixAttribute: theModel.getAttributes()){
			if(famixAttribute.belongsToClass.equals(delcareClass)){
				if(famixAttribute.name.equals(attributeName)){
					return famixAttribute.declareType;
				}
			}
		}
		return "";
	}
	
	private String getClassForParameter(String declareClass, String declareMethod, String attributeName){
		for(FamixFormalParameter parameter: theModel.getParametersForClass(declareClass)){
			if(parameter.belongsToMethod.equals(declareMethod)){
					if(parameter.name.equals(attributeName)){
						return parameter.declareType;
					}
				}
			}		
		return "";
	}
	
	private String getClassForLocalVariable(String declareClass, String belongsToMethod, String nameOfInstance) {
		for(String s : theModel.structuralEntities.keySet()){
			if(s.startsWith(declareClass)){
				
				FamixStructuralEntity entity = (FamixStructuralEntity) theModel.structuralEntities.get(s);
				if (entity instanceof FamixLocalVariable){
					FamixLocalVariable variable = (FamixLocalVariable) entity;
					if(variable.belongsToMethod.equals(belongsToMethod)){
						if(variable.name.equals(nameOfInstance)){
							return variable.declareType;
						}
					}
				}
			}
		}
		return "";
	}
	
	private boolean isInvocation(FamixAssociation association){
		return association instanceof FamixInvocation;
	}

	private String findClassInImports(String importingClass, String typeDeclaration){
		List<FamixImport> imports = theModel.getImportsInClass(importingClass);
		for(FamixImport fImport: imports){
			if(!fImport.importsCompletePackage){
				if(fImport.to.endsWith(typeDeclaration)){
					return fImport.to;
				}
			}else{
				for(String uniqueClassName: getModulesInPackage(fImport.to)){
					if(uniqueClassName.endsWith(typeDeclaration)){
						return uniqueClassName;
					}
				}
			}
		}
		return "";
	}
	
	private boolean isCompleteTypeDeclaration(String typeDeclaration){
		return typeDeclaration.contains(".");
	}
	
	private String findClassInPackage(String className, String uniquePackageName){
		for(String uniqueName : getModulesInPackage(uniquePackageName)){
			if(uniqueName.endsWith(className)) return uniqueName;
		}
		return "";
	}
			
	private String getPackageFromUniqueClassName(String completeImportString) {		
		List<FamixClass> classes = theModel.getClasses();
		for (FamixClass fclass : classes){
			if (fclass.uniqueName.equals(completeImportString)){
				return fclass.belongsToPackage;
			}
		}
		
		FamixInterface f = theModel.interfaces.get(completeImportString);
		if(f != null){
			return f.belongsToPackage;
		}
		
		
		return "";
	}
	
	private List<String> getModulesInPackage(String packageUniqueName){
		List<String> result = new ArrayList<String>();
		Iterator<Entry<String, FamixClass>> classIterator = theModel.classes.entrySet().iterator();
		while(classIterator.hasNext()){
			Entry<String, FamixClass> entry = (Entry<String, FamixClass>)classIterator.next();
			FamixClass currentClass = entry.getValue();
			if(currentClass.belongsToPackage.equals(packageUniqueName)){
				result.add(currentClass.uniqueName);
			}
		}
		Iterator<Entry<String, FamixInterface>> interfaceIterator = theModel.interfaces.entrySet().iterator();
		while(interfaceIterator.hasNext()){
			Entry<String, FamixInterface> entry = (Entry<String, FamixInterface>)interfaceIterator.next();
			FamixInterface currentInterface = entry.getValue();
			if(currentInterface.belongsToPackage.equals(packageUniqueName)){
				result.add(currentInterface.uniqueName);
			}
		}
		return result;
	}
	
	private boolean addToModel(FamixObject newObject){		
		try {
			theModel.addObject(newObject);
			return true;
		} catch (InvalidAttributesException e) {
			return false;
		}
	}
}
