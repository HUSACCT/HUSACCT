package husaccttest;

import static org.junit.Assert.assertTrue;
import husacct.ServiceProvider;
import husacct.validate.IValidateService;
import husacct.validate.domain.exception.ProgrammingLanguageNotFoundException;
import husacct.common.dto.ViolationImExportDTO;
import husacct.control.ControlServiceImpl;
import husacct.control.task.MainController;
import husacct.control.task.WorkspaceController;
import husaccttest.TestResourceFinder;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class SaccOnHusacct {
	private static ControlServiceImpl controlService;
	private static MainController mainController;
	private static WorkspaceController workspaceController;
	private static final String workspace = "HUSACCT_Current_Architecture.xml";
	private static final String exportFile = "ViolationsExportFile_HUSACCT";
	private static final String exportFilePath = TestResourceFinder.getExportFolderForTest("java") + exportFile + "." + "xml";
	private static final String outputFormat = "xml";
	private static ViolationImExportDTO[] newViolationsList = null;

	private static Logger logger = Logger.getLogger(SaccOnHusacct.class);


	@BeforeClass
	public static void beforeClass() {
		try {
			String workspacePath = TestResourceFinder.findHusacctWorkspace("java", workspace);
			setLog4jConfiguration();
			logger.info(String.format(" Start: SACC on HUSACCT's source code using workspace: " + workspacePath));

			setControllers();
			
			loadWorkspace(workspacePath);
	
			analyseApplication();
	
			checkConformance();
			
			newViolationsList = identifyNewArchitecturalViolations(exportFilePath); // Uses the previous export file as input to compare to the new set of violations. 
			
			exportViolations(exportFilePath, outputFormat);

		} catch (Exception e){
			String errorMessage =  "Exception: " + e.getCause().toString();
			logger.warn(errorMessage);
		}
	}

	@AfterClass
	public static void tearDown(){
		workspaceController.closeWorkspace();
		logger.info(String.format(" Finished: SACC on HUSACCT's source code"));
	}
	
	@Test
	public void hasNumberOfViolationsIncreased() {
		IValidateService validate = ServiceProvider.getInstance().getValidateService();
		boolean isValidatedCorrectly = false;
		int numberOfViolations = 0;
		try {
			numberOfViolations = validate.getAllViolations().getValue().size();
		} catch (ProgrammingLanguageNotFoundException e) {
			assertTrue(isValidatedCorrectly);
		}
		assertTrue(numberOfViolations <= 771);
	}


	@Test
	public void newArchitecturalViolations() {
		if (newViolationsList.length > 0) {
			logger.info(" New architectural violations detected! Number of new violations = " + newViolationsList.length);
		} else {
			logger.info(" No new architectural violations detected! Number of new violations = " + newViolationsList.length);
		}
		for (ViolationImExportDTO newViolation : newViolationsList) {
			logger.info(" New violation of rule type: " + newViolation.getRuleType() + " In class: " + newViolation.from);
		}
		assertTrue((newViolationsList != null) && (newViolationsList.length <= 0));
	}
	
	//private helpers; from Maven plugin

	private static void setLog4jConfiguration() {
		URL propertiesFile = Class.class.getResource("log4j.properties");
		PropertyConfigurator.configure(propertiesFile);
	}
	
	private static void setControllers(){ 
		controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
		mainController = controlService.getMainController();
		workspaceController = mainController.getWorkspaceController();
 	} 
 	 
	private static void loadWorkspace(String location) {
		logger.info(String.format("Loading workspace %s", location));
		File file = new File(location);
		if(file.exists()){
			HashMap<String, Object> dataValues = new HashMap<>();
			dataValues.put("file", file);
			workspaceController.loadWorkspace("Xml", dataValues);
			if(workspaceController.isOpenWorkspace()){
				logger.info(String.format(new Date().toString() + " Workspace %s loaded", location));
			} else {
				logger.warn(String.format("Unable to open workspace %s", file.getAbsoluteFile()));
			}
		} else {
			logger.warn(String.format("Unable to locate %s", file.getAbsoluteFile()));
		}
	}


	private static void analyseApplication() {
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

	private static void checkConformance() {
		ServiceProvider.getInstance().getControlService().setValidate(true);
		logger.info(new Date().toString() + " CheckConformanceTask is Starting: IValidateService.checkConformance()" );
		ServiceProvider.getInstance().getValidateService().checkConformance();
		ServiceProvider.getInstance().getControlService().setValidate(false);
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

	private static ViolationImExportDTO[] identifyNewArchitecturalViolations(String filePath) {
		File previousViolationsFile = new File(filePath);
		ViolationImExportDTO[] newViolations = null;
		if(previousViolationsFile.exists()){
			controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
			mainController = controlService.getMainController();
			newViolations = mainController.getExportImportController().identifyNewViolations(previousViolationsFile);
		} else {
			logger.warn(String.format("Unable to locate %s", previousViolationsFile.getAbsoluteFile()));
		}
		return newViolations;
	}
	
	private static void exportViolations(String filePath, String extension) {
		IValidateService validateService = ServiceProvider.getInstance().getValidateService();
		File file = new File(filePath);
		file.delete();
		logger.info(String.format("Export violations to %s", file.getAbsolutePath()));
		validateService.exportViolations(new File(filePath), extension);
	}
}
