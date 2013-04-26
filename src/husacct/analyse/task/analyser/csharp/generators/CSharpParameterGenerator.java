package husacct.analyse.task.analyser.csharp.generators;

import static husacct.analyse.task.analyser.csharp.generators.CSharpGeneratorToolkit.*;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import java.util.*;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class CSharpParameterGenerator extends CSharpGenerator {

	private String belongsToMethod;
	private String belongsToClass;
	Stack<Argument> arguments = new Stack();
	int lineNumber;

	Stack<String> generateParameterObjects(Tree child, String name, String classUniqueName) {
		belongsToClass = classUniqueName;
		belongsToMethod = name;
		lineNumber = child.getLine();

		getArgumentsInformation((CommonTree) child);
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

	private CommonTree getGenericListTree(CommonTree parameterTree) {
		try {
			CommonTree typeTree = (CommonTree) parameterTree.getFirstChildWithType(CSharpParser.TYPE);
			CommonTree typeNameTree = (CommonTree) typeTree.getFirstChildWithType(CSharpParser.NAMESPACE_OR_TYPE_NAME);
			CommonTree genericListTree = (CommonTree) typeNameTree.getFirstChildWithType(CSharpParser.TYPE_ARGUMENT_LIST);
			return genericListTree;
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<String> getGenericTypes(CommonTree inputTree, List<String> genericTypes) {
		if (inputTree != null) {
			for (int i = 0; i < inputTree.getChildCount(); i++) {

				CommonTree childTree = (CommonTree) inputTree.getChild(i);
				CommonTree typeNameTree = (CommonTree) childTree.getFirstChildWithType(CSharpParser.NAMESPACE_OR_TYPE_NAME);

				genericTypes.add(typeNameTree.getFirstChildWithType(CSharpParser.IDENTIFIER).getText());

				if (typeNameTree.getFirstChildWithType(CSharpParser.TYPE_ARGUMENT_LIST) != null) {
					CommonTree genericListTree = (CommonTree) typeNameTree.getFirstChildWithType(CSharpParser.TYPE_ARGUMENT_LIST);
					genericTypes = getGenericTypes(genericListTree, genericTypes);
				}
			}
		}
		return genericTypes;
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
		return getNameSpaceOrTypePart(ctree.getFirstChildWithType(CSharpParser.NAMESPACE_OR_TYPE_NAME));
	}

	private String getNameSpaceOrTypePart(Tree firstChildWithType) {
		if (firstChildWithType == null) {
			return null;
		}
		return firstChildWithType.getChild(0).getText();
	}

	private String getArgumentName(CommonTree child) {
		return child.getFirstChildWithType(CSharpParser.IDENTIFIER).getText();
	}

	private void writeParameterToDomain() {
		String uniqueParentName = createUniqueParentName();

		for (Argument arg : arguments) {
			String uniqueName = uniqueParentName + "." + arg.name;
			modelService.createParameter(arg.name, uniqueName, arg.type, belongsToClass, lineNumber, uniqueParentName, arg.genericType);
		}
	}

	private Stack<String> getArgumentTypes(Stack<Argument> arguments) {
		Stack<String> result = new Stack();
		for (Argument arg : arguments) {
			result.push(arg.type);
		}
		return result;
	}

	private String createUniqueParentName() {
		return belongsToClass + "." + belongsToMethod + "(" + createCommaSeperatedString(getArgumentTypes(arguments)) + ")";
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
