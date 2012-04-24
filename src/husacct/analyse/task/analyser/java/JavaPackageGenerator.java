package husacct.analyse.task.analyser.java;

import husacct.analyse.domain.ModelService;
import husacct.analyse.domain.famix.FamixModelServiceImpl;

import org.antlr.runtime.tree.CommonTree;

class JavaPackageGenerator{
	
	private ModelService modelService = new FamixModelServiceImpl();
	
	public String generateModel(CommonTree treeNode) {
		String currentPackage = treeNode.toStringTree();
		String uniqueName = getUniqueNameOfPackage(currentPackage);
		String belongsToPackage = getParentPackageName(uniqueName);
		String name = getNameOfPackage(uniqueName);
		createPackage(name, uniqueName, belongsToPackage);
		if(hasParentPackages(uniqueName)) {
			createAllParentPackages(belongsToPackage);
		}
		return uniqueName;
	}
	
	private void createAllParentPackages(String uniqueChildPackageName){
		String belongsToPackage = "";
		String name = "";
		if(hasParentPackages(uniqueChildPackageName)){
			belongsToPackage = getParentPackageName(uniqueChildPackageName);
			name = getNameOfPackage(uniqueChildPackageName);
		}
		else{
			name = uniqueChildPackageName;
		}
		createPackage(name, uniqueChildPackageName, belongsToPackage);
		if(hasParentPackages(uniqueChildPackageName)){
			createAllParentPackages(uniqueChildPackageName);
		}
	}
	
	private String getUniqueNameOfPackage(String antlrTree){
		String uniqueName = antlrTree;
		uniqueName = uniqueName.replace("(","");
		uniqueName = uniqueName.replace(")", "");
		uniqueName = uniqueName.replace(".","");
		uniqueName = uniqueName.replace(" ", ".");
		uniqueName = uniqueName.replace("..", ".");
		uniqueName = uniqueName.replace("package.", "");
		return uniqueName;
	}
	
	private String getParentPackageName(String completPackageName){
		String[] allPackages = splitPackages(completPackageName);
		String parentPackage = "";
		for(int i = 0; i<allPackages.length - 1; i++){
			if(parentPackage == "") parentPackage += allPackages[i];
			else parentPackage += "." + allPackages[i];
		}
		return parentPackage;
	}	
	
	private String getNameOfPackage(String completePackageName){
		String[] allPackages = splitPackages(completePackageName);
		return allPackages[allPackages.length - 1];
	}
	
	private String[] splitPackages(String completePackageName){
		String escapedPoint = "\\.";
		return completePackageName.split(escapedPoint);
	}
	
	private void createPackage(String name, String uniqueName, String belongsToPackage){
		modelService.createPackage(uniqueName, belongsToPackage, uniqueName);
	}
	
	private boolean hasParentPackages(String completePackageName){
		return splitPackages(completePackageName).length - 1 > 0;
	}
}
