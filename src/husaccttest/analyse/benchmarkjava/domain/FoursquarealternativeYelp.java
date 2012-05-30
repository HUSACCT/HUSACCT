package husaccttest.analyse.benchmarkjava.domain;

import java.util.HashMap;

import org.junit.Test;

import static org.junit.Assert.*;
import husacct.common.dto.DependencyDTO;
import husaccttest.analyse.benchmarkjava.BenchmarkExtended;



public class FoursquarealternativeYelp extends BenchmarkExtended{

	@Test
	public void testDomainYelpCheckCastTo(){
		String from = "domain.foursquarealternative.yelp.CheckCastTo";
		int expectedDependencies = 2;
		
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = super.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String toImport1Expected = "domain.foursquarealternative.SameExtend";
		int linenumberImport1Expected = 3;
		
		String fromExtendsExpected = from;
		String toExtendsExpected = "domain.foursquarealternative.SameExtend";
		String typeExtendsExpected = super.EXTENDSCONCRETE;
		int linenumberExtendsExpected = 8;
		
		HashMap<String, Object> dependencyImport1Expected = super.createImportHashmap(from, toImport1Expected, linenumberImport1Expected);
		HashMap<String, Object> dependencyExtendsExpected = createDependencyHashmap(
				fromExtendsExpected, toExtendsExpected, typeExtendsExpected, linenumberExtendsExpected);
		boolean foundImport1Dependency = compaireDTOWithValues(dependencyImport1Expected, dependencies);
		boolean foundExtendsDependency = compaireDTOWithValues(dependencyExtendsExpected, dependencies);
		assertEquals(true, foundImport1Dependency);
		assertEquals(true, foundExtendsDependency);
	}
	
