package husacct.analyse.task.analyser.java;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

class JavaLibraryGenerator extends JavaGenerator{
	
	public String generateModel(CommonTree treeNode) {
		String uniqueName = getUniqueNameOfLibrary(treeNode);
		String belongsToLibrary = getParentLibraryName(uniqueName);
		String name = getNameOfLibrary(uniqueName);
		createLibrary(name, uniqueName, belongsToLibrary);
		if(hasParentLibraries(uniqueName)) {
			
			//createAllParentLibraries(belongsToLibrary);
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
		uniqueName = uniqueName.replace(". ","").substring(1);
		uniqueName = uniqueName.replace(" ", ".");
		return uniqueName;
	}
	
	/*private void createAllParentLibraries(String uniqueChildLibraryName){
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
	}*/
	
	private String[] getJarPath(String path) throws URISyntaxException, IOException {
		String strippedPath = path.substring(1);
		URL dirURL = getClass().getClassLoader().getResource(strippedPath);
		if (dirURL != null && dirURL.getProtocol().equals("file")) {
			/* A file path: easy enough */
			return new File(dirURL.toURI()).list();
		} 

		if (dirURL == null) {
			/* 
			 * In case of a jar file, we can't actually find a directory.
			 * Have to assume the same jar as clazz.
			 */
			String me = getClass().getName().replace(".", "/")+".class";
			dirURL = getClass().getClassLoader().getResource(me);
		}

		if (dirURL.getProtocol().equals("jar")) {
			/* A JAR path */
			String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
			JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
			Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
			Set<String> result = new HashSet<String>(); //avoid duplicates in case it is a subdirectory
			while(entries.hasMoreElements()) {
				String name = entries.nextElement().getName();
				if (name.startsWith(strippedPath)) { //filter according to the path
					String entry = name.substring(strippedPath.length());
					int checkSubdir = entry.indexOf("/");
					if (checkSubdir >= 0) {
						// if it is a subdirectory, we just return the directory name
						entry = entry.substring(0, checkSubdir);
					}
					result.add(entry);
				}
			}
			return result.toArray(new String[result.size()]);
		} 

		throw new UnsupportedOperationException("Cannot list files for URL "+dirURL);
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
