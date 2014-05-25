package husacct.validate.domain.validation.ruletype.relationruletypes;

import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.internaltransferobjects.Mapping;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;

public class IsNotAllowedToMakeSkipCall extends RuleType {

	private final static EnumSet<RuleTypes> exceptionRuleTypes = EnumSet.of(RuleTypes.IS_ALLOWED_TO_USE);
	private RuleDTO currentRule;
	private String logicalPathLayerFrom;
	
	public IsNotAllowedToMakeSkipCall(String key, String category, List<ViolationType> violationTypes, Severity severity) {
		super(key, category, violationTypes, exceptionRuleTypes, severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO rule) {
		violations.clear();
		this.currentRule = rule;
		this.logicalPathLayerFrom = currentRule.moduleFrom.logicalPath;

		fromMappings = getAllClasspathsOfModule(currentRule.moduleFrom, currentRule.violationTypeKeys);
		List<ModuleDTO> brotherModules = Arrays.asList(defineService.getModule_TheChildrenOfTheModule(defineService.getModule_TheParentOfTheModule(logicalPathLayerFrom)));
		List<ModuleDTO> potentialLayersToBeSkipped = selectPotentialLayersToBeSkipped(brotherModules);
		if(potentialLayersToBeSkipped.size() >= 1){
			List<Mapping> modulesTo = new ArrayList<>();
			for(ModuleDTO layerTo : potentialLayersToBeSkipped){ 
				modulesTo.addAll(getAllClasspathsOfModule(layerTo, currentRule.violationTypeKeys));
			}

			// Create a HashMap with all allowed from-to combinations, based on the exception rules.  
			HashSet<String> allExceptionFromTos = getAllExceptionFromTos(currentRule);

			for (Mapping classPathFrom : fromMappings) {
				for (Mapping classPathTo : modulesTo) {
					String fromToCombi = classPathFrom.getPhysicalPath() + "|" + classPathTo.getPhysicalPath(); 
					if (allExceptionFromTos.contains(fromToCombi)){
						// Do not add violations, since this usage is allowed. 
					} else{
						DependencyDTO[] violatingDependencies = analyseService.getDependenciesFromTo(classPathFrom.getPhysicalPath(), classPathTo.getPhysicalPath());
						if(violatingDependencies != null){
							for(DependencyDTO dependency : violatingDependencies){
								Violation violation = createViolation(rootRule, classPathFrom, classPathTo, dependency, configuration);
		                        violations.add(violation);
							}
						}
					}
				}
			}
		}
		return violations;
	}

	private List<ModuleDTO> selectPotentialLayersToBeSkipped(List<ModuleDTO> allModules) {
		List<ModuleDTO> returnModules = new ArrayList<>();
		int hierarchicalLevelModuleFrom = defineService.getHierarchicalLevelOfLayer(logicalPathLayerFrom);
		int highestSkipLevel = hierarchicalLevelModuleFrom + 2;
		for (ModuleDTO module : allModules) {
			if (module.type.toLowerCase().equalsIgnoreCase("layer")){
				int hierarchicalLevel = defineService.getHierarchicalLevelOfLayer(module.logicalPath);
                    if(hierarchicalLevel >= highestSkipLevel) {
                    	returnModules.add(module);
                    }
			}
		}
		return returnModules;
	}
}