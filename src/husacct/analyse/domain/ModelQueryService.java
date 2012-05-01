package husacct.analyse.domain;

import husacct.common.dto.AnalysedModuleDTO;
import java.util.List;

public interface ModelQueryService {

	public List<AnalysedModuleDTO> getRootModules(); 
	public List<AnalysedModuleDTO> getChildModulesInModule(String from);
	
}
