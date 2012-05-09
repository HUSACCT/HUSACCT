package husacct.analyse.task.analyser.csharp;

import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class CSharpClassGenerator extends CSharpGenerator{
	private boolean isClass;
	private boolean isAbstract;
	private int classDataIndex = 0;
	private List<CSharpData> indentClassLevel;
	private CSharpData cSharpClassData;
	private boolean isInterface;

	public CSharpClassGenerator(List<CommonTree> classTrees, List<CSharpData> indentClassLevel) {
		this.indentClassLevel = indentClassLevel;
		for (CommonTree tree : classTrees) {
			walkASTTree(tree);	
		}
	}

	private void walkASTTree(CommonTree tree) {
		if (tree.getType() == CLASS || tree.getType() == STRUCT) {
			isClass = true;
		}else if (tree.getType() == INTERFACE) {
			isInterface = true;
			isClass = true;
		} else if (tree.getType() == ABSTRACT) {
			isAbstract = true;
		} else if (isClass) {
			cSharpClassData = new CSharpData();
			processToModelService(tree);
		}
	}

	private void processToModelService(CommonTree tree) {
		isClass = false;
		getRequiredParameters(tree);
		if (!isInterface) {
			addToModelService();
		} else {
			CSharpInterfaceGenerator interfaceGenerator = new CSharpInterfaceGenerator();
			interfaceGenerator.addToModelService(cSharpClassData);
		}
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
		CSharpData data = indentClassLevel.get(classDataIndex);
		String namespace = data.getBelongsToPackage();
		if (namespace != null) {
			cSharpClassData.setUniqueName(namespace + "." + tree.getText());
		} else {
			cSharpClassData.setUniqueName(tree.getText());
		}
		cSharpClassData.setClassName(tree.getText());
		cSharpClassData.setAbstract(isAbstract);
		if (data.getBelongsToPackage() != null) {
			cSharpClassData.setBelongsToPackage(data.getBelongsToPackage());
		} else {
			cSharpClassData.setBelongsToPackage("");
		}
		cSharpClassData.setParentClass(data.getBelongsToPackage() + "." + data.getParentClass());
		cSharpClassData.setHasParent(data.isHasParent());
	}
}