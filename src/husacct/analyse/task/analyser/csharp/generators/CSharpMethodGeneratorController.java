package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.infrastructure.antlr.TreePrinter;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import java.util.ArrayList;
import java.util.*;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class CSharpMethodGeneratorController extends CSharpGenerator {

	String belongsToClass;
	String name;
	String uniqueName;
	String signature;
	Stack<String> argTypes = new Stack<>();
	ArrayList<String> returnTypes;
	boolean isPureAccessor;
	boolean isConstructor;
	boolean isAbstract;
	boolean hasClassScope;

	public void generateMethodToDomain(CommonTree methodTree, String currentUniqueClassName) {
		this.belongsToClass = currentUniqueClassName;
		String accessControlQualifier = CSharpGeneratorToolkit.getVisibility(methodTree);
		int lineNumber = methodTree.getLine();

		checkMethodType(methodTree, belongsToClass);
		walkThroughMethod(methodTree);
		createMethodObject(belongsToClass, accessControlQualifier, lineNumber);
	}

	private void checkMethodType(CommonTree methodTree, String belongsToClass) {
		returnTypes = new ArrayList<String>();

		switch (methodTree.getType()) {
			case CSharpParser.CONSTRUCTOR_DECL:
				returnTypes.add("");
				isConstructor = true;
				name = getClassOfUniqueName(belongsToClass);
				break;
			default:
				getReturnTypes(methodTree);
				isConstructor = false;
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

					System.out.println(child.toStringTree());
					getFormalParameter((CommonTree) child);
					break;

			}
		}
	}

	private void createMethodObject(String belongsToClass, String accessControlQualifier, int lineNumber) {
		uniqueName = belongsToClass + "." + this.name + signature;
		/*for(String s : declaredReturnType){
		 if(!SkippedTypes.isSkippable(s)){
		 returnTypes.add(s);
		 }
		 }
		 */
		//if(!SkippedTypes.isSkippable(declaredReturnType)){
		modelService.createMethod(name, uniqueName, accessControlQualifier, signature, isPureAccessor, returnTypes, belongsToClass, isConstructor, isAbstract, hasClassScope, lineNumber);
		//}
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
}
