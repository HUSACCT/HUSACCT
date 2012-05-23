package husacct.analyse.task.analyser.csharp.generators;

import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class CSharpLocalVariableGenerator extends CSharpGenerator {
	private String name;
	private String uniqueName;
	private String declareType;
	private String methodSignature;
	private String belongsToClass;
	private int lineNumber;
	private int namePosition;
	private int IsPosition;
	
	public void generateLocalVariable(List<CommonTree> tree, String methodSignature, String belongsToClass, int lineNumber){
		this.methodSignature = methodSignature;
		this.belongsToClass = belongsToClass;
		this.lineNumber = lineNumber;
		IsPosition = setIsPosition(tree);
		if(IsPosition == -1){
			generateLocalVariableFamixObject(tree);
		}
	}
	
	private void generateLocalVariableFamixObject(List<CommonTree> tree){
		declareType = checkForDeclareType(tree);
		name = checkForName(tree, namePosition);
		uniqueName = belongsToClass + "." + methodSignature + "." + name;	
		modelService.createLocalVariable(belongsToClass, declareType, name, uniqueName, lineNumber, methodSignature);
	}

	private int setIsPosition(List<CommonTree> tree) {
		int position = -1;
		for(int i = 0; i < tree.size(); i++){
			CommonTree currentNode = tree.get(i);
			int currentType = currentNode.getType();
			if(currentType == IS){
				position = i;
			}
		}
		return position;
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
			}else if(i < 1){
				returnType = currentNode.getText();
			}
			
			namePosition = i;
			
		}
		
		return returnType;
	}
	
	private String checkForName(List<CommonTree> tree, int namePosition) {
		CommonTree node = tree.get(namePosition);
		return node.getText();
	}
}