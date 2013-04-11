package husacct.analyse;

import javax.swing.JInternalFrame;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ProjectDTO;
import husacct.common.services.IObservableService;

public interface IAnalyseService extends IObservableService{
	
	public String[] getAvailableLanguages();
	@Deprecated
	public void analyseApplication(String[] paths, String programmingLanguage);
	public void analyseApplication(ProjectDTO project);
	
	public boolean isAnalysed();
	
	public JInternalFrame getJInternalFrame();
	
	//The following function has been inserted due to performance issues. The function enables
	//function-users to use cache-mechanisms and special search-algorithms
	public DependencyDTO[] getAllDependencies();
	
	public DependencyDTO[] getDependencies(String from, String to);
	public DependencyDTO[] getDependenciesFrom(String from);
	public DependencyDTO[] getDependenciesTo(String to);
	
	public DependencyDTO[] getDependencies(String from, String to, String[] dependencyFilter);
	public DependencyDTO[] getDependenciesFrom(String from, String[] dependencyFilter);
	public DependencyDTO[] getDependenciesTo(String to, String[] dependencyFilter);
	
	public AnalysedModuleDTO getModuleForUniqueName(String uniquename);
	public AnalysedModuleDTO[] getRootModules();
	public AnalysedModuleDTO[] getChildModulesInModule(String from); 
	public AnalysedModuleDTO[] getChildModulesInModule(String from, int depth);
	public AnalysedModuleDTO getParentModuleForModule(String child);
	
	public void exportDependencies(String fullPath);
}
