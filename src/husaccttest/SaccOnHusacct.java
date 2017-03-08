package husaccttest;

import static org.junit.Assert.assertTrue;
import husacct.ServiceProvider;
import husacct.validate.IValidateService;
import husacct.validate.domain.exception.ProgrammingLanguageNotFoundException;
import husacct.common.dto.ViolationImExportDTO;
import husacct.control.ControlServiceImpl;
import husacct.control.task.MainController;
import husacct.control.task.WorkspaceController;
import husacct.control.task.resources.IResource;
import husacct.control.task.resources.ResourceFactory;
import husaccttest.TestResourceFinder;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jdom2.Document;
import org.jdom2.Element;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class SaccOnHusacct {
	private static ControlServiceImpl controlService;
	private static MainController mainController;
	private static WorkspaceController workspaceController;
	// Refers to a files that contains the definition of the intended architecture (modules, rules, assigned software units, ...).
	private static final String workspace = "HUSACCT_Current_Architecture.xml";
	// Refers to a file containing a set of previous violations. Used to determine new violations.
	private static final String importFilePathAllViolations =
			TestResourceFinder.getSaccFolder("java") 
			+ "ArchitectureViolations_All_ImportFile" + "." + "xml";
	// Path of export file with all current violations. This file can be produced, optionally.
	private static final String exportFilePathAllViolations = 
			TestResourceFinder.getExportFolderForTest("java") 
			+ "ArchitectureViolations_All_ExportFile" + "." + "xml";
	// Path of export file with only the new current violations. This file can be produced, optionally.
	private static final String exportFilePathNewViolations = 
			TestResourceFinder.getExportFolderForTest("java") 
			+ "ArchitectureViolations_OnlyNew_ExportFile" + "." + "xml";
	private static final String outputFormat = "xml";
	private static ViolationImExportDTO[] newViolationsList = null;
	private static String previousSaccMoment = "";
	private static int previousNumberOfViolations = 0;

	private static Logger logger = Logger.getLogger(SaccOnHusacct.class);


	@BeforeClass
	public static void beforeClass() {
		try {
			String workspacePath = TestResourceFinder.getSaccFolder("java") + workspace;
			setLog4jConfiguration();
			logger.info(String.format(" Start: SACC on HUSACCT's source code using workspace: " + workspacePath));

			setControllers();
			
			loadWorkspace(workspacePath);
	
			analyseApplication();
	
			checkConformance();
			
			newViolationsList = identifyNewArchitecturalViolations(importFilePathAllViolations); // Uses the previous export file as input to compare to the new set of violations. 
			
			exportAllViolations(exportFilePathAllViolations, outputFormat);

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
		int currentNumberOfViolations = 0;
		try {
			currentNumberOfViolations = validate.getAllViolations().getValue().size();
			logger.info(" Previous number of violations: " + previousNumberOfViolations + "  At: " + previousSaccMoment);
			logger.info(" Current number of violations: " + currentNumberOfViolations);
		} catch (ProgrammingLanguageNotFoundException e) {
			assertTrue(isValidatedCorrectly);
		}
		assertTrue(currentNumberOfViolations <= previousNumberOfViolations);
	}


	@Test
	public void newArchitecturalViolations() {
		if (newViolationsList.length > 0) {
			logger.info(" New architectural violations detected! Number of new violations = " + newViolationsList.length);
		} else {
			logger.info(" No new architectural violations detected! Number of new violations = " + newViolationsList.length);
		}
		for (ViolationImExportDTO newViolation : newViolationsList) {
			logger.info(" Violation in class: " + newViolation.getFrom() + " Line: " + newViolation.getLine() + " Message: " + newViolation.getMessage());
		}
		assertTrue((newViolationsList != null) && (newViolationsList.length <= 0));
	}
	
	//private helpers; from Maven plugin

	private static void setLog4jConfiguration() {
		URL propertiesFile = Class.class.getResource("/husacct/common/resources/log4j.properties");
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

	private static ViolationImExportDTO[] identifyNewArchitecturalViolations(String filePath) {
		File previousViolationsFile = new File(filePath);
		ViolationImExportDTO[] newViolations = null;
		if(previousViolationsFile.exists()){
			getDateAndTotalFromPreviousViolationFile(previousViolationsFile);
			controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
			mainController = controlService.getMainController();
			newViolations = mainController.getExportImportController().identifyNewViolations(previousViolationsFile);
		} else {
			logger.warn(String.format("Unable to locate %s", previousViolationsFile.getAbsoluteFile()));
		}
		return newViolations;
	}
	
	private static void getDateAndTotalFromPreviousViolationFile(File previousViolationsFile) {
		HashMap<String, Object> resourceData = new HashMap<String, Object>();
		resourceData.put("file", previousViolationsFile);
		IResource xmlResource = ResourceFactory.get("xml");
		try {
			Document doc = xmlResource.load(resourceData);	
			Element reportElement = doc.getRootElement();
			previousSaccMoment = reportElement.getChildText("violationsGeneratedOn");
			String violationsTotal = reportElement.getChildText("totalViolations");
			previousNumberOfViolations = Integer.parseInt(violationsTotal);
		} catch (Exception exception){
			logger.warn(String.format(" Exception: " + exception.getCause().toString()));
		}
		
	}
	
	private static void exportAllViolations(String filePath, String extension) {
		IValidateService validateService = ServiceProvider.getInstance().getValidateService();
		File file = new File(filePath);
		file.delete();
		logger.info(String.format("Export violations to %s", file.getAbsolutePath()));
		validateService.exportViolations(new File(filePath), extension);
	}
}
