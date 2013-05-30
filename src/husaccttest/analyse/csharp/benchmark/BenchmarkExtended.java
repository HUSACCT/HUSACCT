package husaccttest.analyse.csharp.benchmark;

import java.util.ArrayList;

import husacct.analyse.AnalyseServiceImpl;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.ProjectDTO;
import husaccttest.analyse.TestCaseExtended;
import husaccttest.analyse.TestProjectFinder;

public class BenchmarkExtended extends TestCaseExtended{

	protected IAnalyseService analyserService;
	private static boolean isAnalysed;
	
	@Override
	public void setUp() {
		
		analyserService = new AnalyseServiceImpl();
		String path = TestProjectFinder.lookupProject("csharp", "benchmark");
		String language = "C#";
		
		super.setUp();
		super.setConfig();
		
		try {
			if(!isAnalysed){
				ArrayList<String> paths = new ArrayList<String>();
				paths.add(path);
				ProjectDTO project = new ProjectDTO(
						"C# test", 
						paths , 
						language, 
						"version999",
						"testdescription",
						null);
				analyserService.analyseApplication(project);
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
		//Do things if needed to break down after the tests are completed
	}
	
}
