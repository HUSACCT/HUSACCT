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

    public String getNamespaceName(Tree namespaceTree) {
        for (int i = 0; i < namespaceTree.getChildCount(); i++) {
            return getQualifiedIdentifiers((CommonTree) namespaceTree.getChild(i));
        }
        throw new ParserException();
    }

    public String getQualifiedIdentifiers(CommonTree tree) {
        String result = "";
        if (tree.getType() == CSharpParser.QUALIFIED_IDENTIFIER) {
        for (int i = 0; i < tree.getChildCount(); i++) {
             
                result += "." + ((CommonTree) tree.getChild(i)).token.getText();
            
        }}
        if (result.length() > 0) {
            result = result.substring(1);
        }
        return result;
    }
}
