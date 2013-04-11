package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import java.util.Stack;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class CSharpNamespaceGenerator extends CSharpGenerator {

    public String generateModel(Stack<String> namespaceStack, Tree namespaceTree) {
        String parentNamespace = CSharpGeneratorToolkit.getParentName(namespaceStack);
        String namespaceName = getNamespaceName(namespaceTree);
        String uniqueName = CSharpGeneratorToolkit.getUniqueName(parentNamespace, namespaceName);

        modelService.createPackage(uniqueName, parentNamespace, namespaceName);
        return namespaceName;
    }
    
     public static String getNamespaceName(Tree namespaceTree) {
        for (int i = 0; i < namespaceTree.getChildCount(); i++) {
            if (namespaceTree.getChild(i).getType() == CSharpParser.QUALIFIED_IDENTIFIER) {
                CommonTree mTree = (CommonTree) namespaceTree.getChild(i).getChild(0);
                return mTree.token.getText();
            }
        }
        throw new ParserException();
    }
}
