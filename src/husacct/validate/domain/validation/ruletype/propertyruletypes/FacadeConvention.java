package husacct.validate.domain.validation.ruletype.propertyruletypes;

import husacct.ServiceProvider;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.internaltransferobjects.Mapping;
import husacct.validate.domain.validation.logicalmodule.LogicalModule;
import husacct.validate.domain.validation.logicalmodule.LogicalModules;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class FacadeConvention extends RuleType {

	public FacadeConvention(String key, String categoryKey, List<ViolationType> violationTypes, Severity severity) {
		super(key, categoryKey, violationTypes, EnumSet.of(RuleTypes.IS_ALLOWED_TO_USE), severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule) {
		violations.clear();
		fromMappings = getAllClasspathsOfModule(currentRule.moduleFrom, currentRule.violationTypeKeys);

		// Create HashMap with all classes within the component; these classes are allowed to use each other
		HashMap<String, Mapping> allInComponentMap = new HashMap<String, Mapping>();
		for(Mapping from : fromMappings){
			allInComponentMap.put(from.getPhysicalPath(), from);
		}
		
		// Create HashMap with all allowed-to-use-classes (the classes mapped to the facade(s)). Note: This HashMap is not used, but useful for debugging, and the costs are low.   
		HashMap<String, Mapping> facadeMap = new HashMap<String, Mapping>();
		ArrayList<Mapping> facadeMappings = getAllClassPathsOfFacadeOfComponent(currentRule.moduleFrom, currentRule.violationTypeKeys);
		for(Mapping facadeClassPath : facadeMappings){
			facadeMap.put(facadeClassPath.getPhysicalPath(), facadeClassPath);
		}

		// Create HashMap with all not allowed-to-use-classes (all other classes (so not facade-classes) within the component)
		HashMap<String, Mapping> classesHiddeninComponentMap = new HashMap<String, Mapping>();
		for (Mapping mappingFrom : fromMappings) {
			if(!facadeMap.containsKey(mappingFrom.getPhysicalPath())) {
				classesHiddeninComponentMap.put(mappingFrom.getPhysicalPath(), mappingFrom);
			}
		}
		
		// Create a HashMap with all allowed from-to combinations, based on the exception rules.  
		HashSet<String> allExceptionFromTos = getAllExceptionFromTos(currentRule);
		
		for (String hiddenClassPath : classesHiddeninComponentMap.keySet()) {
			// Get all dependencies with matching dependency.classPathTo 
			DependencyDTO[] dependencies = analyseService.getDependenciesFromTo("", hiddenClassPath);
			for (DependencyDTO dependency : dependencies) {
				String fromToCombi = dependency.fromClassPath + "|" + dependency.toClassPath; 
				if ((allInComponentMap.containsKey(dependency.from)) || (allExceptionFromTos.contains(fromToCombi))){
					// Do not add a violation, since the dependency is allowed. 
				} else{
					Mapping classPathTo = classesHiddeninComponentMap.get(hiddenClassPath);
					Mapping classPathFrom = new Mapping(dependency.from, classPathTo.getViolationTypes());
                    Violation violation = createViolation(currentRule, classPathFrom, classPathTo, dependency, configuration);

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
	
	private ArrayList<Mapping> getAllClassPathsOfFacadeOfComponent(ModuleDTO module, String[] violationTypeKeys) {
		ArrayList<Mapping> mappingFacade = new ArrayList<Mapping>();
		for (ModuleDTO subModule : defineService.getChildrenFromModule(module.logicalPath)) {
			if (subModule.type.toLowerCase().equals("facade"))
				mappingFacade.addAll(getAllClasspathsOfModule(subModule, violationTypeKeys));
		}
		return mappingFacade;
	}
	
}