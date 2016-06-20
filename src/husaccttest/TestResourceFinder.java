package husaccttest;

import java.io.File;
import java.util.logging.Logger;

/*
 * This class have been developed in order to ensure test-code to find test projects which can be placed 
 * in the 'testresources' folder. The only data that has to be passed in the function that this class provides
 * is the name of the project and the programming language of this project. 
 */

public class TestResourceFinder {

	private static final String TESTPROJECT_FOLDER = "testresources";
	public static final String WORKSPACES_FOLDER = "workspaces";
	public static final String EXPORT_FOLDER = "export";

	/**Function to find the current source path, holding in account if the build or the normal JUnit tests is requesting the path. 
	 * Build tests will run from a different location, thus the path will be different. */
	public static String findSourceCodeDirectory(String languageFolder, String projectName){

		String path = getBaseTestResourcesPath(languageFolder) + projectName;
		checkFilePath(path);
		return path + "/";
	}

	/**Function to find the workspace file, holding in account if the build or the normal JUnit tests is requesting the path. 
	 * Build tests will run from a different location, thus the path will be different. */
	// Requires two versions of the workspace: 1) JUnit version with project path = directory name within projectResources; 2) Build version with the project path = "..\" + directory name within projectResources
	public static String findHusacctWorkspace(String languageFolder, String workspaceName){

		String path =  getBaseTestResourcesPath(languageFolder) + WORKSPACES_FOLDER + "/" + workspaceName;
		checkFilePath(path);
		return path;
	}

	public static String findHusacctExportFile(String languageFolder, String exportFile){

		String path = getBaseTestResourcesPath(languageFolder) + EXPORT_FOLDER + "/" + exportFile;
		checkFilePath(path);
		return path;
	}
	
	public static String findMojoTestFile(String resourceFolder, String fileName){
		String path = getBaseTestResourcesPath(resourceFolder) + fileName;
		checkFilePath(path);
		return path;
	}

	private static String getBaseTestResourcesPath(String languageFolder) {
		return TESTPROJECT_FOLDER + "/" + languageFolder + "/";
	}

	private static void checkFilePath(String path) {
		File pathFile = new File(path);

		if (!pathFile.exists()) {
			String className = TestResourceFinder.class.getSimpleName();
			Logger.getLogger(className).severe(className+ ": Workspace or file \""+path +"\" not found");
		}
	}

}
