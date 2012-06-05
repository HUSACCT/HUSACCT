package husaccttest.analyse.java.benchmark.domain;

import java.util.HashMap;

import org.junit.Test;

import static org.junit.Assert.*;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husaccttest.analyse.java.benchmark.BenchmarkExtended;



public class Gowalla extends BenchmarkExtended{

	@Test
	public void testNavigation(){
		String from = "domain.gowalla";
		
		int expectedchildmodules = 14;
		AnalysedModuleDTO[] childModules = service.getChildModulesInModule(from);
		assertEquals(expectedchildmodules, childModules.length);
		
		HashMap<String, Object> actionExpectedModule = createModuleHashmap(
				"Action", 
				from +  ".Action",
				0,
				super.CLASS);
		
		HashMap<String, Object> campaignExpectedModule = createModuleHashmap(
				"Campaign", 
				from +  ".Campaign",
				0,
				super.CLASS);
		
		HashMap<String, Object> checkinExpectedModule = createModuleHashmap(
				"CheckIn", 
				from +  ".CheckIn",
				0,
				super.CLASS);
		
		HashMap<String, Object> eventExpectedModule = createModuleHashmap(
				"Event", 
				from +  ".Event",
				0,
				super.CLASS);
		
		HashMap<String, Object> gowallaannotationExpectedModule = createModuleHashmap(
				"GoWallaAnnotation", 
				from +  ".GoWallaAnnotation",
				0,
				super.INTERFACE);
		
		HashMap<String, Object> gowallaexceptionExpectedModule = createModuleHashmap(
				"GoWallaException", 
				from +  ".GoWallaException",
				0,
				super.CLASS);
		
		HashMap<String, Object> guideExpectedModule = createModuleHashmap(
				"Guide", 
				from +  ".Guide",
				0,
				super.CLASS);
		
		HashMap<String, Object> ispotsExpectedModule = createModuleHashmap(
				"ISpots", 
				from +  ".ISpots",
				0,
				super.INTERFACE);
		
		HashMap<String, Object> settingsExpectedModule = createModuleHashmap(
				"Settings", 
				from +  ".Settings",
				0,
				super.CLASS);
		
		HashMap<String, Object> specialExpectedModule = createModuleHashmap(
				"Special", 
				from +  ".Special",
				0,
				super.CLASS);
		
		HashMap<String, Object> tipExpectedModule = createModuleHashmap(
				"Tip", 
				from +  ".Tip",
				0,
				super.CLASS);
		
		HashMap<String, Object> tripsExpectedModule = createModuleHashmap(
				"Trips", 
				from +  ".Trips",
				0,
				super.CLASS);
		
		HashMap<String, Object> userExpectedModule = createModuleHashmap(
				"User", 
				from +  ".User",
				0,
				super.CLASS);
		
		HashMap<String, Object> venueExpectedModule = createModuleHashmap(
				"Venue", 
				from +  ".Venue",
				0,
				super.CLASS);
		
		boolean foundaction = compaireDTOWithValues(actionExpectedModule, childModules);
		boolean foundcampaign = compaireDTOWithValues(campaignExpectedModule, childModules);
		boolean foundcheckin = compaireDTOWithValues(checkinExpectedModule, childModules);
		boolean foundevent = compaireDTOWithValues(eventExpectedModule, childModules);
		boolean foundgowallaannotation = compaireDTOWithValues(gowallaannotationExpectedModule, childModules);
		boolean foundgowallaexception = compaireDTOWithValues(gowallaexceptionExpectedModule, childModules);
		boolean foundguide = compaireDTOWithValues(guideExpectedModule, childModules);
		boolean foundispots = compaireDTOWithValues(ispotsExpectedModule, childModules);
		boolean foundsettings = compaireDTOWithValues(settingsExpectedModule, childModules);
		boolean foundspecial = compaireDTOWithValues(specialExpectedModule, childModules);
		boolean foundtip = compaireDTOWithValues(tipExpectedModule, childModules);
		boolean foundtrips = compaireDTOWithValues(tripsExpectedModule, childModules);
		boolean founduser = compaireDTOWithValues(userExpectedModule, childModules);
		boolean foundvenue = compaireDTOWithValues(venueExpectedModule, childModules);
		
		
		assertEquals(true, foundaction);
		assertEquals(true, foundcampaign);
		assertEquals(true, foundcheckin);
		assertEquals(true, foundevent);
		assertEquals(true, foundgowallaannotation);
		assertEquals(true, foundgowallaexception);
		assertEquals(true, foundguide);
		assertEquals(true, foundispots);
		assertEquals(true, foundsettings);
		assertEquals(true, foundspecial);
		assertEquals(true, foundtip);
		assertEquals(true, foundtrips);
		assertEquals(true, founduser);
		assertEquals(true, foundvenue);
	}

