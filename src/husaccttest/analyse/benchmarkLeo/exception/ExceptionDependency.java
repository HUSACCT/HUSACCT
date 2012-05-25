package husaccttest.analyse.benchmarkLeo.exception;

import java.util.HashMap;

import husacct.common.dto.DependencyDTO;
import husaccttest.analyse.benchmarkLeo.BenchmarkExtended;

public class ExceptionDependency extends BenchmarkExtended{

	private String rootDirectory = "exception";
	
	public void testAnotherClass(){
		String from = rootDirectory + ".apackage.AnotherClass";
		int expectedDependencies = 2;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);

		String fromImportExpected = from;
		String toImportExpected = rootDirectory + ".MyException";
		String typeImportExpected = super.IMPORT;
		int linenumberImportExpected = 3;
		
		String fromExceptionExpected = from;
		String toExceptionExpected = rootDirectory + ".MyException";
		String typeExceptionExpected = super.EXCEPTION;
		int linenumberExceptionExpected = 10;
		
		HashMap<String, Object> dependencyImportExpected = createDependencyHashmap(
				fromImportExpected, toImportExpected, typeImportExpected, linenumberImportExpected);
		HashMap<String, Object> dependencyExceptionExpected = createDependencyHashmap(
				fromExceptionExpected, toExceptionExpected, typeExceptionExpected, linenumberExceptionExpected);
		
		boolean foundImportDependency = compaireDTOWithValues(dependencyImportExpected, dependencies);
		boolean foundExceptionDependency = compaireDTOWithValues(dependencyExceptionExpected, dependencies);
		
