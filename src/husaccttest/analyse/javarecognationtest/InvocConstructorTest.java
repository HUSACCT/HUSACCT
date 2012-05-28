package husaccttest.analyse.javarecognationtest;

import static org.junit.Assert.*;

import org.junit.Test;

import husacct.common.dto.DependencyDTO;

public class InvocConstructorTest extends RecognationExtended{

	@Test
	public void testSamePackageA(){
		boolean invocConstructorFound = false;
//		DependencyDTO[] dependencies = service.getDependenciesFrom("invocconstructor.a.SamePackageA");
		DependencyDTO[] dependencies = super.getDependenciesFrom("invocconstructor.a.SamePackageA");
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				invocConstructorFound = true;
				assertEquals(5, dependency.lineNumber);
				assertEquals("invocconstructor.a.TheType", dependency.to);
			}

		}
		
		assertEquals(true, invocConstructorFound);
	}
	

	@Test
	public void testOtherPackageA(){
		boolean invocConstructorFound = false;
		boolean importFound = false;;
//		DependencyDTO[] dependencies = service.getDependenciesFrom("invocconstructor.b.OtherPackageA");
		DependencyDTO[] dependencies = super.getDependenciesFrom("invocconstructor.b.OtherPackageA");
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				invocConstructorFound = true;
				assertEquals(7, dependency.lineNumber);
				assertEquals("invocconstructor.a.TheType", dependency.to);
			}
			else if(dependency.type.equals("Import")){
				importFound = true;
				assertEquals(3, dependency.lineNumber);
				assertEquals("invocconstructor.a.TheType", dependency.to);
			}

		}
		
		assertEquals(true, invocConstructorFound);
		assertEquals(true, importFound);
	}
	
	@Test
	public void testOtherPackageB(){
		boolean invocConstructorFound = false;
		boolean importFound = false;;
//		DependencyDTO[] dependencies = service.getDependenciesFrom("invocconstructor.b.OtherPackageB");
		DependencyDTO[] dependencies = super.getDependenciesFrom("invocconstructor.b.OtherPackageB");
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				invocConstructorFound = true;
				assertEquals(7, dependency.lineNumber);
				assertEquals("invocconstructor.a.TheType", dependency.to);
			}
			else if(dependency.type.equals("Import")){
				importFound = true;
				assertEquals(3, dependency.lineNumber);
				assertEquals("invocconstructor.a", dependency.to);
			}

		}
		
		assertEquals(true, invocConstructorFound);
		assertEquals(true, importFound);
	}

	@Test
	public void testOtherPackageC(){
		boolean invocConstructorFound = false;
//		DependencyDTO[] dependencies = service.getDependenciesFrom("invocconstructor.b.OtherPackageC");
		DependencyDTO[] dependencies = super.getDependenciesFrom("invocconstructor.b.OtherPackageC");
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				invocConstructorFound = true;
				assertEquals(5, dependency.lineNumber);
				assertEquals("invocconstructor.a.TheType", dependency.to);
			}

		}
		
		assertEquals(true, invocConstructorFound);
	}
	

}
