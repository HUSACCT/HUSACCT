package husaccttest.analyse.blackbox;

import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;

import husacct.common.dto.AnalysedModuleDTO;
import husaccttest.analyse.TestCaseExtended;

public class TestDomainModule extends TestCaseExtended{
	
	@Test
	public void testGetRootModules(){
		int totalModulesExpected = 3;
		
		AnalysedModuleDTO[] modules = service.getRootModules();
		assertEquals(totalModulesExpected, modules.length);
		
		String domainNameExpected = "domain";
		String domainUniqueNameExpected = "domain";
		int domainSubmoduleCount = 0;
		String domainTypeExpected = super.PACKAGE;
		
		String infrastructureNameExpected = "infrastructure";
		String infrastructureUniqueNameExpected = "infrastructure";
		int infrastructureSubmoduleCount = 0;
		String infrastructureTypeExpected = super.PACKAGE;
		
		String indirectNameExpected = "indirect";
		String indirectUniqueNameExpected = "indirect";
		int indirectSubmoduleCount = 0;
		String indirectTypeExpected = super.PACKAGE;
		
		HashMap<String, Object> domainExpectedModule = createModuleHashmap(
				domainNameExpected, domainUniqueNameExpected, domainSubmoduleCount, domainTypeExpected);
		HashMap<String, Object> infrastructureExpectedModule = createModuleHashmap(
				infrastructureNameExpected, infrastructureUniqueNameExpected, infrastructureSubmoduleCount, infrastructureTypeExpected);
		HashMap<String, Object> indirectExpectedModule = createModuleHashmap(
				indirectNameExpected, indirectUniqueNameExpected, indirectSubmoduleCount, indirectTypeExpected);
		
		boolean domainFoundModule = compaireDTOWithValues(domainExpectedModule, modules);
		boolean infrastructureFoundModule = compaireDTOWithValues(infrastructureExpectedModule, modules);
		boolean indirectFoundModule = compaireDTOWithValues(indirectExpectedModule, modules);
		assertEquals(true, domainFoundModule);
		assertEquals(true, infrastructureFoundModule);
		assertEquals(true, indirectFoundModule);
	}
	
	@Test
	public void testGetChildrenOfPackageModule(){
		int totalModulesExpected = 2;
		String modulesFrom = "domain.locationbased";
		
		AnalysedModuleDTO[] modules = service.getChildModulesInModule(modulesFrom);
		assertEquals(totalModulesExpected, modules.length);
		
		String foursquareNameExpected = "foursquare";
		String foursquareUniqueNameExpected = modulesFrom + ".foursquare";
		int foursquareSubmodulesExpected = 0;
		String foursquareTypeExpected = super.PACKAGE;
		
		String latitudeNameExpected = "latitude";
		String latitudeUniqueNameExpected = modulesFrom + ".latitude";
		int latitudeSubmodulesExpected = 0;
		String latitudeTypeExpected = super.PACKAGE;
		
		HashMap<String, Object> foursquareExpectedModule = createModuleHashmap(
				foursquareNameExpected, foursquareUniqueNameExpected, foursquareSubmodulesExpected, foursquareTypeExpected);
		HashMap<String, Object> latitudeExpectedModule = createModuleHashmap(
				latitudeNameExpected, latitudeUniqueNameExpected, latitudeSubmodulesExpected, latitudeTypeExpected);
		
		boolean foursquareFoundModule = compaireDTOWithValues(foursquareExpectedModule, modules);
		boolean latitudeFoundModule = compaireDTOWithValues(latitudeExpectedModule, modules);
		assertEquals(true, foursquareFoundModule);
		assertEquals(true, latitudeFoundModule);
	}
	
	@Test
	public void testGetChildrenOfClassModule(){
		int totalModulesExpected = 0;
		String modulesFrom = "domain.locationbased.foursquare.Account";
		
		AnalysedModuleDTO[] modules = service.getChildModulesInModule(modulesFrom);
		assertEquals(totalModulesExpected, modules.length);
	}
	
	@Test
	public void testGetChildrenOfNotExistingModule(){
		int totalModulesExpected = 0;
		String modulesFrom = "domain.notExisting";
		
		AnalysedModuleDTO[] modules = service.getChildModulesInModule(modulesFrom);
		assertEquals(totalModulesExpected, modules.length);
	}
	
