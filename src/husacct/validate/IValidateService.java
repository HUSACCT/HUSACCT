package husacct.validate;

import husacct.common.dto.CategoryDTO;
import husacct.common.dto.RuleTypeDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.services.IObservableService;

import java.io.File;
import java.util.Calendar;

import javax.swing.JInternalFrame;

public interface IValidateService extends IObservableService {

	/**
	 * Gets all the Categories of all the available ruletypes The RuleTypeDTO
	 * contains RuleTypeDTOs The RuleTypeDTO contains ViolationTypeDTOs
	 * 
	 * @return returns an array of CategoryDTO's
	 */
	public CategoryDTO[] getCategories();

	/**
	 * Gets all violations by a specific logicalPath
	 * 
	 * @param logicalpathFrom the 'from' logical path
	 * @param logicalpathTo the 'to' logical path
	 * @return an array of ViolationDTO's
	 */
	public ViolationDTO[] getViolationsByLogicalPath(String logicalpathFrom,
			String logicalpathTo);

	/**
	 * Gets all violations by a specific physical path
	 * 
	 * @param logicalpathFrom the 'from' physical path
	 * @param logicalpathTo the 'to' physical path
	 * @return an array of ViolationDTO's
	 */
	public ViolationDTO[] getViolationsByPhysicalPath(String physicalpathFrom,
			String physicalpathTo);

	/**
	 * Checks all defined rules, and saved the found violations.
	 */
	public void checkConformance();

	/**
	 * Returns the date of the violations
	 * 
	 * @return returns an array of Calendar objects
	 */
	public Calendar[] getViolationHistoryDates();

	/**
	 * Returns a list of supported extensions to which the diagrams can be
	 * exported
	 * 
	 * @return an array of string extensions.
	 */
	public String[] getExportExtentions();

	/**
	 * Returns a list of supported default rule types for a given module
	 * 
	 * @returns an array of RuleType objects
	 */
	public RuleTypeDTO[] getDefaultRuleTypesOfModule(String type);

	/**
	 * Returns a list of supported allowed rule types for a given module
	 * 
	 * @returns an array of RuleType objects
	 */
	public RuleTypeDTO[] getAllowedRuleTypesOfModule(String type);

	/**
	 * Export the found violations to a file
	 * 
	 * @param file the file to write the violations to
	 * @param fileType the type of file (extension)
	 * @param date the date on which the violations have been found.
	 */
	public void exportViolations(File file, String fileType, Calendar date);

	/**
	 * Export the found violations to a file
	 * 
	 * @param file the file to write the violations to
	 * @param fileType the type of file (extension)
	 */
	public void exportViolations(File file, String fileType);

	/**
	 * Indicates whether the source code has been validated yet.
	 * 
	 * @return TRUE if validated, FALSE if not validated.
	 */
	public boolean isValidated();

	/**
	 * Returns a JInternalFrame where the user can browse the found violations
	 * 
	 * @return the JInternalFrame
	 */
	public JInternalFrame getBrowseViolationsGUI();

	/**
	 * Returns a JInternalFrame where the user can configure
	 * 
	 * @return returns a JInteralFrame which is the configuration GUI
	 */
	public JInternalFrame getConfigurationGUI();
}