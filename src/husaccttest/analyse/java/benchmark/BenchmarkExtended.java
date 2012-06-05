package husaccttest.analyse.java.benchmark;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.define.IDefineService;
import husaccttest.analyse.TestProjectFinder;
import husaccttest.analyse.TestCaseExtended;

public abstract class BenchmarkExtended extends TestCaseExtended{

	public IAnalyseService service;
	private static boolean isAnalysed = false;
	
	@Override
	public void setUp(){
		
		ServiceProvider provider = ServiceProvider.getInstance();
		IDefineService defService = provider.getDefineService();

		String path = TestProjectFinder.lookupProject("java", "benchmark");
		String[] paths = new String[]{path};
		defService.createApplication("Java Benchmark", paths, "Java", "1.0");
		
		service = provider.getAnalyseService();
		
		try {
			if(!isAnalysed){
				service.analyseApplication();
				isAnalysed = true;
			}

		} catch (Exception e){
			System.out.println("We're sorry. You need to have a java project 'benchmark_application' with inside the benchmark_application. Or you have the wrong version of the benchmark_application.");
			System.out.println("git://github.com/timmuller/benchmark_application.git");
			System.exit(0);
		}
	}
	
	@Override
	public void tearDown(){
		
	}
	
}
