package husacct.analyse;

import husacct.analyse.domain.IModelPersistencyService;
import husacct.analyse.domain.famix.FamixPersistencyServiceImpl;
import husacct.analyse.presentation.AnalyseInternalFrame;
import husacct.analyse.task.AnalyseControlerServiceImpl;
import husacct.analyse.task.HistoryLogger;
import husacct.analyse.task.IAnalyseControlService;
import husacct.analyse.task.TypeFilter;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ExternalSystemDTO;
import husacct.common.dto.ProjectDTO;
import husacct.common.savechain.ISaveable;
import husacct.common.services.ObservableService;

import javax.swing.JInternalFrame;

import org.jdom2.Element;

public class AnalyseServiceImpl extends ObservableService implements IAnalyseService, ISaveable {

    private IAnalyseControlService service;
    private IModelPersistencyService persistService;
    private AnalyseInternalFrame analyseInternalFrame;
    private HistoryLogger historyLogger;
    private boolean isAnalysed;

    public AnalyseServiceImpl() {
        this.service = new AnalyseControlerServiceImpl();
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
        service.analyseApplication((String[]) project.paths.toArray(new String[project.paths.size()]), project.programmingLanguage);
        this.analyseInternalFrame = new AnalyseInternalFrame();
        this.isAnalysed = true;
        super.notifyServiceListeners();
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
    public AnalysedModuleDTO[] getRootModules() {
        return service.getRootModules();
    }
    
    @Override
    public AnalysedModuleDTO[] getRootModulesWithExternalSystems(){
    	return service.getRootModulesWithExternalSystems();
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
	public ExternalSystemDTO[] getExternalSystems(){
		return TypeFilter.filterExternalSystems(service.getExternalSystems());
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
	public int getAmountOfDependencies() {
    	return service.getAmountOfDependencies();
    }
    
	@Override
    public int getAmountOfInterfaces() {
    	return service.getAmountOfInterfaces();
    }
    
	@Override
    public int getAmountOfPackages() {
    	return service.getAmountOfPackages();
    }
    
	@Override
    public int getAmountOfClasses() {
    	return service.getAmountOfClasses();
    }
    
	@Override
	public int buildCache(){
		return service.buildCache();
	}
}
