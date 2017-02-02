package husacct.analyse.task.analyse.clojure;

import husacct.analyse.domain.IModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.analyse.task.analyse.AbstractAnalyser;

public class ClojureAnalyser extends AbstractAnalyser {

    IModelCreationService modelCreatorService = new FamixCreationServiceImpl();

    @Override
    public void generateModelFromSourceFile(String sourceFilePath) {
//		modelCreatorService.createImport("analyser", importedModule, completeImportString, importsCompletePackage)
    }

    @Override
    public String getFileExtension() {
        return "clj";
    }
}
