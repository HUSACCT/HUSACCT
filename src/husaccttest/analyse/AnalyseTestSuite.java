package husaccttest.analyse;

import husaccttest.analyse.benchmarkjava.BenchmarkTestSuite;
import husaccttest.analyse.blackbox.TestDependencyFilters;
import husaccttest.analyse.blackbox.TestDomainDependencies;
import husaccttest.analyse.blackbox.TestDomainModule;
import husaccttest.analyse.blackbox.TestIndirect;
import husaccttest.analyse.blackbox.TestLanguage;
import husaccttest.analyse.javarecognationtest.RecognationTestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	TestLanguage.class,
	TestDependencyFilters.class,
	TestDomainDependencies.class,
	TestDomainModule.class,
	TestIndirect.class,
	RecognationTestSuite.class,
	BenchmarkTestSuite.class
})
public class AnalyseTestSuite {
}
