package husacct.analyse.task.analyser.csharp.generators;

import java.util.ArrayList;

import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;

import org.antlr.runtime.tree.CommonTree;

public class CSharpInvocationConstructorGenerator extends AbstractCSharpInvocationGenerator{
	private ArrayList<String> toNames = new ArrayList<String>();

	public CSharpInvocationConstructorGenerator(String packageAndClassName) {
		super(packageAndClassName);
	}
	
	public void generateConstructorInvocToDomain(CommonTree treeNode, String belongsToMethod) {
		invocationName = "Constructor";
		this.belongsToMethod = belongsToMethod;
		
		findConstructorInvocation(treeNode);
		saveInvocationToDomain();
	}

	private void findConstructorInvocation(CommonTree treeNode) {
		boolean constructorFound = hasConstructorCall(treeNode);
		if (constructorFound){
			createConstructorInvocationDetails((CommonTree) treeNode);
		}
		else {
			int childcount = treeNode.getChildCount();
			for (int i = 0; i < childcount; i++) {
				CommonTree child =  (CommonTree) treeNode.getChild(i);
				findConstructorInvocation(child);
			}
		}
	}

	private boolean hasConstructorCall(CommonTree treeNode) {
		return treeNode.getType() == CSharpParser.OBJECT_CREATION_EXPRESSION;
	}

	private void createConstructorInvocationDetails(CommonTree treeNode) {
		this.to = setToName(treeNode);
		this.lineNumber = treeNode.getChild(0).getLine();
	}

	private String setToName(CommonTree treeNode) {
		String toName = "";
		walkTreeToGetNames(treeNode);	
		
		for (String name : toNames) {
			toName += name + ".";
		}
		
		toName = removeLastDot(toName);
		return toName;
	}

	private String removeLastDot(String toName) {
		return toName.substring(0, toName.length()-1);
	}

	private void walkTreeToGetNames(CommonTree treeNode) {
		for (int i = 0; i < treeNode.getChildCount(); i++) {
			CommonTree child = (CommonTree)treeNode.getChild(i);
			switch (child.getType()) {
			case CSharpParser.NAMESPACE_OR_TYPE_NAME:
			case CSharpParser.NAMESPACE_OR_TYPE_PART:	
				String name = child.getFirstChildWithType(CSharpParser.IDENTIFIER).getText();
				this.toNames.add(name);
				break;
			}
			walkTreeToGetNames(child);
		}
	}

	@Override
	void saveInvocationToDomain() {
		modelService.createConstructorInvocation(from, to, lineNumber, invocationName, belongsToMethod, nameOfInstance);
	}
}
