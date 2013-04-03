package husacct.define.domain.services;

import husacct.ServiceProvider;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.domain.SoftwareUnitDefinition.Type;
import husacct.define.domain.module.Module;
import husacct.define.task.JtreeController;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class SoftwareUnitDefinitionDomainService {
	
	public ArrayList<String> getSoftwareUnitNames(long moduleId) {
		Module module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		ArrayList<SoftwareUnitDefinition> softwareUnits = module.getUnits();
		ArrayList<String> softwareUnitNames = new ArrayList<String>();
		for (SoftwareUnitDefinition unit : softwareUnits){
			softwareUnitNames.add(unit.getName());
		}
		return softwareUnitNames;
	}
	
	public ArrayList<SoftwareUnitDefinition> getSoftwareUnit(long moduleId) {
		Module module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		ArrayList<SoftwareUnitDefinition> softwareUnits = module.getUnits();
		return softwareUnits;
	}
	
	public String getSoftwareUnitType(String softwareUnitName) {
		SoftwareUnitDefinition unit = getSoftwareUnitByName(softwareUnitName);
		String softwareUnitType = unit.getType().toString();
		return softwareUnitType;
	}
	
	public SoftwareUnitDefinition getSoftwareUnitByName(String softwareUnitName){
		Module module = SoftwareArchitecture.getInstance().getModuleBySoftwareUnit(softwareUnitName);
		SoftwareUnitDefinition softwareUnit = module.getSoftwareUnitByName(softwareUnitName);
		return softwareUnit;
	}

	public void addSoftwareUnit(long moduleId, String softwareUnit, String t) {
		Module module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		try {
			Type type = Type.valueOf(t);
			SoftwareUnitDefinition unit = new SoftwareUnitDefinition(softwareUnit, type);
			module.addSUDefinition(unit);
		} catch (Exception e){
			Logger.getLogger(SoftwareUnitDefinitionDomainService.class).error("Undefined softwareunit type: " + t);
			Logger.getLogger(SoftwareUnitDefinitionDomainService.class).error(e.getMessage());
		}
		ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
	}
	
	public void removeSoftwareUnit(long moduleId, String softwareUnit) {
		Module module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		SoftwareUnitDefinition unit = getSoftwareUnitByName(softwareUnit);
		module.removeSUDefintion(unit);
		JtreeController.registerTreeRestore(unit.getName());
		
		ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
	}
}
