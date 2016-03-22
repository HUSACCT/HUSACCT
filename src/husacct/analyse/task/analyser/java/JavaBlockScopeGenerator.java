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
	    	CommonTree child = (CommonTree) tree.getChild(i);
	        int treeType = child.getType();
	        boolean walkThroughChildren = true;

	        /* Test helper
	       	if (this.belongsToClass.contains("org.eclipse.ui.internal.views.markers.MarkerFieldFilterGroup")){
	    		if (child.getLine() == 522) {
	    				boolean breakpoint1 = true;
	    		}
	    	} */ 

	        switch(treeType) {
	        case JavaParser.VAR_DECLARATION:
	            if (child.getChildCount() > 0) {
	            	detectAndProcessAnonymousClass(child);
	            	javaLocalVariableGenerator.generateLocalVariableToDomain(child, this.belongsToClass, this.belongsToMethod);
		            walkThroughChildren = false;
	            }
	            break;
	        case JavaParser.CLASS_CONSTRUCTOR_CALL: case JavaParser.SUPER_CONSTRUCTOR_CALL:
            	detectAndProcessAnonymousClass(child);
	            delegateInvocation(child, "invocConstructor");
	            walkThroughChildren = false;
	            break;
	        case JavaParser.RETURN: case JavaParser.CAST_EXPR: case JavaParser.ASSIGN: case JavaParser.NOT_EQUAL: case JavaParser.EQUAL: case JavaParser.GREATER_OR_EQUAL: 
        	case JavaParser.LESS_OR_EQUAL: case JavaParser.LESS_THAN: case JavaParser.GREATER_THAN:
	            delegateInvocation(child, "AccessVariable");
	            walkThroughChildren = false;
	            break;
	        case JavaParser.METHOD_CALL: 
            	detectAndProcessAnonymousClass(child);
	            delegateInvocation(child, "invocMethod");
	            walkThroughChildren = false;
	            break;
	        case JavaParser.THROW: case JavaParser.CATCH: case JavaParser.THROWS:
	            delegateException(child);
	            walkThroughChildren = false;
	        	break;
	        case JavaParser.FOR_EACH: case JavaParser.FOR: case JavaParser.WHILE:
	            delegateLoop(child);
	            walkThroughChildren = false;
	            break;
		    }
	        if (walkThroughChildren) {
	        	walkThroughBlockScope(child);
	        } else {
	        	int ttype = child.getType();
	        	if ((ttype != JavaParser.FOR_EACH) && (ttype != JavaParser.FOR) && (ttype != JavaParser.WHILE)) {
		        	Tree descendant = child.getFirstChildWithType(JavaParser.BLOCK_SCOPE);
			        if ((descendant != null) || (child.getType() == JavaParser.BLOCK_SCOPE)) {
			        	walkThroughBlockScope(child);
			        }
	        	}
	        }
	    }
    }


    private void delegateInvocation(Tree treeNode, String type) {
        JavaInvocationGenerator javaInvocationGenerator = new JavaInvocationGenerator(this.belongsToClass);
        if (type.equals("invocConstructor")) {
            javaInvocationGenerator.generateConstructorInvocToDomain((CommonTree) treeNode, this.belongsToMethod);
        } else if (type.equals("invocMethod")) {
            javaInvocationGenerator.generateMethodInvocToDomain((CommonTree) treeNode, this.belongsToMethod);
        } else if (type.equals("AccessVariable")) {
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
    
    private void detectAndProcessAnonymousClass(Tree tree) {
    	int treeType;
    	for (int i = 0; i < tree.getChildCount(); i++) {
	    	CommonTree child = (CommonTree) tree.getChild(i);
	        treeType = child.getType();
        	if (treeType == JavaParser.CLASS_TOP_LEVEL_SCOPE) {
        		walkThroughBlockScope(child);
        		break;
        	}
        	detectAndProcessAnonymousClass(child);
	    }
    }

}