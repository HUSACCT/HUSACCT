package husacct.validate.domain.configuration;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.validate.domain.exception.ProgrammingLanguageNotFoundException;
import husacct.validate.domain.exception.RuleTypeNotFoundException;
import husacct.validate.domain.exception.ViolationTypeNotFoundException;
import husacct.validate.domain.factory.ruletype.RuleTypesFactory;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.ruletype.RuleType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

class ActiveViolationTypesRepository {

	private final IAnalyseService analyseService = ServiceProvider.getInstance().getAnalyseService();
//	private final IDefineService defineService = ServiceProvider.getInstance().getDefineService();
	private final RuleTypesFactory ruletypesfactory;
	private final Map<String, List<ActiveRuleType>> startupViolationTypes;
	private Map<String, List<ActiveRuleType>> currentActiveViolationTypes;
	private Logger logger = Logger.getLogger(ActiveViolationTypesRepository.class);

	public ActiveViolationTypesRepository(RuleTypesFactory ruletypesfactory) {
		this.ruletypesfactory = ruletypesfactory;
		this.startupViolationTypes = initializeAllActiveViolationTypes();
		this.currentActiveViolationTypes = initializeAllActiveViolationTypes();
	}

	private Map<String, List<ActiveRuleType>> initializeAllActiveViolationTypes() {
		Map<String, List<ActiveRuleType>> activeViolationTypes = new HashMap<String, List<ActiveRuleType>>();

		for (String programmingLanguage : analyseService.getAvailableLanguages()) {
			List<ActiveRuleType> activeRuleTypes = new ArrayList<ActiveRuleType>();
			activeViolationTypes.put(programmingLanguage, activeRuleTypes);

			for (List<RuleType> ruleTypes : ruletypesfactory.getRuleTypes(programmingLanguage).values()) {
				for (RuleType ruleType : ruleTypes) {
					ActiveRuleType activeRuleType = initializeActiveViolationTypes(ruleType);
					activeRuleTypes.add(activeRuleType);

					for (RuleType exceptionRuleType : ruleType.getExceptionRules()) {
						try {
							containsRuleType(activeRuleTypes, exceptionRuleType.getKey());
							activeRuleTypes.add(initializeActiveViolationTypes(exceptionRuleType));
						} catch (RuntimeException e) {
						}
					}
				}
			}
		}
		return activeViolationTypes;
	}

	private ActiveRuleType containsRuleType(List<ActiveRuleType> activeRuleTypes, String ruleTypeKey) {
		for (ActiveRuleType activeRuleType : activeRuleTypes) {
			if (activeRuleType.getRuleType().equals(ruleTypeKey)) {
				return activeRuleType;
			}
		}
		throw new RuntimeException();
	}

	private ActiveRuleType initializeActiveViolationTypes(RuleType ruleType) {
		final String ruleTypeKey = ruleType.getKey();
		List<ActiveViolationType> initialActiveViolationTypes = new ArrayList<ActiveViolationType>();

		for (ViolationType violationType : ruleType.getViolationTypes()) {
			final String violationTypeKey = violationType.getViolationTypeKey();
			boolean enabled = violationType.isActive();
			ActiveViolationType activeViolationType = new ActiveViolationType(violationTypeKey, enabled);
			initialActiveViolationTypes.add(activeViolationType);
		}
		ActiveRuleType activeRuleType = new ActiveRuleType(ruleTypeKey);
		activeRuleType.setViolationTypes(initialActiveViolationTypes);
		return activeRuleType;
	}

	public boolean isEnabled(String programmingLanguage, String ruleTypeKey, String violationTypeKey) {
		List<ActiveRuleType> activeRuleTypes = this.currentActiveViolationTypes.get(programmingLanguage);
		if (activeRuleTypes != null) {
			for (ActiveRuleType activeRuleType : activeRuleTypes) {
				if (activeRuleType.getRuleType().equalsIgnoreCase(ruleTypeKey)) {
					List<ActiveViolationType> activeViolationTypes = activeRuleType.getViolationTypes();
					if (activeViolationTypes.isEmpty()) {
						return false;
					}

					for (ActiveViolationType activeViolationType : activeViolationTypes) {
						if (activeViolationType.getType().equalsIgnoreCase(violationTypeKey)) {
							return activeViolationType.isEnabled();
						}
					}
				}
			}
		} else {
			throw new ProgrammingLanguageNotFoundException();
		}
		return false;
	}

	public Map<String, List<ActiveRuleType>> getActiveViolationTypes() {
		return currentActiveViolationTypes;
	}

	public void setActiveViolationTypes(Map<String, List<ActiveRuleType>> activeViolationTypes) {
		for (Entry<String, List<ActiveRuleType>> activeViolationTypeSet : activeViolationTypes.entrySet()) {
			if (programmingLanguageExists(activeViolationTypeSet.getKey())) {
				setActiveViolationTypes(activeViolationTypeSet.getKey(), activeViolationTypes.get(activeViolationTypeSet.getKey()));
			}
		}
	}

	public void setActiveViolationTypes(String programmingLanguage, List<ActiveRuleType> newActiveViolationTypes) {
		if (programmingLanguageExists(programmingLanguage)) {
			List<ActiveRuleType> checkedNewActiveViolationTypes = checkNewActiveViolationTypes(programmingLanguage, newActiveViolationTypes);

			if (currentActiveViolationTypes.containsKey(programmingLanguage)) {
				currentActiveViolationTypes.remove(programmingLanguage);
				currentActiveViolationTypes.put(programmingLanguage, checkedNewActiveViolationTypes);
			} else {
				currentActiveViolationTypes.put(programmingLanguage, checkedNewActiveViolationTypes);
			}
		} else {
			throw new ProgrammingLanguageNotFoundException(programmingLanguage);
		}
	}

