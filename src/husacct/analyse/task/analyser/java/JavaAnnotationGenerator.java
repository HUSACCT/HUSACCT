package husacct.analyse.task.analyser.java;

import java.util.List;

import husacct.analyse.infrastructure.antlr.java.JavaParser;

import org.antlr.runtime.tree.BaseTree;
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
		if(commonTree.getFirstChildWithType(JavaParser.IDENT) != null){
			this.name = commonTree.getFirstChildWithType(JavaParser.IDENT).toString();
		}else{
			CommonTree nameTree = (CommonTree)commonTree.getChild(0);
			this.name = generateName(nameTree);
		}
		this.uniqueName += !this.belongsToPackage.equals("") ? this.belongsToPackage + "." : "";
		this.uniqueName += this.name;
		int linenumber = commonTree.getLine();
		modelService.createAnnotation(this.belongsToPackage, this.name, this.name, this.uniqueName, linenumber);
	}
	
	private String generateName(CommonTree nameTree){
		String result = "";
		if(nameTree != null){
			List<BaseTree> children = nameTree.getChildren();
			if(children != null){
				for(BaseTree tree: children){
					result += tree.getText();
					List<CommonTree> innerChildren = (List<CommonTree>)tree.getChildren();
					if(innerChildren != null){
						for(CommonTree innerChild : innerChildren){
							result += generateName(innerChild);
						}
					}
				}
			}
		}
		return result.replace(".", "");
	}
}
