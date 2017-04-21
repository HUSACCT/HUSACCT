package husacct.define.domain.services;

import husacct.ServiceProvider;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.stateservice.StateService;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition.Type;
import husacct.define.task.DefinitionController;
import husacct.define.task.JtreeController;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class SoftwareUnitDefinitionDomainService {

	public void addSoftwareUnitsToModule(long moduleId, ArrayList<AnalyzedModuleComponent> units) {
		ModuleStrategy module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		if (module != null) {
			try {
				for (AnalyzedModuleComponent softwareunit : units) {
					Type type = Type.valueOf(softwareunit.getType());
					SoftwareUnitDefinition unit = new SoftwareUnitDefinition(softwareunit.getUniqueName(), type);
					module.addSUDefinition(unit);
					JtreeController.instance().removeTreeItem(softwareunit);
				}
				WarningMessageService.getInstance().processModule(module);
			} catch (Exception e) {
				Logger.getLogger(SoftwareUnitDefinitionDomainService.class).error(e.getMessage());
				// System.out.println(e.getStackTrace());
			}
			ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
		}
	}

	public ArrayList<SoftwareUnitDefinition> getSoftwareUnit(long moduleId) {
		ArrayList<SoftwareUnitDefinition> softwareUnits = null;
		ModuleStrategy module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		if (module != null) {
			softwareUnits = module.getUnits();
		}
		return softwareUnits;
	}

	// Returns null, if no SoftwareUnit with softwareUnitName is mapped to a ModuleStrategy	
	public SoftwareUnitDefinition getSoftwareUnitByName(String softwareUnitName) {
		ModuleStrategy module = SoftwareArchitecture.getInstance().getModuleBySoftwareUnit(softwareUnitName);
		SoftwareUnitDefinition softwareUnit = null;
		if (module != null){
			softwareUnit = module.getSoftwareUnitByName(softwareUnitName);
		}	
		return softwareUnit;
	}

	public ArrayList<String> getSoftwareUnitNames(long moduleId) {
		ArrayList<String> softwareUnitNames = new ArrayList<String>();
		try {
			ModuleStrategy module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
			if (module != null) {
				ArrayList<SoftwareUnitDefinition> softwareUnits = module.getUnits();
				for (SoftwareUnitDefinition unit : softwareUnits) {
					softwareUnitNames.add(unit.getName());
				}
			}
		} catch (Exception e) {
			Logger.getLogger(SoftwareUnitDefinitionDomainService.class).error(e.getMessage());
		}
		return softwareUnitNames;
	}

	// Returns "", if no SoftwareUnit with softwareUnitName is mapped to a ModuleStrategy	
	public String getSoftwareUnitType(String softwareUnitName) {
		SoftwareUnitDefinition unit = getSoftwareUnitByName(softwareUnitName);
		String softwareUnitType = "";
		if (unit != null){
			softwareUnitType = unit.getType().toString();
		}
		return softwareUnitType;
	}

	public void removeSoftwareUnit(long moduleId, String softwareUnit) {
		ModuleStrategy module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		if (module != null){
			SoftwareUnitDefinition unit = getSoftwareUnitByName(softwareUnit);
			if (unit != null){
				module.removeSUDefintion(unit);
				StateService.instance().removeSoftwareUnit(module, unit);
				ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
			}
		}
	}

	public void removeSoftwareUnit(long moduleId, ArrayList<AnalyzedModuleComponent> data) {
		ModuleStrategy module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		if (module != null){
			for (AnalyzedModuleComponent units : data) {
				SoftwareUnitDefinition unit = getSoftwareUnitByName(units.getUniqueName());
				if(unit != null){
					module.removeSUDefintion(unit);
					WarningMessageService.getInstance().processModule(module);
					StateService.instance().removeSoftwareUnit(module, unit);
					ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
				}
			}
		}
	}

	public void changeSoftwareUnit(long from, long to, ArrayList<String> names) {
		SoftwareArchitecture.getInstance().changeSoftwareUnit(from,to,names);
	}
	
}
