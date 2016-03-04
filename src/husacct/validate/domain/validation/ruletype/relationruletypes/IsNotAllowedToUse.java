package husacct.validate.domain.validation.ruletype.relationruletypes;

import husacct.analyse.serviceinterface.dto.DependencyDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.internaltransferobjects.Mapping;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;

public class IsNotAllowedToUse extends RuleType {
	private final static EnumSet<RuleTypes> exceptionRuleTypes = EnumSet.of(RuleTypes.IS_ALLOWED_TO_USE);

	public IsNotAllowedToUse(String key, String category, List<ViolationType> violationTypes, Severity severity) {
		super(key, category, violationTypes, exceptionRuleTypes, severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule) {
		violations.clear();
		fromMappings = getAllClasspathsOfModule(currentRule.moduleFrom, currentRule.violationTypeKeys);
		toMappings = getAllClasspathsOfModule(currentRule.moduleTo, currentRule.violationTypeKeys);
		// Create a HashMap with all allowed from-to combinations, based on the exception rules.  
		HashSet<String> allExceptionFromTos = getAllExceptionFromTos(currentRule);

		for (Mapping classPathFrom : fromMappings) {
			for (Mapping classPathTo : toMappings) {
				String fromToCombi = classPathFrom.getPhysicalPath() + "|" + classPathTo.getPhysicalPath(); 
				if (allExceptionFromTos.contains(fromToCombi)){
					// Do not add violations, since this usage is allowed. 
				} else{
					DependencyDTO[] violatingDependencies = analyseService.getDependenciesFromClassToClass(classPathFrom.getPhysicalPath(), classPathTo.getPhysicalPath());
					if(violatingDependencies != null && violatingDependencies.length > 0){
						for(DependencyDTO dependency : violatingDependencies){
							if(dependency != null){
								Violation violation = createViolation(rootRule, classPathFrom, classPathTo, dependency, configuration);
		                        violations.add(violation);
							}
						}
					}
				}
			}
		}
		return violations;
	}
}