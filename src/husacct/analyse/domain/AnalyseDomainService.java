package husacct.analyse.domain;

import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;

public interface AnalyseDomainService {
	
	public void clearModel();
	
	public DependencyDTO[] getDependency(String from, String to);
	public DependencyDTO[] getDependency(String from);
	
	public AnalysedModuleDTO[] getRootModules();
	public AnalysedModuleDTO[] getChildModulesInModule(String from); 
	public AnalysedModuleDTO[] getChildModulesInModule(String from, int depth);
	public AnalysedModuleDTO getParentModuleForModule(String child);
	
}
