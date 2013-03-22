package husaccttest.analyse.java.blackbox;

import java.util.HashMap;

import org.junit.Test;

import static org.junit.Assert.*;

import husacct.common.dto.DependencyDTO;
import husaccttest.analyse.TestCaseExtended;

public class TestDomainDependencies extends TestCaseExtended {

    @Test
    public void testGetDependencyFromAndToClasses() {
        String fromPath = "domain.locationbased.foursquare.History";
        String toPath = "infrastructure.socialmedia.locationbased.foursquare.HistoryDAO";
        int totalDependenciesExpected = 2;

        DependencyDTO[] dependencies = service.getDependencies(fromPath, toPath);
        assertEquals(totalDependenciesExpected, dependencies.length);

        String fromPathExpected = fromPath;
        String toPathExpected = toPath;
        String typeExpected = super.EXTENDSCONCRETE;
        int linenumberExpected = 10;

        String fromImportPathExpected = fromPath;
        String toImportPathExpected = toPath;
        String typeImportExpected = super.IMPORT;
        int linenumberImportExpected = 3;


        HashMap<String, Object> expectedDependency = createDependencyHashmap(
                fromPathExpected, toPathExpected, typeExpected, linenumberExpected);
        HashMap<String, Object> expectedImportDependency = createDependencyHashmap(
                fromImportPathExpected, toImportPathExpected, typeImportExpected, linenumberImportExpected);
        boolean foundDependency = compaireDTOWithValues(expectedDependency, dependencies);
        boolean foundImport = compaireDTOWithValues(expectedImportDependency, dependencies);
        assertEquals(true, foundDependency);
        assertEquals(true, foundImport);
    }

    @Test
    public void testGetDependenciesFromAndToPackages() {
        String fromPath = "domain.locationbased.latitude";
        String toPath = "infrastructure.socialmedia.locationbased.latitude";
        int totalDependenciesExpected = 7;

        DependencyDTO[] dependencies = service.getDependencies(fromPath, toPath);
        assertEquals(totalDependenciesExpected, dependencies.length);

        String accountFromPathExpected = fromPath + ".Account";
        String accountToPathExpected = toPath + ".AccountDAO";
        String accountTypeExpected = super.INVOCCONSTRUCTOR;
        int accountLinenumberExpected = 11;

        String friendsFromPathExpected = fromPath + ".Friends";
        String friendsToPathExpected = toPath + ".FriendsDAO";
        String friendsTypeExpected = super.EXTENDSABSTRACT;
        int friendsLinenumberExpected = 10;

        String mapFromPathExpected = fromPath + ".Map";
        String mapToPathExpected = toPath + ".IMap";
        String mapTypeExpected = super.IMPLEMENTS;
        int mapLinenumberExpected = 10;

        HashMap<String, Object> expectedAccountDependency = createDependencyHashmap(
                accountFromPathExpected, accountToPathExpected, accountTypeExpected, accountLinenumberExpected);
        HashMap<String, Object> expectedFriendsDependency = createDependencyHashmap(
                friendsFromPathExpected, friendsToPathExpected, friendsTypeExpected, friendsLinenumberExpected);
        HashMap<String, Object> expectedMapDependency = createDependencyHashmap(
                mapFromPathExpected, mapToPathExpected, mapTypeExpected, mapLinenumberExpected);

        boolean foundAccountDependency = compaireDTOWithValues(expectedAccountDependency, dependencies);
        boolean foundFriendsDependency = compaireDTOWithValues(expectedFriendsDependency, dependencies);
        boolean foundMapDependency = compaireDTOWithValues(expectedMapDependency, dependencies);
        assertEquals(true, foundAccountDependency);
        assertEquals(true, foundFriendsDependency);
        assertEquals(true, foundMapDependency);
    }

    @Test
    public void testGetDependenciesFromPackageToClass() {
        String fromPath = "domain.locationbased.latitude";
        String toPath = "infrastructure.socialmedia.locationbased.latitude.IMap";
        int totalDependenciesExpected = 3;

        DependencyDTO[] dependencies = service.getDependencies(fromPath, toPath);
        assertEquals(totalDependenciesExpected, dependencies.length);

        String fromPathExpected = fromPath + ".Map";
        String toPathExpected = toPath;
        String typeExpected = super.IMPLEMENTS;
        int linenumberExpected = 10;

        String fromImportPathExpected = fromPath + ".Map";
        String toImportPathExpected = toPath;
        String typeImportExpected = super.IMPORT;
        int linenumberImportExpected = 3;

        HashMap<String, Object> expectedDependency = createDependencyHashmap(
                fromPathExpected, toPathExpected, typeExpected, linenumberExpected);
        HashMap<String, Object> expectedImportDependency = createDependencyHashmap(fromImportPathExpected, toImportPathExpected, typeImportExpected, linenumberImportExpected);
        boolean foundDependency = compaireDTOWithValues(expectedDependency, dependencies);
        boolean foundImportDependency = compaireDTOWithValues(expectedImportDependency, dependencies);
        assertEquals(true, foundDependency);
        assertEquals(true, foundImportDependency);
    }

