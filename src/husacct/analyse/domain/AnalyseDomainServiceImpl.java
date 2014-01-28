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
    public AnalysedModuleDTO[] getRootModulesWithExternalSystems() {
        return this.getRootModules();
        
        // Due to  severe performance problems the inclusion of external systems is disabled 2014-01-16
        //List<AnalysedModuleDTO> rootModuleList = queryService.getRootModules();
        //ExternalSystemDTO[] externalSystems = queryService.getExternalSystems();
        //List<AnalysedModuleDTO> rootModules = new ArrayList<AnalysedModuleDTO>();
        //for (AnalysedModuleDTO rootModule : rootModuleList) {
        //    rootModules.add(rootModule);
        //}
        //for (ExternalSystemDTO eSystem : externalSystems){
        //	rootModules.add(new AnalysedModuleDTO(eSystem.systemPackage, eSystem.systemName, "library", "true"));
        //}
        //return rootModules.toArray(new AnalysedModuleDTO[rootModules.size()]);
    }

    @Override
    public AnalysedModuleDTO[] getChildModulesInModule(String from) {
        List<AnalysedModuleDTO> childModules = queryService.getChildModulesInModule(from);
        AnalysedModuleDTO[] childs = new AnalysedModuleDTO[childModules.size()];
        for (int i = 0; i < childModules.size(); i++) {
            childs[i] = childModules.get(i);
        }
        return childs;
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
    
    @Override
	public ExternalSystemDTO[] getExternalSystems(){
		return queryService.getExternalSystems();
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
