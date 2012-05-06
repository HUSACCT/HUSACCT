package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.JavaParser;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class JavaInheritanceDefinitionGenerator extends JavaGenerator{

	private String from;
	private String to;
	private int lineNumber;
	private String type;

	public void generateModelObject(CommonTree tree, String belongsToClass) {
		from = belongsToClass;
		createInheritanceDefinitionDetails(tree);
		createDomainObject();
	}

	private void createDomainObject() {
		modelService.createInheritanceDefinition(from, to, lineNumber, type);
	}

	private void createInheritanceDefinitionDetails(Tree tree) {
		if (tree != null) {
			for (int i = 0; i < tree.getChildCount(); i++) {
				if(tree.getType() == JavaParser.EXTENDS_CLAUSE){
					type = "extend";
					lineNumber = tree.getLine();
				}
				else if(tree.getType() == JavaParser.IMPLEMENTS_CLAUSE){
					type = "implement";
					lineNumber = tree.getLine();
				}
				if(tree.getType() == JavaParser.QUALIFIED_TYPE_IDENT ){
					to = tree.getChild(i).getText();
				}

				createInheritanceDefinitionDetails(tree.getChild(i));
			}
		}
	}
}