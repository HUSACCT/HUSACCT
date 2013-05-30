package husacct.bootstrap;

import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.ProjectDTO;

import java.io.File;
import java.util.ArrayList;

public class SetCSharpBenchmark extends AbstractBootstrap{
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
//			paths.add(new File("").getAbsolutePath() + File.separator + "testprojects" + File.separator + "java" + File.separator + "benchmark");
//			/Users/GMBosma/Git/HUSACCT/testprojects/csharp/benchmark/domain/Direct/Alowed/AccessClassVariable.cs
			
//			paths.add(new File("").getAbsolutePath() + File.separator + "testprojects" + File.separator + "csharp" + File.separator + "recognition");
//			paths.add(new File("").getAbsolutePath() + File.separator + "testprojects" + File.separator + "csharp" + File.separator + "recognition" + File.separator + "invocmethod");
//			paths.add(new File("/Users/GMBosma/Dropbox/Advanced Software Engineering/TestCases/gerardtests/src/").getAbsolutePath());
//			 paths.add(new File("").getAbsolutePath() + File.separator + "testprojects" + File.separator + "csharp" + File.separator + "recognition" + File.separator +  "invocmethod"  + File.separator + "a"  + File.separator +  "TheType.cs");
//		     paths.add(new File("").getAbsolutePath() + File.separator + "testprojects" + File.separator + "csharp" + File.separator + "recognition" + File.separator +  "invocmethod"  + File.separator + "a"  + File.separator +  "Gui.cs");
//		     paths.add(new File("").getAbsolutePath() + File.separator + "testprojects" + File.separator + "csharp" + File.separator + "recognition" + File.separator +  "invocmethod"  + File.separator + "a"  + File.separator +  "SamePackageG.cs");
		}
		
		ArrayList<AnalysedModuleDTO> analysedModules = new ArrayList<AnalysedModuleDTO>();
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
