package husacct.analyse.task.analyser.csharp.generators.buffers;

import husacct.analyse.domain.IModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;

import org.antlr.runtime.tree.CommonTree;

import static husacct.analyse.task.analyser.csharp.generators.CSharpGeneratorToolkit.*;
import husacct.analyse.task.analyser.csharp.generators.CSharpParameterGenerator;
import husacct.analyse.task.analyser.csharp.generators.SkippableTypes;

import java.util.Stack;

public class DelegateBuffer {
	protected IModelCreationService modelService = new FamixCreationServiceImpl();
	public final String packageAndClassName;
	public String returntype;
	public String name;
	public Stack<String> argtypes;

	public DelegateBuffer(String packageAndClassName) {
		this.packageAndClassName  = packageAndClassName;
	}

	public DelegateBuffer store(CommonTree delegateTree) {
		name = getName(delegateTree);
		returntype = getReturnType(delegateTree);
		argtypes = handleParameters(delegateTree);
		writeToFamix(delegateTree);
		return this;
	}

	private String getName(CommonTree tree) {
		return findHierarchicalSequenceOfTypes(tree, CSharpParser.IDENTIFIER).getText();
	}

	private String getReturnType(CommonTree tree) {
		CommonTree typeTree = findHierarchicalSequenceOfTypes(tree, CSharpParser.TYPE);
		if (typeTree != null) {
			return getTypeNameAndParts(typeTree);
		} else {
			return "";
		}
	}

	private Stack<String> handleParameters(CommonTree tree) {
		CommonTree paramTree = findHierarchicalSequenceOfTypes(tree, CSharpParser.FORMAL_PARAMETER_LIST);
		CSharpParameterGenerator csParamGenerator = new CSharpParameterGenerator();
		return csParamGenerator.generateParameterObjects(paramTree, name, packageAndClassName);
	}

	private void writeToFamix(CommonTree delegateTree) {
		String accessControlQualifier = getVisibility(delegateTree);
		String params = createCommaSeperatedString(argtypes);
		String uniqueName = packageAndClassName + "." + name + "(" + params + ")"; 
		boolean pureAccessor = false;
		boolean isConstructor = false;
		boolean isAbstract = true;
		boolean hasClassScope = true;
		int lineNumber = delegateTree.getLine();
		if(SkippableTypes.isSkippable(returntype)){
			modelService.createMethodOnly(name, uniqueName, accessControlQualifier, params, pureAccessor, returntype, packageAndClassName, isConstructor, isAbstract, hasClassScope, lineNumber);
        } else {
    		modelService.createMethod(name, uniqueName, accessControlQualifier, params, pureAccessor, returntype, packageAndClassName, isConstructor, isAbstract, hasClassScope, lineNumber);
        }
	}
}