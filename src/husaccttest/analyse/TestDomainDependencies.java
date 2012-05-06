package husaccttest.analyse;

import java.util.ArrayList;
import java.util.HashMap;

import husacct.analyse.AnalyseServiceImpl;
import husacct.common.dto.DependencyDTO;

public class TestDomainDependencies extends TestCaseExtended{

	private AnalyseServiceImpl service;
	
	public void setUp(){
		service = new AnalyseServiceImpl();
	}
	
	public void testGetDependencyFromAndToClasses(){
		String fromPath = "domain.locationbased.foursquare.History";
		String toPath = "infrastructure.socialmedia.locationbased.foursquare.HistoryDAO";
		int totalDependenciesExpected = 1;
		
		DependencyDTO[] dependencies = service.getDependencies(fromPath, toPath);
		assertEquals(totalDependenciesExpected, dependencies.length);
		
		String fromPathExpected = fromPath;
		String toPathExpected = toPath;
		String typeExpected = "Extends";
		int linenumberExpected = 10;
		
		HashMap<String, Object> expectedDependency = createDependencyHashmap(
				fromPathExpected, toPathExpected, typeExpected, linenumberExpected);
		
		boolean foundDependency = compaireDTOWithValues(expectedDependency, dependencies);
		assertEquals(true, foundDependency);
	}
	
	public void testGetDependenciesFromAndToPackages(){
		String fromPath = "domain.locationbased.latitude";
		String toPath = "infrastructure.socialmedia.locationbased.latitude";
		int totalDependenciesExpected = 3;
		
		DependencyDTO[] dependencies = service.getDependencies(fromPath, toPath);
		assertEquals(totalDependenciesExpected, dependencies.length);
		
		
		
		String accountFromPathExpected = fromPath + ".Account";
		String accountToPathExpected = toPath + ".AccountDAO";
		String accountTypeExpected = "InvocConstructor";
		int accountLinenumberExpected = 11;
		
		String friendsFromPathExpected = fromPath +".Friends";
		String friendsToPathExpected = toPath + ".FriendsDAO";
		String friendsTypeExpected = "Extends";
		int friendsLinenumberExpected = 10;

		String mapFromPathExpected = fromPath + ".Map";
		String mapToPathExpected = toPath + ".IMap";
		String mapTypeExpected = "Implements";
		int mapLinenumberExpected = 10;
		
		HashMap<String, Object> expectedAccountDependency = createDependencyHashmap (
				accountFromPathExpected, accountToPathExpected, accountTypeExpected, accountLinenumberExpected);
		
		HashMap<String, Object> expectedFriendsDependency = createDependencyHashmap(
				friendsFromPathExpected, friendsToPathExpected, friendsTypeExpected, friendsLinenumberExpected);
		
		HashMap<String, Object> expectedMapDependency = createDependencyHashmap(
				mapFromPathExpected, mapToPathExpected, mapTypeExpected, mapLinenumberExpected);
		
		ArrayList<Object> expectedDependencies = new ArrayList<Object>();
		expectedDependencies.add(expectedAccountDependency);
		expectedDependencies.add(expectedFriendsDependency);
		expectedDependencies.add(expectedMapDependency);
		
		boolean foundAccountDependency = compaireDTOWithValues(expectedAccountDependency, dependencies);
		boolean foundFriendsDependency = compaireDTOWithValues(expectedFriendsDependency, dependencies);
		boolean foundMapDependency = compaireDTOWithValues(expectedMapDependency, dependencies);
		assertEquals(true, foundAccountDependency);
		assertEquals(true, foundFriendsDependency);
		assertEquals(true, foundMapDependency);
	}
	
	public void testGetDependenciesFromPackageToClass(){
		String fromPath = "domain.locationbased.latitude";
		String toPath = "infrastructure.socialmedia.locationbased.latitude.IMap";
		int totalDependenciesExpected = 1;
		
		DependencyDTO[] dependencies = service.getDependencies(fromPath, toPath);
		
		assertEquals(totalDependenciesExpected, dependencies.length);
		
		String fromPathExpected = fromPath + ".Map";
		String toPathExpected = toPath;
		String typeExpected = "Implements";
		int linenumberExpected = 10;
		
		HashMap<String, Object> expectedDependency = createDependencyHashmap(fromPathExpected, toPathExpected, typeExpected, linenumberExpected);
		boolean foundDependency = compaireDTOWithValues(expectedDependency, dependencies);
		assertEquals(true, foundDependency);		
	}
	
	public void testGetDependenciesFromAndToWithoutRelation(){
		String fromPath = "domain.locationbased.foursquare.History";
		String toPath = "infrastructure.socialmedia.locationbased.latitude.IMap";
		int totalDependenciesExpected = 0;
		
		DependencyDTO[] dependencies = service.getDependencies(fromPath, toPath);
		assertEquals(totalDependenciesExpected, dependencies.length);
		assertNotNull(dependencies);
	}
	
	public void testGetDependencyFromAndToNotExistingValus(){
		String fromPath = "domain.notexisting";
		String toPath = "infrastructure.notexisting";
		int totalDependenciesExpected = 0;
		
		DependencyDTO[] dependencies = service.getDependencies(fromPath, toPath);
		assertEquals(totalDependenciesExpected, dependencies.length);
		assertNotNull(dependencies);
	}
	
