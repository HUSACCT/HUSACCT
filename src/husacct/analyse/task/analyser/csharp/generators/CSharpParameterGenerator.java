package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.task.analyser.csharp.CSharpInstanceData;
import husacct.analyse.task.analyser.csharp.CSharpTreeConvertController;

import java.util.Arrays;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class CSharpParameterGenerator  extends CSharpGenerator{
	private final CSharpTreeConvertController treeConvertController;
	
	public CSharpParameterGenerator(List<CommonTree> parameterTrees,CSharpTreeConvertController cSharpTreeConvertController, CSharpTreeConvertController treeConvertController) {
		this.treeConvertController = treeConvertController;
		String typeParameter = "";
		String invocationParameter = "";
		for (CommonTree tree : parameterTrees) {
			int type = tree.getType();	
			String text = tree.getText();	
			if (Arrays.binarySearch(typeCollection, type) > -1) {
				if (typeParameter.equals("")) {
					typeParameter = text;
				} else {
					invocationParameter = text;
				}
			}
			if (type == COMMA || type == BACKWARDBRACKET) {
				String uniqueClassName = cSharpTreeConvertController.getUniqueClassName();
				String uniqueMethodName = uniqueClassName + "." + cSharpTreeConvertController.getCurrentMethodName();
				String uniqueParameterName = uniqueMethodName + "." + invocationParameter;
				modelService.createParameter(invocationParameter, uniqueParameterName, typeParameter, uniqueClassName, tree.getLine(), uniqueMethodName, typeParameter);
				addInstance(uniqueClassName, typeParameter, invocationParameter);
				typeParameter = "";
				invocationParameter = "";
			}
		}	
	}
	private void addInstance(String uniqueClassName, String declareType, String instanceName) {
		CSharpInstanceData instance = new CSharpInstanceData();
		instance.setBelongsToClass(uniqueClassName);
		instance.setTo(declareType);
		instance.setInstanceName(instanceName);
		instance.setClassScope(false);
		treeConvertController.addInstance(instance);
	}
}
