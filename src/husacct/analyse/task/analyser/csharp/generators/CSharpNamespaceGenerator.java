package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import java.util.Stack;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class CSharpNamespaceGenerator extends CSharpGenerator {
	private Stack<String> namespaceStack = new Stack<>();

	public String generateModel(String rootParentNamespace, Tree namespaceTree) {
		String namespaceName = getNamespaceName(namespaceTree);
		createPackageModelForEachNamespace(rootParentNamespace); 	
		return namespaceName;
	}

	private String getNamespaceName(Tree namespaceTree) {
		Tree qualifiedIdentierTree = ((CommonTree)namespaceTree).getFirstChildWithType(CSharpParser.QUALIFIED_IDENTIFIER);
		if (qualifiedIdentierTree == null)
			throw new ParserException();
		return getQualifiedIdentifiers((CommonTree)qualifiedIdentierTree);
	}

	private String getQualifiedIdentifiers(CommonTree tree) {
		String result = "";
		if (tree.getType() == CSharpParser.QUALIFIED_IDENTIFIER) {
			for (int child = 0; child < tree.getChildCount(); child++) {
				result += "." + tree.getChild(child).getText();
				namespaceStack.push(tree.getChild(child).getText());
			}
		}
		if (result.length() > 0) {
			result = result.substring(1);
		}
		return result;
	}

	private void createPackageModelForEachNamespace(String rootNamespace) {
		String namespaceName;
		String uniqueName;
		String parentNamespace;

		for (int i = namespaceStack.size(); i > 0; i--) {
			namespaceName = namespaceStack.peek();
			uniqueName = CSharpGeneratorToolkit.getUniqueName(rootNamespace, CSharpGeneratorToolkit.getParentName(namespaceStack));
			namespaceStack.pop();
			parentNamespace = CSharpGeneratorToolkit.getParentName(namespaceStack);
			modelService.createPackage(uniqueName, parentNamespace, namespaceName);
		}
	}
}
