package husacct.analyse.domain.famix;

import husacct.ServiceProvider;
import husacct.control.task.States;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
    private Logger logger = Logger.getLogger(FamixDependencyConnector.class);
    //Added By Team 1 General GUI & Control
    //Is needed for the progressBar
    private int amountOfModulesConnected = 0;
    //End added by Team 1
    private FamixModuleFinder moduleFinder;

    public FamixDependencyConnector() {
        theModel = FamixModel.getInstance();
        this.moduleFinder = new FamixModuleFinder(theModel);
    }

    void connectStructuralDependecies() {
		String theClass;
		String classFoundInImports;                        
		String belongsToPackage;
		String to;
		
        for (FamixStructuralEntity entity : theModel.waitingStructuralEntitys) {

            //Added By Team 1 General GUI & Control
            //Needed to check if Thread is allowed to continue
            if (!ServiceProvider.getInstance().getControlService().getState().contains(States.ANALYSING)) {
                break;
            }
            //end added by Team 1

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
            } catch (Exception e) {
            }
        }
    }

    void connectAssociationDependencies() {
        int count = 0;
		
		String oldy;
		String  theClass;
		String belongsToPackage;
		String classFoundInImports;
		String to;
		FamixInvocation theInvocation;
		
        for (FamixAssociation association : theModel.waitingAssociations) {

            //Added By Team 1 General GUI & Control
            //Needed to check if Thread is allowed to continue
            if (!ServiceProvider.getInstance().getControlService().getState().contains(States.ANALYSING)) {
                break;
            }
            //end added by Team 1

            oldy = association.to;
            count += 1;
            try {
                boolean connected = false;
                theClass = association.from;
                if (!isCompleteTypeDeclaration(association.to)) {
                    classFoundInImports = findClassInImports(theClass, association.to);
                    if (!classFoundInImports.equals("")) {
                        association.to = classFoundInImports;
                        connected = true;
                    } else {
                        belongsToPackage = getPackageFromUniqueClassName(association.from);
                        to = findClassInPackage(association.to, belongsToPackage);
                        if (!to.equals("")) {
                            association.to = to;
                            connected = true;
                        }
                    }
                    if (!connected) {
                        if (isInvocation(association)) {
                            theInvocation = (FamixInvocation) association;
                            if (theInvocation.belongsToMethod.equals("")) {
                                //Then it is an attribute
                                theInvocation.to = getClassForAttribute(theInvocation.from, theInvocation.nameOfInstance);
                            } else {
                                //checking order now: 1) parameter, 2) localVariable, 3) attribute
                                theInvocation.to = getClassForParameter(theInvocation.from, theInvocation.belongsToMethod, theInvocation.nameOfInstance);
                                if (theInvocation.to.equals("")) {
                                    //checking if it's a localVariable
                                    theInvocation.to = getClassForLocalVariable(theInvocation.from, theInvocation.belongsToMethod, theInvocation.nameOfInstance);
                                }
                                if (theInvocation.to.equals("")) {
                                    //now it is an attribute
                                    theInvocation.to = getClassForAttribute(theInvocation.from, theInvocation.nameOfInstance);
                                }
                            }
                        }
                    }
                }
                if (association.to.equals("") || association.to == null) {
//					logger.info(count + "/" + theModel.waitingAssociations.size() + " Couldn't analyse dependency from " + association.from);
//					System.out.println(count + "/" + theModel.waitingAssociations.size() + " Couldn't analyse dependency from " + association.from + " | " + oldy);
                } else {
                    determineType(association);
                    addToModel(association);
                }
            } catch (Exception e) {
            }
        }
    }

    private void determineType(FamixAssociation association) {
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
        String belongsToMethodFull = declareClass + "." + declareMethod;
        for (FamixFormalParameter parameter : theModel.getParametersForClass(declareClass)) {
            if (parameter.belongsToMethod.equals(belongsToMethodFull)) {
                if (parameter.name.equals(attributeName)) {
                    return parameter.declareType;
                }
            }
        }
        return "";
    }

    private String getClassForLocalVariable(String declareClass, String belongsToMethod, String nameOfInstance) {
		FamixStructuralEntity entity;
		FamixLocalVariable variable;
		
        for (String s : theModel.structuralEntities.keySet()) {
            if (s.startsWith(declareClass)) {

                entity = (FamixStructuralEntity) theModel.structuralEntities.get(s);
                if (entity instanceof FamixLocalVariable) {
                    variable = (FamixLocalVariable) entity;
                    if (variable.belongsToMethod.equals(belongsToMethod)) {
                        if (variable.name.equals(nameOfInstance)) {
                            return variable.declareType;
                        }
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
        List<FamixImport> imports = theModel.getImportsInClass(importingClass);
        for (FamixImport fImport : imports) {
            if (!fImport.importsCompletePackage) {
                if (fImport.to.endsWith("." + typeDeclaration)) {
                    return fImport.to;
                }
            } else {
                for (String uniqueClassName : getModulesInPackage(fImport.to)) {
                    if (uniqueClassName.endsWith("." + typeDeclaration)) {
                        return uniqueClassName;
                    }
                }
            }
        }
        return "";
    }

    private boolean isCompleteTypeDeclaration(String typeDeclaration) {


        //Added By Team 1 General GUI & Control
        //Is needed for the progressBar
        ServiceProvider.getInstance().getControlService().updateProgress((++amountOfModulesConnected * 100) / (1 + theModel.waitingAssociations.size() + theModel.waitingStructuralEntitys.size()));
        //End added by Team 1 General GUI & Control

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
}
