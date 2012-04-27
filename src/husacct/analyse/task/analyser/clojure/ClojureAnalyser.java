package husacct.analyse.task.analyser.clojure;

import husacct.analyse.domain.ModelService;
import husacct.analyse.domain.famix.FamixModelServiceImpl;
import husacct.analyse.task.analyser.AbstractAnalyser;

public class ClojureAnalyser extends AbstractAnalyser{

	ModelService modelCreatorService = new FamixModelServiceImpl();
	
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
