package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.infrastructure.antlr.TreePrinter;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;

import org.antlr.runtime.tree.CommonTree;

public class CSharpInvocationMethodGenerator extends AbstractCSharpInvocationGenerator {
	private CSharpInvocationConstructorGenerator csharpInvocationConstructorGenerator;
	
	public CSharpInvocationMethodGenerator(String packageAndClassName) {
		super(packageAndClassName);
	}

	public void generateMethodInvocToDomain(CommonTree treeNode, String belongsToMethod) {
		this.belongsToMethod = belongsToMethod;
		lineNumber = treeNode.getLine();
		
		findMethodInvocation(treeNode);
		saveInvocationToDomain();
	}

	private void findMethodInvocation(CommonTree treeNode) {
		if (treeNode.getChildCount() <= 0){
			return;
		}
		for (int i = 0; i < treeNode.getChildCount(); i++) {
			CommonTree child = (CommonTree)treeNode.getChild(i);
			if (hasMethod(child)){
				determineMethodType(child);
			}
			else if (hasArguments(child)){
				delegateArguments(child);
			}
		}
	}

	private boolean hasArguments(CommonTree child) {
		return child.getType() == CSharpParser.ARGUMENT;
	}

	private boolean hasMethod(CommonTree child) {
		return child.getType() == CSharpParser.MEMBER_ACCESS;
	}

	private void determineMethodType(CommonTree tree) {
		if(methodHasConstructor(tree)) {
			delegateConstructor(tree);
		}
		else {
			createMethodInvocationDetails(tree);		
		}
	}
	
	private void delegateArguments(CommonTree tree) {
		CSharpArgumentsGenerator csharpArgumentsGenerator = new CSharpArgumentsGenerator(this.from);
		csharpArgumentsGenerator.delegateArguments(tree, this.belongsToMethod);
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
