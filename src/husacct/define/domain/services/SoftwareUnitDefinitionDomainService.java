package husacct.define.domain.services;

import husacct.ServiceProvider;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.domain.SoftwareUnitDefinition.Type;
import husacct.define.domain.SoftwareUnitRegExDefinition;
import husacct.define.domain.module.Module;
import husacct.define.task.JtreeController;
import husacct.define.task.components.AbstractCombinedComponent;
import husacct.define.task.components.AnalyzedModuleComponent;
import husacct.define.task.components.RegexComponent;

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
	
	public ArrayList<String> getRegExSoftwareUnitNames(long moduleId) {
		Module module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		ArrayList<SoftwareUnitRegExDefinition> softwareUnits = module.getRegExUnits();
		ArrayList<String> softwareUnitNames = new ArrayList<String>();
		for (SoftwareUnitRegExDefinition unit : softwareUnits){
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
	
	public SoftwareUnitRegExDefinition getRegExSoftwareUnitByName(String softwareUnitName){
		Module module = SoftwareArchitecture.getInstance().getModuleByRegExSoftwareUnit(softwareUnitName);
		SoftwareUnitRegExDefinition softwareUnit = module.getRegExSoftwareUnitByName(softwareUnitName);
		return softwareUnit;
	}

	public void addSoftwareUnit(long moduleId, String softwareUnit, String t) {
        System.out.println("unit flow  ...."+softwareUnit);
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
	
	
	public void addSoftwareUnit(long moduleId, AnalyzedModuleComponent softwareunit) {
		
	
		
		
		Module module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		
		try {
			
			
			Type type = Type.valueOf(softwareunit.getType().toUpperCase());
			SoftwareUnitDefinition unit = new SoftwareUnitDefinition(softwareunit.getUniqueName(), type);
			module.addSUDefinition(unit);
			Logger.getLogger(SoftwareUnitDefinitionDomainService.class).info("cheking if regex wrapper ");
			if(softwareunit instanceof RegexComponent)
			{
				RegisterRegixSoftwareUnits((RegexComponent)softwareunit,moduleId);
			}else{
				JtreeController.instance().getTree().removeTreeItem(moduleId, softwareunit);
			}
			
		} catch (Exception e){
			Logger.getLogger(SoftwareUnitDefinitionDomainService.class).error("Undefined softwareunit type: " + softwareunit.getType());
			Logger.getLogger(SoftwareUnitDefinitionDomainService.class).error(e.getMessage());
		}
		ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
	}
	
	
	private void RegisterRegixSoftwareUnits(RegexComponent softwareunit,long id) {
		for(AbstractCombinedComponent units : softwareunit.getChildren())
		{
		AnalyzedModuleComponent	unitsToBeRegistered= (AnalyzedModuleComponent) units;
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+softwareunit.getChildren().size());	
		JtreeController.instance().getTree().removeTreeItem(id, unitsToBeRegistered);
			
			
		}
		
	}

	public void addSoftwareUnitToRegex(long moduleId, ArrayList<AnalyzedModuleComponent> softwareUnits, String regExName) {
		
		Module module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		SoftwareUnitRegExDefinition regExDefinition = new SoftwareUnitRegExDefinition(regExName);
		
		try {
			for(AnalyzedModuleComponent softwareUnit : softwareUnits) {
				Type type = Type.valueOf(softwareUnit.getType().toUpperCase());
				SoftwareUnitDefinition unit = new SoftwareUnitDefinition(softwareUnit.getUniqueName(), type);
				regExDefinition.addSoftwareUnitDefinition(unit);
			}
	AnalyzedModuleComponent regixwrapper=JtreeController.instance().registerRegix(regExName);
		addSoftwareUnit(moduleId, regixwrapper);
		} catch (Exception e){
			Logger.getLogger(SoftwareUnitDefinitionDomainService.class).error("Undefined softwareunit");
			Logger.getLogger(SoftwareUnitDefinitionDomainService.class).error(e.getMessage());
		}
		
		ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
	}
	
	public void removeSoftwareUnit(long moduleId, String softwareUnit) {
		Module module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		SoftwareUnitDefinition unit = getSoftwareUnitByName(softwareUnit);
		module.removeSUDefintion(unit);
		//quikfix
		try{
			JtreeController.instance().registerTreeRestore(moduleId, softwareUnit);
		}catch(NullPointerException exe){}
		ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
	}
	
	public void removeRegExSoftwareUnit(long moduleId, String softwareUnit) {
		Module module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		SoftwareUnitRegExDefinition unit = getRegExSoftwareUnitByName(softwareUnit);
		module.removeSURegExDefinition(unit);
		//quikfix
		try{
			JtreeController.instance().registerTreeRestore(moduleId, softwareUnit);
		}catch(NullPointerException exe){}
		ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
	}
}
