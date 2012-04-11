package husacct.analyse.domain.analyseservice;

import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;

public interface AnalyseDomainService {
	
	public DependencyDTO[] getDependency(String from, String to);
	public DependencyDTO[] getDependency(String from);
	
	public String[] getAvailableLanguages();
	
	public AnalysedModuleDTO[] getRootModules();
	public AnalysedModuleDTO[] getChildModulesInModule(String from); 
	public AnalysedModuleDTO[] getChildModulesInModule(String from, int depth);
	public AnalysedModuleDTO getParentModuleForModule(String child);
	
}
