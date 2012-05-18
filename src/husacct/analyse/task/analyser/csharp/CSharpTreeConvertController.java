package husacct.analyse.task.analyser.csharp;

import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser.compilation_unit_return;
import husacct.analyse.task.analyser.csharp.convertControllers.CSharpAttributeConvertController;
import husacct.analyse.task.analyser.csharp.convertControllers.CSharpClassConvertController;
import husacct.analyse.task.analyser.csharp.convertControllers.CSharpNamespaceConvertController;
import husacct.analyse.task.analyser.csharp.generators.CSharpAttributeGenerator;
import husacct.analyse.task.analyser.csharp.generators.CSharpClassGenerator;
import husacct.analyse.task.analyser.csharp.generators.CSharpExceptionGenerator;
import husacct.analyse.task.analyser.csharp.generators.CSharpGenerator;
import husacct.analyse.task.analyser.csharp.generators.CSharpImportGenerator;
import husacct.analyse.task.analyser.csharp.generators.CSharpLocalVariableGenerator;
import husacct.analyse.task.analyser.csharp.generators.CSharpMethodGenerator;
import husacct.analyse.task.analyser.csharp.generators.CSharpNamespaceGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;

public class CSharpTreeConvertController extends CSharpGenerator {

	private List<List<CommonTree>> namespaceTrees;
	private List<CommonTree> classTrees;
	private List<List<CommonTree>> usageTrees;
	private List<CommonTree> methodTrees;
	private List<CommonTree> exceptionTrees;
	private List<CommonTree> localVariableTrees;
	private int indentLevel = 0;
	private int methodIndentLevel = -1;
	private String tempClassName = "";
	private String tempUsingName = "";
	private String tempMethodSignature = "";
	private List<CommonTree> tempUsingTrees;
	private List<CSharpData> indentClassLevel;
	private List<CSharpData> indentNamespaceLevel;
	private List<CSharpData> indentUsingsLevel; 
	private String currentNamespaceName;
	private CSharpNamespaceConvertController csharpNamespaceConvertController;
	private CSharpClassConvertController csharpClassConvertController;
	private CSharpAttributeConvertController csharpAttributeConvertController;

	
	private void checkData(CSharpData cSharpData, List<CSharpData> indentDataLevel) {
		if (getIndentLevel() == cSharpData.getIntentLevel() && !cSharpData.getClosed()) {
			checkEveryClassAndNamespace(cSharpData, indentDataLevel);
			cSharpData.setClosed(true);
		}
	}

	private void checkEveryClassAndNamespace(CSharpData classData, List<CSharpData> indentDataLevel) {
		for (CSharpData nestedClassData : indentDataLevel) {
			if (classData.getIntentLevel() > nestedClassData.getIntentLevel() && !nestedClassData.getClosed()) {
				String nestedClass = nestedClassData.getClassName();
				CSharpData data = indentDataLevel.get(indentDataLevel.indexOf(classData));
				data.setParentClass(nestedClass);
				data.setHasParent(true);
			}
		}
	}

	private void checkIfClosed(CommonTree tree, List<CSharpData> indentDataLevel) {
		for (CSharpData classData : indentDataLevel) {
			checkData(classData, indentDataLevel);
		}
	}

	@SuppressWarnings("unchecked")
	public void delegateDomainObjectGenerators(CSharpParser cSharpParser) throws RecognitionException {
		setIndentClassLevel(new ArrayList<CSharpData>());
		setIndentNamespaceLevel(new ArrayList<CSharpData>());
		indentUsingsLevel = new ArrayList<CSharpData>();
		compilation_unit_return compilationUnit = cSharpParser.compilation_unit();
		CommonTree compilationUnitTree = (CommonTree) compilationUnit.getTree();
		setNamespaceTrees(new ArrayList<List<CommonTree>>());
		usageTrees = new ArrayList<List<CommonTree>>();
		setClassTrees(new ArrayList<CommonTree>());
		methodTrees = new ArrayList<CommonTree>();
		exceptionTrees = new ArrayList<CommonTree>();
		localVariableTrees = new ArrayList<CommonTree>();
		walkAST(compilationUnitTree.getChildren());
		new CSharpClassGenerator(getClassTrees(), getIndentClassLevel());
		CSharpNamespaceGenerator generator = new CSharpNamespaceGenerator();
		for (List<CommonTree> trees : getNamespaceTrees()) {
			generator.namespaceGenerator(trees);
		}
	}


