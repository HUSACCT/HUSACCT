package husacct.analyse.task.analyser;

import husacct.ServiceProvider;
import husacct.analyse.task.analyser.csharp.CSharpAnalyser;
import husacct.analyse.task.analyser.java.JavaAnalyser;
import husacct.common.dto.ApplicationDTO;
import husacct.control.task.States;
import husacct.define.IDefineService;

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
        for (String workspacePath : paths) {
            try {
                List<MetaFile> fileData = sourceFileFinder.getFileInfoFromProject(workspacePath, sourceFileExtension);
                for (MetaFile fileInfo : fileData) {

                    //Added By Team 1 General GUI & Control
                    //Needed to check if Thread is allowed to continue
                    if (!ServiceProvider.getInstance().getControlService().getState().contains(States.ANALYSING)) {
                        break;
                    }
                    //And adding by Team 1

                    analyser.generateModelFromSource(fileInfo.getPath());
                }
            } catch (Exception e) {
                //TODO Generate Custom Exception
            }
        }

        //Added By team 1 General GUI & Control
        //Is needed to allow the Define component to browse in the software units when the pre-analyse is complete
        ServiceProvider.getInstance().getControlService().finishPreAnalysing();
        //End added by Team 1
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