package husacct.validate.domain.configuration;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.validate.domain.exception.KeyNotFoundException;
import husacct.validate.domain.exception.ProgrammingLanguageNotFoundException;
import husacct.validate.domain.exception.SeverityNotFoundException;
import husacct.validate.domain.factory.ruletype.RuleTypesFactory;
import husacct.validate.domain.factory.violationtype.AbstractViolationType;
import husacct.validate.domain.factory.violationtype.ViolationTypeFactory;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.ruletype.RuleType;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

public class SeverityPerTypeRepository {

	private Logger logger = Logger.getLogger(SeverityPerTypeRepository.class);
	private final IAnalyseService analyseService = ServiceProvider.getInstance().getAnalyseService();
	private final RuleTypesFactory ruletypeFactory;
	private final ConfigurationServiceImpl configuration;
	private HashMap<String, HashMap<String, Severity>> severitiesPerTypePerProgrammingLanguage;
	private HashMap<String, HashMap<String, Severity>> defaultSeveritiesPerTypePerProgrammingLanguage;
	private AbstractViolationType violationtypefactory;

	public SeverityPerTypeRepository(RuleTypesFactory ruletypefactory, ConfigurationServiceImpl configuration) {
		this.configuration = configuration;
		this.ruletypeFactory = ruletypefactory;

		severitiesPerTypePerProgrammingLanguage = new HashMap<String, HashMap<String, Severity>>();
		defaultSeveritiesPerTypePerProgrammingLanguage = new HashMap<String, HashMap<String, Severity>>();
	}

	void initializeDefaultSeverities() {
		for (String programmingLanguage : analyseService.getAvailableLanguages()) {
			severitiesPerTypePerProgrammingLanguage.putAll(initializeDefaultSeverityForLanguage(programmingLanguage));
			defaultSeveritiesPerTypePerProgrammingLanguage.putAll(initializeDefaultSeverityForLanguage(programmingLanguage));
		}
	}

	private HashMap<String, HashMap<String, Severity>> initializeDefaultSeverityForLanguage(String programmingLanguage) {
		HashMap<String, HashMap<String, Severity>> severitiesPerTypePerProgrammingLanguage = new HashMap<String, HashMap<String, Severity>>();

		severitiesPerTypePerProgrammingLanguage.put(programmingLanguage, new HashMap<String, Severity>());

		HashMap<String, Severity> severityPerType = severitiesPerTypePerProgrammingLanguage.get(programmingLanguage);
		for (RuleType ruleType : ruletypeFactory.getRuleTypes()) {
			severityPerType.put(ruleType.getKey(), ruleType.getSeverity());

			for (RuleType exceptionRuleType : ruleType.getExceptionRules()) {
				if (severityPerType.get(exceptionRuleType.getKey()) == null) {
					severityPerType.put(exceptionRuleType.getKey(), exceptionRuleType.getSeverity());
				}
			}
		}

		this.violationtypefactory = new ViolationTypeFactory().getViolationTypeFactory(programmingLanguage, configuration);
		if (violationtypefactory != null) {
			for (Entry<String, List<ViolationType>> violationTypeCategory : violationtypefactory.getAllViolationTypes().entrySet()) {
				for (ViolationType violationType : violationTypeCategory.getValue()) {
					severityPerType.put(violationType.getViolationtypeKey(), violationType.getSeverity());
				}
			}
		} else {
			logger.debug("Warning no language specified in define component");
		}
		return severitiesPerTypePerProgrammingLanguage;
	}

	HashMap<String, HashMap<String, Severity>> getSeveritiesPerTypePerProgrammingLanguage() {
		return severitiesPerTypePerProgrammingLanguage;
	}

	Severity getSeverity(String language, String key) {
		HashMap<String, Severity> severityPerType = severitiesPerTypePerProgrammingLanguage.get(language);
		if (severityPerType == null) {
			throw new SeverityNotFoundException();
		} else {
			Severity severity = severityPerType.get(key);
			if (severity == null) {
				throw new SeverityNotFoundException();
			} else {
				return severity;
			}
		}
	}

