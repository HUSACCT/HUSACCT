package husaccttest.analyse.java.blackbox;

import husacct.common.dto.DependencyDTO;
import husaccttest.analyse.TestCaseExtended;

import org.junit.Test;

public class TestIndirectDependency extends TestCaseExtended  {
	
	@Test
	public void testCheck(){
		DependencyDTO[] dependencies = service.getAllDependencies();
		
		printDependencies(dependencies);
		
		for(String s : service.getExternalSystems())
			System.out.println("External system: " + s);
	}
}
