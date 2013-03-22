package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.task.analyser.csharp.CSharpData;

import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class CSharpClassGenerator extends CSharpGenerator {

    private boolean isClass;
    private boolean isAbstract;
    private boolean hasInheritance;
    private int classDataIndex = 0;
    private List<CSharpData> indentClassLevel;
    private CSharpData cSharpClassData;
    private CSharpData cSharpInheritanceData;
    private boolean isInterface;
    private boolean hasMore;

    public CSharpClassGenerator(List<CommonTree> classTrees, List<CSharpData> indentClassLevel) {
        this.indentClassLevel = indentClassLevel;
        cSharpInheritanceData = new CSharpData();
        for (CommonTree tree : classTrees) {
            walkASTTree(tree);
        }
    }

    private void walkASTTree(CommonTree tree) {
        if (tree.getType() == CLASS || tree.getType() == STRUCT) {
        }
        if (tree.getType() == COLON) {
            hasInheritance = true;
        }

        if (hasInheritance && tree.getType() == IDENTIFIER) {
            hasInheritance = false;
            createInheritanceGenerator(tree);
        }

        if (tree.getType() == 89) {
            hasMore = true;
        }

        if (hasMore && tree.getType() == IDENTIFIER) {
            hasMore = false;
            createInheritanceGenerator(tree);
        }

        if (tree.getType() == CLASS) {
            isClass = true;
        } else if (tree.getType() == INTERFACE) {
            isInterface = true;
            isClass = true;
        } else if (tree.getType() == ABSTRACT) {
            isAbstract = true;
        } else if (isClass) {
            cSharpClassData = new CSharpData();
            processToModelService(tree);
        }
    }

    private void createInheritanceGenerator(CommonTree tree) {
        cSharpInheritanceData.setInheritanceFrom(cSharpClassData.getUniqueName());
        cSharpInheritanceData.setInheritanceTo(tree.getText());
        CSharpInheritanceDefinitionGenerator inheritanceGenerator = new CSharpInheritanceDefinitionGenerator();
        inheritanceGenerator.addToModelService(cSharpInheritanceData, tree.getLine());
    }

    private void processToModelService(CommonTree tree) {
        isClass = false;
        getRequiredParameters(tree);
        if (!isInterface) {
            addToModelService();
        } else {
            CSharpInterfaceGenerator interfaceGenerator = new CSharpInterfaceGenerator();
            interfaceGenerator.addToModelService(cSharpClassData);
        }
        classDataIndex++;
    }

    public CSharpData retrieveInheritance(CommonTree tree) {
        CSharpData inheritanceInfo = new CSharpData();
        inheritanceInfo.setInheritanceTo(tree.getText());
        inheritanceInfo.setInheritanceFrom(cSharpClassData.getUniqueName());
        return inheritanceInfo;
    }

    private void addToModelService() {
        if (cSharpClassData.isHasParent()) {
            modelService.createClass(
                    cSharpClassData.getUniqueName(),
                    cSharpClassData.getClassName(),
                    cSharpClassData.getBelongsToPackage(),
                    cSharpClassData.isAbstract(),
                    cSharpClassData.isHasParent(),
                    cSharpClassData.getParentClass());
        } else {
            modelService.createClass(
                    cSharpClassData.getUniqueName(),
                    cSharpClassData.getClassName(),
                    cSharpClassData.getBelongsToPackage(),
                    cSharpClassData.isAbstract(),
                    cSharpClassData.isHasParent());
        }
    }

    private void getRequiredParameters(CommonTree tree) {
        CSharpData data = indentClassLevel.get(classDataIndex);
        String namespace = data.getBelongsToPackage();

        if (namespace != null) {
            cSharpClassData.setUniqueName(namespace + "." + tree.getText());
        } else {
            cSharpClassData.setUniqueName(tree.getText());
        }
        cSharpClassData.setClassName(tree.getText());
        cSharpClassData.setAbstract(isAbstract);
        if (data.getBelongsToPackage() != null) {
            cSharpClassData.setBelongsToPackage(data.getBelongsToPackage());
        } else {
            cSharpClassData.setBelongsToPackage("");
        }
        cSharpClassData.setParentClass(data.getBelongsToPackage() + "." + data.getParentClass());
        cSharpClassData.setHasParent(data.isHasParent());
    }
}