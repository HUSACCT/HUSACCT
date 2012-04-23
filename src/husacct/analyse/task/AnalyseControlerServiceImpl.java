package husacct.analyse.task;

import java.util.List;
import husacct.analyse.domain.AnalyseDomainService;
import husacct.analyse.domain.AnalyseDomainServiceImpl;
import husacct.analyse.task.analyser.ApplicationAnalyser;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;

public class AnalyseControlerServiceImpl implements AnalyseControlService{

	private ApplicationAnalyser analyserService; 
	private AnalyseDomainService domainService;
	
	public AnalyseControlerServiceImpl(){
		this.domainService = new AnalyseDomainServiceImpl();
		this.analyserService = new ApplicationAnalyser();
	}
	
	@Override
	public void analyseApplication() {
//		return analyserService.analyseApplication("benchmark_application");
		analyserService.analyseApplication("benchmark_application");
	}
	
	@Override
	public String[] getAvailableLanguages() {
		return analyserService.getAvailableLanguages();
	}
	
	
	
	

	@Override
	public DependencyDTO[] getDependency(String from, String to) {
		// TODO 
		return null;
	}

	@Override
	public DependencyDTO[] getDependency(String from) {
		// TODO 
		return null;
	}

	@Override
	public AnalysedModuleDTO[] getRootModules() {
		// TODO 
		return null;
	}

	@Override
	public AnalysedModuleDTO[] getChildModulesInModule(String from) {
		// TODO 
		return null;
	}

	@Override
	public AnalysedModuleDTO[] getChildModulesInModule(String from, int depth) {
		// TODO 
		return null;
	}

	@Override
	public AnalysedModuleDTO getParentModuleForModule(String child) {
		// TODO 
		return null;
	}
}
