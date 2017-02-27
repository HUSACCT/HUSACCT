package husaccttest.control;

import static org.junit.Assert.assertEquals;
import husacct.control.task.configuration.ConfigurationManager;
import husacct.control.task.configuration.IConfigListener;

import org.junit.Test;

public class ConfigurationManagerTest {

	@Test
	public void testConfiguration() {
		ConfigurationManager.setProperty("Test", "Test");
		ConfigurationManager.storeProperties();
		assertEquals(ConfigurationManager.getProperty("Test"), "Test");
	}
	
	public void testSingleSetConfiguration() {
		ConfigurationManager.setPropertyIfEmpty("Test", "Test");
		ConfigurationManager.setPropertyIfEmpty("Test", "Test2");
		assertEquals(ConfigurationManager.getProperty("Test"), "Test");
	}
	
	public void testConfigurationListener() {
		ConfigurationManager.addListener(() -> assertEquals(true, true));
		ConfigurationManager.notifyListeners();
	}
}
