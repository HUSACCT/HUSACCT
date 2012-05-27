package husaccttest.analyse.javarecognationtest;

import static org.junit.Assert.*;
import husacct.common.dto.DependencyDTO;
import org.junit.Test;

public class ExtendsConcreteTest extends RecognationExtended{

	@Test
	public void testSamePackage(){
		boolean implementFound = false;
//		DependencyDTO[] dependencies = service.getDependenciesFrom("extendsconcrete.a.SamePackage");
		DependencyDTO[] dependencies = super.getDependenciesFrom("extendsconcrete.a.SamePackage");
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("ExtendsConcrete")){
				implementFound = true;
				assertEquals(3, dependency.lineNumber);
				assertEquals("extendsconcrete.a.ConcreteClass", dependency.to);
			}
		}
		assertEquals(true, implementFound);
	}
	
	@Test
	public void testOtherPackageA(){
		boolean implementFound = false;
		boolean importFound = false;
//		DependencyDTO[] dependencies = service.getDependenciesFrom("extendsconcrete.b.OtherPackageA");
		DependencyDTO[] dependencies = super.getDependenciesFrom("extendsconcrete.b.OtherPackageA");
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("ExtendsConcrete")){
				implementFound = true;
				assertEquals(5, dependency.lineNumber);
				assertEquals("extendsconcrete.a.ConcreteClass", dependency.to);
			}
			if(dependency.type.equals("Import")){
				importFound = true;
				assertEquals(3, dependency.lineNumber);
				assertEquals("extendsconcrete.a.ConcreteClass", dependency.to);
			}
		}
		assertEquals(true, implementFound);
		assertEquals(true, importFound);
	}
	
	@Test
	public void testOtherPackageB(){
		boolean implementFound = false;
		boolean importFound = false;
//		DependencyDTO[] dependencies = service.getDependenciesFrom("extendsconcrete.b.OtherPackageB");
		DependencyDTO[] dependencies = super.getDependenciesFrom("extendsconcrete.b.OtherPackageB");
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("ExtendsConcrete")){
				implementFound = true;
				assertEquals(5, dependency.lineNumber);
				assertEquals("extendsconcrete.a.ConcreteClass", dependency.to);
			}
			if(dependency.type.equals("Import")){
				importFound = true;
				assertEquals(3, dependency.lineNumber);
				assertEquals("extendsconcrete.a", dependency.to);
			}
		}
		assertEquals(true, implementFound);
		assertEquals(true, importFound);
	}
	
	@Test
	public void testOtherPackageC(){
		boolean implementFound = false;
//		DependencyDTO[] dependencies = service.getDependenciesFrom("extendsconcrete.b.OtherPackageC");
		DependencyDTO[] dependencies = super.getDependenciesFrom("extendsconcrete.b.OtherPackageC");
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("ExtendsConcrete")){
				implementFound = true;
				assertEquals(3, dependency.lineNumber);
				assertEquals("extendsconcrete.a.ConcreteClass", dependency.to);
			}
		}
		assertEquals(true, implementFound);
	}
}
