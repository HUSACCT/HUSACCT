package husaccttest.analyse.java;

import husaccttest.analyse.java.benchmark.AccuracyTestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	AccuracyTestSuite.class
	//TestLanguage.class,
	//TestDependencyFilters.class,
	//TestDomainDependencies.class,
	//TestDomainModule.class,
	//TestIndirect.class,
	//RecognationTestSuite.class,
	//BenchmarkTestSuite.class,
	//VisibillityTestSuite.class
})

public class JavaTestSuite {

	
}
