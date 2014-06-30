package husaccttest.analyse.java.benchmark.domain;

import java.util.HashMap;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
import husacct.common.dto.DependencyDTO;
import husaccttest.analyse.java.TestObject;
import husaccttest.analyse.java.benchmark.BenchmarkExtended;



public class FoursquarealternativeKilroy extends BenchmarkExtended{

	@Test
	public void testDomainKilroyCheckCastFrom(){
		TestObject testobject = new TestObject("domain.foursquarealternative.kilroy.CheckCastFrom");
		testobject.addDependency(new DependencyDTO("", "", "domain.foursquarealternative.SameExtend", "domain.foursquarealternative.SameExtend", super.IMPORT, 3));
		testobject.addDependency(new DependencyDTO("", "", "domain.foursquarealternative.whrrl.CheckCastTo", "domain.foursquarealternative.whrrl.CheckCastTo", super.IMPORT, 4));
		testobject.addDependency(new DependencyDTO("", "", "domain.foursquarealternative.SameExtend", "domain.foursquarealternative.SameExtend", super.EXTENDSCONCRETE, 8));
		testobject.addDependency(new DependencyDTO("", "", "domain.foursquarealternative.SameExtend", "domain.foursquarealternative.SameExtend", super.INVOCCONSTRUCTOR, 11));
		boolean result = super.testDependencyObject(testobject);
		assertTrue(testobject.getLastError(), result);
	}
	
	@Test
	public void testDomainKilroyComment(){
		TestObject testobject = new TestObject("domain.foursquarealternative.kilroy.Comment");
		testobject.addDependency(new DependencyDTO("", "", "domain.foursquarealternative.whrrl.WhrrlComment", "domain.foursquarealternative.whrrl.WhrrlComment", super.IMPORT, 3));
		testobject.addDependency(new DependencyDTO("", "", "domain.foursquarealternative.whrrl.WhrrlComment", "domain.foursquarealternative.whrrl.WhrrlComment", super.EXTENDSCONCRETE, 7));
		boolean result = super.testDependencyObject(testobject);
		assertTrue(testobject.getLastError(), result);
	}
	
	@Test
	public void testDomainKilroyFuture(){
		TestObject testobject = new TestObject("domain.foursquarealternative.kilroy.Future");
		testobject.addDependency(new DependencyDTO("", "", "domain.foursquarealternative.whrrl.WhrrlFuture", "domain.foursquarealternative.whrrl.WhrrlFuture", super.IMPORT, 3));
		testobject.addDependency(new DependencyDTO("", "", "domain.foursquarealternative.whrrl.WhrrlFuture", "domain.foursquarealternative.whrrl.WhrrlFuture", super.INVOCCONSTRUCTOR, 9));
		testobject.addDependency(new DependencyDTO("", "", "domain.foursquarealternative.whrrl.WhrrlFuture", "domain.foursquarealternative.whrrl.WhrrlFuture", super.INVOCMETHOD, 9));
		boolean result = super.testDependencyObject(testobject);
		assertTrue(testobject.getLastError(), result);
	}
	
	@Ignore ("InvocMethod on an accesField not recognized")
	@Test
	public void testDomainKilroyMap(){
		String from = "domain.foursquarealternative.kilroy.Map";
		int expectedDependencies = 3;
	
//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String toImport1Expected = "domain.foursquarealternative.whrrl.WhrrlFuture";
		int linenumberImport1Expected = 3;
		
		String fromInvocConstructorExpected = from;
		String toInvocConstructorExpected = "domain.foursquarealternative.whrrl.WhrrlFuture";
		String typeInvocConstructorExpected = super.INVOCCONSTRUCTOR;
		int linenumberInvocConstructorExpected = 9;
		
		String fromInvocMethodExpected = from;
		String toInvocMethodExpected = "domain.foursquarealternative.whrrl.WhrrlFuture";
		String typeInvocMethodExpected = super.INVOCMETHOD;
		int linenumberInvocMethodExpected = 9;

		
		HashMap<String, Object> dependencyImport1Expected = super.createImportHashmap(from, toImport1Expected, linenumberImport1Expected);
		HashMap<String, Object> dependencyInvocConstructorExpected = createDependencyHashmap(
				fromInvocConstructorExpected, toInvocConstructorExpected, typeInvocConstructorExpected, linenumberInvocConstructorExpected);
		HashMap<String, Object> dependencyInvocMethodExpected = createDependencyHashmap(
				fromInvocMethodExpected, toInvocMethodExpected, typeInvocMethodExpected, linenumberInvocMethodExpected);

		boolean foundImport1Dependency = compaireDTOWithValues(dependencyImport1Expected, dependencies);
		boolean foundInvocConstructorDependency = compaireDTOWithValues(dependencyInvocConstructorExpected, dependencies);
		boolean foundInvocMethodDependency = compaireDTOWithValues(dependencyInvocMethodExpected, dependencies);
		
		assertEquals(true, foundImport1Dependency);
		assertEquals(true, foundInvocConstructorDependency);
		assertEquals(true, foundInvocMethodDependency);
	}
	
	@Test
	public void testDomainKilroySettings(){
		TestObject testobject = new TestObject("domain.foursquarealternative.kilroy.Settings");
		testobject.addDependency(new DependencyDTO("", "", "domain.foursquarealternative.whrrl.ISee", "domain.foursquarealternative.whrrl.ISee", super.IMPORT, 3));
		testobject.addDependency(new DependencyDTO("", "", "domain.foursquarealternative.whrrl.WhrrlSettings", "domain.foursquarealternative.whrrl.WhrrlSettings", super.IMPORT, 4));
		testobject.addDependency(new DependencyDTO("", "", "domain.foursquarealternative.whrrl.ISee", "domain.foursquarealternative.whrrl.ISee", super.IMPLEMENTS, 8));
		testobject.addDependency(new DependencyDTO("", "", "domain.foursquarealternative.whrrl.WhrrlSettings", "domain.foursquarealternative.whrrl.WhrrlSettings", super.INVOCCONSTRUCTOR, 10));
		boolean result = super.testDependencyObject(testobject);
		assertTrue(testobject.getLastError(), result);
	}
	
}
