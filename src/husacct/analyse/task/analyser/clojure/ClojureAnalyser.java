package husacct.analyse.task.analyser.clojure;

import husacct.analyse.domain.ModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.analyse.task.analyser.AbstractAnalyser;

public class ClojureAnalyser extends AbstractAnalyser{

	ModelCreationService modelCreatorService = new FamixCreationServiceImpl();
	
	@Override
	public void generateModelFromSource(String sourceFilePath) {
//		modelCreatorService.createImport("analyser", importedModule, completeImportString, importsCompletePackage)
	}

	@Override
	public String getProgrammingLanguage() {
		return "Clojure";
	}

	@Override
	public String getFileExtension() {
		return "clj";
	}
	
}
