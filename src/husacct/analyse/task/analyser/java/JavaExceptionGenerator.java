package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.Java7Parser;
import org.antlr.runtime.tree.CommonTree;

class JavaExceptionGenerator extends JavaGenerator {

    private static final int catchNode = Java7Parser.CATCH;
    private static final int throwNewNode = Java7Parser.THROW;
    private static final int typeIdentifierNode = Java7Parser.QUALIFIED_TYPE_IDENT;
    private String exceptionType;
    private String fromClass;
    private String exceptionClass;
    private int lineNumber;

    public void generateToDomain(CommonTree tree, String theClass) {
    	this.fromClass = "";
    	this.exceptionClass = "";
    	this.exceptionType = "";
    	this.lineNumber = 0;
    	
        this.lineNumber = tree.getLine();
        this.fromClass = theClass;

        setExceptionClass(tree);

        if (isCatchedException(tree)) {
            this.exceptionType = "catch";
        } else if (isThrowedException(tree)) {
            this.exceptionType = "throw";
        } else {
            this.exceptionType = "throws";
        }

        if (!fromClass.equals("") && !exceptionClass.equals("")) {
        	modelService.createException(fromClass, exceptionClass, lineNumber, exceptionType);
        }
    }

    private void setExceptionClass(CommonTree tree) {
        if (tree != null) {
            for (int index = 0; index < tree.getChildCount(); index++) {
                if (tree.getType() == typeIdentifierNode) {
                    this.exceptionClass = this.parserUniquename((CommonTree) tree);
                    // TreeType 156 is a THROWS_CLAUSE
                } else if (tree.getType() == 156) {
                    this.exceptionClass = this.parserUniquename((CommonTree) tree.getChild(0));
                } else {
                    setExceptionClass((CommonTree) tree.getChild(index));
                }
            }
        }
    }

    private String parserUniquename(CommonTree tree) {
        String path = "";
        if (tree.getType() == Java7Parser.DOT) {
            path += packageClassPath(tree);
        } else if (tree.getType() == Java7Parser.QUALIFIED_TYPE_IDENT) {
            int childcount = tree.getChildCount();
            for (int iterator = 0; iterator < childcount; iterator++) {
                path += !path.equals("") ? "." : "";
                path += tree.getChild(iterator).toString();
            }
        } else {
            path = tree.toString();
        }
        return path;
    }

    private String packageClassPath(CommonTree tree) {
        String path = "";
        int totalElements = tree.getChildCount();
        for (int iterator = 0; iterator < totalElements; iterator++) {
            if (tree.getChild(iterator).getType() == Java7Parser.DOT) {
                path += packageClassPath((CommonTree) tree.getChild(iterator));
            } else {
                path += !path.equals("") ? "." : "";
                path += tree.getChild(iterator).getText();
            }
        }
        return path;
    }

    private boolean isCatchedException(CommonTree tree) {
        return tree.getType() == catchNode;
    }

    private boolean isThrowedException(CommonTree tree) {
        return tree.getType() == throwNewNode;
    }
}
