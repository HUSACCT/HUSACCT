package husacct.validate.domain.check.util;

import husacct.ServiceProvider;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.PhysicalPathDTO;
import husacct.common.dto.RuleDTO;
import husacct.define.IDefineService;
import husacct.validate.domain.validation.internaltransferobjects.Mapping;
import husacct.validate.domain.validation.internaltransferobjects.Mappings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class CheckConformanceUtilPackage extends CheckConformanceUtil {

	private static IDefineService define = ServiceProvider.getInstance().getDefineService();

	public static Mappings filterPackages(RuleDTO rule) {
		Mappings mainClasspaths = getAllPackagepathsFromModule(rule);
		List<Mapping> exceptionClasspathFrom = new ArrayList<Mapping>();
		List<Mapping> exceptionClasspathTo = new ArrayList<Mapping>();

		if (rule.exceptionRules != null) {
			for (RuleDTO exceptionRule : rule.exceptionRules) {
				Mappings exceptionClasspaths = getAllPackagepathsFromModule(exceptionRule);
				exceptionClasspathFrom.addAll(exceptionClasspaths.getMappingFrom());
				exceptionClasspathTo.addAll(exceptionClasspaths.getMappingTo());
			}
		}
		return removeExceptionPathsFrom(mainClasspaths, exceptionClasspathFrom, exceptionClasspathTo);
	}

	private static Mappings getAllPackagepathsFromModule(RuleDTO rule) {
		ArrayList<Mapping> mappingFrom = new ArrayList<Mapping>();
		ArrayList<Mapping> mappingTo = new ArrayList<Mapping>();

		Arrays.sort(rule.violationTypeKeys);
		mappingFrom = getAllPackagepathsFromModule(rule.moduleFrom, rule.violationTypeKeys);
		mappingTo = getAllPackagepathsFromModule(rule.moduleTo, rule.violationTypeKeys);

		return new Mappings(mappingFrom, mappingTo);
	}

	private static ArrayList<Mapping> getAllPackagepathsFromModule(ModuleDTO rootModule, String[] violationTypeKeys) {
		HashSet<Mapping> classpathsFrom = new HashSet<Mapping>();

		if (rootModule.logicalPath.equals("**") && rootModule.physicalPathDTOs.length == 0 && rootModule.subModules.length == 0) {
			classpathsFrom.addAll(getAllPackages(violationTypeKeys));
		} else {
			classpathsFrom.addAll(getPackageFromPhysicalPathDTO(rootModule, violationTypeKeys));
			getAllPackagepathsFromModule(rootModule, classpathsFrom, violationTypeKeys);
		}
		return new ArrayList<Mapping>(classpathsFrom);
	}

	private static HashSet<Mapping> getAllPackages(String[] violationTypeKeys) {
		HashSet<Mapping> allPackages = new HashSet<Mapping>();
		ModuleDTO[] rootModules = define.getRootModules();
		for (ModuleDTO rootModule : rootModules) {
			allPackages.addAll(getPackageFromPhysicalPathDTO(rootModule, violationTypeKeys));
			allPackages.addAll(getAllPackagePathsFromAllModules(rootModule, allPackages, violationTypeKeys));
		}
		return allPackages;
	}

	private static HashSet<Mapping> getAllPackagePathsFromAllModules(ModuleDTO module, HashSet<Mapping> classpaths, String[] violationTypeKeys) {
		for (ModuleDTO subModule : define.getChildrenFromModule(module.logicalPath)) {
			classpaths.addAll(getPackageFromPhysicalPathDTO(subModule, violationTypeKeys));
			classpaths.addAll(getAllPackagepathsFromModule(subModule, classpaths, violationTypeKeys));
		}
		return classpaths;
	}

	private static ArrayList<Mapping> getAllPackagepathsFromModule(ModuleDTO module, HashSet<Mapping> classpaths, String[] violationTypeKeys) {
		for (ModuleDTO subModule : module.subModules) {
			classpaths.addAll(getPackageFromPhysicalPathDTO(subModule, violationTypeKeys));
			getAllPackagepathsFromModule(subModule, classpaths, violationTypeKeys);
		}
		return new ArrayList<Mapping>(classpaths);
	}

	private static HashSet<Mapping> getPackageFromPhysicalPathDTO(ModuleDTO module, String[] violationTypeKeys) {
		HashSet<Mapping> classpaths = new HashSet<Mapping>();
		for (String classpath : getAllChildPackages(module)) {
			if (!updateLogicalPaths(classpaths, module, classpath, violationTypeKeys)) {
				classpaths.add(new Mapping(module.logicalPath, module.type, classpath, violationTypeKeys));
			}
		}
		return classpaths;
	}

	private static List<String> getAllChildPackages(ModuleDTO module) {
		List<String> paths = new ArrayList<String>();

		for (PhysicalPathDTO classpath : module.physicalPathDTOs) {
			if (classpath.type.toLowerCase().equals("package")) {
				paths.add(classpath.path);
				paths.addAll(getAllChildPackages(classpath.path));
			}
		}
		return paths;
	}

	private static List<String> getAllChildPackages(String physicalPath) {
		List<String> paths = new ArrayList<String>();
		AnalysedModuleDTO[] analysedSubModuleDTOs = ServiceProvider.getInstance().getAnalyseService().getChildModulesInModule(physicalPath);
		for (AnalysedModuleDTO am : analysedSubModuleDTOs) {
			if (am.type.toLowerCase().equals("package")) {
				List<String> subPaths = getAllChildPackages(am.uniqueName);
				paths.add(am.uniqueName);
				paths.addAll(subPaths);
			}
		}
		return paths;
	}

	private static boolean updateLogicalPaths(HashSet<Mapping> classpaths, ModuleDTO module, String physicalClassPath, String[] violationTypeKeys) {
		List<Mapping> duplicatedMappings = getClassPaths(classpaths, physicalClassPath, module.logicalPath);
		boolean logicalPathUpdated = false;
		for (Mapping duplicatedMapping : duplicatedMappings) {
			classpaths.remove(duplicatedMapping);
			classpaths.add(new Mapping(module.logicalPath, module.type, physicalClassPath, violationTypeKeys));
			logicalPathUpdated = true;
		}
		return logicalPathUpdated;
	}

	private static List<Mapping> getClassPaths(HashSet<Mapping> classpaths, String physicalClassPath, String logicalPath) {
		List<Mapping> foundMappings = new ArrayList<Mapping>();
		for (Mapping mapping : classpaths) {
			if (mapping.getPhysicalPath().equals(physicalClassPath) && !mapping.getLogicalPath().equals(logicalPath) && mapping.getLogicalPath().split("\\.").length < logicalPath.split("\\.").length) {
				foundMappings.add(mapping);
			}
		}
		return foundMappings;
	}
}