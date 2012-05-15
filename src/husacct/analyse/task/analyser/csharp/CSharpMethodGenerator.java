package husacct.analyse.task.analyser.csharp;

import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class CSharpMethodGenerator extends CSharpGenerator {
	private String name;
	private String uniqueName;
	private String accessControlQualifier;
	private String signature;
	private boolean isPureAccessor;
	private String declaredReturnType;
	private boolean isConstructor;
	private boolean isAbstract;
	private boolean hasClassScope;
	
	public void generateMethod(List<CommonTree> tree, String className) {
		name = getName(tree);	
		accessControlQualifier = checkForAccessControlQualifier(tree.get(0));
		isConstructor = checkForConstructor(tree, className);
		isAbstract = checkForAbstract(tree);
		declaredReturnType = checkForReturnType(tree);
		signature = createSignature(tree);
		uniqueName = className+"."+signature;
		hasClassScope = checkForClassScope(tree);
		isPureAccessor = false;
		modelService.createMethod(name, uniqueName, accessControlQualifier, signature, isPureAccessor, declaredReturnType, className, isConstructor, isAbstract, hasClassScope);
//		boolean isPureAccessor = false; //todo
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
		return name;
	}

	private boolean checkForClassScope(List<CommonTree> tree) {
		boolean hasClassScope = false;
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
		int[] listOfPrimitiveTypes = new int[]{INT, BYTE, SBYTE, UINT, SHORT, USHORT, LONG, ULONG, FLOAT, DOUBLE, CHAR, BOOL, OBJECT, STRING, DECIMAL, VAR};
		for(CommonTree thisTree : tree){
			for(int type : listOfPrimitiveTypes){
				if(returnType == ""){
					if(type == thisTree.getType()){
						returnType = thisTree.getText();
					}
					else{
						if(thisTree.getType() == IDENTIFIER){
							return thisTree.getText();
						}
					}
				}
			}
		}
		return returnType;
	}
	
	private String createSignature(List<CommonTree> tree){
		String signature = "";
		boolean isAllowedToAdd = false;
		boolean lastPosititionWasIdent = false;
		for(CommonTree thisTree : tree){
			if(thisTree.getType() == FORWARDBRACKET){
				isAllowedToAdd = true;
			}
			
			if(thisTree.getType() == FORWARDBRACKET || thisTree.getType() == BACKWARDBRACKET || thisTree.getType() == COMMA){
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
