package husacct.analyse.task.analyser.java;

import husacct.analyse.domain.ModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.analyse.infrastructure.antlr.java.JavaParser;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;


class JavaAttributeAndLocalVariableGenerator {
	
	private Boolean classScope = false;
	private String AccesControlQualifier;
	private String belongsToClass; 
	private String declareClass; //example: package.package.class
	private String declareType = "";  //int, string, CustomClass etc
	private int lineNumber;
	private String belongsToMethod = ""; //alleen voor local variables
	
	private ModelCreationService modelService = new FamixCreationServiceImpl();

	private String name;

	public void generateAttributeModel(Tree attributeTree, String belongsToClass) {
		startFiltering(attributeTree, belongsToClass);
		createAttributeObject();
	}
	
	public void generateLocalVariableModel(Tree attributeTree, String belongsToClass, String belongsToMethod){
		this.belongsToMethod = belongsToMethod;
		startFiltering(attributeTree, belongsToClass);
		createLocalVariableObject();
	}

	private void startFiltering(Tree attributeTree, String belongsToClass){
		CommonTree currentTree = (CommonTree) attributeTree;
		CommonTree IdentTree = (CommonTree) currentTree.getFirstChildWithType(JavaParser.IDENT);
		if(IdentTree != null){
			this.name = IdentTree.getText();
		}
		
		this.belongsToClass = belongsToClass;
		CommonTree commonTree = (CommonTree) attributeTree; 
		if (commonTree.getLine() == 0){
			this.lineNumber = commonTree.getChild(0).getLine();
		}
		else{
			this.lineNumber = commonTree.getLine();
		}
		
		walkThroughAST(attributeTree);
	}
	

	private void walkThroughAST(Tree tree) {
		for(int i = 0; i < tree.getChildCount(); i++){
			Tree child = tree.getChild(i);
			int treeType = child.getType();
			if(treeType == JavaParser.MODIFIER_LIST){
				setAccesControllQualifier(tree);
				setClassScope(child);
			}else if(treeType == JavaParser.TYPE){
				setDeclareType(child);		
			}else if(treeType == JavaParser.VAR_DECLARATOR_LIST){
				setAttributeName(child);	
			}
			else if(treeType == JavaParser.CLASS_CONSTRUCTOR_CALL){
				JavaInvocationGenerator javaInvocationGenerator = new JavaInvocationGenerator(this.belongsToClass);
				javaInvocationGenerator.generateConstructorInvocToModel((CommonTree) tree, belongsToMethod);
			} else if(treeType == JavaParser.AT){
				JavaAnnotationGenerator annotationGenerator = new JavaAnnotationGenerator(belongsToClass);
				annotationGenerator.generateMethod((CommonTree) child);
			} else if(treeType == JavaParser.EXPR){
				JavaInvocationGenerator javaInvocationGenerator = new JavaInvocationGenerator(this.belongsToClass);
				if (child.getChild(0).getType() == JavaParser.METHOD_CALL){
					javaInvocationGenerator.generateMethodInvocToModel((CommonTree) tree, belongsToMethod);
				}
				else if (child.getChild(0).getType() == JavaParser.EXPR){
					javaInvocationGenerator.generatePropertyOrFieldInvocToModel((CommonTree) tree, belongsToMethod);
					deleteTreeChild(child.getChild(0));
				}
				else if (child.getChild(0).getType() == JavaParser.DOT){
					javaInvocationGenerator.generatePropertyOrFieldInvocToModel((CommonTree) child, belongsToMethod);
					deleteTreeChild(child.getChild(0));
				}
			}
			walkThroughAST(child);
		}
	}
	
	private void deleteTreeChild(Tree treeNode){ 
        for (int child = 0 ; child < treeNode.getChildCount();){ 
            treeNode.deleteChild(treeNode.getChild(child).getChildIndex()); 
        } 
    } 

	private void createAttributeObject(){
		if(declareType.contains("."))declareType = declareType.substring(0, declareType.length()-1); //deleting the last point
		modelService.createAttribute(classScope, AccesControlQualifier, belongsToClass, declareType, name, belongsToClass + "." + name, lineNumber);
	}
	
	private void createLocalVariableObject() {
		if(declareType.contains("."))declareType = declareType.substring(0, declareType.length()-1); //deleting the last point
		modelService.createLocalVariable(belongsToClass, declareType, name, this.belongsToMethod + "." + this.name, lineNumber, this.belongsToMethod);
	}

	private void setAttributeName(Tree tree) {
		for(int i = 0; i < tree.getChildCount(); i++){
			Tree child = tree.getChild(i);
			int treeType = child.getType();
			if(treeType == JavaParser.IDENT){
				this.name = child.getText();
				this.lineNumber = tree.getLine();
				break;
			} 		
			setAttributeName(child);
		}
	}

	private void setDeclareType(Tree typeTree) {
		
		Tree child = typeTree.getChild(0);
		Tree declaretype = child.getChild(0);
		if(child.getType() != JavaParser.QUALIFIED_TYPE_IDENT){
			declareType = child.getText();
		}else{
			if(child.getChildCount() > 1){
				for(int i=0; i<child.getChildCount(); i++){
					this.declareType += child.getChild(i).toString() + ".";
				}
			}
			else declareType = declaretype.getText();
		}
	}

	private void setAccesControllQualifier(Tree tree) {
		Tree ModifierList = tree.getChild(0);
		Tree Modifier = ModifierList.getChild(0);
		if(Modifier != null){
			AccesControlQualifier = Modifier.getText();			
		}else{
			AccesControlQualifier = "Package";
		}
	}

	private void setClassScope(Tree ModifierList){
		for(int i = 0; i < ModifierList.getChildCount(); i++){
			if(ModifierList.getChild(i).getType() == JavaParser.STATIC){
				classScope = true;
				break;
			}
		}
	}
}