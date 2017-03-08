package husaccttest.define;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.RuleDTO;
import husacct.control.ControlServiceImpl;
import husacct.control.task.MainController;
import husacct.control.task.WorkspaceController;
import husacct.define.IDefineService;
import husaccttest.TestResourceFinder;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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
			logger.info(String.format("Start: Define Services Test"));
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
		} catch (Exception e){
			String errorMessage =  "Exception: " + e.getMessage();
			logger.warn(errorMessage);
		}
	}

	@AfterClass
	public static void tearDown(){
		workspaceController.closeWorkspace();
		logger.info(String.format("Finished: Define Services Test"));
	}

	// TESTS 

	@Test
	public void getAssignedSoftwareUnitsOfModuleTest() {
		defineService = ServiceProvider.getInstance().getDefineService();
		Set<String> assignedSUs = defineService.getAssignedSoftwareUnitsOfModule("Domain.RelationRules.IsNotAllowedToUse");
		boolean su1 = false;
		boolean noOtherSUs = false;
		if (assignedSUs.size() == 1) {
			noOtherSUs = true;
			for (String assignedSU : assignedSUs) {
				if (assignedSU.equals("domain.relationrules.is_not_allowed_to_use")) su1 = true;
			}
		}
		Assert.assertTrue(su1 && noOtherSUs);
	}
	
	
	@Test
	public void getModule_AllPhysicalPackagePathsOfModuleTest() {
		defineService = ServiceProvider.getInstance().getDefineService();
		Set<String> assignedSUs = defineService.getModule_AllPhysicalPackagePathsOfModule("Domain.RelationRules.IsNotAllowedToUse");
		boolean su1 = false;
		boolean su2 = false;
		boolean noOtherSUs = false;
		if (assignedSUs.size() == 2) {
			noOtherSUs = true;
			for (String assignedSU : assignedSUs) {
				if (assignedSU.equals("domain.relationrules.is_not_allowed_to_use.violating")) su1 = true;
				if (assignedSU.equals("domain.relationrules.is_not_allowed_to_use.violating.exception")) su2 = true;
			}
		}
		Assert.assertTrue(su1 && su2 && noOtherSUs);
	}
	
	@Test
	public void getModule_AllPhysicalClassPathsOfModule() {
		defineService = ServiceProvider.getInstance().getDefineService();
		Set<String> assignedSUs = defineService.getModule_AllPhysicalClassPathsOfModule("Domain.RelationRules.IsNotAllowedToUse");
		boolean su1A = false;
		boolean su11A = false;
		boolean su111A = false;
		boolean noOtherSUs = false;
		if (assignedSUs.size() == 10) {
			noOtherSUs = true;
			for (String assignedSU : assignedSUs) {
				if (assignedSU.equals("domain.relationrules.is_not_allowed_to_use.Access_2")) su1A = true;
				if (assignedSU.equals("domain.relationrules.is_not_allowed_to_use.violating.ViolatingAccess_2")) su11A = true;
				if (assignedSU.equals("domain.relationrules.is_not_allowed_to_use.violating.exception.ViolatingAccess_2Exc")) su111A = true;
			}
		}
		Assert.assertTrue(su1A && su11A && su111A && noOtherSUs);
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

	private boolean isRuleExisting(String moduleFromLogicalPath, String moduleTologicalPath, String ruleTypeKey) {
		RuleDTO[] definedRules = defineService.getDefinedRules();
		boolean ruleFound = false;
		for (RuleDTO definedRule : definedRules) {
			if (definedRule.moduleFrom.logicalPath.equals(moduleFromLogicalPath) && definedRule.moduleTo.logicalPath.equals(moduleTologicalPath) && definedRule.ruleTypeKey.equals(ruleTypeKey)) {
				ruleFound = true;
			}
		}
		return ruleFound;
	}
}
