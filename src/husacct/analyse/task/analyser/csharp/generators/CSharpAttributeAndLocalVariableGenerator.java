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
	private String declareType;  //int, string, CustomClass etc
	private int lineNumber;
	private String belongsToMethod = ""; //alleen voor local variables


	private ArrayList<String> declareTypes = new ArrayList<String>();

	public void generateLocalVariableToDomain(Tree treeNode, String packageAndClassName, String belongsToMethod) {
		this.belongsToMethod = belongsToMethod;
		setMembers(treeNode, packageAndClassName);
		createLocalVariableObject();
	}

	public void generateAttributeToDomain(Tree treeNode, String packageAndClassName) {
		setMembers(treeNode, packageAndClassName);
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
		if(declareType.contains("."))declareType = declareType.substring(0, declareType.length()-1);
		if(!SkippableTypes.isSkippable(declareType)){
			modelService.createAttribute(hasClassScope, accessControlQualifier, packageAndClassName, declareType, name, packageAndClassName + "." + name, lineNumber, this.declareTypes);
		}
		declareType = "";

	}

	private void createLocalVariableObject() {
		if(declareType.contains("."))declareType = declareType.substring(0, declareType.length()-1);
		if(!SkippableTypes.isSkippable(declareType)){
			modelService.createLocalVariable(packageAndClassName, declareType, name, this.belongsToMethod + "." + this.name, lineNumber, this.belongsToMethod, this.declareTypes);
		}
		declareType = "";

	}


	private void setMembers(Tree treeNode, String packageAndClassName) { //startFiltering in Java Implementation
		CommonTree currentTree = (CommonTree) treeNode;
		CommonTree identifierTree = (CommonTree) currentTree.getFirstChildWithType(CSharpParser.IDENTIFIER);
		if(identifierTree != null){
			this.name = identifierTree.getText();
			this.lineNumber = identifierTree.getLine();
		}
		this.packageAndClassName = packageAndClassName;
		treeNodeTypeFilter(treeNode);
	}

	private void treeNodeTypeFilter(Tree treeNode) {
		for(int i = 0; i < treeNode.getChildCount(); i++){
			Tree child = treeNode.getChild(i);
			switch(child.getType()){

			case CSharpParser.MODIFIERS:
				setAccessControlQualifier(treeNode);
				setClassScope(child);
				break;
			case CSharpParser.TYPE:
				setDeclareType(child);		
				break;
			case CSharpParser.VARIABLE_INITIALIZER:				
				break;
			case CSharpParser.OBJECT_CREATION_EXPRESSION:
				delegateInvocationConstructor(child);
				break;
			case CSharpParser.METHOD_INVOCATION:
				delegateInvocationMethod(child);
				break;
			case CSharpParser.MEMBER_ACCESS:
				delegateInvocationPropertyOrField(child);
				break;
			}
			treeNodeTypeFilter(child);
		}
	}
	
	private void delegateInvocationMethod(Tree tree) {
		CSharpInvocationMethodGenerator csharpInvocationMethodGenerator = new CSharpInvocationMethodGenerator(this.packageAndClassName);
		csharpInvocationMethodGenerator.generateMethodInvocToDomain((CommonTree) tree, this.belongsToMethod);
	}

	private void delegateInvocationPropertyOrField(Tree tree) {
		CSharpInvocationPropertyOrFieldGenerator csharpInvocationPropertyOrFieldGenerator = new CSharpInvocationPropertyOrFieldGenerator(this.packageAndClassName);
		csharpInvocationPropertyOrFieldGenerator.generatePropertyOrFieldInvocToDomain((CommonTree) tree, this.belongsToMethod);
	}
	
	private void delegateInvocationConstructor(Tree tree) {
		CSharpInvocationConstructorGenerator csharpInvocationConstructorGenerator= new CSharpInvocationConstructorGenerator(this.packageAndClassName);
		csharpInvocationConstructorGenerator.generateConstructorInvocToDomain((CommonTree) tree, this.belongsToMethod);
	}
	
	private void setDeclareType(Tree typeNode) {
		CommonTree typeTree = (CommonTree) typeNode;
		if(declareType == null || !SkippableTypes.isSkippable(declareType))	declareType = CSharpGeneratorToolkit.getTypeNameAndParts(typeTree); 
		addTypeNameIfNotSkippable(typeTree);
		addArgumentListTypes(typeTree);
	}

	private void addArgumentListTypes(CommonTree typeTree) {
		if(CSharpGeneratorToolkit.hasChild(typeTree, CSharpParser.TYPE_ARGUMENT_LIST)) {
			addTypeNameIfNotSkippable((CommonTree)typeTree.getChild(1));
		}
		if(CSharpGeneratorToolkit.getFirstDescendantWithType(typeTree, CSharpParser.TYPE_ARGUMENT_LIST) != null) {
			CommonTree typeArgumentListTree =  CSharpGeneratorToolkit.getFirstDescendantWithType(typeTree, CSharpParser.TYPE_ARGUMENT_LIST);
			treeNodeTypeFilter(typeArgumentListTree);
		}
		
	}
	
	private void addTypeNameIfNotSkippable(CommonTree typeTree) {
		String s = CSharpGeneratorToolkit.getTypeNameAndParts(typeTree);
		if(!SkippableTypes.isSkippable(s))declareTypes.add(s);

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
