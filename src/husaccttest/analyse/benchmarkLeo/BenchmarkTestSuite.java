package husaccttest.analyse.benchmarkLeo;

import husaccttest.analyse.benchmarkLeo.domain.Blog;
import husaccttest.analyse.benchmarkLeo.domain.Facebook;
import husaccttest.analyse.benchmarkLeo.domain.Flickr;
import husaccttest.analyse.benchmarkLeo.domain.FoursquarealternativeDependencies;
import husaccttest.analyse.benchmarkLeo.domain.FoursquarealternativeNavigation;

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
