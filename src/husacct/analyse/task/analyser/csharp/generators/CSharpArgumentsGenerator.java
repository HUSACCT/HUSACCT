package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class CSharpArgumentsGenerator {
	private String packageAndClassName = "";
	private String belongsToMethod = "";
	
	public CSharpArgumentsGenerator(String packageAndClassName){
		this.packageAndClassName = packageAndClassName;

	}

	public void delegateArguments(CommonTree argumentsTree, String belongsToMethod) {
		this.belongsToMethod = belongsToMethod;

		delegateEachArgument(argumentsTree);
	}

	private void delegateEachArgument(Tree argumentsTree) {
		for (int i = 0; i < argumentsTree.getChildCount(); i++) {
			Tree child = argumentsTree.getChild(i);
			switch (child.getType()) {
				case CSharpParser.OBJECT_CREATION_EXPRESSION:
					delegateInvocationConstructor(child);
					break;
				case CSharpParser.METHOD_INVOCATION:
					delegateInvocationMethod(child);
					break;
				case CSharpParser.MEMBER_ACCESS:
					delegateInvocationPropertyOrField(child);
					break;
				default: 
					delegateEachArgument(child);
			}
		}
	}
	
	private void delegateInvocationConstructor(Tree tree) {
		CSharpInvocationConstructorGenerator csharpInvocationConstructorGenerator= new CSharpInvocationConstructorGenerator(this.packageAndClassName);
		csharpInvocationConstructorGenerator.generateConstructorInvocToDomain((CommonTree) tree, this.belongsToMethod);
	}

	private void delegateInvocationMethod(Tree tree) {
		CSharpInvocationMethodGenerator csharpInvocationMethodGenerator = new CSharpInvocationMethodGenerator(this.packageAndClassName);
		csharpInvocationMethodGenerator.generateMethodInvocToDomain((CommonTree) tree, this.belongsToMethod);
	}
	
	private void delegateInvocationPropertyOrField(Tree tree) {
		CSharpInvocationPropertyOrFieldGenerator csharpInvocationPropertyOrFieldGenerator = new CSharpInvocationPropertyOrFieldGenerator(this.packageAndClassName);
		csharpInvocationPropertyOrFieldGenerator.generatePropertyOrFieldInvocToDomain((CommonTree) tree, this.belongsToMethod);
	}
}
