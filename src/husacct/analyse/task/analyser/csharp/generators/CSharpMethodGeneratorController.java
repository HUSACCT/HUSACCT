package husacct.analyse.task.analyser.csharp.generators;

import static husacct.analyse.task.analyser.csharp.generators.CSharpGeneratorToolkit.*;
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
		accessControlQualifier = getVisibility(methodTree);
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
				for(int j = 0  ; j < typeTree.getChildCount(); j++) {
					if (typeTree.getChild(j).getType() == CSharpParser.NAMESPACE_OR_TYPE_NAME) {
						returnTypes.add(typeTree.getChild(j).getChild(0).getText());
					}
				}
				
				if(typeTree.getChild(0) != null && typeTree.getChild(0).getChildCount() >= 2) {
					for(int k = 1 ; k < typeTree.getChild(0).getChildCount() ; k++) {
						returnTypes.add(typeTree.getChild(0).getChild(k).getChild(0).getText());
					}
				}
				if(returnTypes.isEmpty()) {
					returnTypes.add("");
				}
			}
		}
		
		String fullPathString = "";
		for(String dir : returnTypes) {
			fullPathString += dir += ".";
		}
		
		returnTypes.clear();
		returnTypes.add(fullPathString.substring(0, fullPathString.length() - 1));
	}

	private String getMethodName(CommonTree methodTree) {
		CommonTree memberTree = (CommonTree) methodTree.getFirstChildWithType(CSharpParser.MEMBER_NAME);
		Tree nameTree = memberTree.getFirstChildWithType(CSharpParser.NAMESPACE_OR_TYPE_NAME);
		return nameTree.getChild(0).getText();
	}

	private void walkThroughMethod(CommonTree methodTree) {
		for (int childCount = 0; childCount < methodTree.getChildCount(); childCount++) {
			Tree child = methodTree.getChild(childCount);
			switch (child.getType()) {
				case CSharpParser.ABSTRACT:
					isAbstract = true;
					break;
				case CSharpParser.SEALED:
					hasClassScope = true;
					break;
				case CSharpParser.FORMAL_PARAMETER_LIST:
					CSharpParameterGenerator csParameterGenerator = new CSharpParameterGenerator();
					argTypes = csParameterGenerator.generateParameterObjects((CommonTree) child, name, belongsToClass);
					deleteTreeChild(child);
					break;
				case CSharpParser.BLOCK:
					CommonTree blockTree = (CommonTree) child;
					stepIntoBlock(blockTree);
					break;
				default:
					walkThroughMethod((CommonTree) child);
			}
		}
	}

	private void stepIntoBlock(CommonTree blockTree) {
		String belongsToMethod = name + createArgumentString(argTypes);

		CSharpBlockScopeGenerator csBlockScopeGenerator = new CSharpBlockScopeGenerator();
		csBlockScopeGenerator.walkThroughBlockScope(blockTree, belongsToClass, belongsToMethod);
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

	private String getMethodUniqueName(String belongsToClass, String name, String argumentTypes) {
		return CSharpGeneratorToolkit.getUniqueName(belongsToClass, name) + argumentTypes;
	}
}
