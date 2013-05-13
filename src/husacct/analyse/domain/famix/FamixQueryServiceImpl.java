package husacct.analyse.domain.famix;

import java.util.HashMap;
import java.util.List;
import husacct.analyse.domain.IModelQueryService;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ExternalSystemDTO;

public class FamixQueryServiceImpl implements IModelQueryService {

    private FamixModel theModel;
    private FamixModuleFinder moduleFinder;
    private FamixDependencyFinder dependencyFinder;

    public FamixQueryServiceImpl() {
        this.theModel = FamixModel.getInstance();
        this.moduleFinder = new FamixModuleFinder(theModel);
        this.dependencyFinder = new FamixDependencyFinder(this.theModel);
    }

    @Override
    public AnalysedModuleDTO getModuleForUniqueName(String uniquename) {
        return moduleFinder.getModuleForUniqueName(uniquename);
    }

    @Override
    public List<AnalysedModuleDTO> getRootModules() {
        return moduleFinder.getRootModules();
    }

    @Override
    public List<AnalysedModuleDTO> getChildModulesInModule(String from) {
        return moduleFinder.getChildModulesInModule(from);
    }

    @Override
    public AnalysedModuleDTO[] getChildModulesInModule(String from, int depth) {
        List<AnalysedModuleDTO> moduleList = moduleFinder.getChildModulesInModule(from, depth);
        return moduleList.toArray(new AnalysedModuleDTO[moduleList.size()]);
    }

    @Override
    public AnalysedModuleDTO getParentModuleForModule(String child) {
        return moduleFinder.getParentModuleForModule(child);
    }

    @Override
    public DependencyDTO[] getAllDependencies() {
        List<DependencyDTO> result = dependencyFinder.getAllDependencies();
        DependencyDTO[] allDependencies = result.toArray(new DependencyDTO[result.size()]);
        return allDependencies;
    }

    @Override
    public List<DependencyDTO> getDependencies(String from, String to) {
        return dependencyFinder.getDependencies(from, to);
    }

    @Override
    public DependencyDTO[] getDependencies(String from, String to, String[] dependencyFilter) {
        List<DependencyDTO> result = dependencyFinder.getDependencies(from, to, dependencyFilter);
        DependencyDTO[] allDependencies = result.toArray(new DependencyDTO[result.size()]);
        return allDependencies;
    }

    @Override
    public List<DependencyDTO> getDependenciesFrom(String from) {
        return dependencyFinder.getDependenciesFrom(from);
    }

    @Override
    public DependencyDTO[] getDependenciesFrom(String from, String[] dependencyFilter) {
        List<DependencyDTO> result = dependencyFinder.getDependenciesFrom(from, dependencyFilter);
        DependencyDTO[] allDependencies = result.toArray(new DependencyDTO[result.size()]);
        return allDependencies;
    }

    @Override
    public List<DependencyDTO> getDependenciesTo(String to) {
        return dependencyFinder.getDependenciesTo(to);
    }

    @Override
    public DependencyDTO[] getDependenciesTo(String to, String[] dependencyFilter) {
        List<DependencyDTO> result = dependencyFinder.getDependenciesTo(to, dependencyFilter);
        DependencyDTO[] allDependencies = result.toArray(new DependencyDTO[result.size()]);
        return allDependencies;
    }

    @Override
    public HashMap<String, DependencyDTO> mapDependencies() {
        List<DependencyDTO> cache = this.getDependencies("", "");
        HashMap<String, DependencyDTO> dependencyMap = new HashMap<String, DependencyDTO>();
        //TODO Analyse Persistency to file - Do Sorting of the list here!
        int counter = 0;
        for (DependencyDTO dependency : cache) {
            dependencyMap.put("" + counter, dependency);
            counter++;
        }
        return dependencyMap;
    }
    
    @Override
	public ExternalSystemDTO[] getExternalSystems(){
		return theModel.getExternalSystems();
	}
    
    public int getAmountOfDependencies() {
    	return getAllDependencies().length;
    }
    
    public int getAmountOfInterfaces() {
    	return theModel.imports.size();
    }
    
    public int getAmountOfPackages() {
    	return theModel.packages.size();
    }
    
    public int getAmountOfClasses() {
    	return theModel.classes.size();
    }
}
