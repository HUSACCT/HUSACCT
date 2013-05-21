package husaccttest.analyse.java.blackbox;

import static org.junit.Assert.*;

import org.junit.Test;

import husacct.common.dto.ExternalSystemDTO;
import husaccttest.analyse.TestCaseExtended;

public class TestExternalSystems extends TestCaseExtended {
	@Test
	public void ExternalSystemDetection(){
		ExternalSystemDTO[] externalSystems = service.getExternalSystems();
		
		assertEquals(externalSystems.length, 1);
		assertEquals(externalSystems[0].systemName, "Calendar");
		assertEquals(externalSystems[0].systemPackage, "System.Date.Calendar");
		assertEquals(externalSystems[0].fromDependencies.size(), 1);	
	}
}
