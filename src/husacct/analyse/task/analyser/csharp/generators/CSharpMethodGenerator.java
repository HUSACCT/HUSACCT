package husacct.analyse.task.analyser.csharp.generators;

import java.util.Arrays;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class CSharpMethodGenerator extends CSharpGenerator {
	private String name;
	private String uniqueName;
	private String accessControlQualifier;
	private String signature;
	private String belongsToClass;
	private boolean isPureAccessor;
	private String declaredReturnType;
	private boolean isConstructor;
	private boolean isAbstract;
	private boolean hasClassScope;
	
	public void generateMethod(List<CommonTree> tree, String className, String uniqueClassName) {
		name = getName(tree);	
		accessControlQualifier = checkForAccessControlQualifier(tree.get(0));
		isConstructor = checkForConstructor(tree, className);
		isAbstract = checkForAbstract(tree);
		declaredReturnType = checkForReturnType(tree);
		belongsToClass = uniqueClassName;
		signature = createSignature(tree);
		uniqueName = belongsToClass+"."+signature;
		hasClassScope = checkForClassScope(tree);
		isPureAccessor = false;
		modelService.createMethod(name, uniqueName, accessControlQualifier, signature, isPureAccessor, declaredReturnType, belongsToClass, isConstructor, isAbstract, hasClassScope);
//		boolean isPureAccessor = false; //todo
	}
	
	public String getSignature(){
		return signature;
	}

	private String getName(List<CommonTree> tree) {
		String name = "";
		for(CommonTree node : tree){
			int type = node.getType();
			if(type == IDENTIFIER){
				name = node.getText();
			}
			
			if(type == FORWARDBRACKET){
				return name;
			}
		}
		return name;
	}

	private boolean checkForClassScope(List<CommonTree> tree) {
		boolean hasClassScope = false;
		for(CommonTree node : tree){
			if(node.getType() == STATIC){
				hasClassScope = true;
			}
		}
		return hasClassScope;
	}

	private String checkForAccessControlQualifier(CommonTree commonTree) {
		String acces = "";
		for(int type : accessorCollection){
			if(type == commonTree.getType()){
				acces = commonTree.getText();
			}
		}
		return acces;
	}


	private boolean checkForConstructor(List<CommonTree> tree, String className) {
		boolean isConstructor = false;
		for(CommonTree thisTree : tree){	
			if(thisTree.getType() == IDENTIFIER && (thisTree.getText().equals(className))){
				isConstructor = true;
			}
		}
		return isConstructor;
	}

	private boolean checkForAbstract(List<CommonTree> tree) {
		boolean isAbstract = false;
		for(CommonTree thisTree : tree){
			if(thisTree.getType() == ABSTRACT){
				isAbstract = true;
			}
		}
		return isAbstract;
	}

	private String checkForReturnType(List<CommonTree> tree) {
		String returnType = "";
		for(CommonTree thisTree : tree){
			for(int type : typeCollection){
				if(type == thisTree.getType()){
					return thisTree.getText();
				}
			}
			if(isConstructor || thisTree.getType() == VOID){
				return "";
			}
		}
		return returnType;
	}
	
	private String createSignature(List<CommonTree> tree){
		String signature = name;
		boolean isAllowedToAdd = false;
		boolean lastPosititionWasIdent = false;
		for(CommonTree thisTree : tree){
			int type = thisTree.getType();
			if(type == FORWARDBRACKET){
				isAllowedToAdd = true;
			}
			
			if(type == BACKWARDCURLYBRACKET || type == COLON){
				return signature;
			}
			
			if(isAllowedToAdd){
				if(type == FORWARDBRACKET || type == BACKWARDBRACKET || type == COMMA){
					signature = signature + thisTree.getText();
					lastPosititionWasIdent = false;
				}
				
				if(Arrays.binarySearch(typeCollection, type) > -1 && lastPosititionWasIdent == false){
					signature = signature + thisTree.getText();
					lastPosititionWasIdent = true;
				}
			}
		}
		return signature;
	}
}