package husaccttest.analyse.java.recognition;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	AccessFieldTest.class,
	InvocConstructorTest.class,
	InvocMethodTest.class,
	AnnotationTest.class,
	ImplementsTest.class,
	ExtendsAbstractTest.class,
	ExtendsConcreteTest.class
})
public class RecognationTestSuite {

}
