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
	private int indentLevel = 0;
	private String tempClassName = "";
	private List<CSharpClassData> indentClassLevel;
	private CSharpNamespaceGenerator namespaceGenerator; 

	@SuppressWarnings("unchecked")
	public void delegateDomainObjectGenerators(CSharpParser cSharpParser) throws RecognitionException {
		indentClassLevel = new ArrayList<CSharpClassData>();
		compilation_unit_return compilationUnit = cSharpParser.compilation_unit();
		CommonTree compilationUnitTree = (CommonTree) compilationUnit.getTree();
		namespaceTrees = new ArrayList<CommonTree>();
		usageTrees = new ArrayList<CommonTree>();
		classTrees = new ArrayList<CommonTree>();

		walkAST(compilationUnitTree.getChildren());
		namespaceGenerator = new CSharpNamespaceGenerator(namespaceTrees);

		new CSharpImportGenerator(usageTrees, "classname");
		new CSharpClassGenerator(classTrees, namespaceGenerator.getName(), indentClassLevel);
	}

	private void walkAST(List<CommonTree> children) {
		boolean isPartOfNamespace = false;
		boolean isPartOfClass = false;
		boolean isPartOfUsage = false;
		for (CommonTree tree : children) {
			setIndentLevel(tree);
			isPartOfNamespace = namespaceChecking(tree, isPartOfNamespace);
			isPartOfClass = classCheck(tree, isPartOfClass);
			isPartOfUsage = usageCheck(tree, isPartOfUsage);
		}
	}

	private void setIndentLevel(CommonTree tree) {
		if (tree.getType() == FORWARDCURLYBRACKET ) {
			indentLevel++;
		}
		if (tree.getType() == BACKWARDCURLYBRACKET ) {	
			checkIfClassIsClosed(tree);
			indentLevel--;
		}
	}

	private void checkIfClassIsClosed(CommonTree tree) {
		for (CSharpClassData classData : indentClassLevel) {
			if (indentLevel == classData.getIntentLevel() && !classData.getClosed()) {
				checkEveryClass(classData);
				classData.setClosed(true);
			}
		}
	}

	private void checkEveryClass(CSharpClassData classData) {
		for (CSharpClassData nestedClassData : indentClassLevel) {
			if (classData.getIntentLevel() > nestedClassData.getIntentLevel() && !nestedClassData.getClosed()){
				String nestedClass = nestedClassData.getClassName();
				indentClassLevel.get(indentClassLevel.indexOf(classData)).setParentClass(nestedClass);
				indentClassLevel.get(indentClassLevel.indexOf(classData)).setHasParent(true);
			}
		}
	}

	private boolean classCheck(CommonTree tree, boolean isClassPart) {
		if (tree.getType() == ABSTRACT) {
			abstractTree = tree;
		}
		if (isName) {
			tempClassName = tree.getText();
			isName = false;
		}
		if (tree.getType() == CLASS) {
			isClassPart = true;
			isName = true;
		}
		if (isClassPart && tree.getType() == FORWARDCURLYBRACKET ) {
			isClassPart = false;
			indentClassLevel.add(new CSharpClassData(tempClassName,indentLevel));
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

	private boolean usageCheck(CommonTree tree, boolean usage) {
		if(tree.getType() == USING){
			usage = true;
		}
		if(usage){
			if(tree.getType() != USING){
				usageTrees.add(tree);
			}
		}
		if(usage && tree.getType() == SEMICOLON){
			usage = false;
		}
		return usage;
	}
}
