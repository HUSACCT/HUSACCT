package husacct.analyse.domain.famix;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.naming.directory.InvalidAttributesException;

class FamixModel extends FamixObject{

	private static FamixModel currentInstance;

	public List<FamixStructuralEntity> waitingStructuralEntitys;
	public List<FamixAssociation> waitingAssociations;
	
	public HashMap<String, FamixBehaviouralEntity> behaviouralEntities;
	public HashMap<String, FamixStructuralEntity> structuralEntities;
	public HashMap<String, FamixPackage> packages;
	public HashMap<String, FamixClass> classes;
	public HashMap<String, FamixInterface> interfaces;
	public ArrayList<FamixAssociation> associations;
	
	public String exporterName;
	public String exporterVersion;
	public String exporterDate;
	public String exporterTime;
	public String publisherName;
	public String parsedSystemName;
	public String extractionLevel;
	public String sourceLanguage;
	public String sourceDialect;
	
	private FamixModel(){
		this.exporterDate = new Date().toString();
		waitingAssociations = new ArrayList<FamixAssociation>();
		waitingStructuralEntitys = new ArrayList<FamixStructuralEntity>();
		
		associations = new ArrayList<FamixAssociation>();
		classes = new HashMap<String, FamixClass>();
		packages = new HashMap<String, FamixPackage>();
		interfaces = new HashMap<String, FamixInterface>();
		structuralEntities = new HashMap<String, FamixStructuralEntity>();
		behaviouralEntities = new HashMap<String, FamixBehaviouralEntity>();
	}
	
	public static FamixModel getInstance(){
		if(currentInstance == null) currentInstance = new FamixModel(); 
		return currentInstance;
	}
	
	public void clearModel(){
		currentInstance = new FamixModel();
	}

	public void addObject(Object e) throws InvalidAttributesException{
		if (e instanceof FamixEntity){
			if (e instanceof FamixBehaviouralEntity){
				behaviouralEntities.put(((FamixEntity) e).uniqueName, (FamixBehaviouralEntity) e);
			}
			else if (e instanceof FamixStructuralEntity){
				structuralEntities.put(((FamixEntity) e).uniqueName, (FamixStructuralEntity) e);
			}
			else if (e instanceof FamixPackage){
				packages.put(((FamixEntity) e).uniqueName, (FamixPackage) e);
			}
			else if (e instanceof FamixClass){
				classes.put(((FamixEntity) e).uniqueName, (FamixClass) e);
			}
			else if (e instanceof FamixInterface){
				interfaces.put(((FamixEntity) e).uniqueName, (FamixInterface) e);
			}
		}
		else if (e instanceof FamixAssociation){
			associations.add((FamixAssociation) e);
		}
		else{
			throw new InvalidAttributesException("Wrongtype (not of type entity or association) ");
		}
	}

	public ArrayList<FamixAssociation> getAssociations(){
		return associations;
	}
	
	public List<FamixClass> getClasses() {
		ArrayList<FamixClass> result = new ArrayList<FamixClass>();
		for (FamixClass fclass : classes.values()){
			result.add(fclass);
		}
		return result;
	}
	
	public ArrayList<FamixAttribute> getAttributes(){
		ArrayList<FamixAttribute> result = new ArrayList<FamixAttribute>();
		for (FamixStructuralEntity entity: structuralEntities.values()){
			if (entity instanceof FamixAttribute){
				result.add((FamixAttribute) entity);
			}
		}
		return result;
	}

	public ArrayList<FamixInvocation> getInvocations(){
		ArrayList<FamixInvocation> result = new ArrayList<FamixInvocation>();
		for (FamixAssociation association : associations){
			if (association instanceof FamixInvocation) 
				result.add((FamixInvocation) association);
		}
		return result;
	}
	
	public ArrayList<FamixFormalParameter> getParametersForClass(String uniqueClassName){
		ArrayList<FamixFormalParameter> result = new ArrayList<FamixFormalParameter>();
		for (FamixStructuralEntity entity: structuralEntities.values()){
			if (entity instanceof FamixFormalParameter){
				FamixFormalParameter parameter = (FamixFormalParameter)entity;
				if(parameter.belongsToClass.equals(uniqueClassName)){
					result.add(parameter);
				}
			}
		}
		return result;
	}
	
	public ArrayList<FamixLocalVariable> getLocalVariablesForClass(String declareClass) {
		ArrayList<FamixLocalVariable> localVariables = new ArrayList<FamixLocalVariable>();
		for (FamixStructuralEntity entity: structuralEntities.values()){
			if (entity instanceof FamixLocalVariable){
				FamixLocalVariable variable = (FamixLocalVariable) entity;
				if (variable.belongsToClass.equals(declareClass)){
					localVariables.add(variable);
				}
			}
		}
		return localVariables;
	}
	
	public List<FamixImport> getImportsInClass(String uniqueClassName){
		List<FamixImport> imports = new ArrayList<FamixImport>();
		for(FamixAssociation association: associations){
			if(association instanceof FamixImport){
				FamixImport theImport = (FamixImport)association;
				if(theImport.from.equals(uniqueClassName)){
					imports.add((FamixImport)association);
				}
			}
		}
		return imports;
	}

	public FamixStructuralEntity getTypeForVariable(String uniqueVarName) throws Exception{
		String temp = uniqueVarName;
		String[] splitted = temp.split("\\.");
		for (int i = splitted.length; i > 1; i--){
			if (structuralEntities.containsKey(temp)) return structuralEntities.get(temp);
			// Is it an instance variable?
			temp = splitted[0] + "." + splitted[splitted.length - 1];
		}
		// If not, throw new exception
		throw new Exception("The unit (or a part of it) '" + temp + " or " + uniqueVarName + "' is not found or defined.");
	}
	
	public String toString()	{
		return 
				"\n ------------Packages------------- \n" + packages
				+ "\n ------------Classes------------- \n" + classes
				+ "\n ------------Interfaces------------\n" + interfaces
				+ "\n -----------Assocations:-------------- \n" + associations
				+ "\n --------------Methoden (behavioural entities) ----------- \n" + behaviouralEntities
				+ "\n --------------Variabelen (structural entities) ----------- \n" + structuralEntities;
//				+ "\n -----------Invocations-------------- \n" + associations + "num invocs " + associations.size();

	}
	

	public void clear() {
		currentInstance.waitingAssociations.clear();
		currentInstance.waitingStructuralEntitys.clear();
		currentInstance.associations.clear();
		currentInstance.classes.clear();
		currentInstance.packages.clear();
		currentInstance.structuralEntities.clear();
		currentInstance.behaviouralEntities.clear();
	}


}
