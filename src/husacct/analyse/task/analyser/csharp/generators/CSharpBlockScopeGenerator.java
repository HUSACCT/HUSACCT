package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class CSharpBlockScopeGenerator extends CSharpGenerator {

	private String packageAndClassName;
	private String belongsToMethod;

	public void walkThroughBlockScope(CommonTree tree, String packageAndClassName, String belongsToMethod) {
		this.packageAndClassName = packageAndClassName;
		this.belongsToMethod = belongsToMethod;

		walkThroughBlockScope(tree);		
	}

	private void walkThroughBlockScope(Tree tree) {
		for (int i = 0; i < tree.getChildCount(); i++) {
			Tree child = tree.getChild(i);
            boolean walkThroughChildren = true;

			/* Test helper
	       	if (this.packageAndClassName.equals("Domain.Indirect.ViolatingFrom.CallInstanceMethodIndirect_MethodMethod")){
//	    		if (child.getLine() == 44) {
	    				boolean breakpoint1 = true;
//	    		}
	    	} */

			switch (child.getType()) {
				case CSharpParser.VARIABLE_DECLARATOR:
					delegateLocalVariable(child);
		            walkThroughChildren = false;
					break;
				case CSharpParser.LOCAL_VARIABLE_DECLARATOR:
					delegateLocalVariable(child);
		            walkThroughChildren = false;
					break;
				case CSharpParser.UNARY_EXPRESSION:
					delegateInvocation(child);
		            walkThroughChildren = false;
					break;
				case CSharpParser.THROW:
				case CSharpParser.CATCH:
					delegateException(child);
					break;
				case CSharpParser.FOREACH:
				case CSharpParser.FOR:
				case CSharpParser.WHILE:
				case CSharpParser.DO:
					delegateLoop(child);
					break;
			}
	        if (walkThroughChildren) {
	        	walkThroughBlockScope(child);
	        }
		}
	}

	private void delegateLocalVariable(Tree tree) {
		if (tree.toStringTree().contains("= >")) {
			CSharpLamdaGenerator csLamdaGenerator = new CSharpLamdaGenerator();
			csLamdaGenerator.delegateLambdaToBuffer((CommonTree)tree, packageAndClassName, belongsToMethod);
		} else {
			CSharpAttributeAndLocalVariableGenerator csharpLocalVariableGenerator = new CSharpAttributeAndLocalVariableGenerator();
			csharpLocalVariableGenerator.generateLocalVariableToDomain(tree, this.packageAndClassName, this.belongsToMethod);
		}
	}

	private void delegateInvocation(Tree tree) {
		CSharpInvocationGenerator csharpInvocationGenerator = new CSharpInvocationGenerator(this.packageAndClassName);
		csharpInvocationGenerator.generateInvocationToDomain((CommonTree) tree, this.belongsToMethod);
	}

	private void delegateException(Tree tree) {
		CSharpExceptionGenerator csharpExceptionGenerator = new CSharpExceptionGenerator();
		csharpExceptionGenerator.generateExceptionToDomain((CommonTree) tree, this.packageAndClassName);
	}

	private void delegateLoop(Tree tree) {
		CSharpLoopGenerator csharpLoopGenerator = new CSharpLoopGenerator();
		csharpLoopGenerator.generateToDomainFromLoop((CommonTree) tree, this.packageAndClassName, this.belongsToMethod);
	}
}
