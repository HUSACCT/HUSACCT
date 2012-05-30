package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.task.analyser.csharp.CSharpInstanceData;
import husacct.analyse.task.analyser.csharp.CSharpTreeConvertController;
import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class CSharpInvocationGenerator extends CSharpGenerator {
	private final CSharpTreeConvertController treeConvertController;
	private int isPosition = -1;
	private boolean hasNew;
	private boolean hasAccolades;
	private CSharpInstanceData instance;
	private String instanceName;
	private String methodSignature;
	private String belongsToClass;
	private String from;
	private String to;
	
	public CSharpInvocationGenerator(CSharpTreeConvertController treeConvertController, String belongsToClass) {
		this.belongsToClass = belongsToClass;
		this.treeConvertController = treeConvertController;
	}

	public void generateInvocation(List<CommonTree> tree, int lineNumber, String methodSignature){
		this.methodSignature = methodSignature;
		setTreeProperties(tree);
		checkIfTreeHasMultipleInvocations(tree);
		
		if(isPosition == -1 && hasAccolades){
			generateMethodInvocation(tree, lineNumber, methodSignature);
			return;
		}
		
		if(isPosition >= 1 && hasNew){
			generateConstructorInvocation(tree, lineNumber, methodSignature);
			return;
		}else if(isPosition >= 0){
			generatePropertyOrFieldInvocation(tree, lineNumber, methodSignature);
		}
	}
	
	private void setInvocationDetails(List<CommonTree> tree) {
		if(instance != null){
			from = instance.getBelongsToClass();
			to = instance.getTo();	
		}
//		}else{
//			CommonTree node = tree.get(0);
//			from = belongsToClass+"."+methodSignature+"."+instanceName;
//			to = node.getText();
//		}
	}

	private void setInstance(String name) {
		List<CSharpInstanceData> instances = treeConvertController.getInstances();
		for(CSharpInstanceData instance : instances){
			if(instance.getInstanceName().equals(name)){
				this.instance = instance;
				break;
			}
		}
	}

	private String getName(List<CommonTree> tree, int namePosition) {
		String name = "";
		CommonTree firstNode = tree.get(0);
		int type = firstNode.getType();
		if(type == NEW){
			for(int i = 1; i < 4; i++){
				CommonTree node = tree.get(i);
				name = name + node.getText();
			}
		}else{
			if (namePosition >= 0) {
				CommonTree node = tree.get(namePosition);
				name = node.getText();
			}
		}
		return name;
	}

	private void setTreeProperties(List<CommonTree> tree) {
		for(int i = 0; i < tree.size(); i++){
			CommonTree currentNode = tree.get(i);
			int currentType = currentNode.getType();
			
			if(currentType == FORWARDBRACKET){
				hasAccolades = true;;
			}
			if(currentType == IS){
				isPosition = i;
			}
			if(currentType == NEW){
				hasNew = true;
			}
		}
	}
	
	private void generatePropertyOrFieldInvocation(List<CommonTree> tree, int lineNumber, String methodSignature) {
		instanceName = getName(tree, isPosition-1);
		setInstance(instanceName);
		setInvocationDetails(tree);
		modelService.createPropertyOrFieldInvocation(from, to, lineNumber, instanceName, methodSignature, instanceName);
		clear();
	}

	private void generateConstructorInvocation(List<CommonTree> tree, int lineNumber, String methodSignature) {
		instanceName = getName(tree, isPosition-1);
		setInstance(instanceName);
		setInvocationDetails(tree);
		modelService.createConstructorInvocation(from, to, lineNumber, instanceName, methodSignature, instanceName);
		clear();
	}


	private void generateMethodInvocation(List<CommonTree> tree, int lineNumber, String methodSignature ) {
		if(hasNew){
			generateConstructorInvocation(tree, lineNumber, methodSignature);
		}
		instanceName = getName(tree, 0);
		setInstance(instanceName);
		setInvocationDetails(tree);
		modelService.createMethodInvocation(from, to, lineNumber, instanceName, methodSignature, instanceName);
		clear();
	}
	
	private void clear(){
		isPosition = -1;
		hasAccolades = false;
		hasNew = false;
		instance = null;
		instanceName = null;
		from = null;
		to = null;
		methodSignature = null;
	}
	
	private void checkIfTreeHasMultipleInvocations(List<CommonTree> tree) {
		int amounthOfBrackets = 0;
		int lineNumber = 0;
		List<CommonTree> tempTree = new ArrayList<CommonTree>();
		for(CommonTree node : tree){
			int type = node.getType();
			if(type == BACKWARDBRACKET){
				amounthOfBrackets--;
				lineNumber = node.getLine();
			}
			
			if(amounthOfBrackets > 0){
				tempTree.add(node);
			}
			if(type == FORWARDBRACKET){
				amounthOfBrackets++;
			}		
		}
		if(tempTree.size() > 0){
			CSharpInvocationGenerator localVariableConverter = new CSharpInvocationGenerator(treeConvertController, belongsToClass);
			localVariableConverter.generateInvocation(tempTree, lineNumber, methodSignature);
		}
	}

	public boolean checkIfTreeHasInvocation(List<CommonTree> tree) {
		setTreeProperties(tree);
		CommonTree secondNode = tree.get(1);
		int type = secondNode.getType();
		
		if(hasNew && isPosition == 1){
			clear();
			return true;
		}else if(hasAccolades && type == DOT){
			clear();
			return true;
		}else if(type == IS){
			clear();
			return true;
		}
		
		clear();
		return false;
	}
}
