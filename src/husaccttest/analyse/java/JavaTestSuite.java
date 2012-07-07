package husaccttest.analyse.java;

import husaccttest.analyse.java.benchmark.BenchmarkTestSuite;
import husaccttest.analyse.java.blackbox.TestDependencyFilters;
import husaccttest.analyse.java.blackbox.TestDomainDependencies;
import husaccttest.analyse.java.blackbox.TestDomainModule;
import husaccttest.analyse.java.blackbox.TestIndirect;
import husaccttest.analyse.java.blackbox.TestLanguage;
import husaccttest.analyse.java.recognation.RecognationTestSuite;
import husaccttest.analyse.java.visibillities.VisibillityTestSuite;

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
	BenchmarkTestSuite.class,
	VisibillityTestSuite.class
})

public class JavaTestSuite {

	
}
