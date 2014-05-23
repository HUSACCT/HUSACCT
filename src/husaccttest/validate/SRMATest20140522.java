package husaccttest.validate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import husacct.ServiceProvider;
import husacct.common.dto.ViolationDTO;
import husacct.validate.IValidateService;
import husacct.analyse.IAnalyseService;
import husacct.validate.domain.exception.ProgrammingLanguageNotFoundException;
import husacct.control.ControlServiceImpl;
import husacct.control.task.MainController;
import husacct.control.task.WorkspaceController;
import husaccttest.analyse.java.benchmark.accuracy.DependencyDetectionAccuracyTest;
import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

public class SRMATest20140522 {
	private static ControlServiceImpl controlService;
	private static MainController mainController;
	private static WorkspaceController workspaceController;
	private final static String workspaceLocation = "C:\\Tools\\Eclipse43\\Workspace\\HUSACCT\\testprojects\\workspaces\\SrmaTest2014-05-22.xml";
	private final static String outputFormat = "xml";
	private final static String outputLocation = "C:\\Tools\\HUSACCT\\Reports";
	private static Logger logger;

	@BeforeClass
	public static void beforeClass() {
		try {
			setLog4jConfiguration();
			logger.info(String.format("Running HUSACCT using workspace %s, violations report stored as %s at %s", workspaceLocation, outputFormat, outputLocation));

			controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
			mainController = controlService.getMainController();
			workspaceController = mainController.getWorkspaceController();
			loadWorkspace(workspaceLocation);
	
			analyseApplication();
			//analyseApplication() starts a different Thread, and needs some time
			boolean isAnalysing = true;
			controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
			mainController = controlService.getMainController();
			while(isAnalysing){
				try {
					Thread.sleep((long)10);
				} catch (InterruptedException e) {}
				isAnalysing = mainController.getStateController().isAnalysing();
			}
	
			checkConformance();
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

			//exportViolations(outputLocation, outputFormat);

		} catch (Exception e){
			String errorMessage =  "Exception: " + e.getMessage();
			logger.warn(errorMessage);
			//System.exit(0);
		}
	}

	@Test
	public void isAnalysedCorrectly() {
		IAnalyseService analyse = ServiceProvider.getInstance().getAnalyseService();
		boolean isAnalysedCorrectly = false;
		int numberOfDependencies = 0;
		try {
			numberOfDependencies = analyse.getAmountOfDependencies();
		} catch (ProgrammingLanguageNotFoundException e) {
			assertTrue(isAnalysedCorrectly);
		}
		assertEquals(365, numberOfDependencies);
	}


	@Test
	public void isNotAllowedToUse_Internal() {
		IValidateService validate = ServiceProvider.getInstance().getValidateService();
		ViolationDTO[] violations = null;
		boolean isValidatedCorrectly = false;
		try {
			String pathFrom = "domain.relationrules.is_not_allowed_to_use";
			String pathTo = "technology.relationrules.notallowed";
			violations = validate.getViolationsByPhysicalPath(pathFrom, pathTo);
		} catch (ProgrammingLanguageNotFoundException e) {
			assertTrue(isValidatedCorrectly);	
		}
		assertEquals(9, violations.length);
	}

	@Test
	public void isNotAllowedToUse_ExternalSystems() {
		IValidateService validate = ServiceProvider.getInstance().getValidateService();
		ViolationDTO[] violations = null;
		boolean isValidatedCorrectly = false;
		try {
			String pathFrom = "domain.relationrules.is_not_allowed_to_use";
			String pathTo = "fi.foyt";
			violations = validate.getViolationsByPhysicalPath(pathFrom, pathTo);
		} catch (ProgrammingLanguageNotFoundException e) {
			assertTrue(isValidatedCorrectly);	
		}
		assertEquals(3, violations.length);
	}

	@Test
	public void isNotAllowedToBackCall() {
		IValidateService validate = ServiceProvider.getInstance().getValidateService();
		ViolationDTO[] violations = null;
		boolean isValidatedCorrectly = false;
		try {
			String pathFrom = "Domain";
			String pathTo = "Presentation";
			violations = validate.getViolationsByLogicalPath(pathFrom, pathTo);
		} catch (ProgrammingLanguageNotFoundException e) {
			assertTrue(isValidatedCorrectly);	
		}
		assertEquals(9, violations.length);
	}

