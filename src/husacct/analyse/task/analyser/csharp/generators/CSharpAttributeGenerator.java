package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.task.analyser.csharp.CSharpInstanceData;
import husacct.analyse.task.analyser.csharp.CSharpTreeConvertController;

import java.util.Arrays;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class CSharpAttributeGenerator extends CSharpGenerator {
	private final CSharpTreeConvertController treeConvertController;
	private List<CommonTree> attributeTrees;
	private String uniqueClassName;
	private boolean classScope = false;
	private String accessControlQualifier = "";
	private String attributeName = "";
	private String declareType = "";
	private CSharpInvocationGenerator invocationGenerator;
	

	public CSharpAttributeGenerator(List<CommonTree> attributeTrees, String uniqueClassName, CSharpTreeConvertController treeConvertController) {
		this.treeConvertController = treeConvertController;
		this.attributeTrees = attributeTrees;
		this.uniqueClassName = uniqueClassName;
		invocationGenerator = new CSharpInvocationGenerator(this.treeConvertController, this.uniqueClassName);
	}

	private void addToModelService() {
		if (!attributeName.equals("") && !declareType.equals("")) {
			int lineNumber = attributeTrees.get(0).getLine();
			String uniqueName = uniqueClassName + "." + attributeName;
			modelService.createAttribute(classScope, accessControlQualifier, uniqueClassName, declareType, attributeName, uniqueName, lineNumber);
			addInstance();
			createAttributeInvocation(lineNumber);
		}
	}
	
	private void createAttributeInvocation(int lineNumber){
		boolean hasInvocation = invocationGenerator.checkIfTreeHasInvocation(attributeTrees);
		if(hasInvocation){
			invocationGenerator.generateInvocation(attributeTrees, lineNumber, uniqueClassName);
		}
	}
	
	private void addInstance() {
		CSharpInstanceData instance = new CSharpInstanceData();
		instance.setBelongsToClass(uniqueClassName);
		instance.setTo(declareType);
		instance.setInstanceName(attributeName);
		instance.setClassScope(true);
		treeConvertController.addInstance(instance);
	}

	private void checkAccessorCollection(int type, String text) {
		if (Arrays.binarySearch(accessorCollection, type) > -1) {
			accessControlQualifier += text + " ";
		}
	}

	private void checkClassScope(int type) {
		if (type == STATIC) {
			classScope = true;
		}
	}

	private boolean checkName(int type, String text) {
		if (type == IDENTIFIER) {
			attributeName = text;
			return true;
		}
		return false;
	}

	private boolean checkType(int type, String text) {
		if (Arrays.binarySearch(typeCollection, type) > -1) {
			declareType = text;
			return true;
		}
		return false;
	}

	private void editAccessControlQualifier() {
		if (accessControlQualifier.equals("")) {
			accessControlQualifier = "internal";
		} else {
			accessControlQualifier = accessControlQualifier.trim();
		}
	}

	public void scanTree() {
		boolean hadType = false;
		boolean hadName = false;
		for (CommonTree attributeChild : attributeTrees) {
			int type = attributeChild.getType();
			String text = attributeChild.getText();
			checkClassScope(type);
			checkAccessorCollection(type, text);
			if (hadType) {
				hadName = checkName(type, text);
			} else {
				hadType = checkType(type, text);
			}
			if (type == EQUALS && !hadName) {
				return;
			}
		}
		editAccessControlQualifier();
		addToModelService();
	}
}