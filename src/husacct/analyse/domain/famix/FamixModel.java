package husacct.analyse.domain.famix;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.List;

import javax.naming.directory.InvalidAttributesException;

import org.apache.log4j.Logger;

class FamixModel extends FamixObject {

    private static FamixModel currentInstance;
    private final Logger logger = Logger.getLogger(FamixModel.class);
    public List<FamixStructuralEntity> waitingStructuralEntitys;
    public List<FamixAssociation> waitingAssociations;
    public HashMap<String, FamixBehaviouralEntity> behaviouralEntities;
    public HashMap<String, FamixStructuralEntity> structuralEntities;
    //public HashMap<String, FamixPackage> packages;
    public TreeMap<String, FamixPackage> packages;
    //public HashMap<String, FamixClass> classes;
    public HashMap<String, FamixClass> classes;
    public HashMap<String, FamixImport> imports;
    public HashMap<String, FamixLibrary> libraries;
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

    private FamixModel() {
        this.exporterDate = new Date().toString();
        waitingAssociations = new ArrayList<FamixAssociation>();
        waitingStructuralEntitys = new ArrayList<FamixStructuralEntity>();
        associations = new ArrayList<FamixAssociation>();
        //classes = new HashMap<String, FamixClass>();
        classes = new HashMap<String, FamixClass>();
        //packages = new HashMap<String, FamixPackage>();
        packages = new TreeMap<String, FamixPackage>();
        imports = new HashMap<String, FamixImport>();
        libraries = new HashMap<String, FamixLibrary>();
        structuralEntities = new HashMap<String, FamixStructuralEntity>();
        behaviouralEntities = new HashMap<String, FamixBehaviouralEntity>();
    }

    public static FamixModel getInstance() {
        if (currentInstance == null) {
            currentInstance = new FamixModel();
        }
        return currentInstance;
    }

    public void clearModel() {
        currentInstance = new FamixModel();
    }

    public void addObject(FamixObject e) throws InvalidAttributesException {
        try{
    	if (e instanceof FamixEntity) {
        	// Test Utility
    		//if (((FamixEntity) e).uniqueName.contains("org.dtangler.swingui.aboutinfodisplayer.impl.AboutInfoView.Actions")) {
        	//	String test = "breakpoint"; }
            if (e instanceof FamixBehaviouralEntity) {
                behaviouralEntities.put(((FamixEntity) e).uniqueName, (FamixBehaviouralEntity) e);
            } else if (e instanceof FamixStructuralEntity) {
                structuralEntities.put(((FamixEntity) e).uniqueName, (FamixStructuralEntity) e);
            } else if (e instanceof FamixPackage) {
                if (!packages.containsKey(((FamixEntity) e).uniqueName)){
	            	packages.put(((FamixEntity) e).uniqueName, (FamixPackage) e);
	                String parentUniqueName = ((FamixPackage) e).belongsToPackage;
	                FamixPackage parent = null;
	                if (!parentUniqueName.equals("") && (packages.containsKey(parentUniqueName))){
	                	parent = packages.get(parentUniqueName);
	                	parent.children.add(((FamixEntity) e).uniqueName);
	                }
                }
            } else if (e instanceof FamixClass) {
                if (!classes.containsKey(((FamixEntity) e).uniqueName)){
	            	classes.put(((FamixEntity) e).uniqueName, (FamixClass) e);
	                String parentUniqueName;
	                if (((FamixClass) e).isInnerClass) {
	                	parentUniqueName = ((FamixClass) e).belongsToClass;
	            		if ((!parentUniqueName.equals("")) && (classes.containsKey(parentUniqueName))){
	            			FamixClass parent = classes.get(parentUniqueName);
	            			parent.hasInnerClasses = true;
	            			parent.children.add(((FamixEntity) e).uniqueName);
	            		}
	                } else {
	                	parentUniqueName = ((FamixClass) e).belongsToPackage;
		                FamixPackage parent = null;
		                if (!parentUniqueName.equals("") && (packages.containsKey(parentUniqueName))){
		                	parent = packages.get(parentUniqueName);
		                	parent.children.add(((FamixEntity) e).uniqueName);
		                }
	                }
                }
            } else if (e instanceof FamixLibrary) {
                if (!libraries.containsKey(((FamixEntity) e).uniqueName)){
                	libraries.put(((FamixEntity) e).uniqueName, (FamixLibrary) e);
	                String parentUniqueName = ((FamixLibrary) e).belongsToPackage;
	                ((FamixLibrary) e).external = true;
	                FamixPackage parent = null;
	                if (!parentUniqueName.equals("") && (packages.containsKey(parentUniqueName))){
	                	parent = packages.get(parentUniqueName);
	                	parent.children.add(((FamixEntity) e).uniqueName);
	                }
                }
            }
        } else if (e instanceof FamixAssociation){
			if(e instanceof FamixImport){
				String importKey = ((FamixImport)e).importedModule + "." +((FamixImport)e).importingClass;
				imports.put(importKey, (FamixImport) e);
			}
			associations.add((FamixAssociation) e);
		} else {
            throw new InvalidAttributesException("Wrongtype (not of type entity or association) ");
        }
        }catch(Exception e1) {
        	this.logger.error(new Date().toString() + " Exception while adding:  " + e.toString());
	        e1.printStackTrace();
        }
    }

