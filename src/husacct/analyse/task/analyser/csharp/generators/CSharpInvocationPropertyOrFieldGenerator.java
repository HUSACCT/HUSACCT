package husacct.analyse.task.analyser.csharp.generators;

import org.antlr.runtime.tree.CommonTree;

import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import husacct.analyse.task.analyser.csharp.generators.AbstractCSharpInvocationGenerator;

public class CSharpInvocationPropertyOrFieldGenerator extends AbstractCSharpInvocationGenerator {

	public CSharpInvocationPropertyOrFieldGenerator(String packageAndClassName) {
		super(packageAndClassName);
	}
	
	public void generatePropertyOrFieldInvocToDomain(CommonTree tree,String belongsToMethod) {
        this.belongsToMethod = belongsToMethod;	
        
        createPropertyOrFieldInvocationDetails(tree);
        saveInvocationToDomain();
	}

	private void createPropertyOrFieldInvocationDetails(CommonTree treeNode) {
		CommonTree toChild = (CommonTree)treeNode.getFirstChildWithType(CSharpParser.SIMPLE_NAME);
		this.to = toChild.getFirstChildWithType(CSharpParser.IDENTIFIER).getText();
		this.invocationName = treeNode.getFirstChildWithType(CSharpParser.IDENTIFIER).getText();
		this.nameOfInstance = toChild.getFirstChildWithType(CSharpParser.IDENTIFIER).getText();
		this.lineNumber = toChild.getLine();
	}

	@Override
	void saveInvocationToDomain() {
		modelService.createPropertyOrFieldInvocation(from, to, lineNumber, invocationName, belongsToMethod, nameOfInstance);
	}
}
