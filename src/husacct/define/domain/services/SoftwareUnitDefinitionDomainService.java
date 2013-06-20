package husacct.define.domain.services;

import husacct.ServiceProvider;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.SoftwareUnitRegExDefinition;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.stateservice.StateService;
import husacct.define.domain.softwareunit.ExpressionUnitDefinition;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition.Type;
import husacct.define.task.JtreeController;
import husacct.define.task.components.AbstractCombinedComponent;
import husacct.define.task.components.AnalyzedModuleComponent;
import husacct.define.task.components.RegexComponent;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class SoftwareUnitDefinitionDomainService {

	public void addSoftwareUnit(long moduleId,
			AnalyzedModuleComponent softwareunit) {
		ModuleStrategy module = SoftwareArchitecture.getInstance().getModuleById(moduleId);

		try {
			
			Logger.getLogger(SoftwareUnitDefinitionDomainService.class).info(
					"cheking if regex wrapper " + softwareunit.getType()
					+ "ok " + softwareunit.getUniqueName());
			if (softwareunit instanceof RegexComponent) {
				ExpressionUnitDefinition ex = new ExpressionUnitDefinition(softwareunit.getUniqueName(),SoftwareUnitDefinition.Type.REGEX);
				for (AbstractCombinedComponent ir : ((RegexComponent) softwareunit).getChildren()) {
					Type typet = Type.valueOf(ir.getType());
					SoftwareUnitDefinition unitt = new SoftwareUnitDefinition(
							ir.getUniqueName(), typet);
					ex.addSoftwareUnit(unitt);
					
				}
				
				StateService.instance().registerAnalyzedUnit(softwareunit);
				
				module.addSUDefinition(ex);
			
				
				
			} else {
				Type type = Type.valueOf(softwareunit.getType());
				SoftwareUnitDefinition unit = new SoftwareUnitDefinition(
						softwareunit.getUniqueName(), type);
				module.addSUDefinition(unit);
				JtreeController.instance().getTree().removeTreeItem(softwareunit);
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
		ModuleStrategy module = SoftwareArchitecture.getInstance().getModuleById(
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
		
		
		try {
		
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
		ModuleStrategy module = SoftwareArchitecture.getInstance()
				.getModuleByRegExSoftwareUnit(softwareUnitName);
		SoftwareUnitRegExDefinition softwareUnit = module
				.getRegExSoftwareUnitByName(softwareUnitName);
		return softwareUnit;
	}

	public ArrayList<String> getRegExSoftwareUnitNames(long moduleId) {
		ModuleStrategy module = SoftwareArchitecture.getInstance().getModuleById(
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
		ModuleStrategy module = SoftwareArchitecture.getInstance().getModuleById(
				moduleId);
		ArrayList<SoftwareUnitDefinition> softwareUnits = module.getUnits();
		return softwareUnits;
	}

	public SoftwareUnitDefinition getSoftwareUnitByName(String softwareUnitName) {
		ModuleStrategy module = SoftwareArchitecture.getInstance()
				.getModuleBySoftwareUnit(softwareUnitName);
		System.out.println(">>>>>>>>>>>  "+module.getName());
		SoftwareUnitDefinition softwareUnit = module
				.getSoftwareUnitByName(softwareUnitName);
		
		return softwareUnit;
	}

	public ArrayList<String> getSoftwareUnitNames(long moduleId) {
		ArrayList<String> softwareUnitNames = new ArrayList<String>();
		try{
			ModuleStrategy module = SoftwareArchitecture.getInstance().getModuleById(
					moduleId);
			ArrayList<SoftwareUnitDefinition> softwareUnits = module.getUnits();

			for (SoftwareUnitDefinition unit : softwareUnits) {
				softwareUnitNames.add(unit.getName());
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return softwareUnitNames;
	}

	public String getSoftwareUnitType(String softwareUnitName) {
		SoftwareUnitDefinition unit = getSoftwareUnitByName(softwareUnitName);
		String softwareUnitType = unit.getType().toString();
		return softwareUnitType;
	}

	private void RegisterRegixSoftwareUnits(RegexComponent softwareunit,
			ModuleStrategy parent, SoftwareUnitDefinition rootunit) {
		// TODO: Is this one still needed:
		// Regexmodule regex = new Regexmodule()
		JtreeController.instance().getTree()
		.removeRegexTreeItem(parent.getId(), softwareunit);

	}

	public ExpressionUnitDefinition removeRegExSoftwareUnit(long moduleId, String softwareUnit) {
		ModuleStrategy module = SoftwareArchitecture.getInstance().getModuleById(
				moduleId);

	
		SoftwareUnitDefinition unit = getSoftwareUnitByName(softwareUnit);
		module.removeSUDefintion(unit);
		
		
		JtreeController.instance().restoreRegexWrapper((ExpressionUnitDefinition)unit);
		
		ServiceProvider.getInstance().getDefineService()
		.notifyServiceListeners();
		return (ExpressionUnitDefinition)unit;
	}
	public void removeSoftwareUnit(long moduleId, String softwareUnit) {
		ModuleStrategy module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		SoftwareUnitDefinition unit = getSoftwareUnitByName(softwareUnit);
		module.removeSUDefintion(unit);
		
		StateService.instance().removeSoftwareUnit(module, unit);
		ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
	}
	public void addSoftwareUnit(long moduleId,
			ArrayList<AnalyzedModuleComponent> units) {




		ModuleStrategy module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		try{

		
		for (AnalyzedModuleComponent softwareunit : units) {
			
			Type type = Type.valueOf(softwareunit.getType());
			SoftwareUnitDefinition unit = new SoftwareUnitDefinition(softwareunit.getUniqueName(), type);
			Logger.getLogger(SoftwareUnitDefinitionDomainService.class).info("cheking if regex wrapper "+softwareunit.getType()+"ok "+softwareunit.getUniqueName());
			if(softwareunit instanceof RegexComponent)
			{
				module.addSUDefinition(unit);
			   
				RegisterRegixSoftwareUnits((RegexComponent)softwareunit,module,unit);
				JtreeController.instance().removeRegexTreeItem((RegexComponent)softwareunit);
			
			}else{

				
				module.addSUDefinition(unit);
				JtreeController.instance().removeTreeItem(softwareunit);
				

			}
		}

			WarningMessageService.getInstance().processModule(module);
		}catch(Exception e){
			//	Logger.getLogger(SoftwareUnitDefinitionDomainService.class).error("Undefined softwareunit type: " + softwareunit.getType());
			Logger.getLogger(SoftwareUnitDefinitionDomainService.class).error(e.getMessage());
			System.out.println(e.getStackTrace());
		}
		ServiceProvider.getInstance().getDefineService().notifyServiceListeners();







	}

	public void removeSoftwareUnit(long moduleId,
			ArrayList<AnalyzedModuleComponent> data) {
		ModuleStrategy module = SoftwareArchitecture.getInstance().getModuleById(moduleId);

		for (AnalyzedModuleComponent units : data) {

			SoftwareUnitDefinition unit = getSoftwareUnitByName(units.getUniqueName());
			module.removeSUDefintion(unit);
			WarningMessageService.getInstance().processModule(module);
			StateService.instance().removeSoftwareUnit(module, unit);
			ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
		}

	}
	public ExpressionUnitDefinition getExpressionByName(long ModuleId,String name)
	{
		ModuleStrategy module = SoftwareArchitecture.getInstance().getModuleById(ModuleId);
		return  (ExpressionUnitDefinition) module.getSoftwareUnitByName(name); 
		
	}

	public void editRegex(long selectedModuleId,
			ArrayList<AnalyzedModuleComponent> components, String editingRegEx) {
	       removeRegExSoftwareUnit(selectedModuleId, editingRegEx);
	     RegexComponent tobesaved=  JtreeController.instance().createRegexRepresentation(editingRegEx,components);
	     addSoftwareUnit(selectedModuleId, tobesaved);
		
	}
}
