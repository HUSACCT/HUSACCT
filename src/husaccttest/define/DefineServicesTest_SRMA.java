package husaccttest.define;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AnalysisStatisticsDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.RuleDTO;
import husacct.common.dto.SoftwareUnitDTO;
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
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DefineServicesTest_SRMA {
	private static String workspacePath;
	private static ControlServiceImpl controlService;
	private static MainController mainController;
	private static WorkspaceController workspaceController;
	private final static String workspace = "SrmaTest-2014-11-12.xml";
	private static Logger logger;
	private static IAnalyseService analyseService = null;
	private static IDefineService defineService = null;

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
					Thread.sleep((long)10);
				} catch (InterruptedException e) {}
				isAnalysing = mainController.getStateController().isAnalysing();
			}
			
			logger.info(String.format("Start: Define Services Test"));

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

	@Test
	public void editModuleTest_HierarchicalLevel() {
		defineService = ServiceProvider.getInstance().getDefineService();
		int levelOld = defineService.getHierarchicalLevelOfLayer("Presentation");
		defineService.editModule("Presentation", null, levelOld + 3, null);
		int levelNew = defineService.getHierarchicalLevelOfLayer("Presentation");
		Assert.assertTrue(levelNew == levelOld + 3);
		defineService.editModule("Presentation", null, levelOld, null);
		levelNew = defineService.getHierarchicalLevelOfLayer("Presentation");
		Assert.assertTrue(levelNew == levelOld);
	}
	
	@Test
	public void editModuleTest_Name() {
		defineService = ServiceProvider.getInstance().getDefineService();
		String logicalPathOld = defineService.getModule_BasedOnSoftwareUnitName("presentation.relationrules.notallowed").logicalPath;
		defineService.editModule(logicalPathOld, "NothingIsAllowed", 0, null);
		String logicalPathNew = defineService.getModule_BasedOnSoftwareUnitName("presentation.relationrules.notallowed").logicalPath;
		Assert.assertTrue(logicalPathNew.equals("Presentation.RelationRules.NothingIsAllowed"));
		defineService.editModule(logicalPathNew, "NotAllowed", 0, null);
		logicalPathNew = defineService.getModule_BasedOnSoftwareUnitName("presentation.relationrules.notallowed").logicalPath;
		Assert.assertTrue(logicalPathNew.equals("Presentation.RelationRules.NotAllowed"));
	}
	
	@Test
	public void editModuleTest_SoftwareUnits() {
		defineService = ServiceProvider.getInstance().getDefineService();
		HashSet<String> softwarePackagesOld = defineService.getModule_AllPhysicalPackagePathsOfModule("Presentation.RelationRules.NotAllowed");
		ArrayList<SoftwareUnitDTO> newSoftwareUnits = new ArrayList<SoftwareUnitDTO>();
		SoftwareUnitDTO newUnit = new SoftwareUnitDTO("presentation.relationrules.newsoftwareunit", "newsoftwareunit", "Package", "public");
		newSoftwareUnits.add(newUnit);
		defineService.editModule("Presentation.RelationRules.NotAllowed", null, 0, newSoftwareUnits);
		String logicalPathNew = defineService.getModule_BasedOnSoftwareUnitName("presentation.relationrules.newsoftwareunit").logicalPath;
		Assert.assertTrue(logicalPathNew.equals("Presentation.RelationRules.NotAllowed"));
		newUnit.uniqueName = "presentation.relationrules.notallowed";
		newUnit.name = "allowed";
		defineService.editModule("Presentation.RelationRules.NotAllowed", null, 0, newSoftwareUnits);
		logicalPathNew = defineService.getModule_BasedOnSoftwareUnitName("presentation.relationrules.notallowed").logicalPath;
		Assert.assertTrue(logicalPathNew.equals("Presentation.RelationRules.NotAllowed"));
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
		URL propertiesFile = Class.class.getResource("/husacct/common/resources/log4j.properties");
		PropertyConfigurator.configure(propertiesFile);
		logger = Logger.getLogger(DefineServicesTest_SRMA.class);
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

	private int getNumberofDependenciesBetweenSoftwareUnits(String fromUnit, String toUnit) {
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		DependencyDTO[] foundDependencies = analyseService.getDependenciesFromSoftwareUnitToSoftwareUnit(fromUnit, toUnit);
		int numberOfDependencies = foundDependencies.length;
		return numberOfDependencies;
	}

	private int getNumberofDependenciesBetweenModulesInIntendedArchitecture(String fromModule, String toModule) {
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		defineService = ServiceProvider.getInstance().getDefineService();
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