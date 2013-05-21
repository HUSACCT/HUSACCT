package husacct.bootstrap;

import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.ProjectDTO;
import java.io.File;
import java.util.ArrayList;

/**
 * @author Baruch Berger
 * @Datum 5-may-2013
 * --bootstrap:CreateWorkspace,SetCSharpBenchmarkLocalVariables,Analyse,Minimize
 */
public class SetCSharpBenchmarkLocalVariables extends AbstractBootstrap {

    @Override
    public void execute() {
        ArrayList<ProjectDTO> projects = new ArrayList<ProjectDTO>();
        ArrayList<String> paths = new ArrayList<String>();
        paths.add(new File("").getAbsolutePath() + File.separator + "testprojects" + File.separator + "csharp" + File.separator + "recognition" + File.separator + "localvariables");
        ArrayList<AnalysedModuleDTO> analysedModules = new ArrayList<AnalysedModuleDTO>();
        ProjectDTO project = new ProjectDTO("C# SingleTest", paths, "C#", "1.0", "Test Project", analysedModules);
        projects.add(project);
        getDefineService().createApplication("C# SingleTest", projects, "1.0");
    }
}