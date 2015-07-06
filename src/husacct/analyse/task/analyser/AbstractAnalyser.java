package husacct.analyse.task.analyser;

import husacct.analyse.domain.IModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;

public abstract class AbstractAnalyser {

    private IModelCreationService modelCreationService;

    public AbstractAnalyser() {
        this.modelCreationService = new FamixCreationServiceImpl();
    }

    public void analyseApplication(String sourceFilePath) {
        generateModelFromSource(sourceFilePath);
    }

    public void connectDependencies() {
        modelCreationService.executePostProcesses();
    }

    public abstract void generateModelFromSource(String sourceFilePath);

    public abstract String getProgrammingLanguage();

    public abstract String getFileExtension();

	/**
	 * Required to clear the LambdaBuffers after completion of the analysis.
	 */
	public void clearLambdaBuffers() {
		
	}
}
