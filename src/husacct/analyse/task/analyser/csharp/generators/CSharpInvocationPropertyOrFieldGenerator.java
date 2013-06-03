package husacct.analyse.task.analyser.csharp.generators;

import static husacct.analyse.task.analyser.csharp.generators.CSharpGeneratorToolkit.getFirstDescendantWithType;

import org.antlr.runtime.tree.CommonTree;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import husacct.analyse.task.analyser.csharp.generators.AbstractCSharpInvocationGenerator;

public class CSharpInvocationPropertyOrFieldGenerator extends AbstractCSharpInvocationGenerator {
	private CSharpInvocationConstructorGenerator csInvocationConstructorGenerator;

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
		CommonTree toChild = (CommonTree)tree.getFirstChildWithType(CSharpParser.SIMPLE_NAME);
		this.to = toChild.getFirstChildWithType(CSharpParser.IDENTIFIER).getText();
		this.invocationName = tree.getFirstChildWithType(CSharpParser.IDENTIFIER).getText();
		this.nameOfInstance = toChild.getFirstChildWithType(CSharpParser.IDENTIFIER).getText();
	}

	@Override
	void saveInvocationToDomain() {
		modelService.createPropertyOrFieldInvocation(from, to, lineNumber, invocationName, belongsToMethod, nameOfInstance);
	}
}
