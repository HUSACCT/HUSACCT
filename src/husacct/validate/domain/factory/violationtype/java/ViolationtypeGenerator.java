package husacct.validate.domain.factory.violationtype.java;

import husacct.validate.domain.validation.DefaultSeverities;
import husacct.validate.domain.validation.iternal_tranfer_objects.CategoryKeySeverityDTO;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;

class ViolationtypeGenerator {	
	private Logger logger = Logger.getLogger(ViolationtypeGenerator.class);
	private static final String violationTypeInterfaceLocation = "husacct.validate.domain.validation.violationtype.IViolationType"; 

	List<CategoryKeySeverityDTO> getAllViolationTypes(String parentPackage){
		return new ArrayList<CategoryKeySeverityDTO>(getClasses(parentPackage));		
	}

	private Set<CategoryKeySeverityDTO> getClasses(String parentPackage) {
		Set<CategoryKeySeverityDTO> keyList = new HashSet<CategoryKeySeverityDTO>();
		try {
			TreeSet<String> classes = new TreeSet<String>();
			List<String> directories = getDirectories(parentPackage);
			for (String directory : directories) {
				classes.addAll(findClasses(directory, violationTypeInterfaceLocation.replace("IViolationType", "")+ parentPackage, parentPackage));
			}
			for (String clazz : classes) {
				Class<?> scannedClass = Class.forName(clazz);
				if(scannedClass.isEnum() && hasIViolationTypeInterface(scannedClass)){
					keyList.addAll(generateViolationTypes(scannedClass));
				}
			}
			return keyList;
		}
		catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
		}
		return keyList;
	}
	
	private List<String> getDirectories(String parentPackage){
		List<String> directories = new ArrayList<String>();
		try{			
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			assert classLoader != null;
			String path = violationTypeInterfaceLocation.replace('.', '/') + ".class";
			Enumeration<URL> resources = classLoader.getResources(path);			
			while (resources.hasMoreElements()) {
				URL resource = resources.nextElement();
				directories.add(resource.getFile().replace("/IViolationType.class", "") + "/" + parentPackage);				
			}
			return  directories;
		}catch(IOException e){

		}
		return directories;	
	}
	
	private TreeSet<String> findClasses(String directory, String packageName, String parentPackage) throws IOException{
		TreeSet<String> classes = new TreeSet<String>();
		if (directory.startsWith("file:") && directory.contains("!")) {
			String [] split = directory.split("!");
			URL jar = new URL(split[0]);
			ZipInputStream zip = new ZipInputStream(jar.openStream());
			ZipEntry entry = null;
			while ((entry = zip.getNextEntry()) != null) {
				if (entry.getName().startsWith("husacct/validate/domain/validation/violationtype/" + parentPackage) && entry.getName().endsWith(".class")) {
					String className = entry.getName().replaceAll("[$].*", "").replaceAll("[.]class", "").replace('/', '.');
					classes.add(className);
				}
			}
		}
		File dir = new File(URLDecoder.decode(directory, "UTF-8"));
		if (!dir.exists()) {
			return classes;
		}
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file.getAbsolutePath(), packageName + "." + file.getName(), parentPackage));
			} else if (file.getName().endsWith(".class")) {
				classes.add(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
			}
		}
		return classes;
	}

	private boolean hasIViolationTypeInterface(Class<?> scannedClass){
		Class<?>[] interfaces = scannedClass.getInterfaces();
		for(Class<?> violationInterface : interfaces){
			if(violationInterface.getSimpleName().equals("IViolationType")){
				return true;
			}
		}		
		return false;		
	}

	private Set<CategoryKeySeverityDTO> generateViolationTypes(Class<?> scannedClass){
		Set<CategoryKeySeverityDTO> keyList = new HashSet<CategoryKeySeverityDTO>();

		for(Object enumValue : scannedClass.getEnumConstants()){
			Class<?> enumClass = enumValue.getClass();
			try {
				Method getCategoryMethod = enumClass.getDeclaredMethod("getCategory");
				String category = (String) getCategoryMethod.invoke(enumValue);
				
				Method getDefaultSeverityMethod = enumClass.getDeclaredMethod("getDefaultSeverity");
				DefaultSeverities defaultSeverity = (DefaultSeverities) getDefaultSeverityMethod.invoke(enumValue);
				
				if(!containsViolationTypeInSet(keyList, enumValue.toString())){									
					keyList.add(new CategoryKeySeverityDTO(enumValue.toString(), category, defaultSeverity));
				}								
				else{
					logger.warn(String.format("ViolationTypeKey: %s already exists", enumValue.toString()));
				}									
			} catch (SecurityException e) {
				logger.error(e.getMessage(), e);
			} catch (NoSuchMethodException e) {
				logger.error(e.getMessage(), e);
			} catch (IllegalArgumentException e) {
				logger.error(e.getMessage(), e);
			} catch (IllegalAccessException e) {
				logger.error(e.getMessage(), e);
			} catch (InvocationTargetException e) {
				logger.error(e.getMessage(), e);
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