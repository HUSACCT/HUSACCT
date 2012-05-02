package husacct.validate.domain.factory.ruletype;

import husacct.validate.domain.exception.DefaultSeverityNotFoundException;
import husacct.validate.domain.validation.DefaultSeverities;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.iternal_tranfer_objects.CategoryKeyClassDTO;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;

class RuleTypesGenerator {
	private Logger logger = Logger.getLogger(RuleTypesGenerator.class);

	private Map<String, DefaultSeverities> defaultRulesPerRuleType = Collections.emptyMap();
	private static final String ruleTypeAbstractClass = "husacct.validate.domain.validation.ruletype.RuleType";

	RuleTypesGenerator(){
		this.defaultRulesPerRuleType = getRuleTypeDefaultSeverityCombination();
	}

	HashMap<String, CategoryKeyClassDTO> generateRules(EnumSet<RuleTypes> rules) {
		HashMap<String, CategoryKeyClassDTO> keyClasses = new HashMap<String, CategoryKeyClassDTO>();
		HashMap<String, CategoryKeyClassDTO> allClasses = generateAllRules();
		for (Enum<RuleTypes> ruleKey : rules) {			
			CategoryKeyClassDTO ruleCategory = allClasses.get(ruleKey.toString());
			if (ruleCategory != null) {
				keyClasses.put(ruleKey.toString(), ruleCategory);
			} else {				
				logger.warn(String.format("Rulekey: %s not found", ruleKey.toString()));
			}
		}
		return keyClasses;
	}

	HashMap<String, CategoryKeyClassDTO> generateAllRules() {
		HashMap<String, CategoryKeyClassDTO> keyClasses = new HashMap<String, CategoryKeyClassDTO>();
		List<Class<?>> ruleClasses = getRuleClasses();
		for (Class<?> ruleClass : ruleClasses) {
			String ruleKey = "";
			try{
				if (isInstanceOfRule(ruleClass)) {
					ruleKey = getRuleKey(ruleClass);
					final String categoryKey = getCategoryKey(ruleClass);					
					final DefaultSeverities defaultSeverity = getDefaultSeverity(ruleKey);
					keyClasses.put(ruleKey, new CategoryKeyClassDTO(categoryKey, (Class<RuleType>) ruleClass, defaultSeverity));
				}
			}catch(DefaultSeverityNotFoundException e){
				logger.warn(String.format("No default severity found for: %s, thus this ruleType will be ignored", ruleKey), e);
			}
		}

		return keyClasses;
	}

	private DefaultSeverities getDefaultSeverity(String ruleKey){
		DefaultSeverities defaultSeverity = defaultRulesPerRuleType.get(ruleKey);
		if(defaultSeverity != null){
			return defaultSeverity;
		}
		else{
			throw new DefaultSeverityNotFoundException();
		}

	}

	private List<Class<?>> getRuleClasses() {
		return getRuleClasses(ruleTypeAbstractClass);
	}

	private List<Class<?>> getRuleClasses(String packageName) {
		try {
			List<String> directories = getDirectories(packageName);
			TreeSet<String> classes = new TreeSet<String>();
			for (String directory : directories) {
				classes.addAll(findClasses(directory, packageName.replace(".RuleType", "")));
			}
			List<Class<?>> classList = loadRuleClasses(classes);
			return classList;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	private List<String> getDirectories(String packageName) throws IOException {
		List<String> directories = new ArrayList<String>();

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;

		final String path = packageName.replace('.', '/');
		Enumeration<URL> resources;
		resources = classLoader.getResources(path + ".class");
		if (resources != null) {
			while (resources.hasMoreElements()) {
				URL resource = resources.nextElement();
				directories.add(resource.getFile().replace("/RuleType.class", ""));
				System.out.println(resource.getFile().replace("/RuleType.class", ""));
			}
		}
		return directories;
	}

	private TreeSet<String> findClasses(String directory, String packageName) throws IOException {
		TreeSet<String> classes = new TreeSet<String>();
		if (directory.startsWith("file:") && directory.contains("!")) {
			String[] split = directory.split("!");
			URL jar = new URL(split[0]);
			ZipInputStream zip = new ZipInputStream(jar.openStream());
			ZipEntry entry = null;
			while ((entry = zip.getNextEntry()) != null) {
				if (entry.getName().startsWith("husacct/validate/domain/validation/ruletype") && entry.getName().endsWith(".class")) {
					final String className = entry.getName().replaceAll("[$].*", "").replaceAll("[.]class", "").replace('/', '.');
					System.out.println(className);
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

	private List<Class<?>> loadRuleClasses(TreeSet<String> classes) throws ClassNotFoundException {
		List<Class<?>> classList = new ArrayList<Class<?>>();
		for (String clazz : classes) {
			Class<?> ruleClass = Class.forName(clazz);
			if (!Modifier.isAbstract(ruleClass.getModifiers()) && classHasRuleConstructor(ruleClass)) {
				classList.add(Class.forName(clazz));
			}
		}
		return classList;
	}

	private boolean classHasRuleConstructor(Class<?> ruleClass) {		
		try {
			ruleClass.getConstructor(String.class, String.class, List.class, Severity.class);
		} catch (SecurityException e) {
			return exceptionOccured(e);
		} catch (NoSuchMethodException e) {
			return exceptionOccured(e);
		}
		return true;
	}

	private boolean exceptionOccured(Exception e) {
		return false;
	}

	private boolean isInstanceOfRule(Class<?> ruleClass) {
		return ruleClass.getSimpleName().matches("^(.+Rule*)$") && !ruleClass.isAnonymousClass() && !ruleClass.isEnum() && ruleClass.getSuperclass().getName().equals(ruleTypeAbstractClass);
	}

	private String getCategoryKey(Class<?> ruleClass) {
		try {
			String[] splittedPackageName = ruleClass.getPackage().getName().split("\\.");
			return splittedPackageName[splittedPackageName.length - 1];
		} catch (ArrayIndexOutOfBoundsException e) {
			return "";
		}
	}

	private String getRuleKey(Class<?> ruleClass) {
		return ruleClass.getSimpleName().replace("Rule", "");
	}	

	private HashMap<String, DefaultSeverities> getRuleTypeDefaultSeverityCombination(){
		HashMap<String, DefaultSeverities> defaultRulesPerRuleType = new HashMap<String, DefaultSeverities>();
		for(RuleTypes ruletype : EnumSet.allOf(RuleTypes.class)){
			defaultRulesPerRuleType.put(ruletype.toString(), ruletype.getDefaultSeverity());
		}

		return defaultRulesPerRuleType;
	}	
}