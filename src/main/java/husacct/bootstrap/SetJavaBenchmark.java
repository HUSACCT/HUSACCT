package husacct.bootstrap;

import husacct.common.dto.ProjectDTO;
import husacct.common.dto.SoftwareUnitDTO;

import java.io.File;
import java.util.ArrayList;

public class SetJavaBenchmark extends AbstractBootstrap{
	private String[] pathsToCustomTestProject = {};

	@Override
	public void execute() {
		ArrayList<ProjectDTO> projects = new ArrayList<ProjectDTO>();
		ArrayList<String> paths = new ArrayList<String>();

		for(String pathToCustomTestProject : pathsToCustomTestProject){
			if(new File(pathToCustomTestProject).exists() && new File(pathToCustomTestProject).isDirectory()){
				paths.add(pathToCustomTestProject);
			}
		}

		if(paths.size() <= 0){
			paths.add(new File("").getAbsolutePath() + "/" + "testprojects" + "/" + "java" + "/" + "benchmark");
		}

		ArrayList<SoftwareUnitDTO> analysedModules = new ArrayList<SoftwareUnitDTO>();
		ProjectDTO project = new ProjectDTO("Java Benchmark", paths, "Java", "1.0", "Benchmark Project", analysedModules);
		projects.add(project);
		getDefineService().createApplication("Java Benchmark", projects, "1.0");
	}

	@Override
	public void execute(String[] args) {
		pathsToCustomTestProject = args;
		execute();
	}

}
