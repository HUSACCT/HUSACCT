package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.infrastructure.antlr.TreePrinter;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import static husacct.analyse.task.analyser.csharp.generators.CSharpGeneratorToolkit.*;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;


public class CSharpInvocationMethodGenerator extends AbstractCSharpInvocationGenerator {
	private CSharpInvocationConstructorGenerator csharpInvocationConstructorGenerator;
	private CommonTree methodTree;
	
	public CSharpInvocationMethodGenerator(String packageAndClassName) {
		super(packageAndClassName);
	}

	public void generateMethodInvocToDomain(CommonTree tree, String belongsToMethod) {
		this.belongsToMethod = belongsToMethod;
		lineNumber = tree.getLine();
		
		delegateMethodInvocation(tree);
	}
	
	private void delegateMethodInvocation(CommonTree tree) {
		methodTree = findMethodInvocation(tree);
		
		if (methodTree != null) {
			deleteTrainWreck(methodTree);
			determineMethodType(methodTree);
			checkForArguments(methodTree);
			saveInvocationToDomain();
		}
  	}
	
	private void deleteTrainWreck(CommonTree tree) {
		Tree wagon = tree.getChild(0);
		if (wagon.getType() == CSharpParser.METHOD_INVOCATION){
			methodTree = findMethodInvocation((CommonTree)wagon);
			deleteTrainWreck((CommonTree) methodTree);
		}		
	}

	private CommonTree findMethodInvocation(CommonTree tree) {
		return getFirstDescendantWithType(tree, CSharpParser.MEMBER_ACCESS);
	}
	
	private void checkForArguments(CommonTree methodTree) {
		CommonTree argumentsTree = getFirstDescendantWithType(methodTree, CSharpParser.ARGUMENT);
		if (argumentsTree != null) {
			delegateArguments(argumentsTree);
		}
	}

	private void determineMethodType(CommonTree tree) {
		if(methodHasConstructor(tree)) {
			delegateConstructor(tree);
		}
		else {
			createMethodInvocationDetails(tree);		
		}
	}
	
	private void delegateArguments(CommonTree argumentsTree) {
		CSharpArgumentsGenerator csharpArgumentsGenerator = new CSharpArgumentsGenerator(this.from);
		csharpArgumentsGenerator.delegateArguments(argumentsTree, this.belongsToMethod);
	}

	private boolean methodHasConstructor(CommonTree tree) {
		return tree.getChild(0).getType() == CSharpParser.OBJECT_CREATION_EXPRESSION;
	}
	
	private void delegateConstructor(CommonTree tree) {
		csharpInvocationConstructorGenerator = new CSharpInvocationConstructorGenerator(this.from);
		csharpInvocationConstructorGenerator.generateConstructorInvocToDomain(tree, this.belongsToMethod);
		createMethodInvocationDetailsWithConstructor(tree);
	}

	private void createMethodInvocationDetailsWithConstructor(CommonTree tree) {
		this.invocationName = tree.getFirstChildWithType(CSharpParser.IDENTIFIER).getText();
		this.to = csharpInvocationConstructorGenerator.to;
		this.nameOfInstance = this.to;	
	}

	private void createMethodInvocationDetails(CommonTree treeNode) {
		this.invocationName = treeNode.getFirstChildWithType(CSharpParser.IDENTIFIER).getText();
		CommonTree toChild = (CommonTree)treeNode.getFirstChildWithType(CSharpParser.SIMPLE_NAME);
		this.to = toChild.getFirstChildWithType(CSharpParser.IDENTIFIER).getText();
		this.nameOfInstance = toChild.getFirstChildWithType(CSharpParser.IDENTIFIER).getText();
	}
	
	@Override
	void saveInvocationToDomain() {
		modelService.createMethodInvocation(from, to, lineNumber, invocationName, belongsToMethod, nameOfInstance);
	}
}