	@Test
	public void testGetChilderenOfPackageWithDepthOne(){
		int totalModulesExpected = 2;
		String modulesFrom = "domain.locationbased";
		int depth = 1;
		
		AnalysedModuleDTO[] modules = service.getChildModulesInModule(modulesFrom, depth);
		assertEquals(totalModulesExpected, modules.length);
		
		String foursquareNameExpected = "foursquare";
		String foursquareUniqueNameExpected = modulesFrom + "." + foursquareNameExpected;
		int foursquareSubmodulesExpected = 4;
		String foursquareTypeExpected = super.PACKAGE;
		
		String latitudeNameExpected = "latitude";
		String latitudeUniqueNameExpected = modulesFrom + "." + latitudeNameExpected;
		int latitudeSubmodulesExpected = 3;
		String latitudeTypeExpected = super.PACKAGE;
		
		HashMap<String, Object> foursquareExpectedModule = createModuleHashmap(
				foursquareNameExpected, foursquareUniqueNameExpected, foursquareSubmodulesExpected, foursquareTypeExpected);
		HashMap<String, Object> latitudeExpectedModule = createModuleHashmap(
				latitudeNameExpected, latitudeUniqueNameExpected, latitudeSubmodulesExpected, latitudeTypeExpected);
		
		boolean foundFoursquare = compaireDTOWithValues(foursquareExpectedModule, modules);
		boolean foundLatitude = compaireDTOWithValues(latitudeExpectedModule, modules);
		
		assertEquals(true, foundFoursquare);
		assertEquals(true, foundLatitude);
	}
	
	@Test
	public void testGetChildrenOfPackageWithDepthTwo(){
		int totalModulesExpected = 2;
		String modulesFrom = "domain.locationbased";
		int depth = 2;
		
		AnalysedModuleDTO[] modules = service.getChildModulesInModule(modulesFrom, depth);
		assertEquals(totalModulesExpected, modules.length);
		
		String foursquareNameExpected = "foursquare";
		String foursquareUniqueNameExpected = modulesFrom + "." + foursquareNameExpected;
		int foursquareSubmodulesExpected = 4;
		String foursquareTypeExpected = super.PACKAGE;
		
		String latitudeNameExpected = "latitude";
		String latitudeUniqueNameExpected = modulesFrom + "." + latitudeNameExpected;
		int latitudeSubmodulesExpected = 3;
		String latitudeTypeExpected = super.PACKAGE;
		
		HashMap<String, Object> foursquareExpectedModule = createModuleHashmap(
				foursquareNameExpected, foursquareUniqueNameExpected, foursquareSubmodulesExpected, foursquareTypeExpected);
		HashMap<String, Object> latitudeExpectedModule = createModuleHashmap(
				latitudeNameExpected, latitudeUniqueNameExpected, latitudeSubmodulesExpected, latitudeTypeExpected);
		
		boolean foundFoursquare = compaireDTOWithValues(foursquareExpectedModule, modules);
		boolean foundLatitude = compaireDTOWithValues(latitudeExpectedModule, modules);
		
		assertEquals(true, foundFoursquare);
		assertEquals(true, foundLatitude);
	}
	
	@Test
	public void testGetChildrenOfPackageDepthZero(){
		int totalModulesExpected = 1;
		String modulesFrom = "domain";
		int depth = 0;
		
		AnalysedModuleDTO[] modules = service.getChildModulesInModule(modulesFrom, depth);
		assertEquals(totalModulesExpected, modules.length);
		
		String locationbasedNameExpected = "locationbased";
		String locationbasedUniqueNameExpected = modulesFrom + "." + locationbasedNameExpected;
		int locationbasedSubmodulesExpected = 0;
		String locationbasedTypeExpected = super.PACKAGE;
		
		HashMap<String, Object> locationbasedExpectedModule = createModuleHashmap(
				locationbasedNameExpected, locationbasedUniqueNameExpected, locationbasedSubmodulesExpected, locationbasedTypeExpected);
		boolean foundLocationbased = compaireDTOWithValues(locationbasedExpectedModule, modules);
		assertEquals(true, foundLocationbased);
		
		
		List<AnalysedModuleDTO> submodules = modules[0].subModules;
		int totalSubModules = 0;
		assertEquals(totalSubModules, submodules.size());
		
/*		
 		List<AnalysedModuleDTO> foursquaresubmodules = submodules.get(0).subModules;
		int totalFoursquareSubModules = 4;
		assertEquals(totalFoursquareSubModules, foursquaresubmodules.size());
		
		List<AnalysedModuleDTO> latitudesubmodules = submodules.get(1).subModules;
		int totalLatitudeSubModules = 3;
		assertEquals(totalLatitudeSubModules, latitudesubmodules.size());
*/	
	}
	
