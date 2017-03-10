package husacct.control.task;

import husacct.ServiceProvider;
import husacct.common.dto.ViolationReportDTO;
import husacct.control.ControlServiceImpl;
import husacct.control.task.MainController;
import husacct.control.task.WorkspaceController;
import java.io.File;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class ExternalComplianceCheck {
	private ControlServiceImpl controlService;
	private MainController mainController;
	private WorkspaceController workspaceController;
	private ViolationReportDTO violationReport;

	private Logger logger = Logger.getLogger(ExternalComplianceCheck.class);

	/**
	 * Read service definition in class ExternalServiceProvider.
	 */
	public ViolationReportDTO performSoftwareArchitectureComplianceCheck(String husacctWorkspaceFile, String importFilePreviousViolations, String exportFileAllCurrentViolations, String exportFileNewViolations) {
		violationReport = new ViolationReportDTO();
		try {
			logger.info(String.format(" Start: Software Architecture Compliance Check"));

			setControllers();
			
			loadWorkspace(husacctWorkspaceFile);
	
			analyseApplication();
	
			checkConformance();
			
			violationReport = getViolationReportDTO(importFilePreviousViolations, exportFileAllCurrentViolations, exportFileNewViolations);  
			
			exportCurrentViolations(exportFileAllCurrentViolations);

			workspaceController.closeWorkspace();
			logger.info(String.format(" Finished: Software Architecture Compliance Check"));
		} catch (Exception e){
			String errorMessage =  "Exception: " + e.getCause().toString();
			logger.warn(errorMessage);
		}
		return violationReport;
	}

	private void setControllers(){ 
		controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
		mainController = controlService.getMainController();
		workspaceController = mainController.getWorkspaceController();
 	} 
 	 
	private void loadWorkspace(String location) {
		logger.info(String.format("Loading workspace: %s", location));
		File file = new File(location);
		if(file.exists()){
			HashMap<String, Object> dataValues = new HashMap<String, Object>();
			dataValues.put("file", file);
			workspaceController.loadWorkspace("Xml", dataValues);
			if(workspaceController.isAWorkspaceOpened()){
				logger.info(String.format(new Date().toString() + " Workspace %s loaded", location));
			} else {
				logger.warn(String.format("Unable to open workspace %s", file.getAbsoluteFile()));
			}
		} else {
			logger.warn(String.format("Unable to locate %s", file.getAbsoluteFile()));
		}
	}


	private void analyseApplication() {
		controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
		mainController = controlService.getMainController();
		mainController.getApplicationController().analyseApplication();
		//analyseApplication() starts a different Thread, and needs some time finish
		boolean isAnalysing = true;
		while(isAnalysing){
			try {
				Thread.sleep((long)10);
			} catch (InterruptedException e) {}
			isAnalysing = mainController.getStateController().isAnalysing();
		}
	}

	private void checkConformance() {
		ServiceProvider.getInstance().getControlService().setValidating(true);
		logger.info(new Date().toString() + " CheckConformanceTask is Starting: IValidateService.checkConformance()" );
		ServiceProvider.getInstance().getValidateService().checkConformance();
		ServiceProvider.getInstance().getControlService().setValidating(false);
		logger.info(new Date().toString() + " CheckConformanceTask sets state Validating to false" );
		//checkConformance() starts a different Thread, and needs some time
		boolean isValidating = true;
		controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
		mainController = controlService.getMainController();
		while(isValidating){
			try {
				Thread.sleep((long)10);
			} catch (InterruptedException e) {}
			isValidating = mainController.getStateController().isValidating();
		}
	}

	private ViolationReportDTO getViolationReportDTO(String importFilePreviousViolations, String exportFileAllCurrentViolations, String exportFileNewViolations) {
		ViolationReportDTO violationReport = new ViolationReportDTO();
		if (importFilePreviousViolations != null) {
			File previousViolationsFile = new File(importFilePreviousViolations);
			if(previousViolationsFile.exists()){
				controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
				mainController = controlService.getMainController();
				violationReport = mainController.getExportImportController().performSoftwareArchitectureComplianceCheck(previousViolationsFile, exportFileAllCurrentViolations, exportFileNewViolations);
			} else {
				logger.warn(String.format("Unable to locate importFilePreviousViolations: %s", previousViolationsFile.getAbsoluteFile()));
				violationReport = mainController.getExportImportController().performSoftwareArchitectureComplianceCheck(null, exportFileAllCurrentViolations, null);
			}
		} else {
			violationReport = mainController.getExportImportController().performSoftwareArchitectureComplianceCheck(null, exportFileAllCurrentViolations, null);
		}
		return violationReport;
	}

	private void exportCurrentViolations(String exportFilePathAllCurrentViolations) {
		if ((violationReport.getNrOfAllCurrentViolations() >= 0) && (exportFilePathAllCurrentViolations != null) && !exportFilePathAllCurrentViolations.equals("")) {
			File exportFileAllCurrentViolations = new File(exportFilePathAllCurrentViolations);
			controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
			mainController = controlService.getMainController();
			mainController.getExportImportController().exportViolations(exportFileAllCurrentViolations);
		}
	}
}
