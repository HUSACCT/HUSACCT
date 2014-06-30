package husaccttest.analyse.java.benchmark.domain;

import java.util.HashMap;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
import husacct.common.dto.DependencyDTO;
import husaccttest.analyse.java.TestObject;
import husaccttest.analyse.java.benchmark.BenchmarkExtended;



public class FoursquarealternativeBrightkite extends BenchmarkExtended{

	@Test
	public void testDomainBrightkiteAccount1(){
		TestObject testobject = new TestObject("domain.foursquarealternative.brightkite.Account1");
		testobject.addDependency(new DependencyDTO("", "", "domain.foursquarealternative.whrrl.Whrrl", "domain.foursquarealternative.whrrl.Whrrl", super.IMPORT, 3));
		testobject.addDependency(new DependencyDTO("", "", "domain.foursquarealternative.whrrl.Whrrl", "domain.foursquarealternative.whrrl.Whrrl", super.EXTENDSCONCRETE, 10));
		boolean result = super.testDependencyObject(testobject);
		assertTrue(testobject.getLastError(), result);
	}
	
	@Test
	public void testDomainBrightKiteAccount2(){
		TestObject testobject = new TestObject("domain.foursquarealternative.brightkite.Account2");
		testobject.addDependency(new DependencyDTO("", "", "domain.foursquarealternative.whrrl.Whrrl", "domain.foursquarealternative.whrrl.IWhrrl", super.IMPORT, 4));
		testobject.addDependency(new DependencyDTO("", "", "domain.foursquarealternative.whrrl.Whrrl", "domain.foursquarealternative.whrrl.IWhrrl", super.IMPLEMENTS, 11));
		boolean result = super.testDependencyObject(testobject);
		assertTrue(testobject.getLastError(), result);
	}
	
	@Test
	public void testDomainBrightKiteCheckCastFrom(){
		TestObject testobject = new TestObject("domain.foursquarealternative.brightkite.CheckCastFrom");
		testobject.addDependency(new DependencyDTO("", "", "domain.foursquarealternative.SameExtend", "domain.foursquarealternative.SameExtend", super.IMPORT, 3));
		testobject.addDependency(new DependencyDTO("", "", "domain.foursquarealternative.yelp.CheckCastTo", "domain.foursquarealternative.yelp.CheckCastTo", super.IMPORT, 4));
		testobject.addDependency(new DependencyDTO("", "", "domain.foursquarealternative.SameExtend", "domain.foursquarealternative.SameExtend", super.EXTENDSCONCRETE, 8));
		testobject.addDependency(new DependencyDTO("", "", "domain.foursquarealternative.SameExtend", "domain.foursquarealternative.SameExtend", super.INVOCCONSTRUCTOR, 12));
		boolean result = super.testDependencyObject(testobject);
		assertTrue(testobject.getLastError(), result);
	}
	
	@Ignore( "Problem with detecting expected Dependency. (Indirect Invocation by AccessProperty)" )
	@Test
	public void testDomainBrightKiteCheckInIndirectAccess1(){
		
		TestObject testobject = new TestObject("domain.foursquarealternative.brightkite.CheckInIndirectAccess1");
		testobject.addDependency(new DependencyDTO("", "", "domain.foursquarealternative.whrrl.BackgroundService", "domain.foursquarealternative.whrrl.BackgroundService", super.IMPORT, 3));
		testobject.addDependency(new DependencyDTO("", "", "domain.foursquarealternative.whrrl.BackgroundService", "domain.foursquarealternative.whrrl.BackgroundService", super.DECLARATION, 10));
		testobject.addDependency(new DependencyDTO("", "", "domain.foursquarealternative.whrrl.BackgroundService", "domain.foursquarealternative.whrrl.BackgroundService", super.INVOCCONSTRUCTOR, 14));
		boolean result = super.testDependencyObject(testobject, true);
		assertTrue(testobject.getLastError(), result);
		
	}
	
	
	@Ignore( "Problem with detecting expected Dependency. (Indirect Invocation by AccessProperty)" )
	@Test
	public void testDomainBrightKiteCheckInIndirectAccess2(){
		String from = "domain.foursquarealternative.brightkite.CheckInIndirectAccess2";
		int expectedDependencies = 5;
		
		//DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String fromImport1Expected = from;
		String toImport1Expected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeImport1Expected = super.IMPORT;
		int linenumberImport1Expected = 3;
		
		String fromDeclaration1Expected = from;
		String toDeclaration1Expected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeDeclaration1Expected = super.DECLARATION;
		int linenumberDeclaration1Expected = 10;
		
		String fromDeclaration2Expected = from;
		String toDeclaration2Expected = "String";
		String typeDeclaration2Expected = super.DECLARATION;
		int linenumberDeclaration2Expected = 11;
		
		String fromInvocConstructorExpected = from;
		String toInvocConstructorExpected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeInvocConstructorExpected = super.INVOCCONSTRUCTOR;
		int linenumberInvocConstructorExpected = 14;
		
		// INDIRECT VIA ACCESSPROPERTY!!!
		String fromInvocAttributeExpected = from;
		String toInvocAttributeExpected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeInvocAttributeExpected = null;
		int linenumberInvocAttributeExpected = 18;
		
		HashMap<String, Object> dependencyImport1Expected = createDependencyHashmap(
				fromImport1Expected, toImport1Expected, typeImport1Expected, linenumberImport1Expected);
		HashMap<String, Object> dependencyDeclaration1Expected = createDependencyHashmap(
				fromDeclaration1Expected, toDeclaration1Expected, typeDeclaration1Expected, linenumberDeclaration1Expected);
		HashMap<String, Object> dependencyDeclaration2Expected = createDependencyHashmap(
				fromDeclaration2Expected, toDeclaration2Expected, typeDeclaration2Expected, linenumberDeclaration2Expected);
		HashMap<String, Object> dependencyInvocConstructorExpected = createDependencyHashmap(
				fromInvocConstructorExpected, toInvocConstructorExpected, typeInvocConstructorExpected, linenumberInvocConstructorExpected);
		HashMap<String, Object> dependencyInvocAttributeExpected = createDependencyHashmap(
				fromInvocAttributeExpected, toInvocAttributeExpected, typeInvocAttributeExpected, linenumberInvocAttributeExpected);

		boolean foundImport1Dependency = compaireDTOWithValues(dependencyImport1Expected, dependencies);
		boolean foundDeclaration1Dependency = compaireDTOWithValues(dependencyDeclaration1Expected, dependencies);
		boolean foundDeclaration2Dependency = compaireDTOWithValues(dependencyDeclaration2Expected, dependencies);
		boolean foundInvocConstructorDependency = compaireDTOWithValues(dependencyInvocConstructorExpected, dependencies);
		boolean foundInvocAttributeDependency = compaireDTOWithValues(dependencyInvocAttributeExpected, dependencies);
		
		assertEquals(true, foundImport1Dependency);
		assertEquals(true, foundDeclaration1Dependency);
		assertEquals(true, foundDeclaration2Dependency);
		assertEquals(true, foundInvocConstructorDependency);
		assertEquals(true, foundInvocAttributeDependency);
	}
	
