package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.JavaParser;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class JavaInvocationGenerator extends JavaGenerator {

	private String type; //invocConstructor, accessPropertyOrField of invocMethod
	private String from;
	private String to;
	private int lineNumber;
	private String invocationName;
	private String belongsToMethod;
	
	private boolean invocationNameFound;
	
	public JavaInvocationGenerator(String uniqueClassName){
		from = uniqueClassName;
	}
	
	public String generateConstructorInvocToModel(CommonTree commonTree, String belongsToMethod ) {
		type = "invocConstructor";
		invocationName = "Constructor";
		this.belongsToMethod = belongsToMethod;
		createConstructorInvocationDetails(commonTree);
		createConstructorInvocationDomainObject();
		return to;
	}
	
	private void createConstructorInvocationDetails(Tree tree) {
		if (tree != null) {
			for (int i = 0; i < tree.getChildCount(); i++) {
				if(tree.getType() == JavaParser.QUALIFIED_TYPE_IDENT ){
					to = tree.getChild(i).getText();
					lineNumber = tree.getLine();
				}
				createConstructorInvocationDetails(tree.getChild(i));
			}
		}
	}
	
	private void createConstructorInvocationDomainObject(){
		modelService.createConstructorInvocation(type, from, to, lineNumber, invocationName, belongsToMethod);
	}

	public void generateMethodInvocToModel(CommonTree treeNode, String belongsToMethod) {
		invocationNameFound = false;
		type = "invocMethod";
		this.belongsToMethod = belongsToMethod;
		createMethodOrPropertyFieldInvocationDetails(treeNode);
		createMethodInvocationDomainObject();
	}
	
	private void createMethodOrPropertyFieldInvocationDetails(Tree tree){
		if (tree != null) {
			for (int i = 0; i < tree.getChildCount(); i++) {
				Tree treeNode = tree.getChild(i);
				if(treeNode.getType() == JavaParser.IDENT && i == 0){
					to = treeNode.getText();
					lineNumber = treeNode.getLine();
				}
				if(treeNode.getType() == JavaParser.IDENT && i == 1 && invocationNameFound == false){
					invocationName = treeNode.getText();
					invocationNameFound = true;
				}
				createMethodOrPropertyFieldInvocationDetails(tree.getChild(i));
			}
		}
	}
	

	private void createMethodInvocationDomainObject() {
		modelService.createMethodInvocation(type, from, to, lineNumber, invocationName, belongsToMethod);
		
	}

	public void generatePropertyOrFieldInvocToModel(CommonTree treeNode, String belongsToMethod) {
		invocationNameFound = false;
		type = "accessPropertyOrField";
		this.belongsToMethod = belongsToMethod;
		createMethodOrPropertyFieldInvocationDetails(treeNode);
		createPropertyOrFieldInvocationDomainObject();
		
	}

	private void createPropertyOrFieldInvocationDomainObject() {
		modelService.createPropertyOrFieldInvocation(type, from, to, lineNumber, invocationName, belongsToMethod);
	}
}