	@Test
	public void testDomainYelpIYelp(){
		String from = "domain.foursquarealternative.yelp.IYelp";
		int expectedDependencies = 2;
		
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = super.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String toImport1Expected = "domain.foursquarealternative.brightkite.IMap";
		int linenumberImport1Expected = 3;
		
		String fromExtendsExpected = from;
		String toExtendsExpected = "domain.foursquarealternative.brightkite.IMap";
		String typeExtendsExpected = super.EXTENDSINTERFACE;
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
	public void testDomainYelpMyFuture(){
		String from = "domain.foursquarealternative.yelp.MyFuture";
		int expectedDependencies = 2;
		
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = super.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String toImport1Expected = "domain.foursquarealternative.brightkite.IFuture";
		int linenumberImport1Expected = 3;
		
		String fromImplementsExpected = from;
		String toImplementsExpected = "domain.foursquarealternative.brightkite.IFuture";
		String typeImplementsExpected = super.IMPLEMENTS;
		int linenumberImplementsExpected = 7;
		
		HashMap<String, Object> dependencyImport1Expected = super.createImportHashmap(from, toImport1Expected, linenumberImport1Expected);
		HashMap<String, Object> dependencyImplementsExpected = createDependencyHashmap(
				fromImplementsExpected, toImplementsExpected, typeImplementsExpected, linenumberImplementsExpected);
		boolean foundImport1Dependency = compaireDTOWithValues(dependencyImport1Expected, dependencies);
		boolean foundImplementsDependency = compaireDTOWithValues(dependencyImplementsExpected, dependencies);
		assertEquals(true, foundImport1Dependency);
		assertEquals(true, foundImplementsDependency);
	}
	
	@Test
	public void testDomainYelpServiceOne(){
		String from = "domain.foursquarealternative.yelp.ServiceOne";
		int expectedDependencies = 8;
		
		//DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = super.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String toImport1Expected = "java.util.Calendar";
		int linenumberImport1Expected = 3;
		
		String fromDeclaration1Expected = from;
		String toDeclaration1Expected = "String";
		String typeDeclaration1Expected = super.DECLARATION;
		int linenumberDeclaration1Expected = 10;
		
		String fromDeclaration2Expected = from;
		String toDeclaration2Expected = "String";
		String typeDeclaration2Expected = super.DECLARATION;
		int linenumberDeclaration2Expected = 11;
		
		String fromDeclaration3Expected = from;
		String toDeclaration3Expected = "java.util.Calendar";
		String typeDeclaration3Expected = super.DECLARATION;
		int linenumberDeclaration3Expected = 12;
		
		String fromDeclaration4Expected = from;
		String toDeclaration4Expected = "domain.foursquarealternative.yelp.ServiceTwo";
		String typeDeclaration4Expected = super.DECLARATION;
		int linenumberDeclaration4Expected = 13;
		
		String fromInvocMethodExpected = from;
		String toInvocMethodExpected = "java.util.Calendar";
		String typeInvocMethodExpected = super.INVOCMETHOD;
		int linenumberInvocMethodExpected = 17;
		
		HashMap<String, Object> dependencyImport1Expected = super.createImportHashmap(from, toImport1Expected, linenumberImport1Expected);
		HashMap<String, Object> dependencyDeclaration1Expected = createDependencyHashmap(
				fromDeclaration1Expected, toDeclaration1Expected, typeDeclaration1Expected, linenumberDeclaration1Expected);
		HashMap<String, Object> dependencyDeclaration2Expected = createDependencyHashmap(
				fromDeclaration2Expected, toDeclaration2Expected, typeDeclaration2Expected, linenumberDeclaration2Expected);
		HashMap<String, Object> dependencyDeclaration3Expected = createDependencyHashmap(
				fromDeclaration3Expected, toDeclaration3Expected, typeDeclaration3Expected, linenumberDeclaration3Expected);
		HashMap<String, Object> dependencyDeclaration4Expected = createDependencyHashmap(
				fromDeclaration4Expected, toDeclaration4Expected, typeDeclaration4Expected, linenumberDeclaration4Expected);
		HashMap<String, Object> dependencyInvocMethodExpected = createDependencyHashmap(
				fromInvocMethodExpected, toInvocMethodExpected, typeInvocMethodExpected, linenumberInvocMethodExpected);
		
		
		boolean foundImport1Dependency = compaireDTOWithValues(dependencyImport1Expected, dependencies);
		boolean foundDeclaration1Dependency = compaireDTOWithValues(dependencyDeclaration1Expected, dependencies);
		boolean foundDeclaration2Dependency = compaireDTOWithValues(dependencyDeclaration2Expected, dependencies);
		boolean foundDeclaration3Dependency = compaireDTOWithValues(dependencyDeclaration3Expected, dependencies);
		boolean foundDeclaration4Dependency = compaireDTOWithValues(dependencyDeclaration4Expected, dependencies);
		boolean foundInvocMethodDependency = compaireDTOWithValues(dependencyInvocMethodExpected, dependencies);
		
		assertEquals(true, foundImport1Dependency);
		assertEquals(true, foundDeclaration1Dependency);
		assertEquals(true, foundDeclaration2Dependency);
		assertEquals(true, foundDeclaration3Dependency);
		assertEquals(true, foundDeclaration4Dependency);
		assertEquals(true, foundInvocMethodDependency);
	}
	
	@Test
	public void testDomainYelpServiceTwo(){
		String from = "domain.foursquarealternative.yelp.ServiceTwo";
		int expectedDependencies = 7;
		
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = super.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String toImport1Expected = "java.util.Calendar";
		int linenumberImport1Expected = 3;
		
		String fromDeclaration1Expected = from;
		String toDeclaration1Expected = "String";
		String typeDeclaration1Expected = super.DECLARATION;
		int linenumberDeclaration1Expected = 10;
		
		String fromDeclaration2Expected = from;
		String toDeclaration2Expected = "String";
		String typeDeclaration2Expected = super.DECLARATION;
		int linenumberDeclaration2Expected = 11;
		
		String fromDeclaration3Expected = from;
		String toDeclaration3Expected = "java.util.Calendar";
		String typeDeclaration3Expected = super.DECLARATION;
		int linenumberDeclaration3Expected = 12;
		
		String fromInvocMethodExpected = from;
		String toInvocMethodExpected = "java.util.Calendar";
		String typeInvocMethodExpected = super.INVOCMETHOD;
		int linenumberInvocMethodExpected = 16;
		
		HashMap<String, Object> dependencyImport1Expected = super.createImportHashmap(from, toImport1Expected, linenumberImport1Expected);
		HashMap<String, Object> dependencyDeclaration1Expected = createDependencyHashmap(
				fromDeclaration1Expected, toDeclaration1Expected, typeDeclaration1Expected, linenumberDeclaration1Expected);
		HashMap<String, Object> dependencyDeclaration2Expected = createDependencyHashmap(
				fromDeclaration2Expected, toDeclaration2Expected, typeDeclaration2Expected, linenumberDeclaration2Expected);
		HashMap<String, Object> dependencyDeclaration3Expected = createDependencyHashmap(
				fromDeclaration3Expected, toDeclaration3Expected, typeDeclaration3Expected, linenumberDeclaration3Expected);
		HashMap<String, Object> dependencyInvocMethodExpected = createDependencyHashmap(
				fromInvocMethodExpected, toInvocMethodExpected, typeInvocMethodExpected, linenumberInvocMethodExpected);
		
		
		boolean foundImport1Dependency = compaireDTOWithValues(dependencyImport1Expected, dependencies);
		boolean foundDeclaration1Dependency = compaireDTOWithValues(dependencyDeclaration1Expected, dependencies);
		boolean foundDeclaration2Dependency = compaireDTOWithValues(dependencyDeclaration2Expected, dependencies);
		boolean foundDeclaration3Dependency = compaireDTOWithValues(dependencyDeclaration3Expected, dependencies);
		boolean foundInvocMethodDependency = compaireDTOWithValues(dependencyInvocMethodExpected, dependencies);
		
		assertEquals(true, foundImport1Dependency);
		assertEquals(true, foundDeclaration1Dependency);
		assertEquals(true, foundDeclaration2Dependency);
		assertEquals(true, foundDeclaration3Dependency);
		assertEquals(true, foundInvocMethodDependency);
	}
	
	
	
	
}