	@Ignore( "Problem with detecting expected Dependency. (Indirect Invocation by AccessProperty)" )
	@Test
	public void testDomainBrightKiteCheckInIndirectAccess3(){
		String from = "domain.foursquarealternative.brightkite.CheckInIndirectAccess3";
		int expectedDependencies = 5;
		
		
		//DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String fromImport1Expected = from;
		String toImport1Expected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeImport1Expected = super.IMPORT;
		int linenumberImport1Expected = 3;
		
		String fromDeclaration1Expected = from;
		String toDeclaration1Expected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeDeclaration1Expected = super.DECLARATION;
		int linenumberDeclaration1Expected = 10;
		
		String fromDeclaration2Expected = from;
		String toDeclaration2Expected = "String";
		String typeDeclaration2Expected = super.DECLARATION;
		int linenumberDeclaration2Expected = 11;
		
		String fromInvocConstructorExpected = from;
		String toInvocConstructorExpected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeInvocConstructorExpected = super.INVOCCONSTRUCTOR;
		int linenumberInvocConstructorExpected = 14;
		
		// INDIRECT VIA ACCESSPROPERTY!!!
		String fromInvocAttributeExpected = from;
		String toInvocAttributeExpected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeInvocAttributeExpected = null;
		int linenumberInvocAttributeExpected = 18;
		
		HashMap<String, Object> dependencyImport1Expected = createDependencyHashmap(
				fromImport1Expected, toImport1Expected, typeImport1Expected, linenumberImport1Expected);
		HashMap<String, Object> dependencyDeclaration1Expected = createDependencyHashmap(
				fromDeclaration1Expected, toDeclaration1Expected, typeDeclaration1Expected, linenumberDeclaration1Expected);
		HashMap<String, Object> dependencyDeclaration2Expected = createDependencyHashmap(
				fromDeclaration2Expected, toDeclaration2Expected, typeDeclaration2Expected, linenumberDeclaration2Expected);
		HashMap<String, Object> dependencyInvocConstructorExpected = createDependencyHashmap(
				fromInvocConstructorExpected, toInvocConstructorExpected, typeInvocConstructorExpected, linenumberInvocConstructorExpected);
		HashMap<String, Object> dependencyInvocAttributeExpected = createDependencyHashmap(
				fromInvocAttributeExpected, toInvocAttributeExpected, typeInvocAttributeExpected, linenumberInvocAttributeExpected);

		boolean foundImport1Dependency = compaireDTOWithValues(dependencyImport1Expected, dependencies);
		boolean foundDeclaration1Dependency = compaireDTOWithValues(dependencyDeclaration1Expected, dependencies);
		boolean foundDeclaration2Dependency = compaireDTOWithValues(dependencyDeclaration2Expected, dependencies);
		boolean foundInvocConstructorDependency = compaireDTOWithValues(dependencyInvocConstructorExpected, dependencies);
		boolean foundInvocAttributeDependency = compaireDTOWithValues(dependencyInvocAttributeExpected, dependencies);
		
		assertEquals(true, foundImport1Dependency);
		assertEquals(true, foundDeclaration1Dependency);
		assertEquals(true, foundDeclaration2Dependency);
		assertEquals(true, foundInvocConstructorDependency);
		assertEquals(true, foundInvocAttributeDependency);
	}
	
