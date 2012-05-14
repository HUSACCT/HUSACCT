package husacct.analyse.task.analyser.csharp;

import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class CSharpMethodGenerator extends CSharpGenerator {
	private String name;
	private String uniqueName;
	private String accessControlQualifier;
	private String signature;
	private Boolean isPureAccessor;
	private String declaredReturnType;
	private Boolean isConstructor;
	private Boolean isAbstract;
	private Boolean hasClassScope;
	
	public void generate(List<CommonTree> tree, String className) {
		name = getName(tree);
		uniqueName = className+"."+name;
		accessControlQualifier = checkForAccessControlQualifier(tree.get(0));
		isConstructor = checkForConstructor(tree, className);
		isAbstract = checkForAbstract(tree);
		declaredReturnType = checkForReturnType(tree);
		signature = createSignature(tree);
		hasClassScope = checkForClassScope(tree);
		//System.out.println("method: " + uniqueName + accessControlQualifier + isConstructor + isAbstract + "returnType: " + declaredReturnType + " signature: "+signature + hasClassScope);
		isPureAccessor = false;
		modelService.createMethod(name, uniqueName, accessControlQualifier, signature, isPureAccessor, declaredReturnType, className, isConstructor, isAbstract, hasClassScope);
//		Boolean isPureAccessor = false; //todo
	}

	private String getName(List<CommonTree> tree) {
		String name = "";
		for(CommonTree thisTree : tree){
			if(thisTree.getType() == IDENTIFIER){
				name = thisTree.getText();
			}
			
			if(thisTree.getType() == FORWARDBRACKET){
				return name;
			}
		}
		return null;
	}

	private Boolean checkForClassScope(List<CommonTree> tree) {
		Boolean hasClassScope = false;
		for(CommonTree thisTree : tree){
			if(thisTree.getType() == STATIC){
				hasClassScope = true;
			}
		}
		return hasClassScope;
	}

	private String checkForAccessControlQualifier(CommonTree commonTree) {
		String acces = "";
		int[] listOfAccesTypes = new int[]{PUBLIC, PROTECTED, PRIVATE};
		for(int type : listOfAccesTypes){
			if(type == commonTree.getType()){
				acces = commonTree.getText();
			}
		}
		return acces;
	}


	private Boolean checkForConstructor(List<CommonTree> tree, String className) {
		Boolean isConstructor = false;
		for(CommonTree thisTree : tree){	
			if(thisTree.getType() == IDENTIFIER && (thisTree.getText().equals(className))){
				isConstructor = true;
			}
		}
		return isConstructor;
	}

	private Boolean checkForAbstract(List<CommonTree> tree) {
		Boolean isAbstract = false;
		for(CommonTree thisTree : tree){
			if(thisTree.getType() == ABSTRACT){
				isAbstract = true;
			}
		}
		return isAbstract;
	}

	private String checkForReturnType(List<CommonTree> tree) {
		String returnType = null;
		int[] listOfPrimitiveTypes = new int[]{INT};
		for(CommonTree thisTree : tree){
			for(int type : listOfPrimitiveTypes){
				if(returnType == null){
					if(type == thisTree.getType()){
						returnType = thisTree.getText();
					}
					else{
						if(thisTree.getType() == IDENTIFIER){
							returnType = thisTree.getText();
						}
					}
				}
			}
		}
		return returnType;
	}
	
	private String createSignature(List<CommonTree> tree){
		String signature = "";
		Boolean isAllowedToAdd = false;
		Boolean lastPosititionWasIdent = false;
		for(CommonTree thisTree : tree){
			if(thisTree.getType() == FORWARDBRACKET){
				isAllowedToAdd = true;
			}
			
			if(thisTree.getType() == FORWARDBRACKET || thisTree.getType() == BACKWARDBRACKET || thisTree.getType() == KOMMA){
				signature = signature + thisTree.getText();
				lastPosititionWasIdent = false;
			}
			
			if(thisTree.getType() == IDENTIFIER && lastPosititionWasIdent == false){
				signature = signature + thisTree.getText();
				lastPosititionWasIdent = true;
			}
		}
		return signature;
	}

}
