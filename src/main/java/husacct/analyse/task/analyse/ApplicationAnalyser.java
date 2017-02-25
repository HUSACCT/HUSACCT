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

    private static final Logger logger = Logger.getLogger(ApplicationAnalyser.class);
    private AbstractAnalyser analyser;

    public void analyseApplication(String[] paths, String programmingLanguage) {
        logger.info("Start: Parse sourcefiles");

        analyser = createAnalyser(programmingLanguage);
        SourceFileFinder sourceFileFinder = new SourceFileFinder();
        String sourceFileExtension = getExtensionForLanguage(programmingLanguage);
        for (String path : paths) {
            logger.debug("Analysing path " + path);
            try {
                List<MetaFile> fileData = sourceFileFinder.getFileInfoFromProject(path, sourceFileExtension);
                for (MetaFile sourceFileInfo : fileData) {
                    if (!ServiceProvider.getInstance().getControlService().getState().contains(States.ANALYSING)) {
                        break;
                    }
                    analyser.analyseSourceFile(path, sourceFileInfo.getPath());
                }
            } catch (Exception e) {
                logger.warn(" Parse exception in source file: sourceFileInfo.getPath() " + e.getCause().toString());
            }
        }

        logger.info("Number of syntax errors: " + analyser.getNumberOfSyntaxErrors());
        logger.info("Number of files with syntax errors: " + analyser.getNrOfFilesWithSyntaxErrors() + "  Of which files with 'test' in path: " + analyser.getNrOfFilesWithSyntaxErrors_WithTestInPath());
        logger.info("Finished: Parse sourcefiles");

        analyser.connectDependencies();
        //required for clearing the buffers after analysis is finished
        analyser.clearLambdaBuffers();
    }

    public boolean isZip(String path) {
        return path.endsWith(".zip");
    }

    public String[] getAvailableLanguages() {
        return new String[]{"Java", "C#"};
    }

    private AbstractAnalyser createAnalyser(String language) {
        AbstractAnalyser applicationAnalyser;
        switch (language) {
            case "Java":
                applicationAnalyser = new JavaAnalyser();
                break;
            case "C#":
                applicationAnalyser = new CSharpAnalyser();
                break;
            case "Clojure":
                applicationAnalyser = new ClojureAnalyser();
                break;
            default:
                applicationAnalyser = null;
                break;
        }
        return applicationAnalyser;
    }

    
    private String getExtensionForLanguage(String language) {
        return analyser.getFileExtension();
    }
}