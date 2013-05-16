package husacct.validate.domain.validation.ruletype.propertyruletypes;

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
		List<Mapping> mappingsFrom = mappings.getMappingFrom();
		List<Violation> allViolations = new ArrayList<Violation>();
		DependencyDTO[] dependencies = analyseService.getAllDependencies();
		List<String> facadeDependenciesTo = new ArrayList<String>();
		
		Mapping componentMapping = null;
		Mapping facadeMapping = null;
		
		for (Mapping mappingFrom : mappingsFrom) {
			if(mappingFrom.getLogicalPathType().toLowerCase().equals("component")) {
				if(componentMapping == null) {
					componentMapping = mappingFrom;
				}
				else {
					if(mappingFrom.getPhysicalPath().length() < componentMapping.getPhysicalPath().length()) {
						componentMapping = mappingFrom;
					}
				}
			}
			else if(mappingFrom.getLogicalPathType().toLowerCase().equals("facade")) {
				facadeMapping = mappingFrom;
			}
		}
		
		//TIJDELIJK
		System.out.println("================\n- COMPONENT: " + componentMapping.getPhysicalPath() + " - " + componentMapping.getLogicalPath() + " - " 
		+ componentMapping.getLogicalPathType() + "\n- FACADE: " + facadeMapping.getPhysicalPath() + " - " + facadeMapping.getLogicalPath() + " - " 
		+ facadeMapping.getLogicalPathType());
		//EINDE
		
		for (DependencyDTO dependency : dependencies) {
			if (dependency.to.contains(componentMapping.getPhysicalPath())) {
				if (!dependency.from.contains(componentMapping.getPhysicalPath())) {
					if (!dependency.to.contains(facadeMapping.getPhysicalPath())) {
						System.out.println("<1> " + dependency.from + " <2> " + dependency.to);
						
						Violation violation = createViolation(rootRule, facadeMapping, new Mapping(dependency.to, new String[0]), dependency, configuration); 
						allViolations.add(violation);
					}
				}
			}
		}
		
		for (DependencyDTO dependency : dependencies) {
			if(dependency.from.equals(facadeMapping.getPhysicalPath())) {
				facadeDependenciesTo.add(dependency.to);
			}
		}
		

		for (DependencyDTO dependency : dependencies) {
			if(!dependency.from.equals(facadeMapping.getPhysicalPath())) {
				for(String facadeDependencyTo: facadeDependenciesTo) {
					if(facadeDependencyTo.equals(dependency.to)) {
						Violation violation = createViolation(rootRule, facadeMapping, new Mapping(dependency.to, new String[0]), dependency, configuration); 
						allViolations.add(violation);
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