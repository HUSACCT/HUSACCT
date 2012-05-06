package husaccttest.analyse;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;

public abstract class TestCaseExtended extends TestCase{
	
	public boolean itemExistInArray(Object value, Object[] items){
		for(Object o : items){
			if(o.toString().equals(value.toString())){
				return true;
			}
		}
		return false;
	}
	
	public HashMap<String, Object> createDependencyHashmap(String from, String to, String type, int linenumber){
		HashMap<String, Object> dependencyHashMap = new HashMap<String, Object>();
		
		dependencyHashMap.put("from", from);
		dependencyHashMap.put("to", to);
		dependencyHashMap.put("type", type);
		dependencyHashMap.put("lineNumber", linenumber);
		
		return dependencyHashMap;
	}
	
	public HashMap<String, Object> createModuleHashmap(String name, String uniqueName, int totalSubmodules, String type){
		HashMap<String, Object> moduleHashMap = new HashMap<String, Object>();
			
		moduleHashMap.put("name", name);
		moduleHashMap.put("uniqueName", uniqueName);
		moduleHashMap.put("subModules", totalSubmodules);
		moduleHashMap.put("type", type);
		
		return moduleHashMap;
	}
	
	public boolean compaireDTOWithValues(Object o, Object[] allDependencies){

		@SuppressWarnings("unchecked")
		HashMap<String, Object> findingProperties = (HashMap<String, Object>) o;
		
		dependencyloop : for(Object currentDependency : allDependencies){
			for(String currentKey : findingProperties.keySet()){
								
				try {
					Class<? extends Object> objectPropertyClass = currentDependency.getClass();
					Field objectPropertyField = objectPropertyClass.getDeclaredField(currentKey);
														
					Object objectPropertyFieldValueObject = (Object) objectPropertyField.get(currentDependency);
					Object objectPropertyFieldValue;
					
					if(objectPropertyField.getType().getSimpleName().matches("List|Array")){
						if(objectPropertyFieldValueObject == null){
							objectPropertyFieldValue = (Integer) 0;
						} else {
								objectPropertyFieldValue = ((List<?>) objectPropertyFieldValueObject).size();	
						}
					} else {
						objectPropertyFieldValue = objectPropertyFieldValueObject.toString();
					}
					Object checkingObject = (Object) findingProperties.get(currentKey);
					Object checkingObjectValue = checkingObject.toString();
					
					if(!objectPropertyFieldValue.toString().equals(checkingObjectValue.toString())){
						continue dependencyloop;
					}
					
				} catch (Exception e) {
					return false;
				}

			}
			return true;
		}		
		return false;
	}	
	
	
}
