package husacct.define;

import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ProjectDTO;
import husacct.common.dto.RuleDTO;
import husacct.common.savechain.ISaveable;
import husacct.common.services.IObservableService;

import java.util.ArrayList;

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
	 * Gets all the children from a module
	 * 
	 * @param logicalPath
	 *            is the logical path, ** is root module
	 * @return an array of ModuleDTO's
	 */
	public ModuleDTO[] getChildrenFromModule(String logicalPath);

	/**
	 * Gets the GUI from Define
	 * 
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
	 * Returns the logical path of the parent from the given module
	 * 
	 * @param logicalPath
	 *            is the logical path of the module ("**" is root)
	 * @return a String containing the logical path to the Parent
	 */
	public String getParentFromModule(String logicalPath);

	/**
	 * Gets all the root modules
	 * 
	 * @return a array of ModuleDTO's with all the root modules
	 */
	public ModuleDTO[] getRootModules();

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
