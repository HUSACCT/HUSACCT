package husaccttest.analyse.java.benchmark.accuracy;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ProjectDTO;
import husacct.control.ControlServiceImpl;
import husaccttest.analyse.TestProjectFinder;

import java.net.URL;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DependencyDetectionAccuracyTest {
	private static IAnalyseService service = null; 
	private static boolean isAnalysed = false;
	private static Logger logger;
	private static DependencyDTO[] allDependencies = null;
	private static String path = TestProjectFinder.lookupProject("java", "accuracy");
	private static String language = "Java";
	
	@BeforeClass
	public static void beforeClass() {
		try {
			setLog4jConfiguration();
			
			ArrayList<ProjectDTO> projects = createProjectDTOs();
			
			ControlServiceImpl ctrlS = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
			ctrlS.getMainController().startGui();
			ctrlS.getMainController().getWorkspaceController().createWorkspace("JavaAnalyseTestWorkspace");
			ServiceProvider.getInstance().getDefineService().createApplication(language+" test", projects, "1.0");
			ctrlS.getMainController().getWorkspaceController().getCurrentWorkspace().setApplicationData(ServiceProvider.getInstance().getDefineService().getApplicationDetails());
			ctrlS.getMainController().getApplicationController().analyseApplication();
			service = ServiceProvider.getInstance().getAnalyseService();
			
			logger.debug("PROJECT LOADED");
		
			//analyse is in a different Thread, and needs some time
			while(!isAnalysed){
				try {
					Thread.sleep((long)10);
				} catch (InterruptedException e) {}
				isAnalysed = service.isAnalysed();
			}
			allDependencies = service.getAllUnfilteredDependencies();
			//for testing only
			printDependencies();
		} catch (Exception e){
			String errorMessage =  "We're sorry. You need to have a Java project 'Accuracy'. Or you have the wrong version of the Accuracy Test.";
			logger.warn(errorMessage);
			System.exit(0);
		}
	}
	
	@AfterClass
	public static void tearDown(){
		allDependencies = null;
	}
	//TESTS
	//
	//DIRECT
	
	@Test
	public void AccessClassVariable(){
		boolean found = false;
		String toTestFrom = "AccessClassVariable";
		for(DependencyDTO dependency : allDependencies){
			String from = dependency.type.toString();
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}

	@Test
	public void AccessClassVariableConstant(){
		boolean found = false;
		String toTestFrom = "AccessVariableClassConstant";
		for(DependencyDTO dependency : allDependencies){
			String from = dependency.type.toString();
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}

	
	@Test
	public void AccessClassVariableInterface(){
		boolean found = false;
		String toTestFrom = "AccessVariableClassInterface";
		for(DependencyDTO dependency : allDependencies){
			String from = dependency.type.toString();
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
	
	@Test
	public void AccessEnumeration(){
		boolean found = false;
		String toTestFrom = "AccessEnumeration";
		for(DependencyDTO dependency : allDependencies){
			String from = dependency.type.toString();
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
	
		@Test
		public void AccessInstanceVariableRead(){
			boolean found = false;
			String toTestFrom = "AccessVariableInstanceRead";
			for(DependencyDTO dependency : allDependencies){
				String from = dependency.type.toString();
				if(toTestFrom.equals(from)){
					found = true;
					break;
				}
			}
			Assert.assertTrue(found);
		}

		@Test
		public void AccessInstanceVariableConstant(){
			boolean found = false;
			String toTestFrom = "AccessVariableInstanceConstant";
			for(DependencyDTO dependency : allDependencies){
				String from = dependency.type.toString();
				if(toTestFrom.equals(from)){
					found = true;
					break;
				}
			}
			Assert.assertTrue(found);
		}
		
		@Test
		public void AccessObjectReferenceAsParameter(){
			boolean found = false;
			String toTestFrom = "AccessObjectReferenceParameter";
			for(DependencyDTO dependency : allDependencies){
				String from = dependency.type.toString();
				if(toTestFrom.equals(from)){
					found = true;
					break;
				}
			}
			Assert.assertTrue(found);
		}

		@Test
		public void AccessObjectReferenceWithinIfStatement(){
			boolean found = false;
			String toTestFrom = "AccessObjectReferenceWithinStatement";
			for(DependencyDTO dependency : allDependencies){
				String from = dependency.type.toString();
				if(toTestFrom.equals(from)){
					found = true;
					break;
				}
			}
			Assert.assertTrue(found);
		}
		
		@Test
		public void AnnotationDependency(){
			boolean found = false;
			String toTestFrom = "AnnotationDependency";
			for(DependencyDTO dependency : allDependencies){
				String from = dependency.type.toString();
				if(toTestFrom.equals(from)){
					found = true;
					break;
				}
			}
			Assert.assertTrue(found);
		}
		
		@Test
		public void CallClassMethod(){
			boolean found = false;
			String toTestFrom = "CallClassMethod";
			for(DependencyDTO dependency : allDependencies){
				String from = dependency.type.toString();
				if(toTestFrom.equals(from)){
					found = true;
					break;
				}
			}
			Assert.assertTrue(found);
		}
		
		@Test
		public void CallConstructor(){
			boolean found = false;
			String toTestFrom = "CallConstructor";
			for(DependencyDTO dependency : allDependencies){
				String from = dependency.type.toString();
				if(toTestFrom.equals(from)){
					found = true;
					break;
				}
			}
			Assert.assertTrue(found);
		}
		
		@Test
		public void CallConstructorLibraryClass(){
			boolean found = false;
			String toTestFrom = "CallConstructorClassLibrary";
			for(DependencyDTO dependency : allDependencies){
				String from = dependency.type.toString();
				if(toTestFrom.equals(from)){
					found = true;
					break;
				}
			}
			Assert.assertTrue(found);
		}
		
		@Test
		public void CallInstance(){
			boolean found = false;
			String toTestFrom = "CallInstance";
			for(DependencyDTO dependency : allDependencies){
				String from = dependency.type.toString();
				if(toTestFrom.equals(from)){
					found = true;
					break;
				}
			}
			Assert.assertTrue(found);
		}
		
		@Test
		public void CallMethodInstanceInnerClass(){
			boolean found = false;
			String toTestFrom = "CallMethodInstanceInnerClass";
			for(DependencyDTO dependency : allDependencies){
				String from = dependency.type.toString();
				if(toTestFrom.equals(from)){
					found = true;
					break;
				}
			}
			Assert.assertTrue(found);
		}
		
		@Test
		public void CallInstanceInterface(){
			boolean found = false;
			String toTestFrom = "CallInstanceInterface";
			for(DependencyDTO dependency : allDependencies){
				String from = dependency.type.toString();
				if(toTestFrom.equals(from)){
					found = true;
					break;
				}
			}
			Assert.assertTrue(found);
		}
		
		@Test
		public void CallInstanceLibraryClass(){
			boolean found = false;
			String toTestFrom = "CallInstanceClassLibrary";
			for(DependencyDTO dependency : allDependencies){
				String from = dependency.type.toString();
				if(toTestFrom.equals(from)){
					found = true;
					break;
				}
			}
			Assert.assertTrue(found);
		}
		
		@Test
		public void CallInstanceSuperClass(){
			boolean found = false;
			String toTestFrom = "CallSuperInstanceClass";
			for(DependencyDTO dependency : allDependencies){
				String from = dependency.type.toString();
				if(toTestFrom.equals(from)){
					found = true;
					break;
				}
			}
			Assert.assertTrue(found);
		}
		
		@Test
		public void CallInstanceSuperSuperClass(){
			boolean found = false;
			String toTestFrom = "CallSuperInstanceSuperClass";
			for(DependencyDTO dependency : allDependencies){
				String from = dependency.type.toString();
				if(toTestFrom.equals(from)){
					found = true;
					break;
				}
			}
			Assert.assertTrue(found);
		}
		
		@Test
		public void DeclarationExceptionThrows(){
			boolean found = false;
			String toTestFrom = "DeclarationExceptionThrows";
			for(DependencyDTO dependency : allDependencies){
				String from = dependency.type.toString();
				if(toTestFrom.equals(from)){
					found = true;
					break;
				}
			}
			Assert.assertTrue(found);
		}
		
		@Test
		public void DeclarationParameter(){
			boolean found = false;
			String toTestFrom = "DeclarationParameter";
			for(DependencyDTO dependency : allDependencies){
				String from = dependency.type.toString();
				if(toTestFrom.equals(from)){
					found = true;
					break;
				}
			}
			Assert.assertTrue(found);
		}
		
		@Test
		public void DeclarationReturnType(){
			boolean found = false;
			String toTestFrom = "DeclarationReturnType";
			for(DependencyDTO dependency : allDependencies){
				String from = dependency.type.toString();
				if(toTestFrom.equals(from)){
					found = true;
					break;
				}
			}
			Assert.assertTrue(found);
		}
		
		@Test
		public void DeclarationTypeCast(){
			boolean found = false;
			String toTestFrom = "DeclarationCastType";
			for(DependencyDTO dependency : allDependencies){
				String from = dependency.type.toString();
				if(toTestFrom.equals(from)){
					found = true;
					break;
				}
			}
			Assert.assertTrue(found);
		}
		
		@Test
		public void DeclarationTypeCastOfArgument(){
			boolean found = false;
			String toTestFrom = "DeclarationCastOfArgumentType";
			for(DependencyDTO dependency : allDependencies){
				String from = dependency.type.toString();
				if(toTestFrom.equals(from)){
					found = true;
					break;
				}
			}
			Assert.assertTrue(found);
		}
		
		@Test
		public void DeclarationVariableInstance(){
			boolean found = false;
			String toTestFrom = "DeclarationVariableInstance";
			for(DependencyDTO dependency : allDependencies){
				String from = dependency.type.toString();
				if(toTestFrom.equals(from)){
					found = true;
					break;
				}
			}
			Assert.assertTrue(found);
		}
		
		@Test
		public void DeclarationVariableLocal(){
			boolean found = false;
			String toTestFrom = "DeclarationVariableLocal";
			for(DependencyDTO dependency : allDependencies){
				String from = dependency.type.toString();
				if(toTestFrom.equals(from)){
					found = true;
					break;
				}
			}
			Assert.assertTrue(found);
		}
		
		@Test
		public void DeclarationVariableStatic(){
			boolean found = false;
			String toTestFrom = "DeclarationStaticVariable";
			for(DependencyDTO dependency : allDependencies){
				String from = dependency.type.toString();
				if(toTestFrom.equals(from)){
					found = true;
					break;
				}
			}
			Assert.assertTrue(found);
		}
		
		@Test
		public void ImportDependencyUnused(){
			boolean found = false;
			String toTestFrom = "ImportDependencyUnused";
			for(DependencyDTO dependency : allDependencies){
				String from = dependency.type.toString();
				if(toTestFrom.equals(from)){
					found = true;
					break;
				}
			}
			Assert.assertTrue(found);
		}
		@Test
		public void InheritanceExtends(){
			boolean found = false;
			String toTestFrom = "InheritanceExtends";
			for(DependencyDTO dependency : allDependencies){
				String from = dependency.type.toString();
				if(toTestFrom.equals(from)){
					found = true;
					break;
				}
			}
			Assert.assertTrue(found);
		}
		@Test
		public void InheritanceExtendsAbstractClass(){
			boolean found = false;
			String toTestFrom = "InheritanceExtendsAbstractClass";
			for(DependencyDTO dependency : allDependencies){
				String from = dependency.type.toString();
				if(toTestFrom.equals(from)){
					found = true;
					break;
				}
			}
			Assert.assertTrue(found);
		}
		@Test
		public void InheritanceImplementsInterface(){
			boolean found = false;
			String toTestFrom = "InheritanceImplementsInterface";
			for(DependencyDTO dependency : allDependencies){
				String from = dependency.type.toString();
				if(toTestFrom.equals(from)){
					found = true;
					break;
				}
			}
			Assert.assertTrue(found);
		}
	//TESTS
	
	//INDIRECT
	@Test
	public void javaIInheritenceExtendsTest(){
		boolean found = false;
		String toTestFrom = "InheritanceExtendsExtendsIndirect";
		for(DependencyDTO dependency : allDependencies){
			String from = dependency.type.toString();
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}

	@Test
	public void InheritanceExtendsImplementsIndirect(){
		boolean found = false;
		String toTestFrom = "InheritanceExtendsImplementsIndirect";
		for(DependencyDTO dependency : allDependencies){
			String from = dependency.type.toString();
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}

	@Test
	public void InheritanceImplementsExtendsIndirect(){
		boolean found = false;
		String toTestFrom = "InheritanceImplementsExtendsIndirect";
		for(DependencyDTO dependency : allDependencies){
			String from = dependency.type.toString();
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
	//
	//private helpers
	//
	private static ArrayList<ProjectDTO> createProjectDTOs(){
		ArrayList<ProjectDTO> projects = new ArrayList<ProjectDTO>();
		ArrayList<String> paths = new ArrayList<String>();
		paths.add(path);
		ProjectDTO project = new ProjectDTO("TestAccuracy", paths, language, "1", "for testing purposes", new ArrayList<AnalysedModuleDTO>());
		projects.add(project);
		return projects;
	}
	
	private static void setLog4jConfiguration() {
		URL propertiesFile = Class.class.getResource("/husacct/common/resources/log4j.properties");
		PropertyConfigurator.configure(propertiesFile);
		logger = Logger.getLogger(DependencyDetectionAccuracyTest.class);
	}
	
	@SuppressWarnings("unused")
	private static void printDependencies() {
		logger.info("application is analysed");
		logger.info("found dependencies = "+allDependencies.length);
		for(DependencyDTO d : allDependencies){
			logger.info(d.from + " " + d.type.toString());
		}
	}
}