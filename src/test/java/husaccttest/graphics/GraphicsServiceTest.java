package husaccttest.graphics;

import java.net.URL;

import javax.swing.JInternalFrame;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.*;

import static org.junit.Assert.*;
import husacct.graphics.*;
import husaccttest.define.DefineSarServicesTest_SRMA;

public class GraphicsServiceTest {
	IGraphicsService service;
	private static Logger logger;

	@Before
	public void setUp() {
		setLog4jConfiguration();
		logger.info(String.format("Start: GraphicsServiceTest"));
		service = new GraphicsServiceImpl();
	}

	@Test
	public void testGetAnalysedArchitecture() {
		JInternalFrame panel = service.getAnalysedArchitectureGUI();

		assertNotNull(panel);
		assertTrue(panel instanceof JInternalFrame);
	}

	@Test
	public void testGetDefinedArchitecture() {
		JInternalFrame panel = service.getDefinedArchitectureGUI();

		assertNotNull(panel);
		assertTrue(panel instanceof JInternalFrame);
	}

	@AfterClass
	public static void tearDown(){
		logger.info(String.format("Finished: GraphicsServiceTest"));
	}

	//
	//private helpers
	//
	private static void setLog4jConfiguration() {
		URL propertiesFile = Class.class.getResource("log4j.properties");
		PropertyConfigurator.configure(propertiesFile);
		logger = Logger.getLogger(DefineSarServicesTest_SRMA.class);
	}
	
}
