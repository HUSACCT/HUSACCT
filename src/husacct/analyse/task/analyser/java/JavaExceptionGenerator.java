package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.JavaParser;
import org.antlr.runtime.tree.CommonTree;

class JavaExceptionGenerator extends JavaGenerator{
	
	private static final int catchNode = JavaParser.CATCH;
	private static final int throwsNode = JavaParser.THROWS;
	private static final int throwNewNode = JavaParser.THROW;
	private static final int typeIdentifierNode = JavaParser.QUALIFIED_TYPE_IDENT;
	
	private String exceptionType;
	private String fromClass; 
	private String exceptionClass;
	private int lineNumber;
	
	public void generateModel(CommonTree tree, String theClass){
		this.lineNumber = tree.getLine();
		this.fromClass = theClass;
		setExceptionClass(tree);
		
		if(isCatchedException(tree)){
			this.exceptionType = "catch";
		} else if(isThrowedException(tree)){
			this.exceptionType = "throw";
		} else {
			this.exceptionType = "throws";
		}

		modelService.createException(fromClass, exceptionClass, lineNumber, exceptionType);
	}
	
	private boolean isCatchedException(CommonTree tree){
		return tree.getType() == catchNode;
	}
	
	private boolean isThrowedException(CommonTree tree){
		return tree.getType() == throwNewNode;
	}
	
	private void setExceptionClass(CommonTree tree){		
		if(tree != null){
			for(int index = 0; index < tree.getChildCount(); index++){
				if(tree.getType() == typeIdentifierNode) {
					this.exceptionClass = this.parserUniquename((CommonTree) tree);
				} else if (tree.getType() == 156){
					this.exceptionClass = this.parserUniquename((CommonTree) tree.getChild(0));
				} 
				else {
					setExceptionClass((CommonTree)tree.getChild(index));
				}
			}
		}
	}
	
	
	private String parserUniquename(CommonTree tree){
		String path = "";
		if(tree.getType() == JavaParser.DOT){
			path += packageClassPath(tree);
		} else if (tree.getType() == JavaParser.QUALIFIED_TYPE_IDENT){
			int childcount = tree.getChildCount();
			for(int iterator = 0; iterator < childcount; iterator++){
				path += !path.equals("") ? "." : "";
				path += tree.getChild(iterator).toString();
			}
		} else {
			path = tree.toString();
		}
		return path;
	}
	
	private String packageClassPath(CommonTree tree){
		String path = "";
		CommonTree elementsTree = (CommonTree) tree.getChild(0);
		int totalElementen = elementsTree.getChildCount();
		for(int iterator = 0; iterator < totalElementen; iterator++){
			path += path != "" ? "." : "";
			path += elementsTree.getChild(iterator);
		}
		path += "." + tree.getChild(1).toString();
				
		return path;
	}
	
	
}
