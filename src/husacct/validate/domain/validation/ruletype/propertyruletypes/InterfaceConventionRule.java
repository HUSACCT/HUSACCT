package husacct.validate.domain.validation.ruletype.propertyruletypes;

import husacct.common.dto.DependencyDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.check.util.CheckConformanceUtilClass;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.internaltransferobjects.Mapping;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;

public class InterfaceConventionRule extends RuleType {

	private HashSet<String> interfaceCache;
	private HashSet<String> noInterfaceCache;

	public InterfaceConventionRule(String key, String category, List<ViolationType> violationTypes, Severity severity) {
		super(key, category, violationTypes, EnumSet.of(RuleTypes.IS_ALLOWED_TO_USE), severity);
		interfaceCache = new HashSet<>();
		noInterfaceCache = new HashSet<>();
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule) {
		mappings = CheckConformanceUtilClass.filterClassesFrom(currentRule);
		physicalClasspathsFrom = mappings.getMappingFrom();
		List<Mapping> physicalClasspathsTo = mappings.getMappingTo();

		DependencyDTO[] dependencies = analyseService.getAllDependencies();

		for (Mapping classPathFrom : physicalClasspathsFrom) {
			int interfaceCounter = 0;
			for (Mapping classPathTo : physicalClasspathsTo) {
				for (DependencyDTO dependency : dependencies) {
					if (dependency.from.equals(classPathFrom.getPhysicalPath()) && dependency.to.equals(classPathTo.getPhysicalPath()) && isInterface(dependency.to)) {
						interfaceCounter++;
					}
				}
			}
			if (interfaceCounter == 0 && !physicalClasspathsTo.isEmpty()) {
				Violation violation = createViolation(rootRule, classPathFrom, configuration);
				violations.add(violation);
			}
		}
		return violations;
	}

	private boolean isInterface(String classPath) {
		if (interfaceCache.contains(classPath)) {
			return true;
		} else if (noInterfaceCache.contains(classPath)) {
			return false;
		} else {
			return addToCache(classPath);
		}
	}

	private boolean addToCache(String classPath) {
		boolean isInterface = analyseService.getModuleForUniqueName(classPath).type.toLowerCase().equals("interface");
		if (isInterface) {
			interfaceCache.add(classPath);
			return true;
		} else {
			noInterfaceCache.add(classPath);
			return false;
		}
	}
}