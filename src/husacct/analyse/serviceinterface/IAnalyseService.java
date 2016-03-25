package husacct.analyse.serviceinterface;

import java.util.HashSet;
import java.util.List;

import javax.swing.JInternalFrame;

import org.jdom2.Element;

import husacct.analyse.serviceinterface.dto.AnalysisStatisticsDTO;
import husacct.analyse.serviceinterface.dto.DependencyDTO;
import husacct.analyse.serviceinterface.dto.SoftwareUnitDTO;
import husacct.analyse.serviceinterface.dto.UmlLinkDTO;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ProjectDTO;
import husacct.common.savechain.ISaveable;
import husacct.common.services.IObservableService;

public interface IAnalyseService extends IObservableService, ISaveable {

    public String[] getAvailableLanguages();

    public void analyseApplication(ProjectDTO project);

    public boolean isAnalysed();

    public JInternalFrame getJInternalFrame();

    public JInternalFrame getJInternalSARFrame();

    /** Returns unique names of all types (classes, interfaces, inner classes) of SoftwareUnit with uniqueName */  
    public List<String> getAllPhysicalClassPathsOfSoftwareUnit(String uniqueName);
    
    /** Returns unique names of all subpackages of the SoftwareUnit with uniqueName */  
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
	
	/** Returns an array of the dependencies, of the dependency types Access, Call, and References only, between the analyzed units pathFrom and pathTo and all their siblings; a path may refer to a package too. 
     * Relatively fast function, based on HashMap. At least one of the argument must match with an analysedModule.
     */  
	public DependencyDTO[] getDependencies_OnlyAccessCallAndReferences_FromSoftwareUnitToSoftwareUnit(String pathFrom, String pathTo);

	/** Returns the dependencies, of the dependency types Access, Call, and References only, for the exact match from classPathFrom and classPathTo. 
	* Fast function, based on HashMap.
	* Either classPathTFrom or classPathTo should refer to a class or library class and have a value other than "", otherwise an empty array is returned.
	* If classPathTFrom = "", then all dependencies to classPathTo are returned, which refer to existing classPathFrom's.
	* If classPathTo = "", then all dependencies from classPathFrom are returned, which refer to existing classPathTo's.
	*/ 
	public DependencyDTO[] getDependencies_OnlyAccessCallAndReferences_FromClassToClass(String classPathFrom, String classPathTo);
    
    public SoftwareUnitDTO getSoftwareUnitByUniqueName(String uniquename);
    
    public String getSourceFilePathOfClass(String uniquename);

    public SoftwareUnitDTO[] getSoftwareUnitsInRoot();
    
    public SoftwareUnitDTO[] getChildUnitsOfSoftwareUnit(String from);

    public SoftwareUnitDTO getParentUnitOfSoftwareUnit(String child);

    public void createDependencyReport(String fullPath);
    
    public void reconstructArchitecture_Initiate();
    
    public Element exportAnalysisModel();

	public void importAnalysisModel(Element analyseElement);

	public void logHistory(ApplicationDTO applicationDTO, String workspaceName);
    
    /** If selectedModule == null, statistics of the whole application are returned; otherwise statistics of the selectedModule only are returned. */
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
