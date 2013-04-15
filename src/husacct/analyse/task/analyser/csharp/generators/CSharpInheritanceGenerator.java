package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.infrastructure.antlr.TreePrinter;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class CSharpInheritanceGenerator extends CSharpGenerator {

    public void generateToDomain(CommonTree inheritanceTree, String currentUniqueClassName) {
        String from = currentUniqueClassName;
        new TreePrinter(inheritanceTree);
        String temp = getDestination(inheritanceTree);
        // modelService.createInheritanceDefinition(from, to, lineNumber);
    }

    private String getDestination(CommonTree inheritTree) {
        String result = "";
        for(int i = 0; i < inheritTree.getChildCount(); i++) {
            if (inheritTree.getChild(i).getType() == CSharpParser.NAMESPACE_OR_TYPE_NAME) {
                result += "." + getTypeName((CommonTree)inheritTree.getChild(i));
            } 
        }
        return result;
    }

    private String getTypeName(CommonTree commonTree) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
