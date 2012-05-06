package husacct.analyse.task.analyser.csharp;

import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class CSharpNamespaceGenerator extends CSharpGenerator {
	private String namespaceString = null;
	public CSharpNamespaceGenerator(List<CommonTree> namespaceTrees){
		for (CommonTree commonTree : namespaceTrees){
			namespaceString += commonTree.getText();
		}
		namespaceString = namespaceString.split("namespace")[1];
		String belongsToPackage = namespaceString.substring(0, namespaceString.lastIndexOf("."));
		String name = namespaceString.substring(namespaceString.lastIndexOf(".")+1,namespaceString.length());
		modelService.createPackage(namespaceString, belongsToPackage, name);
	}

	public String getName() {
		return namespaceString;
	}
}
