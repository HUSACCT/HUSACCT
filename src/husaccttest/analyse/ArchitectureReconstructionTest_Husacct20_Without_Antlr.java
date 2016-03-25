package husaccttest.analyse;

import husacct.ServiceProvider;
import husacct.analyse.serviceinterface.IAnalyseService;
import husacct.analyse.serviceinterface.dto.AnalysisStatisticsDTO;
import husacct.analyse.serviceinterface.dto.DependencyDTO;
import husacct.control.ControlServiceImpl;
import husacct.control.task.MainController;
import husacct.control.task.WorkspaceController;
import husacct.define.IDefineService;
import husaccttest.TestResourceFinder;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ArchitectureReconstructionTest_Husacct20_Without_Antlr {
	private static String workspacePath;
	private static ControlServiceImpl controlService;
	private static MainController mainController;
	private static WorkspaceController workspaceController;
	private final static String workspace = "Workspace_HUSACCT_20_Arch_Without_ANTLR.xml";
	private static Logger logger;
	private static IAnalyseService analyseService = null;
	
	private static final String exportFile = "ExportFileAnalysedModel_HUSACCT20_WithoutAntlr.xml";
	private static String exportFilePath;
	private static AnalysisStatisticsDTO analyseStatisticsBeforeReconstruction;
	private static AnalysisStatisticsDTO analyseStatisticsAfterReconstruction;

	@BeforeClass
	public static void beforeClass() {
		try {
			setLog4jConfiguration();
			workspacePath = TestResourceFinder.findHusacctWorkspace("java", workspace);
			logger.info(String.format("Running HUSACCT using workspace: " + workspacePath));

			//Import analysed model
			controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
			mainController = controlService.getMainController();
			workspaceController = mainController.getWorkspaceController();
			workspaceController.closeWorkspace();
			loadWorkspace(workspacePath);
			analyseStatisticsBeforeReconstruction = getAnalyseStatistics();
			exportFilePath = TestResourceFinder.findHusacctExportFile("java", exportFile);
			importAnalysisModel();
			analyseStatisticsAfterReconstruction = getAnalyseStatistics();
			
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
	
	
	//
	//private helpers
	//
	private static void setLog4jConfiguration() {
		URL propertiesFile = Class.class.getResource("/husacct/common/resources/log4j.properties");
		PropertyConfigurator.configure(propertiesFile);
		logger = Logger.getLogger(ExportImportAnalysedModelTest.class);
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

	private static AnalysisStatisticsDTO getAnalyseStatistics() {
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		AnalysisStatisticsDTO statistics = analyseService.getAnalysisStatistics(null);
		logger.info(String.format("Statistics - Packages: " + statistics.totalNrOfPackages + ", Classes: " + statistics.totalNrOfClasses + ", Dependencies: " + statistics.totalNrOfDependencies));
		return statistics;
	}
	
	private static void importAnalysisModel() {
		File file = new File(exportFilePath);
		controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
		mainController = controlService.getMainController();
		mainController.getExportImportController().importAnalysisModel(file);
	}

	private static void checkConformance() {
		ServiceProvider.getInstance().getControlService().setValidate(true);
		logger.info(new Date().toString() + " CheckConformanceTask is Starting: IValidateService.checkConformance()" );
		ServiceProvider.getInstance().getValidateService().getCategories();
		ServiceProvider.getInstance().getValidateService().checkConformance();
		ServiceProvider.getInstance().getControlService().setValidate(false);
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