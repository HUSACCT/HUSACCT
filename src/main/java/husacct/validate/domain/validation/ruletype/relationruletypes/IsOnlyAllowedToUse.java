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

public class IsOnlyAllowedToUse extends RuleType {

	private final static EnumSet<RuleTypes> exceptionRuleTypes = EnumSet.of(RuleTypes.IS_ALLOWED_TO_USE);

	public IsOnlyAllowedToUse(String key, String category, List<ViolationType> violationTypes, Severity severity) {
		super(key, category, violationTypes, exceptionRuleTypes, severity);
		//super(key, category, violationTypes, EnumSet.of(RuleTypes.IS_ALLOWED_TO_USE), severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO currentRule) {
		violations.clear();
		fromMappings = getAllClasspathsOfModule(currentRule.moduleFrom, currentRule.violationTypeKeys);
		toMappings = getAllClasspathsOfModule(currentRule.moduleTo, currentRule.violationTypeKeys);
		
		// Create HashMap with all allowed to-classes (including the from-classes)
		HashMap<String, Mapping> allowedMap = new HashMap<String, Mapping>();
		for(Mapping from : fromMappings){
			allowedMap.put(from.getPhysicalPath(), from);
		}
		for(Mapping to : toMappings){
			allowedMap.put(to.getPhysicalPath(), to);
		}

		// Create a HashMap with all allowed from-to combinations, based on the exception rules.  
		HashSet<String> allExceptionFromTos = getAllExceptionFromTos(currentRule);

		for (Mapping classPathFrom : fromMappings) {
			// Get all dependencies with dependency.classPathFrom = classPathFrom 
			DependencyDTO[] allDependenciesFrom = analyseService.getDependenciesFromClassToClass(classPathFrom.getPhysicalPath(), "");
			for (DependencyDTO dependency : allDependenciesFrom) {
				String fromToCombi = classPathFrom.getPhysicalPath() + "|" + dependency.to; 
				if(allowedMap.containsKey(dependency.to) || allExceptionFromTos.contains(fromToCombi)){
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