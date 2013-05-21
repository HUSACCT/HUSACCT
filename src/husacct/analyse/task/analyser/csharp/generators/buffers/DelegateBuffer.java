package husacct.analyse.task.analyser.csharp.generators.buffers;

import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class DelegateBuffer {
	public final String packageAndClassName;
	public String returntype;
	public String name;
	public List<String> argtypes;
	
	public DelegateBuffer(String packageAndClassName) {
		this.packageAndClassName  = packageAndClassName;
	}
	
	public DelegateBuffer writeToFamix(CommonTree tree) {
		//TODO: write delegate to famix as abstract method.
		return this;
	}
	
}
