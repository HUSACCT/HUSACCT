package husacct.validate.domain.validation.ruletype.relationruletypes;

import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
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
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class IsNotAllowedToMakeBackCallRule extends RuleType {
	
	private RuleDTO currentRule;
	private String logicalPathLayerFrom;


	public IsNotAllowedToMakeBackCallRule(String key, String category, List<ViolationType> violationTypes, Severity severity) {
		super(key, category, violationTypes, EnumSet.of(RuleTypes.IS_ALLOWED_TO_USE), severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO rule) {
		this.currentRule = rule;
		this.logicalPathLayerFrom = currentRule.moduleFrom.logicalPath;
		violations.clear();
		mappings = null;

		mappings = CheckConformanceUtilClass.getMappingFromAndMappingTo(currentRule);
		physicalClasspathsFrom = mappings.getMappingFrom();
		List<ModuleDTO> brotherModules = Arrays.asList(defineService.getChildrenFromModule(defineService.getParentFromModule(logicalPathLayerFrom)));
		List<ModuleDTO> potentialLayersToBeBackCalled = selectPotentialLayersToBeBackCalled(brotherModules);
		if(potentialLayersToBeBackCalled.size() >= 1){
			List<Mapping> modulesTo = new ArrayList<Mapping>();
			for(ModuleDTO layerTo : potentialLayersToBeBackCalled){ 
				modulesTo.addAll(CheckConformanceUtilClass.getAllClasspathsOfModule(layerTo, currentRule.violationTypeKeys));
			}

			for (Mapping classPathFrom : physicalClasspathsFrom) {
				for (Mapping classPathTo : modulesTo) {
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
		return violations;
	}

	private List<ModuleDTO> selectPotentialLayersToBeBackCalled(List<ModuleDTO> allModules) {
		List<ModuleDTO> returnModules = new ArrayList<>();
		int hierarchicalLevelModuleFrom = defineService.getHierarchicalLevelOfLayer(logicalPathLayerFrom);
		int lowestBackCallLevel = hierarchicalLevelModuleFrom -1;
		for (ModuleDTO module : allModules) {
			if (module.type.toLowerCase().equalsIgnoreCase("layer")){
				int hierarchicalLevel = defineService.getHierarchicalLevelOfLayer(module.logicalPath);
                    if(hierarchicalLevel <= lowestBackCallLevel) {
                    	returnModules.add(module);
                    }
			}
		}
		return returnModules;
	}


}