package husaccttest.analyse.java.benchmark.domain;

import java.util.HashMap;

import org.junit.Test;

import static org.junit.Assert.*;
import husacct.common.dto.AnalysedModuleDTO;
import husaccttest.analyse.java.benchmark.BenchmarkExtended;



public class FoursquarealternativeNavigation extends BenchmarkExtended{

	@Test
	public void testNavigation(){
		String from = "domain.foursquarealternative";
		
		int expectedchildmodules = 6;
		AnalysedModuleDTO[] childModules = service.getChildModulesInModule(from);
		assertEquals(expectedchildmodules, childModules.length);
		
		HashMap<String, Object> brightkiteExpectedModule = createModuleHashmap(
				"brightkite", 
				from +  ".brightkite",
				0,
				super.PACKAGE);
		
		HashMap<String, Object> glympseExpectedModule = createModuleHashmap(
				"glympse", 
				from +  ".glympse",
				0,
				super.PACKAGE);
		
		HashMap<String, Object> kilroyExpectedModule = createModuleHashmap(
				"kilroy", 
				from +  ".kilroy",
				0,
				super.PACKAGE);
		
		HashMap<String, Object> whrrlExpectedModule = createModuleHashmap(
				"whrrl", 
				from +  ".whrrl",
				0,
				super.PACKAGE);
		
		HashMap<String, Object> yelpExpectedModule = createModuleHashmap(
				"yelp", 
				from +  ".yelp",
				0,
				super.PACKAGE);
		
		HashMap<String, Object> SameExtendExpectedModule = createModuleHashmap(
				"SameExtend", 
				from +  ".SameExtend",
				0,
				super.CLASS);
		
		boolean foundbrightkite = compaireDTOWithValues(brightkiteExpectedModule, childModules);
		boolean foundglympse = compaireDTOWithValues(glympseExpectedModule, childModules);
		boolean foundkilroy = compaireDTOWithValues(kilroyExpectedModule, childModules);
		boolean foundwhrrl = compaireDTOWithValues(whrrlExpectedModule, childModules);
		boolean foundyelp = compaireDTOWithValues(yelpExpectedModule, childModules);
		boolean foundSameExtend = compaireDTOWithValues(SameExtendExpectedModule, childModules);
		
		assertEquals(true, foundbrightkite);
		assertEquals(true, foundglympse);
		assertEquals(true, foundkilroy);
		assertEquals(true, foundwhrrl);
		assertEquals(true, foundyelp);
		assertEquals(true, foundSameExtend);
	}
	
	@Test
	public void testBrightkitePackage(){
		String from = "domain.foursquarealternative.brightkite";
		
		int expectedchildmodules = 24;
		AnalysedModuleDTO[] childModules = service.getChildModulesInModule(from);
		assertEquals(expectedchildmodules, childModules.length);
	}
	
	@Test
	public void testGlympsePackage(){
		String from = "domain.foursquarealternative.glympse";
		
		int expectedchildmodules = 2;
		AnalysedModuleDTO[] childModules = service.getChildModulesInModule(from);
		assertEquals(expectedchildmodules, childModules.length);
	}
	
	@Test
	public void testKilroyPackage(){
		String from = "domain.foursquarealternative.kilroy";
		
		int expectedchildmodules = 5;
		AnalysedModuleDTO[] childModules = service.getChildModulesInModule(from);
		assertEquals(expectedchildmodules, childModules.length);
	}
	
	@Test
	public void testWhrrlPackage(){
		String from = "domain.foursquarealternative.whrrl";
		
		int expectedchildmodules = 12;
		AnalysedModuleDTO[] childModules = service.getChildModulesInModule(from);
		assertEquals(expectedchildmodules, childModules.length);
	}
	
	@Test
	public void testYelpPackage(){
		String from = "domain.foursquarealternative.yelp";
		
		int expectedchildmodules = 11;
		AnalysedModuleDTO[] childModules = service.getChildModulesInModule(from);
		assertEquals(expectedchildmodules, childModules.length);
	}
	
	
	
}
