package husacct.validate.domain.validation.ruletype.legalityofdependency;

import husacct.common.dto.DependencyDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.check.util.CheckConformanceUtilClass;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.iternal_tranfer_objects.Mapping;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class IsOnlyModuleAllowedToUseRule extends RuleType{
	private final static EnumSet<RuleTypes> exceptionrules = EnumSet.of(RuleTypes.IS_ALLOWED);

	public IsOnlyModuleAllowedToUseRule(String key, String category, List<ViolationType> violationtypes, Severity severity) {
		super(key, category, violationtypes, exceptionrules, severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule) {
		this.violations = new ArrayList<Violation>();

		this.mappings = CheckConformanceUtilClass.filterClasses(currentRule);
		this.physicalClasspathsFrom = mappings.getMappingFrom();
		List<Mapping> physicalClasspathsTo = mappings.getMappingTo();

		for(Mapping classPathFrom : physicalClasspathsFrom){
			for(Mapping classPathTo : physicalClasspathsTo ){
				DependencyDTO[] dependencies = analyseService.getDependenciesTo(classPathTo.getPhysicalPath());
				DependencyDTO[] allowedDependencies = analyseService.getDependencies(classPathFrom.getPhysicalPath(),classPathTo.getPhysicalPath(), classPathTo.getViolationTypes());
				for(DependencyDTO dependency: dependencies){
					if(allowedDependencies.length != 0){
						for(DependencyDTO allowedDependency: allowedDependencies){
							if(dependency != allowedDependency){
								Violation violation = createViolation(rootRule, classPathFrom, classPathTo, dependency, configuration);
								violations.add(violation);
							}
						}
					}
					else{					
						Violation violation = createViolation(rootRule, classPathFrom, classPathTo, dependency, configuration);
						violations.add(violation);						
					}
				}
			}
		}
		return violations;
	}
}