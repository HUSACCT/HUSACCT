package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.JavaParser;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class JavaImplementsDefinitionGenerator extends JavaGenerator{

	private String from;
	private String to = "";
	private int lineNumber;

	public void generateToDomain(CommonTree tree, String belongsToClass) {
		from = belongsToClass;
		createImplementsDetails(tree);
		createDomainObject();
	}

	private void createImplementsDetails(Tree tree) {
		if (tree != null) {
			for (int i = 0; i < tree.getChildCount(); i++) {
				if(tree.getType() == JavaParser.IMPLEMENTS_CLAUSE){
					lineNumber = tree.getLine();
				}
				if(tree.getType() == JavaParser.QUALIFIED_TYPE_IDENT ){
					to += tree.getChild(i).getText() + ".";
				}
				createImplementsDetails(tree.getChild(i));
			}
		}
	}
	
	private void createDomainObject() {
		to = to.substring(0, to.length() -1); //delete last point
		modelService.createImplementsDefinition(from, to, lineNumber);
	}
}