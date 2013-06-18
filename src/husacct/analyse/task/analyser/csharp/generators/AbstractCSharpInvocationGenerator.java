package husacct.analyse.task.analyser.csharp.generators;

import static husacct.analyse.task.analyser.csharp.generators.CSharpGeneratorToolkit.*;
import java.util.ArrayList;
import java.util.Collections;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import org.antlr.runtime.tree.CommonTree;

public abstract class AbstractCSharpInvocationGenerator extends CSharpGenerator {
	protected String from = "";
	protected String to = "";
	protected int lineNumber;
	protected String invocationName = "";
	protected String belongsToMethod = "";
	protected String nameOfInstance = "";
	private ArrayList<String> toNames = new ArrayList<String>();
	
	public AbstractCSharpInvocationGenerator(String packageAndClassName) {
		from = packageAndClassName;
	}
	
	abstract void saveInvocationToDomain();
	
	protected String setToName(CommonTree treeNode) {
		String toName = "";
		walkTreeToGetNames(treeNode);	
		
		Collections.reverse(toNames);
		
		for (String name : toNames) {
			toName += name + ".";
		}
		toNames.clear();
		toName = removeLastDot(toName);
		return toName;
	}
	
	protected CommonTree getAccessToClassTree(CommonTree tree) {
		CommonTree accessToClass = getFirstDescendantWithType(tree, CSharpParser.SIMPLE_NAME);
		
		if (accessToClass == null) {
			accessToClass = getAccessToClassTreeWithKeywordThis(tree);
		}
		return accessToClass;
	}
	
	private void walkTreeToGetNames(CommonTree treeNode) {
		for (int i = 0; i < treeNode.getChildCount(); i++) {
			CommonTree child = (CommonTree)treeNode.getChild(i);
			switch (child.getType()) {
			case CSharpParser.SIMPLE_NAME:
			case CSharpParser.MEMBER_ACCESS:	
				String name = child.getFirstChildWithType(CSharpParser.IDENTIFIER).getText();
				this.toNames.add(name);
				break;
			}
			walkTreeToGetNames(child);
		}
	}
	
	private String removeLastDot(String toName) {
		return toName.endsWith(".") ? toName.substring(0, toName.length() -1) : toName;
	}
	
	private CommonTree getAccessToClassTreeWithKeywordThis(CommonTree tree) {
		CommonTree accessToClass = tree; 
		while(hasChild(accessToClass, CSharpParser.MEMBER_ACCESS)) {
			accessToClass = (CommonTree)accessToClass.getFirstChildWithType(CSharpParser.MEMBER_ACCESS);	
		}
		return accessToClass;
	}
}