	@Test
	public void testGetChildrenOfClassDepthOne(){
		int totalModulesExpected = 0;
		String modulesFrom = "domain.locationbased.latitude.Account";
		int depth = 1;
	
		AnalysedModuleDTO[] modules = service.getChildModulesInModule(modulesFrom, depth);
		assertEquals(totalModulesExpected, modules.length);
	}
	
	@Test
	public void testGetChildrenOfClassDepthTwo(){
		int totalModulesExpected = 0;
		String modulesFrom = "domain.locationbased.latitude.Account";
		int depth = 2;
	
		AnalysedModuleDTO[] modules = service.getChildModulesInModule(modulesFrom, depth);
		assertEquals(totalModulesExpected, modules.length);
	}
	
	@Test
	public void testGetChildrenOfClassDepthZero(){
		int totalModulesExpected = 0;
		String modulesFrom = "domain.locationbased.latitude.Account";
		int depth = 0;
	
		AnalysedModuleDTO[] modules = service.getChildModulesInModule(modulesFrom, depth);
		assertEquals(totalModulesExpected, modules.length);
	}
	
	@Test
	public void testGetChildrenOfUnknownPackageDepthZero(){
		int totalModulesExpected = 0;
		String modulesFrom = "domain.unknown";
		int depth = 0;
	
		AnalysedModuleDTO[] modules = service.getChildModulesInModule(modulesFrom, depth);
		assertEquals(totalModulesExpected, modules.length);
	}
	
	@Test
	public void testGetParentOfPackageLevelTwo(){
		String parentFrom = "domain.locationbased";
		AnalysedModuleDTO module = service.getParentModuleForModule(parentFrom);
				
		String nameExpected = "domain";
		String uniqueNameExpected = "domain";
		int submoduleExpected = 0;
		String typeExpected = super.PACKAGE;
		
		assertEquals(nameExpected, module.name);
		assertEquals(uniqueNameExpected, module.uniqueName);
		assertEquals(submoduleExpected, module.subModules.size());
		assertEquals(typeExpected, module.type);
/*		
		AnalysedModuleDTO locationbasedModule = module.subModules.get(0);
		
		String locationbasedNameExpected = "locationbased";
		String locationbasedUniquenameExpected = "domain.locationbased";
		int locationbasedSubmodulesExpected = 0;
		String locationbasedTypeExpected = "package";
		
		assertEquals(locationbasedNameExpected, locationbasedModule.name);
		assertEquals(locationbasedUniquenameExpected, locationbasedModule.uniqueName);
		assertEquals(locationbasedSubmodulesExpected, locationbasedModule.subModules.size());
		assertEquals(locationbasedTypeExpected, locationbasedModule.type);
*/		
	}
	
	@Test
	public void testGetParentOfPackageLevelOne(){
		String parentFrom = "domain";
		AnalysedModuleDTO parentModule = service.getParentModuleForModule(parentFrom);
		
		String nameExpected = "";
		String uniqueNameExpected = "";
		int submodulesExpected = 0;
		String typeExpected = "";
		
		assertEquals(nameExpected, parentModule.name);
		assertEquals(uniqueNameExpected, parentModule.uniqueName);
		assertEquals(submodulesExpected, parentModule.subModules.size());
		assertEquals(typeExpected, parentModule.type);
	}
	
	@Test
	public void testGetParentOfClassLevelFour(){
		String parentFrom = "domain.locationbased.foursquare.Account";
		AnalysedModuleDTO parentModule = service.getParentModuleForModule(parentFrom);
		
		String nameExpected = "foursquare";
		String uniquenameExpected = "domain.locationbased.foursquare";
		int totalSubmodulesExpected = 0;
		String typeExpected = super.PACKAGE;
		
		assertEquals(nameExpected, parentModule.name);
		assertEquals(uniquenameExpected, parentModule.uniqueName);
		assertEquals(totalSubmodulesExpected, parentModule.subModules.size());
		assertEquals(typeExpected, parentModule.type);
	}
	
	@Test
	public void testGetParentOfNotExistingPackageLevelTwo(){
		String parentFrom = "domain.notExist";
		AnalysedModuleDTO parentModule = service.getParentModuleForModule(parentFrom);
		
		String nameExpected = "";
		String uniqueNameExpected = "";
		int submodulesExpected = 0;
		String typeExpected = "";
		
		assertEquals(nameExpected, parentModule.name);
		assertEquals(uniqueNameExpected, parentModule.uniqueName);
		assertEquals(submodulesExpected, parentModule.subModules.size());
		assertEquals(typeExpected, parentModule.type);
	}
	
}
