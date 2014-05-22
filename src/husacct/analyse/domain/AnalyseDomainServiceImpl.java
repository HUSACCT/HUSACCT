package husacct.analyse.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom2.Element;

import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.analyse.domain.famix.FamixPersistencyServiceImpl;
import husacct.analyse.domain.famix.FamixQueryServiceImpl;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ExternalSystemDTO;

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
	        children = new AnalysedModuleDTO[numberOfChildren];
        	int i = 0;
	        for (AnalysedModuleDTO  childModule : childModules) {
	        	children[i] = childModule;
	            i++;
	        }
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
    	return queryService.getAllPhysicalClassPathsOfSoftwareUnit(uniqueName);
    }
    
    @Override
    public DependencyDTO[] getDependencies(String from, String to) {
        List<DependencyDTO> dependencyList = queryService.getDependencies(from, to);
        DependencyDTO[] dependencies = new DependencyDTO[dependencyList.size()];
        int count = 0;
        for (DependencyDTO dependency : dependencyList) {
            dependencies[count] = dependency;
            count++;
        }
        return dependencies;
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

    public HashMap<String, DependencyDTO> mapDependencies() {
        return queryService.mapDependencies();
    }
    
    public int getAmountOfDependencies() {
    	return queryService.getAmountOfDependencies();
    }
    
    public int getAmountOfInterfaces() {
    	return queryService.getAmountOfInterfaces();
    }
    
    public int getAmountOfPackages() {
    	return queryService.getAmountOfPackages();
    }
    
    public int getAmountOfClasses() {
    	return queryService.getAmountOfClasses();
    }
}
