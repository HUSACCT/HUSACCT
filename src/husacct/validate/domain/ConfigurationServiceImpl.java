package husacct.validate.domain;

import husacct.validate.domain.configuration.ActiveRuleType;
import husacct.validate.domain.configuration.ActiveViolationTypesRepository;
import husacct.validate.domain.configuration.SeverityConfigRepository;
import husacct.validate.domain.configuration.SeverityPerTypeRepository;
import husacct.validate.domain.configuration.ViolationHistoryRepository;
import husacct.validate.domain.configuration.ViolationRepository;
import husacct.validate.domain.factory.ruletype.RuleTypesFactory;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationHistory;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

public class ConfigurationServiceImpl {

	private final SeverityConfigRepository severityConfig;
	private final SeverityPerTypeRepository severityRepository;
	private final ViolationRepository violationRepository;
	private final RuleTypesFactory ruletypefactory;
	private final ViolationHistoryRepository violationHistoryRepository;
	private final ActiveViolationTypesRepository activeViolationTypesRepository;

	public ConfigurationServiceImpl() {
		this.severityConfig = new SeverityConfigRepository();
		this.violationRepository = new ViolationRepository();
		this.severityRepository = new SeverityPerTypeRepository(this.ruletypefactory = new RuleTypesFactory(this), this);
		this.severityRepository.initializeDefaultSeverities();	

		this.violationHistoryRepository = new ViolationHistoryRepository();
		this.activeViolationTypesRepository = new ActiveViolationTypesRepository();
	}

	public void clearViolations() {
		violationRepository.clear();
	}

	public int getSeverityValue(Severity severity){
		return severityConfig.getSeverityValue(severity);
	}

	public SimpleEntry<Calendar, List<Violation>> getAllViolations() {
		return violationRepository.getAllViolations();
	}

	public void addViolations(List<Violation> violations) {
		violationRepository.addViolation(violations);
	}

	public List<Severity> getAllSeverities() {
		return severityConfig.getAllSeverities();
	}

	public void addSeverities(List<Severity> severities) {
		severityConfig.addSeverities(severities);
	}

	public Severity getSeverityByName(String severityName){
		return severityConfig.getSeverityByName(severityName);
	}

	public HashMap<String, HashMap<String, Severity>> getAllSeveritiesPerTypesPerProgrammingLanguages() {
		return severityRepository.getSeveritiesPerTypePerProgrammingLanguage();
	}

	public void setSeveritiesPerTypesPerProgrammingLanguages(HashMap<String, HashMap<String, Severity>> severitiesPerTypesPerProgrammingLanguages) {
		severityRepository.setSeverityMap(severitiesPerTypesPerProgrammingLanguages);
	}

	public void setSeveritiesPerTypesPerProgrammingLanguages(String language, HashMap<String, Severity> severitiesPerTypesPerProgrammingLanguages) {
		severityRepository.setSeverityMap(language, severitiesPerTypesPerProgrammingLanguages);
	}

	public Severity getSeverityFromKey(String language, String key){
		return severityRepository.getSeverity(language, key);
	}

	public void restoreAllToDefault(String language){
		severityRepository.restoreAllToDefault(language);
	}

	public void restoreToDefault(String language, String key){
		severityRepository.restoreDefaultSeverity(language, key);
	}

	public void restoreSeveritiesToDefault(){
		severityConfig.restoreToDefault();
	}

	public RuleTypesFactory getRuleTypesFactory(){
		return ruletypefactory;
	}

	public List<ViolationHistory> getViolationHistory() {
		return violationHistoryRepository.getViolationHistory();
	}

	public void setViolationHistory(List<ViolationHistory> list){
		violationHistoryRepository.setViolationHistory(list);
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
		final Calendar date = violationsResult.getKey();
		final List<Violation> violations = violationsResult.getValue();
		//FIXME: clone Severities
		ViolationHistory violationHistory = new ViolationHistory(violations, getAllSeverities(), date, description);	
		violationHistoryRepository.addViolationHistory(violationHistory);
	}
}