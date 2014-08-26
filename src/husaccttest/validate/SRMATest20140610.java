package husaccttest.validate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import husacct.ServiceProvider;
import husacct.common.dto.RuleDTO;
import husacct.common.dto.ViolationDTO;
import husacct.validate.IValidateService;
import husacct.analyse.IAnalyseService;
import husacct.validate.domain.exception.ProgrammingLanguageNotFoundException;
import husacct.control.ControlServiceImpl;
import husacct.control.task.MainController;
import husacct.control.task.WorkspaceController;
import husacct.define.IDefineService;
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

public class SRMATest20140610 {
	private static ControlServiceImpl controlService;
	private static MainController mainController;
	private static WorkspaceController workspaceController;
	private final static String workspace = "SrmaTest2014-06-10 HUSACCT_3.1.xml";
	private static Logger logger = Logger.getLogger(SRMATest20140610.class);


	@BeforeClass
	public static void beforeClass() {
		try {
			setLog4jConfiguration();
			String workspacePath = TestResourceFinder.findHusacctWorkspace("java", workspace);
			logger.info(String.format("Running HUSACCT using workspace: " + workspacePath));

			controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
			mainController = controlService.getMainController();
			workspaceController = mainController.getWorkspaceController();
			loadWorkspace(workspacePath);
	
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

		} catch (Exception e){
			String errorMessage =  "Exception: " + e.getMessage();
			logger.warn(errorMessage);
		}
	}

	@AfterClass
	public static void tearDown(){
		workspaceController.closeWorkspace();
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
		assertEquals(372, numberOfDependencies);
	}


	@Test
	public void isValidatedCorrectly() {
		IValidateService validate = ServiceProvider.getInstance().getValidateService();
		boolean isValidatedCorrectly = false;
		int numberOfViolations = 0;
		try {
			numberOfViolations = validate.getAllViolations().getValue().size();
		} catch (ProgrammingLanguageNotFoundException e) {
			assertTrue(isValidatedCorrectly);
		}
		assertEquals(69, numberOfViolations);
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
			String pathTo = "xLibraries.fi";
			violations = validate.getViolationsByPhysicalPath(pathFrom, pathTo);
		} catch (ProgrammingLanguageNotFoundException e) {
			assertTrue(isValidatedCorrectly);	
		}
		assertEquals(4, violations.length);
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
	public void isTheOnlyModuleAllowedToUse_ViaGetViolationsByRule() {
		IDefineService define = ServiceProvider.getInstance().getDefineService();
		IValidateService validate = ServiceProvider.getInstance().getValidateService();
		ViolationDTO[] violations = null;
		boolean isValidatedCorrectly = false;
		try {
			RuleDTO[] rules = define.getDefinedRules();
			for (RuleDTO rule : rules) {
				if (rule.ruleTypeKey.equals("IsTheOnlyModuleAllowedToUse") 
						&& rule.moduleFrom.logicalPath.equals("Domain.RelationRules.IsTheOnlyModuleAllowedToUse1")) {
					violations = validate.getViolationsByRule(rule);
					break;
				}
			}
		} catch (ProgrammingLanguageNotFoundException e) {
			assertTrue(isValidatedCorrectly);	
		}
		assertEquals(9, violations.length);
	}

	@Test
	public void MustUse() {
		IDefineService define = ServiceProvider.getInstance().getDefineService();
		IValidateService validate = ServiceProvider.getInstance().getValidateService();
		ViolationDTO[] violations = null;
		boolean isValidatedCorrectly = false;
		try {
			RuleDTO[] rules = define.getDefinedRules();
			for (RuleDTO rule : rules) {
				if (rule.ruleTypeKey.equals("MustUse") && rule.moduleFrom.logicalPath.equals("Domain.RelationRules.MustUse-Violating")) {
					violations = validate.getViolationsByRule(rule);
					break;
				}
			}
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
		assertEquals(7, violations.length);
	}

	@Test
	public void NamingPrefix() {
		IDefineService define = ServiceProvider.getInstance().getDefineService();
		IValidateService validate = ServiceProvider.getInstance().getValidateService();
		ViolationDTO[] violations = null;
		boolean isValidatedCorrectly = false;
		try {
			RuleDTO[] rules = define.getDefinedRules();
			for (RuleDTO rule : rules) {
				if (rule.ruleTypeKey.equals("NamingConvention") && rule.moduleFrom.logicalPath.equals("Technology.PropertyRules.NamingPrefix")) {
					violations = validate.getViolationsByRule(rule);
					break;
				}
			}
		} catch (ProgrammingLanguageNotFoundException e) {
			assertTrue(isValidatedCorrectly);	
		}
		assertEquals(2, violations.length);
	}

	@Test
	public void NamingMid() {
		IDefineService define = ServiceProvider.getInstance().getDefineService();
		IValidateService validate = ServiceProvider.getInstance().getValidateService();
		ViolationDTO[] violations = null;
		boolean isValidatedCorrectly = false;
		try {
			RuleDTO[] rules = define.getDefinedRules();
			for (RuleDTO rule : rules) {
				if (rule.ruleTypeKey.equals("NamingConvention") && rule.moduleFrom.logicalPath.equals("Technology.PropertyRules.NamingMid")) {
					violations = validate.getViolationsByRule(rule);
					break;
				}
			}
		} catch (ProgrammingLanguageNotFoundException e) {
			assertTrue(isValidatedCorrectly);	
		}
		assertEquals(2, violations.length);
	}

	@Test
	public void NamingPostfix() {
		IDefineService define = ServiceProvider.getInstance().getDefineService();
		IValidateService validate = ServiceProvider.getInstance().getValidateService();
		ViolationDTO[] violations = null;
		boolean isValidatedCorrectly = false;
		try {
			RuleDTO[] rules = define.getDefinedRules();
			for (RuleDTO rule : rules) {
				if (rule.ruleTypeKey.equals("NamingConvention") && rule.moduleFrom.logicalPath.equals("Technology.PropertyRules.NamingPostfix")) {
					violations = validate.getViolationsByRule(rule);
					break;
				}
			}
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
		assertEquals(1, violations.length);
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
		assertEquals(1, violations.length);
	}

	@Test
	public void VisibilityConvention() {
		IDefineService define = ServiceProvider.getInstance().getDefineService();
		IValidateService validate = ServiceProvider.getInstance().getValidateService();
		ViolationDTO[] violations = null;
		boolean isValidatedCorrectly = false;
		try {
			RuleDTO[] rules = define.getDefinedRules();
			for (RuleDTO rule : rules) {
				if (rule.ruleTypeKey.equals("VisibilityConvention")) {
					violations = validate.getViolationsByRule(rule);
					break;
				}
			}
		} catch (ProgrammingLanguageNotFoundException e) {
			assertTrue(isValidatedCorrectly);	
		}
		assertEquals(4, violations.length);
	}

	//private helpers; from Maven plugin

	private static void setLog4jConfiguration() {
		URL propertiesFile = Class.class.getResource("/husacct/common/resources/log4j.properties");
		PropertyConfigurator.configure(propertiesFile);
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