package husaccttest.analyse.benchmarkjava;

import husaccttest.analyse.benchmarkjava.domain.Blog;
import husaccttest.analyse.benchmarkjava.domain.Facebook;
import husaccttest.analyse.benchmarkjava.domain.Flickr;
import husaccttest.analyse.benchmarkjava.domain.FoursquarealternativeDependencies;
import husaccttest.analyse.benchmarkjava.domain.FoursquarealternativeNavigation;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	TestNavigation.class,
	Blog.class,
	Facebook.class,
	Flickr.class,
	FoursquarealternativeNavigation.class,
	FoursquarealternativeDependencies.class
})
public class BenchmarkTestSuite {

}
