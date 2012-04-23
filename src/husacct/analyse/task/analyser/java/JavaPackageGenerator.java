package husacct.analyse.task.analyser.java;

import husacct.analyse.domain.FamixModelServiceImpl;
import husacct.analyse.domain.ModelService;
import husacct.analyse.domain.famix.FamixPackage;

import org.antlr.runtime.tree.CommonTree;
class JavaPackageGenerator{

	String name = "";
	String belongsToPackage = "";
	String packageName = "";
	
	Boolean nameSet = false;
	FamixPackage famixPackage = new FamixPackage();
	final int DOT = 15;
	final int INDENTIFIER = 164;

	public void generateFamix(CommonTree packageNode) {
			if(packageNode.getChild(0).getType() == DOT && packageNode.getChild(1) == null){
				generateFamix((CommonTree)packageNode.getChild(0));
			}else if(packageNode.getChild(0).getType() == INDENTIFIER && packageNode.getChild(1) == null){
				name = packageNode.getChild(0).toString();
			}else{
				if(packageNode.getChild(0) != null && packageNode.getChild(0).getType() == INDENTIFIER && nameSet != true){
					belongsToPackage = packageNode.getChild(0).toString();
					name = packageNode.getChild(1).toString();
					nameSet = true;
				}else if(packageNode.getChild(0) != null && packageNode.getChild(0).getType() == DOT && nameSet != true){
					name = packageNode.getChild(1).toString();
					nameSet = true;
					generateFamix((CommonTree) packageNode.getChild(0));				
				}else if (packageNode.getChild(0) != null && (packageNode.getChild(0).getType() == INDENTIFIER || packageNode.getChild(0).getType() == DOT) ){
					if(packageNode.getChild(0).getType() == DOT){
						belongsToPackage = packageNode.getChild(1).toString() + "." + belongsToPackage; 
						generateFamix((CommonTree) packageNode.getChild(0));	
					}else{
						belongsToPackage = packageNode.getChild(1).toString() + "." + belongsToPackage; 
						belongsToPackage = packageNode.getChild(0).toString() + "." + belongsToPackage; 
					}
				}
			}
	}

	public String getName() {
		return name;
	}

	public String getBelongsToPackage() {
		return belongsToPackage;
	}
	
	public String getUniqueName(){
		return belongsToPackage + "." + name;
	}
	
}
