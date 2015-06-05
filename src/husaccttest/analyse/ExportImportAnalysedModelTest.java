package husaccttest.analyse;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AnalysisStatisticsDTO;
import husacct.common.dto.DependencyDTO;
import husacct.control.ControlServiceImpl;
import husacct.control.task.MainController;
import husacct.control.task.WorkspaceController;
import husaccttest.TestResourceFinder;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ExportImportAnalysedModelTest {
	private static String workspacePath;
	private static ControlServiceImpl controlService;
	private static MainController mainController;
	private static WorkspaceController workspaceController;
	private final static String workspace = "AccuracyTest-Java-2013-06-11.xml";
	private static Logger logger;
	private static IAnalyseService analyseService = null;
	
	private static final String exportFile = "ExportFileAnalysedModel.XML";
	private static String exportFilePath;
	private static AnalysisStatisticsDTO analyseStatisticsBeforeExport;
	private static AnalysisStatisticsDTO analyseStatisticsAfterImport;

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
			
			analyseStatisticsBeforeExport = getAnalyseStatistics();
			exportFilePath = TestResourceFinder.findHusacctExportFile("java", exportFile);
			exportAnalysisModel();

			controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
			mainController = controlService.getMainController();
			workspaceController = mainController.getWorkspaceController();
			workspaceController.closeWorkspace();
			loadWorkspace(workspacePath);
			getAnalyseStatistics(); //Needed to show statistics data in console
			importAnalysisModel();
			analyseStatisticsAfterImport = getAnalyseStatistics();

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
	public void ImportPackages(){
		Assert.assertTrue(analyseStatisticsAfterImport.totalNrOfPackages == analyseStatisticsBeforeExport.totalNrOfPackages);
	}

	@Test
	public void ImportClasses(){
		Assert.assertTrue(analyseStatisticsAfterImport.totalNrOfClasses == analyseStatisticsBeforeExport.totalNrOfClasses);
	}
	
	@Test
	public void ImportDependencies(){
		Assert.assertTrue(analyseStatisticsAfterImport.totalNrOfDependencies == analyseStatisticsBeforeExport.totalNrOfDependencies);
	}
	
	@Test
	public void ImportLinesOfCode(){
		Assert.assertTrue(analyseStatisticsAfterImport.totalNrOfLinesOfCode == analyseStatisticsBeforeExport.totalNrOfLinesOfCode);
	}
	
	// Tests Dependency Detection after Import
	
	@Test
	public void AccessClassVariableConstant(){
		String fromModule = "domain.direct.violating.AccessClassVariableConstant";
		String toModule = "technology.direct.dao.UserDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Class Variable Constant", false));
	}

	@Test
	public void AccessObjectReferenceAsParameter(){
		String fromModule = "domain.direct.violating.AccessObjectReferenceAsParameter";
		String toModule = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Reference");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Type of Variable", false));
	}

	@Test
	public void AnnotationDependency(){
		String fromModule = "domain.direct.violating.AnnotationDependency";
		String toModule = "technology.direct.dao.SettingsAnnotation";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Annotation");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "", false));
	}

	@Test
	public void CallInstanceInnerInterface(){
		String fromModule = "domain.direct.violating.CallInstanceInnerInterface";
		String toModule = "technology.direct.dao.CallInstanceOuterClassDAO.CallInstanceInnerInterfaceDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Interface Method", false));
	}

	@Test
	public void DeclarationOuterClassByStaticNestedClass(){
		String fromModule = "technology.direct.dao.CallInstanceOuterClassDAO.StaticNestedClass";
		String toModule = "technology.direct.dao.CallInstanceOuterClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Instance Variable", false));
	}

	@Test
	public void ImportDependencyStatic(){
		String fromModule = "domain.direct.violating.ImportDependencyStatic";
		String toModule = "technology.direct.dao.AccountDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void InheritanceExtends(){
		String fromModule = "domain.direct.violating.InheritanceExtends";
		String toModule = "technology.direct.dao.HistoryDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Extends Class", false));	}

	
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
	
	private static void exportAnalysisModel() {
		File file = new File(exportFilePath);
		controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
		mainController = controlService.getMainController();
		mainController.getExportController().exportAnalysisModel(file);
	}

	private static void importAnalysisModel() {
		File file = new File(exportFilePath);
		controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
		mainController = controlService.getMainController();
		mainController.getImportController().importAnalysisModel(file);
	}

	private boolean areDependencyTypesDetected(String moduleFrom, String moduleTo, ArrayList<String> dependencyTypes, boolean isIndirect) {
		return areDependencyTypesDetected(moduleFrom, moduleTo, dependencyTypes, "", isIndirect);
	}

	private boolean areDependencyTypesDetected(String moduleFrom, String moduleTo, ArrayList<String> dependencyTypes, String subType, boolean isIndirect) {
		boolean dependencyTypesDetected = false;
		TreeMap<String, Boolean> foundDependencyTypes = new TreeMap<String, Boolean>();
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		DependencyDTO[] foundDependencies = analyseService.getDependenciesFromTo(moduleFrom, moduleTo);
		int numberOfDependencies = foundDependencies.length;
		for (String dependencyType : dependencyTypes) {
			boolean found = false;
			for (int i=0 ; i < numberOfDependencies; i++){
				if (foundDependencies[i].type.equals(dependencyType) && (foundDependencies[i].isIndirect) == isIndirect) {
					if (!subType.equals("")) {
						if (foundDependencies[i].subType.equals(subType)) {
							found = true;	
						}
					} else {
						found = true;
					}
				}
			}
			foundDependencyTypes.put(dependencyType,found);
		}
		if (!foundDependencyTypes.containsValue(false)){
			dependencyTypesDetected = true;
		}
		return dependencyTypesDetected;
	}

}