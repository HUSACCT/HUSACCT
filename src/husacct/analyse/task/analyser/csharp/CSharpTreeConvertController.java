package husacct.analyse.task.analyser.csharp;

import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser.compilation_unit_return;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;

class CSharpTreeConvertController {

	boolean innerClass = false;
	private List<CommonTree> namespaceTrees;
	private List<CommonTree> classTrees;
	private int amoutofAccolades;
	private boolean isAbstractClass;
	private CommonTree abstractTree;

	public int getAmoutofAccolades() {
		return amoutofAccolades;
	}

	public void setAmoutofAccolades(int amoutofAccolades) {
		this.amoutofAccolades = amoutofAccolades;
	}

	public void delegateDomainObjectGenerators(CSharpParser cSharpParser) throws RecognitionException {
		compilation_unit_return compilationUnit = cSharpParser.compilation_unit();
		CommonTree compilationUnitTree = (CommonTree) compilationUnit.getTree();
		namespaceTrees = new ArrayList<CommonTree>();
		classTrees = new ArrayList<CommonTree>();
		boolean namespace = false;
		boolean isClass = false;
		for (Object trees : compilationUnitTree.getChildren()) {
			CommonTree tree = (CommonTree) trees;
			namespace = namespaceChecking(tree, namespace);
			isClass = classChecking(tree, isClass);
		}
		CSharpNamespaceGenerator namespaceGenerator = new CSharpNamespaceGenerator(namespaceTrees);
		
		new CSharpClassGenerator(classTrees, namespaceGenerator.getName());
	}

	private boolean classChecking(CommonTree tree, boolean isClass) {
		if (tree.getType() == 74) {
			abstractTree = tree;
		}
		
		if (tree.getType() == 155) {
			isClass = true;
		}

		if (isClass && tree.getType() == 62 ) {
			isClass = false;
			innerClass = true;
		}

		if (tree.getType() == 62) {
			amoutofAccolades++;
		}

		if (tree.getType() == 63) {
			amoutofAccolades--;
		}

		if (isClass) {
			if (abstractTree != null) {
				classTrees.add(abstractTree);
				abstractTree = null;
			}
			classTrees.add(tree);
		}
		return isClass;
	}


	private boolean namespaceChecking(CommonTree tree, boolean namespace) {
		if (tree.getType() == 61) {
			namespace = true;
		}

		if (namespace && tree.getType() == 62 ) {
			namespace = false;
		}

		if (namespace) {
			namespaceTrees.add(tree);
		}
		return namespace;
	}
}
