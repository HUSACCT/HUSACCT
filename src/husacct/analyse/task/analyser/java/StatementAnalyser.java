package husacct.analyse.task.analyser.java;

import java.util.List;

import husacct.analyse.infrastructure.antlr.java.Java7Parser;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.ExpressionContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.StatementContext;
import husacct.common.enums.DependencySubTypes;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.apache.log4j.Logger;

public class StatementAnalyser extends JavaGenerator {

    private String from;
    private String to = "";
    private String leftVariableInAssignment = "";
    private String rightVariableInAssignment = "";
    private int lineNumber;
    private String belongsToMethod;
    private Logger logger = Logger.getLogger(StatementAnalyser.class);


    public StatementAnalyser(String uniqueClassName) {
        from = uniqueClassName;
    }

    public void generateConstructorInvocToDomain(CommonTree commonTree, String belongsToMethod) {
        this.belongsToMethod = belongsToMethod;
        this. lineNumber = commonTree.getLine();
       	if ((commonTree.getChildCount() > 0)) {
        	String invocTo = getCompleteToString(commonTree, from, DependencySubTypes.DECL_TYPE_PARAMETER);
        	this.to = invocTo;
            if (to != null && !to.trim().equals("") && !SkippedTypes.isSkippable(to)) {
                modelService.createMethodInvocation(from, to, lineNumber, belongsToMethod, "InvocConstructor");
            }
        }
    }

    public void generateMethodInvocToDomain(CommonTree treeNode, String belongsToMethod) {
        this.belongsToMethod = belongsToMethod;
        lineNumber = treeNode.getLine();
      	if ((treeNode.getChildCount() > 0)) {
        	String invocTo = getCompleteToString(treeNode, from, DependencySubTypes.DECL_TYPE_PARAMETER);
        	this.to = invocTo;
            if (to != null && !to.trim().equals("") && !SkippedTypes.isSkippable(to)) {
                modelService.createMethodInvocation(from, to, lineNumber, belongsToMethod, "InvocMethod");
            }
        }
    }

    public void analyseStatement(StatementContext statement, String belongsToMethod) {
        this.belongsToMethod = belongsToMethod;
		String rs = statement.getText();
		if ((statement.statementExpression() != null) && (statement.statementExpression().expression() != null)) {
			String rs1 = statement.statementExpression().expression().getText();
			delegateExpression(statement.statementExpression().expression());
			
/*			if (statement.statementExpression().expression().getChildCount() == 4) {
				delegateExpression(statement.statementExpression().expression());
			}
			if (statement.statementExpression().expression().getChildCount() >= 1) {
				if (statement.statementExpression().expression().getChildCount() == 3) {
					if (statement.statementExpression().expression().getChild(1) instanceof TerminalNodeImpl) {
						if (statement.statementExpression().expression().getChild(0) instanceof ExpressionContext && 
								(statement.statementExpression().expression().getChild(2) instanceof ExpressionContext)) {
							// E.g: expression "=" expression
							addExpressionToModel((ExpressionContext) statement.statementExpression().expression().getChild(0));
							addExpressionToModel((ExpressionContext) statement.statementExpression().expression().getChild(2));
						}
					}
				}
				if (rs1.endsWith(")")) {
					// E.g: hashMap.clear()
					this.to = rs1;
					this.lineNumber = statement.start.getLine();
					createPropertyOrFieldInvocationDomainObject();
				}
*/			}
    	if (statement.getChild(0).getText().equals("return")) {
    		// E.g: return pDao.get(0).toString()
			if (statement.getChild(1) instanceof ExpressionContext) {
				addExpressionToModel((ExpressionContext) statement.getChild(1));
			}
    	}
    }

