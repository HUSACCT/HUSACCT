package husaccttest.analyse;

import husaccttest.analyse.javarecognationtest.RecognationTestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ 
	TestLanguage.class,
	TestDependencyFilters.class,
	TestDomainDependencies.class,
	TestDomainModule.class,
	TestIndirect.class,
	RecognationTestSuite.class
})
public class AnalyseTestSuite {

}
