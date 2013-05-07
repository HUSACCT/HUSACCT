package husacct.analyse.task.analyser.csharp.generators;



import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import static husacct.analyse.task.analyser.csharp.generators.CSharpGeneratorToolkit.*;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.apache.log4j.Logger;

public class CSharpInvocationGenerator extends CSharpGenerator {
	private String from = "";
	private String to = "";
	private int lineNumber;
	private String invocationName = "";
	private String belongsToMethod = "";
	private String nameOfInstance = "";
	private boolean invocationNameFound;
	private boolean constructorInMethodInvocationFound = false;
	private boolean foundAllMethodInvocInfo = false;
	private boolean allIdents = true;
	
	
	public CSharpInvocationGenerator(String uniqueClassName) {
		from = uniqueClassName;
	}

	public void generateConstructorInvocToDomain(CommonTree treeNode, String belongsToMethod) {
		invocationName = "Constructor";
		this.belongsToMethod = belongsToMethod;
		createConstructorInvocationDetails(treeNode);
		createConstructorInvocationDomainObject();
	}
	
	public void generateMethodInvocToDomain(CommonTree treeNode, String belongsToMethod) {
		invocationNameFound = false;
		constructorInMethodInvocationFound = false;
		allIdents = true;
		this.belongsToMethod = belongsToMethod;
		lineNumber = treeNode.getLine();
		// TODO
	}

	public void generatePropertyOrFieldInvocToDomain(CommonTree tree,String belongsToMethod) {
		// TODO Auto-generated method stub	
	}

	private void createConstructorInvocationDetails(CommonTree tree) {
		boolean constructorFound = hasConstructorCall(tree);
		if (constructorFound){
			createConstructorInvocationDetailsWhenFoundClassConstructorCall((CommonTree) tree);
		}
		else {
			int childcount = tree.getChildCount();
			for (int i = 0; i < childcount; i++) {
				CommonTree child =  (CommonTree) tree.getChild(i);

				if (hasConstructorCall(child)) { 
					createConstructorInvocationDetailsWhenFoundClassConstructorCall(child);
				}
				createConstructorInvocationDetails(child);
			}
		}
	}
		
	
	private CommonTree getDetailsForConstructorInvocationTree(CommonTree tree) {
		return walkTree(tree, CSharpParser.TYPE, CSharpParser.NAMESPACE_OR_TYPE_NAME, CSharpParser.IDENTIFIER);
	}

	private void createConstructorInvocationDetailsWhenFoundClassConstructorCall(CommonTree tree) {	
		CommonTree constructorInvocationDetailsTree = getDetailsForConstructorInvocationTree(tree);
		if (constructorInvocationDetailsTree != null) {
			this.to = constructorInvocationDetailsTree.getText();
			this.lineNumber = constructorInvocationDetailsTree.getLine();
		}
	}
	
	private void createConstructorInvocationDomainObject() {
		modelService.createConstructorInvocation(from, to, lineNumber, invocationName, belongsToMethod, nameOfInstance);
	}
	
	private boolean hasConstructorCall(CommonTree tree) {
		return tree.getType() == CSharpParser.OBJECT_CREATION_EXPRESSION;
	}
}
