package husacct.analyse.domain;

import husacct.analyse.domain.famix.FamixModel;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;

public class AnalyseDomainServiceImpl implements AnalyseDomainService{

	private FamixModelServiceImpl modelManager;
	private FamixModel model;

	public AnalyseDomainServiceImpl(){
		this.modelManager = new FamixModelServiceImpl();
		this.model = modelManager.getModel();
	}	
	
	@Override
	public DependencyDTO[] getDependency(String from, String to) {
		//TODO 	
		return null;
	}

	@Override
	public DependencyDTO[] getDependency(String from) {
		//TODO 
		return null;
	}

	@Override
	public AnalysedModuleDTO[] getChildModulesInModule(String from) {
		//TODO 
		return null;
	}

	@Override
	public AnalysedModuleDTO[] getChildModulesInModule(String from, int depth) {
		//TODO 
		return null;
	}

	@Override
	public AnalysedModuleDTO[] getRootModules() {
		// TODO
		return null;
	}

	@Override
	public AnalysedModuleDTO getParentModuleForModule(String child) {
		// TODO
		return null;
	}
}