	private boolean methodCheck(CommonTree tree, boolean isPartOfMethod) {
		int[] ListOfTypes = new int[] { FINAL, PUBLIC, PROTECTED, PRIVATE, ABSTRACT, VOID /* synchronised */};
		int type = tree.getType();
		for (int singleType : ListOfTypes) {
			if (tree.getType() == singleType) {
				isPartOfMethod = true;
			}
		}
		if (type == FORWARDCURLYBRACKET || type == SEMICOLON) {
			isPartOfMethod = false;
			boolean isNewInstance = false;
			boolean hasBrackets = false;
			for (CommonTree methodTree : methodTrees) {
				int methodType = methodTree.getType();
				if (methodType == NEW) {
					isNewInstance = true;
				}
				if (methodType == FORWARDBRACKET) {
					hasBrackets = true;
				}
			}
			if (isNewInstance == false && hasBrackets == true) {
				CSharpMethodGenerator methodGenerator = new CSharpMethodGenerator();
				methodGenerator.generateMethod(methodTrees, getCurrentNamespaceName(), getTempClassName());

				tempMethodSignature = methodGenerator.getSignature();
				methodIndentLevel = getIndentLevel() -1;
			}
			methodTrees.clear();
		}
		if (isPartOfMethod) {
			methodTrees.add(tree);
		}
		return isPartOfMethod;
	}

	private void setIndentLevel(CommonTree tree) {
		int type = tree.getType();
		if (type == FORWARDCURLYBRACKET) {
			indentLevel++;
		}
		if (type == BACKWARDCURLYBRACKET) {
			checkIfClosed(tree, getIndentClassLevel());
			checkIfClosed(tree, getIndentNamespaceLevel());
			checkIfUsingsClosed(tree, indentUsingsLevel);
			indentLevel--;
		}
	}

	private void checkIfUsingsClosed(CommonTree tree,List<CSharpData> indentUsingsLevel) {
		for (CSharpData classData : indentUsingsLevel) {
			if (classData.getIntentLevel() == getIndentLevel()) {
				classData.setClosed(true);
			}
		}
	}

	private boolean usingCheck(CommonTree tree, boolean usage) {
		int type = tree.getType();
		if (type == USING) {
			usage = true;
			usageTrees.add(tempUsingTrees);
			tempUsingTrees = new ArrayList<CommonTree>();
			tempUsingName = "";
		}
		if (usage) {
			if (type != USING) {
				tempUsingTrees.add(tree);
				tempUsingName += tree.getText();
			}
		}
		if (usage && type == SEMICOLON) {
			if (!tempUsingName.equals("")) {
				indentUsingsLevel.add(new CSharpData(tempUsingName,getIndentLevel()));
			}
			usage = false;
		}
		return usage;
	}

	private boolean exceptionCheck(CommonTree tree, boolean isPartOfException){
		int type = tree.getType();

		if(type == CATCH || type == THROW || type == FINALLY){
			isPartOfException = true;
		}

		if(!(exceptionTrees.isEmpty()) && (type == SEMICOLON || type == FORWARDCURLYBRACKET)){
			isPartOfException = false;
			int lineNumber = tree.getLine();
			String uniqueClassName = getCurrentNamespaceName() + "." + getTempClassName();
			CSharpExceptionGenerator exceptionGenerator = new CSharpExceptionGenerator();
			exceptionGenerator.generateException(exceptionTrees, uniqueClassName, lineNumber);
			exceptionTrees.clear();
		}

		if(isPartOfException){
			exceptionTrees.add(tree);

		}
		return isPartOfException;
	}

