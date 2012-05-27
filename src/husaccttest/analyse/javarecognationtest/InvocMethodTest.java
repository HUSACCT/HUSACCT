package husaccttest.analyse.javarecognationtest;

import static org.junit.Assert.*;

import org.junit.Test;

import husacct.common.dto.DependencyDTO;

public class InvocMethodTest extends RecognationExtended{

	@Test
	public void testSamePackageA(){
		boolean methodCallFound = false;
		boolean invocConstructorFound = false;
		DependencyDTO[] dependencies = service.getDependenciesFrom("invocmethod.a.SamePackageA");
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				invocConstructorFound = true;
				assertEquals(7, dependency.lineNumber);
				assertEquals("invocmethod.a.TheType", dependency.to);
			}
			else if(dependency.type.equals("InvocMethod")){
				methodCallFound = true;
				assertEquals(7, dependency.lineNumber);
				assertEquals("invocmethod.a.TheType", dependency.to);
			}
		}
		
		assertEquals(true, methodCallFound);
		assertEquals(true, invocConstructorFound);
	}
	
	@Test
	public void testSamePackageB(){
		boolean methodCallFound = false;
		boolean invocConstructorFound = false;
		DependencyDTO[] dependencies = service.getDependenciesFrom("invocmethod.a.SamePackageB");
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				invocConstructorFound = true;
				assertEquals(5, dependency.lineNumber);
				assertEquals("invocmethod.a.TheType", dependency.to);
			}
			else if(dependency.type.equals("InvocMethod")){
				methodCallFound = true;
				assertEquals(8, dependency.lineNumber);
				assertEquals("invocmethod.a.TheType", dependency.to);
			}
		}
		
		assertEquals(true, methodCallFound);
		assertEquals(true, invocConstructorFound);
	}
	
	@Test
	public void testSamePackageC(){
		boolean methodCallFound = false;
		boolean invocConstructorFound = false;
		DependencyDTO[] dependencies = service.getDependenciesFrom("invocmethod.a.SamePackageC");
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				invocConstructorFound = true;
				assertEquals(5, dependency.lineNumber);
				assertEquals("invocmethod.a.TheType", dependency.to);
			}
			else if(dependency.type.equals("InvocMethod")){
				methodCallFound = true;
				assertEquals(8, dependency.lineNumber);
				assertEquals("invocmethod.a.TheType", dependency.to);
			}
		}
		
		assertEquals(true, methodCallFound);
		assertEquals(true, invocConstructorFound);
	}
	
	@Test
	public void testSamePackageD(){
		boolean methodCallFound = false;
		boolean invocConstructorTheTypeFound = false;
		boolean invocConstructorGuiFound = false;
		DependencyDTO[] dependencies = service.getDependenciesFrom("invocmethod.a.SamePackageD");
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				if (dependency.to.equals("invocmethod.a.TheType")){
					invocConstructorTheTypeFound = true;
					assertEquals(5, dependency.lineNumber);
				}
				else if(dependency.to.equals("invocmethod.a.Gui")){
					invocConstructorGuiFound = true;
					assertEquals(8, dependency.lineNumber);
				}
			}
			else if(dependency.type.equals("InvocMethod")){
				methodCallFound = true;
				assertEquals(8, dependency.lineNumber);
				assertEquals("invocmethod.a.TheType", dependency.to);
			}
		}
		
		assertEquals(true, methodCallFound);
		assertEquals(true, invocConstructorTheTypeFound);
		assertEquals(true, invocConstructorGuiFound);
		
	}
	
	@Test
	public void testSamePackageE(){
		boolean methodCallFound = false;
		boolean invocConstructorTheTypeFound = false;
		boolean invocConstructorGuiFound = false;
		DependencyDTO[] dependencies = service.getDependenciesFrom("invocmethod.a.SamePackageE");
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				if (dependency.to.equals("invocmethod.a.TheType")){
					invocConstructorTheTypeFound = true;
					assertEquals(5, dependency.lineNumber);
				}
				else if(dependency.to.equals("invocmethod.a.Gui")){
					invocConstructorGuiFound = true;
					assertEquals(6, dependency.lineNumber);
				}
				
				
			}
			else if(dependency.type.equals("InvocMethod")){
				methodCallFound = true;
				assertEquals(9, dependency.lineNumber);
				assertEquals("invocmethod.a.TheType", dependency.to);
			}
		}
		
		assertEquals(true, methodCallFound);
		assertEquals(true, invocConstructorTheTypeFound);
		assertEquals(true, invocConstructorGuiFound);
		
	}
	
	@Test
	public void testSamePackageF(){
		boolean methodCallTheTypeFound = false;
		boolean methodCallGuiFound = false;
		
		boolean invocConstructorTheTypeFound = false;
		boolean invocConstructorGuiFound = false;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom("invocmethod.a.SamePackageF");
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				if (dependency.to.equals("invocmethod.a.TheType")){
					invocConstructorTheTypeFound = true;
					assertEquals(5, dependency.lineNumber);
				}
				else if(dependency.to.equals("invocmethod.a.Gui")){
					invocConstructorGuiFound = true;
					assertEquals(6, dependency.lineNumber);
				}
				
				
			}
			else if(dependency.type.equals("InvocMethod")){
				if (dependency.to.equals("invocmethod.a.TheType")){
					methodCallTheTypeFound = true;
					assertEquals(9, dependency.lineNumber);
				}
				else if(dependency.to.equals("invocmethod.a.Gui")){
					methodCallGuiFound = true;
					assertEquals(9, dependency.lineNumber);
				}
			}
		}
		
		assertEquals(true, methodCallTheTypeFound);
		assertEquals(true, methodCallGuiFound);
		assertEquals(true, invocConstructorTheTypeFound);
		assertEquals(true, invocConstructorGuiFound);
		
	}
	
	@Test
	public void testSamePackageG(){
		boolean methodCallTheTypeFound = false;
		boolean methodCallGuiFound = false;
		
		boolean invocConstructorTheTypeFound = false;
		boolean invocConstructorGuiFound = false;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom("invocmethod.a.SamePackageG");
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				if (dependency.to.equals("invocmethod.a.TheType")){
					invocConstructorTheTypeFound = true;
					assertEquals(5, dependency.lineNumber);
				}
				else if(dependency.to.equals("invocmethod.a.Gui")){
					invocConstructorGuiFound = true;
					assertEquals(8, dependency.lineNumber);
				}
			}
			else if(dependency.type.equals("InvocMethod")){
				if (dependency.to.equals("invocmethod.a.TheType")){
					methodCallTheTypeFound = true;
					assertEquals(8, dependency.lineNumber);
				}
				else if(dependency.to.equals("invocmethod.a.Gui")){
					methodCallGuiFound = true;
					assertEquals(8, dependency.lineNumber);
				}
			}
		}
		
		assertEquals(true, methodCallTheTypeFound);
		assertEquals(true, methodCallGuiFound);
		assertEquals(true, invocConstructorTheTypeFound);
		assertEquals(true, invocConstructorGuiFound);
		
	}
	
	
	@Test
	public void testSamePackageH(){
		boolean methodCallTheTypeFound = false;
		boolean methodCallGuiFound = false;
		
		boolean invocConstructorTheTypeFound = false;
		boolean invocConstructorGuiFound = false;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom("invocmethod.a.SamePackageH");
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				if (dependency.to.equals("invocmethod.a.TheType")){
					invocConstructorTheTypeFound = true;
					assertEquals(6, dependency.lineNumber);
				}
				else if(dependency.to.equals("invocmethod.a.Gui")){
					invocConstructorGuiFound = true;
					assertEquals(6, dependency.lineNumber);
				}
			}
			else if(dependency.type.equals("InvocMethod")){
				if (dependency.to.equals("invocmethod.a.TheType")){
					methodCallTheTypeFound = true;
					assertEquals(6, dependency.lineNumber);
				}
				else if(dependency.to.equals("invocmethod.a.Gui")){
					methodCallGuiFound = true;
					assertEquals(6, dependency.lineNumber);
				}
			}
		}
		
		assertEquals(true, methodCallTheTypeFound);
		assertEquals(true, methodCallGuiFound);
		assertEquals(true, invocConstructorTheTypeFound);
		assertEquals(true, invocConstructorGuiFound);
		
	}
	
	
	
	@Test
	public void testSamePackageI(){
		boolean methodCallTheTypeFound = false;
		
		boolean invocConstructorTheTypeFound = false;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom("invocmethod.a.SamePackageI");
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				invocConstructorTheTypeFound = true;
				assertEquals(5, dependency.lineNumber);
				assertEquals("invocmethod.a.TheType", dependency.to);
			}
			else if(dependency.type.equals("InvocMethod")){
				methodCallTheTypeFound = true;
				assertEquals(8, dependency.lineNumber);
				assertEquals("invocmethod.a.TheType", dependency.to);
			}
		}
		
		assertEquals(true, methodCallTheTypeFound);
		assertEquals(true, invocConstructorTheTypeFound);
		
	}
	
	@Test
	public void testOtherPackageA(){
		boolean methodCallFound = false;
		boolean invocConstructorFound = false;
		boolean importFound = false;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom("invocmethod.b.OtherPackageA");
		
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				invocConstructorFound = true;
				assertEquals(9, dependency.lineNumber);
				assertEquals("invocmethod.a.TheType", dependency.to);
			}
			else if(dependency.type.equals("InvocMethod")){
				methodCallFound = true;
				assertEquals(9, dependency.lineNumber);
				assertEquals("invocmethod.a.TheType", dependency.to);
			}
			else if(dependency.type.equals("Import")){
				importFound = true;
				assertEquals(3, dependency.lineNumber);
				assertEquals("invocmethod.a.TheType", dependency.to);
			}
		}
		
		assertEquals(true, methodCallFound);
		assertEquals(true, invocConstructorFound);
		assertEquals(true, importFound);
	}
	
	@Test
	public void testOtherPackageB(){
		boolean methodCallFound = false;
		boolean invocConstructorFound = false;
		boolean importFound = false;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom("invocmethod.b.OtherPackageB");
		
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				invocConstructorFound = true;
				assertEquals(9, dependency.lineNumber);
				assertEquals("invocmethod.a.TheType", dependency.to);
			}
			else if(dependency.type.equals("InvocMethod")){
				methodCallFound = true;
				assertEquals(9, dependency.lineNumber);
				assertEquals("invocmethod.a.TheType", dependency.to);
			}
			else if(dependency.type.equals("Import")){
				importFound = true;
				assertEquals(3, dependency.lineNumber);
				assertEquals("invocmethod.a", dependency.to);
			}
		}
		
		assertEquals(true, methodCallFound);
		assertEquals(true, invocConstructorFound);
		assertEquals(true, importFound);
	}

	@Test
	public void testOtherPackageC(){
		boolean methodCallFound = false;
		boolean invocConstructorFound = false;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom("invocmethod.b.OtherPackageC");
		
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				invocConstructorFound = true;
				assertEquals("invocmethod.a.TheType", dependency.to);
				assertEquals(7, dependency.lineNumber);
			}
			else if(dependency.type.equals("InvocMethod")){
				methodCallFound = true;
				assertEquals(7, dependency.lineNumber);
				assertEquals("invocmethod.a.TheType", dependency.to);
			}
		}
		
		assertEquals(true, methodCallFound);
		assertEquals(true, invocConstructorFound);
		
	}
	
	@Test
	public void testOtherPackageD(){
		boolean methodCallFound = false;
		boolean invocConstructorFound = false;
		boolean importFound = false;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom("invocmethod.b.OtherPackageD");
		
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				invocConstructorFound = true;
				assertEquals(7, dependency.lineNumber);
				assertEquals("invocmethod.a.TheType", dependency.to);
			}
			else if(dependency.type.equals("InvocMethod")){
				methodCallFound = true;
				assertEquals(10, dependency.lineNumber);
				assertEquals("invocmethod.a.TheType", dependency.to);
			}
			else if(dependency.type.equals("Import")){
				importFound = true;
				assertEquals(3, dependency.lineNumber);
				assertEquals("invocmethod.a.TheType", dependency.to);
			}
		}
		
		assertEquals(true, methodCallFound);
		assertEquals(true, invocConstructorFound);
		assertEquals(true, importFound);
		
	}
	
	@Test
	public void testOtherPackageE(){
		boolean methodCallFound = false;
		boolean invocConstructorFound = false;
		boolean importFound = false;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom("invocmethod.b.OtherPackageE");
		
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				invocConstructorFound = true;
				assertEquals(7, dependency.lineNumber);
				assertEquals("invocmethod.a.TheType", dependency.to);
			}
			else if(dependency.type.equals("InvocMethod")){
				methodCallFound = true;
				assertEquals(10, dependency.lineNumber);
				assertEquals("invocmethod.a.TheType", dependency.to);
			}
			else if(dependency.type.equals("Import")){
				importFound = true;
				assertEquals(3, dependency.lineNumber);
				assertEquals("invocmethod.a", dependency.to);
			}
		}
		
		assertEquals(true, methodCallFound);
		assertEquals(true, invocConstructorFound);
		assertEquals(true, importFound);
		
	}
	
	@Test
	public void testOtherPackageF(){
		boolean methodCallFound = false;
		boolean invocConstructorFound = false;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom("invocmethod.b.OtherPackageF");
		
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("InvocConstructor")){
				invocConstructorFound = true;
				assertEquals(5, dependency.lineNumber);
				assertEquals("invocmethod.a.TheType", dependency.to);
			}
			else if(dependency.type.equals("InvocMethod")){
				methodCallFound = true;
				assertEquals(8, dependency.lineNumber);
				assertEquals("invocmethod.a.TheType", dependency.to);
			}
		}
		
		assertEquals(true, methodCallFound);
		assertEquals(true, invocConstructorFound);
		
	}
}