	@Ignore( "Problem with detecting expected Dependency. (Indirect Invocation by AccessProperty)" )
	@Test
	public void testDomainBrightKiteCheckInIndirectAccess4(){
		String from = "domain.foursquarealternative.brightkite.CheckInIndirectAccess4";
		int expectedDependencies = 5;
		
		//DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String fromImport1Expected = from;
		String toImport1Expected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeImport1Expected = super.IMPORT;
		int linenumberImport1Expected = 3;
		
		String fromDeclaration1Expected = from;
		String toDeclaration1Expected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeDeclaration1Expected = super.DECLARATION;
		int linenumberDeclaration1Expected = 10;
		
		String fromDeclaration2Expected = from;
		String toDeclaration2Expected = "String";
		String typeDeclaration2Expected = super.DECLARATION;
		int linenumberDeclaration2Expected = 11;
		
		String fromInvocConstructorExpected = from;
		String toInvocConstructorExpected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeInvocConstructorExpected = super.INVOCCONSTRUCTOR;
		int linenumberInvocConstructorExpected = 14;
		
		// INDIRECT VIA ACCESSPROPERTY!!!
		String fromInvocAttributeExpected = from;
		String toInvocAttributeExpected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeInvocAttributeExpected = null;
		int linenumberInvocAttributeExpected = 18;
		
		HashMap<String, Object> dependencyImport1Expected = createDependencyHashmap(
				fromImport1Expected, toImport1Expected, typeImport1Expected, linenumberImport1Expected);
		HashMap<String, Object> dependencyDeclaration1Expected = createDependencyHashmap(
				fromDeclaration1Expected, toDeclaration1Expected, typeDeclaration1Expected, linenumberDeclaration1Expected);
		HashMap<String, Object> dependencyDeclaration2Expected = createDependencyHashmap(
				fromDeclaration2Expected, toDeclaration2Expected, typeDeclaration2Expected, linenumberDeclaration2Expected);
		HashMap<String, Object> dependencyInvocConstructorExpected = createDependencyHashmap(
				fromInvocConstructorExpected, toInvocConstructorExpected, typeInvocConstructorExpected, linenumberInvocConstructorExpected);
		HashMap<String, Object> dependencyInvocAttributeExpected = createDependencyHashmap(
				fromInvocAttributeExpected, toInvocAttributeExpected, typeInvocAttributeExpected, linenumberInvocAttributeExpected);

		boolean foundImport1Dependency = compaireDTOWithValues(dependencyImport1Expected, dependencies);
		boolean foundDeclaration1Dependency = compaireDTOWithValues(dependencyDeclaration1Expected, dependencies);
		boolean foundDeclaration2Dependency = compaireDTOWithValues(dependencyDeclaration2Expected, dependencies);
		boolean foundInvocConstructorDependency = compaireDTOWithValues(dependencyInvocConstructorExpected, dependencies);
		boolean foundInvocAttributeDependency = compaireDTOWithValues(dependencyInvocAttributeExpected, dependencies);
		
		assertEquals(true, foundImport1Dependency);
		assertEquals(true, foundDeclaration1Dependency);
		assertEquals(true, foundDeclaration2Dependency);
		assertEquals(true, foundInvocConstructorDependency);
		assertEquals(true, foundInvocAttributeDependency);
	}
	
	@Ignore( "Problem with detecting expected Dependency. (Indirect Invocation by AccessProperty)" )
	@Test
	public void testDomainBrightKiteCheckInIndirectAccess5(){
		String from = "domain.foursquarealternative.brightkite.CheckInIndirectAccess5";
		int expectedDependencies = 5;
		
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String fromImport1Expected = from;
		String toImport1Expected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeImport1Expected = super.IMPORT;
		int linenumberImport1Expected = 3;
		
		String fromDeclaration1Expected = from;
		String toDeclaration1Expected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeDeclaration1Expected = super.DECLARATION;
		int linenumberDeclaration1Expected = 10;
		
		String fromDeclaration2Expected = from;
		String toDeclaration2Expected = "String";
		String typeDeclaration2Expected = super.DECLARATION;
		int linenumberDeclaration2Expected = 11;
		
		String fromInvocConstructorExpected = from;
		String toInvocConstructorExpected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeInvocConstructorExpected = super.INVOCCONSTRUCTOR;
		int linenumberInvocConstructorExpected = 14;
		
		// INDIRECT VIA ACCESSPROPERTY!!!
		String fromInvocAttributeExpected = from;
		String toInvocAttributeExpected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeInvocAttributeExpected = null;
		int linenumberInvocAttributeExpected = 18;
		
		HashMap<String, Object> dependencyImport1Expected = createDependencyHashmap(
				fromImport1Expected, toImport1Expected, typeImport1Expected, linenumberImport1Expected);
		HashMap<String, Object> dependencyDeclaration1Expected = createDependencyHashmap(
				fromDeclaration1Expected, toDeclaration1Expected, typeDeclaration1Expected, linenumberDeclaration1Expected);
		HashMap<String, Object> dependencyDeclaration2Expected = createDependencyHashmap(
				fromDeclaration2Expected, toDeclaration2Expected, typeDeclaration2Expected, linenumberDeclaration2Expected);
		HashMap<String, Object> dependencyInvocConstructorExpected = createDependencyHashmap(
				fromInvocConstructorExpected, toInvocConstructorExpected, typeInvocConstructorExpected, linenumberInvocConstructorExpected);
		HashMap<String, Object> dependencyInvocAttributeExpected = createDependencyHashmap(
				fromInvocAttributeExpected, toInvocAttributeExpected, typeInvocAttributeExpected, linenumberInvocAttributeExpected);

		boolean foundImport1Dependency = compaireDTOWithValues(dependencyImport1Expected, dependencies);
		boolean foundDeclaration1Dependency = compaireDTOWithValues(dependencyDeclaration1Expected, dependencies);
		boolean foundDeclaration2Dependency = compaireDTOWithValues(dependencyDeclaration2Expected, dependencies);
		boolean foundInvocConstructorDependency = compaireDTOWithValues(dependencyInvocConstructorExpected, dependencies);
		boolean foundInvocAttributeDependency = compaireDTOWithValues(dependencyInvocAttributeExpected, dependencies);
		
		assertEquals(true, foundImport1Dependency);
		assertEquals(true, foundDeclaration1Dependency);
		assertEquals(true, foundDeclaration2Dependency);
		assertEquals(true, foundInvocConstructorDependency);
		assertEquals(true, foundInvocAttributeDependency);
	}
	
