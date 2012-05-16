package husacct.analyse.domain.famix;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.naming.directory.InvalidAttributesException;

class FamixDependencyConnector {

	private FamixModel theModel;
	
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
				String theClass = association.from;
				if(!isCompleteTypeDeclaration(association.to)){
					String classFoundInImports = findClassInImports(theClass, association.to);
					if(!classFoundInImports.equals("")){
						association.to = classFoundInImports;
					}else{
						String belongsToPackage = getPackageFromUniqueClassName(association.from);
						String to = findClassInPackage(association.to, belongsToPackage);
						if(!to.equals("")){
							association.to = to;
						}
					}
				}
				addToModel(association);
			} catch(Exception e){
				
			}
		}		
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
