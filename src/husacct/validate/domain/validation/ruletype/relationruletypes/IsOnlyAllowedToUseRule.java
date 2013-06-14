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
import java.util.List;

public class IsOnlyAllowedToUseRule extends RuleType {

	public IsOnlyAllowedToUseRule(String key, String category, List<ViolationType> violationTypes, Severity severity) {
		super(key, category, violationTypes, EnumSet.of(RuleTypes.IS_ALLOWED_TO_USE), severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule) {
		mappings = CheckConformanceUtilClass.filterClassesFrom(currentRule);
		physicalClasspathsFrom = mappings.getMappingFrom();

		DependencyDTO[] dependencies = analyseService.getAllDependencies();

		for (Mapping classPathFrom : physicalClasspathsFrom) {
			for (DependencyDTO dependency : dependencies) {
				if (classPathFrom.getPhysicalPath().equals(dependency.from) &&
                        !containsMapping(mappings, dependency.to) &&
                        Arrays.binarySearch(classPathFrom.getViolationTypes(), dependency.type) >= 0) {
                    Mapping classPathTo = new Mapping(dependency.to, classPathFrom.getViolationTypes());
                    Violation violation = createViolation(rootRule, classPathFrom, classPathTo, dependency, configuration);
                    violations.add(violation);
                }
			}
		}
		return violations;
	}

	private boolean containsMapping(Mappings mappings, String physicalPath) {
		for (Mapping mappingFrom : mappings.getMappingFrom()) {
			if (mappingFrom.getPhysicalPath().equals(physicalPath)) {
				return true;
			}
		}

		for (Mapping mappingTo : mappings.getMappingTo()) {
			if (mappingTo.getPhysicalPath().equals(physicalPath)) {
				return true;
			}
		}
		return false;
	}
}