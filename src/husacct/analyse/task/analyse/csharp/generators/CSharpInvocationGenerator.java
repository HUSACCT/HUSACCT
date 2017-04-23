package husacct.analyse.task.analyse.csharp.generators;

import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import husacct.common.enums.DependencySubTypes;

import org.antlr.runtime.tree.CommonTree;
import org.apache.log4j.Logger;

public class CSharpInvocationGenerator extends CSharpGenerator {

    private String from;
    private String to = "";
    private int lineNumber;
    private String belongsToMethod;
    private Logger logger = Logger.getLogger(CSharpInvocationGenerator.class);


    public CSharpInvocationGenerator(String uniqueClassName) {
        from = uniqueClassName;
    }

    // Is used for call and access; during post-processing, the dependency type is determined. 
    public void generateConstructorInvocToDomain(CommonTree treeNode, String belongsToMethod) {
        this.belongsToMethod = belongsToMethod;
        lineNumber = treeNode.getLine();
    	this.to = "superBaseClass()";
        if ((from != null) && (to != null) && !to.equals("") && !SkippableTypes.isSkippable(to)) {
            modelService.createMethodInvocation(from, to, lineNumber, belongsToMethod, "InvocConstructor");
        }
    }

    // Is used for call and access; during post-processing, the dependency type is determined. 
    public void generateMethodInvocToDomain(CommonTree treeNode, String belongsToMethod) {
        this.belongsToMethod = belongsToMethod;
        lineNumber = treeNode.getLine();
      	if ((treeNode.getChildCount() > 0)) {
        	String invocTo = getCompleteToString((CommonTree) treeNode.getChild(0), from, DependencySubTypes.DECL_TYPE_PARAMETER);
        	this.to = invocTo;
            if ((from != null) && (to != null) && !to.equals("") && !SkippableTypes.isSkippable(to)) {
                modelService.createMethodInvocation(from, to, lineNumber, belongsToMethod, "InvocMethod");
            }
        }
    }

    
    /* Returns the complete string of an expression of a variable, also in case of chaining call/access combinations.
     * In case of a type cast, it does not include the type cast in the returnValue, but it creates a declaration association.
     * Use this method to determine the to-string in case in the following cases: 
     * 1) type declaration of a (local) variable; 2) at both sides of an assignment; 3) at both sides of an comparison. 
     */
    public String getCompleteToString(CommonTree tree, String belongsToClass, DependencySubTypes dependencySubType) {  
    	/* Test code
    	if (belongsToClass.contains("CallConstructor_GenericType_MultipleTypeParameters")) {
    		boolean testPoint = true;
    	} */
    	
    	String returnValue = "";
    	if (tree == null) {
    		return returnValue;
    	}
    	try {
    		int treeType = tree.getType();
    		switch(treeType) {
	        case CSharpParser.MEMBER_ACCESS: case CSharpParser.NAMESPACE_OR_TYPE_NAME: case CSharpParser.NAMESPACE_OR_TYPE_PART: case CSharpParser.SIMPLE_NAME: 
	    		boolean isFirstSubString = true;
	        	for (int i = 0; i < tree.getChildCount(); i++) {
	    			String subString= getCompleteToString((CommonTree) tree.getChild(i), belongsToClass, dependencySubType);
	    			if ((subString != null) && !subString.equals("")) {
		        		if (isFirstSubString) { 
		                	returnValue += subString;
		                	isFirstSubString = false;
		                } else {
		                	if (tree.getChild(i).getType() == CSharpParser.TYPE_ARGUMENT_LIST) { // In case of generic classes, add the parameters as <p1>, <p1, p2>, etc.
		                		returnValue += subString;
		                	} else {
		                		returnValue += "." + subString;
		                	}
		                } 
	    			}
	    		}
	            break;
	        case CSharpParser.TYPE_ARGUMENT_LIST: // In case of generic classes, add the parameters as <p1>, <p1, p2>, etc.
            	if (dependencySubType != null) {
            		addGenericTypeParameters(tree, belongsToClass, dependencySubType);
            	}
	        	String parameters = "";
            	int nrOfParameters = tree.getChildCount();
            	if (nrOfParameters > 0) {
            		for (int f = 0; f < nrOfParameters; f++) {
    	    			String subString= getCompleteToString((CommonTree) tree.getChild(f), belongsToClass, dependencySubType);
		            	if ((subString != null) && subString != null) {
		            		if (f == 0) {
		            			parameters += "p" + 1;
		            		} else {
		            			parameters += ", p" + (f+1);
		            		}
		            	}
            		}
            	}
        		returnValue += "<"+ parameters + ">";
	        	break;
	        case CSharpParser.ARGUMENT_VALUE: case CSharpParser.UNARY_EXPRESSION: case CSharpParser.TYPE: case CSharpParser.EXPRESSION_STATEMENT: case CSharpParser.VARIABLE_INITIALIZER:
	        	returnValue += getCompleteToString((CommonTree) tree.getChild(0), belongsToClass, dependencySubType);
	            break;
	        case CSharpParser.METHOD_INVOCATION:
	        	returnValue += getMethodInvocationString(tree);
	            break;
	        case CSharpParser.OBJECT_CREATION_EXPRESSION: 
	        	returnValue += getConstructorInvocationString(tree); 
	            break;
	        case CSharpParser.ARGUMENT: 
	        	returnValue += getCompleteToString((CommonTree) tree.getChild(0), belongsToClass, dependencySubType);
	    		createPropertyOrFieldInvocationDomainObject(returnValue, tree.getLine());
	            break;
	        case CSharpParser.IDENTIFIER:
	        	returnValue += tree.getText();
	            break;
	        case CSharpParser.QUALIFIED_IDENTIFIER: // ? Not encountered within test with Limaki
	        	returnValue = tree.getChild(0).getText();
	            break;
	        case CSharpParser.THIS: 
	        	returnValue = "";
	            break;
	        case CSharpParser.BASE: 
	        	returnValue = "superBaseClass";
	            break;
	        case CSharpParser.INT: case CSharpParser.INTEGER_LITERAL: case CSharpParser.Decimal_integer_literal: //?
	        	returnValue += "int";
	            break;
	        case CSharpParser.STRING_LITERAL: case CSharpParser.STRING:
	        	returnValue += "string";
	            break;
	        case CSharpParser.TRUE: case CSharpParser.FALSE: case CSharpParser.BOOL: //?
	        	returnValue += "bool";
	            break;
	        case CSharpParser.CHAR: 
	        	returnValue += "char";
	            break;
	        case CSharpParser.BYTE:
	        	returnValue += "byte";
	            break;
	        case CSharpParser.CAST_EXPRESSION: //
	    		returnValue += getCompleteToString((CommonTree) tree.getChild(1), belongsToClass, dependencySubType);
	    		// Create association of typecast-type access
	    		CommonTree typeChild = (CommonTree) tree.getFirstChildWithType(CSharpParser.TYPE);
	        	if (typeChild != null) {
	        		String typeCastTo = getCompleteToString(typeChild, belongsToClass, dependencySubType);
                    for (int i = 1; i < typeChild.getChildCount(); i++) { // In case of inner classes
                    	typeCastTo = typeCastTo + "." + typeChild.getChild(i).getText();
                    }
        	        if ((typeCastTo != null) && !typeCastTo.equals("") && !SkippableTypes.isSkippable(typeCastTo)) {
        	            modelService.createDeclarationTypeCast(from, typeCastTo, typeChild.getLine());
        	        }
	        	}
	            break;
	        case CSharpParser.DOT: // "."
	        	String left = getCompleteToString((CommonTree) tree.getChild(0), belongsToClass, dependencySubType);
	        	String right = getCompleteToString((CommonTree) tree.getChild(1), belongsToClass, dependencySubType);
	        	if ((left.equals("")) || (right.equals(""))) {
	        		returnValue += left + right;
	        	} else {
		    		returnValue += left + "." + right;
	        	}
	            break;
	        }
    	} catch (Exception e) {
    		logger.error("Exception: "+ e);
    	}
        return returnValue;
    }

