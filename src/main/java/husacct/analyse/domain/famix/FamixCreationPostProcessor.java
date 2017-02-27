package husacct.analyse.domain.famix;

import husacct.ServiceProvider;
import husacct.common.enums.DependencySubTypes;
import husacct.common.enums.DependencyTypes;
import husacct.common.enums.UmlLinkTypes;
import husacct.control.task.States;

import java.util.*;

import javax.naming.directory.InvalidAttributesException;

import org.apache.log4j.Logger;

class FamixCreationPostProcessor {

    private FamixModel theModel;
    private HashMap<String, ArrayList<FamixImport>> importsPerEntity;
    private HashMap<String, ArrayList<FamixMethod>> sequencesPerMethod;
    private HashMap<String, String> firstSuperClassPerClass; // get(uniqueClassName) will return the uniqueName of the internal base/super class (no interface, no external class).
    private HashMap<String, HashSet<String>> inheritanceAssociationsPerClass;
    private List<FamixAssociation> indirectAssociations = new ArrayList<>();
    private List<FamixInvocation> waitingDerivedAssociations = new ArrayList<>();

	//private ILocaleService husacctLocaleService = ServiceProvider.getInstance().getLocaleService();
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
		importsPerEntity = new HashMap<>();
	    
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
			        	foundImportsList = new ArrayList<>();
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
            	
            	/* Test helper
            	if (theContainingClass.contains("domain.direct.violating.AccessInstanceVariableLibraryClass")){
            		boolean breakpoint1 = true;
            	} */
            	
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
            	if (belongsToClassExists && !declareTypeExists && declareTypeHasValue) {
	            	String classFoundInImports = "";
                    classFoundInImports = findClassInImports(theContainingClass, entity.declareType);
                    if (!classFoundInImports.equals("")) {
	                	if (theModel.classes.containsKey(classFoundInImports) || theModel.libraries.containsKey("xLibraries." + classFoundInImports)) {
	                        entity.declareType = classFoundInImports;
	                        declareTypeExists = true;
	                	}
                	}
        		}

            	// Find out or the name refers to a type in the same package as the from class.
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
                
            	if (belongsToClassExists && (entity instanceof FamixAttribute)){ 
            		createFamixUmlLinkIfNeeded(entity, declareTypeExists);
                }
            	
