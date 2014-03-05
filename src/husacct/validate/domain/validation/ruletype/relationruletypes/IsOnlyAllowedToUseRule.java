package husacct.validate.domain.validation.ruletype.relationruletypes;

import husacct.common.dto.DependencyDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.check.util.CheckConformanceUtilClass;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.internaltransferobjects.Mapping;
import husacct.validate.domain.validation.internaltransferobjects.Mappings;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

public class IsOnlyAllowedToUseRule extends RuleType {

	public IsOnlyAllowedToUseRule(String key, String category, List<ViolationType> violationTypes, Severity severity) {
		super(key, category, violationTypes, EnumSet.of(RuleTypes.IS_ALLOWED_TO_USE), severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule) {
		mappings = CheckConformanceUtilClass.filterClassesFrom(currentRule);
		physicalClasspathsFrom = mappings.getMappingFrom();
		List<Mapping> physicalClasspathsTo = mappings.getMappingTo();
		
		// Create HashMap with all allowed to-classes (including the from-classes)
		HashMap<String, Mapping> fromMap = new HashMap<String, Mapping>();
		for(Mapping from : physicalClasspathsFrom){
			fromMap.put(from.getPhysicalPath(), from);
		}
		for(Mapping to : physicalClasspathsTo){
			fromMap.put(to.getPhysicalPath(), to);
		}

		for (Mapping classPathFrom : physicalClasspathsFrom) {
			// Get all dependencies with dependency.classPathFrom = classPathFrom 
			DependencyDTO[] allDependenciesFrom = analyseService.getDependenciesFromTo(classPathFrom.getPhysicalPath(), "");
			for (DependencyDTO dependency : allDependenciesFrom) {
				if(fromMap.containsKey(dependency.to)){
					// Do nothing
				}
				else{
                    Mapping classPathTo = new Mapping(dependency.to, classPathFrom.getViolationTypes());
                    Violation violation = createViolation(rootRule, classPathFrom, classPathTo, dependency, configuration);
                    violations.add(violation);
				}
			}
		}
		return violations;
	}
}