    private String getMethodInvocationString(CommonTree tree) {
		String returnValue = "";
		String bodyString = "";
		String argumentString = "";
		boolean firstArgument = true;
		for (int i = 0; i < tree.getChildCount(); i++) {
			String argTo = getCompleteToString((CommonTree) tree.getChild(i), from, DependencySubTypes.DECL_TYPE_PARAMETER);
			if (tree.getChild(i).getType() != CSharpParser.ARGUMENT) {
				if (i == 0) {
					bodyString = argTo;
				} else {
					bodyString = bodyString + "." + argTo;
				}
			} else {
				if (argTo.contains(".") || argTo.contains(",")) { // Currently, arguments with a "." or "," disable the indirect dependency detection algorithm. In case of future improvements: create a FamixArgument object per argument. 
					argTo = "";
				}
				if (firstArgument) {
					argumentString = argTo;
					firstArgument = false;
				} else {
					argumentString += "," + argTo;
				}
			}
		}
		if (!bodyString.equals("")) {
			returnValue = bodyString + "(" + argumentString + ")";
		}
    	return returnValue;
    }
    
    private String getConstructorInvocationString(CommonTree tree) {
		String returnValue = "";
		String bodyString = "";
		String argumentString = "";
		boolean firstArgument = true;
		for (int i = 0; i < tree.getChildCount(); i++) {
			String subString = getCompleteToString((CommonTree) tree.getChild(i), from, DependencySubTypes.DECL_TYPE_PARAMETER);
			if (tree.getChild(i).getType() == CSharpParser.TYPE) {
				bodyString = subString;
			}
			if (tree.getChild(i).getType() == CSharpParser.ARGUMENT) {
				if (subString.contains(".") || subString.contains(",")) { // Currently, arguments with a "." or "," disable the indirect dependency detection algorithm. In case of future improvements: create a FamixArgument object per argument. 
					subString = "";
				}
				if (firstArgument) {
					argumentString = subString;
					firstArgument = false;
				} else {
					argumentString += "," + subString;
				}
			}
		}
		if (!bodyString.equals("")) {
			returnValue = bodyString + "(" + argumentString + ")";
		}
    	return returnValue;
    }
    
