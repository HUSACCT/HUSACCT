package husacct.analyse.task.analyser.java;

import java.util.ArrayList;

import husacct.analyse.domain.ModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser.delegate_creation_expression_return;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser.qualified_identifier_return;
import husacct.analyse.infrastructure.antlr.java.JavaParser;
import husacct.analyse.infrastructure.antlr.java.JavaParser.qualifiedTypeIdentSimplified_return;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.apache.log4j.Logger;

class JavaMethodGeneratorController extends JavaGenerator{
	
	private boolean hasClassScope = false;
	private boolean isAbstract = false;
	private boolean isConstructor;
	private String belongsToClass;

	private String accessControlQualifier = "package-private";

	private boolean isPureAccessor;  //TODO Fille isPureAccessor
	private String declaredReturnType;

	private String signature;
	public String name;
	public String uniqueName;
	private Logger logger = Logger.getLogger(JavaMethodGeneratorController.class);
	
	JavaAttributeAndLocalVariableGenerator javaLocalVariableGenerator = new JavaAttributeAndLocalVariableGenerator();

	public void delegateMethodBlock(CommonTree methodTree, String className) {
		this.belongsToClass = className;
		checkMethodType(methodTree);
		WalkThroughMethod(methodTree);
		createMethodObject();	
	}

	private void checkMethodType(CommonTree methodTree) {
		if (methodTree.getType() == JavaParser.CONSTRUCTOR_DECL){ 
			declaredReturnType = "";
			isConstructor = true;
		}
		else if(methodTree.getType() == JavaParser.VOID_METHOD_DECL){
			declaredReturnType = "";
			isConstructor = false;
		}
		else if(methodTree.getType() == JavaParser.FUNCTION_METHOD_DECL){
			isConstructor = false;
		}
		else {
			logger.warn("MethodGenerator aangeroepen maar geen herkenbaar type methode");
		}
	}

	private void WalkThroughMethod(Tree tree) {
		for(int i = 0; i < tree.getChildCount(); i++){
			Tree child = tree.getChild(i);
			int treeType = child.getType();
			if(treeType == JavaParser.ABSTRACT){
				isAbstract = true;
			}
			if(treeType == JavaParser.FINAL){
				hasClassScope = true;
			}
			if(treeType == JavaParser.PUBLIC){
				accessControlQualifier = "public";
			}
			if(treeType == JavaParser.PRIVATE){
				accessControlQualifier = "private";
			}
			if(treeType == JavaParser.PROTECTED){
				accessControlQualifier = "protected";
			}
			if(treeType == JavaParser.TYPE){
				declaredReturnType = getReturnType(child);
				deleteTreeChild(child);
			}
			if(treeType == JavaParser.IDENT){
				name = child.getText();
				uniqueName = belongsToClass + "." + name;
			}
			if(treeType == JavaParser.FORMAL_PARAM_LIST){
//				JavaParameterGenerator javaParameterGenerator = new JavaParameterGenerator();
//				signature = javaParameterGenerator.generateParameterObjects(child, name, belongsToClass);
				deleteTreeChild(child);
			}
			if(treeType == JavaParser.BLOCK_SCOPE){
				loopThroughBlockMethod(child);
				deleteTreeChild(child);
			}

			WalkThroughMethod(child);
		}
	}

	private void loopThroughBlockMethod(Tree tree) {
		for(int i = 0; i < tree.getChildCount(); i++){
			Tree child = tree.getChild(i);
			int treeType = child.getType();
			if(treeType == JavaParser.VAR_DECLARATION){
				javaLocalVariableGenerator.generateLocalVariableModel(child, name, belongsToClass);
				deleteTreeChild(child);
			}
			loopThroughBlockMethod(child);
		}
		
	}

	private String getReturnType(Tree tree){
		//op dit moment worden arraylisten en hashmaps dusdanig gezet als 'hashmap' en 'arraylist' en niet als
		//ArrayList<User> bijv. Dit is wellicht een TODO
		if (tree.getChild(0).getType() == JavaParser.QUALIFIED_TYPE_IDENT ){
			return tree.getChild(0).getChild(0).getText();
		}
		else{
			return tree.getChild(0).getText();
		}
	}

	private void deleteTreeChild(Tree treeNode){ 
        for (int child = 0 ; child < treeNode.getChildCount();){ 
            treeNode.deleteChild(treeNode.getChild(child).getChildIndex()); 
        } 
    } 
	private void createMethodObject(){
		modelService.createMethod(name, uniqueName, accessControlQualifier, signature, isPureAccessor, declaredReturnType, belongsToClass, isConstructor, isAbstract, hasClassScope);
	}	
}