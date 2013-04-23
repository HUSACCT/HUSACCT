package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.infrastructure.antlr.TreePrinter;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import java.util.*;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class CSharpMethodGeneratorController extends CSharpGenerator {

	String belongsToClass;
	String name;
	String uniqueName;
	String accessControlQualifier;
	Stack<String> argTypes = new Stack<>();
	ArrayList<String> returnTypes;
	boolean isPureAccessor = false; //Not known
	boolean isConstructor = false;
	boolean isAbstract = false;
	boolean hasClassScope = false;
	int lineNumber;

	public void generateMethodToDomain(CommonTree methodTree, String currentUniqueClassName) {
		belongsToClass = currentUniqueClassName;
		accessControlQualifier = CSharpGeneratorToolkit.getVisibility(methodTree);
		lineNumber = methodTree.getLine();

		checkMethodType(methodTree, belongsToClass);
		walkThroughMethod(methodTree);
		createMethodObject();
	}

	private void checkMethodType(CommonTree methodTree, String belongsToClass) {
		returnTypes = new ArrayList<>();

		switch (methodTree.getType()) {
			case CSharpParser.CONSTRUCTOR_DECL:
				returnTypes.add("");
				name = getClassOfUniqueName(belongsToClass);
				isConstructor = true;
				break;
			default:
				getReturnTypes(methodTree);
				name = getMethodName(methodTree);
				break;
		}
	}

	private String getClassOfUniqueName(String uniqueName) {
		String[] parts = uniqueName.split("\\.");
		return parts[parts.length - 1];
	}

	private void getReturnTypes(CommonTree methodTree) {
		for (int i = 0; i < methodTree.getChildCount(); i++) {
			if (methodTree.getChild(i).getType() == CSharpParser.TYPE) {
				CommonTree typeTree = (CommonTree) methodTree.getChild(i);
				for (int j = 0; j < typeTree.getChildCount(); j++) {
					if (typeTree.getChild(j).getType() == CSharpParser.NAMESPACE_OR_TYPE_NAME) {
						returnTypes.add(typeTree.getChild(j).getChild(0).getText());
					}
				}
				if (returnTypes.isEmpty()) {
					returnTypes.add("");
				}
			}
		}
	}

	private String getMethodName(CommonTree methodTree) {
		CommonTree memberTree = (CommonTree) methodTree.getFirstChildWithType(CSharpParser.MEMBER_NAME);
		Tree nameTree = memberTree.getFirstChildWithType(CSharpParser.NAMESPACE_OR_TYPE_NAME);
		return nameTree.getChild(0).getText();
	}

	private void walkThroughMethod(CommonTree methodTree) {
		for (int childCount = 0; childCount < methodTree.getChildCount(); childCount++) {
			Tree child = methodTree.getChild(childCount);
			new TreePrinter(methodTree);
			
			
			switch (child.getType()) {
				case CSharpParser.ABSTRACT:
					isAbstract = true;
					break;
				case CSharpParser.SEALED:
					hasClassScope = true;
					break;
				case CSharpParser.FORMAL_PARAMETER_LIST:
					System.out.println(child.toStringTree());
					getFormalParameter((CommonTree) child);
					break;
				case CSharpParser.BLOCK:
					CommonTree blockTree = (CommonTree)child;
					stepIntoBlock(blockTree);
					System.out.println(child.toStringTree());
					break;
				default:
					walkThroughMethod((CommonTree)child);
			}
		}
	}

	private void stepIntoBlock(CommonTree blockTree) {
		String belongsToMethod = name + createArgumentString(argTypes);
		
		CSharpBlockScopeGenerator csBlockScopeGenerator = new CSharpBlockScopeGenerator();
		csBlockScopeGenerator.walkThroughBlockScope(blockTree, belongsToClass, belongsToMethod);
		
		
	}

	private void getFormalParameter(CommonTree paramtree) {
		for (int i = 0; i < paramtree.getChildCount(); i++) {
			Tree child = paramtree.getChild(i);
			if (child.getType() == CSharpParser.FIXED_PARAMETER) {
				CommonTree typetree = (CommonTree) child;
				getType(typetree.getFirstChildWithType(CSharpParser.TYPE));
			}
		}
	}

	private void getType(Tree tree) {
		CommonTree ctree = (CommonTree) tree;
		getNameSpaceOrTypePart(ctree.getFirstChildWithType(CSharpParser.NAMESPACE_OR_TYPE_NAME));

	}

	private void getNameSpaceOrTypePart(Tree firstChildWithType) {
		if (firstChildWithType == null) {
			return;
		}
		argTypes.push(firstChildWithType.getChild(0).getText());
	}

	private void createMethodObject() {
		String argumentTypes = createArgumentString(argTypes);
		argTypes.clear();
		this.uniqueName = getMethodUniqueName(belongsToClass, name, argumentTypes);
		modelService.createMethod(name, uniqueName, accessControlQualifier, argumentTypes, isPureAccessor, returnTypes, belongsToClass, isConstructor, isAbstract, hasClassScope, lineNumber);
	}

	private String createArgumentString(Stack<String> argTypes) {
		return "(" + createCommaSeperatedString(argTypes) + ")";
	}

	private String createCommaSeperatedString(Stack<String> argTypes) {
		String result = "";
		for (String parentNamePart : argTypes) {
			result += parentNamePart + ",";
		}
		return result.length() > 0 ? result.substring(0, result.length() - 1) : "";
	}

	private String getMethodUniqueName(String belongsToClass, String name, String argumentTypes) {
		return CSharpGeneratorToolkit.getUniqueName(belongsToClass, name) + argumentTypes;
	}
}
