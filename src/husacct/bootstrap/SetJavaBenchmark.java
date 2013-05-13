package husacct.bootstrap;

import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.ProjectDTO;

import java.io.File;
import java.util.ArrayList;

public class SetJavaBenchmark extends AbstractBootstrap{
	
	@Override
	public void execute() {
		ArrayList<ProjectDTO> projects = new ArrayList<ProjectDTO>();
		ArrayList<String> paths = new ArrayList<String>();
		
		paths.add(new File("").getAbsolutePath() + File.separator + "testprojects" + File.separator + "java" + File.separator + "benchmark");
		paths.add(new File("").getAbsolutePath() + File.separator + "testprojects" + File.separator + "java" + File.separator + "benchmark");
		
		
		ArrayList<AnalysedModuleDTO> analysedModules = new ArrayList<AnalysedModuleDTO>();
		ProjectDTO project = new ProjectDTO("Java Benchmark", paths, "Java", "1.0", "Benchmark Project", analysedModules);
		ProjectDTO project2 = new ProjectDTO("Project 2", paths, "Java", "1.0", "Benchmark Project", analysedModules);
		
		
		projects.add(project);
		projects.add(project2);
		getDefineService().createApplication("Java Benchmark", projects, "1.0");
	}

}
