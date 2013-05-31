package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.JavaParser;
import husacct.analyse.task.analyser.VisibilitySet;

import org.antlr.runtime.tree.CommonTree;

class JavaInterfaceGenerator extends JavaGenerator {

    private String name;
    private String uniqueName;
    private String belongsToPackage;
    private String visibillity = VisibilitySet.DEFAULT.toString();

    public JavaInterfaceGenerator(String uniquePackageName) {
        this.belongsToPackage = uniquePackageName;
    }

    public String generateToDomain(CommonTree commonTree) {
        setVisibillityFromTree(commonTree);
        this.name = commonTree.getChild(1).toString();
        if (belongsToPackage.equals("")) {
            this.uniqueName = commonTree.getChild(1).toString();
        } else {
            this.uniqueName = belongsToPackage + "." + commonTree.getChild(1).toString();
        }
        modelService.createInterface(uniqueName, name, belongsToPackage, visibillity);
        return uniqueName;
    }

    private void setVisibillityFromTree(CommonTree tree) {
        CommonTree modifierList = (CommonTree) tree.getFirstChildWithType(JavaParser.MODIFIER_LIST);
        if (!(modifierList == null || modifierList.getChildCount() < 1)) {
            String found = modifierList.getChild(0).toString();
            if (VisibilitySet.isValidVisibillity(found)) {
                this.visibillity = found;
            }
        }
    }
}
