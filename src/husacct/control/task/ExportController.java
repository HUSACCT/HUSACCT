package husacct.control.task;

import husacct.ServiceProvider;
import husacct.control.presentation.util.ExportLogicalArchitectureDialog;
import husacct.control.task.resources.IResource;
import husacct.control.task.resources.ResourceFactory;

import java.io.File;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;

public class ExportController {

	private MainController mainController;
	private Logger logger = Logger.getLogger(ExportController.class);
	
	public ExportController(MainController mainController){
		this.mainController = mainController;
	}
	
	public void showExportLogicalArchitectureGui() {
		new ExportLogicalArchitectureDialog(mainController);
		
	}

	public void showExportViolationsReportGui() {
		System.out.println("showExportViolationsReportGui");
	}
	
	public void exportLogicalArchitecture(File file){
		HashMap<String, Object> resourceData = new HashMap<String, Object>();
		resourceData.put("file", file);
		IResource xmlResource = ResourceFactory.get("xml");
		try {
			Element logicalData = ServiceProvider.getInstance().getDefineService().getLogicalArchitectureData();
			Document doc = new Document(logicalData);
			xmlResource.save(doc, resourceData);
		} catch (Exception e) {
			logger.debug("Unable to export logical architecture: " + e.getMessage());
		}
	}
}
