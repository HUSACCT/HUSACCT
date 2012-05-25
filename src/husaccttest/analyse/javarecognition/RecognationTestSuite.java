package husaccttest.analyse.javarecognition;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	AccessFieldTest.class,
	InvocConstructorTest.class,
	InvocMethodTest.class
})
public class RecognationTestSuite {

}
