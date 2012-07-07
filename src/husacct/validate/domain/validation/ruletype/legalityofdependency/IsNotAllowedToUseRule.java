package husacct.validate.domain.validation.ruletype.legalityofdependency;

import husacct.common.dto.DependencyDTO;
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
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class IsNotAllowedToUseRule extends RuleType {
	private final static EnumSet<RuleTypes> exceptionrules = EnumSet.of(RuleTypes.IS_ALLOWED);

	public IsNotAllowedToUseRule(String key, String category, List<ViolationType> violationtypes, Severity severity) {
		super(key, category, violationtypes, exceptionrules, severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule) {
		this.violations = new ArrayList<Violation>();

		this.mappings = CheckConformanceUtilClass.filterClassesFrom(currentRule);
		this.physicalClasspathsFrom = mappings.getMappingFrom();
		List<Mapping> physicalClasspathsTo = mappings.getMappingTo();

		DependencyDTO[] dependencies = analyseService.getAllDependencies();

		for(Mapping classPathFrom : physicalClasspathsFrom){
			for(Mapping classPathTo : physicalClasspathsTo){
				for(DependencyDTO dependency : dependencies){
					if(dependency.from.equals(classPathFrom.getPhysicalPath())){
						if(dependency.to.equals(classPathTo.getPhysicalPath())){
							if(Arrays.binarySearch(classPathFrom.getViolationTypes(), dependency.type) >= 0){
								Violation violation = createViolation(rootRule, classPathFrom, classPathTo, dependency, configuration);
								violations.add(violation);						
							}
						}
					}
				}
			}
		}
		return violations;
	}
}