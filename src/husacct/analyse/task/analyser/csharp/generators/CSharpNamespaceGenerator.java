package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import java.util.Stack;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class CSharpNamespaceGenerator extends CSharpGenerator {

    public String generateModel(Stack<String> namespaceStack, Tree namespaceTree) {
        String parentNamespace = getParentNamespace(namespaceStack);
        String namespaceName = getNamespaceName(namespaceTree);
        String uniqueName = parentNamespace + potentiallyInsertDot(parentNamespace) + namespaceName;

        modelService.createPackage(uniqueName, parentNamespace, namespaceName);
        return namespaceName;
    }

    private String getParentNamespace(Stack<String> namespaceStack) {
        String result = "";
        for (String namespacePart : namespaceStack) {
            result += namespacePart + ".";
        }
        return result.length() > 0 ? result.substring(0, result.length() - 1) : "";
    }

    private String getNamespaceName(Tree namespaceTree) {
        for (int i = 0; i < namespaceTree.getChildCount(); i++) {
            if (namespaceTree.getChild(i).getType() == CSharpParser.QUALIFIED_IDENTIFIER) {
                return filterShortNames(namespaceTree.getChild(i));
            }
        }
        throw new ParserException();
    }

    private String filterShortNames(Tree antlrTree) {
        CommonTree mTree = (CommonTree) antlrTree.getChild(0);
        return mTree.token.getText();
    }

    private String potentiallyInsertDot(String parentNamespace) {
        return parentNamespace.length() > 0 ? "." : "";
    }
}
