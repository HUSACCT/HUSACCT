package husaccttest.analyse.benchmarkjava.domain;

import java.util.HashMap;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
import husacct.common.dto.DependencyDTO;
import husaccttest.analyse.benchmarkjava.BenchmarkExtended;



public class FoursquarealternativeKilroy extends BenchmarkExtended{

	@Test
	public void testDomainKilroyCheckCastFrom(){
		String from = "domain.foursquarealternative.kilroy.CheckCastFrom";
		int expectedDependencies = 6;
		
		//DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = super.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String toImport1Expected = "domain.foursquarealternative.SameExtend";
		int linenumberImport1Expected = 3;
		
		String toImport2Expected = "domain.foursquarealternative.whrrl.CheckCastTo";
		int linenumberImport2Expected = 4;
		
		String fromExtendsExpected = from;
		String toExtendsExpected = "domain.foursquarealternative.SameExtend";
		String typeExtendsExpected = super.EXTENDSCONCRETE;
		int linenumberExtendsExpected = 8;
		
		String fromDeclaration1Expected = from;
		String toDeclaration1Expected = "Object";
		String typeDeclaration1Expected = super.DECLARATION;
		int linenumberDeclaration1Expected = 11;
		
		String fromDeclaration2Expected = from;
		String toDeclaration2Expected = "domain.foursquarealternative.whrrl.CheckCastTo";
		String typeDeclaration2Expected = super.DECLARATION;
		int linenumberDeclaration2Expected = 11;
		
		String fromInvocConstructorExpected = from;
		String toInvocConstructorExpected = "domain.foursquarealternative.SameExtend";
		String typeInvocConstructorExpected = super.INVOCCONSTRUCTOR;
		int linenumberInvocConstructorExpected = 11;
		
		HashMap<String, Object> dependencyImport1Expected = super.createImportHashmap(from, toImport1Expected, linenumberImport1Expected);
		HashMap<String, Object> dependencyImport2Expected = super.createImportHashmap(from, toImport2Expected, linenumberImport2Expected);
		HashMap<String, Object> dependencyExtendsExpected = createDependencyHashmap(
				fromExtendsExpected, toExtendsExpected, typeExtendsExpected, linenumberExtendsExpected);
		HashMap<String, Object> dependencyDeclaration1Expected = createDependencyHashmap(
				fromDeclaration1Expected, toDeclaration1Expected, typeDeclaration1Expected, linenumberDeclaration1Expected);
		HashMap<String, Object> dependencyDeclaration2Expected = createDependencyHashmap(
				fromDeclaration2Expected, toDeclaration2Expected, typeDeclaration2Expected, linenumberDeclaration2Expected);
		HashMap<String, Object> dependencyInvocConstructorExpected = createDependencyHashmap(
				fromInvocConstructorExpected, toInvocConstructorExpected, typeInvocConstructorExpected, linenumberInvocConstructorExpected);
		
		boolean foundImport1Dependency = compaireDTOWithValues(dependencyImport1Expected, dependencies);
		boolean foundImport2Dependency = compaireDTOWithValues(dependencyImport2Expected, dependencies);
		boolean foundExtendsDependency = compaireDTOWithValues(dependencyExtendsExpected, dependencies);
		boolean foundDeclaration1Dependency = compaireDTOWithValues(dependencyDeclaration1Expected, dependencies);
		boolean foundDeclaration2Dependency = compaireDTOWithValues(dependencyDeclaration2Expected, dependencies);
		boolean foundInvocConstructorDependency = compaireDTOWithValues(dependencyInvocConstructorExpected, dependencies);
		
		assertEquals(true, foundImport1Dependency);
		assertEquals(true, foundImport2Dependency);
		assertEquals(true, foundExtendsDependency);
		assertEquals(true, foundDeclaration2Dependency);
		assertEquals(true, foundDeclaration1Dependency);
		assertEquals(true, foundInvocConstructorDependency);
	}
	
	@Test
	public void testDomainKilroyComment(){
		String from = "domain.foursquarealternative.kilroy.Comment";
		int expectedDependencies = 2;
		
		//DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = super.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String toImport1Expected = "domain.foursquarealternative.whrrl.WhrrlComment";
		int linenumberImport1Expected = 3;
		
		
		String fromExtendsExpected = from;
		String toExtendsExpected = "domain.foursquarealternative.whrrl.WhrrlComment";
		String typeExtendsExpected = super.EXTENDSCONCRETE;
		int linenumberExtendsExpected = 7;

		
		HashMap<String, Object> dependencyImport1Expected = super.createImportHashmap(from, toImport1Expected, linenumberImport1Expected);
		HashMap<String, Object> dependencyExtendsExpected = createDependencyHashmap(
				fromExtendsExpected, toExtendsExpected, typeExtendsExpected, linenumberExtendsExpected);

		boolean foundImport1Dependency = compaireDTOWithValues(dependencyImport1Expected, dependencies);
		boolean foundExtendsDependency = compaireDTOWithValues(dependencyExtendsExpected, dependencies);
		
		assertEquals(true, foundImport1Dependency);
		assertEquals(true, foundExtendsDependency);
	}
	
