package husacct.analyse.task.analyser.java;

import java.util.List;

import org.apache.log4j.Logger;

import husacct.analyse.infrastructure.antlr.java.Java7Parser.CreatorContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.ExpressionContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.ExpressionListContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.PrimaryContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.TypeTypeContext;
import husacct.common.enums.DependencySubTypes;

public class ExpressionAnalyser extends JavaGenerator{

	/* Functionality:
	 * - Transforms expression in text (expressionText)
	 * - Creates dependencies on arguments
	 * - Creates dependencies on typeArguments
	 * - Nullifies arguments with "."or "'"
	 * - Removes type cast from expressionText
	 * - Replaces "super" by "superBaseClass"
	 */
	
	// Variables to create associations on arguments
    private String belongsToClass;
    private String to = "";
    private int lineNumber;
    private String belongsToMethod;
    
    private Logger logger = Logger.getLogger(ExpressionAnalyser.class);


    public ExpressionAnalyser(String uniqueClassName, String belongsToMethod) {
    	this.belongsToClass = uniqueClassName;
    	this.belongsToMethod = belongsToMethod;
    }
    
    public void analyseExpression(ExpressionContext mainExpression) {
    	try {
	    	if (mainExpression != null) {
	    		String expressionText1 = mainExpression.getText();
		    	if (mainExpression.primary() != null) {
		    		if (mainExpression.primary().expression() != null) {
		    			analyseExpression(mainExpression.primary().expression());
		    		} else if (mainExpression.primary().Identifier() != null) {
		    			analyseElementaryExpression(mainExpression);
		    		} else if (mainExpression.primary().literal() != null) {
		    			analyseElementaryExpression(mainExpression);
		    		} else if (mainExpression.primary().typeType() != null) {
		    			analyseElementaryExpression(mainExpression);
		    		}
		    	} else if (!mainExpression.expression().isEmpty()) {
			    	List<ExpressionContext> expressions = mainExpression.expression();
			    	int nrOfExpressions = expressions.size();
			    	if (nrOfExpressions == 1) {
			    		analyseElementaryExpression(mainExpression);
					} else if (nrOfExpressions >= 2 ) {
						if (mainExpression.getChild(1).getText().equals("[")) {
							analyseElementaryExpression(mainExpression);
						} else {
							for (ExpressionContext expression : expressions) {
								analyseElementaryExpression(expression);
							}
						}
					}
				} else if (mainExpression.creator() != null) {
					this.to = analyseCreator(mainExpression.creator());
					this.lineNumber = mainExpression.creator().start.getLine();
					addAssociationToModel();
		    	} else {
		    		// Should not be possible, based on the grammar.
					logger.warn(" Unexpected value in: " + belongsToClass + " Line: " + mainExpression.start.getLine());
		    	}
	    	}
    	}
		catch (Exception e) {
			logger.warn(" Exception while processing: " + belongsToClass + " " + e.getCause().toString());
		}
    }
    
    private void analyseElementaryExpression(ExpressionContext expression) {
		this.lineNumber = expression.getStart().getLine();
		this.to = buildToString(expression);
		addAssociationToModel();
        /* Test helper
		String expressionText2 = expression.getText();
		String to_string = this.to;
    	if (belongsToClass.contains("DeclarationVariableInstance_GenericType_MultipleTypeParameters")) {
			boolean breakpoint = true;
    	} 
		this.to = buildToString(expression);
		*/
    }
    
    /* Builds up a string, based on the values of the children in the line of the first sub-expressions.
     */
    private String buildToString(ExpressionContext expression) {
    	String to_string = "";
    	List<ExpressionContext> subExpressions = expression.expression();
    	int nrOfExpressions = subExpressions.size();
    	if (nrOfExpressions == 0) {
	    	if (expression.primary() != null) {
    			PrimaryContext primary = (PrimaryContext) expression.primary();
	    		if (primary.expression() != null) {
	    			to_string += buildToString(primary.expression());
	    		} else if (primary.Identifier() != null) {
	    			to_string += primary.getText();
	    		} else if (primary.literal() != null) {
	    			// To do: transform literal in type?
	    		} else if (primary.typeType() != null) {
	    			to_string += primary.getText();
	    		}
	    	} else if (expression.creator() != null) {
	    		to_string += analyseCreator(expression.creator());
	    	}
    	} else if (nrOfExpressions == 1 ) {
    		to_string += transformSingularExpressionToString(expression);
		} else if (nrOfExpressions >= 2 ) {
    		to_string += transformSingularExpressionToString(expression.expression(0));
			for (int i = 1 ; i < nrOfExpressions ; i++) {
	    		analyseElementaryExpression(expression.expression(i));
			}
		}
    	return to_string;
    }

