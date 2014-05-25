package husacct.validate.domain.validation.ruletype.relationruletypes;

import husacct.ServiceProvider;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.internaltransferobjects.Mapping;
import husacct.validate.domain.validation.logicalmodule.LogicalModule;
import husacct.validate.domain.validation.logicalmodule.LogicalModules;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

public class IsOnlyAllowedToUse extends RuleType {

	private final static EnumSet<RuleTypes> exceptionRuleTypes = EnumSet.noneOf(RuleTypes.class);

	public IsOnlyAllowedToUse(String key, String category, List<ViolationType> violationTypes, Severity severity) {
		super(key, category, violationTypes, exceptionRuleTypes, severity);
		//super(key, category, violationTypes, EnumSet.of(RuleTypes.IS_ALLOWED_TO_USE), severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule) {
		violations.clear();
		fromMappings = getAllClasspathsOfModule(currentRule.moduleFrom, currentRule.violationTypeKeys);
		toMappings = getAllClasspathsOfModule(currentRule.moduleTo, currentRule.violationTypeKeys);
		
		// Create HashMap with all allowed to-classes (including the from-classes)
		HashMap<String, Mapping> fromMap = new HashMap<String, Mapping>();
		for(Mapping from : fromMappings){
			fromMap.put(from.getPhysicalPath(), from);
		}
		for(Mapping to : toMappings){
			fromMap.put(to.getPhysicalPath(), to);
		}

		for (Mapping classPathFrom : fromMappings) {
			// Get all dependencies with dependency.classPathFrom = classPathFrom 
			DependencyDTO[] allDependenciesFrom = analyseService.getDependenciesFromTo(classPathFrom.getPhysicalPath(), "");
			for (DependencyDTO dependency : allDependenciesFrom) {
				if(fromMap.containsKey(dependency.to)){
					// Do nothing
				}
				else{
                    Mapping classPathTo = new Mapping(dependency.to, classPathFrom.getViolationTypes());
                    Violation violation = createViolation(rootRule, classPathFrom, classPathTo, dependency, configuration);

					// Get logicalModuleTo based on dependency.to and add it to the violation
                    ModuleDTO moduleTo = ServiceProvider.getInstance().getDefineService().getModule_BasedOnSoftwareUnitName(dependency.to);
					if(moduleTo != null){
						// Add moduleTo to violation.logicalModules, so that graphics can include these violations in architecture diagrams
						LogicalModules logicalModulesOld = violation.getLogicalModules();
						LogicalModule logicalModuleFrom = logicalModulesOld.getLogicalModuleFrom();
						LogicalModule logicalModuleTo = new LogicalModule(moduleTo.logicalPath, moduleTo.type);
						LogicalModules logicalModules = new LogicalModules(logicalModuleFrom, logicalModuleTo);
						violation.setLogicalModules(logicalModules);
					}

                    violations.add(violation);
				}
			}
		}
		return violations;
	}
}