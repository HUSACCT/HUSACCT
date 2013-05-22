package husacct.validate.domain.validation.ruletype.relationruletypes;

import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.check.util.CheckConformanceUtilClass;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.internal_transfer_objects.Mapping;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class IsNotAllowedToMakeBackCallRule extends RuleType {

	private final static EnumSet<RuleTypes> exceptionrules = EnumSet.of(RuleTypes.IS_ALLOWED);

	public IsNotAllowedToMakeBackCallRule(String key, String category, List<ViolationType> violationtypes, Severity severity) {
		super(key, category, violationtypes, exceptionrules, severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule) {
		this.violations = new ArrayList<Violation>();

		this.mappings = CheckConformanceUtilClass.filterClassesFrom(currentRule);
		this.physicalClasspathsFrom = mappings.getMappingFrom();
		List<List<Mapping>> modulesTo = filterLayers(Arrays.asList(defineService.getChildrenFromModule(defineService.getParentFromModule(currentRule.moduleFrom.logicalPath))), currentRule);

		DependencyDTO[] dependencies = analyseService.getAllDependencies();

		for (Mapping classPathFrom : physicalClasspathsFrom) {
			for (List<Mapping> moduleTo : modulesTo) {
				for (Mapping classpathTo : moduleTo) {
					for (DependencyDTO dependency : dependencies) {
						if (dependency.from.equals(classPathFrom.getPhysicalPath())) {
							if (dependency.to.equals(classpathTo.getPhysicalPath())) {
								if (Arrays.binarySearch(classPathFrom.getViolationTypes(), dependency.type) >= 0) {
									Violation violation = createViolation(rootRule, classPathFrom, classpathTo, dependency, configuration);
									violations.add(violation);
								}
							}
						}
					}
				}
			}
		}
		return violations;
	}

	private List<List<Mapping>> filterLayers(List<ModuleDTO> allModules, RuleDTO currentRule) {
		List<List<Mapping>> returnModules = new ArrayList<List<Mapping>>();
		int counter = -1;
		for (ModuleDTO module : allModules) {
			counter++;
			if (module.type.toLowerCase().equals("layer")) {
				if (module.logicalPath.toLowerCase().equals(currentRule.moduleFrom.logicalPath.toLowerCase())) {
					returnModules = getModulesTo(allModules, counter, currentRule.violationTypeKeys);
				}
			}
		}
		return returnModules;
	}

	private List<List<Mapping>> getModulesTo(List<ModuleDTO> allModules, int counter, String[] violationTypeKeys) {
		List<List<Mapping>> returnList = new ArrayList<List<Mapping>>();
		for (int i = 0; i < counter; i++) {
			returnList.add(CheckConformanceUtilClass.getAllClasspathsFromModule(allModules.get(i), violationTypeKeys));
		}
		return returnList;
	}
}