	@Ignore( "Problem with detecting expected Dependency. (Indirect Invocation by AccessProperty)" )
	@Test
	public void testDomainBrightKiteCheckInIndirectAccess6(){
		String from = "domain.foursquarealternative.brightkite.CheckInIndirectAccess6";
		int expectedDependencies = 5;
		
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String fromImport1Expected = from;
		String toImport1Expected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeImport1Expected = super.IMPORT;
		int linenumberImport1Expected = 3;
		
		String fromDeclaration1Expected = from;
		String toDeclaration1Expected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeDeclaration1Expected = super.DECLARATION;
		int linenumberDeclaration1Expected = 10;
		
		String fromDeclaration2Expected = from;
		String toDeclaration2Expected = "String";
		String typeDeclaration2Expected = super.DECLARATION;
		int linenumberDeclaration2Expected = 11;
		
		String fromInvocConstructorExpected = from;
		String toInvocConstructorExpected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeInvocConstructorExpected = super.INVOCCONSTRUCTOR;
		int linenumberInvocConstructorExpected = 14;
		
		// INDIRECT VIA ACCESSPROPERTY!!!
		String fromInvocAttributeExpected = from;
		String toInvocAttributeExpected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeInvocAttributeExpected = null;
		int linenumberInvocAttributeExpected = 18;
		
		HashMap<String, Object> dependencyImport1Expected = createDependencyHashmap(
				fromImport1Expected, toImport1Expected, typeImport1Expected, linenumberImport1Expected);
		HashMap<String, Object> dependencyDeclaration1Expected = createDependencyHashmap(
				fromDeclaration1Expected, toDeclaration1Expected, typeDeclaration1Expected, linenumberDeclaration1Expected);
		HashMap<String, Object> dependencyDeclaration2Expected = createDependencyHashmap(
				fromDeclaration2Expected, toDeclaration2Expected, typeDeclaration2Expected, linenumberDeclaration2Expected);
		HashMap<String, Object> dependencyInvocConstructorExpected = createDependencyHashmap(
				fromInvocConstructorExpected, toInvocConstructorExpected, typeInvocConstructorExpected, linenumberInvocConstructorExpected);
		HashMap<String, Object> dependencyInvocAttributeExpected = createDependencyHashmap(
				fromInvocAttributeExpected, toInvocAttributeExpected, typeInvocAttributeExpected, linenumberInvocAttributeExpected);

		boolean foundImport1Dependency = compaireDTOWithValues(dependencyImport1Expected, dependencies);
		boolean foundDeclaration1Dependency = compaireDTOWithValues(dependencyDeclaration1Expected, dependencies);
		boolean foundDeclaration2Dependency = compaireDTOWithValues(dependencyDeclaration2Expected, dependencies);
		boolean foundInvocConstructorDependency = compaireDTOWithValues(dependencyInvocConstructorExpected, dependencies);
		boolean foundInvocAttributeDependency = compaireDTOWithValues(dependencyInvocAttributeExpected, dependencies);
		
		assertEquals(true, foundImport1Dependency);
		assertEquals(true, foundDeclaration1Dependency);
		assertEquals(true, foundDeclaration2Dependency);
		assertEquals(true, foundInvocConstructorDependency);
		assertEquals(true, foundInvocAttributeDependency);
	}
	
	@Ignore( "Problem with detecting expected Dependency. (Indirect Invocation by AccessProperty)" )
	@Test
	public void testDomainBrightKiteCheckInIndirectAccess7(){
		String from = "domain.foursquarealternative.brightkite.CheckInIndirectAccess7";
		int expectedDependencies = 3;
		
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String fromImport1Expected = from;
		String toImport1Expected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeImport1Expected = super.IMPORT;
		int linenumberImport1Expected = 3;
		
		String fromDeclaration2Expected = from;
		String toDeclaration2Expected = "String";
		String typeDeclaration2Expected = super.DECLARATION;
		int linenumberDeclaration2Expected = 11;
		
		// INDIRECT VIA ACCESSPROPERTY!!!
		String fromInvocAttributeExpected = from;
		String toInvocAttributeExpected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeInvocAttributeExpected = null;
		int linenumberInvocAttributeExpected = 18;
		
		HashMap<String, Object> dependencyImport1Expected = createDependencyHashmap(
				fromImport1Expected, toImport1Expected, typeImport1Expected, linenumberImport1Expected);
		HashMap<String, Object> dependencyDeclaration2Expected = createDependencyHashmap(
				fromDeclaration2Expected, toDeclaration2Expected, typeDeclaration2Expected, linenumberDeclaration2Expected);
		HashMap<String, Object> dependencyInvocAttributeExpected = createDependencyHashmap(
				fromInvocAttributeExpected, toInvocAttributeExpected, typeInvocAttributeExpected, linenumberInvocAttributeExpected);

		boolean foundImport1Dependency = compaireDTOWithValues(dependencyImport1Expected, dependencies);
		boolean foundDeclaration2Dependency = compaireDTOWithValues(dependencyDeclaration2Expected, dependencies);
		boolean foundInvocAttributeDependency = compaireDTOWithValues(dependencyInvocAttributeExpected, dependencies);
		
		assertEquals(true, foundImport1Dependency);
		assertEquals(true, foundDeclaration2Dependency);
		assertEquals(true, foundInvocAttributeDependency);
	}
	
