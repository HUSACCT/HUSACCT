package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import org.antlr.runtime.tree.CommonTree;

public class CSharpInheritanceGenerator extends CSharpGenerator {

    public void generateToDomain(CommonTree inheritanceTree, String currentUniqueClassName) {
        String from = currentUniqueClassName;
        String to;

        for (int i = 0; i < inheritanceTree.getChildCount(); i++) {
            if (inheritanceTree.getChild(i).getType() == CSharpParser.NAMESPACE_OR_TYPE_NAME) {
                to = getTypeName((CommonTree) inheritanceTree.getChild(i));
                int line = inheritanceTree.getChild(i).getLine();

                modelService.createInheritanceDefinition(from, to, line);
            }
        }
    }

    private String getTypeName(CommonTree commonTree) {
        String result = "";
        result = commonTree.getChild(0).getText();
        return result;
    }
}
