package husaccttest.control;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import husacct.ServiceProvider;
import husacct.common.Resource;
import husacct.control.ControlServiceImpl;
import husacct.control.domain.Workspace;
import husacct.control.task.MainController;
import husacct.control.task.WorkspaceController;
import husacct.control.task.resources.XmlResource;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WorkspaceControllerTest {

	WorkspaceController workspaceController;
	File testFile = new File("WorkspaceTestFile.xml");
	File validTestFile;
	
	@Before
	public void setup(){
		ServiceProvider serviceProvider = ServiceProvider.getInstance();
		serviceProvider.resetServices();
		ControlServiceImpl controlService = (ControlServiceImpl) serviceProvider.getControlService();
		MainController mainController = controlService.getMainController();
		workspaceController = mainController.getWorkspaceController();
		URL testFileURI = Resource.get(Resource.CONTROL_TEST_WORKSPACE);
		try {
			validTestFile = new File(testFileURI.toURI());
		} catch (URISyntaxException e) {
			System.out.println("Unable to load valid test file from resources");
		}
	}
	
	@After
	public void cleanup(){
		testFile.delete();
	}
	
	@Test
	public void testInitialWorkspace(){
		assertNull(workspaceController.getCurrentWorkspace());
	}
	
	@Test
	public void testNewWorkspace(){
		workspaceController.createWorkspace("JUnitTestWorkspace");
		assertNotNull(workspaceController.getCurrentWorkspace());
	}
	
	@Test
	public void testIsOpenWorkspace(){
		workspaceController.createWorkspace("JUnitTestWorkspace");
		assertTrue(workspaceController.isOpenWorkspace());
	}
	
	@Test
	public void testCloseWorkspace(){
		workspaceController.createWorkspace("JUnitTestWorkspace");
		workspaceController.closeWorkspace();
		assertNull(workspaceController.getCurrentWorkspace());
	}
	
	@Test
	public void testSetWorkspace(){
		Workspace workspace1 = new Workspace();
		workspaceController.setWorkspace(workspace1);
		Workspace workspace2 = workspaceController.getCurrentWorkspace();
		assertSame(workspace1, workspace2);
	}
	
	@Test
	public void testGetWorkspaceData(){
		Document doc = workspaceController.getWorkspaceData();
		assertNotNull(doc.getRootElement());
	}
	
	@Test
	public void testLoadWorkspaceData(){
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("file", validTestFile);
		XmlResource xmlResource = new XmlResource();
		Document doc1 = xmlResource.load(data);
		
		workspaceController.loadWorkspace("xml", data);
		Document doc2 = workspaceController.getWorkspaceData();
		
		Element doc1ControlServiceElement = doc1.getRootElement().getChild("husacct.control.ControlServiceImpl");
		Element doc2ControlServiceElement = doc2.getRootElement().getChild("husacct.control.ControlServiceImpl");
		
		XMLOutputter outputter = new XMLOutputter();
		outputter.setFormat(Format.getCompactFormat());
		
		String doc1String = outputter.outputString(doc1ControlServiceElement);
		String doc2String = outputter.outputString(doc2ControlServiceElement);
		
		assertEquals(doc1String.length(), doc2String.length());
	}
	
	@Test
	public void testSaveWorkspace(){
		HashMap<String, Object> data = new HashMap<String, Object>();
		HashMap<String, Object> config = new HashMap<String, Object>();
		data.put("file", testFile);
		workspaceController.saveWorkspace("xml", data,config);
		assertTrue(testFile.exists());
	}
	
	@Test
	public void testLoadWorkspace(){
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("file", validTestFile);
		workspaceController.loadWorkspace("xml", data);
		assertNotNull(workspaceController.getCurrentWorkspace());
		
	}
}
