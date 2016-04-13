package husacct.analyse.task.analyser;

import husacct.analyse.domain.IModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;

public abstract class AbstractAnalyser {

    private IModelCreationService modelCreationService;
    private String projectPath = "";

    public AbstractAnalyser() {
        this.modelCreationService = new FamixCreationServiceImpl();
    }

    public void analyseSourceFile(String projectPath, String sourceFilePath) {
    	this.projectPath = projectPath;
        generateModelFromSourceFile(sourceFilePath);
    }

    public void connectDependencies() {
        modelCreationService.executePostProcesses();
    }

    public abstract void generateModelFromSourceFile(String sourceFilePath);

    public abstract String getProgrammingLanguage();

    public abstract String getFileExtension();

	/**
	 * Required to clear the LambdaBuffers after completion of the analysis.
	 */
	public void clearLambdaBuffers() {
		
	}
	
	public String getProjectPath() {
		return projectPath;
	}
}
