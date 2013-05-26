package husaccttest;

import husaccttest.analyse.AnalyseTestSuite;
import husaccttest.control.ControlTestSuite;
import husaccttest.graphics.GraphicsTestSuite;
import husaccttest.validate.ValidateTestSuite;

import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ 
	AnalyseTestSuite.class, 
	ControlTestSuite.class,
	DefineTestSuite.class, 		//TODO: Responsible person, please fix this
	GraphicsTestSuite.class,
	ValidateTestSuite.class
})
public class TestAll {
	@Before
	public void prepareLog4J(){
		BasicConfigurator.configure();
	}
}