package husacct.analyse.task.analyser;

import husacct.ServiceProvider;
import husacct.analyse.task.analyser.csharp.CSharpAnalyser;
import husacct.analyse.task.analyser.java.JavaAnalyser;
import husacct.control.task.States;
import java.util.List;

public class ApplicationAnalyser {

    private AnalyserBuilder builder;

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
                List<MetaFile> fileData = sourceFileFinder.getFileInfoFromProject(paths[i], sourceFileExtension);
                for (MetaFile fileInfo : fileData) {
                    if (!ServiceProvider.getInstance().getControlService().getState().contains(States.ANALYSING)) {
                        break;
                    }
                    analyser.generateModelFromSource(fileInfo.getPath());
                }
            } catch (Exception e) {
            }
        }

        ServiceProvider.getInstance().getControlService().finishPreAnalysing();
        analyser.connectDependencies();
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