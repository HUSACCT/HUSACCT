package husaccttest.analyse.benchmarkjava.domain;

import java.util.HashMap;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husaccttest.analyse.benchmarkjava.BenchmarkExtended;



public class FoursquarealternativeDependencies extends BenchmarkExtended{

	@Test
	public void testDomainBrightkiteAccount1(){
		String from = "domain.foursquarealternative.brightkite.Account1";
		int expectedDependencies = 2;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String fromImportExpected = from;
		String toImportExpected = "domain.foursquarealternative.whrrl.Whrrl";
		String typeImportExpected = super.IMPORT;
		int linenumberImportExpected = 3;
		
		String fromExtendsExpected = from;
		String toExtendsExpected = "domain.foursquarealternative.whrrl.Whrrl";
		String typeExtendsExpected = super.EXTENDSCONCRETE;
		int linenumberExtendsExpected = 10;
		
		HashMap<String, Object> dependencyImportExpected = createDependencyHashmap(
				fromImportExpected, toImportExpected, typeImportExpected, linenumberImportExpected);
		HashMap<String, Object> dependencyExtendsExpected = createDependencyHashmap(
				fromExtendsExpected, toExtendsExpected, typeExtendsExpected, linenumberExtendsExpected);
		
		boolean foundImportDependency = compaireDTOWithValues(dependencyImportExpected, dependencies);
		boolean foundExtendsDependency = compaireDTOWithValues(dependencyExtendsExpected, dependencies);
		assertEquals(true, foundImportDependency);
		assertEquals(true, foundExtendsDependency);
	}
	
	@Test
	public void testDomainBrightKiteAccount2(){
		String from = "domain.foursquarealternative.brightkite.Account2";
		int expectedDependencies = 2;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String fromImportExpected = from;
		String toImportExpected = "domain.foursquarealternative.whrrl.IWhrrl";
		String typeImportExpected = super.IMPORT;
		int linenumberImportExpected = 4;
		
		String fromExtendsExpected = from;
		String toExtendsExpected = "domain.foursquarealternative.whrrl.IWhrrl";
		String typeExtendsExpected = super.IMPLEMENTS;
		int linenumberExtendsExpected = 11;
		
		HashMap<String, Object> dependencyImportExpected = createDependencyHashmap(
				fromImportExpected, toImportExpected, typeImportExpected, linenumberImportExpected);
		HashMap<String, Object> dependencyExtendsExpected = createDependencyHashmap(
				fromExtendsExpected, toExtendsExpected, typeExtendsExpected, linenumberExtendsExpected);
		
		boolean foundImportDependency = compaireDTOWithValues(dependencyImportExpected, dependencies);
		boolean foundExtendsDependency = compaireDTOWithValues(dependencyExtendsExpected, dependencies);
		assertEquals(true, foundImportDependency);
		assertEquals(true, foundExtendsDependency);
	}
	
	@Test
	public void testDomainBrightKiteCheckCastFrom(){
		String from = "domain.foursquarealternative.brightkite.CheckCastFrom";
		int expectedDependencies = 6;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		super.printDependencies(dependencies);
		assertEquals(expectedDependencies, dependencies.length);
		
		String fromImport1Expected = from;
		String toImport1Expected = "domain.foursquarealternative.SameExtend";
		String typeImport1Expected = super.IMPORT;
		int linenumberImport1Expected = 3;
		
		String fromImport2Expected = from;
		String toImport2Expected = "domain.foursquarealternative.SameExtend";
		String typeImport2Expected = super.IMPORT;
		int linenumberImport2Expected = 4;
		
		String fromExtendsExpected = from;
		String toExtendsExpected = "domain.foursquarealternative.whrrl.IWhrrl";
		String typeExtendsExpected = super.IMPLEMENTS;
		int linenumberExtendsExpected = 11;
//		
//		HashMap<String, Object> dependencyImportExpected = createDependencyHashmap(
//				fromImportExpected, toImportExpected, typeImportExpected, linenumberImportExpected);
//		HashMap<String, Object> dependencyExtendsExpected = createDependencyHashmap(
//				fromExtendsExpected, toExtendsExpected, typeExtendsExpected, linenumberExtendsExpected);
//		
//		boolean foundImportDependency = compaireDTOWithValues(dependencyImportExpected, dependencies);
//		boolean foundExtendsDependency = compaireDTOWithValues(dependencyExtendsExpected, dependencies);
//		assertEquals(true, foundImportDependency);
//		assertEquals(true, foundExtendsDependency);
	}
	
	
}
