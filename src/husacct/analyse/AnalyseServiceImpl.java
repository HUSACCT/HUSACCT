package husacct.analyse;

import husacct.analyse.presentation.AnalyseInternalFrame;
import husacct.analyse.task.AnalyseControlerServiceImpl;
import husacct.analyse.task.IAnalyseControlService;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ProjectDTO;
import husacct.common.services.ObservableService;

import javax.swing.JInternalFrame;

//TODO Add implement-clause  ISavable when the savechain is fixed
public class AnalyseServiceImpl extends ObservableService implements IAnalyseService {

    private IAnalyseControlService service;
    private AnalyseInternalFrame analyseInternalFrame;
    private boolean isAnalysed;

    public AnalyseServiceImpl() {
        this.service = new AnalyseControlerServiceImpl();
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
}
