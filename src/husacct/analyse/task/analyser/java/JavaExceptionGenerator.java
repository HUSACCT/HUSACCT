package husacct.analyse.task.analyser.java;

import org.antlr.runtime.tree.CommonTree;

class JavaExceptionGenerator extends JavaGenerator{
	
	public void generateModel(CommonTree tree){
		System.out.println(tree.toStringTree());
	}
	
}
