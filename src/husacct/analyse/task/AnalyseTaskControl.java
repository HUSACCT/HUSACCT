package husacct.analyse.task;

import java.util.Date;

import org.apache.log4j.Logger;
import org.jdom2.Element;

import husacct.analyse.domain.IModelPersistencyService;
import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.task.analyser.ApplicationAnalyser;
import husacct.analyse.task.reconstruct.ReconstructArchitecture;
import husacct.analyse.task.reconstruct.ReconstructArchitectureDTOList;
import husacct.common.dto.AnalysisStatisticsDTO;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.dto.SoftwareUnitDTO;

public class AnalyseTaskControl {

    private boolean isAnalysed;
    private ApplicationAnalyser analyserService;
    private IModelPersistencyService persistencyService;
    private IModelQueryService queryService;
    private DependencyReportController reportController;
    private HistoryLogger historyLogger;
    private ReconstructArchitecture reconstructArchitecture;
    public ReconstructArchitectureDTOList reconstructArchitectureDTOList;
    
    private final Logger logger = Logger.getLogger(AnalyseTaskControl.class);


    public AnalyseTaskControl(IModelPersistencyService persistencyService, IModelQueryService queryService) {
        this.isAnalysed = false;
        this.persistencyService = persistencyService;
    	this.queryService = queryService;
        this.analyserService = new ApplicationAnalyser();
        this.reportController = new DependencyReportController(queryService);
        this.historyLogger = new HistoryLogger();
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
	
	public ReconstructArchitectureDTOList createReconstructArchitectureList(){
		reconstructArchitectureDTOList = new ReconstructArchitectureDTOList(queryService);
		return reconstructArchitectureDTOList;
	}
}