	@Ignore( "Problem with detecting expected Dependency. (Indirect Invocation by AccessProperty)" )
	@Test
	public void testDomainBrightKiteCheckInIndirectAccess8(){
		String from = "domain.foursquarealternative.brightkite.CheckInIndirectAccess8";
		int expectedDependencies = 5;
		
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String fromImport1Expected = from;
		String toImport1Expected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeImport1Expected = super.IMPORT;
		int linenumberImport1Expected = 3;
		
		String fromDeclaration1Expected = from;
		String toDeclaration1Expected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeDeclaration1Expected = super.DECLARATION;
		int linenumberDeclaration1Expected = 10;
		
		String fromDeclaration2Expected = from;
		String toDeclaration2Expected = "String";
		String typeDeclaration2Expected = super.DECLARATION;
		int linenumberDeclaration2Expected = 11;
		
		String fromInvocConstructorExpected = from;
		String toInvocConstructorExpected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeInvocConstructorExpected = super.INVOCCONSTRUCTOR;
		int linenumberInvocConstructorExpected = 14;
		
		// INDIRECT VIA ACCESSPROPERTY!!!
		String fromInvocAttributeExpected = from;
		String toInvocAttributeExpected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeInvocAttributeExpected = null;
		int linenumberInvocAttributeExpected = 18;
		
		HashMap<String, Object> dependencyImport1Expected = createDependencyHashmap(
				fromImport1Expected, toImport1Expected, typeImport1Expected, linenumberImport1Expected);
		HashMap<String, Object> dependencyDeclaration1Expected = createDependencyHashmap(
				fromDeclaration1Expected, toDeclaration1Expected, typeDeclaration1Expected, linenumberDeclaration1Expected);
		HashMap<String, Object> dependencyDeclaration2Expected = createDependencyHashmap(
				fromDeclaration2Expected, toDeclaration2Expected, typeDeclaration2Expected, linenumberDeclaration2Expected);
		HashMap<String, Object> dependencyInvocConstructorExpected = createDependencyHashmap(
				fromInvocConstructorExpected, toInvocConstructorExpected, typeInvocConstructorExpected, linenumberInvocConstructorExpected);
		HashMap<String, Object> dependencyInvocAttributeExpected = createDependencyHashmap(
				fromInvocAttributeExpected, toInvocAttributeExpected, typeInvocAttributeExpected, linenumberInvocAttributeExpected);

		boolean foundImport1Dependency = compaireDTOWithValues(dependencyImport1Expected, dependencies);
		boolean foundDeclaration1Dependency = compaireDTOWithValues(dependencyDeclaration1Expected, dependencies);
		boolean foundDeclaration2Dependency = compaireDTOWithValues(dependencyDeclaration2Expected, dependencies);
		boolean foundInvocConstructorDependency = compaireDTOWithValues(dependencyInvocConstructorExpected, dependencies);
		boolean foundInvocAttributeDependency = compaireDTOWithValues(dependencyInvocAttributeExpected, dependencies);
		
		assertEquals(true, foundImport1Dependency);
		assertEquals(true, foundDeclaration1Dependency);
		assertEquals(true, foundDeclaration2Dependency);
		assertEquals(true, foundInvocConstructorDependency);
		assertEquals(true, foundInvocAttributeDependency);
	}
	
	@Ignore( "Problem with detecting expected Dependency. (Indirect Invocation by InvocConstructor)" )
	@Test
	public void testDomainBrightKiteCheckInIndirectInvocation1(){
		String from = "domain.foursquarealternative.brightkite.CheckInIndirectInvocation1";
		int expectedDependencies = 3;
		
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String fromImport1Expected = from;
		String toImport1Expected = "domain.foursquarealternative.whrrl.WhrrlHistory";
		String typeImport1Expected = super.IMPORT;
		int linenumberImport1Expected = 4;
		
		String fromDeclaration1Expected = from;
		String toDeclaration1Expected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeDeclaration1Expected = super.INVOCCONSTRUCTOR;
		int linenumberDeclaration1Expected = 19;
		
		// Indirect via Constructor
		String fromDeclaration2Expected = from;
		String toDeclaration2Expected = "String";
		String typeDeclaration2Expected = super.DECLARATION;
		int linenumberDeclaration2Expected = 11;
		
		HashMap<String, Object> dependencyImport1Expected = createDependencyHashmap(
				fromImport1Expected, toImport1Expected, typeImport1Expected, linenumberImport1Expected);
		HashMap<String, Object> dependencyDeclaration1Expected = createDependencyHashmap(
				fromDeclaration1Expected, toDeclaration1Expected, typeDeclaration1Expected, linenumberDeclaration1Expected);
		HashMap<String, Object> dependencyDeclaration2Expected = createDependencyHashmap(
				fromDeclaration2Expected, toDeclaration2Expected, typeDeclaration2Expected, linenumberDeclaration2Expected);

		boolean foundImport1Dependency = compaireDTOWithValues(dependencyImport1Expected, dependencies);
		boolean foundDeclaration1Dependency = compaireDTOWithValues(dependencyDeclaration1Expected, dependencies);
		boolean foundDeclaration2Dependency = compaireDTOWithValues(dependencyDeclaration2Expected, dependencies);
		
		assertEquals(true, foundImport1Dependency);
		assertEquals(true, foundDeclaration1Dependency);
		assertEquals(true, foundDeclaration2Dependency);
	}
	
