package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.JavaParser;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class JavaBlockScopeGenerator extends JavaGenerator {

    private String belongsToClass;
    private String belongsToMethod;
    JavaAttributeAndLocalVariableGenerator javaLocalVariableGenerator;

    public void walkThroughBlockScope(CommonTree tree, String belongsToClass, String belongsToMethod) {
        this.belongsToClass = belongsToClass;
        this.belongsToMethod = belongsToMethod;
        javaLocalVariableGenerator = new JavaAttributeAndLocalVariableGenerator();

        walkThroughBlockScope(tree);
    }
    
    
    private void walkThroughBlockScope(Tree tree) {
    for (int i = 0; i < tree.getChildCount(); i++) {
        Tree child = tree.getChild(i);
        int treeType = child.getType();
        switch(treeType) {
        case JavaParser.VAR_DECLARATION:
            javaLocalVariableGenerator.generateLocalVariableToDomain(child, this.belongsToClass, this.belongsToMethod);
            deleteTreeChild(child);
            break;
        case JavaParser.CLASS_CONSTRUCTOR_CALL:
            delegateInvocation(child, "invocConstructor");
            break;
        case JavaParser.METHOD_CALL: 
            if (child.getChild(0).getType() == JavaParser.DOT) {
                delegateInvocation(child, "invocMethod");
            }
            break;
        case JavaParser.THROW: case JavaParser.CATCH: case JavaParser.THROWS:
            delegateException(child);
            deleteTreeChild(child);
        	break;
        case JavaParser.ASSIGN:
            if (child.getChild(0).getType() == JavaParser.DOT) {
                delegateInvocation(child, "accessPropertyOrField");
            }
            break;
        case JavaParser.FOR_EACH: case JavaParser.FOR: case JavaParser.WHILE:
            delegateLoop(child);
            deleteTreeChild(child);
            break;
        
    }
        walkThroughBlockScope(child);
    }
    }


    private void delegateInvocation(Tree treeNode, String type) {
        JavaInvocationGenerator javaInvocationGenerator = new JavaInvocationGenerator(this.belongsToClass);
        if (type.equals("invocConstructor")) {
            javaInvocationGenerator.generateConstructorInvocToDomain((CommonTree) treeNode, this.belongsToMethod);
        } else if (type.equals("invocMethod")) {
            javaInvocationGenerator.generateMethodInvocToDomain((CommonTree) treeNode, this.belongsToMethod);
        } else if (type.equals("accessPropertyOrField")) {
            javaInvocationGenerator.generatePropertyOrFieldInvocToDomain((CommonTree) treeNode, this.belongsToMethod);
        }
    }

    private void delegateException(Tree exceptionTree) {
        JavaExceptionGenerator exceptionGenerator = new JavaExceptionGenerator();
        exceptionGenerator.generateToDomain((CommonTree) exceptionTree, this.belongsToClass);
    }

    private void delegateLoop(Tree tree) {
        JavaLoopGenerator forEachLoopGenerator = new JavaLoopGenerator();
        forEachLoopGenerator.generateToDomainFromLoop((CommonTree) tree, this.belongsToClass, this.belongsToMethod);
    }

    private void deleteTreeChild(Tree treeNode) {
        for (int child = 0; child < treeNode.getChildCount();) {
            treeNode.deleteChild(treeNode.getChild(child).getChildIndex());
        }
    }
}