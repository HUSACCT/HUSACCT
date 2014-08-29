package husacct.analyse.domain.famix;

import husacct.ServiceProvider;
import husacct.analyse.infrastructure.antlr.java.JavaParser;
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
    private HashMap<String, ArrayList<FamixMethod>> sequencesPerMethod;
    private HashMap<String, HashSet<String>> inheritanceAssociationsPerClass;
    private List<FamixAssociation> indirectAssociations = new ArrayList<FamixAssociation>();
    private List<FamixInvocation> waitingDerivedAssociations = new ArrayList<FamixInvocation>();

    private final Logger logger = Logger.getLogger(FamixCreationPostProcessor.class);
    private int numberOfNotConnectedWaitingAssociations;
    //Needed for the ProgressBar of the Analyse application LoaderDialog 
    private int amountOfModulesConnected;
    private int progressPercentage;
    private int numberOfWaitingObjects; 
    private int numberOfDerivedAssociations;

    public FamixCreationPostProcessor() {
        theModel = FamixModel.getInstance();
        numberOfNotConnectedWaitingAssociations = 0;
		numberOfDerivedAssociations = 0;
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
            	boolean declareTypeHasValue = false;
            	String theContainingClass = entity.belongsToClass;
            	
            	// Test helper
            	//if (theContainingClass.contains("BaseIndirect")){
            	//	boolean breakpoint1 = true;
            	//}
            	
            	// Check if belongsToClass refers to an existing class
            	if (theModel.classes.containsKey(theContainingClass)) {
            		belongsToClassExists = true;
            	}

            	// Objective: If FamixStructuralEntity.declareType is a name instead of a unique name,  than replace it by the unique name of a FamixEntity (Class or Library) it represents.
            	// Check if declareType refers to an existing class or library
                if ((entity.declareType != null) && (!entity.declareType.equals(""))){ 
                	declareTypeHasValue = true;
	            	if (theModel.classes.containsKey(entity.declareType) || theModel.libraries.containsKey(entity.declareType)) {
	            		declareTypeExists = true;
	            	}
                }

            	// Objective: If declareType is a name instead of a unique name,  than replace it by the unique name of a FamixEntity (Class or Library) it represents.
            	// Try to derive declareType from the unique name from the imports.
            	if (belongsToClassExists && (!declareTypeExists) && declareTypeHasValue) {
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
            	if (belongsToClassExists && (!declareTypeExists) && declareTypeHasValue) {
                    String belongsToPackage = theModel.classes.get(theContainingClass).belongsToPackage;
            		String type = findClassInPackage(entity.declareType, belongsToPackage);
                    if (!type.equals("")) {
	                	if (theModel.classes.containsKey(type)) {
	                        entity.declareType = type;
	                        declareTypeExists = true;
	                	}
                    }
                }
                
            	if (belongsToClassExists && declareTypeHasValue) { // Entities with primitive types should not be filtered out
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

    void processBehaviouralEntities() {
    	sequencesPerMethod = new HashMap<String, ArrayList<FamixMethod>>(); 
    	for (FamixBehaviouralEntity entity : theModel.behaviouralEntities.values()) {
            try {
            	boolean belongsToClassExists = false;
            	boolean declareTypeExists = false;
            	boolean declareTypeHasValue = false;
            	String theContainingClass = entity.belongsToClass;
            	
            	// Test helper
            	if (theContainingClass.contains("BaseIndirect")){
            		boolean breakpoint1 = true;
            	} 
            	
            	// Check if belongsToClass refers to an existing class
            	if (theModel.classes.containsKey(theContainingClass)) {
            		belongsToClassExists = true;
            	}

            	// Objective: If FamixBehaviouralEntity.declaredReturnType is not the unique name of a FamixEntity (Class or Library), than replace it by the unique name of a FamixEntity it represents.
            	// 0) Check if declareType refers to an existing class or library
                if ((entity.declaredReturnType != null) && (!entity.declaredReturnType.equals(""))){ 
                	declareTypeHasValue = true;
	            	if (theModel.classes.containsKey(entity.declaredReturnType) || theModel.libraries.containsKey(entity.declaredReturnType)) {
	            		declareTypeExists = true;
	            	}
                }

            	// Objective: If declareType is a name instead of a unique name,  than replace it by the unique name of a FamixEntity (Class or Library) it represents.
            	// 1) Try to derive declareType from the unique name from the imports.
            	if (belongsToClassExists && declareTypeHasValue) {
	            	String classFoundInImports = "";
                    classFoundInImports = findClassInImports(theContainingClass, entity.declaredReturnType);
                    if (!classFoundInImports.equals("")) {
	                	if (theModel.classes.containsKey(classFoundInImports) || theModel.libraries.containsKey("xLibraries." + classFoundInImports)) {
	                        entity.declaredReturnType = classFoundInImports;
	                        declareTypeExists = true;
	                	}
                	}
        		}

            	// 2) Find out or the name refers to a type in the same package as the from class.*/
            	if (belongsToClassExists && (!declareTypeExists) && declareTypeHasValue) {
                    String belongsToPackage = theModel.classes.get(theContainingClass).belongsToPackage;
            		String type = findClassInPackage(entity.declaredReturnType, belongsToPackage);
                    if (!type.equals("")) {
	                	if (theModel.classes.containsKey(type)) {
	                        entity.declaredReturnType = type;
	                        declareTypeExists = true;
	                	}
                    }
                }
                
                // Fill HashMap methodsPerClass 
            	if (belongsToClassExists && (entity instanceof FamixMethod)) {
            		FamixMethod method = (FamixMethod) entity;
	            	ArrayList<FamixMethod> alreadyIncludedMethodsList = null;
	            	String methodNameWithoutSignature = method.belongsToClass + "." + method.name; 
	            	if (sequencesPerMethod.containsKey(methodNameWithoutSignature)){
	            		alreadyIncludedMethodsList = sequencesPerMethod.get(methodNameWithoutSignature);
	            		alreadyIncludedMethodsList.add(method);
	            		sequencesPerMethod.put(methodNameWithoutSignature, alreadyIncludedMethodsList);
	            	}
	            	else{
	            		ArrayList<FamixMethod> newMethodsList = new ArrayList<FamixMethod>();
		            	newMethodsList.add(method);
		            	sequencesPerMethod.put(methodNameWithoutSignature, newMethodsList);
	            	}
            	}
            	
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

            	/* Test helper
            	if (association.from.startsWith("Limaki.Actions.Command")){
            		boolean breakpoint = true;
            	} */

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
	        	if (inheritanceAssociation && fromExists && toExists && !theModel.associations.contains(association)) {
		        	// Add the inheritance association to the FamixModel, but only if to and from are equal. 
	            	if (!association.from.equals(association.to)) { 
	            		addToModel(association);
	            	}

	            	// Fill the HashMap inheritanceAccociationsPerClass with inheritance dependencies to super classes or interfaces 
	            	alreadyIncludedInheritanceList = null;
	            	foundInheritance = null;
	            	foundInheritanceList = null;
	            	alreadyIncludedInheritanceList = null;
	            	foundInheritance = association;
	            	if(inheritanceAssociationsPerClass.containsKey(uniqueNameFrom)){
	            		alreadyIncludedInheritanceList = inheritanceAssociationsPerClass.get(uniqueNameFrom);
	            		if (!alreadyIncludedInheritanceList.contains(association.to)) {
	            			alreadyIncludedInheritanceList.add(foundInheritance.to);
	            		}
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
        this.logger.info(new Date().toString() + " Finished: processInheritanceAssociations()");
    }

	// Objective: Check references to FamixObjects and try to replace missing information (focusing on the to object).
    void processWaitingAssociations() {
		int numberOfConnectedViaImport = 0;
		int numberOfConnectedViaPackage = 0;
		int numberOfConnectedViaAttribute = 0;
		int numberOfConnectedViaLocalVariable = 0;

        for (FamixAssociation association : theModel.waitingAssociations) {
            try {
            	boolean fromExists = false;
            	boolean toExists = false;
            	boolean toHasValue = false;
            	boolean chainingInvocation = false;
            	boolean typeIsAccess = false;
            	boolean nextAssociationIsIndirect = false;
            	String toRemainderChainingInvocation = "";
            	String toString = "";
            	FamixInvocation theInvocation = null;

                // Test helpers
            	if (association.from.equals("domain.indirect.violatingfrom.AccessObjectReferenceIndirect_AsReturnValueOfSuperClassMethod_FromSide")){
            		if (association.lineNumber == 9) {
            			boolean breakpoint = true;
        			}
            	}

            	// Check if association.from refers to an existing class
            	if (theModel.classes.containsKey(association.from)) {
            		fromExists = true;
            	} 
            	// Check if association.to (or a part of it) refers to an existing class or library
                if ((association.to != null) && !association.to.equals("") && !association.to.trim().equals(".")){ 
                	toHasValue = true;
                	if (theModel.classes.containsKey(association.to) || theModel.libraries.containsKey("xLibraries." + association.to)) {
                		toExists = true;
                 	} else { // Check if a part of association.to refers to an existing class or library 
                        if (association.to.contains(".")) {
			            	String[] allSubstrings = association.to.split("\\.");
			            	toString = allSubstrings[0];
		                    for (int i = 1; i < allSubstrings.length ; i++) {
		                    	toString += "."+ allSubstrings[i];
		                    	if (theModel.classes.containsKey(toString) || theModel.libraries.containsKey("xLibraries." + toString)) {
		                    		if ((association instanceof FamixInvocation)) {
		                                theInvocation = (FamixInvocation) association;
		                                association.to = toString;
			                    		toExists = true;
			                    		chainingInvocation = true;
					                    // Put the remainder in a variable; needed to create a separate indirect association later on remainder substrings
					                    i++;
			                    		if (allSubstrings.length >= i) {
				                    		toRemainderChainingInvocation = allSubstrings[i];
						                    for (int j = i + 1; j < allSubstrings.length ; j++) {
						                    	toRemainderChainingInvocation = toRemainderChainingInvocation + "." + allSubstrings[j];
						                    }
			                    		}
		                    		}
		                    	}
		                    }
                        }
                 	}
                }

            	// Objective: If FamixAssociation.to is a name instead of a unique name,  than replace it by the unique name of a FamixEntity (Class or Library) it represents. 

                /* 0) Select and process FamixInvocations with a composed to-name
                 * If FamixAssociation.to is composed of substrings (a chaining assignment or call), a dependency to the type of the first substring is a direct dependency.
                 * Dependencies to types of the following substrings are determined afterwards. The next one is indirect, if the first substring is a method or variable of a super class, otherwise it is direct.
                 * Algorithm: Split the string and try to identify the type of the first substring. Create a separate association to identify dependencies to following substring (variables or methods). 
                 * If the type of the first substring is identified, replace the substring by the type in the next association, and store this association to be processed later on.   
                 * */
                if (fromExists && !toExists && toHasValue){
                	if ((association instanceof FamixInvocation)) {
                        theInvocation = (FamixInvocation) association;
                        if (association.to.contains(".")) {
			            	String[] allSubstrings = association.to.split("\\.");
			            	if (allSubstrings.length > 1) {
			                	chainingInvocation = true;
			                	association.to = allSubstrings[0]; 
			                    // Put the remainder in a variable; needed to create a separate indirect association later on remainder substrings
			                    toRemainderChainingInvocation = allSubstrings[1];
			                    for (int i = 2; i < allSubstrings.length ; i++) {
			                    	toRemainderChainingInvocation = toRemainderChainingInvocation + "." + allSubstrings[i];
			                    }
			            	}
                        }
                    }
                }
                
                // 1) Try to derive the unique name from the imports.
                if (fromExists && !toExists && toHasValue){
                	if (!association.to.contains(".")) {
	                    toString = findClassInImports(association.from, association.to);
	                    if (!toString.equals("")) {
	                        association.to = toString;
                			toExists = true;
                			numberOfConnectedViaImport ++;
	                    }
                	}
                }
	                    
                // 2) Find out or association.to refers to a type in the same package as the from class.
                if (fromExists && !toExists && toHasValue){
                	if (!association.to.contains(".")) {
	                	String belongsToPackage = theModel.classes.get(association.from).belongsToPackage;
	                    toString = findClassInPackage(association.to, belongsToPackage);
	                    if (!toString.equals("")) { // So, in case association.to does not contain "." AND association.to shares the same package as association.from 
	                        association.to = toString;
	            			toExists = true;
                			typeIsAccess = true;
	            			numberOfConnectedViaPackage ++;
	                    }
                	}
                }

            	// 3) Determine if association.to is an (inherited) attribute. If so determine the type of the attribute. 
                if ((association instanceof FamixInvocation) && (!association.to.endsWith(")"))) {
    	        	String classOfAttribute = findAttribute(association.from, association.to);
    	            if (!classOfAttribute.equals("")) {
	        			if (!classOfAttribute.equals(association.from)) { // classOfAttribute refers to a super class  
		                	// Create an access dependency on the superclass.
		    				FamixInvocation newIndirectInvocation = indirectAssociations_AddIndirectInvocation("AccessPropertyOrField", association.from, classOfAttribute, theInvocation.lineNumber, theInvocation.belongsToMethod, association.to, theInvocation.nameOfInstance, false);
		                	addToModel(newIndirectInvocation);  
			    			numberOfDerivedAssociations ++;
	                    }
    	        		FamixStructuralEntity entity = theModel.structuralEntities.get(classOfAttribute + "." + association.to);
    	        		if (entity.declareType != null && !entity.declareType.equals("")){
    	        			association.to = entity.declareType;
    	                	toExists = true;
                			typeIsAccess = true;
        	            	numberOfConnectedViaAttribute++;
    	        		}

    	        	}
                } 
 
                // 4) Find out or association.to refers to a local variable or parameter: Get StructuralEntity on key ClassName.MethodName.VariableName
	            if (fromExists && !toExists && toHasValue && (!association.to.endsWith(")"))){
                    if (association instanceof FamixInvocation) {
		            	String searchKey = association.from + "." + theInvocation.belongsToMethod + "." + theInvocation.to;
	                	if (theModel.structuralEntities.containsKey(searchKey)) {
	                		FamixStructuralEntity entity = theModel.structuralEntities.get(searchKey);
	                		if (entity.declareType != null && !entity.declareType.equals("")){
	                			theInvocation.to = entity.declareType;
		            			toExists = true;
	                			typeIsAccess = true;
		            			numberOfConnectedViaLocalVariable ++;
	                		}
	                	}
                    }
            	}

            	// 5) Determine if association.to is an (inherited) method. If so determine the return type of the method. 
                if ((association instanceof FamixInvocation) && (association.to.endsWith(")"))) {
                	boolean methodFound = false;
                	FamixMethod foundMethod = findInvokedMethodOnName(association.from, theInvocation.belongsToMethod, association.from, association.to);
    	            if (foundMethod != null) {
    	            	methodFound = true;
    	            } else { // Determine if association.to is an inherited method. 
    	        		String superClassName = indirectAssociations_findSuperClassThatContainsMethod(association.from, theInvocation.belongsToMethod, association.from, association.to);
    	        		if ((superClassName != null) && !superClassName.equals("")) {
    	                	foundMethod = findInvokedMethodOnName(association.from, theInvocation.belongsToMethod, superClassName, association.to);
    	    	            if (foundMethod != null) {
    	    	            	methodFound = true;
    		                	// Create a call dependency on the superclass;
    		    				FamixInvocation newInvocation = indirectAssociations_AddIndirectInvocation("InvocMethod", theInvocation.from, superClassName, theInvocation.lineNumber, theInvocation.belongsToMethod, association.to, theInvocation.nameOfInstance, false);
    		                	addToModel(newInvocation);  
    			    			numberOfDerivedAssociations ++;
    	                    }
    	    			}
    	            }
    	            if (methodFound) {
    	            	// Determine the return type of the method.
    	                if ((foundMethod != null) && (foundMethod.declaredReturnType != null) && !foundMethod.declaredReturnType.equals("")) {
    	        			numberOfNotConnectedWaitingAssociations ++;
    	                	association.to = foundMethod.declaredReturnType;
    	                	theInvocation.invocationName = toRemainderChainingInvocation;
    	                	if (chainingInvocation) { 
    	                		// The invocation is not added to the model yet, because it reflects an invisible access of the return type of the method. Creating a new derived invocation too is redundant.
	    	                	association.type = "Undetermined";
	    	                	waitingDerivedAssociations.add(theInvocation);
    	                	} else {
								// The invocation is added  as an access invocation to the referred type; the return value of the complete string.
	    	                	association.type = "AccessPropertyOrField";
		            			toExists = true;
							}

    	        		}
    	        	}
             	}
                
                if (fromExists && toExists) {
					if (!association.from.equals(association.to) && (theModel.classes.containsKey(association.to) || theModel.libraries.containsKey("xLibraries." + association.to))) {
	    				determineSpecificExtendType(association);
            			if (typeIsAccess && association.type.startsWith("Invoc")) {
            				association.type = "AccessPropertyOrField";
            			}
						addToModel(association);
					} else {
	        			numberOfNotConnectedWaitingAssociations ++;
					}
					if (association instanceof FamixInvocation) {
						if (chainingInvocation) { // If true, create an association to identify dependencies to the remaining parts of the chain. Store it temporarily; it is processed in a separate method. 
		                    FamixInvocation indirectAssociation = new FamixInvocation();
		                    indirectAssociation.type = "Undetermined";
		                    indirectAssociation.isIndirect = nextAssociationIsIndirect;
		                    indirectAssociation.from = association.from;
		                    indirectAssociation.lineNumber = association.lineNumber;
		                    indirectAssociation.to = association.to;
		                    indirectAssociation.invocationName = toRemainderChainingInvocation;
		                    indirectAssociation.belongsToMethod = theInvocation.belongsToMethod;
		                    indirectAssociation.nameOfInstance = theInvocation.nameOfInstance;
		                    waitingDerivedAssociations.add(indirectAssociation);
						} else {
						}
					}
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
        // Add the indirect associations created during this process to FamixModel.associations
        for (FamixAssociation indirectAssociation : indirectAssociations) {
        	addToModel(indirectAssociation);
        }

        this.logger.info(" Connected via 1) Import: " + numberOfConnectedViaImport 
        		+ ", 2) Package: " + numberOfConnectedViaPackage + ", 3) Attribute: " + numberOfConnectedViaAttribute + ", 4) Inherited var/method: " + numberOfDerivedAssociations 
        		+ ", 5) Local var: " + numberOfConnectedViaLocalVariable);
    }
    
	// Objective: Identify dependencies to the remaining parts of the chain in a chaining invocation (assignment or call).
    void processWaitingDerivedAssociations() {
    	List<FamixInvocation> addedInvocations = new ArrayList<FamixInvocation>();
    	for (FamixInvocation invocation : waitingDerivedAssociations) {
        	
        	// Test helper
        	if (invocation.from.contains("domain.direct.violating.AccessInstanceVariable_SetArgumentValue")){
        		if (invocation.lineNumber == 10) {
        			boolean breakpoint = true;
    			}
        	} 
        	
        	// 1) Split invocationName. Precondition: invocation.to is a type and invocationName contains the remainder of the string (determined in the previous process step).
        	String originalToType = invocation.to;
        	String nextToString = "";
    		boolean continueChaining = false;
        	String toRemainderChainingInvocation = "";
            String[] allSubstrings = invocation.invocationName.split("\\.");
            if ((allSubstrings.length > 0) && (!allSubstrings[0].equals(""))) {
            	nextToString = allSubstrings[0];
	            invocation.to = invocation.to + "." + nextToString; 
	            // Put the remainder in a variable; needed to create a separate indirect association, later on
	            if (allSubstrings.length > 1) {
	            	continueChaining = true;
	                toRemainderChainingInvocation = allSubstrings[1];
	                for (int i = 2; i < allSubstrings.length ; i++) {
	                	toRemainderChainingInvocation = toRemainderChainingInvocation + "." + allSubstrings[i];
	                }
	            }
            }

        	/* 2) Determine if the second substring is an (inherited) attribute. If so determine the type of the attribute. 
        	 * If the type refers to a FamixClass or FamixLibrary, set this type as invocation.to. */
        	boolean addInvocation = false;
        	String toTypeNewIndirectInvocation = "";
            if (!invocation.to.endsWith(")")) {
	        	boolean attributeFound = false;
	        	String searchKey = invocation.to;
	            if (theModel.structuralEntities.containsKey(searchKey)) {
	            	attributeFound = true;
        			invocation.to = originalToType;
	            } else { // Determine if nextToString is an inherited attribute 
	        		String superClassName = indirectAssociations_findSuperClassThatDeclaresVariable(originalToType, nextToString);
	        		if ((superClassName != null) && !superClassName.equals("")) {
	        			invocation.to = superClassName;
	        			searchKey = superClassName + "." + nextToString;
	        			if (theModel.structuralEntities.containsKey(searchKey)) {
	                    	attributeFound = true;
		        			invocation.to = superClassName;
		        			invocation.isIndirect = true;
	                    }
	    			}
	        	}
            	// This current association should be added as an access call dependency on the original to-type or on the superclass
    			invocation.type = "AccessPropertyOrField";
    			invocation.invocationName = nextToString;
    			addInvocation = true;
	            if (attributeFound) {
        			// Determine if a next invocation in the chain needs to be created
	        		FamixStructuralEntity entity = theModel.structuralEntities.get(searchKey);
	        		if (entity.declareType != null && !entity.declareType.equals("")){
	        			toTypeNewIndirectInvocation = entity.declareType;
	        		} else {
	        			continueChaining = false;
	        		}
	        	}
            }

            
        	/* 3) Determine if the second substring is an (inherited) method. If so determine the return type of the method. 
        	 * If not a primitive type, but an FamixClass or FamixLibrary,set this type as invocation.to. */
            if (invocation.to.endsWith(")")) {
                // Try to find the method; first as a method of the originalToType class
                boolean methodFound = false;
    			invocation.to = originalToType;
                FamixMethod foundMethod = findInvokedMethodOnName(invocation.from, invocation.belongsToMethod, originalToType, nextToString);
                if (foundMethod != null) {
	            	methodFound = true;
	            } else { // Determine if nextToString is an inherited method 
	        		String superClassName = indirectAssociations_findSuperClassThatContainsMethod(invocation.from, invocation.belongsToMethod, originalToType, nextToString);
	        		if ((superClassName != null) && !superClassName.equals("")) {
	        			foundMethod = findInvokedMethodOnName(invocation.from, invocation.belongsToMethod, superClassName, nextToString);
	                    if (foundMethod != null) {
	        				methodFound = true;
		        			invocation.to = superClassName;
		        			invocation.isIndirect = true;
	                    }
	    			}
	            }
            	// This current association should be added as a call dependency on the original to-type or on the superclass
    			invocation.type = "InvocMethod";
    			invocation.invocationName = nextToString;
    			addInvocation = true;
	            if (methodFound) {
        			// Determine if a next invocation in the chain needs to be created
	            	if (foundMethod.declaredReturnType != null && !foundMethod.declaredReturnType.equals("")){
	            		toTypeNewIndirectInvocation = foundMethod.declaredReturnType;
	        		} else {
	        			continueChaining = false;
	        		}
	        	}
         	}
            
    		if (addInvocation) { 
    			if (!invocation.from.equals(invocation.to) && (theModel.classes.containsKey(invocation.to) || theModel.libraries.containsKey("xLibraries." + invocation.to))) {
	    			addToModel(invocation);
					if (continueChaining) { 
						// Create an indirect association to identify dependencies to the remaining parts of the chain. Store it temporarily; it is processed in a separate method. 
						FamixInvocation newIndirectInvocation = indirectAssociations_AddIndirectInvocation("Undetermined", invocation.from, toTypeNewIndirectInvocation, invocation.lineNumber, invocation.belongsToMethod, toRemainderChainingInvocation, invocation.nameOfInstance, true);
	                    addedInvocations.add(newIndirectInvocation);
		    			numberOfDerivedAssociations ++;
					} else {
						if (toRemainderChainingInvocation.equals("") && !toTypeNewIndirectInvocation.equals("")) {
							// Create an access invocation to the referred type; the return value of the complete string.
							FamixInvocation newIndirectInvocation = indirectAssociations_AddIndirectInvocation("AccessPropertyOrField", invocation.from, toTypeNewIndirectInvocation, invocation.lineNumber, invocation.belongsToMethod, toRemainderChainingInvocation, invocation.nameOfInstance, true);
		                    addedInvocations.add(newIndirectInvocation);
			    			numberOfDerivedAssociations ++;
						}
					}
    			}
    		}
    	}
    	waitingDerivedAssociations.clear();
    	if (addedInvocations.size() > 0) {
    		waitingDerivedAssociations.addAll(addedInvocations);
    		addedInvocations.clear();
        	this.logger.info(" Number of derived invocations: " + numberOfDerivedAssociations);
    		processWaitingDerivedAssociations();
    	}
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

    // Tries to find the specific method
    // Returns FamixMethod if one unique method is found AND declaredReturnType refers to an existing FamixClass or FamixLibrary. Else, it returns "". 
    private FamixMethod findInvokedMethodOnName(String fromClass, String fromMethod, String invokedClassName, String invokedMethodName) {
    	FamixMethod result = null;
		// Test Helper
		if (fromClass.equals("domain.direct.violating.AccessLocalVariable_SetArgumentValue")){
			boolean breakpoint = true;
		}

    	// First, if methodNameAsInInvocation matches with a method unique name, return that method. 
    	String searchKey = invokedClassName + "." + invokedMethodName;
    	if (theModel.behaviouralEntities.containsKey(searchKey)) {
    		return (FamixMethod) theModel.behaviouralEntities.get(searchKey);
    	}
 
    	String methodName = invokedMethodName.substring(0, invokedMethodName.indexOf("(")); // Methodname without signature
    	 searchKey = invokedClassName + "." + methodName;
    	if (sequencesPerMethod.containsKey(searchKey)){
    		ArrayList<FamixMethod> methodsList = sequencesPerMethod.get(searchKey);
    		if (methodsList.size() == 0) {
    			return result;
    		} else if (methodsList.size() == 1) {
    				return methodsList.get(0);
    		} else {
    			String invocationSignature = invokedMethodName.substring(invokedMethodName.indexOf("("));;
    			String contentsInvocationSignature = invocationSignature.substring(invocationSignature.indexOf("(") + 1, invocationSignature.indexOf(")")); 
    			String[] invocationArguments = contentsInvocationSignature.split(",");
    			int numberOfArguments = invocationArguments.length;
    			// If there is only one method with the same number of parameters as invocationArguments, then return this method
    			List<FamixMethod> matchingMethods1 = new ArrayList<FamixMethod>();
	    		for (FamixMethod method : methodsList){
	    			if ((method.signature != null) && (!method.signature.equals(""))) {
		    			String contentsmethodParameter = method.signature.substring(method.signature.indexOf("(") + 1, method.signature.indexOf(")")); 
		    			String[] methodParameters = contentsmethodParameter.split(",");
		    			if (methodParameters.length == numberOfArguments) {
		    				matchingMethods1.add(method);
		    			}
	    			}
	    		} 
	    		if (matchingMethods1.size() == 0)
	    			return result;
	    		if (matchingMethods1.size() == 1)
	    			return matchingMethods1.get(0);
    			
    			// If there is only one method where the first parameter type == the first argument type, then return this method  
    			List<FamixMethod> matchingMethods2 = new ArrayList<FamixMethod>();
    			if (numberOfArguments >= 1) {
    				// Replace the argument string by a type, in case of an attribute
    				invocationArguments[0] = getTypeOfAttribute(fromClass, fromMethod, invocationArguments[0]);
    	    		for (FamixMethod matchingMethod1 : matchingMethods1){
    	    			String contentsmethodParameter = matchingMethod1.signature.substring(matchingMethod1.signature.indexOf("(") + 1, matchingMethod1.signature.indexOf(")")); 
    	    			String[] methodParameters = contentsmethodParameter.split(",");
    	    			if (methodParameters.length >= 1) {
        	    			methodParameters[0] = getfullPathDeclaredType(fromClass, methodParameters[0]);
    	    				if (methodParameters[0].equals(invocationArguments[0])) {
    	    					matchingMethods2.add(matchingMethod1);
    	    				}
    	    			}
    	    			
    	    		} 
    	    		if (matchingMethods2.size() == 0)
    	    			return result;
    	    		if (matchingMethods2.size() == 1)
    	    			return matchingMethods2.get(0);
    			}
    			// If there is only one method where the second parameter type == the first argument type, then return this method  
    			if (numberOfArguments >= 2) {
    				matchingMethods1.clear();
    				// Replace the argument string by a type, in case of an attribute
    				invocationArguments[1] = getTypeOfAttribute(fromClass, fromMethod, invocationArguments[1]);
    	    		for (FamixMethod matchingMethod1 : matchingMethods2){
    	    			String contentsmethodParameter = matchingMethod1.signature.substring(matchingMethod1.signature.indexOf("(") + 1, matchingMethod1.signature.indexOf(")")); 
    	    			String[] methodParameters = contentsmethodParameter.split(",");
    	    			if (methodParameters.length >= 2) {
        	    			methodParameters[1] = getfullPathDeclaredType(fromClass, methodParameters[1]);
    	    				if (methodParameters[1].equals(invocationArguments[1])) {
    	    					matchingMethods1.add(matchingMethod1);
    	    				}
    	    			}
    	    			
    	    		} 
    	    		if (matchingMethods1.size() == 0)
    	    			return result;
    	    		if (matchingMethods1.size() == 1)
    	    			return matchingMethods1.get(0);
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
    				indirectInheritanceAssociations.addAll(indirectAssociations_AddIndirectInheritanceAssociation(directAssociation.from, directAssociation.to, directAssociation.lineNumber));
				}
			}
	        for (FamixAssociation indirectInheritanceAssociation : indirectInheritanceAssociations) {
	        	addToModel(indirectInheritanceAssociation);
	        }
        } catch (Exception e) {
	        this.logger.debug(new Date().toString() + " "  + e);
	        e.printStackTrace();
        }
    }

    private List<FamixAssociation> indirectAssociations_AddIndirectInheritanceAssociation(String from, String to, int lineNumber) {
    	List<FamixAssociation> indirectInheritanceAssociations = new ArrayList<FamixAssociation>();
		HashSet<String> foundInheritanceList = inheritanceAssociationsPerClass.get(to);
		if (foundInheritanceList != null) {
			for (String foundInheritance : foundInheritanceList){
				if (!foundInheritance.equals(to)){
					//Create a new association of type FamixInheritanceDefinition from association to superSuperClass
					FamixAssociation newAssociation = new FamixAssociation();
					newAssociation.from = from;
					newAssociation.to = foundInheritance;
					newAssociation.lineNumber = lineNumber;
					newAssociation.type = EXTENDS_INDIRECT;
					newAssociation.isIndirect = true;
					indirectInheritanceAssociations.add(newAssociation);
					if (inheritanceAssociationsPerClass.containsKey(newAssociation.to) ){
		    			indirectInheritanceAssociations.addAll(indirectAssociations_AddIndirectInheritanceAssociation(newAssociation.from, newAssociation.to, newAssociation.lineNumber));
					}
				}
			}
		}
		return indirectInheritanceAssociations;
    }

    // Returns the unique name of the superclass of the uniqueClassName that contains method variableName. 
    private String indirectAssociations_findSuperClassThatContainsMethod(String fromClass, String fromMethod, String uniqueClassName, String methodName) {
    	String result = "";
    	if (inheritanceAssociationsPerClass.containsKey(uniqueClassName)){
    		HashSet<String> inheritanceAssociations = inheritanceAssociationsPerClass.get(uniqueClassName);
    		for (String superClass : inheritanceAssociations){
    			if (superClass.equals(uniqueClassName)) {
    				break; // Otherwise, things go wrong with derived C# classes with the same name.
    			}
                FamixMethod foundMethod = findInvokedMethodOnName(fromClass, fromMethod, superClass, methodName);
                if (foundMethod != null) {
            		result = superClass;
            		break;
	            }
	            else {
	            	if (inheritanceAssociationsPerClass.containsKey(superClass)){
	            		result = indirectAssociations_findSuperClassThatContainsMethod(fromClass, fromMethod, superClass, methodName);
	            		break;
	            	}
	            }
    		} 
    	}
    	return result; 
    }
    
    // Returns the unique name of the superclass of the uniqueClassName that declares variable variableName. 
    private String indirectAssociations_findSuperClassThatDeclaresVariable(String uniqueClassName, String variableName) {
    	String result = "";
    	if (inheritanceAssociationsPerClass.containsKey(uniqueClassName)){
    		HashSet<String> inheritanceAssociations = inheritanceAssociationsPerClass.get(uniqueClassName);
    		for (String superClass : inheritanceAssociations){
    			if (superClass.equals(uniqueClassName)) {
    				break; // Otherwise, things go wrong with derived C# classes with the same name.
    			}
	    		String searchKey = superClass + "." + variableName;
            	if (theModel.structuralEntities.containsKey(searchKey)) {
            		result = superClass;
            		break;
	            }
	            else {
	            	if (inheritanceAssociationsPerClass.containsKey(superClass)){
	            		result = indirectAssociations_findSuperClassThatDeclaresVariable(superClass, variableName);
	            		break;
	            	}
	            }
    		} 
    	}
    	return result; 
    }
    
	private FamixInvocation indirectAssociations_AddIndirectInvocation(String type, String from, String to, int lineNumber, String belongsToMethod, String invocationName, String nameOfInstance, boolean isIndirect) {
		FamixInvocation newInvocation = new FamixInvocation();
		newInvocation.type = type;
		newInvocation.from = from;
		newInvocation.to = to;
		newInvocation.lineNumber = lineNumber;
		newInvocation.belongsToMethod = belongsToMethod;
		newInvocation.invocationName = invocationName;
		newInvocation.nameOfInstance = nameOfInstance;
		newInvocation.isIndirect = isIndirect;
		return newInvocation;
	}

	// Tries to find out if the given attributeName matches an attribute of the className or a super class.
	// Returns the unique name of the class that contains the attribute, or "" if no attribute is found.
	private String findAttribute (String className, String attributeName) {
    	String returnValue = "";
		String toString = className + "." + attributeName;
	    if (theModel.structuralEntities.containsKey(toString)) {
        	return className;
        } else { // Determine if attributeName is an inherited attribute 
    		String superClassName = indirectAssociations_findSuperClassThatDeclaresVariable(className, attributeName);
    		if ((superClassName != null) && !superClassName.equals("")) {
    			toString = superClassName + "." + attributeName;
    			if (theModel.structuralEntities.containsKey(toString)) {
                	return superClassName;
    			}
    		}
        }
       	return returnValue;
	}
	
	private String getTypeOfAttribute(String className, String fromMethod, String attributeName) {
    	String returnValue = attributeName;
    	// Find out if attributeName is a class or instance variable
    	String classOfAttribute = findAttribute(className, attributeName);
        if (!classOfAttribute.equals("")) {
    		FamixStructuralEntity entity = theModel.structuralEntities.get(classOfAttribute + "." + attributeName);
    		if (entity.declareType != null && !entity.declareType.equals("")){
    			return entity.declareType;
    		}
        }
    	// Find out if attributeName is a local variable
    	String searchKey = className + "." + fromMethod + "." + attributeName;
    	if (theModel.structuralEntities.containsKey(searchKey)) {
    		FamixStructuralEntity entity = theModel.structuralEntities.get(searchKey);
    		if (entity.declareType != null && !entity.declareType.equals("")){
    			return entity.declareType;
    		}
    	}
       	return returnValue;
	}
	
	private String getfullPathDeclaredType(String className, String typeName) {
    	String returnValue = typeName;
        // Try to replace the type based on imports
    	String typeString = findClassInImports(className, typeName);
        if (!typeString.equals("")) {
            return typeString;
        }
        // Find out or association.to refers to a type in the same package as the from class
    	typeString = "";
        String belongsToPackage = theModel.classes.get(className).belongsToPackage;
    	typeString = findClassInPackage(typeName, belongsToPackage);
        if (!typeString.equals("")) { 
            return typeString;
        }
       	return returnValue;
	}
}