	@Test
	public void testDomainKilroyFuture(){
		String from = "domain.foursquarealternative.kilroy.Future";
		int expectedDependencies = 3;
		
		//DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = super.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String toImport1Expected = "domain.foursquarealternative.whrrl.WhrrlFuture";
		int linenumberImport1Expected = 3;
		
		String fromInvocConstructorExpected = from;
		String toInvocConstructorExpected = "domain.foursquarealternative.whrrl.WhrrlFuture";
		String typeInvocConstructorExpected = super.INVOCCONSTRUCTOR;
		int linenumberInvocConstructorExpected = 9;
		
		String fromInvocMethodExpected = from;
		String toInvocMethodExpected = "domain.foursquarealternative.whrrl.WhrrlFuture";
		String typeInvocMethodExpected = super.INVOCMETHOD;
		int linenumberInvocMethodExpected = 9;

		
		HashMap<String, Object> dependencyImport1Expected = super.createImportHashmap(from, toImport1Expected, linenumberImport1Expected);
		HashMap<String, Object> dependencyInvocConstructorExpected = createDependencyHashmap(
				fromInvocConstructorExpected, toInvocConstructorExpected, typeInvocConstructorExpected, linenumberInvocConstructorExpected);
		HashMap<String, Object> dependencyInvocMethodExpected = createDependencyHashmap(
				fromInvocMethodExpected, toInvocMethodExpected, typeInvocMethodExpected, linenumberInvocMethodExpected);

		boolean foundImport1Dependency = compaireDTOWithValues(dependencyImport1Expected, dependencies);
		boolean foundInvocConstructorDependency = compaireDTOWithValues(dependencyInvocConstructorExpected, dependencies);
		boolean foundInvocMethodDependency = compaireDTOWithValues(dependencyInvocMethodExpected, dependencies);
		
		assertEquals(true, foundImport1Dependency);
		assertEquals(true, foundInvocConstructorDependency);
		assertEquals(true, foundInvocMethodDependency);
	}
	
	@Ignore ("InvocMethod on an accesField not recognized")
	@Test
	public void testDomainKilroyMap(){
		String from = "domain.foursquarealternative.kilroy.Map";
		int expectedDependencies = 3;
	
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = super.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String toImport1Expected = "domain.foursquarealternative.whrrl.WhrrlFuture";
		int linenumberImport1Expected = 3;
		
		String fromInvocConstructorExpected = from;
		String toInvocConstructorExpected = "domain.foursquarealternative.whrrl.WhrrlFuture";
		String typeInvocConstructorExpected = super.INVOCCONSTRUCTOR;
		int linenumberInvocConstructorExpected = 9;
		
		String fromInvocMethodExpected = from;
		String toInvocMethodExpected = "domain.foursquarealternative.whrrl.WhrrlFuture";
		String typeInvocMethodExpected = super.INVOCMETHOD;
		int linenumberInvocMethodExpected = 9;

		
		HashMap<String, Object> dependencyImport1Expected = super.createImportHashmap(from, toImport1Expected, linenumberImport1Expected);
		HashMap<String, Object> dependencyInvocConstructorExpected = createDependencyHashmap(
				fromInvocConstructorExpected, toInvocConstructorExpected, typeInvocConstructorExpected, linenumberInvocConstructorExpected);
		HashMap<String, Object> dependencyInvocMethodExpected = createDependencyHashmap(
				fromInvocMethodExpected, toInvocMethodExpected, typeInvocMethodExpected, linenumberInvocMethodExpected);

		boolean foundImport1Dependency = compaireDTOWithValues(dependencyImport1Expected, dependencies);
		boolean foundInvocConstructorDependency = compaireDTOWithValues(dependencyInvocConstructorExpected, dependencies);
		boolean foundInvocMethodDependency = compaireDTOWithValues(dependencyInvocMethodExpected, dependencies);
		
		assertEquals(true, foundImport1Dependency);
		assertEquals(true, foundInvocConstructorDependency);
		assertEquals(true, foundInvocMethodDependency);
	}
	
	@Test
	public void testDomainKilroySettings(){
		String from = "domain.foursquarealternative.kilroy.Settings";
		int expectedDependencies = 4;
		
		//DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = super.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String toImport1Expected = "domain.foursquarealternative.whrrl.ISee";
		int linenumberImport1Expected = 3;
		
		String toImport2Expected = "domain.foursquarealternative.whrrl.WhrrlSettings";
		int linenumberImport2Expected = 4;
		
		String fromImplementsExpected = from;
		String toImplementsExpected = "domain.foursquarealternative.whrrl.ISee";
		String typeImplementsExpected = super.IMPLEMENTS;
		int linenumberImplementsExpected = 8;
		
		String fromInvocConstructorExpected = from;
		String toInvocConstructorExpected = "domain.foursquarealternative.whrrl.WhrrlSettings";
		String typeInvocConstructorExpected = super.INVOCCONSTRUCTOR;
		int linenumberInvocConstructorExpected = 10;

		
		HashMap<String, Object> dependencyImport1Expected = super.createImportHashmap(from, toImport1Expected, linenumberImport1Expected);
		HashMap<String, Object> dependencyImport2Expected = super.createImportHashmap(from, toImport2Expected, linenumberImport2Expected);
		HashMap<String, Object> dependencyInvocConstructorExpected = createDependencyHashmap(
				fromInvocConstructorExpected, toInvocConstructorExpected, typeInvocConstructorExpected, linenumberInvocConstructorExpected);
		HashMap<String, Object> dependencyImplementsExpected = createDependencyHashmap(
				fromImplementsExpected, toImplementsExpected, typeImplementsExpected, linenumberImplementsExpected);

		boolean foundImport1Dependency = compaireDTOWithValues(dependencyImport1Expected, dependencies);
		boolean foundImport2Dependency = compaireDTOWithValues(dependencyImport2Expected, dependencies);
		boolean foundInvocConstructorDependency = compaireDTOWithValues(dependencyInvocConstructorExpected, dependencies);
		boolean foundImplementsDependency = compaireDTOWithValues(dependencyImplementsExpected, dependencies);
		
		assertEquals(true, foundImport1Dependency);
		assertEquals(true, foundImport2Dependency);
		assertEquals(true, foundInvocConstructorDependency);
		assertEquals(true, foundImplementsDependency);
	}
	
}
