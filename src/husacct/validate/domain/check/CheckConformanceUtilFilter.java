package husacct.validate.domain.check;

import husacct.ServiceProvider;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.RuleDTO;
import husacct.define.IDefineService;
import husacct.validate.domain.validation.iternal_tranfer_objects.Mapping;
import husacct.validate.domain.validation.iternal_tranfer_objects.Mappings;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CheckConformanceUtilFilter {
	private static IDefineService defineService = ServiceProvider.getInstance().getDefineService();

	public static ArrayList<Mapping> getAllModulesFromLayer(ModuleDTO layerModule){
		HashSet<Mapping> classpathsFrom = new HashSet<Mapping>();
		ModuleDTO[] childModules = defineService.getChildrenFromModule(layerModule.logicalPath);
		if(childModules.length != 0){
			for(ModuleDTO module : childModules){
				classpathsFrom.addAll(getAllClasspathsFromModule(module));
				classpathsFrom.addAll(getAllModulesFromLayer(module,classpathsFrom));
			}			
		}
		return new ArrayList<Mapping>(classpathsFrom);
	}

	private static Set<Mapping> getAllModulesFromLayer(ModuleDTO layerModule, HashSet<Mapping> classpaths){
		ModuleDTO[] childModules = defineService.getChildrenFromModule(layerModule.logicalPath);
		if(childModules.length != 0){
			for(ModuleDTO module : childModules){
				classpaths.addAll(getAllClasspathsFromModule(module));
				return getAllModulesFromLayer(module,classpaths);
			}
		}
		return classpaths;
	}

	public static Mappings filter(RuleDTO rule){
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

		return removeExceptionClasses(mainClasspaths, exceptionClasspathFrom, exceptionClasspathTo);
	}


	private static Mappings getAllClasspathsFromModule(RuleDTO rule){		
		ArrayList<Mapping> mappingFrom;
		ArrayList<Mapping> mappingTo;
		if(emptyModule(rule.moduleFrom)== false){
			mappingFrom = getAllClasspathsFromModule(rule.moduleFrom);
		}
		else{
			mappingFrom = new ArrayList<Mapping>();
		}
		if(emptyModule(rule.moduleTo)== false){
			mappingTo = getAllClasspathsFromModule(rule.moduleTo);
		}
		else{
			mappingTo = new ArrayList<Mapping>();
		}
		return new Mappings(mappingFrom, mappingTo);
	}

	private static boolean emptyModule(ModuleDTO module){
		if(module.type == null || module.type.isEmpty()){
			return true;
		}
		else{
			return false;
		}
	}

	private static ArrayList<Mapping> getAllClasspathsFromModule(ModuleDTO rootModule){
		HashSet<Mapping> classpathsFrom = new HashSet<Mapping>();
		for(String classpath : rootModule.physicalPaths){
			classpathsFrom.add(new Mapping(rootModule.logicalPath, rootModule.type, classpath));
		}
		getAllClasspathsFromModule(rootModule, classpathsFrom);	
		return new ArrayList<Mapping>(classpathsFrom);
	}

	private static HashSet<Mapping> getAllClasspathsFromModule(ModuleDTO module, HashSet<Mapping> classpaths){
		for(ModuleDTO submodule : module.subModules){
			for(String classpath : submodule.physicalPaths){
				classpaths.add(new Mapping(submodule.logicalPath, submodule.type, classpath));
			}
			classpaths.addAll(getAllClasspathsFromModule(submodule, classpaths));
		}
		return classpaths;
	}	

	private static Mappings removeExceptionClasses(Mappings mainClasspaths, List<Mapping> exceptionClasspathsFrom, List<Mapping> exceptionClasspathsTo){
		List<Mapping> filteredFrom = removeExceptionClasses(mainClasspaths.getMappingFrom(), exceptionClasspathsFrom);
		List<Mapping> filteredTo = removeExceptionClasses(mainClasspaths.getMappingTo(), exceptionClasspathsTo);
		return new Mappings(filteredFrom, filteredTo);
	}

	private static List<Mapping> removeExceptionClasses(List<Mapping> mainClasspaths, List<Mapping> exceptionClasspaths){
		for(int i = 0; i<mainClasspaths.size(); i++){
			for(Mapping exceptionMapping : exceptionClasspaths){
				if(mainClasspaths.get(i).getPhysicalPath().equals(exceptionMapping.getPhysicalPath())){
					mainClasspaths.remove(i);
					i--;
				}
			}
		}
		return mainClasspaths;
	}
}