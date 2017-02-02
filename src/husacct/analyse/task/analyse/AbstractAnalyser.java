package husacct.analyse.task.analyse;

import java.util.HashSet;
import java.util.Set;

import husacct.analyse.domain.IModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;

public abstract class AbstractAnalyser {
	private String sourceFilePath = ""; // Needed for debugging of grammar with ErrorDebugListener
	private int numberOfSyntaxErrors = 0;
	private Set<String> filesWithSyntaxErrorsStack = new HashSet<String>();

    private IModelCreationService modelCreationService;
    private String projectPath = "";

    public AbstractAnalyser() {
        this.modelCreationService = new FamixCreationServiceImpl();
    }

    public void analyseSourceFile(String projectPath, String sourceFilePathInput) {
    	sourceFilePath = "";
    	this.projectPath = projectPath.replace('\\','/');
    	sourceFilePath = sourceFilePathInput;
        generateModelFromSourceFile(sourceFilePathInput);
    }

    public void connectDependencies() {
        modelCreationService.executePostProcesses();
    }

    public abstract void generateModelFromSourceFile(String sourceFilePath);

    public abstract String getFileExtension();

	/**
	 * Required to clear the LambdaBuffers after completion of the analysis.
	 */
	public void clearLambdaBuffers() {
		
	}
	
	public String getProjectPath() {
		return projectPath;
	}
	
	public String getSourceFilePath() {
		return sourceFilePath;
	}
	
	public void raiseNumberOfSyntaxErrors() {
		numberOfSyntaxErrors ++;
	}

	public int getNumberOfSyntaxErrors() {
		return numberOfSyntaxErrors;
	}
	
	public void addFileToFilesWithSyntaxErrorsStack() {
		filesWithSyntaxErrorsStack.add(sourceFilePath);
	}
	
	public int getNrOfFilesWithSyntaxErrors() {
		return filesWithSyntaxErrorsStack.size();
	}
	
	public int getNrOfFilesWithSyntaxErrors_WithTestInPath() {
		int returnValue = 0;
		for (String path : filesWithSyntaxErrorsStack) {
			if (path.toLowerCase().contains("test")) {
				returnValue ++;
			}
		}
		return returnValue;
	}
	
	public void resetNrOfSyntaxMessages() {
		numberOfSyntaxErrors = 0;
		filesWithSyntaxErrorsStack.clear();
	}

}
