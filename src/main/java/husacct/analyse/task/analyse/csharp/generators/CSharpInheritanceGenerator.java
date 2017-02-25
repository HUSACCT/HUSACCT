package husacct.analyse.task.analyse.csharp.generators;

import static husacct.analyse.task.analyse.csharp.generators.CSharpGeneratorToolkit.getTypeNameAndParts;

public class CSharpInheritanceGenerator extends CSharpGenerator {

    public void generateToDomain(CommonTree inheritanceTree, String currentUniqueClassName) {
        String from = currentUniqueClassName;
        int numberOfChildren = inheritanceTree.getChildCount();
        for (int j = 0; j < numberOfChildren; j++) {
            CommonTree inhChildTree = (CommonTree) inheritanceTree.getChild(j);
	        String to = getTypeNameAndParts(inhChildTree);
	        int line = inhChildTree.getChild(0).getLine();
	        modelService.createInheritanceDefinition(from, to, line);
        }
    }
}
