package husaccttest.analyse.benchmarkjava.domain;

import java.util.HashMap;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
import husacct.common.dto.DependencyDTO;
import husaccttest.analyse.benchmarkjava.BenchmarkExtended;



public class FoursquarealternativeWhrrl extends BenchmarkExtended{

	@Test
	public void testDomainWhrrlBackgroundService(){
		String from = "domain.foursquarealternative.whrrl.BackgroundService";
		int expectedDependencies = 3;
		
		DependencyDTO[] dependencies = super.getOnlyDirectDependencies(service.getDependenciesFrom(from));
//		super.printDependencies(dependencies);
		assertEquals(expectedDependencies, dependencies.length);
		
		String toImport1Expected = "domain.foursquarealternative.yelp.ServiceOne";
		int linenumberImport1Expected = 3;
		
		String fromDeclaration1Expected = from;
		String toDeclaration1Expected = "domain.foursquarealternative.yelp.ServiceOne";
		String typeDeclaration1Expected = super.DECLARATION;
		int linenumberDeclaration1Expected = 8;
		
		String fromDeclaration2Expected = from;
		String toDeclaration2Expected = "domain.foursquarealternative.yelp.ServiceOne";
		String typeDeclaration2Expected = super.DECLARATION;
		int linenumberDeclaration2Expected = 9;
		
		HashMap<String, Object> dependencyImport1Expected = super.createImportHashmap(from, toImport1Expected, linenumberImport1Expected);
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
	
	@Test
	public void testDomainWhrrlCheckCastTo(){
		String from = "domain.foursquarealternative.whrrl.CheckCastTo";
		int expectedDependencies = 2;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
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
	public void testDomainWhrrlFrontService(){
		String from = "domain.foursquarealternative.whrrl.FrontService";
		int expectedDependencies = 4;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String toImport1Expected = "domain.foursquarealternative.yelp.IYelp";
		int linenumberImport1Expected = 3;
		
		String fromDeclarationExpected = from;
		String toDeclarationExpected = "domain.foursquarealternative.yelp.IYelp";
		String typeDeclarationExpected = super.DECLARATION;
		int linenumberDeclarationExpected = 8;
		
		HashMap<String, Object> dependencyImport1Expected = super.createImportHashmap(from, toImport1Expected, linenumberImport1Expected);
		HashMap<String, Object> dependencyDeclarationExpected = createDependencyHashmap(
				fromDeclarationExpected, toDeclarationExpected, typeDeclarationExpected, linenumberDeclarationExpected);
		
		boolean foundImport1Dependency = compaireDTOWithValues(dependencyImport1Expected, dependencies);
		boolean foundDeclarationDependency = compaireDTOWithValues(dependencyDeclarationExpected, dependencies);
		
		assertEquals(true, foundImport1Dependency);
		assertEquals(true, foundDeclarationDependency);
		
		HashMap<String, Object> indirectImportExpected = createDependencyHashmap(
				from,
				"domain.foursquarealternative.brightkite.IMap",
				super.IMPORT,
				3,
				true);
		
		HashMap<String, Object> indirectImplementsExpected = createDependencyHashmap(
				from,
				"domain.foursquarealternative.brightkite.IMap",
				super.EXTENDSINTERFACE,
				7,
				true);
		
		boolean foundindirectImport = compaireDTOWithValues(indirectImportExpected, dependencies);
		boolean foundindirectImplements = compaireDTOWithValues(indirectImplementsExpected, dependencies);
		assertEquals(true, foundindirectImport);
		assertEquals(true, foundindirectImplements);
		
	}	
	
	@Test
	public void testDomainWhrrlIWhrrl(){
		String from = "domain.foursquarealternative.whrrl.IWhrrl";
		int expectedDependencies = 2;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String toImport1Expected = "domain.foursquarealternative.yelp.IPreferences";
		int linenumberImport1Expected = 3;
		
		String fromExtendsExpected = from;
		String toExtendsExpected = "domain.foursquarealternative.yelp.IPreferences";
		String typeExtendsExpected = super.EXTENDSINTERFACE;
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
	public void testDomainWhrrlMapsService(){
		String from = "domain.foursquarealternative.whrrl.MapsService";
		int expectedDependencies = 2;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String toImport1Expected = "domain.foursquarealternative.yelp.POI";
		int linenumberImport1Expected = 2;
		
		String fromExtendsExpected = from;
		String toExtendsExpected = "domain.foursquarealternative.yelp.POI";
		String typeExtendsExpected = super.EXTENDSABSTRACT;
		int linenumberExtendsExpected = 6;
		
		HashMap<String, Object> dependencyImport1Expected = super.createImportHashmap(from, toImport1Expected, linenumberImport1Expected);
		HashMap<String, Object> dependencyExtendsExpected = createDependencyHashmap(
				fromExtendsExpected, toExtendsExpected, typeExtendsExpected, linenumberExtendsExpected);
		
		boolean foundImport1Dependency = compaireDTOWithValues(dependencyImport1Expected, dependencies);
		boolean foundExtendsDependency = compaireDTOWithValues(dependencyExtendsExpected, dependencies);
		
		assertEquals(true, foundImport1Dependency);
		assertEquals(true, foundExtendsDependency);
	}	
	
	@Ignore ("Cant analyse because class element is not defined")
	@Test
	public void testDomainWhrrlProfile(){
		String from = "domain.foursquarealternative.whrrl.Profile";
		int expectedDependencies = 1;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String toImport1Expected = "domain.foursquarealternative.yelp.IPreferences";
		int linenumberImport1Expected = 2;
		
		HashMap<String, Object> dependencyImport1Expected = super.createImportHashmap(from, toImport1Expected, linenumberImport1Expected);
		
		boolean foundImport1Dependency = compaireDTOWithValues(dependencyImport1Expected, dependencies);
		
		assertEquals(true, foundImport1Dependency);
	}
	
	@Test
	public void testDomainWhrrlTips(){
		String from = "domain.foursquarealternative.whrrl.Tips";
		int expectedDependencies = 2;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String toImport1Expected = "domain.foursquarealternative.yelp.POI";
		int linenumberImport1Expected = 3;
		
		String fromExtendsExpected = from;
		String toExtendsExpected = "domain.foursquarealternative.yelp.POI";
		String typeExtendsExpected = super.EXTENDSABSTRACT;
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
	public void testDomainWhrrlWhrrl(){
		String from = "domain.foursquarealternative.whrrl.Whrrl";
		int expectedDependencies = 2;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String toImport1Expected = "domain.foursquarealternative.yelp.IPreferences";
		int linenumberImport1Expected = 3;
		
		String fromImplementsExpected = from;
		String toImplementsExpected = "domain.foursquarealternative.yelp.IPreferences";
		String typeImplementsExpected = super.IMPLEMENTS;
		int linenumberImplementsExpected = 8;
		
		HashMap<String, Object> dependencyImport1Expected = super.createImportHashmap(from, toImport1Expected, linenumberImport1Expected);
		HashMap<String, Object> dependencyImplementsExpected = createDependencyHashmap(
				fromImplementsExpected, toImplementsExpected, typeImplementsExpected, linenumberImplementsExpected);
		
		boolean foundImport1Dependency = compaireDTOWithValues(dependencyImport1Expected, dependencies);
		boolean foundImplementsDependency = compaireDTOWithValues(dependencyImplementsExpected, dependencies);
		
		assertEquals(true, foundImport1Dependency);
		assertEquals(true, foundImplementsDependency);
	}
	
	@Test
	public void testDomainWhrrlWhrrlComment(){
		String from = "domain.foursquarealternative.whrrl.WhrrlComment";
		int expectedDependencies = 2;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String toImport1Expected = "domain.foursquarealternative.yelp.YelpComment";
		int linenumberImport1Expected = 3;
		
		String fromExtendsExpected = from;
		String toExtendsExpected = "domain.foursquarealternative.yelp.YelpComment";
		String typeExtendsExpected = super.EXTENDSABSTRACT;
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
	public void testDomainWhrrlWhrrlFuture(){
		String from = "domain.foursquarealternative.whrrl.WhrrlFuture";
		int expectedDependencies = 3;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String toImport1Expected = "domain.foursquarealternative.yelp.MyFuture";
		int linenumberImport1Expected = 3;
		
		HashMap<String, Object> dependencyImport1Expected = super.createImportHashmap(from, toImport1Expected, linenumberImport1Expected);
		boolean foundImport1Dependency = compaireDTOWithValues(dependencyImport1Expected, dependencies);
		assertEquals(true, foundImport1Dependency);
		
		
		HashMap<String, Object> indirectImportExpected = createDependencyHashmap(
				from,
				"domain.foursquarealternative.brightkite.IFuture",
				super.IMPORT,
				3,
				true);
		
		HashMap<String, Object> indirectImplementsExpected = createDependencyHashmap(
				from,
				"domain.foursquarealternative.brightkite.IFuture",
				super.IMPLEMENTS,
				7,
				true);
		
		boolean foundindirectImport = compaireDTOWithValues(indirectImportExpected, dependencies);
		boolean foundindirectImplements = compaireDTOWithValues(indirectImplementsExpected, dependencies);
		assertEquals(true, foundindirectImport);
		assertEquals(true, foundindirectImplements);
		
		
	}
	
	@Test
	public void testDomainWhrrlWhrrlHistory(){
		String from = "domain.foursquarealternative.whrrl.WhrrlHistory";
		int expectedDependencies = 1;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String toImport1Expected = "domain.foursquarealternative.yelp.MyHistory";
		int linenumberImport1Expected = 3;
		
		HashMap<String, Object> dependencyImport1Expected = super.createImportHashmap(from, toImport1Expected, linenumberImport1Expected);
		boolean foundImport1Dependency = compaireDTOWithValues(dependencyImport1Expected, dependencies);
		assertEquals(true, foundImport1Dependency);
	}
	
	@Test
	public void testDomainWhrrlWhrrlSettings(){
		String from = "domain.foursquarealternative.whrrl.WhrrlSettings";
		int expectedDependencies = 2;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String toImport1Expected = "domain.foursquarealternative.yelp.Yelp";
		int linenumberImport1Expected = 3;
		
		String fromExtendsExpected = from;
		String toExtendsExpected = "domain.foursquarealternative.yelp.Yelp";
		String typeExtendsExpected = super.EXTENDSABSTRACT;
		int linenumberExtendsExpected = 7;
		
		HashMap<String, Object> dependencyImport1Expected = super.createImportHashmap(from, toImport1Expected, linenumberImport1Expected);
		HashMap<String, Object> dependencyExtendsExpected = createDependencyHashmap(
				fromExtendsExpected, toExtendsExpected, typeExtendsExpected, linenumberExtendsExpected);
		
		boolean foundImport1Dependency = compaireDTOWithValues(dependencyImport1Expected, dependencies);
		boolean foundExtendsDependency = compaireDTOWithValues(dependencyExtendsExpected, dependencies);
		
		assertEquals(true, foundImport1Dependency);
		assertEquals(true, foundExtendsDependency);
	}
	
	
	
}