            	if (belongsToClassExists && declareTypeHasValue) { // Entities with primitive types should not be filtered out, neither those with xLibrary type
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

	/* A FamixUmlLink contains the data necessary to report 1..1 and 1..* associations, e.g. as used in UML class diagrams. 
	 * If entity is an instance variable, and if typeInClassDiagram refers to a FamixClass 
     * or a FamixLibrary, a FamixUmlLink has to be created.
     * An instance variable: is an instance of FamixAttribute with hasClassScope = false.
     * If (isComposite = false), typeInClassDiagram can be set to the value of declareType.
     * If (isComposite = true) and (typeInClassDiagram != ""), repeat the procedure above to determine the type of typeInClassDiagram.
     */
    private void createFamixUmlLinkIfNeeded(FamixStructuralEntity entity, boolean declareTypeExists) {
    	boolean typeInClassDiagramRefersToClassOrLibrary = false;
    	FamixAttribute attribute = (FamixAttribute) entity;
		if (!attribute.hasClassScope) {
			if (!attribute.isComposite) {  
				if (declareTypeExists) {
					attribute.typeInClassDiagram = attribute.declareType;
					typeInClassDiagramRefersToClassOrLibrary = true;
				}
			} else {
				if ((attribute.typeInClassDiagram != null) && (!attribute.typeInClassDiagram.equals(""))) {
        			// Check if typeInClassDiagram refers to an existing class or library
        			if (theModel.classes.containsKey(attribute.typeInClassDiagram)) {
	            		typeInClassDiagramRefersToClassOrLibrary = true;
	            	} else {
	            		// Try to derive typeInClassDiagram from the unique name from the imports.
		            	String classFoundInImports = "";
	                    classFoundInImports = findClassInImports(attribute.belongsToClass, attribute.typeInClassDiagram);
	                    if (!classFoundInImports.equals("")) {
	                		attribute.typeInClassDiagram = classFoundInImports;
	                		typeInClassDiagramRefersToClassOrLibrary = true;
	                	}
	            	}
                	if (!typeInClassDiagramRefersToClassOrLibrary) {
                    	// Find out if typeInClassDiagram refers to a type in the same package as the from class.
                        String belongsToPackage = theModel.classes.get(attribute.belongsToClass).belongsToPackage;
                		String type = findClassInPackage(attribute.typeInClassDiagram, belongsToPackage);
                        if (!type.equals("")) {
	                		attribute.typeInClassDiagram = type;
	                		typeInClassDiagramRefersToClassOrLibrary = true;
                        }
                    }
				}
			}
        	if (typeInClassDiagramRefersToClassOrLibrary) {
        		if (theModel.libraries.containsKey("xLibraries." + attribute.typeInClassDiagram)) {
        			attribute.typeInClassDiagram = "xLibraries." + attribute.typeInClassDiagram;
        		}
        		// Create FamixUmlLink and add it to the FamixModel
        		FamixUmlLink newLink = new FamixUmlLink();
        		newLink.from = attribute.belongsToClass;
        		newLink.to = attribute.typeInClassDiagram;
        		newLink.attributeFrom = attribute.name;
        		newLink.type = UmlLinkTypes.ATTRIBUTELINK.toString();
        		newLink.isComposite = attribute.isComposite;
        		newLink.lineNumber = attribute.lineNumber;
        		addToModel(newLink);
        	}
		}
    }
    
    public void processBehaviouralEntities() {
    	sequencesPerMethod = new HashMap<>();
    	for (FamixBehaviouralEntity entity : theModel.behaviouralEntities.values()) {
            try {
            	boolean belongsToClassExists = false;
            	boolean declareTypeExists = false;
            	boolean declareTypeHasValue = false;
            	String theContainingClass = entity.belongsToClass;
            	
            	/* Test helper
            	if (theContainingClass.contains("BaseIndirect")){
            		boolean breakpoint1 = true;
            	} */
            	
            	// Check if belongsToClass refers to an existing class
            	if (theModel.classes.containsKey(theContainingClass)) {
            		belongsToClassExists = true;
            	}

            	// Objective: If FamixBehaviouralEntity.declaredReturnType is not the unique name of a FamixEntity (Class or Library), than replace it by the unique name of a FamixEntity it represents.
            	// 0) Check if declareType refers to an existing class or library
                if ((entity.declaredReturnType != null) && (!entity.declaredReturnType.equals(""))){ 
                	declareTypeHasValue = true;
	            	if (theModel.classes.containsKey(entity.declaredReturnType) || theModel.libraries.containsKey("xLibraries." + entity.declaredReturnType)) {
	            		declareTypeExists = true;
	            	}
                }

            	// Objective: If declareType is a name instead of a unique name,  than replace it by the unique name of a FamixEntity (Class or Library) it represents.
            	// 1) Try to derive declareType from the unique name from the imports.
            	if (belongsToClassExists && !declareTypeExists && declareTypeHasValue) {
                	if (!entity.declaredReturnType.contains(".")) {
		            	String classFoundInImports = "";
	                    classFoundInImports = findClassInImports(theContainingClass, entity.declaredReturnType);
	                    if (!classFoundInImports.equals("")) {
		                	if (theModel.classes.containsKey(classFoundInImports) || theModel.libraries.containsKey("xLibraries." + classFoundInImports)) {
		                        entity.declaredReturnType = classFoundInImports;
		                        declareTypeExists = true;
		                	}
	                	}
                	}
        		}

            	// 2) Find out or the name refers to a type in the same package as the from class.*/
            	if (belongsToClassExists && !declareTypeExists && declareTypeHasValue) {
                    String belongsToPackage = theModel.classes.get(theContainingClass).belongsToPackage;
            		String type = findClassInPackage(entity.declaredReturnType, belongsToPackage);
                    if (!type.equals("")) {
	                	if (theModel.classes.containsKey(type)) {
	                        entity.declaredReturnType = type;
	                        declareTypeExists = true;
	                	}
                    }
                }
            	
            	/* Test helper
            	if (!declareTypeExists && declareTypeHasValue) {
            		boolean testhelper = true;
            	} */
            	
                // Fill HashMap methodsPerClass. Fill this Hashmap also with methods whose declaredReturnType is not found to exist, to prevent false positives in case of two or more methods with the same name, but with different signatures and return types. 
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
	            		ArrayList<FamixMethod> newMethodsList = new ArrayList<>();
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
		firstSuperClassPerClass = new HashMap<>();
		inheritanceAssociationsPerClass = new HashMap<>();
		
		try{
			Iterator<FamixAssociation> iterator = theModel.waitingAssociations.iterator();
	        for (Iterator<FamixAssociation> i=iterator ; i.hasNext();) {
	        	boolean inheritanceAssociation = false;
	        	boolean fromExists = false;
            	boolean toExists = false;
            	boolean toHasValue = false;
	        	FamixAssociation association = i.next();
		        String uniqueNameFrom = association.from;

            	/* //Test helper
            	if (association.from.contains("CSharpTreeConvertController")){
            		if(association.lineNumber == 26) {
            			boolean breakpoint = true; }
            	} */

		        if (association instanceof FamixInheritanceDefinition){
		        	inheritanceAssociation = true;
		        }
            	if (inheritanceAssociation) {
			        if (theModel.classes.containsKey(uniqueNameFrom)) {
			        	fromExists = true;
			        }
	            	if (fromExists) {
				        if ((association.to != null) && (!association.to.equals(""))) {
				        	toHasValue = true;
					        if (theModel.classes.containsKey(association.to) || theModel.libraries.containsKey("xLibraries." + association.to)) {
					        	toExists = true;
					        }
				        }
				        // If association.to is not a unique name of an existing type, try to replace it by the complete unique name.
			        	// Determine the type of association.to, first based on imports, and second based on package contents.
			        	if (!toExists && toHasValue) {
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
		    		        if (theModel.classes.containsKey(association.to) || theModel.libraries.containsKey("xLibraries." + association.to)) {
		    		        	toExists = true;
		    		        }
		            	}
			        	if (toExists && !association.from.equals(association.to)) {
				        	// Add the inheritance association to the FamixModel 
			            	if (!theModel.associations.contains(association)) { 
			            		determineDependencyTypeAndOrSubType(association);
			            		addToModel(association);
			            	}
			            	
			        		if (theModel.classes.containsKey(association.to)) {
				            	//Add the inheritance to hashmap firstSuperClassPerClass, if association-to refers to an internal class (not an interface, not an external class).
				        		FamixClass superClass = theModel.classes.get(association.to);
				        		if (!superClass.isInterface) {
				        			firstSuperClassPerClass.put(association.from, association.to);
				        		}
				            	// Create a FamixUmlLink to enable inclusion of the inheritance association in a UML class diagram (not in case to = xLibrary)
				        		FamixUmlLink newLink = new FamixUmlLink();
				        		newLink.from = association.from;
				        		newLink.to = association.to;
				        		newLink.attributeFrom = "";
				        		if (association.subType.startsWith("Extends")) {
					        		newLink.type = UmlLinkTypes.INHERITANCELINK.toString();
				        		} else {
					        		newLink.type = UmlLinkTypes.IMPLEMENTSLINK.toString();
				        		}
				        		newLink.isComposite = false;
				        		newLink.lineNumber = association.lineNumber;
				        		addToModel(newLink);
			            	}
			            	
			            	// Fill the HashMap inheritanceAccociationsPerClass with inheritance dependencies to super classes or interfaces 
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
				            	foundInheritanceList = new HashSet<>();
				            	foundInheritanceList.add(foundInheritance.to);
				            	inheritanceAssociationsPerClass.put(uniqueNameFrom, foundInheritanceList);
			            	}
			        	}
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
        //this.logger.info(new Date().toString() + " Finished: processInheritanceAssociations()");
    }

	// Objective: Check references to FamixObjects and try to replace missing information (focusing on the to object).
    void processWaitingAssociations() {
		int numberOfConnectedViaImportOrPackage = 0;
		int numberOfConnectedViaAttribute = 0;
		int numberOfConnectedViaLocalVariable = 0;
		int numberOfConnectedViaMethod = 0;
		int numberofAssociationsAddedToModel = 0;

        for (FamixAssociation association : theModel.waitingAssociations) {
            try {
            	boolean fromExists = false;
            	boolean toExists = false;
            	boolean toHasValue = false;
            	boolean chainingInvocation = false;
            	String toRemainderChainingInvocation = "";
            	String toString = "";
            	FamixInvocation theInvocation = null;

            	// Check if association.from refers to an existing class and association.to contains a workable value
            	if (theModel.classes.containsKey(association.from)) {
            		fromExists = true;
            	}
                if ((association.to != null) && !association.to.equals("") && !association.to.trim().equals(".")){ 
                	toHasValue = true;
                	if (fromExists && (association instanceof FamixInvocation)) {
                        theInvocation = (FamixInvocation) association;
                        theInvocation.originalToString = theInvocation.to;
                	}
                }

                /* Test helper
            	if (fromExists && association.from.contains("CSharpTreeConvertController")) {
            		if (association.lineNumber == 26) {
    	    				boolean breakpoint = true;
        			}
            	} */

            	// Objective: If FamixAssociation.to is a name instead of a unique name, than replace it by the unique name of a FamixEntity (Class or Library) it represents. 

                // 1) Check if association.to contains keyword "superBaseClass" (e.g. in Java "super"). If so, replace it by the name of the super class.
            	if (fromExists && toHasValue && (association instanceof FamixInvocation) && association.to.startsWith("superBaseClass")) {
            		String superClass = indirectAssociations_findSuperClass_FirstAbove(association.from);
            		if (!superClass.equals("")) {
            			toRemainderChainingInvocation = association.to.replace("superBaseClass", "");
		            	String[] allSubstrings = association.to.split("\\.");
            			association.to = superClass;
            			toExists = true;
            			association.isInheritanceRelated = true;
                        if (toRemainderChainingInvocation.startsWith("(")) {
                        	association.type = "InvocConstructor";
                        	String superClassName = theModel.classes.get(superClass).name;
    		            	toString = allSubstrings[0];
    		            	toString = toString.replace("superBaseClass", superClassName);
		                	FamixMethod foundMethod = findInvokedMethodOnName(association.from, theInvocation.belongsToMethod, association.to, toString);
		    	            if (foundMethod != null) {
		    	            	theInvocation.usedEntity = foundMethod.uniqueName;
		    	            }
                        	// Improvement, make language independent: 1) get language; 2) husacctLocaleService.getTranslatedString(superJava); 
                        	theInvocation.statement = "super()";
                        } else {
                        	association.type = "AccessReference";
                        	theInvocation.statement = "super";
                        }
		            	if (allSubstrings.length > 1) {
		                	chainingInvocation = true;
		                	theInvocation.remainingToString = allSubstrings[1];
		                    for (int i = 2; i < allSubstrings.length ; i++) {
		                    	theInvocation.remainingToString = theInvocation.remainingToString + "." + allSubstrings[i];
		                    }
		            	}
            		}
            	}
            	
            	// 2) Check if association.to (or a part of it) represents the full path to an existing class or library
                if (fromExists && !toExists && toHasValue){ 
                	if (theModel.classes.containsKey(association.to) || theModel.libraries.containsKey("xLibraries." + association.to)) {
                		toExists = true; // E.g., in case of declarations. 
                 	} else { // Check if a part of association.to refers to an existing class or library.  
                        if ((association instanceof FamixInvocation) && association.to.contains(".")) {
			            	String[] allSubstrings = association.to.split("\\.");
			            	toString = allSubstrings[0];
			            	boolean isMethodInvocation = false;
		                    for (int i = 1; i < allSubstrings.length ; i++) {
		                    	toString += "."+ allSubstrings[i];
		                    	if (toString.contains("(")) {
		                    		toString = toString.substring(0, toString.indexOf("("));
		                    		isMethodInvocation = true;
		                    	}
		                    	if (theModel.classes.containsKey(toString)) { // Not for xLibraries, since there is no difference made between packages and classes.
	                                association.to = toString;
	                                theInvocation.statement = toString;
		                    		toExists = true;
		                        	if (isMethodInvocation) {
		                        		association.type = "InvocConstructor";
		    		                	FamixMethod foundMethod = findInvokedMethodOnName(association.from, theInvocation.belongsToMethod, association.to, allSubstrings[i]);
		    		    	            if (foundMethod != null) {
		    		    	            	theInvocation.usedEntity = foundMethod.uniqueName;
		    		    	            }

		                        	} else {
		                        		association.type = "AccessReference";
		                        	}
				                    // Put the remainder in a variable; needed to create a separate indirect association later on the remainder substrings
		                    		if (allSubstrings.length > i + 1) {
			                    		chainingInvocation = true;
		                    			i++;
			                    		toRemainderChainingInvocation = allSubstrings[i];
					                    for (int j = i + 1; j < allSubstrings.length ; j++) {
					                    	toRemainderChainingInvocation = toRemainderChainingInvocation + "." + allSubstrings[j];
					                    }
					                    theInvocation.remainingToString = toRemainderChainingInvocation;
		                    		}
		                    		break;
		                    	}

		                    }
                        }
                 	}
                }

                /* 3) Select and process FamixInvocations with a composed to-name
                 * If FamixAssociation.to is composed of substrings (a chaining assignment or call), a dependency to the type of the first substring is a direct dependency.
                 * Dependencies to types of the following substrings are determined afterwards. The next one is indirect, if the first substring is a method or variable of a super class, otherwise it is direct.
                 * Algorithm: Split the string and try to identify the type of the first substring. Create a separate association to identify dependencies to following substring (variables or methods). 
                 * If the type of the first substring is identified, replace the substring by the type in the next association, and store this association to be processed later on.   
                 * */
                if (fromExists && !toExists && toHasValue){
                	if (association instanceof FamixInvocation) {
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
	                        theInvocation.statement = allSubstrings[0];
	                        theInvocation.remainingToString = toRemainderChainingInvocation;
                        }
                    }
                }
                
                // 4) Find out if association.to is a type reference to an imported type, or a type in the same package as the from class; both including nested classes.
                if (fromExists && !toExists && toHasValue){
            	    toString = findClassInImports(association.from, association.to); // 4.1) Find out if association.to refers to an imported type
                    if (toString.equals("")) {
                    	String belongsToPackage = theModel.classes.get(association.from).belongsToPackage;
                        toString = findClassInPackage(association.to, belongsToPackage); // 4.2) Find out if association.to refers to a type in the same package as the from class
                    }
                    if (!toString.equals("")) {
                        association.to = toString;
            			toExists = true;
            			numberOfConnectedViaImportOrPackage ++;
                    } else if (association.to.contains(".")) { // 4.3) Find out if association.to refers to an inner class of an imported type or a type in the same package 
		            	String[] allSubstrings = association.to.split("\\.");
		            	if (allSubstrings.length > 1) {
		            		String searchString = "";
		                    for (int s = 0; s < allSubstrings.length -1 ; s++) {
		                    	searchString = searchString + allSubstrings[s];
			                    toString = findClassInImports(association.from, searchString);
			                    if (toString.equals("")) {
			                    	String belongsToPackage = theModel.classes.get(association.from).belongsToPackage;
			                        toString = findClassInPackage(searchString, belongsToPackage);
			                    }
			                    if (!toString.equals("")) {
			                    	String concTo = toString + "." + allSubstrings[1];
				                    for (int i = 2; i < allSubstrings.length ; i++) {
				                    	concTo = concTo + "." + allSubstrings[i];
				                    }
			                    	if (theModel.classes.containsKey(concTo) || theModel.libraries.containsKey("xLibraries." + toString)) {
				                        association.to = concTo;
			                			toExists = true;
			                			numberOfConnectedViaImportOrPackage ++;
			                			break;
			                    	}
			                    }
			                    searchString = searchString + ".";
		                    }
		            	}
                	}
                	if (toExists && (association instanceof FamixInvocation)) {
                		association.type = "AccessReference";
                	}
                }
	                    
            	// 5) Determine if association.to is a variable (inherited, local variable, parameter ... ). If so determine the type of the attribute. 
                if (fromExists && !toExists && toHasValue && !association.to.endsWith(")")){
	                if ((association instanceof FamixInvocation)) {
	                	FamixStructuralEntity entity = null;
	    	        	// 5.1 Determine if association.to refers to an attribute (or class annotation)
	                	String classOfAttribute = findAttribute(association.from, association.to);
	    	            if (!classOfAttribute.equals("")) {
	    	        		entity = theModel.structuralEntities.get(classOfAttribute + "." + association.to);
    	                	theInvocation.usedEntity = entity.uniqueName;
		    	        	// 5.2 Determine if association.to refers to an inherited attribute
		        			if (!classOfAttribute.equals(association.from)) { // classOfAttribute refers to a super class  
		        				association.isInheritanceRelated = true;
		        				association.isIndirect = true;
			                	// Create an access dependency on the superclass.
			    				FamixInvocation newInvocation = indirectAssociations_AddIndirectInvocation(theInvocation, association.type, classOfAttribute, theInvocation.usedEntity, "", true);
			    				newInvocation.isInheritanceRelated = true;
				        		newInvocation.type = determineDependencyTypeBasedOnFoundAttribute(entity);
			            		determineDependencyTypeAndOrSubType(newInvocation);
			    				addToModel(newInvocation);  
				    			numberOfDerivedAssociations ++;
		                    }
	    	        		if (entity.declareType != null && !entity.declareType.equals("")){
	    	        			association.to = entity.declareType;
	        	            	numberOfConnectedViaAttribute++;
	    	        		}
	    	        	// 5.3 Find out or association.to refers to a local variable or parameter: Get StructuralEntity on key ClassName.MethodName.VariableName
	    	            } else { 
    		            	String searchKey = association.from + "." + theInvocation.belongsToMethod + "." + theInvocation.to;
    	                	if (theModel.structuralEntities.containsKey(searchKey)) {
    	                		entity = theModel.structuralEntities.get(searchKey);
    	                		theInvocation.usedEntity = entity.uniqueName;
    	                		if (entity.declareType != null && !entity.declareType.equals("")){
    	                			association.to = entity.declareType;
    		            			numberOfConnectedViaLocalVariable ++;
    	                		}
    	                	}
    	        		}
	    	            // 5.4 Finalize (for all types of attributes)
	    	            if (entity != null && (entity.declareType != null) && !entity.declareType.equals("")){
    	        			if (chainingInvocation) { 
    	                		// The invocation is not added to the model yet, because it reflects an invisible access of the type of variable. Creating a new derived invocation is redundant.
	    	                	association.type = "Undetermined";
    	    	            	association.isInheritanceRelated = false;
    	    	            	theInvocation.usedEntity = "";
	    	                	waitingDerivedAssociations.add(theInvocation);
    	                	} else {
								// The invocation is added  as an access invocation.
    	                		association.type = "AccessReference_TypeOfVariable";
    	                		toExists = true;
							}
    	        		}
	                }
                }
 
            	// 6) Determine if association.to is an (inherited) method. If so determine the return type of the method. 
	            if (fromExists && !toExists && toHasValue && (association.to.endsWith(")"))){
                    if (association instanceof FamixInvocation) {
    	            	// 6.1 Check if the invocation is a constructor call
    	            	String methodNameWithoutSignature = association.to.substring(0, association.to.indexOf("("));
	                    toString = findClassInImports(association.from, methodNameWithoutSignature);
	                    if (toString.equals("")) {
	                        String belongsToPackage = theModel.classes.get(association.from).belongsToPackage;
	                        toString = findClassInPackage(methodNameWithoutSignature, belongsToPackage);
	                    }
	                    if (!toString.equals("")) {
		                	FamixMethod foundMethod = findInvokedMethodOnName(association.from, theInvocation.belongsToMethod, toString, association.to);
		    	            if (foundMethod != null) {
		    	            	theInvocation.usedEntity = foundMethod.uniqueName;
		    	            }
		    	            association.to = toString;
    	            		association.type = "InvocConstructor";
	            			toExists = true;
	    	            	numberOfConnectedViaMethod++;
	                    }
	                    
	                    // If the method is no constructor, try to find the method. 
                    	if (!toExists) {
		                	boolean methodFound = false;
		                	// 6.2 Determine if association.to is a normal method.
		                	FamixMethod foundMethod = findInvokedMethodOnName(association.from, theInvocation.belongsToMethod, association.from, association.to);
		    	            if (foundMethod != null) {
		    	            	methodFound = true;
		    	            	theInvocation.usedEntity = foundMethod.uniqueName;
		    	            } else { // 6.3 Determine if association.to is an inherited method. 
		    	        		String superClassName = indirectAssociations_findSuperClassThatContainsMethod(association.from, theInvocation.belongsToMethod, association.from, association.to);
		    	        		if ((superClassName != null) && !superClassName.equals("")) {
		    	                	foundMethod = findInvokedMethodOnName(association.from, theInvocation.belongsToMethod, superClassName, association.to);
		    	    	            if (foundMethod != null) {
		    	    	            	methodFound = true;
				    	            	theInvocation.usedEntity = foundMethod.uniqueName;
		    	    	            	association.isInheritanceRelated = true;
		    	    	            	association.isIndirect = true;
		    		                	// Create a call dependency on the superclass;
		    		    				FamixInvocation newInvocation = indirectAssociations_AddIndirectInvocation(theInvocation, "InvocInstanceMethod", superClassName, theInvocation.usedEntity,  "", true);
		    		    				newInvocation.isInheritanceRelated = true;
		    		    				newInvocation.type = determineDependencyTypeBasedOnFoundMethod(foundMethod);
					            		determineDependencyTypeAndOrSubType(newInvocation);
		    		                	addToModel(newInvocation);  
		    			    			numberOfDerivedAssociations ++;
		    	                    }
		    	    			}
		    	            }
		    	            if (methodFound) {
		    	            	// Set association.type
    		    				association.type = determineDependencyTypeBasedOnFoundMethod(foundMethod);
		    	            	// Determine the return type of the method.
		    	                if ((foundMethod != null) && (foundMethod.declaredReturnType != null) && !foundMethod.declaredReturnType.equals("")) {
		    	                	association.to = foundMethod.declaredReturnType;
		        	            	numberOfConnectedViaMethod++;
		    	                	if (chainingInvocation) { 
		    	                		// The invocation is not added to the model yet, to prevent redundant dependencies on the return type of the method.
			    	                	association.type = "Undetermined";
		    	    	            	association.isInheritanceRelated = false;
		    	    	            	theInvocation.usedEntity = "";
			    	                	waitingDerivedAssociations.add(theInvocation);
		    	                	} else {
										// The invocation is added  as an indirect access invocation to the return value of the method.
			    	                	association.type = "AccessReference_ReturnType";
				            			toExists = true;
									}
		    	        		}
		    	        	}
                    	}
	             	}
	            }
                
	            // 7) Finalize (for all types of associations)
                if (fromExists && toExists) {
					if (!association.from.equals(association.to) && (theModel.classes.containsKey(association.to) || theModel.libraries.containsKey("xLibraries." + association.to))) {
        				if (association.type.startsWith("Access") & chainingInvocation) {
	                		// The invocation is not added to the model yet, to avoid redundant access dependencies: this association (for the first string) only refers to a type.
        					// A derived association is created below, which will result in an access or call dependency. 
        				} else {
		    				determineDependencyTypeAndOrSubType(association);
							addToModel(association);
							numberofAssociationsAddedToModel ++;
        				}
					} else {
	        			// Do nothing
					}
					if (association instanceof FamixInvocation) {
						if (chainingInvocation) { // If true, create an association to identify dependencies to the remaining parts of the chain. Store it temporarily; it is processed in a separate method. 
		    				FamixInvocation derivedInvocation = indirectAssociations_AddIndirectInvocation(theInvocation, "Undetermined", theInvocation.to, "", theInvocation.remainingToString, false);
		                    waitingDerivedAssociations.add(derivedInvocation);
						} else {
		        			// Do nothing
						}
					}
                } else {
                	if (!chainingInvocation) { 
                		numberOfNotConnectedWaitingAssociations ++;
                	} else {
                		// Do nothing
                	}
                	
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
    		determineDependencyTypeAndOrSubType(indirectAssociation);
        	addToModel(indirectAssociation);
        }

        String logString = " Connected via 1) Import or Package: " + numberOfConnectedViaImportOrPackage + ", 2) Variable: " + numberOfConnectedViaAttribute  
        		+ ", 3) Local var: " + numberOfConnectedViaLocalVariable + ", 4) Method: " +  numberOfConnectedViaMethod +  ", 5) Inherited var/method: " + numberOfDerivedAssociations + ", 6) added to Model: " + numberofAssociationsAddedToModel;
        /*this.logger.info(new Date().toString() + logString); */
        logString.trim();
    }
    
	// Objective: Identify dependencies to the remaining parts of the chain in a chaining invocation (assignment or call).
    void processWaitingDerivedAssociations() {
    	List<FamixInvocation> addedInvocations = new ArrayList<>();
    	for (FamixInvocation invocation : waitingDerivedAssociations) {
        	
        	/* Test helper
        	if (invocation.from.contains("org.eclipse.ui.internal.views.markers.MarkerFieldFilterGroup")){
        		//if (invocation.lineNumber == 522) {
        			int breakpoint = 1;
        		//}
        	} */

        	// 1) Split invocationName. Precondition: invocation.to is a type and invocationName contains the remainder of the string (determined in the previous process step).
        	boolean addInvocation = false;
    		String originalToType = invocation.to;
        	String nextToString = "";
    		boolean continueChaining = false;
        	String toTypeNewIndirectInvocation = "";
        	if (invocation.remainingToString.equals("")) {
        		// In case, the final indirect invocation in a chain is a reference to the type of the last attribute, or the return type of the last method.
				String typeNewIndirectInvocation = "AccessReference_TypeOfVariable";
				if (invocation.type.startsWith("Call")) {
					typeNewIndirectInvocation = "AccessReference_ReturnType";
				}
				invocation.type = typeNewIndirectInvocation;
        		addInvocation = true;
        	} else {
	            String[] allSubstrings = invocation.remainingToString.split("\\.");
	            invocation.remainingToString = "";
	            if ((allSubstrings.length > 0) && (!allSubstrings[0].equals(""))) {
	            	nextToString = allSubstrings[0];
		            invocation.statement = nextToString;
		            invocation.to = invocation.to + "." + nextToString; 
		            // Put the remainder in a variable; needed to create a separate indirect association, later on
		            if (allSubstrings.length > 1) {
		            	continueChaining = true;
		            	invocation.remainingToString = allSubstrings[1];
		                for (int i = 2; i < allSubstrings.length ; i++) {
		                	invocation.remainingToString = invocation.remainingToString + "." + allSubstrings[i];
		                }
		            }
	            }
        	}

        	/* 2) Determine if the nextToString is an (inherited) attribute. If so determine the type of the attribute. 
        	 * If this type refers to a FamixClass or FamixLibrary, set this type as invocation.to. */
        	if (! addInvocation) {
	            if (!invocation.to.endsWith(")")) {
		        	boolean attributeFound = false;
		        	boolean innerClassFound = false;
		        	String searchKey = invocation.to;
		            if (theModel.structuralEntities.containsKey(searchKey)) {
		            	attributeFound = true;
		            } else {
			            if (theModel.classes.containsKey(searchKey)) { // In case of reference of an inner class 
			            	innerClassFound = true; 
			            	attributeFound = true;
			            	if (continueChaining) { // If so, do add this reference to the model, but create a new invocation which will report the actual usage.
			    				FamixInvocation newInvocation = indirectAssociations_AddIndirectInvocation(invocation, "undetermined", invocation.to, "", invocation.remainingToString, false);
			                    addedInvocations.add(newInvocation);
			            	} else {
				    			invocation.type = "AccessReference";
			            		addInvocation = true;
			            	}
			            }
		            } 
			        if (!attributeFound) { // Determine if nextToString is an inherited attribute 
		        		String superClassName = indirectAssociations_findSuperClassThatDeclaresVariable(originalToType, nextToString);
		        		if ((superClassName != null) && !superClassName.equals("")) {
		        			searchKey = superClassName + "." + nextToString;
		        			if (theModel.structuralEntities.containsKey(searchKey)) {
		        				invocation.isInheritanceRelated = true;
		        				attributeFound = true;
			                	// Create an access dependency on the superclass;
				        		FamixStructuralEntity entity = theModel.structuralEntities.get(searchKey);
				        		String dependencyType = determineDependencyTypeBasedOnFoundAttribute(entity);
			    				FamixInvocation newInvocation = indirectAssociations_AddIndirectInvocation(invocation, dependencyType, superClassName, entity.uniqueName,  "", true);
			    				newInvocation.isInheritanceRelated = true;
				    			determineDependencyTypeAndOrSubType(newInvocation);
			                	addToModel(newInvocation);  
				    			numberOfDerivedAssociations ++;
		                    }
		    			}
		        	}
	            	// This current association should be added as an access dependency on the original to-type or on the superclass
	    			if (!innerClassFound) {
	    		        invocation.to = originalToType;
		    			invocation.type = "AccessVariable";
		    			addInvocation = true;
			            if (attributeFound) {
		        			// Determine if a next invocation in the chain needs to be created
			        		FamixStructuralEntity entity = theModel.structuralEntities.get(searchKey);
			    			invocation.usedEntity = entity.uniqueName;
			    			invocation.type = determineDependencyTypeBasedOnFoundAttribute(entity);
			        		if (entity.declareType != null && !entity.declareType.equals("")){
			        			toTypeNewIndirectInvocation = entity.declareType;
			        		} else {
			        			continueChaining = false;
			        		}
			        	}
	    			}
	            }
        	}

        	// 3) Determine if nextToString is an (inherited) method. 
            if ((!addInvocation) && invocation.to.endsWith(")")) {
            	// The invocation must be added as a method invocation.
    			invocation.to = originalToType; 
            	invocation.type = "InvocMethod";
    			addInvocation = true;
                // Try to find the method to determine the return type of the method.
                // 3.1 Determine if nextToString is a (default) constructor of an inner class. 
    			String searchKey2;
    			if (nextToString.indexOf("(") >= 0) {
	    			String methodNameWithoutSignature = nextToString.substring(0, nextToString.indexOf("("));
		        	searchKey2 = originalToType + "." + methodNameWithoutSignature;
	        	} else {
	        		searchKey2 = invocation.to; // Exceptional situation in case of errors in code, like in POI version 311.
	            	this.logger.warn(new Date().toString() + " Inconsistent input for method in: " + invocation.from + ", line: " + invocation.lineNumber);
	        	}
	        	if (theModel.classes.containsKey(searchKey2)){ // If so, nextToString is a constructor of an inner class.
	    			invocation.to = searchKey2;
	    			invocation.type = "InvocConstructor";
	    			toTypeNewIndirectInvocation = searchKey2;
	                FamixMethod foundMethod = findInvokedMethodOnName(invocation.from, invocation.belongsToMethod, invocation.to, nextToString);
	                if (foundMethod != null) {
		    			invocation.usedEntity = foundMethod.uniqueName;
	                }
		    	} else {   
	    			// 3.2 Determine if nextToString is a method of class originalToType 
	                boolean methodFound = false;
	                FamixMethod foundMethod = findInvokedMethodOnName(invocation.from, invocation.belongsToMethod, originalToType, nextToString);
	                if (foundMethod != null) {
		            	methodFound = true;
		    			invocation.usedEntity = foundMethod.uniqueName;
		            } else { // 3.3 Determine if nextToString is an inherited method 
		        		String superClassName = indirectAssociations_findSuperClassThatContainsMethod(invocation.from, invocation.belongsToMethod, originalToType, nextToString);
		        		if ((superClassName != null) && !superClassName.equals("")) {
		        			foundMethod = findInvokedMethodOnName(invocation.from, invocation.belongsToMethod, superClassName, nextToString);
		                    if (foundMethod != null) {
		        				methodFound = true;
				    			invocation.usedEntity = foundMethod.uniqueName;
		        				invocation.isInheritanceRelated = true;
			                	// Create a call dependency on the superclass;
			    				FamixInvocation newInvocation = indirectAssociations_AddIndirectInvocation(invocation, "InvocMethod", superClassName, invocation.usedEntity,  "", true);
			    				newInvocation.isInheritanceRelated = true;
			    				newInvocation.type = determineDependencyTypeBasedOnFoundMethod(foundMethod);
				    			determineDependencyTypeAndOrSubType(newInvocation);
			                	addToModel(newInvocation);  
				    			numberOfDerivedAssociations ++;
		                    }
		    			}
		            }
		            if (methodFound) { // If so, determine dependency type and determine the return type of the method.
		            	invocation.type = determineDependencyTypeBasedOnFoundMethod(foundMethod);
	        			// Determine if a next invocation in the chain needs to be created
		            	if (foundMethod.declaredReturnType != null && !foundMethod.declaredReturnType.equals("")){
		            		toTypeNewIndirectInvocation = foundMethod.declaredReturnType;
		        		} else {
		        			continueChaining = false;
		        		}
		        	}
		    	}
         	}
            
            if (addInvocation) { 
    			if (!invocation.from.equals(invocation.to) && (theModel.classes.containsKey(invocation.to) || theModel.libraries.containsKey("xLibraries." + invocation.to))) {
	    			determineDependencyTypeAndOrSubType(invocation);
    				addToModel(invocation);
	    			numberOfDerivedAssociations ++;
	    			if (continueChaining) { 
						// Create an indirect association to identify dependencies to the remaining parts of the chain. Store it temporarily; it is processed in a separate method. 
						FamixInvocation newIndirectInvocation = indirectAssociations_AddIndirectInvocation(invocation, "Undetermined", toTypeNewIndirectInvocation, "", invocation.remainingToString, true);
	                    addedInvocations.add(newIndirectInvocation);
					} else {
						if (invocation.remainingToString.equals("") && !toTypeNewIndirectInvocation.equals("") && !invocation.subType.equals("Constructor")) {
							// Create a final indirect invocation in a chain to the type of the last attribute or the return type of the last method.
							FamixInvocation newIndirectInvocation = indirectAssociations_AddIndirectInvocation(invocation, invocation.type, toTypeNewIndirectInvocation, invocation.usedEntity, invocation.remainingToString, true);
		                    addedInvocations.add(newIndirectInvocation);
						}
					}
    			}
    		}
    	}
    	if (addedInvocations.size() > 0) {
        	waitingDerivedAssociations.clear();
    		waitingDerivedAssociations.addAll(addedInvocations);
    		addedInvocations.clear();
        	// this.logger.info(new Date().toString() + " Number of derived Associations: " + numberOfDerivedAssociations);
    		processWaitingDerivedAssociations();
    	} else if ((waitingDerivedAssociations != null) && (waitingDerivedAssociations.size() > 0)) {
    		//FamixInvocation lastInvocatioInLargeChain = waitingDerivedAssociations.get(waitingDerivedAssociations.size() - 1);
        	// this.logger.info(new Date().toString() + " Last large chain in: " + lastInvocatioInLargeChain.from  + " , line: " + lastInvocatioInLargeChain.lineNumber);
        	waitingDerivedAssociations.clear();
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
    
    private void determineDependencyTypeAndOrSubType(FamixAssociation association) {
    	FamixClass theClass;
    	// Inheritance
    	if (Objects.equals(association.type, DependencyTypes.INHERITANCE.toString())) {
            theClass = theModel.classes.get(association.to);
            if (theClass != null) {
            	if (theClass.isInterface){
            		association.subType = DependencySubTypes.INH_IMPLEMENTS_INTERFACE.toString();
            	} else {
                    if (theClass.isAbstract) {
                    	association.subType = DependencySubTypes.INH_EXTENDS_ABSTRACT_CLASS.toString();
                    } else {
                        association.subType = DependencySubTypes.INH_EXTENDS_CLASS.toString();
                    }
            	}
            } else {
                if (theModel.libraries.containsKey("xLibraries." + association.to)) {
                	association.subType = DependencySubTypes.INH_FROM_LIBRARY_CLASS.toString();
                }
            }
        // Call
        } else if (association.type.startsWith("Invoc")) {
        	association.subType = DependencySubTypes.CALL_METHOD.toString();
        	theClass = theModel.classes.get(association.to);
            if (theClass != null) {
            	if (theClass.isInterface) {
            		association.subType = DependencySubTypes.CALL_INTERFACE_METH.toString();
            	} else if (theClass.isEnumeration) {
            		association.subType = DependencySubTypes.CALL_ENUM_METH.toString();
            	} else {
                    switch(association.type) {
    	            case "InvocConstructor":
    	            	association.subType = DependencySubTypes.CALL_CONSTRUCTOR.toString();
    	            	break;
    	            case "InvocClassMethod":
    	            	association.subType = DependencySubTypes.CALL_CLASS_METH.toString();
    	            	break;
    	            case "InvocInstanceMethod":
    	            	association.subType = DependencySubTypes.CALL_INSTANCE_METH.toString();
    	            	break;
                    }
                }
            } else {
                if (theModel.libraries.containsKey("xLibraries." + association.to)) {
                	association.subType = DependencySubTypes.CALL_LIBARRY_METH.toString();
                }
            }
            association.type = DependencyTypes.CALL.toString();
        // Access
        } else if (association.type.startsWith("Access")) {
        	if (association.type.startsWith("AccessReference")) {
    			association.subType = DependencySubTypes.REF_TYPE.toString();
	    		if (association.type.equals("AccessReference_ReturnType")) {
	    			association.subType = DependencySubTypes.REF_RETURN_TYPE.toString();
	    		} else if (association.type.equals("AccessReference_TypeOfVariable")) {
	    			association.subType = DependencySubTypes.REF_TYPE_OF_VAR.toString();
	    		}
	    		association.type = DependencyTypes.REFERENCE.toString();
        	} else {
            	association.subType = DependencySubTypes.ACC_VARIABLE.toString();
	        	theClass = theModel.classes.get(association.to);
	            if (theClass != null) {
	            	if (theClass.isInterface) {
	            		association.subType = DependencySubTypes.ACC_INTERFACE_VAR.toString();
	            	} else if (theClass.isEnumeration) {
		            		association.subType = DependencySubTypes.ACC_ENUMERATION_VAR.toString();
	            	} else {
	                    switch(association.type) {
	        	            case "AccessInstanceAttribute":
	        	            	association.subType = DependencySubTypes.ACC_INSTANCE_VAR.toString();
	        	            	break;
	        	            case "AccessInstanceAttributeConstant":
	        	            	association.subType = DependencySubTypes.ACC_INSTANCE_VAR_CONST.toString();
	        	            	break;
	        	            case "AccessClassAttribute":
	        	            	association.subType = DependencySubTypes.ACC_CLASS_VAR.toString();
	        	            	break;
	        	            case "AccessClassAttributeConstant":
	        	            	association.subType = DependencySubTypes.ACC_CLASS_VAR_CONST.toString();
	        	            	break;
	                    }
	            	}
	            } else {
	                if (theModel.libraries.containsKey("xLibraries." + association.to)) {
	                	association.subType = DependencySubTypes.ACC_LIBRARY_VAR.toString();
	                }
	            }
	            association.type = DependencyTypes.ACCESS.toString();
        	}
            // Declaration
        } else if (association.type.startsWith("Declaration")) {
        	if (association.subType.equals("Type Cast")) { 
	    		association.type = DependencyTypes.REFERENCE.toString();
        	}
        }
    }
    
    private String determineDependencyTypeBasedOnFoundMethod(FamixMethod foundMethod) {
    	String returnValue = "Invoc";
		if (foundMethod.hasClassScope) {
			returnValue = "InvocClassMethod";
		} else {
			returnValue = "InvocInstanceMethod";
		}
		return returnValue;
    }

    private String determineDependencyTypeBasedOnFoundAttribute(FamixStructuralEntity foundEntity) {
    	String returnValue = "Access";
    	if (foundEntity instanceof FamixAttribute) {
	    	FamixAttribute foundAttribute= (FamixAttribute) foundEntity;
	    	if (foundAttribute.hasClassScope) {
				if (foundAttribute.isFinal) {
					returnValue = "AccessClassAttributeConstant";
					
				} else {
					returnValue = "AccessClassAttribute";
				}
			} else {
				if (foundAttribute.isFinal) {
					returnValue = "AccessInstanceAttributeConstant";
					
				} else {
					returnValue = "AccessInstanceAttribute";
				}
			}
    	}
		return returnValue;
    }
    
    // Tries to find the specific method
    // Returns FamixMethod if one unique method is found. Else, it returns null. 
    private FamixMethod findInvokedMethodOnName(String fromClass, String fromMethod, String invokedClassName, String invokedMethodName) {
    	FamixMethod nullResult = null;
    	if (invokedClassName == null || invokedClassName.isEmpty()
    			|| invokedMethodName == null || invokedMethodName.isEmpty() || !invokedMethodName.contains("(")) {
    		return nullResult;
    	}
		/* Test Helper
		if (fromClass.equals("domain.direct.violating.AccessLocalVariable_SetArgumentValue")){
			boolean breakpoint = true;
		} */

    	// 1) If methodNameAsInInvocation matches with a method unique name, return that method. 
    	String searchKey = invokedClassName + "." + invokedMethodName;
    	if (theModel.behaviouralEntities.containsKey(searchKey)) {
    		return (FamixMethod) theModel.behaviouralEntities.get(searchKey);
    	}
    	// 2) Find out if there are more methods with the same name of the invoked class. If only one method is found, then return this method.  
    	String methodName = invokedMethodName.substring(0, invokedMethodName.indexOf("(")); // Methodname without signature
    	searchKey = invokedClassName + "." + methodName;
    	if (sequencesPerMethod.containsKey(searchKey)){
    		ArrayList<FamixMethod> methodsList = sequencesPerMethod.get(searchKey);
    		if (methodsList.size() == 0) {
    			return nullResult;
    		} else if (methodsList.size() == 1) {
    			// FamixMethod result1 = methodsList.get(0);
    			return methodsList.get(0);
    		} else { // 3) if there are more methods with the same name, then compare the invocation arguments with the method parameters.
    			String invocationSignature = invokedMethodName.substring(invokedMethodName.indexOf("("));;
    			String contentsInvocationSignature = invocationSignature.substring(invocationSignature.indexOf("(") + 1, invocationSignature.indexOf(")")); 
    			String[] invocationArguments = contentsInvocationSignature.split(",");
    			int numberOfArguments = invocationArguments.length;
    			// 3a) If there is only one method with the same number of parameters as invocationArguments, then return this method
    			List<FamixMethod> matchingMethods1 = new ArrayList<>();
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
	    			return nullResult;
	    		if (matchingMethods1.size() == 1)
	    			return matchingMethods1.get(0);
    			
    			// 3b) If there is only one method where the first parameter type == the first argument type, then return this method  
    			List<FamixMethod> matchingMethods2 = new ArrayList<>();
    			if (numberOfArguments >= 1) {
    				// Replace the argument string by a type, in case of an attribute
    				invocationArguments[0] = getTypeOfAttribute(fromClass, fromMethod, invocationArguments[0]);
    	    		for (FamixMethod matchingMethod1 : matchingMethods1){
    	    			String contentsmethodParameter = matchingMethod1.signature.substring(matchingMethod1.signature.indexOf("(") + 1, matchingMethod1.signature.indexOf(")")); 
    	    			String[] methodParameters = contentsmethodParameter.split(",");
    	    			if (methodParameters.length >= 1) {
        	    			methodParameters[0] = getfullPathOfDeclaredType(fromClass, methodParameters[0]);
    	    				if (methodParameters[0].equals(invocationArguments[0])) {
    	    					matchingMethods2.add(matchingMethod1);
    	    				}
    	    			}
    	    			
    	    		} 
    	    		if (matchingMethods2.size() == 0)
    	    			return nullResult;
    	    		if (matchingMethods2.size() == 1)
    	    			return matchingMethods2.get(0);
    			}
    			// If there is only one method where the second parameter type == the first argument type, then return this method  
    			if (numberOfArguments >= 2) {
    				matchingMethods1.clear();
    				// Replace the argument string by a type, in case of an attribute
    				invocationArguments[1] = getTypeOfAttribute(fromClass, fromMethod, invocationArguments[1]);
    	    		for (FamixMethod matchingMethod2 : matchingMethods2){
    	    			String contentsmethodParameter = matchingMethod2.signature.substring(matchingMethod2.signature.indexOf("(") + 1, matchingMethod2.signature.indexOf(")")); 
    	    			String[] methodParameters = contentsmethodParameter.split(",");
    	    			if (methodParameters.length >= 2) {
        	    			methodParameters[1] = getfullPathOfDeclaredType(fromClass, methodParameters[1]);
    	    				if (methodParameters[1].equals(invocationArguments[1])) {
    	    					matchingMethods1.add(matchingMethod2);
    	    				}
    	    			}
    	    			
    	    		} 
    	    		if (matchingMethods1.size() == 0)
    	    			return nullResult;
    	    		if (matchingMethods1.size() == 1)
    	    			return matchingMethods1.get(0);
    			}
    		}
		} 
    	return nullResult; 
    }
    
    private String findClassInImports(String importingClass, String typeDeclaration) {
    	if (theModel.classes.containsKey(importingClass)) {
	    	FamixClass fromClass = theModel.classes.get(importingClass);
	    	if (fromClass.isInnerClass) {
	    		importingClass = fromClass.belongsToClass;
	    	}
    	}
    	List<FamixImport> importsOfClass = importsPerEntity.get(importingClass);
    	if (importsOfClass != null){
	        for (FamixImport fImport : importsOfClass) {
	            if (!fImport.importsCompletePackage) {
	                if (fImport.to.endsWith("." + typeDeclaration)) {
	            		return fImport.to;
	                } else {
	                	FamixClass importedClass = theModel.classes.get(fImport.to);
	        			if (importedClass != null){
	        				if (importedClass.hasInnerClasses){
	        					for (String innerClass : importedClass.children) {
		        	                if (innerClass.endsWith("." + typeDeclaration)) {
		        	                    return innerClass;
		        	                }
	        					}
	        				}
	        			}
	                }
	            } else {
		            // If the import refers to a complete package or namespace, try to find a class with a matching name within this package
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
        List<String> result = new ArrayList<>();
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
        	List<FamixAssociation> indirectInheritanceAssociations = new ArrayList<>();
	        for (FamixAssociation directAssociation : theModel.associations) {
                if (directAssociation.to == null || directAssociation.from == null || directAssociation.to.equals("") || directAssociation.from.equals("")){ 
                	numberOfNotConnectedWaitingAssociations ++;
                }
                else if (directAssociation instanceof FamixInheritanceDefinition){ 
    				indirectInheritanceAssociations.addAll(indirectAssociations_AddIndirectInheritanceAssociation(directAssociation.from, directAssociation.to, directAssociation.lineNumber));
				}
			}
	        for (FamixAssociation indirectInheritanceAssociation : indirectInheritanceAssociations) {
        		determineDependencyTypeAndOrSubType(indirectInheritanceAssociation);
	        	addToModel(indirectInheritanceAssociation);
	        }
        } catch (Exception e) {
	        this.logger.debug(new Date().toString() + " "  + e);
	        e.printStackTrace();
        }
    }

    private List<FamixAssociation> indirectAssociations_AddIndirectInheritanceAssociation(String from, String to, int lineNumber) {
    	List<FamixAssociation> indirectInheritanceAssociations = new ArrayList<>();
		HashSet<String> foundInheritanceList = inheritanceAssociationsPerClass.get(to);
		if (foundInheritanceList != null) {
			for (String foundInheritance : foundInheritanceList){
				//FamixClass superSuperClass = theModel.classes.get(foundInheritance);
				if (!foundInheritance.equals(to)){ // To exclude interfaces, add: && !superSuperClass.isInterface
					//Create a new association of type FamixInheritanceDefinition from association to superSuperClass
					FamixAssociation newAssociation = new FamixAssociation();
					newAssociation.from = from;
					newAssociation.to = foundInheritance;
					newAssociation.lineNumber = lineNumber;
					newAssociation.type = "Inheritance";
					newAssociation.isIndirect = true;
					newAssociation.isInheritanceRelated = true;
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
    		for (String stringSuper : inheritanceAssociations){
    			FamixClass superClass = theModel.classes.get(stringSuper);
    			if ((superClass != null) && !superClass.isInterface) {
	    			if (stringSuper.equals(uniqueClassName)) {
	    				break; // Otherwise, things go wrong with derived C# classes with the same name.
	    			}
	                FamixMethod foundMethod = findInvokedMethodOnName(fromClass, fromMethod, stringSuper, methodName);
	                if (foundMethod != null) {
	            		result = stringSuper;
	            		break;
		            }
		            else {
		            	if (inheritanceAssociationsPerClass.containsKey(stringSuper)){
		            		result = indirectAssociations_findSuperClassThatContainsMethod(fromClass, fromMethod, stringSuper, methodName);
		            		break;
		            	}
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
    
    private String indirectAssociations_findSuperClass_FirstAbove(String uniqueClassName) {
    	String result = "";
    	if (firstSuperClassPerClass.containsKey(uniqueClassName)) { 
    		result = firstSuperClassPerClass.get(uniqueClassName);
    	}
    	return result;     	
    }
    
	private FamixInvocation indirectAssociations_AddIndirectInvocation(FamixInvocation theInvocation, String type, String to, String usedEntity, String remainingToString, boolean isIndirect) {
		FamixInvocation newInvocation = new FamixInvocation();
		newInvocation.type = type;
		newInvocation.from = theInvocation.from;
		newInvocation.to = to;
		newInvocation.lineNumber = theInvocation.lineNumber;
		newInvocation.belongsToMethod = theInvocation.belongsToMethod;
		newInvocation.usedEntity = usedEntity;
		newInvocation.remainingToString = remainingToString;
		newInvocation.originalToString = theInvocation.originalToString;
		newInvocation.statement = theInvocation.statement;
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
	
	private String getfullPathOfDeclaredType(String className, String typeName) {
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
