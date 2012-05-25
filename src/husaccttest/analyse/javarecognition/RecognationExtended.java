package husaccttest.analyse.javarecognition;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.define.IDefineService;
import husaccttest.analyse.TestCaseExtended;

public abstract class RecognationExtended extends TestCaseExtended{

	public IAnalyseService service;
	
	@Override
	public void setUp(){
		
		ServiceProvider provider = ServiceProvider.getInstance();
		IDefineService defService = provider.getDefineService();

		try{
			String path = getClass().getResource("testapplication").getPath();
			System.out.println(path);
			String[] paths = new String[]{path};
			defService.createApplication("Husacct Test Application", paths, "Java", "1.0");
			service = provider.getAnalyseService();
						
			try {
				if(!service.isAnalysed()){
					service.analyseApplication();
				}

			} catch (Exception e){
				System.err.println("Incorrect application loaded");
				System.exit(0);
			}
		} catch (Exception e) {
			System.err.println("Test path is not existing");
			e.printStackTrace();
		}
	}
	
	@Override
	public void tearDown(){
		
	}
	
}
