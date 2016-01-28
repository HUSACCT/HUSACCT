package husacct.analyse.domain.famix;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.List;

import javax.naming.directory.InvalidAttributesException;

import org.apache.log4j.Logger;

class FamixModel extends FamixObject {

    private static FamixModel currentInstance;
    private final Logger logger = Logger.getLogger(FamixModel.class);
    public List<FamixStructuralEntity> waitingStructuralEntities;
    public List<FamixAssociation> waitingAssociations;
    public HashMap<String, FamixBehaviouralEntity> behaviouralEntities;
    public HashMap<String, FamixStructuralEntity> structuralEntities;
    public TreeMap<String, FamixPackage> packages;
    public HashMap<String, FamixClass> classes;
    public HashMap<String, FamixImport> imports;
    public HashMap<String, FamixLibrary> libraries;
    private HashMap<String, HashMap<String, HashSet<FamixUmlLink>>> umlLinks;  // UmlLinks per from-class (first String), per to-class (second String).
    public ArrayList<FamixAssociation> associations;
    public String modelCreationDate;
	private int totalNumberOfLinesOfCode;

    private FamixModel() {
        this.modelCreationDate = new Date().toString();
        waitingAssociations = new ArrayList<FamixAssociation>();
        waitingStructuralEntities = new ArrayList<FamixStructuralEntity>();
        associations = new ArrayList<FamixAssociation>();
        classes = new HashMap<String, FamixClass>();
        packages = new TreeMap<String, FamixPackage>();
        imports = new HashMap<String, FamixImport>();
        libraries = new HashMap<String, FamixLibrary>();
        structuralEntities = new HashMap<String, FamixStructuralEntity>();
        behaviouralEntities = new HashMap<String, FamixBehaviouralEntity>();
        umlLinks = new HashMap<String, HashMap<String, HashSet<FamixUmlLink>>>();
        totalNumberOfLinesOfCode = 0;
    }

    public void clear() {
        currentInstance.waitingAssociations.clear();
        currentInstance.waitingStructuralEntities.clear();
        currentInstance.associations.clear();
        currentInstance.classes.clear();
        currentInstance.packages.clear();
        currentInstance.libraries.clear();
        currentInstance.imports.clear();
        currentInstance.structuralEntities.clear();
        currentInstance.behaviouralEntities.clear();
        currentInstance.umlLinks.clear();
        totalNumberOfLinesOfCode = 0;
    }
    
    public void clearAfterPostProcessing() {
        currentInstance.waitingAssociations.clear();
        currentInstance.waitingStructuralEntities.clear();
    }

    public static FamixModel getInstance() {
        if (currentInstance == null) {
            currentInstance = new FamixModel();
        }
        return currentInstance;
    }

    // Add Methods
    
    public void addObject(FamixObject famixObject) throws InvalidAttributesException {
        try{
    	if (famixObject instanceof FamixEntity) {
        	// Test Utility
    		//if (((FamixEntity) e).uniqueName.contains("xxx")) {
        	//	String test = "breakpoint"; }
            if (famixObject instanceof FamixBehaviouralEntity) {
                behaviouralEntities.put(((FamixEntity) famixObject).uniqueName, (FamixBehaviouralEntity) famixObject);
            } else if (famixObject instanceof FamixStructuralEntity) {
                structuralEntities.put(((FamixEntity) famixObject).uniqueName, (FamixStructuralEntity) famixObject);
            } else if (famixObject instanceof FamixPackage) {
            	addFamixPackage(famixObject);
            } else if (famixObject instanceof FamixClass) {
            	addFamixClass(famixObject);
            } else if (famixObject instanceof FamixLibrary) {
            	addFamixLibrary(famixObject);
            }
        } else if (famixObject instanceof FamixAssociation){
			if(famixObject instanceof FamixImport){
				String importKey = ((FamixImport)famixObject).importedModule + "." +((FamixImport)famixObject).importingClass;
				imports.put(importKey, (FamixImport) famixObject);
			}
			associations.add((FamixAssociation) famixObject);
        } else if (famixObject instanceof FamixUmlLink){
        	addFamixUmlLink((FamixUmlLink) famixObject);
        } else {
            throw new InvalidAttributesException("Wrongtype (not of type entity or association) ");
        }
        }catch(Exception e1) {
        	this.logger.error(new Date().toString() + " Exception while adding:  " + famixObject.toString());
	        e1.printStackTrace();
        }
    }

    private void addFamixPackage(FamixObject famixObject) {
        if (!packages.containsKey(((FamixEntity) famixObject).uniqueName)){
        	packages.put(((FamixEntity) famixObject).uniqueName, (FamixPackage) famixObject);
            String parentUniqueName = ((FamixPackage) famixObject).belongsToPackage;
            FamixPackage parent = null;
            if (!parentUniqueName.equals("") && (packages.containsKey(parentUniqueName))){
            	parent = packages.get(parentUniqueName);
            	parent.children.add(((FamixEntity) famixObject).uniqueName);
            }
        }
    }
    
