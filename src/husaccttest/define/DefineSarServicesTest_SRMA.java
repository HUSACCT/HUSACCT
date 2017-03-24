package husaccttest.define;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.RuleDTO;
import husacct.common.dto.SoftwareUnitDTO;
import husacct.common.enums.ModuleTypes;
import husacct.control.ControlServiceImpl;
import husacct.control.task.MainController;
import husacct.control.task.WorkspaceController;
import husacct.define.IDefineSarService;
import husacct.define.IDefineService;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.services.AppliedRuleDomainService;
import husaccttest.TestResourceFinder;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DefineSarServicesTest_SRMA {
	private static String workspacePath;
	private static ControlServiceImpl controlService;
	private static MainController mainController;
	private static WorkspaceController workspaceController;
	private final static String workspace = "SrmaTest-2014-11-12.xml";
	private static Logger logger;
	private static IAnalyseService analyseService = null;
	private static IDefineService defineService = null;
	private static IDefineSarService defineSarService = null;

	@BeforeClass
	public static void beforeClass() {
		try {
			setLog4jConfiguration();
			logger.info(String.format("Start: Define SarServices Test"));
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
		} catch (Exception e){
			String errorMessage =  "Exception: " + e.getMessage();
			logger.warn(errorMessage);
		}
	}

	@AfterClass
	public static void tearDown(){
		workspaceController.closeWorkspace();
		logger.info(String.format("Finished: Define SarServices Test"));
	}

	// TESTS 

	@Test
	public void addModuleTest() {
		defineService = ServiceProvider.getInstance().getDefineService();
		defineSarService = defineService.getSarService();
		ArrayList<SoftwareUnitDTO> newSoftwareUnits = new ArrayList<SoftwareUnitDTO>();
		SoftwareUnitDTO newUnit = new SoftwareUnitDTO("presentation.softwareunit1", "softwareunit1", "Package", "public");
		newSoftwareUnits.add(newUnit);
		defineSarService.addModule("TestModule1", "Presentation", "Layer", 1, newSoftwareUnits);
		ModuleDTO retrievedModule = defineService.getModule_BasedOnSoftwareUnitName("presentation.softwareunit1");
		Assert.assertTrue(retrievedModule.logicalPath.equals("Presentation.TestModule1"));
	}
	
	@Test
	public void editModuleTest_Name() {
		defineService = ServiceProvider.getInstance().getDefineService();
		defineSarService = defineService.getSarService();
		String logicalPathOld = defineService.getModule_BasedOnSoftwareUnitName("presentation.relationrules.notallowed").logicalPath;
		defineSarService.editModule(logicalPathOld, null, "NothingIsAllowed", 0, null);
		String logicalPathNew = defineService.getModule_BasedOnSoftwareUnitName("presentation.relationrules.notallowed").logicalPath;
		Assert.assertTrue(logicalPathNew.equals("Presentation.RelationRules.NothingIsAllowed"));
		defineSarService.editModule(logicalPathNew, null, "NotAllowed", 0, null);
		logicalPathNew = defineService.getModule_BasedOnSoftwareUnitName("presentation.relationrules.notallowed").logicalPath;
		Assert.assertTrue(logicalPathNew.equals("Presentation.RelationRules.NotAllowed"));
	}
	
	@Test
	public void editModuleTest_Type() {
		defineService = ServiceProvider.getInstance().getDefineService();
		defineSarService = defineService.getSarService();
		ModuleDTO module = defineService.getModule_BasedOnSoftwareUnitName("presentation.relationrules.notallowed");
		String logicalPathOfModule = module.logicalPath;
		String typeOfModule_Original = module.type;
		defineSarService.editModule(logicalPathOfModule, ModuleTypes.COMPONENT.toString(), null, 0, null);
		String typeOfModule_New = defineService.getModule_ByUniqueName(logicalPathOfModule).type;
		Assert.assertTrue(typeOfModule_New.equals(ModuleTypes.COMPONENT.toString()));
		defineSarService.editModule(logicalPathOfModule, ModuleTypes.LAYER.toString(), null, 0, null);
		typeOfModule_New = defineService.getModule_ByUniqueName(logicalPathOfModule).type;
		Assert.assertTrue(typeOfModule_New.equals(ModuleTypes.LAYER.toString()));
		defineSarService.editModule(logicalPathOfModule, typeOfModule_Original, null, 0, null);
		typeOfModule_New = defineService.getModule_ByUniqueName(logicalPathOfModule).type;
		Assert.assertTrue(typeOfModule_New.equals(typeOfModule_Original));
	}
	
	@Test
	public void editModuleTest_HierarchicalLevel() {
		defineService = ServiceProvider.getInstance().getDefineService();
		defineSarService = defineService.getSarService();
		int levelOld = defineService.getHierarchicalLevelOfLayer("Presentation");
		defineSarService.editModule("Presentation", null, null, levelOld + 3, null);
		int levelNew = defineService.getHierarchicalLevelOfLayer("Presentation");
		Assert.assertTrue(levelNew == levelOld + 3);
		defineSarService.editModule("Presentation", null, null, levelOld, null);
		levelNew = defineService.getHierarchicalLevelOfLayer("Presentation");
		Assert.assertTrue(levelNew == levelOld);
	}
	
	@Test
	public void editModuleTest_SoftwareUnits() {
		defineService = ServiceProvider.getInstance().getDefineService();
		defineSarService = defineService.getSarService();
		ArrayList<SoftwareUnitDTO> newSoftwareUnits = new ArrayList<SoftwareUnitDTO>();
		SoftwareUnitDTO newUnit = new SoftwareUnitDTO("presentation.relationrules.newsoftwareunit", "newsoftwareunit", "Package", "public");
		newSoftwareUnits.add(newUnit);
		defineSarService.editModule("Presentation.RelationRules.NotAllowed", null, null, 0, newSoftwareUnits);
		String logicalPathNew = defineService.getModule_BasedOnSoftwareUnitName("presentation.relationrules.newsoftwareunit").logicalPath;
		Assert.assertTrue(logicalPathNew.equals("Presentation.RelationRules.NotAllowed"));
		newUnit.uniqueName = "presentation.relationrules.notallowed";
		newUnit.name = "allowed";
		defineSarService.editModule("Presentation.RelationRules.NotAllowed", null, null, 0, newSoftwareUnits);
		logicalPathNew = defineService.getModule_BasedOnSoftwareUnitName("presentation.relationrules.notallowed").logicalPath;
		Assert.assertTrue(logicalPathNew.equals("Presentation.RelationRules.NotAllowed"));
	}
	
	@Test
	public void removeModuleTest() {
		defineService = ServiceProvider.getInstance().getDefineService();
		defineSarService = defineService.getSarService();
		defineSarService.removeModule("Presentation.TestModule1");
		ModuleDTO retrievedModule = defineService.getModule_BasedOnSoftwareUnitName("presentation.softwareunit1");
		Assert.assertFalse(retrievedModule.logicalPath.equals("Presentation.TestModule1"));
	}
	
	// Test case that checks if a main rule is added correctly.
	@Test
	public void addMainRuleTest() {
		defineService = ServiceProvider.getInstance().getDefineService();
		defineSarService = defineService.getSarService();
		defineSarService.addMainRule("Presentation", "Domain", "IsNotAllowedToUse");
		Assert.assertTrue(isRuleExisting("Presentation", "Domain", "IsNotAllowedToUse"));
	}
	
	// Test case that checks if a  main rule is not added if the ruleTypeKey is not correct.
	@Test
	public void addMainRuleTest_IncorrectRuleTypeKey() {
		defineService = ServiceProvider.getInstance().getDefineService();
		defineSarService = defineService.getSarService();
		defineSarService.addMainRule("Domain", "Presentation", "IsNotAllowedTotUse"); // Tot instead of To
		Assert.assertFalse(isRuleExisting("IsNotAllowedToUse", "Presentation", "Domain"));
	}
	
	// Test case that checks if a duplicate main rule is not added.
	@Test
	public void addMainRuleTest_DuplicateRuleTypeKey() {
		defineService = ServiceProvider.getInstance().getDefineService();
		defineSarService = defineService.getSarService();
		boolean rule1Added = defineSarService.addMainRule("Presentation", "Technology", "IsNotAllowedToUse");
		boolean rule2Added = defineSarService.addMainRule("Presentation", "Technology", "IsNotAllowedToUse");
		AppliedRuleDomainService appliedRuleService = new AppliedRuleDomainService();
		AppliedRuleStrategy rule = appliedRuleService.getAppliedMainRuleBy_From_To_RuleTypeKey("Presentation", "Technology", "IsNotAllowedToUse");
		boolean ruleFound = false;
		if (rule != null) ruleFound = true;
		Assert.assertTrue(rule1Added);
		Assert.assertFalse(rule2Added);
		Assert.assertTrue(ruleFound);
	}
	
	// Test case that checks if a rule is disabled.
	@Test
	public void editRuleTest_IsEnabled() {
		defineService = ServiceProvider.getInstance().getDefineService();
		defineSarService = defineService.getSarService();
		boolean ruleAdded = defineSarService.addMainRule("Technology.PropertyRules", "Technology.RelationRules", "IsNotAllowedToUse");
		boolean ruleEdited = defineSarService.editRule_IsEnabled("Technology.PropertyRules", "Technology.RelationRules", "IsNotAllowedToUse", false);
		AppliedRuleDomainService appliedRuleService = new AppliedRuleDomainService();
		AppliedRuleStrategy rule = appliedRuleService.getAppliedMainRuleBy_From_To_RuleTypeKey("Technology.PropertyRules", "Technology.RelationRules", "IsNotAllowedToUse");
		boolean ruleFound = false;
		boolean isEnabled = true;
		if (rule != null) { 
			ruleFound = true;
			isEnabled = rule.isEnabled();
		}
		Assert.assertTrue(ruleAdded);
		Assert.assertTrue(ruleEdited);
		Assert.assertTrue(ruleFound);
		Assert.assertFalse(isEnabled);

	}
	
	// Tests case that checks the number of found dependencies between modules in the intended architecture. 
	@Test
	public void CompareNumberOfDependenciesBetweenModules_Domain_Presentation(){
		String fromModule = "Domain";
		String toModule = "Presentation";
		int numberOfDependencies = getNumberofDependenciesBetweenModulesInIntendedArchitecture(fromModule, toModule);
		Assert.assertEquals("Incorrect number of dependencies", 16, numberOfDependencies);
	}
	
	//
	//private helpers
	//
	private static void setLog4jConfiguration() {
		URL propertiesFile = Class.class.getResource("/husacct/common/resources/log4j.properties");
		PropertyConfigurator.configure(propertiesFile);
		logger = Logger.getLogger(DefineSarServicesTest_SRMA.class);
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