    // To do: 1) niet zuinig; replacement van argumenten werkt niet goed!
    private void delegateExpression(ExpressionContext expression) {
		ExpressionAnalyser expressionAnalyser = new ExpressionAnalyser(from, belongsToMethod);
		ParseTreeWalker walker = new ParseTreeWalker();
		//walker.walk(expressionAnalyser, expression);
    	this.lineNumber = expression.getStart().getLine();
    	this.to = expression.getText();
    	// this.to = expressionAnalyser.getExpressionText();
		createPropertyOrFieldInvocationDomainObject();
		if (expression.expression() != null) {
			for (ExpressionContext subExpression : expression.expression()) {
				delegateExpression(subExpression);
			}
		}
    }
    
    private void addExpressionToModel(ExpressionContext expression) {
    	this.lineNumber = expression.getStart().getLine();
    	this.to = expression.getText();
		createPropertyOrFieldInvocationDomainObject();
    }

    public void analyseExpression(ExpressionContext expression, String belongsToMethod) {
        this.belongsToMethod = belongsToMethod;
        addExpressionToModel(expression);
        
/*        if (treeNode != null) {
            for (int childCount = 0; childCount < treeNode.getChildCount(); childCount++) {
                CommonTree childNode = (CommonTree) treeNode.getChild(childCount);
                if (childCount == 0) {
                	leftVariableInAssignment = getCompleteToString(childNode, from, DependencySubTypes.DECL_TYPE_PARAMETER);
                }
                if (childCount == 1) {
                	rightVariableInAssignment = getCompleteToString(childNode, from, DependencySubTypes.DECL_TYPE_PARAMETER);
            		this.lineNumber = childNode.getLine();
                }
            }
    		
    		// Create invocation of variable at left side of =
    		this.to = leftVariableInAssignment;
    		createPropertyOrFieldInvocationDomainObject();
    		
    		int treeType = treeNode.getStart().getType();
        	if((treeType == Java7Parser.ASSIGN) || (treeType == Java7Parser.NOTEQUAL) || (treeType == Java7Parser.EQUAL) 
        			|| (treeType == Java7Parser.GE) || (treeType == Java7Parser.LE)
        			|| (treeType == Java7Parser.LT) || (treeType == Java7Parser.GT)){
        		// Create invocation of variable at right side of =
        		this.to = rightVariableInAssignment;
        		createPropertyOrFieldInvocationDomainObject();
        	}
        }
*/    }

