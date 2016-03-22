package husaccttest;

import java.io.File;

/*
 * This class have been developed in order to ensure test-code to find test projects which can be placed 
 * in the 'testresources' folder. The only data that has to be passed in the function that this class provides
 * is the name of the project and the programming language of this project. 
 */

public class TestResourceFinder {
	
	private static final String testprojectFolder = "testresources";
	
	/**Function to find the current source path, holding in account if the build or the normal JUnit tests is requesting the path. 
	 * Build tests will run from a different location, thus the path will be different. */
	public static String findSourceCodeDirectory(String languageFolder, String projectName){
		
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

	/**Function to find the workspace file, holding in account if the build or the normal JUnit tests is requesting the path. 
	 * Build tests will run from a different location, thus the path will be different. */
	// Requires two versions of the workspace: 1) JUnit version with project path = directory name within projectResources; 2) Build version with the project path = "..\" + directory name within projectResources
	public static String findHusacctWorkspace(String languageFolder, String workspaceName){
		
		String buildPrefix = ".." + File.separator;
		String simplePath = testprojectFolder + File.separator + languageFolder + File.separator + "workspaces" + File.separator + workspaceName;
		String pathFromBuild = buildPrefix + simplePath;
		
		String selectedPath = simplePath;
		// Determine if the build is running the test
		File pathFromBuildDirectory = new File(pathFromBuild);
		if (pathFromBuildDirectory.isDirectory()) {
			// buildIsRunning = true;
			selectedPath = pathFromBuild + "_build";
		}
		return selectedPath + File.separator;
	}

	public static String findHusacctExportFile(String languageFolder, String exportFile){
		
		String buildPrefix = ".." + File.separator;
		String simplePath = testprojectFolder + File.separator + languageFolder + File.separator + "export" + File.separator + exportFile;
		String pathFromBuild = buildPrefix + simplePath;
		
		String selectedPath = simplePath;
		// Determine if the build is running the test
		File pathFromBuildDirectory = new File(pathFromBuild);
		if (pathFromBuildDirectory.isDirectory()) {
			// buildIsRunning = true;
			selectedPath = pathFromBuild + "_build";
		}
		
		return selectedPath + File.separator;
	}

}
