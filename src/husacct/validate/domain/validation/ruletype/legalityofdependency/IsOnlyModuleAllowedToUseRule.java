package husacct.validate.domain.validation.ruletype.legalityofdependency;

import husacct.common.dto.DependencyDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.check.util.CheckConformanceUtilClass;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.internal_transfer_objects.Mapping;
import husacct.validate.domain.validation.internal_transfer_objects.Mappings;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class IsOnlyModuleAllowedToUseRule extends RuleType{
	private final static EnumSet<RuleTypes> exceptionrules = EnumSet.noneOf(RuleTypes.class);

	public IsOnlyModuleAllowedToUseRule(String key, String category, List<ViolationType> violationtypes, Severity severity) {
		super(key, category, violationtypes, exceptionrules, severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule) {
		this.violations = new ArrayList<Violation>();

		this.mappings = CheckConformanceUtilClass.filterClasses(currentRule);
		this.physicalClasspathsFrom = mappings.getMappingFrom();
		List<Mapping> physicalClasspathsTo = mappings.getMappingTo();

		DependencyDTO[] dependencies = analyseService.getAllDependencies();	

		for(Mapping classPathTo : physicalClasspathsTo){
			for(DependencyDTO dependency : dependencies){
				if(dependency.to.equals(classPathTo.getPhysicalPath())){
					if(!containsMapping(mappings, dependency.from)){
						if(Arrays.binarySearch(classPathTo.getViolationTypes(), dependency.type) >= 0){
							Mapping classPathFrom = new Mapping(dependency.from, classPathTo.getViolationTypes());
							Violation violation = createViolation(rootRule, classPathFrom, classPathTo, dependency, configuration);
							violations.add(violation);						
						}
					}
				}
			}
		}
		return violations;
	}

	private boolean containsMapping(Mappings mappings, String physicalPath){
		for(Mapping mappingFrom : mappings.getMappingFrom()){
			if(mappingFrom.getPhysicalPath().equals(physicalPath)){
				return true;
			}
		}

		for(Mapping mappingTo : mappings.getMappingTo()){
			if(mappingTo.getPhysicalPath().equals(physicalPath)){
				return true;
			}
		}
		return false;
	}
}