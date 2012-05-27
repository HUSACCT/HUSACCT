package husaccttest.analyse.benchmarkjava.domain;

import java.util.HashMap;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husaccttest.analyse.benchmarkjava.BenchmarkExtended;



public class Google_plus extends BenchmarkExtended{

	@Test
	public void testNavigation(){
		String from = "domain.google_plus";
		
		int expectedchildmodules = 3;
		AnalysedModuleDTO[] childModules = service.getChildModulesInModule(from);
		assertEquals(expectedchildmodules, childModules.length);
		
		HashMap<String, Object> circleExpectedModule = createModuleHashmap(
				"Circle", 
				from +  ".Circle",
				0,
				super.CLASS);
		
		HashMap<String, Object> contactpictureExpectedModule = createModuleHashmap(
				"Contact", 
				from +  ".Contact",
				0,
				super.CLASS);
		
		HashMap<String, Object> observableExpectedModule = createModuleHashmap(
				"Observable", 
				from +  ".Observable",
				0,
				super.CLASS);
		
		boolean foundcircle = compaireDTOWithValues(circleExpectedModule, childModules);
		boolean foundcontact = compaireDTOWithValues(contactpictureExpectedModule, childModules);
		boolean foundobservable = compaireDTOWithValues(observableExpectedModule, childModules);
		
		assertEquals(true, foundcircle);
		assertEquals(true, foundcontact);
		assertEquals(true, foundobservable);
	}
	
	@Ignore ("Generics in a InvocConstructor are not recognized!")
	@Test
	public void testDomainGooglePlusCircle(){
		String from = "domain.google_plus.Circle";
		int expectedDependencies = 11;
		
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = super.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
	}
	
	@Ignore ("this.name = name = 2 Dependencies")
	@Test
	public void testDomainGooglePlusContact(){
		String from = "domain.google_plus.Contact";
		int expectedDependencies = 2;
		
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = super.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String fromExtendsExpected = from;
		String toExtendsExpected = "domain.google_plus.Observable";
		String typeExtendsExpected = super.EXTENDSABSTRACT;
		int linenumberExtendsExpected = 3;
		
		String fromDeclarationExpected = from;
		String toDeclarationExpected = "String";
		String typeDeclarationExpected = super.DECLARATION;
		int linenumberDeclarationExpected = 5;
		
		
		HashMap<String, Object> dependencyExtendsExpected = createDependencyHashmap(
				fromExtendsExpected, toExtendsExpected, typeExtendsExpected, linenumberExtendsExpected);
		HashMap<String, Object> dependencyDeclarationExpected = createDependencyHashmap(
				fromDeclarationExpected, toDeclarationExpected, typeDeclarationExpected, linenumberDeclarationExpected);
		boolean foundExtendsDependency = compaireDTOWithValues(dependencyExtendsExpected, dependencies);
		boolean foundDeclarationDependency = compaireDTOWithValues(dependencyDeclarationExpected, dependencies);
		assertEquals(true, foundExtendsDependency);
		assertEquals(true, foundDeclarationDependency);
	}
	
	@Ignore ("test(package.package.test a){} = Dependencies pacakge|package|test|a (4)")
	@Test
	public void testDomainGooglePlusObservable(){
		String from = "domain.google_plus.Observable";
		int expectedDependencies = 13;
		
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = super.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String toImport1Expected = "java.util.ArrayList";
		int linenumberImport1Expected = 3;
		
		String toImport2Expected = "java.util.List";
		int linenumberImport2Expected = 4;
		
		String toImport3Expected = "presentation.gui.observer.google_plus.Observer";
		int linenumberImport3Expected = 6;
		
		
		HashMap<String, Object> dependencyImport1Expected = createImportHashmap(from, toImport1Expected, linenumberImport1Expected);
		HashMap<String, Object> dependencyImport2Expected = createImportHashmap(from, toImport2Expected, linenumberImport2Expected);
		HashMap<String, Object> dependencyImport3Expected = createImportHashmap(from, toImport3Expected, linenumberImport3Expected);
		boolean foundImport1Dependency = compaireDTOWithValues(dependencyImport1Expected, dependencies);
		boolean foundImport2Dependency = compaireDTOWithValues(dependencyImport2Expected, dependencies);
		boolean foundImport3Dependency = compaireDTOWithValues(dependencyImport3Expected, dependencies);
		assertEquals(true, foundImport1Dependency);
		assertEquals(true, foundImport2Dependency);
		assertEquals(true, foundImport3Dependency);
	}
	
	
	
	
}
