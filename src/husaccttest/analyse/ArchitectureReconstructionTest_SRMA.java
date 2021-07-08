package husaccttest.analyse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AnalysisStatisticsDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.RuleDTO;
import husacct.common.dto.ViolationDTO;
import husacct.control.ControlServiceImpl;
import husacct.control.task.MainController;
import husacct.control.task.WorkspaceController;
import husacct.define.IDefineService;
import husacct.validate.IValidateService;
import husacct.validate.domain.exception.ProgrammingLanguageNotFoundException;
import husaccttest.TestResourceFinder;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ArchitectureReconstructionTest_SRMA {
	private static String workspacePath;
	private static ControlServiceImpl controlService;
	private static MainController mainController;
	private static WorkspaceController workspaceController;
	private final static String workspace = "SrmaTest-2014-11-12.xml";
	private static Logger logger;
	private static IAnalyseService analyseService = null;

	private static AnalysisStatisticsDTO analyseStatisticsBeforeReconstruction;
	private static AnalysisStatisticsDTO analyseStatisticsAfterReconstruction;
	
	@BeforeClass
	public static void beforeClass() {
		try {
			setLog4jConfiguration();
			workspacePath = TestResourceFinder.findHusacctWorkspace("java", workspace);
			logger.info(String.format("Running HUSACCT using workspace: " + workspacePath));

			controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
			mainController = controlService.getMainController();
			workspaceController = mainController.getWorkspaceController();
			loadWorkspace(workspacePath);
	
			analyseApplication(); //analyseApplication() starts a different Thread, and needs some time
			boolean isAnalysing = true;
			controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
			mainController = controlService.getMainController();
			while(isAnalysing){
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {}
				isAnalysing = mainController.getStateController().isAnalysing();
			}
			
			logger.info(String.format("Start: Architecture Reconstruction"));
			analyseStatisticsBeforeReconstruction = getAnalyseStatistics();
			reconstructArchitecture();
			analyseStatisticsAfterReconstruction = getAnalyseStatistics();
			logger.info(String.format("Finished: Architecture Reconstruction"));
			
			checkConformance();	//checkConformance() starts a different Thread, and needs some time
			boolean isValidating = true;
			controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
			mainController = controlService.getMainController();
			while(isValidating){
				try {
					Thread.sleep(10);
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

	// TESTS 

	// Extend with test cases concerning architecture reconstruction! Below are examples of test cases with functionality that can be useful.
	
	// Test cases to check that the data in the model have not changed (in numbers) by the reconstruction activities
	@Test
	public void ComparePackages(){
		Assert.assertTrue(analyseStatisticsAfterReconstruction.totalNrOfPackages == analyseStatisticsBeforeReconstruction.totalNrOfPackages);
	}

	@Test
	public void CompareClasses(){
		Assert.assertTrue(analyseStatisticsAfterReconstruction.totalNrOfClasses == analyseStatisticsBeforeReconstruction.totalNrOfClasses);
	}
	
	@Test
	public void CompareDependencies(){
		Assert.assertTrue(analyseStatisticsAfterReconstruction.totalNrOfDependencies == analyseStatisticsBeforeReconstruction.totalNrOfDependencies);
	}
	
	@Test
	public void CompareLinesOfCode(){
		Assert.assertTrue(analyseStatisticsAfterReconstruction.totalNrOfLinesOfCode == analyseStatisticsBeforeReconstruction.totalNrOfLinesOfCode);
	}
	
	// Test cases from SRMA test, which test per type of rule. See the SRMA test for other cases (husaccttest/validate).
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

	// Tests case that checks the number of found dependencies between packages in the implemented architecture. 
	@Test
	public void CompareNumberOfDependenciesBetweenSoftwareUnits_domain_presentation(){
		String fromUnit = "domain";
		String toUnit = "presentation";
		int numberOfDependencies = getNumberofDependenciesBetweenSoftwareUnits(fromUnit, toUnit);
		Assert.assertTrue(numberOfDependencies == 16);
	}
	
	// Tests case that checks the number of found dependencies between modules in the intended architecture. 
	@Test
	public void CompareNumberOfDependenciesBetweenModules_Domain_Presentation(){
		String fromModule = "Domain";
		String toModule = "Presentation";
		int numberOfDependencies = getNumberofDependenciesBetweenModulesInIntendedArchitecture(fromModule, toModule);
		Assert.assertTrue(numberOfDependencies == 16);
	}
	
	//
	//private helpers
	//
	private static void setLog4jConfiguration() {
		URL propertiesFile = husacct.Main.class.getResource("/husacct/common/resources/log4j.properties");
		PropertyConfigurator.configure(propertiesFile);
		logger = Logger.getLogger(ArchitectureReconstructionTest_SRMA.class);
	}
	
	private static void loadWorkspace(String location) {
		logger.info(String.format("Loading workspace %s", location));
		File file = new File(location);
		if(file.exists()){
			HashMap<String, Object> dataValues = new HashMap<String, Object>();
			dataValues.put("file", file);
			workspaceController.loadWorkspace("Xml", dataValues);
			if(workspaceController.isAWorkspaceOpened()){
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

	private static AnalysisStatisticsDTO getAnalyseStatistics() {
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		AnalysisStatisticsDTO statistics = analyseService.getAnalysisStatistics(null);
		logger.info(String.format("Statistics - Packages: " + statistics.totalNrOfPackages + ", Classes: " + statistics.totalNrOfClasses + ", Dependencies: " + statistics.totalNrOfDependencies));
		return statistics;
	}
	
	private static void checkConformance() {
		ServiceProvider.getInstance().getControlService().setValidating(true);
		logger.info(new Date().toString() + " CheckConformanceTask is Starting: IValidateService.checkConformance()" );
		ServiceProvider.getInstance().getValidateService().getCategories();
		ServiceProvider.getInstance().getValidateService().checkConformance();
		ServiceProvider.getInstance().getControlService().setValidating(false);
		logger.info(new Date().toString() + " CheckConformanceTask sets state Validating to false" );
	}

	private static void reconstructArchitecture() {
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		analyseService.reconstructArchitecture_Initiate();
	}

	private int getNumberofDependenciesBetweenSoftwareUnits(String fromUnit, String toUnit) {
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		DependencyDTO[] foundDependencies = analyseService.getDependenciesFromSoftwareUnitToSoftwareUnit(fromUnit, toUnit);
		int numberOfDependencies = foundDependencies.length;
		return numberOfDependencies;
	}

	private int getNumberofDependenciesBetweenModulesInIntendedArchitecture(String fromModule, String toModule) {
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		IDefineService defineService = ServiceProvider.getInstance().getDefineService();
		HashSet<String> physicalFromClassPaths = defineService.getModule_AllPhysicalClassPathsOfModule(fromModule);
		HashSet<String> physicalToClassPaths = defineService.getModule_AllPhysicalClassPathsOfModule(toModule);
		ArrayList<DependencyDTO> allFoundDependencies = new ArrayList<DependencyDTO>();
		for (String fromPackages : physicalFromClassPaths) {
			for (String toPackages: physicalToClassPaths) {
				for (DependencyDTO dependency : analyseService.getDependenciesFromSoftwareUnitToSoftwareUnit(fromPackages, toPackages)) {
					allFoundDependencies.add(dependency);
				}
			}
		}
		int numberOfDependencies = allFoundDependencies.size();
		return numberOfDependencies;
	}

}