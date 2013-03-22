package husacct.analyse.task.analyser.csharp.convertControllers;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

import husacct.analyse.task.analyser.csharp.CSharpTreeConvertController;
import husacct.analyse.task.analyser.csharp.generators.CSharpGenerator;
import husacct.analyse.task.analyser.csharp.generators.CSharpLocalVariableGenerator;

public class CSharpLocalVariableConvertController extends CSharpGenerator {

    private final CSharpTreeConvertController treeConvertController;
    private List<CommonTree> localVariableTrees;

    public CSharpLocalVariableConvertController(CSharpTreeConvertController treeConvertController) {
        this.treeConvertController = treeConvertController;
        localVariableTrees = new ArrayList<CommonTree>();
    }

    public boolean localVariableCheck(CommonTree tree, boolean isPartOfLocalVariable) {
        int type = tree.getType();
        if (!isPartOfLocalVariable) {
            isPartOfLocalVariable = checkBeginning(isPartOfLocalVariable, type);
        }
        if (type == SEMICOLON) {
            isPartOfLocalVariable = endLocalVariable(tree);
        }
        if (isPartOfLocalVariable) {
            localVariableTrees.add(tree);
        }
        return isPartOfLocalVariable;
    }

    private boolean checkBeginning(boolean isPartOfLocalVariable, int type) {
        for (int thisType : typeCollection) {
            if (type == thisType) {
                return true;
            }
        }
        if (type == NEW) {
            return true;
        }
        return isPartOfLocalVariable;
    }

    private boolean endLocalVariable(CommonTree node) {
        int lineNumber = node.getLine();
        if (localVariableTrees.size() > 0) {
            cleanVariableList();
        }
        if (localVariableTrees.size() > 2) {
            createLocalVariableGenerator(lineNumber);
        }
        localVariableTrees.clear();
        return false;
    }

    private void createLocalVariableGenerator(int lineNr) {
        String uniqueClassName = treeConvertController.getUniqueClassName();
        CSharpLocalVariableGenerator localVariableGenerator = new CSharpLocalVariableGenerator(treeConvertController);
        localVariableGenerator.generateLocalVariable(localVariableTrees, treeConvertController.getCurrentMethodName(), uniqueClassName, lineNr);
    }

    private void cleanVariableList() {
        boolean isLocalVariable = true;
        for (CommonTree node : localVariableTrees) {
            int type = node.getType();
            if (type == FORWARDCURLYBRACKET) {
                isLocalVariable = false;
            }
        }
        if (isLocalVariable) {
            if (localVariableTrees.size() > 2) {
                CommonTree node = localVariableTrees.get(1);
                int type = node.getType();
                if (type == FORWARDBRACKET) {
                    isLocalVariable = false;
                }
            } else {
                isLocalVariable = false;
            }
        }
        if (!(isLocalVariable)) {
            localVariableTrees.clear();
        }
    }
}