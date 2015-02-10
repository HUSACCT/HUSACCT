package husacct.analyse.task.analyser.java;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

class JavaPackageGenerator extends JavaGenerator {

    public String generateModel(CommonTree treeNode) {
        String uniqueName = getUniqueNameOfPackage(treeNode);
        String belongsToPackage = getParentPackageName(uniqueName);
        String name = getNameOfPackage(uniqueName);
        createPackage(name, uniqueName, belongsToPackage);
        return uniqueName;
    }

    private String getParentPackageName(String completPackageName) {
        String[] allPackages = splitPackages(completPackageName);
        String parentPackage = "";
        for (int i = 0; i < allPackages.length - 1; i++) {
            if (parentPackage == "") {
                parentPackage += allPackages[i];
            } else {
                parentPackage += "." + allPackages[i];
            }
        }
        return parentPackage;
    }

    private String getUniqueNameOfPackage(Tree antlrTree) {
        String uniqueName = antlrTree.toStringTree();
        uniqueName = uniqueName.replace("(package", "").replace("(", "").replace(")", "").replace(". ", "").substring(1).replace(" ", ".");
        return uniqueName;
    }

    private String getNameOfPackage(String completePackageName) {
        String[] allPackages = splitPackages(completePackageName);
        return allPackages[allPackages.length - 1];
    }

    private String[] splitPackages(String completePackageName) {
        String escapedPoint = "\\.";
        return completePackageName.split(escapedPoint);
    }

    private void createPackage(String name, String uniqueName, String belongsToPackage) {
        modelService.createPackage(uniqueName, belongsToPackage, name);
    }

    public String generateNoPackage_Package() {
        String uniqueName = "no_package";
        String belongsToPackage = "";
        String name = "noPackage_HusacctDefined";
        createPackage(name, uniqueName, belongsToPackage);
        return uniqueName;
    }

}
