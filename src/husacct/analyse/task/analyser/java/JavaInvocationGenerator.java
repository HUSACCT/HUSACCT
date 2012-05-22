package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.JavaParser;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.apache.log4j.Logger;

public class JavaInvocationGenerator extends JavaGenerator {

	private String type = ""; //invocConstructor, accessPropertyOrField of invocMethod
	private String from = "";
	private String to = "";
	private int lineNumber;
	private String invocationName = "";
	private String belongsToMethod = "";
	private String nameOfInstance = "";
	
	private boolean invocationNameFound;
	
	private Logger logger = Logger.getLogger(JavaInvocationGenerator.class);
	
	public JavaInvocationGenerator(String uniqueClassName){
		from = uniqueClassName;
	}
	
	public void generateConstructorInvocToModel(CommonTree commonTree, String belongsToMethod ) {
		type = "invocConstructor";
		invocationName = "Constructor";
		this.belongsToMethod = belongsToMethod;
		createConstructorInvocationDetails(commonTree);
		createConstructorInvocationDomainObject();
	}
	
	private void createConstructorInvocationDetails(Tree tree) {
		//expects: '.'
		if (tree != null) {
			if (tree.getType() == JavaParser.METHOD_CALL){
				if (tree.getChild(0).getType() == JavaParser.DOT){
					createConstructorInvocationDetails(tree.getChild(0));
				}
			}
			else{
				CommonTree firstChildClassConstructorCall = (CommonTree) tree;
				
				if(tree.getType() != JavaParser.CLASS_CONSTRUCTOR_CALL){
					firstChildClassConstructorCall =  getFirstChildThrougTree(tree, JavaParser.CLASS_CONSTRUCTOR_CALL);
				}
				if (firstChildClassConstructorCall != null){
					createConstructorInvocationDetailsWhenFoundClassConstructorCall(firstChildClassConstructorCall);
				}
				else{
					logger.warn("Couldn't handle Constructor Call Invocation Element in class: " + to);
				}
			}
			
		}
		
	}
	
	private CommonTree getFirstChildThrougTree(Tree tree,int classConstructorCall) {
		for (int i = 0; i < tree.getChildCount(); i++) {
			if (tree.getChild(i).getType() == classConstructorCall){
				return (CommonTree) tree.getChild(i);
			}
			getFirstChildThrougTree(tree.getChild(i), classConstructorCall);
		}
		return null;
	}

	private void createConstructorInvocationDetailsWhenFoundClassConstructorCall(CommonTree firstChildClassConstructorCall) {
			Tree child = firstChildClassConstructorCall.getChild(0);			
			if(child.getType() != JavaParser.QUALIFIED_TYPE_IDENT){
				this.to = child.getText();				
			}else{
				if(child.getChildCount() > 1){
					for(int i=0; i<child.getChildCount(); i++){
						if (i == child.getChildCount() - 1){	
							this.to += child.getChild(i).toString();
						}else{
							this.to += child.getChild(i).toString() + ".";
						}
					}
				} else {
					this.to = child.getChild(0).getText();
				}
			}
			this.lineNumber = child.getLine();
		
	}

	private void createConstructorInvocationDomainObject(){
		modelService.createConstructorInvocation(from, to, lineNumber, invocationName, belongsToMethod, nameOfInstance);
	}

	public void generateMethodInvocToModel(CommonTree treeNode, String belongsToMethod) {
		invocationNameFound = false;
		type = "invocMethod";
		this.belongsToMethod = belongsToMethod;
		lineNumber = treeNode.getLine();
		if(treeHasConstructorInvocation(treeNode)){
			createMethodOrPropertyFieldInvocationDetailsWhenConstructorIsFound(treeNode);
			if(treeNode.getType() == JavaParser.EXPR){
				generateConstructorInvocToModel((CommonTree) treeNode.getChild(0), belongsToMethod);
			} 
			else {
				generateConstructorInvocToModel((CommonTree) treeNode, belongsToMethod);
			}
			
		} else {
			createMethodOrPropertyFieldInvocationDetails(treeNode);
		}	
		createMethodInvocationDomainObject();
	}
	
	private boolean treeHasConstructorInvocation(Tree tree) {
		for (int i = 0; i < tree.getChildCount(); i++) {
			if(tree.getType() == JavaParser.CLASS_CONSTRUCTOR_CALL){
				return true;
			}
			
			return treeHasConstructorInvocation(tree.getChild(i));
			
		}
		return false;
	}

	private void createMethodOrPropertyFieldInvocationDetailsWhenConstructorIsFound(Tree tree) {
		if (tree != null){
			for (int i = 0; i < tree.getChildCount(); i++) {
				CommonTree child = (CommonTree) tree.getChild(i);
				if (child.getType() == JavaParser.DOT){ 
					invocationName = child.getChild(1).getText();
					this.lineNumber = child.getLine();
					child.deleteChild(1);
				}
				if (child.getType() == JavaParser.QUALIFIED_TYPE_IDENT){
					if(child.getChildCount() > 1){
						for(int i2=0; i2<child.getChildCount(); i2++){
							if (i2 == child.getChildCount() - 1){
								this.to += child.getChild(i2).toString();
							}else{
								this.to += child.getChild(i2).toString() + ".";
							}
							
						}
					}
					else this.to = child.getChild(i).getText();
				}
				createMethodOrPropertyFieldInvocationDetailsWhenConstructorIsFound(child);
			}
		}
		
	}

	private void createMethodOrPropertyFieldInvocationDetails(Tree tree){
		if (tree != null) {
			for (int i = 0; i < tree.getChildCount(); i++) {
				CommonTree treeNode = (CommonTree) tree.getChild(i);
				if(treeNode.getType() == JavaParser.IDENT && i == 0){
					to = treeNode.getText();
					nameOfInstance = to;
					
				}
				if(treeNode.getType() == JavaParser.IDENT && i == 1 && invocationNameFound == false){
					invocationName = treeNode.getText();
					invocationNameFound = true;
					this.lineNumber = treeNode.getLine();
				}
				if(treeNode.getType() == JavaParser.CLASS_CONSTRUCTOR_CALL){
					generateConstructorInvocToModel((CommonTree) tree, belongsToMethod);
				}
				createMethodOrPropertyFieldInvocationDetails(tree.getChild(i));
			}
		}
	}


	


	private void createMethodInvocationDomainObject() {
		modelService.createMethodInvocation(from, to, lineNumber, invocationName, belongsToMethod, nameOfInstance);
		
	}

	public void generatePropertyOrFieldInvocToModel(CommonTree treeNode, String belongsToMethod) {
		invocationNameFound = false;
		type = "accessPropertyOrField";
		this.belongsToMethod = belongsToMethod;
		
		if(treeHasConstructorInvocation(treeNode)){
			createMethodOrPropertyFieldInvocationDetailsWhenConstructorIsFound(treeNode);
			createPropertyOrFieldInvocationDomainObject();
			if(treeNode.getType() == JavaParser.EXPR){
				generateConstructorInvocToModel((CommonTree) treeNode.getChild(0), belongsToMethod);
			} 
			else {
				generateConstructorInvocToModel((CommonTree) treeNode, belongsToMethod);
			}
		} else {
			
			createMethodOrPropertyFieldInvocationDetails(treeNode);
			createPropertyOrFieldInvocationDomainObject();
		}		
	}

	private void createPropertyOrFieldInvocationDomainObject() {	
		modelService.createPropertyOrFieldInvocation(from, to, lineNumber, invocationName, belongsToMethod, nameOfInstance);
	}
}
