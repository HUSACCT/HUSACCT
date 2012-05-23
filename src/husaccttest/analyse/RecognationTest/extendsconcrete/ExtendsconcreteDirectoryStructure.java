package husaccttest.analyse.RecognationTest.extendsconcrete;

import java.util.HashMap;

import husacct.common.dto.AnalysedModuleDTO;
import husaccttest.analyse.benchmarkLeo.BenchmarkExtended;

public class ExtendsconcreteDirectoryStructure extends BenchmarkExtended {

	
	public void testNavigationRootModules(){
		String from = "extendsconcrete";
		
		int expectedchildmodules = 2;
		AnalysedModuleDTO[] childModules = service.getChildModulesInModule(from);
		assertEquals(expectedchildmodules, childModules.length);
		
		HashMap<String, Object> apackageExpectedModule = createModuleHashmap(
				"package1", 
				from +  ".package1",
				0,
				super.PACKAGE);
		
		HashMap<String, Object> bpackageExpectedModule = createModuleHashmap(
				"package2", 
				from +  ".package2",
				0,
				super.PACKAGE);
		
		boolean foundinpackage = compaireDTOWithValues(apackageExpectedModule, childModules);
		boolean foundouterpackage = compaireDTOWithValues(bpackageExpectedModule, childModules);
		
		assertEquals(true, foundinpackage);
		assertEquals(true, foundouterpackage);
	}
	
	public void testNavigationSubClassesPackage1(){
		String from = "extendsconcrete.package1";
		
		int expectedchildmodules = 2;
		AnalysedModuleDTO[] childModules = service.getChildModulesInModule(from);
		assertEquals(expectedchildmodules, childModules.length);
		
		HashMap<String, Object> concreteChildClassExpectedModule = createModuleHashmap(
				"ConcreteChildInPackage", 
				from +  ".ConcreteChildInPackage",
				0,
				super.CLASS);
		
		HashMap<String, Object> concreteParentClassExpectedModule = createModuleHashmap(
				"ConcreteParent", 
				from +  ".ConcreteParent",
				0,
				super.CLASS);
	
		boolean foundconcreteChild = compaireDTOWithValues(concreteChildClassExpectedModule, childModules);
		boolean foundconcreteParent = compaireDTOWithValues(concreteParentClassExpectedModule, childModules);

		assertEquals(true, foundconcreteChild);
		assertEquals(true, foundconcreteParent);
	}
	
	public void testNavigationSubClassesOuterpackage(){
		String from = "extendsconcrete.package2";
		
		int expectedchildmodules = 3;
		AnalysedModuleDTO[] childModules = service.getChildModulesInModule(from);
		assertEquals(expectedchildmodules, childModules.length);
		
		HashMap<String, Object> completeDefinitionExpectedModule = createModuleHashmap(
				"ConcreteChildCompleteDefinition", 
				from +  ".ConcreteChildCompleteDefinition",
				0,
				super.CLASS);

		HashMap<String, Object> importsClassExpectedModule = createModuleHashmap(
				"ConcreteChildImportsClass", 
				from +  ".ConcreteChildImportsClass",
				0,
				super.CLASS);
		
		HashMap<String, Object> importsPackageExpectedModule = createModuleHashmap(
				"ConcreteChildImportsPackage", 
				from +  ".ConcreteChildImportsPackage",
				0,
				super.CLASS);
		
		
		boolean foundcompleteDefinition = compaireDTOWithValues(completeDefinitionExpectedModule, childModules);
		boolean foundimportsClass = compaireDTOWithValues(importsClassExpectedModule, childModules);
		boolean foundimportsPackage = compaireDTOWithValues(importsPackageExpectedModule, childModules);
		
		assertEquals(true, foundcompleteDefinition);
		assertEquals(true, foundimportsClass);
		assertEquals(true, foundimportsPackage);
	}
}
