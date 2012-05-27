package husaccttest.analyse.benchmarkjava;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.define.IDefineService;
import husaccttest.analyse.TestCaseExtended;

public abstract class BenchmarkExtended extends TestCaseExtended{

	public IAnalyseService service;
	private static boolean isanalysed = false;
	
	@Override
	public void setUp(){
		
		ServiceProvider provider = ServiceProvider.getInstance();
		IDefineService defService = provider.getDefineService();

		String path = "testjavabenchmark/";
		String[] paths = new String[]{path};
		defService.createApplication("Boobies Sanders Application", paths, "Java", "1.0");
		
		service = provider.getAnalyseService();
		
//		if(service.isAnalysed()){
//			return;
//		}
		
		try {
			if(!isanalysed){
				FamixCreationServiceImpl famix = new FamixCreationServiceImpl();
				famix.clearModel();
				service.analyseApplication();
				isanalysed = true;
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
