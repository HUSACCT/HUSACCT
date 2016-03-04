package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.JavaParser;
import husacct.analyse.task.analyser.VisibilitySet;

import org.antlr.runtime.tree.CommonTree;

class JavaClassGenerator extends JavaGenerator {

    private static final int abstractNode = JavaParser.ABSTRACT;
    private String name;
    private String uniqueName;
    private String belongsToPackage;
    private String belongsToClass;
    private boolean isAbstract;
    private int nrOfLinesOfCode;
    private String visibility;

    public JavaClassGenerator(String uniquePackageName) {
        this.belongsToPackage = uniquePackageName;
    }

    public String generateToDomain(String sourceFilePath, int linesOfCode, CommonTree commonTree, boolean isNestedClass, String parentClassName, boolean isInterface, boolean isEnumeration) {
        if ((commonTree.getChildCount() > 1) &&  commonTree.getChild(1) != null) {
        	name = "";
        	uniqueName = "";
        	belongsToClass = "";
        	nrOfLinesOfCode = 0;
        	isAbstract = false;
        	visibility = "";
        	
            this.name = commonTree.getChild(1).toString();
            if (isNestedClass) {
                this.belongsToClass = parentClassName;
                this.uniqueName = belongsToClass + "." + name;
            	this.nrOfLinesOfCode = 0;
            } else {
                if (belongsToPackage.equals("")) {
                    this.uniqueName = name;
                } else {
                    this.uniqueName = belongsToPackage + "." + name;
                }
                this.belongsToClass = "";
                this.nrOfLinesOfCode = linesOfCode;
            }
            CommonTree modifierList = (CommonTree) commonTree.getFirstChildWithType(JavaParser.MODIFIER_LIST);
            if (modifierList != null) {
            	this.isAbstract = isAbstract(modifierList);
                this.visibility = getVisibillityFromTree(modifierList);
            }
            if (!name.equals("") && !belongsToPackage.equals("")) {
            	modelService.createClass(sourceFilePath, nrOfLinesOfCode, uniqueName, name, belongsToPackage, isAbstract, isNestedClass, belongsToClass, visibility, isInterface, isEnumeration);
            }
            return uniqueName;
        } else {
        	return "";
        }
    }

    private boolean isAbstract(CommonTree tree) {
        return tree.getFirstChildWithType(abstractNode) != null;
    }
    private String getVisibillityFromTree(CommonTree modifierList) {
        if (modifierList == null || modifierList.getChildCount() < 1) {
            return VisibilitySet.DEFAULT.toString();
        } else {
        	String found = modifierList.getChild(0).toString();
            if (VisibilitySet.isValidVisibillity(found)) {
                return found;
            } else {
                return VisibilitySet.DEFAULT.toString();
            }

        }
    }

}
