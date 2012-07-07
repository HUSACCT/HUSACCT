package husaccttest.analyse.csharp;

import husaccttest.analyse.csharp.benchmark.BenchmarkTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	BenchmarkTestSuite.class
})

public class CSharpTestSuite {
}
