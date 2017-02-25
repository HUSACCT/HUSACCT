package husacct.analyse.task.analyse.csharp.generators.buffers;

import static husacct.analyse.task.analyse.csharp.generators.CSharpGeneratorToolkit.*;

public class LambdaBuffer {
	public final String packageAndClassName;
	public final String methodName;
	public final String lambdaTypeName;
	public CommonTree lambdaTree;
	
	public LambdaBuffer(String packageAndClassName, String methodName, CommonTree lambdaTree) {
		this.packageAndClassName  = packageAndClassName;
		this.methodName = methodName;
		this.lambdaTree = lambdaTree;
		this.lambdaTypeName = getReturnType(lambdaTree);
	}

	private String getReturnType(CommonTree tree) {
		CommonTree typeTree = findHierarchicalSequenceOfTypes(tree, CSharpParser.TYPE);
		return getTypeNameAndParts(typeTree);
	}
}
