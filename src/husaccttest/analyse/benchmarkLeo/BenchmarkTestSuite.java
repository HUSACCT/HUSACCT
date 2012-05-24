package husaccttest.analyse.benchmarkLeo;

import husaccttest.analyse.benchmarkLeo.domain.Blog;
import husaccttest.analyse.benchmarkLeo.domain.Facebook;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	TestNavigation.class,
	Blog.class,
	Facebook.class,
})
public class BenchmarkTestSuite {

}
