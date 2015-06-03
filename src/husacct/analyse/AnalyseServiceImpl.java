package husacct.analyse;

import java.util.Date;
import java.util.List;

import husacct.analyse.domain.IModelPersistencyService;
import husacct.analyse.domain.famix.FamixPersistencyServiceImpl;
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

    private IAnalyseControlService service ;
    private IModelPersistencyService persistencyService;
    private AnalyseInternalFrame analyseInternalFrame;
    private HistoryLogger historyLogger;
    private final Logger logger = Logger.getLogger(AnalyseServiceImpl.class);
    private boolean isAnalysed;

    public AnalyseServiceImpl() {
        this.service = new AnalyseControlServiceImpl();
        this.persistencyService = new FamixPersistencyServiceImpl();
        this.historyLogger = new HistoryLogger();
        this.analyseInternalFrame = null;
        this.isAnalysed = false;
    }

    @Override
    public String[] getAvailableLanguages() {
        return service.getAvailableLanguages();
    }

	@Override
    public void analyseApplication(ProjectDTO project) {
        this.service.analyseApplication((String[]) project.paths.toArray(new String[project.paths.size()]), project.programmingLanguage);
        this.logger.info(new Date().toString() + " Finished: IAnalyseControlService.analyseApplication()");
        this.analyseInternalFrame = new AnalyseInternalFrame();
        this.logger.info(new Date().toString() + " Finished: creation analyseInternalFrame");
        this.isAnalysed = true;
        super.notifyServiceListeners();
        this.logger.info(new Date().toString() + " Finished: notifying ServiceListeners; this.isAnalysed = true");
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
        return service.getModuleForUniqueName(uniquename);
    }

    @Override
    public String getSourceFilePathOfClass(String uniquename) {
    	return service.getSourceFilePathOfClass(uniquename);
    }

    @Override
    public AnalysedModuleDTO[] getRootModules() {
        return service.getRootModules();
    }
    
    @Override
    public AnalysedModuleDTO[] getChildModulesInModule(String from) {
        return service.getChildModulesInModule(from);
    }

    @Override
    public AnalysedModuleDTO[] getChildModulesInModule(String from, int depth) {
        return service.getChildModulesInModule(from, depth);
    }

    @Override
    public AnalysedModuleDTO getParentModuleForModule(String child) {
        return service.getParentModuleForModule(child);
    }

    @Override
    public DependencyDTO[] getAllDependencies() {
        return service.getAllDependencies();
    }
    
    @Override
    public List<String> getAllPhysicalClassPathsOfSoftwareUnit(String uniqueName){
    	return service.getAllPhysicalClassPathsOfSoftwareUnit(uniqueName);
    }

    @Override
    public List<String> getAllPhysicalPackagePathsOfSoftwareUnit(String uniqueName){
    	return service.getAllPhysicalPackagePathsOfSoftwareUnit(uniqueName);
    }

    @Override
	public DependencyDTO[] getAllUnfilteredDependencies() {
		return service.getAllDependencies();
	}

    @Override
    public DependencyDTO[] getDependencies(String from, String to) {
        return service.getDependencies(from, to);
    }

    @Override
    public DependencyDTO[] getDependencies(String from, String to, String[] dependencyFilter) {
        return service.getDependencies(from, to, dependencyFilter);
    }

    @Override
    public DependencyDTO[] getDependenciesFrom(String from) {
        return service.getDependenciesFrom(from);
    }

    @Override
    public DependencyDTO[] getDependenciesFrom(String from, String[] dependencyFilter) {
        return service.getDependenciesFrom(from, dependencyFilter);
    }

    @Override
	public DependencyDTO[] getDependenciesFromTo(String classPathFrom, String classPathTo){
		return service.getDependenciesFromTo(classPathFrom, classPathTo);
	}
	
    @Override
    public DependencyDTO[] getDependenciesTo(String to) {
        return service.getDependenciesTo(to);
    }

    @Override
    public DependencyDTO[] getDependenciesTo(String to, String[] dependencyFilter) {
        return service.getDependenciesTo(to, dependencyFilter);
    }

    @Override
    public void exportDependencies(String fullPath) {
        service.exportDependencies(fullPath);
    }
    
    public Element exportAnalysisModel() {
    	return persistencyService.exportAnalysisModel();
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
		return service.getAnalysisStatistics(selectedModule);
	}

}
