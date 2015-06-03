package husacct.analyse.task.analyser.csharp.generators;

import static husacct.analyse.task.analyser.csharp.generators.CSharpGeneratorToolkit.*;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class CSharpPropertyGenerator extends CSharpGenerator {
	private String belongsToClass = "";
	private String propertyName = "";
	private CSharpBlockScopeGenerator csBlockScopeGenerator;

	public void generateProperyToDomain(CommonTree propertyTree, String belongsToClass) {
		this.belongsToClass = belongsToClass;
		
		setPropertyName(propertyTree);
		findGetterAndSetter(propertyTree);
	}
	
	private void setPropertyName(CommonTree propertyTree) {
		CommonTree propertyNameTree = getFirstDescendantWithType(propertyTree, CSharpParser.MEMBER_NAME);
		
		if (propertyNameTree != null) {
			this.propertyName = getTypeNameAndParts(propertyNameTree);	
		}
	}
	
	private void findGetterAndSetter(Tree tree) {
		for (int i = 0; i < tree.getChildCount(); i++) {
			Tree child = tree.getChild(i);
			if (child.getType() == CSharpParser.IDENTIFIER) {
				String type = child.getText();
				if (type.equals("get")) {
					CommonTree propertyGetBlock = (CommonTree)tree.getChild(i + 1);
					setPropertyGetBlock(propertyGetBlock);
				}
				else if (type.equals("set")) {
					CommonTree propertySetBlock = (CommonTree)tree.getChild(i + 1);
					setPropertySetBlock(propertySetBlock);
				}
			}
			findGetterAndSetter(child);
		}
	}
	
	private void setPropertySetBlock(CommonTree propertySetBlock) {
		String methodName = "set" + propertyName;
		delegateBlockToBlockScopeGenerator(propertySetBlock, methodName);
	}

	private void setPropertyGetBlock(CommonTree propertyGetBlock) {
		String methodName = "get" + propertyName;
		delegateBlockToBlockScopeGenerator(propertyGetBlock, methodName);
	}

	private void delegateBlockToBlockScopeGenerator(CommonTree tree, String methodName) {
		csBlockScopeGenerator = new CSharpBlockScopeGenerator();
		csBlockScopeGenerator.walkThroughBlockScope(tree, belongsToClass, methodName);
	}
}
