package husacct.analyse.task.analyser.csharp;

import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class CSharpClassGenerator extends CSharpGenerator{
	private boolean isClass = false;
	private boolean isAbstract = false;
	private int classDataIndex = 0;
	private String namespaceName;
	private List<CSharpClassData> indentClassLevel;
	private CSharpClassData cSharpClassData;

	public CSharpClassGenerator(List<CommonTree> classTrees, String namespaceName, List<CSharpClassData> indentClassLevel) {
		this.namespaceName = namespaceName;
		this.indentClassLevel = indentClassLevel;
		for (CommonTree tree : classTrees) {
			walkASTTree(tree);	
		}
	}

	private void walkASTTree(CommonTree tree) {
		if (tree.getType() == CLASS) {
			isClass = true;
		} else if (tree.getType() == ABSTRACT) {
			isAbstract = true;
		} else if (isClass) {
			cSharpClassData = new CSharpClassData();
			processToModelService(tree);
		}
	}

	private void processToModelService(CommonTree tree) {
		isClass = false;
		getRequiredParameters(tree);
		addToModelService();
		classDataIndex++;		
	}

	private void addToModelService() {
		if (cSharpClassData.isHasParent()) {
			modelService.createClass(
					cSharpClassData.getUniqueName(),
					cSharpClassData.getClassName(),
					cSharpClassData.getBelongsToPackage(),
					cSharpClassData.isAbstract(), 
					cSharpClassData.isHasParent(), 
					cSharpClassData.getParentClass());
		} else {
			modelService.createClass(
					cSharpClassData.getUniqueName(), 
					cSharpClassData.getClassName(), 
					cSharpClassData.getBelongsToPackage(), 
					cSharpClassData.isAbstract(),
					cSharpClassData.isHasParent());
		}
	}

	private void getRequiredParameters(CommonTree tree) {
		cSharpClassData.setUniqueName(namespaceName + "." + tree.getText());
		cSharpClassData.setClassName(tree.getText());
		cSharpClassData.setAbstract(isAbstract);
		cSharpClassData.setBelongsToPackage(namespaceName);
		cSharpClassData.setParentClass(namespaceName + "." + indentClassLevel.get(classDataIndex).getParentClass());
		cSharpClassData.setHasParent(indentClassLevel.get(classDataIndex).isHasParent());
	}
}