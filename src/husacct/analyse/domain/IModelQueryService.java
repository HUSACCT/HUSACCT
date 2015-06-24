package husacct.analyse.domain;

import husacct.common.dto.SoftwareUnitDTO;
import husacct.common.dto.AnalysisStatisticsDTO;
import husacct.common.dto.DependencyDTO;

import java.util.List;
import java.util.TreeSet;

public interface IModelQueryService {

    public void clearModel();

    public void buildCache();
    
    public void importDependencies(List<DependencyDTO> dependencies);
    
    public SoftwareUnitDTO getSoftwareUnitByUniqueName(String uniquename);

    public String getSourceFilePathOfClass(String uniquename);

    public SoftwareUnitDTO[] getSoftwareUnitsInRoot();

    public SoftwareUnitDTO[] getChildUnitsOfSoftwareUnit(String from);

    public SoftwareUnitDTO getParentUnitOfSoftwareUnit(String child);

    public DependencyDTO[] getAllDependencies();

    /** Returns List with unique names of all types (classes, interfaces, inner classes) within the SoftwareUnit with uniqueName 
    */  
    public List<String> getAllPhysicalClassPathsOfSoftwareUnit(String uniqueName);
    
    /** Returns List with unique names of all packages within this SoftwareUnit 
    */  
    public List<String> getAllPhysicalPackagePathsOfSoftwareUnit(String uniqueName);
    
	/** Returns an array of dependencies between the analyzed units pathFrom and pathTo and all their siblings; a path may refer to a package too. 
    * Relatively fast function, based on HashMap. At least one of the argument must match with an analysedModule.
    */  
    public DependencyDTO[] getDependenciesFromSoftwareUnitToSoftwareUnit(String pathFrom, String pathTo);

	/** Returns all dependencies for the exact match from classPathFrom and classPathTo. Fast function, based on HashMap.
	* Either classPathTFrom or classPathTo should refer to a class or library class and have a value other than "", otherwise an empty array is returned.
	* If classPathTFrom = "", then all dependencies to classPathTo are returned, which refer to existing classPathFrom's.
	* If classPathTo = "", then all dependencies from classPathFrom are returned, which refer to existing classPathTo's.
	*/ 
    public DependencyDTO[] getDependenciesFromClassToClass(String classPathFrom, String classPathTo);

    /** If selectedModule == null, statistics of the whole application are returned; otherwise statistics of the selectedModule only are returned. 
    */
    public AnalysisStatisticsDTO getAnalysisStatistics(SoftwareUnitDTO selectedModule);
}
