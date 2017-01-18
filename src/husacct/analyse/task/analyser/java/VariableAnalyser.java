package husacct.analyse.task.analyser.java;

import java.util.List;

import husacct.analyse.domain.IModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.FieldDeclarationContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.LocalVariableDeclarationContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.ModifierContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.TypeArgumentContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.TypeArgumentsContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.TypeTypeContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.VariableDeclaratorContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.VariableDeclaratorsContext;
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

    public VariableAnalyser(String belongsToClass) {
        this.belongsToClass = belongsToClass;
    }
    
    public void analyseVariable(List<ModifierContext> modifierList, FieldDeclarationContext fieldDeclaration) {
        /* Test helpers
    	if (belongsToClass.contains("DeclarationVariableInstance_GenericType_OneTypeParameter")) {
    		if (modifierList.get(0).start.getLine() == 18) {
    				boolean breakpoint = true;
    		}
    	} */

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

    public void analyseLocalVariable(LocalVariableDeclarationContext localVariableDeclaration, String belongsToMethod) {
        this.isLocalVariable = true;
        this.isFinal = determine_IsFinalForLocalVariable(localVariableDeclaration);
		this.dependencySubType = DependencySubTypes.DECL_LOCAL_VAR;
        this.belongsToMethod = belongsToMethod;
        dispatchAnnotationsOfLocalVariable(localVariableDeclaration);
        this.declareType = determineTypeOfTypeType(localVariableDeclaration.typeType(), belongsToClass, dependencySubType);
		determine_name(localVariableDeclaration.variableDeclarators());
		dispatchAssignment(localVariableDeclaration.variableDeclarators());
    }

    // Still needed in J7?
    public void analyseLocalVariableForLoop(String belongsToClass, String belongsToMethod, String name, String type, int line) {
        isLocalVariable = true;
		dependencySubType = DependencySubTypes.DECL_LOCAL_VAR;
        this.belongsToClass = belongsToClass;
        this.belongsToMethod = belongsToMethod;
        this.name = name;
        this.declareType = type;
        this.lineNumber = line;
        createLocalVariableObject();
    }

    private void determine_name(VariableDeclaratorsContext attributeTree) {
    	for (VariableDeclaratorContext variableDeclarator : attributeTree.variableDeclarator()) {
    		if (variableDeclarator.variableDeclaratorId() != null) {
    			if (variableDeclarator.variableDeclaratorId().Identifier() != null) {
	                this.name = variableDeclarator.variableDeclaratorId().getText();
	                this.lineNumber = variableDeclarator.variableDeclaratorId().start.getLine();
	                if (isLocalVariable) {
	                	createLocalVariableObject();
	                } else {
	            		createAttributeObject();
	                }
    			}
    		}
    	}
     }

    private void dispatchAssignment(VariableDeclaratorsContext attributeTree) {
    	for (VariableDeclaratorContext variableDeclarator : attributeTree.variableDeclarator()) {
    		if (variableDeclarator.variableInitializer() != null) {
    			if (variableDeclarator.variableInitializer().expression() != null) {
    		        StatementAnalyser expressionAnalyser = new StatementAnalyser(this.belongsToClass);
        			expressionAnalyser.analyseExpression(variableDeclarator.variableInitializer().expression(), belongsToMethod);
    			}
    		}
    	}
     }

    private void determine_isComposite_and_typeInClassDiagram(TypeTypeContext typeTree) {
    	if (typeTree.classOrInterfaceType() != null) {
            if (typeTree.stop.getText().equals("]")) { // In case of array, e.g. VariableDTO[]
            	this.isComposite = true;
            	if (!hasClassScope) { 
                	this.typeInClassDiagram = this.declareType;
            	}
            } else if (typeTree.classOrInterfaceType().typeArguments() != null) { // Check on (generic) type parameters. 
        		for (TypeArgumentsContext typeArguments : typeTree.classOrInterfaceType().typeArguments()) {
        			for (TypeArgumentContext typeArgument : typeArguments.typeArgument()) {
        				this.typeInClassDiagram = transformIdentifierToString(typeArgument.typeType().classOrInterfaceType().Identifier());
           				// Note in both cases Person will be assigned: ArrayList<Person>, HashMap<String, Person>.
                    	this.isComposite = true;
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

    private void dispatchAnnotationsOfLocalVariable(LocalVariableDeclarationContext localVariableDeclaration) {
        if (localVariableDeclaration.variableModifier() != null) {
        	int size = localVariableDeclaration.variableModifier().size();
        	for (int i = 0; i < size; i++) {
            	if (localVariableDeclaration.variableModifier(i).annotation() != null) {
    				new AnnotationAnalyser(localVariableDeclaration.variableModifier(i).annotation(), this.belongsToClass);
            	}
			}
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