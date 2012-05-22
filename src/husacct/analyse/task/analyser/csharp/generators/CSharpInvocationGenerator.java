package husacct.analyse.task.analyser.csharp.generators;

import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class CSharpInvocationGenerator extends CSharpGenerator {
	private String uniqueClassName;
	
	public CSharpInvocationGenerator(String uniqueClassName){
		this.setUniqueClassName(uniqueClassName);
	}
	
	public void generateConstructorInvocation(List<CommonTree> tree, int lineNumber){
		
	}

	public String getUniqueClassName() {
		return uniqueClassName;
	}

	public void setUniqueClassName(String uniqueClassName) {
		this.uniqueClassName = uniqueClassName;
	}
}
