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

	public IsNotAllowedToMakeBackCallRule(String key, String category, List<ViolationType> violationTypes, Severity severity) {
		super(key, category, violationTypes, EnumSet.of(RuleTypes.IS_ALLOWED_TO_USE), severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule) {
		violations.clear();
		mappings = CheckConformanceUtilClass.filterClassesFrom(currentRule);
		physicalClasspathsFrom = mappings.getMappingTo();
		List<List<Mapping>> modulesTo = filterLayers(Arrays.asList(defineService.getChildrenFromModule(defineService.getParentFromModule(currentRule.moduleFrom.logicalPath))), currentRule);
		DependencyDTO[] dependencies = analyseService.getAllDependencies();
		for (Mapping classPathFrom : physicalClasspathsFrom) {
			for (List<Mapping> moduleTo : modulesTo) {
				for (Mapping classpathTo : moduleTo) {
					for (DependencyDTO dependency : dependencies) {
						if (dependency.from.equals(classPathFrom.getPhysicalPath()) &&
                                dependency.to.equals(classpathTo.getPhysicalPath())) {
                            Violation violation = createViolation(rootRule, classPathFrom, classpathTo, dependency, configuration);
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
		int counter = -1;
		for (ModuleDTO module : allModules) {
			counter++;
			if (module.type.toLowerCase().equals("layer") &&
                    module.logicalPath.toLowerCase().equals(currentRule.moduleFrom.logicalPath.toLowerCase())) {
                returnModules = getModulesTo(allModules, counter, currentRule.violationTypeKeys);
			}
		}
		return returnModules;
	}

	private List<List<Mapping>> getModulesTo(List<ModuleDTO> allModules, int counter, String[] violationTypeKeys) {
		List<List<Mapping>> returnList = new ArrayList<>();
		for (int i = 0; i < counter; i++) {
			returnList.add(CheckConformanceUtilClass.getAllClasspathsFromModule(allModules.get(i), violationTypeKeys));
		}
		return returnList;
	}
}