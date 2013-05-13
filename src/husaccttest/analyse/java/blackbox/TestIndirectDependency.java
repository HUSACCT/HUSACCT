package husaccttest.analyse.java.blackbox;

import husacct.ServiceProvider;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ExternalSystemDTO;
import husacct.control.task.States;
import husaccttest.analyse.TestCaseExtended;

import org.junit.Test;

public class TestIndirectDependency extends TestCaseExtended  {
	
	@Test
	public void testCheck(){
		// important so it won't break during analysing.
		ServiceProvider.getInstance().getControlService().getState().add(States.ANALYSING);
		
		DependencyDTO[] dependencies = service.getAllDependencies();
		
		// reset the state of the analyser.
		ServiceProvider.getInstance().getControlService().getState().remove(States.ANALYSING);
		
		printDependencies(dependencies);
		
		for(ExternalSystemDTO dto : service.getExternalSystems())
			System.out.println("External system: " + dto.toString());
	}
}
