package husacct.define;

import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.PhysicalPathDTO;
import husacct.common.dto.ProjectDTO;
import husacct.common.dto.RuleDTO;
import husacct.common.savechain.ISaveable;
import husacct.common.services.IObservableService;

import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JInternalFrame;

import org.jdom2.Element;

public interface IDefineService extends ISaveable, IObservableService {

	/**
	 * Starts analyze
	 */
	public void analyze();

	/**
	 * Creates an application with the given arguments
	 * 
	 * @param name
	 *            the name of the Application
	 * @param projects
	 *            is an ArrayList<ProjectDTO> with all the projects within the
	 *            application
	 * @param version
	 *            is the version of the Application
	 */
	public void createApplication(String name, ArrayList<ProjectDTO> projects,
			String version);

	/**
	 * Gets all details of the application
	 * 
	 * @return an ApplicationDTO with the details
	 */
	public ApplicationDTO getApplicationDetails();

	/**
	 * Gets all the children from a module, not the grand children
	 * @param String logicalPath is the logical path, ** is root module
	 * @return an array of ModuleDTO's. Throws RuntimeException when the module is not found.
	 */
	public ModuleDTO[] getModule_TheChildrenOfTheModule(String logicalPath);

	/**
	 * Gets all the physical classPaths of the types represented by the assigned software units and these of all the children of the module
	 * @param String logicalPath is the logical path, ** is root module
	 * @return a HashSet<PhysicalPathDTO>. Throws RuntimeException when the module is not found.
	 */
	public HashSet<String> getModule_AllPhysicalClassPathsOfModule(String logicalPath);

	/**
	 * Gets all the physical classPaths of the packages represented by the assigned software units and these of all the child packages of the module
	 * @param String logicalPath is the logical path, ** is root module
	 * @return a HashSet<PhysicalPathDTO>. Throws RuntimeException when the module is not found.
	 */
	public HashSet<String> getModule_AllPhysicalPackagePathsOfModule(String logicalPath);

	
	/**
	 * Gets the hierarchical level of a module
	 * @param logicalPath is the logical path, ** is root module
	 * @return an int, the hierarchical level. Throws RuntimeException when the module is not found.
	 */
	public int getHierarchicalLevelOfLayer(String logicalPath);

	/**
	 * Gets the GUI from Define
	 * @return a JInternalFrame from the Define component
	 */
	public JInternalFrame getDefinedGUI();

	/**
	 * Returns all the defined rules
	 * 
	 * @return an array of RuleDTO's (the defined rules)
	 */
	public RuleDTO[] getDefinedRules();

	/**
	 * Gets the logical architecture data
	 * 
	 * @return Element with architecture data
	 */
	public Element getLogicalArchitectureData();
	
	
	/**
	 * Returns the logical module to which the physical path is assigned
	 * 
	 * @param physicalPath is the physical path of a class or interface
	 * @return a ModuleDTO if a module is found based on physicalPath (or a part of physicalPath) 
	 */
	public ModuleDTO getModule_BasedOnSoftwareUnitName(String physicalPath);


	/**
	 * Returns the logical path of the parent from the given module
	 * 
	 * @param logicalPath
	 *            is the logical path of the module ("**" is root)
	 * @return a String containing the logical path to the Parent
	 */
	public String getModule_TheParentOfTheModule(String logicalPath);

	/**
	 * Gets all the root modules
	 * 
	 * @return a array of ModuleDTO's with all the root modules
	 */
	public ModuleDTO[] getModule_AllRootModules();

	/**
	 * Checks if there is a module defined.
	 * 
	 * @return a boolean
	 */
	public boolean isDefined();

	/**
	 * Checks if a module has been mapped
	 * 
	 * @return a boolean
	 */
	public boolean isMapped();

	/**
	 * Sets the load state on false
	 */
	public void isReanalyzed();

	/**
	 * Loads the logical architecture data
	 * 
	 * @param e
	 *            is an element with workspace data
	 */
	public void loadLogicalArchitectureData(Element e);
}
