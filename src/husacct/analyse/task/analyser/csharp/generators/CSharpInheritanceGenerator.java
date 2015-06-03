package husacct.analyse.task.analyser.csharp.generators;

import static husacct.analyse.task.analyser.csharp.generators.CSharpGeneratorToolkit.getTypeNameAndParts;
import org.antlr.runtime.tree.CommonTree;

public class CSharpInheritanceGenerator extends CSharpGenerator {

    public void generateToDomain(CommonTree inheritanceTree, String currentUniqueClassName) {
        String from = currentUniqueClassName;
        String to = getTypeNameAndParts((CommonTree) inheritanceTree);;
        int line = inheritanceTree.getChild(0).getLine();


        modelService.createInheritanceDefinition(from, to, line);

    }
}
