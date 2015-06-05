package husacct.control.task;

import husacct.ServiceProvider;
import husacct.control.presentation.util.ImportDialog;
import husacct.control.task.resources.IResource;
import husacct.control.task.resources.ResourceFactory;

import java.io.File;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;

public class ImportController {
	
	private MainController mainController;
	private Logger logger = Logger.getLogger(ImportController.class);
	
	public ImportController(MainController mainController){
		this.mainController = mainController;
	}
	
	public void showImportArchitectureGui(){
		new ImportDialog(mainController, "ImportArchitecture");
	}
	
	public void showImportAnalyseModelGui(){
		new ImportDialog(mainController, "ImportAnalysisModel");
	}
	
	public void importArchitecture(File file){
		HashMap<String, Object> resourceData = new HashMap<String, Object>();
		resourceData.put("file", file);
		IResource xmlResource = ResourceFactory.get("xml");
		try {
			Document doc = xmlResource.load(resourceData);	
			Element logicalData = doc.getRootElement();
			ServiceProvider.getInstance().getDefineService().loadLogicalArchitectureData(logicalData);
		} catch (Exception e) {
			logger.debug("Unable to import logical architecture: " + e.getMessage());
		}
	}

	public void importAnalysisModel(File file){
		HashMap<String, Object> resourceData = new HashMap<String, Object>();
		resourceData.put("file", file);
		IResource xmlResource = ResourceFactory.get("xml");
		try {
			Document doc = xmlResource.load(resourceData);	
			Element logicalData = doc.getRootElement();
			ServiceProvider.getInstance().getAnalyseService().importAnalysisModel(logicalData);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Unable to export analysis model: " + e.getMessage());
		}
	}

}
