package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import static husacct.analyse.task.analyser.csharp.generators.CSharpGeneratorToolkit.*;

import java.util.ArrayList;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class CSharpAttributeAndLocalVariableGenerator extends CSharpGenerator{

	private Boolean hasClassScope = false;
	private String name;
	private String accessControlQualifier;
	private String packageAndClassName; 
	private String declareType = "";  //int, string, CustomClass etc
	private int lineNumber;
	private String belongsToMethod = ""; //alleen voor local variables

	public void generateLocalVariableToDomain(Tree treeNode, String packageAndClassName, String belongsToMethod) {
		this.packageAndClassName = packageAndClassName;
		this.belongsToMethod = belongsToMethod;
		setMembers(treeNode);
		treeNodeTypeFilter(treeNode);
		createLocalVariableObject();
	}

	public void generateAttributeToDomain(Tree treeNode, String packageAndClassName) {
		this.packageAndClassName = packageAndClassName;
		setMembers(treeNode);
		treeNodeTypeFilter(treeNode);
		createAttributeObject();
	}

	public void generateLocalLoopVariable(String packageAndClassName, String belongsToMethod, String declareType, String name, int lineNumber) {
		this.packageAndClassName = packageAndClassName;
		this.belongsToMethod = packageAndClassName + "." + belongsToMethod;
		this.declareType = declareType;
		this.name = name;
		this.lineNumber = lineNumber;
		createLocalVariableObject();
	}

	private void createAttributeObject() {
		if ((declareType != null) && (declareType != "")) {
			if(SkippableTypes.isSkippable(declareType)){
				modelService.createAttributeOnly(hasClassScope, accessControlQualifier, packageAndClassName, declareType, name, packageAndClassName + "." + name, lineNumber);
	        } else {
	        	modelService.createAttribute(hasClassScope, accessControlQualifier, packageAndClassName, declareType, name, packageAndClassName + "." + name, lineNumber);
			}
		}
		declareType = "";
	}

	private void createLocalVariableObject() {
		if(declareType.endsWith("."))declareType = declareType.substring(0, declareType.length()-1);
		modelService.createLocalVariable(packageAndClassName, declareType, name, this.belongsToMethod + "." + this.name, lineNumber, this.belongsToMethod);
		declareType = "";
	}


	private void setMembers(Tree treeNode) { //startFiltering in Java Implementation
		CommonTree currentTree = (CommonTree) treeNode;
		CommonTree identifierTree = (CommonTree) currentTree.getFirstChildWithType(CSharpParser.IDENTIFIER);
		if(identifierTree != null){
			this.name = identifierTree.getText();
			this.lineNumber = identifierTree.getLine();
		}
	}

	private void treeNodeTypeFilter(Tree treeNode) {
		for(int i = 0; i < treeNode.getChildCount(); i++){
            Tree child = treeNode.getChild(i);
            boolean walkThroughChildren = true;
			switch(child.getType()){
			case CSharpParser.MODIFIERS:
				setAccessControlQualifier(treeNode);
				setClassScope(child);
				break;
			case CSharpParser.TYPE:
				setDeclareType(child);		
	            walkThroughChildren = false;
				break;
			case CSharpParser.UNARY_EXPRESSION:
				delegateInvocation(child);
	            walkThroughChildren = false;
				break;
			} 
	        if (walkThroughChildren) {
	        	treeNodeTypeFilter(child);
	        }
		}
	}
	
	private void delegateInvocation(Tree tree) {
		CSharpInvocationGenerator csharpInvocationGenerator = new CSharpInvocationGenerator(this.packageAndClassName);
		csharpInvocationGenerator.generateInvocationToDomain((CommonTree) tree, this.belongsToMethod);
	}

	private void setDeclareType(Tree typeNode) {
		CommonTree typeTree = (CommonTree) typeNode;
		CSharpInvocationGenerator csharpInvocationGenerator = new CSharpInvocationGenerator(this.packageAndClassName);
    	String foundType = csharpInvocationGenerator.getCompleteToString(typeTree);
        if ((foundType != null) && !foundType.equals("")) {
            this.declareType = foundType;
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
