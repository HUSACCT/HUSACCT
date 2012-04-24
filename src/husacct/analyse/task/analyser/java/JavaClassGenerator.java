package husacct.analyse.task.analyser.java;

import java.util.List;

import husacct.analyse.infrastructure.antlr.JavaParser;

import org.antlr.runtime.tree.BaseTree;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

class JavaClassGenerator extends JavaGenerator{
	
	private static final int classNode = JavaParser.CLASS;
	private static final int abstractNode = JavaParser.ABSTRACT;
	
	private String name = "";
	private String uniqueName = "";
	private String belongsToPackage = "";
	
	private boolean isInnerClass = false; //TODO Goed implementeren. 
	private boolean isAbstract = false; 
	
	public JavaClassGenerator(String uniquePackageName){
		this.belongsToPackage = uniquePackageName;
	}
	
	public String generateModel(CommonTree commonTree) {
		this.name = commonTree.getChild(1).toString();
		this.uniqueName = belongsToPackage + "." + commonTree.getChild(1).toString();
		this.isInnerClass = isInnerClass(commonTree);
		this.isAbstract = isAbstract(commonTree);
		//TODO InnerClasses
		if(isInnerClass){
			modelService.createClass(uniqueName, name, belongsToPackage, isAbstract, isInnerClass);
		}else{
			modelService.createClass(uniqueName, name, belongsToPackage, isAbstract, isInnerClass, this.uniqueName);
		}
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
	
	private boolean hasInnerClasses(CommonTree classTree){
//		for(int i=0; i<classTree.getChildCount(); i++){
//		if(classTree.getChild(i).getType() == classNode){
//			return true;
//		}
		return false;
	}
	
	private boolean isInnerClass(CommonTree classTree){
//		for(int i=0; i<classTree.getChildCount(); i++){
//			if(classTree.getChild(i).getType() == classNode){
//				return true;
//			}
//		}
		return false;
	}
}
