package husacct.validate.domain.validation.ruletype.relationruletypes;

import husacct.common.dto.DependencyDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.internaltransferobjects.Mapping;
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
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO currentRule) {
		violations.clear();
		fromMappings = getAllClasspathsOfModule(currentRule.moduleFrom, currentRule.violationTypeKeys);
		toMappings = getAllClasspathsOfModule(currentRule.moduleTo, currentRule.violationTypeKeys);

		// Create HashMap with all allowed to-classes (including the from-classes)
		HashMap<String, Mapping> fromMap = new HashMap<>();
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
                    Violation violation = createViolation(currentRule, dependency, configuration);
                    violations.add(violation);
				}
			}
		}
		return violations;
	}
}