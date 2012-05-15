package husacct.analyse.task.analyser.csharp;

import java.util.List;
import org.antlr.runtime.tree.CommonTree;

public class CSharpAttributeGenerator extends CSharpGenerator{
	private List<CommonTree> attributeTrees;
	private String uniqueClassName;
	private boolean classScope = false;
	private String accesControlQualifier = "";
	private String attributeName;
	private String declareType;
	

	public CSharpAttributeGenerator(List<CommonTree> attributeTrees, String uniqueClassName){
		this.attributeTrees = attributeTrees;
		this.uniqueClassName = uniqueClassName;
	}

	public void scan() {
		boolean hadType = false;
		boolean hadName = false;
		for(CommonTree attributeChild : attributeTrees){
			int type = attributeChild.getType();
			if(type == STATIC){
				classScope = true;
			}
			if(type == PRIVATE || type == PUBLIC || type == PROTECTED || type == INTERNAL){
				accesControlQualifier += attributeChild.getText() + " ";
			}
			if(hadType && type == IDENTIFIER){
				attributeName = attributeChild.getText();
				hadName = true;
			}
			if (type == BYTE || type == SBYTE || type == INT || type == UINT 
					|| type == SHORT || type == USHORT || type == LONG ||
					type == ULONG || type == FLOAT || type == DOUBLE || type == CHAR
					|| type == BOOL || type == OBJECT || type == STRING || type 
					== DECIMAL || type == VAR || type == IDENTIFIER) {
				declareType = attributeChild.getText();
				hadType = true;
			}
			if (type == EQUALS) {
				if (!hadName) {
					return;
				}
			}
		}
		if (attributeName != null) {
			if (accesControlQualifier == "") {
				accesControlQualifier = "internal";
			} else {
				accesControlQualifier = accesControlQualifier.trim();
			}
			modelService.createAttribute(classScope, accesControlQualifier, this.uniqueClassName, declareType, attributeName, (this.uniqueClassName + "." + attributeName),attributeTrees.get(0).getLine());
		} 
	}
}