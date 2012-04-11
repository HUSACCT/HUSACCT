package husacct.validate.domain.factory.violationtypeutil;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ViolationtypeKeys {
	private Set<String> getClasses(String packageName) {
		try {
			Set<String> keyList = new HashSet<String>();
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
					for(Object enumValue : Arrays.asList(scannedClass.getEnumConstants())){
						if(!keyList.contains(enumValue.toString())){
							keyList.add(enumValue.toString());
						}
						else{
							System.out.println("WARNING: key already exists");
						}
					}
				}
			}
			return keyList;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.emptySet();
	}

	private TreeSet<String> findClasses(String directory, String packageName) throws Exception {
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

	public List<String> getAllViolationTypeKeys(String packagename){
		return new ArrayList<String>(getClasses(packagename));
	}
}