package husacct.define;

import java.util.ArrayList;

import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.RuleDTO;
import husacct.define.domain.Application;
import husacct.define.domain.AppliedRule;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.module.Module;

public class DomainParser {

	/**
	 * Application
	 **/
	public ApplicationDTO parseApplication(Application app) {
		String name = app.getName();
		String[] paths = app.getPaths();
		String programmingLanguage = app.getLanguage();
		String version = app.getVersion();
		ApplicationDTO appDTO = new ApplicationDTO(name, paths, programmingLanguage, version);
		return appDTO;
	}
	/**
	 * Modules
	 **/
	public ModuleDTO[] parseModules(Module[] modules){
		ArrayList<ModuleDTO> moduleDTOsList = new ArrayList<ModuleDTO>();
		for (Module module : modules){
			ModuleDTO moduleDTO = parseModule(module);
			moduleDTOsList.add(moduleDTO);
		}
		ModuleDTO[] moduleDTOs = new ModuleDTO[moduleDTOsList.size()];
		moduleDTOsList.toArray(moduleDTOs);
		return moduleDTOs;
	}
	
	public ModuleDTO[] parseRootModules(Module[] modules){
		ArrayList<ModuleDTO> moduleDTOsList = new ArrayList<ModuleDTO>();
		for (Module module : modules){
			ModuleDTO moduleDTO = parseRootModule(module);
			moduleDTOsList.add(moduleDTO);
		}
		ModuleDTO[] moduleDTOs = new ModuleDTO[moduleDTOsList.size()];
		moduleDTOsList.toArray(moduleDTOs);
		return moduleDTOs;
	}
	
	//TODO performance check if you have to parse SubModules
	public ModuleDTO parseModule(Module module){
		String logicalPath = getLogicalPath(module.getId());
		String[] physicalPaths = module.getPhysicalPaths();
		String type = module.getType();
		
		
		ArrayList<ModuleDTO> subModuleDTOsList = new ArrayList<ModuleDTO>();
		for (Module subModule : module.getSubModules()){
			ModuleDTO subModuleDTO = parseModule(subModule);
			subModuleDTOsList.add(subModuleDTO);
		}
		
		ModuleDTO[] subModuleDTOs = new ModuleDTO[subModuleDTOsList.size()];
		subModuleDTOsList.toArray(subModuleDTOs);
		ModuleDTO[] subModules = subModuleDTOs; 
		
		ModuleDTO modDTO = new ModuleDTO(logicalPath, physicalPaths, type, subModules);
		return modDTO;
	}
	
	public ModuleDTO parseRootModule(Module module){
		String logicalPath = getLogicalPath(module.getId());
		String[] physicalPaths = module.getPhysicalPaths();
		String type = module.getType();
		ModuleDTO[] subModules = new ModuleDTO[0]; 
		
		ModuleDTO modDTO = new ModuleDTO(logicalPath, physicalPaths, type, subModules);
		return modDTO;
	}
	
	public String getLogicalPath(long moduleId){
		String logicalPath = SoftwareArchitecture.getInstance().getModulesLogicalPath(moduleId);
		return logicalPath;
	}
	
	/**
	 * Applied Rules
	 **/
	public RuleDTO[] parseRules(AppliedRule[] rules) {
		ArrayList<RuleDTO> ruleDTOsList = new ArrayList<RuleDTO>();
		for (AppliedRule rule : rules){
			RuleDTO ruleDTO = parseRule(rule);
			ruleDTOsList.add(ruleDTO);
		}
		RuleDTO[] ruleDTOs = new RuleDTO[ruleDTOsList.size()];
		ruleDTOsList.toArray(ruleDTOs);
		return ruleDTOs;
	}
	
	public RuleDTO parseRule(AppliedRule rule){
		
		String ruleTypeKey = rule.getRuleType();
		ModuleDTO moduleFrom = parseModule(rule.getModuleFrom());
		ModuleDTO moduleTo = parseModule(rule.getModuleTo());
		String[] violationTypeKeys = rule.getDependencies();
		String regex = rule.getRegex();
		
		ArrayList<RuleDTO> exceptionRuleList = new ArrayList<RuleDTO>();
		for (AppliedRule exceptionRule : rule.getExceptions()){
			RuleDTO exceptionRuleDTO = parseRule(exceptionRule);
			exceptionRuleList.add(exceptionRuleDTO);
		}
		
		RuleDTO[] exceptionRuleDTOs = new RuleDTO[exceptionRuleList.size()];
		exceptionRuleList.toArray(exceptionRuleDTOs);
		RuleDTO[] exceptionRules = exceptionRuleDTOs; 
		
		RuleDTO ruleDTO = new RuleDTO(ruleTypeKey, moduleTo, moduleFrom, violationTypeKeys, regex, exceptionRules);
		return ruleDTO;
	}
}
