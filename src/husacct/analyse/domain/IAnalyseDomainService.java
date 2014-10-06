package husacct.analyse.domain;

import java.util.HashMap;
import java.util.List;

import org.jdom2.Element;

import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.AnalysisStatisticsDTO;
import husacct.common.dto.DependencyDTO;

public interface IAnalyseDomainService {

    public void clearModel();

    public DependencyDTO[] getAllDependencies();

    // Returns unique names of all types (classes, interfaces, inner classes) of SoftwareUnit with uniqueName  
    public List<String> getAllPhysicalClassPathsOfSoftwareUnit(String uniqueName);
    
    // Returns unique names of all subpackages of the SoftwareUnit with uniqueName  
    public List<String> getAllPhysicalPackagePathsOfSoftwareUnit(String uniqueName);
    
    public DependencyDTO[] getDependencies(String from, String to);

    public DependencyDTO[] getDependencies(String from, String to, String[] dependencyFilter);

    public DependencyDTO[] getDependenciesFrom(String from);

    public DependencyDTO[] getDependenciesFrom(String from, String[] dependencyFilter);

	// Returns a list of dependencies between the fromClass and toClass.
    // Fast function, based on HashMap get-search. Both class paths should match exactly to a uniqueName of a type! 
    public DependencyDTO[] getDependenciesFromTo(String classPathFrom, String classPathTo);

    public DependencyDTO[] getDependenciesTo(String to);

    public DependencyDTO[] getDependenciesTo(String to, String[] dependencyFilter);

    public AnalysedModuleDTO getModuleForUniqueName(String uniquename);

    public String getSourceFilePathOfClass(String uniquename);

    public AnalysedModuleDTO[] getRootModules();

    public AnalysedModuleDTO[] getChildModulesInModule(String from);

    public AnalysedModuleDTO[] getChildModulesInModule(String from, int depth);

    public AnalysedModuleDTO getParentModuleForModule(String child);

    public Element saveModel();

    public void loadModel(Element analyseElement);

    public HashMap<String, DependencyDTO> mapDependencies();
    
    public AnalysisStatisticsDTO getAnalysisStatistics(AnalysedModuleDTO selectedModule);

	public void buildCache();
}
