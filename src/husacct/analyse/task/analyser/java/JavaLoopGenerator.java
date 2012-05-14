package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.JavaParser;

import org.antlr.runtime.tree.CommonTree;

public class JavaLoopGenerator {

	private String currentClass;
	
	public void generateModel(CommonTree loopTree, String currentClass){
			this.currentClass = currentClass;
			walkAST(loopTree);
	}
	
	private void walkAST(CommonTree currentElement){
		int currentElementChildCount = currentElement.getChildCount();
		int currentElementType = currentElement.getType();
		
		switch(currentElementType){
			case JavaParser.TYPE:
//				System.out.println(currentElement.parent.toStringTree());
					JavaAttributeAndLocalVariableGenerator attributeGenerator = new JavaAttributeAndLocalVariableGenerator();
					attributeGenerator.generateModel(currentElement.parent, currentClass, attributeGenerator.LOCALVARIABLE);
		}
		
		for(int currentE = 0; currentE < currentElementChildCount; currentE++){
			walkAST((CommonTree) currentElement.getChild(currentE));
		}
		
		
//		for(int currentE = 0; currentE < currentElementChildCount; currentE++){
//			CommonTree childElement = (CommonTree) currentElement.getChild(currentE);
//			int childElementType = childElement.getType();
//			
//			System.out.println(currentElement.toStringTree());
//			
//			switch(childElementType){
//				case JavaParser.LOCAL_MODIFIER_LIST:
//					System.err.println(childElement.getChildCount());
//					
////					JavaAttributeGenerator attributeGenerator = new JavaAttributeGenerator();
////					attributeGenerator.generateModel(childElement, currentClass);
//				case JavaParser.BLOCK_SCOPE:
//					System.err.println("BLOCK SCOPE");
//				default:
//					continue;
//			}
//			
//			
//		}
		
		
	}
	
}
