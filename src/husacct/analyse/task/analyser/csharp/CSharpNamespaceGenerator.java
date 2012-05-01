package husacct.analyse.task.analyser.csharp;

import husacct.analyse.domain.ModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;

import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class CSharpNamespaceGenerator {
	private String namespaceString = null;
	public CSharpNamespaceGenerator(List<CommonTree> namespaceTrees){
		for (CommonTree commonTree : namespaceTrees){
			namespaceString += commonTree.getText();
		}
		namespaceString = namespaceString.split("namespace")[1];
		ModelCreationService ms = new FamixCreationServiceImpl();
		String belongsToPackage = namespaceString.substring(0, namespaceString.lastIndexOf("."));
		String name = namespaceString.substring(namespaceString.lastIndexOf(".")+1,namespaceString.length());
		ms.createPackage(namespaceString, belongsToPackage, name);
	}

	public String getName() {
		return namespaceString;
	}
}
