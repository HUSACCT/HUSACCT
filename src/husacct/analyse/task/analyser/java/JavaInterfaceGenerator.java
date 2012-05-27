package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.JavaParser;

import org.antlr.runtime.tree.CommonTree;

class JavaInterfaceGenerator extends JavaGenerator{
	
	private String name = "";
	private String uniqueName = "";
	private String belongsToPackage = "";
	private String visibility = "default";
	
	public JavaInterfaceGenerator(String uniquePackageName){
		this.belongsToPackage = uniquePackageName;
	}
	
	public String generateModel(CommonTree commonTree) {
		setVisibility(commonTree);
		this.name = commonTree.getChild(1).toString();
		if(belongsToPackage.equals("")) {
			this.uniqueName = commonTree.getChild(1).toString();
		}else{
			this.uniqueName = belongsToPackage + "." + commonTree.getChild(1).toString();
		}
		modelService.createInterface(uniqueName, name, belongsToPackage, visibility);
		return uniqueName;
	}
	
	private void setVisibility(CommonTree tree){
		CommonTree modifierTree = (CommonTree)tree.getFirstChildWithType(JavaParser.MODIFIER_LIST);
		if(modifierTree.getFirstChildWithType(JavaParser.PUBLIC) != null) this.visibility = "public";
		if(modifierTree.getFirstChildWithType(JavaParser.PRIVATE) != null) this.visibility = "private";
	}
}
