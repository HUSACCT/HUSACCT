package husacct.define.domain.services;

import husacct.ServiceProvider;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.domain.SoftwareUnitDefinition.Type;
import husacct.define.domain.SoftwareUnitRegExDefinition;
import husacct.define.domain.module.Module;
import husacct.define.task.JtreeController;
import husacct.define.task.components.AnalyzedModuleComponent;
import husacct.define.task.components.RegexComponent;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class SoftwareUnitDefinitionDomainService {

    public void addSoftwareUnit(long moduleId,
	    AnalyzedModuleComponent softwareunit) {
	Module module = SoftwareArchitecture.getInstance().getModuleById(
		moduleId);

	try {
	    Type type = Type.valueOf(softwareunit.getType());
	    SoftwareUnitDefinition unit = new SoftwareUnitDefinition(
		    softwareunit.getUniqueName(), type);
	    Logger.getLogger(SoftwareUnitDefinitionDomainService.class).info(
		    "cheking if regex wrapper " + softwareunit.getType()
			    + "ok " + softwareunit.getUniqueName());
	    if (softwareunit instanceof RegexComponent) {
		module.addSUDefinition(unit);
		RegisterRegixSoftwareUnits((RegexComponent) softwareunit,
			module, unit);
	    } else {
		module.addSUDefinition(unit);
		JtreeController.instance().getTree()
			.removeTreeItem(moduleId, softwareunit);
	    }
	    WarningMessageService.getInstance().processModule(module);
	} catch (Exception e) {
	    Logger.getLogger(SoftwareUnitDefinitionDomainService.class).error(
		    "Undefined softwareunit type: " + softwareunit.getType());
	    Logger.getLogger(SoftwareUnitDefinitionDomainService.class).error(
		    e.getMessage());
	    System.out.println(e.getStackTrace());
	}
	ServiceProvider.getInstance().getDefineService()
		.notifyServiceListeners();
    }

    public void addSoftwareUnit(long moduleId, String softwareUnit, String t) {
	System.out.println("unit flow  ...." + softwareUnit);
	Module module = SoftwareArchitecture.getInstance().getModuleById(
		moduleId);
	try {
	    Type type = Type.valueOf(t);
	    SoftwareUnitDefinition unit = new SoftwareUnitDefinition(
		    softwareUnit, type);

	    module.addSUDefinition(unit);
	} catch (Exception e) {
	    Logger.getLogger(SoftwareUnitDefinitionDomainService.class).error(
		    "Undefined softwareunit type: " + t);
	    Logger.getLogger(SoftwareUnitDefinitionDomainService.class).error(
		    e.getMessage());
	}
	ServiceProvider.getInstance().getDefineService()
		.notifyServiceListeners();
    }

    public void addSoftwareUnitToRegex(long moduleId,
	    ArrayList<AnalyzedModuleComponent> softwareUnits, String regExName) {
	SoftwareUnitRegExDefinition regExDefinition = new SoftwareUnitRegExDefinition(
		regExName);
	try {
	    for (AnalyzedModuleComponent softwareUnit : softwareUnits) {
		Type type = Type.valueOf(softwareUnit.getType());
		SoftwareUnitDefinition unit = new SoftwareUnitDefinition(
			softwareUnit.getUniqueName(), type);
		regExDefinition.addSoftwareUnitDefinition(unit);
	    }
	    RegexComponent regixwrapper = JtreeController.instance()
		    .registerRegix(regExName);
	    addSoftwareUnit(moduleId, regixwrapper);
	} catch (Exception e) {
	    Logger.getLogger(SoftwareUnitDefinitionDomainService.class).error(
		    "Undefined softwareunit");
	    Logger.getLogger(SoftwareUnitDefinitionDomainService.class).error(
		    e.getMessage());
	}
	ServiceProvider.getInstance().getDefineService()
		.notifyServiceListeners();
    }

    public SoftwareUnitRegExDefinition getRegExSoftwareUnitByName(
	    String softwareUnitName) {
	Module module = SoftwareArchitecture.getInstance()
		.getModuleByRegExSoftwareUnit(softwareUnitName);
	SoftwareUnitRegExDefinition softwareUnit = module
		.getRegExSoftwareUnitByName(softwareUnitName);
	return softwareUnit;
    }

    public ArrayList<String> getRegExSoftwareUnitNames(long moduleId) {
	Module module = SoftwareArchitecture.getInstance().getModuleById(
		moduleId);
	ArrayList<SoftwareUnitRegExDefinition> softwareUnits = module
		.getRegExUnits();
	ArrayList<String> softwareUnitNames = new ArrayList<String>();
	for (SoftwareUnitRegExDefinition unit : softwareUnits) {
	    softwareUnitNames.add(unit.getName());
	}
	return softwareUnitNames;
    }

    public ArrayList<SoftwareUnitDefinition> getSoftwareUnit(long moduleId) {
	Module module = SoftwareArchitecture.getInstance().getModuleById(
		moduleId);
	ArrayList<SoftwareUnitDefinition> softwareUnits = module.getUnits();
	return softwareUnits;
    }

    public SoftwareUnitDefinition getSoftwareUnitByName(String softwareUnitName) {
	Module module = SoftwareArchitecture.getInstance()
		.getModuleBySoftwareUnit(softwareUnitName);
	SoftwareUnitDefinition softwareUnit = module
		.getSoftwareUnitByName(softwareUnitName);
	return softwareUnit;
    }

    public ArrayList<String> getSoftwareUnitNames(long moduleId) {
	Module module = SoftwareArchitecture.getInstance().getModuleById(
		moduleId);
	ArrayList<SoftwareUnitDefinition> softwareUnits = module.getUnits();
	ArrayList<String> softwareUnitNames = new ArrayList<String>();
	for (SoftwareUnitDefinition unit : softwareUnits) {
	    softwareUnitNames.add(unit.getName());
	}
	return softwareUnitNames;
    }

    public String getSoftwareUnitType(String softwareUnitName) {
	SoftwareUnitDefinition unit = getSoftwareUnitByName(softwareUnitName);
	String softwareUnitType = unit.getType().toString();
	return softwareUnitType;
    }

    private void RegisterRegixSoftwareUnits(RegexComponent softwareunit,
	    Module parent, SoftwareUnitDefinition rootunit) {
	// TODO: Is this one still needed:
	// Regexmodule regex = new Regexmodule()
	JtreeController.instance().getTree()
		.removeRegexTreeItem(parent.getId(), softwareunit);

    }

    public void removeRegExSoftwareUnit(long moduleId, String softwareUnit) {
	Module module = SoftwareArchitecture.getInstance().getModuleById(
		moduleId);
	// TODO: Why is this here? Can this be resolved?
	// thread problems
	// SoftwareUnitRegExDefinition unit =
	// getRegExSoftwareUnitByName(softwareUnit);

	Type type = Type.valueOf("regex".toUpperCase());
	SoftwareUnitDefinition unit = new SoftwareUnitDefinition(softwareUnit,
		type);
	module.removeSUDefintion(unit);

	JtreeController.instance().restoreRegexWrapper(softwareUnit);
	ServiceProvider.getInstance().getDefineService()
		.notifyServiceListeners();
    }

    public void removeSoftwareUnit(long moduleId, String softwareUnit) {
	Module module = SoftwareArchitecture.getInstance().getModuleById(
		moduleId);
	SoftwareUnitDefinition unit = getSoftwareUnitByName(softwareUnit);
	module.removeSUDefintion(unit);
	WarningMessageService.getInstance().processModule(module);
	// TODO: This is a quick fix
	try {
	    JtreeController.instance().registerTreeRestore(moduleId,
		    softwareUnit);
	} catch (NullPointerException exe) {
	    // TODO: Create exception handling
	}
	ServiceProvider.getInstance().getDefineService()
		.notifyServiceListeners();
    }
}
