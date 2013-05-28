package husaccttest.analyse.csharp.benchmark.accuracy;


import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ProjectDTO;
import husacct.control.ControlServiceImpl;
import husaccttest.analyse.TestProjectFinder;
import java.io.File;
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
	private static String path = new File(TestProjectFinder.lookupProject("csharp", "benchmark")).getAbsolutePath();
	private static String language = "C#";
	
	@SuppressWarnings("static-access")
	@BeforeClass
	public static void beforeClass() {
		try {
			setLog4jConfiguration();
			ArrayList<ProjectDTO> projects = createProjectDTOs();
			
			ServiceProvider.getInstance().getDefineService().createApplication(language+" test", projects, "1.0");
			service = ServiceProvider.getInstance().getAnalyseService();
			ControlServiceImpl ctrlS = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
			ctrlS.getMainController().getApplicationController().analyseApplication();
			
			//analyse is in a different Thread, and needs some time
			while(!isAnalysed){
				try {
					Thread.currentThread().sleep((long)10);
				} catch (InterruptedException e) {}
				isAnalysed = service.isAnalysed();
			}
			allDependencies = service.getAllDependencies();
			//for testing only
			//printDependencies();
			
		} catch (Exception e){
			String errorMessage =  "We're sorry. You need to have a C# project 'benchmark_accuracy'. Or you have the wrong version of the benchmark_accuracy.";
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
	
	//Instance method
	@Test
	public void cSharpCallInstanceMethodTest(){
		boolean found = false;
		String toTestFrom = "CallInstanceMethod";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	instance method, inherited
	@Test
	public void cSharpCallInstanceMethodInheritedTest(){
		boolean found = false;
		String toTestFrom = "CallInstanceMethodInherited";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Class method
	@Test
	public void cSharpCallClassMethodTest(){
		boolean found = false;
		String toTestFrom = "CallClassMethod";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Constructor
	@Test
	public void cSharpCallConstructorTest(){
		boolean found = false;
		String toTestFrom = "CallConstructor";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Inner class method
	@Test
	public void cSharpCallInnerClassMethodTest(){
		boolean found = false;
		String toTestFrom = "CallInnerClassMethod";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Interface method
	@Test
	public void cSharpCallInterfaceMethodTest(){
		boolean found = false;
		String toTestFrom = "CallInterfaceMethod";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Library class method
	@Test
	public void cSharpCallLibraryClassTest(){
		boolean found = false;
		String toTestFrom = "CallLibraryClass";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//
//	Instance variable
	@Test
	public void cSharpAccessInstanceVariableTest(){
		boolean found = false;
		String toTestFrom = "AccessInstanceVariable";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Instance variable, constant
	@Test
	public void cSharpAccessInstanceVariableConstantTest(){
		boolean found = false;
		String toTestFrom = "AccessInstanceVariableConstant";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Instance variable, inherited
	@Test
	public void cSharpAccessInstanceVariableInheritedTest(){
		boolean found = false;
		String toTestFrom = "AccessInstanceVariableInherited";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Class variable
	@Test
	public void cSharpAccessClassVariableTest(){
		boolean found = false;
		String toTestFrom = "AccessClassVariable";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Class variable, constant
	@Test
	public void cSharpAccessClassVariableConstantTest(){
		boolean found = false;
		String toTestFrom = "AccessClassVariableConstant";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Class variable, interface
	@Test
	public void cSharpAccessClassVariableInterfaceTest(){
		boolean found = false;
		String toTestFrom = "AccessClassVariableInterface";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Enumeration
	@Test
	public void cSharpAccessEnumerationTest(){
		boolean found = false;
		String toTestFrom = "AccessEnumeration";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Object reference, ref. variable
	//ToDo tweede ref variabele,
	// eentje in param en eentje in if-statement
	@Test
	public void ObjectReferenceReferenceVariableTest(){
		boolean found = false;
		String toTestFrom = "ReferenceReferenceVariable";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Extends class
	@Test
	public void cSharpInheritanceExtendsClassTest(){
		boolean found = false;
		String toTestFrom = "InheritanceExtends";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Extends abstract class
	@Test
	public void cSharpInheritanceExtendsAbstractTest(){
		boolean found = false;
		String toTestFrom = "InheritanceExtendsAbstract";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Implements interface
	@Test
	public void cSharpInheritanceImplementsInterfaceTest(){
		boolean found = false;
		String toTestFrom = "InheritanceImplementsInterface";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Instance variable
	@Test
	public void cSharpDeclarationInstanceVariableTest(){
		boolean found = false;
		String toTestFrom = "DeclarationInstanceVariable";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Class variable
	@Test
	public void cSharpDeclarationClassVariableTest(){
		boolean found = false;
		String toTestFrom = "DeclarationClassVariable";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Local variable
	@Test
	public void cSharpDeclarationLocalVariableTest(){
		boolean found = false;
		String toTestFrom = "DeclarationLocalVariable";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Parameter
	@Test
	public void cSharpDeclarationParameterTest(){
		boolean found = false;
		String toTestFrom = "DeclarationParameter";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Return type
	@Test
	public void cSharpDeclarationReturnTypeTest(){
		boolean found = false;
		String toTestFrom = "DeclarationReturnType";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Exception
	@Test
	public void cSharpDeclarationExceptionTest(){
		boolean found = false;
		String toTestFrom = "DeclarationExceptionThrows";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Type cast
	@Test
	public void cSharpDeclarationTypeCastTest(){
		boolean found = false;
		String toTestFrom = "DeclarationTypeCast";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Class annotation
	@Test
	public void cSharpAnnotationTest(){
		boolean found = false;
		String toTestFrom = "AnnotationDependency";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Class using
	@Test
	public void cSharpUsingTest(){
		boolean found = false;
		String toTestFrom = "ImportDependencyUnused";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
	
	
	//TESTS
	
	//INDIRECT
//	Access
//	Type: Access � Instance variable
//	From: AccessInstanceVariableIndirect_MethodVar
	@Test
	public void cSharpIAccessInstanceVariableTest(){
		boolean found = false;
		String toTestFrom = "AccessInstanceVariableIndirect_MethodVar";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Type: Access � Instance variable
//	From: AccessInstanceVariableIndirect_VarVar
	@Test
	public void cSharpIAccessInstanceVariableTest2(){
		boolean found = false;
		String toTestFrom = "AccessInstanceVariableIndirect_VarVar";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Type: Access � Instance variable
//	From: AccessInstanceVariableIndirect_VarVarToString
	@Test
	public void cSharpIAccessInstanceVariableTest3(){
		boolean found = false;
		String toTestFrom = "AccessInstanceVariableIndirect_VarVarToString";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Type: Access � Instance variable � Double indirect
//	From: AccessInstanceVariableIndirectIndirect_MethodVarVar
	@Test
	public void cSharpIAccessInstanceVariableDoubleIndirectTest(){
		boolean found = false;
		String toTestFrom = "AccessInstanceVariableIndirectIndirect_MethodVarVar";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Type: Access � Instance variable � Double indirect
//	From: AccessInstanceVariableIndirectIndirect_VarVarVar
	@Test
	public void cSharpIAccessInstanceVariableDoubleIndirectTest2(){
		boolean found = false;
		String toTestFrom = "AccessInstanceVariableIndirectIndirect_VarVarVar";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Type: Access � Instance variable � Inherited
//	From: AccessInstanceVariableIndirect_SuperClass
	@Test
	public void cSharpIAccessInstanceVariableInheritedTest(){
		boolean found = false;
		String toTestFrom = "AccessInstanceVariableIndirect_SuperClass";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Type: Access � Instance variable � Inherited of 2nd super class
//	From: AccessInstanceVariableIndirect_SuperSuperClass
	@Test
	public void cSharpIAccessInstanceVariableInheritedFromSecondSuperClassTest(){
		boolean found = false;
		String toTestFrom = "AccessInstanceVariableIndirect_SuperSuperClass";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Type: Access � Object reference � Reference Variable
//	From: AccessObjectReferenceIndirect_AsParameter_POI
	@Test
	public void cSharpIAccessObjectReferenceReferenceVariableTest(){
		boolean found = false;
		String toTestFrom = "AccessObjectReferenceIndirect_AsParameter_POI";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Type: Access � Object reference � As return value
//	From: AccessObjectReferenceIndirect_AsParameter
	@Test
	public void cSharpIAccessObjectReferenceAsReturnValueTest(){
		boolean found = false;
		String toTestFrom = "AccessObjectReferenceIndirect_AsParameter";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Type: Access � Object reference � Reference Variable
//	From: AccessObjectReferenceIndirect_WithinIfStament_POI
	@Test
	public void cSharpIAccessObjectReferenceReferenceVariableTest2(){
		boolean found = false;
		String toTestFrom = "AccessObjectReferenceIndirect_WithinIfStament_POI";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Type: Access � Object reference � As return value
//	From: AccessObjectReferenceIndirect_WithinIfStament
	@Test
	public void cSharpIAccessObjectReferenceAsReturnValueTest2(){
		boolean found = false;
		String toTestFrom = "AccessObjectReferenceIndirect_WithinIfStament";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Type: Access � Static variable
//	From: AccessStaticVariableIndirect_MethodVar
	@Test
	public void cSharpIAccessStaticVariableTest(){
		boolean found = false;
		String toTestFrom = "AccessStaticVariableIndirect_MethodVar";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Type: Access � Static variable
//	From: AccessStaticVariableIndirect_VarVar
	@Test
	public void cSharpIAccessStaticVariableTest2(){
		boolean found = false;
		String toTestFrom = "AccessStaticVariableIndirect_VarVar";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Type: Access � Static variable
//	From: AccessStaticVariableIndirect_VarVarToString
	@Test
	public void cSharpIAccessStaticVariableTest3(){
		boolean found = false;
		String toTestFrom = "AccessStaticVariableIndirect_VarVarToString";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Type: Access � Static variable � Double indirect
//	From: AccessStaticVariableIndirectIndirect_MethodVarVar
	@Test
	public void cSharpIAccessStaticVariableDoubleIndirectTest(){
		boolean found = false;
		String toTestFrom = "AccessStaticVariableIndirectIndirect_MethodVarVar";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Type: Access � Static variable � Double indirect
//	From: AccessStaticVariableIndirectIndirect_VarVarVar
	@Test
	public void cSharpIAccessStaticVariableDoubleIndirectTest2(){
		boolean found = false;
		String toTestFrom = "AccessStaticVariableIndirectIndirect_VarVarVar";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//
//
//	Call
//	Type: Call � Instance method
//	From: CallInstanceMethodIndirect_MethodMethod
	@Test
	public void cSharpICallInstanceMethodTest(){
		boolean found = false;
		String toTestFrom = "ImportDependencyUnused";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Type: Call � Instance method
//	From: CallInstanceMethodIndirect_MethodMethodToString
	@Test
	public void cSharpICallInstanceMethodTest2(){
		boolean found = false;
		String toTestFrom = "CallInstanceMethodIndirect_MethodMethodToString";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Type: Call � Instance method
//	From: CallInstanceMethodIndirect_MethodMethod_ViaConstructor
	@Test
	public void cSharpICallInstanceMethodTest3(){
		boolean found = false;
		String toTestFrom = "CallInstanceMethodIndirect_MethodMethod_ViaConstructor";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Type: Call � Static method
//	From: CallInstanceMethodIndirect_StaticMethodInstanceMethod
	@Test
	public void cSharpICallStaticMethodTest(){
		boolean found = false;
		String toTestFrom = "CallInstanceMethodIndirect_StaticMethodInstanceMethod";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Type: Call � Instance method � Inherited (Virtual call)
//	From: CallInstanceMethodIndirect_SuperClass
	@Test
	public void cSharpICallInstanceMethodInheritedVirtualCallTest(){
		boolean found = false;
		String toTestFrom = "CallInstanceMethodIndirect_SuperClass";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Type: Call � Instance method � Inherited of 2nd super class
//	From: CallInstanceMethodIndirect_SuperSuperClass
	@Test
	public void cSharpICallInstanceMethodInheritedFromSecondSuperClassTest(){
		boolean found = false;
		String toTestFrom = "CallInstanceMethodIndirect_SuperSuperClass";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Type: Call � Instance method
//	From: CallInstanceMethodIndirect_VarMethod
	@Test
	public void cSharpICallInstanceMethodTest4(){
		boolean found = false;
		String toTestFrom = "CallInstanceMethodIndirect_VarMethod";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Type: Call � Instance method � Double indirect
//	From: CallInstanceMethodIndirectIndirect_MethodVarMethod
	@Test
	public void cSharpICallInstanceMethodDoubleIndirectTest(){
		boolean found = false;
		String toTestFrom = "CallInstanceMethodIndirectIndirect_MethodVarMethod";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Type: Call � Instance method � Double indirect
//	From: CallInstanceMethodIndirectIndirect_VarVarMethod
	@Test
	public void cSharpICallInstanceMethodDoubleIndirectTest2(){
		boolean found = false;
		String toTestFrom = "CallInstanceMethodIndirectIndirect_VarVarMethod";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Type: Call � Static method
//	From: CallStaticMethodIndirect_MethodStaticMethod
	@Test
	public void cSharpICallStaticMethodTest2(){
		boolean found = false;
		String toTestFrom = "CallStaticMethodIndirect_MethodStaticMethod";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//	Type: Call � Static method
//	From: CallStaticMethodIndirect_VarStaticMethod
	@Test
	public void cSharpICallStaticMethodTest3(){
		boolean found = false;
		String toTestFrom = "CallStaticMethodIndirect_VarStaticMethod";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			if(toTestFrom.equals(from)){
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
//
//
//	Inheritance
//	Type: Inheritance � Extends 
//	From: InheritanceExtendsExtendsIndirect
	@Test
	public void cSharpIInheritenceExtendsTest(){
		boolean found = false;
		String toTestFrom = "InheritanceExtendsExtendsIndirect";
		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
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
		ArrayList<AnalysedModuleDTO> analysedModules = new ArrayList<AnalysedModuleDTO>();
		ProjectDTO project = new ProjectDTO("TestAccuracy", paths, language, "version0", "for testing purposes", analysedModules);
		projects.add(project);
		return projects;
	}
	private static void setLog4jConfiguration() {
		URL propertiesFile = Class.class.getResource("/husacct/common/resources/husacct.properties");
		PropertyConfigurator.configure(propertiesFile);
		logger = Logger.getLogger(DependencyDetectionAccuracyTest.class);
	}
	private static void printDependencies() {
		logger.info("application is analysed");
		logger.info("found dependencies = "+allDependencies.length);
		for(DependencyDTO d : allDependencies){
			logger.info(d.type);
			//logger.info(getClass(d.from));
		}
	}
	private static String getClass(String fromPath){
		return (String) fromPath.subSequence(fromPath.lastIndexOf('.')+1, fromPath.length());
	}
}