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

    void processWaitingStructuralEntities() {
		String theClass;
        numberOfWaitingObjects = (theModel.waitingAssociations.size() + theModel.waitingStructuralEntities.size());
		initializeHashMapsimportsPerEntity();
		
	
		for (FamixStructuralEntity entity : theModel.waitingStructuralEntities) {
            try {
            	boolean belongsToClassExists = false;
            	boolean declareTypeExists = false;
        		theClass = entity.belongsToClass;
            	
            	// Test helper
            	if (theClass.contains("domain.direct.Base")){
            		boolean breakpoint1 = true;
            	}
            	
            	// Check if belongsToClass refers to an existing class
            	if (theModel.classes.containsKey(theClass)) {
            		belongsToClassExists = true;
            	}

            	// Objective: If FamixStructuralEntity.declareType is a name instead of a unique name,  than replace it by the unique name of a FamixEntity (Class or Library) it represents.
            	// Check if declareType refers to an existing class or library
            	if (theModel.classes.containsKey(entity.declareType) || theModel.libraries.containsKey(entity.declareType) || theModel.libraries.containsKey("xLibraries." + entity.declareType)) {
            		declareTypeExists = true;
            	}

            	// Objective: If declareType is a name instead of a unique name,  than replace it by the unique name of a FamixEntity (Class or Library) it represents.
            	// Try to derive declareType from the unique name from the imports.
            	if (!declareTypeExists) {
	            	String classFoundInImports = "";
                    classFoundInImports = findClassInImports(theClass, entity.declareType);
                    if (!classFoundInImports.equals("")) {
	                	if (theModel.classes.containsKey(classFoundInImports) || theModel.libraries.containsKey("xLibraries." + classFoundInImports)) {
	                        entity.declareType = classFoundInImports;
	                        declareTypeExists = true;
	                	}
                	}
        		}

            	// Find out or the name refers to a type in the same package as the from class.*/
            	if (belongsToClassExists && (!declareTypeExists)) {
                    String belongsToPackage = theModel.classes.get(theClass).belongsToPackage;
            		String type = findClassInPackage(entity.declareType, belongsToPackage);
                    if (!type.equals("")) {
	                	if (theModel.classes.containsKey(type)) {
	                        entity.declareType = type;
	                        declareTypeExists = true;
	                	}
                    }
                }
                
            	if (belongsToClassExists && declareTypeExists) {
            		addToModel(entity);
            	} else {
            		//boolean breakpoint = true;
            	}
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
    }

    void connectAssociationDependencies() {
		String theClass;
		String belongsToPackage;
		String to;
		FamixInvocation theInvocation;
        
		initializeHashMapsinheritanceAccociationsPerClass();

		int numberOfConnectedViaImport = 0;
		int numberOfConnectedViaPackage = 0;
		int numberOfConnectedViaAssignment = 0;
		int numberOfConnectedViaAttribute = 0;
		int numberOfConnectedViaLocalVariable = 0;
		int numberOfConnectedViaSuperclass = 0;
		int numberOfConnectedAccess = 0;

    	/* Objective: If FamixAssociation.to is a name instead of a unique name,  than replace it by the unique name of a FamixEntity (Class or Library) it represents. 
    	 * The following options are available:
    	 * 1) FamixAssociation.to is a unique name already => Check if it refers to a FamixClass or FamixLibrary. If so, add to model.
    	 * 2) FamixAssociation.to is not a unique name =>
    	 * 2.1) Try to derive the unique name from the imports.
    	 * 2.2) Find out or the name refers to a type in the same package as the from class.
    	 * 2.3) Find out or the name refers to a superclass variable. */

		List<FamixAssociation> allWaitingAssociations  = theModel.waitingAssociations;
        for (FamixAssociation association : allWaitingAssociations) {
            try {
            	to = "";
                boolean connected = false;
                theInvocation = null;
            	// Test helper
            	if (association.from.equals("domain.direct.allowed.CallInstanceLibraryClass")){
            		boolean breakpoint = true;
            	}
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
	                    to = findClassInImports(theClass, association.to);
	                    if (!to.equals("")) {
	                        // So, in case association.to does not contain "." AND association.to is an import of association.from
	                        association.to = to;
	                        connected = true;
	                        numberOfConnectedViaImport ++;
	                    } else {
	                        if (theModel.classes.containsKey(association.from)) {
		                    	belongsToPackage = theModel.classes.get(association.from).belongsToPackage;
		                        to = findClassInPackage(association.to, belongsToPackage);
		                        if (!to.equals("")) { // So, in case association.to does not contain "." AND association.to shares the same package as association.from 
		                            association.to = to;
		                            connected = true;
		                            numberOfConnectedViaPackage ++;
		                        }
	                        }
	                    }
	                    if (!connected) {
	                        if (association instanceof FamixInvocation) {
	                            theInvocation = (FamixInvocation) association;
	                            if (theInvocation.belongsToMethod == null || theInvocation.belongsToMethod.equals("")) {
	                                //Then it is an attribute assignment. Example: currentFunction = FinderArguments.ROOT; 
	                                theInvocation.to = getClassForAttribute(theInvocation.from, theInvocation.nameOfInstance);
	                                numberOfConnectedViaAssignment ++;
	                                connected = true;
	                            } else {
	                            	// If association.type == InvocConstructor, then connected should be true
	                            	// If association.type == AccessPropertyOrField or InvocMethod, the type of the variable needs to be determined
	                            	// The variable may be an attribute, local variable, inherited variable, global variable, parameter, ...                             	

	                            	// 1) Attribute: Get StructuralEntity on key ClassName.VariableName
	                            	String searchKey = theInvocation.from + "." + theInvocation.nameOfInstance;
	                            	if (theModel.structuralEntities.containsKey(searchKey)) {
	                            		FamixStructuralEntity entity = theModel.structuralEntities.get(searchKey);
	                            		if (entity.declareType != null && !entity.declareType.equals("")){
	                            			theInvocation.to = entity.declareType;
	                            			numberOfConnectedViaAttribute ++;
	                            			connected = true;
	                            		}
    		                        } else { // 2) Local variable: Get StructuralEntity on key ClassName.MethodName.VariableName
	                            		searchKey = theInvocation.from + "." + theInvocation.belongsToMethod + "." + theInvocation.nameOfInstance;
		                            	if (theModel.structuralEntities.containsKey(searchKey)) {
		                            		FamixStructuralEntity entity = theModel.structuralEntities.get(searchKey);
		                            		if (entity.declareType != null && !entity.declareType.equals("")){
		                            			theInvocation.to = entity.declareType;
		                            			numberOfConnectedViaLocalVariable ++;
		                            			connected = true;
		                            		}
		                            	}
	                            	}
	                            }
	                        }
		                    if (!connected) {
		                    	// 3) // Find out or association.to refers to the type of a superclass variable
	    	                    to = findVariableTypeViaSuperClass(association.from, association.to);
	    		                if (!to.equals("")) { // So, in case association.to refers to the type of a superclass variable 
	    		                	association.to = to;
	    		                	numberOfConnectedViaSuperclass ++;
		                        }
	                        }
	                        if (!connected && association instanceof FamixAccess) {
	                            FamixAccess z = (FamixAccess) association;
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
        		+ ", 4) Attribute: " + numberOfConnectedViaAttribute + ", 5) Inherited var: " + numberOfConnectedViaSuperclass 
        		+ ", 6) Local var: " + numberOfConnectedViaLocalVariable  + ", 7) Access: " + numberOfConnectedAccess);
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

    private String findVariableTypeViaSuperClass(String uniqueClassName, String variableName) {
    	String result = "";
    	if (inheritanceAccociationsPerClass.containsKey(uniqueClassName)){
    		for (FamixAssociation inheritanceAssociation : inheritanceAccociationsPerClass.get(uniqueClassName)){
        		String superClass = inheritanceAssociation.to;
	    		String searchKey = superClass + "." + variableName;
            	if (theModel.structuralEntities.containsKey(searchKey)) {
            		result = theModel.structuralEntities.get(searchKey).declareType;
	            }
	            else {
	            	if (inheritanceAccociationsPerClass.containsKey(superClass)){
	            		result = findVariableTypeViaSuperClass(superClass, variableName);
	            	}
	            }
    			
    		}
    	}
    	return result;
    }
    
    private String findClassInImports(String importingClass, String typeDeclaration) {
    	List<FamixImport> importsOfClass = importsPerEntity.get(importingClass);
    	if (importsOfClass != null){
	        for (FamixImport fImport : importsOfClass) {
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
    
    private String findClassInPackage(String className, String uniquePackageName) {
        for (String uniqueName : getClassesInPackage(uniquePackageName)) {
            if (uniqueName.endsWith("." + className)) {
                return uniqueName;
            }
        }
        return "";
    }

    private List<String> getClassesInPackage(String packageUniqueName) { // Including the inner classes!
        List<String> result = new ArrayList<String>();
        if (theModel.packages.containsKey(packageUniqueName)){
        	TreeSet<String> children = theModel.packages.get(packageUniqueName).children;
        	if ((children != null)){
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

    private boolean addToModel(FamixObject newObject) {
        try {
            theModel.addObject(newObject);
            return true;
        } catch (InvalidAttributesException e) {
            return false;
        }
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
    
    private void initializeHashMapsinheritanceAccociationsPerClass() {
		FamixAssociation foundInheritance;
		ArrayList<FamixAssociation> foundInheritanceList;
		ArrayList<FamixAssociation>  alreadyIncludedInheritanceList;
		inheritanceAccociationsPerClass = new HashMap<String, ArrayList<FamixAssociation>>();

		try{
	        for (FamixAssociation association : theModel.waitingAssociations) {
            	String uniqueNameFrom = association.from;
            	//if(uniqueNameFrom.equals("domain.indirect.violatingfrom.InheritanceExtendsExtendsIndirect")){
            	//	String type = association.type;
            	//	this.logger.debug(new Date().toString() + " Type: domain.indirect.violatingfrom.InheritanceExtendsExtendsIndirect =  " + type);
            	//}
	            //Fill the HashMaps inheritanceAccociationsPerClass with inheritance dependencies to super classes or interfaces 
		        if (association.type.equals(EXTENDS) || association.type.equals(EXTENDS_CONCRETE)|| association.type.equals(EXTENDS_ABSTRACT) || association.type.equals(EXTENDS_INTERFACE)){
	            	alreadyIncludedInheritanceList = null;
	            	foundInheritance = null;
	            	foundInheritanceList = null;
	            	alreadyIncludedInheritanceList = null;
	            	foundInheritance = association;
	            	if(inheritanceAccociationsPerClass.containsKey(uniqueNameFrom)){
	            		alreadyIncludedInheritanceList = inheritanceAccociationsPerClass.get(uniqueNameFrom);
	            		alreadyIncludedInheritanceList.add(foundInheritance);
	            		inheritanceAccociationsPerClass.put(uniqueNameFrom, alreadyIncludedInheritanceList);
	            	}
	            	else{
		            	foundInheritanceList = new ArrayList<FamixAssociation>();
		            	foundInheritanceList.add(foundInheritance);
		            	inheritanceAccociationsPerClass.put(uniqueNameFrom, foundInheritanceList);
	            	}
	            }
            }
	        this.logger.info(new Date().toString() + " Finished: initializeHashMapsForQueries()");
		} catch(Exception e) {
	        this.logger.debug(new Date().toString() + "Exception may result in incomplete dependency list. Exception:  " + e);
	        //e.printStackTrace();
		}
    	
    	return;
    }

}
