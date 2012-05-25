package husaccttest.analyse.recognitiontest;

import husaccttest.analyse.benchmarkjava.domain.Blog;
import husaccttest.analyse.benchmarkjava.domain.Facebook;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	AccessFieldTest.class,
	InvocConstructorTest.class,
	InvocMethodTest.class,
	AnnotationTest.class
})
public class RecognationTestSuite {

}