	@Test
	public void isNotAllowedToSkipCall() {
		IValidateService validate = ServiceProvider.getInstance().getValidateService();
		ViolationDTO[] violations = null;
		boolean isValidatedCorrectly = false;
		try {
			String pathFrom = "Presentation";
			String pathTo = "Technology";
			violations = validate.getViolationsByLogicalPath(pathFrom, pathTo);
		} catch (ProgrammingLanguageNotFoundException e) {
			assertTrue(isValidatedCorrectly);	
		}
		assertEquals(9, violations.length);
	}

	@Test
	public void isOnlyAllowedToUse() {
		IValidateService validate = ServiceProvider.getInstance().getValidateService();
		ViolationDTO[] violations = null;
		boolean isValidatedCorrectly = false;
		try {
			String pathFrom = "Domain.RelationRules.IsOnlyAllowedToUse";
			String pathTo = "Technology.RelationRules.NotAllowed";
			violations = validate.getViolationsByLogicalPath(pathFrom, pathTo);
		} catch (ProgrammingLanguageNotFoundException e) {
			assertTrue(isValidatedCorrectly);	
		}
		assertEquals(9, violations.length);
	}

	@Test
	public void isTheOnlyModuleAllowedToUse() {
		IValidateService validate = ServiceProvider.getInstance().getValidateService();
		ViolationDTO[] violations = null;
		boolean isValidatedCorrectly = false;
		try {
			String pathFrom = "Domain.RelationRules.IsTheOnlyModuleAllowedToUse2";
			String pathTo = "Technology.RelationRules.TheOnlyOne-Allowed";
			violations = validate.getViolationsByLogicalPath(pathFrom, pathTo);
		} catch (ProgrammingLanguageNotFoundException e) {
			assertTrue(isValidatedCorrectly);	
		}
		assertEquals(9, violations.length);
	}

	@Test
	public void MustUse() {
		IValidateService validate = ServiceProvider.getInstance().getValidateService();
		ViolationDTO[] violations = null;
		boolean isValidatedCorrectly = false;
		try {
			String pathFrom = "Domain.RelationRules.MustUse-Violating";
			String pathTo = "Technology.RelationRules.Allowed";
			violations = validate.getViolationsByLogicalPath(pathFrom, pathTo);
		} catch (ProgrammingLanguageNotFoundException e) {
			assertTrue(isValidatedCorrectly);	
		}
		assertEquals(1, violations.length);
	}

	@Test
	public void FacadeConvention() {
		IValidateService validate = ServiceProvider.getInstance().getValidateService();
		ViolationDTO[] violations = null;
		boolean isValidatedCorrectly = false;
		try {
			String pathFrom = "Technology.PropertyRules.FacadeConvention";
			String pathTo = "Technology.PropertyRules.Component";
			violations = validate.getViolationsByLogicalPath(pathFrom, pathTo);
		} catch (ProgrammingLanguageNotFoundException e) {
			assertTrue(isValidatedCorrectly);	
		}
		assertEquals(6, violations.length);
	}

	@Test
	public void NamingPrefix() {
		IValidateService validate = ServiceProvider.getInstance().getValidateService();
		ViolationDTO[] violations = null;
		boolean isValidatedCorrectly = false;
		try {
			String pathFrom = "Technology.PropertyRules.NamingPrefix";
			String pathTo = "";
			violations = validate.getViolationsByLogicalPath(pathFrom, pathTo);
		} catch (ProgrammingLanguageNotFoundException e) {
			assertTrue(isValidatedCorrectly);	
		}
		assertEquals(2, violations.length);
	}

	@Test
	public void NamingMid() {
		IValidateService validate = ServiceProvider.getInstance().getValidateService();
		ViolationDTO[] violations = null;
		boolean isValidatedCorrectly = false;
		try {
			String pathFrom = "Technology.PropertyRules.NamingMid";
			String pathTo = "";
			violations = validate.getViolationsByLogicalPath(pathFrom, pathTo);
		} catch (ProgrammingLanguageNotFoundException e) {
			assertTrue(isValidatedCorrectly);	
		}
		assertEquals(2, violations.length);
	}

