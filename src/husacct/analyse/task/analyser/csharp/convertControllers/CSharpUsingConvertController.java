package husacct.analyse.task.analyser.csharp.convertControllers;

import husacct.analyse.task.analyser.csharp.CSharpData;
import husacct.analyse.task.analyser.csharp.CSharpTreeConvertController;
import husacct.analyse.task.analyser.csharp.generators.CSharpGenerator;
import husacct.analyse.task.analyser.csharp.generators.CSharpImportGenerator;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class CSharpUsingConvertController extends CSharpGenerator {

    private final CSharpTreeConvertController treeConvertController;
    private List<List<CommonTree>> usageTrees;
    private String tempUsingName = "";
    private List<CommonTree> tempUsingTrees;
    private List<CSharpData> indentUsingsLevel;

    public CSharpUsingConvertController(CSharpTreeConvertController treeConvertController) {
        this.treeConvertController = treeConvertController;
        usageTrees = new ArrayList<List<CommonTree>>();
        tempUsingTrees = new ArrayList<CommonTree>();
        indentUsingsLevel = new ArrayList<CSharpData>();
    }

    public boolean usingCheck(CommonTree tree, boolean usage) {
        int type = tree.getType();
        if (type == USING) {
            usage = startUsing();
        }
        if (usage && type != USING) {
            aUsingPart(tree);
        }
        if (usage && type == SEMICOLON && !tempUsingName.equals("")) {
            setIndentLevel();
        }
        if (usage && type == SEMICOLON) {
            usage = false;
        }
        return usage;
    }

    private void setIndentLevel() {
        usageTrees.add(tempUsingTrees);
        int indentLevel = treeConvertController.getIndentLevel();
        CSharpData data = new CSharpData(tempUsingName, indentLevel);
        indentUsingsLevel.add(data);
    }

    private void aUsingPart(CommonTree tree) {
        tempUsingTrees.add(tree);
        tempUsingName += tree.getText();
    }

    private boolean startUsing() {
        tempUsingTrees = new ArrayList<CommonTree>();
        tempUsingName = "";
        return true;
    }

    public void createGenerators() {
        for (List<CommonTree> trees : usageTrees) {
            if (!trees.isEmpty() && !indentUsingsLevel.get(usageTrees.indexOf(trees)).getClosed()) {
                new CSharpImportGenerator(trees, treeConvertController.getUniqueClassName());
            }
        }
    }

    public void checkIfUsingsClosed(CommonTree tree) {
        for (CSharpData classData : indentUsingsLevel) {
            if (classData.getIntentLevel() == treeConvertController.getIndentLevel()) {
                classData.setClosed(true);
            }
        }
    }
}