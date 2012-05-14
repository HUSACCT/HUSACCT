package husacct.analyse.task.analyser.csharp;

import java.util.List;
import org.antlr.runtime.tree.CommonTree;

public class CSharpAttributeGenerator extends CSharpGenerator{
	List<CommonTree> attributeTrees;
	String uniqueClassName;
	boolean classScope = false;
	String accesControlQualifier = "";
	String attributeName;
	String declareType;
	public CSharpAttributeGenerator(List<CommonTree> attributeTrees, String uniqueClassName){
		this.attributeTrees = attributeTrees;
		this.uniqueClassName = uniqueClassName;
		
	}

	public void scan() {
		int childNumber = 0;
		for(CommonTree attributeChild : attributeTrees){
			childNumber++;
			if(attributeChild.equals("static")){
				classScope = true;
			}
			if(attributeChild.getType() == PRIVATE || attributeChild.getType() == PUBLIC || attributeChild.getType() == PROTECTED){
				accesControlQualifier = attributeChild.getText();
			}
			if(childNumber >= 3 && attributeChild.getType() == IDENTIFIER){
				attributeName = attributeChild.getText();
			}
			if(childNumber >= 2 && attributeChild.getType() == IDENTIFIER){
				declareType = attributeChild.getText();
			}			
		}
		if (attributeName != null) {
			modelService.createAttribute(classScope, accesControlQualifier, this.uniqueClassName, declareType, attributeName, (this.uniqueClassName + "." + attributeName),attributeTrees.get(0).getLine());
		}
		
	}
}
