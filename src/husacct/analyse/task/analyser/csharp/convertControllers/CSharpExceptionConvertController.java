package husacct.analyse.task.analyser.csharp.convertControllers;

import java.util.ArrayList;
import java.util.List;

import husacct.analyse.task.analyser.csharp.CSharpTreeConvertController;
import husacct.analyse.task.analyser.csharp.generators.CSharpExceptionGenerator;
import husacct.analyse.task.analyser.csharp.generators.CSharpGenerator;

import org.antlr.runtime.tree.CommonTree;

public class CSharpExceptionConvertController extends CSharpGenerator {

    private final CSharpTreeConvertController treeConvertController;
    private List<CommonTree> exceptionTrees;

    public CSharpExceptionConvertController(CSharpTreeConvertController treeConvertController) {
        this.treeConvertController = treeConvertController;
        exceptionTrees = new ArrayList<CommonTree>();
    }

    public boolean exceptionCheck(CommonTree tree, boolean isPartOfException) {
        int type = tree.getType();
        if (type == CATCH || type == THROW || type == FINALLY) {
            isPartOfException = true;
        }
        if (!(exceptionTrees.isEmpty()) && (type == BACKWARDCURLYBRACKET)) {
            isPartOfException = endOfException(tree);
        }
        if (isPartOfException) {
            exceptionTrees.add(tree);
        }
        return isPartOfException;
    }

    private boolean endOfException(CommonTree tree) {
        int lineNumber = tree.getLine();
        String uniqueClassName = treeConvertController.getUniqueClassName();
        CSharpExceptionGenerator exceptionGenerator = new CSharpExceptionGenerator();
        exceptionGenerator.generateException(exceptionTrees, uniqueClassName, lineNumber);
        exceptionTrees.clear();
        return false;
    }
}
