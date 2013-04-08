package husacct.bootstrap;

import husacct.define.domain.Project;

import java.io.File;
import java.util.ArrayList;

public class SetJavaBenchmark extends AbstractBootstrap{
	
	@Override
	public void execute() {
		ArrayList<Project> projects = new ArrayList<Project>();
		ArrayList<String> paths = new ArrayList<String>();
		paths.add(new File("").getAbsolutePath() + File.separator + "testprojects" + File.separator + "java" + File.separator + "benchmark");
		Project project = new Project("Java Benchmark", paths, "Java");
		projects.add(project);
		
		getDefineService().createApplication("Java Benchmark", projects, "1.0");
	}

}