	public void testGetAllDependenciesOfClass(){
		String fromPath = "domain.locationbased.foursquare.Account";
		int totalDependencies = 1;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(fromPath);
		assertEquals(totalDependencies, dependencies.length);
		
		String fromPathExpected = fromPath;
		String toPathExpected = "infrastructure.socialmedia.locationbased.foursquare.AccountDAO";
		String typeExpected = "InvocConstructor";
		int linenumberExpected = 10;
		
		HashMap<String, Object> expectedDependency = createDependencyHashmap(fromPathExpected, toPathExpected, typeExpected, linenumberExpected);
		boolean foundDependency = compaireDTOWithValues(expectedDependency, dependencies);
		assertEquals(true, foundDependency); 
	}
	
	public void testGetDependenciesToClass(){
		String toPath = "infrastructure.socialmedia.locationbased.foursquare.AccountDAO";
		int totalDependencies = 1;
		
		DependencyDTO[] dependencies = service.getDependenciesTo(toPath);
		assertEquals(totalDependencies, dependencies.length);
		
		String fromExpected = "domain.locationbased.foursquare.Account";
		String toExpected = toPath;
		String typeExpected = "InvocConstructor";
		int lineExpected = 10;
		
		HashMap<String, Object> expectedDependency = createDependencyHashmap(
				fromExpected, toExpected, typeExpected, lineExpected);
		
		boolean foundDependency = compaireDTOWithValues(expectedDependency, dependencies);
		assertEquals(true, foundDependency);
	}
	
	public void testGetDependenciesToPackage(){
		String toPath = "infrastructure.socialmedia.locationbased.latitude";
		int totalDependenciesExpected = 3;
				
		DependencyDTO[] dependencies = service.getDependenciesTo(toPath);
		assertEquals(totalDependenciesExpected, dependencies.length);
		
		String accountFromExpected = "domain.locationbased.latitude.Account";
		String accountToExpected = toPath + ".AccountDAO";
		String accountTypeExpected = "InvocConstructor";
		int accountLineExpected = 11;
		
		String friendsFromExpected = "domain.locationbased.latitude.Friends";
		String friendsToExpected = toPath + ".FriendsDAO";
		String friendsTypeExpected = "Extends";
		int friendsLineExpected = 10;
		
		String mapFromExpected  = "domain.locationbased.latitude.Map";
		String mapToExpected = toPath + ".IMap";
		String mapTypeExpected = "Implements";
		int mapLineExpected = 10;
		
		HashMap<String, Object> accountDependency = createDependencyHashmap(accountFromExpected, accountToExpected, accountTypeExpected, accountLineExpected);
		HashMap<String, Object> friendsDependency = createDependencyHashmap(friendsFromExpected, friendsToExpected, friendsTypeExpected, friendsLineExpected);
		HashMap<String, Object> mapDependency = createDependencyHashmap(mapFromExpected, mapToExpected, mapTypeExpected, mapLineExpected);
		
		boolean foundAccount = compaireDTOWithValues(accountDependency, dependencies);
		boolean foundFriends = compaireDTOWithValues(friendsDependency, dependencies);
		boolean foundMap = compaireDTOWithValues(mapDependency, dependencies);
		
		assertEquals(true, foundAccount);
		assertEquals(true, foundFriends);
		assertEquals(true, foundMap);
	}
	
	public void testGetDependencyToUnknown(){
		String toPath = "infrastructure.unknown";
		int totalExpected = 0;
		
		DependencyDTO[] dependencies = service.getDependenciesTo(toPath);
		assertEquals(totalExpected, dependencies.length);
	}
	
	public void testGetAllDependenciesOfPackage(){
		String fromPath = "domain.locationbased.latitude";
		int totalDependencies = 3;		
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(fromPath);
		assertEquals(totalDependencies, dependencies.length);
		
		String accountFromPathExpected = fromPath + ".Account";
		String accountToPathExpected = "infrastructure.socialmedia.locationbased.latitude.AccountDAO";
		String accountTypeExpected = "InvocConstructor";
		int accountLinenumberExpected = 11;
		
		String friendsFromPathExpected = fromPath + ".Friends";
		String friendsToPathExpected = "infrastructure.socialmedia.locationbased.latitude.FriendsDAO";
		String friendsTypeExpected = "Extends";
		int friendsLinenumberExpected = 10;
		
		String mapFromPathExpected = fromPath + ".Map";
		String mapToPathExpected = "infrastructure.socialmedia.locationbased.latitude.IMap";
		String mapTypeExpected = "Implements";
		int mapLinenumberExpected = 10;
		
		
		HashMap<String, Object> accountExpectedDependency = createDependencyHashmap(
				accountFromPathExpected, accountToPathExpected, accountTypeExpected, accountLinenumberExpected);
		HashMap<String, Object> friendsExpectedDependency = createDependencyHashmap(
				friendsFromPathExpected, friendsToPathExpected, friendsTypeExpected, friendsLinenumberExpected);
		HashMap<String, Object> mapExpectedDependency = createDependencyHashmap(
				mapFromPathExpected, mapToPathExpected, mapTypeExpected, mapLinenumberExpected);
		
		boolean accountFoundDependency = compaireDTOWithValues(accountExpectedDependency, dependencies);
		boolean friendsFoundDependency = compaireDTOWithValues(friendsExpectedDependency, dependencies);
		boolean mapFoundDependency = compaireDTOWithValues(mapExpectedDependency, dependencies);
		assertEquals(true, accountFoundDependency);
		assertEquals(true, friendsFoundDependency);
		assertEquals(true, mapFoundDependency);
	}
	
	
}
