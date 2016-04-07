package husacct.analyse.task.analyser;

import husacct.ServiceProvider;
import husacct.analyse.task.analyser.csharp.CSharpAnalyser;
import husacct.analyse.task.analyser.java.JavaAnalyser;
import husacct.control.task.States;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

public class ApplicationAnalyser {

    private AnalyserBuilder builder;
    
    private final Logger logger = Logger.getLogger(ApplicationAnalyser.class);

    public ApplicationAnalyser() {
        this.builder = new AnalyserBuilder();
    }

    public void analyseApplication(String[] paths, String programmingLanguage) {
        AbstractAnalyser analyser = builder.getAnalyser(programmingLanguage);
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
            }
        }

        this.logger.info(new Date().toString() + " Finished: Model generated from sourcefile");
        analyser.connectDependencies();
        //this.logger.info(new Date().toString() + " Finished: Connecting dependencies");
        //required for clearing the buffers after analysis is finished
        analyser.clearLambdaBuffers();
    }

    public boolean isZip(String path) {
        return path.endsWith(".zip");
    }

    public String[] getAvailableLanguages() {
        String[] availableLanguages = new String[]{
            new JavaAnalyser().getProgrammingLanguage(),
            new CSharpAnalyser().getProgrammingLanguage()
            
        };
        return availableLanguages;
    }

    private String getExtensionForLanguage(String language) {
        AbstractAnalyser analyser = builder.getAnalyser(language);
        return analyser.getFileExtension();
    }
}