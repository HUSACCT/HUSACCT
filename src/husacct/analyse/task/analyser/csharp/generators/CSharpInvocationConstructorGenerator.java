package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import static husacct.analyse.task.analyser.csharp.generators.CSharpGeneratorToolkit.*;
import org.antlr.runtime.tree.CommonTree;

public class CSharpInvocationConstructorGenerator extends AbstractCSharpInvocationGenerator{

	public CSharpInvocationConstructorGenerator(String packageAndClassName) {
		super(packageAndClassName);
	}
	
	public void generateConstructorInvocToDomain(CommonTree tree, String belongsToMethod) {
		invocationName = "Constructor";
		this.belongsToMethod = belongsToMethod;
		
		checkForConstructorInvocation(tree);
	}
	
	private void checkForConstructorInvocation(CommonTree tree) {
		CommonTree constructorTree = tree;
		if (constructorTree.getType() != CSharpParser.OBJECT_CREATION_EXPRESSION) {
			constructorTree = findConstructorInvocation(tree);
		}
		if (constructorTree != null) {
			createConstructorInvocationDetails(constructorTree);
			checkForArguments(tree);
			saveInvocationToDomain();	
		}
	}

	private CommonTree findConstructorInvocation(CommonTree tree) {
		return getFirstDescendantWithType(tree, CSharpParser.OBJECT_CREATION_EXPRESSION);
	}
	
	private void createConstructorInvocationDetails(CommonTree constructorTree) {
		this.to = getTypeNameAndParts(constructorTree);
		this.lineNumber = constructorTree.getChild(0).getLine();
	}

	private void checkForArguments(CommonTree methodTree) {
		CommonTree argumentsTree = getFirstDescendantWithType(methodTree, CSharpParser.ARGUMENT);
		if (argumentsTree != null) {
			delegateArguments(argumentsTree);
		}
	}
	
	private void delegateArguments(CommonTree tree) {
		CSharpArgumentsGenerator csharpArgumentsGenerator = new CSharpArgumentsGenerator(this.from);
		csharpArgumentsGenerator.delegateArguments(tree, this.belongsToMethod);
	}
	
	@Override
	void saveInvocationToDomain() {
		modelService.createConstructorInvocation(from, to, lineNumber, invocationName, belongsToMethod, nameOfInstance);
	}
}
