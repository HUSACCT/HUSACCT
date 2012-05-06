package husacct.analyse.domain;

import java.util.List;
import husacct.analyse.domain.famix.FamixQueryServiceImpl;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;

public class AnalyseDomainServiceImpl implements AnalyseDomainService{

	private ModelQueryService queryService;
	private ModelCreationService creationService;
	
	public AnalyseDomainServiceImpl(){
		this.queryService = new FamixQueryServiceImpl();
	}	
	
	public void clearModel(){
		
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
		for(int i=0; i<childModules.size(); i++){
			childs[i] = childModules.get(i);
		}
		return childs;
	}

	@Override
	public AnalysedModuleDTO[] getChildModulesInModule(String from, int depth) {
		//TODO Implement Service
		return null;
	}

	@Override
	public AnalysedModuleDTO getParentModuleForModule(String child) {
		// TODO Implement Service
		return null;
	}
	
	@Override
	public DependencyDTO[] getDependency(String from, String to) {
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
	public DependencyDTO[] getDependency(String from) {
		//TODO Implement Service
		return null;
	}	
}
