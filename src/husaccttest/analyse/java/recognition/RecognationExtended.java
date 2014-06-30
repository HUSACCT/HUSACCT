package husaccttest.analyse.java.recognition;

import java.util.ArrayList;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.ProjectDTO;
import husacct.define.IDefineService;
import husaccttest.TestResourceFinder;
import husaccttest.analyse.java.TestCaseExtended;

public abstract class RecognationExtended extends TestCaseExtended{

	public IAnalyseService service;
	private static boolean isAnalysed = false;
	
	@Override
	public void setUp(){
		
		ServiceProvider provider = ServiceProvider.getInstance();
		IDefineService defService = provider.getDefineService();

		String path = TestResourceFinder.findSourceCodeDirectory("java", "recognition");
		ArrayList<String> paths = new ArrayList<String>();
		paths.add(path);
		ArrayList<ProjectDTO> projects = new ArrayList<ProjectDTO>();
		projects.add(new ProjectDTO("TestProject", new ArrayList<String>(), "java", "1.0", "test project for unit tests", new ArrayList<AnalysedModuleDTO>()));
		defService.createApplication("Java Recognition", projects, "1.0");
		
		service = provider.getAnalyseService();
				
		try {
			if(!isAnalysed){
				service.analyseApplication(projects.get(0));
				isAnalysed = true;
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