    /* Returns the complete string of an expression of a variable, also in case of chaining call/access combinations.
     * In case of a type cast, it does not include the type cast in the returnValue, but it creates a declaration association.
     * Use this method to determine the to-string in case in the following cases: 
     * 1) type declaration of a (local) variable, parameter, or return type; 2) at both sides of an assignment; 3) at both sides of an comparison. 
     */
    public String getCompleteToString(CommonTree tree, String belongsToClass, DependencySubTypes dependencySubType) {  
    	String returnValue = "";
/*    	if (tree == null) {
    		return returnValue;
    	}
    	try {
    		int treeType = tree.getType();
    		if (tree.getText().equals("STATIC_ARRAY_CREATOR")) {
    			treeType = Java7Parser.STATIC_ARRAY_CREATOR;	
    		}
    		switch(treeType) {
	        case Java7Parser.DOT: // "."
	        	String left = getCompleteToString((CommonTree) tree.getChild(0), belongsToClass, dependencySubType);
	        	String right = getCompleteToString((CommonTree) tree.getChild(1), belongsToClass, dependencySubType);
	        	if ((left == "") || (right == "")) {
	        		returnValue += left + right;
	        	} else {
		    		returnValue += left + "." + right;
	        	}
	            break;
	        case Java7Parser.METHOD_CALL:
	        	String left1 = getCompleteToString((CommonTree) tree.getChild(0), belongsToClass, dependencySubType);
	        	String right1 = getCompleteToString((CommonTree) tree.getChild(1), belongsToClass, dependencySubType);
	        	right1 = nullifyIfStringContainsDot(right1);
	    		returnValue += left1 + "(" + right1 + ")";
	            break;
	        case Java7Parser.CLASS_CONSTRUCTOR_CALL: 
	        	String left2 = getCompleteToString((CommonTree) tree.getChild(0), belongsToClass, dependencySubType);
	        	String right2 = getCompleteToString((CommonTree) tree.getChild(1), belongsToClass, dependencySubType);
	        	right2 = nullifyIfStringContainsDot(right2);
	    		returnValue += left2 + "(" + right2 + ")";
	            break;
	        case Java7Parser.SUPER_CONSTRUCTOR_CALL: 
	        	String superConstructorCallArg = getCompleteToString((CommonTree) tree.getChild(0), belongsToClass, dependencySubType);
	        	superConstructorCallArg = nullifyIfStringContainsDot(superConstructorCallArg);
	        	returnValue += "superBaseClass" + "(" + superConstructorCallArg + ")";
	            break;
	        case Java7Parser.STATIC_ARRAY_CREATOR: 
	        	if ((tree.getChildCount() > 0) && ((tree.getChild(0).getType() == Java7Parser.QUALIFIED_TYPE_IDENT) || (tree.getChild(0).getType() == Java7Parser.Identifier))) {
		        	String left3 = getCompleteToString((CommonTree) tree.getChild(0), belongsToClass, dependencySubType);
		        	String right3 = getCompleteToString((CommonTree) tree.getChild(1), belongsToClass, dependencySubType);
		        	right3 = nullifyIfStringContainsDot(right3);
		        	returnValue += left3 + "(" + right3 + ")";
	        	} else {
	        		returnValue += "";
	        	}
	            break;
	        case Java7Parser.VAR_DECLARATOR: case Java7Parser.VAR_DECLARATOR_LIST:
	        	returnValue += getCompleteToString((CommonTree) tree.getChild(0), belongsToClass, dependencySubType);
	            break;
	        case Java7Parser.TYPE: case Java7Parser.QUALIFIED_TYPE_IDENT:
                // Check if the types is a generic type. If so, determine the subType and attributeName, based on the number of type parameters.
                CommonTree genericType = JavaGeneratorToolkit.getFirstDescendantWithType((CommonTree) tree, Java7Parser.GENERIC_TYPE_ARG_LIST);
                if ((genericType != null) && (dependencySubType != null)) {
                	addGenericTypeParameters(genericType, belongsToClass, dependencySubType);
                } 
	            int childCount = tree.getChildCount();
	            returnValue += getCompleteToString((CommonTree) tree.getChild(0), belongsToClass, dependencySubType);
	            for (int currentChild = 1; currentChild < childCount; currentChild++) {
		        	returnValue += "." + getCompleteToString((CommonTree) tree.getChild(currentChild), belongsToClass, dependencySubType);
	            }
	            if (returnValue.endsWith(".")) {
	            	returnValue = returnValue.substring(0, returnValue.length() - 1); //deleting the last point
	            }
	            break;
	        case Java7Parser.EXPR: case Java7Parser.PARENTESIZED_EXPR:
	        	returnValue += getCompleteToString((CommonTree) tree.getChild(0), belongsToClass, dependencySubType);
	            break;
	        case Java7Parser.ARGUMENT_LIST:
	    		for (int i = 0; i < tree.getChildCount(); i++) {
	    			String argTo= getCompleteToString((CommonTree) tree.getChild(i), belongsToClass, dependencySubType);
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
	        case Java7Parser.ADD:
	        	returnValue += getCompleteToString((CommonTree) tree.getChild(0), belongsToClass, dependencySubType);
    			String argTo= getCompleteToString((CommonTree) tree.getChild(1), belongsToClass, dependencySubType);
	        	createPropertyOrFieldInvocationDomainObject(argTo, (CommonTree) tree.getChild(1));
	        	break;
	        case Java7Parser.Identifier:
	        	returnValue = tree.getText();
	            break;
	        case Java7Parser.THIS:
	        	returnValue = "";
	            break;
	        case Java7Parser.SUPER:
	        	returnValue = "superBaseClass";
	            break;
	        case Java7Parser.DECIMAL_LITERAL: case Java7Parser.INT: 
	        	returnValue += "int";
	            break;
	        case Java7Parser.STRING_LITERAL:
	        	returnValue += "String";
	            break;
	        case Java7Parser.BooleanLiteral: case Java7Parser.BOOLEAN:
	        	returnValue += "boolean";
	            break;
	        case Java7Parser.CHAR: 
	        	returnValue += "char";
	            break;
	        case Java7Parser.BYTE:
	        	returnValue += "byte";
	            break;
	        case Java7Parser.CAST_EXPR:
	    		returnValue += getCompleteToString((CommonTree) tree.getChild(1), belongsToClass, dependencySubType);
	    		// Create association of typecast-type access
	    		CommonTree typeChild = (CommonTree) tree.getFirstChildWithType(Java7Parser.TYPE);
	        	if (typeChild != null) {
	        		CommonTree identChild = (CommonTree) typeChild.getFirstChildWithType(Java7Parser.QUALIFIED_TYPE_IDENT);
	        		if ((identChild != null) && (identChild.getChildCount() > 0)) {
	        			String typeCastTo = identChild.getChild(0).getText();
	                    for (int i = 1; i < identChild.getChildCount(); i++) { // In case of inner classes
	                    	typeCastTo = typeCastTo + "." + identChild.getChild(i).getText();
	                    }
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
*/        return returnValue;
    }
    
