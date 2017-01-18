package husacct.analyse.task.analyser.csharp.generators;

import static husacct.analyse.task.analyser.csharp.generators.CSharpGeneratorToolkit.*;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import husacct.common.enums.DependencySubTypes;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class CSharpMethodGeneratorController extends CSharpGenerator {

	String belongsToClass;
	String name;
	String uniqueName;
	String accessControlQualifier;
	String paramTypesInSignature = "";
	String returnType = "";
	boolean isConstructor = false;
	boolean isAbstract = false;
	boolean hasClassScope = false;
	int lineNumber;

	public void generateMethodToDomain(CommonTree methodTree, String currentUniqueClassName) {
		/* Test helper
       	if (currentUniqueClassName.equals("Domain.Direct.Violating.CallConstructorSuper")){
//    		if (child.getLine() == 8) {
    				boolean breakpoint1 = true;
//    		}
    	} */
		belongsToClass = currentUniqueClassName;
		accessControlQualifier = getVisibility(methodTree);
		lineNumber = methodTree.getLine();

		checkMethodType(methodTree, belongsToClass);
		walkThroughMethod(methodTree);
		createMethodObject();
	}

	private void checkMethodType(CommonTree methodTree, String belongsToClass) {

		switch (methodTree.getType()) {
			case CSharpParser.CONSTRUCTOR_DECL:
				name = getClassOfUniqueName(belongsToClass);
				isConstructor = true;
				break;
			default:
				getReturnType(methodTree);
				name = getMethodName(methodTree);
				break;
		}
	}

	private String getClassOfUniqueName(String uniqueName) {
		String[] parts = uniqueName.split("\\.");
		return parts[parts.length - 1];
	}

	private void getReturnType(CommonTree methodTree) {
		for (int i = 0; i < methodTree.getChildCount(); i++) {
			if (methodTree.getChild(i).getType() == CSharpParser.TYPE) {
				CommonTree typeTree = (CommonTree) methodTree.getChild(i);
				CSharpInvocationGenerator csharpInvocationGenerator = new CSharpInvocationGenerator(this.belongsToClass);
		    	String foundType = csharpInvocationGenerator.getCompleteToString(typeTree, belongsToClass, DependencySubTypes.DECL_RETURN_TYPE);
		        if ((foundType != null) && !foundType.equals("")) {
		            this.returnType = foundType;
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
			switch (child.getType()) {
				case CSharpParser.ABSTRACT:
					isAbstract = true;
					break;
				case CSharpParser.STATIC:
					hasClassScope = true;
					break;
				case CSharpParser.FORMAL_PARAMETER_LIST:
					CSharpParameterGenerator csParameterGenerator = new CSharpParameterGenerator();
					paramTypesInSignature = csParameterGenerator.generateParameterObjects((CommonTree) child, name, belongsToClass);
					deleteTreeChild(child);
					break;
				case CSharpParser.BASE:
					CSharpInvocationGenerator csharpInvocationGenerator = new CSharpInvocationGenerator(this.belongsToClass);
					csharpInvocationGenerator.generateConstructorInvocToDomain((CommonTree) child, this.name);
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
		String belongsToMethod = name + createArgumentString();

		CSharpBlockScopeGenerator csBlockScopeGenerator = new CSharpBlockScopeGenerator();
		csBlockScopeGenerator.walkThroughBlockScope(blockTree, belongsToClass, belongsToMethod);
	}

	private void createMethodObject() {
		String argumentTypes = createArgumentString();
		this.uniqueName = getMethodUniqueName(belongsToClass, name, argumentTypes);
		if(SkippableTypes.isSkippable(returnType)){
			modelService.createMethodOnly(name, uniqueName, accessControlQualifier, argumentTypes, returnType, belongsToClass, isConstructor, isAbstract, hasClassScope, lineNumber);
        } else {
    		modelService.createMethod(name, uniqueName, accessControlQualifier, argumentTypes, returnType, belongsToClass, isConstructor, isAbstract, hasClassScope, lineNumber);
        }
	}

	private String createArgumentString() {
		return "(" + paramTypesInSignature + ")";
	}

	private String getMethodUniqueName(String belongsToClass, String name, String argumentTypes) {
		return CSharpGeneratorToolkit.getUniqueName(belongsToClass, name) + argumentTypes;
	}
}
