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

		if(arrayContainsValue(currentRule.violationTypeKeys, "package")){
			checkPackageConvention(currentRule, rootRule, configuration);
		}

		if(arrayContainsValue(currentRule.violationTypeKeys, "class")){
			checkClassConvention(currentRule, rootRule, configuration);
		}

		return violations;
	}

	private List<Violation> checkPackageConvention(RuleDTO currentRule, RuleDTO rootRule, ConfigurationServiceImpl configuration){
		this.violations = new ArrayList<Violation>();

		this.mappings = CheckConformanceUtilPackage.filterPackages(currentRule);
		this.physicalClasspathsFrom = mappings.getMappingFrom();
		for(Mapping physicalClasspathFrom : physicalClasspathsFrom){

			AnalysedModuleDTO analysedModule = analyseService.getModuleForUniqueName(physicalClasspathFrom.getPhysicalPath());	
			if(!Regex.matchRegex(Regex.makeRegexString(currentRule.regex),analysedModule.name) && analysedModule.type.toLowerCase().equals("package")){
				Violation violation = createViolation(rootRule, physicalClasspathFrom, configuration);
				violations.add(violation);
			}
		}
		return violations;
	}

	private List<Violation> checkClassConvention(RuleDTO currentRule, RuleDTO rootRule, ConfigurationServiceImpl configuration){
		this.violations = new ArrayList<Violation>();

		this.mappings = CheckConformanceUtilClass.filterClasses(currentRule);
		this.physicalClasspathsFrom = mappings.getMappingFrom();

		for(Mapping physicalClasspathFrom : physicalClasspathsFrom ){

			AnalysedModuleDTO analysedModule = analyseService.getModuleForUniqueName(physicalClasspathFrom.getPhysicalPath());	
			if(!Regex.matchRegex(Regex.makeRegexString(currentRule.regex),analysedModule.name) && analysedModule.type.toLowerCase().equals("class")){
				Violation violation = createViolation(rootRule, physicalClasspathFrom, null , null, configuration);
				violations.add(violation);
			}
		}
		return violations;
	}

	private boolean arrayContainsValue(String[] array, String value){
		for(String arrayValue : array){
			if(arrayValue.toLowerCase().equals(value.toLowerCase())){
				return true;
			}
		}
		return false;
	}
}