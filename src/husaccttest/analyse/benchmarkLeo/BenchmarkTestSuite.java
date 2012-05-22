package husaccttest.analyse.benchmarkLeo;

import husaccttest.analyse.benchmarkLeo.declarations.DeclarationDirectoryStructure;
import husaccttest.analyse.benchmarkLeo.declarations.DeclarationInnerPackage;
import husaccttest.analyse.benchmarkLeo.declarations.DeclarationOuterPackage;
import husaccttest.analyse.benchmarkLeo.domain.Blog;
import husaccttest.analyse.benchmarkLeo.domain.Facebook;
import husaccttest.analyse.benchmarkLeo.exception.ExceptionDirectoryStructure;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	TestNavigation.class,
	Blog.class,
	Facebook.class,
	DeclarationDirectoryStructure.class,
	DeclarationInnerPackage.class,
	DeclarationOuterPackage.class,
	ExceptionDirectoryStructure.class
})
public class BenchmarkTestSuite {

}
