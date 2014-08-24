package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.JavaParser;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
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
    private boolean invocationNameFound;
    private boolean constructorInMethodInvocationFound = false;
    private boolean foundAllMethodInvocInfo = false;
    private boolean allIdents = true;
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
        }
        createConstructorInvocationDomainObject();
    }

    private void createConstructorInvocationDomainObject() {
        if (to != null && to != "" && !SkippedTypes.isSkippable(to)) {
            modelService.createConstructorInvocation(from, to, lineNumber, invocationName, belongsToMethod, nameOfInstance);
        }
    }

    public void generateMethodInvocToDomain(CommonTree treeNode, String belongsToMethod) {
        invocationNameFound = false;
        constructorInMethodInvocationFound = false;
        allIdents = true;
        this.belongsToMethod = belongsToMethod;
        lineNumber = treeNode.getLine();
        
        /* Test helper
       	if (this.from.equals("domain.indirect.violatingfrom.CallInstanceMethodIndirect_SuperClass")){
    		if (lineNumber == 10) {
    			if (treeNode.getType() == JavaParser.METHOD_CALL) {		
    				boolean breakpoint1 = true;
    			}
    		}
    	} */

       	if ((treeNode.getChildCount() > 0) && (treeNode.getChild(0).getChildCount() > 0)) {
        	String invocTo = getAssignmentString(treeNode);
        	this.to = invocTo;
        	this.nameOfInstance = to;
        	this.invocationName = to;
        	createMethodInvocationDomainObject();
        }
    }

    private boolean TreeHasConstructorInvocation(CommonTree treeNode) {
        checkIfTreeHasConstructorInvocation(treeNode);
        return constructorInMethodInvocationFound;
    }

    private void checkIfTreeHasConstructorInvocation(Tree tree) {
        for (int childCount = 0; childCount < tree.getChildCount(); childCount++) {
            Tree child = tree.getChild(childCount);
            int treeType = child.getType();
            if (treeType == JavaParser.ARGUMENT_LIST) { //cut the loop, otherwise another constructor might be found
                break;
            }
            if (treeType == JavaParser.CLASS_CONSTRUCTOR_CALL) {
                constructorInMethodInvocationFound = true;
            }
            checkIfTreeHasConstructorInvocation(child);
        }
    }

    private void createMethodInvocationDomainObject() {
        if (to != null && to != "" && !SkippedTypes.isSkippable(to)) {
            modelService.createMethodInvocation(from, to, lineNumber, invocationName, belongsToMethod, nameOfInstance);
        }
    }

    private void createMethodOrPropertyFieldInvocationDetailsWhenConstructorIsFound(Tree tree) {
        if (tree == null) {
            return;
        }
        if (foundAllMethodInvocInfo) {
            return;
        }
        for (int childCount = 0; childCount < tree.getChildCount(); childCount++) {
            CommonTree child = (CommonTree) tree.getChild(childCount);
            if (child.getType() == JavaParser.DOT) {
                for (int iChild = 0; iChild < child.getChildCount(); iChild++) {
                    if (child.getChild(iChild).getType() != JavaParser.IDENT) {
                        allIdents = false;
                        break;
                    }
                }
                if (allIdents == false) {
                    invocationName = child.getChild(1).getText();
                    this.lineNumber = child.getLine();
                } else {
                    this.to = child.getChild(0).getText();
                    invocationName = child.getChild(1).getText();
                    break;
                }
            } else if (child.getType() == JavaParser.QUALIFIED_TYPE_IDENT && (allIdents == false)) {
            	this.to = getToValue(child);
                foundAllMethodInvocInfo = true;
            }
            createMethodOrPropertyFieldInvocationDetailsWhenConstructorIsFound(child);
        }
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

    private void createPropertyOrFieldInvocationDetails(Tree tree) {
        if (tree != null) {
            for (int childCount = 0; childCount < tree.getChildCount(); childCount++) {
                CommonTree treeNode = (CommonTree) tree.getChild(childCount);
                if (childCount == 0 && (treeNode.getType() == JavaParser.IDENT) && (treeNode.getParent().getType() != JavaParser.VAR_DECLARATOR)) {
                	leftVariableInAssignment = treeNode.getText();
                	if (this.to == null || this.to.equals("")) {
                        to = treeNode.getText();
                        nameOfInstance = to;
                    }
                }
                if (childCount == 1 && (treeNode.getType() == JavaParser.IDENT)) {
                	rightVariableInAssignment = treeNode.getText();
                	if (invocationNameFound == false) {
                		invocationName = treeNode.getText();
                		invocationNameFound = true;
                		this.lineNumber = treeNode.getLine();
                	}
                }
                if (treeNode.getType() == JavaParser.EXPR) {
                    this.parseExprToAssociation(treeNode);
                }
                createPropertyOrFieldInvocationDetails(tree.getChild(childCount));
            }
        }
    }

    private void parseExprToAssociation(CommonTree exprTree) {
        String to = "";
        String invocationName = "";
        CommonTree dotTree = (CommonTree) exprTree.getFirstChildWithType(JavaParser.DOT);
        if (dotTree != null) {
            to = dotTree.getChild(0).getText();
            invocationName = dotTree.getChild(dotTree.getChildCount() - 1).getText();
            modelService.createPropertyOrFieldInvocation(from, to, lineNumber, invocationName, belongsToMethod, to);
        } else {
            switch (exprTree.getType()) {
                case JavaParser.DECIMAL_LITERAL:
                case JavaParser.STRING_LITERAL:
                case JavaParser.TRUE:
                case JavaParser.FALSE:
                    break;
                case JavaParser.IDENT:
                    to = exprTree.getText();
                    modelService.createPropertyOrFieldInvocation(from, to, lineNumber, to, belongsToMethod, to);
                    break;
                case JavaParser.METHOD_CALL:
                    JavaInvocationGenerator myInvocationGenerator = new JavaInvocationGenerator(this.from);
                    myInvocationGenerator.generateMethodInvocToDomain(exprTree, belongsToMethod);
                    break;
                default:
                    for (int i = 0; i < exprTree.getChildCount(); i++) {
                        parseExprToAssociation((CommonTree) exprTree.getChild(i));
                    }
            }
        }
    }

    public void generatePropertyOrFieldInvocToDomain(CommonTree treeNode, String belongsToMethod) {
        allIdents = true;
        this.belongsToMethod = belongsToMethod;

        if (treeNode != null) {
	        if (TreeHasConstructorInvocation(treeNode)) {
	            createMethodOrPropertyFieldInvocationDetailsWhenConstructorIsFound(treeNode);
	            createPropertyOrFieldInvocationDomainObject();
	        } else {
	        	int treeType = treeNode.getType();
	        	if((treeType == JavaParser.ASSIGN) || (treeType == JavaParser.NOT_EQUAL) || (treeType == JavaParser.EQUAL) || (treeType == JavaParser.GREATER_OR_EQUAL) || (treeType == JavaParser.LESS_OR_EQUAL)){
	        		// CreateAssignmentDetails(treeNode);
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
            		this.invocationName = "";
	        		createPropertyOrFieldInvocationDomainObject();
	        		// Create invocation of variable at right side of =
	        		this.to = rightVariableInAssignment;
	        		this.nameOfInstance = to;
            		this.invocationName = "";
	        		createPropertyOrFieldInvocationDomainObject();
	        	} else {
	            	createPropertyOrFieldInvocationDetails(treeNode);
	        		createPropertyOrFieldInvocationDomainObject();
	        	}
	        }
        }
    }

    private String getAssignmentString(CommonTree tree) {
    	String returnValue = "";
    	try {
	        if (tree.getType() == JavaParser.DOT) { // "."
	    		String left = getAssignmentString((CommonTree) tree.getChild(0));
	    		String right = getAssignmentString((CommonTree) tree.getChild(1));
	    		returnValue += left + "." + right;
	        } else if (tree.getType() == JavaParser.METHOD_CALL) {
	    		String left = getAssignmentString((CommonTree) tree.getChild(0));
	    		String right = getAssignmentString((CommonTree) tree.getChild(1));
	    		returnValue += left + "(" + right + ")";
	        } else if (tree.getType() == JavaParser.CLASS_CONSTRUCTOR_CALL) {
	        	returnValue += getAssignmentString((CommonTree) tree.getChild(0));
	        } else if ((tree.getType() == JavaParser.STATIC_ARRAY_CREATOR) || (tree.getText().equals("STATIC_ARRAY_CREATOR"))) {
	        	if ((tree.getChildCount() > 0) && (tree.getChild(0).getType() == JavaParser.QUALIFIED_TYPE_IDENT)) {
	        		returnValue += tree.getChild(0).getChild(0).getText();
	        	} else {
	        		returnValue += "";
	        	}
	        } else if ((tree.getType() == JavaParser.ARGUMENT_LIST) || (tree.getType() == JavaParser.EXPR)) {
	    		for (int i = 0; i < tree.getChildCount(); i++) {
	    			String argTo= getAssignmentString((CommonTree) tree.getChild(i));
	    			createPropertyOrFieldInvocationDomainObject(argTo);
	        		returnValue += "";
	                /* if (i == tree.getChildCount() - 1) { // Activate to include the arguments in the method call
	                	returnValue += argTo;
	                } else {
	                	returnValue += argTo + ",";
	                } */
	    		}
	        } else if ((tree.getType() == JavaParser.IDENT) || (tree.getType() == JavaParser.STRING_LITERAL) || (tree.getType() == JavaParser.QUALIFIED_TYPE_IDENT)) {
	        	//returnValue = tree.getText();
	        	returnValue = getToValue(tree);
	        } else if (tree.getType() == JavaParser.CAST_EXPR) {
	    		returnValue += getAssignmentString((CommonTree) tree.getChild(1));
	    		// Create association of typecast-type access
	    		CommonTree typeChild = (CommonTree) tree.getFirstChildWithType(JavaParser.TYPE);
	        	if (typeChild != null) {
	        		CommonTree identChild = (CommonTree) typeChild.getFirstChildWithType(JavaParser.QUALIFIED_TYPE_IDENT);
	        		if ((identChild != null) && (identChild.getChildCount() > 0)) {
	        			String typeCastTo = identChild.getChild(0).getText();
	        			this.lineNumber = tree.getLine();
	        			createPropertyOrFieldInvocationDomainObject(typeCastTo);
	        		}
	        	}
	        } else {
	        	//int type = tree.getType(); // Test helper
	        	returnValue += "";
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
    
    private void createPropertyOrFieldInvocationDomainObject(String invocationTo) {
        if ((invocationTo != null) && (invocationTo != "") && !SkippedTypes.isSkippable(invocationTo)) {
            modelService.createPropertyOrFieldInvocation(from, invocationTo, lineNumber, invocationTo, belongsToMethod, invocationTo);
        }
    }

}

