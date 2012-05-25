package husaccttest.analyse.javarecognition;

import static org.junit.Assert.*;

import org.junit.Test;

import husacct.common.dto.DependencyDTO;

public class InvocMethodTest extends RecognationExtended{

	@Test
	public void testSamePackageA(){
		boolean accessPropertyOrFieldFound = false;
		boolean invocConstructorFound = false;
		DependencyDTO[] dependencies = service.getDependenciesFrom("husaccttest.analyse.javarecognition.testapplication.invocconstructor.a.SamePackageA");
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				invocConstructorFound = true;
				assertEquals(7, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.invocconstructor.a.TheType", dependency.to);
			}
			else if(dependency.type.equals("InvocMethod")){
				accessPropertyOrFieldFound = true;
				assertEquals(7, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.invocconstructor.a.TheType", dependency.to);
			}
		}
		
		assertEquals(true, accessPropertyOrFieldFound);
		assertEquals(true, invocConstructorFound);
	}
	
	@Test
	public void testSamePackageB(){
		boolean accessPropertyOrFieldFound = false;
		boolean invocConstructorFound = false;
		DependencyDTO[] dependencies = service.getDependenciesFrom("husaccttest.analyse.javarecognition.testapplication.invocconstructor.a.SamePackageB");
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				invocConstructorFound = true;
				assertEquals(5, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.invocconstructor.a.TheType", dependency.to);
			}
			else if(dependency.type.equals("InvocMethod")){
				accessPropertyOrFieldFound = true;
				assertEquals(8, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.invocconstructor.a.TheType", dependency.to);
			}
		}
		
		assertEquals(true, accessPropertyOrFieldFound);
		assertEquals(true, invocConstructorFound);
	}
	
	@Test
	public void testSamePackageC(){
		boolean accessPropertyOrFieldFound = false;
		boolean invocConstructorFound = false;
		DependencyDTO[] dependencies = service.getDependenciesFrom("husaccttest.analyse.javarecognition.testapplication.invocconstructor.a.SamePackageC");
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				invocConstructorFound = true;
				assertEquals(5, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.invocconstructor.a.TheType", dependency.to);
			}
			else if(dependency.type.equals("InvocMethod")){
				accessPropertyOrFieldFound = true;
				assertEquals(8, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.invocconstructor.a.TheType", dependency.to);
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
		
		DependencyDTO[] dependencies = service.getDependenciesFrom("husaccttest.analyse.javarecognition.testapplication.invocconstructor.b.OtherPackageA");
		
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				invocConstructorFound = true;
				assertEquals(9, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.invocmethod.a.TheType", dependency.to);
			}
			else if(dependency.type.equals("InvocMethod")){
				accessPropertyOrFieldFound = true;
				assertEquals(9, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.invocconstructor.a.TheType", dependency.to);
			}
			else if(dependency.type.equals("Import")){
				importFound = true;
				assertEquals(3, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.invocconstructor.a.TheType", dependency.to);
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
		
		DependencyDTO[] dependencies = service.getDependenciesFrom("husaccttest.analyse.javarecognition.testapplication.invocconstructor.b.OtherPackageB");
		
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				invocConstructorFound = true;
				assertEquals(9, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.invocconstructor.a.TheType", dependency.to);
			}
			else if(dependency.type.equals("InvocMethod")){
				accessPropertyOrFieldFound = true;
				assertEquals(9, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.invocconstructor.a.TheType", dependency.to);
			}
			else if(dependency.type.equals("Import")){
				importFound = true;
				assertEquals(3, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.invocconstructor.a", dependency.to);
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
		
		DependencyDTO[] dependencies = service.getDependenciesFrom("husaccttest.analyse.javarecognition.testapplication.invocconstructor.b.OtherPackageC");
		
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				invocConstructorFound = true;
				assertEquals("husaccttest.analyse.javarecognition.testapplication.invocconstructor.a.TheType", dependency.to);
				assertEquals(7, dependency.lineNumber);
			}
			else if(dependency.type.equals("InvocMethod")){
				accessPropertyOrFieldFound = true;
				assertEquals(7, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.invocconstructor.a.TheType", dependency.to);
			}
		}
		
		assertEquals(true, accessPropertyOrFieldFound);
		assertEquals(true, invocConstructorFound);
		
	}
	
	@Test
	public void testOtherPackageD(){
		boolean accessPropertyOrFieldFound = false;
		boolean invocConstructorFound = false;
		boolean importFound = false;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom("husaccttest.analyse.javarecognition.testapplication.invocconstructor.b.OtherPackageD");
		
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				invocConstructorFound = true;
				assertEquals(7, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.invocconstructor.a.TheType", dependency.to);
			}
			else if(dependency.type.equals("InvocMethod")){
				accessPropertyOrFieldFound = true;
				assertEquals(10, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.invocconstructor.a.TheType", dependency.to);
			}
			else if(dependency.type.equals("Import")){
				importFound = true;
				assertEquals(3, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.invocconstructor.a.TheType", dependency.to);
			}
		}
		
		assertEquals(true, accessPropertyOrFieldFound);
		assertEquals(true, invocConstructorFound);
		assertEquals(true, importFound);
		
	}
	
	@Test
	public void testOtherPackageE(){
		boolean accessPropertyOrFieldFound = false;
		boolean invocConstructorFound = false;
		boolean importFound = false;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom("husaccttest.analyse.javarecognition.testapplication.invocmethod.b.OtherPackageE");
		
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				invocConstructorFound = true;
				assertEquals(7, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.invocconstructor.a.TheType", dependency.to);
			}
			else if(dependency.type.equals("InvocMethod")){
				accessPropertyOrFieldFound = true;
				assertEquals(10, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.invocconstructor.a.TheType", dependency.to);
			}
			else if(dependency.type.equals("Import")){
				importFound = true;
				assertEquals(3, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.invocconstructor.a", dependency.to);
			}
		}
		
		assertEquals(true, accessPropertyOrFieldFound);
		assertEquals(true, invocConstructorFound);
		assertEquals(true, importFound);
		
	}
	
	@Test
	public void testOtherPackageF(){
		boolean accessPropertyOrFieldFound = false;
		boolean invocConstructorFound = false;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom("husaccttest.analyse.javarecognition.testapplication.invocconstructor.b.OtherPackageF");
		
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				invocConstructorFound = true;
				assertEquals(5, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.invocconstructor.a.TheType", dependency.to);
			}
			else if(dependency.type.equals("InvocMethod")){
				accessPropertyOrFieldFound = true;
				assertEquals(8, dependency.lineNumber);
				assertEquals("husaccttest.analyse.javarecognition.testapplication.invocconstructor.a.TheType", dependency.to);
			}
		}
		
		assertEquals(true, accessPropertyOrFieldFound);
		assertEquals(true, invocConstructorFound);
		
	}
}
