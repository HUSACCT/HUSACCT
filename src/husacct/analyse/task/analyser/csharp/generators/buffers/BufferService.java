package husacct.analyse.task.analyser.csharp.generators.buffers;

import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class BufferService {
	private static BufferService instance;
	public List<LambdaBuffer> lambdabuffer;
	public List<DelegateBuffer> delegatebuffer;
	
	private BufferService() {}
	
	public static BufferService getInstance() {
		if (instance == null)
			instance = new BufferService();
		return instance;
	}
	
	public void addLambda(String packageAndClassname, String methodname, CommonTree lambdaTree) {
		lambdabuffer.add(new LambdaBuffer(packageAndClassname, methodname, lambdaTree));
	}
	
	public void addDelegate(String packageAndClassname, CommonTree delegateTree) {
		delegatebuffer.add(new DelegateBuffer(packageAndClassname, delegateTree));
	}
}
