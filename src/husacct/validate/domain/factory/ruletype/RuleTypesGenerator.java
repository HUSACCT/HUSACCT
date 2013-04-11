package husacct.validate.domain.factory.ruletype;

import husacct.validate.domain.exception.DefaultSeverityNotFoundException;
import husacct.validate.domain.validation.DefaultSeverities;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.internal_transfer_objects.CategoryKeyClassDTO;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

class RuleTypesGenerator {

	private Logger logger = Logger.getLogger(RuleTypesGenerator.class);
	private Map<String, DefaultSeverities> defaultRulesPerRuleType = Collections.emptyMap();
	private static final String[] ruleTypeLocations = new String[] {"husacct.validate.domain.validation.ruletype.contentsofamodule", "husacct.validate.domain.validation.ruletype.dependencylimitation", "husacct.validate.domain.validation.ruletype.legalityofdependency"};

	RuleTypesGenerator() {
		this.defaultRulesPerRuleType = getRuleTypeDefaultSeverity();
	}

	HashMap<String, CategoryKeyClassDTO> generateRules(EnumSet<RuleTypes> rules) {
		HashMap<String, CategoryKeyClassDTO> keyClasses = new HashMap<String, CategoryKeyClassDTO>();
		HashMap<String, CategoryKeyClassDTO> allClasses = generateAllRules();

		for (Enum<RuleTypes> ruleKey : rules) {
			CategoryKeyClassDTO ruleCategory = allClasses.get(ruleKey.toString());
			if (ruleCategory != null) {
				keyClasses.put(ruleKey.toString(), ruleCategory);
			}
			else {
				logger.warn(String.format("Rulekey: %s not found", ruleKey.toString()));
			}
		}
		return keyClasses;
	}

	HashMap<String, CategoryKeyClassDTO> generateAllRules() {
		HashMap<String, CategoryKeyClassDTO> keyClasses = new HashMap<String, CategoryKeyClassDTO>();
		List<Class<?>> ruleClasses = getRuleClasses(EnumSet.allOf(RuleTypes.class));
		for (Class<?> ruleClass : ruleClasses) {
			String ruleKey = "";
			try {
				if (isInstanceOfRule(ruleClass)) {
					ruleKey = getRuleKey(ruleClass);
					final String categoryKey = getCategoryKey(ruleClass);
					final DefaultSeverities defaultSeverity = getDefaultSeverity(ruleKey);
					keyClasses.put(ruleKey, new CategoryKeyClassDTO(categoryKey, (Class<RuleType>) ruleClass, defaultSeverity));
				}
			}
			catch (DefaultSeverityNotFoundException e) {
				logger.warn(String.format("No default severity found for: %s, thus this ruleType will be ignored", ruleKey), e);
			}
		}
		return keyClasses;
	}

	private DefaultSeverities getDefaultSeverity(String ruleKey) {
		DefaultSeverities defaultSeverity = defaultRulesPerRuleType.get(ruleKey);
		if (defaultSeverity != null) {
			return defaultSeverity;
		}
		else {
			throw new DefaultSeverityNotFoundException();
		}
	}

	private List<Class<?>> getRuleClasses(EnumSet<RuleTypes> ruleTypes) {
		return getRuleClasses(ruleTypeLocations, ruleTypes);
	}

	private List<Class<?>> getRuleClasses(String[] packageNames, EnumSet<RuleTypes> ruleTypes) {
		List<Class<?>> classList = new ArrayList<Class<?>>();

		ClassLoader myClassLoader = this.getClass().getClassLoader();
		for (String packageName : packageNames) {
			for (Enum<RuleTypes> ruleType : ruleTypes) {
				String classPath;
				try {
					classPath = packageName + "." + ruleType.toString() + "Rule";
					Class<?> myClass = myClassLoader.loadClass(classPath);

					if (!Modifier.isAbstract(myClass.getModifiers()) && classHasRuleConstructor(myClass)) {
						classList.add(myClass);
					}
				}
				catch (ClassNotFoundException e) {
					// logger.debug(String.format("Classpath: %s not found" ,
					// classPath));
				}
			}
		}
		return classList;
	}

	private boolean classHasRuleConstructor(Class<?> ruleClass) {
		try {
			ruleClass.getConstructor(String.class, String.class, List.class, Severity.class);
		}
		catch (SecurityException e) {
			return exceptionOccured(e);
		}
		catch (NoSuchMethodException e) {
			return exceptionOccured(e);
		}
		return true;
	}

	private boolean exceptionOccured(Exception e) {
		return false;
	}

	private boolean isInstanceOfRule(Class<?> ruleClass) {
		return ruleClass.getSimpleName().matches("^(.+Rule*)$") && !ruleClass.isAnonymousClass() && !ruleClass.isEnum() && ruleClass.getSuperclass().getName().equals("husacct.validate.domain.validation.ruletype.RuleType");
	}

	private String getCategoryKey(Class<?> ruleClass) {
		try {
			String[] splittedPackageName = ruleClass.getPackage().getName().split("\\.");
			return splittedPackageName[splittedPackageName.length - 1];
		}
		catch (ArrayIndexOutOfBoundsException e) {
			return "";
		}
	}

	private String getRuleKey(Class<?> ruleClass) {
		return ruleClass.getSimpleName().replace("Rule", "");
	}

	private HashMap<String, DefaultSeverities> getRuleTypeDefaultSeverity() {
		HashMap<String, DefaultSeverities> defaultRulesPerRuleTypeLocal = new HashMap<String, DefaultSeverities>();
		for (RuleTypes ruletype : EnumSet.allOf(RuleTypes.class)) {
			defaultRulesPerRuleTypeLocal.put(ruletype.toString(), ruletype.getDefaultSeverity());
		}

		return defaultRulesPerRuleTypeLocal;
	}
}