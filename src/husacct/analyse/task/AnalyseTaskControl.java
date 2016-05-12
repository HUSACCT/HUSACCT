package husacct.analyse.task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.jdom2.Element;

import husacct.ServiceProvider;
import husacct.analyse.domain.IModelPersistencyService;
import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.task.analyser.ApplicationAnalyser;
import husacct.analyse.task.reconstruct.ReconstructArchitecture;
import husacct.analyse.task.reconstruct.mojo.MoJo;
import husacct.common.dto.AnalysisStatisticsDTO;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.dto.ReconstructArchitectureListDTO;
import husacct.common.dto.SoftwareUnitDTO;
import husacct.common.locale.ILocaleService;
import husacct.define.IDefineService;

public class AnalyseTaskControl {

    private boolean isAnalysed;
    private ApplicationAnalyser analyserService;
    private IModelPersistencyService persistencyService;
    private IModelQueryService queryService;
    private DependencyReportController reportController;
    private HistoryLogger historyLogger;
    private ReconstructArchitecture reconstructArchitecture;
    private ILocaleService localeService;
    public ReconstructArchitectureListDTO reconstructArchitectureListDTO;
    
    private final Logger logger = Logger.getLogger(AnalyseTaskControl.class);


    public AnalyseTaskControl(IModelPersistencyService persistencyService, IModelQueryService queryService) {
        this.isAnalysed = false;
        this.persistencyService = persistencyService;
    	this.queryService = queryService;
        this.analyserService = new ApplicationAnalyser();
        this.reportController = new DependencyReportController(queryService);
        this.historyLogger = new HistoryLogger();
        this.localeService = ServiceProvider.getInstance().getLocaleService();
     }

    public void analyseApplication(String[] paths, String programmingLanguage) {
    	queryService.clearModel();
        analyserService.analyseApplication(paths, programmingLanguage);
        queryService.buildCache();
        this.isAnalysed = true;
        this.logger.info(new Date().toString() + " Finished: Analyse Application; ServiceListeners notified; State isAnalysed = true");
    }

    public Element exportAnalysisModel() {
        this.logger.info(new Date().toString() + " Starting: Export Analysis Model");
        Element exportElement = persistencyService.exportAnalysisModel();
        this.logger.info(new Date().toString() + " Finished: Export Analysis Model");
    	return exportElement;
    }

    public String[] getAvailableLanguages() {
        return analyserService.getAvailableLanguages();
    }

    public void importAnalysisModel(Element analyseElement) {
        this.logger.info(new Date().toString() + " Starting: Import Analysis Model");
    	persistencyService.importAnalysisModel(analyseElement);
        this.isAnalysed = true;
        this.logger.info(new Date().toString() + " Finished: Import Analysis Model; State isAnalysed = true");
    }

    public boolean isAnalysed() {
        return this.isAnalysed;
    }

	public void logHistory(ApplicationDTO applicationDTO, String workspaceName) {
		historyLogger.logHistory(applicationDTO, workspaceName);
	}
	
    public void createDependencyReport(String path) {
        reportController.createDependencyReport(path);
    }
    
    //method for RecontructArchitecture
    public void reconstructArchitecture_Initiate() {
    	reconstructArchitecture = new ReconstructArchitecture(queryService);
    }

    public void reconstructArchitecture_Execute(ReconstructArchitectureDTO dto){
    	if (reconstructArchitecture == null) {
    		reconstructArchitecture = new ReconstructArchitecture(queryService);
    	}
    	reconstructArchitecture.reconstructArchitecture_Execute(dto);
	}
    
    public boolean getAlgorithmSucces(){
    	return reconstructArchitecture.getAlgorithmSucces();
    }

	public void reconstructArchitecture_Reverse(){
		reconstructArchitecture.reverseReconstruction();
	}
    
	public void reconstructArchitecture_ClearAll(){
		reconstructArchitecture.clearAllModules();
	}
	
	public void testAlgorithm(ReconstructArchitectureDTO dto){
		//gets the golden standard (current intended architecture) and writes it to a file
		ModuleDTO[] intendedArchitecture = reconstructArchitecture.getGoldenStandard();
		File goldenStandard = exportToRSF(intendedArchitecture, "C:\\users\\jorns\\desktop\\Golden_Standard");
		
		if (reconstructArchitecture == null) {
    		reconstructArchitecture = new ReconstructArchitecture(queryService);
    	}
		
		//reset intended architecture 
		reconstructArchitecture.clearAllModules();
		
		//runs the selected algorithm and writes it
    	reconstructArchitecture.reconstructArchitecture_Execute(dto);
    	intendedArchitecture = reconstructArchitecture.getGoldenStandard();
    	File toCompare = exportToRSF(intendedArchitecture, "C:\\users\\jorns\\desktop\\To_Compare");
    	
    	//mojo Call (not yet implemented)
    	MoJo mojoTest = new MoJo();
    	String[] daoArray = {toCompare.getName(), goldenStandard.getName(), "-fm"}; //"-fm is a different execution of mojo, see MoJo.java.showerrormessage() for more functions"
    	mojoTest.executeMojo(daoArray, localeService.getTranslatedString(dto.getApproach()));
    	
    	//remove the created files
    	goldenStandard.delete();
    	toCompare.delete();
	}
	
	private File exportToRSF(ModuleDTO[] intendedArchitecture, String fileName){
		IDefineService defineService = ServiceProvider.getInstance().getDefineService();
		File exportFile = null;
		try {
			String completeFileName = fileName + ".rsf";
			exportFile = new File(completeFileName);
			exportFile.createNewFile();
			FileWriter writer = new FileWriter(exportFile);
			String toWrite = "";
			for(ModuleDTO moduleDTO : intendedArchitecture){
				for(String softwareDTOPath : defineService.getAssignedSoftwareUnitsOfModule(moduleDTO.logicalPath)){
					toWrite = "contain " + moduleDTO.logicalPath + " " + softwareDTOPath + "\n";
					writer.write(toWrite);
				}
			}
			
			writer.flush();
			writer.close();
		} catch (IOException e) {
			logger.info("Error occured while writing the file");
		}
		return exportFile;
	}
    
	//Methods for AnalyseUIController
	public SoftwareUnitDTO[] getSoftwareUnitsInRoot() {
		return queryService.getSoftwareUnitsInRoot();
	}

	public SoftwareUnitDTO[] getChildUnitsOfSoftwareUnit(String from) {
		return queryService.getChildUnitsOfSoftwareUnit(from);
	}

	public DependencyDTO[] getDependenciesFromSoftwareUnitToSoftwareUnit(String pathFrom, String pathTo) {
		return queryService.getDependenciesFromSoftwareUnitToSoftwareUnit(pathFrom, pathTo);
	}

	public AnalysisStatisticsDTO getAnalysisStatistics(SoftwareUnitDTO selectedModule) {
		return queryService.getAnalysisStatistics(selectedModule);
	}
	
	public ReconstructArchitectureListDTO createReconstructArchitectureList(){
		reconstructArchitectureListDTO = new ReconstructArchitectureListDTO(queryService);
		return reconstructArchitectureListDTO;
	}
}
