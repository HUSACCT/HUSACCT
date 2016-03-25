package husacct.analyse.task;

import java.util.Date;

import org.apache.log4j.Logger;
import org.jdom2.Element;

import husacct.analyse.domain.IModelPersistencyService;
import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.task.analyser.ApplicationAnalyser;
import husacct.common.dto.ApplicationDTO;

public class AnalyseTaskControl {

    private boolean isAnalysed;
    private ApplicationAnalyser analyserService;
    private IModelPersistencyService persistencyService;
    private IModelQueryService queryService;
    private DependencyReportController reportController;
    private HistoryLogger historyLogger;

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
    
    public void reconstructArchitecture() {
    	//new ReconstructArchitecture(queryService);
    }
}