		assertEquals(true, foundImportDependency);
		assertEquals(true, foundExceptionDependency);
	}
	
	public void testBnotherClass(){
		String from = rootDirectory + ".apackage.BnotherClass";
		int expectedDependencies = 1;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);

		String fromExceptionExpected = from;
		String toExceptionExpected = rootDirectory + ".MyException";
		String typeExceptionExpected = super.EXCEPTION;
		int linenumberExceptionExpected = 8;
		
		HashMap<String, Object> dependencyExceptionExpected = createDependencyHashmap(
				fromExceptionExpected, toExceptionExpected, typeExceptionExpected, linenumberExceptionExpected);
		
		boolean foundExceptionDependency = compaireDTOWithValues(dependencyExceptionExpected, dependencies);
		
		assertEquals(true, foundExceptionDependency);
	}
	
	public void testCnotherClass(){
		String from = rootDirectory + ".apackage.CnotherClass";
		int expectedDependencies = 2;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);

		String fromImportExpected = from;
		String toImportExpected = rootDirectory + "";
		String typeImportExpected = super.IMPORT;
		int linenumberImportExpected = 3;
		
		String fromExceptionExpected = from;
		String toExceptionExpected = rootDirectory + ".MyException";
		String typeExceptionExpected = super.EXCEPTION;
		int linenumberExceptionExpected = 7;
		
		HashMap<String, Object> dependencyImportExpected = createDependencyHashmap(
				fromImportExpected, toImportExpected, typeImportExpected, linenumberImportExpected);
		HashMap<String, Object> dependencyExceptionExpected = createDependencyHashmap(
				fromExceptionExpected, toExceptionExpected, typeExceptionExpected, linenumberExceptionExpected);
		
		boolean foundImportDependency = compaireDTOWithValues(dependencyImportExpected, dependencies);
		boolean foundExceptionDependency = compaireDTOWithValues(dependencyExceptionExpected, dependencies);
		
		assertEquals(true, foundImportDependency);
		assertEquals(true, foundExceptionDependency);
	}
	
	public void testMyClass(){
		String from = rootDirectory + ".apackage.MyClass";
		int expectedDependencies = 2;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);

		String fromImportExpected = from;
		String toImportExpected = rootDirectory + ".MyException";
		String typeImportExpected = super.IMPORT;
		int linenumberImportExpected = 3;
		
		String fromExceptionExpected = from;
		String toExceptionExpected = rootDirectory + ".MyException";
		String typeExceptionExpected = super.EXCEPTION;
		int linenumberExceptionExpected = 7;
		
		HashMap<String, Object> dependencyImportExpected = createDependencyHashmap(
				fromImportExpected, toImportExpected, typeImportExpected, linenumberImportExpected);
		HashMap<String, Object> dependencyExceptionExpected = createDependencyHashmap(
				fromExceptionExpected, toExceptionExpected, typeExceptionExpected, linenumberExceptionExpected);
		
		boolean foundImportDependency = compaireDTOWithValues(dependencyImportExpected, dependencies);
		boolean foundExceptionDependency = compaireDTOWithValues(dependencyExceptionExpected, dependencies);
		
		assertEquals(true, foundImportDependency);
		assertEquals(true, foundExceptionDependency);
	}
	
	public void testOtherClass(){
		String from = rootDirectory + ".apackage.OtherClass";
		int expectedDependencies = 1;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String fromExceptionExpected = from;
		String toExceptionExpected = rootDirectory + ".MyException";
		String typeExceptionExpected = super.EXCEPTION;
		int linenumberExceptionExpected = 5;
		
		HashMap<String, Object> dependencyExceptionExpected = createDependencyHashmap(
				fromExceptionExpected, toExceptionExpected, typeExceptionExpected, linenumberExceptionExpected);
		
		boolean foundExceptionDependency = compaireDTOWithValues(dependencyExceptionExpected, dependencies);
		
		assertEquals(true, foundExceptionDependency);
	}
	
	public void testbOtherclass(){
		String from = rootDirectory + ".bpackage.OtherClass";
		int expectedDependencies = 2;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);

		String fromImportExpected = from;
		String toImportExpected = rootDirectory + "";
		String typeImportExpected = super.IMPORT;
		int linenumberImportExpected = 3;
		
		String fromExceptionExpected = from;
		String toExceptionExpected = rootDirectory + ".MyException";
		String typeExceptionExpected = super.EXCEPTION;
		int linenumberExceptionExpected = 10;
		
		HashMap<String, Object> dependencyImportExpected = createDependencyHashmap(
				fromImportExpected, toImportExpected, typeImportExpected, linenumberImportExpected);
		HashMap<String, Object> dependencyExceptionExpected = createDependencyHashmap(
				fromExceptionExpected, toExceptionExpected, typeExceptionExpected, linenumberExceptionExpected);
		
		boolean foundImportDependency = compaireDTOWithValues(dependencyImportExpected, dependencies);
		boolean foundExceptionDependency = compaireDTOWithValues(dependencyExceptionExpected, dependencies);
		
		assertEquals(true, foundImportDependency);
		assertEquals(true, foundExceptionDependency);
	}
	
	public void testMyException(){
		String from = rootDirectory + ".MyException";
		int expectedDependencies = 1;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
//		String fromExceptionExpected = from;
//		String toExceptionExpected = "RuntimeException";
//		String typeExceptionExpected = super.EXTENDSCONCRETE;
//		int linenumberExceptionExpected = 3;
		
		String fromDeclarationExpected = from;
		String toDeclarationExpected = "long";
		String typeDeclarationExpected = super.DECLARATION;
		int linenumberDeclarationExpected = 5;
		
//		HashMap<String, Object> dependencyExceptionExpected = createDependencyHashmap(
//				fromExceptionExpected, toExceptionExpected, typeExceptionExpected, linenumberExceptionExpected);
		HashMap<String, Object> dependencyDeclarationExpected = createDependencyHashmap(
				fromDeclarationExpected, toDeclarationExpected, typeDeclarationExpected, linenumberDeclarationExpected);
		
//		boolean foundExceptionDependency = compaireDTOWithValues(dependencyExceptionExpected, dependencies);
		boolean foundDeclarationDependency = compaireDTOWithValues(dependencyDeclarationExpected, dependencies);
		
//		assertEquals(true, foundExceptionDependency);
		assertEquals(true, foundDeclarationDependency);
	}
	

}
