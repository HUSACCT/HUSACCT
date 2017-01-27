package husacct.analyse.task.analyser.java;

import java.util.List;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.log4j.Logger;

import husacct.analyse.domain.IModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.CatchClauseContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.CatchTypeContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.ConstDeclarationContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.ConstantDeclaratorContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.EnhancedForControlContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.FieldDeclarationContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.LocalVariableDeclarationContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.ModifierContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.QualifiedNameContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.ResourceContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.TypeArgumentContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.TypeArgumentsContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.TypeTypeContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.VariableDeclaratorContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.VariableDeclaratorIdContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.VariableDeclaratorsContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.VariableModifierContext;
import husacct.analyse.task.analyser.VisibilitySet;
import husacct.common.enums.DependencySubTypes;

class VariableAnalyser extends JavaGenerator{
    private String name = "";
    private String visibility = "";
    private boolean hasClassScope = false;
    private boolean isFinal = false;
    private String belongsToClass = "";
    private int lineNumber = 0;
    private String belongsToMethod = "";
    private String declareType = "";			// Type of the attribute. In case of an instance variable with a generic type e.g. ArrayList<Person>, this value is ArrayList.
    private boolean isComposite = false; 		// False if the type allows one value only, like Person; True in case of a generic type, or e.g. Person[]. 
    private String typeInClassDiagram = ""; 	// E.g. in case of an instance variable with a generic type ArrayList<Person>, this value is Person.
    private boolean isLocalVariable = false;
	private DependencySubTypes dependencySubType = null;

    private IModelCreationService modelService = new FamixCreationServiceImpl();
    private Logger logger = Logger.getLogger(VariableAnalyser.class);

    public VariableAnalyser(String belongsToClass) {
        this.belongsToClass = belongsToClass;
    }
    
    public void analyseVariable(List<ModifierContext> modifierList, FieldDeclarationContext fieldDeclaration) {
        /* Test helpers
    	if (belongsToClass.contains("husacct.analyse.task.reconstruct.ReconstructArchitectureDTOList")) {
    		if (fieldDeclaration.start.getLine() == 37) {
    				boolean breakpoint = true;
    		}
    	} */
    	try {
	        this.isLocalVariable = false;
	        this.visibility = determineVisibility(modifierList);
	        this.isFinal = determineIsFinal(modifierList);
	        this.hasClassScope = determineIsStatic(modifierList);
			if (hasClassScope) {
				dependencySubType = DependencySubTypes.DECL_CLASS_VAR;
			} else {
				dependencySubType = DependencySubTypes.DECL_INSTANCE_VAR;
			}
			dispatchAnnotationsOfMember(modifierList, belongsToClass);
			this.declareType = determineTypeOfTypeType(fieldDeclaration.typeType(), belongsToClass, dependencySubType);
			if (!hasClassScope) {
				determine_isComposite_and_typeInClassDiagram(fieldDeclaration.typeType());
			}
			determine_name(fieldDeclaration.variableDeclarators());
			dispatchAssignment(fieldDeclaration.variableDeclarators());
    	}
		catch (Exception e) {
			logger.warn(" Exception while processing: " + belongsToClass + " Line: " + fieldDeclaration.start.getLine() + " " + e.getMessage());
		}
    }

    public void analyseLocalVariable(LocalVariableDeclarationContext localVariableDeclaration, String belongsToMethod) {
    	try {
	    	this.isLocalVariable = true;
	        this.isFinal = determine_IsFinalForLocalVariable(localVariableDeclaration);
			this.dependencySubType = DependencySubTypes.DECL_LOCAL_VAR;
	        this.belongsToMethod = belongsToMethod;
	        dispatchAnnotationsOfLocalVariable(localVariableDeclaration.variableModifier());
	        this.declareType = determineTypeOfTypeType(localVariableDeclaration.typeType(), belongsToClass, dependencySubType);
			determine_name(localVariableDeclaration.variableDeclarators());
			dispatchAssignment(localVariableDeclaration.variableDeclarators());
    	}
		catch (Exception e) {
			logger.warn(" Exception while processing: " + belongsToClass + " Line: " + localVariableDeclaration.start.getLine() + " " + e.getMessage());
		}
    }

    public void analyseForControlVariable(EnhancedForControlContext enhancedForControl, String belongsToMethod) {
    	try {
    		this.isLocalVariable = true;
	        this.belongsToMethod = belongsToMethod;
			this.dependencySubType = DependencySubTypes.DECL_LOCAL_VAR;
	        dispatchAnnotationsOfLocalVariable(enhancedForControl.variableModifier());
	        this.declareType = determineTypeOfTypeType(enhancedForControl.typeType(), belongsToClass, dependencySubType);
			determine_name(enhancedForControl.variableDeclaratorId());
    	}
		catch (Exception e) {
			logger.warn(" Exception while processing: " + belongsToClass + " Line: " + enhancedForControl.start.getLine() + " " + e.getMessage());
		}
    }

