package husacct.analyse.task.analyser.csharp.convertControllers;

import husacct.analyse.task.analyser.csharp.CSharpData;
import husacct.analyse.task.analyser.csharp.CSharpTreeConvertController;
import husacct.analyse.task.analyser.csharp.generators.CSharpGenerator;

import org.antlr.runtime.tree.CommonTree;

public class CSharpClassConvertController extends CSharpGenerator {
	private CommonTree abstractTree;
	private boolean isClassName;

	private final CSharpTreeConvertController treeConverter;

	public CSharpClassConvertController(CSharpTreeConvertController treeConverter) {
		this.treeConverter = treeConverter;
	}
	
	public boolean classCheck(CommonTree tree, boolean isClassPart) {
		int type = tree.getType();
		if (type == ABSTRACT) {
			abstractTree = tree;
		}
		if (isClassName) {
			treeConverter.setTempClassName(tree.getText());
			treeConverter.createUsingGenerator();
			isClassName = false;
		}
		if (type == CLASS || type == INTERFACE || type == STRUCT) {
			isClassPart = true;
			isClassName = true;
		}
		if (isClassPart && type == FORWARDCURLYBRACKET) {
			isClassPart = false;
			treeConverter.getIndentClassLevel().add(new CSharpData(treeConverter.getTempClassName(), treeConverter.getIndentLevel(),treeConverter.getCurrentNamespaceName()));
		}
		if (isClassPart) {
			if (abstractTree != null) {
				treeConverter.getClassTrees().add(abstractTree);
				abstractTree = null;
			}
			treeConverter.getClassTrees().add(tree);
		}
		return isClassPart;
	}

}
