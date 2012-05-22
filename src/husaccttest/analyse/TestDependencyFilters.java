package husaccttest.analyse;

import java.util.HashMap;

import husacct.common.dto.DependencyDTO;

public class TestDependencyFilters extends TestCaseExtended{


	public void testGetDependenciesBetweenClassTypeExtends(){
		String from = "domain.locationbased.foursquare.History";
		String to = "infrastructure.socialmedia.locationbased.foursquare.HistoryDAO";
		String[] dependencyFilter = {super.EXTENDSCONCRETE};
		int dependenciesExpected = 1;

		DependencyDTO[] dependencies = service.getDependencies(from, to, dependencyFilter);
		assertEquals(dependenciesExpected, dependencies.length);
		
		String fromExpected = from;
		String toExpected = to;
		String typeExpected = super.EXTENDSCONCRETE;
		int linenumberExpected = 10;
		
		HashMap<String, Object> expectedDependency = createDependencyHashmap(
				fromExpected, toExpected, typeExpected, linenumberExpected);
		boolean foundDependency = compaireDTOWithValues(expectedDependency, dependencies);
		assertEquals(true, foundDependency);	
	}
	
	public void testGetDependenciesBetweenPackageTypeExtends(){
		String from = "domain.locationbased.foursquare";
		String to = "infrastructure.socialmedia.locationbased.foursquare";
		String[] dependencyFilter = {super.EXTENDSCONCRETE};
		int dependenciesExpected = 1;
		
		DependencyDTO[] dependencies = service.getDependencies(from, to, dependencyFilter);
		assertEquals(dependenciesExpected, dependencies.length);
		
		String historyFromExpected = from + ".History";
		String historyToExpected = to + ".HistoryDAO";
		String historyTypeExpected = dependencyFilter[0];
		int historyLinenumberExpected = 10;
		
		HashMap<String, Object> historyDependency = createDependencyHashmap(
				historyFromExpected, historyToExpected, historyTypeExpected, historyLinenumberExpected);
		boolean foundHistory = compaireDTOWithValues(historyDependency, dependencies);
		assertEquals(true, foundHistory);
	}
	
	public void testGetDependenciesBetweenPackageTypeExtendsAndInvocConstructor(){
		String from = "domain.locationbased.foursquare";
		String to = "infrastructure.socialmedia.locationbased.foursquare";
		String[] dependencyFilter = {super.EXTENDSCONCRETE, super.INVOCCONSTRUCTOR};
		int dependenciesExpected = 2;
		
		DependencyDTO[] dependencies = service.getDependencies(from, to, dependencyFilter);
		assertEquals(dependenciesExpected, dependencies.length);
		
		String historyFromExpected = from + ".History";
		String historyToExpected = to + ".HistoryDAO";
		String historyTypeExpected = super.EXTENDSCONCRETE;
		int historyLinenumberExpected = 10;
		
		String accountFromExpected = from + ".Account";
		String accountToExpected = to + ".AccountDAO";
		String accountTypeExpected = super.INVOCCONSTRUCTOR;
		int accountLinenumberExpected = 10;
		
		HashMap<String, Object> historyDependency = createDependencyHashmap(
				historyFromExpected, historyToExpected, historyTypeExpected, historyLinenumberExpected);
		HashMap<String, Object> accountDependency = createDependencyHashmap(
				accountFromExpected, accountToExpected, accountTypeExpected, accountLinenumberExpected);
		
		
		boolean foundHistory = compaireDTOWithValues(historyDependency, dependencies);
		boolean foundAccount = compaireDTOWithValues(accountDependency, dependencies);
		
		assertEquals(true, foundHistory);
		assertEquals(true, foundAccount);
	}
	
	public void testGetDependenciesFilterWithoutResults(){
		String from = "domain.locationbased.foursquare";
		String to = "infrastructure.socialmedia.locationbased.foursquare";
		String[] dependencyFilter = {super.EXCEPTION};
		int dependenciesExpected = 0;
		
		DependencyDTO[] dependencies = service.getDependencies(from, to, dependencyFilter);
		assertEquals(dependenciesExpected, dependencies.length);
	}
	
