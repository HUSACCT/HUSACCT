package husacct.validate.domain.validation.ruletype.propertyruletypes;

import husacct.common.dto.DependencyDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.check.util.CheckConformanceUtilClass;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.internaltransferobjects.Mapping;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class SuperClassInheritanceConventionRule extends RuleType {

	private final static EnumSet<RuleTypes> superClassInheritanceExceptionRules = EnumSet.of(RuleTypes.IS_ALLOWED_TO_USE);

	public SuperClassInheritanceConventionRule(String key, String category, List<ViolationType> violationTypes, Severity severity) {
		super(key, category, violationTypes, superClassInheritanceExceptionRules, severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule) {
		mappings = CheckConformanceUtilClass.filterClassesFrom(currentRule);
		physicalClasspathsFrom = mappings.getMappingFrom();
		List<Mapping> physicalClasspathsTo = mappings.getMappingTo();

		DependencyDTO[] dependencies = analyseService.getAllDependencies();

		for (Mapping classPathFrom : physicalClasspathsFrom) {
			int dependencyCounter = 0;
			for (Mapping classPathTo : physicalClasspathsTo) {
				for (DependencyDTO dependency : dependencies) {
					if (dependency.from.equals(classPathFrom.getPhysicalPath()) &&
                            dependency.to.equals(classPathTo.getPhysicalPath()) &&
                            Arrays.binarySearch(classPathFrom.getViolationTypes(), dependency.type) >= 0) {
                        dependencyCounter++;
                    }
				}
			}
			if (dependencyCounter == 0 && !physicalClasspathsTo.isEmpty()) {
				Violation violation = createViolation(rootRule, classPathFrom, configuration);
				violations.add(violation);
			}
		}
		return violations;
	}
}
