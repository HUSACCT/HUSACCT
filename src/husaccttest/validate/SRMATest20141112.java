package husaccttest.validate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import husacct.ServiceProvider;
import husacct.common.dto.RuleDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.dto.ViolationImExportDTO;
import husacct.validate.IValidateService;
import husacct.analyse.IAnalyseService;
import husacct.validate.domain.exception.ProgrammingLanguageNotFoundException;
import husacct.validate.task.imexporting.importing.ImportViolations;
import husacct.control.ControlServiceImpl;
import husacct.control.task.MainController;
import husacct.control.task.WorkspaceController;
import husacct.control.task.resources.IResource;
import husacct.control.task.resources.ResourceFactory;
import husacct.define.IDefineService;
import husaccttest.TestResourceFinder;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jdom2.Document;
import org.jdom2.Element;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class SRMATest20141112 {
	private static ControlServiceImpl controlService;
	private static MainController mainController;
	private static WorkspaceController workspaceController;
	private final static String workspace = "SrmaTest-2014-11-12.xml";
	private static Logger logger = Logger.getLogger(SRMATest20141112.class);


	@BeforeClass
	public static void beforeClass() {
		try {
			setLog4jConfiguration();
			String workspacePath = TestResourceFinder.findHusacctWorkspace("java", workspace);
			logger.info(String.format(new Date().toString() + " Start: SRMA Test using workspace: " + workspacePath));

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
		logger.info(String.format(new Date().toString() + " Finished: SRMA Test"));
	}
	
	@Test
	public void isAnalysedCorrectly() {
		IAnalyseService analyse = ServiceProvider.getInstance().getAnalyseService();
		boolean isAnalysedCorrectly = false;
		int numberOfDependencies = 0;
		try {
			numberOfDependencies = analyse.getAnalysisStatistics(null).totalNrOfDependencies;
		} catch (ProgrammingLanguageNotFoundException e) {
			assertTrue(isAnalysedCorrectly);
		}
		assertEquals(411, numberOfDependencies);
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
		assertEquals(61, numberOfViolations);
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
		assertEquals(8, violations.length);
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
		assertEquals(8, violations.length);
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
		assertEquals(8, violations.length);
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
		assertEquals(8, violations.length);
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
		assertEquals(8, violations.length);
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
		assertEquals(8, violations.length);
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
		assertEquals(5, violations.length);
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
		int length = violations.length;
		assertEquals(2, length);
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

	// Export and Import Violations Tests

	@Test
	public void isNumberOfExportedViolationsCorrect() {
		// Tests if the service "exportViolations(exportFile)" works correctly
		IValidateService validate = ServiceProvider.getInstance().getValidateService();
		boolean isProcessedCompletely = false;
		boolean	numberOfExportedViolationsIsCorrect = false;
		int numberOfViolationsInModel = 0;
		int numberOfViolationsInXML = 0;
		String exportFilePath = TestResourceFinder.getExportFolderForTest("java") + "ExportImportViolationsTest" + "." + "xml";
		try {
			numberOfViolationsInModel = validate.getAllViolations().getValue().size();
			File exportFile = new File(exportFilePath);
			exportFile.delete();
			exportFile = new File(exportFilePath);
			mainController.getExportImportController().exportViolations(exportFile);
			numberOfViolationsInXML = countNumberOfViolationsInExportFile(exportFile);
			exportFile.delete();
			if (numberOfViolationsInModel == numberOfViolationsInXML) {
				numberOfExportedViolationsIsCorrect = true;
			}
			isProcessedCompletely = true;
		} catch (Exception exception) {
			logger.warn(String.format(" Exception: " + exception.getCause().toString()));
		}
		assertTrue(isProcessedCompletely);
		assertTrue(numberOfExportedViolationsIsCorrect);
	}

	private int countNumberOfViolationsInExportFile(File exportile) {
		int numberOfViolations = 0;
		HashMap<String, Object> resourceData = new HashMap<String, Object>();
		resourceData.put("file", exportile);
		IResource xmlResource = ResourceFactory.get("xml");
		try {
			Document doc = xmlResource.load(resourceData);	
			Element logicalData = doc.getRootElement();
			ImportViolations importer = new ImportViolations();
			List<ViolationImExportDTO> previousViolationsDtoList = importer.importViolations(logicalData);
			numberOfViolations = previousViolationsDtoList.size();
		} catch (Exception exception){
			logger.warn(String.format(" Exception: " + exception.getCause().toString()));
		}
		return numberOfViolations;
	}
	
	@Test
	public void isIdentifiedNumberOfNewViolationsCorrect() {
		// Tests if the service "identifyNewViolations(previousViolationsFile)" works correctly
		boolean isProcessedCompletely = false;
		boolean	numberOfNewViolationsIsCorrect = false;
		int numberOfMissingViolationsInImportFile = 5; // The import file contains 56 violations of 61 in total. Five violations are removed with WordPad from the exportAllViolations file.
		int numberOfIdentifiedNewViolations = 0;
		String importFilePath = TestResourceFinder.getSaccFolder("java") + "ArchitectureViolations_SrmaTest_All-5_ImportFile" + "." + "xml";
		try {
			File previousViolationsFile = new File(importFilePath);
			ViolationImExportDTO[] newViolations = mainController.getExportImportController().identifyNewViolations(previousViolationsFile);
			numberOfIdentifiedNewViolations = newViolations.length;
			if (numberOfMissingViolationsInImportFile == numberOfIdentifiedNewViolations) {
				numberOfNewViolationsIsCorrect = true;
			}
			isProcessedCompletely = true;
		} catch (Exception exception) {
			logger.warn(String.format(" Exception: " + exception.getCause().toString()));
		}
		assertTrue(isProcessedCompletely);
		assertTrue(numberOfNewViolationsIsCorrect);
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
	}

	private static void checkConformance() {
		ServiceProvider.getInstance().getControlService().setValidating(true);
		logger.info(new Date().toString() + " CheckConformanceTask is Starting: IValidateService.checkConformance()" );
		ServiceProvider.getInstance().getValidateService().getCategories();
		ServiceProvider.getInstance().getValidateService().checkConformance();
		ServiceProvider.getInstance().getControlService().setValidating(false);
		//logger.info(new Date().toString() + " CheckConformanceTask sets state Validating to false" );
	}

}
