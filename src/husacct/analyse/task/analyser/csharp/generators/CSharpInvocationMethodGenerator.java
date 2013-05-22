package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;

import org.antlr.runtime.tree.CommonTree;

public class CSharpInvocationMethodGenerator extends AbstractCSharpInvocationGenerator {
	
	public CSharpInvocationMethodGenerator(String packageAndClassName) {
		super(packageAndClassName);
	}

	public void generateMethodInvocToDomain(CommonTree treeNode, String belongsToMethod) {
		this.belongsToMethod = belongsToMethod;
		lineNumber = treeNode.getLine();
		
		findMethodInvocation(treeNode);
	}

	private void findMethodInvocation(CommonTree treeNode) {
		if (treeNode.getChildCount() <= 0){
			return;
		}
		for (int i = 0; i < treeNode.getChildCount(); i++) {
			CommonTree child = (CommonTree)treeNode.getChild(i);
			switch (child.getType()) {
			case CSharpParser.MEMBER_ACCESS:
				createMethodInvocationDetails(child);
				saveInvocationToDomain();
			}
		}
	}

	private void createMethodInvocationDetails(CommonTree child) {
		CommonTree toChild = (CommonTree)child.getFirstChildWithType(CSharpParser.SIMPLE_NAME);
		this.to = toChild.getFirstChildWithType(CSharpParser.IDENTIFIER).getText();
		this.invocationName = child.getFirstChildWithType(CSharpParser.IDENTIFIER).getText();
		this.nameOfInstance = toChild.getFirstChildWithType(CSharpParser.IDENTIFIER).getText();	
	}

	@Override
	void saveInvocationToDomain() {
		modelService.createMethodInvocation(from, to, lineNumber, invocationName, belongsToMethod, nameOfInstance);
	}
}
