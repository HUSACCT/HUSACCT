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
		//List<Mapping> physicalClasspathsTo = mappings.getMappingTo();
		DependencyDTO[] dependencies = analyseService.getAllDependencies();

		for (Mapping classPathFrom : physicalClasspathsFrom) {
			List<String> facadeDependenciesTo = new ArrayList<String>();
			for (DependencyDTO dependency : dependencies) {
				if (dependency.from.equals(classPathFrom.getPhysicalPath())) {
					System.out.println("[VERZAMELEN] " + dependency.to);
					
					if(!dependency.to.startsWith("java")) {
					//	if(!facadeDependenciesTo.contains(dependency.to)) {
							facadeDependenciesTo.add(dependency.to);
					//	}
					}
				}
			}
			
			for (DependencyDTO dependency : dependencies) {
				if(!dependency.from.equals(classPathFrom.getPhysicalPath())) {
					for(String facadeDependencyTo: facadeDependenciesTo) {
						if(facadeDependencyTo.equals(dependency.to)) {
							Violation violation = createViolation(rootRule, new Mapping(dependency.from, new String[0]), new Mapping(dependency.to, new String[0]), dependency, configuration);
									//createViolation(rootRule, classPathFrom, configuration);
							violations.add(violation);
						}
					}
				}
			}
			
		}	
		
		//TEST VIOLATIONS
		for(Violation theViolation: violations) {
			System.out.println("[VIOLATION] FROM " + theViolation.getClassPathFrom() + " - TO " + theViolation.getClassPathTo() + " - " 
					+ theViolation.getLinenumber() + " - " + theViolation.getViolationtypeKey() + " - " + theViolation.getMessage().getRuleKey());
		}
		
		return violations;
	}
}
