package husacct.analyse.task.analyser.java;

import husacct.analyse.domain.IModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.analyse.infrastructure.antlr.java.JavaParser;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

class JavaAttributeAndLocalVariableGenerator {

    private boolean hasClassScope;
    private boolean isFinal;
    private String name;
    private String accessControlQualifier;
    private String belongsToClass;
    private String declareType;
    private int lineNumber;
    private String belongsToMethod;
    private IModelCreationService modelService = new FamixCreationServiceImpl();

    public void generateAttributeToDomain(Tree attributeTree, String belongsToClass) {
        /* Test helpers
    	if (belongsToClass.contains("technology.direct.dao.UserDAO")) {
    				boolean breakpoint = true;
    	} */
        this.belongsToClass = belongsToClass;
        startFiltering(attributeTree, belongsToClass);
        createAttributeObject();
    }

    public void generateLocalVariableToDomain(Tree attributeTree, String belongsToClass, String belongsToMethod) {
        this.belongsToClass = belongsToClass;
        this.belongsToMethod = belongsToMethod;
        startFiltering(attributeTree, belongsToClass);
        createLocalVariableObject();
    }

    public void generateLocalVariableForLoopToDomain(String belongsToClass, String belongsToMethod, String name, String type, int line) {
        this.belongsToClass = belongsToClass;
        this.belongsToMethod = belongsToMethod;
        this.name = name;
        this.declareType = type;
        this.lineNumber = line;
        createLocalVariableObject();
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
                this.declareType = javaInvocationGenerator.getCompleteToString((CommonTree) child);
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
                annotationGenerator.generateToDomain((CommonTree) child, belongsToClass);
            	break;
            }
            if (walkThroughChildren) {
            	walkThroughAST(child);
            }
        }
    }

    private void createAttributeObject() {
    	if ((declareType != null) && !declareType.trim().equals("")) {
    		if (declareType.endsWith(".")) {
	            declareType = declareType.substring(0, declareType.length() - 1); //deleting the last point
	        }
	        if (SkippedTypes.isSkippable(declareType)) {
	            modelService.createAttributeOnly(hasClassScope, isFinal, accessControlQualifier, belongsToClass, declareType, name, belongsToClass + "." + name, lineNumber);
	        } else {
	            modelService.createAttribute(hasClassScope, isFinal, accessControlQualifier, belongsToClass, declareType, name, belongsToClass + "." + name, lineNumber);
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