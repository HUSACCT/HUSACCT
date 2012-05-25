package husaccttest.analyse.benchmark;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.define.IDefineService;
import husaccttest.analyse.TestCaseExtended;

public abstract class BenchmarkExtended extends TestCaseExtended{

	public IAnalyseService service;
	
	@Override
	public void setUp(){
		
		ServiceProvider provider = ServiceProvider.getInstance();
		IDefineService defService = provider.getDefineService();

		String path = "../benchmark_application/src";
		String[] paths = new String[]{path};
		defService.createApplication("Boobies Sanders Application", paths, "Java", "1.0");
		
		service = provider.getAnalyseService();
		
//		if(service.isAnalysed()){
//			return;
//		}
		
		try {
			if(!service.isAnalysed()){
				service.analyseApplication();
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
