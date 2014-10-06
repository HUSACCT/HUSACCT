package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import static husacct.analyse.task.analyser.csharp.generators.CSharpGeneratorToolkit.*;

import org.antlr.runtime.tree.CommonTree;
import org.apache.log4j.Logger;

public class CSharpClassGenerator extends CSharpGenerator {
    private static final Logger logger = Logger.getLogger(CSharpGenerator.class);


    public String generateToDomain(String sourceFilePath, int numberOfLinesOfCode, CommonTree classTree, String namespace, boolean isInterface) {
        String name = getClassName(classTree);
        String uniqueName = getUniqueName(namespace, name);
        String visibility = getVisibility(classTree);
        boolean isAbstract = isAbstract(classTree);
        modelService.createClass(sourceFilePath, numberOfLinesOfCode, uniqueName, name, namespace, isAbstract, false, "", visibility, isInterface);
        return name;
    }

    public String generateToModel(String sourceFilePath, int numberOfLinesOfCode, CommonTree classTree, String namespace, String parentClassNames, boolean isInterface) {
        String name = getClassName(classTree);
	        String belongsToClass = belongsToClass(namespace, parentClassNames);
	        String uniqueName = getUniqueName(belongsToClass, name);
	        String visibility = getVisibility(classTree);
	        boolean isAbstract = isAbstract(classTree);
	
	        modelService.createClass(sourceFilePath, numberOfLinesOfCode, uniqueName, name, namespace, isAbstract, true, belongsToClass, visibility, isInterface);
        return name;
    }

    private String getClassName(CommonTree classTree) {
    	String returnValue = null;
		try {
			int nrOfChildren = classTree.getChildCount();
	        for (int i = 0; i < nrOfChildren; i++) {
	            if (classTree.getChild(i).getType() == CSharpParser.IDENTIFIER) {
	                CommonTree mTree = (CommonTree) classTree.getChild(i);
	                returnValue = mTree.token.getText();
	            }
	        }
		} catch (Exception e) {
	        logger.warn("Exception: "  + e + ", in getTypeNameAndParts()");
	        //e.printStackTrace();
		}
        return returnValue;
    }
    
}
