package husacct.analyse.task.analyse.java.analysing;

import java.util.List;

import org.antlr.v4.runtime.tree.TerminalNode;

import husacct.analyse.domain.IModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.analyse.task.analyse.VisibilitySet;
import husacct.analyse.task.analyse.java.parsing.JavaParser;
import husacct.analyse.task.analyse.java.parsing.JavaParser.ModifierContext;
import husacct.analyse.task.analyse.java.parsing.JavaParser.TypeArgumentContext;
import husacct.analyse.task.analyse.java.parsing.JavaParser.TypeArgumentsContext;
import husacct.analyse.task.analyse.java.parsing.JavaParser.TypeTypeContext;

abstract class JavaGenerator {

    protected IModelCreationService modelService = new FamixCreationServiceImpl();
    
    protected String determineVisibility(List<ModifierContext> modifierList) {
    	String visibility = VisibilitySet.DEFAULT.toString();
        if (modifierList != null) {
			for (ModifierContext modifier : modifierList) {
	            if (VisibilitySet.isValidVisibillity(modifier.getText())) {
	            	visibility = modifier.getText();
	            }
			}
 		}
        return visibility;
    }

    protected boolean determineIsAbstract(List<ModifierContext> modifierList) {
    	boolean isAbstract = false;
        if (modifierList != null) {
			for (ModifierContext modifier : modifierList) {
	            if (modifier.getText().equals("abstract")) {
	            	isAbstract = true;
	            } 
			}
 		}
        return isAbstract;
    }

    protected boolean determineIsFinal(List<ModifierContext> modifierList) {
    	boolean isFinal = false;
        if (modifierList != null) {
			for (ModifierContext modifier : modifierList) {
	            if (modifier.getText().equals("final")) {
	            	isFinal = true;
	            } 
			}
 		}
        return isFinal;
    }

    protected boolean determineIsStatic(List<JavaParser.ModifierContext> modifierList) {
    	boolean isStatic = false;
        if (modifierList != null) {
			for (ModifierContext modifier : modifierList) {
	            if (modifier.getText().equals("static")) {
	            	isStatic = true;
	            } 
			}
 		}
        return isStatic;
    }

    protected void dispatchAnnotationsOfMember(List<ModifierContext> modifierList, String belongsToClass) {
        if (modifierList != null) {
        	//int size = modifierList.size();
        	for (ModifierContext modifier : modifierList) {
            	if ((modifier.classOrInterfaceModifier() != null) && (modifier.classOrInterfaceModifier().annotation() != null)) {
    				new AnnotationAnalyser(modifier.classOrInterfaceModifier().annotation(), belongsToClass);
            	}
			}
 		}
    }
    
    /** Transforms the output of a list of Identifiers to a String. E.g. needed to transform TypeType.Identifier().
     * 
     * @param List<TerminalNode> identifierList
     * @return String
     */
    protected String transformIdentifierToString(List<TerminalNode> identifierList) {
    	String returnValue = "";
    	int sequence = 1;
    	for(TerminalNode identifier : identifierList){
    		if (sequence == 1) {
    			returnValue += identifier.getText();
    		} else {
    			returnValue += "." + identifier.getText();
    		}
    		sequence ++;
    	}
    	return returnValue;
    }
    
    protected String determineTypeOfTypeType(TypeTypeContext typeType, String belongsToClass) {
    	String returnType = "";
    	if (typeType != null) {
			if (typeType.primitiveType() != null) {
				returnType = typeType.getText();
			} else if (typeType.classOrInterfaceType() != null) {
				returnType = transformIdentifierToString(typeType.classOrInterfaceType().Identifier());
		       if (typeType.classOrInterfaceType().typeArguments() != null) { // Check if the contains generic type parameters. 
		        	analyseTypeArguments(belongsToClass, typeType.classOrInterfaceType().typeArguments());
		       }
			}
    	}
		return returnType;
    }

    // Detects generic type parameters recursively. Also in complex types such as: HashMap<ProfileDAO, ArrayList<FriendsDAO>>>
    protected void analyseTypeArguments(String belongsToClass, List<TypeArgumentsContext> typeArgumentsList) {
		for (TypeArgumentsContext typeArguments : typeArgumentsList) {
			for (TypeArgumentContext typeArgument : typeArguments.typeArgument()) {
				if (typeArgument.typeType() != null) {
					String parameterTypeOfGeneric = determineTypeOfTypeType(typeArgument.typeType(), belongsToClass);
	            	int currentLineNumber = typeArgument.typeType().start.getLine();
	            	modelService.createTypeParameter(belongsToClass, currentLineNumber, parameterTypeOfGeneric);
				} 
 			}
		}
    }

}
