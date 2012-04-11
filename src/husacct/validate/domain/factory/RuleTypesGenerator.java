package husacct.validate.domain.factory;

import husacct.validate.domain.factory.violationtypeutil.CategorykeyClassDTO;
import husacct.validate.domain.ruletype.Rule;
import husacct.validate.domain.ruletype.RuleTypes;
import java.io.File;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class RuleTypesGenerator {
	private static final String rootFolderRules = "husacct.validate.domain.ruletype";

	private List<Class<?>> getClasses(String packageName) {
		try {
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

			ArrayList<Class<?>> classList = new ArrayList<Class<?>>();
			for (String clazz : classes) {
					Class<?> ruleClass = Class.forName(clazz);
				if(!Modifier.isAbstract(ruleClass.getModifiers())){
					classList.add(Class.forName(clazz));
				}
			}
			return classList;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
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

	public HashMap<String, CategorykeyClassDTO> generateRules(EnumSet<RuleTypes> rules){
		HashMap<String, CategorykeyClassDTO> keyClasses = new HashMap<String, CategorykeyClassDTO>();
		HashMap<String, CategorykeyClassDTO> allClasses = generateAllRules();
		for(Enum<RuleTypes> ruleKey : rules){
			CategorykeyClassDTO ruleCategory = allClasses.get(ruleKey.toString());
			if(ruleCategory != null){
				keyClasses.put(ruleKey.toString(), ruleCategory);
			}
			else{
				System.out.println("RuleKey " + ruleKey.toString() + " not found");
			}
		}
		return keyClasses;
	}

	public HashMap<String, CategorykeyClassDTO> generateAllRules(){
		HashMap<String, CategorykeyClassDTO> keyClasses = new HashMap<String, CategorykeyClassDTO>();
		List<Class<?>> ruleClasses = getRuleClasses();
		for(Class<?> ruleClass : ruleClasses){
			if(isInstanceOfRule(ruleClass)){
				final String categoryKey = getCategoryKey(ruleClass);
				final String ruleKey = getRuleKey(ruleClass);
				keyClasses.put(ruleKey, new CategorykeyClassDTO(categoryKey, (Class<Rule>) ruleClass));
			}
		}
		return keyClasses;
	}

	private boolean isInstanceOfRule(Class<?> ruleClass){
		return ruleClass.getSimpleName().matches("^(.+Rule*)$") && ruleClass.getSuperclass().getName().equals("husacct.validate.domain.ruletype.Rule");
	}

	private List<Class<?>> getRuleClasses(){
		return getClasses(rootFolderRules);
	}

	private String getRuleKey(Class<?> ruleClass){
		return ruleClass.getSimpleName().replace("Rule", "");
	}

	private String getCategoryKey(Class<?> ruleClass){
		try{
			String[] splittedPackageName = ruleClass.getPackage().getName().split("\\.");
			return splittedPackageName[splittedPackageName.length-1];
		}catch(ArrayIndexOutOfBoundsException e){
			return "";
		}
	}
}