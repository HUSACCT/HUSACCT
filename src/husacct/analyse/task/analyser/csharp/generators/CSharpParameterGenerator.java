package husacct.analyse.task.analyser.csharp.generators;

import static husacct.analyse.task.analyser.csharp.generators.CSharpGeneratorToolkit.*;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import java.util.*;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class CSharpParameterGenerator extends CSharpGenerator {

	private String methodName;
	private String packageAndClassName;
	Stack<Argument> arguments = new Stack();
	int lineNumber;

	public Stack<String> generateParameterObjects(CommonTree argumentTree, String name, String classUniqueName) {
		packageAndClassName = classUniqueName;
		methodName = name;
		lineNumber = argumentTree.getLine();

		getArgumentsInformation(argumentTree);
		writeParameterToDomain();

		return getArgumentTypes(arguments);
	}

	private void getArgumentsInformation(CommonTree argumentTree) {
		String argType;
		String argName;
		List<String> genericTypes = new ArrayList<>();
		for (int i = 0; i < argumentTree.getChildCount(); i++) {
			CommonTree child = (CommonTree) argumentTree.getChild(i);
			argType = getFormalParameter(child);

			if (argType != null) {
				argName = getArgumentName(child);

				CommonTree genericListTree = getGenericListTree(child);
				genericTypes = getGenericTypes(genericListTree, genericTypes);

				Argument arg = new Argument(argName, argType, genericTypes);
				arguments.add(arg);
			}
		}
	}

	private String getFormalParameter(CommonTree childTree) {
		if (childTree.getType() == CSharpParser.FIXED_PARAMETER) {
			CommonTree typetree = (CommonTree) childTree;
			return getType(typetree.getFirstChildWithType(CSharpParser.TYPE));
		}
		return null;
	}

	private String getType(Tree tree) {
		CommonTree ctree = (CommonTree) tree;
		return getNameSpaceOrTypePart((CommonTree) ctree.getFirstChildWithType(CSharpParser.NAMESPACE_OR_TYPE_NAME));
	}

	private String getNameSpaceOrTypePart(CommonTree typeTree) {
		if (typeTree == null) {
			return null;
		}
		return typeTree.getFirstChildWithType(CSharpParser.IDENTIFIER).getText();
	}

	private String getArgumentName(CommonTree child) {
		return child.getFirstChildWithType(CSharpParser.IDENTIFIER).getText();
	}

	private CommonTree getGenericListTree(CommonTree parameterTree) {
		return walkTree(parameterTree, CSharpParser.TYPE, CSharpParser.NAMESPACE_OR_TYPE_NAME, CSharpParser.TYPE_ARGUMENT_LIST);
	}

	private List<String> getGenericTypes(CommonTree genericListTree, List<String> genericTypes) {
		if (genericListTree != null) {
			for (int i = 0; i < genericListTree.getChildCount(); i++) {

				CommonTree childTree = (CommonTree) genericListTree.getChild(i);
				CommonTree typeNameTree = (CommonTree) childTree.getFirstChildWithType(CSharpParser.NAMESPACE_OR_TYPE_NAME);

				genericTypes.add(typeNameTree.getFirstChildWithType(CSharpParser.IDENTIFIER).getText());

				if (typeNameTree.getFirstChildWithType(CSharpParser.TYPE_ARGUMENT_LIST) != null) {
					genericListTree = (CommonTree) typeNameTree.getFirstChildWithType(CSharpParser.TYPE_ARGUMENT_LIST);
					genericTypes = getGenericTypes(genericListTree, genericTypes);
				}
			}
		}
		return genericTypes;
	}

	private void writeParameterToDomain() {
		String uniqueParentName = createUniqueParentName();

		for (Argument arg : arguments) {
			String uniqueName = uniqueParentName + "." + arg.name;
			modelService.createParameter(arg.name, uniqueName, arg.type, packageAndClassName, lineNumber, uniqueParentName, arg.genericType);
		}
	}

	private String createUniqueParentName() {
		return packageAndClassName + "." + methodName + "(" + createCommaSeperatedString(getArgumentTypes(arguments)) + ")";
	}

	private Stack<String> getArgumentTypes(Stack<Argument> arguments) {
		Stack<String> result = new Stack();
		for (Argument arg : arguments) {
			result.push(arg.type);
		}
		return result;
	}

	public class Argument {

		final String name;
		final String type;
		final List genericType;

		public Argument(String name, String type, List generic) {
			this.name = name;
			this.type = type;
			this.genericType = generic;
		}
	}
}
