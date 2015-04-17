package husacct.analyse.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.jdom2.Element;

import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.analyse.domain.famix.FamixPersistencyServiceImpl;
import husacct.analyse.domain.famix.FamixQueryServiceImpl;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.AnalysisStatisticsDTO;
import husacct.common.dto.DependencyDTO;

public class AnalyseDomainServiceImpl implements IAnalyseDomainService {

    private IModelQueryService queryService;
    private IModelCreationService creationService;
    private IModelPersistencyService persistencyService;

    public AnalyseDomainServiceImpl() {
        this.queryService = new FamixQueryServiceImpl();
        this.creationService = new FamixCreationServiceImpl();
        this.persistencyService = new FamixPersistencyServiceImpl();
    }

    public void clearModel() {
        this.queryService = new FamixQueryServiceImpl();
        creationService.clearModel();
    }

    @Override
    public AnalysedModuleDTO getModuleForUniqueName(String uniquename) {
        return queryService.getModuleForUniqueName(uniquename);
    }
    
    @Override
    public String getSourceFilePathOfClass(String uniquename) {
    	return queryService.getSourceFilePathOfClass(uniquename);
    }

    @Override
    public AnalysedModuleDTO[] getRootModules(){
    	List<AnalysedModuleDTO> rootModules = queryService.getRootModules();
    	return rootModules.toArray(new AnalysedModuleDTO[rootModules.size()]);
    }

    @Override
    public AnalysedModuleDTO[] getChildModulesInModule(String from) {
    	AnalysedModuleDTO[] children;
    	List<AnalysedModuleDTO> childModules = null;
    	childModules = queryService.getChildModulesInModule(from);
        int numberOfChildren = childModules.size();
        if ((childModules != null) && numberOfChildren > 0) {
	        children = childModules.toArray(new AnalysedModuleDTO[childModules.size()]);
        } else {
        	children = new AnalysedModuleDTO[0];
        }
	    return children;
    }

    @Override
    public AnalysedModuleDTO[] getChildModulesInModule(String from, int depth) {
        return queryService.getChildModulesInModule(from, depth);
    }

    @Override
    public AnalysedModuleDTO getParentModuleForModule(String child) {
        return queryService.getParentModuleForModule(child);
    }

    @Override
    public DependencyDTO[] getAllDependencies() {
        return queryService.getAllDependencies();
    }

    @Override
    public List<String> getAllPhysicalClassPathsOfSoftwareUnit(String uniqueName){
    	List<String> returnValue = new ArrayList<String>(); 
    	TreeSet<String> allPaths = queryService.getAllPhysicalClassPathsOfSoftwareUnit(uniqueName);
    	for (String path : allPaths) {
    		returnValue.add(path);
    	}
    	return returnValue;
    }
    
    @Override
    public List<String> getAllPhysicalPackagePathsOfSoftwareUnit(String uniqueName){
    	List<String> returnValue = new ArrayList<String>(); 
    	TreeSet<String> allPaths = queryService.getAllPhysicalPackagePathsOfSoftwareUnit(uniqueName);
    	for (String path : allPaths) {
    		returnValue.add(path);
    	}
    	return returnValue;
    }
    
    @Override
    public DependencyDTO[] getDependencies(String from, String to) {
        List<DependencyDTO> dependencies = queryService.getDependencies(from, to);
        return dependencies.toArray(new DependencyDTO[dependencies.size()]);
    }

    @Override
    public DependencyDTO[] getDependencies(String from, String to, String[] dependencyFilter) {
        return queryService.getDependencies(from, to, dependencyFilter);
    }

    @Override
    public DependencyDTO[] getDependenciesFrom(String from) {
        List<DependencyDTO> dependencies = queryService.getDependenciesFrom(from);
        return dependencies.toArray(new DependencyDTO[dependencies.size()]);
    }

    @Override
    public DependencyDTO[] getDependenciesFrom(String from, String[] dependencyFilter) {
        return queryService.getDependenciesFrom(from, dependencyFilter);
    }

    @Override
	public DependencyDTO[] getDependenciesFromTo(String classPathFrom, String classPathTo){
		return queryService.getDependenciesFromTo(classPathFrom, classPathTo);
	}
	
    @Override
    public DependencyDTO[] getDependenciesTo(String to) {
        List<DependencyDTO> dependencies = queryService.getDependenciesTo(to);
        return dependencies.toArray(new DependencyDTO[dependencies.size()]);
    }

    @Override
    public DependencyDTO[] getDependenciesTo(String to, String[] dependencyFilter) {
        return queryService.getDependenciesTo(to, dependencyFilter);
    }
    
    @Override
    public void buildCache(){
    	queryService.buildCache();
    	return;
    }

    @Override
    public Element saveModel() {
        return persistencyService.saveModel();
    }

    @Override
    public void loadModel(Element analyseElement) {
        persistencyService.loadModel(analyseElement);
    }

	@Override
    public DependencyDTO[] mapDependencies() {
        return queryService.mapDependencies();
    }
    
	@Override
	public AnalysisStatisticsDTO getAnalysisStatistics(AnalysedModuleDTO selectedModule) {
		return queryService.getAnalysisStatistics(selectedModule);
	}
}
