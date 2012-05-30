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
	private CSharpInvocationGenerator invocationGenerator;
	
	public CSharpLocalVariableGenerator(CSharpTreeConvertController treeConvertController){
		this.treeConvertController = treeConvertController;
	}
	
	public void generateLocalVariable(List<CommonTree> tree, String methodSignature, String belongsToClass, int lineNumber){
		this.methodSignature = methodSignature;
		this.belongsToClass = belongsToClass;
		this.lineNumber = lineNumber;
		
		invocationGenerator = new CSharpInvocationGenerator(this.treeConvertController, belongsToClass);
		boolean isOnlyInvocation = invocationGenerator.checkIfTreeHasInvocation(tree);
		
		if(!(isOnlyInvocation)){
			generateFamixObject(tree);
		}
		
		invocationGenerator.generateInvocation(tree, lineNumber, methodSignature);
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
		instance.setClassScope(false);
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
			usesLongName = false;
			
			if(i < 1 && currentType == NEW){
				returnType = nextNode.getText();
				namePosition = i+1;
				return returnType;
			}else if(currentType == FORWARDBRACKET && nextType == NEW){
				CommonTree node = tree.get(i+2);
				//int type = node.getType();
				returnType = node.getText();
				namePosition = i+2;
				return returnType;
			}
			
			if(nextNode != null){
				if(currentType == DOT /*|| nextType == DOT || currentType == LESSTHAN ||*/ /*nextType == LESSTHAN || currentType == GREATERTHAN*/ /*|| nextType == GREATERTHAN */|| nextType == IDENTIFIER){
					usesLongName = true;
				}
				else{
					return returnType;
				}
			}
			
			if(usesLongName){
				returnType = returnType + currentNode.getText();
				namePosition = i+1;
			}else if(i < 1){
				returnType = currentNode.getText();
				namePosition = i+1;
				return returnType;
			}
		}
		
		return returnType;
	}
	
	private String checkForName(List<CommonTree> tree, int namePosition) {
		String name = "";
		CommonTree firstNode = tree.get(0);
		int type = firstNode.getType();
		if(type == NEW){
			for(int i = 1; i < 4; i++){
				CommonTree node = tree.get(i);
				name = name + node.getText();
			}
		}else{
			CommonTree node = tree.get(namePosition);
			name = node.getText();
		}
		return name;
	}
}