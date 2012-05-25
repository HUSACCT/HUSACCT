package husaccttest.analyse.benchmark;

import husaccttest.analyse.benchmark.domain.Blog;
import husaccttest.analyse.benchmark.domain.Facebook;
import husaccttest.analyse.benchmark.domain.Flickr;
import husaccttest.analyse.benchmark.domain.FoursquarealternativeDependencies;
import husaccttest.analyse.benchmark.domain.FoursquarealternativeNavigation;

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
