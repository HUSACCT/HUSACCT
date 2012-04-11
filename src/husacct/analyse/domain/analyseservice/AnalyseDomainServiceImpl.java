package husacct.analyse.domain.analyseservice;

import husacct.analyse.abstraction.mappers.codemapper.CodeMapper;
import husacct.analyse.abstraction.mappers.codemapper.CodeMapperService;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;

public class AnalyseDomainServiceImpl implements AnalyseDomainService{

	CodeMapperService mapperService; 

	public AnalyseDomainServiceImpl(){
		this.mapperService = new CodeMapper();
	}
	
	@Override
	public String[] getAvailableLanguages() {
		return mapperService.getAvailableLanguages();
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
