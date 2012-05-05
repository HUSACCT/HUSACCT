package husacct.analyse.task.analyser.csharp;

import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser.compilation_unit_return;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;

class CSharpTreeConvertController extends CSharpGenerator{

	private List<CommonTree> namespaceTrees;
	private List<CommonTree> classTrees;
	private List<CommonTree> usageTrees;
	private CommonTree abstractTree;
	private boolean isName = false;

	@SuppressWarnings("unchecked")
	public void delegateDomainObjectGenerators(CSharpParser cSharpParser) throws RecognitionException {
		compilation_unit_return compilationUnit = cSharpParser.compilation_unit();
		CommonTree compilationUnitTree = (CommonTree) compilationUnit.getTree();
		namespaceTrees = new ArrayList<CommonTree>();
		usageTrees = new ArrayList<CommonTree>();
		classTrees = new ArrayList<CommonTree>();
		walkAST(compilationUnitTree.getChildren());
		CSharpNamespaceGenerator namespaceGenerator = new CSharpNamespaceGenerator(namespaceTrees);
		new CSharpClassGenerator(classTrees, namespaceGenerator.getName());
		new CSharpImportGenerator(usageTrees, "classname");
	}

	private void walkAST(List<CommonTree> children) {
		boolean namespace = false;
		boolean isClassPart = false;
		boolean usage = false;
		for (CommonTree tree : children) {
			namespace = namespaceChecking(tree, namespace);
			isClassPart = setClassTree(tree, isClassPart);
			usage = usageCheck(tree, usage);
		}
	}

	private boolean setClassTree(CommonTree tree, boolean isClassPart) {
		if (tree.getType() == ABSTRACT) {
			abstractTree = tree;
		}
		if (isName ) {
			isName = false;
		}
		if (tree.getType() == CLASS) {
			isClassPart = true;
			isName = true;
		}
		
		if (isClassPart && tree.getType() == FORWARDCURLYBRACKET ) {
			isClassPart = false;
		}
		
		if (isClassPart) {
			if (abstractTree != null) {
				classTrees.add(abstractTree);
				abstractTree = null;
			}
			classTrees.add(tree);
		}
		return isClassPart;
	}


	private boolean namespaceChecking(CommonTree tree, boolean namespace) {
		if (tree.getType() == NAMESPACE) {
			namespace = true;
		}

		if (namespace && tree.getType() == FORWARDCURLYBRACKET ) {
			namespace = false;
		}

		if (namespace) {
			namespaceTrees.add(tree);
		}
		return namespace;
	}
	
	private boolean usageCheck(CommonTree tree, boolean usage){
		
		if(tree.getType() == USING){
			usage = true;
		}
		
		if(usage){
			if(tree.getType() != USING){
				usageTrees.add(tree);
			}
		}
		
		if(usage && tree.getType() == 25){
			usage = false;
		}
		
		return usage;
	}
}
