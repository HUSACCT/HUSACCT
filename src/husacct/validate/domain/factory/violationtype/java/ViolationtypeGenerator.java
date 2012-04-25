package husacct.validate.domain.factory.violationtype.java;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;

class ViolationtypeGenerator {	
	private Logger logger = Logger.getLogger(ViolationtypeGenerator.class);

	List<String> getAllViolationTypeKeys(String packagename){
		Map<String, String> classes = getClasses(packagename);
		ArrayList<String> violationKeys = new ArrayList<String>();
		for(String violationkey : classes.keySet()){
			violationKeys.add(violationkey);
		}
		return violationKeys;
	}

	Map<String, String> getAllViolationTypesWithCategory(String packageName){
		return getClasses(packageName);
	}

	private Map<String, String> getClasses(String packageName) {
		try {			
			Map<String, String> keyList = new HashMap<String, String>();
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			assert classLoader != null;
			String path = packageName.replace('.', '/');
			Enumeration<URL> resources = classLoader.getResources(path);
			List<String> dirs = new ArrayList<String>();
			while (resources.hasMoreElements()) {
				URL resource = resources.nextElement();
				dirs.add(resource.getFile());
			}
			TreeSet<String> classes = new TreeSet<String>();
			for (String directory : dirs) {
				classes.addAll(findClasses(directory, packageName));
			}

			for (String clazz : classes) {
				Class<?> scannedClass = Class.forName(clazz);
				if(scannedClass.isEnum()){	
					Class<?>[] interfaces = scannedClass.getInterfaces();

					for(Class<?> violationInterface : interfaces){
						if(violationInterface.getSimpleName().equals("IViolationType")){						

							for(Object enumValue : scannedClass.getEnumConstants()){

								Class<?> enumClass = enumValue.getClass();
								try {
									Method getCategoryMethod = enumClass.getDeclaredMethod("getCategory");
									String category = (String) getCategoryMethod.invoke(enumValue);
									if(!keyList.containsKey(enumValue.toString())){									
										keyList.put(enumValue.toString(), category);									

									}								
									else{
										logger.warn(String.format("ViolationTypeKey: %s already exists", enumValue.toString()));
									}									
								} catch (SecurityException e) {
									e.printStackTrace();
								} catch (NoSuchMethodException e) {
									e.printStackTrace();
								} catch (IllegalArgumentException e) {
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									e.printStackTrace();
								}								
							}
						}
					}
				}
			}
			return keyList;
		}
		catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
		}
		return Collections.emptyMap();
	}

	private TreeSet<String> findClasses(String directory, String packageName) throws IOException{
		TreeSet<String> classes = new TreeSet<String>();
		if (directory.startsWith("file:") && directory.contains("!")) {
			String [] split = directory.split("!");
			URL jar = new URL(split[0]);
			ZipInputStream zip = new ZipInputStream(jar.openStream());
			ZipEntry entry = null;
			while ((entry = zip.getNextEntry()) != null) {
				if (entry.getName().endsWith(".class")) {
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
				classes.addAll(findClasses(file.getAbsolutePath(), packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
			}
		}
		return classes;
	}
}