	@Test
	public void testDomainGowallaAction(){
		String from = "domain.gowalla.Action";
		int expectedDependencies = 0;
		
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = super.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
	}
	
	@Test
	public void testDomainGowallaCampaign(){
		String from = "domain.gowalla.Campaign";
		int expectedDependencies = 0;
		
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = super.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
	}
	
	@Test
	public void testDomainGowallaCheckIn(){
		String from = "domain.gowalla.CheckIn";
		int expectedDependencies = 0;
		
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = super.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
	}
	
	@Test
	public void testDomainGowallaEvent(){
		String from = "domain.gowalla.Event";
		int expectedDependencies = 0;
		
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = super.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String fromDeclarationExpected = from;
		String toDeclarationExpected = "String";
		String typeDeclarationExpected = super.DECLARATION;
		int linenumberDeclarationExpected = 4;
		
		HashMap<String, Object> dependencyDeclarationExpected = createDependencyHashmap(
				fromDeclarationExpected, toDeclarationExpected, typeDeclarationExpected, linenumberDeclarationExpected);
		
		boolean foundDeclarationDependency = compaireDTOWithValues(dependencyDeclarationExpected, dependencies);
		
		//assertEquals(true, foundDeclarationDependency);
	}
	
	@Test
	public void testDomainGowallaGoWallaAnnotation(){
		String from = "domain.gowalla.GoWallaAnnotation";
		int expectedDependencies = 0;
		
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = super.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
	}
	
	@Test
	public void testDomainGowallaException(){
		String from = "domain.gowalla.GoWallaException";
		int expectedDependencies = 0;
		
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = super.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String fromDeclarationExpected = from;
		String toDeclarationExpected = "String";
		String typeDeclarationExpected = super.DECLARATION;
		int linenumberDeclarationExpected = 4;
		
		HashMap<String, Object> dependencyDeclarationExpected = createDependencyHashmap(
				fromDeclarationExpected, toDeclarationExpected, typeDeclarationExpected, linenumberDeclarationExpected);
		
		boolean foundDeclarationDependency = compaireDTOWithValues(dependencyDeclarationExpected, dependencies);
		
		//assertEquals(true, foundDeclarationDependency);
	}
	
	@Test
	public void testDomainGowallaGuide(){
		String from = "domain.gowalla.Guide";
		int expectedDependencies = 0;
		
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = super.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String fromDeclarationExpected = from;
		String toDeclarationExpected = "String";
		String typeDeclarationExpected = super.DECLARATION;
		int linenumberDeclarationExpected = 4;
		
		HashMap<String, Object> dependencyDeclarationExpected = createDependencyHashmap(
				fromDeclarationExpected, toDeclarationExpected, typeDeclarationExpected, linenumberDeclarationExpected);
		
		boolean foundDeclarationDependency = compaireDTOWithValues(dependencyDeclarationExpected, dependencies);
		
		//assertEquals(true, foundDeclarationDependency);
	}
	
	@Test
	public void testDomainGowallaISpots(){
		String from = "domain.gowalla.ISpots";
		int expectedDependencies = 0;
		
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = super.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
	}
	
	@Test
	public void testDomainGowallaSettings(){
		String from = "domain.gowalla.Settings";
		int expectedDependencies = 0;
		
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = super.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
	}
	
	@Test
	public void testDomainGowallaSpecial(){
		String from = "domain.gowalla.Special";
		int expectedDependencies = 0;
		
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = super.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
	}
	
	@Test
	public void testDomainGowallaTip(){
		String from = "domain.gowalla.Tip";
		int expectedDependencies = 0;
		
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = super.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
	}
	
	@Test
	public void testDomainGowallaTrips(){
		String from = "domain.gowalla.Trips";
		int expectedDependencies = 0;
		
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = super.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
	}
	
	@Test
	public void testDomainGowallaUser(){
		String from = "domain.gowalla.User";
		int expectedDependencies = 0;
		
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = super.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
	}
	
	@Test
	public void testDomainGowallaVenue(){
		String from = "domain.gowalla.Venue";
		int expectedDependencies = 0;
		
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = super.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
	}
	
	
	
	
	
}
