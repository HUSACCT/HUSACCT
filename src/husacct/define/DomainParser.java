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

	public ApplicationDTO parseApplication(Application app) {
		ApplicationDTO appDTO = new ApplicationDTO();
		appDTO.name = app.getName();
		appDTO.paths = app.getPaths();
		appDTO.programmingLanguage = app.getLanguage();
		appDTO.version = app.getVersion();
		return appDTO;
	}
	
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
	
	public ModuleDTO parseModule(Module module){
		ModuleDTO modDTO = new ModuleDTO();
		modDTO.logicalPath = getLogicalPath(module.getId());
		modDTO.physicalPaths = module.getPhysicalPaths();
		modDTO.type = module.getType();
		
		ArrayList<ModuleDTO> subModuleDTOsList = new ArrayList<ModuleDTO>();
		for (Module subModule : module.getSubModules()){
			ModuleDTO subModuleDTO = parseModule(subModule);
			subModuleDTOsList.add(subModuleDTO);
		}
		
		ModuleDTO[] subModuleDTOs = new ModuleDTO[subModuleDTOsList.size()];
		subModuleDTOsList.toArray(subModuleDTOs);
		modDTO.subModules = subModuleDTOs; 
		return modDTO;
	}
	
	public RuleDTO[] parseRule(AppliedRule[] rules) {
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
		RuleDTO ruleDTO = new RuleDTO();
		ruleDTO.ruleTypeKey = rule.getRuleType();
		ruleDTO.moduleFrom = parseModule(rule.getUsedModule());
		ruleDTO.moduleTo = parseModule(rule.getRestrictedModule());
		ruleDTO.violationTypeKeys = rule.getDependencies();
		
		ArrayList<RuleDTO> exceptionRuleList = new ArrayList<RuleDTO>();
		for (AppliedRule exceptionRule : rule.getExceptions()){
			RuleDTO exceptionRuleDTO = parseRule(exceptionRule);
			exceptionRuleList.add(exceptionRuleDTO);
		}
		
		RuleDTO[] exceptionRuleDTOs = new RuleDTO[exceptionRuleList.size()];
		exceptionRuleList.toArray(exceptionRuleDTOs);
		ruleDTO.exceptionRules = exceptionRuleDTOs; 
		return ruleDTO;
	}
	
	public String getLogicalPath(long moduleId){
		String logicalPath = SoftwareArchitecture.getInstance().getModulesLogicalPath(moduleId);
		return logicalPath;
	}
}
