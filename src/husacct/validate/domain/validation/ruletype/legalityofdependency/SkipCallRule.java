package husacct.validate.domain.validation.ruletype.legalityofdependency;

import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.check.CheckConformanceUtil;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.factory.violationtype.ViolationTypeFactory;
import husacct.validate.domain.validation.Message;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.iternal_tranfer_objects.Mapping;
import husacct.validate.domain.validation.logicalmodule.LogicalModule;
import husacct.validate.domain.validation.logicalmodule.LogicalModules;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class SkipCallRule extends RuleType {
	private final static EnumSet<RuleTypes> exceptionrules = EnumSet.of(RuleTypes.IS_ALLOWED);
	
	public SkipCallRule(String key, String category, List<ViolationType> violationtypes, Severity severity) {
		super(key, category, violationtypes, exceptionrules, severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule) {
		this.violations = new ArrayList<Violation>();
		this.violationtypefactory = new ViolationTypeFactory().getViolationTypeFactory(configuration);
		
		this.mappings = CheckConformanceUtil.filter(currentRule);
		List<Mapping> physicalClasspathsFrom = mappings.getMappingFrom();		
		List<List<Mapping>> modulesTo = filerLayers(Arrays.asList(defineService.getChildsFromModule(defineService.getParentFromModule(currentRule.moduleFrom.logicalPath))),currentRule);

		for(Mapping physicalClassPathFrom : physicalClasspathsFrom){
			for(List<Mapping> physicalClasspathsTo : modulesTo){
				for(Mapping classPathTo : physicalClasspathsTo ){
					DependencyDTO[] dependencies = analyseService.getDependencies(physicalClassPathFrom.getPhysicalPath(), classPathTo.getPhysicalPath(), currentRule.violationTypeKeys);	
					for(DependencyDTO dependency: dependencies){
						Message message = new Message(rootRule);
	
						LogicalModule logicalModuleFrom = new LogicalModule(physicalClassPathFrom);
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
	
	private List<List<Mapping>> filerLayers(List<ModuleDTO> allModules, RuleDTO currentRule){
		List<List<Mapping>> returnModules = new ArrayList<List<Mapping>>();
		for (ModuleDTO module :allModules){
			if(module.type.toLowerCase().contains("layer"))
			{
				if(module.logicalPath.toLowerCase().equals(currentRule.moduleFrom.logicalPath.toLowerCase()))
					returnModules = getModulesTo(allModules,allModules.indexOf(module));				
			}
		}			
		return returnModules;	
	}	
	
	private List<List<Mapping>> getModulesTo(List<ModuleDTO> allModules, int moduleFromNumber){
		List<List<Mapping>> returnList = new ArrayList<List<Mapping>>();
		for(ModuleDTO module : allModules){
			if(allModules.indexOf(module) > moduleFromNumber+1)
			returnList.add(CheckConformanceUtil.getAllModulesFromLayer(allModules.get(allModules.indexOf(module))));
		}		
		return returnList;
	}
}