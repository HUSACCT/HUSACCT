package husacct.analyse.task.analyse.csharp.generators;

import husacct.analyse.task.analyse.csharp.generators.buffers.BufferService;

public class CSharpLamdaGenerator {
	
	public void delegateDelegateToBuffer(CommonTree delegateTree, String packageAndClassName) {
		BufferService.getInstance().addDelegate(packageAndClassName, delegateTree);
	}
	
	public void delegateLambdaToBuffer(CommonTree delegateTree, String packageAndClassName, String belongsToMethod) {
		BufferService.getInstance().addLambda(packageAndClassName, belongsToMethod, delegateTree);
	}
}
