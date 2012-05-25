package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.JavaParser;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class JavaBlockScopeGenerator extends JavaGenerator {

	private String belongsToClass;
	private String belongsToMethod;
	
	JavaAttributeAndLocalVariableGenerator javaLocalVariableGenerator;
	
	public void walkThroughBlockScope(CommonTree tree, String belongsToClass, String belongsToMethod){
		this.belongsToClass = belongsToClass;
		this.belongsToMethod = belongsToMethod;
		javaLocalVariableGenerator = new JavaAttributeAndLocalVariableGenerator();
		
		walkThroughBlockScope(tree);
	}
	
	private void walkThroughBlockScope(Tree tree){
		for(int i = 0; i < tree.getChildCount(); i++){
			Tree child = tree.getChild(i);
			int treeType = child.getType();
			if(treeType == JavaParser.VAR_DECLARATION){
				javaLocalVariableGenerator.generateLocalVariableModel(child, this.belongsToClass, this.belongsToMethod);
				deleteTreeChild(child);
			}
			if(treeType == JavaParser.CLASS_CONSTRUCTOR_CALL ){ 
                delegateInvocation(child, "invocConstructor"); 
                //ik ben er nog niet uit of deze wel gedelete mag worden
            } 
            if(treeType == JavaParser.METHOD_CALL ){ 
                if (child.getChild(0).getType() == 15){ //getType omdat 15 een punt is
                	delegateInvocation(child, "invocMethod");
                	deleteTreeChild(child); 
                }
            } 
            if(treeType == JavaParser.THROW || treeType == JavaParser.CATCH || treeType == JavaParser.THROWS){
				delegateException(child); 
				deleteTreeChild(child); 
			} 
            if(treeType == JavaParser.ASSIGN ){ //=
                if (child.getChild(0).getType() == 15){ //getType omdat 15 een punt is
                	delegateInvocation(child, "accessPropertyOrField");
                	deleteTreeChild(child); 
                }
            } 
            if(treeType == JavaParser.FOR_EACH || treeType == JavaParser.FOR || treeType == JavaParser.WHILE ){
            	delegateLoop(child);
            	deleteTreeChild(child); 
            }
            walkThroughBlockScope(child);
		}
	}
	
	private void delegateInvocation(Tree treeNode, String type) {
		JavaInvocationGenerator javaInvocationGenerator = new JavaInvocationGenerator(this.belongsToClass);
		if (type.equals("invocConstructor")){
			javaInvocationGenerator.generateConstructorInvocToModel((CommonTree) treeNode, this.belongsToMethod);
		}
		else if (type.equals("invocMethod")){
			javaInvocationGenerator.generateMethodInvocToModel((CommonTree) treeNode, this.belongsToMethod);
		}
		else if (type.equals("accessPropertyOrField")){
			javaInvocationGenerator.generatePropertyOrFieldInvocToModel((CommonTree) treeNode, this.belongsToMethod);
		}
	}
	
	private void delegateException(Tree exceptionTree){ 
		JavaExceptionGenerator exceptionGenerator = new JavaExceptionGenerator(); 
		exceptionGenerator.generateModel((CommonTree)exceptionTree, this.belongsToClass); 
	} 
	
	private void delegateLoop(Tree tree) {
		JavaLoopGenerator forEachLoopGenerator = new JavaLoopGenerator();
    	forEachLoopGenerator.generateModelFromLoop((CommonTree) tree, this.belongsToClass,  this.belongsToMethod);
		
	}
	
	private void deleteTreeChild(Tree treeNode){ 
        for (int child = 0 ; child < treeNode.getChildCount();){ 
            treeNode.deleteChild(treeNode.getChild(child).getChildIndex()); 
        } 
    } 
}
