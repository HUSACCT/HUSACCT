package husacct.analyse.task.analyser.csharp.generators;

import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class CSharpNamespaceGenerator extends CSharpGenerator {

    public void namespaceGenerator(List<CommonTree> namespaceTrees) {
        String namespaceString = "";
        String uniqueName = "";
        for (CommonTree commonTree : namespaceTrees) {
            String name = "";
            if (commonTree.getType() != NAMESPACE) {
                name = commonTree.getText();
                uniqueName += name;
            }
            if (commonTree.getType() == IDENTIFIER) {
                recursiveNamespaces(name, uniqueName, namespaceString);
                if (namespaceString.equals("")) {
                    namespaceString = name;
                } else {
                    namespaceString += "." + name;
                }
            }
        }
    }

    public void recursiveNamespaces(String name, String uniqueName, String namespaceString) {
        modelService.createPackage(uniqueName, namespaceString, name);
    }
}