    private String transformSingularExpressionToString(ExpressionContext expression) {
    	String to_string = "";
    	int childCount = expression.getChildCount();
		ExpressionContext subExpression = expression.expression(0);
 		if (expression.typeType() != null) {
			if (expression.getChild(0).equals("(")) { // Type Cast
				analyseTypeCast(expression.typeType(), DependencySubTypes.REF_TYPE_CAST);
			} else { // expression instanceof typecast
				analyseTypeCast(expression.typeType(), DependencySubTypes.REF_TYPE);
			}
			to_string += buildToString(subExpression);
		} else  if ((childCount >= 3) && expression.getChild(childCount - 1).getText().equals(")")) { // Call
			if ((expression.expressionList() != null)) {
				String expressionListText = analyseExpressionList(expression.expressionList());
				to_string += buildToString(subExpression) + "(" + expressionListText + ")";
			} else if (expression.getChild(childCount - 2).getText().equals("(")) {
				to_string += buildToString(subExpression) + "(" + ")";
			} else {
				to_string += buildToString(subExpression);
			}
		} else  if (expression.Identifier() != null) { // Call
			if (expression.Identifier().getText().equals("")) {
				to_string += buildToString(subExpression);
			} else {
				to_string += buildToString(subExpression) + "." + expression.Identifier().getText();
			}
		} else {
			// To do: expression '.' 'this'  and others 
		}
    	return to_string;
    }
    
    private void addAssociationToModel() {
        if ((to != null) && !to.trim().equals("") && !SkippedTypes.isSkippable(to)) {
            modelService.createVariableInvocation(belongsToClass, to, lineNumber, belongsToMethod);
        }
        to = "";
        lineNumber = 0;
    }

    private void analyseTypeCast(TypeTypeContext typeType, DependencySubTypes dependencySubType) {
    	String typeCastTo = "";
    	if (typeType != null) {
    		typeCastTo = determineTypeOfTypeType(typeType, belongsToClass, DependencySubTypes.REF_TYPE_CAST);
            if ((typeCastTo != null) && (typeCastTo != "") && !SkippedTypes.isSkippable(typeCastTo)) {
                modelService.createDeclarationTypeCast(belongsToClass, typeCastTo, typeType.start.getLine());
            }
    	}
    }
    
	private String analyseExpressionList(ExpressionListContext expressionList) { // Argument list without ()
		String expressionListString = expressionList.getText();
		for (ExpressionContext expression : (expressionList.expression())) {
			to = expression.getText(); 
			lineNumber = expression.start.getLine();
			createPropertyOrFieldInvocationDomainObject();
    		//Nullify the arguments in the method  signature, if needed. Currently, arguments with a "." or "," disable the indirect dependency detection algorithm. In case of future improvements: create a FamixArgument object per argument.
			if (to.contains(".") || to.contains(",")) {
				if (expressionListString.equals(to)) { // In case of 1 argument
					expressionListString = "";
				} else {
					expressionListString.replaceAll(to, "");
				}
			}
			to = "";
		}
		return expressionListString;
	}

	private String analyseCreator(CreatorContext creator) {
		String creatorString = "";
		String name = "";
		if (creator.createdName() != null) {
			if (creator.createdName().primitiveType() != null) {
				name = creator.createdName().getText();
			} else {
				name = transformIdentifierToString(creator.createdName().Identifier());
			}
		}
		String arguments = "()";
		if (creator.classCreatorRest() != null) {
			if (creator.classCreatorRest().arguments() != null) {
				if (creator.classCreatorRest().arguments().expressionList() != null) {
					arguments = "(" + analyseExpressionList(creator.classCreatorRest().arguments().expressionList()) + ")";
				}
			}
			if (creator.classCreatorRest().classBody() != null) {
				new ClassBodyAnalyser(belongsToClass, creator.classCreatorRest().classBody());
			}
		}
		creatorString = name + arguments;
		return creatorString;
	}
	
	private void createPropertyOrFieldInvocationDomainObject() {
        if ((to != null) && !to.trim().equals("") && !SkippedTypes.isSkippable(to)) {
            modelService.createVariableInvocation(belongsToClass, to, lineNumber, belongsToMethod);
        }
    }
}