	@Ignore( "Problem with detecting expected Dependency. (Indirect Invocation by AccessProperty)" )
	@Test
	public void testDomainBrightKiteCheckInIndirectInvocation2(){
		String from = "domain.foursquarealternative.brightkite.CheckInIndirectInvocation2";
		int expectedDependencies = 5;
		
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String fromImport1Expected = from;
		String toImport1Expected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeImport1Expected = super.IMPORT;
		int linenumberImport1Expected = 3;
		
		String fromDeclaration1Expected = from;
		String toDeclaration1Expected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeDeclaration1Expected = super.DECLARATION;
		int linenumberDeclaration1Expected = 9;
		
		String fromDeclaration2Expected = from;
		String toDeclaration2Expected = "String";
		String typeDeclaration2Expected = super.DECLARATION;
		int linenumberDeclaration2Expected = 10;
		
		String fromInvocConstructorExpected = from;
		String toInvocConstructorExpected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeInvocConstructorExpected = super.INVOCCONSTRUCTOR;
		int linenumberInvocConstructorExpected = 13;
		
		// INDIRECT VIA ACCESSPROPERTY!!!
		String fromInvocAttributeExpected = from;
		String toInvocAttributeExpected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeInvocAttributeExpected = null;
		int linenumberInvocAttributeExpected = 18;
		
		HashMap<String, Object> dependencyImport1Expected = createDependencyHashmap(
				fromImport1Expected, toImport1Expected, typeImport1Expected, linenumberImport1Expected);
		HashMap<String, Object> dependencyDeclaration1Expected = createDependencyHashmap(
				fromDeclaration1Expected, toDeclaration1Expected, typeDeclaration1Expected, linenumberDeclaration1Expected);
		HashMap<String, Object> dependencyDeclaration2Expected = createDependencyHashmap(
				fromDeclaration2Expected, toDeclaration2Expected, typeDeclaration2Expected, linenumberDeclaration2Expected);
		HashMap<String, Object> dependencyInvocConstructorExpected = createDependencyHashmap(
				fromInvocConstructorExpected, toInvocConstructorExpected, typeInvocConstructorExpected, linenumberInvocConstructorExpected);
		HashMap<String, Object> dependencyInvocAttributeExpected = createDependencyHashmap(
				fromInvocAttributeExpected, toInvocAttributeExpected, typeInvocAttributeExpected, linenumberInvocAttributeExpected);

		boolean foundImport1Dependency = compaireDTOWithValues(dependencyImport1Expected, dependencies);
		boolean foundDeclaration1Dependency = compaireDTOWithValues(dependencyDeclaration1Expected, dependencies);
		boolean foundDeclaration2Dependency = compaireDTOWithValues(dependencyDeclaration2Expected, dependencies);
		boolean foundInvocConstructorDependency = compaireDTOWithValues(dependencyInvocConstructorExpected, dependencies);
		boolean foundInvocAttributeDependency = compaireDTOWithValues(dependencyInvocAttributeExpected, dependencies);
		
		assertEquals(true, foundImport1Dependency);
		assertEquals(true, foundDeclaration1Dependency);
		assertEquals(true, foundDeclaration2Dependency);
		assertEquals(true, foundInvocConstructorDependency);
		assertEquals(true, foundInvocAttributeDependency);
	}
	
	@Ignore( "Problem with detecting expected Dependency. (Indirect Invocation by AccessProperty)" )
	@Test
	public void testDomainBrightKiteCheckInIndirectInvocation3(){
		String from = "domain.foursquarealternative.brightkite.CheckInIndirectInvocation3";
		int expectedDependencies = 5;
		
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String fromImport1Expected = from;
		String toImport1Expected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeImport1Expected = super.IMPORT;
		int linenumberImport1Expected = 3;
		
		String fromDeclaration1Expected = from;
		String toDeclaration1Expected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeDeclaration1Expected = super.DECLARATION;
		int linenumberDeclaration1Expected = 9;
		
		String fromDeclaration2Expected = from;
		String toDeclaration2Expected = "String";
		String typeDeclaration2Expected = super.DECLARATION;
		int linenumberDeclaration2Expected = 10;
		
		String fromInvocConstructorExpected = from;
		String toInvocConstructorExpected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeInvocConstructorExpected = super.INVOCCONSTRUCTOR;
		int linenumberInvocConstructorExpected = 13;
		
		// INDIRECT VIA ACCESSPROPERTY!!!
		String fromInvocAttributeExpected = from;
		String toInvocAttributeExpected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeInvocAttributeExpected = null;
		int linenumberInvocAttributeExpected = 18;
		
		HashMap<String, Object> dependencyImport1Expected = createDependencyHashmap(
				fromImport1Expected, toImport1Expected, typeImport1Expected, linenumberImport1Expected);
		HashMap<String, Object> dependencyDeclaration1Expected = createDependencyHashmap(
				fromDeclaration1Expected, toDeclaration1Expected, typeDeclaration1Expected, linenumberDeclaration1Expected);
		HashMap<String, Object> dependencyDeclaration2Expected = createDependencyHashmap(
				fromDeclaration2Expected, toDeclaration2Expected, typeDeclaration2Expected, linenumberDeclaration2Expected);
		HashMap<String, Object> dependencyInvocConstructorExpected = createDependencyHashmap(
				fromInvocConstructorExpected, toInvocConstructorExpected, typeInvocConstructorExpected, linenumberInvocConstructorExpected);
		HashMap<String, Object> dependencyInvocAttributeExpected = createDependencyHashmap(
				fromInvocAttributeExpected, toInvocAttributeExpected, typeInvocAttributeExpected, linenumberInvocAttributeExpected);

		boolean foundImport1Dependency = compaireDTOWithValues(dependencyImport1Expected, dependencies);
		boolean foundDeclaration1Dependency = compaireDTOWithValues(dependencyDeclaration1Expected, dependencies);
		boolean foundDeclaration2Dependency = compaireDTOWithValues(dependencyDeclaration2Expected, dependencies);
		boolean foundInvocConstructorDependency = compaireDTOWithValues(dependencyInvocConstructorExpected, dependencies);
		boolean foundInvocAttributeDependency = compaireDTOWithValues(dependencyInvocAttributeExpected, dependencies);
		
		assertEquals(true, foundImport1Dependency);
		assertEquals(true, foundDeclaration1Dependency);
		assertEquals(true, foundDeclaration2Dependency);
		assertEquals(true, foundInvocConstructorDependency);
		assertEquals(true, foundInvocAttributeDependency);
	}
	
