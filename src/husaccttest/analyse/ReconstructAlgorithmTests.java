package husaccttest.analyse;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AnalysisStatisticsDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.dto.SoftwareUnitDTO;
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
	private static IDefineService defineService;
	private final static String workspace = "Workspace_HUSACCT_20_Arch_Without_ANTLR.xml";
	private static Logger logger;
	private static IAnalyseService analyseService = null;
	
	private static final String exportFile = "ExportFileAnalysedModel_HUSACCT20_WithoutAntlr.xml";
	private static String exportFilePath;
	@BeforeClass
	public static void beforeClass() {
		try {
			setLog4jConfiguration();
			workspacePath = TestResourceFinder.findHusacctWorkspace("java", workspace);
			logger.info(String.format("Running HUSACCT using workspace: " + workspacePath));
			
			defineService = ServiceProvider.getInstance().getDefineService();
			//Import analysed model
			controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
			mainController = controlService.getMainController();
			workspaceController = mainController.getWorkspaceController();
			workspaceController.closeWorkspace();
			loadWorkspace(workspacePath);
			getAnalyseStatistics();
			exportFilePath = TestResourceFinder.findHusacctExportFile("java", exportFile);
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
					Thread.sleep((long)10);
				} catch (InterruptedException e) {}
				isValidating = mainController.getStateController().isValidating();
			}
			
		} catch (Exception e){
			String errorMessage =  "Exception: " + e.getMessage();
			logger.warn(errorMessage);
		}
	}
	
	@Test
	public void TestAlgorithms(){
		boolean algortithmSucces = false;
		ArrayList<ReconstructArchitectureDTO> reconstructionArchitectureDTOs = createTestReconstructArchitectureDTOs();
		for (ReconstructArchitectureDTO dto : reconstructionArchitectureDTOs){
			try{
				logger.info("Algorithm: '" + dto.getName() + "' started");
				
				algortithmSucces = analyseService.reconstructArchitecture_Execute(dto);
				
				if (algortithmSucces){
					logger.info("Algorithm: '" + dto.getName() + "' " + "Tested succesfully");
				}else{
					logger.error("Algorithm: '" + dto.getName() + "' " + "Failed");
				}
				
			}catch(Exception e){
				logger.error("Error: " + e);
			}
			
			Assert.assertTrue(algortithmSucces);
		}
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
	
	private static void reconstructArchitecture() {
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		analyseService.reconstructArchitecture_Initiate();
	}

	private static void checkConformance() {
		ServiceProvider.getInstance().getControlService().setValidate(true);
		logger.info(new Date().toString() + " CheckConformanceTask is Starting: IValidateService.checkConformance()" );
		ServiceProvider.getInstance().getValidateService().getCategories();
		ServiceProvider.getInstance().getValidateService().checkConformance();
		ServiceProvider.getInstance().getControlService().setValidate(false);
		logger.info(new Date().toString() + " CheckConformanceTask sets state Validating to false" );
	}
	
	private static ArrayList<ReconstructArchitectureDTO> createTestReconstructArchitectureDTOs(){
		ArrayList<ReconstructArchitectureDTO> testDtos = new ArrayList<>();
		SoftwareUnitDTO firstSoftwareUnitFromRoot = getFirstSoftwareUnitFromRoot();
		ModuleDTO firstModuleFromRoot = defineService.getModule_BasedOnSoftwareUnitName(firstSoftwareUnitFromRoot.uniqueName);
				
		ReconstructArchitectureDTO ScannielloLayers_RootOriginal = new ReconstructArchitectureDTO();
		ScannielloLayers_RootOriginal.setApproach("Scanniello - originalRoot");
		ScannielloLayers_RootOriginal.setThreshold(10);
		ScannielloLayers_RootOriginal.setName("Scanniello Layers Root Original");
		testDtos.add(ScannielloLayers_RootOriginal);
		
		ReconstructArchitectureDTO ScannielloLayers_RootImprover = new ReconstructArchitectureDTO();
		ScannielloLayers_RootImprover.setApproach("Scanniello - improved");
		ScannielloLayers_RootImprover.setThreshold(10);
		ScannielloLayers_RootImprover.setName("Scanniello Layers Root Improved");
		testDtos.add(ScannielloLayers_RootImprover);

		ReconstructArchitectureDTO ScannielloLayers_SelectedModuleImprover = new ReconstructArchitectureDTO();
		ScannielloLayers_SelectedModuleImprover.setApproach("Scanniello - improved");
		ScannielloLayers_SelectedModuleImprover.setThreshold(10);
		ScannielloLayers_SelectedModuleImprover.setName("Scanniello Layers SelectedModule Improved");
		ScannielloLayers_SelectedModuleImprover.setSelectedModule(firstModuleFromRoot);
		testDtos.add(ScannielloLayers_SelectedModuleImprover);		
		
		ReconstructArchitectureDTO Gateway_SelectedModule = new ReconstructArchitectureDTO();
		Gateway_SelectedModule.setApproach("Gateway");
		Gateway_SelectedModule.setThreshold(10);
		Gateway_SelectedModule.setName("Gateway SelectedModule");
		Gateway_SelectedModule.setSelectedModule(firstModuleFromRoot);
		testDtos.add(Gateway_SelectedModule);	
		
		return testDtos;
	}
	
	private static SoftwareUnitDTO getFirstSoftwareUnitFromRoot(){
		SoftwareUnitDTO[] allSoftwareUntisFromRoot = analyseService.getSoftwareUnitsInRoot();
		SoftwareUnitDTO firstSoftwareUnitFromRoot = allSoftwareUntisFromRoot[0];
		return firstSoftwareUnitFromRoot;
	}
}
