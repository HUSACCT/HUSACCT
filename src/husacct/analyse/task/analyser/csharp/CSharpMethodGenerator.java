package husacct.analyse.task.analyser.csharp;

import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class CSharpMethodGenerator extends CSharpGenerator {

	public void generate(List<CommonTree> tree, String uniqueClassName) {
		String returnType = checkForReturnType(tree);
		
	}

	private String checkForReturnType(List<CommonTree> tree) {
		String returnType;
		int[] listOfPrimitiveTypes = new int[]{164/*int*/};
		// TODO Auto-generated method stub
		return null;
	}

}
