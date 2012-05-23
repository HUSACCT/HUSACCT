package husacct.validate.domain.check;

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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CheckConformanceUtilFilter {
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

		return removeExceptionClasses(mainClasspaths, exceptionClasspathFrom, exceptionClasspathTo);
	}

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
		return removeExceptionClasses(mainClasspaths, exceptionClasspathFrom, exceptionClasspathTo);
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
		for(PhysicalPathDTO classpath : rootModule.physicalPathDTOs){
			if(classpath.type.toLowerCase().equals("package")){
				classpathsFrom.add(new Mapping(rootModule.logicalPath, rootModule.type, classpath.path, violationTypeKeys));					
			}
		}
		getAllPackagepathsFromModule(rootModule, classpathsFrom, violationTypeKeys);
		return new ArrayList<Mapping>(classpathsFrom);
	}

	private static ArrayList<Mapping> getAllPackagepathsFromModule(ModuleDTO module, HashSet<Mapping> classpaths, String[] violationTypeKeys){
		for(ModuleDTO subModule : module.subModules){
			for(PhysicalPathDTO classpath : module.physicalPathDTOs){
				if(classpath.type.toLowerCase().equals("package")){
					classpaths.add(new Mapping(module.logicalPath, module.type, classpath.path, violationTypeKeys));					
				}
			}
			getAllPackagepathsFromModule(subModule, classpaths, violationTypeKeys);
		}
		return new ArrayList<Mapping>(classpaths);
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

		for(PhysicalPathDTO classpath : rootModule.physicalPathDTOs){
			if(classpath.type.toLowerCase().equals("package")){
				AnalysedModuleDTO[] analysedModules = analyse.getChildModulesInModule(classpath.path);
				for(AnalysedModuleDTO analysedClass : analysedModules){
					classpathsFrom.add(new Mapping(rootModule.logicalPath, rootModule.type, analysedClass.uniqueName, violationTypeKeys));
					classpathsFrom.addAll(getInnerClasses(analysedClass, new ArrayList<Mapping>(), rootModule, violationTypeKeys));
				}
			}
			else{
				classpathsFrom.add(new Mapping(rootModule.logicalPath, rootModule.type, classpath.path, violationTypeKeys));
				AnalysedModuleDTO analysedModule = analyse.getModuleForUniqueName(classpath.path);
				classpathsFrom.addAll(getInnerClasses(analysedModule, new ArrayList<Mapping>(), rootModule, violationTypeKeys));
			}
		}
		getAllClasspathsFromModule(rootModule, classpathsFrom, violationTypeKeys);	
		return new ArrayList<Mapping>(classpathsFrom);
	}

	private static HashSet<Mapping> getAllClasspathsFromModule(ModuleDTO module, HashSet<Mapping> classpaths, String[] violationTypeKeys){
		for(ModuleDTO subModule : module.subModules){

			for(PhysicalPathDTO classpath : subModule.physicalPathDTOs){
				if(classpath.type.toLowerCase().equals("package")){
					AnalysedModuleDTO[] analysedModules = analyse.getChildModulesInModule(classpath.path);

					for(AnalysedModuleDTO analysedClass : analysedModules){
						if(analysedClass.type.toLowerCase().equals("class")){
							classpaths.add(new Mapping(subModule.logicalPath, subModule.type, analysedClass.uniqueName, violationTypeKeys));
							classpaths.addAll(getInnerClasses(analysedClass, new ArrayList<Mapping>(), subModule, violationTypeKeys));
						}
					}
				}
				else{
					classpaths.add(new Mapping(subModule.logicalPath, subModule.type, classpath.path, violationTypeKeys));
					AnalysedModuleDTO analysedModule = analyse.getModuleForUniqueName(classpath.path);
					classpaths.addAll(getInnerClasses(analysedModule, new ArrayList<Mapping>(), subModule, violationTypeKeys));
				}
			}

			classpaths.addAll(getAllClasspathsFromModule(subModule, classpaths, violationTypeKeys));
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

	private static Mappings removeExceptionClasses(Mappings mainClasspaths, List<Mapping> exceptionClasspathsFrom, List<Mapping> exceptionClasspathsTo){
		List<Mapping> filteredFrom = removeExceptionClasses(mainClasspaths.getMappingFrom(), exceptionClasspathsFrom);
		List<Mapping> filteredTo = removeExceptionClasses(mainClasspaths.getMappingTo(), exceptionClasspathsTo);
		return new Mappings(filteredFrom, filteredTo);
	}

	private static List<Mapping> removeExceptionClasses(List<Mapping> mainClasspaths, List<Mapping> exceptionClasspaths){
		for(int i = 0; i<mainClasspaths.size(); i++){
			for(Mapping exceptionMapping : exceptionClasspaths){
				if(mainClasspaths.get(i).getPhysicalPath().equals(exceptionMapping.getPhysicalPath())){

					for(String violationTypeKey : mainClasspaths.get(i).getViolationTypes()){
						if(Arrays.asList(exceptionMapping.getViolationTypes()).contains(violationTypeKey)){
							mainClasspaths.get(i).removeViolationType(violationTypeKey);
						}
					}

					//if there are no violationtypes attached to this class you can remove this class for better checkconformace performance
					if(mainClasspaths.get(i).getViolationTypes().length == 0){
						mainClasspaths.remove(i);
						i--;
					}
				}
			}
		}
		return mainClasspaths;
	}
}