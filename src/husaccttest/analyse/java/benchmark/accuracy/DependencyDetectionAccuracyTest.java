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
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "AccessClassVariable";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("AccessPropertyOrField");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}

	@Test
	public void AccessClassVariableConstant(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "AccessClassVariableConstant";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("AccessPropertyOrField");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}

	@Test
	public void AccessClassVariableInterface(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "AccessClassVariableInterface";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("AccessPropertyOrField");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}

	@Test
	public void AccessEnumeration(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "AccessEnumeration";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("AccessPropertyOrField");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}

	@Test
	public void AccessInstanceVariable(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "AccessInstanceVariable";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}

	@Test
	public void AccessInstanceVariableConstant(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "AccessVariableInstanceConstant";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}

	@Test
	public void AccessObjectReferenceAsParameter(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "AccessObjectReferenceParameter";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}

	@Test
	public void AccessObjectReferenceWithinIfStatement(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "AccessObjectReferenceWithinStatement";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}

	@Test
	public void AnnotationDependency(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "AnnotationDependency";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("Annotation");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}

	@Test
	public void CallClassMethod(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "CallClassMethod";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("InvocMethod");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}

	@Test
	public void CallConstructor(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "CallConstructor";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("InvocConstructor");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}

	@Test
	public void CallConstructorLibraryClass(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "CallConstructorLibraryClass";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("InvocConstructor");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}

	@Test
	public void CallInstance(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "CallInstance";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("ExtendsConcrete");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}

	@Test
	public void CallInstanceInnerClass(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "CallInstanceInnerClass";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("ExtendsConcrete");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}

	@Test
	public void CallInstanceInterface(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "CallInstanceInterface";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("ExtendsConcrete");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}

	@Test
	public void CallInstanceLibraryClass(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "CallInstanceLibraryClass";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("ExtendsConcrete");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}

	@Test
	public void CallInstanceSuperClass(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "CallInstanceSuperClass";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("ExtendsConcrete");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}

	@Test
	public void CallInstanceSuperSuperClass(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "CallInstanceSuperSuperClass";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("ExtendsConcrete");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}

	@Test
	public void DeclarationExceptionThrows(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "DeclarationExceptionThrows";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("Exception");
		typesToFind.add("Exception");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}

	@Test
	public void DeclarationParameter(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "DeclarationParameter";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("DeclarationParameter");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}

	@Test
	public void DeclarationReturnType(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "DeclarationReturnType";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("DeclarationReturnType");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}

	@Test
	public void DeclarationTypeCast(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "DeclarationTypeCast";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("undefined");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}

	@Test
	public void DeclarationTypeCastOfArgument(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "DeclarationTypeCastOfArgument";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("undefined");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}

	@Test
	public void DeclarationVariableInstance(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "DeclarationVariableInstance";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("DeclarationInstanceVariable");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}

	@Test
	public void DeclarationVariableStatic(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "DeclarationVariableStatic";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("DeclarationInstanceVariable");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}

	@Test
	public void DeclarationVariableWithinMethod(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "DeclarationVariableWithinMethod";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("DeclarationInstanceVariable");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}

	@Test
	public void ImportDependencyUnused(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "ImportDependencyUnused";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}

	@Test
	public void InheritanceExtends(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "InheritanceExtends";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("ExtendsConcrete");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}

	@Test
	public void InheritanceExtendsAbstractClass(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "InheritanceExtendsAbstractClass";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("ExtendsAbstract");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}

	@Test
	public void InheritanceImplementsInterface(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "InheritanceImplementsInterface";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("Implements");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}
	
	//TESTS
	//INDIRECT
	
	@Test
	public void AccessInstanceVariableIndirect_MethodVar(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "AccessInstanceVariableIndirect_MethodVar";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}
	
	@Test
	public void AccessInstanceVariableIndirect_VarVar(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "AccessInstanceVariableIndirect_VarVar";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}
	
	@Test
	public void AccessInstanceVariableIndirect_VarVarToString(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "AccessInstanceVariableIndirect_VarVarToString";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}
	
	@Test
	public void AccessInstanceVariableIndirectIndirect_MethVarVar(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "AccessInstanceVariableIndirectIndirect_MethVarVar";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}
	
	@Test
	public void AccessInstanceVariableIndirectIndirect_VarVarVar(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "AccessInstanceVariableIndirectIndirect_VarVarVar";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}
	
	@Test
	public void AccessObjectReferenceIndirect_AsParameter_POI(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "AccessObjectReferenceIndirect_AsParameter_POI";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}
	
	@Test
	public void AccessObjectReferenceIndirect_AsParameter(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "AccessObjectReferenceIndirect_AsParameter";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}
	
	@Test
	public void AccessObjectReferenceIndirect_WithinIfStament_POI(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "AccessObjectReferenceIndirect_WithinIfStament_POI";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}
	
	@Test
	public void AccessObjectReferenceIndirect_WithinIfStament(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "AccessObjectReferenceIndirect_WithinIfStament";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}
	
	@Test
	public void AccessStaticVariableIndirect_MethodVar(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "AccessStaticVariableIndirect_MethodVar";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}
	
	@Test
	public void AccessStaticVariableIndirect_VarVar(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "AccessStaticVariableIndirect_VarVar";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}
	
	@Test
	public void AccessStaticVariableIndirect_VarVarToString(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "AccessStaticVariableIndirect_VarVarToString";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}
	
	@Test
	public void AccessStaticVariableIndirectIndirect_MethodVarVar(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "AccessStaticVariableIndirect_VarVarToString";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}
	
	@Test
	public void AccessStaticVariableIndirectIndirect_VarVarVar(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "AccessStaticVariableIndirectIndirect_VarVarVar";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}
	
	@Test
	public void CallInstanceMethodIndirect_MethodMethod(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "CallInstanceMethodIndirect_MethodMethod";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}
	
	@Test
	public void CallInstanceMethodIndirect_MethodMethodToString(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "CallInstanceMethodIndirect_MethodMethodToString";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}
	
	@Test
	public void CallInstanceMethodIndirect_MethodMethod_ViaConstructor(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "CallInstanceMethodIndirect_MethodMethod_ViaConstructor";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}
	
	@Test
	public void CallInstanceMethodIndirect_StaticMethodInstanceMethod(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "CallInstanceMethodIndirect_StaticMethodInstanceMethod";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}
	
	@Test
	public void CallInstanceMethodIndirect_SuperClass(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "CallInstanceMethodIndirect_SuperClass";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}
	
	@Test
	public void CallInstanceMethodIndirect_SuperSuperClass(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "CallInstanceMethodIndirect_SuperSuperClass";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}
	
	@Test
	public void CallInstanceMethodIndirect_VarMethod(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "CallInstanceMethodIndirect_VarMethod";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}
	
	@Test
	public void CallInstanceMethodIndirectIndirect_MethodVarMethod(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "CallInstanceMethodIndirectIndirect_MethodVarMethod";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}
	
	@Test
	public void CallInstanceMethodIndirectIndirect_VarVarMethod(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "CallInstanceMethodIndirectIndirect_VarVarMethod";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}
	
	@Test
	public void CallStaticMethodIndirect_MethodStaticMethod(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "CallStaticMethodIndirect_MethodStaticMethod";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}
	
	@Test
	public void CallStaticMethodIndirect_VarStaticMethod(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "CallStaticMethodIndirect_VarStaticMethod";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}
	
	@Test
	public void InheritanceExtendsExtendsIndirect(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "InheritanceExtendsExtendsIndirect";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("ExtendsConcreteExtendsAbstract");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}
	
	@Test
	public void InheritanceExtendsImplementsIndirect(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "InheritanceExtendsImplementsIndirect";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("ExtendsConcreteImplements");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
	}
	
	@Test
	public void InheritanceImplementsExtendsIndirect(){
		boolean foundFrom = false;
		boolean foundTypes = false;
		String fromToTest = "InheritanceImplementsExtendsIndirect";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("ImplementsExtendsInterface");
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}
		Assert.assertTrue(foundFrom);
		Assert.assertTrue(foundTypes);
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
		logger.info("found dependencies = "+ allDependencies.length);
		for(DependencyDTO d : allDependencies){
			if(d.isIndirect) {
				logger.info("From: " + getClass(d.from) + " To: " + getClass(d.to) + "Type: " + d.type.toString());
			}
			
		}
	}

	private static String getClass(String fromPath){
		return (String) fromPath.subSequence(fromPath.lastIndexOf('.')+1, fromPath.length());
	}
}