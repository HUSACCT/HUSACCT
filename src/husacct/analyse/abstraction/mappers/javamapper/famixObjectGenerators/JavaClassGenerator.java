package husacct.analyse.abstraction.mappers.javamapper.famixObjectGenerators;

import husacct.analyse.domain.famix.FamixClass;

import org.antlr.runtime.tree.CommonTree;

public class JavaClassGenerator extends JavaGenerator {
	FamixClass famixClass = new FamixClass();
	String packageName = "";
	String uniqueName = "";
	
	public FamixClass generateFamix(CommonTree commonTree) {
		famixClass.setName(commonTree.getChild(1).toString());
		famixClass.setAbstract(false);
		famixClass.setBelongsToPackage(this.packageName);
		famixClass.setUniqueName(uniqueName+"."+commonTree.getChild(1).toString());
		return famixClass;
	}
	public void setPackageNameAndUniqueName(String packageName, String uniqueName){
		this.packageName = packageName;
		this.uniqueName = uniqueName;
	}
	
}
