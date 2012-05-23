package husacct.validate.domain.validation.ruletype.dependencylimitation;

import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.check.CheckConformanceUtilFilter;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.factory.violationtype.ViolationTypeFactory;
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

public class LoopsInModuleRule extends RuleType{
	private final static EnumSet<RuleTypes> exceptionrules = EnumSet.noneOf(RuleTypes.class);

	public LoopsInModuleRule(String key, String category, List<ViolationType> violationtypes, Severity severity) {
		super(key, category, violationtypes, exceptionrules, severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule) {
		this.violations = new ArrayList<Violation>();
		this.violationtypefactory = new ViolationTypeFactory().getViolationTypeFactory(configuration);
		this.physicalClasspathsFrom = new ArrayList<Mapping>();

		this.mappings = CheckConformanceUtilFilter.filter(currentRule);
		//FromModule physicalPaths array
		//ToModule physicalPaths array
		
		if(mappings.getMappingFrom().isEmpty()){
			for(ModuleDTO module : defineService.getRootModules()){
				physicalClasspathsFrom.addAll(CheckConformanceUtilFilter.getAllModulesFromLayer(module));
			}
			
		}else{
			physicalClasspathsFrom = mappings.getMappingFrom();
		}
		
		for(Mapping physicalClassPathFrom : physicalClasspathsFrom){
			getClassPathsTo(physicalClassPathFrom.getPhysicalPath(), new HashSet<String>());
		}
		return violations;
	}

	private List<Violation> getClassPathsTo(String physicalPath, HashSet<String> history)	{
		history.add(physicalPath);
		DependencyDTO[] dep = analyseService.getDependenciesFrom(physicalPath);
		for(DependencyDTO dependency : dep){
			if(history.contains(dependency.to)){
					System.out.println("violations!!");					
			}
			
			violations.addAll(getClassPathsTo(dependency.to,history));
		}
		return violations;
	}
}