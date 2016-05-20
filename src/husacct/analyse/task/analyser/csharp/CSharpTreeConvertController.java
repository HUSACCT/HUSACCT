package husacct.analyse.task.analyser.csharp;

import static husacct.analyse.task.analyser.csharp.generators.CSharpGeneratorToolkit.*;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser.compilation_unit_return;
import husacct.analyse.task.analyser.csharp.generators.*;

import java.io.File;
import java.util.Stack;
import java.util.regex.Matcher;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.apache.log4j.Logger;

public class CSharpTreeConvertController {
	CSharpAnalyser cSharpAnalyser;
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
	String noNameSpaceString = "";
	Stack<String> namespaceStack = new Stack<>();
	Stack<String> classNameStack = new Stack<>();
    private Logger logger = Logger.getLogger(CSharpTreeConvertController.class);

	public CSharpTreeConvertController(CSharpAnalyser cSharpAnalyser) { // Constructor call for each source file.
		this.cSharpAnalyser = cSharpAnalyser;
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
		final CommonTree compilationCommonTree = getCompilationTree(cSharpParser);
		this.sourceFilePath = sourceFilePath;
		this.numberOfLinesOfCode = nrOfLinesOfCode;
		recalculateLocInCaseOfMultipleClasses(compilationCommonTree);
		// Analyse the AST and create domain objects for the relevant code elements.
		delegateASTToGenerators(compilationCommonTree);
	}

	private CommonTree getCompilationTree(final CSharpParser cSharpParser) throws RecognitionException {
		final compilation_unit_return compilationUnit = cSharpParser.compilation_unit();
		return (CommonTree) compilationUnit.getTree();
	}

