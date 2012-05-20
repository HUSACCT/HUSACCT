package husacct.analyse.task.analyser.csharp.generators;

import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class CSharpInvocationGenerator extends CSharpGenerator {
	private String uniqueClassName;
	
	public CSharpInvocationGenerator(String uniqueClassName){
		this.uniqueClassName = uniqueClassName;
	}
	
	public void generateConstructorInvocation(List<CommonTree> tree, int lineNumber){
		
	}
}
