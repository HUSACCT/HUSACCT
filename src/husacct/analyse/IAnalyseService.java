package husacct.analyse;

import java.util.List;
import javax.swing.JInternalFrame;
import org.jdom2.Element;

import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.AnalysisStatisticsDTO;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ProjectDTO;
import husacct.common.savechain.ISaveable;
import husacct.common.services.IObservableService;

public interface IAnalyseService extends IObservableService, ISaveable {

    public String[] getAvailableLanguages();

    public void analyseApplication(ProjectDTO project);

    public boolean isAnalysed();

    public JInternalFrame getJInternalFrame();

    //The following function has been inserted due to performance issues. The function enables
    //function-users to use cache-mechanisms and special search-algorithms
    public DependencyDTO[] getAllDependencies();
    
    // Returns unique names of all types (classes, interfaces, inner classes) of SoftwareUnit with uniqueName  
    public List<String> getAllPhysicalClassPathsOfSoftwareUnit(String uniqueName);
    
    // Returns unique names of all subpackages of the SoftwareUnit with uniqueName  
    public List<String> getAllPhysicalPackagePathsOfSoftwareUnit(String uniqueName);
    
    public DependencyDTO[] getAllUnfilteredDependencies();

	// Returns an array of dependencies between the fromModule and toModule.
    // Fast function, based on HashMap get-search. At least one of the argument must match with an analysedModule, which may be a composite one. 
    public DependencyDTO[] getDependencies(String from, String to);

    public DependencyDTO[] getDependenciesFrom(String from);

	// Returns an array of dependencies between the fromClass and toClass.
    // Fast function, based on HashMap get-search. Both class paths should match exactly to a uniqueName of a type! 
    public DependencyDTO[] getDependenciesFromTo(String classPathFrom, String classPathTo);
	
    public DependencyDTO[] getDependenciesTo(String to);

    public DependencyDTO[] getDependencies(String from, String to, String[] dependencyFilter);

    public DependencyDTO[] getDependenciesFrom(String to, String[] dependencyFilter);

    public DependencyDTO[] getDependenciesTo(String to, String[] dependencyFilter);

    public AnalysedModuleDTO getModuleForUniqueName(String uniquename);
    
    public String getSourceFilePathOfClass(String uniquename);

    public AnalysedModuleDTO[] getRootModules();
    
    public AnalysedModuleDTO[] getChildModulesInModule(String from);

    public AnalysedModuleDTO[] getChildModulesInModule(String from, int depth);

    public AnalysedModuleDTO getParentModuleForModule(String child);

    public void exportDependencies(String fullPath);
    
    public void reconstructArchitecture();
    
    public Element exportAnalysisModel();

	public void importAnalysisModel(Element analyseElement);

	public void logHistory(ApplicationDTO applicationDTO, String workspaceName);
    
    public AnalysisStatisticsDTO getAnalysisStatistics(AnalysedModuleDTO selectedModule);
    
}