    private void createPropertyOrFieldInvocationDomainObject(String invocationTo, int line) {
        if ((from != null) && (invocationTo != null) && !invocationTo.equals("") && !SkippableTypes.isSkippable(invocationTo)) {
            modelService.createVariableInvocation(from, invocationTo, line, belongsToMethod);
        }
    }

    // Detects generic type parameters, also in complex types, like: HashMap<ProfileDAO, ArrayList<FriendsDAO>>>
    private void addGenericTypeParameters(CommonTree genericType, String belongsToClass, DependencySubTypes dependencySubType) {
        int numberOfTypeParameters = genericType.getChildCount();
        for (int j = 0; j < numberOfTypeParameters; j++) {
            CommonTree parameterTypeOfGenericTree = (CommonTree) genericType.getChild(j);
        	// Check if parameterTypeOfGenericTree contains a generic type arg list. If so, handle it recursively.
            CommonTree genericTypeRecursive = CSharpGeneratorToolkit.getFirstDescendantWithType(parameterTypeOfGenericTree, CSharpParser.TYPE_ARGUMENT_LIST);
            if (genericTypeRecursive != null) {
            	addGenericTypeParameters(genericTypeRecursive, belongsToClass, dependencySubType);
            } else {
	            CommonTree qualifiedType = CSharpGeneratorToolkit.getFirstDescendantWithType(parameterTypeOfGenericTree, CSharpParser.NAMESPACE_OR_TYPE_NAME);
	            if (qualifiedType != null) {
	            	String parameterTypeOfGeneric = getCompleteToString(qualifiedType, from, null); // Last argument = null, since no recursion should take place here.
	                if ((parameterTypeOfGeneric != null) && (dependencySubType != null)) {
	                	int currentLineNumber = qualifiedType.getLine();
	                	modelService.createTypeParameter(belongsToClass, currentLineNumber, parameterTypeOfGeneric);
	                }
	            }
            }
        }
	}

}

