package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import org.antlr.runtime.tree.CommonTree;
import org.apache.log4j.Logger;

public class CSharpInvocationGenerator extends CSharpGenerator {

    private String from;
    private String to = "";
    private int lineNumber;
    private String invocationName;
    private String belongsToMethod;
    private String nameOfInstance = "";
    private Logger logger = Logger.getLogger(CSharpInvocationGenerator.class);


    public CSharpInvocationGenerator(String uniqueClassName) {
        from = uniqueClassName;
    }

    // Is used for call and access; during post-processing, the dependency type is determined. 
    public void generateInvocationToDomain(CommonTree treeNode, String belongsToMethod) {
        this.belongsToMethod = belongsToMethod;
        lineNumber = treeNode.getLine();
      	if ((treeNode.getChildCount() > 0)) {
        	String invocTo = getCompleteToString((CommonTree) treeNode.getChild(0));
        	this.to = invocTo;
        	this.nameOfInstance = to;
        	this.invocationName = to;
            if (to != null && !to.equals("") && !SkippableTypes.isSkippable(to)) {
                modelService.createMethodInvocation(from, to, lineNumber, invocationName, belongsToMethod, nameOfInstance, "InvocMethod");
            }
        }
    }

    // Is used for call and access; during post-processing, the dependency type is determined. 
    public void generateInvocationBaseConstructorToDomain(CommonTree treeNode, String belongsToMethod) {
        this.belongsToMethod = belongsToMethod;
        lineNumber = treeNode.getLine();
    	this.to = "superBaseClass()";
    	this.nameOfInstance = to;
    	this.invocationName = to;
        if (to != null && !to.equals("") && !SkippableTypes.isSkippable(to)) {
            modelService.createMethodInvocation(from, to, lineNumber, invocationName, belongsToMethod, nameOfInstance, "InvocConstructor");
        }
    }

    
    /* Returns the complete string of an expression of a variable, also in case of chaining call/access combinations.
     * In case of a type cast, it does not include the type cast in the returnValue, but it creates a declaration association.
     * Use this method to determine the to-string in case in the following cases: 
     * 1) type declaration of a (local) variable; 2) at both sides of an assignment; 3) at both sides of an comparison. 
     */
    public String getCompleteToString(CommonTree tree) {  
    	String returnValue = "";
    	try {
    		int treeType = tree.getType();
    		switch(treeType) {
	        case CSharpParser.MEMBER_ACCESS: case CSharpParser.NAMESPACE_OR_TYPE_NAME:
	    		boolean isFirstSubString = true;
	        	for (int i = 0; i < tree.getChildCount(); i++) {
	    			String subString= getCompleteToString((CommonTree) tree.getChild(i));
	    			if (!subString.equals("")) {
		        		if (isFirstSubString) { 
		                	returnValue += subString;
		                	isFirstSubString = false;
		                } else {
		                	returnValue += "." + subString;
		                } 
	    			}
	    		}
	            break;
	        case CSharpParser.SIMPLE_NAME: case CSharpParser.ARGUMENT_VALUE: case CSharpParser.UNARY_EXPRESSION: case CSharpParser.TYPE: case CSharpParser.NAMESPACE_OR_TYPE_PART: case CSharpParser.EXPRESSION_STATEMENT: case CSharpParser.VARIABLE_INITIALIZER:
	        	returnValue += getCompleteToString((CommonTree) tree.getChild(0));
	            break;
	        case CSharpParser.METHOD_INVOCATION:
	        	returnValue += getMethodInvocationString(tree);
	            break;
	        case CSharpParser.OBJECT_CREATION_EXPRESSION: 
	        	returnValue += getConstructorInvocationString(tree); 
	            break;
	        case CSharpParser.ARGUMENT: 
	        	returnValue += getCompleteToString((CommonTree) tree.getChild(0));
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
	    		returnValue += getCompleteToString((CommonTree) tree.getChild(1));
	    		// Create association of typecast-type access
	    		CommonTree typeChild = (CommonTree) tree.getFirstChildWithType(CSharpParser.TYPE);
	        	if (typeChild != null) {
	        		String typeCastTo = getCompleteToString(typeChild);
        	        if ((typeCastTo != null) && !typeCastTo.equals("") && !SkippableTypes.isSkippable(typeCastTo)) {
        	            modelService.createDeclarationTypeCast(from, typeCastTo, typeChild.getLine());
        	        }
	        	}
	            break;
	        case CSharpParser.DOT: // "."
	        	String left = getCompleteToString((CommonTree) tree.getChild(0));
	        	String right = getCompleteToString((CommonTree) tree.getChild(1));
	        	if ((left == "") || (right == "")) {
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
			String argTo = getCompleteToString((CommonTree) tree.getChild(i));
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
			String subString = getCompleteToString((CommonTree) tree.getChild(i));
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
        if ((invocationTo != null) && !invocationTo.equals("") && !SkippableTypes.isSkippable(invocationTo)) {
            modelService.createPropertyOrFieldInvocation(from, invocationTo, line, invocationTo, belongsToMethod, invocationTo);
        }
    }
}

