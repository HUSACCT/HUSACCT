package husacct.analyse.task.analyse;

import husacct.ServiceProvider;
import husacct.analyse.task.analyse.clojure.ClojureAnalyser;
import husacct.analyse.task.analyse.csharp.CSharpAnalyser;
import husacct.analyse.task.analyse.java.JavaAnalyser;
import husacct.control.task.States;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

public class ApplicationAnalyser {

    private AbstractAnalyser analyser;
    
    private final Logger logger = Logger.getLogger(ApplicationAnalyser.class);

    public ApplicationAnalyser() {
    }

    public void analyseApplication(String[] paths, String programmingLanguage) {
        this.logger.info(new Date().toString() + " Start: Parse sourcefiles");

        analyser = createAnalyser(programmingLanguage);
        SourceFileFinder sourceFileFinder = new SourceFileFinder();
        String sourceFileExtension = getExtensionForLanguage(programmingLanguage);
		int size = paths.length;
        for (int i = 0; i < size; i ++) {
            try {
            	String projectPath = paths[i];
                List<MetaFile> fileData = sourceFileFinder.getFileInfoFromProject(projectPath, sourceFileExtension);
                for (MetaFile sourceFileInfo : fileData) {
                    if (!ServiceProvider.getInstance().getControlService().getState().contains(States.ANALYSING)) {
                        break;
                    }
                    analyser.analyseSourceFile(projectPath, sourceFileInfo.getPath());
                }
            } catch (Exception e) {
                this.logger.warn(" Parse exception in source file: sourceFileInfo.getPath() " + e.getCause().toString());
            }
        }

        this.logger.info(" Number of syntax errors: " + analyser.getNumberOfSyntaxErrors());
        this.logger.info(" Number of files with syntax errors: " + analyser.getNrOfFilesWithSyntaxErrors() + "  Of which files with 'test' in path: " + analyser.getNrOfFilesWithSyntaxErrors_WithTestInPath());
        this.logger.info(new Date().toString() + " Finished: Parse sourcefiles");

        analyser.connectDependencies();
        //required for clearing the buffers after analysis is finished
        analyser.clearLambdaBuffers();
    }

    public boolean isZip(String path) {
        return path.endsWith(".zip");
    }

    public String[] getAvailableLanguages() {
        String[] availableLanguages = new String[]{"Java", "C#"};
        return availableLanguages;
    }

    private AbstractAnalyser createAnalyser(String language) {
        AbstractAnalyser applicationAnalyser;
        if (language.equals("Java")) {
            applicationAnalyser = new JavaAnalyser();
        } else if (language.equals("C#")) {
            applicationAnalyser = new CSharpAnalyser();
        } else if (language.equals("Clojure")) {
            applicationAnalyser = new ClojureAnalyser();
        } else {
            applicationAnalyser = null;
        }
        return applicationAnalyser;
    }

    
    private String getExtensionForLanguage(String language) {
        return analyser.getFileExtension();
    }
}