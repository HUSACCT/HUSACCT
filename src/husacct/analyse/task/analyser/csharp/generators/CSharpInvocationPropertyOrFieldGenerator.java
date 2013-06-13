package husacct.analyse.task.analyser.csharp.generators;

import static husacct.analyse.task.analyser.csharp.generators.CSharpGeneratorToolkit.getFirstDescendantWithType;
import static husacct.analyse.task.analyser.csharp.generators.CSharpGeneratorToolkit.tryToGetFilePath;

import java.util.ArrayList;
import java.util.Collections;

import org.antlr.runtime.tree.CommonTree;
import husacct.analyse.infrastructure.antlr.TreePrinter;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import husacct.analyse.task.analyser.csharp.generators.AbstractCSharpInvocationGenerator;

public class CSharpInvocationPropertyOrFieldGenerator extends AbstractCSharpInvocationGenerator {
	private CSharpInvocationConstructorGenerator csInvocationConstructorGenerator;
	private ArrayList<String> toNames = new ArrayList<String>();

	public CSharpInvocationPropertyOrFieldGenerator(String packageAndClassName) {
		super(packageAndClassName);
	}
	
	public void generatePropertyOrFieldInvocToDomain(CommonTree tree,String belongsToMethod) {
        this.belongsToMethod = belongsToMethod;	
        this.lineNumber = tree.getLine();
        
        determineInvocationType(tree);
        saveInvocationToDomain();        
	}

	private void determineInvocationType(CommonTree tree) {
		CommonTree constructorTree = findConstructorInvocation(tree);
		if (constructorTree != null){
			delegateConstructor(tree);
		}
		else{
			createPropertyOrFieldInvocationDetails(tree);
		}
	}
	
	private void delegateConstructor(CommonTree tree) {
		csInvocationConstructorGenerator = new CSharpInvocationConstructorGenerator(this.from);
		csInvocationConstructorGenerator.generateConstructorInvocToDomain(tree, this.belongsToMethod);
		createPropertyOrFieldInvocationWithConstructorDetails(tree);
	}
	
	private void createPropertyOrFieldInvocationWithConstructorDetails(CommonTree tree) {
		invocationName = tree.getFirstChildWithType(CSharpParser.IDENTIFIER).getText();
		to = csInvocationConstructorGenerator.to;
		nameOfInstance = to;
	}

	private CommonTree findConstructorInvocation(CommonTree tree) {
		return getFirstDescendantWithType(tree, CSharpParser.OBJECT_CREATION_EXPRESSION);
	}

	private void createPropertyOrFieldInvocationDetails(CommonTree tree) {
		try {
			this.invocationName = tree.getFirstChildWithType(CSharpParser.IDENTIFIER).getText();
			
			CommonTree accessToClass = getFirstDescendantWithType(tree, CSharpParser.SIMPLE_NAME);

			this.to = setToName(tree);
			this.nameOfInstance = accessToClass.getFirstChildWithType(CSharpParser.IDENTIFIER).getText();	
		} catch (Exception e) {
			System.out.println("Could not detect dependency on line " + this.lineNumber +  " in: " + tryToGetFilePath(tree));
		}
	}
	
	private String setToName(CommonTree treeNode) {
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

	@Override
	void saveInvocationToDomain() {
		modelService.createPropertyOrFieldInvocation(from, to, lineNumber, invocationName, belongsToMethod, nameOfInstance);
	}
}
