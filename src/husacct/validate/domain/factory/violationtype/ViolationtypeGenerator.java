package husacct.validate.domain.factory.violationtype;

import husacct.validate.domain.validation.DefaultSeverities;
import husacct.validate.domain.validation.internal_transfer_objects.CategoryKeySeverityDTO;
import husacct.validate.domain.validation.violationtype.IViolationType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

class ViolationtypeGenerator {	
	private Logger logger = Logger.getLogger(ViolationtypeGenerator.class);

	List<CategoryKeySeverityDTO> getAllViolationTypes(List<IViolationType> violationtypes) {
		return new ArrayList<CategoryKeySeverityDTO>(getClasses(violationtypes));		
	}

	private Set<CategoryKeySeverityDTO> getClasses(List<IViolationType> violationtypes) {
		Set<CategoryKeySeverityDTO> keyList = new HashSet<CategoryKeySeverityDTO>();

		Set<Class<?>> scannedClasses = new HashSet<Class<?>>();
		for(IViolationType type : violationtypes) {
			Class<?> scannedClass = type.getClass();
			if(scannedClass.isEnum() && hasIViolationTypeInterface(scannedClass)) {
				scannedClasses.add(scannedClass);
			}
		}		
		keyList.addAll(generateViolationTypes(scannedClasses));
		return keyList;
	}

	private boolean hasIViolationTypeInterface(Class<?> scannedClass) {
		Class<?>[] interfaces = scannedClass.getInterfaces();
		for(Class<?> violationInterface : interfaces){
			if(violationInterface.getSimpleName().equals("IViolationType")) {
				return true;
			}
		}		
		return false;		
	}

	private Set<CategoryKeySeverityDTO> generateViolationTypes(Set<Class<?>> scannedClasses) {
		Set<CategoryKeySeverityDTO> keyList = new HashSet<CategoryKeySeverityDTO>();

		Iterator<Class<?>> it = scannedClasses.iterator();
		while(it.hasNext()) {
			Class<?> scannedClass = it.next();
			for(Object enumValue : scannedClass.getEnumConstants()) {
				Class<?> enumClass = enumValue.getClass();
				try {
					Method getCategoryMethod = enumClass.getDeclaredMethod("getCategory");
					String category = (String) getCategoryMethod.invoke(enumValue);

					Method getDefaultSeverityMethod = enumClass.getDeclaredMethod("getDefaultSeverity");
					DefaultSeverities defaultSeverity = (DefaultSeverities) getDefaultSeverityMethod.invoke(enumValue);

					if(!containsViolationTypeInSet(keyList, enumValue.toString())) {									
						keyList.add(new CategoryKeySeverityDTO(enumValue.toString(), category, defaultSeverity));
					}								
					else {
						logger.warn(String.format("ViolationTypeKey: %s already exists", enumValue.toString()));
					}									
				} 
				catch (SecurityException e) {
					logger.error(e.getMessage(), e);
				} 
				catch (NoSuchMethodException e) {
					logger.error(e.getMessage(), e);
				} 
				catch (IllegalArgumentException e) {
					logger.error(e.getMessage(), e);
				} 
				catch (IllegalAccessException e) {
					logger.error(e.getMessage(), e);
				}
				catch (InvocationTargetException e) {
					logger.error(e.getMessage(), e);
				}								
			}
		}
		return keyList;
	}

	private boolean containsViolationTypeInSet(Set<CategoryKeySeverityDTO> keyList, String key){
		for(CategoryKeySeverityDTO dto : keyList){
			if(dto.getKey().toLowerCase().equals(key.toLowerCase())){
				return true;
			}
		}
		return false;
	}
}