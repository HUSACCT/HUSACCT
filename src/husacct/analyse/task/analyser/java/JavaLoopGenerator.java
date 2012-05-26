package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.JavaParser;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.apache.log4j.Logger;

public class JavaLoopGenerator extends JavaGenerator{

	private Logger logger = Logger.getLogger(JavaInvocationGenerator.class);
	
	private String belongsToClass;
	private String belongsToMethod;
	
	JavaAttributeAndLocalVariableGenerator javaLocalVariableGenerator = new JavaAttributeAndLocalVariableGenerator();
	JavaInvocationGenerator javaInvocationGenerator;
	JavaBlockScopeGenerator javaBlockScopeGenerator;
	
	public void generateModelFromLoop(CommonTree loopTree, String belongsToClass, String belongsToMethod){
			this.belongsToClass = belongsToClass;
			this.belongsToMethod = belongsToMethod;
			javaInvocationGenerator = new JavaInvocationGenerator(this.belongsToClass);
			if(loopTree.getType() == JavaParser.FOR || loopTree.getType() == JavaParser.WHILE){
				walkForAndWhileAST(loopTree);
			}
			else if(loopTree.getType() == JavaParser.FOR_EACH){
				walkForEachAST(loopTree);
			}
			else {
				logger.warn("Onbekend type loop gevonden tijdens analyseren");
			}

	}
	
	private void walkForAndWhileAST(Tree tree) {
		for(int i = 0; i < tree.getChildCount(); i++){
			Tree child = tree.getChild(i);
			int treeType = child.getType();
			if (treeType == JavaParser.VAR_DECLARATION){
				javaLocalVariableGenerator.generateLocalVariableModel(child, this.belongsToClass, this.belongsToMethod);
				deleteTreeChild(child);
			}
			else if(treeType == JavaParser.METHOD_CALL){
				javaInvocationGenerator.generateMethodInvocToModel((CommonTree) child, belongsToMethod);
				deleteTreeChild(child);
			}
			else if(treeType == JavaParser.DOT){
				CommonTree newTree = new CommonTree();
				newTree.addChild(child);
				javaInvocationGenerator.generatePropertyOrFieldInvocToModel((CommonTree) newTree, this.belongsToMethod);
				deleteTreeChild(child);
			}
			else if(treeType == JavaParser.BLOCK_SCOPE){
				JavaBlockScopeGenerator javaBlockScopeGenerator = new JavaBlockScopeGenerator();
				javaBlockScopeGenerator.walkThroughBlockScope((CommonTree)child, this.belongsToClass, this.belongsToMethod);
				deleteTreeChild(child);
			}
			
			walkForAndWhileAST(child);
		}
	}
	
	private void walkForEachAST(Tree tree) {
		for(int i = 0; i < tree.getChildCount(); i++){
			Tree child = tree.getChild(i);
			int treeType = child.getType();
			if(treeType == JavaParser.TYPE){
				CommonTree myLoopTree = (CommonTree) child;
				CommonTree typeIdentTree = (CommonTree) myLoopTree.getFirstChildWithType(JavaParser.QUALIFIED_TYPE_IDENT);
				if(typeIdentTree != null){
					
					String type = "";
					for(int count = 0; count < typeIdentTree.getChildCount(); count++){
						type += !type.equals("") ? "." : "";
						type += typeIdentTree.getChild(count).getText();
					}
					
					int lineNumber = typeIdentTree.getFirstChildWithType(JavaParser.IDENT).getLine();
					javaLocalVariableGenerator.generateLocalLoopVariable(belongsToClass, belongsToMethod, type , tree.getChild(i + 1).getText(), lineNumber);
				} else {
					logger.warn("Problemen with finding type. Please notice analyse");
				}
			}
			
			else if(treeType == JavaParser.METHOD_CALL){
				javaInvocationGenerator.generateMethodInvocToModel((CommonTree) child, belongsToMethod);
				deleteTreeChild(child);
			}
			else if(treeType == JavaParser.DOT){
				CommonTree newTree = new CommonTree();
				newTree.addChild(child);
				javaInvocationGenerator.generatePropertyOrFieldInvocToModel((CommonTree) newTree, this.belongsToMethod);
				deleteTreeChild(child);
			}
			
			walkForEachAST(child);
		}
		
	}
	
	private void deleteTreeChild(Tree treeNode){ 
        for (int child = 0 ; child < treeNode.getChildCount();){ 
            treeNode.deleteChild(treeNode.getChild(child).getChildIndex()); 
        } 
    } 

}
