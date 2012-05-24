package husacct.analyse.domain.famix;

import java.util.List;
import husacct.analyse.domain.IModelQueryService;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;

public class FamixQueryServiceImpl implements IModelQueryService{

	private FamixModel theModel;
	private FamixModuleFinder moduleFinder;
	private FamixDependencyFinder dependencyFinder;
	
	public FamixQueryServiceImpl(){
		this.theModel = FamixModel.getInstance();
		this.moduleFinder = new FamixModuleFinder(theModel);
		this.dependencyFinder = new FamixDependencyFinder(this.theModel);
	}
	
	@Override
	public AnalysedModuleDTO getModuleForUniqueName(String uniquename){
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
	public DependencyDTO[] getDependenciesFrom(String from,	String[] dependencyFilter) {
		List<DependencyDTO> result = dependencyFinder.getDependenciesFrom(from, dependencyFilter);
		DependencyDTO[] allDependencies = result.toArray(new DependencyDTO[result.size()]); 
		return allDependencies; 
	}
	
	@Override
	public List<DependencyDTO> getDependenciesTo(String to) {
		return dependencyFinder.getDependenciesTo(to);
	}
	
	@Override
	public DependencyDTO[] getDependenciesTo(String to,	String[] dependencyFilter) {
		List<DependencyDTO> result = dependencyFinder.getDependenciesTo(to, dependencyFilter);
		DependencyDTO[] allDependencies = result.toArray(new DependencyDTO[result.size()]); 
		return allDependencies;
	}
}
