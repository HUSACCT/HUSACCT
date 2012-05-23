package husacct.analyse.task.analyser.csharp;

import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser.compilation_unit_return;
import husacct.analyse.task.analyser.csharp.convertControllers.CSharpAttributeConvertController;
import husacct.analyse.task.analyser.csharp.convertControllers.CSharpClassConvertController;
import husacct.analyse.task.analyser.csharp.convertControllers.CSharpExceptionConvertController;
import husacct.analyse.task.analyser.csharp.convertControllers.CSharpLocalVariableConvertController;
import husacct.analyse.task.analyser.csharp.convertControllers.CSharpMethodConvertController;
import husacct.analyse.task.analyser.csharp.convertControllers.CSharpNamespaceConvertController;
import husacct.analyse.task.analyser.csharp.convertControllers.CSharpUsingConvertController;
import husacct.analyse.task.analyser.csharp.generators.CSharpClassGenerator;
import husacct.analyse.task.analyser.csharp.generators.CSharpGenerator;
import husacct.analyse.task.analyser.csharp.generators.CSharpNamespaceGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;

public class CSharpTreeConvertController extends CSharpGenerator {
	private List<List<CommonTree>> namespaceTrees;
	private List<CommonTree> classTrees;
	private int currentIndentLevel = 0;
	private int currentMethodIndentLevel = -1;
	private String currentNamespaceName;
	private String currentClassName = "";
	private int currentClassIndent = 0;
	private int currentNamespaceIndent = 0;
	private String currentMethodName = "";
	private List<CSharpData> indentClassLevel;
	private List<CSharpData> indentNamespaceLevel;
	private CSharpUsingConvertController usingConverter;

	public CSharpTreeConvertController() {
		setIndentClassLevel(new ArrayList<CSharpData>());
		setIndentNamespaceLevel(new ArrayList<CSharpData>());
		setNamespaceTrees(new ArrayList<List<CommonTree>>());
		setClassTrees(new ArrayList<CommonTree>());
	}

	public void delegateDomainObjectGenerators(final CSharpParser cSharpParser)	throws RecognitionException {
		final CommonTree compilationCommonTree = getCompilationTree(cSharpParser);
		final List<CommonTree> tree = checkedConversionToCommonTree(compilationCommonTree.getChildren());
		walkThroughAST(tree);
		createGeneratorsAfterWalkAST();
	}

	private CommonTree getCompilationTree(final CSharpParser cSharpParser) throws RecognitionException {
		final compilation_unit_return compilationUnit = cSharpParser.compilation_unit();
		return (CommonTree) compilationUnit.getTree();
	}

	public List<CommonTree> checkedConversionToCommonTree(final Collection<?> collection) {
		final List<CommonTree> commonTreeList = new ArrayList<CommonTree>(collection.size());
		for (final Object object : collection) {
			commonTreeList.add(CommonTree.class.cast(object));
		}
		return commonTreeList;
	}

	private void walkThroughAST(final List<CommonTree> children) {
		boolean isPartOfNamespace = false;
		boolean isPartOfClass = false;
		boolean isPartOfUsing = false;
		boolean isPartOfAttribute = false;
		boolean isPartOfMethod = false;
		boolean isPartOfException = false;
		boolean isPartOfLocalVariable = false;
		final CSharpNamespaceConvertController namespaceConverter = new CSharpNamespaceConvertController(this);
		final CSharpClassConvertController classConverter = new CSharpClassConvertController(this);
		final CSharpAttributeConvertController attributeConverter = new CSharpAttributeConvertController(this);
		final CSharpLocalVariableConvertController localVariableConverter = new CSharpLocalVariableConvertController(this);
		final CSharpMethodConvertController methodConverter = new CSharpMethodConvertController(this);
		usingConverter = new CSharpUsingConvertController(this);
		final CSharpExceptionConvertController exceptionConverter = new CSharpExceptionConvertController(this);
		for (final CommonTree tree : children) {
			setIndentLevel(tree);
			isPartOfNamespace = namespaceConverter.namespaceChecking(tree, isPartOfNamespace);
			isPartOfClass = classConverter.classCheck(tree,	isPartOfClass);
			isPartOfAttribute = attributeConverter.attributeCheck(tree,	isPartOfAttribute);
			isPartOfUsing = usingConverter.usingCheck(tree,	isPartOfUsing);
			isPartOfMethod = methodConverter.methodCheck(tree, isPartOfMethod);
			isPartOfException = exceptionConverter.exceptionCheck(tree, isPartOfException);
			if ((getIndentLevel() > currentMethodIndentLevel) && (currentMethodIndentLevel != -1)) {
				isPartOfLocalVariable = localVariableConverter.localVariableCheck(tree, isPartOfLocalVariable);
			}
		}
	}

