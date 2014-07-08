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
        for (int i = 0; i < tree.getChildCount(); i++) {
            Tree child = tree.getChild(i);
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
            case JavaParser.VAR_DECLARATOR_LIST:
                setAttributeName(child);
            	break;
            case JavaParser.CLASS_CONSTRUCTOR_CALL:
                javaInvocationGenerator = new JavaInvocationGenerator(this.belongsToClass);
                javaInvocationGenerator.generateConstructorInvocToDomain((CommonTree) tree, belongsToMethod);
            	break;
            case JavaParser.AT:
                annotationGenerator = new JavaAnnotationGenerator(belongsToClass);
                annotationGenerator.generateMethod((CommonTree) child);
            	break;
            case JavaParser.EXPR:
                javaInvocationGenerator = new JavaInvocationGenerator(this.belongsToClass);
                if (child.getChild(0).getType() == JavaParser.METHOD_CALL) {
                    javaInvocationGenerator.generateMethodInvocToDomain((CommonTree) child.getChild(0), belongsToMethod);
                } else if (child.getChild(0).getType() == JavaParser.DOT) {
                    javaInvocationGenerator.generatePropertyOrFieldInvocToDomain((CommonTree) child, belongsToMethod);
                }
            	break;
            }
            walkThroughAST(child);
        }
    }

    private void createAttributeObject() {
        if (declareType.contains(".")) {
            declareType = declareType.substring(0, declareType.length() - 1); //deleting the last point
        }
        if (!SkippedTypes.isSkippable(declareType)) {
            modelService.createAttribute(classScope, AccesControlQualifier, belongsToClass, declareType, name, belongsToClass + "." + name, lineNumber, this.declareTypes);
        }
        declareType = "";
    }

    private void createLocalVariableObject() {
        if (declareType.contains(".")) {
            declareType = declareType.substring(0, declareType.length() - 1); //deleting the last point
        }
        if (!SkippedTypes.isSkippable(declareType)) {
            modelService.createLocalVariable(belongsToClass, declareType, name, this.belongsToMethod + "." + this.name, lineNumber, this.belongsToMethod, this.declareTypes);
        }
        declareType = "";
    }

    private void setAttributeName(Tree tree) {
        for (int i = 0; i < tree.getChildCount(); i++) {
            Tree child = tree.getChild(i);
            int treeType = child.getType();
            if (treeType == JavaParser.IDENT) {
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

    public ArrayList<String> generateMethodReturnType(Tree returnTypeTree, String belongsToClass) {
        this.belongsToClass = belongsToClass;

        if (returnTypeTree.getType() == JavaParser.TYPE) {
            setDeclareType(returnTypeTree);
        }

        walkThroughAST(returnTypeTree);
        
        ArrayList<String> returnDeclareTypes = new ArrayList<String>();
        returnDeclareTypes.add(this.declareType);
        for (String s : this.declareTypes) {
            returnDeclareTypes.add(s);
        }

        return returnDeclareTypes;
    }
}