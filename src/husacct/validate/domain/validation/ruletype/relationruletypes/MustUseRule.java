package husacct.validate.domain.validation.ruletype.relationruletypes;

import husacct.common.dto.DependencyDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.check.util.CheckConformanceUtilClass;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.internaltransferobjects.Mapping;
import husacct.validate.domain.validation.logicalmodule.LogicalModule;
import husacct.validate.domain.validation.logicalmodule.LogicalModules;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class MustUseRule extends RuleType {

	private final static EnumSet<RuleTypes> exceptionrules = EnumSet.of(RuleTypes.IS_ALLOWED_TO_USE, RuleTypes.IS_NOT_ALLOWED_TO_USE);

	public MustUseRule(String key, String category, List<ViolationType> violationtypes, Severity severity) {
		super(key, category, violationtypes, exceptionrules, severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule) {
		violations = new ArrayList<>();

		mappings = CheckConformanceUtilClass.filterClassesFrom(currentRule);
		physicalClasspathsFrom = mappings.getMappingFrom();
		List<Mapping> physicalClasspathsTo = mappings.getMappingTo();

		DependencyDTO[] dependencies = analyseService.getAllDependencies();

		int dependencyCounter = 0;
		for (Mapping classPathFrom : physicalClasspathsFrom) {
			for (Mapping classPathTo : physicalClasspathsTo) {
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
		}
		if (dependencyCounter == 0 && !physicalClasspathsTo.isEmpty()) {
			LogicalModule logicalModuleFrom = new LogicalModule(currentRule.moduleFrom.logicalPath, currentRule.moduleTo.logicalPath);
			LogicalModule logicalModuleTo = new LogicalModule(currentRule.moduleFrom.logicalPath, currentRule.moduleTo.logicalPath);
			LogicalModules logicalModules = new LogicalModules(logicalModuleFrom, logicalModuleTo);

			Violation violation = createViolation(rootRule, logicalModules, configuration);
			violations.add(violation);
		}
		return violations;
	}
}