package husacct.analyse.domain.famix;

import husacct.common.dto.ExternalSystemDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.List;

import javax.naming.directory.InvalidAttributesException;

class FamixModel extends FamixObject {

    private static FamixModel currentInstance;
    public List<FamixStructuralEntity> waitingStructuralEntitys;
    public List<FamixAssociation> waitingAssociations;
    public HashMap<String, FamixBehaviouralEntity> behaviouralEntities;
    public HashMap<String, FamixStructuralEntity> structuralEntities;
    //public HashMap<String, FamixPackage> packages;
    public TreeMap<String, FamixPackage> packages;
    //public HashMap<String, FamixClass> classes;
    public HashMap<String, FamixClass> classes;
    public HashMap<String, FamixImport> imports;
    public HashMap<String, FamixInterface> interfaces;
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
        interfaces = new HashMap<String, FamixInterface>();
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
    	String name = "";
    	FamixClass returnValue;
        try{
    	if (e instanceof FamixEntity) {
            if (e instanceof FamixBehaviouralEntity) {
                behaviouralEntities.put(((FamixEntity) e).uniqueName, (FamixBehaviouralEntity) e);
            } else if (e instanceof FamixStructuralEntity) {
                structuralEntities.put(((FamixEntity) e).uniqueName, (FamixStructuralEntity) e);
            } else if (e instanceof FamixPackage) {
                packages.put(((FamixEntity) e).uniqueName, (FamixPackage) e);
            } else if (e instanceof FamixClass) {
            	name = ((FamixEntity) e).uniqueName.trim();
            	classes.put(((FamixEntity) e).uniqueName, (FamixClass) e);
            } else if (e instanceof FamixInterface) {
                interfaces.put(((FamixEntity) e).uniqueName, (FamixInterface) e);
            } else if (e instanceof FamixLibrary) {
                libraries.put(((FamixLibrary) e).uniqueName, (FamixLibrary) e);
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
    
    @Deprecated
    public ExternalSystemDTO[] getExternalSystems(){
		List<ExternalSystemDTO> externalSystems = new ArrayList<ExternalSystemDTO>();
		List<String> pathsToImports = new ArrayList<String>();
		List<String> pathsToPackages = new ArrayList<String>();
		for(String imp : imports.keySet())
			if(!pathsToImports.contains(imp))
				pathsToImports.add(imp);
		for(String clls : classes.keySet())
			if(!pathsToPackages.contains(clls))
				pathsToPackages.add(clls);
		for(String intrfc : interfaces.keySet())
			if(!pathsToPackages.contains(intrfc))
				pathsToPackages.add(intrfc);
		for(String compareString : pathsToImports)
			if(!pathsToPackages.contains(compareString))
				externalSystems.add(new ExternalSystemDTO(compareString.substring(compareString.lastIndexOf('.')+1), compareString));
		return externalSystems.toArray(new ExternalSystemDTO[externalSystems.size()]);
	}

    public String toString() {
        return "\n ------------Packages------------- \n" + packages
                + "\n ------------Classes------------- \n" + classes
                + "\n ------------Interfaces------------\n" + interfaces
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
        currentInstance.interfaces.clear();
        currentInstance.libraries.clear();
        currentInstance.structuralEntities.clear();
        currentInstance.behaviouralEntities.clear();
    }
}
