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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class SubClassConventionRule extends RuleType {

	private final static EnumSet<RuleTypes> subClassExceptionRules = EnumSet.of(RuleTypes.IS_ALLOWED);

	public SubClassConventionRule(String key, String category, List<ViolationType> violationtypes, Severity severity) {
		super(key, category, violationtypes, subClassExceptionRules, severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule) {
		violations = new ArrayList<>();
		mappings = CheckConformanceUtilClass.filterClassesFrom(currentRule);
		classpathsFrom = mappings.getMappingFrom();
		List<Mapping> classpathsTo = mappings.getMappingTo();

		DependencyDTO[] dependencies = analyseService.getAllDependencies();

		for (Mapping classPathFrom : classpathsFrom) {
			int dependencyCounter = 0;
			for (Mapping classPathTo : classpathsTo) {
				for (DependencyDTO dependency : dependencies) {
					if (dependency.from.equals(classPathFrom.getPhysicalPath())) {
						if (dependency.to.equals(classPathTo.getPhysicalPath())) {
							if (Arrays.binarySearch(classPathFrom.getViolationTypes(), dependency.type) >= 0) {
								dependencyCounter++;
							}
						}
					}
				}
			}
			if (dependencyCounter == 0 && !classpathsTo.isEmpty()) {
				Violation violation = createViolation(rootRule, classPathFrom, configuration);
				violations.add(violation);
			}
		}
		return violations;
	}
}
