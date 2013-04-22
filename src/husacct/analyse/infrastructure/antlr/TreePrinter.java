package husacct.analyse.infrastructure.antlr;

import org.antlr.runtime.tree.Tree;

/**
 * @author Bart Goes
 * @Datum 5-apr-2013
 */
public class TreePrinter {

    private static String space = " ";
    private int indend = 0;

    public TreePrinter(Tree tree) {
        walkThroughNode(tree);
    }

    private void walkThroughNode(Tree node) {
        for (int i = 0; i < node.getChildCount(); i++) {
            indend++;
            System.out.println(fixLine(node.getLine()) + indent() + space + node.getType() + space + node.getText() + space + node.getCharPositionInLine() + space + filterShortNames(node.getChild(i)));
            walkThroughNode(node.getChild(i));
            indend--;
        }
    }

    private String fixLine(int line) {
        String result = "";
        if (line < 10) {
            result += "000";
        } else if (line < 100) {
            result += "00";
        } else if (line < 1000) {
            result += "0";
        }
        return (result += line);
    }

    private String indent() {
        String result = "";
        for (int i = 0; i < indend; i++) {
            result += "    ";
        }
        return result;
    }

    private String filterShortNames(Tree antlrTree) {
        String uniqueName = antlrTree.toStringTree();
        return uniqueName.contains("(") ? "" : uniqueName;
    }
}
