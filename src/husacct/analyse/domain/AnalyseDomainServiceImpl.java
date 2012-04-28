package husacct.analyse.domain;

import husacct.analyse.domain.famix.FamixModelServiceImpl;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;

public class AnalyseDomainServiceImpl implements AnalyseDomainService{

	private ModelService modelManager;

	public AnalyseDomainServiceImpl(){
		this.modelManager = new FamixModelServiceImpl();
	}	
	
	@Override
	public DependencyDTO[] getDependency(String from, String to) {
		//TODO Implement Service
		return null;
	}

	@Override
	public DependencyDTO[] getDependency(String from) {
		//TODO Implement Service
		return null;
	}

	@Override
	public AnalysedModuleDTO[] getChildModulesInModule(String from) {
		//TODO Implement Service
		return null;
	}

	@Override
	public AnalysedModuleDTO[] getChildModulesInModule(String from, int depth) {
		//TODO Implement Service
		return null;
	}

	@Override
	public AnalysedModuleDTO[] getRootModules() {
		// TODO Implement Service
		return null;
	}

	@Override
	public AnalysedModuleDTO getParentModuleForModule(String child) {
		// TODO Implement Service
		return null;
	}
}
