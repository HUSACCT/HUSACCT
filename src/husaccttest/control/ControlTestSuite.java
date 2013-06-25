package husaccttest.control;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ 
	LocaleControllerTest.class, 
	ServiceProviderTest.class,
	StateControllerTest.class,
	ImportExportControllerTest.class,
	WorkspaceControllerTest.class,
	ObservableServiceTest.class,
	ConfigurationManagerTest.class
})
public class ControlTestSuite {

}
