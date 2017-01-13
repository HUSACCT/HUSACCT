package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.Java7Parser;
import husacct.common.enums.DependencySubTypes;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.apache.log4j.Logger;

public class JavaLoopGenerator extends JavaGenerator {

    private Logger logger = Logger.getLogger(AccessAndCallAnalyser.class);
    private String belongsToClass;
    private String belongsToMethod;
    private String variableTypeForLoop;
    VariableAnalyser javaLocalVariableGenerator = new VariableAnalyser(belongsToClass);
    AccessAndCallAnalyser javaInvocationGenerator;
    BlockAnalyser javaBlockScopeGenerator;

    public void generateToDomainFromLoop(CommonTree loopTree, String belongsToClass, String belongsToMethod) {
        this.belongsToClass = belongsToClass;
        this.belongsToMethod = belongsToMethod;
    	variableTypeForLoop = "";
        javaInvocationGenerator = new AccessAndCallAnalyser(this.belongsToClass);
        if (loopTree.getType() == Java7Parser.FOR || loopTree.getType() == Java7Parser.WHILE) {
            walkForAndWhileAST(loopTree);
        } else if (loopTree.getType() == Java7Parser.VOID) { //FOR_EACH) {
            walkForEachAST(loopTree);
        } else {
            logger.warn("Found unknown type looping during analysis");
        }

    }
    
    private void walkForAndWhileAST(Tree tree) {
/*		int size = tree.getChildCount();
        for (int i = 0; i < size; i++) {
            Tree child = tree.getChild(i);
            int treeType = child.getType();    
            switch(treeType) {
            case Java7Parser.VAR_DECLARATION:
                javaLocalVariableGenerator.generateLocalVariableToDomain(child, this.belongsToClass, this.belongsToMethod);
                deleteTreeChild(child);
                break;
            case Java7Parser.METHOD_CALL:
                javaInvocationGenerator.generateMethodInvocToDomain((CommonTree) child, belongsToMethod);
                deleteTreeChild(child);
                break;
            case Java7Parser.DOT:
                CommonTree newTree = new CommonTree();
                newTree.addChild(child);
                javaInvocationGenerator.generatePropertyOrFieldInvocToDomain((CommonTree) newTree, this.belongsToMethod);
                deleteTreeChild(child);
                break;
            case Java7Parser.BLOCK_SCOPE:
                delegateBlockScope(child);
            }
            walkForAndWhileAST(child);
        }
*/    }

    private void walkForEachAST(Tree tree) {
/*    	Tree child;
    	int size = tree.getChildCount();
        for (int childCount = 0; childCount < size; childCount++) {
            child = tree.getChild(childCount);
            int treeType = child.getType();
            switch(treeType) {
            case Java7Parser.VAR_DECLARATION:
                javaLocalVariableGenerator.generateLocalVariableToDomain(child, this.belongsToClass, this.belongsToMethod);
                deleteTreeChild(child);
                break;
            case Java7Parser.TYPE:
            	String foundType = javaInvocationGenerator.getCompleteToString((CommonTree) child, belongsToClass, DependencySubTypes.DECL_TYPE_PARAMETER);
                if (foundType != null) {
                    this.variableTypeForLoop = foundType;
                }
                deleteTreeChild(child);
                break;
            case Java7Parser.Identifier:
            	String foundName = javaInvocationGenerator.getCompleteToString((CommonTree) child, belongsToClass, null);
            	int lineNumber = 0;
                if (foundName != null) {
                    lineNumber = child.getLine();
                } else {
                	foundName = "";
               	}
                if (!foundName.trim().equals("") && !variableTypeForLoop.trim().equals("")) {
                	javaLocalVariableGenerator.generateLocalVariableForLoopToDomain(belongsToClass, belongsToMethod, foundName, variableTypeForLoop, lineNumber);
                	variableTypeForLoop = "";
                }
                deleteTreeChild(child);
                break;
            case Java7Parser.CAST_EXPR:
                javaInvocationGenerator = new AccessAndCallAnalyser(this.belongsToClass);
                javaInvocationGenerator.generatePropertyOrFieldInvocToDomain((CommonTree) child, belongsToMethod);
                deleteTreeChild(child);
            	break;
            case Java7Parser.METHOD_CALL:
                javaInvocationGenerator.generateMethodInvocToDomain((CommonTree) child, belongsToMethod);
                deleteTreeChild(child);
            	break;
            case Java7Parser.DOT:
                CommonTree newTree = new CommonTree();
                newTree.addChild(child);
                javaInvocationGenerator.generatePropertyOrFieldInvocToDomain((CommonTree) newTree, this.belongsToMethod);
                deleteTreeChild(child);
            	break;
            case Java7Parser.BLOCK_SCOPE:
                delegateBlockScope(child);
                break;
            }
            walkForEachAST(child);
        }
*/    }

    private void deleteTreeChild(Tree treeNode) {
        for (int child = 0; child < treeNode.getChildCount();) {
            treeNode.deleteChild(treeNode.getChild(child).getChildIndex());
        }
    }

    private void delegateBlockScope(Tree child) {
        BlockAnalyser javaBlockScopeGenerator = new BlockAnalyser();
        javaBlockScopeGenerator.walkThroughBlockScope((CommonTree) child, this.belongsToClass, this.belongsToMethod);
        deleteTreeChild(child);
    }
}