    private void createPropertyOrFieldInvocationDomainObject() {
        if ((to != null) && !to.trim().equals("") && !SkippedTypes.isSkippable(to)) {
            modelService.createVariableInvocation(from, to, lineNumber, belongsToMethod);
        }
    }
    
    private void createPropertyOrFieldInvocationDomainObject(String invocationTo, CommonTree tree) {
        if ((invocationTo != null) && !invocationTo.trim().equals("") && !SkippedTypes.isSkippable(invocationTo)) {
            modelService.createVariableInvocation(from, invocationTo, tree.getLine(), belongsToMethod);
        }
    }

    // Detects generic type parameters, also in complex types, like: HashMap<ProfileDAO, ArrayList<FriendsDAO>>>
/*    private void addGenericTypeParameters(CommonTree genericType, String belongsToClass, DependencySubTypes dependencySubType) {
        int numberOfTypeParameters = genericType.getChildCount();
        for (int j = 0; j < numberOfTypeParameters; j++) {
            CommonTree parameterTypeOfGenericTree = (CommonTree) genericType.getChild(j);
        	// Check if parameterTypeOfGenericTree contains a generic type arg list. If so, handle it recursively.
            CommonTree genericTypeRecursive = JavaGeneratorToolkit.getFirstDescendantWithType((CommonTree) parameterTypeOfGenericTree, Java7Parser.GENERIC_TYPE_ARG_LIST);
            if (genericTypeRecursive != null) {
            	addGenericTypeParameters(genericTypeRecursive, belongsToClass, dependencySubType);
            } else {
	            CommonTree qualifiedType = JavaGeneratorToolkit.getFirstDescendantWithType(parameterTypeOfGenericTree, Java7Parser.QUALIFIED_TYPE_IDENT);
	            if (qualifiedType != null) {
	            	String parameterTypeOfGeneric = getCompleteToString(qualifiedType, from, null); // Last argument = null, since no recursion should take place here.
	                if ((parameterTypeOfGeneric != null) && (dependencySubType != null)) {
	                	int currentLineNumber = qualifiedType.getLine();
	                	modelService.createTypeParameter(belongsToClass, currentLineNumber, parameterTypeOfGeneric, dependencySubType);
	                }
	            }
            }
        }
	}
*/
    private String nullifyIfStringContainsDot(String argument) {
    	String returnValue = "";
		if (!argument.contains(".")) { // Currently, arguments with a "." or "," disable the indirect dependency detection algorithm. In case of future improvements: create a FamixArgument object per argument. 
			returnValue = argument;
		}
		return returnValue;
    }
}

