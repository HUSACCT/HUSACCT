package husacct.validate.domain.check.util;

import husacct.ServiceProvider;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.PhysicalPathDTO;
import husacct.common.dto.RuleDTO;
import husacct.define.IDefineService;
import husacct.validate.domain.validation.iternal_tranfer_objects.Mapping;
import husacct.validate.domain.validation.iternal_tranfer_objects.Mappings;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CheckConformanceUtilPackage extends CheckConformanceUtil{
	private static IDefineService define = ServiceProvider.getInstance().getDefineService();

	public static Mappings filterPackages(RuleDTO rule){
		Mappings mainClasspaths = getAllPackagepathsFromModule(rule);
		List<Mapping> exceptionClasspathFrom = new ArrayList<Mapping>();
		List<Mapping> exceptionClasspathTo = new ArrayList<Mapping>();

		if(rule.exceptionRules!= null){
			for(RuleDTO exceptionRule : rule.exceptionRules){
				Mappings exceptionClasspaths = getAllPackagepathsFromModule(exceptionRule);
				exceptionClasspathFrom.addAll(exceptionClasspaths.getMappingFrom());
				exceptionClasspathTo.addAll(exceptionClasspaths.getMappingTo());
			}
		}
		return removeExceptionPaths(mainClasspaths, exceptionClasspathFrom, exceptionClasspathTo);
	}

	private static Mappings getAllPackagepathsFromModule(RuleDTO rule){
		ArrayList<Mapping> mappingFrom = new ArrayList<Mapping>();
		ArrayList<Mapping> mappingTo = new ArrayList<Mapping>();

		mappingFrom = getAllPackagepathsFromModule(rule.moduleFrom, rule.violationTypeKeys);
		mappingTo = getAllPackagepathsFromModule(rule.moduleTo, rule.violationTypeKeys);

		return new Mappings(mappingFrom, mappingTo);
	}

	private static ArrayList<Mapping> getAllPackagepathsFromModule(ModuleDTO rootModule, String[] violationTypeKeys){
		HashSet<Mapping> classpathsFrom = new HashSet<Mapping>();

		if(rootModule.logicalPath.equals("**") && rootModule.physicalPathDTOs.length == 0 && rootModule.subModules.length == 0){
			classpathsFrom.addAll(getAllPackages(violationTypeKeys));
		}
		else{
			classpathsFrom.addAll(getPackageFromPhysicalPathDTO(rootModule, violationTypeKeys));
			getAllPackagepathsFromModule(rootModule, classpathsFrom, violationTypeKeys);
		}
		return new ArrayList<Mapping>(classpathsFrom);
	}

	private static HashSet<Mapping> getAllPackages(String[] violationTypeKeys){
		HashSet<Mapping> allPackages = new HashSet<Mapping>();
		ModuleDTO[] rootModules = define.getRootModules();
		for(ModuleDTO rootModule : rootModules){
			allPackages.addAll(getPackageFromPhysicalPathDTO(rootModule, violationTypeKeys));
			allPackages.addAll(getAllPackagePathsFromAllModules(rootModule, allPackages, violationTypeKeys));
		}
		return allPackages;
	}

	private static HashSet<Mapping> getAllPackagePathsFromAllModules(ModuleDTO module, HashSet<Mapping> classpaths, String[] violationTypeKeys){
		for(ModuleDTO subModule : define.getChildrenFromModule(module.logicalPath)){

			classpaths.addAll(getPackageFromPhysicalPathDTO(subModule, violationTypeKeys));

			classpaths.addAll(getAllPackagepathsFromModule(subModule, classpaths, violationTypeKeys));
		}
		return classpaths;
	}	

	private static ArrayList<Mapping> getAllPackagepathsFromModule(ModuleDTO module, HashSet<Mapping> classpaths, String[] violationTypeKeys){
		for(ModuleDTO subModule : module.subModules){
			classpaths.addAll(getPackageFromPhysicalPathDTO(subModule, violationTypeKeys));
			getAllPackagepathsFromModule(subModule, classpaths, violationTypeKeys);
		}
		return new ArrayList<Mapping>(classpaths);
	}

	private static HashSet<Mapping> getPackageFromPhysicalPathDTO(ModuleDTO module, String[] violationTypeKeys){
		HashSet<Mapping> classpaths = new HashSet<Mapping>();
		for(PhysicalPathDTO physicalClasspath : module.physicalPathDTOs){
			if(physicalClasspath.type.toLowerCase().equals("package")){
				if(!updateLogicalPaths(classpaths, module, physicalClasspath.path, violationTypeKeys)){
					classpaths.add(new Mapping(module.logicalPath, module.type, physicalClasspath.path, violationTypeKeys));	
				}
			}
		}
		return classpaths;
	}

	private static boolean updateLogicalPaths(HashSet<Mapping> classpaths, ModuleDTO module, String physicalClassPath, String[] violationTypeKeys){
		List<Mapping> duplicatedMappings = getClassPaths(classpaths, physicalClassPath, module.logicalPath);
		boolean logicalPathUpdated = false;
		for(Mapping duplicatedMapping : duplicatedMappings){	
			classpaths.remove(duplicatedMapping);
			classpaths.add(new Mapping(module.logicalPath, module.type, physicalClassPath, violationTypeKeys));
			logicalPathUpdated = true;
		}	
		return logicalPathUpdated;
	}

	private static List<Mapping> getClassPaths(HashSet<Mapping> classpaths, String physicalClassPath, String logicalPath){
		List<Mapping> foundMappings = new ArrayList<Mapping>();
		for(Mapping mapping : classpaths){
			if(mapping.getPhysicalPath().equals(physicalClassPath) && !mapping.getLogicalPath().equals(logicalPath) && mapping.getLogicalPath().split("\\.").length < logicalPath.split("\\.").length){
				foundMappings.add(mapping);
			}
		}
		return foundMappings;
	}
}