package husaccttest.analyse.javarecognationtest;

import static org.junit.Assert.*;
import husacct.common.dto.DependencyDTO;
import org.junit.Test;

public class ExtendsAbstractTest extends RecognationExtended{

	@Test
	public void testSamePackage(){
		boolean implementFound = false;
		DependencyDTO[] dependencies = service.getDependenciesFrom("extendsabstract.a.SamePackage");
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("ExtendsAbstract")){
				implementFound = true;
				assertEquals(3, dependency.lineNumber);
				assertEquals("extendsabstract.a.AbstractClass", dependency.to);
			}
		}
		assertEquals(true, implementFound);
	}
	
	@Test
	public void testOtherPackageA(){
		boolean implementFound = false;
		boolean importFound = false;
		DependencyDTO[] dependencies = service.getDependenciesFrom("extendsabstract.b.OtherPackageA");
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("ExtendsAbstract")){
				implementFound = true;
				assertEquals(5, dependency.lineNumber);
				assertEquals("extendsabstract.a.AbstractClass", dependency.to);
			}
			if(dependency.type.equals("Import")){
				importFound = true;
				assertEquals(3, dependency.lineNumber);
				assertEquals("extendsabstract.a.AbstractClass", dependency.to);
			}
		}
		assertEquals(true, implementFound);
		assertEquals(true, importFound);
	}
	
	@Test
	public void testOtherPackageB(){
		boolean implementFound = false;
		boolean importFound = false;
		DependencyDTO[] dependencies = service.getDependenciesFrom("extendsabstract.b.OtherPackageB");
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("ExtendsAbstract")){
				implementFound = true;
				assertEquals(5, dependency.lineNumber);
				assertEquals("extendsabstract.a.AbstractClass", dependency.to);
			}
			if(dependency.type.equals("Import")){
				importFound = true;
				assertEquals(3, dependency.lineNumber);
				assertEquals("extendsabstract.a", dependency.to);
			}
		}
		assertEquals(true, implementFound);
		assertEquals(true, importFound);
	}
	
	@Test
	public void testOtherPackageC(){
		boolean implementFound = false;
		DependencyDTO[] dependencies = service.getDependenciesFrom("extendsabstract.b.OtherPackageC");
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("ExtendsAbstract")){
				implementFound = true;
				assertEquals(3, dependency.lineNumber);
				assertEquals("extendsabstract.a.AbstractClass", dependency.to);
			}
		}
		assertEquals(true, implementFound);
	}
}
