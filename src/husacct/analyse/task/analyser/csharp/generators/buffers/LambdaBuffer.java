package husacct.analyse.task.analyser.csharp.generators.buffers;

import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import org.antlr.runtime.tree.CommonTree;
import static husacct.analyse.task.analyser.csharp.generators.CSharpGeneratorToolkit.*;

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
		CommonTree typeTree = walkTree(tree, CSharpParser.TYPE);
		return getTypeNameAndParts(typeTree);
	}
}
