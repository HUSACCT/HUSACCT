package husacct.validate;

import husacct.common.dto.CategoryDTO;
import husacct.common.dto.RuleDTO;
import husacct.common.dto.RuleTypeDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.services.IObservableService;
import husacct.externalinterface.ViolationReportDTO;
import husacct.validate.domain.validation.Violation;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;

import javax.swing.JInternalFrame;

import org.jdom2.Document;

public interface IValidateService extends IObservableService {

	/**
	 * Gets all the Categories of all the available ruletypes The RuleTypeDTO
	 * contains RuleTypeDTOs The RuleTypeDTO contains ViolationTypeDTOs
	 * 
	 * @return returns an array of CategoryDTO's
	 */
	public CategoryDTO[] getCategories();

	/**
	 * This method is used only for testing within the Test suite
	 */
	public SimpleEntry<Calendar, List<Violation>> getAllViolations();

	
	/**
	 * Gets all violations by a specific logicalPath
	 * 
	 * @param logicalpathFrom the 'from' logical path
	 * @param logicalpathTo the 'to' logical path
	 * @return an array of ViolationDTO's
	 */
	public ViolationDTO[] getViolationsByLogicalPath(String logicalpathFrom, String logicalpathTo);

	/**
	 * Gets all violations by a specific physical path
	 * 
	 * @param logicalpathFrom the 'from' physical path
	 * @param logicalpathTo the 'to' physical path
	 * @return an array of ViolationDTO's
	 */
	public ViolationDTO[] getViolationsByPhysicalPath(String physicalpathFrom, String physicalpathTo);

	/**
	 * Gets the detected violations of the given rule.
	 * 
	 * @param appliedRule is one RuleDTO.
	 * @return an array of ViolationDTO's
	 */
	public ViolationDTO[] getViolationsByRule(RuleDTO appliedRule);

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
	public RuleTypeDTO[] getDefaultRuleTypesOfModule(String moduleType);

	/**
	 * Returns a list of supported allowed rule types for a given module
	 * 
	 * @returns an array of RuleType objects
	 */
	public RuleTypeDTO[] getAllowedRuleTypesOfModule(String moduleType);

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
	 * Identifies new violations in comparison to the violations in previousViolationsFile
	 * @param exportAllViolations TODO
	 * @param exportNewViolations TODO
	 * @param previousViolationsFile: an xml-file containing the data of violations detected in the past
	 * @return Array of identified new violations (only the new ones)
	 */
	public ViolationReportDTO getViolationReportData(Document previousViolations, boolean exportAllViolations, boolean exportNewViolations);
	
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
	
	/**
	 * Set the value of a allowed rule of a module
	 */
	public void setAllowedRuleTypeOfModule(String moduleType, String ruleTypeKey, boolean value);
	
	/**
	 * Set the value of a default rule of a module
	 */
	public void setDefaultRuleTypeOfModule(String moduleType, String ruleTypeKey, boolean value);
}