	@Ignore( "Problem with detecting expected Dependency. (Indirect Invocation by AccessProperty)" )
	@Test
	public void testDomainBrightKiteCheckInIndirectInvocation4(){
		String from = "domain.foursquarealternative.brightkite.CheckInIndirectInvocation4";
		int expectedDependencies = 5;
		
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String fromImport1Expected = from;
		String toImport1Expected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeImport1Expected = super.IMPORT;
		int linenumberImport1Expected = 3;
		
		String fromDeclaration1Expected = from;
		String toDeclaration1Expected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeDeclaration1Expected = super.DECLARATION;
		int linenumberDeclaration1Expected = 9;
		
		String fromDeclaration2Expected = from;
		String toDeclaration2Expected = "String";
		String typeDeclaration2Expected = super.DECLARATION;
		int linenumberDeclaration2Expected = 10;
		
		String fromInvocConstructorExpected = from;
		String toInvocConstructorExpected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeInvocConstructorExpected = super.INVOCCONSTRUCTOR;
		int linenumberInvocConstructorExpected = 13;
		
		// INDIRECT VIA ACCESSPROPERTY!!!
		String fromInvocAttributeExpected = from;
		String toInvocAttributeExpected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeInvocAttributeExpected = null;
		int linenumberInvocAttributeExpected = 18;
		
		HashMap<String, Object> dependencyImport1Expected = createDependencyHashmap(
				fromImport1Expected, toImport1Expected, typeImport1Expected, linenumberImport1Expected);
		HashMap<String, Object> dependencyDeclaration1Expected = createDependencyHashmap(
				fromDeclaration1Expected, toDeclaration1Expected, typeDeclaration1Expected, linenumberDeclaration1Expected);
		HashMap<String, Object> dependencyDeclaration2Expected = createDependencyHashmap(
				fromDeclaration2Expected, toDeclaration2Expected, typeDeclaration2Expected, linenumberDeclaration2Expected);
		HashMap<String, Object> dependencyInvocConstructorExpected = createDependencyHashmap(
				fromInvocConstructorExpected, toInvocConstructorExpected, typeInvocConstructorExpected, linenumberInvocConstructorExpected);
		HashMap<String, Object> dependencyInvocAttributeExpected = createDependencyHashmap(
				fromInvocAttributeExpected, toInvocAttributeExpected, typeInvocAttributeExpected, linenumberInvocAttributeExpected);

		boolean foundImport1Dependency = compaireDTOWithValues(dependencyImport1Expected, dependencies);
		boolean foundDeclaration1Dependency = compaireDTOWithValues(dependencyDeclaration1Expected, dependencies);
		boolean foundDeclaration2Dependency = compaireDTOWithValues(dependencyDeclaration2Expected, dependencies);
		boolean foundInvocConstructorDependency = compaireDTOWithValues(dependencyInvocConstructorExpected, dependencies);
		boolean foundInvocAttributeDependency = compaireDTOWithValues(dependencyInvocAttributeExpected, dependencies);
		
		assertEquals(true, foundImport1Dependency);
		assertEquals(true, foundDeclaration1Dependency);
		assertEquals(true, foundDeclaration2Dependency);
		assertEquals(true, foundInvocConstructorDependency);
		assertEquals(true, foundInvocAttributeDependency);
	}
	
	@Ignore( "Problem with detecting expected Dependency. (Indirect Invocation by AccessProperty)" )
	@Test
	public void testDomainBrightKiteCheckInIndirectInvocation5(){
		String from = "domain.foursquarealternative.brightkite.CheckInIndirectInvocation5";
		int expectedDependencies = 5;
		
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String fromImport1Expected = from;
		String toImport1Expected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeImport1Expected = super.IMPORT;
		int linenumberImport1Expected = 3;
		
		String fromDeclaration1Expected = from;
		String toDeclaration1Expected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeDeclaration1Expected = super.DECLARATION;
		int linenumberDeclaration1Expected = 9;
		
		String fromDeclaration2Expected = from;
		String toDeclaration2Expected = "String";
		String typeDeclaration2Expected = super.DECLARATION;
		int linenumberDeclaration2Expected = 10;
		
		String fromInvocConstructorExpected = from;
		String toInvocConstructorExpected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeInvocConstructorExpected = super.INVOCCONSTRUCTOR;
		int linenumberInvocConstructorExpected = 13;
		
		// INDIRECT VIA ACCESSPROPERTY!!!
		String fromInvocAttributeExpected = from;
		String toInvocAttributeExpected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeInvocAttributeExpected = null;
		int linenumberInvocAttributeExpected = 18;
		
		HashMap<String, Object> dependencyImport1Expected = createDependencyHashmap(
				fromImport1Expected, toImport1Expected, typeImport1Expected, linenumberImport1Expected);
		HashMap<String, Object> dependencyDeclaration1Expected = createDependencyHashmap(
				fromDeclaration1Expected, toDeclaration1Expected, typeDeclaration1Expected, linenumberDeclaration1Expected);
		HashMap<String, Object> dependencyDeclaration2Expected = createDependencyHashmap(
				fromDeclaration2Expected, toDeclaration2Expected, typeDeclaration2Expected, linenumberDeclaration2Expected);
		HashMap<String, Object> dependencyInvocConstructorExpected = createDependencyHashmap(
				fromInvocConstructorExpected, toInvocConstructorExpected, typeInvocConstructorExpected, linenumberInvocConstructorExpected);
		HashMap<String, Object> dependencyInvocAttributeExpected = createDependencyHashmap(
				fromInvocAttributeExpected, toInvocAttributeExpected, typeInvocAttributeExpected, linenumberInvocAttributeExpected);

		boolean foundImport1Dependency = compaireDTOWithValues(dependencyImport1Expected, dependencies);
		boolean foundDeclaration1Dependency = compaireDTOWithValues(dependencyDeclaration1Expected, dependencies);
		boolean foundDeclaration2Dependency = compaireDTOWithValues(dependencyDeclaration2Expected, dependencies);
		boolean foundInvocConstructorDependency = compaireDTOWithValues(dependencyInvocConstructorExpected, dependencies);
		boolean foundInvocAttributeDependency = compaireDTOWithValues(dependencyInvocAttributeExpected, dependencies);
		
		assertEquals(true, foundImport1Dependency);
		assertEquals(true, foundDeclaration1Dependency);
		assertEquals(true, foundDeclaration2Dependency);
		assertEquals(true, foundInvocConstructorDependency);
		assertEquals(true, foundInvocAttributeDependency);
	}
	
