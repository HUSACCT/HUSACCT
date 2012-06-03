package husaccttest.analyse;

import java.io.File;

/*
 * This class have been developed in order to ensure test-code to find test projects which can be placed 
 * in the 'recognitiontests' folder. The only thing that has to be passed in the function that this class provides
 * is the name of the project and the programming language of this project. 
 */

public class TestProjectFinder {
	
	private static final String testprojectFolder = "testprojects";
	
	/**Function to find the current path, holding in account if the build is requesting the path or the normal 
	 * JUnit tests. Build tests will run from a different location, thus the path will be different. */
	public static String lookupProject(String languageFolder, String projectName){
		
		String buildPrefix = ".." + File.separator;
		String simplePath = testprojectFolder + File.separator + languageFolder + File.separator + projectName;
		String pathFromBuild = buildPrefix + simplePath;
		String[] possiblePaths = new String[]{simplePath, pathFromBuild};
		
		String selectedPath = simplePath;
		for(String path: possiblePaths){
			File directory = new File(path);
			if(directory.isDirectory()){
				selectedPath = path;
			}
		}
		return selectedPath + File.separator;
	}
}
