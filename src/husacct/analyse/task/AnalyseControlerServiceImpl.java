package husacct.analyse.task;

import husacct.analyse.domain.AnalyseDomainService;
import husacct.analyse.domain.AnalyseDomainServiceImpl;
import husacct.analyse.task.analyser.ApplicationAnalyser;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;

public class AnalyseControlerServiceImpl implements AnalyseControlService{

	private ApplicationAnalyser analyserService; 
	private AnalyseDomainService domainService;
	private boolean isAnalysed = false;
	
	public AnalyseControlerServiceImpl(){
		this.domainService = new AnalyseDomainServiceImpl();
		this.analyserService = new ApplicationAnalyser();
	}
	
	@Override
	public void analyseApplication() {
		if(isAnalysed) domainService.clearModel();
		analyserService.analyseApplication();
		isAnalysed = true;
	}
	
	@Override
	public String[] getAvailableLanguages() {
		return analyserService.getAvailableLanguages();
	}
	
	@Override
	public AnalysedModuleDTO[] getRootModules() {
		return domainService.getRootModules();
	}
	

	@Override
	public AnalysedModuleDTO[] getChildModulesInModule(String from) {
		return domainService.getChildModulesInModule(from);
	}

	@Override
	public AnalysedModuleDTO[] getChildModulesInModule(String from, int depth) {
		// TODO Implement Service
		return null;
	}
	
	@Override
	public DependencyDTO[] getDependency(String from, String to) {
		return domainService.getDependency(from, to);
	}

	@Override
	public AnalysedModuleDTO getParentModuleForModule(String child) {
		// TODO Implement Service
		return null;
	}

	@Override
	public DependencyDTO[] getDependency(String from) {
		// TODO Implement Service
		return null;
	}
}