	private void delegateASTToGenerators(CommonTree tree) {
		/* Test and Debug
		if (sourceFilePath.contains("CallClassMethod_ClassWithoutNamespace")) {
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

	/* In case of multiple first-level classes in the file, numberOfLinesOfCode needs to be recalculated.
	 * In these (exceptional) cases, numberOfLinesOfCode is not determined exactly per class, but as average LOC per class in the file. 
	 * To test, use Limada-source code: Limaki.View\Limaki.View\Visualizers\ImageDisplay.cs. It contains two namespaces and four types.
	 */
	private void recalculateLocInCaseOfMultipleClasses(CommonTree tree) {
		int numberOfTypesInSourceFile = calculateNumberOfClassesInSourceFile(tree);
		if (numberOfTypesInSourceFile > 1) {
			numberOfLinesOfCode = numberOfLinesOfCode / numberOfTypesInSourceFile;
		}
	}
	private int calculateNumberOfClassesInSourceFile(CommonTree tree) {
		int numberOfTypesInSourceFile = 0;
		CommonTree namespaceMembersTree = CSharpGeneratorToolkit.getFirstDescendantWithType(tree, CSharpParser.NAMESPACE_MEMBER_DECLARATIONS);
		if (namespaceMembersTree != null) {
			int nrOfMembers = namespaceMembersTree.getChildCount();
			for (int i = 0; i < nrOfMembers; i++) {
				CommonTree memberTree = (CommonTree) namespaceMembersTree.getChild(i);
				int nodeType = memberTree.getType();
				switch (nodeType) {
					case CSharpParser.NAMESPACE:
						numberOfTypesInSourceFile = numberOfTypesInSourceFile + calculateNumberOfClassesInSourceFile(memberTree);
						break;
					case CSharpParser.CLASS:
					case CSharpParser.INTERFACE:
					case CSharpParser.ENUM:
					case CSharpParser.STRUCT:
						numberOfTypesInSourceFile ++;
						break;
					default:
						// Do nothing
				}
			}
		}
		return numberOfTypesInSourceFile;
	}
	
	private void saveUsing(CommonTree usingTree) {
		csUsingGenerator.addUsings(usingTree);
	}

	private void delegateUsings() {
		csUsingGenerator.generateToDomain(createPackageAndClassName(classNameStack));
	}

	private String delegateNamespace(CommonTree namespaceTree) {
		return csNamespaceGenerator.generateModel(CSharpGeneratorToolkit.getNameFromStack(namespaceStack), namespaceTree);
	}

	private String delegateClass(CommonTree classTree, boolean isInnerClass, boolean isInterface, boolean isEnumeration) {
		String analysedClass;
		if (isInnerClass) {
			analysedClass = csClassGenerator.generateToModel(sourceFilePath, 0, classTree, getNameSpaceName(), getNameFromStack(classNameStack), isInterface, isEnumeration);
			if (analysedClass == null){
				analysedClass = "";
	    		// logger.warn("Inner class not added of parent: " + getParentName(namespaceStack));
			}
		} else {
			analysedClass = csClassGenerator.generateToDomain(sourceFilePath, numberOfLinesOfCode, classTree, getNameSpaceName(), isInterface, isEnumeration);
		}
		return analysedClass;
	}

	private void delegateInheritanceDefinition(CommonTree inheritanceTree) {
		csInheritanceGenerator.generateToDomain(inheritanceTree, createPackageAndClassName(classNameStack));
	}

	private void delegateAttribute(CommonTree attributeTree) {
		if (attributeTree.toStringTree().contains("= >")) {
			csLamdaGenerator.delegateLambdaToBuffer((CommonTree) attributeTree, createPackageAndClassName(classNameStack), "");
		} else {
			csAttributeGenerator.generateAttributeToDomain(attributeTree, createPackageAndClassName(classNameStack));
		}
	}
	
	private void delegateProperty(CommonTree propertyTree) {
    	csPropertyGenerator.generateProperyToDomain(propertyTree, createPackageAndClassName(classNameStack));
	}

	private void delegateMethod(CommonTree methodTree) {
		csMethodeGenerator.generateMethodToDomain(methodTree, createPackageAndClassName(classNameStack));
	}

	private void delegateDelegate(CommonTree lamdaTree) {
		csLamdaGenerator.delegateDelegateToBuffer(lamdaTree, createPackageAndClassName(classNameStack));
	}

    /**
     * Retrieves the package and classname concatenated with dots.
     * ([A,B] and [C,D] becomes "A.B.C.D")
     * @return The package and classname concatenated with dots.
     */
    private String createPackageAndClassName(Stack<String> classStack) {
        String namespaces = getNameSpaceName();
        String classes = getNameFromStack(classStack);
        return getUniqueName(namespaces, classes);
    }

    private String getNameSpaceName() {
    	String namespace = "";
    	namespace = getNameFromStack(namespaceStack);
    	if (namespace.equals("")) {
    		if (noNameSpaceString.equals("")) {
	    		// Create a No_Namespace package, extended with the directories in the sourceFilePath - projectPath.
	    		String projectPath = cSharpAnalyser.getProjectPath();
	    		projectPath = Matcher.quoteReplacement(projectPath);
	    		char separator = '/';
				projectPath = projectPath.replace(separator, '_');
				sourceFilePath = sourceFilePath.replace('\\',separator);
	    		String sourceFilePathReplace = Matcher.quoteReplacement(sourceFilePath);
	    		sourceFilePathReplace = sourceFilePathReplace.replace(cSharpAnalyser.getFileExtension(), "");
	    		int positionLastSeparator = sourceFilePathReplace.lastIndexOf(separator);
	    		String sourceFilePathWithoutProjectPath = "";
	    		if (positionLastSeparator >= 0) {
		    		sourceFilePathReplace = sourceFilePathReplace.substring(0, positionLastSeparator);
		    		sourceFilePathReplace = sourceFilePathReplace.replace(separator, '_');
		    		if (sourceFilePathReplace.contains(projectPath)) {
		    			sourceFilePathWithoutProjectPath = sourceFilePathReplace.replaceAll(projectPath, "");
		    		}
	    		}
	    		namespace = csNamespaceGenerator.generateNo_Namespace(sourceFilePathWithoutProjectPath);
	    		noNameSpaceString = namespace;
	    		logger.info(" Class without namespace. Created namespace: " + namespace);
    		} else {
    			namespace = noNameSpaceString;
    		}
    	}
    	return namespace;
    }
}
