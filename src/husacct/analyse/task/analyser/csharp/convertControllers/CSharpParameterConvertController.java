package husacct.analyse.task.analyser.csharp.convertControllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import husacct.analyse.task.analyser.csharp.CSharpTreeConvertController;
import husacct.analyse.task.analyser.csharp.generators.CSharpGenerator;
import husacct.analyse.task.analyser.csharp.generators.CSharpParameterGenerator;

import org.antlr.runtime.tree.CommonTree;

public class CSharpParameterConvertController extends CSharpGenerator {

    private final CSharpTreeConvertController cSharpTreeConvertController;
    private List<CommonTree> parameterTrees;
    private boolean isClosed;

    public CSharpParameterConvertController(CSharpTreeConvertController cSharpTreeConvertController) {
        this.cSharpTreeConvertController = cSharpTreeConvertController;
        parameterTrees = new ArrayList<CommonTree>();
    }

    public boolean parameterCheck(CommonTree tree, boolean isPartOfParameter) {
        int type = tree.getType();
        if (type == FORWARDBRACKET) {
            isPartOfParameter = true;
        }
        if (isPartOfParameter) {
            parameterTrees.add(tree);
        }
        if (isClosed) {
            if (type == FORWARDCURLYBRACKET && parameterTrees.size() > 2) {
                if (Arrays.binarySearch(typeCollection, parameterTrees.get(1).getType()) > -1) {
                    if (parameterTrees.get(2).getType() == IDENTIFIER) {
                        new CSharpParameterGenerator(parameterTrees, cSharpTreeConvertController, cSharpTreeConvertController);
                    }
                }
            }
            isPartOfParameter = clearParameterTree();
        }
        if (isPartOfParameter && type == BACKWARDBRACKET) {
            isClosed = true;
            isPartOfParameter = false;
        }

        return isPartOfParameter;
    }

    private boolean clearParameterTree() {
        parameterTrees.clear();
        isClosed = false;
        return false;
    }
}
