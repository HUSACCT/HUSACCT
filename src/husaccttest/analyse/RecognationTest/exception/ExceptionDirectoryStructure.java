package husaccttest.analyse.RecognationTest.exception;

import java.util.HashMap;
import static org.junit.Assert.*;

import husacct.common.dto.AnalysedModuleDTO;
import husaccttest.analyse.benchmarkLeo.BenchmarkExtended;

public class ExceptionDirectoryStructure extends BenchmarkExtended {

	
	public void testNavigationRootModules(){
		String from = "exception";
		
		int expectedchildmodules = 3;
		AnalysedModuleDTO[] childModules = service.getChildModulesInModule(from);
		assertEquals(expectedchildmodules, childModules.length);
		
		HashMap<String, Object> apackageExpectedModule = createModuleHashmap(
				"apackage", 
				from +  ".apackage",
				0,
				super.PACKAGE);
		
		HashMap<String, Object> bpackageExpectedModule = createModuleHashmap(
				"bpackage", 
				from +  ".bpackage",
				0,
				super.PACKAGE);
		
		HashMap<String, Object> exceptionclassExpectedModule = createModuleHashmap(
				"MyException", 
				from +  ".MyException",
				0,
				super.CLASS);
		
		boolean foundinpackage = compaireDTOWithValues(apackageExpectedModule, childModules);
		boolean foundouterpackage = compaireDTOWithValues(bpackageExpectedModule, childModules);
		boolean foundexception = compaireDTOWithValues(exceptionclassExpectedModule, childModules);
		
		assertEquals(true, foundinpackage);
		assertEquals(true, foundouterpackage);
		assertEquals(true, foundexception);
	}
	
	public void testNavigationSubClassesInpackage(){
		String from = "exception.apackage";
		
		int expectedchildmodules = 5;
		AnalysedModuleDTO[] childModules = service.getChildModulesInModule(from);
		assertEquals(expectedchildmodules, childModules.length);
		
		HashMap<String, Object> anotherClassExpectedModule = createModuleHashmap(
				"AnotherClass", 
				from +  ".AnotherClass",
				0,
				super.CLASS);
		
		HashMap<String, Object> bnotherClassExpectedModule = createModuleHashmap(
				"BnotherClass", 
				from +  ".BnotherClass",
				0,
				super.CLASS);
		
		HashMap<String, Object> cnotherClassExpectedModule = createModuleHashmap(
				"CnotherClass", 
				from +  ".CnotherClass",
				0,
				super.CLASS);
		
		HashMap<String, Object> myClassExpectedModule = createModuleHashmap(
				"MyClass", 
				from +  ".MyClass",
				0,
				super.CLASS);
		
		HashMap<String, Object> otherClassExpectedModule = createModuleHashmap(
				"OtherClass", 
				from +  ".OtherClass",
				0,
				super.CLASS);
		
		boolean foundanother = compaireDTOWithValues(anotherClassExpectedModule, childModules);
		boolean foundbnother = compaireDTOWithValues(bnotherClassExpectedModule, childModules);
		boolean foundcnother = compaireDTOWithValues(cnotherClassExpectedModule, childModules);
		boolean foundmyclass = compaireDTOWithValues(myClassExpectedModule, childModules);
		boolean foundother = compaireDTOWithValues(otherClassExpectedModule, childModules);
		
		assertEquals(true, foundanother);
		assertEquals(true, foundbnother);
		assertEquals(true, foundcnother);
		assertEquals(true, foundmyclass);
		assertEquals(true, foundother);
	}
	
	public void testNavigationSubClassesOuterpackage(){
		String from = "exception.bpackage";
		
		int expectedchildmodules = 1;
		AnalysedModuleDTO[] childModules = service.getChildModulesInModule(from);
		assertEquals(expectedchildmodules, childModules.length);
		
		HashMap<String, Object> otherExpectedModule = createModuleHashmap(
				"OtherClass", 
				from +  ".OtherClass",
				0,
				super.CLASS);

		
		boolean foundother = compaireDTOWithValues(otherExpectedModule, childModules);
		
		assertEquals(true, foundother);
	}
}
