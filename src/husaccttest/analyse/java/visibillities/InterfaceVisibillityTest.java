package husaccttest.analyse.java.visibillities;

import static org.junit.Assert.assertEquals;
import husacct.common.dto.AnalysedModuleDTO;

import org.junit.Test;

public class InterfaceVisibillityTest extends VisibillityTestExtended{
	
	@Test
	public void testInterfaceVisibillityPublic(){
		testModuleVisibility("application.InterfacePublic", PUBLIC);
	}
	
	@Test
	public void testInterfaceVisibillityDefault(){
		testModuleVisibility("application.InterfaceDefault", DEFAULT);
	}
	
	private void testModuleVisibility(String uniquename, String visibillity){
		AnalysedModuleDTO[] modules = service.getChildModulesInModule("application");
		for(AnalysedModuleDTO module : modules){
			if(module.uniqueName.equals(uniquename)){
				assertEquals(visibillity, module.visibility);
			}
		}
	}
}
