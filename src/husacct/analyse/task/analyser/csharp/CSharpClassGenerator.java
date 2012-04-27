package husacct.analyse.task.analyser.csharp;

import husacct.analyse.domain.ModelService;
import husacct.analyse.domain.famix.FamixModelServiceImpl;

import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class CSharpClassGenerator {


	public CSharpClassGenerator(List<CommonTree> classTrees, String string) {
		ModelService ms = new FamixModelServiceImpl();
		String uniqueName = null;
		String name = string;
		boolean isAbstract = false;
		for (CommonTree tree : classTrees) {
			if (tree.getType() == 155) {
				uniqueName = tree.getText();
				name += "." + tree.getText();
			}
			if (tree.getType() == 74) {
				isAbstract = true;
			}
		}
		System.out.println(isAbstract);
		boolean isInnerClass = false;
		String belongsToPackage = null;
		ms.createClass(uniqueName,name,belongsToPackage,isAbstract,isInnerClass);
		
	}

}
