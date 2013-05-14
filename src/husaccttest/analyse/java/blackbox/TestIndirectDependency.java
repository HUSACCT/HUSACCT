package husaccttest.analyse.java.blackbox;

import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ExternalSystemDTO;
import husaccttest.analyse.TestCaseExtended;

import org.junit.Test;

public class TestIndirectDependency extends TestCaseExtended  {
	
	@Test
	public void testCheck(){
		DependencyDTO[] dependencies = service.getAllDependencies();
		
		printDependencies(dependencies);
		
		for(ExternalSystemDTO dto : service.getExternalSystems())
			System.out.println("External system: " + dto.toString());
	}
}
