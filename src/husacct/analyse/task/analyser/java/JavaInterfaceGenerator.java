package husacct.analyse.task.analyser.java;

import org.antlr.runtime.tree.CommonTree;

class JavaInterfaceGenerator extends JavaGenerator{
	
	private String name = "";
	private String uniqueName = "";
	private String belongsToPackage = "";
	
	public JavaInterfaceGenerator(String uniquePackageName){
		this.belongsToPackage = uniquePackageName;
	}
	
	public String generateModel(CommonTree commonTree) {
		
		if(commonTree == null){
			System.out.println("NULL");
		}
		
		
		this.name = commonTree.getChild(1).toString();
		if(belongsToPackage.equals("")) {
			this.uniqueName = commonTree.getChild(1).toString();
		}else{
			this.uniqueName = belongsToPackage + "." + commonTree.getChild(1).toString();
		}
		modelService.createInterface(uniqueName, name, belongsToPackage);
		return uniqueName;
	}
}
