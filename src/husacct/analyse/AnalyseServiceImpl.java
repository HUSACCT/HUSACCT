package husacct.analyse;

import java.util.Date;
import java.util.List;

import husacct.ServiceProvider;
import husacct.analyse.domain.AnalyseDomainServiceImpl;
import husacct.analyse.domain.IAnalyseDomainService;
import husacct.analyse.domain.IModelPersistencyService;
import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.domain.famix.FamixPersistencyServiceImpl;
import husacct.analyse.domain.famix.FamixQueryServiceImpl;
import husacct.analyse.presentation.AnalyseInternalFrame;
import husacct.analyse.task.AnalyseControlServiceImpl;
import husacct.analyse.task.HistoryLogger;
import husacct.analyse.task.IAnalyseControlService;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.AnalysisStatisticsDTO;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ProjectDTO;
import husacct.common.savechain.ISaveable;
import husacct.common.services.ObservableService;

import javax.swing.JInternalFrame;

import org.apache.log4j.Logger;
import org.jdom2.Element;

public class AnalyseServiceImpl extends ObservableService implements IAnalyseService, ISaveable {

    private IModelQueryService queryService;
    private IAnalyseDomainService domainService;
    private IModelPersistencyService persistencyService;
    //private IModelCreationService creationService;
	private IAnalyseControlService analyseApplicationService;
    private AnalyseInternalFrame analyseInternalFrame;
    private HistoryLogger historyLogger;
    private final Logger logger = Logger.getLogger(AnalyseServiceImpl.class);
    private boolean isAnalysed;

    public AnalyseServiceImpl() {
        this.queryService = new FamixQueryServiceImpl(); //Must be created as first, since it clears the model (needed in case of reloading workspaces). 
    	this.domainService = new AnalyseDomainServiceImpl(queryService);
        this.persistencyService = new FamixPersistencyServiceImpl(queryService);
        //this.creationService = new FamixCreationServiceImpl(); //to be used for analyseApplication()
        this.analyseApplicationService = new AnalyseControlServiceImpl(domainService);
        this.historyLogger = new HistoryLogger();
        this.analyseInternalFrame = null;
        this.isAnalysed = false;
    }

    @Override
    public String[] getAvailableLanguages() {
        return analyseApplicationService.getAvailableLanguages();
    }

	@Override
    public void analyseApplication(ProjectDTO project) {
        this.analyseApplicationService.analyseApplication((String[]) project.paths.toArray(new String[project.paths.size()]), project.programmingLanguage);
        this.analyseInternalFrame = new AnalyseInternalFrame();
        this.isAnalysed = true;
        super.notifyServiceListeners();
        this.logger.info(new Date().toString() + " Finished: Analyse Application; ServiceListeners notified; State isAnalysed = true");
    }

    @Override
    public boolean isAnalysed() {
        return this.isAnalysed;
    }

    @Override
    public JInternalFrame getJInternalFrame() {
        if (analyseInternalFrame == null) {
            analyseInternalFrame = new AnalyseInternalFrame();
        }
        return analyseInternalFrame;
    }

    @Override
    public AnalysedModuleDTO getModuleForUniqueName(String uniquename) {
        return analyseApplicationService.getModuleForUniqueName(uniquename);
    }

    @Override
    public String getSourceFilePathOfClass(String uniquename) {
    	return analyseApplicationService.getSourceFilePathOfClass(uniquename);
    }

    @Override
    public AnalysedModuleDTO[] getRootModules() {
        return analyseApplicationService.getRootModules();
    }
    
    @Override
    public AnalysedModuleDTO[] getChildModulesInModule(String from) {
        return analyseApplicationService.getChildModulesInModule(from);
    }

    @Override
    public AnalysedModuleDTO[] getChildModulesInModule(String from, int depth) {
        return analyseApplicationService.getChildModulesInModule(from, depth);
    }

    @Override
    public AnalysedModuleDTO getParentModuleForModule(String child) {
        return analyseApplicationService.getParentModuleForModule(child);
    }

    @Override
    public DependencyDTO[] getAllDependencies() {
        return analyseApplicationService.getAllDependencies();
    }
    
    @Override
    public List<String> getAllPhysicalClassPathsOfSoftwareUnit(String uniqueName){
    	return analyseApplicationService.getAllPhysicalClassPathsOfSoftwareUnit(uniqueName);
    }

    @Override
    public List<String> getAllPhysicalPackagePathsOfSoftwareUnit(String uniqueName){
    	return analyseApplicationService.getAllPhysicalPackagePathsOfSoftwareUnit(uniqueName);
    }

    @Override
	public DependencyDTO[] getAllUnfilteredDependencies() {
		return analyseApplicationService.getAllDependencies();
	}

    @Override
    public DependencyDTO[] getDependencies(String from, String to) {
        return analyseApplicationService.getDependencies(from, to);
    }

    @Override
    public DependencyDTO[] getDependencies(String from, String to, String[] dependencyFilter) {
        return analyseApplicationService.getDependencies(from, to, dependencyFilter);
    }

    @Override
    public DependencyDTO[] getDependenciesFrom(String from) {
        return analyseApplicationService.getDependenciesFrom(from);
    }

    @Override
    public DependencyDTO[] getDependenciesFrom(String from, String[] dependencyFilter) {
        return analyseApplicationService.getDependenciesFrom(from, dependencyFilter);
    }

    @Override
	public DependencyDTO[] getDependenciesFromTo(String classPathFrom, String classPathTo){
		return analyseApplicationService.getDependenciesFromTo(classPathFrom, classPathTo);
	}
	
    @Override
    public DependencyDTO[] getDependenciesTo(String to) {
        return analyseApplicationService.getDependenciesTo(to);
    }

    @Override
    public DependencyDTO[] getDependenciesTo(String to, String[] dependencyFilter) {
        return analyseApplicationService.getDependenciesTo(to, dependencyFilter);
    }

    @Override
    public void exportDependencies(String fullPath) {
        analyseApplicationService.exportDependencies(fullPath);
    }
    
    @Override
    public Element exportAnalysisModel() {
        this.logger.info(new Date().toString() + " Starting: Export Analysis Model");
        Element exportElement = persistencyService.exportAnalysisModel();
        this.logger.info(new Date().toString() + " Finished: Export Analysis Model");
    	return exportElement;
    }

    @Override
    public void importAnalysisModel(Element analyseElement) {
        this.logger.info(new Date().toString() + " Starting: Import Analysis Model");
    	persistencyService.importAnalysisModel(analyseElement);
        this.analyseInternalFrame = new AnalyseInternalFrame();
        this.isAnalysed = true;
        super.notifyServiceListeners();
        this.logger.info(new Date().toString() + " Finished: Import Analysis Model; State isAnalysed = true");
    }

	@Override
    public void reconstructArchitecture() {
    	// Waiting for implementation
    }
    
    // Used for the generic mechanism to save workspace data of all components; e.g. configuration settings  
	@Override // From ISaveable
	public Element getWorkspaceData() {
		return persistencyService.getWorkspaceData();
	}

    // Used for the generic mechanism to load workspace data of all components; e.g. configuration settings  
	@Override // From ISaveable
	public void loadWorkspaceData(Element rootElement) {
		persistencyService.loadWorkspaceData(rootElement);
	}

	@Override
	public void logHistory(ApplicationDTO applicationDTO, String workspaceName) {
		historyLogger.logHistory(applicationDTO, workspaceName);
	}
	
	@Override
	public AnalysisStatisticsDTO getAnalysisStatistics(AnalysedModuleDTO selectedModule) {
		return analyseApplicationService.getAnalysisStatistics(selectedModule);
	}

}
