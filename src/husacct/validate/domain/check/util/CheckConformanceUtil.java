package husacct.validate.domain.check.util;

import husacct.validate.domain.validation.iternal_tranfer_objects.Mapping;
import husacct.validate.domain.validation.iternal_tranfer_objects.Mappings;

import java.util.Arrays;
import java.util.List;

public abstract class CheckConformanceUtil {
	protected static Mappings removeExceptionPaths(Mappings mainClasspaths, List<Mapping> exceptionClasspathsFrom, List<Mapping> exceptionClasspathsTo){
		List<Mapping> filteredFrom = removeExceptionPaths(mainClasspaths.getMappingFrom(), exceptionClasspathsFrom);
		List<Mapping> filteredTo = removeExceptionPaths(mainClasspaths.getMappingTo(), exceptionClasspathsTo);
		return new Mappings(filteredFrom, filteredTo);
	}	

	private static List<Mapping> removeExceptionPaths(List<Mapping> mainClasspaths, List<Mapping> exceptionClasspaths){
		for(int iterator = 0; iterator<mainClasspaths.size(); iterator++){
			for(Mapping exceptionMapping : exceptionClasspaths){
				if(mainClasspaths.get(iterator).getPhysicalPath().equals(exceptionMapping.getPhysicalPath())){

					for(String violationTypeKey : mainClasspaths.get(iterator).getViolationTypes()){
						if(Arrays.asList(exceptionMapping.getViolationTypes()).contains(violationTypeKey)){
							mainClasspaths.get(iterator).removeViolationType(violationTypeKey);
						}
					}

					//if there are no violationtypes attached to this class you can remove this class for better checkconformace performance
					if(mainClasspaths.get(iterator).getViolationTypes().length == 0){
						mainClasspaths.remove(iterator);
						iterator--;
					}
				}
			}
		}
		return mainClasspaths;
	}
}