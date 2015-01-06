package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.JavaParser;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class JavaImplementsDefinitionGenerator extends JavaGenerator {

    private String from;
    private String to;
    private int lineNumber;

    public void generateToDomain(CommonTree tree, String belongsToClass) {
    	this.from = "";
    	this.to = "";
    	this.lineNumber = 0;
    	
        from = belongsToClass;
        createImplementsDetails(tree);
    }

    private void createImplementsDetails(Tree tree) {
        if (tree != null) {
            for (int i = 0; i < tree.getChildCount(); i++) {
                Tree child = tree.getChild(i);
                int treeType = child.getType();
                if (tree.getType() == JavaParser.IMPLEMENTS_CLAUSE) {
                    lineNumber = tree.getLine();
                }
                if (treeType == JavaParser.QUALIFIED_TYPE_IDENT) {
                    for (int childCount = 0; childCount < child.getChildCount(); childCount++) {
                        to += child.getChild(childCount).getText() + ".";
                    }

                    createDomainObject();
                    deleteTreeChild(child);
                }
                this.to = "";
                createImplementsDetails(tree.getChild(i));
            }
        }
    }

    private void createDomainObject() {
        to = to.substring(0, to.length() - 1); 
        if ((to != null) && !to.trim().equals("") && !SkippedTypes.isSkippable(to)) {
            modelService.createInheritanceDefinition(from, to, lineNumber);
        }
    }

    private void deleteTreeChild(Tree treeNode) {
        for (int child = 0; child < treeNode.getChildCount();) {
            treeNode.deleteChild(treeNode.getChild(child).getChildIndex());
        }
    }
}