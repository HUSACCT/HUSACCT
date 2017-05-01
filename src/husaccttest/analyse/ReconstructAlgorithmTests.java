package husaccttest.analyse;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants;
import husacct.analyse.task.reconstruct.dto.ReconstructArchitectureDTO;
import husacct.common.dto.AnalysisStatisticsDTO;
import husacct.common.dto.ModuleDTO;
import husacct.control.ControlServiceImpl;
import husacct.control.task.MainController;
import husacct.control.task.WorkspaceController;
import husacct.define.IDefineService;
import husaccttest.TestResourceFinder;

import org.junit.Test;

public class ReconstructAlgorithmTests {
	private static String workspacePath;
	private static ControlServiceImpl controlService;
	private static MainController mainController;
	private static WorkspaceController workspaceController;
	private final static String workspace = "CoCoMe_Workspace.xml";
	private static Logger logger;
	private static IAnalyseService analyseService = null;
	
	private static final String exportFile = "CoCoMe-AnalysisModel.xml";
	private static String exportFilePath;

	@BeforeClass
	public static void beforeClass() {
		try {
			setLog4jConfiguration();
			workspacePath = TestResourceFinder.findHusacctWorkspace("java", workspace);
			logger.info(" Start test: ReconstructAlgorithmTests");
			
			//Import analysed model
			controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
			mainController = controlService.getMainController();
			workspaceController = mainController.getWorkspaceController();
			workspaceController.closeWorkspace();
			loadWorkspace(workspacePath);
			getAnalyseStatistics();
			exportFilePath = TestResourceFinder.getExportFolderForTest("java") + exportFile;
			importAnalysisModel();
			getAnalyseStatistics();
			
			logger.info(String.format("Start: Architecture Reconstruction"));
			getAnalyseStatistics();
			reconstructArchitecture();
			getAnalyseStatistics();
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
	public static void tearDownClass(){
		controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
		mainController = controlService.getMainController();
		mainController.getWorkspaceController().closeWorkspace();
		logger.info(" Finished test: ReconstructAlgorithmTests");
	}
	
	@Test
	public void TestAlgorithms(){
		boolean algortithmSucces = false;
		boolean testResult = false;
		boolean totalResult = true;
		ArrayList<ReconstructArchitectureDTO> reconstructionArchitectureDTOs = createTestReconstructArchitectureDTOs();
		for (ReconstructArchitectureDTO dto : reconstructionArchitectureDTOs){
			testResult = false;
			try{
				logger.info("Algorithms: '" + dto.getName() + "' started");
				
				algortithmSucces = analyseService.reconstructArchitecture_Execute(dto);
				
				if (algortithmSucces){
					logger.info("Algorithms: '" + dto.getName() + "' " + "Tested succesfully");
					IDefineService defineService = ServiceProvider.getInstance().getDefineService();
					
					switch (dto.approachId) {
					case AnalyseReconstructConstants.Algorithms.Layers_Scanniello_Improved:
						ArrayList<String> shouldBeList = new ArrayList<>();
						shouldBeList.add("Layer1");
						shouldBeList.add("Layer2");
						shouldBeList.add("Layer3");
						
						ArrayList<String> nameList = new ArrayList<>();
						for(ModuleDTO moduleDTO : defineService.getAllModules()){
							nameList.add(moduleDTO.logicalPath);
						}
						if(nameList.containsAll(shouldBeList) &&shouldBeList.containsAll(nameList) ){
							testResult = true;
						}
						break;
					case AnalyseReconstructConstants.Algorithms.Layers_Goldstein_Root_Original:
						shouldBeList = new ArrayList<>();
						shouldBeList.add("Layer1");
						shouldBeList.add("Layer2");
						
						nameList = new ArrayList<>();
						for(ModuleDTO moduleDTO : defineService.getAllModules()){
							nameList.add(moduleDTO.logicalPath);
						}
						if(nameList.containsAll(shouldBeList) &&shouldBeList.containsAll(nameList) ){
							testResult = true;
						}
						
						break;
					case AnalyseReconstructConstants.Algorithms.Component_HUSACCT_SelectedModule:
						shouldBeList = new ArrayList<>();
						shouldBeList.add("org");
						
						
						nameList = new ArrayList<>();
						for(ModuleDTO moduleDTO : defineService.getAllModules()){
							nameList.add(moduleDTO.logicalPath);
						}
						if(nameList.containsAll(shouldBeList) &&shouldBeList.containsAll(nameList)){
							testResult = true;
						}
						break;
					}
					
					
					
					
				}else{
					logger.error("Algorithms: '" + dto.getName() + "' " + "Failed");
				}
				
			}catch(Exception e){
				logger.error("Error: " + e);
			}
			
			if (totalResult && !testResult){
				totalResult = false;
			}
			analyseService.reconstructArchitecture_ClearAll();
			String result = testResult ? " ran succesfully" : " FAILED";
			logger.info(dto.approachId + result);
			
		}
		Assert.assertTrue(totalResult);
	}
	
	private static ArrayList<ReconstructArchitectureDTO> createTestReconstructArchitectureDTOs(){
		ArrayList<ReconstructArchitectureDTO> testDtos = new ArrayList<>();
		
		//Scanniello - Improved
		ReconstructArchitectureDTO scannielloImproved = new ReconstructArchitectureDTO();
		scannielloImproved.approachId = AnalyseReconstructConstants.Algorithms.Layers_Scanniello_Improved;
		scannielloImproved.granularity = AnalyseReconstructConstants.Granularities.PackagesAndClasses;
		scannielloImproved.relationType = AnalyseReconstructConstants.RelationTypes.allDependencies;
		scannielloImproved.setSelectedModule(new ModuleDTO("**", "**", "package", new ModuleDTO[]{}));
		scannielloImproved.threshold = 10;
		testDtos.add(scannielloImproved);
		
		//Goldstein - Original
		ReconstructArchitectureDTO goldsteinOriginal = new ReconstructArchitectureDTO();
		goldsteinOriginal.approachId = AnalyseReconstructConstants.Algorithms.Layers_Goldstein_Root_Original;
		goldsteinOriginal.granularity = AnalyseReconstructConstants.Granularities.PackagesAndClasses;
		goldsteinOriginal.relationType = AnalyseReconstructConstants.RelationTypes.accessCallReferenceDependencies;
		goldsteinOriginal.threshold = 10;
		testDtos.add(goldsteinOriginal);
		
		//Component
		ReconstructArchitectureDTO component = new ReconstructArchitectureDTO();
		component.approachId = AnalyseReconstructConstants.Algorithms.Component_HUSACCT_SelectedModule;
		component.granularity = AnalyseReconstructConstants.Granularities.Classes;
		component.relationType = AnalyseReconstructConstants.RelationTypes.allDependencies;
		component.setSelectedModule(new ModuleDTO("**", "**", "package", new ModuleDTO[]{}));
		component.threshold = 10;
		testDtos.add(component);
		
		return testDtos;
	}
	
	
	
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
			if(workspaceController.isAWorkspaceOpened()){
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
	
	private static void reconstructArchitecture() {
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		analyseService.reconstructArchitecture_Initiate();
	}

	private static void checkConformance() {
		ServiceProvider.getInstance().getControlService().setValidating(true);
		logger.info(new Date().toString() + " CheckConformanceTask is Starting: IValidateService.checkConformance()" );
		ServiceProvider.getInstance().getValidateService().getCategories();
		ServiceProvider.getInstance().getValidateService().checkConformance();
		ServiceProvider.getInstance().getControlService().setValidating(false);
		logger.info(new Date().toString() + " CheckConformanceTask sets state Validating to false" );
	}
}
