package husacct.validate.domain.validation.ruletype.propertyruletypes;

import husacct.common.dto.DependencyDTO;
import husacct.common.dto.RuleDTO;
import husacct.common.dto.SoftwareUnitDTO;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.internaltransferobjects.Mapping;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;

import java.util.EnumSet;
import java.util.List;

public class InheritanceConvention extends RuleType {
	private final static EnumSet<RuleTypes> exceptionRuleTypes = EnumSet.noneOf(RuleTypes.class);

	public InheritanceConvention(String key, String category, List<ViolationType> violationTypes, Severity severity) {
		super(key, category, violationTypes, exceptionRuleTypes, severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO currentRule) {
		violations.clear();
		fromMappings = getAllClasspathsOfModule(currentRule.moduleFrom, currentRule.violationTypeKeys);
		toMappings = getAllClasspathsOfModule(currentRule.moduleTo, currentRule.violationTypeKeys);

		for (Mapping classPathFrom : fromMappings) {
			for (Mapping classPathTo : toMappings) {
				SoftwareUnitDTO from = analyseService.getSoftwareUnitByUniqueName(classPathFrom.getPhysicalPath());
				SoftwareUnitDTO to = analyseService.getSoftwareUnitByUniqueName(classPathTo.getPhysicalPath());
				if((!from.type.equals("package")) && (!to.type.equals("package"))){
					boolean classInherits = false;
					DependencyDTO[] dependencies = analyseService.getDependenciesFromClassToClass(classPathFrom.getPhysicalPath(), classPathTo.getPhysicalPath());
					if(dependencies != null && dependencies.length > 0){
						for(DependencyDTO dependency : dependencies){
							if((dependency != null) && (dependency.type.equals("Inheritance"))){
								classInherits = true;
							}
						}	
					}
					if(classInherits == false){
						Violation violation = createViolation(currentRule, classPathFrom, classPathTo, configuration);
	                    violations.add(violation);
					}
				}
			}
		}
		return violations;
	}
}
