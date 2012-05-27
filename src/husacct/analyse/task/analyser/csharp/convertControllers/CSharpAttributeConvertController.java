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
		if (treeController.getCurrentClassIndent() != treeController.getIndentLevel()) {
			return false;
		}
		int type = tree.getType();
		boolean biggerThanOne = attributeTrees.size() > 1;
		if (isPartOfAttribute && type == SEMICOLON && biggerThanOne) {
			isPartOfAttribute = startNewAttributeGenerator();
		}
		if (Arrays.binarySearch(notPartOfAttribute, type) > -1) {
			isPartOfAttribute = clearAttributeTree();
		}
		if (isPartOfAttribute) {
			attributeTrees.add(tree);
		}
		if (Arrays.binarySearch(isAPartOfAttribute, type) > -1) {
			isPartOfAttribute = true;
		}
		return isPartOfAttribute;
	}
	
	private boolean clearAttributeTree() {
		attributeTrees.clear();
		return false;
	}
	
	private boolean startNewAttributeGenerator() {
		String uniqueClassName = treeController.getUniqueClassName();
		CSharpAttributeGenerator attributeGenerator = new CSharpAttributeGenerator(attributeTrees, uniqueClassName, treeController);
		attributeGenerator.scanTree();	
		return clearAttributeTree();
	}

}
