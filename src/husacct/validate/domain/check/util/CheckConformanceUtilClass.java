package husacct.validate.domain.check.util;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.PhysicalPathDTO;
import husacct.common.dto.RuleDTO;
import husacct.define.IDefineService;
import husacct.validate.domain.validation.iternal_tranfer_objects.Mapping;
import husacct.validate.domain.validation.iternal_tranfer_objects.Mappings;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CheckConformanceUtilClass extends CheckConformanceUtil {
	private static IDefineService define = ServiceProvider.getInstance().getDefineService();
	private static IAnalyseService analyse = ServiceProvider.getInstance().getAnalyseService();

	public static Mappings filterClasses(RuleDTO rule){
		Mappings mainClasspaths = getAllClasspathsFromModule(rule);
		List<Mapping> exceptionClasspathFrom = new ArrayList<Mapping>();
		List<Mapping> exceptionClasspathTo = new ArrayList<Mapping>();

		if(rule.exceptionRules!= null){
			for(RuleDTO exceptionRule : rule.exceptionRules){
				Mappings exceptionClasspaths = getAllClasspathsFromModule(exceptionRule);
				exceptionClasspathFrom.addAll(exceptionClasspaths.getMappingFrom());
				exceptionClasspathTo.addAll(exceptionClasspaths.getMappingTo());
			}
		}
		return removeExceptionPaths(mainClasspaths, exceptionClasspathFrom, exceptionClasspathTo);
	}

	private static Mappings getAllClasspathsFromModule(RuleDTO rule){		
		ArrayList<Mapping> mappingFrom = new ArrayList<Mapping>();
		ArrayList<Mapping> mappingTo = new ArrayList<Mapping>();

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
		for(PhysicalPathDTO classpath : module.physicalPathDTOs){
			if(!classpath.type.toLowerCase().equals("package")){
				if(!updateLogicalPaths(classpaths, module, classpath.path, violationTypeKeys)){
					classpaths.add(new Mapping(module.logicalPath, module.type, classpath.path, violationTypeKeys));
				}

				AnalysedModuleDTO analysedModule = analyse.getModuleForUniqueName(classpath.path);
				classpaths.addAll(getInnerClasses(analysedModule, new ArrayList<Mapping>(), module, violationTypeKeys));
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

	private static List<Mapping> getInnerClasses(AnalysedModuleDTO analysedClass, List<Mapping> innerClassPaths, ModuleDTO module, String[] violationTypeKeys){
		for(AnalysedModuleDTO innerClass : analysedClass.subModules){
			if(!analysedClass.type.toLowerCase().equals("package")){
				innerClassPaths.add(new Mapping(module.logicalPath, module.type, innerClass.uniqueName, violationTypeKeys));			
			}
			innerClassPaths.addAll(getInnerClasses(analysedClass, innerClassPaths, module, violationTypeKeys));
		}		
		return innerClassPaths;
	}
}