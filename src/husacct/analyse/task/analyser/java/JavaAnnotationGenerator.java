package husacct.analyse.task.analyser.java;

import java.util.EnumSet;

import husacct.analyse.infrastructure.antlr.java.JavaParser;

import org.antlr.runtime.tree.CommonTree;

class JavaAnnotationGenerator extends JavaGenerator {

    private String name;
    private String uniqueName;
    private int lineNumber;

    public JavaAnnotationGenerator() {
    }

    public void generateToDomain(CommonTree annotationTree, String belongsToClass) {
        if (isTreeExisting(annotationTree) && (belongsToClass != null) && !belongsToClass.equals("")) {
        	name = "";
        	uniqueName = "";
        	lineNumber = 0;
        	
	    	this.setName(annotationTree);
	    	if (!isSkippable(name)) { 
		        this.uniqueName = belongsToClass + "." + this.name; 
		        this.setLinenumber(annotationTree);
		        modelService.createAnnotation(belongsToClass, this.name, this.name, this.uniqueName, this.lineNumber);
	    	}
    	}
    }

    private void setName(CommonTree commonTree) {
        CommonTree IdentNode = (CommonTree) commonTree.getFirstChildWithType(JavaParser.IDENT);
        if (isTreeExisting(IdentNode)) {
            this.name = IdentNode.getText();
        } else {
            CommonTree nameTree = (CommonTree) commonTree.getFirstChildWithType(JavaParser.DOT);
            this.name = parseDottedTreeToName(nameTree);
        }
    }

    private String parseDottedTreeToName(CommonTree nameTree) {
        String result = "";

        if (!isTreeExisting(nameTree)) {
            return "";
        }

        CommonTree dottedChild = (CommonTree) nameTree.getFirstChildWithType(JavaParser.DOT);
        if (isTreeExisting(dottedChild)) {
            result += parseDottedTreeToName(dottedChild);
        }

        int childCount = nameTree.getChildCount();
        for (int currentChild = 0; currentChild < childCount; currentChild++) {
            CommonTree childNode = (CommonTree) nameTree.getChild(currentChild);
            if (childNode.getType() == JavaParser.IDENT) {
                result += result.equals("") ? "" : ".";
                result += childNode.getText();
            }
        }
        return result;
    }

    private void setLinenumber(CommonTree lineTree) {
        this.lineNumber = lineTree.getLine();
    }

    private boolean isTreeExisting(CommonTree tree) {
     if (tree != null) {
    	         return true;
          }
    	    return false; 
    }
    
    private boolean isSkippable(String type) {
		for(SkippedTypes skippedType : EnumSet.allOf(SkippedTypes.class)){
			if(skippedType.toString().equals(type)){
				return true;
			}
		}
		return false;    }
    
    enum SkippedTypes {
    	
    	Override ("Override"),
    	Author ("Author"),
    	SuppressWarnings ("SuppressWarnings"),
    	Deprecated ("Deprecated"),
    	SafeVarargs ("SafeVarargs"),
    	FunctionalInterface ("FunctionalInterface"),
    	Interned ("Interned"),
    	NonNull ("NonNull"),
    	Readonly ("Readonly");
     	
    	private String type;
    	
    	private SkippedTypes(String type){
    		this.type = type;
    	}
    	
    	@Override
    	public String toString(){
    		return type;
    	}
    }	

}
