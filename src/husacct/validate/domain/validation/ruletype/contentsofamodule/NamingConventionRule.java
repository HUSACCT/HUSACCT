package husacct.validate.domain.validation.ruletype.contentsofamodule;

import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.check.util.CheckConformanceUtilClass;
import husacct.validate.domain.check.util.CheckConformanceUtilPackage;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.Regex;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.internal_transfer_objects.Mapping;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class NamingConventionRule extends RuleType {
	private final static EnumSet<RuleTypes> exceptionrules = EnumSet.of(RuleTypes.NAMING_CONVENTION_EXCEPTION, RuleTypes.NAMING_CONVENTION);

	public NamingConventionRule(String key, String category, List<ViolationType> violationtypes, Severity severity) {
		super(key, category, violationtypes, exceptionrules, severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule) {
		this.violations = new ArrayList<Violation>();

		if (arrayContainsValue(currentRule.violationTypeKeys, "package")) {
			checkPackageConvention(currentRule, rootRule, configuration);
		}

		if (arrayContainsValue(currentRule.violationTypeKeys, "class")) {
			checkClassConvention(currentRule, rootRule, configuration);
		}

		return violations;
	}

	private List<Violation> checkPackageConvention(RuleDTO currentRule, RuleDTO rootRule, ConfigurationServiceImpl configuration) {
		this.violations = new ArrayList<Violation>();

		this.mappings = CheckConformanceUtilPackage.filterPackages(currentRule);
		this.physicalClasspathsFrom = mappings.getMappingFrom();

		final String regex = Regex.makeRegexString(currentRule.regex);

		for (Mapping physicalClasspathFrom : physicalClasspathsFrom) {
			AnalysedModuleDTO analysedModule = analyseService.getModuleForUniqueName(physicalClasspathFrom.getPhysicalPath());
			if (!Regex.matchRegex(regex, analysedModule.name) && analysedModule.type.toLowerCase().equals("package")) {
				Violation violation = createViolation(rootRule, physicalClasspathFrom, configuration);
				violations.add(violation);
			}
		}
		return violations;
	}

	private List<Violation> checkClassConvention(RuleDTO currentRule, RuleDTO rootRule, ConfigurationServiceImpl configuration) {
		this.violations = new ArrayList<Violation>();

		this.mappings = CheckConformanceUtilClass.filterClassesFrom(currentRule);
		this.physicalClasspathsFrom = mappings.getMappingFrom();

		final String regex = Regex.makeRegexString(currentRule.regex);

		for (Mapping physicalClasspathFrom : physicalClasspathsFrom) {
			AnalysedModuleDTO analysedModule = analyseService.getModuleForUniqueName(physicalClasspathFrom.getPhysicalPath());
			if (!Regex.matchRegex(regex, analysedModule.name) && !analysedModule.type.toLowerCase().equals("package")) {
				Violation violation = createViolation(rootRule, physicalClasspathFrom, configuration);
				violations.add(violation);
			}
		}
		return violations;
	}

	private boolean arrayContainsValue(String[] array, String value) {
		for (String arrayValue : array) {
			if (arrayValue.toLowerCase().equals(value.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
}