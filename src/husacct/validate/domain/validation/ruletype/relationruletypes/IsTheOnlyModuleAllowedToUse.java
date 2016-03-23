package husacct.validate.domain.validation.ruletype.relationruletypes;

import husacct.ServiceProvider;
import husacct.analyse.serviceinterface.dto.DependencyDTO;
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
import java.util.HashSet;
import java.util.List;

public class IsTheOnlyModuleAllowedToUse extends RuleType {

	private final static EnumSet<RuleTypes> exceptionRuleTypes = EnumSet.of(RuleTypes.IS_ALLOWED_TO_USE);

	public IsTheOnlyModuleAllowedToUse(String key, String category, List<ViolationType> violationtypes, Severity severity) {
		super(key, category, violationtypes, exceptionRuleTypes, severity);
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

		// Create a HashMap with all allowed from-to combinations, based on the exception rules.  
		HashSet<String> allExceptionFromTos = getAllExceptionFromTos(currentRule);

		for (Mapping classPathTo : toMappings) {
			// Get all dependencies with matching dependency.classPathTo 
			DependencyDTO[] dependenciesTo = analyseService.getDependenciesFromClassToClass("", classPathTo.getPhysicalPath());
			for (DependencyDTO dependency : dependenciesTo) {
				String fromToCombi = dependency.from + "|" + classPathTo.getPhysicalPath(); 
				if(fromMap.containsKey(dependency.from) || allExceptionFromTos.contains(fromToCombi)){
					// Do nothing
				}
				else{
                    Mapping classPathFrom = new Mapping(dependency.from, classPathTo.getViolationTypes());
                    Violation violation = createViolation(rootRule, classPathFrom, classPathTo, dependency, configuration);
                    
					// Get logicalModuleFrom based on dependency.from and add it to the violation
                    ModuleDTO moduleFrom = ServiceProvider.getInstance().getDefineService().getModule_BasedOnSoftwareUnitName(dependency.from);
					if(moduleFrom != null){
						// Add moduleFrom to violation.logicalModules, so that graphics can include these violations in architecture diagrams
						LogicalModules logicalModulesOld = violation.getLogicalModules();
						LogicalModule logicalModuleTo = logicalModulesOld.getLogicalModuleTo();
						LogicalModule logicalModuleFrom = new LogicalModule(moduleFrom.logicalPath, moduleFrom.type);
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