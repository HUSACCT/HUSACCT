package husaccttest.analyse.RecognationTest.declarations;

import java.util.HashMap;

import husacct.common.dto.DependencyDTO;
import husaccttest.analyse.benchmarkLeo.BenchmarkExtended;



public class DeclarationOuterPackage extends BenchmarkExtended{

	private String rootDirectory = "declarations.outerpackage";
	
	public void testBycicle(){
		String from = rootDirectory + ".package2.Bycicle";
		int expectedDependencies = 2;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);

		String fromImportExpected = from;
		String toImportExpected = rootDirectory + ".package1";
		String typeImportExpected = super.IMPORT;
		int linenumberImportExpected = 3;
		
		String fromDeclarationExpected = from;
		String toDeclarationExpected = rootDirectory + ".package1.Driver";
		String typeDeclarationExpected = super.DECLARATION;
		int linenumberDeclarationExpected = 7;
		
		HashMap<String, Object> dependencyImportExpected = createDependencyHashmap(
				fromImportExpected, toImportExpected, typeImportExpected, linenumberImportExpected);
		HashMap<String, Object> dependencyDeclarationExpected = createDependencyHashmap(
				fromDeclarationExpected, toDeclarationExpected, typeDeclarationExpected, linenumberDeclarationExpected);
		
		boolean foundImportDependency = compaireDTOWithValues(dependencyImportExpected, dependencies);
		boolean foundDeclarationDependency = compaireDTOWithValues(dependencyDeclarationExpected, dependencies);
		
		assertEquals(true, foundImportDependency);
		assertEquals(true, foundDeclarationDependency);
	}
	
	public void testCar(){
		String from = rootDirectory + ".package2.Car";
		int expectedDependencies = 2;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);

		String fromImportExpected = from;
		String toImportExpected = rootDirectory + ".package1.Driver";
		String typeImportExpected = super.IMPORT;
		int linenumberImportExpected = 3;
		
		String fromDeclarationExpected = from;
		String toDeclarationExpected = rootDirectory + ".package1.Driver";
		String typeDeclarationExpected = super.DECLARATION;
		int linenumberDeclarationExpected = 7;
		
		HashMap<String, Object> dependencyImportExpected = createDependencyHashmap(
				fromImportExpected, toImportExpected, typeImportExpected, linenumberImportExpected);
		HashMap<String, Object> dependencyDeclarationExpected = createDependencyHashmap(
				fromDeclarationExpected, toDeclarationExpected, typeDeclarationExpected, linenumberDeclarationExpected);
		
		boolean foundImportDependency = compaireDTOWithValues(dependencyImportExpected, dependencies);
		boolean foundDeclarationDependency = compaireDTOWithValues(dependencyDeclarationExpected, dependencies);
		
		assertEquals(true, foundImportDependency);
		assertEquals(true, foundDeclarationDependency);
	}
	
	public void testMotor(){
		String from = rootDirectory + ".package2.Motor";
		int expectedDependencies = 1;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String fromDeclarationExpected = from;
		String toDeclarationExpected = rootDirectory + ".package1.Driver";
		String typeDeclarationExpected = super.DECLARATION;
		int linenumberDeclarationExpected = 5;
		
		HashMap<String, Object> dependencyDeclarationExpected = createDependencyHashmap(
				fromDeclarationExpected, toDeclarationExpected, typeDeclarationExpected, linenumberDeclarationExpected);
		
		boolean foundDeclarationDependency = compaireDTOWithValues(dependencyDeclarationExpected, dependencies);
		
		assertEquals(true, foundDeclarationDependency);
	}

	
	
	
}
