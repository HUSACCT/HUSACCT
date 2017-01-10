package husacct.analyse.task.analyser.java;

import husacct.analyse.domain.IModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.analyse.infrastructure.antlr.java.Java7Parser;
import husacct.common.enums.DependencySubTypes;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

class JavaAttributeAndLocalVariableGenerator {
    private String name;
    private String accessControlQualifier;
    private boolean hasClassScope;
    private boolean isFinal;
    private String belongsToClass;
    private int lineNumber;
    private String belongsToMethod;
    private String declareType;			// Type of the attribute. In case of an instance variable with a generic type e.g. ArrayList<Person>, this value is ArrayList.
    private String typeInClassDiagram; 	// E.g. in case of an instance variable with a generic type ArrayList<Person>, this value is Person.
    private boolean isComposite; 		// False if the type allows one value only, like Person; True in case of a generic type, or e.g. Person[]. 

    private boolean isLocalVariable;
    private int levelOfRecursionWithinGenericType;
	private DependencySubTypes dependencySubType;
    private JavaInvocationGenerator javaInvocationGenerator;
    private IModelCreationService modelService = new FamixCreationServiceImpl();

    public void generateAttributeToDomain(Tree attributeTree, String belongsToClass) {
        /* Test helpers
    	if (belongsToClass.contains("DeclarationVariableInstance_MultipleAttributesAtTheSameLine")) {
    				boolean breakpoint = true;
    	} */
        initialize();
    	dependencySubType = DependencySubTypes.DECL_INSTANCE_VAR;
        this.belongsToClass = belongsToClass;
        walkThroughAST(attributeTree);
    }

    public void generateLocalVariableToDomain(Tree attributeTree, String belongsToClass, String belongsToMethod) {
        initialize();
        isLocalVariable = true;
		dependencySubType = DependencySubTypes.DECL_LOCAL_VAR;
        this.belongsToClass = belongsToClass;
        this.belongsToMethod = belongsToMethod;
        walkThroughAST(attributeTree);
    }

    public void generateLocalVariableForLoopToDomain(String belongsToClass, String belongsToMethod, String name, String type, int line) {
        initialize();
        isLocalVariable = true;
		dependencySubType = DependencySubTypes.DECL_LOCAL_VAR;
        this.belongsToClass = belongsToClass;
        this.belongsToMethod = belongsToMethod;
        this.name = name;
        this.declareType = type;
        this.lineNumber = line;
        createLocalVariableObject();
    }

    private void initialize(){
        hasClassScope = false;
        isFinal = false;
        isComposite = false;
        typeInClassDiagram = "";
        name = "";
        accessControlQualifier = "";
        belongsToClass = "";
        belongsToMethod = "";
        declareType = "";
        lineNumber = 0;
        levelOfRecursionWithinGenericType = 0;
        isLocalVariable = false;
        dependencySubType = null;
    }
    
    private void walkThroughAST(Tree tree) {
    	javaInvocationGenerator = null;
    	JavaAnnotationGenerator annotationGenerator = null;

        for (int i = 0; i < tree.getChildCount(); i++) {
            Tree child = tree.getChild(i);
            boolean walkThroughChildren = true;
            int treeType = child.getType();
            switch(treeType)
            {
            // The first three cases are the default ones for a variable declaration, in order of appearance 
            case Java7Parser.MODIFIER_LIST:
                setModifiers(child);
                walkThroughChildren = false;
            	break;
            case Java7Parser.TYPE:
            	setType(child);
                walkThroughChildren = false;
            	break;
            case Java7Parser.VAR_DECLARATOR_LIST:
                setName(child);
                // Walk through the children is needed, since these children may represent assignment statements too.
            	break;
        	// A variable declaration may present as prefix of the variable declaration
            case Java7Parser.AT:
                annotationGenerator = new JavaAnnotationGenerator();
                annotationGenerator.generateToDomain((CommonTree) child, belongsToClass, "variable");
            	break;
        	// Assignment statements are passed to a suitable method of JavaInvocationGenerator 
            case Java7Parser.EXPR:
                javaInvocationGenerator = new JavaInvocationGenerator(this.belongsToClass);
                javaInvocationGenerator.generatePropertyOrFieldInvocToDomain((CommonTree) child, belongsToMethod);
                walkThroughChildren = false;
            	break;
            case Java7Parser.METHOD_CALL:
            	javaInvocationGenerator = new JavaInvocationGenerator(this.belongsToClass);
               	javaInvocationGenerator.generateMethodInvocToDomain((CommonTree) child, belongsToMethod);
	            walkThroughChildren = false;
	            break;
            case Java7Parser.CLASS_CONSTRUCTOR_CALL:
                javaInvocationGenerator = new JavaInvocationGenerator(this.belongsToClass);
                javaInvocationGenerator.generateConstructorInvocToDomain((CommonTree) tree, belongsToMethod);
                walkThroughChildren = false;
            	break;
            }
            if (walkThroughChildren) {
            	walkThroughAST(child);
            }
        }
    }

    private void setModifiers(Tree ModifierList) {
    	accessControlQualifier = "package";
    	hasClassScope = false;
    	isFinal = false;
        for (int i = 0; i < ModifierList.getChildCount(); i++) {
            int treeType = ModifierList.getChild(i).getType();
            switch(treeType)
            {
	            case Java7Parser.PRIVATE:
	            	accessControlQualifier = "private";
	            	break;
	            case Java7Parser.PUBLIC:
	            	accessControlQualifier = "public";
	            	break;
	            case Java7Parser.PROTECTED:
	            	accessControlQualifier = "protected";
	            	break;
	            case Java7Parser.STATIC:
	            	hasClassScope = true;
	            	break;
	            case Java7Parser.FINAL:
	                isFinal = true;
	            	break;
        	}
        }
        // Set dependencySubType
        if (!isLocalVariable) {
    		if (hasClassScope) {
    			dependencySubType = DependencySubTypes.DECL_CLASS_VAR;
    		} else {
    			dependencySubType = DependencySubTypes.DECL_INSTANCE_VAR;
    		}
    	}
    }

