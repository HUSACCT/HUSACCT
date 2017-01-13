package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.Java7Parser;
import husacct.common.enums.DependencySubTypes;

import java.util.ArrayList;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class JavaParameterGenerator extends JavaGenerator {

    private String belongsToMethod; //this includes the signature of the method (uniqueName)
    private String belongsToClass;
    private String declareName;
    private String declareType;
	private int lineNumber;
    private String uniqueName;
    private String paramTypesInSignature = ""; // Builds up to e.g. (String,int). Used to identify methods with the same name, but different parameters. 
    private boolean nameFound = false;
    private boolean declareTypeFound = false;
    private ArrayList<ArrayList<Object>> parameterQueue; // The parameters need to be stored, until signature has been build up completely.

    public String generateParameterObjects(Tree allParametersTree, String belongsToMethod, String belongsToClass) { // allParametersTree = FORMAL_PARAM_LIST
        this.parameterQueue = new ArrayList<ArrayList<Object>>();
        this.belongsToMethod = belongsToMethod;
        this.belongsToClass = belongsToClass;
        /* Test helper
       	if (this.belongsToClass.equals("domain.direct.violating.DeclarationParameter_GenericType_OneTypeParameter")){
    		//if (belongsToMethod.contains("performExternalScript")) {
    				boolean breakpoint1 = true;
    		//}
    	} */

		if (allParametersTree != null) {
		   	deriveParametersFromTree(allParametersTree);
		    writeParametersToDomain();
		}
        return paramTypesInSignature;
    }

    private void deriveParametersFromTree(Tree allParametersTree) {
        int totalParameters = allParametersTree.getChildCount();
        for (int currentChild = 0; currentChild < totalParameters; currentChild++) {
            CommonTree child = (CommonTree) allParametersTree.getChild(currentChild);
            int treeType = child.getType();
            if (treeType == Java7Parser.VOID) { //FORMAL_PARAM_STD_DECL) {
                getParameterName(child);
                getTypeOfParameter(child);
                if (this.nameFound && this.declareTypeFound) {
                    this.addToQueue();
                }
                nameFound = false;
                declareTypeFound = false;
            }
            deriveParametersFromTree(child);
        }
    }

    private void getParameterName(CommonTree tree) {
        Tree parameterNameTree = tree.getFirstChildWithType(Java7Parser.Identifier);
        if (parameterNameTree != null) {
            this.declareName = parameterNameTree.getText();
            if ((this.declareName != null)&& (!this.declareName.trim().equals(""))) {
	            this.nameFound = true;
            }
        }
    }

    private void getTypeOfParameter(CommonTree tree) {
        CommonTree typeOfParameterTree = JavaGeneratorToolkit.getFirstDescendantWithType(tree, Java7Parser.VOID); //.TYPE
        if (typeOfParameterTree != null) {
        	AccessAndCallAnalyser javaInvocationGenerator = new AccessAndCallAnalyser(this.belongsToClass);
           	this.declareType = javaInvocationGenerator.getCompleteToString((CommonTree) typeOfParameterTree, belongsToClass, DependencySubTypes.DECL_PARAMETER);
            this.lineNumber = typeOfParameterTree.getLine();
            if (this.declareType.endsWith(".")) {
            	this.declareType = this.declareType.substring(0, this.declareType.length() - 1); //deleting the last point
            }
           	if(!this.declareType.trim().equals("")) {
           		this.declareTypeFound = true;
                this.paramTypesInSignature += !this.paramTypesInSignature.equals("") ? "," : "";
                this.paramTypesInSignature += this.declareType;
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

	private void writeParametersToDomain() {
        for (ArrayList<Object> currentParameter : parameterQueue) {
            String type = (String) currentParameter.get(0);
            String name = (String) currentParameter.get(1);
            int lineNr =  (int) currentParameter.get(2);
            this.uniqueName = this.belongsToClass + "." + this.belongsToMethod + "(" + this.paramTypesInSignature + ")." + name;
            String belongsToMethodToPassThrough = this.belongsToClass + "." + this.belongsToMethod + "(" + this.paramTypesInSignature + ")";
            if (SkippedTypes.isSkippable(type)) {
                modelService.createParameterOnly(name, uniqueName, type, belongsToClass, lineNr, belongsToMethodToPassThrough);
            } else {
                modelService.createParameter(name, uniqueName, type, belongsToClass, lineNr, belongsToMethodToPassThrough);
            }
        }
    }
}