    private void addFamixClass(FamixObject famixObject) {
        if (!classes.containsKey(((FamixEntity) famixObject).uniqueName)){
        	classes.put(((FamixEntity) famixObject).uniqueName, (FamixClass) famixObject);
            String parentUniqueName;
            if (((FamixClass) famixObject).isInnerClass) {
            	((FamixClass) famixObject).linesOfCode = 0;
            	parentUniqueName = ((FamixClass) famixObject).belongsToClass;
        		if ((!parentUniqueName.equals("")) && (classes.containsKey(parentUniqueName))){
        			FamixClass parent = classes.get(parentUniqueName);
        			parent.hasInnerClasses = true;
        			parent.children.add(((FamixEntity) famixObject).uniqueName);
        		}
            } else {
            	parentUniqueName = ((FamixClass) famixObject).belongsToPackage;
                FamixPackage parent = null;
                if (!parentUniqueName.equals("") && (packages.containsKey(parentUniqueName))){
                	parent = packages.get(parentUniqueName);
                	parent.children.add(((FamixEntity) famixObject).uniqueName);
                }
            }
            int loc = ((FamixClass) famixObject).linesOfCode;
            if (loc > 0) { 
            	totalNumberOfLinesOfCode = totalNumberOfLinesOfCode + loc;
            }
        }
    }
    
    private void addFamixLibrary(FamixObject famixObject) {
    	if (!libraries.containsKey(((FamixEntity) famixObject).uniqueName)){
        	libraries.put(((FamixEntity) famixObject).uniqueName, (FamixLibrary) famixObject);
            ((FamixLibrary) famixObject).external = true;
            String parentUniqueName = ((FamixLibrary) famixObject).belongsToPackage;
            if (parentUniqueName.equals("xLibraries")){
                FamixPackage parent = null;
                if (!parentUniqueName.equals("") && (packages.containsKey(parentUniqueName))){
                	parent = packages.get(parentUniqueName);
                	parent.children.add(((FamixEntity) famixObject).uniqueName);
                }
            } else {
                FamixLibrary parent = null;
                if (!parentUniqueName.equals("") && (libraries.containsKey(parentUniqueName))){
                	parent = libraries.get(parentUniqueName);
                	parent.children.add(((FamixEntity) famixObject).uniqueName);
                }
            }
        }
    }
    
    private void addFamixUmlLink(FamixUmlLink famixUmlLink) {
    	HashMap<String, HashSet<FamixUmlLink>> mapOfLinksPerFromClass;
    	HashSet<FamixUmlLink> setOfLinksPerToClass;
    	if (umlLinks.containsKey(famixUmlLink.from)){
    		mapOfLinksPerFromClass = umlLinks.get(famixUmlLink.from);
    		if (mapOfLinksPerFromClass.containsKey(famixUmlLink.to)) {
    			setOfLinksPerToClass = mapOfLinksPerFromClass.get(famixUmlLink.to);
    		} else {
    			setOfLinksPerToClass = new HashSet<FamixUmlLink>();
    			mapOfLinksPerFromClass.put(famixUmlLink.to, setOfLinksPerToClass);
    		}
    	} else {
			setOfLinksPerToClass = new HashSet<FamixUmlLink>();
			mapOfLinksPerFromClass = new HashMap<String, HashSet<FamixUmlLink>>();
			mapOfLinksPerFromClass.put(famixUmlLink.to, setOfLinksPerToClass);
			umlLinks.put(famixUmlLink.from, mapOfLinksPerFromClass);
    	}
		setOfLinksPerToClass.add(famixUmlLink);
	}

    // Get methods
    
    public int getTotalNumberOfLinesOfCode() {
    	return totalNumberOfLinesOfCode;
    }
    
    /* Returns all the FamixUmlLinks going from the fromClass to other FamixClasses (not to xLibraries). 
     * fromClass must be a unique name of FamixClass (not of an xLibraries). 
     */
    public HashSet<FamixUmlLink> getAllUmlLinksFromClassToOtherClasses(String fromClass) {
    	HashSet<FamixUmlLink> returnValue = new HashSet<FamixUmlLink>();
    	if (umlLinks.containsKey(fromClass)) {
    		HashMap<String, HashSet<FamixUmlLink>> mapOfLinksPerFromClass = umlLinks.get(fromClass);
    		for (String toClass : mapOfLinksPerFromClass.keySet()) {
    			HashSet<FamixUmlLink> setOfLinksPerToClass = mapOfLinksPerFromClass.get(toClass);
    			returnValue.addAll(setOfLinksPerToClass);
    		}
    	}
    	return returnValue;
    }
    
    /* Returns all the FamixUmlLinks going from the fromClass to the specific toClass.
     * fromClass and toClass must both be a unique name of FamixClass (not of an xLibraries). 
     * */
    public HashSet<FamixUmlLink> getAllUmlLinksFromClassToToClass(String fromClass, String toClass) {
    	HashSet<FamixUmlLink> returnValue = new HashSet<FamixUmlLink>();
    	if (umlLinks.containsKey(fromClass)) {
    		HashMap<String, HashSet<FamixUmlLink>> mapOfLinksPerFromClass = umlLinks.get(fromClass);
    		if (mapOfLinksPerFromClass.containsKey(toClass)) {
    			returnValue = mapOfLinksPerFromClass.get(toClass);
    		}
    	}
    	return returnValue;
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

}
