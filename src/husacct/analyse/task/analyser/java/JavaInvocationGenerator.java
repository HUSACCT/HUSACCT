package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.JavaParser;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

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

    public JavaInvocationGenerator(String uniqueClassName) {
        from = uniqueClassName;
    }

    public void generateConstructorInvocToDomain(CommonTree commonTree, String belongsToMethod) {
        invocationName = "Constructor";
        this.belongsToMethod = belongsToMethod;
        createConstructorInvocationDetails(commonTree);
        createConstructorInvocationDomainObject();
    }

    private void createConstructorInvocationDetails(Tree tree) {
        boolean constructorFound = false;
        if (tree.getType() == JavaParser.CLASS_CONSTRUCTOR_CALL) {
            createConstructorInvocationDetailsWhenFoundClassConstructorCall((CommonTree) tree);
            constructorFound = true;
        }
        if (constructorFound == false) {
            int childcount = tree.getChildCount();
            for (int i = 0; i < childcount; i++) {
                Tree child = tree.getChild(i);
                int treeType = child.getType();

                if (treeType == JavaParser.CLASS_CONSTRUCTOR_CALL) {
                    createConstructorInvocationDetailsWhenFoundClassConstructorCall((CommonTree) child);
                }
                createConstructorInvocationDetails(child);
            }
        }
    }

    private void createConstructorInvocationDetailsWhenFoundClassConstructorCall(CommonTree firstChildClassConstructorCall) {
        Tree child = firstChildClassConstructorCall.getChild(0);
        this.to = "";
        if (child.getType() != JavaParser.QUALIFIED_TYPE_IDENT) {
            this.to = child.getText();
        } else {
        	this.to = getToValue((CommonTree) child);
        }
        this.lineNumber = child.getLine();
    }

    private void createConstructorInvocationDomainObject() {
        if (!SkippedTypes.isSkippable(to)) {
            modelService.createConstructorInvocation(from, to, lineNumber, invocationName, belongsToMethod, nameOfInstance);
        }
    }

    public void generateMethodInvocToDomain(CommonTree treeNode, String belongsToMethod) {
        invocationNameFound = false;
        constructorInMethodInvocationFound = false;
        allIdents = true;
        this.belongsToMethod = belongsToMethod;
        lineNumber = treeNode.getLine();
        
        // Test helper
       	if (this.from.equals("domain.indirect.violatingfrom.CallInstanceMethodIndirect_MethodMethodViaConstructor")){
//    		if (lineNumber == 13) {
//    			if (child.getType() == JavaParser.METHOD_CALL) {		
    				boolean breakpoint1 = true;
//    			}
//    		}
    	} 

        if ((treeNode.getChildCount() > 0) && (treeNode.getChild(0).getChildCount() > 0)) {
        	int type = treeNode.getChild(0).getChild(0).getType();
        	if ((type == JavaParser.METHOD_CALL) || (type == JavaParser.DOT)) { // Needed for some indirect invocation test cases, e.g. CallStaticMethodIndirect_VarStaticMethod 
	        	String invocTo = getAssignmentString(treeNode);
	        	this.to = invocTo;
	        	this.nameOfInstance = to;
	        	this.invocationName = to;
	        	createMethodInvocationDomainObject();
        	}
	        if (TreeHasConstructorInvocation(treeNode)) {
	            createMethodOrPropertyFieldInvocationDetailsWhenConstructorIsFound(treeNode);
		        createMethodInvocationDomainObject();
	        } else {
	            createMethodInvocationDetails(treeNode);
		        createMethodInvocationDomainObject();
	        }
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

    private void createMethodInvocationDetails(Tree tree) {
        if (tree != null) {
            for (int childCount = 0; childCount < tree.getChildCount(); childCount++) {
                CommonTree treeNode = (CommonTree) tree.getChild(childCount);
                if (treeNode.getType() == JavaParser.IDENT && childCount == 0 && treeNode.getParent().getType() != JavaParser.VAR_DECLARATOR) {
                    if (this.to == null || this.to.equals("")) {
                        to = treeNode.getText();
                        nameOfInstance = to;
                    }
                }
                if (treeNode.getType() == JavaParser.IDENT && childCount == 1 && invocationNameFound == false) {
                    invocationName = treeNode.getText();
                    invocationNameFound = true;
                    this.lineNumber = treeNode.getLine();
                }
                if (treeNode.getType() == JavaParser.EXPR) {
                    this.parseExprToAssociation(treeNode);
                }
                createMethodInvocationDetails(tree.getChild(childCount));
            }
        }
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
	        	if(treeNode.getType() == JavaParser.ASSIGN){
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
        if (tree.getType() == JavaParser.DOT) { // "."
    		String left = getAssignmentString((CommonTree) tree.getChild(0));
    		String right = getAssignmentString((CommonTree) tree.getChild(1));
    		returnValue += left + "." + right;
        } else if (tree.getType() == JavaParser.METHOD_CALL) {
    		String left = getAssignmentString((CommonTree) tree.getChild(0));
    		String right = getAssignmentString((CommonTree) tree.getChild(1));
    		returnValue += left + "(" + right + ")";
        } else if ((tree.getType() == JavaParser.STATIC_ARRAY_CREATOR) || (tree.getText().equals("STATIC_ARRAY_CREATOR"))) {
        	if (tree.getChild(0).getType() == JavaParser.QUALIFIED_TYPE_IDENT) {
        		returnValue += tree.getChild(0).getChild(0).getText();
        	} else {
        		returnValue += "";
        	}
        } else if (tree.getType() == JavaParser.ARGUMENT_LIST) {
        	if (tree.getChildCount() == 0) {
        		returnValue += "";
        	}
        } else if ((tree.getType() == JavaParser.IDENT) || (tree.getType() == JavaParser.STRING_LITERAL) || (tree.getType() == JavaParser.QUALIFIED_TYPE_IDENT)) {
        	returnValue = tree.getText();
        } else if (tree.getType() == JavaParser.CAST_EXPR) {
        	CommonTree typeChild = (CommonTree) tree.getFirstChildWithType(JavaParser.TYPE);
        	String typeCastTo = typeChild.getFirstChildWithType(JavaParser.QUALIFIED_TYPE_IDENT).getChild(0).getText();
    		String right = getAssignmentString((CommonTree) tree.getChild(1));
    		returnValue += right;
        	// Generate FamixTypeCast
    		generateTypeCastToDomain(typeCastTo);
        } else {
        	int type = tree.getType();
        	returnValue += "";
        }
        return returnValue;
    }

    private void createPropertyOrFieldInvocationDomainObject() {
        if ((to != null) && (to != "") && !SkippedTypes.isSkippable(to)) {
            modelService.createPropertyOrFieldInvocation(from, to, lineNumber, invocationName, belongsToMethod, nameOfInstance);
        }
    }
    
    public void generateTypeCastToDomain(String typeCastTo) {
        if ((typeCastTo != null) && (typeCastTo != "") && !SkippedTypes.isSkippable(typeCastTo)) {
        	String typeCastInvocationName = "";
        	String typeCastNameOfInstance =  typeCastTo;
            modelService.createPropertyOrFieldInvocation(from, typeCastTo, lineNumber, typeCastInvocationName, belongsToMethod, typeCastNameOfInstance);
        }
    }

}
