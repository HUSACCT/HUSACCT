package husacct.analyse.domain.famix;

import husacct.ServiceProvider;
import husacct.control.task.States;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import javax.naming.directory.InvalidAttributesException;

import org.apache.log4j.Logger;

class FamixCreationPostProcessor {

    private static final String EXTENDS = "Extends";
    private static final String EXTENDS_LIBRARY = "ExtendsLibrary";
    private static final String EXTENDS_ABSTRACT = "ExtendsAbstract";
    private static final String EXTENDS_CONCRETE = "ExtendsConcrete";
    private static final String EXTENDS_INTERFACE = "ExtendsInterface";
    private static final String EXTENDS_INDIRECT = "ExtendsIndirect";
    private FamixModel theModel;
    private HashMap<String, ArrayList<FamixImport>> importsPerEntity;
    private HashMap<String, HashSet<String>> inheritanceAssociationsPerClass;
    private final Logger logger = Logger.getLogger(FamixCreationPostProcessor.class);
    private int numberOfNotConnectedWaitingAssociations;
    //Needed for the ProgressBar of the analyse application LoaderDialog 
    private int amountOfModulesConnected;
    private int progressPercentage;
    private int numberOfWaitingObjects; 

    public FamixCreationPostProcessor() {
        theModel = FamixModel.getInstance();
        numberOfNotConnectedWaitingAssociations = 0;
        amountOfModulesConnected = 0;
        progressPercentage = 0;
    }

