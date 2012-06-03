package husacct.validate.domain.check.util;

import husacct.validate.domain.validation.iternal_tranfer_objects.Mapping;
import husacct.validate.domain.validation.iternal_tranfer_objects.Mappings;

import java.util.List;

public abstract class CheckConformanceUtil {
	protected static Mappings removeExceptionPaths(Mappings mainClasspaths, List<Mapping> exceptionClasspathsFrom, List<Mapping> exceptionClasspathsTo){
		List<Mapping> filteredFrom = removeExceptionPaths(mainClasspaths.getMappingFrom(), exceptionClasspathsFrom);
		List<Mapping> filteredTo = removeExceptionPaths(mainClasspaths.getMappingTo(), exceptionClasspathsTo);
		return new Mappings(filteredFrom, filteredTo);
	}	

	protected static Mappings removeExceptionPathsFrom(Mappings mainClasspaths, List<Mapping> exceptionClasspathsFrom, List<Mapping> exceptionClasspathsTo){
		List<Mapping> filteredFrom = removeExceptionPaths(mainClasspaths.getMappingFrom(), exceptionClasspathsFrom);
		return new Mappings(filteredFrom, mainClasspaths.getMappingTo());
	}

	protected static Mappings removeExceptionPathsTo(Mappings mainClasspaths, List<Mapping> exceptionClasspathsFrom, List<Mapping> exceptionClasspathsTo){
		List<Mapping> filteredTo = removeExceptionPaths(mainClasspaths.getMappingTo(), exceptionClasspathsTo);
		return new Mappings(mainClasspaths.getMappingFrom(), filteredTo);
	}

	private static List<Mapping> removeExceptionPaths(List<Mapping> mainClasspaths, List<Mapping> exceptionClasspaths){
		outerloop:for(int iterator = 0; iterator<mainClasspaths.size(); iterator++){
			for(Mapping exceptionMapping : exceptionClasspaths){
				if(mainClasspaths.get(iterator).getPhysicalPath().equals(exceptionMapping.getPhysicalPath())){

					for(String violationTypeKey : mainClasspaths.get(iterator).getViolationTypes()){
						if(containsValueInArray(exceptionMapping.getViolationTypes(), violationTypeKey)){
							mainClasspaths.get(iterator).removeViolationType(violationTypeKey);
						}
					}

					//if there are no violationtypes attached to this class you can remove this class for better checkconformace performance
					if(mainClasspaths.get(iterator).getViolationTypes().length == 0){
						mainClasspaths.remove(iterator);
						iterator--;
						if(iterator < 0){
							iterator = 0;
							if(mainClasspaths.size() == 0){
								break outerloop;
							}
						}
					}
				}
			}
		}
	return mainClasspaths;
	}

	private static boolean containsValueInArray(String[] keys, String key){
		for(String loopKey : keys){
			if(loopKey.toLowerCase().equals(key.toLowerCase())){
				return true;
			}
		}
		return false;
	}
}