	void restoreKeyToDefaultSeverity(String language, String key) {
		HashMap<String, Severity> severitiesPerType = severitiesPerTypePerProgrammingLanguage.get(language);

		// if there is no value, automatically the default severities will be
		// applied
		if (severitiesPerType != null) {
			Severity oldSeverity = severitiesPerType.get(key);
			if (oldSeverity != null) {
				Severity defaultSeverity = getDefaultRuleKey(language, key);
				if (defaultSeverity != null) {
					severitiesPerType.remove(key);
					severitiesPerType.put(key, defaultSeverity);
				}
			}
		}
	}

	private Severity getDefaultRuleKey(String language, String key) {
		HashMap<String, Severity> severityPerType = defaultSeveritiesPerTypePerProgrammingLanguage.get(language);
		if (severityPerType == null) {
			throw new SeverityNotFoundException();
		} else {
			Severity severity = severityPerType.get(key);
			if (severity == null) {
				throw new SeverityNotFoundException();
			} else {
				return severity;
			}
		}
	}

	void restoreAllKeysToDefaultSeverities(String programmingLanguage) {
		initializeDefaultSeverityForLanguage(programmingLanguage);
	}

	void setSeverityMap(HashMap<String, HashMap<String, Severity>> severitiesPerTypePerProgrammingLanguage) {
		for (String programmingLanguage : severitiesPerTypePerProgrammingLanguage.keySet()) {
			if (!programmingLanguageExists(programmingLanguage)) {
				throw new ProgrammingLanguageNotFoundException(programmingLanguage);
			} else {
				for (Entry<String, Severity> keySeverity : severitiesPerTypePerProgrammingLanguage.get(programmingLanguage).entrySet()) {
					if (isValidKey(programmingLanguage, keySeverity.getKey())) {
						keySeverity.setValue(isValidSeverity(keySeverity.getValue()));
					} else {
						throw new KeyNotFoundException(keySeverity.getKey());
					}
				}
			}
		}
		this.severitiesPerTypePerProgrammingLanguage = severitiesPerTypePerProgrammingLanguage;
	}

	void setSeverityMap(String programmingLanguage, HashMap<String, Severity> severityMap) {
		HashMap<String, Severity> local = severitiesPerTypePerProgrammingLanguage.get(programmingLanguage);
		if (local != null && programmingLanguageExists(programmingLanguage)) {
			for (Entry<String, Severity> entry : severityMap.entrySet()) {
				try {
					Severity severity = isValidSeverity(entry.getValue());
					if (isValidKey(programmingLanguage, entry.getKey())) {
						local.remove(entry.getKey());
						local.put(entry.getKey(), severity);
					}
				} catch (SeverityNotFoundException e) {
					logger.warn(String.format("%s is not a know severity, %s will not be set in SeverityPerTypeRepository", entry.getValue().getSeverityKey(), entry.getKey()));
				} catch (NullPointerException e) {
					logger.error("Cannot severity cannot be null in SeverityPerTypeRepository");
				}
			}
		} else {
			throw new ProgrammingLanguageNotFoundException(programmingLanguage);
		}
	}

	private boolean isValidKey(String programmingLanguage, String key) {
		if (programmingLanguageExists(programmingLanguage)) {
			for (String defaultKey : defaultSeveritiesPerTypePerProgrammingLanguage.get(programmingLanguage).keySet()) {
				if (defaultKey.toLowerCase().equals(key.toLowerCase())) {
					return true;
				}
			}
		} else {
			throw new ProgrammingLanguageNotFoundException(programmingLanguage);
		}
		return false;
	}

	private boolean programmingLanguageExists(String programmingLanguage) {
		HashMap<String, Severity> local = defaultSeveritiesPerTypePerProgrammingLanguage.get(programmingLanguage);
		if (local != null) {
			return true;
		} else {
			return false;
		}
	}

	private Severity isValidSeverity(Severity severity) {
		for (Severity currentSeverity : configuration.getAllSeverities()) {
			if (severity == currentSeverity) {
				return severity;
			}
		}
		Severity newSeverity = configuration.getSeverityByName(severity.getSeverityKey());
		if (newSeverity != null) {
			return newSeverity;
		}
		throw new SeverityNotFoundException();
	}
}