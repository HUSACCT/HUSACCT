package husacct.bootstrap;

import java.io.File;

public class SetJavaBenchmark extends AbstractBootstrap{
	
	@Override
	public void execute() {
		getDefineService().createApplication("Java Benchmark", new String[]{new File("").getAbsolutePath() + File.separator + "testprojects" + File.separator + "java" + File.separator + "benchmark"}, "Java", "1.0");
	}

}
