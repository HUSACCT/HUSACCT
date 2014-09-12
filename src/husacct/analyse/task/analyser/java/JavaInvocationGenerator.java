package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.JavaParser;

import org.antlr.runtime.tree.CommonTree;
import org.apache.log4j.Logger;

public class JavaInvocationGenerator extends JavaGenerator {

    private String from;
    private String to = "";
    private String leftVariableInAssignment = "";
    private String rightVariableInAssignment = "";
    private int lineNumber;
    private String invocationName;
    private String belongsToMethod;
    private String nameOfInstance = "";
    private Logger logger = Logger.getLogger(JavaInvocationGenerator.class);


    public JavaInvocationGenerator(String uniqueClassName) {
        from = uniqueClassName;
    }

    public void generateConstructorInvocToDomain(CommonTree commonTree, String belongsToMethod) {
    	this.invocationName = "Constructor";
        this.belongsToMethod = belongsToMethod;
        this. lineNumber = commonTree.getLine();
       	if ((commonTree.getChildCount() > 0)) {
        	String invocTo = getCompleteToString(commonTree);
        	this.to = invocTo;
        	this.nameOfInstance = to;
            if (to != null && to != "" && !SkippedTypes.isSkippable(to)) {
                modelService.createMethodInvocation(from, to, lineNumber, invocationName, belongsToMethod, nameOfInstance, "InvocConstructor");
            }
        }
    }

    public void generateMethodInvocToDomain(CommonTree treeNode, String belongsToMethod) {
        this.belongsToMethod = belongsToMethod;
        lineNumber = treeNode.getLine();
      	if ((treeNode.getChildCount() > 0)) {
        	String invocTo = getCompleteToString(treeNode);
        	this.to = invocTo;
        	this.nameOfInstance = to;
        	this.invocationName = to;
            if (to != null && to != "" && !SkippedTypes.isSkippable(to)) {
                modelService.createMethodInvocation(from, to, lineNumber, invocationName, belongsToMethod, nameOfInstance, "InvocMethod");
            }
        }
    }

    public void generatePropertyOrFieldInvocToDomain(CommonTree treeNode, String belongsToMethod) {
        this.belongsToMethod = belongsToMethod;
        this.lineNumber = treeNode.getLine();
        if (treeNode != null) {
            for (int childCount = 0; childCount < treeNode.getChildCount(); childCount++) {
                CommonTree childNode = (CommonTree) treeNode.getChild(childCount);
                if (childCount == 0) {
                	leftVariableInAssignment = getCompleteToString(childNode);
                }
                if (childCount == 1) {
                	rightVariableInAssignment = getCompleteToString(childNode);
            		this.lineNumber = childNode.getLine();
                }
            }
    		
    		// Create invocation of variable at left side of =
    		this.to = leftVariableInAssignment;
    		this.nameOfInstance = to;
    		this.invocationName = to;
    		createPropertyOrFieldInvocationDomainObject();
    		
    		int treeType = treeNode.getType();
        	if((treeType == JavaParser.ASSIGN) || (treeType == JavaParser.NOT_EQUAL) || (treeType == JavaParser.EQUAL) 
        			|| (treeType == JavaParser.GREATER_OR_EQUAL) || (treeType == JavaParser.LESS_OR_EQUAL)
        			|| (treeType == JavaParser.LESS_THAN) || (treeType == JavaParser.GREATER_THAN)){
        		// Create invocation of variable at right side of =
        		this.to = rightVariableInAssignment;
        		this.nameOfInstance = to;
        		this.invocationName = to;
        		createPropertyOrFieldInvocationDomainObject();
        	}
        }
    }

