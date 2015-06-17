package husacct.validate.domain.validation.ruletype.propertyruletypes;

import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.PhysicalPathDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.check.util.CheckConformanceUtilPackage;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.Regex;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.internaltransferobjects.Mapping;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

public class NamingConvention extends RuleType {

	private final static EnumSet<RuleTypes> exceptionRuleTypes = EnumSet.of(RuleTypes.NAMING_CONVENTION);
	private HashMap<String, String> exceptionRegExes = new HashMap<String, String>();

	public NamingConvention(String key, String category, List<ViolationType> violationTypes, Severity severity) {
		super(key, category, violationTypes, exceptionRuleTypes, severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule) {
		violations.clear();
		if (currentRule.exceptionRules.length > 0) {
			for (RuleDTO exception : currentRule.exceptionRules) {
				if ((exception.regex != null) && !exception.regex.equals("")) {
					String value = Regex.makeRegexString(exception.regex);
					exceptionRegExes.put(exception.regex, value);
				}
			}
		}
		if (arrayContainsValue(currentRule.violationTypeKeys, "package")) {
			checkPackageConvention(currentRule, rootRule, configuration);
		}

		if (arrayContainsValue(currentRule.violationTypeKeys, "class")) {
			checkClassConvention(currentRule, rootRule, configuration);
		}
		return violations;
	}

	private List<Violation> checkPackageConvention(RuleDTO currentRule, RuleDTO rootRule, ConfigurationServiceImpl configuration) {
		fromMappings = CheckConformanceUtilPackage.getAllPackagepathsFromModule(currentRule.moduleFrom, currentRule.violationTypeKeys);
		String regex = Regex.makeRegexString(currentRule.regex);

		for (Mapping fromMapping : fromMappings) {
			AnalysedModuleDTO analysedModule = analyseService.getModuleForUniqueName(fromMapping.getPhysicalPath());
			if (!Regex.matchRegex(regex, analysedModule.name) && nameDoesNotMatchException(analysedModule.name)) {
				if (analysedModule.type.toLowerCase().equals("package")){
					Violation violation = createViolation(rootRule, fromMapping, configuration);
					violations.add(violation);
					
				}
			}
		}
		return violations;
	}

	private List<Violation> checkClassConvention(RuleDTO currentRule, RuleDTO rootRule, ConfigurationServiceImpl configuration) {
		fromMappings = getAllClasspathsOfModule(currentRule.moduleFrom, currentRule.violationTypeKeys);
		String regex = Regex.makeRegexString(currentRule.regex);
		
		for (Mapping physicalClasspathFrom : fromMappings) {
			AnalysedModuleDTO analysedModule = analyseService.getModuleForUniqueName(physicalClasspathFrom.getPhysicalPath());
			if (!Regex.matchRegex(regex, analysedModule.name) && analysedModule.type.toLowerCase().equals("class") && nameDoesNotMatchException(analysedModule.name)) {
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
	
	private boolean nameDoesNotMatchException(String name) {
		boolean nameDoesNotMatchException = true;
		for (String exceptionRegExe : exceptionRegExes.values()) {
			if (Regex.matchRegex(exceptionRegExe, name)) {
				nameDoesNotMatchException = false;
			}
		}
		return nameDoesNotMatchException;
	}
}