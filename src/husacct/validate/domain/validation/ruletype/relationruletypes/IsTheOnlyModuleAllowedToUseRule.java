package husacct.validate.domain.validation.ruletype.relationruletypes;

import husacct.common.dto.DependencyDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.check.util.CheckConformanceUtilClass;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.internaltransferobjects.Mapping;
import husacct.validate.domain.validation.internaltransferobjects.Mappings;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

public class IsTheOnlyModuleAllowedToUseRule extends RuleType {

	private final static EnumSet<RuleTypes> exceptionrules = EnumSet.noneOf(RuleTypes.class);

	public IsTheOnlyModuleAllowedToUseRule(String key, String category, List<ViolationType> violationtypes, Severity severity) {
		super(key, category, violationtypes, exceptionrules, severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule) {
		mappings = CheckConformanceUtilClass.filterClassesFrom(currentRule);
		physicalClasspathsFrom = mappings.getMappingFrom();
		List<Mapping> physicalClasspathsTo = mappings.getMappingTo();

		// Create HashMap with all allowed to-classes (including the from-classes)
		HashMap<String, Mapping> fromMap = new HashMap<String, Mapping>();
		for(Mapping from : physicalClasspathsFrom){
			fromMap.put(from.getPhysicalPath(), from);
		}
		for(Mapping to : physicalClasspathsTo){
			fromMap.put(to.getPhysicalPath(), to);
		}

		for (Mapping classPathTo : physicalClasspathsTo) {
			// Get all dependencies with matching dependency.classPathTo 
			DependencyDTO[] dependenciesTo = analyseService.getDependenciesFromTo("", classPathTo.getPhysicalPath());
			for (DependencyDTO dependency : dependenciesTo) {
				if(fromMap.containsKey(dependency.from)){
					// Do nothing
				}
				else{
                    Mapping classPathFrom = new Mapping(dependency.from, classPathTo.getViolationTypes());
                    Violation violation = createViolation(rootRule, classPathFrom, classPathTo, dependency, configuration);
                    violations.add(violation);
				}
			}
		}
		return violations;
	}
}