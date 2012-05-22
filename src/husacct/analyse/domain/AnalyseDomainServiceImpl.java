package husacct.analyse.domain;

import java.util.List;

import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.analyse.domain.famix.FamixQueryServiceImpl;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;

public class AnalyseDomainServiceImpl implements IAnalyseDomainService{

	private IModelQueryService queryService;
	private IModelCreationService creationService;
	
	public AnalyseDomainServiceImpl(){
		this.queryService = new FamixQueryServiceImpl();
		this.creationService = new FamixCreationServiceImpl();
	}	
	
	public void clearModel(){
		creationService.clearModel();
	}
	
	@Override
	public AnalysedModuleDTO getModuleForUniqueName(String uniquename){
		return queryService.getModuleForUniqueName(uniquename);
	}
	
	@Override
	public AnalysedModuleDTO[] getRootModules() {
		List<AnalysedModuleDTO> rootModuleList = queryService.getRootModules();
		AnalysedModuleDTO[] rootModules = new AnalysedModuleDTO[rootModuleList.size()];
		for(int i=0; i<rootModuleList.size(); i++){
			rootModules[i] = rootModuleList.get(i);
		}
		return rootModules;
	}
	
	@Override
	public AnalysedModuleDTO[] getChildModulesInModule(String from) {
		List<AnalysedModuleDTO> childModules = queryService.getChildModulesInModule(from);
		AnalysedModuleDTO[] childs = new AnalysedModuleDTO[childModules.size()];
		for(int i = 0; i < childModules.size(); i++){
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
	public DependencyDTO[] getDependencies(String from, String to) {
		List<DependencyDTO> depdencyList = queryService.getDependencies(from, to);
		DependencyDTO[] dependencies = new DependencyDTO[depdencyList.size()];
		int count = 0;
		for(DependencyDTO dependency: depdencyList){
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
}
