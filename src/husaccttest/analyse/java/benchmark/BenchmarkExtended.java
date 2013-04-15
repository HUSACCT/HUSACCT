package husaccttest.analyse.java.benchmark;

import java.util.ArrayList;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.common.dto.DependencyDTO;
import husacct.define.IDefineService;
import husaccttest.analyse.TestObject;
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
		//defService.createApplication("Java Benchmark", paths, "Java", "1.0");
		
		super.setConfig();
		service = provider.getAnalyseService();
		
		try {
			if(!isAnalysed){
				service.analyseApplication(paths, "Java");
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
	
	public boolean testDependencyObject(TestObject test){
		return testDependencyObject(test, false);
	}
	
	public boolean testDependencyObject(TestObject test, boolean printTree){
		DependencyDTO[] dependencies = service.getDependenciesFrom(test.getFrom());

		if(printTree){
			super.printDependencies(dependencies);
		}
		
		for(DependencyDTO d : test.getDependencies()){
			if(!super.MayDependencyBeChecked(d)){
				continue;
			}
			
			ArrayList<DependencyDTO> directDependencies = super.getDirectDependencies(dependencies);
			
			if(test.getDependencies().size() != directDependencies.size()){
				test.setError("Expected dependencies count doesn't match (expected : " + test.getDependencies().size() + "; actual : " + directDependencies.size() + ")");
			}
			
			ArrayList<DependencyDTO> linenumberDependencies = super.getDependenciesByLinenumber(d, directDependencies);
			if(linenumberDependencies.size() == 0){
				test.setError("Couldn't match linenumber (" + d.lineNumber + " | "+ d.type +")");
			}
			
			ArrayList<DependencyDTO> typeDependencies = super.getDependenciesByType(d, linenumberDependencies);
			if(typeDependencies.size() == 0){
				test.setError("Couldn't match type (" + d.type + " | "+ d.lineNumber +")");
			}
			
			ArrayList<DependencyDTO> toDependencies = super.getDependenciesByTo(d, typeDependencies);
			if(toDependencies.size() == 0){
				test.setError("Couldn't match to (" + d.to + " | " + d.lineNumber +")");
			}
			
			ArrayList<DependencyDTO> fromDependencies = super.getDependenciesByFrom(d, toDependencies);
			if(fromDependencies.size() == 0){
				test.setError("Couldn't match from (" + d.from + " | "+ d.lineNumber +")");
			}
			
			if(!test.getLastError().equals("")){
				break;
			}
			
		}
		
		
		if(test.getLastError().equals("")){
			return true;
		}
		
		return false;
	}
	
}
