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
    private String signature = ""; // Builds up to e.g. (String,int). Used to identify methods with the same name, but different parameters. 
    private boolean nameFound = false;
    private boolean declareTypeFound = false;
    private ArrayList<ArrayList<Object>> parameterQueue; // The parameters need to be stored, until signature has been build up completely.

    public String generateParameterObjects(Tree allParametersTree, String belongsToMethod, String belongsToClass) {
        this.parameterQueue = new ArrayList<ArrayList<Object>>();
        this.belongsToMethod = belongsToMethod;
        this.belongsToClass = belongsToClass;

        /* Test helper
       	if (this.belongsToClass.equals("domain.direct.violating.DeclarationParameter_GenericType_OneTypeParameter")){
    		//if (belongsToMethod.contains("performExternalScript")) {
    				boolean breakpoint1 = true;
    		//}
    	} */

       	delegateParametersFromTree(allParametersTree);
        writeParameterToDomain();
        return signature;
    }

    private void delegateParametersFromTree(Tree allParametersTree) {
        int totalParameters = allParametersTree.getChildCount();
        for (int currentChild = 0; currentChild < totalParameters; currentChild++) {
            CommonTree child = (CommonTree) allParametersTree.getChild(currentChild);
            int treeType = child.getType();
            if (treeType == JavaParser.FORMAL_PARAM_STD_DECL) {
                getParameterName(child);
                getTypeOfParameter(child);
                if (this.nameFound && this.declareTypeFound) {
                    this.addToQueue();
                }
                nameFound = false;
                declareTypeFound = false;
            }
            delegateParametersFromTree(child);
        }
    }

    private void getParameterName(CommonTree tree) {
        Tree parameterNameTree = tree.getFirstChildWithType(JavaParser.IDENT);
        if (parameterNameTree != null) {
            this.declareName = parameterNameTree.getText();
            if ((this.declareName != null)&& (!this.declareName.trim().equals(""))) {
	            this.nameFound = true;
	            this.lineNumber = parameterNameTree.getLine();
            }
        }
    }

    private void getTypeOfParameter(CommonTree tree) {
        CommonTree typeOfParameterTree = JavaGeneratorToolkit.getFirstDescendantWithType(tree, JavaParser.TYPE);
        if (typeOfParameterTree != null) {
        	JavaInvocationGenerator javaInvocationGenerator = new JavaInvocationGenerator(this.belongsToClass);
           	this.declareType = javaInvocationGenerator.getCompleteToString((CommonTree) typeOfParameterTree, belongsToClass);
            if (this.declareType.endsWith(".")) {
            	this.declareType = this.declareType.substring(0, this.declareType.length() - 1); //deleting the last point
            }
           	if(!this.declareType.trim().equals("")) {
           		this.declareTypeFound = true;
                this.signature += !this.signature.equals("") ? "," : "";
                this.signature += this.declareType;
           	}
        }
    }
    
    private void addToQueue() {
        ArrayList<Object> myParam = new ArrayList<Object>();
        myParam.add(this.declareType);
        myParam.add(this.declareName);
        myParam.add(this.lineNumber);
        parameterQueue.add(myParam);
        this.declareType = null;
        this.declareName = null;
    }

	private void writeParameterToDomain() {
        for (ArrayList<Object> currentParameter : parameterQueue) {
            String type = (String) currentParameter.get(0);
            String name = (String) currentParameter.get(1);
            int lineNr =  (int) currentParameter.get(2);
            ArrayList<String> types = new ArrayList<String>(); // May be removed as soon as types is removed frommodelService.createParameterOnly(). 
            this.uniqueName = this.belongsToClass + "." + this.belongsToMethod + "(" + this.signature + ")." + name;
            String belongsToMethodToPassThrough = this.belongsToClass + "." + this.belongsToMethod + "(" + this.signature + ")";
            if (SkippedTypes.isSkippable(type)) {
                modelService.createParameterOnly(name, uniqueName, type, belongsToClass, lineNr, belongsToMethodToPassThrough, types);
            } else {
                modelService.createParameter(name, uniqueName, type, belongsToClass, lineNr, belongsToMethodToPassThrough, types);
            	
            }
        }
    }
}
