package husaccttest.analyse;

import husaccttest.analyse.csharp.CSharpTestSuite;
import husaccttest.analyse.java.JavaTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	JavaTestSuite.class,
	CSharpTestSuite.class
})
public class AnalyseTestSuite {
}
