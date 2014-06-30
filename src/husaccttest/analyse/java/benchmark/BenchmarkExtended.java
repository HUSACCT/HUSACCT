package husaccttest.analyse.java.benchmark;

import java.util.ArrayList;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ProjectDTO;
import husacct.define.IDefineService;
import husaccttest.TestResourceFinder;
import husaccttest.analyse.java.TestCaseExtended;
import husaccttest.analyse.java.TestObject;

public abstract class BenchmarkExtended extends TestCaseExtended{

	public IAnalyseService service;
	private static boolean isAnalysed = false;
	
	@Override
	public void setUp(){
		
		ServiceProvider provider = ServiceProvider.getInstance();
		IDefineService defService = provider.getDefineService();

		String path = TestResourceFinder.findSourceCodeDirectory("java", "benchmark_2012");
		ArrayList<String> paths = new ArrayList<String>();
		paths.add(path);
		ArrayList<ProjectDTO> projects = new ArrayList<ProjectDTO>();
		projects.add(new ProjectDTO("TestProjectBenchmark", new ArrayList<String>(), "java", "1.0", "test project for unit tests", new ArrayList<AnalysedModuleDTO>()));
		defService.createApplication("Java Benchmark", projects, "1.0");
		
		super.setConfig();
		service = provider.getAnalyseService();
		
		try {
			if(!isAnalysed){
				service.analyseApplication(projects.get(0));
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
