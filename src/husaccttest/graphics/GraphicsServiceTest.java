package husaccttest.graphics;

import javax.swing.JInternalFrame;

import org.junit.*;

import static org.junit.Assert.*;
import husacct.graphics.*;

public class GraphicsServiceTest {

    IGraphicsService service;

    @Before
    public void setUp() {
        service = (IGraphicsService) new GraphicsServiceImpl();
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
}