    @Test
    public void testGetDependenciesFromAndToWithoutRelation() {
        String fromPath = "domain.locationbased.foursquare.History";
        String toPath = "infrastructure.socialmedia.locationbased.latitude.IMap";
        int totalDependenciesExpected = 0;

        DependencyDTO[] dependencies = service.getDependencies(fromPath, toPath);
        assertEquals(totalDependenciesExpected, dependencies.length);
        assertNotNull(dependencies);
    }

    @Test
    public void testGetDependencyFromAndToNotExistingValus() {
        String fromPath = "domain.notexisting";
        String toPath = "infrastructure.notexisting";
        int totalDependenciesExpected = 0;

        DependencyDTO[] dependencies = service.getDependencies(fromPath, toPath);
        assertEquals(totalDependenciesExpected, dependencies.length);
        assertNotNull(dependencies);
    }

    @Test
    public void testGetAllDependenciesOfClass() {
        String fromPath = "domain.locationbased.foursquare.Account";
        int totalDependencies = 2;

        DependencyDTO[] dependencies = service.getDependenciesFrom(fromPath);
        assertEquals(totalDependencies, dependencies.length);

        String fromPathExpected = fromPath;
        String toPathExpected = "infrastructure.socialmedia.locationbased.foursquare.AccountDAO";
        String typeExpected = super.INVOCCONSTRUCTOR;
        int linenumberExpected = 10;

        HashMap<String, Object> expectedDependency = createDependencyHashmap(fromPathExpected, toPathExpected, typeExpected, linenumberExpected);
        boolean foundDependency = compaireDTOWithValues(expectedDependency, dependencies);
        assertEquals(true, foundDependency);
    }

    @Test
    public void testGetDependenciesToClass() {
        String toPath = "infrastructure.socialmedia.locationbased.foursquare.AccountDAO";
        int totalDependencies = 2;

        DependencyDTO[] dependencies = service.getDependenciesTo(toPath);
        assertEquals(totalDependencies, dependencies.length);

        String fromExpected = "domain.locationbased.foursquare.Account";
        String toExpected = toPath;
        String typeExpected = super.INVOCCONSTRUCTOR;
        int lineExpected = 10;

        HashMap<String, Object> expectedDependency = createDependencyHashmap(
                fromExpected, toExpected, typeExpected, lineExpected);
        boolean foundDependency = compaireDTOWithValues(expectedDependency, dependencies);
        assertEquals(true, foundDependency);
    }

    @Test
    public void testGetDependenciesToPackage() {
        String toPath = "infrastructure.socialmedia.locationbased.latitude";
        int totalDependenciesExpected = 7;

        DependencyDTO[] dependencies = service.getDependenciesTo(toPath);
        assertEquals(totalDependenciesExpected, dependencies.length);

        String accountFromExpected = "domain.locationbased.latitude.Account";
        String accountToExpected = toPath + ".AccountDAO";
        String accountTypeExpected = super.INVOCCONSTRUCTOR;
        int accountLineExpected = 11;

        String friendsFromExpected = "domain.locationbased.latitude.Friends";
        String friendsToExpected = toPath + ".FriendsDAO";
        String friendsTypeExpected = super.EXTENDSABSTRACT;
        int friendsLineExpected = 10;

        String mapFromExpected = "domain.locationbased.latitude.Map";
        String mapToExpected = toPath + ".IMap";
        String mapTypeExpected = super.IMPLEMENTS;
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

    @Test
    public void testGetDependencyToUnknown() {
        String toPath = "infrastructure.unknown";
        int totalExpected = 0;

        DependencyDTO[] dependencies = service.getDependenciesTo(toPath);
        assertEquals(totalExpected, dependencies.length);
    }

    @Test
    public void testGetAllDependenciesOfPackage() {
        String fromPath = "domain.locationbased.latitude";
        int totalDependencies = 7;

        DependencyDTO[] dependencies = service.getDependenciesFrom(fromPath);
        assertEquals(totalDependencies, dependencies.length);

        String accountFromPathExpected = fromPath + ".Account";
        String accountToPathExpected = "infrastructure.socialmedia.locationbased.latitude.AccountDAO";
        String accountTypeExpected = super.INVOCCONSTRUCTOR;
        int accountLinenumberExpected = 11;

        String friendsFromPathExpected = fromPath + ".Friends";
        String friendsToPathExpected = "infrastructure.socialmedia.locationbased.latitude.FriendsDAO";
        String friendsTypeExpected = super.EXTENDSABSTRACT;
        int friendsLinenumberExpected = 10;

        String mapFromPathExpected = fromPath + ".Map";
        String mapToPathExpected = "infrastructure.socialmedia.locationbased.latitude.IMap";
        String mapTypeExpected = super.IMPLEMENTS;
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
