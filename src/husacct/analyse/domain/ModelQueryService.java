package husacct.analyse.domain;

import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;

import java.util.List;

public interface ModelQueryService {

	public List<AnalysedModuleDTO> getRootModules(); 
	public List<AnalysedModuleDTO> getChildModulesInModule(String from);
	
	public List<DependencyDTO> getDependencies(String from, String to);
	
}
