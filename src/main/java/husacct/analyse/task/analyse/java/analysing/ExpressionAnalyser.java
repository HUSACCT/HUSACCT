package husacct.analyse.task.analyse.java.analysing;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.log4j.Logger;

import husacct.analyse.task.analyse.java.parsing.JavaParser.CreatorContext;
import husacct.analyse.task.analyse.java.parsing.JavaParser.ExplicitGenericInvocationContext;
import husacct.analyse.task.analyse.java.parsing.JavaParser.ExplicitGenericInvocationSuffixContext;
import husacct.analyse.task.analyse.java.parsing.JavaParser.ExpressionContext;
import husacct.analyse.task.analyse.java.parsing.JavaParser.ExpressionListContext;
import husacct.analyse.task.analyse.java.parsing.JavaParser.FormalParameterContext;
import husacct.analyse.task.analyse.java.parsing.JavaParser.InnerCreatorContext;
import husacct.analyse.task.analyse.java.parsing.JavaParser.LambdaExpressionContext;
import husacct.analyse.task.analyse.java.parsing.JavaParser.LastFormalParameterContext;
import husacct.analyse.task.analyse.java.parsing.JavaParser.PrimaryContext;
import husacct.analyse.task.analyse.java.parsing.JavaParser.SuperSuffixContext;
import husacct.analyse.task.analyse.java.parsing.JavaParser.TypeArgumentsContext;
import husacct.analyse.task.analyse.java.parsing.JavaParser.TypeArgumentsOrDiamondContext;
import husacct.analyse.task.analyse.java.parsing.JavaParser.TypeTypeContext;
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
	private static final String superReplacement = "superBaseClass";
	
	// Variables to create associations on arguments
    private String belongsToClass;
    private String to = "";
    private int lineNumber;
    private String belongsToMethod;
    
    private Logger logger = Logger.getLogger(ExpressionAnalyser.class);


    public ExpressionAnalyser(String uniqueClassName, String belongsToMethod, ExpressionContext mainExpression) {
    	this.belongsToClass = uniqueClassName;
    	this.belongsToMethod = belongsToMethod;
    	analyseExpression(mainExpression);
    }
    
    // Analyses an ExpressionContext; based on the definition of "expression" in Java7.g4.
    private void analyseExpression(ExpressionContext mainExpression) {
    	try {
	    	if (mainExpression != null) {
	    		//String expressionText1 = mainExpression.getText();
		    	if (mainExpression.primary() != null) {
		    		if (mainExpression.primary().expression() != null) {
		    			analyseExpression(mainExpression.primary().expression());
		    		} else if (mainExpression.primary().Identifier() != null) {
		    			analyseElementaryExpression(mainExpression);
		    		} else if (mainExpression.primary().arrayType() != null) {
		    			analyseElementaryExpression(mainExpression);
		    		} else if (mainExpression.primary().literal() != null) {
		    			analyseElementaryExpression(mainExpression);
		    		} else if (mainExpression.primary().typeType() != null) {
		    			analyseElementaryExpression(mainExpression);
		    		}
		    	} else if (mainExpression.lambdaExpression() != null) {
		    		analyseLambdaExpression(mainExpression.lambdaExpression());
		    	} else if ((mainExpression.expression() != null ) && !mainExpression.expression().isEmpty()) {
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
        /* Test helper
		String expressionText2 = expression.getText();
		String to_string = this.to;
    	if (belongsToClass.equals("domain.indirect.violatingfrom.AccessObjectReferenceIndirect_AsReturnValue_MethodDerivedViaHeuristic") &&
    			(expression.start.getLine() == 11)) {
				boolean breakpoint = true;
    	} */
		
		this.lineNumber = expression.getStart().getLine();
		this.to = transformExpressionToString(expression);
		addAssociationToModel();
    }
    
    /* Builds up a string, based on the values of the children.
     * If subexpressions are contained, only the first one is transformed to text and included.
     * Following subexpressions are processed as individual expressions.
     */
    private String transformExpressionToString(ExpressionContext expression) {
    	String to_string = "";
    	List<ExpressionContext> subExpressions = expression.expression();
    	int nrOfSubExpressions = subExpressions.size();
    	if (nrOfSubExpressions == 0) {
	    	if (expression.primary() != null) {
    			PrimaryContext primary = expression.primary();
	    		if (primary.expression() != null) {
	    			to_string += transformExpressionToString(primary.expression());
	    		} else if (primary.Identifier() != null) {
	    			to_string += primary.getText();
	    		} else if (primary.arrayType() != null) {
	    			if (primary.arrayType().Identifier() != null) {
	    				to_string += primary.arrayType().Identifier().getText();
	    			} else if (primary.arrayType().classOrInterfaceType() != null) {
	    				to_string += primary.arrayType().classOrInterfaceType().getText();
	    			}
	    		} else if (primary.literal() != null) {
	    			if (primary.literal().IntegerLiteral() != null) {
	    				to_string += "int";
	    			} else if (primary.literal().FloatingPointLiteral() != null) {
		    			to_string += "float";
	    			} else if (primary.literal().CharacterLiteral() != null) {
		    			to_string += "char";
	    			} else if (primary.literal().StringLiteral() != null) {
		    			to_string += "String";
	    			} else if (primary.literal().BooleanLiteral() != null) {
		    			to_string += "boolean";
	    			}
	    		} else if (primary.typeType() != null) {
	    			to_string += primary.getText();
	    		} else if (primary.getText().equals("super")) {
	    			to_string += superReplacement;
	    		}
	    	} else if (expression.creator() != null) {
	    		to_string += analyseCreator(expression.creator());
	    	}
    	} else if (nrOfSubExpressions == 1 ) {
    		to_string += transformSingularExpressionToString(expression);
		} else if (nrOfSubExpressions >= 2 ) {
    		to_string += transformSingularExpressionToString(expression.expression(0));
			for (int i = 1 ; i < nrOfSubExpressions ; i++) {
	    		analyseElementaryExpression(expression.expression(i));
			}
		}
    	return to_string;
    }

    /* Transforms the expression into a string of useful information for the post processor.
     * Precondition (taken care of in previous step): There is exactly one child-expression.
     * Consequently, only these sub-compositions in the definition of "expression"need to be handled.
     * Basically, all children are included in the resulting string. However not in case of:
     * 1) a type cast. 
     * 2) an instanceof statement.
     * 3) an argument that includes "." or ",".
     */
    private String transformSingularExpressionToString(ExpressionContext expression) {
    	// String testString = expression.getText();
    	String to_string = "";
    	int childCount = expression.getChildCount();
		ExpressionContext subExpression = expression.expression(0);
		if (childCount == 2) { // In case of subExpression with prefix or postfix text ('!'|'+'|'++', ...)
			to_string += transformExpressionToString(subExpression); 
		}
 		if (expression.typeType() != null) {
			if (expression.getChild(0).equals("(")) { // type cast
				analyseTypeCast(expression.typeType(), DependencySubTypes.REF_TYPE_CAST);
			} else { // expression instanceof typeType
				analyseTypeCast(expression.typeType(), DependencySubTypes.REF_TYPE);
			}
			to_string += transformExpressionToString(subExpression);
		} else if ((childCount >= 3) && expression.getChild(childCount - 1).getText().equals(")")) { // Call
			if ((expression.expressionList() != null)) { // List of arguments
				String expressionListText = analyseExpressionList(expression.expressionList());
				to_string += transformExpressionToString(subExpression) + "(" + expressionListText + ")";
			} else if (expression.getChild(childCount - 2).getText().equals("(")) {
				to_string += transformExpressionToString(subExpression) + "(" + ")";
			} else {
				to_string += transformExpressionToString(subExpression);
			}
		} else if (expression.Identifier() != null) {
			if (expression.Identifier().getText().equals("")) {
				to_string += transformExpressionToString(subExpression);
			} else {
				to_string += transformExpressionToString(subExpression) + "." + expression.Identifier().getText();
			}
		} else if (expression.innerCreator() != null){
			to_string += transformExpressionToString(subExpression);
			String constructorString = analyseInnerCreator(expression.innerCreator());
			if (!constructorString.equals("")) {
				to_string += "." + constructorString;
			}
		} else if (expression.superSuffix() != null) {
			to_string += transformExpressionToString(subExpression);
			String superSuffixString = analyseSuperSuffix(expression.superSuffix()); 
			if (!superSuffixString.equals("")) {
				to_string += "." + superReplacement + superSuffixString;
			}
		} else if ( expression.explicitGenericInvocation() != null) {
			to_string += transformExpressionToString(subExpression);
			String explicitGenericInvString = analyseExplicitGenericInvocation(expression.explicitGenericInvocation());
			if (!explicitGenericInvString.equals("")) {
				to_string += "." + explicitGenericInvString;
			}
		}
    	return to_string;
    }
    
    private void addAssociationToModel() {
        if ((to != null) && !to.trim().equals("") && !SkippedJavaTypes.isSkippable(to)) {
            modelService.createVariableInvocation(belongsToClass, to, lineNumber, belongsToMethod);
        }
        to = "";
        lineNumber = 0;
    }

	private void analyseLambdaExpression(LambdaExpressionContext lambdaExpression){
		if(lambdaExpression.lambdaParameters() != null) {
			if(lambdaExpression.lambdaParameters().Identifier() != null) {
				this.to = lambdaExpression.lambdaParameters().Identifier().getText();
				this.lineNumber = lambdaExpression.getStart().getLine();
				addAssociationToModel();
			}else if(lambdaExpression.lambdaParameters().formalParameterList() != null) {
				if (lambdaExpression.lambdaParameters().formalParameterList().formalParameter() != null) {
		    		for (FormalParameterContext parameter : lambdaExpression.lambdaParameters().formalParameterList().formalParameter()) {
	    				this.lineNumber = parameter.getStart().getLine();
		    			if (parameter.variableDeclaratorId() != null && parameter.variableDeclaratorId().Identifier() != null) {
		    				this.to = parameter.variableDeclaratorId().Identifier().getText();
		    				addAssociationToModel();
		    			}
		    			if (parameter.typeType() != null) {
		    				this.to = determineTypeOfTypeType(parameter.typeType(), belongsToClass);
		    				addAssociationToModel();
		    			}
		    		}
		    	} 
		    	if (lambdaExpression.lambdaParameters().formalParameterList().lastFormalParameter() != null) {
		    		LastFormalParameterContext parameter = lambdaExpression.lambdaParameters().formalParameterList().lastFormalParameter();
    				this.lineNumber = parameter.getStart().getLine();
		    		if (parameter.variableDeclaratorId() != null && parameter.variableDeclaratorId().Identifier() != null) {
	    				this.to = parameter.variableDeclaratorId().Identifier().getText();
	    				addAssociationToModel();
		    		}
		    		if (parameter.typeType() != null) {
	    				this.to = determineTypeOfTypeType(parameter.typeType(), belongsToClass);
	    				addAssociationToModel();
		    		}
				}
			} else if (lambdaExpression.lambdaParameters().inferredFormalParameterList() != null) {
				if (lambdaExpression.lambdaParameters().inferredFormalParameterList().Identifier() != null) {
					for (TerminalNode identifier : lambdaExpression.lambdaParameters().inferredFormalParameterList().Identifier()) {
	    				this.lineNumber = lambdaExpression.lambdaParameters().inferredFormalParameterList().start.getLine();
	    				this.to = identifier.getText();
	    				addAssociationToModel();
					}
				}
			}
		}
		if(lambdaExpression.lambdaBody() != null) {
			if(lambdaExpression.lambdaBody().expression() != null) {
				analyseElementaryExpression(lambdaExpression.lambdaBody().expression());
			} else if(lambdaExpression.lambdaBody().block() != null) {
				new BlockAnalyser(lambdaExpression.lambdaBody().block(), this.belongsToClass, this.belongsToMethod);
			}
		}
	}
	
    private void analyseTypeCast(TypeTypeContext typeType, DependencySubTypes dependencySubType) {
    	String typeCastTo = "";
    	if (typeType != null) {
    		typeCastTo = determineTypeOfTypeType(typeType, belongsToClass);
            if ((typeCastTo != null) && (!Objects.equals(typeCastTo, "")) && !SkippedJavaTypes.isSkippable(typeCastTo)) {
                modelService.createDeclarationTypeCast(belongsToClass, typeCastTo, typeType.start.getLine());
            }
    	}
    }
    
	private String analyseExpressionList(ExpressionListContext expressionList) { // Argument list without ()
		String returnString = "";
		if ((expressionList.expression() != null) && !expressionList.expression().isEmpty()) {
			for(int i = 0 ; i < expressionList.expression().size() ; i++) {
				ExpressionContext expression = expressionList.expression(i);
				String argumentText = "";
/*				if ((expression.expressionList() != null) && !expression.expressionList().isEmpty()) {
					argumentText = transformSingularExpressionToString(expression);
				} else {
					argumentText = expression.getText(); 
				}
*/				argumentText = transformExpressionToString(expression);
				int lineOfArgument = expression.start.getLine();
				createDependencyOnArgument(argumentText, lineOfArgument);
	    		//Nullify the arguments in the method  signature, if needed. Currently, arguments with a "." or "," disable the indirect dependency detection algorithm. In case of future improvements: create a FamixArgument object per argument.
				if (argumentText.contains(".") || argumentText.contains(",")) {
					if (i == 0) {
						returnString = ""; // No change
					} else {
						returnString += "," + "";
					}
				} else {
					if (i == 0) {
						returnString = argumentText;
					} else {
						returnString += "," + argumentText;
					}
				}
				
			}
		}
		return returnString;
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
			if (creator.createdName().typeArgumentsOrDiamond() != null) {
				for (TypeArgumentsOrDiamondContext typeArgumentsOrDiamond : creator.createdName().typeArgumentsOrDiamond()) {
					if ((typeArgumentsOrDiamond.typeArguments() != null) && (typeArgumentsOrDiamond.typeArguments().typeArgument() != null)) {
						List<TypeArgumentsContext> typeArgumentsList = new ArrayList<>();
						typeArgumentsList.add(typeArgumentsOrDiamond.typeArguments());
						analyseTypeArguments(belongsToClass, typeArgumentsList);
					}
				}
			}
		}
		String argumentsString = "()";
		if (creator.classCreatorRest() != null) {
			if (creator.classCreatorRest().arguments() != null) {
				if (creator.classCreatorRest().arguments().expressionList() != null) {
					argumentsString = "(" + analyseExpressionList(creator.classCreatorRest().arguments().expressionList()) + ")";
				}
			}
			if (creator.classCreatorRest().classBody() != null) {
				new TypeBodyAnalyser(belongsToClass).analyseClassBody(creator.classCreatorRest().classBody());;
			}
		}
		creatorString = name + argumentsString;
		return creatorString;
	}
	
	private String analyseInnerCreator(InnerCreatorContext creator) {
		String creatorString = "";
		String name = "";
		if (creator.Identifier() != null) {
			name = creator.Identifier().getText();
		}
		String argumentsString = "()";
		if (creator.classCreatorRest() != null) {
			if (creator.classCreatorRest().arguments() != null) {
				if (creator.classCreatorRest().arguments().expressionList() != null) {
					argumentsString = "(" + analyseExpressionList(creator.classCreatorRest().arguments().expressionList()) + ")";
				}
			}
			if (creator.classCreatorRest().classBody() != null) {
				new TypeBodyAnalyser(belongsToClass).analyseClassBody(creator.classCreatorRest().classBody());
			}
		}
		creatorString = name + argumentsString;
		return creatorString;
	}
	
	private String analyseSuperSuffix(SuperSuffixContext superSuffix) {
		String superSuffixString = "";
		String argumentsString = "()";
		if (superSuffix.arguments() != null) {
			if (superSuffix.arguments().expressionList() != null) {
				argumentsString = "(" + analyseExpressionList(superSuffix.arguments().expressionList()) + ")";
			}
		}
		String name = "";
		if (superSuffix.Identifier() != null) {
			name = superSuffix.Identifier().getText();
		}
		if (!name.equals("")) {
			superSuffixString = "."+ name + argumentsString;
		} else {
			superSuffixString = argumentsString;
		}
		return superSuffixString;
	}
	
	private String analyseExplicitGenericInvocation(ExplicitGenericInvocationContext explicitGenericInvocation) {
		String explicitGenericInvocationString = "";
		if (explicitGenericInvocation.explicitGenericInvocationSuffix() != null) {
			ExplicitGenericInvocationSuffixContext suffix = explicitGenericInvocation.explicitGenericInvocationSuffix();
			if (suffix.superSuffix() != null) {
				explicitGenericInvocationString  = superReplacement + analyseSuperSuffix(suffix.superSuffix());
			} else if (suffix.Identifier() != null) {
				String name = suffix.Identifier().getText();
				String argumentsString = "()";
				if ((suffix.arguments() != null) && (suffix.arguments().expressionList() != null)) {
					argumentsString = "(" + analyseExpressionList(suffix.arguments().expressionList()) + ")";
				}
				if ((name != null) && !name.equals("")) {
					explicitGenericInvocationString = name + argumentsString;
				} 
			}
		}
		return explicitGenericInvocationString;
	}
	
	private void createDependencyOnArgument(String argument, int lineOfArgument) {
        if ((argument != null) && !argument.trim().equals("") && !SkippedJavaTypes.isSkippable(argument)) {
            modelService.createVariableInvocation(belongsToClass, argument, lineOfArgument, belongsToMethod);
        }
    }

}
