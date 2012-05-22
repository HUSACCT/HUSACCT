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
		if(!emptyModule(rule.moduleFrom)){
			mappingFrom = getAllClasspathsFromModule(rule.moduleFrom, rule.violationTypeKeys);
		}
		else{
			mappingFrom = new ArrayList<Mapping>();
		}
		if(!emptyModule(rule.moduleTo)){
			mappingTo = getAllClasspathsFromModule(rule.moduleTo, rule.violationTypeKeys);
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

	private static ArrayList<Mapping> getAllClasspathsFromModule(ModuleDTO rootModule, String[] violationTypeKeys){
		HashSet<Mapping> classpathsFrom = new HashSet<Mapping>();		

		for(PhysicalPathDTO classpath : rootModule.physicalPathDTOs){
			if(classpath.type.toLowerCase().equals("package")){
				AnalysedModuleDTO[] analysedModules = analyse.getChildModulesInModule(classpath.path);
				for(AnalysedModuleDTO analysedClass : analysedModules){
					classpathsFrom.add(new Mapping(rootModule.logicalPath, rootModule.type, analysedClass.uniqueName, violationTypeKeys));
					//check for innerclasses
				}
			}
			else{
				classpathsFrom.add(new Mapping(rootModule.logicalPath, rootModule.type, classpath.path, violationTypeKeys));
				//class check innerclasses
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
						classpaths.add(new Mapping(subModule.logicalPath, subModule.type, analysedClass.uniqueName, violationTypeKeys));
						//check for innerclasses
					}
				}
				else{
					classpaths.add(new Mapping(subModule.logicalPath, subModule.type, classpath.path, violationTypeKeys));
					//class check innerclasses
				}
			}

			classpaths.addAll(getAllClasspathsFromModule(subModule, classpaths, violationTypeKeys));
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

					for(String violationTypeKey : mainClasspaths.get(i).getViolationTypes()){
						if(Arrays.asList(exceptionMapping.getViolationTypes()).contains(violationTypeKey)){
							mainClasspaths.get(i).removeViolationType(violationTypeKey);
						}
					}

					//if there are no violationtypes attached to this class you can remove this class for better validate performance
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