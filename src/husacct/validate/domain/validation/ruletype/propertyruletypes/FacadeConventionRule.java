package husacct.validate.domain.validation.ruletype.propertyruletypes;

import husacct.common.dto.DependencyDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.check.util.CheckConformanceUtilClass;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.internaltransferobjects.Mapping;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class FacadeConventionRule extends RuleType {

	public FacadeConventionRule(String key, String categoryKey, List<ViolationType> violationTypes, Severity severity) {
		super(key, categoryKey, violationTypes, EnumSet.noneOf(RuleTypes.class), severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule) {
		mappings = CheckConformanceUtilClass.filterClassesFrom(currentRule);
		List<Mapping> mappingsFrom = mappings.getMappingFrom();
		List<Violation> allViolations = new ArrayList<Violation>();
		DependencyDTO[] dependencies = analyseService.getAllDependencies();

		Mapping componentMapping = null;
		List<Mapping> facadeMappings = new ArrayList<Mapping>();

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
				boolean existingMapping = false;
				for(Mapping facadeMapping: facadeMappings) {
					if(facadeMapping.getLogicalPath().equals(mappingFrom.getLogicalPath())) {
						existingMapping = true;
					}
				}
				
				if(!existingMapping) {
					facadeMappings.add(mappingFrom);
				}
			}
		}

		if(facadeMappings.size() > 0) {
			for (DependencyDTO dependency : dependencies) {
				if (dependency.to.contains(componentMapping.getPhysicalPath())) {
					if (!dependency.from.contains(componentMapping.getPhysicalPath())) {
						for(Mapping facadeMapping: facadeMappings) {
							if (!dependency.to.contains(facadeMapping.getPhysicalPath())) {
								Mapping fromMapping = new Mapping(dependency.from, new String[0]);
								Mapping toMapping = new Mapping(dependency.to, new String[0]);
	
								for(Mapping theMapping: mappingsFrom) {
									if(theMapping.getPhysicalPath().equals(fromMapping.getPhysicalPath())) {
										fromMapping = new Mapping(theMapping.getLogicalPath(), theMapping.getLogicalPathType(),
											fromMapping.getPhysicalPath(), theMapping.getViolationTypes());
									}
									else if(theMapping.getPhysicalPath().equals(toMapping.getPhysicalPath())) {
										toMapping = new Mapping(theMapping.getLogicalPath(), theMapping.getLogicalPathType(),
											toMapping.getPhysicalPath(), theMapping.getViolationTypes());
									}
								}
	
								Violation violation = createViolation(rootRule, fromMapping, toMapping, dependency, configuration);
								allViolations.add(violation);
							}
						}
					}
				}
			}
		}

		return allViolations;
	}
}