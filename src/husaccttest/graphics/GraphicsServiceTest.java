package husaccttest.graphics;

import org.junit.*;

import static org.junit.Assert.*;
import husacct.graphics.*;

public class GraphicsServiceTest {

	GraphicsServiceImpl service;
	
	@Before
	public void setUp() {
		service = (GraphicsServiceImpl) new GraphicsServiceImpl();
	}
	
	@Test
	public void testGetAnalysedArchitecture() {
		javax.swing.JInternalFrame panel = service.getAnalysedArchitectureGUI();
		
		assertNotNull(panel);
		assertTrue(panel instanceof javax.swing.JInternalFrame);
	}
	
	@Test
	public void testGetDefinedArchitecture() {
		javax.swing.JInternalFrame panel = service.getDefinedArchitectureGUI();
		
		assertNotNull(panel);
		assertTrue(panel instanceof javax.swing.JInternalFrame);
	}

}
