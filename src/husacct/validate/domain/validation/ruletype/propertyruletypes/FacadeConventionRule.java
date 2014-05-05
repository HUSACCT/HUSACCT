package husacct.validate.domain.validation.ruletype.propertyruletypes;

import husacct.ServiceProvider;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.check.util.CheckConformanceUtilClass;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.internaltransferobjects.Mapping;
import husacct.validate.domain.validation.logicalmodule.LogicalModule;
import husacct.validate.domain.validation.logicalmodule.LogicalModules;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

public class FacadeConventionRule extends RuleType {

	public FacadeConventionRule(String key, String categoryKey, List<ViolationType> violationTypes, Severity severity) {
		super(key, categoryKey, violationTypes, EnumSet.of(RuleTypes.IS_ALLOWED_TO_USE), severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule) {
		violations.clear();
		mappings = CheckConformanceUtilClass.getMappingFromAndMappingTo(currentRule);
		physicalClasspathsFrom = mappings.getMappingFrom();

		// Create HashMap with all allowed-to-use-classes (the classes mapped to the facade(s)). Note: This HashMap is not used, but useful for debugging, and the costs are low.   
		HashMap<String, Mapping> facadeMap = new HashMap<String, Mapping>();

		// Create HashMap with all not allowed-to-use-classes (all other classes within the component)
		HashMap<String, Mapping> classesHiddeninComponentMap = new HashMap<String, Mapping>();

		// Create HashMap with all classes within the component; these classes are allowed to use each other
		HashMap<String, Mapping> allInComponentMap = new HashMap<String, Mapping>();
		for(Mapping from : physicalClasspathsFrom){
			allInComponentMap.put(from.getPhysicalPath(), from);
		}
		
		for (Mapping mappingFrom : physicalClasspathsFrom) {
			allInComponentMap.put(mappingFrom.getPhysicalPath(), mappingFrom);
			if(mappingFrom.getLogicalPathType().toLowerCase().equals("facade")) {
				facadeMap.put(mappingFrom.getPhysicalPath(), mappingFrom);
			}
			else {
				classesHiddeninComponentMap.put(mappingFrom.getPhysicalPath(), mappingFrom);
			}
		}
		
		for (String hiddenClassPath : classesHiddeninComponentMap.keySet()) {
			// Get all dependencies with matching dependency.classPathTo 
			DependencyDTO[] dependencies = analyseService.getDependenciesFromTo("", hiddenClassPath);
			for (DependencyDTO dependency : dependencies) {
				if(allInComponentMap.containsKey(dependency.from)){
					// Do nothing
				}
				else{
					Mapping classPathTo = classesHiddeninComponentMap.get(hiddenClassPath);
					Mapping classPathFrom = new Mapping(dependency.from, classPathTo.getViolationTypes());
                    Violation violation = createViolation(rootRule, classPathFrom, classPathTo, dependency, configuration);

					// Get logicalModuleFrom based on dependency.from and add it to the violation
                    ModuleDTO moduleFrom = ServiceProvider.getInstance().getDefineService().getLogicalModuleBySoftwareUnitName(dependency.from);
					if(moduleFrom != null){
						// Add moduleFrom to violation.logicalModules, so that graphics can include these violations in architecture diagrams
						LogicalModules logicalModulesOld = violation.getLogicalModules();
						LogicalModule logicalModuleTo = logicalModulesOld.getLogicalModuleTo();
						LogicalModule logicalModuleFrom = new LogicalModule(moduleFrom.logicalPath, moduleFrom.type);
						LogicalModules logicalModules = new LogicalModules(logicalModuleFrom, logicalModuleTo);
						violation.setLogicalModules(logicalModules);
					}
					
                    violations.add(violation);
				}
			}
		}		

		return violations;
	}
}