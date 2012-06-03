package husaccttest.analyse.java.recognation;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.define.IDefineService;
import husaccttest.analyse.TestProjectFinder;
import husaccttest.analyse.TestCaseExtended;

public abstract class RecognationExtended extends TestCaseExtended{

	public IAnalyseService service;
	
	@Override
	public void setUp(){
		
		ServiceProvider provider = ServiceProvider.getInstance();
		IDefineService defService = provider.getDefineService();

		String path = TestProjectFinder.lookupProject("java", "recognition");
		String[] paths = new String[]{path};
		defService.createApplication("Java Recognition", paths, "Java", "1.0");
		
		service = provider.getAnalyseService();
				
		try {
			if(!service.isAnalysed()){
				service.analyseApplication();
			}

		} catch (Exception e){
			System.out.println("We're sorry. You need to have a java project 'benchmark_application' with inside the benchmark_application. Or you have the wrong version of the benchmark_application.");
			System.exit(0);
		}
	}
	
	@Override
	public void tearDown(){
		
	}
	
}
