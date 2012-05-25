package husacct.analyse.task.analyser.csharp.convertControllers;

import java.util.ArrayList;
import java.util.List;

import husacct.analyse.task.analyser.csharp.CSharpTreeConvertController;
import husacct.analyse.task.analyser.csharp.generators.CSharpGenerator;
import husacct.analyse.task.analyser.csharp.generators.CSharpMethodGenerator;

import org.antlr.runtime.tree.CommonTree;

public class CSharpMethodConvertController extends CSharpGenerator {
	private final CSharpTreeConvertController treeConvertController;
	private List<CommonTree> methodTrees;

	public CSharpMethodConvertController(CSharpTreeConvertController treeConvertController) {
		this.treeConvertController = treeConvertController;
		methodTrees = new ArrayList<CommonTree>();
	}

	public boolean methodCheck(CommonTree tree, boolean isPartOfMethod) {
		int type = tree.getType();
		for (int singleMethodType : listOfMethodTypes) {
			if (type == singleMethodType) {
				isPartOfMethod = true;
			}
		}
		if (type == FORWARDCURLYBRACKET || type == SEMICOLON) {
			isPartOfMethod = endOfMethod();
		}
		if (isPartOfMethod) {
			methodTrees.add(tree);
		}
		return isPartOfMethod;
	}

	private boolean endOfMethod() {
		boolean isNewInstance = false;
		boolean hasBrackets = false;
		for (CommonTree tree : methodTrees) {
			int type = tree.getType();
			if (type == NEW) {
				isNewInstance = true;
			}
			if (type == FORWARDBRACKET) {
				hasBrackets = true;
			}
		}
		if (isNewInstance == false && hasBrackets == true) {
			endMethodTree();
		}
		methodTrees.clear();
		return false;
	}

	private void endMethodTree() {
		CSharpMethodGenerator methodGenerator = new CSharpMethodGenerator();
		methodGenerator.generateMethod(methodTrees, treeConvertController.getCurrentClassName(), treeConvertController.getUniqueClassName());
		treeConvertController.setCurrentMethodName(methodGenerator.getSignature());
		treeConvertController.setMethodIndentLevel(treeConvertController.getIndentLevel() -1);
		treeConvertController.cleanInstances();
	}

}
