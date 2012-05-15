package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.JavaParser;

import org.antlr.runtime.tree.CommonTree;

class JavaAnnotationGenerator extends JavaGenerator{
	
	private String name = "";
	private String uniqueName = "";
	private String belongsToPackage = "";
	
	public JavaAnnotationGenerator(String uniquePackageName){
		this.belongsToPackage = uniquePackageName;
	}
	
	public String generateModel(CommonTree commonTree) {
		CommonTree identifierTree = (CommonTree) commonTree.getFirstChildWithType(JavaParser.IDENT);
		this.name = identifierTree.toString();
		
		this.uniqueName += !this.belongsToPackage.equals("") ? this.belongsToPackage + "." : "";
		this.uniqueName += this.name;
		
		modelService.createInterface(uniqueName, name, belongsToPackage);
		return uniqueName;
	}
	
	public void generateMethod(CommonTree commonTree){
		this.name = commonTree.getFirstChildWithType(JavaParser.IDENT).toString();
		
		this.uniqueName += !this.belongsToPackage.equals("") ? this.belongsToPackage + "." : "";
		this.uniqueName += this.name;
		
		int linenumber = commonTree.getLine();
		//modelService.createAttribute(false, "private", this.belongsToPackage, this.name, this.name, this.uniqueName, linenumber);
		modelService.createAnnotation(this.belongsToPackage, this.name, this.name, this.uniqueName, linenumber);
	}
}
