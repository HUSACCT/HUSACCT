package husacct.analyse.task.analyser.java;

import java.util.ArrayList;

import husacct.analyse.domain.IModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.analyse.infrastructure.antlr.java.JavaParser;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

class JavaAttributeAndLocalVariableGenerator {

    private Boolean classScope = false;
    private String name;
    private String AccesControlQualifier;
    private String belongsToClass;
    private String declareType;
    private int lineNumber;
    private String belongsToMethod;
    private ArrayList<String> declareTypes = new ArrayList<String>();
    private IModelCreationService modelService = new FamixCreationServiceImpl();

    public void generateAttributeToDomain(Tree attributeTree, String belongsToClass) {
        startFiltering(attributeTree, belongsToClass);
        createAttributeObject();
    }

    public void generateLocalVariableToDomain(Tree attributeTree, String belongsToClass, String belongsToMethod) {
        this.belongsToMethod = belongsToMethod;
        startFiltering(attributeTree, belongsToClass);
        createLocalVariableObject();
    }

    private void startFiltering(Tree attributeTree, String belongsToClass) {
        CommonTree currentTree = (CommonTree) attributeTree;
        CommonTree IdentTree = (CommonTree) currentTree.getFirstChildWithType(JavaParser.IDENT);
        if (IdentTree != null) {
            this.name = IdentTree.getText();
        }
        this.belongsToClass = belongsToClass;
        walkThroughAST(attributeTree);
    }

    private void walkThroughAST(Tree tree) {
    	JavaInvocationGenerator javaInvocationGenerator = null;
    	JavaAnnotationGenerator annotationGenerator = null;
        boolean walkThroughChildren = true;

        for (int i = 0; i < tree.getChildCount(); i++) {
            Tree child = tree.getChild(i);

            // Test helper
           	if (this.belongsToClass.equals("domain.direct.violating.AccessLocalVariable_Argument")){
        		if (child.getLine() == 8) {
        				boolean breakpoint1 = true;
        		}
        	} //

            int treeType = child.getType();
            switch(treeType)
            {
            case JavaParser.MODIFIER_LIST:
                setAccesControllQualifier(tree);
                setClassScope(child);
            	break;
            case JavaParser.TYPE:
                setDeclareType(child);
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
                annotationGenerator = new JavaAnnotationGenerator(belongsToClass);
                annotationGenerator.generateMethod((CommonTree) child);
            	break;
            }
            if (walkThroughChildren) {
            	walkThroughAST(child);
            }
        }
    }

    private void createAttributeObject() {
        if (declareType.contains(".")) {
            declareType = declareType.substring(0, declareType.length() - 1); //deleting the last point
        }
        if (SkippedTypes.isSkippable(declareType)) {
            modelService.createAttributeOnly(classScope, AccesControlQualifier, belongsToClass, declareType, name, belongsToClass + "." + name, lineNumber);
        } else {
            modelService.createAttribute(classScope, AccesControlQualifier, belongsToClass, declareType, name, belongsToClass + "." + name, lineNumber, this.declareTypes);
        }
        declareType = "";
    }

    private void createLocalVariableObject() {
        if (declareType.contains(".")) {
            declareType = declareType.substring(0, declareType.length() - 1); //deleting the last point
        }
        if (SkippedTypes.isSkippable(declareType)) {
            //modelService.createLocalVariableOnly(belongsToClass, declareType, name, this.belongsToMethod + "." + this.name, lineNumber, this.belongsToMethod);
            modelService.createLocalVariable(belongsToClass, declareType, name, this.belongsToMethod + "." + this.name, lineNumber, this.belongsToMethod, this.declareTypes);
        } else {
        	modelService.createLocalVariable(belongsToClass, declareType, name, this.belongsToMethod + "." + this.name, lineNumber, this.belongsToMethod, this.declareTypes);
        }
        	declareType = "";
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

    private void setDeclareType(Tree typeTree) {
        Tree child = typeTree.getChild(0);
        Tree declaretype = child.getChild(0);
        String foundType = "";
        if (child.getType() != JavaParser.QUALIFIED_TYPE_IDENT) {
            foundType = child.getText();
        } else {
            if (child.getChildCount() > 1) {
                for (int i = 0; i < child.getChildCount(); i++) {
                    foundType += child.getChild(i).toString() + ".";
                }
            } else {
                foundType = declaretype.getText();
            }
        }
        if (this.declareType == null || this.declareType.equals("")) {
            this.declareType = foundType;
        } else {
            declareTypes.add(foundType);
        }
    }

    private void setAccesControllQualifier(Tree tree) {
        Tree ModifierList = tree.getChild(0);
        Tree Modifier = ModifierList.getChild(0);
        if (Modifier != null) {
            AccesControlQualifier = Modifier.getText();
        } else {
            AccesControlQualifier = "Package";
        }
    }

    private void setClassScope(Tree ModifierList) {
        for (int i = 0; i < ModifierList.getChildCount(); i++) {
            if (ModifierList.getChild(i).getType() == JavaParser.STATIC) {
                classScope = true;
                break;
            }
        }
    }

    public void generateLocalLoopVariable(String belongsToClass, String belongsToMethod, String declareType, String name, int lineNumber) {
        this.belongsToClass = belongsToClass;
        this.belongsToMethod = belongsToClass + "." + belongsToMethod;
        this.declareType = declareType;
        this.name = name;
        this.lineNumber = lineNumber;
        createLocalVariableObject();
    }

}