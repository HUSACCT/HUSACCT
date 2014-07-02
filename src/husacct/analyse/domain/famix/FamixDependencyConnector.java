package husacct.analyse.domain.famix;

import husacct.ServiceProvider;
import husacct.control.task.States;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
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
    private HashMap<String, ArrayList<FamixImport>> importsPerEntity;
    private HashMap<String, ArrayList<FamixAssociation>> inheritanceAccociationsPerClass;
    private HashMap<String, FamixStructuralEntity> structuralEntityHashMapOnClassNameVariableName;
    private final Logger logger = Logger.getLogger(FamixDependencyConnector.class);
    private int numberOfNotConnectedWaitingAssociations;
    //Needed for the ProgressBar of the analyse application LoaderDialog 
    private int amountOfModulesConnected;
    private int progressPercentage;
    private int numberOfWaitingObjects; 

    public FamixDependencyConnector() {
        theModel = FamixModel.getInstance();
        numberOfNotConnectedWaitingAssociations = 0;
        amountOfModulesConnected = 0;
        progressPercentage = 0;
    }

    void connectStructuralDependecies() {
		String theClass;
		String classFoundInImports;                        
		String belongsToPackage;
		String to;
        numberOfWaitingObjects = (theModel.waitingAssociations.size() + theModel.waitingStructuralEntitys.size());
		initializeHashMapsimportsPerEntity();
		
        for (FamixStructuralEntity entity : theModel.waitingStructuralEntitys) {
            try {
            	// Try to replace declareType by a unique name of its containing entity.
        		classFoundInImports = "";
        		theClass = entity.belongsToClass;
                if (!entity.declareType.contains(".")) {
                    classFoundInImports = findClassInImports(theClass, entity.declareType);
                    // Extend in the future!!! Type may be an import of one of the super classes!
                    if (!classFoundInImports.equals("")) {
                        entity.declareType = classFoundInImports;
                    } else {
                        belongsToPackage = getPackageFromUniqueClassName(entity.belongsToClass);
                        to = findClassInPackage(entity.declareType, belongsToPackage);
                        if (!to.equals("")) {
                            entity.declareType = to;
                        }
                    }
                }
                addToModel(entity);
            	calculateProgress();
                //Needed to check if Thread is allowed to continue
                if (!ServiceProvider.getInstance().getControlService().getState().contains(States.ANALYSING)) {
                	break;
                }
            } catch (Exception e) {
            	this.logger.error(new Date().toString() + " Exception:  " + e);
            	e.printStackTrace();
            }
        }
		// Not needed (any more/currently)to connect waitingAssociations
        //initializeHashMapstructuralEntityHashMap();
    }

    void connectAssociationDependencies() {
		String theClass;
		String belongsToPackage;
		String classFoundInImports;
		String to;
		FamixInvocation theInvocation;
		int numberOfConnectedViaImport = 0;
		int numberOfConnectedViaPackage = 0;
		int numberOfConnectedViaAssignment = 0;
		int numberOfConnectedViaAttribute = 0;
		int numberOfConnectedViaLocalVariable = 0;
		int numberOfConnectedAccess = 0;


		List<FamixAssociation> allWaitingAssociations  = theModel.waitingAssociations;
        for (FamixAssociation association : allWaitingAssociations) {
            try {
            	classFoundInImports = "";
                boolean connected = false;
                theInvocation = null;
                /*
                Note: Usefull to select a certain testcase and breakline here with the debugger
                if (isInvocation(association)) {
                	theInvocation = (FamixInvocation) association;
                	if (theInvocation.belongsToMethod.equalsIgnoreCase("CallInstanceMethod()")){
                		String method = theInvocation.belongsToMethod;
                		String fromClass = theInvocation.from;
                	}
                }
                */
                
                if (association.to == null || association.from == null || association.to.equals("") || association.from.equals("")){ 
                	numberOfNotConnectedWaitingAssociations ++;
                }
                else {
                	theClass = association.from;
                	if (!association.to.contains(".")) {
	                    classFoundInImports = findClassInImports(theClass, association.to);
	                    if (!classFoundInImports.equals("")) {
	                        // So, in case association.to does not contain "." AND association.to is an import of association.from
	                        association.to = classFoundInImports;
	                        connected = true;
	                        numberOfConnectedViaImport ++;
	                    } else {
	                        belongsToPackage = getPackageFromUniqueClassName(association.from);
	                        to = findClassInPackage(association.to, belongsToPackage);
	                        if (!to.equals("")) {
	                            // So, in case association.to does not contain "." AND association.to shares the same package as association.from 
	                            association.to = to;
	                            connected = true;
	                            numberOfConnectedViaPackage ++;
	                        }
	                    }
	                    if (!connected) {
	                        if (association instanceof FamixInvocation) {
	                            theInvocation = (FamixInvocation) association;
	                            if (theInvocation.belongsToMethod == null || theInvocation.belongsToMethod.equals("")) {
	                                //Then it is an attribute assignment. Example: currentFunction = FinderArguments.ROOT; 
	                                theInvocation.to = getClassForAttribute(theInvocation.from, theInvocation.nameOfInstance);
	                                numberOfConnectedViaAssignment ++;
	                            } else {
	                            	// If association.type == InvocConstructor, then connected should be true
	                            	// If association.type == AccessPropertyOrField or InvocMethod, the type of the variable needs to be determined
	                            	// The variable may be an attribute, local variable, global variable, parameter, ...                             	

	                            	// 1) Attribute: Get StructuralEntity on key ClassName.VariableName
	                            	String searchKey = theInvocation.from + "." + theInvocation.nameOfInstance;
	                            	//if (structuralEntityHashMapOnClassNameVariableName.containsKey(searchKey)) {
	                            		//FamixStructuralEntity entity = structuralEntityHashMapOnClassNameVariableName.get(searchKey);
	                            	if (theModel.structuralEntities.containsKey(searchKey)) {
	                            		FamixStructuralEntity entity = theModel.structuralEntities.get(searchKey);
	                            		if (entity.declareType != null && !entity.declareType.equals("")){
	                            			theInvocation.to = entity.declareType;
	                            			numberOfConnectedViaAttribute ++;
	                            		}
	                            	} else {
	                            		// 2) Local variable: Get StructuralEntity on key ClassName.MethodName.VariableName
	                            		searchKey = theInvocation.from + "." + theInvocation.belongsToMethod + "." + theInvocation.nameOfInstance;
		                            	if (theModel.structuralEntities.containsKey(searchKey)) {
		                            		FamixStructuralEntity entity = theModel.structuralEntities.get(searchKey);
		                            		if (entity.declareType != null && !entity.declareType.equals("")){
		                            			theInvocation.to = entity.declareType;
		                            			numberOfConnectedViaLocalVariable ++;
		                            		}
		                            	}
	                            	}
	                            			
	                            }
	                        }
	                        if (association instanceof FamixAccess) {
	                            FamixAccess theAccess = (FamixAccess) association;
	                            numberOfConnectedAccess ++;
	                        }
	                    }
	                }
    				

            		if(association.to == null || association.from == null || association.to.equals("") || association.from.equals("")){
            			numberOfNotConnectedWaitingAssociations ++;
    				} else {
    					determineSpecificExtendType(association);
    					addToModel(association);
	                    calculateProgress();
	                    //Needed to check if Thread is allowed to continue
	                	if (!ServiceProvider.getInstance().getControlService().getState().contains(States.ANALYSING)) {
	                        break;
	                	}
    				}
                }
            } catch (Exception e) {
            	String associationType = association.type;
    	        this.logger.error(new Date().toString() + " "  + e + " " + associationType + " " + association.toString());
    	        e.printStackTrace();
            }
        }

        this.logger.info(" Connected via 1) Import: " + numberOfConnectedViaImport 
        		+ ", 2) Package: " + numberOfConnectedViaPackage + ", 3) Assignment: " + numberOfConnectedViaAssignment 
        		+ ", 4) Attribute: " + numberOfConnectedViaAttribute + ", 5) Local var: " + numberOfConnectedViaLocalVariable 
        		+ ", 6) Access: " + numberOfConnectedAccess);
    }
    
    public String getNumberOfRejectedWaitingAssociations() {
    	String number = String.valueOf(numberOfNotConnectedWaitingAssociations);
    	return number;
    }
    
    private void calculateProgress(){
    	int currentProgress = (++amountOfModulesConnected * 100) / this.numberOfWaitingObjects; 
    	if (currentProgress >= this.progressPercentage + 1){
    		progressPercentage = currentProgress;
            ServiceProvider.getInstance().getControlService().updateProgress(progressPercentage);
    	}
    }
    
    private void determineSpecificExtendType(FamixAssociation association) {
        String type = association.type;
        if (type.equals(EXTENDS)) {
            FamixClass theClass = theModel.classes.get(association.to);
            if (theClass != null) {
                if (theClass.isAbstract) {
                    type = EXTENDS_ABSTRACT;
                } else if (!theClass.isAbstract) {
                	if (theClass.isInterface){
                		type = EXTENDS_INTERFACE;
                	} else {
                		type = EXTENDS_CONCRETE;
                	}
                }
            } else {
                if (theModel.libraries.containsKey(association.to)) {
                	type = EXTENDS_LIBRARY;
                }
            }
       }
        association.type = type;
    }

    private String getClassForAttribute(String declareClass, String attributeName) {
        for (FamixAttribute famixAttribute : theModel.getAttributes()) {
            if (famixAttribute.belongsToClass.equals(declareClass)) {
                if (famixAttribute.name.equals(attributeName)) {
                    return famixAttribute.declareType;
                }
            }
        }
        return "";
    }

    private String findClassInImports(String importingClass, String typeDeclaration) {
    	List<FamixImport> imports = getImportsInClass(importingClass);
    	if (imports != null){
	        for (FamixImport fImport : imports) {
	            if (!fImport.importsCompletePackage) {
	                if (fImport.to.endsWith("." + typeDeclaration)) {
	                    return fImport.to;
	                }
	            } 
	            else {
	                for (String uniqueClassName : getClassesInPackage(fImport.to)) {
	                    if (uniqueClassName.endsWith("." + typeDeclaration)) {
	                        return uniqueClassName;
	                    }
	                }
	            }
	        }
    	}
        return "";
    }
    
    public List<FamixImport> getImportsInClass(String uniqueClassName) {
    	//Find FamixEntity matching uniqueClassName and return the list of imports  
    	List<FamixImport> importsReturned = new ArrayList<FamixImport>();
    	importsReturned = importsPerEntity.get(uniqueClassName); 
        return importsReturned;
    }

    private String findClassInPackage(String className, String uniquePackageName) {
        for (String uniqueName : getClassesInPackage(uniquePackageName)) {
            if (uniqueName.endsWith("." + className)) {
                return uniqueName;
            }
        }
        return "";
    }

    private List<String> getClassesInPackage(String packageUniqueName) {
        List<String> result = new ArrayList<String>();
      // New finder function, based on FamixDecompositionEntity. Status 2014-07-01: results in less found dependencies!!!  
        if (theModel.packages.containsKey(packageUniqueName)){
        	TreeSet<String> children = theModel.packages.get(packageUniqueName).children;
        	if ((children != null) && (children.size() > 0)){
        		for (String uniqueName : children){
        			FamixClass foundClass = theModel.classes.get(uniqueName);
        			if (foundClass != null){
        				result.add(uniqueName);
        				if (foundClass.hasInnerClasses){
        					result.addAll(foundClass.children);
        				}
        			}
        			
        		}
        	}
        }
        return result;
    }

    private String getPackageFromUniqueClassName(String completeImportString) {
        List<FamixClass> classes = theModel.getClasses();
        for (FamixClass fclass : classes) {
            if (fclass.uniqueName.equals(completeImportString)) {
                return fclass.belongsToPackage;
            }
        }
       return "";
    }

    private boolean addToModel(FamixObject newObject) {
        try {
            theModel.addObject(newObject);
            return true;
        } catch (InvalidAttributesException e) {
            return false;
        }
    }

    private void initializeHashMapstructuralEntityHashMap() {
    	structuralEntityHashMapOnClassNameVariableName = new HashMap<String, FamixStructuralEntity>();

    	int nrOfDuplicateStructuralEntities = 0;
    	int nrOfIndexedStructuralEntities = 0;

		try{
	    	for (FamixStructuralEntity entity : theModel.structuralEntities.values()) {
	    		String searchKey = entity.belongsToClass + "." + entity.name;
	            if (structuralEntityHashMapOnClassNameVariableName.containsKey(searchKey)){
	            	nrOfDuplicateStructuralEntities ++;
	            }
	            else {
	        		structuralEntityHashMapOnClassNameVariableName.put(searchKey, entity);
	        		nrOfIndexedStructuralEntities ++;
	            }
	    	}
	        this.logger.info(new Date().toString() + " Finished: initializeHashMapsForQueries(), Number of duplicate StructuralEntities: "  + String.valueOf(nrOfDuplicateStructuralEntities));
	        
		} catch(Exception e) {
	        this.logger.error(new Date().toString() + "Exception may result in incomplete dependency list. Exception:  " + e);
	        e.printStackTrace();
		}
    	
    	return;
    }
    
    private void initializeHashMapsimportsPerEntity() {
        FamixImport foundImport;
		ArrayList<FamixImport> foundImportsList;
		ArrayList<FamixImport>  alreadyIncludedImportsList;
		importsPerEntity = new HashMap<String, ArrayList<FamixImport>>();
	    
		try{
            //Fill HashMaps importsPerEntity and inheritanceAccociationsPerClass 
	        for (FamixAssociation association : theModel.associations) {
            	String uniqueNameFrom = association.from;
	            if (association instanceof FamixImport) {
		        	alreadyIncludedImportsList = null;
		        	foundImport = null;
	            	foundImport = (FamixImport) association;
	            	if (importsPerEntity.containsKey(uniqueNameFrom)){
	            		alreadyIncludedImportsList = importsPerEntity.get(uniqueNameFrom);
	            		alreadyIncludedImportsList.add(foundImport);
	            		importsPerEntity.put(uniqueNameFrom, alreadyIncludedImportsList);
	            	}
	            	else{
			        	foundImportsList = new ArrayList<FamixImport>();
		            	foundImportsList.add(foundImport);
		            	importsPerEntity.put(uniqueNameFrom, foundImportsList);
	            	}
	            }
	        }
	        
	        
		} catch(Exception e) {
	        this.logger.debug(new Date().toString() + "Exception may result in incomplete dependency list. Exception:  " + e);
	        e.printStackTrace();
		}
    }
}
