package husacct.analyse.domain;

import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;

import java.util.HashMap;
import java.util.List;

public interface IModelQueryService {

    public AnalysedModuleDTO getModuleForUniqueName(String uniquename);

    public List<AnalysedModuleDTO> getRootModules();

    public List<AnalysedModuleDTO> getChildModulesInModule(String from);

    public AnalysedModuleDTO[] getChildModulesInModule(String from, int depth);

    public AnalysedModuleDTO getParentModuleForModule(String child);

    public DependencyDTO[] getAllDependencies();

    public List<DependencyDTO> getDependencies(String from, String to);

    public DependencyDTO[] getDependencies(String from, String to, String[] dependencyFilter);

    public List<DependencyDTO> getDependenciesFrom(String from);

    public DependencyDTO[] getDependenciesFrom(String from, String[] dependencyFilter);

    public List<DependencyDTO> getDependenciesTo(String to);

    public DependencyDTO[] getDependenciesTo(String to, String[] dependencyFilter);

    public HashMap<String, DependencyDTO> mapDependencies();
    
    public String[] getExternalSystems();
}
