package husacct.validate.domain.validation.ruletype.legalityofdependency;

import husacct.common.dto.DependencyDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.ConfigurationServiceImpl;
import husacct.validate.domain.check.CheckConformanceUtil;
import husacct.validate.domain.factory.violationtype.java.ViolationTypeFactory;
import husacct.validate.domain.validation.Message;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.iternal_tranfer_objects.Mapping;
import husacct.validate.domain.validation.iternal_tranfer_objects.Mappings;
import husacct.validate.domain.validation.logicalmodule.LogicalModule;
import husacct.validate.domain.validation.logicalmodule.LogicalModules;
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
		List<Violation> violations = new ArrayList<Violation>();
		this.violationtypefactory = new ViolationTypeFactory().getViolationTypeFactory(configuration);

		Mappings mappings = CheckConformanceUtil.filter(currentRule);
		List<Mapping> physicalClasspathsFrom = mappings.getMappingFrom();
		List<Mapping> physicalClasspathsTo = mappings.getMappingTo();

		for(Mapping classPathFrom : physicalClasspathsFrom){
			for(Mapping classPathTo : physicalClasspathsTo ){
				DependencyDTO[] dependencies = analyseService.getDependenciesTo(classPathTo.getPhysicalPath());
				DependencyDTO[] allowedDependencies = analyseService.getDependencies(classPathFrom.getPhysicalPath(),classPathTo.getPhysicalPath());
				for(DependencyDTO dependency: dependencies){
					if(allowedDependencies.length != 0){
						for(DependencyDTO allowedDependency: allowedDependencies){
							if(dependency != allowedDependency){
								Message message = new Message(rootRule);
	
								LogicalModule logicalModuleFrom = new LogicalModule(classPathFrom);
								LogicalModule logicalModuleTo = new LogicalModule(classPathTo);
								LogicalModules logicalModules = new LogicalModules(logicalModuleFrom, logicalModuleTo);
	
								final Severity violationTypeSeverity = getViolationTypeSeverity(dependency.type);
								Severity severity = CheckConformanceUtil.getSeverity(configuration, super.severity, violationTypeSeverity);						
								Violation violation = createViolation(dependency, 1, this.key, logicalModules, false, message, severity);
								violations.add(violation);
							}
						}
					}
					else{
						Message message = new Message(rootRule);
						
						LogicalModule logicalModuleFrom = new LogicalModule(classPathFrom);
						LogicalModule logicalModuleTo = new LogicalModule(classPathTo);
						LogicalModules logicalModules = new LogicalModules(logicalModuleFrom, logicalModuleTo);

						final Severity violationTypeSeverity = getViolationTypeSeverity(dependency.type);
						Severity severity = CheckConformanceUtil.getSeverity(configuration, super.severity, violationTypeSeverity);						
						Violation violation = createViolation(dependency, 1, this.key, logicalModules, false, message, severity);
						violations.add(violation);
						
					}

				}
			}
		}
		return violations;
	}
}
