package husacct.define;

import husacct.common.dto.ModuleDTO;
import husacct.common.dto.SoftwareUnitDTO;

import java.util.ArrayList;

// Services for SAR: Software Architecture Reconstruction
public interface IDefineSarService {

	/** Adds a module to the intended architecture
	 * Argument name is the simple name, so not the logical path of the module, which is formed by the combination of parentLogicalPath + "." + name.
	 * Returns a ModuleDTO with filled fields, if adding was successful, or empty fields, if not.
	 */
	public ModuleDTO addModule(String name, String parentLogicalPath, String moduleType, int hierarchicalLevel, ArrayList<SoftwareUnitDTO> softwareUnits);
	
	/** Edits an existing module of the intended architecture
	 * Argument logicalPath identifies the existing module. If the other arguments don't have a null-value (or 0 for int), the value is set as new attribute value. 
	 * Argument name is the simple name, so not the logical path of the module.  
	 * @param newType TODO
	 */
	public ModuleDTO editModule(String logicalPath, String newType, String newName, int newHierarchicalLevel, ArrayList<SoftwareUnitDTO> newSoftwareUnits);

	/** Removes an existing module from the intended architecture
	 * Argument logicalPath identifies the existing module. 
	 */
	public void removeModule(String logicalPath);
	
	/** Adds a main rule to the intended architecture. So not usable for exception rules.
	 * Returns true, if the rule is really added, and false if the rule is not added (in that case, watch the warnings in the console).
	 * If added, the isEnabled = true, isException = false, and the all ViolationTypes default  for the ruleTypeKey are added. 
	 */
	boolean addMainRule(String moduleFromLogicalPath, String moduleTologicalPath, String ruleTypeKey);
	
	/** Edits a rule, but only for the attribute isEnabled. Works for main rules and exception rules.
	 * Returns true, if the change is set, and false if not.
	 */
	boolean editRule_IsEnabled(String moduleFromLogicalPath, String moduleTologicalPath, String ruleTypeKey, boolean isEnabled);

	/**
	 * Gets the module selected by the user in the view "Define intended architecture". 
	 * @return a ModuleDTO; with empty values if no module is selected or if the selected module is not found.
	 */
	public ModuleDTO getModule_SelectedInGUI();
	
	/**
	 * Updates the GUI by refreshing the module tree within DefinitionInternalFrame. 
	 * Note: Call only after all reconstruction work is done. 
	 */
	public void updateModulePanel(String selectedModuleLogicalPath);

}
