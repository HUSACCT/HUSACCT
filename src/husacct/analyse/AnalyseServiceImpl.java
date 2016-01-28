package husacct.analyse;

import java.util.HashSet;
import java.util.List;

import husacct.analyse.domain.IModelPersistencyService;
import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.domain.famix.FamixPersistencyServiceImpl;
import husacct.analyse.domain.famix.FamixQueryServiceImpl;
import husacct.analyse.presentation.AnalyseInternalFrame;
import husacct.analyse.service.UmlLinkDTO;
import husacct.analyse.task.AnalyseTaskControl;
import husacct.common.dto.SoftwareUnitDTO;
import husacct.common.dto.AnalysisStatisticsDTO;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ProjectDTO;
import husacct.common.savechain.ISaveable;
import husacct.common.services.ObservableService;

import javax.swing.JInternalFrame;

import org.jdom2.Element;

public class AnalyseServiceImpl extends ObservableService implements IAnalyseService, ISaveable {

    private IModelQueryService queryService;
    private IModelPersistencyService persistencyService;
	private AnalyseTaskControl analyseTaskControl;
    private AnalyseInternalFrame analyseInternalFrame;

    public AnalyseServiceImpl() {
        this.queryService = new FamixQueryServiceImpl(); //Must be created as first, since it clears the model (needed in case of reloading workspaces). 
        this.persistencyService = new FamixPersistencyServiceImpl(queryService);
        this.analyseTaskControl = new AnalyseTaskControl(persistencyService, queryService);
        this.analyseInternalFrame = null;
    }

    @Override
    public String[] getAvailableLanguages() {
        return analyseTaskControl.getAvailableLanguages();
    }

	@Override
    public void analyseApplication(ProjectDTO project) {
        this.analyseTaskControl.analyseApplication((String[]) project.paths.toArray(new String[project.paths.size()]), project.programmingLanguage);
        this.analyseInternalFrame = new AnalyseInternalFrame();
        super.notifyServiceListeners();
    }

    @Override
    public boolean isAnalysed() {
        return analyseTaskControl.isAnalysed();
    }

    @Override
    public JInternalFrame getJInternalFrame() {
        if (analyseInternalFrame == null) {
            analyseInternalFrame = new AnalyseInternalFrame();
        }
        return analyseInternalFrame;
    }

    @Override
    public SoftwareUnitDTO getSoftwareUnitByUniqueName(String uniquename) {
        return queryService.getSoftwareUnitByUniqueName(uniquename);
    }

    @Override
    public String getSourceFilePathOfClass(String uniquename) {
    	return queryService.getSourceFilePathOfClass(uniquename);
    }

    @Override
    public SoftwareUnitDTO[] getSoftwareUnitsInRoot() {
        return queryService.getSoftwareUnitsInRoot();
    }
    
    @Override
    public SoftwareUnitDTO[] getChildUnitsOfSoftwareUnit(String from) {
        return queryService.getChildUnitsOfSoftwareUnit(from);
    }

    @Override
    public SoftwareUnitDTO getParentUnitOfSoftwareUnit(String child) {
        return queryService.getParentUnitOfSoftwareUnit(child);
    }

    @Override
    public List<String> getAllPhysicalClassPathsOfSoftwareUnit(String uniqueName){
    	return queryService.getAllPhysicalClassPathsOfSoftwareUnit(uniqueName);
    }

    @Override
    public List<String> getAllPhysicalPackagePathsOfSoftwareUnit(String uniqueName){
    	return queryService.getAllPhysicalPackagePathsOfSoftwareUnit(uniqueName);
    }

    @Override
    public DependencyDTO[] getDependenciesFromSoftwareUnitToSoftwareUnit(String pathFrom, String pathTo) {
        return queryService.getDependenciesFromSoftwareUnitToSoftwareUnit(pathFrom, pathTo);
    }

    @Override
	public DependencyDTO[] getDependenciesFromClassToClass(String classPathFrom, String classPathTo){
		return queryService.getDependenciesFromClassToClass(classPathFrom, classPathTo);
	}
	
    @Override
    public void createDependencyReport(String fullPath) {
        analyseTaskControl.createDependencyReport(fullPath);
    }
    
    @Override
    public Element exportAnalysisModel() {
    	return analyseTaskControl.exportAnalysisModel();
    }

    @Override
    public void importAnalysisModel(Element analyseElement) {
    	analyseTaskControl.importAnalysisModel(analyseElement);
        this.analyseInternalFrame = new AnalyseInternalFrame();
        super.notifyServiceListeners();
    }

	@Override
    public void reconstructArchitecture() {
    	analyseTaskControl.reconstructArchitecture();
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
		analyseTaskControl.logHistory(applicationDTO, workspaceName);
	}
	
	@Override
	public AnalysisStatisticsDTO getAnalysisStatistics(SoftwareUnitDTO selectedModule) {
		return queryService.getAnalysisStatistics(selectedModule);
	}

    public HashSet<UmlLinkDTO> getAllUmlLinksFromClassToToClass(String fromClass, String toClass) {
    	return queryService.getAllUmlLinksFromClassToToClass(fromClass, toClass);
    }

}
