package husacct.control.task;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ProjectDTO;
import husacct.control.IControlService;
import husacct.control.presentation.util.ExportImportDialog;
import husacct.control.presentation.util.Filename;
import husacct.control.task.resources.IResource;
import husacct.control.task.resources.ResourceFactory;
import husacct.validate.IValidateService;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;

public class ExportImportController {

	private MainController mainController;
	private Logger logger = Logger.getLogger(ExportImportController.class);
	
	public ExportImportController(MainController mainController){
		this.mainController = mainController;
	}
	
	public void showExportAnalysisModelGui() {
		new ExportImportDialog(mainController, "ExportAnalysisModel");
	}

	public void showExportArchitectureGui() {
		new ExportImportDialog(mainController, "ExportArchitecture");
	}

	public void showExportViolationsGui() {
		new ExportImportDialog(mainController, "ExportViolations");
	}
	
	public void showReportArchitectureGui(){
		new ExportImportDialog(mainController, "ReportArchitecture");
	}
	
	public void showReportDependenciesGui(){
		new ExportImportDialog(mainController, "ReportDependencies");
	}
	
	public void showReportViolationsGui() {
		new ExportImportDialog(mainController, "ReportViolations");
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
	
	public void reportArchitecture(File file){
		try {
			ServiceProvider.getInstance().getDefineService().reportArchitecture(file.getAbsolutePath());
		} catch (Exception exception){
			IControlService controlService = ServiceProvider.getInstance().getControlService();
			controlService.showErrorMessage(exception.getMessage());
		}
	}
	
	public void reportDependencies(File file){
		IAnalyseService analyseService = ServiceProvider.getInstance().getAnalyseService();
		try {
			analyseService.exportDependencies(file.getAbsolutePath());
		} catch (Exception exception){
			IControlService controlService = ServiceProvider.getInstance().getControlService();
			controlService.showErrorMessage(exception.getMessage());
		}
	}
	
	public String[] getExportExtensionsValidate(){
		IValidateService validateService = ServiceProvider.getInstance().getValidateService();
		return validateService.getExportExtentions();
	}
	
	public void showImportArchitectureGui(){
		new ExportImportDialog(mainController, "ImportArchitecture");
	}
	
	public void showImportAnalyseModelGui(){
		new ExportImportDialog(mainController, "ImportAnalysisModel");
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
			ApplicationDTO applicationDTO = ServiceProvider.getInstance().getDefineService().getApplicationDetails();
			for (int i = 0; i < applicationDTO.projects.size(); i++) {
				ProjectDTO currentProject = applicationDTO.projects.get(i);
				if (currentProject.paths.size() > 0) {
					// Add analysed root modules to project
					currentProject.analysedModules = new ArrayList<AnalysedModuleDTO>();
					AnalysedModuleDTO[] analysedRootModules = ServiceProvider.getInstance().getAnalyseService().getRootModules();
					for (AnalysedModuleDTO analysedModule : analysedRootModules) {
						currentProject.analysedModules.add(analysedModule);
					}
					// Update project with analysedRootModules
					applicationDTO.projects.remove(i);
					applicationDTO.projects.add(i, currentProject);
				}
			}
			mainController.getWorkspaceController().getCurrentWorkspace().setApplicationData(applicationDTO);
			ServiceProvider.getInstance().getDefineService().analyze();
			mainController.getViewController().showApplicationOverviewGui();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Unable to export analysis model: " + e.getMessage());
		}
	}

}
