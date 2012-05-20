package husacct.analyse.task.analyser.csharp.convertControllers;

import husacct.analyse.task.analyser.csharp.CSharpData;
import husacct.analyse.task.analyser.csharp.CSharpTreeConvertController;
import husacct.analyse.task.analyser.csharp.generators.CSharpGenerator;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class CSharpNamespaceConvertController extends CSharpGenerator {
	private String tempNamespaceName = "";
	private boolean isNamespaceName = false;
	private final CSharpTreeConvertController treeConverter;
	private List<CommonTree> tempNamespaceTrees;

	public CSharpNamespaceConvertController(CSharpTreeConvertController treeConverter) {
		this.treeConverter = treeConverter;		
		tempNamespaceTrees = new ArrayList<CommonTree>();
	}
	
	public boolean namespaceChecking(CommonTree tree, boolean isNamespacePart) {
		int type = tree.getType();
		if (isNamespaceName) {
			tempNamespaceName = tree.getText();
			isNamespaceName = false;
		}
		if (type == NAMESPACE) {
			isNamespacePart = true;
			isNamespaceName = true;
		}
		if (isNamespacePart && type == FORWARDCURLYBRACKET) {
			makeFullNamespaceName();
			isNamespacePart = false;
			treeConverter.getNamespaceTrees().add(tempNamespaceTrees);
			tempNamespaceTrees = new ArrayList<CommonTree>();
			treeConverter.getIndentNamespaceLevel().add(new CSharpData(tempNamespaceName, treeConverter.getIndentLevel()));
		}
		if (isNamespacePart) {
			tempNamespaceTrees.add(tree);
		}
		return isNamespacePart;
	}

	private void makeFullNamespaceName() {
		treeConverter.setCurrentNamespaceName("");
		for (CommonTree tree : tempNamespaceTrees) {
			if (tree.getType() != NAMESPACE) {
				treeConverter.setCurrentNamespaceName(treeConverter.getCurrentNamespaceName() + tree.getText());
			}
		}
	}
}