    public void analyseCatchClauseVariable(CatchClauseContext catchClause, String belongsToMethod) {
    	try {
	    	this.isLocalVariable = true;
	        this.belongsToMethod = belongsToMethod;
			this.dependencySubType = DependencySubTypes.DECL_EXCEPTION;
	        dispatchAnnotationsOfLocalVariable(catchClause.variableModifier());
	        this.declareType = determine_type(catchClause.catchType());
			this.name = catchClause.Identifier().getText();
			this.lineNumber = catchClause.start.getLine();
	        createObject();
    	}
		catch (Exception e) {
			logger.warn(" Exception while processing: " + belongsToClass + " Line: " + catchClause.start.getLine() + " " + e.getMessage());
		}
    }

    public void analyseResourceVariable(ResourceContext resource, String belongsToMethod) {
    	try {
	    	this.isLocalVariable = true;
	        this.belongsToMethod = belongsToMethod;
			this.dependencySubType = DependencySubTypes.DECL_LOCAL_VAR;
	        dispatchAnnotationsOfLocalVariable(resource.variableModifier());
	        this.declareType = resource.classOrInterfaceType().getText();
			determine_name(resource.variableDeclaratorId());
    	}
		catch (Exception e) {
			logger.warn(" Exception while processing: " + belongsToClass + " Line: " + resource.start.getLine() + " " + e.getMessage());
		}
    }

    public void analyseConstant(List<ModifierContext> modifierList, ConstDeclarationContext constDeclaration) {
    	try {
	        this.isLocalVariable = false;
	        this.visibility = VisibilitySet.PUBLIC.toString();
	        this.isFinal = true;
	        this.hasClassScope = true;
			dependencySubType = DependencySubTypes.DECL_CLASS_VAR;
			dispatchAnnotationsOfMember(modifierList, belongsToClass);
			this.declareType = determineTypeOfTypeType(constDeclaration.typeType(), belongsToClass, dependencySubType);
			for (ConstantDeclaratorContext constantDeclarator : constDeclaration.constantDeclarator()) {
				if (constantDeclarator.Identifier() != null) {
					this.lineNumber = constantDeclarator.start.getLine();
					this.name = constantDeclarator.Identifier().getText();
					createObject();
				}
    			if ((constantDeclarator.variableInitializer() != null) && (constantDeclarator.variableInitializer().expression() != null)) {
    				new ExpressionAnalyser(belongsToClass, belongsToMethod, constantDeclarator.variableInitializer().expression());
    			}
			}
    	}
		catch (Exception e) {
			logger.warn(" Exception while processing: " + belongsToClass + " Line: " + constDeclaration.start.getLine() + " " + e.getMessage());
		}
    }
    
    public void analyseEnumConstant(TerminalNode identifier) {
    	try {
	        this.isLocalVariable = false;
	        this.visibility = VisibilitySet.PUBLIC.toString();
	        this.isFinal = true;
	        this.hasClassScope = true;
			dependencySubType = DependencySubTypes.DECL_CLASS_VAR;
			this.declareType = belongsToClass;
			this.name = identifier.getText();
    	}
		catch (Exception e) {
			logger.warn(" Exception while processing: " + belongsToClass + " Line: " + identifier.getText());
		}
    }
    
    public void analyseAnnotationConstant(List<ModifierContext> modifierList, TypeTypeContext typeType, VariableDeclaratorsContext variableDeclarators) {
    	try {
	        this.isLocalVariable = false;
	        this.visibility = VisibilitySet.PUBLIC.toString();
	        this.isFinal = true;
	        this.hasClassScope = true;
			dependencySubType = DependencySubTypes.DECL_CLASS_VAR;
			dispatchAnnotationsOfMember(modifierList, belongsToClass);
			this.declareType = determineTypeOfTypeType(typeType, belongsToClass, dependencySubType);
			determine_name(variableDeclarators);
    	}
		catch (Exception e) {
			logger.warn(" Exception while processing: " + belongsToClass + " Line: " + typeType.start.getLine() + " " + e.getMessage());
		}
    }
    
    private void determine_name(VariableDeclaratorsContext variableDeclaratorsList) {
    	for (VariableDeclaratorContext variableDeclarator : variableDeclaratorsList.variableDeclarator()) {
    		determine_name(variableDeclarator.variableDeclaratorId());
    	}
     }

