package husacct.validate.domain.validation.ruletype.legalityofdependency;

import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.check.util.CheckConformanceUtilClass;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.factory.violationtype.ViolationTypeFactory;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.iternal_tranfer_objects.Mapping;
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

		this.mappings = CheckConformanceUtilClass.filterClasses(currentRule);
		this.physicalClasspathsFrom = mappings.getMappingFrom();		
		List<List<Mapping>> modulesTo = filerLayers(Arrays.asList(defineService.getChildrenFromModule(defineService.getParentFromModule(currentRule.moduleFrom.logicalPath))),currentRule);

		for(Mapping physicalClassPathFrom : physicalClasspathsFrom){
			for(List<Mapping> physicalClasspathsTo : modulesTo){
				for(Mapping classPathTo : physicalClasspathsTo ){
					DependencyDTO[] dependencies = analyseService.getDependencies(physicalClassPathFrom.getPhysicalPath(), classPathTo.getPhysicalPath(), physicalClassPathFrom.getViolationTypes());	
					for(DependencyDTO dependency: dependencies){
						Violation violation = createViolation(rootRule,physicalClassPathFrom,classPathTo,dependency,configuration);
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
					returnModules = getModulesTo(allModules,allModules.indexOf(module), currentRule.violationTypeKeys);				
			}
		}			
		return returnModules;	
	}	

	private List<List<Mapping>> getModulesTo(List<ModuleDTO> allModules, int moduleFromNumber, String[] violationTypeKeys){
		List<List<Mapping>> returnList = new ArrayList<List<Mapping>>();
		for(ModuleDTO module : allModules){
			if(allModules.indexOf(module) > moduleFromNumber+1)
				returnList.add(CheckConformanceUtilClass.getAllModulesFromLayer(allModules.get(allModules.indexOf(module)), violationTypeKeys));
		}		
		return returnList;
	}
}