	private boolean programmingLanguageExists(String programmingLanguage) {
		for (String language : startupViolationTypes.keySet()) {
			if (language.toLowerCase().equals(programmingLanguage.toLowerCase())) {
				return true;
			}
		}
		throw new ProgrammingLanguageNotFoundException(programmingLanguage);
	}

	private List<ActiveRuleType> checkNewActiveViolationTypes(String programmingLanguage, List<ActiveRuleType> newActiveViolationTypes) {
		List<ActiveRuleType> activeViolationTypesForLanguage = new ArrayList<ActiveRuleType>();

		for (ActiveRuleType newActiveRuleType : newActiveViolationTypes) {
			if (ruleTypeKeyExists(programmingLanguage, newActiveRuleType.getRuleType())) {

				List<ActiveViolationType> activeViolationTypes = new ArrayList<ActiveViolationType>();
				ActiveRuleType activeRuleType = new ActiveRuleType(newActiveRuleType.getRuleType());
				activeRuleType.setViolationTypes(activeViolationTypes);
				boolean foundViolationTypeKey = false;

				for (ActiveViolationType newActiveViolationType : newActiveRuleType.getViolationTypes()) {
					if (violationTypeKeyExists(programmingLanguage, newActiveRuleType.getRuleType(), newActiveViolationType.getType())) {
						foundViolationTypeKey = true;
						activeViolationTypes.add(new ActiveViolationType(newActiveViolationType.getType(), newActiveViolationType.isEnabled()));
					} else {
						logger.debug(String.format("violationTypeKey %s not exists", newActiveViolationType.getType()));
					}
				}
				if (foundViolationTypeKey) {
					activeViolationTypesForLanguage.add(activeRuleType);
				}
			} else {
				logger.debug(String.format("ruleTypeKey %s not exists in programminglanguage %s", newActiveRuleType.getRuleType(), programmingLanguage));
			}
		}
		return mergeNewViolationTypes(programmingLanguage, activeViolationTypesForLanguage);
	}

	private List<ActiveRuleType> mergeNewViolationTypes(String programmingLanguage, List<ActiveRuleType> newActiveViolationTypes) {
		List<ActiveRuleType> activeViolationTypesForLanguage = new ArrayList<ActiveRuleType>();

		for (ActiveRuleType currentActiveRuleType : startupViolationTypes.get(programmingLanguage)) {
			try {
				ActiveRuleType existingActiveRuleType = containsRuleType(newActiveViolationTypes, currentActiveRuleType.getRuleType());
				List<ActiveViolationType> activeViolationTypes = new ArrayList<ActiveViolationType>();

				for (ActiveViolationType currentActiveViolationType : containsRuleType(startupViolationTypes.get(programmingLanguage), existingActiveRuleType.getRuleType()).getViolationTypes()) {
					boolean found = false;
					for (ActiveViolationType newViolationType : existingActiveRuleType.getViolationTypes()) {
						if (newViolationType.getType().equals(currentActiveViolationType.getType())) {
							activeViolationTypes.add(newViolationType);
							found = true;
						}
					}
					if (!found) {
						activeViolationTypes.add(new ActiveViolationType(currentActiveViolationType.getType(), currentActiveViolationType.isEnabled()));
					}
				}
				activeViolationTypesForLanguage.add(new ActiveRuleType(existingActiveRuleType.getRuleType(), activeViolationTypes));

			} catch (RuntimeException e) {
				List<ActiveViolationType> activeViolationTypes = new ArrayList<ActiveViolationType>();
				for (ActiveViolationType activeViolationType : currentActiveRuleType.getViolationTypes()) {
					activeViolationTypes.add(new ActiveViolationType(activeViolationType.getType(), activeViolationType.isEnabled()));
				}
				ActiveRuleType activeRuleType = new ActiveRuleType(currentActiveRuleType.getRuleType(), activeViolationTypes);
				activeViolationTypesForLanguage.add(activeRuleType);
			}
		}
		return activeViolationTypesForLanguage;
	}

	private boolean ruleTypeKeyExists(String programmingLanguage, String ruleTypeKey) {
		if (programmingLanguageExists(programmingLanguage)) {
			for (ActiveRuleType activeRuleType : startupViolationTypes.get(programmingLanguage)) {
				if (activeRuleType.getRuleType().toLowerCase().equals(ruleTypeKey.toLowerCase())) {
					return true;
				}
			}
		} else {
			throw new ProgrammingLanguageNotFoundException(programmingLanguage);
		}
		throw new RuleTypeNotFoundException(ruleTypeKey);
	}

	private boolean violationTypeKeyExists(String programmingLanguage, String ruleTypeKey, String violationTypeKey) {
		if (programmingLanguageExists(programmingLanguage) && ruleTypeKeyExists(programmingLanguage, ruleTypeKey)) {
			for (ActiveRuleType activeRuleType : startupViolationTypes.get(programmingLanguage)) {
				for (ActiveViolationType activeViolationType : activeRuleType.getViolationTypes()) {
					if (activeViolationType.getType().toLowerCase().equals(violationTypeKey.toLowerCase())) {
						return true;
					}
				}
			}
		}
		throw new ViolationTypeNotFoundException(violationTypeKey);
	}
}