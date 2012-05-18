package husacct.analyse.task.analyser.csharp.convertControllers;

import husacct.analyse.task.analyser.csharp.CSharpTreeConvertController;
import husacct.analyse.task.analyser.csharp.generators.CSharpAttributeGenerator;
import husacct.analyse.task.analyser.csharp.generators.CSharpGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class CSharpAttributeConvertController extends CSharpGenerator{
	private final CSharpTreeConvertController treeController;
	private List<CommonTree> attributeTrees;
	
	public CSharpAttributeConvertController(CSharpTreeConvertController treeController){
		this.treeController = treeController;
		attributeTrees = new ArrayList<CommonTree>();
	}
	
	public boolean attributeCheck(CommonTree tree, boolean isPartOfAttribute) {
		int type = tree.getType();
		final int[] notPartOfAttribute = new int[] { FORWARDCURLYBRACKET, USING, NAMESPACE, CLASS, RETURN, SET, GET, DOT };
		final int[] isAPartOfAttribute = new int[] { FORWARDCURLYBRACKET, SEMICOLON, BACKWARDCURLYBRACKET };
		if (isPartOfAttribute && type == SEMICOLON && attributeTrees.size() > 1) {
			startNewAttributeGenerator();
			isPartOfAttribute = false;
			attributeTrees.clear();
		}
		if (Arrays.binarySearch(notPartOfAttribute, type) > -1) {
			isPartOfAttribute = false;
			attributeTrees.clear();
		}
		if (isPartOfAttribute) {
			attributeTrees.add(tree);
		}
		if (Arrays.binarySearch(isAPartOfAttribute, type) > -1) {
			isPartOfAttribute = true;
		}
		return isPartOfAttribute;
	}
	private void startNewAttributeGenerator() {
		String className = treeController.getCurrentNamespaceName() + "." + treeController.getTempClassName();
		CSharpAttributeGenerator attributeGenerator = new CSharpAttributeGenerator(attributeTrees, className);
		attributeGenerator.scanTree();	
	}

}