    /* Returns the complete string of an expression of a variable, also in case of chaining call/access combinations.
     * In case of a type cast, it does not include the type cast in the returnValue, but it creates a declaration association.
     * Use this method to determine the to-string in case in the following cases: 
     * 1) type declaration of a (local) variable; 2) at both sides of an assignment; 3) at both sides of an comparison. 
     */
    public String getCompleteToString(CommonTree tree) {  
    	String returnValue = "";
    	if (tree == null) {
    		return returnValue;
    	}
    	try {
    		int treeType = tree.getType();
    		if (tree.getText().equals("STATIC_ARRAY_CREATOR")) {
    			treeType = JavaParser.STATIC_ARRAY_CREATOR;	
    		}
    		switch(treeType) {
	        case JavaParser.DOT: // "."
	        	String left = getCompleteToString((CommonTree) tree.getChild(0));
	        	String right = getCompleteToString((CommonTree) tree.getChild(1));
	        	if ((left == "") || (right == "")) {
	        		returnValue += left + right;
	        	} else {
		    		returnValue += left + "." + right;
	        	}
	            break;
	        case JavaParser.METHOD_CALL:
	        	String left1 = getCompleteToString((CommonTree) tree.getChild(0));
	        	String right1 = getCompleteToString((CommonTree) tree.getChild(1));
	    		returnValue += left1 + "(" + right1 + ")";
	            break;
	        case JavaParser.CLASS_CONSTRUCTOR_CALL: 
	        	String left2 = getCompleteToString((CommonTree) tree.getChild(0));
	        	String right2 = getCompleteToString((CommonTree) tree.getChild(1));
	    		returnValue += left2 + "(" + right2 + ")";
	            break;
	        case JavaParser.SUPER_CONSTRUCTOR_CALL: 
	        	returnValue += getCompleteToString((CommonTree) tree.getChild(0));
	            break;
	        case JavaParser.STATIC_ARRAY_CREATOR: 
	        	if ((tree.getChildCount() > 0) && ((tree.getChild(0).getType() == JavaParser.QUALIFIED_TYPE_IDENT) || (tree.getChild(0).getType() == JavaParser.IDENT))) {
		        	String left3 = getCompleteToString((CommonTree) tree.getChild(0));
		        	String right3 = getCompleteToString((CommonTree) tree.getChild(1));
		    		returnValue += left3 + "(" + right3 + ")";
	        	} else {
	        		returnValue += "";
	        	}
	            break;
	        case JavaParser.VAR_DECLARATOR: case JavaParser.VAR_DECLARATOR_LIST: case JavaParser.TYPE:
	        	returnValue += getCompleteToString((CommonTree) tree.getChild(0));
	            break;
	        case JavaParser.EXPR: case JavaParser.PARENTESIZED_EXPR:
	        	returnValue += getCompleteToString((CommonTree) tree.getChild(0));
	            break;
	        case JavaParser.ARGUMENT_LIST:
	    		for (int i = 0; i < tree.getChildCount(); i++) {
	    			String argTo= getCompleteToString((CommonTree) tree.getChild(i));
	    			createPropertyOrFieldInvocationDomainObject(argTo, (CommonTree) tree.getChild(i));
	        		//Includes the arguments in the method call signature
	    			if (argTo.contains(".") || argTo.contains(",")) { // Currently, arguments with a "." or "," disable the indirect dependency detection algorithm. In case of future improvements: create a FamixArgument object per argument. 
	    				argTo = "";
	    			}
	        		if (i == tree.getChildCount() - 1) { 
	                	returnValue += argTo;
	                } else {
	                	returnValue += argTo + ",";
	                } 
	    		}
	            break;
	        case JavaParser.IDENT:
	        	returnValue = tree.getText();
	            break;
	        case JavaParser.QUALIFIED_TYPE_IDENT:
	        	returnValue = tree.getChild(0).getText();
	            break;
	        case JavaParser.THIS: case JavaParser.SUPER:
	        	returnValue = "";
	            break;
	        case JavaParser.DECIMAL_LITERAL: case JavaParser.INT: 
	        	returnValue += "int";
	            break;
	        case JavaParser.STRING_LITERAL:
	        	returnValue += "String";
	            break;
	        case JavaParser.TRUE: case JavaParser.FALSE: case JavaParser.BOOLEAN:
	        	returnValue += "boolean";
	            break;
	        case JavaParser.CHAR: 
	        	returnValue += "char";
	            break;
	        case JavaParser.BYTE:
	        	returnValue += "byte";
	            break;
	        case JavaParser.CAST_EXPR:
	    		returnValue += getCompleteToString((CommonTree) tree.getChild(1));
	    		// Create association of typecast-type access
	    		CommonTree typeChild = (CommonTree) tree.getFirstChildWithType(JavaParser.TYPE);
	        	if (typeChild != null) {
	        		CommonTree identChild = (CommonTree) typeChild.getFirstChildWithType(JavaParser.QUALIFIED_TYPE_IDENT);
	        		if ((identChild != null) && (identChild.getChildCount() > 0)) {
	        			String typeCastTo = identChild.getChild(0).getText();
	        	        if ((typeCastTo != null) && (typeCastTo != "") && !SkippedTypes.isSkippable(typeCastTo)) {
	        	            modelService.createDeclarationTypeCast(from, typeCastTo, tree.getLine());
	        	        }
	        		}
	        	}
	            break;
	        }
    	} catch (Exception e) {
    		logger.error("Exception: "+ e);
    	}
        return returnValue;
    }

    private void createPropertyOrFieldInvocationDomainObject() {
        if ((to != null) && (to != "") && !SkippedTypes.isSkippable(to)) {
            modelService.createPropertyOrFieldInvocation(from, to, lineNumber, invocationName, belongsToMethod, nameOfInstance);
        }
    }
    
    private void createPropertyOrFieldInvocationDomainObject(String invocationTo, CommonTree tree) {
        if ((invocationTo != null) && (invocationTo != "") && !SkippedTypes.isSkippable(invocationTo)) {
            modelService.createPropertyOrFieldInvocation(from, invocationTo, tree.getLine(), invocationTo, belongsToMethod, invocationTo);
        }
    }
}

