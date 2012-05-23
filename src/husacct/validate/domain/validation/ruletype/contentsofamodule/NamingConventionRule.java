package husacct.validate.domain.validation.ruletype.contentsofamodule;

import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.check.CheckConformanceUtilFilter;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.factory.violationtype.ViolationTypeFactory;
import husacct.validate.domain.validation.Regex;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.iternal_tranfer_objects.Mapping;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class NamingConventionRule extends RuleType {
	private final static EnumSet<RuleTypes> exceptionrules = EnumSet.of(RuleTypes.NAMING_CONVENTION_EXCEPTION);

	public NamingConventionRule(String key, String category, List<ViolationType> violationtypes, Severity severity) {
		super(key, category, violationtypes, exceptionrules, severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule) {
		this.violations = new ArrayList<Violation>();
		this.violationtypefactory = new ViolationTypeFactory().getViolationTypeFactory(configuration);

		this.mappings = CheckConformanceUtilFilter.filter(currentRule);
		this.physicalClasspathsFrom = mappings.getMappingFrom();

		AnalysedModuleDTO analysedModule;
		for(Mapping physicalClasspathFrom : physicalClasspathsFrom ){

			analysedModule = analyseService.getModuleForUniqueName(physicalClasspathFrom.getPhysicalPath());
			if(!Regex.matchRegex(Regex.makeRegexString(currentRule.regex),analysedModule.name)){
				Violation violation = createViolation(rootRule,physicalClasspathFrom,null,null,configuration);
				violations.add(violation);
			}
		}
		return violations;
	}
}