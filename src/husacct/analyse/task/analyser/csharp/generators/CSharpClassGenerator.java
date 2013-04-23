package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import static husacct.analyse.task.analyser.csharp.generators.CSharpGeneratorToolkit.*;
import org.antlr.runtime.tree.CommonTree;

public class CSharpClassGenerator extends CSharpGenerator {

    public String generateToDomain(CommonTree classTree, String namespace) {
        String name = getClassName(classTree);
        String uniqueName = getUniqueName(namespace, name);
        String visibility = getVisibility(classTree);
        boolean isAbstract = isAbstract(classTree);

        modelService.createClass(uniqueName, name, namespace, isAbstract, false, "", visibility);

        return name;
    }

    public String generateToModel(CommonTree classTree, String namespace, String parentClassNames) {
        String name = getClassName(classTree);
        String belongsToClass = belongsToClass(namespace, parentClassNames);
        String uniqueName = getUniqueName(belongsToClass, name);
        String visibility = getVisibility(classTree);
        boolean isAbstract = isAbstract(classTree);

        modelService.createClass(uniqueName, name, namespace, isAbstract, true, belongsToClass, visibility);
        return name;
    }

    private String getClassName(CommonTree classTree) {
        for (int i = 0; i < classTree.getChildCount(); i++) {
            if (classTree.getChild(i).getType() == CSharpParser.IDENTIFIER) {
                CommonTree mTree = (CommonTree) classTree.getChild(i);
                return mTree.token.getText();
            }
        }
        throw new ParserException();
    }
}
