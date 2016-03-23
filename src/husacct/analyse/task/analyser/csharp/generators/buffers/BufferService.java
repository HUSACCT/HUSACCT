package husacct.analyse.task.analyser.csharp.generators.buffers;

import husacct.analyse.domain.IModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import husacct.analyse.task.analyser.csharp.generators.CSharpBlockScopeGenerator;
import husacct.analyse.task.analyser.csharp.generators.SkippableTypes;
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
		DelegateBuffer delegateBuffer = delegatebuffers.get(delegatebuffers.size() - 1);
		for(LambdaBuffer lambdaBuffer : lambdabuffers) {
			if(lambdaBuffer.lambdaTypeName.equals(delegateBuffer.name)) {
				combineDelegateAndLambdaToMethodAndSendToBlockScope(delegateBuffer, lambdaBuffer);
			}
		}
	}
	
	//checks name maby add unique name later.
	public void checkDelegateExistsAndCombine() {
		LambdaBuffer lambdaBuffer = lambdabuffers.peekLast();
		for (DelegateBuffer delegateBuffer : delegatebuffers) {
			if (lambdaBuffer.lambdaTypeName.equals(delegateBuffer.name)) {
				combineDelegateAndLambdaToMethodAndSendToBlockScope(delegateBuffer, lambdaBuffer);
			}
		}
	}
	
	private void combineDelegateAndLambdaToMethod(DelegateBuffer delegateBuffer, LambdaBuffer lambdaBuffer, String name, String belongsToMethod, String packageAndClassName) {
		String accessControlQualifier = getVisibility(lambdaBuffer.lambdaTree);
		String paramTypesInSignature = delegateBuffer.paramTypesInSignature;
		boolean isPureAccessor = false;
		String returnTypes = delegateBuffer.returntype;
		String uniqueName = getUniqueName(packageAndClassName, belongsToMethod) + "." + name + "(" + paramTypesInSignature + ")";
		boolean isConstructor = false;
		boolean isAbstract = false;
		boolean hasClassScope = checkClassScope(belongsToMethod);
		int lineNumber = lambdaBuffer.lambdaTree.getLine();
		if(SkippableTypes.isSkippable(returnTypes)){
			modelService.createMethodOnly(name, uniqueName, accessControlQualifier, paramTypesInSignature, isPureAccessor, returnTypes, packageAndClassName, isConstructor, isAbstract, hasClassScope, lineNumber);
        } else {
    		modelService.createMethod(name, uniqueName, accessControlQualifier, paramTypesInSignature, isPureAccessor, returnTypes, packageAndClassName, isConstructor, isAbstract, hasClassScope, lineNumber);
        }
	}

	private void combineDelegateAndLambdaToMethodAndSendToBlockScope(DelegateBuffer delegateBuffer, LambdaBuffer lambdaBuffer) {
		String name = getName(lambdaBuffer.lambdaTree);
		String belongsToMethod = lambdaBuffer.methodName;
		String packageAndClassName = lambdaBuffer.packageAndClassName;
		combineDelegateAndLambdaToMethod(delegateBuffer, lambdaBuffer, name, belongsToMethod, packageAndClassName);
		sendScopeToGenerator(getTreeAftherLambdaSign(lambdaBuffer.lambdaTree), packageAndClassName, belongsToMethod, name);
	}

	private boolean checkClassScope(String methodName) {
		return methodName.isEmpty();
	}

	private String getName(CommonTree lambdaTree) {
		return findHierarchicalSequenceOfTypes(lambdaTree, CSharpParser.IDENTIFIER).getText();
	}

	private CommonTree getTreeAftherLambdaSign(CommonTree lambdaTree) {
		CommonTree lambdaPartTree = findHierarchicalSequenceOfTypes(lambdaTree, CSharpParser.LOCAL_VARIABLE_INITIALIZER);
		CommonTree firstTypeTree = (CommonTree) lambdaPartTree.getFirstChildWithType(CSharpParser.ASSIGNMENT);
		CommonTree secondTypeTree = (CommonTree) lambdaPartTree.getFirstChildWithType(CSharpParser.GT);
		if (firstTypeTree != null && secondTypeTree != null && (firstTypeTree.childIndex + 1) == secondTypeTree.childIndex) {
			return (CommonTree) lambdaPartTree.getChild(secondTypeTree.childIndex + 1);
		}
		return null;
	}

	private void sendScopeToGenerator(CommonTree treeAftherLambdaSign, String packageAndClassName, String methodName, String name) {
		String belongsToMethod = methodName;
		CSharpBlockScopeGenerator blockscopeGenerator = new CSharpBlockScopeGenerator();
		blockscopeGenerator.walkThroughBlockScope(treeAftherLambdaSign, packageAndClassName, belongsToMethod);
	}
	
	public void clear() {
		delegatebuffers = new ArrayList<>();
		lambdabuffers = new LinkedList<>();
	}
}