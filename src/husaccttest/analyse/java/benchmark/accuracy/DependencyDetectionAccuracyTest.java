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
	//DIRECT
	@Test
	public void AccessClassVariable(){
		String fromToTest = "AccessClassVariable";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("AccessPropertyOrField");

		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void AccessClassVariableConstant(){
		String fromToTest = "AccessClassVariableConstant";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("AccessPropertyOrField");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void AccessClassVariableInterface(){
		String fromToTest = "AccessClassVariableInterface";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("AccessPropertyOrField");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void AccessEnumeration(){
		String fromToTest = "AccessEnumeration";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("AccessPropertyOrField");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void AccessInstanceVariable(){
		String fromToTest = "AccessInstanceVariable";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void AccessInstanceVariableConstant(){
		String fromToTest = "AccessVariableInstanceConstant";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void AccessObjectReferenceAsParameter(){
		String fromToTest = "AccessObjectReferenceParameter";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void AccessObjectReferenceWithinIfStatement(){
		String fromToTest = "AccessObjectReferenceWithinStatement";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void AnnotationDependency(){
		String fromToTest = "AnnotationDependency";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("Annotation");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void CallClassMethod(){
		String fromToTest = "CallClassMethod";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("InvocMethod");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void CallConstructor(){
		String fromToTest = "CallConstructor";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("InvocConstructor");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void CallConstructorLibraryClass(){
		String fromToTest = "CallConstructorLibraryClass";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("InvocConstructor");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void CallInstance(){
		String fromToTest = "CallInstance";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("ExtendsConcrete");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void CallInstanceInnerClass(){
		String fromToTest = "CallInstanceInnerClass";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("ExtendsConcrete");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void CallInstanceInterface(){
		String fromToTest = "CallInstanceInterface";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("ExtendsConcrete");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void CallInstanceLibraryClass(){
		String fromToTest = "CallInstanceLibraryClass";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("ExtendsConcrete");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void CallInstanceSuperClass(){
		String fromToTest = "CallInstanceSuperClass";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("ExtendsConcrete");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void CallInstanceSuperSuperClass(){
		String fromToTest = "CallInstanceSuperSuperClass";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("ExtendsConcrete");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void DeclarationExceptionThrows(){
		String fromToTest = "DeclarationExceptionThrows";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("Exception");
		typesToFind.add("Exception");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void DeclarationParameter(){
		String fromToTest = "DeclarationParameter";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("DeclarationParameter");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void DeclarationReturnType(){
		String fromToTest = "DeclarationReturnType";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("DeclarationReturnType");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void DeclarationTypeCast(){
		String fromToTest = "DeclarationTypeCast";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("undefined");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void DeclarationTypeCastOfArgument(){
		String fromToTest = "DeclarationTypeCastOfArgument";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("undefined");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void DeclarationVariableInstance(){
		String fromToTest = "DeclarationVariableInstance";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("DeclarationInstanceVariable");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void DeclarationVariableStatic(){
		String fromToTest = "DeclarationVariableStatic";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("DeclarationInstanceVariable");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void DeclarationVariableWithinMethod(){
		String fromToTest = "DeclarationVariableWithinMethod";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("DeclarationInstanceVariable");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void ImportDependencyUnused(){
		String fromToTest = "ImportDependencyUnused";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");

		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void InheritanceExtends(){
		String fromToTest = "InheritanceExtends";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("ExtendsConcrete");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));	}

	@Test
	public void InheritanceExtendsAbstractClass(){
		String fromToTest = "InheritanceExtendsAbstractClass";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("ExtendsAbstract");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void InheritanceImplementsInterface(){
		String fromToTest = "InheritanceImplementsInterface";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		typesToFind.add("Implements");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	//TESTS
	//INDIRECT

	@Test
	public void AccessInstanceVariableIndirect_MethodVar(){
		String fromToTest = "AccessInstanceVariableIndirect_MethodVar";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void AccessInstanceVariableIndirect_VarVar(){
		String fromToTest = "AccessInstanceVariableIndirect_VarVar";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void AccessInstanceVariableIndirect_VarVarToString(){
		String fromToTest = "AccessInstanceVariableIndirect_VarVarToString";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void AccessInstanceVariableIndirectIndirect_MethVarVar(){
		String fromToTest = "AccessInstanceVariableIndirectIndirect_MethVarVar";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void AccessInstanceVariableIndirectIndirect_VarVarVar(){
		String fromToTest = "AccessInstanceVariableIndirectIndirect_VarVarVar";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void AccessObjectReferenceIndirect_AsParameter_POI(){
		String fromToTest = "AccessObjectReferenceIndirect_AsParameter_POI";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void AccessObjectReferenceIndirect_AsParameter(){
		String fromToTest = "AccessObjectReferenceIndirect_AsParameter";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void AccessObjectReferenceIndirect_WithinIfStament_POI(){
		String fromToTest = "AccessObjectReferenceIndirect_WithinIfStament_POI";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void AccessObjectReferenceIndirect_WithinIfStament(){
		String fromToTest = "AccessObjectReferenceIndirect_WithinIfStament";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void AccessStaticVariableIndirect_MethodVar(){
		String fromToTest = "AccessStaticVariableIndirect_MethodVar";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");

		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void AccessStaticVariableIndirect_VarVar(){
		String fromToTest = "AccessStaticVariableIndirect_VarVar";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void AccessStaticVariableIndirect_VarVarToString(){
		String fromToTest = "AccessStaticVariableIndirect_VarVarToString";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void AccessStaticVariableIndirectIndirect_MethodVarVar(){
		String fromToTest = "AccessStaticVariableIndirect_VarVarToString";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void AccessStaticVariableIndirectIndirect_VarVarVar(){
		String fromToTest = "AccessStaticVariableIndirectIndirect_VarVarVar";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");

		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void CallInstanceMethodIndirect_MethodMethod(){
		String fromToTest = "CallInstanceMethodIndirect_MethodMethod";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void CallInstanceMethodIndirect_MethodMethodToString(){
		String fromToTest = "CallInstanceMethodIndirect_MethodMethodToString";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void CallInstanceMethodIndirect_MethodMethod_ViaConstructor(){
		String fromToTest = "CallInstanceMethodIndirect_MethodMethod_ViaConstructor";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void CallInstanceMethodIndirect_StaticMethodInstanceMethod(){
		String fromToTest = "CallInstanceMethodIndirect_StaticMethodInstanceMethod";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void CallInstanceMethodIndirect_SuperClass(){
		String fromToTest = "CallInstanceMethodIndirect_SuperClass";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void CallInstanceMethodIndirect_SuperSuperClass(){
		String fromToTest = "CallInstanceMethodIndirect_SuperSuperClass";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void CallInstanceMethodIndirect_VarMethod(){
		String fromToTest = "CallInstanceMethodIndirect_VarMethod";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void CallInstanceMethodIndirectIndirect_MethodVarMethod(){
		String fromToTest = "CallInstanceMethodIndirectIndirect_MethodVarMethod";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void CallInstanceMethodIndirectIndirect_VarVarMethod(){
		String fromToTest = "CallInstanceMethodIndirectIndirect_VarVarMethod";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void CallStaticMethodIndirect_MethodStaticMethod(){
		String fromToTest = "CallStaticMethodIndirect_MethodStaticMethod";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void CallStaticMethodIndirect_VarStaticMethod(){
		String fromToTest = "CallStaticMethodIndirect_VarStaticMethod";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void InheritanceExtendsExtendsIndirect(){
		String fromToTest = "InheritanceExtendsExtendsIndirect";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("ExtendsConcreteExtendsAbstract");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void InheritanceExtendsImplementsIndirect(){
		String fromToTest = "InheritanceExtendsImplementsIndirect";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("ExtendsConcreteImplements");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
	}

	@Test
	public void InheritanceImplementsExtendsIndirect(){
		String fromToTest = "InheritanceImplementsExtendsIndirect";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("ImplementsExtendsInterface");
		
		Assert.assertTrue(searchDependencies(fromToTest, typesToFind));
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
			logger.info("From: " + getClass(d.from) + " To: " + getClass(d.to) + "Type: " + d.type.toString());
		}
	}

	private static String getClass(String fromPath){
		return (String) fromPath.subSequence(fromPath.lastIndexOf('.')+1, fromPath.length());
	}

	private static boolean searchDependencies(String fromToTest, ArrayList<String> typesToFind) {
		boolean foundFrom = false;
		boolean foundTypes = false;
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

		return (foundFrom == foundTypes && foundTypes == true) ? true : false;
	}
}