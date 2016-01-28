package husacct.analyse.task.analyser.java;

import husacct.analyse.domain.IModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.analyse.infrastructure.antlr.java.JavaParser;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

class JavaAttributeAndLocalVariableGenerator {
	// Attributes of the to-be-created variable
    private String name;
    private String accessControlQualifier;
    private boolean hasClassScope;
    private boolean isFinal;
    private String belongsToClass;
    private int lineNumber;
    private String belongsToMethod;
    private String declareType;			// Note: In case of an instance variable with a generic type e.g. ArrayList<Person>, this value is ArrayList.
    private String typeInClassDiagram; 	// E.g. in case of an instance variable with a generic type ArrayList<Person>, this value is Person.
    private boolean isComposite; 		// False if the type allows one value only, like Person; True in case of a generic type, or e.g. Person[]. 

    private int levelOfRecursionWithinGenericType;

    private IModelCreationService modelService = new FamixCreationServiceImpl();

    public void generateAttributeToDomain(Tree attributeTree, String belongsToClass) {
        /* Test helpers
    	if (belongsToClass.contains("DeclarationVariableInstance_GenericType_OneTypeParameter")) {
    				boolean breakpoint = true;
    	} */
        initialize();
        this.belongsToClass = belongsToClass;
        startFiltering(attributeTree, belongsToClass);
        createAttributeObject();
    }

    public void generateLocalVariableToDomain(Tree attributeTree, String belongsToClass, String belongsToMethod) {
        initialize();
        this.belongsToClass = belongsToClass;
        this.belongsToMethod = belongsToMethod;
        startFiltering(attributeTree, belongsToClass);
        createLocalVariableObject();
    }

    public void generateLocalVariableForLoopToDomain(String belongsToClass, String belongsToMethod, String name, String type, int line) {
        initialize();
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
    }
    
    private void startFiltering(Tree attributeTree, String belongsToClass) {
        CommonTree currentTree = (CommonTree) attributeTree;
        CommonTree IdentTree = (CommonTree) currentTree.getFirstChildWithType(JavaParser.IDENT);
        if (IdentTree != null) {
            this.name = IdentTree.getText();
        } 
        walkThroughAST(attributeTree);
    }

    private void walkThroughAST(Tree tree) {
    	JavaInvocationGenerator javaInvocationGenerator = null;
    	JavaAnnotationGenerator annotationGenerator = null;

        for (int i = 0; i < tree.getChildCount(); i++) {
            Tree child = tree.getChild(i);
            boolean walkThroughChildren = true;
            int treeType = child.getType();
            switch(treeType)
            {
            case JavaParser.MODIFIER_LIST:
                setModifiers(child);
            	break;
            case JavaParser.TYPE:
                javaInvocationGenerator = new JavaInvocationGenerator(this.belongsToClass);
                // Check if the types is a generic type. If so, determine the subType and attributeName, based on the number of type parameters.
                CommonTree genericType = JavaGeneratorToolkit.getFirstDescendantWithType((CommonTree) child, JavaParser.GENERIC_TYPE_ARG_LIST);
                if (genericType != null) {
                	this.declareType = genericType.getParent().getText(); // Container type, e.g. ArrayList;
                	this.isComposite = true;
                	addGenericTypeParameters(genericType);
                } else {
                	this.declareType = javaInvocationGenerator.getCompleteToString((CommonTree) child, belongsToClass);
                	//	Check if the type contains an Array declaration.
                    CommonTree arrayType = JavaGeneratorToolkit.getFirstDescendantWithType((CommonTree) child, JavaParser.ARRAY_DECLARATOR);
                    if (arrayType != null) {
                    	this.isComposite = true;
                    	if (!hasClassScope) { 
	                    	this.typeInClassDiagram = this.declareType;
                    	}
                    }
                }
                walkThroughChildren = false;
            	break;
            case JavaParser.VAR_DECLARATOR:
                setAttributeName(child);
            	break;
            case JavaParser.EXPR:
                javaInvocationGenerator = new JavaInvocationGenerator(this.belongsToClass);
                javaInvocationGenerator.generatePropertyOrFieldInvocToDomain((CommonTree) child, belongsToMethod);
                walkThroughChildren = false;
            	break;
            case JavaParser.GENERIC_TYPE_ARG_LIST:
            	break;
            case JavaParser.METHOD_CALL:
            	javaInvocationGenerator = new JavaInvocationGenerator(this.belongsToClass);
               	javaInvocationGenerator.generateMethodInvocToDomain((CommonTree) child, belongsToMethod);
	            walkThroughChildren = false;
	            break;
            case JavaParser.CLASS_CONSTRUCTOR_CALL:
                javaInvocationGenerator = new JavaInvocationGenerator(this.belongsToClass);
                javaInvocationGenerator.generateConstructorInvocToDomain((CommonTree) tree, belongsToMethod);
                walkThroughChildren = false;
            	break;
            case JavaParser.AT:
                annotationGenerator = new JavaAnnotationGenerator();
                annotationGenerator.generateToDomain((CommonTree) child, belongsToClass, "variable");
            	break;
            }
            if (walkThroughChildren) {
            	walkThroughAST(child);
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
            CommonTree genericTypeRecursive = JavaGeneratorToolkit.getFirstDescendantWithType((CommonTree) parameterTypeOfGenericTree, JavaParser.GENERIC_TYPE_ARG_LIST);
            if (genericTypeRecursive != null) {
            	levelOfRecursionWithinGenericType ++; // Needed to prevent that this.typeInClassDiagram is set with a type included in a recursive generic type. 
            	addGenericTypeParameters(genericTypeRecursive);
            } else {
	            CommonTree qualifiedType = JavaGeneratorToolkit.getFirstDescendantWithType(parameterTypeOfGenericTree, JavaParser.QUALIFIED_TYPE_IDENT);
	            if (qualifiedType != null) {
	                javaInvocationGenerator = new JavaInvocationGenerator(this.belongsToClass);
	            	String parameterTypeOfGeneric = javaInvocationGenerator.getCompleteToString(qualifiedType, belongsToClass);
	                if (parameterTypeOfGeneric != null) {
	                    if ((numberOfTypeParameters == 1) && !hasClassScope && (levelOfRecursionWithinGenericType == 0)) {
	                 		this.typeInClassDiagram = parameterTypeOfGeneric;
	                    }
	                	int currentLineNumber = qualifiedType.getLine();
	                	modelService.createGenericParameterType(belongsToClass, belongsToMethod, currentLineNumber, parameterTypeOfGeneric);
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
	        declareType = "";
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
	        	declareType = "";
	    }
    }

    private void setAttributeName(Tree tree) {
        for (int i = 0; i < tree.getChildCount(); i++) {
            Tree child = tree.getChild(i);
            int treeType = child.getType();
            if (treeType == JavaParser.IDENT) { // <164>
                this.name = child.getText();
                this.lineNumber = tree.getLine();
                break;
            }
            setAttributeName(child);
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
	            case JavaParser.PRIVATE:
	            	accessControlQualifier = "private";
	            	break;
	            case JavaParser.PUBLIC:
	            	accessControlQualifier = "public";
	            	break;
	            case JavaParser.PROTECTED:
	            	accessControlQualifier = "protected";
	            	break;
	            case JavaParser.STATIC:
	            	hasClassScope = true;
	            	break;
	            case JavaParser.FINAL:
	                isFinal = true;
	            	break;
        	}
        }
    }

}