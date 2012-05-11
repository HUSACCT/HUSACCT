package husacct.analyse.task.analyser.csharp;

import java.util.List;
import org.antlr.runtime.tree.CommonTree;

public class CSharpAttributeGenerator extends CSharpGenerator{
	List<CommonTree> attributeTrees;
	String uniqueClassName;
	public CSharpAttributeGenerator(List<CommonTree> attributeTrees, String uniqueClassName){
		this.attributeTrees = attributeTrees;
		this.uniqueClassName = uniqueClassName;
		
	}

	public void scan() {
		//System.out.println(attributeTrees);
		//modelService.createAttribute(classScope, accesControlQualifier, belongsToClass, declareType, name, uniqueName)
		//modelService.createAttribute(classScope, accesControlQualifier, belongsToClass, declareType, name, uniqueName, lineNumber)
	}
}