	@Test
	public void NamingPostfix() {
		IValidateService validate = ServiceProvider.getInstance().getValidateService();
		ViolationDTO[] violations = null;
		boolean isValidatedCorrectly = false;
		try {
			String pathFrom = "Technology.PropertyRules.NamingPostfix";
			String pathTo = "";
			violations = validate.getViolationsByLogicalPath(pathFrom, pathTo);
		} catch (ProgrammingLanguageNotFoundException e) {
			assertTrue(isValidatedCorrectly);	
		}
		assertEquals(2, violations.length);
	}

	@Test
	public void InheritanceConventionSuperClass() {
		IValidateService validate = ServiceProvider.getInstance().getValidateService();
		ViolationDTO[] violations = null;
		boolean isValidatedCorrectly = false;
		try {
			String pathFrom = "Technology.PropertyRules.SuperclassInheritance";
			String pathTo = "Technology.PropertyRules.DataSource";
			violations = validate.getViolationsByLogicalPath(pathFrom, pathTo);
		} catch (ProgrammingLanguageNotFoundException e) {
			assertTrue(isValidatedCorrectly);	
		}
		assertEquals(2, violations.length);
	}

	@Test
	public void InheritanceConventionInterface() {
		IValidateService validate = ServiceProvider.getInstance().getValidateService();
		ViolationDTO[] violations = null;
		boolean isValidatedCorrectly = false;
		try {
			String pathFrom = "Technology.PropertyRules.InterfaceInheritance";
			String pathTo = "Technology.PropertyRules.IDataSource";
			violations = validate.getViolationsByLogicalPath(pathFrom, pathTo);
		} catch (ProgrammingLanguageNotFoundException e) {
			assertTrue(isValidatedCorrectly);	
		}
		assertEquals(2, violations.length);
	}

	@Test
	public void VisibilityConvention() {
		IValidateService validate = ServiceProvider.getInstance().getValidateService();
		ViolationDTO[] violations = null;
		boolean isValidatedCorrectly = false;
		try {
			String pathFrom = "Technology.PropertyRules.Component";
			String pathTo = "";
			violations = validate.getViolationsByLogicalPath(pathFrom, pathTo);
		} catch (ProgrammingLanguageNotFoundException e) {
			assertTrue(isValidatedCorrectly);	
		}
		assertEquals(4, violations.length);
	}

	//
	//private helpers
	//
	//From Maven plugin

	private static void setLog4jConfiguration() {
		URL propertiesFile = Class.class.getResource("/husacct/common/resources/log4j.properties");
		PropertyConfigurator.configure(propertiesFile);
		logger = Logger.getLogger(DependencyDetectionAccuracyTest.class);
	}
	
	private static void loadWorkspace(String location) {
		logger.info(String.format("Loading workspace %s", location));
		File file = new File(location);
		if(file.exists()){
			HashMap<String, Object> dataValues = new HashMap<String, Object>();
			dataValues.put("file", file);
			workspaceController.loadWorkspace("Xml", dataValues);
			if(workspaceController.isOpenWorkspace()){
				logger.info(String.format("Workspace %s loaded", location));
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
	}

	private static void checkConformance() {
		ServiceProvider.getInstance().getControlService().setValidate(true);
		logger.info(new Date().toString() + " CheckConformanceTask is Starting: IValidateService.checkConformance()" );
		ServiceProvider.getInstance().getValidateService().getCategories();
		ServiceProvider.getInstance().getValidateService().checkConformance();
		ServiceProvider.getInstance().getControlService().setValidate(false);
		logger.info(new Date().toString() + " CheckConformanceTask sets state Validating to false" );
	}

/*

	private void exportViolations(String location, String extension) {
		IValidateService validateService = ServiceProvider.getInstance().getValidateService();
		File file = new File(location);
		logger.debug(String.format("Export violations to %s", file.getAbsolutePath()));
		validateService.exportViolations(new File(location), extension);
	}
*/
}