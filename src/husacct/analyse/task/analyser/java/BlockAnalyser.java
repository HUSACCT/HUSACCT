package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.Java7Parser;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class BlockAnalyser extends JavaGenerator {

    private String belongsToClass;
    private String belongsToMethod;
    VariableAnalyser javaLocalVariableGenerator;

    public void walkThroughBlockScope(CommonTree tree, String belongsToClass, String belongsToMethod) {
        this.belongsToClass = belongsToClass;
        this.belongsToMethod = belongsToMethod;
        javaLocalVariableGenerator = new VariableAnalyser(belongsToClass);
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

/*	        switch(treeType) {
	        case Java7Parser.VAR_DECLARATION:
	            if (child.getChildCount() > 0) {
	            	detectAndProcessAnonymousClass(child);
	            	javaLocalVariableGenerator.generateLocalVariableToDomain(child, this.belongsToClass, this.belongsToMethod);
		            walkThroughChildren = false;
	            }
	            break;
	        case Java7Parser.CLASS_CONSTRUCTOR_CALL: case Java7Parser.SUPER_CONSTRUCTOR_CALL:
            	detectAndProcessAnonymousClass(child);
	            delegateInvocation(child, "invocConstructor");
	            walkThroughChildren = false;
	            break;
	        case Java7Parser.RETURN: case Java7Parser.CAST_EXPR: case Java7Parser.ASSIGN: case Java7Parser.NOTEQUAL: case Java7Parser.EQUAL: case Java7Parser.GE: 
        	case Java7Parser.LE: case Java7Parser.LT: case Java7Parser.GT:
	            delegateInvocation(child, "AccessVariable");
	            walkThroughChildren = false;
	            break;
	        case Java7Parser.METHOD_CALL: 
            	detectAndProcessAnonymousClass(child);
	            delegateInvocation(child, "invocMethod");
	            walkThroughChildren = false;
	            break;
	        case Java7Parser.THROW: case Java7Parser.CATCH: case Java7Parser.THROWS:
	            delegateException(child);
	            walkThroughChildren = false;
	        	break;
	        case Java7Parser.FOR_EACH: case Java7Parser.FOR: case Java7Parser.WHILE:
	            delegateLoop(child);
	            walkThroughChildren = false;
	            break;
		    }
	        if (walkThroughChildren) {
	        	walkThroughBlockScope(child);
	        } else {
	        	int ttype = child.getType();
	        	if ((ttype != Java7Parser.FOR_EACH) && (ttype != Java7Parser.FOR) && (ttype != Java7Parser.WHILE)) {
		        	Tree descendant = child.getFirstChildWithType(Java7Parser.BLOCK_SCOPE);
			        if ((descendant != null) || (child.getType() == Java7Parser.BLOCK_SCOPE)) {
			        	walkThroughBlockScope(child);
			        }
	        	}
	        }
*/	    }
    }


    private void delegateInvocation(Tree treeNode, String type) {
        AccessAndCallAnalyser javaInvocationGenerator = new AccessAndCallAnalyser(this.belongsToClass);
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
        	if (treeType == Java7Parser.VOID) { //CLASS_TOP_LEVEL_SCOPE) {
        		walkThroughBlockScope(child);
        		break;
        	}
        	detectAndProcessAnonymousClass(child);
	    }
    }

}