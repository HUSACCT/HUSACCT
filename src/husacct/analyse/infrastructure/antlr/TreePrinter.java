package husacct.analyse.infrastructure.antlr;

import org.antlr.runtime.tree.CommonTree;

public class TreePrinter {
	private static final String INDENT = "  ";
	
	public TreePrinter(CommonTree tree) {
		printTreeOnLevel(tree, 0);
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
	
	private void printTreeOnLevel(CommonTree tree, int indent) {
		System.out.println(indent(indent) + tree.getText());
		for (int i = 0; i < tree.getChildCount(); i++) {
			printTreeOnLevel((CommonTree)tree.getChild(i), ++indent);
		}
	}
}