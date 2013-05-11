package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.task.analyser.csharp.generators.CSharpInvocationGenerator;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;


import java.util.ArrayList;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class CSharpAttributeAndLocalVariableGenerator extends CSharpGenerator{

	private Boolean hasClassScope = false;
	private String name;
	private String accessControlQualifier;
	private String belongsToClass; 
	private String declareType;  //int, string, CustomClass etc
	private int lineNumber;
	private String belongsToMethod = ""; //alleen voor local variables
	

	private ArrayList<String> declareTypes = new ArrayList<String>();
	
	public void generateLocalVariableToDomain(Tree treeNode, String belongsToClass, String belongsToMethod) {
		this.belongsToMethod = belongsToMethod;
		setMembers(treeNode, belongsToClass);
		createLocalVariableObject();
	}

	private void createLocalVariableObject() {
		if(declareType.contains("."))declareType = declareType.substring(0, declareType.length()-1);
		if(!SkippableTypes.isSkippable(declareType)){
			modelService.createAttribute(hasClassScope, accessControlQualifier, belongsToClass, declareType, name, belongsToClass + "." + name, lineNumber, this.declareTypes);
		}
		declareType = "";
		
	}

	private void setMembers(Tree treeNode, String belongsToClass) { //startFiltering in Java Implementation
		CommonTree currentTree = (CommonTree) treeNode;
		CommonTree identifierTree = (CommonTree) currentTree.getFirstChildWithType(CSharpParser.IDENTIFIER);
		if(identifierTree != null){
			this.name = identifierTree.getText();
		}
		this.belongsToClass = belongsToClass;
		treeNodeTypeFilter(treeNode);
	}

	private void treeNodeTypeFilter(Tree treeNode) {
		
		for(int i = 0; i < treeNode.getChildCount(); i++){
			Tree child = treeNode.getChild(i);
			int treeType = child.getType();
			if(treeType == CSharpParser.MODIFIERS){
				setAccessControlQualifier(treeNode);
				setClassScope(child);
			}else if(treeType == CSharpParser.TYPE){
				setDeclareType(child);		
			}else if(treeType == CSharpParser.VARIABLE_DECLARATOR){
				setAttributeName(child);	
			}
			else if(treeType == CSharpParser.CONSTRUCTOR_DECL){
				CSharpInvocationGenerator cSharpInvocationGenerator = new CSharpInvocationGenerator(this.belongsToClass);
				cSharpInvocationGenerator.generateConstructorInvocToDomain((CommonTree) treeNode, belongsToMethod);
			
			} else if(treeType == CSharpParser.EXPRESSION_STATEMENT){
				CSharpInvocationGenerator cSharpInvocationGenerator = new CSharpInvocationGenerator(this.belongsToClass);
				cSharpInvocationGenerator.generateConstructorInvocToDomain((CommonTree) treeNode, belongsToMethod);
			
				if (child.getChild(0).getType() == CSharpParser.METHOD_INVOCATION){
					cSharpInvocationGenerator.generateMethodInvocToDomain((CommonTree) child.getChild(0), belongsToMethod);
				}
				else if (child.getChild(0).getType() == CSharpParser.DOT){
					cSharpInvocationGenerator.generatePropertyOrFieldInvocToDomain((CommonTree) child, belongsToMethod);
				}
			}
			treeNodeTypeFilter(child);
		}
		

	}

	private void setAttributeName(Tree tree) {
		for(int i = 0; i < tree.getChildCount(); i++){
			Tree child = tree.getChild(i);
			int treeType = child.getType();
			if(treeType == CSharpParser.IDENTIFIER){
				this.name = child.getText();
				this.lineNumber = tree.getLine();
				break;
			} 		
			setAttributeName(child);
		}
		
	}
	
	public ArrayList<String> generateMethodReturnType(Tree returnTypeTree, String belongsToClass){
		this.belongsToClass = belongsToClass;
		
		if(returnTypeTree.getType() == CSharpParser.TYPE){
			setDeclareType(returnTypeTree);
		}
		
		treeNodeTypeFilter(returnTypeTree);
		
		ArrayList<String> returnDeclareTypes = new ArrayList<String>();
		returnDeclareTypes.add(this.declareType);
		for(String s : this.declareTypes){
			returnDeclareTypes.add(s);
		}
		
		return returnDeclareTypes;
	}
	
	private void setDeclareType(Tree typeNode) {
		Tree child = typeNode.getChild(0);
		Tree declareTypeNode = child.getChild(0);
		String foundType = "";
		if(child.getType() != CSharpParser.QUALIFIED_IDENTIFIER){
			foundType = child.getText();
		}else{
			if(child.getChildCount() > 1){
				for(int i = 0; i<child.getChildCount(); i++){
					foundType += child.getChild(i).toString() + ".";
				}
			}
			else foundType = declareTypeNode.getText();
		}
		
		if(this.declareType == null || this.declareType.equals("")){
			this.declareType = foundType;
		} else {
			declareTypes.add(foundType);
		}
		
	}

	private void setClassScope(Tree modifierList) {
		for(int i = 0; i < modifierList.getChildCount(); i++){
			if(modifierList.getChild(i).getType() == CSharpParser.STATIC){
				hasClassScope = true;
				break;
			}
		}
	}

	private void setAccessControlQualifier(Tree treeNode) {
		Tree ModifierList = treeNode.getChild(0);
		Tree Modifier = ModifierList.getChild(0);
		if(Modifier != null){
			accessControlQualifier = Modifier.getText();			
		}else{
			accessControlQualifier = "Package";
		}
	}
	

}
