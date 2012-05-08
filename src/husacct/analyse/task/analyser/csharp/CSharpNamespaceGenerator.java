package husacct.analyse.task.analyser.csharp;

import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class CSharpNamespaceGenerator extends CSharpGenerator {
	public String namespaceGenerator(List<CommonTree> namespaceTrees){
		String namespaceString = "";
		for (CommonTree commonTree : namespaceTrees){
			if (commonTree.getType() != NAMESPACE) {
				namespaceString += commonTree.getText();
			}
		}
		String name = namespaceTrees.get(namespaceTrees.size()-1).getText();
		String belongsToPackage = namespaceString.replace("."+name, "");
		if (belongsToPackage.equals(namespaceString)) {
			belongsToPackage = "";
		}
		modelService.createPackage(namespaceString, belongsToPackage, name);
		return namespaceString;
	}
}
