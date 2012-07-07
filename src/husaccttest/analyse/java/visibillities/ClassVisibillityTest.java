package husaccttest.analyse.java.visibillities;

import static org.junit.Assert.assertEquals;
import husacct.common.dto.AnalysedModuleDTO;

import org.junit.Test;

public class ClassVisibillityTest extends VisibillityTestExtended{

	@Test
	public void testClassDefault(){
		checkVisibility("application.ClassDefault", DEFAULT);
	}
	
	@Test
	public void testClassPublic(){
		checkVisibility("application.ClassPublic", PUBLIC);
	}
	
	@Test
	public void testInnerClassVisibilityPrivate(){
		checkVisibility("application.ClassPublic.PrivateInnerClass", PRIVATE);
	}
	
	@Test
	public void testInnerClassVisibilityDefault(){
		checkVisibility("application.ClassPublic.DefaultInnerClass", DEFAULT);
	}
	
	@Test
	public void testInnerClassVisibilityPublic(){
		checkVisibility("application.ClassPublic.PublicInnerClass", PUBLIC);
	}
	
	private void checkVisibility(String uniquename, String visbility){
		AnalysedModuleDTO module = service.getModuleForUniqueName(uniquename);
		if(module.uniqueName.equals(uniquename)){
			assertEquals(visbility, module.visibility);
		}
		else{
			System.out.println("Module " + uniquename + " not found in test");
		}
	}
}
