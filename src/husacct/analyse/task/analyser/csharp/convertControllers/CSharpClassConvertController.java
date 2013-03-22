package husacct.analyse.task.analyser.csharp.convertControllers;

import husacct.analyse.task.analyser.csharp.CSharpData;
import husacct.analyse.task.analyser.csharp.CSharpTreeConvertController;
import husacct.analyse.task.analyser.csharp.generators.CSharpGenerator;

import org.antlr.runtime.tree.CommonTree;

public class CSharpClassConvertController extends CSharpGenerator {

    private CommonTree abstractTree;
    private boolean isClassName;
    private final CSharpTreeConvertController treeConverter;

    public CSharpClassConvertController(CSharpTreeConvertController treeConverter) {
        this.treeConverter = treeConverter;
    }

    public boolean classCheck(CommonTree tree, boolean isClassPart) {
        int type = tree.getType();
        if (type == ABSTRACT) {
            abstractTree = tree;
        }
        if (isClassName) {
            setClassName(tree);
        }
        if (type == CLASS || type == INTERFACE || type == STRUCT) {
            isClassPart = true;
            isClassName = true;
        }
        if (isClassPart && type == FORWARDCURLYBRACKET) {
            isClassPart = false;
            CSharpData data = getCurrentData();
            treeConverter.getIndentClassLevel().add(data);
        }
        if (isClassPart) {
            addToClassTrees(tree);
        }
        return isClassPart;
    }

    private void setClassName(CommonTree tree) {
        treeConverter.setCurrentClassName(tree.getText());
        treeConverter.setCurrentClassIndent(treeConverter.getIndentLevel() + 1);
        treeConverter.createUsingGenerator();
        isClassName = false;
    }

    private void addToClassTrees(CommonTree tree) {
        if (abstractTree != null) {
            addAbstractTree();
        }
        treeConverter.getClassTrees().add(tree);
    }

    private void addAbstractTree() {
        treeConverter.getClassTrees().add(abstractTree);
        abstractTree = null;
    }

    private CSharpData getCurrentData() {
        String className = treeConverter.getCurrentClassName();
        int indentLevel = treeConverter.getIndentLevel();
        String namespaceName = treeConverter.getCurrentNamespaceName();
        return new CSharpData(className, indentLevel, namespaceName);
    }
}
