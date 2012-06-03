package husaccttest.analyse.java.benchmark;

import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husaccttest.analyse.java.benchmark.domain.*;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	TestNavigation.class,
	Blog.class,
	Facebook.class,
	Flickr.class,
	FoursquarealternativeNavigation.class,
	FoursquarealternativeBrightkite.class,
	FoursquarealternativeKilroy.class,
	FoursquarealternativeWhrrl.class,
	FoursquarealternativeYelp.class,
	Google_plus.class,
	Gowalla.class,
	Hyves.class,
	LanguageNavigation.class,
	LanguageBabbel.class,
	LanguageBusuu.class,
	LastFM.class
})
public class BenchmarkTestSuite {	
}