	private void createGeneratorsAfterWalkAST() {
		new CSharpClassGenerator(getClassTrees(), getIndentClassLevel());
		final CSharpNamespaceGenerator generator = new CSharpNamespaceGenerator();
		for (final List<CommonTree> trees : getNamespaceTrees()) {
			generator.namespaceGenerator(trees);
		}
	}

	private void setIndentLevel(final CommonTree tree) {
		final int type = tree.getType();
		if (type == FORWARDCURLYBRACKET) {
			currentIndentLevel++;
		}
		if (type == BACKWARDCURLYBRACKET) {
			checkIfClosed(tree, getIndentClassLevel());
			checkIfClosed(tree, getIndentNamespaceLevel());
			usingConverter.checkIfUsingsClosed(tree);
			currentIndentLevel--;
		}
	}

	private void checkData(final CSharpData cSharpData, final List<CSharpData> indentDataLevel) {
		if ((getIndentLevel() == cSharpData.getIntentLevel()) && !cSharpData.getClosed()) {
			checkEveryClassAndNamespace(cSharpData, indentDataLevel);
			cSharpData.setClosed(true);
		}
	}

	private void checkEveryClassAndNamespace(final CSharpData classData, final List<CSharpData> indentDataLevel) {
		for (final CSharpData nestedClassData : indentDataLevel) {
			if ((classData.getIntentLevel() > nestedClassData.getIntentLevel()) && !nestedClassData.getClosed()) {
				final String nestedClass = nestedClassData.getClassName();
				final CSharpData data = indentDataLevel.get(indentDataLevel.indexOf(classData));
				data.setParentClass(nestedClass);
				data.setHasParent(true);
			}
		}
	}

	private void checkIfClosed(final CommonTree tree, final List<CSharpData> indentDataLevel) {
		for (final CSharpData classData : indentDataLevel) {
			checkData(classData, indentDataLevel);
		}
	}

	public String getUniqueClassName() {
		if (getCurrentNamespaceName() != null) {
			return getCurrentNamespaceName() + "." + getCurrentClassName();
		} else {
			return getCurrentClassName();
		}
	}

	public void createUsingGenerator() {
		usingConverter.createGenerators();
	}

	public int getIndentLevel() {
		return currentIndentLevel;
	}

	public void setIndentLevel(final int indentLevel) {
		this.currentIndentLevel = indentLevel;
	}

	public List<CSharpData> getIndentNamespaceLevel() {
		return indentNamespaceLevel;
	}

	public void setIndentNamespaceLevel(final List<CSharpData> indentNamespaceLevel) {
		this.indentNamespaceLevel = indentNamespaceLevel;
	}

	public String getCurrentNamespaceName() {
		return currentNamespaceName;
	}

	public void setCurrentNamespaceName(final String currentNamespaceName) {
		this.currentNamespaceName = currentNamespaceName;
	}

	public List<List<CommonTree>> getNamespaceTrees() {
		return namespaceTrees;
	}

	public void setNamespaceTrees(final List<List<CommonTree>> namespaceTrees) {
		this.namespaceTrees = namespaceTrees;
	}

	public String getCurrentClassName() {
		return currentClassName;
	}

	public void setCurrentClassName(final String currentClassName) {
		this.currentClassName = currentClassName;
	}

	public List<CSharpData> getIndentClassLevel() {
		return indentClassLevel;
	}

	public void setIndentClassLevel(final List<CSharpData> indentClassLevel) {
		this.indentClassLevel = indentClassLevel;
	}

	public List<CommonTree> getClassTrees() {
		return classTrees;
	}

	public void setClassTrees(final List<CommonTree> classTrees) {
		this.classTrees = classTrees;
	}

	public String getCurrentMethodName() {
		return currentMethodName;
	}

	public void setCurrentMethodName(final String currentMethodName) {
		this.currentMethodName = currentMethodName;
	}

	public int getMethodIndentLevel() {
		return currentMethodIndentLevel;
	}

	public void setMethodIndentLevel(final int methodIndentLevel) {
		this.currentMethodIndentLevel = methodIndentLevel;
	}

	public int getCurrentClassIndent() {
		return currentClassIndent;
	}

	public void setCurrentClassIndent(int currentClassIndent) {
		this.currentClassIndent = currentClassIndent;
	}

	public int getCurrentNamespaceIndent() {
		return currentNamespaceIndent;
	}

	public void setCurrentNamespaceIndent(int currentNamespaceIndent) {
		this.currentNamespaceIndent = currentNamespaceIndent;
	}
}