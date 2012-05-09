package husacct.analyse.task.analyser.csharp;

import java.util.List;
import org.antlr.runtime.tree.CommonTree;

public class CSharpAttributeGenerator {
	List<CommonTree> attributeTrees;
	String uniqueClassName;
	public CSharpAttributeGenerator(List<CommonTree> attributeTrees, String uniqueClassName){
		this.attributeTrees = attributeTrees;
		this.uniqueClassName = uniqueClassName;
		
	}

	public void scan() {
		// TODO Auto-generated method stub
		System.out.println(attributeTrees);
	}
}
