package husacct.validate.domain.configuration;

import husacct.ServiceProvider;
import husacct.validate.domain.exception.SeverityChangedException;
import husacct.validate.domain.factory.ruletype.RuleTypesFactory;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationHistory;

import java.util.AbstractMap.SimpleEntry;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

public final class ConfigurationServiceImpl extends Observable {

	private final SeverityConfigRepository severityConfig;
	private final SeverityPerTypeRepository severityPerTypeRepository;
	private final ViolationRepository violationRepository;
	private final RuleTypesFactory ruletypeFactory;
	private final ViolationHistoryRepository violationHistoryRepository;
	private final ActiveViolationTypesRepository activeViolationTypesRepository;
	
	public ConfigurationServiceImpl() {
		this.severityConfig = new SeverityConfigRepository();
		this.violationRepository = new ViolationRepository();
		this.severityPerTypeRepository = new SeverityPerTypeRepository(this.ruletypeFactory = new RuleTypesFactory(this), this);
		this.activeViolationTypesRepository = new ActiveViolationTypesRepository(this.ruletypeFactory);
		this.severityPerTypeRepository.initializeDefaultSeverities();
		this.violationHistoryRepository = new ViolationHistoryRepository();
	}

	public void clearViolations() {
		violationRepository.clear();
	}

	public int getSeverityValue(Severity severity) {
		return severityConfig.getSeverityValue(severity);
	}

	public List<Severity> getAllSeverities() {
		return severityConfig.getAllSeverities();
	}

	/**
	 * @throws SeverityChangedException
	 */
	public void setSeverities(List<Severity> severities) {
		severityConfig.setSeverities(severities);
		notifyServiceListeners();
	}

	public Severity getSeverityByName(String severityName) {
		return severityConfig.getSeverityByName(severityName);
	}

	public SimpleEntry<Calendar, List<Violation>> getAllViolations() {
		return violationRepository.getAllViolations();
	}

	// returns a List of Violations; it is empty if no Violation is registered for the specific combination of from-to
	public List<Violation> getViolationsFromTo(String physicalPathFrom, String physicalPathTo) {
		return violationRepository.getViolationsFromTo(physicalPathFrom, physicalPathTo);
	}
	
	public Set<String> getViolatedRules() {
		return violationRepository.getViolatedRules();
	}
	
	public List<Violation> getViolationsByRule(String moduleFrom, String moduleTo, String ruleTypeKey) {
		return violationRepository.getViolationsByRule(moduleFrom, moduleTo, ruleTypeKey);
	}

	public void addViolations(List<Violation> violations) {
		violationRepository.addViolation(violations);
		
		setChanged();
		notifyObservers();
		notifyServiceListeners();
	}
	
	public void filterAndSortAllViolations(){
		violationRepository.filterAndSortAllViolations();
	}

	public HashMap<String, HashMap<String, Severity>> getAllSeveritiesPerTypesPerProgrammingLanguages() {
		return severityPerTypeRepository.getSeveritiesPerTypePerProgrammingLanguage();
	}

	public void setSeveritiesPerTypesPerProgrammingLanguages(HashMap<String, HashMap<String, Severity>> severitiesPerTypesPerProgrammingLanguages) {
		severityPerTypeRepository.setSeverityMap(severitiesPerTypesPerProgrammingLanguages);
		notifyServiceListeners();
	}

	public void setSeveritiesPerTypesPerProgrammingLanguages(String language, HashMap<String, Severity> severitiesPerTypesPerProgrammingLanguages) {
		severityPerTypeRepository.setSeverityMap(language, severitiesPerTypesPerProgrammingLanguages);
		notifyServiceListeners();
	}

	public Severity getSeverityFromKey(String language, String key) {
		return severityPerTypeRepository.getSeverity(language, key);
	}

	public void restoreAllKeysToDefaultSeverities(String language) {
		severityPerTypeRepository.restoreAllKeysToDefaultSeverities(language);
		setChanged();
		notifyObservers();
		notifyServiceListeners();
	}

	public void restoreKeyToDefaultSeverity(String language, String key) {
		severityPerTypeRepository.restoreKeyToDefaultSeverity(language, key);
		setChanged();
		notifyObservers();
		notifyServiceListeners();
	}

	public void restoreSeveritiesToDefault() {
		severityConfig.restoreToDefault();
		notifyServiceListeners();
	}

	public RuleTypesFactory getRuleTypesFactory() {
		return ruletypeFactory;
	}

	public List<ViolationHistory> getViolationHistory() {
		return violationHistoryRepository.getViolationHistory();
	}

	public void setViolationHistory(List<ViolationHistory> list) {
		violationHistoryRepository.setViolationHistories(list);
	}

	public Map<String, List<ActiveRuleType>> getActiveViolationTypes() {
		return activeViolationTypesRepository.getActiveViolationTypes();
	}

	public void setActiveViolationTypes(String programmingLanguage, List<ActiveRuleType> activeViolationTypes) {
		activeViolationTypesRepository.setActiveViolationTypes(programmingLanguage, activeViolationTypes);
	}

	public void setActiveViolationTypes(Map<String, List<ActiveRuleType>> activeViolationTypes) {
		activeViolationTypesRepository.setActiveViolationTypes(activeViolationTypes);
	}

	public void createHistoryPoint(String description) {
		SimpleEntry<Calendar, List<Violation>> violationsResult = getAllViolations();
		Calendar date = violationsResult.getKey();
		List<Violation> violations = violationsResult.getValue();

		ViolationHistory violationHistory = new ViolationHistory(violations, getAllSeverities(), date, description);
		violationHistoryRepository.addViolationHistory(violationHistory);
	}

	public void removeViolationHistory(Calendar date) {
		violationHistoryRepository.removeViolationHistory(date);
	}

	public ViolationHistory getViolationHistoryByDate(Calendar date) {
		return violationHistoryRepository.getViolationHistoryByDate(date);
	}

	public List<ViolationHistory> getViolationHistories() {
		return violationHistoryRepository.getViolationHistory();
	}

	public boolean isViolationEnabled(String programmingLanguage, String ruleTypeKey, String violationTypeKey) {
		if (activeViolationTypesRepository != null) {
			getActiveViolationTypes();
			return activeViolationTypesRepository.isEnabled(programmingLanguage, ruleTypeKey, violationTypeKey);
		} else {
			return true;
		}
	}

	public void attachViolationHistoryRepositoryObserver(Observer observer) {
		violationHistoryRepository.addObserver(observer);
	}

	private void notifyServiceListeners() {
		ServiceProvider.getInstance().getValidateService().notifyServiceListeners();
	}
}