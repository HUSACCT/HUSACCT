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

        /* Test helper
       	if (this.belongsToClass.equals("domain.direct.violating.AccessLocalVariable_Argument")){
    		if (lineNumber == 7) {
    				boolean breakpoint1 = true;
    		}
    	} */

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
            if (currentChild.getType() == JavaParser.TYPE) {
            	setDeclareType(currentChild);
                this.declareTypeFound = true;
                this.signature += !this.signature.equals("") ? "," : "";
                this.signature += this.declareType;
            } else {
                getParameterAttributes(currentChild, indent + 1);
            }
        }
        return "";
    }

    private void setDeclareType(Tree typeTree) {
        Tree child = typeTree.getChild(0);
        Tree declaretype = child.getChild(0);
        String foundType = "";
        if (child.getType() != JavaParser.QUALIFIED_TYPE_IDENT) {
            foundType = child.getText();
        } else {
            if (child.getChildCount() > 1) {
                for (int i = 0; i < child.getChildCount(); i++) {
                    foundType += child.getChild(i).toString() + ".";
                }
            } else {
                foundType = declaretype.getText();
            }
        }
        if (this.declareType == null || this.declareType.equals("")) {
            this.declareType = foundType;
        } else {
        	currentTypes.add(foundType);
        }
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
            if (type.endsWith(".")) {
            	type = type.substring(0, type.length() - 1); //deleting the last point
            }
            String name = (String) currentParam.get(1);
            ArrayList<String> types = (ArrayList<String>) currentParam.get(2);
            this.uniqueName = this.belongsToClass + "." + this.belongsToMethod + "(" + this.signature + ")." + name;
            String belongsToMethodToPassThrough = this.belongsToClass + "." + this.belongsToMethod + "(" + this.signature + ")";
            if (SkippedTypes.isSkippable(type)) {
                modelService.createParameterOnly(name, uniqueName, type, belongsToClass, lineNumber, belongsToMethodToPassThrough, types);
            } else {
                modelService.createParameter(name, uniqueName, type, belongsToClass, lineNumber, belongsToMethodToPassThrough, types);
            	
            }
        }
    }
}
