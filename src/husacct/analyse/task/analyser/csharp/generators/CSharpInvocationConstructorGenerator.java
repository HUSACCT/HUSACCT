package husacct.analyse.task.analyser.csharp.generators;

import java.util.ArrayList;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import static husacct.analyse.task.analyser.csharp.generators.CSharpGeneratorToolkit.*;

import org.antlr.runtime.tree.CommonTree;

public class CSharpInvocationConstructorGenerator extends AbstractCSharpInvocationGenerator{
	private ArrayList<String> toNames = new ArrayList<String>();

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
		this.to = setToName(constructorTree);
		this.lineNumber = constructorTree.getChild(0).getLine();
	}
	
	private String setToName(CommonTree treeNode) {
		String toName = "";
		walkTreeToGetNames(treeNode);	
		
		for (String name : toNames) {
			toName += name + ".";
		}
		toNames.clear();
		toName = removeLastDot(toName);
		return toName;
	}
	
	private void walkTreeToGetNames(CommonTree treeNode) {
		for (int i = 0; i < treeNode.getChildCount(); i++) {
			CommonTree child = (CommonTree)treeNode.getChild(i);
			switch (child.getType()) {
			case CSharpParser.NAMESPACE_OR_TYPE_NAME:
			case CSharpParser.NAMESPACE_OR_TYPE_PART:	
				String name = child.getFirstChildWithType(CSharpParser.IDENTIFIER).getText();
				this.toNames.add(name);
				break;
			}
			walkTreeToGetNames(child);
		}
	}
	
	private String removeLastDot(String toName) {
		return toName.endsWith(".") ? toName.substring(0, toName.length() -1) : toName;
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
