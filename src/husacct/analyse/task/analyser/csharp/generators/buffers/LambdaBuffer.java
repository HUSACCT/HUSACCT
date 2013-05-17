package husacct.analyse.task.analyser.csharp.generators.buffers;

import org.antlr.runtime.tree.CommonTree;

public class LambdaBuffer {
	public final String packageAndClassName;
	public final String methodName;
	public CommonTree lambdaTree;
	
	public LambdaBuffer(String packageAndClassName, String methodName, CommonTree lambdaTree) {
		this.packageAndClassName  = packageAndClassName;
		this.methodName = methodName;
		this.lambdaTree = lambdaTree;
	}
}
