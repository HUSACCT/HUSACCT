package husaccttest.control;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import husacct.ServiceProvider;
import husacct.control.task.ExportController;
import husacct.control.task.ImportController;
import husacct.control.task.MainController;

import java.io.File;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ImportExportControllerTest {
	
	ImportController importController;
	ExportController exportController;
	
	File testFile = new File("importExportTestFile.xml");
	
	@Before
	public void setup(){
		MainController mainController = new MainController();
		importController = mainController.getImportController();
		exportController = mainController.getExportController();
	}
	
	@After
	public void cleanUp(){
		testFile.delete();
	}
	
	@Test
	public void testExport(){
		exportController.exportArchitecture(testFile);
		assertTrue(testFile.exists());
	}
	
	@Test
	public void testImport(){
		exportController.exportArchitecture(testFile);
		importController.importArchitecture(testFile);
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
