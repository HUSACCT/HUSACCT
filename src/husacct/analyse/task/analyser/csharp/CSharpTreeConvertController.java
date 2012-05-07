package husacct.analyse.task.analyser.csharp;

import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser.compilation_unit_return;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;

class CSharpTreeConvertController extends CSharpGenerator{

	private List<List<CommonTree>> namespaceTrees;
	private List<CommonTree> classTrees;
	private List<CommonTree> usageTrees;
	private CommonTree abstractTree;
	private boolean isClassName = false;
	private boolean isNamespaceName = false;
	private int indentLevel = 0;
	private String tempClassName = "";
	private String tempNamespaceName = "";
	List<CommonTree> tempNamespaceTrees;
	private List<CSharpData> indentClassLevel;
	private List<CSharpData> indentNamespaceLevel;
	private String tempFullNamespaceName;

	
	@SuppressWarnings("unchecked")
	public void delegateDomainObjectGenerators(CSharpParser cSharpParser) throws RecognitionException {
		indentClassLevel = new ArrayList<CSharpData>();
		indentNamespaceLevel = new ArrayList<CSharpData>();
		compilation_unit_return compilationUnit = cSharpParser.compilation_unit();
		CommonTree compilationUnitTree = (CommonTree) compilationUnit.getTree();
		namespaceTrees = new ArrayList<List<CommonTree>>();
		usageTrees = new ArrayList<CommonTree>();
		classTrees = new ArrayList<CommonTree>();
		walkAST(compilationUnitTree.getChildren());
		new CSharpImportGenerator(usageTrees, "classname");
		new CSharpClassGenerator(classTrees, indentClassLevel);
		CSharpNamespaceGenerator generator = new CSharpNamespaceGenerator();
		for (List<CommonTree> trees: namespaceTrees)
		generator.namespaceGenerator(trees);
	}

	private void walkAST(List<CommonTree> children) {
		boolean isPartOfNamespace = false;
		boolean isPartOfClass = false;
		boolean isPartOfUsage = false;
		tempNamespaceTrees = new ArrayList<CommonTree>();
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
			checkIfClosed(tree, indentClassLevel);
			checkIfClosed(tree, indentNamespaceLevel);
			indentLevel--;
		}
	}
	
	private void checkData(CSharpData cSharpData, List<CSharpData> indentDataLevel) {
		if (indentLevel == cSharpData.getIntentLevel() && !cSharpData.getClosed()) {
			checkEveryClass(cSharpData,indentDataLevel);
			cSharpData.setClosed(true);
		}
	}
	
	private void checkIfClosed(CommonTree tree, List<CSharpData> indentDataLevel) {
		for (CSharpData classData : indentDataLevel) {
			checkData(classData,indentDataLevel);
		}
	}

	private void checkEveryClass(CSharpData classData, List<CSharpData> indentDataLevel) {
		for (CSharpData nestedClassData : indentDataLevel) {
			if (classData.getIntentLevel() > nestedClassData.getIntentLevel() && !nestedClassData.getClosed()){
				String nestedClass = nestedClassData.getClassName();
				CSharpData data = indentDataLevel.get(indentDataLevel.indexOf(classData));
				data.setParentClass(nestedClass);
				data.setHasParent(true);
			}
		}
	}

	private boolean classCheck(CommonTree tree, boolean isClassPart) {
		if (tree.getType() == ABSTRACT) {
			abstractTree = tree;
		}
		if (isClassName) {
			tempClassName = tree.getText();
			isClassName = false;
		}
		if (tree.getType() == CLASS|| tree.getType() == INTERFACE) {
			isClassPart = true;
			isClassName = true;
		}
		if (isClassPart && tree.getType() == FORWARDCURLYBRACKET ) {
			isClassPart = false;
			indentClassLevel.add(new CSharpData(tempClassName,indentLevel, tempFullNamespaceName));
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


	private boolean namespaceChecking(CommonTree tree, boolean isNamespacePart) {
		if (isNamespaceName) {
			tempNamespaceName = tree.getText();
			isNamespaceName = false;
		}
		if (tree.getType() == NAMESPACE) {
			isNamespacePart = true;
			isNamespaceName = true;
		}
		if (isNamespacePart && tree.getType() == FORWARDCURLYBRACKET ) {
			makeFullNamespaceName();
			isNamespacePart = false;
			namespaceTrees.add(tempNamespaceTrees);
			tempNamespaceTrees = new ArrayList<CommonTree>();
			indentNamespaceLevel.add(new CSharpData(tempNamespaceName,indentLevel));
		}

		if (isNamespacePart) {
			tempNamespaceTrees.add(tree);
		}
		return isNamespacePart;
	}

	private void makeFullNamespaceName() {
		tempFullNamespaceName = "";
		for (CommonTree tree : tempNamespaceTrees) {
			if (tree.getType() != NAMESPACE) {
				tempFullNamespaceName +=  tree.getText(); 
			}
		}
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
