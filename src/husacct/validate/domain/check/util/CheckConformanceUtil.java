package husacct.validate.domain.check.util;

import husacct.validate.domain.validation.internaltransferobjects.Mapping;
import java.util.HashMap;
import java.util.List;

public abstract class CheckConformanceUtil {

	protected static List<Mapping> removeExceptionRulePathsFromMainRulePaths(List<Mapping> mainRuleClasspaths, List<Mapping> exceptionRuleClasspaths) {
		HashMap<String, Mapping> mainRuleMappingHashMap= new HashMap<String, Mapping>(); 
		for (Mapping mainRuleMapping : mainRuleClasspaths) {
			mainRuleMappingHashMap.put(mainRuleMapping.getPhysicalPath(), mainRuleMapping);
		}
		for (Mapping exceptionRuleMapping : exceptionRuleClasspaths) {
			if (mainRuleMappingHashMap.containsKey(exceptionRuleMapping.getPhysicalPath())) {
				Mapping toBeRemoved = mainRuleMappingHashMap.get(exceptionRuleMapping.getPhysicalPath());
				mainRuleClasspaths.remove(toBeRemoved);
			}
		}
		return mainRuleClasspaths;
		
	}

	private static boolean containsValueInArray(String[] keys, String key) {
		for (String loopKey : keys) {
			if (loopKey.toLowerCase().equals(key.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
}