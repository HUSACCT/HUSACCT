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

public class FacadeConventionRule extends RuleType {
	private final static EnumSet<RuleTypes> exceptionrules = EnumSet.noneOf(RuleTypes.class);
	private HashSet<String> facadeCollection;

	public FacadeConventionRule(String key, String categoryKey, List<ViolationType> violationtypes, Severity severity) {
		super(key, categoryKey, violationtypes, exceptionrules, severity);

		this.facadeCollection = new HashSet<String>();
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule) {
		this.violations = new ArrayList<Violation>();

		this.mappings = CheckConformanceUtilClass.filterClassesFrom(currentRule);
		List<Mapping> physicalClasspathsFrom = mappings.getMappingFrom();
		List<Mapping> physicalClasspathsTo = mappings.getMappingTo();
		DependencyDTO[] dependencies = analyseService.getAllDependencies();

		for (Mapping classPathFrom : physicalClasspathsFrom) {
			
		}
		
		
		for (DependencyDTO dependency : dependencies) {
			System.out.println("[DEPENDENCY] " + dependency.from + " - " + dependency.to + " - " + dependency.lineNumber + " - " + dependency.type + " - " + dependency.isIndirect);
		}
		
		for (Mapping classPathFrom : physicalClasspathsFrom) {
			System.out.println("[MAPPING FROM] " + classPathFrom.getLogicalPath() + " - " + classPathFrom.getPhysicalPath() + " - type: " + classPathFrom.getLogicalPathType());
		}
		
		return violations;
		
//		for (Mapping classPathFrom : physicalClasspathsFrom) {
//			isClassPathAFacade(classPathFrom.getLogicalPath());
//		}
//
//		if (facadeCollection.size() > 0) {
//			for (String facadeClassPath : facadeCollection) {
//				String classPathFacadeLevel = facadeClassPath.substring(0, facadeClassPath.lastIndexOf("."));
//				for (DependencyDTO dependency : dependencies) {
//					String dependencyToClassPath = dependency.to.toLowerCase();
//					dependencyToClassPath = dependencyToClassPath.substring(0, dependencyToClassPath.lastIndexOf("."));
//					if (dependencyToClassPath.contains(classPathFacadeLevel)) {
//						if (!dependencyToClassPath.equals(classPathFacadeLevel)) {
//							Mapping classPathMappingDependency = null;
//							for (Mapping classPathFrom : physicalClasspathsFrom) {
//								if (classPathFrom.equals(dependency.from)) {
//									classPathMappingDependency = classPathFrom;
//								}
//							}
//							Violation violation = createViolation(rootRule, classPathMappingDependency, configuration);
//							violations.add(violation);
//						}
//					}
//				}
//			}
//		}
//		return violations;
//	}
//
//	private boolean isClassPathAFacade(String classPathFrom) {
//		String classPath = classPathFrom.toLowerCase();
//		if (classPath.contains("facade")) {
//			if (!isFacadeAlreadyInCollection(classPath)) {
//				addFacadeToCollection(classPath);
//			}
//		}
//		return false;
//	}
//
//	private boolean isFacadeAlreadyInCollection(String classPath) {
//		if (facadeCollection.contains(classPath)) {
//			return true;
//		}
//		return false;
//	}
//
//	private void addFacadeToCollection(String classPath) {
//		facadeCollection.add(classPath);
	}
}
