package husacct.analyse.domain;

import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.AnalysisStatisticsDTO;
import husacct.common.dto.DependencyDTO;

import java.util.List;
import java.util.TreeSet;

public interface IModelQueryService {

    public void clearModel();

    public void buildCache();
    
    public void importDependencies(List<DependencyDTO> dependencies);
    
    public AnalysedModuleDTO getModuleForUniqueName(String uniquename);

    public String getSourceFilePathOfClass(String uniquename);

    public List<AnalysedModuleDTO> getRootModules();

    public List<AnalysedModuleDTO> getChildModulesInModule(String from);

    public AnalysedModuleDTO[] getChildModulesInModule(String from, int depth);

    public AnalysedModuleDTO getParentModuleForModule(String child);

    public DependencyDTO[] getAllDependencies();

    public TreeSet<String> getAllPhysicalClassPathsOfSoftwareUnit(String uniqueName);
    
    public TreeSet<String> getAllPhysicalPackagePathsOfSoftwareUnit(String uniqueName);
    
    public List<DependencyDTO> getDependencies(String from, String to);

    public DependencyDTO[] getDependencies(String from, String to, String[] dependencyFilter);

    public List<DependencyDTO> getDependenciesFrom(String from);

    public DependencyDTO[] getDependenciesFrom(String from, String[] dependencyFilter);

    public DependencyDTO[] getDependenciesFromTo(String classPathFrom, String classPathTo);

    public List<DependencyDTO> getDependenciesTo(String to);

    public DependencyDTO[] getDependenciesTo(String to, String[] dependencyFilter);

    public DependencyDTO[] mapDependencies();
    
    // If selectedModule == null, statistics of the whole application are returned; otherwise statistics of the selectedModule only are returned.
    public AnalysisStatisticsDTO getAnalysisStatistics(AnalysedModuleDTO selectedModule);
}