	@Ignore( "Problem with detecting expected Dependency. (Indirect Invocation by AccessProperty)" )
	@Test
	public void testDomainBrightKiteCheckInIndirectInvocation6(){
		String from = "domain.foursquarealternative.brightkite.CheckInIndirectInvocation6";
		int expectedDependencies = 5;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String fromImport1Expected = from;
		String toImport1Expected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeImport1Expected = super.IMPORT;
		int linenumberImport1Expected = 3;
		
		String fromDeclaration1Expected = from;
		String toDeclaration1Expected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeDeclaration1Expected = super.DECLARATION;
		int linenumberDeclaration1Expected = 9;
		
		String fromDeclaration2Expected = from;
		String toDeclaration2Expected = "String";
		String typeDeclaration2Expected = super.DECLARATION;
		int linenumberDeclaration2Expected = 10;
		
		String fromInvocConstructorExpected = from;
		String toInvocConstructorExpected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeInvocConstructorExpected = super.INVOCCONSTRUCTOR;
		int linenumberInvocConstructorExpected = 13;
		
		// INDIRECT VIA ACCESSPROPERTY!!!
		String fromInvocAttributeExpected = from;
		String toInvocAttributeExpected = "domain.foursquarealternative.whrrl.BackgroundService";
		String typeInvocAttributeExpected = null;
		int linenumberInvocAttributeExpected = 18;
		
		HashMap<String, Object> dependencyImport1Expected = createDependencyHashmap(
				fromImport1Expected, toImport1Expected, typeImport1Expected, linenumberImport1Expected);
		HashMap<String, Object> dependencyDeclaration1Expected = createDependencyHashmap(
				fromDeclaration1Expected, toDeclaration1Expected, typeDeclaration1Expected, linenumberDeclaration1Expected);
		HashMap<String, Object> dependencyDeclaration2Expected = createDependencyHashmap(
				fromDeclaration2Expected, toDeclaration2Expected, typeDeclaration2Expected, linenumberDeclaration2Expected);
		HashMap<String, Object> dependencyInvocConstructorExpected = createDependencyHashmap(
				fromInvocConstructorExpected, toInvocConstructorExpected, typeInvocConstructorExpected, linenumberInvocConstructorExpected);
		HashMap<String, Object> dependencyInvocAttributeExpected = createDependencyHashmap(
				fromInvocAttributeExpected, toInvocAttributeExpected, typeInvocAttributeExpected, linenumberInvocAttributeExpected);

		boolean foundImport1Dependency = compaireDTOWithValues(dependencyImport1Expected, dependencies);
		boolean foundDeclaration1Dependency = compaireDTOWithValues(dependencyDeclaration1Expected, dependencies);
		boolean foundDeclaration2Dependency = compaireDTOWithValues(dependencyDeclaration2Expected, dependencies);
		boolean foundInvocConstructorDependency = compaireDTOWithValues(dependencyInvocConstructorExpected, dependencies);
		boolean foundInvocAttributeDependency = compaireDTOWithValues(dependencyInvocAttributeExpected, dependencies);
		
		assertEquals(true, foundImport1Dependency);
		assertEquals(true, foundDeclaration1Dependency);
		assertEquals(true, foundDeclaration2Dependency);
		assertEquals(true, foundInvocConstructorDependency);
		assertEquals(true, foundInvocAttributeDependency);
	}
	
	@Test
	public void testDomainBrightkiteCheckOut(){
		TestObject testobject = new TestObject("domain.foursquarealternative.brightkite.CheckOut");
		testobject.addDependency(new DependencyDTO("", "", "domain.foursquarealternative.whrrl.MapsService", "domain.foursquarealternative.whrrl.MapsService", super.IMPORT, 3));
		testobject.addDependency(new DependencyDTO("", "", "domain.foursquarealternative.whrrl.MapsService", "domain.foursquarealternative.whrrl.MapsService", super.EXTENDSCONCRETE, 7));
		boolean result = super.testDependencyObject(testobject);
		assertTrue(testobject.getLastError(), result);
	}
	
	@Test
	public void testDomainBrightkiteHistory(){
		TestObject testobject = new TestObject("domain.foursquarealternative.brightkite.History");
		boolean result = super.testDependencyObject(testobject);
		assertTrue(testobject.getLastError(), result);
	}
}
