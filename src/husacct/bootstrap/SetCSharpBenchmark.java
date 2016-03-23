package husacct.bootstrap;

import husacct.analyse.serviceinterface.dto.SoftwareUnitDTO;
import husacct.common.dto.ProjectDTO;

import java.io.File;
import java.util.ArrayList;

/***
 * Use this file with the following run argument to automatically run HUSACCT with a CSharp project.
 * you can add as many paths as necessary when no path is given it will load the benchmark.
 * --bootstrap:CreateWorkspace,SetCSharpBenchmark?"path1"|"path2"|"path3",Analyse,DefineJavaBenchmark,MapJavaBenchmark?"SoftwareUnit">"PACKAGE",Validate
 */
public class SetCSharpBenchmark extends AbstractBootstrap{
	private String[] pathsToCustomTestProject = {};
	private static final String PATH = new File("").getAbsolutePath() + File.separator + "testprojects" + File.separator + "csharp" + File.separator + "benchmark";
	
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
			paths.add(PATH);
		}
		
		ArrayList<SoftwareUnitDTO> analysedModules = new ArrayList<SoftwareUnitDTO>();
		ProjectDTO project = new ProjectDTO("CSharp", paths, "C#", "1.0", "Benchmark Project", analysedModules);
		projects.add(project);
		getDefineService().createApplication("CSharp", projects, "1.0");
	}

	@Override
	public void execute(String[] args) {
		pathsToCustomTestProject = args;
		execute();
	}

}
