package husacct.validate.domain.check.util;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.PhysicalPathDTO;
import husacct.common.dto.RuleDTO;
import husacct.define.IDefineService;
import husacct.validate.domain.validation.iternal_transfer_objects.Mapping;
import husacct.validate.domain.validation.iternal_transfer_objects.Mappings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class CheckConformanceUtilClass extends CheckConformanceUtil {
	private static IDefineService define = ServiceProvider.getInstance().getDefineService();
	private static IAnalyseService analyse = ServiceProvider.getInstance().getAnalyseService();

	public static Mappings filterClasses(RuleDTO rule){
		Mappings mainClasspaths = getAllClasspathsFromModule(rule);
		List<Mapping> exceptionClasspathFrom = getExceptionClassPathFrom(rule);
		List<Mapping> exceptionClasspathTo = getExceptionClassPathTo(rule);

		return removeExceptionPaths(mainClasspaths, exceptionClasspathFrom, exceptionClasspathTo);
	}

	public static Mappings filterClassesFrom(RuleDTO rule){
		Mappings mainClasspaths = getAllClasspathsFromModule(rule);
		List<Mapping> exceptionClasspathFrom = getExceptionClassPathFrom(rule);
		List<Mapping> exceptionClasspathTo = getExceptionClassPathTo(rule);

		return removeExceptionPathsFrom(mainClasspaths, exceptionClasspathFrom, exceptionClasspathTo);
	}

	public static Mappings filterClassesTo(RuleDTO rule){
		Mappings mainClasspaths = getAllClasspathsFromModule(rule);
		List<Mapping> exceptionClasspathFrom = getExceptionClassPathFrom(rule);
		List<Mapping> exceptionClasspathTo = getExceptionClassPathTo(rule);

		return removeExceptionPathsTo(mainClasspaths, exceptionClasspathFrom, exceptionClasspathTo);
	}

	private static List<Mapping> getExceptionClassPathTo(RuleDTO rule){
		List<Mapping> exceptionClasspathTo = new ArrayList<Mapping>();
			for(RuleDTO exceptionRule : rule.exceptionRules){
				Mappings exceptionClasspaths = getAllClasspathsFromModule(exceptionRule);
				exceptionClasspathTo.addAll(exceptionClasspaths.getMappingTo());
			}
		return exceptionClasspathTo;
	}

	private static List<Mapping> getExceptionClassPathFrom(RuleDTO rule){
		List<Mapping> exceptionClasspathFrom = new ArrayList<Mapping>();
			for(RuleDTO exceptionRule : rule.exceptionRules){
				Mappings exceptionClasspaths = getAllClasspathsFromModule(exceptionRule);
				exceptionClasspathFrom.addAll(exceptionClasspaths.getMappingFrom());
		}
		return exceptionClasspathFrom;
	}

	private static Mappings getAllClasspathsFromModule(RuleDTO rule){		
		ArrayList<Mapping> mappingFrom = new ArrayList<Mapping>();
		ArrayList<Mapping> mappingTo = new ArrayList<Mapping>();		

		Arrays.sort(rule.violationTypeKeys);

		mappingFrom = getAllClasspathsFromModule(rule.moduleFrom, rule.violationTypeKeys);
		mappingTo = getAllClasspathsFromModule(rule.moduleTo, rule.violationTypeKeys);

		return new Mappings(mappingFrom, mappingTo);
	}

	public static ArrayList<Mapping> getAllClasspathsFromModule(ModuleDTO rootModule, String[] violationTypeKeys){
		HashSet<Mapping> classpathsFrom = new HashSet<Mapping>();

		if(rootModule.logicalPath.equals("**") && rootModule.physicalPathDTOs.length == 0 && rootModule.subModules.length == 0){
			classpathsFrom.addAll(getAllClassPaths(violationTypeKeys));
		}
		else{
			classpathsFrom.addAll(getClassFromPhysicalPathDTO(rootModule, violationTypeKeys, classpathsFrom));
			getAllClasspathsFromModule(rootModule, classpathsFrom, violationTypeKeys);	
		}
		return new ArrayList<Mapping>(classpathsFrom);
	}

	private static HashSet<Mapping> getAllClassPaths(String[] violationTypeKeys){
		HashSet<Mapping> allClasses = new HashSet<Mapping>();
		ModuleDTO[] rootModules = define.getRootModules();
		for(ModuleDTO rootModule : rootModules){
			allClasses.addAll(getClassFromPhysicalPathDTO(rootModule, violationTypeKeys, allClasses));
			allClasses.addAll(getAllClasspathsFromAllModules(rootModule, allClasses, violationTypeKeys));
		}
		return allClasses;
	}

	private static HashSet<Mapping> getAllClasspathsFromAllModules(ModuleDTO module, HashSet<Mapping> classpaths, String[] violationTypeKeys){
		for(ModuleDTO subModule : define.getChildrenFromModule(module.logicalPath)){

			classpaths.addAll(getClassFromPhysicalPathDTO(subModule, violationTypeKeys, classpaths));

			classpaths.addAll(getAllClasspathsFromModule(subModule, classpaths, violationTypeKeys));
		}
		return classpaths;
	}	

	private static HashSet<Mapping> getAllClasspathsFromModule(ModuleDTO module, HashSet<Mapping> classpaths, String[] violationTypeKeys){
		for(ModuleDTO subModule : module.subModules){

			classpaths.addAll(getClassFromPhysicalPathDTO(subModule, violationTypeKeys, classpaths));

			classpaths.addAll(getAllClasspathsFromModule(subModule, classpaths, violationTypeKeys));
		}
		return classpaths;
	}	

	private static HashSet<Mapping> getClassFromPhysicalPathDTO(ModuleDTO module, String[] violationTypeKeys, HashSet<Mapping> classpaths){
		for(String classpath : getAllChildPhysicalPaths(module)){			
			if(!updateLogicalPaths(classpaths, module, classpath, violationTypeKeys)){
				classpaths.add(new Mapping(module.logicalPath, module.type, classpath, violationTypeKeys));

				AnalysedModuleDTO analysedModule = analyse.getModuleForUniqueName(classpath);
				classpaths.addAll(getInnerClasses(analysedModule, new ArrayList<Mapping>(), module, violationTypeKeys));
			}
		}
		return classpaths;
	}	
	
	private static List<String> getAllChildPhysicalPaths(ModuleDTO module){
		List<String> paths = new ArrayList<String>();
		
		for(PhysicalPathDTO classpath : module.physicalPathDTOs){
			if(classpath.type.toLowerCase().equals("package")){	
				paths.add(classpath.path);
				paths.addAll(getAllChildPhysicalPaths(classpath.path));
			}
		}		
		return paths;
	}
	
	private static List<String> getAllChildPhysicalPaths(String physicalPath){
		List<String> paths = new ArrayList<String>();
		AnalysedModuleDTO[] analysedSubModuleDTOs = ServiceProvider.getInstance().getAnalyseService().getChildModulesInModule(physicalPath);
		for (AnalysedModuleDTO am : analysedSubModuleDTOs){
			if(am.type.toLowerCase().equals("package")){
				List<String> subPaths = getAllChildPhysicalPaths(am.uniqueName);
				paths.addAll(subPaths);
			}
			paths.add(am.uniqueName);
		}
		return paths;
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

	private static List<Mapping> getInnerClasses(AnalysedModuleDTO analysedClass, List<Mapping> innerClassPaths, ModuleDTO module, String[] violationTypeKeys){
		for(AnalysedModuleDTO innerClass : analysedClass.subModules){
			innerClassPaths.add(new Mapping(module.logicalPath, module.type, innerClass.uniqueName, violationTypeKeys));			
			innerClassPaths.addAll(getInnerClasses(analysedClass, innerClassPaths, module, violationTypeKeys));
		}		
		return innerClassPaths;
	}
}