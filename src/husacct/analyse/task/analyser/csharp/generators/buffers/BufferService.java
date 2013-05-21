package husacct.analyse.task.analyser.csharp.generators.buffers;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class BufferService {
	private static BufferService instance;
	public List<LambdaBuffer> lambdabuffers = new ArrayList<>();
	public List<DelegateBuffer> delegatebuffers = new ArrayList<>();

	private BufferService() {}

	public static BufferService getInstance() {
		if (instance == null)
			instance = new BufferService();
		return instance;
	}

	public void addLambda(String packageAndClassname, String methodname, CommonTree lambdaTree) {
		lambdabuffers.add(new LambdaBuffer(packageAndClassname, methodname, lambdaTree));
	}

	public void addDelegate(String packageAndClassname, CommonTree delegateTree) {
		delegatebuffers.add(new DelegateBuffer(packageAndClassname).store(delegateTree));
	}

	public void checkDelegateExists() {
		LambdaBuffer lb = lambdabuffers.get(lambdabuffers.size()-1);
		for(DelegateBuffer db : delegatebuffers) {
			if(lb.lambdaTypeName.equals(db.name)) {
				
			}
		}
	}
}