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
	private boolean constructorInMethodInvocationFound = false;
	private boolean allIdents = true;
	
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
		boolean constructorFound = false;
		if (tree.getType() == JavaParser.CLASS_CONSTRUCTOR_CALL){
			createConstructorInvocationDetailsWhenFoundClassConstructorCall((CommonTree) tree);
			constructorFound = true;
		}
		if (constructorFound == false){
			for (int i = 0; i < tree.getChildCount(); i++) {
				Tree child = tree.getChild(i);
				int treeType = child.getType();
	
				if (treeType == JavaParser.CLASS_CONSTRUCTOR_CALL) {
					createConstructorInvocationDetailsWhenFoundClassConstructorCall((CommonTree) child);
				}
				createConstructorInvocationDetails(child);
			}
		}
	}
	
	private void deleteTreeChild(Tree treeNode){ 
        for (int child = 0 ; child < treeNode.getChildCount();){ 
            treeNode.deleteChild(treeNode.getChild(child).getChildIndex()); 
        } 
    } 

//	private CommonTree getFirstChildThrougTree(Tree tree,int classConstructorCall) {
//		for (int i = 0; i < tree.getChildCount(); i++) {
//			Tree child = tree.getChild(i);
//			int treeType = child.getType();
//			if (treeType == classConstructorCall){
//				return (CommonTree) tree.getChild(i);
//			}
//			getFirstChildThrougTree(child, classConstructorCall);
//		}
//		
//		return null;
//	}

	private void createConstructorInvocationDetailsWhenFoundClassConstructorCall(CommonTree firstChildClassConstructorCall) {
			Tree child = firstChildClassConstructorCall.getChild(0);	
			this.to = "";
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

	public void generateMethodInvocToModel(CommonTree treeNode,
			String belongsToMethod) {
		invocationNameFound = false;
		constructorInMethodInvocationFound = false;
		allIdents = true;
		type = "invocMethod";
		this.belongsToMethod = belongsToMethod;
		lineNumber = treeNode.getLine();

		
		if (treeNode.getChildCount() > 0)
			if (treeNode.getChild(0).getChildCount() > 0){
				if (treeNode.getChild(0).getChild(0).getType() != JavaParser.METHOD_CALL) {
					if (TreeHasConstructorInvocation(treeNode)) {
						createMethodOrPropertyFieldInvocationDetailsWhenConstructorIsFound(treeNode);
					} else {
						createMethodOrPropertyFieldInvocationDetails(treeNode);
					}
					createMethodInvocationDomainObject();
				}
		}
	}
	
	private boolean TreeHasConstructorInvocation(CommonTree treeNode) {
		checkIfTreeHasConstructorInvocation(treeNode);
		return constructorInMethodInvocationFound;
	}
	private void checkIfTreeHasConstructorInvocation(Tree tree) {
		for(int i = 0; i < tree.getChildCount(); i++){
			Tree child = tree.getChild(i);
			int treeType = child.getType();
			if(treeType == JavaParser.ARGUMENT_LIST){ //cut the loop, otherwise another constructor might be found
				break;
			}
			if(treeType == JavaParser.CLASS_CONSTRUCTOR_CALL){
				constructorInMethodInvocationFound = true;
			}
			
			checkIfTreeHasConstructorInvocation(child);
		}
	}

	private boolean foundAllMethodInvocInfo = false;

	private void createMethodOrPropertyFieldInvocationDetailsWhenConstructorIsFound(
			Tree tree) {
		if (tree != null) {
			if (foundAllMethodInvocInfo == false) {
				for (int i = 0; i < tree.getChildCount(); i++) {
					CommonTree child = (CommonTree) tree.getChild(i);
					if (child.getType() == JavaParser.DOT) {

						for (int iChild = 0; iChild < child.getChildCount(); iChild++) {
							if (child.getChild(iChild).getType() != JavaParser.IDENT) {
								allIdents = false;
								break;
							}
						}
						if (allIdents == false) {
							invocationName = child.getChild(1).getText();
							this.lineNumber = child.getLine();
						} else {
							this.to = child.getChild(0).getText();
							invocationName = child.getChild(1).getText();
							break;
						}
					}
					if (child.getType() == JavaParser.QUALIFIED_TYPE_IDENT && (allIdents == false)) {
						if (child.getChildCount() > 1) {
							for (int i2 = 0; i2 < child.getChildCount(); i2++) {
								if (i2 == child.getChildCount() - 1) {
									this.to += child.getChild(i2).toString();
								} else {
									this.to += child.getChild(i2).toString()+ ".";
								}
							}
						} else {
							this.to = child.getChild(i).getText();
						}
						foundAllMethodInvocInfo = true;
					}
					createMethodOrPropertyFieldInvocationDetailsWhenConstructorIsFound(child);
				}
			}
		}

	}

	private void createMethodOrPropertyFieldInvocationDetails(Tree tree){
		if (tree != null) {
			for (int i = 0; i < tree.getChildCount(); i++) {
				CommonTree treeNode = (CommonTree) tree.getChild(i);
				if(treeNode.getType() == JavaParser.IDENT && i == 0 && treeNode.getParent().getType() != JavaParser.VAR_DECLARATOR){
					setToAndNameOfInstance(treeNode.getText());
					//nameOfInstance = to;
					
				}
				if(treeNode.getType() == JavaParser.IDENT && i == 1 && invocationNameFound == false){
					invocationName = treeNode.getText();
					invocationNameFound = true;
					this.lineNumber = treeNode.getLine();
				}
//				if(treeNode.getType() == JavaParser.CLASS_CONSTRUCTOR_CALL){
//					generateConstructorInvocToModel((CommonTree) tree, belongsToMethod);
//				}
				createMethodOrPropertyFieldInvocationDetails(tree.getChild(i));
			}
		}
	}

	private void setToAndNameOfInstance(String thisTo){
		if(this.to == null || this.to.equals("")){
			to = thisTo;
			nameOfInstance = to;
		}
	}
	


	private void createMethodInvocationDomainObject() {
		modelService.createMethodInvocation(from, to, lineNumber, invocationName, belongsToMethod, nameOfInstance);
	}

	public void generatePropertyOrFieldInvocToModel(CommonTree treeNode, String belongsToMethod) {
		invocationNameFound = false;
		allIdents = true;
		type = "accessPropertyOrField";
		this.belongsToMethod = belongsToMethod;
		
		if(TreeHasConstructorInvocation(treeNode)){
			createMethodOrPropertyFieldInvocationDetailsWhenConstructorIsFound(treeNode);
//			createPropertyOrFieldInvocationDomainObject();
//			if(treeNode.getType() == JavaParser.EXPR ){
//				generateConstructorInvocToModel((CommonTree) treeNode.getChild(0), belongsToMethod);
//			} 
//			else {
//				generateConstructorInvocToModel((CommonTree) treeNode, belongsToMethod);
//			}
		} else {
			
			createMethodOrPropertyFieldInvocationDetails(treeNode);
			createPropertyOrFieldInvocationDomainObject();
		}		
		createPropertyOrFieldInvocationDomainObject();
	}

	

	private void createPropertyOrFieldInvocationDomainObject() {	
		modelService.createPropertyOrFieldInvocation(from, to, lineNumber, invocationName, belongsToMethod, nameOfInstance);
	}
}
