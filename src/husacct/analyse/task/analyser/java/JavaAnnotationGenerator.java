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
	
	public String generateToDomain(CommonTree commonTree) {
		setName(commonTree);
		setUniquename();
		modelService.createInterface(uniqueName, name, belongsToPackage);
		return this.uniqueName;
	}
	
	public void generateMethod(CommonTree commonTree){
		setName(commonTree);
		setUniquename();
		int linenumber = commonTree.getLine();
		modelService.createAnnotation(this.belongsToPackage, this.name, this.name, this.uniqueName, linenumber);
	}

	private void setName(CommonTree commonTree){
		CommonTree IdentNode = (CommonTree) commonTree.getFirstChildWithType(JavaParser.IDENT);
		if(IdentNode != null){
			this.name = IdentNode.getText();
		} else {
			CommonTree nameTree = (CommonTree)commonTree.getFirstChildWithType(JavaParser.DOT);
			this.name = parseDottedNameToString(nameTree);
		}
	}
	
	private String parseDottedNameToString(CommonTree nameTree){
		String result = "";
		
		if(nameTree == null){
			return "";
		}
		
		CommonTree dottedChild = (CommonTree) nameTree.getFirstChildWithType(JavaParser.DOT); 
		if(dottedChild != null){
			result += parseDottedNameToString(dottedChild);
		}
		
		int childCount = nameTree.getChildCount();
		for(int currentChild = 0; currentChild < childCount; currentChild++){
			CommonTree childNode = (CommonTree) nameTree.getChild(currentChild);
			if(childNode.getType() == JavaParser.IDENT){
				result += result.equals("") ? "" : ".";
				result += childNode.getText();
			}
		}
		return result;
	}
	
	private void setUniquename(){
		this.uniqueName += this.belongsToPackage + getDotForUniquename();
		this.uniqueName += this.name;
	}
	
	private String getDotForUniquename(){
		if(!this.belongsToPackage.equals("")){
			return ".";
		}
		return "";
	}
	
	
}
