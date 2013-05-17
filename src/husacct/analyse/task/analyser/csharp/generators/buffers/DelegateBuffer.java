package husacct.analyse.task.analyser.csharp.generators.buffers;

import org.antlr.runtime.tree.CommonTree;

public class DelegateBuffer {
	public final String packageAndClassName;
	public CommonTree delegateTree;
	
	public DelegateBuffer(String packageAndClassName, CommonTree delegateTree) {
		this.packageAndClassName  = packageAndClassName;
		this.delegateTree = delegateTree;
	}
}
