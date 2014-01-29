package husacct.analyse.domain.famix;

import husacct.ServiceProvider;
import husacct.control.task.States;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.naming.directory.InvalidAttributesException;

import org.apache.log4j.Logger;

class FamixDependencyConnectorNew {

    private static final String EXTENDS = "Extends";
    private static final String EXTENDS_LIBRARY = "ExtendsLibrary";
    private static final String EXTENDS_ABSTRACT = "ExtendsAbstract";
    private static final String EXTENDS_CONCRETE = "ExtendsConcrete";
    private static final String EXTENDS_INTERFACE = "ExtendsInterface";
    private FamixModel theModel;
    private HashMap<String, ArrayList<FamixImport>> importsPerEntity;
    private HashMap<String, ArrayList<FamixAssociation>> inheritanceAccociationsPerClass;
    private ArrayList<FamixFormalParameter> parametersArrayList;
    private HashMap<String, FamixStructuralEntity> structuralEntityHashMap;
    private final Logger logger = Logger.getLogger(FamixDependencyConnector.class);
    private int numberOfNotConnectedWaitingAssociations;
    //Needed for the ProgressBar of the analyse application LoaderDialog 
    private int amountOfModulesConnected;
    private int progressPercentage;
    private int numberOfWaitingObjects; 

    public FamixDependencyConnectorNew() {
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
		initializeHashMapstructuralEntityHashMap();
		
        for (FamixStructuralEntity entity : theModel.waitingStructuralEntitys) {
            try {
            	theClass = entity.belongsToClass;
                if (!isCompleteTypeDeclaration(entity.declareType)) {
                    classFoundInImports = findClassInImports(theClass, entity.declareType);
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
            	this.logger.debug(new Date().toString() + " Exception:  " + e);
            	e.printStackTrace();
            }
        }
    }

    void connectAssociationDependencies() {
		String theClass;
		String belongsToPackage;
		String classFoundInImports;
		String to;
		FamixInvocation theInvocation;
		
        for (FamixAssociation association : theModel.waitingAssociations) {
            try {
            	classFoundInImports = "";
                boolean connected = false;
                if (association.to == null || association.from == null || association.to.equals("") || association.from.equals("")){ 
                	numberOfNotConnectedWaitingAssociations ++;
                }
                else {
                	theClass = association.from;
                	if (!isCompleteTypeDeclaration(association.to)) {
	                    classFoundInImports = findClassInImports(theClass, association.to);
	                    if (!classFoundInImports.equals("")) {
	                        // So, in case association.to does not contain "." AND association.to is an import of association.from
	                        association.to = classFoundInImports;
	                        connected = true;
	                    } else {
	                        belongsToPackage = getPackageFromUniqueClassName(association.from);
	                        to = findClassInPackage(association.to, belongsToPackage);
	                        if (!to.equals("")) {
	                            // So, in case association.to does not contain "." AND association.to shares the same package as association.from 
	                            association.to = to;
	                            connected = true;
	                        }
	                    }
	                    if (!connected) {
	                        if (isInvocation(association)) {
	                            theInvocation = (FamixInvocation) association;
	                            if (theInvocation.belongsToMethod == null || theInvocation.belongsToMethod.equals("")) {
	                                //Then it is an attribute assignment. Example: currentFunction = FinderArguments.ROOT; 
	                                theInvocation.to = getClassForAttribute(theInvocation.from, theInvocation.nameOfInstance);
	                            } else {
	                            	// get StructuralEntity on key belongsToClass/line/name on from/line/nameOfInstance
	                            	String searchKey = theInvocation.from + theInvocation.lineNumber + theInvocation.nameOfInstance;
	                            	if (structuralEntityHashMap.containsKey(searchKey)) {
	                            		FamixStructuralEntity entity = structuralEntityHashMap.get(searchKey);
	                            		if (entity.declareType != null && !entity.declareType.equals("")){
	                            			theInvocation.to = entity.declareType;
	                            			if (entity instanceof FamixAttribute){
	                            				String entityUniqueName = entity.uniqueName;
	                            				String entityDeclareType = entity.declareType;
	                            				String invocationTo = theInvocation.to;
	                            				String invocationFrom = theInvocation.from;
	                            			}
	                            			if (entity instanceof FamixLocalVariable){
	                            				String entityUniqueName = entity.uniqueName;
	                            				String entityDeclareType = entity.declareType;
	                            				String invocationTo = theInvocation.to;
	                            				String invocationFrom = theInvocation.from;
	                            			}
	                            			if (entity instanceof FamixImplicitVariable){
	                            				String entityUniqueName = entity.uniqueName;
	                            				String entityDeclareType = entity.declareType;
	                            				String invocationTo = theInvocation.to;
	                            				String invocationFrom = theInvocation.from;
	                            			}
	                            			if (entity instanceof FamixGlobalVariable){
	                            				String entityUniqueName = entity.uniqueName;
	                            				String entityDeclareType = entity.declareType;
	                            				String invocationTo = theInvocation.to;
	                            				String invocationFrom = theInvocation.from;
	                            			}
	                            			if (entity instanceof FamixFormalParameter){
	                            				String entityUniqueName = entity.uniqueName;
	                            				String entityDeclareType = entity.declareType;
	                            				String invocationTo = theInvocation.to;
	                            				String invocationFrom = theInvocation.from;
	                            			}
	                            		}
	                            	}
	                            }
	                        }
	                        if (association instanceof FamixAccess) {
	                            FamixAccess theAccess = (FamixAccess) association;
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
    	        this.logger.debug(new Date().toString() + " "  + e + " " + associationType + " " + association.toString());
    	        e.printStackTrace();
            }
        }
        addIndirectExtendAssociations();
    }
    
    public void addIndirectExtendAssociations() {
            try{
            	//Add indirect inheritance dependencies 
        		initializeHashMapsinheritanceAccociationsPerClass();
    	        for (FamixAssociation associationx : theModel.associations) {
                    if (associationx.to == null || associationx.from == null || associationx.to.equals("") || associationx.from.equals("")){ 
                    	numberOfNotConnectedWaitingAssociations ++;
                    }
                    else 	
		            	if (associationx.type.equals(EXTENDS_ABSTRACT) || associationx.type.equals(EXTENDS_CONCRETE)){ 
		    				FamixClass superClass = theModel.classes.get(associationx.to);
		    				//If there is an association of type FamixInheritanceDefinition with superClass.to
		    				if (inheritanceAccociationsPerClass.containsKey(associationx.to) ){
		    					ArrayList<FamixAssociation> foundInheritanceList = inheritanceAccociationsPerClass.get(associationx.to);
		    					for (FamixAssociation foundInheritance : foundInheritanceList){
		            				//Create a new association of type FamixInheritanceDefinition from association to superSuperClass
									FamixAssociation newAssociation = new FamixAssociation();
									newAssociation.from = associationx.from;
									newAssociation.to = foundInheritance.to;
									newAssociation.lineNumber = associationx.lineNumber;
									newAssociation.type = foundInheritance.type;
									addToModel(newAssociation);
	    					}
	    				}
	    			}
	        	}
                
            } 	catch (Exception e) {
    	        this.logger.debug(new Date().toString() + " "  + e);
    	        e.printStackTrace();
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
        if (type.equals(EXTENDS)) {
            FamixClass theClass = getClassForUniqueName(association.to);
            if (theClass != null) {
                if (theClass.isAbstract) {
                    type = EXTENDS_ABSTRACT;
                } else if (!theClass.isAbstract) {
                    type = EXTENDS_CONCRETE;
                }
            } else {
                FamixInterface theInterface = getInterfaceForUniqueName(association.to);
                if (theInterface != null) {
                    type = EXTENDS_INTERFACE;
                } else {
                    type = EXTENDS_LIBRARY;
                }

            }

        }
        association.type = type;
    }

    private FamixClass getClassForUniqueName(String uniqueName) {
        return theModel.classes.get(uniqueName);
    }

    private FamixInterface getInterfaceForUniqueName(String uniqueName) {
        return theModel.interfaces.get(uniqueName);
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

    private String getClassForParameter(String declareClass, String declareMethod, String attributeName) {
        HashMap<String, ArrayList<FamixFormalParameter>> parameterPerClassHashMap = null;
    	String belongsToMethodFull = declareClass + "." + declareMethod;
        ArrayList<FamixFormalParameter> paramsPerClass = parameterPerClassHashMap.get(declareClass);
        if (paramsPerClass != null){     
        for (FamixFormalParameter parameter : paramsPerClass) {
	            if (parameter.belongsToMethod.equals(belongsToMethodFull)) {
	                if (parameter.name.equals(attributeName)) {
	                    return parameter.declareType;
	                }
	            }
	        }
        }
        return "";
    }

    private String getClassForLocalVariable(String declareClass, String belongsToMethod, String nameOfInstance) {
		FamixStructuralEntity entity;
		FamixLocalVariable variable;
		
        for (String s : theModel.structuralEntities.keySet()) {
            if (!s.startsWith(declareClass)) {
                entity = (FamixStructuralEntity) theModel.structuralEntities.get(s);
                if (entity instanceof FamixLocalVariable) {
                    variable = (FamixLocalVariable) entity;
                    if (variable.belongsToMethod.equals(belongsToMethod) && variable.name.equals(nameOfInstance)) {
                            return variable.declareType;
                    }
                }
            }
        }
        return "";
    }

    private boolean isInvocation(FamixAssociation association) {
        return association instanceof FamixInvocation;
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
	                for (String uniqueClassName : getModulesInPackage(fImport.to)) {
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

    private boolean isCompleteTypeDeclaration(String typeDeclaration) {
        return typeDeclaration.contains(".");
    }

    private String findClassInPackage(String className, String uniquePackageName) {
        for (String uniqueName : getModulesInPackage(uniquePackageName)) {
            if (uniqueName.endsWith("." + className)) {
                return uniqueName;
            }
        }
        return "";
    }

    private String getPackageFromUniqueClassName(String completeImportString) {
        List<FamixClass> classes = theModel.getClasses();
        for (FamixClass fclass : classes) {
            if (fclass.uniqueName.equals(completeImportString)) {
                return fclass.belongsToPackage;
            }
        }

        FamixInterface f = theModel.interfaces.get(completeImportString);
        if (f != null) {
            return f.belongsToPackage;
        }


        return "";
    }

    private List<String> getModulesInPackage(String packageUniqueName) {
        List<String> result = new ArrayList<String>();
        Iterator<Entry<String, FamixClass>> classIterator = theModel.classes.entrySet().iterator();
		FamixClass currentClass;
		FamixInterface currentInterface;
		
        while (classIterator.hasNext()) {
            Entry<String, FamixClass> entry = (Entry<String, FamixClass>) classIterator.next();
            currentClass = entry.getValue();
            if (currentClass.belongsToPackage.equals(packageUniqueName)) {
                result.add(currentClass.uniqueName);
            }
        }
        Iterator<Entry<String, FamixInterface>> interfaceIterator = theModel.interfaces.entrySet().iterator();
        while (interfaceIterator.hasNext()) {
            Entry<String, FamixInterface> entry = (Entry<String, FamixInterface>) interfaceIterator.next();
            currentInterface = entry.getValue();
            if (currentInterface.belongsToPackage.equals(packageUniqueName)) {
                result.add(currentInterface.uniqueName);
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

    private void initializeHashMapstructuralEntityHashMap() {
    	structuralEntityHashMap = new HashMap<String, FamixStructuralEntity>();

    	int nrOfDuplicateStructuralEntities = 0;

		try{
	    	for (FamixStructuralEntity entity : theModel.structuralEntities.values()) {
	    		String searchKey = entity.belongsToClass + entity.lineNumber + entity.name;
	            if (structuralEntityHashMap.containsKey(searchKey)){
	            	nrOfDuplicateStructuralEntities ++;
	            }
	            else {
	        		structuralEntityHashMap.put(searchKey, entity);
	            }
	    	}
	        //this.logger.debug(new Date().toString() + " Finished: initializeHashMapsForQueries(), Number of duplicate StructuralEntities: "  + String.valueOf(nrOfDuplicateStructuralEntities));
	        
		} catch(Exception e) {
	        this.logger.debug(new Date().toString() + "Exception may result in incomplete dependency list. Exception:  " + e);
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
    	
    	return;
    }
    
    private void initializeHashMapsinheritanceAccociationsPerClass() {
		FamixAssociation foundInheritance;
		ArrayList<FamixAssociation> foundInheritanceList;
		ArrayList<FamixAssociation>  alreadyIncludedInheritanceList;
		inheritanceAccociationsPerClass = new HashMap<String, ArrayList<FamixAssociation>>();

		try{
	        for (FamixAssociation association : theModel.associations) {
            	String uniqueNameFrom = association.from;
            	if(uniqueNameFrom.equals("domain.indirect.violatingfrom.InheritanceExtendsExtendsIndirect")){
            		String type = association.type;
            		this.logger.debug(new Date().toString() + " Type: domain.indirect.violatingfrom.InheritanceExtendsExtendsIndirect =  " + type);
            	}
	            //Fill the HashMaps inheritanceAccociationsPerClass with inheritance dependencies to super classes or interfaces 
	            if (association.type.equals(EXTENDS_CONCRETE)|| association.type.equals(EXTENDS_ABSTRACT) || association.type.equals(EXTENDS)){
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
		} catch(Exception e) {
	        this.logger.debug(new Date().toString() + "Exception may result in incomplete dependency list. Exception:  " + e);
	        e.printStackTrace();
		}
    	
    	return;
    }

    
}
