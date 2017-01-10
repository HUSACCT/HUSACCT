package husacct.analyse.infrastructure.antlr;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.DOTTreeGenerator;
import static husacct.analyse.task.analyser.csharp.generators.CSharpGeneratorToolkit.tryToGetFilePath;

/***
 * Use this class to print you Antlr tree for debugging purposes
 */

public class TreePrinter {
	private static final String INDENT = "  ";
	
	public TreePrinter(CommonTree tree, String className) {
		System.out.println("<<<<<<-------------------->>>>>>");
		String fileName = tryToGetFilePath(tree);
		
		System.out.println("Class: " + className);
		System.out.println("File: " + fileName);
		System.out.println("Line: " + tree.getLine());
		
		System.out.println("\nSimple tree output:\n");
		printSimpleTreeOutput(tree, 0);
		
		System.out.println("--------------------");
		
		System.out.println("\nDetailed tree output:\n");
		printDetailedTreeOutput(tree);
		System.out.println("\n<<<<<<-------------------->>>>>>");
	}
	
	private void printDetailedTreeOutput(CommonTree tree) {
		DOTTreeGenerator gen = new DOTTreeGenerator();
		//System.out.println(gen.toDOT(tree));
		System.out.println("\n Use: http://graphviz-dev.appspot.com");
	}
	
	private void printSimpleTreeOutput(CommonTree tree, int indent) {
		System.out.println(indent(indent) + tree.getText());
		for (int i = 0; i < tree.getChildCount(); i++) {
			printSimpleTreeOutput((CommonTree)tree.getChild(i), ++indent);
		}
	}
	
	private String indent(int indent) {
		String result = "";
		for (int i = 0; i< indent; i++) {
			result += INDENT;
		}
		if (result.length() > 1)
			result = result.substring(0, result.length() - 2) + "+ ";
		return result;
	}
}  