package husacct.analyse.task.analyser.csharp.generators;



import java.util.ArrayList;

import husacct.analyse.infrastructure.antlr.TreePrinter;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import static husacct.analyse.task.analyser.csharp.generators.CSharpGeneratorToolkit.*;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.apache.log4j.Logger;

public class CSharpInvocationGenerator extends CSharpGenerator {
	private String from = "";
	private String to = "";
	private int lineNumber;
	private String invocationName = "";
	private String belongsToMethod = "";
	private String nameOfInstance = "";
	private boolean invocationNameFound;
	private boolean constructorInMethodInvocationFound = false;
	private boolean foundAllMethodInvocInfo = false;
	private boolean allIdents = true;
	private ArrayList<String> toNames = new ArrayList<String>();
	
	
	public CSharpInvocationGenerator(String uniqueClassName) {
		from = uniqueClassName;
	}

	public void generateConstructorInvocToDomain(CommonTree treeNode, String belongsToMethod) {
		invocationName = "Constructor";
		this.belongsToMethod = belongsToMethod;
		
		findConstructorInvocation(treeNode);
		createConstructorInvocationDomainObject();
	}
	
	public void generateMethodInvocToDomain(CommonTree treeNode, String belongsToMethod) {
		invocationNameFound = false;
		constructorInMethodInvocationFound = false;
		allIdents = true;
		this.belongsToMethod = belongsToMethod;
		lineNumber = treeNode.getLine();
		
		findMethodInvocation(treeNode);
	}
	
	public void generatePropertyOrFieldInvocToDomain(CommonTree tree,String belongsToMethod) {
		invocationNameFound = false;
        allIdents = true;
        this.belongsToMethod = belongsToMethod;	
        
        createPropertyOrFieldInvocationDetails(tree);
        createPropertyOrFieldInvocationDomainObject();
	}

	private void findMethodInvocation(CommonTree tree) {
		if (tree.getChildCount() <= 0){
			return;
		}
		for (int i = 0; i < tree.getChildCount(); i++) {
			CommonTree child = (CommonTree)tree.getChild(i);
			switch (child.getType()) {
			case CSharpParser.MEMBER_ACCESS:
				createMethodInvocationDetails(child);
				createMethodInvocationDomainObject();
			}
		}
	}

	private void createMethodInvocationDetails(CommonTree child) {
		CommonTree toChild = (CommonTree)child.getFirstChildWithType(CSharpParser.SIMPLE_NAME);
		this.to = toChild.getFirstChildWithType(CSharpParser.IDENTIFIER).getText();
		this.invocationName = child.getFirstChildWithType(CSharpParser.IDENTIFIER).getText();
		this.nameOfInstance = toChild.getFirstChildWithType(CSharpParser.IDENTIFIER).getText();
	}

	private void createPropertyOrFieldInvocationDetails(CommonTree child) {
		CommonTree toChild = (CommonTree)child.getFirstChildWithType(CSharpParser.SIMPLE_NAME);
		this.to = toChild.getFirstChildWithType(CSharpParser.IDENTIFIER).getText();
		this.invocationName = child.getFirstChildWithType(CSharpParser.IDENTIFIER).getText();
		this.nameOfInstance = toChild.getFirstChildWithType(CSharpParser.IDENTIFIER).getText();
		this.lineNumber = toChild.getLine();
	}

	private void createPropertyOrFieldInvocationDomainObject() {
		modelService.createPropertyOrFieldInvocation(from, to, lineNumber, invocationName, belongsToMethod, nameOfInstance);
	}

	private void findConstructorInvocation(CommonTree tree) {
		boolean constructorFound = hasConstructorCall(tree);
		if (constructorFound){
			createConstructorInvocationDetails((CommonTree) tree);
		}
		else {
			int childcount = tree.getChildCount();
			for (int i = 0; i < childcount; i++) {
				CommonTree child =  (CommonTree) tree.getChild(i);
				findConstructorInvocation(child);
			}
		}
	}
		
	
	private void createConstructorInvocationDetails(CommonTree tree) {	
		this.to = setToName(tree);
		this.lineNumber = tree.getChild(0).getLine();

	}
	
	private String setToName(CommonTree tree) {
		String toName = "";
		walkTreeToGetNames(tree);	
		
		for (String name : toNames) {
			toName += name + ".";
		}
		toName = toName.substring(0, toName.length()-1); // remove last dot
		
		return toName;
	}
	
	private void walkTreeToGetNames(CommonTree tree) {
		for (int i = 0; i < tree.getChildCount(); i++) {
			CommonTree child = (CommonTree)tree.getChild(i);
			switch (child.getType()) {
			case CSharpParser.NAMESPACE_OR_TYPE_NAME:
			case CSharpParser.NAMESPACE_OR_TYPE_PART:	
				String name = child.getFirstChildWithType(CSharpParser.IDENTIFIER).getText();
				this.toNames.add(name);
				break;
			}
			walkTreeToGetNames(child);
		}	
	}

	private void createConstructorInvocationDomainObject() {
		modelService.createConstructorInvocation(from, to, lineNumber, invocationName, belongsToMethod, nameOfInstance);
	}
	
	private void createMethodInvocationDomainObject() {
		modelService.createMethodInvocation(from, to, lineNumber, invocationName, belongsToMethod, nameOfInstance);
	}
	
	private boolean hasConstructorCall(CommonTree tree) {
		return tree.getType() == CSharpParser.OBJECT_CREATION_EXPRESSION;
	}
}
