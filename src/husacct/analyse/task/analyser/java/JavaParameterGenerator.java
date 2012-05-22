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
	
	private ArrayList<Object> saveQueue;
	
	public String generateParameterObjects(Tree tree, String belongsToMethod, String belongsToClass){
		//returns the signature, which MethodGenerator uses

		this.saveQueue = new ArrayList<Object>();
		
		this.belongsToMethod = belongsToMethod;
		this.belongsToClass = belongsToClass;
		lineNumber = tree.getLine();
		
		DelegateParametersFromTree(tree);	
//		correctBelongsToMethod();
		writeParameterToDomain();
		return signature;
	}



//	private void correctBelongsToMethod() {
//		String method = this.belongsToMethod + "(";
//		for (String declaredType : decalareTypes){
//			method += declareType;
//		}
//		
//	}



	private void DelegateParametersFromTree(Tree tree) {
		for(int i = 0; i < tree.getChildCount(); i++){
			CommonTree child = (CommonTree) tree.getChild(i);
			int treeType = child.getType();
			if(treeType == JavaParser.FORMAL_PARAM_STD_DECL ){
				getAttributeName(child);
				getParameterAttributes(child, 1);
				if(this.nameFound && this.declareTypeFound){
//					writeParameterToDomain();
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
		attributeType = attributeType.replace("<,", "<");
		attributeType = attributeType.substring(1);
		return attributeType;
	}
	
	public String getAttributeRecursive(CommonTree tree){
		String attributeType = "";
		int childCount = tree.getChildCount();
		for(int i = 0; i < childCount; i++){
			CommonTree childTree = (CommonTree) tree.getChild(i);
			
			switch(childTree.getType()){
				case JavaParser.IDENT:
					attributeType += "." + childTree.getText();
					if(childTree.getChildCount() > 0){
						attributeType += getAttributeRecursive(childTree);
					}
					break;
				case JavaParser.GENERIC_TYPE_ARG_LIST:
					attributeType += "<";
					attributeType += getAttributeRecursive(childTree);
					attributeType += ">";
					break;
				default:
					attributeType += getAttributeRecursive(childTree);
				
			}
		}
		return attributeType;
	}

	private String getParameterAttributes(Tree tree, int indent) {
		int childrenCount = tree.getChildCount();
		for(int i = 0; i < childrenCount; i++){
			CommonTree currentChild = (CommonTree) tree.getChild(i);
			
			if(currentChild.getType() == JavaParser.QUALIFIED_TYPE_IDENT){
				this.declareType = getAttributeType(currentChild);
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
		saveQueue.add(myParam);
	}
	
	private void writeParameterToDomain() {
		for(Object object : saveQueue){
			ArrayList<Object> currentParam = (ArrayList<Object>) object;
			String type = (String) currentParam.get(0);
			String name = (String) currentParam.get(1);
			this.uniqueName = this.belongsToClass + "." + this.belongsToMethod + "(" + this.signature + ")." + name;
			String belongsToMethodToPassThrough = this.belongsToClass + "." + this.belongsToMethod + "(" + this.signature + ")";
			modelService.createParameter(name, uniqueName, type, belongsToClass, lineNumber, belongsToMethodToPassThrough, type);
		}
	}
//	
//	private String getBelongsToMethodString(String completeName){
//		System.err.println(completeName);
//		String belongsToMethod = "";
//		String[] parts = completeName.split(".");
//		for(int i=0; i<parts.length-2; i++) {
//			belongsToMethod += parts[i] + ".";
//		}
//		return belongsToMethod.substring(0, belongsToMethod.length() -1);
//	}
}
