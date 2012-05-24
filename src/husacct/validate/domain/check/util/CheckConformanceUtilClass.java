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
import java.util.Set;

public class CheckConformanceUtilClass extends CheckConformanceUtil {
	private static IDefineService define = ServiceProvider.getInstance().getDefineService();
	private static IAnalyseService analyse = ServiceProvider.getInstance().getAnalyseService();

	public static ArrayList<Mapping> getAllModulesFromLayer(ModuleDTO layerModule, String [] violationTypeKeys){
		HashSet<Mapping> classpathsFrom = new HashSet<Mapping>();
		ModuleDTO[] childModules = define.getChildrenFromModule(layerModule.logicalPath);
		if(childModules.length != 0){
			for(ModuleDTO module : childModules){
				classpathsFrom.addAll(getAllClasspathsFromModule(module, violationTypeKeys));
				classpathsFrom.addAll(getAllModulesFromLayer(module,classpathsFrom, violationTypeKeys));
			}			
		}
		return new ArrayList<Mapping>(classpathsFrom);
	}

	private static Set<Mapping> getAllModulesFromLayer(ModuleDTO layerModule, HashSet<Mapping> classpaths, String [] violationTypeKeys){
		ModuleDTO[] childModules = define.getChildrenFromModule(layerModule.logicalPath);
		if(childModules.length != 0){
			for(ModuleDTO module : childModules){
				classpaths.addAll(getAllClasspathsFromModule(module, violationTypeKeys));
				return getAllModulesFromLayer(module, classpaths, violationTypeKeys);
			}
		}
		return classpaths;
	}

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

	private static ArrayList<Mapping> getAllClasspathsFromModule(ModuleDTO rootModule, String[] violationTypeKeys){
		HashSet<Mapping> classpathsFrom = new HashSet<Mapping>();		

		classpathsFrom.addAll(getClassFromPhysicalPathDTO(rootModule, violationTypeKeys));

		getAllClasspathsFromModule(rootModule, classpathsFrom, violationTypeKeys);	
		return new ArrayList<Mapping>(classpathsFrom);
	}

	private static HashSet<Mapping> getAllClasspathsFromModule(ModuleDTO module, HashSet<Mapping> classpaths, String[] violationTypeKeys){
		for(ModuleDTO subModule : module.subModules){

			classpaths.addAll(getClassFromPhysicalPathDTO(subModule, violationTypeKeys));

			classpaths.addAll(getAllClasspathsFromModule(subModule, classpaths, violationTypeKeys));
		}
		return classpaths;
	}	

	private static HashSet<Mapping> getClassFromPhysicalPathDTO(ModuleDTO module, String[] violationTypeKeys){
		HashSet<Mapping> classpaths = new HashSet<Mapping>();
		for(PhysicalPathDTO classpath : module.physicalPathDTOs){
			if(!classpath.type.toLowerCase().equals("package")){
				classpaths.add(new Mapping(module.logicalPath, module.type, classpath.path, violationTypeKeys));
				AnalysedModuleDTO analysedModule = analyse.getModuleForUniqueName(classpath.path);
				classpaths.addAll(getInnerClasses(analysedModule, new ArrayList<Mapping>(), module, violationTypeKeys));

			}
		}
		return classpaths;
	}	

	private static List<Mapping> getInnerClasses(AnalysedModuleDTO analysedClass, List<Mapping> innerClassPaths, ModuleDTO module, String[] violationTypeKeys){
		for(AnalysedModuleDTO innerClass : analysedClass.subModules){
			if(analysedClass.type.toLowerCase().equals("class")){
				innerClassPaths.add(new Mapping(module.logicalPath, module.type, innerClass.uniqueName, violationTypeKeys));			
			}
			innerClassPaths.addAll(getInnerClasses(analysedClass, innerClassPaths, module, violationTypeKeys));
		}		
		return innerClassPaths;
	}
}