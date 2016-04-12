package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;

import java.util.Stack;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class CSharpNamespaceGenerator extends CSharpGenerator {
	private Stack<String> namespaceStack = new Stack<>();

	public String generateModel(String rootParentNamespace, Tree namespaceTree) {
		String namespaceName = "";
		Tree qualifiedIdentierTree = ((CommonTree)namespaceTree).getFirstChildWithType(CSharpParser.QUALIFIED_IDENTIFIER);
		if (qualifiedIdentierTree != null) {
			namespaceName = getQualifiedIdentifiers((CommonTree)qualifiedIdentierTree);
			if (rootParentNamespace.isEmpty()) {
				createPackageModelForEachNamespace(rootParentNamespace);
			} else {
				String uniqueName = CSharpGeneratorToolkit.getUniqueName(rootParentNamespace, namespaceName);
				modelService.createPackage(uniqueName, rootParentNamespace, namespaceName);
			}
		} 
		return namespaceName;
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
			result = result.substring(1); // Remove first "."
		}
		return result;
	}

	private void createPackageModelForEachNamespace(String rootNamespace) {
		String namespaceName;
		String uniqueName;
		String parentNamespace;

		for (int i = namespaceStack.size(); i > 0; i--) {
			namespaceName = namespaceStack.peek();
			uniqueName = CSharpGeneratorToolkit.getUniqueName(rootNamespace, CSharpGeneratorToolkit.getNameFromStack(namespaceStack));
			namespaceStack.pop();
			parentNamespace = CSharpGeneratorToolkit.getNameFromStack(namespaceStack);
			modelService.createPackage(uniqueName, parentNamespace, namespaceName);
		}
	}

	public String generateNo_Namespace(String sourcePathShort) {
        String uniqueName = "No_Namespace" + sourcePathShort;
        String parentNamespace = "";
        String namespaceName = "No_Namespace_HUSACCT_Defined" + sourcePathShort;
        modelService.createPackage(uniqueName, parentNamespace, namespaceName);
        return uniqueName;
    }
}
