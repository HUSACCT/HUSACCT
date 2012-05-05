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

public class MustUseRule extends RuleType{
	private final static EnumSet<RuleTypes> exceptionrules = EnumSet.of(RuleTypes.IS_ALLOWED, RuleTypes.IS_NOT_ALLOWED);

	public MustUseRule(String key, String category, List<ViolationType> violationtypes, Severity severity) {
		super(key, category, violationtypes, exceptionrules,severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule) {	
		List<Violation> violations = new ArrayList<Violation>();
		this.violationtypefactory = new ViolationTypeFactory().getViolationTypeFactory(configuration);

		Mappings mappings = CheckConformanceUtil.filter(currentRule);
		List<Mapping> physicalClasspathsFrom = mappings.getMappingFrom();
		List<Mapping> physicalClasspathsTo = mappings.getMappingTo();

		int totalCounter = 0, noDependencyCounter = 0;
		for(Mapping classPathFrom : physicalClasspathsFrom){			
			for(Mapping classPathTo : physicalClasspathsTo){
				DependencyDTO[] dependencies = analyseService.getDependencies(classPathFrom.getPhysicalPath(),classPathTo.getPhysicalPath());
				totalCounter++;
				if(dependencies.length == 0) noDependencyCounter++;			
			}
			if(noDependencyCounter == totalCounter){
				Message message = new Message(rootRule);

				LogicalModule logicalModuleFrom = new LogicalModule(classPathFrom);
				LogicalModules logicalModules = new LogicalModules(logicalModuleFrom);
				Severity severity = CheckConformanceUtil.getSeverity(configuration, super.severity, null);
				Violation violation = createViolation(super.key, classPathFrom.getPhysicalPath(), false, message, logicalModules, severity);
				violations.add(violation);
			}
		}	
		if(noDependencyCounter != totalCounter){
			violations.clear();
		}
		return violations;
	}
}