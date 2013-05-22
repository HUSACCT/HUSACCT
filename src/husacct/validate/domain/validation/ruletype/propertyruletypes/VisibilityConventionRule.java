package husacct.validate.domain.validation.ruletype.propertyruletypes;

import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.check.util.CheckConformanceUtilClass;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.internal_transfer_objects.Mapping;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class VisibilityConventionRule extends RuleType {

	private final static EnumSet<RuleTypes> exceptionrules = EnumSet.of(RuleTypes.VISIBILITY_CONVENTION, RuleTypes.VISIBILITY_CONVENTION_EXCEPTION);

	public VisibilityConventionRule(String key, String category, List<ViolationType> violationtypes, Severity severity) {
		super(key, category, violationtypes, exceptionrules, severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule) {
		this.violations = new ArrayList<Violation>();

		this.mappings = CheckConformanceUtilClass.filterClassesFrom(currentRule);
		this.physicalClasspathsFrom = mappings.getMappingFrom();

		int violationCounter = 0;
		for (Mapping physicalClasspathFrom : physicalClasspathsFrom) {
			AnalysedModuleDTO analysedModule = analyseService.getModuleForUniqueName(physicalClasspathFrom.getPhysicalPath());
			if (!analysedModule.type.toLowerCase().equals("package")) {
				for (String violationKey : currentRule.violationTypeKeys) {
					if (!analysedModule.visibility.toLowerCase().equals(violationKey.toLowerCase())) {
						violationCounter++;
					}
				}
				if (violationCounter == currentRule.violationTypeKeys.length && currentRule.violationTypeKeys.length != 0) {
					Violation violation = createViolation(rootRule, physicalClasspathFrom, analysedModule.visibility, configuration);
					violations.add(violation);
				}
				violationCounter = 0;
			}
		}
		return violations;
	}
}