package husaccttest.control;

import static org.junit.Assert.assertTrue;
import husacct.ServiceProvider;
import husacct.control.task.ImportExportController;
import husacct.control.task.MainController;

import java.io.File;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ImportExportControllerTest {
	
	ImportExportController importExportController;
	File testFile = new File("importExportTestFile.xml");
	
	@Before
	public void setup(){
		MainController mainController = new MainController(new String[]{"nogui"});
		importExportController = mainController.getImportExportController();
	}
	
	@After
	public void cleanUp(){
		testFile.delete();
	}
	
	@Test
	public void testExport(){
		importExportController.exportLogicalArchitecture(testFile);
		assertTrue(testFile.exists());
	}
	
	@Test
	public void testImport(){
		importExportController.exportLogicalArchitecture(testFile);
		importExportController.importLogicalArchitecture(testFile);
		SAXBuilder sax = new SAXBuilder();
		Document testDoc = new Document();
		try {
			testDoc = sax.build(testFile);
		} catch (Exception e) {
		}
		Element element = ServiceProvider.getInstance().getDefineService().getLogicalArchitectureData();
		String originalRootElement = testDoc.getRootElement().toString();
		String importedRootElement = element.toString();
		assertEquals(originalRootElement, importedRootElement);
	}
}
