package husaccttest.analyse.benchmarkjava.domain;

import java.util.HashMap;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husaccttest.analyse.benchmarkjava.BenchmarkExtended;



public class Hyves extends BenchmarkExtended{

	@Test
	public void testNavigation(){
		String from = "domain.hyves";
		
		int expectedchildmodules = 1;
		AnalysedModuleDTO[] childModules = service.getChildModulesInModule(from);
		assertEquals(expectedchildmodules, childModules.length);
		
		HashMap<String, Object> krabbelExpectedModule = createModuleHashmap(
				"Krabbel", 
				from +  ".Krabbel",
				0,
				super.CLASS);
		
		boolean foundKrabbel = compaireDTOWithValues(krabbelExpectedModule, childModules);
		
		assertEquals(true, foundKrabbel);
	}
	
	@Ignore("AccessOfProperty dubbel + setChanged && notifyObservers are not recognized")
	@Test
	public void testDomainHyvesKrabbel(){
		String from = "domain.hyves.Krabbel";
		int expectedDependencies = 8;
		
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = super.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);

		String fromImportExpected = from;
		String toImportExpected = "infrastructure.blog.ILocation";
		String typeImportExpected = super.IMPORT;
		int linenumberImportExpected = 3;
		
		String fromImplementsExpected = from;
		String toImplementsExpected = "infrastructure.blog.ILocation";
		String typeImplementsExpected = super.IMPLEMENTS;
		int linenumberImplementsExpected = 10;
		
		HashMap<String, Object> dependencyImportExpected = createDependencyHashmap(
				fromImportExpected, toImportExpected, typeImportExpected, linenumberImportExpected);
		HashMap<String, Object> dependencyImplementsExpected = createDependencyHashmap(
				fromImplementsExpected, toImplementsExpected, typeImplementsExpected, linenumberImplementsExpected);
		
		boolean foundImportDependency = compaireDTOWithValues(dependencyImportExpected, dependencies);
		boolean foundImplementsDependency = compaireDTOWithValues(dependencyImplementsExpected, dependencies);
		
		assertEquals(true, foundImportDependency);
		assertEquals(true, foundImplementsDependency);
	}	
}