    private void setType(Tree typeTree) {
        javaInvocationGenerator = new JavaInvocationGenerator(this.belongsToClass);
        // Check if the types is a generic type. If so, determine the subType and attributeName, based on the number of type parameters.
        CommonTree typeArgumentList = JavaGeneratorToolkit.getFirstDescendantWithType((CommonTree) typeTree, Java7Parser.GENERIC_TYPE_ARG_LIST);
        if (typeArgumentList != null) {
        	this.declareType = typeArgumentList.getParent().getText(); // Container type, e.g. ArrayList;
        	this.isComposite = true;
        	addGenericTypeParameters(typeArgumentList);
        } else {
        	this.declareType = javaInvocationGenerator.getCompleteToString((CommonTree) typeTree, belongsToClass, dependencySubType);
        	//	Check if the type contains an Array declaration.
            CommonTree arrayType = JavaGeneratorToolkit.getFirstDescendantWithType((CommonTree) typeTree, Java7Parser.ARRAY_DECLARATOR);
            if (arrayType != null) {
            	this.isComposite = true;
            	if (!hasClassScope) { 
                	this.typeInClassDiagram = this.declareType;
            	}
            }
        }
    }
    
    // Detects generic type parameters, also in complex types, like: HashMap<ProfileDAO, ArrayList<FriendsDAO>>>
    private void addGenericTypeParameters(CommonTree genericType) {
    	JavaInvocationGenerator javaInvocationGenerator = null;
        int numberOfTypeParameters = genericType.getChildCount();
        for (int j = 0; j < numberOfTypeParameters; j++) {
            CommonTree parameterTypeOfGenericTree = (CommonTree) genericType.getChild(j);
        	// Check if parameterTypeOfGenericTree contains a generic type arg list. If so, handle it recursively.
            CommonTree genericTypeRecursive = JavaGeneratorToolkit.getFirstDescendantWithType((CommonTree) parameterTypeOfGenericTree, Java7Parser.GENERIC_TYPE_ARG_LIST);
            if (genericTypeRecursive != null) {
            	levelOfRecursionWithinGenericType ++; // Needed to prevent that this.typeInClassDiagram is set with a type included in a recursive generic type. 
            	addGenericTypeParameters(genericTypeRecursive);
            } else {
	            CommonTree qualifiedType = JavaGeneratorToolkit.getFirstDescendantWithType(parameterTypeOfGenericTree, Java7Parser.QUALIFIED_TYPE_IDENT);
	            if (qualifiedType != null) {
	                javaInvocationGenerator = new JavaInvocationGenerator(belongsToClass);
	            	String parameterTypeOfGeneric = javaInvocationGenerator.getCompleteToString(qualifiedType, belongsToClass, null); // Null to prevent redundancy in creation of TypeParameters. 
	                if (parameterTypeOfGeneric != null) {
	                    if (!hasClassScope && (levelOfRecursionWithinGenericType == 0)) {
	                    	if (numberOfTypeParameters == 1) {
	                    		this.typeInClassDiagram = parameterTypeOfGeneric; 	// E.g. ArrayList<Person>, the type of the first TypeParameter is set. 
	                    	} else if ((numberOfTypeParameters == 2) && (j == 1)) {
	                    		this.typeInClassDiagram = parameterTypeOfGeneric;	// E.g. HshMap<String, Person>, the type of the second TypeParameter is set.
	                    	}
	                    }
	                	int currentLineNumber = qualifiedType.getLine();
	                	modelService.createTypeParameter(belongsToClass, currentLineNumber, parameterTypeOfGeneric, dependencySubType);
	                }
	            }
            }
        }
	}

    private void setName(Tree varDeclaratorListTree) {
        for (int i = 0; i < varDeclaratorListTree.getChildCount(); i++) {
            Tree varDeclaratorTree = varDeclaratorListTree.getChild(i);
            int treeType = varDeclaratorTree.getType();
            if (treeType == Java7Parser.VAR_DECLARATOR) {
            	CommonTree IdentTree = JavaGeneratorToolkit.getFirstDescendantWithType((CommonTree) varDeclaratorTree, Java7Parser.Identifier);
                if (IdentTree != null) {
                    this.name = IdentTree.getText();
                    this.lineNumber = IdentTree.getLine();
                    if (isLocalVariable) {
                    	createLocalVariableObject();
                    } else {
                    	createAttributeObject();
                    }
                } 
            }
        }
    }

    private void createAttributeObject() {
    	if ((declareType != null) && !declareType.trim().equals("")) {
    		if (declareType.endsWith(".")) {
	            declareType = declareType.substring(0, declareType.length() - 1); //deleting the last point
	        }
	        if (SkippedTypes.isSkippable(declareType)) {
	            modelService.createAttributeOnly(hasClassScope, isFinal, accessControlQualifier, belongsToClass, declareType, name, belongsToClass + "." + name, lineNumber, typeInClassDiagram, isComposite);
	        } else {
	            modelService.createAttribute(hasClassScope, isFinal, accessControlQualifier, belongsToClass, declareType, name, belongsToClass + "." + name, lineNumber, typeInClassDiagram, isComposite);
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