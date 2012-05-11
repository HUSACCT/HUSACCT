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
	private List<CommonTree> variousTrees;
	private List<CommonTree> attributeTrees;
	private CommonTree abstractTree;
	private boolean isScanning = false;
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
		attributeTrees = new ArrayList<CommonTree>();
		variousTrees = new ArrayList<CommonTree>();
		walkAST(compilationUnitTree.getChildren());
		new CSharpImportGenerator(usageTrees);
		new CSharpClassGenerator(classTrees, indentClassLevel);
		CSharpNamespaceGenerator generator = new CSharpNamespaceGenerator();
		for (List<CommonTree> trees: namespaceTrees)
			generator.namespaceGenerator(trees);
	}

	private void walkAST(List<CommonTree> children) {
		boolean isPartOfNamespace = false;
		boolean isPartOfClass = false;
		boolean isPartOfUsage = false;
		boolean isPartOfAttribute = false;
		tempNamespaceTrees = new ArrayList<CommonTree>();

		for (CommonTree tree : children) {
			setIndentLevel(tree);
			isPartOfNamespace = namespaceChecking(tree, isPartOfNamespace);
			isPartOfClass = classCheck(tree, isPartOfClass);
			isPartOfUsage = usageCheck(tree, isPartOfUsage);
			isPartOfAttribute = attributeCheck(tree, isPartOfAttribute);
			//isScanning = splitAttributeAndMethods(tree, isScanning);
		}
	}

	private boolean attributeCheck(CommonTree tree, boolean isPartOfAttribute) {
		
		if(isPartOfAttribute && tree.getType() == SEMICOLON){
			CSharpAttributeGenerator attributeGenerator = new CSharpAttributeGenerator(attributeTrees, tempClassName);
			
			attributeGenerator.scan();
			attributeTrees.clear();
			isPartOfAttribute = false;
		}
		
		if (isPartOfAttribute && (tree.getType() == FORWARDCURLYBRACKET || tree.getType() == USING || tree.getType() == NAMESPACE || tree.getType()==CLASS)) {
			isPartOfAttribute = false;
			attributeTrees.clear();
		}
		
		if(isPartOfAttribute){
			attributeTrees.add(tree);
		}
		
		if(tree.getType() == FORWARDCURLYBRACKET || tree.getType() == SEMICOLON || tree.getType() == BACKWARDCURLYBRACKET){
			isPartOfAttribute = true;
		}
		return isPartOfAttribute;
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
/*
	private boolean splitAttributeAndMethods(CommonTree tree, boolean isScanning) {
		boolean thisIsAnAttributeAndNotAUsing = false;
		//System.out.println(tree);
		if (tree.getType() != USING) {
			thisIsAnAttributeAndNotAUsing = true;
			System.out.println(tree);
		}	
		if(isScanning){
			variousTrees.add(tree);
			
		}
				
		if(tree.getType() == FORWARDCURLYBRACKET || tree.getType() == SEMICOLON){
			isScanning = false;
			MultipleChecks();
			
			variousTrees.clear();
		}
		
		return isScanning;
	}
*/
	private void MultipleChecks() {
		checkForMethod();
		//checkForAttribute();
	}
	
/*
	private void checkForAttribute() {
		boolean isNewInstance = false;
		boolean hasBrackets = false;
		boolean hasSemicolon = false;
		for(CommonTree thistree : variousTrees){
			if(thistree.getType() == NEW){
				isNewInstance = true;
			}
			if(thistree.getType() == FORWARDBRACKET){
				hasBrackets = true;
			}
			if(thistree.getType() == SEMICOLON){
				hasSemicolon = true;
			}
			if(hasBrackets == false && hasSemicolon){
				System.out.println(thistree);
				CSharpAttributeGenerator attributeGenerator = new CSharpAttributeGenerator(variousTrees, tempClassName);
				attributeGenerator.scan();
			}
		}
		
	}*/

	private void checkForMethod() {
		boolean isNewInstance = false;
		boolean hasBrackets = false;
		for(CommonTree thistree : variousTrees){
			if(thistree.getType() == NEW){
				isNewInstance = true;
			}
			if(thistree.getType() == FORWARDBRACKET){
				hasBrackets = true;
			}
		}
		
		if(isNewInstance == false && hasBrackets == true){
			CSharpMethodGenerator methodGenerator = new CSharpMethodGenerator();
			methodGenerator.generate(variousTrees, "className");
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
		if (tree.getType() == CLASS|| tree.getType() == INTERFACE || tree.getType() == STRUCT) {
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
