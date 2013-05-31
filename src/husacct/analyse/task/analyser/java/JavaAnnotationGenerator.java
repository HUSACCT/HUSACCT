package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.JavaParser;
import org.antlr.runtime.tree.CommonTree;

class JavaAnnotationGenerator extends JavaGenerator {

    private String name;
    private String uniqueName;
    private String belongsToPackage;
    private int lineNumber = 0;

    public JavaAnnotationGenerator(String uniquePackageName) {
        this.belongsToPackage = uniquePackageName;
    }

    public String generateToDomain(CommonTree commonTree) {
        this.setName(commonTree);
        this.setUniquename();
        modelService.createInterface(this.uniqueName, this.name, this.belongsToPackage);
        return this.uniqueName;
    }

    public void generateMethod(CommonTree commonTree) {
        this.setName(commonTree);
        this.setUniquename();
        this.setLinenumber(commonTree);
        modelService.createAnnotation(this.belongsToPackage, this.name, this.name, this.uniqueName, this.lineNumber);
    }

    private void setName(CommonTree commonTree) {
        CommonTree IdentNode = (CommonTree) commonTree.getFirstChildWithType(JavaParser.IDENT);
        if (isTreeAvailable(IdentNode)) {
            this.name = IdentNode.getText();
        } else {
            CommonTree nameTree = (CommonTree) commonTree.getFirstChildWithType(JavaParser.DOT);
            this.name = parseDottedTreeToName(nameTree);
        }
    }

    private String parseDottedTreeToName(CommonTree nameTree) {
        String result = "";

        if (!isTreeAvailable(nameTree)) {
            return "";
        }

        CommonTree dottedChild = (CommonTree) nameTree.getFirstChildWithType(JavaParser.DOT);
        if (isTreeAvailable(dottedChild)) {
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

    private void setUniquename() {
        this.uniqueName += this.belongsToPackage + getDotAfterUniquename();
        this.uniqueName += this.name;
    }

    private void setLinenumber(CommonTree lineTree) {
        this.lineNumber = lineTree.getLine();
    }

    private String getDotAfterUniquename() {
        if (!this.belongsToPackage.equals("")) {
            return ".";
        }
        return "";
    }

    private boolean isTreeAvailable(CommonTree tree) {
     if (tree != null) {
    	         return true;
          }
    	    return false; 
    }
}