    public ArrayList<FamixAssociation> getAssociations() {
        return associations;
    }

    public List<FamixClass> getClasses() {
        ArrayList<FamixClass> result = new ArrayList<FamixClass>();
        for (FamixClass fclass : classes.values()) {
            result.add(fclass);
        }
        return result;
    }

    public ArrayList<FamixAttribute> getAttributes() {
        ArrayList<FamixAttribute> result = new ArrayList<FamixAttribute>();
        for (FamixStructuralEntity entity : structuralEntities.values()) {
            if (entity instanceof FamixAttribute) {
                result.add((FamixAttribute) entity);
            }
        }
        return result;
    }

    public ArrayList<FamixInvocation> getInvocations() {
        ArrayList<FamixInvocation> result = new ArrayList<FamixInvocation>();
        for (FamixAssociation association : associations) {
            if (association instanceof FamixInvocation) {
                result.add((FamixInvocation) association);
            }
        }
        return result;
    }

    public ArrayList<FamixFormalParameter> getParametersForClass(String uniqueClassName) {
        ArrayList<FamixFormalParameter> result = new ArrayList<FamixFormalParameter>();
        for (FamixStructuralEntity entity : structuralEntities.values()) {
            if (entity instanceof FamixFormalParameter) {
                FamixFormalParameter parameter = (FamixFormalParameter) entity;
                if (parameter.belongsToClass.equals(uniqueClassName)) {
                    result.add(parameter);
                }
            }
        }
        return result;
    }

    public ArrayList<FamixLocalVariable> getLocalVariablesForClass(String declareClass) {
        ArrayList<FamixLocalVariable> localVariables = new ArrayList<FamixLocalVariable>();
        for (FamixStructuralEntity entity : structuralEntities.values()) {
            if (entity instanceof FamixLocalVariable) {
                FamixLocalVariable variable = (FamixLocalVariable) entity;
                if (variable.belongsToClass.equals(declareClass)) {
                    localVariables.add(variable);
                }
            }
        }
        return localVariables;
    }

    public List<FamixImport> getImportsInClass(String uniqueClassName) {
        List<FamixImport> imports = new ArrayList<FamixImport>();
        for (FamixAssociation association : associations) {
            if (association instanceof FamixImport) {
                FamixImport theImport = (FamixImport) association;
                if (theImport.from.equals(uniqueClassName)) {
                    imports.add(theImport); 
                }
            }
        }
        return imports;
    }

    public FamixStructuralEntity getTypeForVariable(String uniqueVarName) throws Exception {
        String typeVariable = uniqueVarName;
        String[] splitted = typeVariable.split("\\.");
        for (int i = splitted.length; i > 1; i--) {
            if (structuralEntities.containsKey(typeVariable)) {
                return structuralEntities.get(typeVariable);
            }
            // Is it an instance variable?
            typeVariable = splitted[0] + "." + splitted[splitted.length - 1];
        }
        // If not, throw new exception
        throw new Exception("The unit (or a part of it) '" + typeVariable + " or " + uniqueVarName + "' is not found or defined.");
    }
    
    public String toString() {
        return "\n ------------Packages------------- \n" + packages
                + "\n ------------Classes------------- \n" + classes
                + "\n -----------Assocations:-------------- \n" + associations
                + "\n -----------Libraries:-------------- \n" + libraries
                + "\n --------------Methoden (behavioural entities) ----------- \n" + behaviouralEntities
                + "\n --------------Variabelen (structural entities) ----------- \n" + structuralEntities
                + "\n -----------Invocations-------------- \n" + associations
                + "num invocs " + associations.size();

    }

    public void clear() {
        currentInstance.waitingAssociations.clear();
        currentInstance.waitingStructuralEntitys.clear();
        currentInstance.associations.clear();
        currentInstance.classes.clear();
        currentInstance.packages.clear();
        currentInstance.libraries.clear();
        currentInstance.imports.clear();
        currentInstance.structuralEntities.clear();
        currentInstance.behaviouralEntities.clear();
    }
}
