package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.task.analyser.csharp.CSharpInstanceData;
import husacct.analyse.task.analyser.csharp.CSharpTreeConvertController;

import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class CSharpLocalVariableGenerator extends CSharpGenerator {
	private final CSharpTreeConvertController treeConvertController;
	private String name;
	private String uniqueName;
	private String declareType;
	private String methodSignature;
	private String belongsToClass;
	private int lineNumber;
	private int namePosition;
	CSharpInvocationGenerator invocationGenerator;
	
	public CSharpLocalVariableGenerator(CSharpTreeConvertController treeConvertController){
		this.treeConvertController = treeConvertController;
		invocationGenerator = new CSharpInvocationGenerator(this.treeConvertController);
		
	}
	
	public void generateLocalVariable(List<CommonTree> tree, String methodSignature, String belongsToClass, int lineNumber){
		this.methodSignature = methodSignature;
		this.belongsToClass = belongsToClass;
		this.lineNumber = lineNumber;
		 
		boolean isInvocation = invocationGenerator.checkIfTreeisOnlyInvocation(tree);
		
		if(!(isInvocation)){
			generateFamixObject(tree);
		}else{
			invocationGenerator.generateInvocation(tree, lineNumber, methodSignature);
		}
	}

	private void generateFamixObject(List<CommonTree> tree){
		declareType = checkForDeclareType(tree);
		name = checkForName(tree, namePosition);
		uniqueName = belongsToClass + "." + methodSignature + "." + name;	
		modelService.createLocalVariable(belongsToClass, declareType, name, uniqueName, lineNumber, methodSignature);
		addInstance();
	}

	private void addInstance() {
		CSharpInstanceData instance = new CSharpInstanceData();
		instance.setBelongsToClass(uniqueName);
		instance.setTo(declareType);
		instance.setInstanceName(name);
		instance.setHasMethodScope(true);
		treeConvertController.addInstance(instance);
	}

	private String checkForDeclareType(List<CommonTree> tree) {
		String returnType = "";
		boolean usesLongName = false;
		for(int i = 0; i < tree.size(); i++){
			CommonTree currentNode = tree.get(i);
			CommonTree nextNode = null;
			int currentType = currentNode.getType();
			int nextType = 0;
			
			if(i < tree.size()-1){
				nextNode = tree.get(i+1);
				nextType = nextNode.getType();
			}
			
			if(nextNode != null){
				if(currentType == DOT || nextType == DOT || currentType == LESSTHAN || nextType == LESSTHAN || currentType == GREATERTHAN || nextType == GREATERTHAN){
					usesLongName = true;
				}
			}else{
				usesLongName = false;
			}
			
			if(usesLongName){
				returnType = returnType + currentNode.getText();
				namePosition = i+1;
			}else if(i < 1){
				returnType = currentNode.getText();
				namePosition = i+1;
			}
		}
		
		return returnType;
	}
	
	private String checkForName(List<CommonTree> tree, int namePosition) {
		CommonTree node = tree.get(namePosition);
		return node.getText();
	}
}