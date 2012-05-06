package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.JavaParser;

import java.util.List;

import org.antlr.runtime.tree.BaseTree;
import org.antlr.runtime.tree.CommonTree;

class JavaClassGenerator extends JavaGenerator{
	
	private static final int classNode = JavaParser.CLASS;
	private static final int abstractNode = JavaParser.ABSTRACT;
	
	private String name = "";
	private String uniqueName = "";
	private String belongsToPackage = "";
	private String belongsToClass = null;
	
	private boolean isInnerClass = false; //TODO Goed implementeren. 
	private boolean isAbstract = false; 
	
	public JavaClassGenerator(String uniquePackageName){
		this.belongsToPackage = uniquePackageName;
	}
	
	public String generateModel(CommonTree commonTree) {
		this.name = commonTree.getChild(1).toString();
		if(belongsToPackage.equals("")) {
			this.uniqueName = commonTree.getChild(1).toString();
		}else{
			this.uniqueName = belongsToPackage + "." + commonTree.getChild(1).toString();
		}
		this.isAbstract = isAbstract(commonTree);
		modelService.createClass(uniqueName, name, belongsToPackage, isAbstract, isInnerClass);
		return uniqueName;
	}
	
	public String generateModel(CommonTree commonTree, String parentClassName) {
		this.name = commonTree.getChild(1).toString();
		this.uniqueName = belongsToPackage + "." + commonTree.getChild(1).toString();
		this.isInnerClass = true;
		this.isAbstract = isAbstract(commonTree);
		this.isInnerClass = true;
		this.belongsToClass = parentClassName;
		modelService.createClass(uniqueName, name, belongsToPackage, isAbstract, isInnerClass, belongsToClass);
		return uniqueName;
	}
	
	private boolean isAbstract(CommonTree tree){
		List<BaseTree> trees = tree.getChildren();
		if(trees != null){
			for(BaseTree aTree: trees){
				if(aTree.getFirstChildWithType(abstractNode) != null) return true;
			}
		}
		return false;
	}
}
