package husacct.control.task;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.control.IControlService;
import husacct.control.presentation.util.ExportDialog;
import husacct.control.presentation.util.ReportDependenciesDialog;
import husacct.control.presentation.util.ExportViolationsReportDialog;
import husacct.control.presentation.util.Filename;
import husacct.control.task.resources.IResource;
import husacct.control.task.resources.ResourceFactory;
import husacct.validate.IValidateService;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;

public class ExportController {

	private MainController mainController;
	private Logger logger = Logger.getLogger(ExportController.class);
	
	public ExportController(MainController mainController){
		this.mainController = mainController;
	}
	
	public void showExportAnalysisModelGui() {
		new ExportDialog(mainController, "ExportAnalysisModel");
	}

	public void showExportArchitectureGui() {
		new ExportDialog(mainController, "ExportArchitecture");
	}

	public void showExportViolationsReportGui() {
		new ExportViolationsReportDialog(mainController);
	}
	
	public void showReportDependenciesGui(){
		new ReportDependenciesDialog(mainController);
	}
	
	public void exportAnalysisModel(File file){
		HashMap<String, Object> resourceData = new HashMap<String, Object>();
		resourceData.put("file", file);
		IResource xmlResource = ResourceFactory.get("xml");
		try {
			Element logicalData = ServiceProvider.getInstance().getAnalyseService().exportAnalysisModel();
			Document doc = new Document(logicalData);
			
			xmlResource.save(doc, resourceData);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Unable to export analysis model: " + e.getMessage());
		}
	}
	
	public void exportArchitecture(File file){
		HashMap<String, Object> resourceData = new HashMap<String, Object>();
		resourceData.put("file", file);
		IResource xmlResource = ResourceFactory.get("xml");
		try {
			Element logicalData = ServiceProvider.getInstance().getDefineService().getLogicalArchitectureData();
			Document doc = new Document(logicalData);
			
			xmlResource.save(doc, resourceData);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Unable to export logical architecture: " + e.getMessage());
		}
	}
	
	public void exportViolationsReport(File file){
		Filename filename = new Filename(file, File.separatorChar, '.');
		IValidateService validateService = ServiceProvider.getInstance().getValidateService();
		try {
			validateService.exportViolations(file, filename.getExtension());
		} catch (Exception exception){
			IControlService controlService = ServiceProvider.getInstance().getControlService();
			controlService.showErrorMessage(exception.getMessage());
		}
	}
	
	public void exportDependencies(File file){
		IAnalyseService analyseService = ServiceProvider.getInstance().getAnalyseService();
		try {
			analyseService.exportDependencies(file.getAbsolutePath());
		} catch (Exception exception){
			IControlService controlService = ServiceProvider.getInstance().getControlService();
			controlService.showErrorMessage(exception.getMessage());
		}
	}
	
	public String[] getExportExtensions(){
		IValidateService validateService = ServiceProvider.getInstance().getValidateService();
		return validateService.getExportExtentions();
	}
}
