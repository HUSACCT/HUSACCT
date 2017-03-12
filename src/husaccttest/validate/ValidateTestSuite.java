package husaccttest.validate;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	ValidateTest.class, 
	ImportExportWorkspaceTest.class, 
	SACCandSRMAtest.class
})
public class ValidateTestSuite {
}