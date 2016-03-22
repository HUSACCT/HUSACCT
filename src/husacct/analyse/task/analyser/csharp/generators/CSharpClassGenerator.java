package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import static husacct.analyse.task.analyser.csharp.generators.CSharpGeneratorToolkit.*;

import org.antlr.runtime.tree.CommonTree;
import org.apache.log4j.Logger;

public class CSharpClassGenerator extends CSharpGenerator {
    private static final Logger logger = Logger.getLogger(CSharpGenerator.class);


    public String generateToDomain(String sourceFilePath, int numberOfLinesOfCode, CommonTree classTree, String namespace, boolean isInterface, boolean isEnumeration) {
        String name = getClassName(classTree);
        String uniqueName = getUniqueName(namespace, name);
        String visibility = getVisibility(classTree);
        boolean isAbstract = isAbstract(classTree);
        modelService.createClass(sourceFilePath, numberOfLinesOfCode, uniqueName, name, namespace, isAbstract, false, "", visibility, isInterface, isEnumeration);
        return name;
    }

    public String generateToModel(String sourceFilePath, int numberOfLinesOfCode, CommonTree classTree, String namespace, String parentClassNames, boolean isInterface, boolean isEnumeration) {
        String name = getClassName(classTree);
	        String belongsToClass = belongsToClass(namespace, parentClassNames);
	        String uniqueName = getUniqueName(belongsToClass, name);
	        String visibility = getVisibility(classTree);
	        boolean isAbstract = isAbstract(classTree);
	        modelService.createClass(sourceFilePath, numberOfLinesOfCode, uniqueName, name, namespace, isAbstract, true, belongsToClass, visibility, isInterface, isEnumeration);
        return name;
    }

    private String getClassName(CommonTree classTree) {
    	String returnValue = null;
    	String parameters = "";
		try {
			int nrOfChildren = classTree.getChildCount();
	        for (int i = 0; i < nrOfChildren; i++) {
	            if (classTree.getChild(i).getType() == CSharpParser.IDENTIFIER) {
	                CommonTree mTree = (CommonTree) classTree.getChild(i);
	                returnValue = mTree.token.getText();
	            } else if ((classTree.getChild(i).getType() == CSharpParser.TYPE_PARAMETERS) || (classTree.getChild(i).getType() == CSharpParser.VARIANT_TYPE_PARAMETERS)) { // In case of generic classes or interfaces, add the parameters as ,p1>, <p1, p2>, etc.
	            	CommonTree parametersTree = (CommonTree) classTree.getChild(i);
	            	int nrOfParameters = parametersTree.getChildCount();
	            	if (nrOfParameters > 0) {
	            		for (int f = 0; f < nrOfParameters; f++) {
			            	CommonTree parameterTree = (CommonTree) parametersTree.getChild(f);
			            	boolean childFound = false;
			            	if (parameterTree.getType() == CSharpParser.IDENTIFIER) {
			            		childFound = true;
			            	} else {
			            		CommonTree IdentTree = (CommonTree) parameterTree.getFirstChildWithType(CSharpParser.IDENTIFIER);
			            		if (IdentTree != null) {
			            			childFound = true;
			            		}
			            	}
			            	if (childFound) {
			            		if (f == 0) {
			            			parameters += "p" + 1;
			            		} else {
			            			parameters += ", p" + (f+1);
			            		}
			            	}
	            		}
	            	}
            		returnValue += "<"+ parameters + ">";
	            }
	        }
		} catch (Exception e) {
	        logger.warn("Exception: "  + e + ", in getTypeNameAndParts()");
	        //e.printStackTrace();
		}
        return returnValue;
    }
    
}
