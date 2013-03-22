package husacct.analyse.task.analyser.csharp.convertControllers;

import husacct.analyse.task.analyser.csharp.CSharpTreeConvertController;
import husacct.analyse.task.analyser.csharp.generators.CSharpGenerator;
import husacct.analyse.task.analyser.csharp.generators.CSharpPropertyGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class CSharpPropertyConvertController extends CSharpGenerator {

    private List<CommonTree> propertyTrees;
    private final CSharpTreeConvertController cSharpTreeConvertController;

    public CSharpPropertyConvertController(CSharpTreeConvertController cSharpTreeConvertController) {
        this.cSharpTreeConvertController = cSharpTreeConvertController;
        propertyTrees = new ArrayList<CommonTree>();
    }

    public boolean propertyCheck(CommonTree tree, boolean isPartOfProperty) {
        int type = tree.getType();
        boolean biggerThanOne = propertyTrees.size() > 1;
        if (cSharpTreeConvertController.getCurrentClassIndent() != cSharpTreeConvertController.getIndentLevel()) {
            if (isPartOfProperty && type == FORWARDCURLYBRACKET && biggerThanOne) {
                isPartOfProperty = startNewPropertyGenerator();
            }
        }

        if (Arrays.binarySearch(notPartOfAttribute, type) > -1) {
            isPartOfProperty = clearPropertyTree();

        }
        if (isPartOfProperty) {
            propertyTrees.add(tree);
        }
        if (type == FORWARDBRACKET || type == SEMICOLON) {
            isPartOfProperty = clearPropertyTree();
        }
        if (Arrays.binarySearch(isAPartOfAttribute, type) > -1) {
            isPartOfProperty = true;
        }
        return isPartOfProperty;
    }

    private boolean startNewPropertyGenerator() {
        new CSharpPropertyGenerator(propertyTrees, cSharpTreeConvertController.getUniqueClassName());
        return clearPropertyTree();
    }

    private boolean clearPropertyTree() {
        propertyTrees.clear();
        return false;
    }
}
