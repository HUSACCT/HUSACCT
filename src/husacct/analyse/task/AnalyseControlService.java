package husacct.analyse.task;

import husacct.analyse.domain.famix.FamixObject;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;

import java.util.List;

public interface AnalyseControlService {
		
	public void analyseApplication();
	
	public DependencyDTO[] getDependency(String from, String to);
	public DependencyDTO[] getDependency(String from);
	
	public String[] getAvailableLanguages();
	
	public AnalysedModuleDTO[] getRootModules();
	public AnalysedModuleDTO[] getChildModulesInModule(String from); 
	public AnalysedModuleDTO[] getChildModulesInModule(String from, int depth);
	public AnalysedModuleDTO getParentModuleForModule(String child);
}
