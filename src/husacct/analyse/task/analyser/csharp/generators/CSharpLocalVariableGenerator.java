package husacct.analyse.task.analyser.csharp.generators;

import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class CSharpLocalVariableGenerator extends CSharpGenerator {
	private String name;
	private String uniqueName;
	private String declareType;
	
	public void generateLocalVariable(List<CommonTree> tree, String methodSignature, String belongsToClass, int lineNumber){
		declareType = checkForDeclareType(tree);
		name = checkForName(tree);
		uniqueName = belongsToClass + "." + methodSignature + "." + name;
		modelService.createLocalVariable(methodSignature, belongsToClass, declareType, name, uniqueName, lineNumber);
	}

	private String checkForDeclareType(List<CommonTree> tree) {
		CommonTree node = tree.get(0);
		String returnType = node.getText();
		
		return returnType;
	}
	
	private String checkForName(List<CommonTree> tree) {
		CommonTree node = tree.get(1);
		String name = node.getText();
		
		return name;
	}
}