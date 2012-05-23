package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.task.analyser.csharp.CSharpInstanceData;
import husacct.analyse.task.analyser.csharp.CSharpTreeConvertController;

import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class CSharpInvocationGenerator extends CSharpGenerator {
	private final CSharpTreeConvertController treeConvertController;
	private int isPosition = -1;
	private boolean hasNew;
	private boolean hasAccolades;
	private CSharpInstanceData instance;
	private String instanceName;
	private String from;
	private String to;
	
	public CSharpInvocationGenerator(CSharpTreeConvertController treeConvertController) {
		this.treeConvertController = treeConvertController;
	}

	public void generateInvocation(List<CommonTree> tree, int lineNumber, String methodSignature){
		setTreeProperties(tree);
		
		if(isPosition == -1 && hasAccolades){
			instanceName = getName(tree, 0);
			setInstance(instanceName);
			setInvocationDetails();
			generateMethodInvocation(lineNumber, methodSignature);
			clear();
			return;
		}
		if(isPosition >= 1 && hasNew){
			instanceName = getName(tree, isPosition-1);
			setInstance(instanceName);
			setInvocationDetails();
			generateConstructorInvocation(lineNumber, methodSignature);
			clear();
			return;
		}else if(isPosition >= 0){
			instanceName = getName(tree, isPosition-1);
			setInstance(instanceName);
			setInvocationDetails();
			generatePropertyOrFieldInvocation(lineNumber, methodSignature);
			clear();
		}
	}
	
	private void setInvocationDetails() {
		from = instance.getBelongsToClass();
		to = instance.getTo();	
	}

	private void setInstance(String name) {
		List<CSharpInstanceData> instances = treeConvertController.getInstances();
		for(CSharpInstanceData instance : instances){
			if(instance.getInstanceName().equals(name)){
				this.instance = instance;
			}
		}
	}

	private String getName(List<CommonTree> tree, int namePosition) {
		CommonTree node = tree.get(namePosition);
		return node.getText();
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
	
	private void generatePropertyOrFieldInvocation(int lineNumber, String methodSignature) {
		modelService.createPropertyOrFieldInvocation(from, to, lineNumber, instanceName, methodSignature, instanceName);
	}

	private void generateConstructorInvocation(int lineNumber, String methodSignature) {
		modelService.createConstructorInvocation(from, to, lineNumber, instanceName, methodSignature, instanceName);
	}


	private void generateMethodInvocation(int lineNumber, String methodSignature ) {
		modelService.createMethodInvocation(from, to, lineNumber, instanceName, methodSignature, instanceName)	;
	}
	
	private void clear(){
		isPosition = -1;
		hasAccolades = false;
		hasNew = false;
		instance = null;
		instanceName = null;
		from = null;
		to = null;
	}

	public boolean checkIfTreeisOnlyInvocation(List<CommonTree> tree) {
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
