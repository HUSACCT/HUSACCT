package husacct.define;

import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.PhysicalPathDTO;
import husacct.common.dto.ProjectDTO;
import husacct.common.dto.RuleDTO;
import husacct.define.domain.Application;
import husacct.define.domain.Project;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition;

import java.util.ArrayList;

public class DomainToDtoParser {

	/**
	 * Application
	 **/
	public ApplicationDTO parseApplication(Application app) {
		String name = app.getName();
		ArrayList<ProjectDTO> projects = parseProjects(app.getProjects());
		String version = app.getVersion();
		ApplicationDTO appDTO = new ApplicationDTO(name, projects, version);
		return appDTO;
	}

	/**
	 * Modules
	 **/
	public ModuleDTO parseModule(ModuleStrategy module) {
		String logicalPath = getLogicalPath(module);
		String name = module.getName();
		String type = module.getType();

		ArrayList<ModuleDTO> subModuleDTOsList = new ArrayList<ModuleDTO>();
		ArrayList<ModuleStrategy> subModulesList = module.getSubModules();
		for (ModuleStrategy subModule : subModulesList) {
			ModuleDTO subModuleDTO = parseModule(subModule);
			subModuleDTOsList.add(subModuleDTO);
		}

		ModuleDTO[] subModuleDTOs = new ModuleDTO[subModuleDTOsList.size()];
		subModuleDTOsList.toArray(subModuleDTOs);
		ModuleDTO[] subModules = subModuleDTOs;

		ModuleDTO modDTO = new ModuleDTO(logicalPath, name, type, subModules);
		return modDTO;
	}

	private String getLogicalPath(ModuleStrategy module) {
		String logicalPath = "";
		// If the type is ModuleStrategy then its a placeholder for a
		// non-existing
		// module
		// since you cannot add modules of the type module
		if (!module.getType().equals("ModuleStrategy")) {
			logicalPath = SoftwareArchitecture.getInstance().getModulesLogicalPath(module.getId());
		}
		return logicalPath;
	}

	public ModuleDTO[] parseModules(ModuleStrategy[] modules) {
		ArrayList<ModuleDTO> moduleDTOsList = new ArrayList<ModuleDTO>();
		for (ModuleStrategy module : modules) {
			ModuleDTO moduleDTO = parseModule(module);
			moduleDTOsList.add(moduleDTO);
		}
		ModuleDTO[] moduleDTOs = new ModuleDTO[moduleDTOsList.size()];
		moduleDTOsList.toArray(moduleDTOs);
		return moduleDTOs;
	}

	public PhysicalPathDTO parsePhysicalPathDTO(SoftwareUnitDefinition su) {
		String path = su.getName();
		String type = su.getType().toString();
		PhysicalPathDTO physicalPathDTO = new PhysicalPathDTO(path, type);
		return physicalPathDTO;
	}

	/**
	 * PhysicalPaths
	 **/
	public PhysicalPathDTO[] parsePhysicalPathDTOs(
			ArrayList<SoftwareUnitDefinition> softwareUnits) {
		ArrayList<PhysicalPathDTO> physicalPathDTOList = new ArrayList<PhysicalPathDTO>();
		for (SoftwareUnitDefinition su : softwareUnits) {
			PhysicalPathDTO physicalPathDTO = parsePhysicalPathDTO(su);
			physicalPathDTOList.add(physicalPathDTO);
		}
		PhysicalPathDTO[] physicalPathDTOs = physicalPathDTOList
				.toArray(new PhysicalPathDTO[physicalPathDTOList.size()]);
		return physicalPathDTOs;
	}

	public ArrayList<ProjectDTO> parseProjects(ArrayList<Project> projects) {
		ArrayList<ProjectDTO> projectDTOs = new ArrayList<ProjectDTO>();
		for (Project project : projects) {
			ProjectDTO projectDTO = new ProjectDTO(project.getName(),
					project.getPaths(), project.getProgrammingLanguage(),
					project.getVersion(), project.getDescription(), null);
			projectDTOs.add(projectDTO);
		}
		return projectDTOs;
	}

	public ModuleDTO parseRootModule(ModuleStrategy module) {
		String logicalPath = getLogicalPath(module);
		String name = module.getName();
		String type = module.getType();
		ModuleDTO[] subModules = new ModuleDTO[0];
		ModuleDTO modDTO = new ModuleDTO(logicalPath, name, type, subModules);
		return modDTO;
	}

	public ModuleDTO[] parseRootModules(ModuleStrategy[] modules) {
		ArrayList<ModuleDTO> moduleDTOsList = new ArrayList<ModuleDTO>();
		for (ModuleStrategy module : modules) {
			ModuleDTO moduleDTO = parseRootModule(module);
			moduleDTOsList.add(moduleDTO);
		}
		ModuleDTO[] moduleDTOs = new ModuleDTO[moduleDTOsList.size()];
		moduleDTOsList.toArray(moduleDTOs);
		return moduleDTOs;
	}

	/**
	 * Applied Rules
	 **/
	public RuleDTO parseRule(AppliedRuleStrategy rule) {

		String ruleTypeKey = rule.getRuleTypeKey();
		boolean enabled = rule.isEnabled();
		ModuleDTO moduleFrom = parseModule(rule.getModuleFrom());
		ModuleDTO moduleTo = parseModule(rule.getModuleTo());
		String[] violationTypeKeys = rule.getDependencyTypes();
		String regex = rule.getRegex();

		ArrayList<RuleDTO> exceptionRuleList = new ArrayList<RuleDTO>();
		for (AppliedRuleStrategy exceptionRule : rule.getExceptions()) {
			RuleDTO exceptionRuleDTO = parseRule(exceptionRule);
			exceptionRuleList.add(exceptionRuleDTO);
		}

		RuleDTO[] exceptionRuleDTOs = new RuleDTO[exceptionRuleList.size()];
		exceptionRuleList.toArray(exceptionRuleDTOs);
		RuleDTO[] exceptionRules = exceptionRuleDTOs;
		boolean isException = rule.isException();


		RuleDTO ruleDTO = new RuleDTO(ruleTypeKey, enabled, moduleTo, moduleFrom,
				violationTypeKeys, regex, exceptionRules, isException);
		return ruleDTO;
	}

	public RuleDTO[] parseRules(ArrayList<AppliedRuleStrategy> rules) {
		ArrayList<RuleDTO> ruleDTOsList = new ArrayList<RuleDTO>();
		for (AppliedRuleStrategy rule : rules) {
			RuleDTO ruleDTO = parseRule(rule);
			ruleDTOsList.add(ruleDTO);
		}
		RuleDTO[] ruleDTOs = new RuleDTO[ruleDTOsList.size()];
		ruleDTOsList.toArray(ruleDTOs);
		return ruleDTOs;
	}

}
