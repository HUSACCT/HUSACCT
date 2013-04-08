package husacct.analyse.task.analyser.java;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

class JavaLibraryGenerator extends JavaGenerator{
	
	public String generateModel(CommonTree treeNode) {
		String uniqueName = getUniqueNameOfLibrary(treeNode);
		String belongsToLibrary = getParentLibraryName(uniqueName);
		String name = getNameOfLibrary(uniqueName);
		createLibrary(name, uniqueName, belongsToLibrary);
		if(hasParentLibraries(uniqueName)) {
			createAllParentLibraries(belongsToLibrary);
		}
		return uniqueName;
	}
	
	private String getParentLibraryName(String completLibraryName){
		String[] allLibraries = splitLibraries(completLibraryName);
		String parentLibrary = "";
		for(int i = 0; i<allLibraries.length - 1; i++){
			if(parentLibrary == "") parentLibrary += allLibraries[i];
			else parentLibrary += "." + allLibraries[i];
		}
		return parentLibrary;
	}	
	
	private String getUniqueNameOfLibrary(Tree antlrTree){
		String uniqueName = antlrTree.toStringTree();
		uniqueName = uniqueName.replace("(import", "");
		uniqueName = uniqueName.replace("(","");
		uniqueName = uniqueName.replace(")", "");
		uniqueName = uniqueName.replace(". ","").substring(1); //trim first space character
		uniqueName = uniqueName.replace(" ", ".");
		return uniqueName;
	}
	
	private void createAllParentLibraries(String uniqueChildLibraryName){
		String belongsToLibrary = "";
		String name = "";
		if(hasParentLibraries(uniqueChildLibraryName)){
			belongsToLibrary = getParentLibraryName(uniqueChildLibraryName);
			name = getNameOfLibrary(uniqueChildLibraryName);
		}
		else{
			name = uniqueChildLibraryName;
		}
		createLibrary(name, uniqueChildLibraryName, belongsToLibrary);
		if(hasParentLibraries(uniqueChildLibraryName)){
			createAllParentLibraries(belongsToLibrary);
		}
	}
	
	private String getNameOfLibrary(String completeLibraryName){
		String[] allLibraries = splitLibraries(completeLibraryName);
		return allLibraries[allLibraries.length -1];
	}
	
	private String[] splitLibraries(String completeLibraryName){
		String escapedPoint = "\\.";
		return completeLibraryName.split(escapedPoint);
	}
	
	private void createLibrary(String name, String uniqueName, String belongsToLibrary){
		modelService.createLibrary(uniqueName, belongsToLibrary, name);
	}
	
	private boolean hasParentLibraries(String completeLibraryName){
		return completeLibraryName.contains(".");
	}
}
