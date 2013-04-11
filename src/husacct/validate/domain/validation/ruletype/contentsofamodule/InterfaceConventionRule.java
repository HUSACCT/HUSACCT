package husacct.validate.domain.validation.ruletype.contentsofamodule;

import husacct.common.dto.DependencyDTO;
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
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;

public class InterfaceConventionRule extends RuleType {

	private final static EnumSet<RuleTypes> exceptionrules = EnumSet.of(RuleTypes.IS_ALLOWED);
	private HashSet<String> interfaceCache;
	private HashSet<String> noInterfaceCache;

	public InterfaceConventionRule(String key, String category, List<ViolationType> violationtypes, Severity severity) {
		super(key, category, violationtypes, exceptionrules, severity);

		this.interfaceCache = new HashSet<String>();
		this.noInterfaceCache = new HashSet<String>();
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule) {
		this.violations = new ArrayList<Violation>();

		this.mappings = CheckConformanceUtilClass.filterClassesFrom(currentRule);
		List<Mapping> physicalClasspathsFrom = mappings.getMappingFrom();
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
			if (interfaceCounter == 0 && physicalClasspathsTo.size() != 0) {
				Violation violation = createViolation(rootRule, classPathFrom, configuration);
				violations.add(violation);
			}
		}
		return violations;
	}

	private boolean isInterface(String classPath) {
		if (interfaceCache.contains(classPath)) {
			return true;
		}
		else if (noInterfaceCache.contains(classPath)) {
			return false;
		}
		else {
			return addToCache(classPath);
		}
	}

	private boolean addToCache(String classPath) {
		boolean isInterface = analyseService.getModuleForUniqueName(classPath).type.toLowerCase().equals("interface");
		if (isInterface) {
			interfaceCache.add(classPath);
			return true;
		}
		else {
			noInterfaceCache.add(classPath);
			return false;
		}
	}
}