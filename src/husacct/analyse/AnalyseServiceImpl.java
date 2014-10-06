package husacct.analyse;

import java.util.Date;
import java.util.List;

import husacct.analyse.domain.IModelPersistencyService;
import husacct.analyse.domain.famix.FamixPersistencyServiceImpl;
import husacct.analyse.presentation.AnalyseInternalFrame;
import husacct.analyse.task.AnalyseControlServiceImpl;
import husacct.analyse.task.HistoryLogger;
import husacct.analyse.task.IAnalyseControlService;
import husacct.analyse.task.TypeFilter;
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
    private IModelPersistencyService persistService;
    private AnalyseInternalFrame analyseInternalFrame;
    private HistoryLogger historyLogger;
    private final Logger logger = Logger.getLogger(AnalyseServiceImpl.class);
    private boolean isAnalysed;

    public AnalyseServiceImpl() {
        this.service = new AnalyseControlServiceImpl();
        this.persistService = new FamixPersistencyServiceImpl();
        this.historyLogger = new HistoryLogger();
        this.analyseInternalFrame = null;
        this.isAnalysed = false;
    }

    @Override
    public String[] getAvailableLanguages() {
        return service.getAvailableLanguages();
    }

    @Deprecated
    public void analyseApplication(String[] paths, String programmingLanguage) {
        service.analyseApplication(paths, programmingLanguage);
        this.analyseInternalFrame = new AnalyseInternalFrame();
        this.isAnalysed = true;
        super.notifyServiceListeners();
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
        return TypeFilter.filterDependencies(service.getAllDependencies());
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
        return TypeFilter.filterDependencies(service.getDependencies(from, to));
    }

    @Override
    public DependencyDTO[] getDependencies(String from, String to, String[] dependencyFilter) {
        return TypeFilter.filterDependencies(service.getDependencies(from, to, dependencyFilter));
    }

    @Override
    public DependencyDTO[] getDependenciesFrom(String from) {
        return TypeFilter.filterDependencies(service.getDependenciesFrom(from));
    }

    @Override
    public DependencyDTO[] getDependenciesFrom(String from, String[] dependencyFilter) {
        return TypeFilter.filterDependencies(service.getDependenciesFrom(from, dependencyFilter));
    }

    @Override
	public DependencyDTO[] getDependenciesFromTo(String classPathFrom, String classPathTo){
		return service.getDependenciesFromTo(classPathFrom, classPathTo);
	}
	
    @Override
    public DependencyDTO[] getDependenciesTo(String to) {
        return TypeFilter.filterDependencies(service.getDependenciesTo(to));
    }

    @Override
    public DependencyDTO[] getDependenciesTo(String to, String[] dependencyFilter) {
        return TypeFilter.filterDependencies(service.getDependenciesTo(to, dependencyFilter));
    }

    @Override
    public void exportDependencies(String fullPath) {
        service.exportDependencies(fullPath);
    }
    
	@Override
	public Element getWorkspaceData() {
		Element rootElement = new Element("rootElement");
		rootElement.addContent(persistService.saveModel());
		return rootElement;
	}

	@Override
	public void loadWorkspaceData(Element rootElement) {
		persistService.loadModel(rootElement.getChild("AnalysedApplication"));
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
