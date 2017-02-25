package husaccttest.analyse;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AnalysisStatisticsDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.UmlLinkDTO;
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
	
	private static final String exportFileName = "ExportFileAnalysedModel.xml";
	private static String exportFilePath;
	private static File exportFile;

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
			exportFilePath = TestResourceFinder.getExportFolderForTest("java") + exportFileName;
			exportFile = new File(exportFilePath);
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
		try {
			workspaceController.closeWorkspace();
			exportFile.delete();
		} catch (Exception e){
			String errorMessage =  "Exception: " + e.getMessage();
			logger.warn(errorMessage);
		}
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
	
	@Test
	public void ImportUmlLinks(){
		Assert.assertTrue(analyseStatisticsAfterImport.totalNrOfUmlLinks == analyseStatisticsBeforeExport.totalNrOfUmlLinks);
	}
	
	// Tests Dependency Detection after Import
	
	@Test
	public void AccessClassVariableConstant(){
		String fromClass = "domain.direct.violating.AccessClassVariableConstant";
		String toClass = "technology.direct.dao.UserDAO";
		ArrayList<String> typesToFind = new ArrayList<>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Class Variable Constant", false));
	}

	@Test
	public void AccessObjectReferenceAsParameter(){
		String fromClass = "domain.direct.violating.AccessObjectReferenceAsParameter";
		String toClass = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<>();
		typesToFind.add("Reference");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Type of Variable", false));
	}

	@Test
	public void AnnotationDependency(){
		String fromClass = "domain.direct.violating.AnnotationDependency";
		String toClass = "technology.direct.dao.SettingsAnnotation";
		ArrayList<String> typesToFind = new ArrayList<>();
		typesToFind.add("Annotation");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "", false));
	}

	@Test
	public void CallInstanceInnerInterface(){
		String fromClass = "domain.direct.violating.CallInstanceInnerInterface";
		String toClass = "technology.direct.dao.CallInstanceOuterClassDAO.CallInstanceInnerInterfaceDAO";
		ArrayList<String> typesToFind = new ArrayList<>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Interface Method", false));
	}

	@Test
	public void DeclarationOuterClassByStaticNestedClass(){
		String fromClass = "technology.direct.dao.CallInstanceOuterClassDAO.StaticNestedClass";
		String toClass = "technology.direct.dao.CallInstanceOuterClassDAO";
		ArrayList<String> typesToFind = new ArrayList<>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Instance Variable", false));
	}

	@Test
	public void ImportDependencyStatic(){
		String fromClass = "domain.direct.violating.ImportDependencyStatic";
		String toClass = "technology.direct.dao.AccountDAO";
		ArrayList<String> typesToFind = new ArrayList<>();
		typesToFind.add("Import");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void InheritanceExtends(){
		String fromClass = "domain.direct.violating.InheritanceExtends";
		String toClass = "technology.direct.dao.HistoryDAO";
		ArrayList<String> typesToFind = new ArrayList<>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Extends Class", false));	}

	// UmlLinkTypes: Positive
	@Test
	public void UmlLinkType_InstanceVariableDeclaration_NotComposite(){
		String fromClass = "domain.direct.violating.DeclarationVariableInstance";
		String toClass = "technology.direct.dao.ProfileDAO";
		String fromAttribute = "pdao";
		boolean isComposite = false;
		String typeToFind = "Attribute";
		Assert.assertTrue(isUmlLinkDetected(fromClass, toClass, fromAttribute, isComposite, typeToFind));
	}

	@Test
	public void UmlLinkType_InstanceVariableDeclaration_Composite_Array(){
		String fromClass = "domain.direct.violating.DeclarationVariableInstance_GenericType_OneTypeParameter";
		String toClass = "technology.direct.dao.AccountDAO";
		String fromAttribute = "aDao";
		boolean isComposite = true;
		String typeToFind = "Attribute";
		Assert.assertTrue(isUmlLinkDetected(fromClass, toClass, fromAttribute, isComposite, typeToFind));
	}

	@Test
	public void UmlLinkType_InstanceVariableDeclaration_Composite_List(){
		String fromClass = "domain.direct.violating.DeclarationVariableInstance_GenericType_OneTypeParameter";
		String toClass = "technology.direct.dao.BadgesDAO";
		String fromAttribute = "bDao";
		boolean isComposite = true;
		String typeToFind = "Attribute";
		Assert.assertTrue(isUmlLinkDetected(fromClass, toClass, fromAttribute, isComposite, typeToFind));
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
			HashMap<String, Object> dataValues = new HashMap<>();
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
		controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
		mainController = controlService.getMainController();
		mainController.getExportImportController().exportAnalysisModel(exportFile);
	}

	private static void importAnalysisModel() {
		controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
		mainController = controlService.getMainController();
		mainController.getExportImportController().importAnalysisModel(exportFile);
	}

	private boolean areDependencyTypesDetected(String moduleFrom, String moduleTo, ArrayList<String> dependencyTypes, boolean isIndirect) {
		return areDependencyTypesDetected(moduleFrom, moduleTo, dependencyTypes, "", isIndirect);
	}

	private boolean areDependencyTypesDetected(String fromClass, String toClass, ArrayList<String> dependencyTypes, String subType, boolean isIndirect) {
		boolean dependencyTypesDetected = false;
		TreeMap<String, Boolean> foundDependencyTypes = new TreeMap<>();
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		DependencyDTO[] foundDependencies = analyseService.getDependenciesFromClassToClass(fromClass, toClass);
		int numberOfDependencies = foundDependencies.length;
		for (String dependencyType : dependencyTypes) {
			boolean found = false;
            for (DependencyDTO foundDependency : foundDependencies) {
                if (foundDependency.type.equals(dependencyType) && (foundDependency.isIndirect) == isIndirect) {
                    if (!subType.equals("")) {
                        if (foundDependency.subType.equals(subType)) {
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

	private boolean isUmlLinkDetected(String classFrom, String classTo, String attributeFrom, boolean isComposite, String linkType) {
		boolean umlLinkDetected = false;
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		UmlLinkDTO[]  umlLinkDTOs = analyseService.getUmlLinksFromClassToToClass(classFrom, classTo);
		for (UmlLinkDTO linkDTO : umlLinkDTOs) {
			if (linkDTO.from.equals(classFrom) && linkDTO.to.equals(classTo) && linkDTO.attributeFrom.equals(attributeFrom) && 
					(linkDTO.isComposite == isComposite) && linkDTO.type.equals(linkType)) {
				umlLinkDetected = true;
			}
		}
		return umlLinkDetected;
	}

}