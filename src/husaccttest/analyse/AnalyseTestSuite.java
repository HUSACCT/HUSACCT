package husaccttest.analyse;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ 
	TestLanguage.class,
	TestDependencyFilters.class,
	TestDomainDependencies.class,
	TestDomainModule.class
})
public class AnalyseTestSuite {

}
