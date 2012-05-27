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
	private boolean foundAllMethodInvocInfo = false;
	private boolean allIdents = true;
	
	private Logger logger = Logger.getLogger(JavaInvocationGenerator.class);
	
	public JavaInvocationGenerator(String uniqueClassName){
		from = uniqueClassName;
	}
	
	public void generateConstructorInvocToDomain(CommonTree commonTree, String belongsToMethod ) {
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

	private void createConstructorInvocationDetailsWhenFoundClassConstructorCall(CommonTree firstChildClassConstructorCall) {
			Tree child = firstChildClassConstructorCall.getChild(0);	
			this.to = "";
			if(child.getType() != JavaParser.QUALIFIED_TYPE_IDENT){
				this.to = child.getText();				
			}else{
				setTo((CommonTree) child);
			}
			this.lineNumber = child.getLine();
	}

	private void createConstructorInvocationDomainObject(){
		modelService.createConstructorInvocation(from, to, lineNumber, invocationName, belongsToMethod, nameOfInstance);
	}

	public void generateMethodInvocToDomain(CommonTree treeNode, String belongsToMethod) {
		invocationNameFound = false;
		constructorInMethodInvocationFound = false;
		allIdents = true;
		type = "invocMethod";
		this.belongsToMethod = belongsToMethod;
		lineNumber = treeNode.getLine();

		checkKindOfTreeAndDelegate(treeNode);
		
	}
	
	private void checkKindOfTreeAndDelegate(CommonTree treeNode) {
		if (treeNode.getChildCount() > 0){
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
	}

	private boolean TreeHasConstructorInvocation(CommonTree treeNode) {
		checkIfTreeHasConstructorInvocation(treeNode);
		return constructorInMethodInvocationFound;
	}
	
	private void checkIfTreeHasConstructorInvocation(Tree tree) {
		for(int childCount = 0; childCount < tree.getChildCount(); childCount++){
			Tree child = tree.getChild(childCount);
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
	
	private void createMethodInvocationDomainObject() {
		modelService.createMethodInvocation(from, to, lineNumber, invocationName, belongsToMethod, nameOfInstance);
	}

	private void createMethodOrPropertyFieldInvocationDetailsWhenConstructorIsFound(Tree tree) {
		if (tree != null) {
			if (foundAllMethodInvocInfo == false) {
				for (int childCount = 0; childCount < tree.getChildCount(); childCount++) {
					CommonTree child = (CommonTree) tree.getChild(childCount);
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
						setTo(child);
						foundAllMethodInvocInfo = true;
					}
					createMethodOrPropertyFieldInvocationDetailsWhenConstructorIsFound(child);
				}
			}
		}
	}
	
	private void setTo(CommonTree tree){
		if(tree.getChildCount() > 1){
			for(int childCount=0; childCount<tree.getChildCount(); childCount++){
				if (childCount == tree.getChildCount() - 1){	
					this.to += tree.getChild(childCount).toString();
				}else{
					this.to += tree.getChild(childCount).toString() + ".";
				}
			}
		} else {
			this.to = tree.getChild(0).getText();
		}

	}

	private void createMethodOrPropertyFieldInvocationDetails(Tree tree){
		if (tree != null) {
			for (int childCount = 0; childCount < tree.getChildCount(); childCount++) {
				CommonTree treeNode = (CommonTree) tree.getChild(childCount);
				if(treeNode.getType() == JavaParser.IDENT && childCount == 0 && treeNode.getParent().getType() != JavaParser.VAR_DECLARATOR){
					setToAndNameOfInstance(treeNode.getText());
				}
				if(treeNode.getType() == JavaParser.IDENT && childCount == 1 && invocationNameFound == false){
					invocationName = treeNode.getText();
					invocationNameFound = true;
					this.lineNumber = treeNode.getLine();
				}
				createMethodOrPropertyFieldInvocationDetails(tree.getChild(childCount));
			}
		}
	}

	private void setToAndNameOfInstance(String thisTo){
		if(this.to == null || this.to.equals("")){
			to = thisTo;
			nameOfInstance = to;
		}
	}
	
	public void generatePropertyOrFieldInvocToDomain(CommonTree treeNode, String belongsToMethod) {
		invocationNameFound = false;
		allIdents = true;
		type = "accessPropertyOrField";
		this.belongsToMethod = belongsToMethod;
		
		if(TreeHasConstructorInvocation(treeNode)){
			createMethodOrPropertyFieldInvocationDetailsWhenConstructorIsFound(treeNode);
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
