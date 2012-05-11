package husacct.analyse.task.analyser;

import husacct.analyse.domain.ModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;

public abstract class AbstractAnalyser {
	
	private ModelCreationService modelCreationService;
	
	public AbstractAnalyser(){
		this.modelCreationService = new FamixCreationServiceImpl();
	}
	
	public void analyseApplication(String sourceFilePath){
		generateModelFromSource(sourceFilePath);
	}
	
	public void connectDependencies(){
		modelCreationService.connectDependencies();
	}
	
	public abstract void generateModelFromSource(String sourceFilePath);
	public abstract String getProgrammingLanguage();
	public abstract String getFileExtension();
}
