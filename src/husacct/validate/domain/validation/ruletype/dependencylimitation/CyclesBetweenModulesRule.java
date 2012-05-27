package husacct.validate.domain.validation.ruletype.dependencylimitation;

import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.check.util.CheckConformanceUtilClass;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.iternal_tranfer_objects.Mapping;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;

public class CyclesBetweenModulesRule extends RuleType{
	private final static EnumSet<RuleTypes> exceptionrules = EnumSet.noneOf(RuleTypes.class);

	public CyclesBetweenModulesRule(String key, String category, List<ViolationType> violationtypes, Severity severity) {
		super(key, category, violationtypes, exceptionrules, severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule) {
		this.violations = new ArrayList<Violation>();
		this.physicalClasspathsFrom = new ArrayList<Mapping>();

		this.mappings = CheckConformanceUtilClass.filterClasses(currentRule);

		if(mappings.getMappingFrom().isEmpty()){
			for(ModuleDTO module : defineService.getRootModules()){
				physicalClasspathsFrom.addAll(CheckConformanceUtilClass.getAllClassesFromLayer(module, currentRule.violationTypeKeys));
			}

		}else{
			physicalClasspathsFrom = mappings.getMappingFrom();
		}

		for(Mapping physicalClassPathFrom : physicalClasspathsFrom){
			checkCircularDependencies(physicalClassPathFrom.getPhysicalPath(), new HashSet<String>(),configuration, rootRule,physicalClassPathFrom);
		}
		return violations;
	}

	private List<Violation> checkCircularDependencies(String physicalPath, HashSet<String> history, ConfigurationServiceImpl configuration, RuleDTO rootRule,Mapping mappingFrom)	{
		//TODO: if all classes in loop have the same mapping.logicalpath, NO violation!
		history.add(physicalPath);
		DependencyDTO[] dependencies = analyseService.getDependenciesFrom(physicalPath);
		for(DependencyDTO dependency : dependencies){
			if(history.contains(dependency.to)){
				Violation violation = createViolation(rootRule, mappingFrom, configuration);
				violations.add(violation);				
			}

			violations.addAll(checkCircularDependencies(dependency.to,history,configuration,rootRule,mappingFrom));
		}
		return violations;
	}
}