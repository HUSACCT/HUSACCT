package husaccttest.analyse.RecognationTest.declarations;

import java.util.HashMap;
import static org.junit.Assert.*;

import husacct.common.dto.DependencyDTO;
import husaccttest.analyse.benchmarkLeo.BenchmarkExtended;



public class DeclarationInnerPackage extends BenchmarkExtended{

	private String rootDirectory = "declarations";
	
	public void testPhone(){
		String from = rootDirectory + ".inpackage.Phone";
		int expectedDependencies = 1;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);

		String fromDeclarationExpected = from;
		String toDeclarationExpected = rootDirectory + ".inpackage.User";
		String typeDeclarationExpected = super.DECLARATION;
		int linenumberDeclarationExpected = 5;
		
		HashMap<String, Object> dependencyDeclarationExpected = createDependencyHashmap(
				fromDeclarationExpected, toDeclarationExpected, typeDeclarationExpected, linenumberDeclarationExpected);
		
		boolean foundDeclarationDependency = compaireDTOWithValues(dependencyDeclarationExpected, dependencies);
		
		assertEquals(true, foundDeclarationDependency);
	}
	
	public void testToy(){
		String from = rootDirectory + ".inpackage.Toy";
		int expectedDependencies = 1;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);

		String fromDeclarationExpected = from;
		String toDeclarationExpected = rootDirectory + ".inpackage.User";
		String typeDeclarationExpected = super.DECLARATION;
		int linenumberDeclarationExpected = 5;
		
		HashMap<String, Object> dependencyDeclarationExpected = createDependencyHashmap(
				fromDeclarationExpected, toDeclarationExpected, typeDeclarationExpected, linenumberDeclarationExpected);
		
		boolean foundDeclarationDependency = compaireDTOWithValues(dependencyDeclarationExpected, dependencies);
		
		assertEquals(true, foundDeclarationDependency);
	}
	
	
	
}
