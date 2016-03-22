package husacct.analyse.task.analyser.csharp;

import static husacct.analyse.task.analyser.csharp.generators.CSharpGeneratorToolkit.*;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser.compilation_unit_return;
import husacct.analyse.task.analyser.csharp.generators.*;

import java.util.Stack;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class CSharpTreeConvertController {
	String sourceFilePath = "";
    int numberOfLinesOfCode = 0;
	CSharpUsingGenerator csUsingGenerator;
	CSharpNamespaceGenerator csNamespaceGenerator;
	CSharpClassGenerator csClassGenerator;
	CSharpEnumGenerator csEnumGenerator;
	CSharpInheritanceGenerator csInheritanceGenerator;
	CSharpAttributeAndLocalVariableGenerator csAttributeGenerator;
	CSharpPropertyGenerator csPropertyGenerator;
	CSharpMethodGeneratorController csMethodeGenerator;
	CSharpLamdaGenerator csLamdaGenerator;
	Stack<String> namespaceStack = new Stack<>();
	Stack<String> classNameStack = new Stack<>();
    //private Logger logger = Logger.getLogger(CSharpTreeConvertController.class);

	public CSharpTreeConvertController() {
		csUsingGenerator = new CSharpUsingGenerator();
		csNamespaceGenerator = new CSharpNamespaceGenerator();
		csClassGenerator = new CSharpClassGenerator();
		csEnumGenerator = new CSharpEnumGenerator();
		csInheritanceGenerator = new CSharpInheritanceGenerator();
		csAttributeGenerator = new CSharpAttributeAndLocalVariableGenerator();
		csPropertyGenerator = new CSharpPropertyGenerator();
		csMethodeGenerator = new CSharpMethodGeneratorController();
		csLamdaGenerator = new CSharpLamdaGenerator();
	}

	public void delegateDomainObjectGenerators(final CSharpParser cSharpParser, String sourceFilePath, int nrOfLinesOfCode) throws RecognitionException {
		this.sourceFilePath = sourceFilePath;
		this.numberOfLinesOfCode = nrOfLinesOfCode;
		final CommonTree compilationCommonTree = getCompilationTree(cSharpParser);
		delegateASTToGenerators(compilationCommonTree);
	}

	private CommonTree getCompilationTree(final CSharpParser cSharpParser) throws RecognitionException {
		final compilation_unit_return compilationUnit = cSharpParser.compilation_unit();
		return (CommonTree) compilationUnit.getTree();
	}

	private void delegateASTToGenerators(CommonTree tree) {
		/* Test and Debug
		if (sourceFilePath.contains("ThingGraphUiManager")) {
			boolean breakpoint = true;
		} */
		
		if (isTreeAvailable(tree)) {
			for (int i = 0; i < tree.getChildCount(); i++) {
				CommonTree treeNode = (CommonTree) tree.getChild(i);
				int nodeType = treeNode.getType();

				switch (nodeType) {
					case CSharpParser.USING_DIRECTIVES:
						saveUsing(treeNode);
						deleteTreeChild(treeNode);
						break;
					case CSharpParser.NAMESPACE:
						CommonTree namespaceTree = treeNode;
						namespaceStack.push(delegateNamespace(namespaceTree));
						delegateASTToGenerators(namespaceTree);
						namespaceStack.pop();
						break;
					case CSharpParser.CLASS:
					case CSharpParser.INTERFACE:
					case CSharpParser.ENUM:
					case CSharpParser.STRUCT:
						CommonTree classTree = treeNode;
						boolean isInterface = false;
						boolean isEnumeration =  false;
						if (nodeType == CSharpParser.INTERFACE) {
							isInterface = true;
						} else if (nodeType == CSharpParser.ENUM) {
							isEnumeration = true;
						}
						boolean isInner = classNameStack.size() > 0;
						classNameStack.push(delegateClass(classTree, isInner, isInterface, isEnumeration));
						if (!isInner) {
							delegateUsings();
						}
						delegateASTToGenerators(classTree);
						classNameStack.pop();
						break;
					case CSharpParser.EXTENDS_OR_IMPLEMENTS:
					case CSharpParser.IMPLEMENTS:
						delegateInheritanceDefinition(treeNode);
						deleteTreeChild(treeNode);
						break;
					case CSharpParser.VARIABLE_DECLARATOR:
						delegateAttribute(treeNode);
						deleteTreeChild(treeNode);
						break;
					case CSharpParser.PROPERTY_DECL:
                    	delegateProperty(treeNode);
                    	deleteTreeChild(treeNode);
                    	break;
					case CSharpParser.METHOD_DECL:
					case CSharpParser.CONSTRUCTOR_DECL:
						delegateMethod(treeNode);
						deleteTreeChild(treeNode);
						break;
					case CSharpParser.DELEGATE:
						delegateDelegate(treeNode);
						break;
					default:
						delegateASTToGenerators(treeNode);
				}
			}
		}
	}

	private boolean isTreeAvailable(Tree tree) {
		if (tree != null) {
			return true;
		}
		return false;
	}

	private void saveUsing(CommonTree usingTree) {
		csUsingGenerator.addUsings(usingTree);
	}

	private void delegateUsings() {
		csUsingGenerator.generateToDomain(createPackageAndClassName(namespaceStack, classNameStack));
	}

	private String delegateNamespace(CommonTree namespaceTree) {
		return csNamespaceGenerator.generateModel(CSharpGeneratorToolkit.getParentName(namespaceStack), namespaceTree);
	}

	private String delegateClass(CommonTree classTree, boolean isInnerClass, boolean isInterface, boolean isEnumeration) {
		String analysedClass;
		if (isInnerClass) {
			analysedClass = csClassGenerator.generateToModel(sourceFilePath, 0, classTree, getParentName(namespaceStack), getParentName(classNameStack), isInterface, isEnumeration);
			if (analysedClass == null){
				analysedClass = "";
	    		// logger.warn("Inner class not added of parent: " + getParentName(namespaceStack));
			}
		} else {
			analysedClass = csClassGenerator.generateToDomain(sourceFilePath, numberOfLinesOfCode, classTree, getParentName(namespaceStack), isInterface, isEnumeration);
		}
		return analysedClass;
	}

	private void delegateInheritanceDefinition(CommonTree inheritanceTree) {
		csInheritanceGenerator.generateToDomain(inheritanceTree, createPackageAndClassName(namespaceStack, classNameStack));
	}

	private void delegateAttribute(CommonTree attributeTree) {
		if (attributeTree.toStringTree().contains("= >")) {
			csLamdaGenerator.delegateLambdaToBuffer((CommonTree) attributeTree, createPackageAndClassName(namespaceStack, classNameStack), "");
		} else {
			csAttributeGenerator.generateAttributeToDomain(attributeTree, createPackageAndClassName(namespaceStack, classNameStack));
		}
	}
	
	private void delegateProperty(CommonTree propertyTree) {
    	csPropertyGenerator.generateProperyToDomain(propertyTree, createPackageAndClassName(namespaceStack, classNameStack));
	}

	private void delegateMethod(CommonTree methodTree) {
		csMethodeGenerator.generateMethodToDomain(methodTree, createPackageAndClassName(namespaceStack, classNameStack));
	}

	private void delegateDelegate(CommonTree lamdaTree) {
		csLamdaGenerator.delegateDelegateToBuffer(lamdaTree, createPackageAndClassName(namespaceStack, classNameStack));
	}
}