    // Objective: Fill HashMap importsPerEntity 
    void processImports() {
        FamixImport foundImport;
		ArrayList<FamixImport> foundImportsList;
		ArrayList<FamixImport>  alreadyIncludedImportsList;
		importsPerEntity = new HashMap<String, ArrayList<FamixImport>>();
	    
		try{
	        for (FamixAssociation association : theModel.associations) {
            	String uniqueNameFrom = association.from;
	        	foundImport = null;
	            if (association instanceof FamixImport) {
	            	foundImport = (FamixImport) association;
	                // Fill HashMap importsPerEntity 
	            	alreadyIncludedImportsList = null;
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
	        this.logger.warn(new Date().toString() + "Exception may result in incomplete dependency list. Exception:  " + e);
	        //e.printStackTrace();
		}
    }
    
    void processWaitingStructuralEntities() {
        numberOfWaitingObjects = (theModel.waitingAssociations.size() + theModel.waitingStructuralEntities.size());
	
		for (FamixStructuralEntity entity : theModel.waitingStructuralEntities) {
            try {
            	boolean belongsToClassExists = false;
            	boolean declareTypeExists = false;
            	String theContainingClass = entity.belongsToClass;
            	
            	// Test helper
            	//if (theContainingClass.contains("CheckInDAO")){
            	//	boolean breakpoint1 = true;
            	//}
            	
            	// Check if belongsToClass refers to an existing class
            	if (theModel.classes.containsKey(theContainingClass)) {
            		belongsToClassExists = true;
            	}

            	// Objective: If FamixStructuralEntity.declareType is a name instead of a unique name,  than replace it by the unique name of a FamixEntity (Class or Library) it represents.
            	// Check if declareType refers to an existing class or library
            	if (theModel.classes.containsKey(entity.declareType) || theModel.libraries.containsKey(entity.declareType)) {
            		declareTypeExists = true;
            	} else {
            		if (theModel.libraries.containsKey("xLibraries." + entity.declareType)) {
            			entity.declareType = "xLibraries." + entity.declareType;
            			declareTypeExists = true;
            		}
            	}

            	// Objective: If declareType is a name instead of a unique name,  than replace it by the unique name of a FamixEntity (Class or Library) it represents.
            	// Try to derive declareType from the unique name from the imports.
            	if (belongsToClassExists && (!declareTypeExists)) {
	            	String classFoundInImports = "";
                    classFoundInImports = findClassInImports(theContainingClass, entity.declareType);
                    if (!classFoundInImports.equals("")) {
	                	if (theModel.classes.containsKey(classFoundInImports) || theModel.libraries.containsKey("xLibraries." + classFoundInImports)) {
	                        entity.declareType = classFoundInImports;
	                        declareTypeExists = true;
	                	}
                	}
        		}

            	// Find out or the name refers to a type in the same package as the from class.*/
            	if (belongsToClassExists && (!declareTypeExists)) {
                    String belongsToPackage = theModel.classes.get(theContainingClass).belongsToPackage;
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

    // Objective: 1) Add complete inheritance associations to FamixModel.associations; 2) Fill the HashMap inheritanceAccociationsPerClass; 3) Remove waitingAssociation afterwards.
    void processInheritanceAssociations() {
		FamixAssociation foundInheritance;
		HashSet<String> foundInheritanceList;
		HashSet<String> alreadyIncludedInheritanceList;
		inheritanceAssociationsPerClass = new HashMap<String, HashSet<String>>();
		try{
			Iterator<FamixAssociation> iterator = theModel.waitingAssociations.iterator();
	        for (Iterator<FamixAssociation> i=iterator ; i.hasNext();) {
	        	boolean inheritanceAssociation = false;
	        	boolean fromExists = false;
            	boolean toExists = false;
            	boolean toHasValue = false;
	        	FamixAssociation association = (FamixAssociation) i.next();
		        String uniqueNameFrom = association.from;

            	// Test helper
            	//if (association.from.startsWith("Domain.Indirect.Intermediate.IWhrrl")){
            	//	boolean breakpoint = true;
            	//}

		        if (association instanceof FamixInheritanceDefinition){
		        	inheritanceAssociation = true;
		        }
            	if (inheritanceAssociation) {
			        if (theModel.classes.containsKey(uniqueNameFrom)) {
			        	fromExists = true;
			        }
            	}
            	if (inheritanceAssociation && fromExists) {
			        if ((association.to != null) && (!association.to.equals(""))) {
			        	toHasValue = true;
				        if (theModel.classes.containsKey(association.to) || theModel.classes.containsKey("xLibraries." + association.to)) {
				        	toExists = true;
				        }
			        }
            	}
		        // If association.to is not a unique name of an existing type, try to replace it by the complete unique name.
	        	// Determine the type of association.to, first based on imports, and second based on package contents.
	        	if (inheritanceAssociation && fromExists && !toExists && toHasValue) {
                    String to = findClassInImports(association.from, association.to);
                    if (!to.equals("")) {
                        association.to = to;
                    }
                    else {
	                	String belongsToPackage = theModel.classes.get(association.from).belongsToPackage;
	                    to = findClassInPackage(association.to, belongsToPackage);
	                    if (!to.equals("")) { // So, in case association.to shares the same package as association.from 
	                        association.to = to;
	                    }
                    }
    		        if (theModel.classes.containsKey(association.to) || theModel.classes.containsKey("xLibraries." + association.to)) {
    		        	toExists = true;
    		        }
            	}
	        	if (inheritanceAssociation && fromExists && toExists) {
		        	// Add the inheritance association to the FamixModel, but only if to and from refer to an existing type. 
	            	addToModel(association);

	            	// Fill the HashMap inheritanceAccociationsPerClass with inheritance dependencies to super classes or interfaces 
	            	alreadyIncludedInheritanceList = null;
	            	foundInheritance = null;
	            	foundInheritanceList = null;
	            	alreadyIncludedInheritanceList = null;
	            	foundInheritance = association;
	            	if(inheritanceAssociationsPerClass.containsKey(uniqueNameFrom)){
	            		alreadyIncludedInheritanceList = inheritanceAssociationsPerClass.get(uniqueNameFrom);
	            		alreadyIncludedInheritanceList.add(foundInheritance.to);
	            		inheritanceAssociationsPerClass.put(uniqueNameFrom, alreadyIncludedInheritanceList);
	            	}
	            	else{
		            	foundInheritanceList = new HashSet<String>();
		            	foundInheritanceList.add(foundInheritance.to);
		            	inheritanceAssociationsPerClass.put(uniqueNameFrom, foundInheritanceList);
	            	}
	            	
	            	// Remove from waitingAssociations afterwards, to enhance the performance.    	
			        i.remove();
	        	}
	        }
		} catch(Exception e) {
	        this.logger.debug(new Date().toString() + "Exception may result in incomplete dependency list. Exception:  " + e);
	        //e.printStackTrace();
		}
		indirectAssociations_DeriveIndirectInheritance();
    }

	// Objective: Check references to FamixObjects and try to replace missing information (focusing on the to object).
    void processWaitingAssociations() {
		int numberOfConnectedViaImport = 0;
		int numberOfConnectedViaPackage = 0;
		int numberOfConnectedViaAttribute = 0;
		int numberOfConnectedViaLocalVariable = 0;
		int numberOfConnectedViaSuperclass = 0;
		int numberOfConnectedAccess = 0;

        for (FamixAssociation association : theModel.waitingAssociations) {
            try {
            	boolean fromExists = false;
            	boolean toExists = false;
            	boolean toHasValue = false;
            	String to = "";
            	FamixInvocation theInvocation = null;

                // Test helpers
            	//if (association.from.contains("AccessClassVariableInterface")){
            	//	boolean breakpoint = true;
            	//}
                /*
                if (isInvocation(association)) {
                	theInvocation = (FamixInvocation) association;
                	if (theInvocation.belongsToMethod.equalsIgnoreCase("CallInstanceMethod()")){
                		String method = theInvocation.belongsToMethod;
                		String fromClass = theInvocation.from;
                	}
                }
                */

            	// Check if association.from refers to an existing class
            	if (theModel.classes.containsKey(association.from)) {
            		fromExists = true;
            	} 
            	// Check if association.to refers to an existing class or library
                if ((association.to != null) && (!association.to.equals(""))){ 
                	toHasValue = true;
                	if (theModel.classes.containsKey(association.to) || theModel.libraries.containsKey("xLibraries." + association.to)) {
                		toExists = true;
                 	}
                }

            	// Objective: If FamixAssociation.to is a name instead of a unique name,  than replace it by the unique name of a FamixEntity (Class or Library) it represents. 
                // 1) Try to derive the unique name from the imports.
                if (fromExists && !toExists && toHasValue){
                	if (!association.to.contains(".")) {
	                    to = findClassInImports(association.from, association.to);
	                    if (!to.equals("") && (theModel.classes.containsKey(to) || theModel.libraries.containsKey("xLibraries." + to))) {
	                        association.to = to;
                			toExists = true;
                			numberOfConnectedViaImport ++;
	                    }
                	}
                }
	                    
                // 2) Find out or association.to refers to a type in the same package as the from class.
                if (fromExists && !toExists && toHasValue){
                	if (!association.to.contains(".")) {
	                	String belongsToPackage = theModel.classes.get(association.from).belongsToPackage;
	                    to = findClassInPackage(association.to, belongsToPackage);
	                    if (!to.equals("")) { // So, in case association.to does not contain "." AND association.to shares the same package as association.from 
	                        association.to = to;
	            			toExists = true;
	            			numberOfConnectedViaPackage ++;
	                    }
                	}
                }

                // 3) Find out or association.to refers to an attribute. If so, get StructuralEntity on key ClassName.VariableName
                if (fromExists && !toExists && toHasValue){
                	String searchKey = association.from + "." + association.to;
                	if (theModel.structuralEntities.containsKey(searchKey)) {
                		FamixStructuralEntity entity = theModel.structuralEntities.get(searchKey);
                		if (entity.declareType != null && !entity.declareType.equals("")){
                			association.to = entity.declareType;
	            			toExists = true;
	            			numberOfConnectedViaAttribute ++;
                		}
                	}
                }

                // 4) Local variable: Get StructuralEntity on key ClassName.MethodName.VariableName
	            if (fromExists && !toExists && toHasValue){
                    if (association instanceof FamixInvocation) {
                        theInvocation = (FamixInvocation) association;
		            	String searchKey = association.from + "." + theInvocation.belongsToMethod + "." + theInvocation.nameOfInstance;
	                	if (theModel.structuralEntities.containsKey(searchKey)) {
	                		FamixStructuralEntity entity = theModel.structuralEntities.get(searchKey);
	                		if (entity.declareType != null && !entity.declareType.equals("")){
	                			theInvocation.to = entity.declareType;
		            			toExists = true;
		            			numberOfConnectedViaLocalVariable ++;
	                		}
	                	}
                    }
            	}

            	// 5) // Find out or association.to refers to the type of a superclass variable
                if (fromExists && !toExists && toHasValue){
                    to = findVariableTypeViaSuperClass(association.from, association.to);
	                if (!to.equals("")) { // So, in case association.to refers to the type of a superclass variable 
	                	association.to = to;
	                	toExists = true;
	                	numberOfConnectedViaSuperclass ++;
                    }
                }

                if ((fromExists && !toExists && toHasValue) && association instanceof FamixAccess) {
                    numberOfConnectedAccess ++;
                }
    				
    			if (fromExists && toExists) {
					determineSpecificExtendType(association);
					addToModel(association);
                } else {
        			numberOfNotConnectedWaitingAssociations ++;
                }
    			
                calculateProgress();
                //Needed to check if Thread is allowed to continue
            	if (!ServiceProvider.getInstance().getControlService().getState().contains(States.ANALYSING)) {
                    break;
            	}

            } catch (Exception e) {
            	String associationType = association.type;
    	        this.logger.error(new Date().toString() + " "  + e + " " + associationType + " " + association.toString());
    	        e.printStackTrace();
            }
        }

        this.logger.info(" Connected via 1) Import: " + numberOfConnectedViaImport 
        		+ ", 2) Package: " + numberOfConnectedViaPackage + ", 3) Attribute: " + numberOfConnectedViaAttribute + ", 4) Inherited var: " + numberOfConnectedViaSuperclass 
        		+ ", 5) Local var: " + numberOfConnectedViaLocalVariable  + ", 6) Access: " + numberOfConnectedAccess);
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
        if (association instanceof FamixInheritanceDefinition) {
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
                if (theModel.libraries.containsKey("xLibraries." + association.to)) {
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
    	if (inheritanceAssociationsPerClass.containsKey(uniqueClassName)){
    		HashSet<String> inheritanceAssociations = inheritanceAssociationsPerClass.get(uniqueClassName);
    		for (String superClass : inheritanceAssociations){
    			if (superClass.equals(uniqueClassName)) {
    				break; // Otherwise, things go wrong with derived C# classes with the same name.
    			}
	    		String searchKey = superClass + "." + variableName;
            	if (theModel.structuralEntities.containsKey(searchKey)) {
            		result = theModel.structuralEntities.get(searchKey).declareType;
            		break;
	            }
	            else {
	            	if (inheritanceAssociationsPerClass.containsKey(superClass)){
	            		result = findVariableTypeViaSuperClass(superClass, variableName);
	            		break;
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
    
    // Add Indirect Associations
    
	// Add indirect inheritance associations. Requires the existence of HashMap inheritanceAccociationsPerClass.
    private void indirectAssociations_DeriveIndirectInheritance() {
        try{
        	List<FamixAssociation> indirectInheritanceAssociations = new ArrayList<FamixAssociation>();
	        for (FamixAssociation directAssociation : theModel.associations) {
                if (directAssociation.to == null || directAssociation.from == null || directAssociation.to.equals("") || directAssociation.from.equals("")){ 
                	numberOfNotConnectedWaitingAssociations ++;
                }
                else if (directAssociation instanceof FamixInheritanceDefinition){ 
                	// Test helper
                	//if (associationx.from.startsWith("domain.indirect.violatingfrom.Inheritance")){
                	//	boolean breakpoint = true;
                	//}
    				if (inheritanceAssociationsPerClass.containsKey(directAssociation.to) ){
    					indirectInheritanceAssociations.addAll(IndirectAssociations_AddIndirectInheritanceAssociation(directAssociation.from, directAssociation.to, directAssociation.lineNumber));
    				}
    			}
        	}
	        for (FamixAssociation indirectInheritanceAssociation : indirectInheritanceAssociations) {
	        	addToModel(indirectInheritanceAssociation);
	        }
        } 	catch (Exception e) {
	        this.logger.debug(new Date().toString() + " "  + e);
	        e.printStackTrace();
        }
    }

    private List<FamixAssociation> IndirectAssociations_AddIndirectInheritanceAssociation(String from, String to, int lineNumber) {
    	List<FamixAssociation> indirectInheritanceAssociations = new ArrayList<FamixAssociation>();
		HashSet<String> foundInheritanceList = inheritanceAssociationsPerClass.get(to);
		for (String foundInheritance : foundInheritanceList){
			//Create a new association of type FamixInheritanceDefinition from association to superSuperClass
			FamixAssociation newAssociation = new FamixAssociation();
			newAssociation.from = from;
			newAssociation.to = foundInheritance;
			newAssociation.lineNumber = lineNumber;
			newAssociation.type = EXTENDS_INDIRECT;
			newAssociation.isIndirect = true;
			indirectInheritanceAssociations.add(newAssociation);
			if (inheritanceAssociationsPerClass.containsKey(newAssociation.to) ){
				indirectInheritanceAssociations.addAll(IndirectAssociations_AddIndirectInheritanceAssociation(newAssociation.from, newAssociation.to, newAssociation.lineNumber));
			}
		}
		return indirectInheritanceAssociations;
    }

}
