package husaccttest.analyse.java.recognition;

import static org.junit.Assert.*;
import husacct.common.dto.DependencyDTO;
import org.junit.Test;

public class ImplementsTest extends RecognationExtended{

	@Test
	public void testSamePackage(){
		boolean implementFound = false;
//		DependencyDTO[] dependencies = service.getDependenciesFrom("implement.a.SamePackage");
		DependencyDTO[] dependencies = super.getDependenciesFrom("implement.a.SamePackage");
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("Implements")){
				implementFound = true;
				assertEquals(3, dependency.lineNumber);
				assertEquals("implement.a.TheInterface", dependency.to);
			}
		}
		assertEquals(true, implementFound);
	}
	
	@Test
	public void testOtherPackageA(){
		boolean implementFound = false;
		boolean importFound = false;
//		DependencyDTO[] dependencies = service.getDependenciesFrom("implement.b.OtherPackageA");
		DependencyDTO[] dependencies = super.getDependenciesFrom("implement.b.OtherPackageA");
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("Implements")){
				implementFound = true;
				assertEquals(5, dependency.lineNumber);
				assertEquals("implement.a.TheInterface", dependency.to);
			}
			if(dependency.type.equals("Import")){
				importFound = true;
				assertEquals(3, dependency.lineNumber);
				assertEquals("implement.a.TheInterface", dependency.to);
			}
		}
		assertEquals(true, implementFound);
		assertEquals(true, importFound);
	}
	
	@Test
	public void testOtherPackageB(){
		boolean implementFound = false;
		boolean importFound = false;
//		DependencyDTO[] dependencies = service.getDependenciesFrom("implement.b.OtherPackageB");
		DependencyDTO[] dependencies = super.getDependenciesFrom("implement.b.OtherPackageB");
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("Implements")){
				implementFound = true;
				assertEquals(5, dependency.lineNumber);
				assertEquals("implement.a.TheInterface", dependency.to);
			}
			if(dependency.type.equals("Import")){
				importFound = true;
				assertEquals(3, dependency.lineNumber);
				assertEquals("implement.a", dependency.to);
			}
		}
		assertEquals(true, implementFound);
		assertEquals(true, importFound);
	}
	
	@Test
	public void testOtherPackageC(){
		boolean implementFound = false;
//		DependencyDTO[] dependencies = service.getDependenciesFrom("implement.b.OtherPackageC");
		DependencyDTO[] dependencies = super.getDependenciesFrom("implement.b.OtherPackageC");
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("Implements")){
				implementFound = true;
				assertEquals(3, dependency.lineNumber);
				assertEquals("implement.a.TheInterface", dependency.to);
			}
		}
		assertEquals(true, implementFound);
	}

}
