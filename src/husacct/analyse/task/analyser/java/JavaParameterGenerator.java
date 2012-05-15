package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.JavaParser;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.tree.Tree;

public class JavaParameterGenerator extends JavaGenerator {

	private String belongsToMethod; //dit moet de unique name van de method worden dus met signature
	private String belongsToClass;
	private int lineNumber;
	
	private String declareType;
	private List<String> declareTypes = new ArrayList<String>();
	private String declareName;
	private String uniqueName;
	
	
	private String signature;
	private boolean nameFound = false;
	private boolean declareTypeFound = false;
	
	public String generateParameterObjects(Tree tree, String belongsToMethod, String belongsToClass){
		//returns the signature, which MethodGenerator uses
		
		this.belongsToMethod = belongsToMethod;
		this.belongsToClass = belongsToClass;
		lineNumber = tree.getLine();
		
		DelegateParametersFromTree(tree);
		
		createSignature();
		return signature;
	}



	private void DelegateParametersFromTree(Tree tree) {
		for(int i = 0; i < tree.getChildCount(); i++){
			Tree child = tree.getChild(i);
			int treeType = child.getType();
			if(treeType == JavaParser.FORMAL_PARAM_STD_DECL ){
				getParameterAttributes(child, 1);
				writeParameterToDomain();
				deleteTreeChild(child);
				nameFound = false;
				declareTypeFound = false;
			}
			DelegateParametersFromTree(child);
		}
	}



	private void getParameterAttributes(Tree tree, int indent) {
		for(int i = 0; i < tree.getChildCount(); i++){
			Tree child = tree.getChild(i);
			int treeType = child.getType();
			if(treeType == JavaParser.LOCAL_MODIFIER_LIST && (nameFound == false)){
				declareName = tree.getChild(2).getText();
				nameFound = true;
			}
			if(treeType == JavaParser.TYPE ){
				if(declareTypeFound = false){
					if (child.getChild(0).getType() != JavaParser.QUALIFIED_TYPE_IDENT){
						declareType = child.getChild(0).getText();
					} else{
						declareType = child.getChild(0).getChild(0).getText();
					}
					declareTypeFound = true;
				} else {
					if (child.getChild(0).getType() != JavaParser.QUALIFIED_TYPE_IDENT){
						declareTypes.add(child.getChild(0).getText());
					} else{
						declareTypes.add(child.getChild(0).getChild(0).getText());
					}
					
				}
			}
			if(treeType == JavaParser.GENERIC_TYPE_ARG_LIST){
				declareTypes.add("<");
				
			}
			getParameterAttributes(child, indent + 1);
		}
	}
	
	
	private void deleteTreeChild(Tree treeNode){ 
        for (int child = 0 ; child < treeNode.getChildCount();){ 
            treeNode.deleteChild(treeNode.getChild(child).getChildIndex()); 
        } 
    } 
	
	private void createSignature() {
		
	}
	
	private void writeParameterToDomain() {
		modelService.createParameter(declareName, uniqueName, declareType, belongsToClass, lineNumber, belongsToMethod, declareTypes);
	}
}
