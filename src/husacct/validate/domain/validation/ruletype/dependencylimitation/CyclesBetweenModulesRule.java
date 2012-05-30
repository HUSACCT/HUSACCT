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
				physicalClasspathsFrom.addAll(CheckConformanceUtilClass.getAllClasspathsFromModule(module, currentRule.violationTypeKeys));
			}

		}else{
			physicalClasspathsFrom = mappings.getMappingFrom();
		}

		for(Mapping physicalClassPathFrom : physicalClasspathsFrom){
			checkCircularDependencies(physicalClassPathFrom.getPhysicalPath(), new HashSet<String>(),configuration, rootRule,physicalClassPathFrom);
		}
		removeDuplicateViolations();
		return violations;
	}

	private void removeDuplicateViolations() {
			HashSet<Violation> h = new HashSet<Violation>(violations);
			violations.clear();
			violations.addAll(h);		
	}

	private HashSet<Violation> checkCircularDependencies(String physicalPath, HashSet<String> history, ConfigurationServiceImpl configuration, RuleDTO rootRule,Mapping mappingFrom)	{
		history.add(physicalPath);
		DependencyDTO[] dependencies = analyseService.getDependenciesFrom(physicalPath, mappingFrom.getViolationTypes());
		for(DependencyDTO dependency : dependencies){
			if(containsString(history, dependency.to) && !dependency.isIndirect){
				for(Mapping map : mappings.getMappingFrom()){
					if(!map.getPhysicalPath().equals(dependency.from)){
						Violation violation = createViolation(rootRule, mappingFrom, configuration);
						violations.add(violation);
					}
				}						
			}if(!containsString(history, dependency.to))
			violations.addAll(checkCircularDependencies(dependency.to,history,configuration,rootRule,mappingFrom));
		}
		return new HashSet<Violation>(violations);
	}
	private boolean containsString(HashSet<String> set, String match){
		for(String s : set){
			if(s.equals(match)){
				return true;
			}
		}
		return false;		
	}
}