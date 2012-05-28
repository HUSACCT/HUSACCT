package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.JavaParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.apache.log4j.Logger;

public class JavaParameterGenerator extends JavaGenerator {

	private String belongsToMethod; //dit moet de unique name van de method worden dus met signature
	private String belongsToClass;
	private int lineNumber;
	
	private String declareType;
	private List<String> declareTypes = new ArrayList<String>();
	private String declareName;
	private String uniqueName;
	
	private Logger logger = Logger.getLogger(JavaParameterGenerator.class);
	private String signature = "";
	private boolean nameFound = false;
	private boolean declareTypeFound = false;
	
	private ArrayList<String> currentTypes;
	private ArrayList<Object> saveQueue;
	
	public String generateParameterObjects(Tree tree, String belongsToMethod, String belongsToClass){
		//returns the signature, which MethodGenerator uses

		this.saveQueue = new ArrayList<Object>();
		this.currentTypes = new ArrayList<String>();
		
		this.belongsToMethod = belongsToMethod;
		this.belongsToClass = belongsToClass;
		lineNumber = tree.getLine();
		
		DelegateParametersFromTree(tree);	

		writeParameterToDomain();
		return signature;
	}

	private void DelegateParametersFromTree(Tree tree) {		
		for(int i = 0; i < tree.getChildCount(); i++){
			CommonTree child = (CommonTree) tree.getChild(i);
			int treeType = child.getType();
			if(treeType == JavaParser.FORMAL_PARAM_STD_DECL ){
				getAttributeName(child);
				getParameterAttributes(child, 1);
				if(this.nameFound && this.declareTypeFound){
					this.addToQueue();
				}
				deleteTreeChild(child);
				nameFound = false;
				declareTypeFound = false;
			}
			DelegateParametersFromTree(child);
		}
	}


	private void getAttributeName(Tree tree){
		CommonTree attributeTree = (CommonTree) tree;
		Tree attributeNameTree = attributeTree.getFirstChildWithType(JavaParser.IDENT);
		try{
			this.declareName = attributeNameTree.getText();
			this.nameFound = true;			
		} catch (Exception e) { }		
	}
	
	public String getAttributeType(CommonTree tree){
		String attributeType = "";
		attributeType = getAttributeRecursive(tree);
		attributeType = attributeType.replace("<.", "<");
		attributeType = attributeType.substring(1);
		return attributeType;
	}
	
	public String getAttributeRecursive(CommonTree tree){
		String attributeType = "";
				
		switch(tree.getType()){
			case JavaParser.QUALIFIED_TYPE_IDENT:
				String type = getType(tree);
				if(declareType == null || declareType.equals("")){
					declareType = type;
				} else {
					currentTypes.add(type);
				}
				attributeType += "." + type;
				int childcount = tree.getChildCount();
				for(int currentChild = 0; currentChild < childcount; currentChild++){
					CommonTree childNode = (CommonTree) tree.getChild(currentChild);
					if(childNode.getChildCount() > 0){
						attributeType += getAttributeRecursive((CommonTree) childNode.getFirstChildWithType(JavaParser.GENERIC_TYPE_ARG_LIST));
					}
				}
				break;
			case JavaParser.GENERIC_TYPE_ARG_LIST:
				CommonTree typeNode = (CommonTree) tree.getFirstChildWithType(JavaParser.TYPE);
				CommonTree qualifiedNode = (CommonTree) typeNode.getFirstChildWithType(JavaParser.QUALIFIED_TYPE_IDENT);
				attributeType += "<";
				attributeType += getAttributeRecursive(qualifiedNode);
				attributeType += ">";
				break;
			default:
				break;
		}
		return attributeType;
	}
	
	public String getType(CommonTree tree){
		String attributeType = "";
		int childcount = tree.getChildCount();
		for(int i = 0; i < childcount; i++){
			Tree identTree = tree.getChild(i);
			if(identTree.getType() == JavaParser.IDENT){
				attributeType += attributeType.equals("") ? "" : ".";
				attributeType += identTree.getText();
			}
		}
		return attributeType;
	}
	

	private String getParameterAttributes(Tree tree, int indent) {
		int childrenCount = tree.getChildCount();
		for(int i = 0; i < childrenCount; i++){
			CommonTree currentChild = (CommonTree) tree.getChild(i);
			
			if(currentChild.getType() == JavaParser.QUALIFIED_TYPE_IDENT){
				getAttributeType(currentChild);
				this.declareTypeFound = true;
				this.signature += !this.signature.equals("") ? "," : "";
				this.signature += this.declareType;
			} else {
				getParameterAttributes(currentChild, indent + 1);
			}
		}
		return "";
	}
	
	
	private void deleteTreeChild(Tree treeNode){ 
        for (int child = 0 ; child < treeNode.getChildCount();){ 
            treeNode.deleteChild(treeNode.getChild(child).getChildIndex()); 
        } 
    } 
	
	private void addToQueue(){
		ArrayList<Object> myParam = new ArrayList<Object>();
		myParam.add(this.declareType);
		myParam.add(this.declareName);
		myParam.add(this.currentTypes);
		saveQueue.add(myParam);
		this.declareType = null;
		this.declareName = null;
		this.currentTypes = new ArrayList<String>();
	}
	
	private void writeParameterToDomain() {
		for(Object object : saveQueue){
			ArrayList<Object> currentParam = (ArrayList<Object>) object;
			String type = (String) currentParam.get(0);
			String name = (String) currentParam.get(1);
			ArrayList<String> types = (ArrayList<String>) currentParam.get(2);
			this.uniqueName = this.belongsToClass + "." + this.belongsToMethod + "(" + this.signature + ")." + name;
			String belongsToMethodToPassThrough = this.belongsToClass + "." + this.belongsToMethod + "(" + this.signature + ")";
			modelService.createParameter(name, uniqueName, type, belongsToClass, lineNumber, belongsToMethodToPassThrough, types);
		}
	}

}
