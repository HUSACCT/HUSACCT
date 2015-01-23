package husacct.analyse.task.analyser.csharp.generators;

import static husacct.analyse.task.analyser.csharp.generators.CSharpGeneratorToolkit.*;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;

import java.util.*;

import org.antlr.runtime.tree.CommonTree;
import org.apache.log4j.Logger;

public class CSharpParameterGenerator extends CSharpGenerator {

	private String methodName;
	private String packageAndClassName;
	private Stack<Argument> arguments = new Stack<>();
	private int lineNumber;
    private final Logger logger = Logger.getLogger(CSharpParameterGenerator.class);

	public Stack<String> generateParameterObjects(CommonTree argumentTree, String name, String classUniqueName) {
		Stack<String> returnvalue = new Stack<String>();
		packageAndClassName = classUniqueName;
		methodName = name;
		if (argumentTree != null) {
			lineNumber = argumentTree.getLine();
			getArgumentsInformation(argumentTree);
			writeParameterToDomain();
			returnvalue = getArgumentTypes(arguments);
		}
		return returnvalue;
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
			String returnValue = getTypeNameAndParts((CommonTree) childTree.getChild(0));
			return returnValue;
			}
		return null;
	}

	private String getArgumentName(CommonTree child) {
		return child.getFirstChildWithType(CSharpParser.IDENTIFIER).getText();
	}

	private CommonTree getGenericListTree(CommonTree parameterTree) {
		return walkTree(parameterTree, CSharpParser.TYPE, CSharpParser.NAMESPACE_OR_TYPE_NAME, CSharpParser.TYPE_ARGUMENT_LIST);
	}

	private List<String> getGenericTypes(CommonTree genericListTree, List<String> genericTypes) {
    	try {		
			if (genericListTree != null) {
				for (int i = 0; i < genericListTree.getChildCount(); i++) {
	
					CommonTree childTree = (CommonTree) genericListTree.getChild(i);
					CommonTree typeNameTree = (CommonTree) childTree.getFirstChildWithType(CSharpParser.NAMESPACE_OR_TYPE_NAME);
					if (typeNameTree != null) {
						genericTypes.add(typeNameTree.getFirstChildWithType(CSharpParser.IDENTIFIER).getText());
		
						if (typeNameTree.getFirstChildWithType(CSharpParser.TYPE_ARGUMENT_LIST) != null) {
							genericListTree = (CommonTree) typeNameTree.getFirstChildWithType(CSharpParser.TYPE_ARGUMENT_LIST);
							genericTypes = getGenericTypes(genericListTree, genericTypes);
						}
					}
				}
			}
        } catch (Exception e) {
	        logger.warn("Exception: "  + e + ", in getGenericTypes()");
	        //e.printStackTrace();
        }
		return genericTypes;
	}

	private void writeParameterToDomain() {
		String uniqueParentName = createUniqueParentName();
		for (Argument arg : arguments) {
			String uniqueName = uniqueParentName + "." + arg.name;
			if ((arg.type != null) && !arg.type.equals("")) {
				if(SkippableTypes.isSkippable(arg.type)){
					modelService.createParameterOnly(arg.name, uniqueName, arg.type, packageAndClassName, lineNumber, uniqueParentName, arg.genericType);
		        } else {
					modelService.createParameter(arg.name, uniqueName, arg.type, packageAndClassName, lineNumber, uniqueParentName, arg.genericType);
				}
			}
		}
	}

	private String createUniqueParentName() {
		return packageAndClassName + "." + methodName + "(" + createCommaSeperatedString(getArgumentTypes(arguments)) + ")";
	}

	private Stack<String> getArgumentTypes(Stack<Argument> arguments) {
		Stack<String> result = new Stack<>();
		for (Argument arg : arguments) 
			result.push(arg.type);
		return result;
	}

	public class Argument {

		final String name;
		final String type;
		final List<String> genericType;

		public Argument(String name, String type, List<String> generic) {
			this.name = name;
			this.type = type;
			this.genericType = generic;
		}
	}
}
