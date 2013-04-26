package husacct.validate.domain.validation.ruletype.contentsofamodule;

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
import java.util.EnumSet;
import java.util.List;

public class FacadeConventionRule extends RuleType {
	private final static EnumSet<RuleTypes> exceptionrules = EnumSet.noneOf(RuleTypes.class);

	public FacadeConventionRule(String key, String categoryKey, List<ViolationType> violationtypes, Severity severity) {
		super(key, categoryKey, violationtypes, exceptionrules, severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule) {
		this.violations = new ArrayList<Violation>();

		this.mappings = CheckConformanceUtilClass.filterClassesFrom(currentRule);
		List<Mapping> physicalClasspathsFrom = mappings.getMappingFrom();
		List<Violation> allViolations = new ArrayList<Violation>();
		DependencyDTO[] dependencies = analyseService.getAllDependencies();

		for (Mapping classPathFrom : physicalClasspathsFrom) {
			List<String> facadeDependenciesTo = new ArrayList<String>();
			for (DependencyDTO dependency : dependencies) {
				if (dependency.from.equals(classPathFrom.getPhysicalPath())) {
					boolean fromDependencyKnown = false;
					for (DependencyDTO innerDependency : dependencies) {
						if(dependency.to.equals(innerDependency.from)) {
							fromDependencyKnown = true;
						}
					}
					
					if(fromDependencyKnown) {
						facadeDependenciesTo.add(dependency.to);
					}
				}
			}
			
			for (DependencyDTO dependency : dependencies) {
				if(!dependency.from.equals(classPathFrom.getPhysicalPath())) {
					for(String facadeDependencyTo: facadeDependenciesTo) {
						if(facadeDependencyTo.equals(dependency.to)) { 					
							Violation violation = createViolation(rootRule, new Mapping(dependency.from, new String[0]), 
									new Mapping(dependency.to, new String[0]), dependency, configuration);
							allViolations.add(violation);
						}
					}
				}
			}
		}	
		
		for(Violation violation: allViolations) {
			if(violations.size() == 0) {
				violations.add(violation);
			}
			else { 
				boolean newViolation = true;
				for(Violation theViolation: violations) {
					if(theViolation.getClassPathTo().equals(violation.getClassPathTo()) && theViolation.getLinenumber() == violation.getLinenumber() 
							&& violation.getViolationtypeKey().equals(theViolation.getViolationtypeKey())) {
						newViolation = false;
					}
				}
				
				if(newViolation) {
					violations.add(violation);
				}
			}
		}
		
		return violations;
	}
}