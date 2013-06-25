package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.JavaParser;

import java.util.ArrayList;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class JavaParameterGenerator extends JavaGenerator {

    private String belongsToMethod; //this includes the signature of the method (uniqueName)
    private String belongsToClass;
    private int lineNumber;
    private String declareType;
    private String declareName;
    private String uniqueName;
    private String signature = "";
    private boolean nameFound = false;
    private boolean declareTypeFound = false;
    private ArrayList<String> currentTypes;
    private ArrayList<ArrayList<Object>> saveParameterQueue;

    public String generateParameterObjects(Tree allParametersTree, String belongsToMethod, String belongsToClass) {
        this.saveParameterQueue = new ArrayList<ArrayList<Object>>();
        this.currentTypes = new ArrayList<String>();
        this.belongsToMethod = belongsToMethod;
        this.belongsToClass = belongsToClass;
        setLineNumber(allParametersTree);
        DelegateParametersFromTree(allParametersTree);
        writeParameterToDomain();
        return signature;
    }

    private void setLineNumber(Tree linenumberTree) {
        this.lineNumber = linenumberTree.getLine();
    }

    private void DelegateParametersFromTree(Tree allParametersTree) {
        int totalParameters = allParametersTree.getChildCount();
        for (int currentChild = 0; currentChild < totalParameters; currentChild++) {
            CommonTree child = (CommonTree) allParametersTree.getChild(currentChild);
            int treeType = child.getType();
            if (treeType == JavaParser.FORMAL_PARAM_STD_DECL) {
                getAttributeName(child);
                getParameterAttributes(child, 1);
                if (this.nameFound && this.declareTypeFound) {
                    this.addToQueue();
                }
                deleteTreeChild(child);
                nameFound = false;
                declareTypeFound = false;
            }
            DelegateParametersFromTree(child);
        }
    }

    private void getAttributeName(Tree tree) {
        CommonTree attributeTree = (CommonTree) tree;
        Tree attributeNameTree = attributeTree.getFirstChildWithType(JavaParser.IDENT);
        if (attributeNameTree != null) {
            this.declareName = attributeNameTree.getText();
            this.nameFound = true;
        }
    }

    private String getParameterAttributes(Tree tree, int indent) {
        int childrenCount = tree.getChildCount();
        for (int i = 0; i < childrenCount; i++) {
            CommonTree currentChild = (CommonTree) tree.getChild(i);

            if (currentChild.getType() == JavaParser.QUALIFIED_TYPE_IDENT) {
                getAttributeType(currentChild);
                this.declareTypeFound = true;
                this.signature += !this.signature.equals("") ? "," : "";
                this.signature += this.declareType;
            } else {
                getParameterAttributes(currentChild, indent + 1);
            }
        }
        return "";
    }

    public String getAttributeType(CommonTree tree) {
        String attributeType = "";
        attributeType = getAttributeRecursive(tree);
        attributeType = attributeType.replace("<.", "<");
        attributeType = attributeType.substring(1);
        return attributeType;
    }

    public String getAttributeRecursive(CommonTree tree) {
        String attributeType = "";
        int childs = tree.getChildCount();
        for (int childCount = 0; childCount < childs; childCount++) {
            CommonTree childTree = (CommonTree) tree.getChild(childCount);
            switch (childTree.getType()) {
                case JavaParser.IDENT:
                    if (declareType == null || declareType.equals("")) {
                        declareType = childTree.getText();
                    } else {
                        currentTypes.add(childTree.getText());
                    }
                    attributeType += "." + childTree.getText();
                    if (childTree.getChildCount() > 0) {
                        attributeType += getAttributeRecursive(childTree);
                    }
                    break;
                case JavaParser.GENERIC_TYPE_ARG_LIST:
                    attributeType += "<";
                    attributeType += getAttributeRecursive(childTree);
                    attributeType += ">";
                    break;
                default:
                    attributeType += getAttributeRecursive(childTree);
            }
        }
        return attributeType;
    }

    private void deleteTreeChild(Tree treeNode) {
        for (int child = 0; child < treeNode.getChildCount();) {
            treeNode.deleteChild(treeNode.getChild(child).getChildIndex());
        }
    }

    private void addToQueue() {
        ArrayList<Object> myParam = new ArrayList<Object>();
        myParam.add(this.declareType);
        myParam.add(this.declareName);
        myParam.add(this.currentTypes);
        saveParameterQueue.add(myParam);
        this.declareType = null;
        this.declareName = null;
        this.currentTypes = new ArrayList<String>();
    }

    @SuppressWarnings("unchecked")
	private void writeParameterToDomain() {
        for (ArrayList<Object> object : saveParameterQueue) {
            ArrayList<Object> currentParam = object;
            String type = (String) currentParam.get(0);
            String name = (String) currentParam.get(1);
            ArrayList<String> types = (ArrayList<String>) currentParam.get(2);
            this.uniqueName = this.belongsToClass + "." + this.belongsToMethod + "(" + this.signature + ")." + name;
            String belongsToMethodToPassThrough = this.belongsToClass + "." + this.belongsToMethod + "(" + this.signature + ")";
            if (!SkippedTypes.isSkippable(type)) {
                modelService.createParameter(name, uniqueName, type, belongsToClass, lineNumber, belongsToMethodToPassThrough, types);
            }
        }
    }
}
