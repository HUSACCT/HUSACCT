package husacct.analyse.task.analyser.csharp;

import husacct.analyse.domain.ModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;

import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class CSharpClassGenerator {

	public CSharpClassGenerator(List<CommonTree> classTrees, String string) {
		ModelCreationService ms = new FamixCreationServiceImpl();
		String uniqueName = null;
		String name = string;
		boolean isClass = false;
		boolean isAbstract = false;
		for (CommonTree tree : classTrees) {
			if (tree.getType() == 155) {
				isClass = true;
				name += "." + tree.getText();
			} else if (tree.getType() == 74) {
				isAbstract = true;
			} else if (tree.getType() == 40) {
				
			} else if (isClass) {
				uniqueName = string + "." + tree.getText();
				name = tree.getText();
				isClass = false;
				String belongsToPackage = string;
				boolean innerClass = false;
				ms.createClass(uniqueName,name,belongsToPackage,isAbstract,innerClass);
			}
			
		}
		
	}

}
