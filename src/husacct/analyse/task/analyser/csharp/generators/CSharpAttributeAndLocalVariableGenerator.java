package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.domain.IModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import husacct.analyse.infrastructure.antlr.java.JavaParser;
import husacct.analyse.task.analyser.java.JavaGeneratorToolkit;
import husacct.analyse.task.analyser.java.JavaInvocationGenerator;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class CSharpAttributeAndLocalVariableGenerator extends CSharpGenerator{
    private String name;
    private String accessControlQualifier;
    private boolean hasClassScope;
    private boolean isFinal;
    private String belongsToClass;		// Unique name, including packages
    private int lineNumber;
    private String belongsToMethod;		// Alleen voor local variables
    private String declareType;			// Type of the attribute. In case of an instance variable with a generic type e.g. ArrayList<Person>, this value is ArrayList.
    private String typeInClassDiagram; 	// E.g. in case of an instance variable with a generic type ArrayList<Person>, this value is Person.
    private boolean isComposite; 		// False if the type allows one value only, like Person; True in case of a generic type, or e.g. Person[]. 

    private boolean isLocalVariable;
    private int levelOfRecursionWithinGenericType;
    private CSharpInvocationGenerator csharpInvocationGenerator;
    private IModelCreationService modelService = new FamixCreationServiceImpl();
    
	public void generateAttributeToDomain(Tree treeNode, String packageAndClassName) {
        initialize();
		this.belongsToClass = packageAndClassName;
        // Test helpers
    	if (belongsToClass.contains("DeclarationVariableInstance_GenericType_OneTypeParameter")) {
    				boolean breakpoint = true;
    	} //
		walkThroughAST(treeNode);
		createAttributeObject();
	}

	public void generateLocalVariableToDomain(Tree treeNode, String belongsToClass, String belongsToMethod) {
        initialize();
		this.belongsToClass = belongsToClass;
		this.belongsToMethod = belongsToMethod;
		walkThroughAST(treeNode);
		createLocalVariableObject();
	}

    public void generateLocalVariableForEachLoopToDomain(String packageAndClassName, String belongsToMethod, String name, String type, int line) {
        initialize();
        this.belongsToClass = packageAndClassName;
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
    }
    
	private void walkThroughAST(Tree treeNode) {
		for(int i = 0; i < treeNode.getChildCount(); i++){
            Tree child = treeNode.getChild(i);
            boolean walkThroughChildren = true;
			switch(child.getType()){
            // The first three cases are the default ones for a variable declaration, in order of appearance 
			case CSharpParser.MODIFIERS:
				setModifiers(child);
	            walkThroughChildren = false;
				break;
			case CSharpParser.TYPE:
				setType(child);		
	            walkThroughChildren = false;
				break;
			case CSharpParser.IDENTIFIER: // There is only one, since multiple names after a type result in multiple calls to this class.
				setName(child);		
	            walkThroughChildren = false;
				break;
        	// Assignment statements are passed to a suitable method of csharpInvocationGenerator 
			case CSharpParser.UNARY_EXPRESSION:
				csharpInvocationGenerator = new CSharpInvocationGenerator(this.belongsToClass);
				csharpInvocationGenerator.generateInvocationToDomain((CommonTree) child, this.belongsToMethod);
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
	            case CSharpParser.PRIVATE:
	            	accessControlQualifier = "private";
	            	break;
	            case CSharpParser.PUBLIC:
	            	accessControlQualifier = "public";
	            	break;
	            case CSharpParser.PROTECTED:
	            	accessControlQualifier = "protected";
	            	break;
	            case CSharpParser.STATIC:
	            	hasClassScope = true;
	            	break;
	            case CSharpParser.READONLY: case CSharpParser.CONST: 
	                isFinal = true;
	            	break;
        	}
        }
    }

	private void setType(Tree typeNode) {
		CommonTree typeTree = (CommonTree) typeNode;
		// Determine the type of the declared variable
		csharpInvocationGenerator = new CSharpInvocationGenerator(this.belongsToClass);
    	String foundType = csharpInvocationGenerator.getCompleteToString(typeTree);
        if ((foundType != null) && !foundType.equals("")) {
            this.declareType = foundType;
        } 
        // Check if the types is a generic type, e.g.: List<Type1>, Dictionary<Type2, Type3>
        CommonTree typeArgumentList = CSharpGeneratorToolkit.getFirstDescendantWithType((CommonTree) typeTree, CSharpParser.TYPE_ARGUMENT_LIST);
        if (typeArgumentList != null) {
        	this.isComposite = true;
        	addGenericTypeParameters(typeArgumentList);
        }
        // Check if an array is declared.  
        CommonTree rankSpecifier = CSharpGeneratorToolkit.getFirstDescendantWithType((CommonTree) typeTree, CSharpParser.RANK_SPECIFIER);
        if (rankSpecifier != null) {
        	this.isComposite = true;
        }
	}

    // Detects generic type parameters, also in complex types, like: HashMap<ProfileDAO, ArrayList<FriendsDAO>>>
    private void addGenericTypeParameters(CommonTree genericType) {
    	csharpInvocationGenerator = null;
        int numberOfTypeParameters = genericType.getChildCount();
        for (int j = 0; j < numberOfTypeParameters; j++) {
            CommonTree parameterTypeOfGenericTree = (CommonTree) genericType.getChild(j);
        	// Check if parameterTypeOfGenericTree contains a generic type arg list. If so, handle it recursively.
            CommonTree genericTypeRecursive = CSharpGeneratorToolkit.getFirstDescendantWithType((CommonTree) parameterTypeOfGenericTree, CSharpParser.TYPE_ARGUMENT_LIST);
            if (genericTypeRecursive != null) {
            	levelOfRecursionWithinGenericType ++; // Needed to prevent that this.typeInClassDiagram is set with a type included in a recursive generic type. 
            	addGenericTypeParameters(genericTypeRecursive);
            } else {
	            CommonTree qualifiedType = CSharpGeneratorToolkit.getFirstDescendantWithType(parameterTypeOfGenericTree, CSharpParser.NAMESPACE_OR_TYPE_NAME);
	            if (qualifiedType != null) {
	                csharpInvocationGenerator = new CSharpInvocationGenerator(this.belongsToClass);
	            	String parameterTypeOfGeneric = csharpInvocationGenerator.getCompleteToString(qualifiedType); // ,belongsToClass
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

 	private void setName(Tree identifierTree) {
		if(identifierTree != null){
			this.name = identifierTree.getText();
			this.lineNumber = identifierTree.getLine();
		}
	}

	private void createAttributeObject() {
		if ((declareType != null) && !declareType.equals("")) {
			if(SkippableTypes.isSkippable(declareType)){
				modelService.createAttributeOnly(hasClassScope, isFinal, accessControlQualifier, belongsToClass, declareType, name, belongsToClass + "." + name, lineNumber, "", false);
	        } else {
	        	modelService.createAttribute(hasClassScope, isFinal, accessControlQualifier, belongsToClass, declareType, name, belongsToClass + "." + name, lineNumber, "", false);
			}
		}
	}

	private void createLocalVariableObject() {
		if ((declareType != null) && !declareType.equals("")) {
			if(SkippableTypes.isSkippable(declareType)){
				modelService.createLocalVariableOnly(belongsToClass, declareType, name, belongsToClass + "." + belongsToMethod + "." + name, lineNumber, belongsToMethod);
	        } else {
				modelService.createLocalVariable(belongsToClass, declareType, name, belongsToClass + "." + belongsToMethod + "." + name, lineNumber, belongsToMethod);
	        }
		}
	}

}
