package husacct.analyse.task.analyser.csharp.generators;

import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class CSharpExceptionGenerator extends CSharpGenerator{
	private String exceptionClass;
	
	public void generateException(List<CommonTree> tree, String belongsToClass, int lineNumber){
		for(CommonTree node : tree){
			exceptionClass = checkForExceptionClass(node);
		}
		modelService.createException(belongsToClass, exceptionClass, lineNumber, exceptionClass);
	}

	private String checkForExceptionClass(CommonTree node) {
		if(exceptionClass == null){
			exceptionClass = "";
		}
		
		if(node.getType() == IDENTIFIER && exceptionClass.isEmpty()){
			exceptionClass = node.getText();
		}
		return exceptionClass;
	}
}