	public void testGetDependenciesFilterWithNotExistingPackage(){
		String from = "domain.notExisting";
		String to = "infrastructure.socialmedia.locationbased.foursquare";
		String[] dependencyFilter = {super.EXCEPTION};
		int dependenciesExpected = 0;
		
		DependencyDTO[] dependencies = service.getDependencies(from, to, dependencyFilter);
		assertEquals(dependenciesExpected, dependencies.length);
	}
	
	public void testGetDependenciesFromClassTypeImplements(){
		String from = "domain.locationbased.latitude.Map";
		String[] dependencyFilter = {super.IMPLEMENTS};
		int dependenciesExpected = 1;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from, dependencyFilter);
		super.printDependencies(dependencies);
		assertEquals(dependenciesExpected, dependencies.length);
		
		String fromExpected = from;
		String toExpected = "infrastructure.socialmedia.locationbased.latitude.IMap";
		String typeExpected = super.IMPLEMENTS;
		int linenumberExpected = 10;
		
		HashMap<String, Object> dependencyExpected = createDependencyHashmap(
				fromExpected, toExpected, typeExpected, linenumberExpected);
		boolean foundDependency = compaireDTOWithValues(dependencyExpected, dependencies);
		assertEquals(true, foundDependency);
	}
	
	public void testGetDependenciesFromPackageTypeExtends(){
		String from = "domain.locationbased.latitude";
		String[] dependencyFilter = {super.EXTENDSABSTRACT};
		int dependenciesExpected = 1;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from, dependencyFilter);
		assertEquals(dependenciesExpected, dependencies.length);
		
		String fromExpected = from + ".Friends";
		String toExpected = "infrastructure.socialmedia.locationbased.latitude.FriendsDAO";
		String typeExpected = super.EXTENDSABSTRACT;
		int linenumberExpected = 10;
		
		HashMap<String, Object> dependencyExpected = createDependencyHashmap(
				fromExpected, toExpected, typeExpected, linenumberExpected);
		boolean foundDependency = compaireDTOWithValues(dependencyExpected, dependencies);
		assertEquals(true, foundDependency);
	}
	
	public void testGetDependenciesFromPackageTypesExtendsANDInvocConstructor(){
		String from = "domain.locationbased.latitude";
		String[] dependencyFilter = {super.EXTENDSABSTRACT, super.INVOCCONSTRUCTOR};
		int expectedDependencies = 2;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from, dependencyFilter);
		assertEquals(expectedDependencies, dependencies.length);
		
		String friendsFromExpected = from + ".Friends";
		String friendsToExpeceted = "infrastructure.socialmedia.locationbased.latitude.FriendsDAO";
		String friendsTypeExpected = super.EXTENDSABSTRACT;
		int friendsLinenumberExpected = 10;
		
		String accountFromExpected = from + ".Account";
		String accountToExpected = "infrastructure.socialmedia.locationbased.latitude.AccountDAO";
		String accountTypeExpected = super.INVOCCONSTRUCTOR;
		int accountLinenumberExpected = 11;
		
		HashMap<String, Object> friendDependencyExpected = createDependencyHashmap(friendsFromExpected, friendsToExpeceted, friendsTypeExpected, friendsLinenumberExpected);
		HashMap<String, Object> accountDependencyExpected = createDependencyHashmap(accountFromExpected, accountToExpected, accountTypeExpected, accountLinenumberExpected);
		
		boolean foundFriends = compaireDTOWithValues(friendDependencyExpected, dependencies);
		boolean foundAccount = compaireDTOWithValues(accountDependencyExpected, dependencies);
		
		assertEquals(true, foundFriends);
		assertEquals(true, foundAccount);
	}
	
	public void testGetDependenciesFromNoResultsWithFilter(){
		String from = "domain.locationbased.latitude";
		String[] dependencyFilter = {super.EXCEPTION};
		int dependenciesExpected = 0;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from, dependencyFilter);
		assertEquals(dependenciesExpected, dependencies.length);
	}
	
	public void testGetDependenciesFromNotExistingPackage(){
		String from = "domain.notExisting";
		String[] dependencyFilter = {super.EXCEPTION};
		int expectedDependencies = 0;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from, dependencyFilter);
		assertEquals(expectedDependencies, dependencies.length);
	}
		
	public void testGetDependenciesToClassTypeImplements(){
		String to = "infrastructure.socialmedia.locationbased.latitude.IMap";
		String[] dependencyFilter = {super.IMPLEMENTS};
		int dependenciesExpected = 2;
		
		DependencyDTO[] dependencies = service.getDependenciesTo(to, dependencyFilter);
		assertEquals(dependenciesExpected, dependencies.length);
		
		String fromExpected = "domain.locationbased.latitude.Map";
		String toExpected = to;
		String typeExpected = super.IMPLEMENTS;
		int linenumberExpected = 10;
		
		HashMap<String, Object> dependencyExpected = createDependencyHashmap(
				fromExpected, toExpected, typeExpected, linenumberExpected);
		boolean foundDependency = compaireDTOWithValues(dependencyExpected, dependencies);
		assertEquals(true, foundDependency);
	}
	
	public void testGetDependenciesToPackageTypeExtends(){
		String to = "infrastructure.socialmedia.locationbased.latitude";
		String[] dependencyFilter = {super.EXTENDSABSTRACT};
		int dependenciesExpected = 1;
		
		DependencyDTO[] dependencies = service.getDependenciesTo(to, dependencyFilter);
		assertEquals(dependenciesExpected, dependencies.length);
		
		String fromExpected = "domain.locationbased.latitude.Friends";
		String toExpected = to + ".FriendsDAO";
		String typeExpected = super.EXTENDSABSTRACT;
		int linenumberExpected = 10;
		
		HashMap<String, Object> dependencyExpected = createDependencyHashmap(
				fromExpected, toExpected, typeExpected, linenumberExpected);
		boolean foundDependency = compaireDTOWithValues(dependencyExpected, dependencies);
		assertEquals(true, foundDependency);
	}
	
	public void testGetDependenciesToPackageTypesExtendsANDInvocConstructor(){
		String to = "infrastructure.socialmedia.locationbased.latitude";
		String[] dependencyFilter = {super.EXTENDSABSTRACT, super.INVOCCONSTRUCTOR};
		int expectedDependencies = 2;
		
		DependencyDTO[] dependencies = service.getDependenciesTo(to, dependencyFilter);
		assertEquals(expectedDependencies, dependencies.length);
		
		String friendsFromExpected = "domain.locationbased.latitude.Friends";
		String friendsToExpeceted = to + ".FriendsDAO";
		String friendsTypeExpected = super.EXTENDSABSTRACT;
		int friendsLinenumberExpected = 10;
		
		String accountFromExpected = "domain.locationbased.latitude.Account";
		String accountToExpected = to + ".AccountDAO";
		String accountTypeExpected = super.INVOCCONSTRUCTOR;
		int accountLinenumberExpected = 11;
		
		HashMap<String, Object> friendDependencyExpected = createDependencyHashmap(friendsFromExpected, friendsToExpeceted, friendsTypeExpected, friendsLinenumberExpected);
		HashMap<String, Object> accountDependencyExpected = createDependencyHashmap(accountFromExpected, accountToExpected, accountTypeExpected, accountLinenumberExpected);
		
		boolean foundFriends = compaireDTOWithValues(friendDependencyExpected, dependencies);
		boolean foundAccount = compaireDTOWithValues(accountDependencyExpected, dependencies);
		
		assertEquals(true, foundFriends);
		assertEquals(true, foundAccount);
	}
	
	public void testGetDependenciesToNoResultsWithFilter(){
		String from = "domain.locationbased.latitude";
		String[] dependencyFilter = {super.EXCEPTION};
		int dependenciesExpected = 0;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from, dependencyFilter);
		assertEquals(dependenciesExpected, dependencies.length);
	}
	
	public void testGetDependenciesToNotExistingPackage(){
		String from = "domain.notExisting";
		String[] dependencyFilter = {super.EXCEPTION};
		int expectedDependencies = 0;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from, dependencyFilter);
		assertEquals(expectedDependencies, dependencies.length);
	}
}

