package husacct.analyse.task.analyser.csharp.convertControllers;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

import husacct.analyse.task.analyser.csharp.CSharpTreeConvertController;
import husacct.analyse.task.analyser.csharp.generators.CSharpGenerator;
import husacct.analyse.task.analyser.csharp.generators.CSharpLocalVariableGenerator;

public class CSharpLocalVariableConvertController extends CSharpGenerator {
	private final CSharpTreeConvertController treeConvertController;
	private List<CommonTree> localVariableTrees;

	public CSharpLocalVariableConvertController(CSharpTreeConvertController treeConvertController) {
		this.treeConvertController = treeConvertController;
		localVariableTrees = new ArrayList<CommonTree>();
	}
	
	public boolean localVariableCheck(CommonTree tree, boolean isPartOfLocalVariable) {
		int type = tree.getType();		
		if(!isPartOfLocalVariable){
			isPartOfLocalVariable = checkBeginning(isPartOfLocalVariable, type);
		}		
		if(type == SEMICOLON){
			isPartOfLocalVariable = endLocalVariable(tree);
		}		
		if(isPartOfLocalVariable){
			localVariableTrees.add(tree);
		}		
		return isPartOfLocalVariable;
	}

	private boolean checkBeginning(boolean isPartOfLocalVariable, int type) {
		for(int thisType : typeCollection){
			if(type == thisType){
				return true;
			}
		}
		return isPartOfLocalVariable;
	}

	private boolean endLocalVariable(CommonTree tree) {
		int lineNumber = tree.getLine();
		String uniqueClassName = treeConvertController.getUniqueClassName();
		if(localVariableTrees.size() > 0){
			cleanVariableList();
		}		
		if(localVariableTrees.size() > 1){
			CSharpLocalVariableGenerator localVariableGenerator = new CSharpLocalVariableGenerator(treeConvertController);
			localVariableGenerator.generateLocalVariable(localVariableTrees, treeConvertController.getCurrentMethodName(), uniqueClassName, lineNumber);
		}
		localVariableTrees.clear();
		return false;
	}

	private void cleanVariableList() {
		boolean isLocalVariable = true;
		final int[] notAllowedTypes = new int[]{FORWARDCURLYBRACKET, FORWARDBRACKET};
		for(CommonTree node : localVariableTrees){
			int type = node.getType();
			for (int notAllowedtype : notAllowedTypes){
				if(type == notAllowedtype){
					isLocalVariable = false;
				}
			}
		}		
		if(!(isLocalVariable)){
			localVariableTrees.clear();
		}
	}
}