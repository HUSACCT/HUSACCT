package husacct.analyse.domain;

import husacct.common.dto.AnalysisStatisticsDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.SoftwareUnitDTO;
import husacct.common.dto.UmlLinkDTO;

import java.util.HashSet;
import java.util.List;

public interface IModelQueryService {

    /** Removes all the data from the analysis model.
     * Only to be used at the beginning of an analysis or import process. 
    */  
    public void clearModel();

    /** Builds up list of dependencies, if not yet existing, 
     * and initializes dependency hashmaps for fast queries.
     */
    public void buildCache();
    
    /** Replaces the existing list of dependencies by the passed list of dependencies. 
     */
    public void importDependencies(List<DependencyDTO> dependencies);
    
    /** Returns a SoftwareUnitDTO with the characteristics of a package, class 
     * or library, identified by the passed uniqueName.
     * If no software unit is found, a SoftwareUnitDTO is returned with "" values, and an empty list of children.    
     */
    public SoftwareUnitDTO getSoftwareUnitByUniqueName(String uniqueName);

    /** Returns the sourceFilePath of the class with the passed uniqueName.
     * If no class is found, value "" is returned.
     */
    public String getSourceFilePathOfClass(String uniqueName);

    /** Returns an array of SoftwareUnitDTOs of all root packages/namespaces 
     * within the path(s) of all project(s) in the workspace. Notes:
     * a)External software units are always prefixed by package: xLibraries.
     * b)If classes have no specified package/namespace (exceptionally), than these classes are added to generated package: noPackage_HusacctDefined. 
     */
    public SoftwareUnitDTO[] getSoftwareUnitsInRoot();

    /** Returns an array of SoftwareUnitDTOs with the children of the software unit (package, class or library) 
     * identified by the passed uniqueName.
     * If no software unit is found, an empty SoftwareUnitDTO[] is returned.    
     */
    public SoftwareUnitDTO[] getChildUnitsOfSoftwareUnit(String uniqueName);

    /** Returns a SoftwareUnitDTO of the parent of the software unit (package, class or library) 
     * identified by the passed uniqueName.
     * If no software unit is found, a SoftwareUnitDTO is returned with "" values, and an empty list of children.    
     */
    public SoftwareUnitDTO getParentUnitOfSoftwareUnit(String uniqueName);

    /** Returns an array with DependencyDTOs of all dependencies.
     * It is used only for export purposes. Working with this list for queries is very slow, so better use specific query services. 
     */
    public DependencyDTO[] getAllDependencies();

	/** Returns a list with the uniqueNames of the rootPackages with a class: the first packages (starting from the root) that contain one or more classes.
	*/ 
	public List<String> getRootPackagesWithClass(String module);

	/** Returns List with unique names of all types (classes, interfaces, inner classes) within the SoftwareUnit with uniqueName 
    */  
    public List<String> getAllPhysicalClassPathsOfSoftwareUnit(String uniqueName);
    
    /** Returns List with unique names of all packages within this SoftwareUnit 
    */  
    public List<String> getAllPhysicalPackagePathsOfSoftwareUnit(String uniqueName);
    
	/** Returns an array of all dependencies between the analyzed units pathFrom and pathTo and all their siblings; a path may refer to a package too. 
    * Relatively fast function, based on HashMap. At least one of the argument must match with an analysedModule.
    */  
    public DependencyDTO[] getDependenciesFromSoftwareUnitToSoftwareUnit(String pathFrom, String pathTo);

	/** Returns all dependencies for the exact match from classPathFrom and classPathTo. Fast function, based on HashMap.
	* Either classPathTFrom or classPathTo should refer to a class or library class and have a value other than "", otherwise an empty array is returned.
	* If classPathTFrom = "", then all dependencies to classPathTo are returned, which refer to existing classPathFrom's.
	* If classPathTo = "", then all dependencies from classPathFrom are returned, which refer to existing classPathTo's.
	*/ 
    public DependencyDTO[] getDependenciesFromClassToClass(String classPathFrom, String classPathTo);

	/** Returns an array of the dependencies, of the dependency types Access, Call, and References only, between the analyzed units pathFrom and pathTo and all their siblings; a path may refer to a package too. 
     * Relatively fast function, based on HashMap. At least one of the argument must match with an analysedModule.
     */  
	public DependencyDTO[] getDependencies_OnlyAccessCallAndReferences_FromSoftwareUnitToSoftwareUnit(String pathFrom, String pathTo);

	/** Returns the dependencies, of the dependency types Access, Call, and References only,  for the exact match from classPathFrom and classPathTo. Fast function, based on HashMap.
	* Either classPathTFrom or classPathTo should refer to a class or library class and have a value other than "", otherwise an empty array is returned.
	* If classPathTFrom = "", then all dependencies to classPathTo are returned, which refer to existing classPathFrom's.
	* If classPathTo = "", then all dependencies from classPathFrom are returned, which refer to existing classPathTo's.
	*/ 
 	public DependencyDTO[] getDependencies_OnlyAccessCallAndReferences_FromClassToClass(String classPathFrom, String classPathTo);

    /** Returns an AnalysisStatisticsDTO with statistical data of the analyzed application.
     * If selectedModule == null, statistics of the whole application are returned; 
     * otherwise statistics of the selectedModule only are returned. 
    */
    public AnalysisStatisticsDTO getAnalysisStatistics(SoftwareUnitDTO selectedModule);
    
    /** Returns all the UML-Links going from the fromClass to another FamixClass or FamixLibrary. 
     * fromClass must be a unique name of a FamixClass (not of an FamixLibrary (starting with "xLibraries."). 
     */
    public HashSet<UmlLinkDTO> getUmlLinksFromClassToOtherClasses(String fromClass);

    /** Returns all the UML-Links going from the fromClass to the specific toClass.
     * fromClass and toClass must both be a unique name of FamixClass, or (in case of toClass) FamixLibrary (include "xLibraries."). 
     * */
    public HashSet<UmlLinkDTO> getUmlLinksFromClassToToClass(String fromClass, String toClass);
    
	/** Returns an array of all umlLinks  (enclosed in UmlLinkDTOs)between the analyzed units pathFrom and pathTo and all their siblings; a path may refer to a package too. 
     * Relatively fast function, based on HashMap. Both argument must match with an analysedModule.
     */  
    public UmlLinkDTO[] getUmlLinksFromSoftwareUnitToSoftwareUnit(String pathFrom, String pathTo);
    
	/** Returns an array of all umlLinks (enclosed in DependencyDTOs) between the analyzed units pathFrom and pathTo and all their siblings; a path may refer to a package too. 
     * Relatively fast function, based on HashMap. Both argument must match with an analysedModule.
     */  
    public DependencyDTO[] getUmlLinksAsDependencyDtosFromSoftwareUnitToSoftwareUnit(String pathFrom, String pathTo);

}
