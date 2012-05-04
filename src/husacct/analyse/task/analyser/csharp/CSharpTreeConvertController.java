package husacct.analyse.task.analyser.csharp;

import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser.compilation_unit_return;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;

class CSharpTreeConvertController {

	private List<CommonTree> namespaceTrees;
	private List<CommonTree> classTrees;
<<<<<<< HEAD
=======
	private List<CommonTree> usageTrees;
	private int amoutofAccolades;
	private boolean isAbstractClass;
>>>>>>> d6033310b3ddafe280d5fe00efde535e54683265
	private CommonTree abstractTree;
	private final int ABSTRACT = 74;
	private final int CLASS = 155;
	private final int FORWARDCURLYBRACKET = 62;
	private List<String> classNames = new ArrayList<String>();
	private List<Integer> innerClassDepth = new ArrayList<Integer>();
	private String currentClassName;
	private boolean isName = false;
	private int depth = 0;

	public void delegateDomainObjectGenerators(CSharpParser cSharpParser) throws RecognitionException {
		compilation_unit_return compilationUnit = cSharpParser.compilation_unit();
		CommonTree compilationUnitTree = (CommonTree) compilationUnit.getTree();
		namespaceTrees = new ArrayList<CommonTree>();
		usageTrees = new ArrayList<CommonTree>();
		classTrees = new ArrayList<CommonTree>();
		boolean namespace = false;
<<<<<<< HEAD
		boolean isClassPart = false;
		depth = 0;
		innerClassDepth = new ArrayList<Integer>();
		for (Object trees : compilationUnitTree.getChildren()) {
			CommonTree tree = (CommonTree) trees;
			namespace = namespaceChecking(tree, namespace);
			isClassPart = setClassTree(tree, isClassPart);
=======
		boolean usage = false;
		boolean isClass = false;
		for (Object trees : compilationUnitTree.getChildren()) {
			CommonTree tree = (CommonTree) trees;
			namespace = namespaceChecking(tree, namespace);
			isClass = classChecking(tree, isClass);
			usage = usageCheck(tree, usage);
>>>>>>> d6033310b3ddafe280d5fe00efde535e54683265
		}
		CSharpNamespaceGenerator namespaceGenerator = new CSharpNamespaceGenerator(namespaceTrees);
		new CSharpClassGenerator(classTrees, namespaceGenerator.getName());
		CSharpImportGenerator importGenerator = new CSharpImportGenerator(usageTrees, "classname");
	}

	private boolean setClassTree(CommonTree tree, boolean isClassPart) {
		if (tree.getType() == ABSTRACT) {
			abstractTree = tree;
		}
		if (isName ) {
			currentClassName = tree.getText();
			isName = false;
		}
		if (tree.getType() == CLASS) {
			isClassPart = true;
			isName = true;
		}
		
		if (isClassPart && tree.getType() == FORWARDCURLYBRACKET ) {
			isClassPart = false;
			classNames.add(currentClassName);
			innerClassDepth.add(depth++);
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
	
	private boolean usageCheck(CommonTree tree, boolean usage){
		
		if(tree.getType() == 18){
			usage = true;
		}
		
		if(usage){
			if(tree.getType() != 18){
				usageTrees.add(tree);
			}
		}
		
		if(usage && tree.getType() == 25){
			usage = false;
		}
		
		return usage;
	}
}
