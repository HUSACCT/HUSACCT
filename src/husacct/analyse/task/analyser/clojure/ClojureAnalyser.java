package husacct.analyse.task.analyser.clojure;

import husacct.analyse.domain.IModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.analyse.task.analyser.AbstractAnalyser;

public class ClojureAnalyser extends AbstractAnalyser {

    IModelCreationService modelCreatorService = new FamixCreationServiceImpl();

    @Override
    public void generateModelFromSourceFile(String sourceFilePath) {
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
