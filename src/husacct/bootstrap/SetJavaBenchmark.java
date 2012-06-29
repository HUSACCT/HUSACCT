package husacct.bootstrap;

public class SetJavaBenchmark extends AbstractBootstrap{
	
	@Override
	public void execute() {
		executeBootstrap(CreateWorkspace.class);
		getDefineService().createApplication("Java Benchmark", new String[]{new java.io.File("").getAbsolutePath() + "\\testprojects\\java\\benchmark"}, "Java", "1.0");
	}

}