    private void determine_name(VariableDeclaratorIdContext variableDeclaratorId) {
		if (variableDeclaratorId != null) {
			if (variableDeclaratorId.Identifier() != null) {
                this.lineNumber = variableDeclaratorId.start.getLine();
                this.name = variableDeclaratorId.getText();
                createObject();
			}
		}
     }

    private String determine_type(CatchTypeContext catchType) {
    	String returnValue = "";
    	for (QualifiedNameContext qualifiedName : catchType.qualifiedName()) {
    		returnValue = qualifiedName.getText();
    	}
    	return returnValue;
     }

    private void dispatchAssignment(VariableDeclaratorsContext attributeTree) {
    	for (VariableDeclaratorContext variableDeclarator : attributeTree.variableDeclarator()) {
    		if (variableDeclarator.variableInitializer() != null) {
    			if (variableDeclarator.variableInitializer().expression() != null) {
    				new ExpressionAnalyser(belongsToClass, belongsToMethod, variableDeclarator.variableInitializer().expression());
    			}
    		}
    	}
     }

    private void determine_isComposite_and_typeInClassDiagram(TypeTypeContext typeType) {
    	if (typeType.classOrInterfaceType() != null) {
            if (typeType.stop.getText().equals("]")) { // In case of array, e.g. VariableDTO[]
            	this.isComposite = true;
            	if (!hasClassScope) { 
                	this.typeInClassDiagram = this.declareType;
            	}
            } else if (typeType.classOrInterfaceType().typeArguments() != null) { // Check on (generic) type parameters. 
        		for (TypeArgumentsContext typeArguments : typeType.classOrInterfaceType().typeArguments()) {
        			for (TypeArgumentContext typeArgument : typeArguments.typeArgument()) {
        				if (typeArgument.typeType() != null) {
	        				this.typeInClassDiagram = transformIdentifierToString(typeArgument.typeType().classOrInterfaceType().Identifier());
	           				// Note in both cases Person will be assigned: ArrayList<Person>, HashMap<String, Person>.
	                    	this.isComposite = true;
        				}
        			}
        		}
            }
		}
    	if (!isComposite) {
    		this.typeInClassDiagram = this.declareType;
    	}
    }

    private boolean determine_IsFinalForLocalVariable(LocalVariableDeclarationContext localVariableDeclaration) {
    	boolean isFinalReturn = false;
        if (localVariableDeclaration.variableModifier() != null) {
        	int size = localVariableDeclaration.variableModifier().size();
        	for (int i = 0; i < size; i++) {
            	if (localVariableDeclaration.variableModifier(i).annotation() == null) {
					String modifier = localVariableDeclaration.variableModifier(i).getText();
		            if (modifier.equals("final")) {
		            	isFinalReturn = true;
		            }
            	}
			}
 		}
        return isFinalReturn;
    }

    private void dispatchAnnotationsOfLocalVariable(List<VariableModifierContext> variableModifierList) {
    	for (VariableModifierContext variableModifier : variableModifierList) {
        	if (variableModifier.annotation() != null) {
				new AnnotationAnalyser(variableModifier.annotation(), this.belongsToClass);
        	}
		}
     }
    
    private void createObject() {
        if (isLocalVariable) {
        	createLocalVariableObject();
        } else {
    		createAttributeObject();
        }
    }
    
    private void createAttributeObject() {
    	if ((name != null) && !name.trim().equals("") && (declareType != null) && !declareType.trim().equals("")) {
    		if (declareType.endsWith(".")) {
	            declareType = declareType.substring(0, declareType.length() - 1); //deleting the last point
	        }
	        if (SkippedTypes.isSkippable(declareType)) {
	            modelService.createAttributeOnly(hasClassScope, isFinal, visibility, belongsToClass, declareType, name, belongsToClass + "." + name, lineNumber, typeInClassDiagram, isComposite);
	        } else {
	            modelService.createAttribute(hasClassScope, isFinal, visibility, belongsToClass, declareType, name, belongsToClass + "." + name, lineNumber, typeInClassDiagram, isComposite);
	        }
    	}
    }

    private void createLocalVariableObject() {
    	if ((declareType != null) && !declareType.trim().equals("")) {
	        if (declareType.endsWith(".")) {
	            declareType = declareType.substring(0, declareType.length() - 1); //deleting the last point
	        }
	        if (SkippedTypes.isSkippable(declareType)) {
	            modelService.createLocalVariableOnly(belongsToClass, declareType, name, belongsToClass + "." + belongsToMethod + "." + this.name, lineNumber, this.belongsToMethod);
	        } else {
	        	modelService.createLocalVariable(belongsToClass, declareType, name, belongsToClass + "." + belongsToMethod + "." + this.name, lineNumber, this.belongsToMethod);
	        }
	    }
    }
}