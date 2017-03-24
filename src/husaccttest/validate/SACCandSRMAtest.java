package husaccttest.validate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import husacct.ExternalServiceProvider;
import husacct.ServiceProvider;
import husacct.common.dto.RuleDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.dto.ViolationImExportDTO;
import husacct.common.dto.ViolationReportDTO;
import husacct.validate.IValidateService;
import husacct.analyse.IAnalyseService;
import husacct.validate.domain.exception.ProgrammingLanguageNotFoundException;
import husacct.validate.task.imexporting.importing.ImportViolations;
import husacct.control.ControlServiceImpl;
import husacct.control.task.MainController;
import husacct.define.IDefineService;
import husaccttest.TestResourceFinder;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jdom2.Document;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

// Tests the full SACC-cycle using the SRMA test and tests the externally provided SACC via ExternalServiceProvider.
public class SACCandSRMAtest {
	private static ControlServiceImpl controlService;
	private static MainController mainController;
	// Refers to a files that contains the definition of the intended architecture (modules, rules, assigned software units, ...).
	private static final String workspacePath = 
			TestResourceFinder.findHusacctWorkspace("java", "SrmaTest-2014-11-12.xml"); 
	// Refers to a file containing a set of previous violations. Used to determine new violations.
	private static final String importFilePathAllViolations =
			TestResourceFinder.getSaccFolder("java") 
			+ "ArchitectureViolations_SrmaTest_All-5_ImportFile.xml";
	// Path of export file with all current violations. This file can be produced, optionally.
	private static final String exportFilePathAllViolations = 
			TestResourceFinder.getExportFolderForTest("java") 
			+ "ArchitectureViolations_SrmaTest_All_ExportFile.xml";
	// Path of export file with only the new current violations. This file can be produced, optionally.
	private static final String exportFilePathNewViolations = 
			TestResourceFinder.getExportFolderForTest("java") 
			+ "ArchitectureViolations_SrmaTest_New_ExportFile.xml";

	private static ViolationReportDTO violationReport = null;
	private static int numberOfAllViolationsInSrmaSourceCode = 61;
	private static int numberOfMissingViolationsInImportFile = 5; // The import file contains 56 violations of 61 in total. Five violations have been removed with WordPad from the exportAllViolations file.

	private static Logger logger = Logger.getLogger(SACCandSRMAtest.class);


	@BeforeClass
	public static void beforeClass() {
		try {
			setLog4jConfiguration();
			logger.info(String.format(new Date().toString() + " Start: Validate - SACC and SRMA Test"));
			ExternalServiceProvider externalServiceProvider = ExternalServiceProvider.getInstance();
			violationReport = externalServiceProvider.performSoftwareArchitectureComplianceCheck(workspacePath, importFilePathAllViolations, true, true);
		} catch (Exception e){
			String errorMessage =  "Exception: " + e.getMessage();
			logger.warn(errorMessage);
		}
	}

	@AfterClass
	public static void tearDownClass(){
		controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
		mainController = controlService.getMainController();
		mainController.getWorkspaceController().closeWorkspace();
		File exportFileAllViolations = new File(exportFilePathAllViolations);
		exportFileAllViolations.delete();
		File exportFileNewViolations = new File(exportFilePathNewViolations);
		exportFileNewViolations.delete();
		logger.info(String.format(new Date().toString() + " Finished: Validate - SACC and SRMA Test"));
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
		assertEquals(413, numberOfDependencies);
	}


	@Test
	public void isValidatedCorrectly() {
		assertEquals(violationReport.getNrOfAllCurrentViolations() , numberOfAllViolationsInSrmaSourceCode);
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

	// Test on Export-function and services of ExternalServiceProvider

	@Test
	public void isNumberOfAllViolationsInViolationReportCorrect() {
		// Checks on ExternalServiceProvider.performSoftwareArchitectureComplianceCheck()
		int numberOfAllViolationsInReport = violationReport.getNrOfAllCurrentViolations();
		assertTrue(numberOfAllViolationsInReport == numberOfAllViolationsInSrmaSourceCode);
		int reportedNumberOfAllViolations = violationReport.getAllViolations().length;
		assertTrue(reportedNumberOfAllViolations == numberOfAllViolationsInSrmaSourceCode);
	}

	@Test
	public void isNumberOfNewViolationsInViolationReportCorrect() {
		// Checks on ExternalServiceProvider.performSoftwareArchitectureComplianceCheck()
		int numberOfReportedNewViolations = violationReport.getNrOfNewViolations();
		assertTrue(numberOfMissingViolationsInImportFile == numberOfReportedNewViolations);
		int numberOfNewViolationsInReport = violationReport.getNewViolations().length;
		assertTrue(numberOfMissingViolationsInImportFile == numberOfNewViolationsInReport);
		for (ViolationImExportDTO newViolation : violationReport.getNewViolations()) {
			logger.info(" New violation in class: " + newViolation.getFrom() + " Line: " + newViolation.getLine() + " Message: " + newViolation.getMessage());
		}
	}

	@Test
	public void isNumberOfAllViolationsInExportedXmlDocumentCorrect() {
		int numberOfAllViolationsInReport = violationReport.getNrOfAllCurrentViolations();
		int numberOfAllViolationsInXML = countNumberOfViolationsInExportedXmlDocument(violationReport.getExportDocAllViolations());
		assertTrue(numberOfAllViolationsInXML == numberOfAllViolationsInReport);
	}

	@Test
	public void isNumberOfNewViolationsInExportedXmlDocumentCorrect() {
		int numberOfNewViolationsInReport = violationReport.getNrOfNewViolations();
		int numberOfNewViolationsInXML = countNumberOfViolationsInExportedXmlDocument(violationReport.getExportDocNewViolations());
		assertTrue(numberOfNewViolationsInXML == numberOfNewViolationsInReport);
	}

	private int countNumberOfViolationsInExportedXmlDocument(Document xmlDoc) {
		int numberOfViolations = 0;
		try {
			if (xmlDoc != null) {
				ImportViolations importer = new ImportViolations(xmlDoc);
				List<ViolationImExportDTO> previousViolationsDtoList = importer.importViolations();
				numberOfViolations = previousViolationsDtoList.size();
			}
		} catch (Exception exception){
			logger.warn(String.format(" Exception: " + exception.getCause().toString()));
		}
		return numberOfViolations;
	}
	
	// Private helpers
	
	private static void setLog4jConfiguration() {
		URL propertiesFile = Class.class.getResource("/husacct/common/resources/log4j.properties");
		PropertyConfigurator.configure(propertiesFile);
	}
}