	private void walkAST(List<CommonTree> children) {
		boolean isPartOfNamespace = false;
		boolean isPartOfClass = false;
		boolean isPartOfUsing = false;
		boolean isPartOfAttribute = false;
		boolean isPartOfMethod = false;
		boolean isPartOfException = false;
		boolean ispartOfLocalVariable = false;
		tempUsingTrees = new ArrayList<CommonTree>();
		csharpNamespaceConvertController = new CSharpNamespaceConvertController(this);
		csharpClassConvertController = new CSharpClassConvertController(this);
		csharpAttributeConvertController = new CSharpAttributeConvertController(this);
		for (CommonTree tree : children) {
			setIndentLevel(tree);
			isPartOfNamespace = csharpNamespaceConvertController.namespaceChecking(tree, isPartOfNamespace);
			isPartOfClass = csharpClassConvertController.classCheck(tree, isPartOfClass);
			isPartOfAttribute = csharpAttributeConvertController.attributeCheck(tree, isPartOfAttribute);
			isPartOfUsing = usingCheck(tree, isPartOfUsing);
			
			isPartOfMethod = methodCheck(tree, isPartOfMethod);
			isPartOfException = exceptionCheck(tree, isPartOfException);
			if((getIndentLevel() > methodIndentLevel) && methodIndentLevel != -1){
				ispartOfLocalVariable = localVariableCheck(tree, ispartOfLocalVariable);
			}
			
		}
	}

	private boolean localVariableCheck(CommonTree tree, boolean ispartOfLocalVariable) {
		int type = tree.getType();
		
		if(!(ispartOfLocalVariable)){
			for(int thisType : typeCollection){
				if(type == thisType){
					ispartOfLocalVariable = true;
				}
			}
		}
		
		if(type == SEMICOLON || type == IS){
			ispartOfLocalVariable = false;
			int lineNumber = tree.getLine();
			String belongsToClass = getCurrentNamespaceName() + "." + getTempClassName();
			if(localVariableTrees.size() > 0){
				cleanVariableList();
			}
			
			if(localVariableTrees.size() > 1){
				CSharpLocalVariableGenerator localVariableGenerator = new CSharpLocalVariableGenerator();
				localVariableGenerator.generateLocalVariable(localVariableTrees, tempMethodSignature, belongsToClass, lineNumber);
			}
			localVariableTrees.clear();
		}
		
		if(ispartOfLocalVariable){
			localVariableTrees.add(tree);
		}
		
		return ispartOfLocalVariable;
	}

	private void cleanVariableList() {
		boolean isLocalVariable = true;
		final int[] notAllowedTypes = new int[]{DOT, FORWARDCURLYBRACKET, FORWARDBRACKET};
		
		for(CommonTree node : localVariableTrees){
			int type = node.getType();
			
			for (int notAllowedtype : notAllowedTypes){
				if(type == notAllowedtype){
					isLocalVariable = false;
				}
			}
		}
		
		if(!(isLocalVariable)){
			localVariableTrees.clear();
		}
	}

	public int getIndentLevel() {
		return indentLevel;
	}

	public void setIndentLevel(int indentLevel) {
		this.indentLevel = indentLevel;
	}

	public List<CSharpData> getIndentNamespaceLevel() {
		return indentNamespaceLevel;
	}

	public void setIndentNamespaceLevel(List<CSharpData> indentNamespaceLevel) {
		this.indentNamespaceLevel = indentNamespaceLevel;
	}

	public String getCurrentNamespaceName() {
		return currentNamespaceName;
	}

	public void setCurrentNamespaceName(String currentNamespaceName) {
		this.currentNamespaceName = currentNamespaceName;
	}

	public List<List<CommonTree>> getNamespaceTrees() {
		return namespaceTrees;
	}

	public void setNamespaceTrees(List<List<CommonTree>> namespaceTrees) {
		this.namespaceTrees = namespaceTrees;
	}

	public String getTempClassName() {
		return tempClassName;
	}

	public void setTempClassName(String tempClassName) {
		this.tempClassName = tempClassName;
	}

	public void createUsingGenerator() {
		for (List<CommonTree> trees : usageTrees) {
			if (!trees.isEmpty() && !indentUsingsLevel.get(usageTrees.indexOf(trees)).getClosed()) {
				new CSharpImportGenerator(trees, getCurrentNamespaceName() + "." + tempClassName);
			}
		}
	}

	public List<CSharpData> getIndentClassLevel() {
		return indentClassLevel;
	}

	public void setIndentClassLevel(List<CSharpData> indentClassLevel) {
		this.indentClassLevel = indentClassLevel;
	}

	public List<CommonTree> getClassTrees() {
		return classTrees;
	}

	public void setClassTrees(List<CommonTree> classTrees) {
		this.classTrees = classTrees;
	}
}
