package husaccttest.analyse.RecognationTest.declarations;

import java.util.HashMap;

import husacct.common.dto.AnalysedModuleDTO;
import husaccttest.analyse.benchmarkLeo.BenchmarkExtended;
import static org.junit.Assert.*;


public class DeclarationDirectoryStructure extends BenchmarkExtended {

	
	public void testNavigationRootModules(){
		String from = "declarations";
		
		int expectedchildmodules = 2;
		AnalysedModuleDTO[] childModules = service.getChildModulesInModule(from);
		assertEquals(expectedchildmodules, childModules.length);
		
		HashMap<String, Object> inpackageExpectedModule = createModuleHashmap(
				"inpackage", 
				from +  ".inpackage",
				0,
				super.PACKAGE);
		
		HashMap<String, Object> outerpackageExpectedModule = createModuleHashmap(
				"outerpackage", 
				from +  ".outerpackage",
				0,
				super.PACKAGE);
		
		boolean foundinpackage = compaireDTOWithValues(inpackageExpectedModule, childModules);
		boolean foundouterpackage = compaireDTOWithValues(outerpackageExpectedModule, childModules);
		
		assertEquals(true, foundinpackage);
		assertEquals(true, foundouterpackage);
	}
	
	public void testNavigationSubClassesInpackage(){
		String from = "declarations.inpackage";
		
		int expectedchildmodules = 3;
		AnalysedModuleDTO[] childModules = service.getChildModulesInModule(from);
		assertEquals(expectedchildmodules, childModules.length);
		
		HashMap<String, Object> phoneExpectedModule = createModuleHashmap(
				"Phone", 
				from +  ".Phone",
				0,
				super.CLASS);
		
		HashMap<String, Object> toyExpectedModule = createModuleHashmap(
				"Toy", 
				from +  ".Toy",
				0,
				super.CLASS);
		
		HashMap<String, Object> userExpectedModule = createModuleHashmap(
				"User", 
				from +  ".User",
				0,
				super.CLASS);
		
		boolean foundphone = compaireDTOWithValues(phoneExpectedModule, childModules);
		boolean foundtoy = compaireDTOWithValues(toyExpectedModule, childModules);
		boolean founduser = compaireDTOWithValues(userExpectedModule, childModules);
		
		assertEquals(true, foundphone);
		assertEquals(true, foundtoy);
		assertEquals(true, founduser);
	}
	
	public void testNavigationSubClassesOuterpackage(){
		String from = "declarations.outerpackage";
		
		int expectedchildmodules = 2;
		AnalysedModuleDTO[] childModules = service.getChildModulesInModule(from);
		assertEquals(expectedchildmodules, childModules.length);
		
		HashMap<String, Object> package1ExpectedModule = createModuleHashmap(
				"package1", 
				from +  ".package1",
				0,
				super.PACKAGE);
		
		HashMap<String, Object> package2ExpectedModule = createModuleHashmap(
				"package2", 
				from +  ".package2",
				0,
				super.PACKAGE);

		
		boolean foundpackage1 = compaireDTOWithValues(package1ExpectedModule, childModules);
		boolean foundpackage2 = compaireDTOWithValues(package2ExpectedModule, childModules);
		
		assertEquals(true, foundpackage1);
		assertEquals(true, foundpackage2);
	}
	
	public void testNavigationSubClassesOuterpackage1(){
		String from = "declarations.outerpackage.package1";
		
		int expectedchildmodules = 1;
		AnalysedModuleDTO[] childModules = service.getChildModulesInModule(from);
		assertEquals(expectedchildmodules, childModules.length);
		
		HashMap<String, Object> driverExpectedModule = createModuleHashmap(
				"Driver", 
				from +  ".Driver",
				0,
				super.CLASS);

		
		boolean founddriver = compaireDTOWithValues(driverExpectedModule, childModules);
		
		assertEquals(true, founddriver);
	}
	
	public void testNavigationSubClassesOuterpackage2(){
		String from = "declarations.outerpackage.package2";
		
		int expectedchildmodules = 3;
		AnalysedModuleDTO[] childModules = service.getChildModulesInModule(from);
		assertEquals(expectedchildmodules, childModules.length);
		
		HashMap<String, Object> bycicleExpectedModule = createModuleHashmap(
				"Bycicle", 
				from +  ".Bycicle",
				0,
				super.CLASS);
		
		HashMap<String, Object> carExpectedModule = createModuleHashmap(
				"Car", 
				from +  ".Car",
				0,
				super.CLASS);

		HashMap<String, Object> motorExpectedModule = createModuleHashmap(
				"Motor", 
				from +  ".Motor",
				0,
				super.CLASS);
		
		boolean foundbycicle = compaireDTOWithValues(bycicleExpectedModule, childModules);
		boolean foundcar = compaireDTOWithValues(carExpectedModule, childModules);
		boolean foundmotor = compaireDTOWithValues(motorExpectedModule, childModules);
		
		assertEquals(true, foundbycicle);
		assertEquals(true, foundcar);
		assertEquals(true, foundmotor);
		
	}
	
	
}
