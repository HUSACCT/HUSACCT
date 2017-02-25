package husacct.analyse.task.analyse.csharp.generators;

import husacct.analyse.task.analyse.csharp.generators.CSharpGeneratorToolkit;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import husacct.common.enums.DependencySubTypes;

import java.util.*;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class CSharpParameterGenerator extends CSharpGenerator {

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
    //private final Logger logger = Logger.getLogger(CSharpParameterGenerator.class);

	public String generateParameterObjects(CommonTree allParametersTree, String belongsToMethod, String belongsToClass) { // allParametersTree = FORMAL_PARAMETER_LIST
		String returnvalue = "";
        this.parameterQueue = new ArrayList<>();
		this.belongsToClass = belongsToClass;
		this.belongsToMethod = belongsToMethod;
        /* Test helper
       	if (this.belongsToClass.contains("DeclarationParameter")){
    		//if (belongsToMethod.contains("performExternalScript")) {
    				boolean breakpoint1 = true;
    		//}
    	} */

		if (allParametersTree != null) {
			lineNumber = allParametersTree.getLine();
			deriveParametersFromTree(allParametersTree);
			writeParametersToDomain();
			returnvalue = paramTypesInSignature;
		}
		return returnvalue;
	}

    private void deriveParametersFromTree(Tree allParametersTree) {
        int totalParameters = allParametersTree.getChildCount();
        for (int currentChild = 0; currentChild < totalParameters; currentChild++) {
            CommonTree child = (CommonTree) allParametersTree.getChild(currentChild);
            int treeType = child.getType();
            if (treeType == CSharpParser.FIXED_PARAMETER) {
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
        Tree parameterNameTree = tree.getFirstChildWithType(CSharpParser.IDENTIFIER);
        if (parameterNameTree != null) {
            this.declareName = parameterNameTree.getText();
            if ((this.declareName != null)&& (!this.declareName.trim().equals(""))) {
	            this.nameFound = true;
            }
        }
    }

    private void getTypeOfParameter(CommonTree tree) {
        CommonTree typeOfParameterTree = CSharpGeneratorToolkit.getFirstDescendantWithType(tree, CSharpParser.TYPE);
        if (typeOfParameterTree != null) {
        	CSharpInvocationGenerator cSharpInvocationGenerator = new CSharpInvocationGenerator(this.belongsToClass);
           	this.declareType = cSharpInvocationGenerator.getCompleteToString((CommonTree) typeOfParameterTree, belongsToClass, DependencySubTypes.DECL_PARAMETER);
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
        ArrayList<Object> myParam = new ArrayList<>();
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
            if (SkippableTypes.isSkippable(type)) {
                modelService.createParameterOnly(name, uniqueName, type, belongsToClass, lineNr, belongsToMethodToPassThrough);
            } else {
                modelService.createParameter(name, uniqueName, type, belongsToClass, lineNr, belongsToMethodToPassThrough);
            }
        }
    }

}
