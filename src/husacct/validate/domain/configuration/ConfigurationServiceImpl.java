package husacct.validate.domain.configuration;

import husacct.validate.domain.factory.ruletype.RuleTypesFactory;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationHistory;
import husacct.validate.presentation.ViolationHistoryRepositoryObserver;

import java.util.AbstractMap.SimpleEntry;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class ConfigurationServiceImpl extends Observable {

	private final SeverityConfigRepository severityConfig;
	private final SeverityPerTypeRepository severityPerTypeRepository;
	private final ViolationRepository violationRepository;
	private final RuleTypesFactory ruletypefactory;
	private final ViolationHistoryRepository violationHistoryRepository;
	private final ActiveViolationTypesRepository activeViolationTypesRepository;

	public ConfigurationServiceImpl() {
		this.severityConfig = new SeverityConfigRepository();
		this.violationRepository = new ViolationRepository();		
		this.severityPerTypeRepository = new SeverityPerTypeRepository(this.ruletypefactory = new RuleTypesFactory(this), this);
		this.activeViolationTypesRepository = new ActiveViolationTypesRepository(this.ruletypefactory);
		this.severityPerTypeRepository.initializeDefaultSeverities();			
		this.violationHistoryRepository = new ViolationHistoryRepository();		
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
		return severityPerTypeRepository.getSeveritiesPerTypePerProgrammingLanguage();
	}

	public void setSeveritiesPerTypesPerProgrammingLanguages(HashMap<String, HashMap<String, Severity>> severitiesPerTypesPerProgrammingLanguages) {
		severityPerTypeRepository.setSeverityMap(severitiesPerTypesPerProgrammingLanguages);
	}

	public void setSeveritiesPerTypesPerProgrammingLanguages(String language, HashMap<String, Severity> severitiesPerTypesPerProgrammingLanguages) {
		severityPerTypeRepository.setSeverityMap(language, severitiesPerTypesPerProgrammingLanguages);
	}

	public Severity getSeverityFromKey(String language, String key){
		return severityPerTypeRepository.getSeverity(language, key);
	}

	public void restoreAllToDefault(String language){
		severityPerTypeRepository.restoreAllToDefault(language);
	}

	public void restoreToDefault(String language, String key){
		severityPerTypeRepository.restoreDefaultSeverity(language, key);
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
		final SimpleEntry<Calendar, List<Violation>> violationsResult = getAllViolations();
		final Calendar date = violationsResult.getKey();
		final List<Violation> violations = violationsResult.getValue();

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

	public void attachViolationHistoryRepositoryObserver(ViolationHistoryRepositoryObserver observer) {
		violationHistoryRepository.attachObserver(observer);		
	}

	public boolean isViolationEnabled(String programmingLanguage, String ruleTypeKey, String violationTypeKey){
		if(activeViolationTypesRepository != null){
			getActiveViolationTypes();
			return activeViolationTypesRepository.isEnabled(programmingLanguage, ruleTypeKey, violationTypeKey);
		}
		else{
			return true;
		}
	}
}