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
        boolean walkThroughChildren = true;

        // Test helper
       	if (this.belongsToClass.equals("husacct.define.presentation.jdialog.AddModuleValuesJDialog")){
    		if (child.getLine() == 44) {
//    			if (child.getType() == JavaParser.METHOD_CALL) {		
    				boolean breakpoint1 = true;
    			}
//    		}
    	} 

        switch(treeType) {
        case JavaParser.VAR_DECLARATION:
            if (child.getChildCount() > 0) {
            	javaLocalVariableGenerator.generateLocalVariableToDomain(child, this.belongsToClass, this.belongsToMethod);
            }
            deleteTreeChild(child);
            break;
        case JavaParser.CLASS_CONSTRUCTOR_CALL: case JavaParser.SUPER_CONSTRUCTOR_CALL:
            delegateInvocation(child, "invocConstructor");
            //walkThroughChildren = false;
            break;
        case JavaParser.CAST_EXPR:
            delegateInvocation(child, "accessPropertyOrField");
            walkThroughChildren = false;
            break;
        case JavaParser.METHOD_CALL: 
            delegateInvocation(child, "invocMethod");
            walkThroughChildren = false;
            break;
        case JavaParser.THROW: case JavaParser.CATCH: case JavaParser.THROWS:
            delegateException(child);
            deleteTreeChild(child);
        	break;
        case JavaParser.ASSIGN:
            delegateInvocation(child, "accessPropertyOrField");
            walkThroughChildren = false;
            break;
        case JavaParser.NOT_EQUAL: case JavaParser.EQUAL: case JavaParser.GREATER_OR_EQUAL: 
        	case JavaParser.LESS_OR_EQUAL: case JavaParser.LESS_THAN: case JavaParser.GREATER_THAN:
            delegateInvocation(child, "accessPropertyOrField");
            break;
        case JavaParser.FOR_EACH: case JavaParser.FOR: case JavaParser.WHILE:
            delegateLoop(child);
            deleteTreeChild(child);
            break;
    }
        if (walkThroughChildren) {
        	walkThroughBlockScope(child);
        }
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