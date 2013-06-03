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

public class IsNotAllowedToMakeSkipCallRule extends RuleType {

	public IsNotAllowedToMakeSkipCallRule(String key, String category, List<ViolationType> violationTypes, Severity severity) {
		super(key, category, violationTypes, EnumSet.of(RuleTypes.IS_ALLOWED_TO_USE), severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule) {
		mappings = CheckConformanceUtilClass.filterClassesFrom(currentRule);
		physicalClasspathsFrom = mappings.getMappingFrom();
		List<List<Mapping>> modulesTo = filterLayers(Arrays.asList(defineService.getChildrenFromModule(defineService.getParentFromModule(currentRule.moduleFrom.logicalPath))), currentRule);

		DependencyDTO[] dependencies = analyseService.getAllDependencies();

		for (Mapping classPathFrom : physicalClasspathsFrom) {
			for (List<Mapping> physicalClasspathsTo : modulesTo) {
				for (Mapping classPathTo : physicalClasspathsTo) {
					for (DependencyDTO dependency : dependencies) {
						if (dependency.from.equals(classPathFrom.getPhysicalPath()) &&
                                dependency.to.equals(classPathTo.getPhysicalPath()) &&
                                Arrays.binarySearch(classPathFrom.getViolationTypes(), dependency.type) >= 0) {
                            Violation violation = createViolation(rootRule, classPathFrom, classPathTo, dependency, configuration);
                            violations.add(violation);
                        }
					}
				}
			}
		}
		return violations;
	}

	private List<List<Mapping>> filterLayers(List<ModuleDTO> allModules, RuleDTO currentRule) {
		List<List<Mapping>> returnModules = new ArrayList<>();
		for (ModuleDTO module : allModules) {
			if (module.type.toLowerCase().contains("layer") &&
                    module.logicalPath.toLowerCase().equals(currentRule.moduleFrom.logicalPath.toLowerCase())) {
                returnModules = getModulesTo(allModules, allModules.indexOf(module), currentRule.violationTypeKeys);
			}
		}
		return returnModules;
	}

	private List<List<Mapping>> getModulesTo(List<ModuleDTO> allModules, int moduleFromNumber, String[] violationTypeKeys) {
		List<List<Mapping>> returnList = new ArrayList<>();
		for (ModuleDTO module : allModules) {
			if (allModules.indexOf(module) > moduleFromNumber + 1) {
				returnList.add(CheckConformanceUtilClass.getAllClasspathsFromModule(allModules.get(allModules.indexOf(module)), violationTypeKeys));
			}
		}
		return returnList;
	}
}