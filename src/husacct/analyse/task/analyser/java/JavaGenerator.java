package husacct.analyse.task.analyser.java;

import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import husacct.analyse.infrastructure.antlr.java.Java7Parser;


import husacct.analyse.infrastructure.antlr.java.Java7Parser.TypeArgumentContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.TypeArgumentsContext;
import husacct.analyse.domain.IModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.analyse.task.analyser.VisibilitySet;
import husacct.common.enums.DependencySubTypes;

abstract class JavaGenerator {

    protected IModelCreationService modelService = new FamixCreationServiceImpl();
    
    protected String determineVisibility(List<Java7Parser.ModifierContext> modifierList) {
    	String visibility = VisibilitySet.DEFAULT.toString();
        if (modifierList != null && modifierList.size() >= 1) {
			for (ParseTree child : modifierList) {
				String modifier = child.getText();
	            if (VisibilitySet.isValidVisibillity(modifier)) {
	            	visibility = modifier;
	            } else {
	                visibility = VisibilitySet.DEFAULT.toString();
	            }
			}
 		}
        return visibility;
    }

    protected boolean determineIsFinal(List<Java7Parser.ModifierContext> modifierList) {
    	boolean isFinal = false;
        if (modifierList != null && modifierList.size() >= 1) {
			for (ParseTree child : modifierList) {
				String modifier = child.getText();
	            if (modifier.equals("final")) {
	            	isFinal = true;
	            } 
			}
 		}
        return isFinal;
    }

    protected boolean determineIsStatic(List<Java7Parser.ModifierContext> modifierList) {
    	boolean isStatic = false;
        if (modifierList != null && modifierList.size() >= 1) {
			for (ParseTree child : modifierList) {
				String modifier = child.getText();
	            if (modifier.equals("static")) {
	            	isStatic = true;
	            } 
			}
 		}
        return isStatic;
    }

    protected void dispatchAnnotationsOfMember(List<Java7Parser.ModifierContext> modifierList, String belongsToClass) {
        if (modifierList != null) {
        	int size = modifierList.size();
        	for (int i = 0; i < size; i++) {
            	if (modifierList.get(i).classOrInterfaceModifier().annotation() != null) {
    				new AnnotationAnalyser(modifierList.get(i).classOrInterfaceModifier().annotation(), belongsToClass);
            	}
			}
 		}
    }
    
    // Transforms the output of Identifier() to a String
    protected String transformIdentifierToString(List<TerminalNode> identifier) {
    	String returnValue = "";
    	for(TerminalNode node : identifier){
    		returnValue += node.getText();
    	}
    	return returnValue;
    }
    
    // Detects generic type parameters recursively. Also in complex types such as: HashMap<ProfileDAO, ArrayList<FriendsDAO>>>
    protected String dispatchGenericTypeParameters(String belongsToClass, List<TypeArgumentsContext> typeArgumentsList, int recursionLevel, DependencySubTypes dependencySubType) {
    	String typeInClassDiagram = "";
    	int levelOfRecursion = recursionLevel + 1;
		for (TypeArgumentsContext typeArguments : typeArgumentsList) {
			for (TypeArgumentContext typeArgument : typeArguments.typeArgument()) {
				String parameterTypeOfGeneric = transformIdentifierToString(typeArgument.typeType().classOrInterfaceType().Identifier());
            	int currentLineNumber = typeArgument.typeType().classOrInterfaceType().start.getLine();
            	modelService.createTypeParameter(belongsToClass, currentLineNumber, parameterTypeOfGeneric, dependencySubType);
				// Check if typeArgument contains type parameters too (recursively)
				if ((typeArgument.typeType().classOrInterfaceType() != null) 
					&& (typeArgument.typeType().classOrInterfaceType().typeArguments() != null)) {
					dispatchGenericTypeParameters(belongsToClass, typeArgument.typeType().classOrInterfaceType().typeArguments(), levelOfRecursion, dependencySubType);
				}
            	// If the variable is an instance variable, return this.typeInClassDiagram
                if ((dependencySubType == DependencySubTypes.DECL_INSTANCE_VAR) && (levelOfRecursion == 1)) { // E.g. ArrayList<Person>. In case of HashMap<String, Person> Person will be assigned.
            		typeInClassDiagram = parameterTypeOfGeneric; 
                }
			}
		}
		return typeInClassDiagram;
    }

}
