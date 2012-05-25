package husaccttest.analyse.javarecognition;

import static org.junit.Assert.*;

import org.junit.Test;

import husacct.common.dto.DependencyDTO;

public class AccessFieldTest extends RecognationExtended{

	@Test
	public void testSamePackageA(){
		boolean accessPropertyOrFieldFound = false;
		boolean invocConstructorFound = false;
		DependencyDTO[] dependencies = service.getDependenciesFrom("husaccttest.analyse.javarecognition.testapplication.accessfield.a.SamePackageA");
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				invocConstructorFound = true;
				assertEquals(5, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.accessfield.a.TheOwner", dependency.to);
			}
			else if(dependency.type.equals("AccessPropertyOrField")){
				accessPropertyOrFieldFound = true;
				assertEquals(8, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.accessfield.a.TheOwner", dependency.to);
			}
		}
		
		assertEquals(true, accessPropertyOrFieldFound);
		assertEquals(true, invocConstructorFound);
	}
	
	@Test
	public void testSamePackageB(){
		boolean accessPropertyOrFieldFound = false;
		boolean invocConstructorFound = false;
		DependencyDTO[] dependencies = service.getDependenciesFrom("husaccttest.analyse.javarecognition.testapplication.accessfield.a.SamePackageB");
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				invocConstructorFound = true;
				assertEquals(5, dependency.lineNumber);
				assertEquals("accessfield.a.TheOwner", dependency.to);
			}
			else if(dependency.type.equals("AccessPropertyOrField")){
				accessPropertyOrFieldFound = true;
				assertEquals(8, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.accessfield.a.TheOwner", dependency.to);
			}
		}
		
		assertEquals(true, accessPropertyOrFieldFound);
		assertEquals(true, invocConstructorFound);
	}
	
	@Test
	public void testOtherPackageA(){
		boolean accessPropertyOrFieldFound = false;
		boolean invocConstructorFound = false;
		boolean importFound = false;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom("husaccttest.analyse.javarecognition.testapplication.accessfield.b.OtherPackageA");
		
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				invocConstructorFound = true;
				assertEquals(7, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.accessfield.a.TheOwner", dependency.to);
			}
			else if(dependency.type.equals("AccessPropertyOrField")){
				accessPropertyOrFieldFound = true;
				assertEquals(10, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.accessfield.a.TheOwner", dependency.to);
			}
			else if(dependency.type.equals("Import")){
				importFound = true;
				assertEquals(3, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.accessfield.a.TheOwner", dependency.to);
			}
		}
		
		assertEquals(true, accessPropertyOrFieldFound);
		assertEquals(true, invocConstructorFound);
		assertEquals(true, importFound);
	}
	
	@Test
	public void testOtherPackageB(){
		boolean accessPropertyOrFieldFound = false;
		boolean invocConstructorFound = false;
		boolean importFound = false;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom("husaccttest.analyse.javarecognition.testapplication.accessfield.b.OtherPackageB");
		
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				invocConstructorFound = true;
				assertEquals(7, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.accessfield.a.TheOwner", dependency.to);
			}
			else if(dependency.type.equals("AccessPropertyOrField")){
				accessPropertyOrFieldFound = true;
				assertEquals(10, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.accessfield.a.TheOwner", dependency.to);
			}
			else if(dependency.type.equals("Import")){
				importFound = true;
				assertEquals(3, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.accessfield.a", dependency.to);
			}
		}
		
		assertEquals(true, accessPropertyOrFieldFound);
		assertEquals(true, invocConstructorFound);
		assertEquals(true, importFound);
	}

	@Test
	public void testOtherPackageC(){
		boolean accessPropertyOrFieldFound = false;
		boolean invocConstructorFound = false;
	
		
		DependencyDTO[] dependencies = service.getDependenciesFrom("husaccttest.analyse.javarecognition.testapplication.accessfield.b.OtherPackageC");
		
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				invocConstructorFound = true;
				assertEquals(6, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.accessfield.a.TheOwner", dependency.to);
			}
			else if(dependency.type.equals("AccessPropertyOrField")){
				accessPropertyOrFieldFound = true;
				assertEquals(6, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.accessfield.a.TheOwner", dependency.to);
			}
		}
		
		assertEquals(true, accessPropertyOrFieldFound);
		assertEquals(true, invocConstructorFound);
		
	}
	
	@Test
	public void testOtherPackageD(){
		boolean accessPropertyOrFieldFound = false;
		boolean invocConstructorFound = false;
	
		
		DependencyDTO[] dependencies = service.getDependenciesFrom("husaccttest.analyse.javarecognition.testapplication.accessfield.b.OtherPackageD");
		
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				invocConstructorFound = true;
				assertEquals(6, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.accessfield.a.TheOwner", dependency.to);
			}
			else if(dependency.type.equals("AccessPropertyOrField")){
				accessPropertyOrFieldFound = true;
				assertEquals(6, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.accessfield.a.TheOwner", dependency.to);
			}
		}
		
		assertEquals(true, accessPropertyOrFieldFound);
		assertEquals(true, invocConstructorFound);
		
	}
}
