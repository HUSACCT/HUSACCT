package husacct.validate.domain.check;

import husacct.common.dto.ModuleDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.validation.iternal_tranfer_objects.Mapping;
import husacct.validate.domain.validation.iternal_tranfer_objects.Mappings;

import java.util.ArrayList;
import java.util.List;

public class CheckConformanceUtil {
	private static Mappings getAllClasspathsFromModule(RuleDTO rule){		
		ArrayList<Mapping> mappingFrom = getAllClasspathsFromModule(rule.moduleFrom);
		ArrayList<Mapping> mappingTo = getAllClasspathsFromModule(rule.moduleTo);
		return new Mappings(mappingFrom, mappingTo);
	}

	private static ArrayList<Mapping> getAllClasspathsFromModule(ModuleDTO rootModule){
		ArrayList<Mapping> classpathsFrom = new ArrayList<Mapping>();
		for(String classpath : rootModule.physicalPaths){
			classpathsFrom.add(new Mapping(rootModule.logicalPath, rootModule.type, classpath));
		}
		return getAllClasspathsFromModule(rootModule, classpathsFrom);		
	}

	private static ArrayList<Mapping> getAllClasspathsFromModule(ModuleDTO module, ArrayList<Mapping> classpaths){
		for(ModuleDTO submodule : module.subModules){
			for(String classpath : submodule.physicalPaths){
				classpaths.add(new Mapping(submodule.logicalPath, submodule.type, classpath));
			}
			return getAllClasspathsFromModule(submodule, classpaths);
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