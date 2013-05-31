package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.JavaParser;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.apache.log4j.Logger;

public class JavaLoopGenerator extends JavaGenerator {

    private Logger logger = Logger.getLogger(JavaInvocationGenerator.class);
    private String belongsToClass;
    private String belongsToMethod;
    JavaAttributeAndLocalVariableGenerator javaLocalVariableGenerator = new JavaAttributeAndLocalVariableGenerator();
    JavaInvocationGenerator javaInvocationGenerator;
    JavaBlockScopeGenerator javaBlockScopeGenerator;

    public void generateToDomainFromLoop(CommonTree loopTree, String belongsToClass, String belongsToMethod) {
        this.belongsToClass = belongsToClass;
        this.belongsToMethod = belongsToMethod;
        javaInvocationGenerator = new JavaInvocationGenerator(this.belongsToClass);
        if (loopTree.getType() == JavaParser.FOR || loopTree.getType() == JavaParser.WHILE) {
            walkForAndWhileAST(loopTree);
        } else if (loopTree.getType() == JavaParser.FOR_EACH) {
            walkForEachAST(loopTree);
        } else {
            logger.warn("Onbekend type loop gevonden tijdens analyseren");
        }

    }

    private void walkForAndWhileAST(Tree tree) {
		int size = tree.getChildCount();
        for (int i = 0; i < size; i++) {
            Tree child = tree.getChild(i);
            int treeType = child.getType();
            if (treeType == JavaParser.VAR_DECLARATION) {
                javaLocalVariableGenerator.generateLocalVariableToDomain(child, this.belongsToClass, this.belongsToMethod);
                deleteTreeChild(child);
            } else if (treeType == JavaParser.METHOD_CALL) {
                javaInvocationGenerator.generateMethodInvocToDomain((CommonTree) child, belongsToMethod);
                deleteTreeChild(child);
            } else if (treeType == JavaParser.DOT) {
                CommonTree newTree = new CommonTree();
                newTree.addChild(child);
                javaInvocationGenerator.generatePropertyOrFieldInvocToDomain((CommonTree) newTree, this.belongsToMethod);
                deleteTreeChild(child);
            } else if (treeType == JavaParser.BLOCK_SCOPE) {
                delegateBlockScope(child);
            }

            walkForAndWhileAST(child);
        }
    }

    private void walkForEachAST(Tree tree) {
    	Tree child;
    	CommonTree myLoopTree;
    	CommonTree typeIdentTree;
    	int size = tree.getChildCount();
        for (int childCount = 0; childCount < size; childCount++) {
            child = tree.getChild(childCount);
            int treeType = child.getType();
            if (treeType == JavaParser.TYPE) {
                if (tree.getType() != JavaParser.CAST_EXPR) {
                    myLoopTree = (CommonTree) child;
                    typeIdentTree = (CommonTree) myLoopTree.getFirstChildWithType(JavaParser.QUALIFIED_TYPE_IDENT);
                    if (typeIdentTree != null) {
                        String type = "";
                        for (int count = 0; count < typeIdentTree.getChildCount(); count++) {
                            type += !type.equals("") ? "." : "";
                            type += typeIdentTree.getChild(count).getText();
                        }

                        int lineNumber = typeIdentTree.getFirstChildWithType(JavaParser.IDENT).getLine();
                        if (tree.getChild(childCount + 1) != null) {
                            javaLocalVariableGenerator.generateLocalLoopVariable(belongsToClass, belongsToMethod, type, tree.getChild(childCount + 1).getText(), lineNumber);
                        }
                    } else if (tree.getChild(childCount + 1).getType() == JavaParser.IDENT) {
                        int lineNumber = tree.getChild(childCount + 1).getLine();
                        javaLocalVariableGenerator.generateLocalLoopVariable(belongsToClass, belongsToMethod, child.getChild(0).getText(), tree.getChild(childCount + 1).getText(), lineNumber);
                    } else {
                        logger.warn("Problemen with finding type. Please notice analyse");
                    }
                }
            } else if (treeType == JavaParser.CAST_EXPR) {
                //a cast can ruin our algorithm. We delete the cast part since we don't need to know it anyway
                deleteTreeChild(child.getChild(0));
            } else if (treeType == JavaParser.METHOD_CALL) {
                javaInvocationGenerator.generateMethodInvocToDomain((CommonTree) child, belongsToMethod);
                deleteTreeChild(child);
            } else if (treeType == JavaParser.DOT) {
                CommonTree newTree = new CommonTree();
                newTree.addChild(child);
                javaInvocationGenerator.generatePropertyOrFieldInvocToDomain((CommonTree) newTree, this.belongsToMethod);
                deleteTreeChild(child);
            } else if (treeType == JavaParser.BLOCK_SCOPE) {
                delegateBlockScope(child);
            }
            walkForEachAST(child);
        }

    }

    private void deleteTreeChild(Tree treeNode) {
        for (int child = 0; child < treeNode.getChildCount();) {
            treeNode.deleteChild(treeNode.getChild(child).getChildIndex());
        }
    }

    private void delegateBlockScope(Tree child) {
        JavaBlockScopeGenerator javaBlockScopeGenerator = new JavaBlockScopeGenerator();
        javaBlockScopeGenerator.walkThroughBlockScope((CommonTree) child, this.belongsToClass, this.belongsToMethod);
        deleteTreeChild(child);
    }
}
