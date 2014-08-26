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
        	String invocTo = getAssignmentString(commonTree);
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
      	if ((treeNode.getChildCount() > 0) && (treeNode.getChild(0).getChildCount() > 0)) {
        	String invocTo = getAssignmentString(treeNode);
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
        this. lineNumber = treeNode.getLine();

        if (treeNode != null) {
            for (int childCount = 0; childCount < treeNode.getChildCount(); childCount++) {
                CommonTree childNode = (CommonTree) treeNode.getChild(childCount);
                if (childCount == 0) {
                	leftVariableInAssignment = getAssignmentString(childNode);
                }
                if (childCount == 1) {
                	rightVariableInAssignment = getAssignmentString(childNode);
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

    private String getAssignmentString(CommonTree tree) {
    	String returnValue = "";
    	try {
    		String left = "";
    		String right = "";
    		int treeType = tree.getType();
    		if (tree.getText().equals("STATIC_ARRAY_CREATOR")) {
    			treeType = JavaParser.STATIC_ARRAY_CREATOR;	
    		}
    		switch(treeType) {
	        case JavaParser.DOT: // "."
	    		left = getAssignmentString((CommonTree) tree.getChild(0));
	    		right = getAssignmentString((CommonTree) tree.getChild(1));
	    		returnValue += left + "." + right;
	            break;
	        case JavaParser.METHOD_CALL:
	    		left = getAssignmentString((CommonTree) tree.getChild(0));
	    		right = getAssignmentString((CommonTree) tree.getChild(1));
	    		returnValue += left + "(" + right + ")";
	            break;
	        case JavaParser.CLASS_CONSTRUCTOR_CALL: case JavaParser.VAR_DECLARATOR: case JavaParser.VAR_DECLARATOR_LIST:
	        	returnValue += getAssignmentString((CommonTree) tree.getChild(0));
	            break;
	        case JavaParser.STATIC_ARRAY_CREATOR: 
	        	if ((tree.getChildCount() > 0) && (tree.getChild(0).getType() == JavaParser.QUALIFIED_TYPE_IDENT)) {
	        		returnValue += tree.getChild(0).getChild(0).getText();
	        	} else {
	        		returnValue += "";
	        	}
	            break;
	        case JavaParser.EXPR:
	        	returnValue += getAssignmentString((CommonTree) tree.getChild(0));
	            break;
	        case JavaParser.ARGUMENT_LIST:
	            //Test helper
	           	if (from.equals("domain.indirect.violatingfrom.AccessObjectReferenceIndirect_AsReturnValueOfSuperClassMethod_FromSide")){
	        		boolean breakpoint1 = true;
	        	}

	    		for (int i = 0; i < tree.getChildCount(); i++) {
	    			String argTo= getAssignmentString((CommonTree) tree.getChild(i));
	    			createPropertyOrFieldInvocationDomainObject(argTo);
	        		//Includes the arguments in the method call signature
	        		if (i == tree.getChildCount() - 1) { 
	                	returnValue += argTo;
	                } else {
	                	returnValue += argTo + ",";
	                } 
	    		}
	            break;
	        case JavaParser.IDENT: case JavaParser.QUALIFIED_TYPE_IDENT:
	        	returnValue = getToValue(tree);
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
	    		returnValue += getAssignmentString((CommonTree) tree.getChild(1));
	    		// Create association of typecast-type access
	    		CommonTree typeChild = (CommonTree) tree.getFirstChildWithType(JavaParser.TYPE);
	        	if (typeChild != null) {
	        		CommonTree identChild = (CommonTree) typeChild.getFirstChildWithType(JavaParser.QUALIFIED_TYPE_IDENT);
	        		if ((identChild != null) && (identChild.getChildCount() > 0)) {
	        			String typeCastTo = identChild.getChild(0).getText();
	        	        if ((typeCastTo != null) && (typeCastTo != "") && !SkippedTypes.isSkippable(typeCastTo)) {
	        	            modelService.createDeclarationTypeCast(from, typeCastTo, lineNumber);
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

    private String getToValue(CommonTree tree) {
    	String returnValue = "";
        if (tree.getChildCount() > 1) {
            for (int childCount = 0; childCount < tree.getChildCount(); childCount++) {
                if (childCount == tree.getChildCount() - 1) {
                	returnValue += tree.getChild(childCount).toString();
                } else {
                	returnValue += tree.getChild(childCount).toString() + ".";
                }
            }
        } else if (tree.getChildCount() == 1){
        	returnValue = tree.getChild(0).getText();
        } else {
        	returnValue = tree.getText();
        }
        return returnValue;
    }

    private String getArgumentValueOrType(CommonTree tree) {
    	String returnValue = "";
        if (tree.getChildCount() > 1) {
            for (int childCount = 0; childCount < tree.getChildCount(); childCount++) {
                if (childCount == tree.getChildCount() - 1) {
                	returnValue += tree.getChild(childCount).toString();
                } else {
                	returnValue += tree.getChild(childCount).toString() + ".";
                }
            }
        } else if (tree.getChildCount() == 1){
        	returnValue = tree.getChild(0).getText();
        } else {
        	returnValue = tree.getText();
        }
        return returnValue;
    }

    private void createPropertyOrFieldInvocationDomainObject() {
        if ((to != null) && (to != "") && !SkippedTypes.isSkippable(to)) {
            modelService.createPropertyOrFieldInvocation(from, to, lineNumber, invocationName, belongsToMethod, nameOfInstance);
        }
    }
    
    private void createPropertyOrFieldInvocationDomainObject(String invocationTo) {
        if ((invocationTo != null) && (invocationTo != "") && !SkippedTypes.isSkippable(invocationTo)) {
            modelService.createPropertyOrFieldInvocation(from, invocationTo, lineNumber, invocationName, belongsToMethod, nameOfInstance);
        }
    }
}

