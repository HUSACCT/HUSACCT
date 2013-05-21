package husacct.analyse.task.analyser.csharp.generators.buffers;

import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import java.util.List;
import org.antlr.runtime.tree.CommonTree;
import static husacct.analyse.task.analyser.csharp.generators.CSharpGeneratorToolkit.*;
import husacct.analyse.task.analyser.csharp.generators.CSharpParameterGenerator;
import java.util.Stack;

public class DelegateBuffer {
	public final String packageAndClassName;
	public String returntype;
	public String name;
	public Stack<String> argtypes;
	
	public DelegateBuffer(String packageAndClassName) {
		this.packageAndClassName  = packageAndClassName;
	}
	
	public DelegateBuffer store(CommonTree delegateTree) {
		System.out.println(delegateTree.toStringTree());
		name = getName(delegateTree);
		returntype = getReturnType(delegateTree);
		argtypes = handleParameters(delegateTree);
		
		return this;
	}
	
	private String getName(CommonTree tree) {
		return walkTree(tree, CSharpParser.IDENTIFIER).getText();
	}

	private String getReturnType(CommonTree tree) {
		CommonTree typeTree = walkTree(tree, CSharpParser.TYPE);
		return getTypeNameAndParts(typeTree);
	}
	
	private Stack<String> handleParameters(CommonTree tree) {
		CSharpParameterGenerator csParamGenerator = new CSharpParameterGenerator();
		return csParamGenerator.generateParameterObjects(tree, name, packageAndClassName);
	}
}
