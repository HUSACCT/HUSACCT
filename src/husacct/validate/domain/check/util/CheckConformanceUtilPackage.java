package husacct.validate.domain.check.util;

import husacct.common.dto.ModuleDTO;
import husacct.common.dto.PhysicalPathDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.validation.iternal_tranfer_objects.Mapping;
import husacct.validate.domain.validation.iternal_tranfer_objects.Mappings;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CheckConformanceUtilPackage extends CheckConformanceUtil{
	
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
		classpathsFrom.addAll(getPackageFromPhysicalPathDTO(rootModule, violationTypeKeys));
		getAllPackagepathsFromModule(rootModule, classpathsFrom, violationTypeKeys);
		return new ArrayList<Mapping>(classpathsFrom);
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
				classpaths.add(new Mapping(module.logicalPath, module.type, physicalClasspath.path, violationTypeKeys));					
			}
		}
		return classpaths;
	}
}