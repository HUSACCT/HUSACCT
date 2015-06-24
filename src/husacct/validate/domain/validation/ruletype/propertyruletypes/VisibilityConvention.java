package husacct.validate.domain.validation.ruletype.propertyruletypes;

import husacct.common.dto.SoftwareUnitDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.internaltransferobjects.Mapping;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;
import java.util.EnumSet;
import java.util.List;

public class VisibilityConvention extends RuleType {

	private final static EnumSet<RuleTypes> exceptionRuleTypes = EnumSet.noneOf(RuleTypes.class);

	public VisibilityConvention(String key, String category, List<ViolationType> violationTypes, Severity severity) {
		super(key, category, violationTypes, exceptionRuleTypes, severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule) {
		violations.clear();
		fromMappings = getAllClasspathsOfModule(currentRule.moduleFrom, currentRule.violationTypeKeys);

		int violationCounter = 0;
		for (Mapping physicalClasspathFrom : fromMappings) {
			SoftwareUnitDTO analysedModule = analyseService.getSoftwareUnitByUniqueName(physicalClasspathFrom.getPhysicalPath());
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