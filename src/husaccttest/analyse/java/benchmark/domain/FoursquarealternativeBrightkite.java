package husaccttest.analyse.java.benchmark.domain;

import java.util.HashMap;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
import husacct.common.dto.DependencyDTO;
import husaccttest.analyse.java.benchmark.BenchmarkExtended;



public class FoursquarealternativeBrightkite extends BenchmarkExtended{

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
		
		
		HashMap<String, Object> indirectImportExpected = createDependencyHashmap(
				from,
				"domain.foursquarealternative.yelp.IPreferences",
				super.IMPORT,
				3,
				true);
		
		HashMap<String, Object> indirectImplementsExpected = createDependencyHashmap(
				from,
				"domain.foursquarealternative.yelp.IPreferences",
				super.IMPLEMENTS,
				8,
				true);
		
//		boolean foundindirectImport = compaireDTOWithValues(indirectImportExpected, dependencies);
//		boolean foundindirectImplements = compaireDTOWithValues(indirectImplementsExpected, dependencies);
//		assertEquals(true, foundindirectImport);
//		assertEquals(true, foundindirectImplements);
		
	}
	
	@Test
	public void testDomainBrightKiteAccount2(){
		String from = "domain.foursquarealternative.brightkite.Account2";
		int expectedDependencies = 2;
		
		//DependencyDTO[] dependencies = service.getDependenciesFrom(from);
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
		
		
		HashMap<String, Object> indirectImportExpected = createDependencyHashmap(
				from,
				"domain.foursquarealternative.yelp.IPreferences",
				super.IMPORT,
				3,
				true);
		
		HashMap<String, Object> indirectImplementsExpected = createDependencyHashmap(
				from,
				"domain.foursquarealternative.yelp.IPreferences",
				super.EXTENDSINTERFACE,
				8,
				true);
		
		boolean foundindirectImport = compaireDTOWithValues(indirectImportExpected, dependencies);
		boolean foundindirectImplements = compaireDTOWithValues(indirectImplementsExpected, dependencies);
//		assertEquals(true, foundindirectImport);
//		assertEquals(true, foundindirectImplements);
		
	}
	
	@Test
	public void testDomainBrightKiteCheckCastFrom(){
		String from = "domain.foursquarealternative.brightkite.CheckCastFrom";
		int expectedDependencies = 6;
		
		//DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String fromImport1Expected = from;
		String toImport1Expected = "domain.foursquarealternative.SameExtend";
		String typeImport1Expected = super.IMPORT;
		int linenumberImport1Expected = 3;
		
		String fromImport2Expected = from;
		String toImport2Expected = "domain.foursquarealternative.yelp.CheckCastTo";
		String typeImport2Expected = super.IMPORT;
		int linenumberImport2Expected = 4;
		
		String fromExtendsExpected = from;
		String toExtendsExpected = "domain.foursquarealternative.SameExtend";
		String typeExtendsExpected = super.EXTENDSCONCRETE;
		int linenumberExtendsExpected = 8;
		
		String fromDeclarationExpected = from;
		String toDeclarationExpected = "Object";
		String typeDeclarationExpected = super.DECLARATION;
		int linenumberDeclarationExpected = 12;
		
		String fromInvocConstructorExpected = from;
		String toInvocConstructorExpected = "domain.foursquarealternative.SameExtend";
		String typeInvocConstructorExpected = super.INVOCCONSTRUCTOR;
		int linenumberInvocConstructorExpected = 12;
		
		String fromDeclaration2Expected = from;
		String toDeclaration2Expected = "domain.foursquarealternative.yelp.CheckCastTo";
		String typeDeclaration2Expected = super.DECLARATION;
		int linenumberDeclaration2Expected = 12;
		
		
		
		HashMap<String, Object> dependencyImport1Expected = createDependencyHashmap(
				fromImport1Expected, toImport1Expected, typeImport1Expected, linenumberImport1Expected);
		HashMap<String, Object> dependencyImport2Expected = createDependencyHashmap(
				fromImport2Expected, toImport2Expected, typeImport2Expected, linenumberImport2Expected);
		HashMap<String, Object> dependencyExtendsExpected = createDependencyHashmap(
				fromExtendsExpected, toExtendsExpected, typeExtendsExpected, linenumberExtendsExpected);
		HashMap<String, Object> dependencyDeclarationExpected = createDependencyHashmap(
				fromDeclarationExpected, toDeclarationExpected, typeDeclarationExpected, linenumberDeclarationExpected);
		HashMap<String, Object> dependencyInvocConstructorExpected = createDependencyHashmap(
				fromInvocConstructorExpected, toInvocConstructorExpected, typeInvocConstructorExpected, linenumberInvocConstructorExpected);
		HashMap<String, Object> dependencyDeclaration2Expected = createDependencyHashmap(
				fromDeclaration2Expected, toDeclaration2Expected, typeDeclaration2Expected, linenumberDeclaration2Expected);

		boolean foundImport1Dependency = compaireDTOWithValues(dependencyImport1Expected, dependencies);
		boolean foundImport2Dependency = compaireDTOWithValues(dependencyImport2Expected, dependencies);
		boolean foundExtendsDependency = compaireDTOWithValues(dependencyExtendsExpected, dependencies);
		boolean foundDeclarationDependency = compaireDTOWithValues(dependencyDeclarationExpected, dependencies);
		boolean foundInvocConstructorDependency = compaireDTOWithValues(dependencyInvocConstructorExpected, dependencies);
		boolean foundDeclaration2Dependency = compaireDTOWithValues(dependencyDeclaration2Expected, dependencies);
		
		assertEquals(true, foundImport1Dependency);
		assertEquals(true, foundImport2Dependency);
		assertEquals(true, foundExtendsDependency);
		assertEquals(true, foundDeclarationDependency);
		assertEquals(true, foundInvocConstructorDependency);
		assertEquals(true, foundDeclaration2Dependency);
		
		
		HashMap<String, Object> indirectImportExpected = createDependencyHashmap(
				from,
				"domain.foursquarealternative.SameExtend",
				super.IMPORT,
				3,
				true);
		
		HashMap<String, Object> indirectImplementsExpected = createDependencyHashmap(
				from,
				"domain.foursquarealternative.SameExtend",
				super.EXTENDSCONCRETE,
				8,
				true);
		
		boolean foundindirectImport = compaireDTOWithValues(indirectImportExpected, dependencies);
		boolean foundindirectImplements = compaireDTOWithValues(indirectImplementsExpected, dependencies);
//		assertEquals(true, foundindirectImport);
//		assertEquals(true, foundindirectImplements);
		
		
	}
	
	@Ignore( "Problem with detecting expected Dependency. (Indirect Invocation by AccessProperty)" )
	@Test
	public void testDomainBrightKiteCheckInIndirectAccess1(){
		String from = "domain.foursquarealternative.brightkite.CheckInIndirectAccess1";
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
		String from = "domain.foursquarealternative.brightkite.CheckOut";
		int expectedDependencies = 2;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String fromImportExpected = from;
		String toImportExpected = "domain.foursquarealternative.whrrl.MapsService";
		String typeImportExpected = super.IMPORT;
		int linenumberImportExpected = 3;
		
		String fromExtendsExpected = from;
		String toExtendsExpected = "domain.foursquarealternative.whrrl.MapsService";
		String typeExtendsExpected = super.EXTENDSCONCRETE;
		int linenumberExtendsExpected = 7;
		
		HashMap<String, Object> dependencyImportExpected = createDependencyHashmap(
				fromImportExpected, toImportExpected, typeImportExpected, linenumberImportExpected);
		HashMap<String, Object> dependencyExtendsExpected = createDependencyHashmap(
				fromExtendsExpected, toExtendsExpected, typeExtendsExpected, linenumberExtendsExpected);
		
		boolean foundImportDependency = compaireDTOWithValues(dependencyImportExpected, dependencies);
		boolean foundExtendsDependency = compaireDTOWithValues(dependencyExtendsExpected, dependencies);
		assertEquals(true, foundImportDependency);
		assertEquals(true, foundExtendsDependency);
		
		HashMap<String, Object> indirectImportExpected = createDependencyHashmap(
				from,
				"domain.foursquarealternative.yelp.POI",
				super.IMPORT,
				2,
				true);
		
		HashMap<String, Object> indirectImplementsExpected = createDependencyHashmap(
				from,
				"domain.foursquarealternative.yelp.POI",
				super.EXTENDSABSTRACT,
				6,
				true);
		
		boolean foundindirectImport = compaireDTOWithValues(indirectImportExpected, dependencies);
		boolean foundindirectImplements = compaireDTOWithValues(indirectImplementsExpected, dependencies);
//		assertEquals(true, foundindirectImport);
//		assertEquals(true, foundindirectImplements);
		
		
	}
	
	@Test
	public void testDomainBrightkiteHistory(){
		String from = "domain.foursquarealternative.brightkite.History";
		int expectedDependencies = 0;
		
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
	}
}
