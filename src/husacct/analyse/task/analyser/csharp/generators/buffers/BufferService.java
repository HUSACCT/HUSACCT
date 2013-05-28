package husacct.analyse.task.analyser.csharp.generators.buffers;

import husacct.analyse.domain.IModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import husacct.analyse.task.analyser.csharp.generators.CSharpBlockScopeGenerator;
import static husacct.analyse.task.analyser.csharp.generators.CSharpGeneratorToolkit.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.antlr.runtime.tree.CommonTree;

public class BufferService {
	protected IModelCreationService modelService = new FamixCreationServiceImpl();
	private static BufferService instance;
	public LinkedList<LambdaBuffer> lambdabuffers = new LinkedList<>();
	public List<DelegateBuffer> delegatebuffers = new ArrayList<>();

	private BufferService() {}

	public static BufferService getInstance() {
		if (instance == null)
			instance = new BufferService();
		return instance;
	}

	public void addLambda(String packageAndClassname, String methodname, CommonTree lambdaTree) {
		lambdabuffers.add(new LambdaBuffer(packageAndClassname, methodname, lambdaTree));
		checkDelegateExistsAndCombine();
	}

	public void addDelegate(String packageAndClassname, CommonTree delegateTree) {
		delegatebuffers.add(new DelegateBuffer(packageAndClassname).store(delegateTree));
		checkLambdaExistsAndCombine();
	}

	public void checkLambdaExistsAndCombine() {
		DelegateBuffer db = delegatebuffers.get(delegatebuffers.size()-1);
		for(LambdaBuffer lb : lambdabuffers) {
			if(lb.lambdaTypeName.equals(db.name)) {
				combineDelegateAndLambdaToMethodAndSendToBlockScope(db, lb);
			}
		}
	}
	
	//checks name maby add unique name later.
	public void checkDelegateExistsAndCombine() {
		LambdaBuffer lb = lambdabuffers.peekLast();
		for(DelegateBuffer db : delegatebuffers) {
			if(lb.lambdaTypeName.equals(db.name)) {
				combineDelegateAndLambdaToMethodAndSendToBlockScope(db, lb);
			}
		}
	}
	
	private void combineDelegateAndLambdaToMethod(DelegateBuffer db, LambdaBuffer lb, String name, String belongsToMethod, String packageAndClassName) {

		String accessControlQualifier = getVisibility(lb.lambdaTree);
		String argumentTypes = createCommaSeperatedString(db.argtypes);
		boolean isPureAccessor = false;
		String returnTypes = db.returntype;
		String uniqueName = getUniqueName(packageAndClassName, belongsToMethod) + "." + name + "(" + argumentTypes + ")";
		boolean isConstructor = false;
		boolean isAbstract = false;
		boolean hasClassScope = checkClassScope(belongsToMethod);
		int lineNumber = lb.lambdaTree.getLine();
		modelService.createMethod(name, uniqueName, accessControlQualifier, argumentTypes, isPureAccessor, returnTypes, packageAndClassName, isConstructor, isAbstract, hasClassScope, lineNumber);
	}

	private void combineDelegateAndLambdaToMethodAndSendToBlockScope(DelegateBuffer db, LambdaBuffer lb) {
		String name = getName(lb.lambdaTree);
		String belongsToMethod = lb.methodName;
		String packageAndClassName = lb.packageAndClassName;
		
		combineDelegateAndLambdaToMethod(db, lb, name, belongsToMethod, packageAndClassName);
		sendScopeToGenerator(getTreeAftherLambdaSign(lb.lambdaTree), packageAndClassName, belongsToMethod, name);
	}

	private boolean checkClassScope(String methodName) {
		return methodName.isEmpty();
	}

	private String getName(CommonTree lambdaTree) {
		return walkTree(lambdaTree, CSharpParser.IDENTIFIER).getText();
	}

	private CommonTree getTreeAftherLambdaSign(CommonTree lambdaTree) {
		CommonTree lambdaPartTree = walkTree(lambdaTree, CSharpParser.LOCAL_VARIABLE_INITIALIZER);
		
		CommonTree firstTypeTree = (CommonTree) lambdaPartTree.getFirstChildWithType(CSharpParser.ASSIGNMENT);
		CommonTree secondTypeTree = (CommonTree) lambdaPartTree.getFirstChildWithType(CSharpParser.GT);
		if(firstTypeTree != null && secondTypeTree != null && (firstTypeTree.childIndex + 1) == secondTypeTree.childIndex)
		{
			return (CommonTree) lambdaPartTree.getChild(secondTypeTree.childIndex + 1);
		}
		return null;
	}

	private void sendScopeToGenerator(CommonTree treeAftherLambdaSign, String packageAndClassName, String methodName, String name) {
		String belongsToMethod = methodName;
	/*	if(methodName.isEmpty()) {
			belongsToMethod = name;
		}*/
		
		CSharpBlockScopeGenerator cbsg = new CSharpBlockScopeGenerator();
		cbsg.walkThroughBlockScope(treeAftherLambdaSign, packageAndClassName, belongsToMethod);
//		cbsg.walkThroughBlockScope(treeAftherLambdaSign, packageAndClassName, name);
	}
}